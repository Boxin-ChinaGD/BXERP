DROP PROCEDURE IF EXISTS `SP_RetailTradeCommodity_UploadTrade_CreateSaleCommodity`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_RetailTradeCommodity_UploadTrade_CreateSaleCommodity`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iTradeID INT,
	IN iCommodityID INT,	
	IN iBarcodeID INT,
	IN iNO INT,
	IN fPriceOriginal Decimal(20,6),
	IN iNOCanReturn INT,
	IN iStaffID INT, 
	IN fPriceReturn Decimal(20,6),
	IN fPriceSpecialOffer Decimal(20,6),
	IN fPriceVIPOriginal Decimal(20,6)
	)
BEGIN
	DECLARE currentWarehousingID INT;
	DECLARE iShopID INT;
	DECLARE commID INT;
	DECLARE cm INT; -- 多包装商品的商品倍数
	DECLARE oldNO INT; -- 商品初始的库存
	DECLARE iSubCommodityID INT; -- 组合商品子商品ID
	DECLARE iSubCommodityNO INT; -- 组合商品子商品数量
	DECLARE iType INT; -- 商品的类型
	DECLARE iFuncReturnCode INT;
	DECLARE done INT DEFAULT FALSE;					-- 创建结束标志变量
	DECLARE list CURSOR FOR (
		SELECT  
		    F_SubCommodityID AS iSubCommodityID, 
			F_SubCommodityNO AS iSubCommodityNO
		FROM t_subcommodity 
		WHERE F_CommodityID = iCommodityID);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  -- 指定游标循环结束时返回值
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SET sErrorMsg := '';
		SET iErrorCode := 0;
		SELECT F_ShopID INTO iShopID FROM t_retailtrade WHERE F_ID = iTradeID;
		
		IF NOT EXISTS(SELECT 1 FROM t_commodity WHERE F_ID = iCommodityID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '该商品不存在';
		ELSE 
			INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
			VALUES (iTradeID, iCommodityID,(SELECT F_Name FROM t_commodity WHERE F_ID = iCommodityID), iBarcodeID, iNO, fPriceOriginal, iNO, fPriceReturn, fPriceSpecialOffer, fPriceVIPOriginal);
			SET @retailTradeCommodityID := Last_insert_id();
			
			SELECT F_ID, F_TradeID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal
			FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
			
			SELECT F_Type INTO iType FROM t_commodity WHERE F_ID = iCommodityID;

			-- 多包装商品 
	  		IF iType = 2 THEN 
		  		-- 获取多包装商品的单品ID和倍数
		  		SELECT F_refCommodityMultiple, F_RefCommodityID INTO cm, commID FROM t_commodity WHERE F_ID = iCommodityID;
		 		-- 获取单品的初始库存和当值入库ID
--		 		SELECT F_NO, F_CurrentWarehousingID INTO oldNO, currentWarehousingID FROM t_commodity WHERE F_ID = commID; 
				SELECT F_NO, F_CurrentWarehousingID INTO oldNO, currentWarehousingID FROM t_commodityshopinfo WHERE F_CommodityID = commID AND F_ShopID = iShopID;
				    	   
		  		UPDATE t_commodityshopinfo AS t SET t.F_NO = t.F_NO - iNO * cm WHERE t.F_CommodityID = commID AND t.F_ShopID = iShopID;
			
		  		SELECT Func_CreateRetailTradeCommoditySource(commID, iNO * cm, currentWarehousingID, @retailTradeCommodityID) INTO iFuncReturnCode;	
		  		-- 插入商品修改历史表
		  		SELECT Func_CreateCommodityHistory(commID, '$', '$', '$', -1, -1, -100000000, oldNO, iStaffID, '$', iShopID) INTO iFuncReturnCode;
			
	   		-- 组合商品
	  		ELSEIF iType = 1 THEN
	   			OPEN list;
					read_loop: LOOP
					FETCH list INTO iSubCommodityID,iSubCommodityNO;
						IF done THEN
				  		LEAVE read_loop;
		   			END IF;
					
				   	SELECT iSubCommodityID;
				 	-- 获取商品修改前的库存
					SELECT F_NO, F_CurrentWarehousingID INTO oldNO, currentWarehousingID FROM t_commodityshopinfo WHERE F_CommodityID = iSubCommodityID AND F_ShopID = iShopID;
						
					UPDATE t_commodityshopinfo AS t SET t.F_NO = t.F_NO - iNO * iSubCommodityNO WHERE t.F_CommodityID = iSubCommodityID AND F_ShopID = iShopID;
			
			   		-- 插入商品修改历史表
		   	   		SELECT Func_CreateCommodityHistory(iSubCommodityID, '$', '$', '$', -1, -1, -100000000, oldNO, iStaffID, '$', iShopID) INTO iFuncReturnCode;

		   	   		SELECT Func_CreateRetailTradeCommoditySource(iSubCommodityID, iNO * iSubCommodityNO, currentWarehousingID, @retailTradeCommodityID) INTO iFuncReturnCode;	
					
		   	   		END LOOP read_loop;
			
		   		CLOSE list;
	  		-- 普通商品
	  		ELSE
	  			-- 获取单品的初始库存和当值入库ID
		   		SELECT F_NO, F_CurrentWarehousingID INTO oldNO, currentWarehousingID FROM t_commodityshopinfo WHERE F_CommodityID = iCommodityID AND F_ShopID = iShopID;
	  		   
	  			-- 服务商品并不需要修改库存和增加商品历史
		   		IF iType <> 3 THEN
		   			UPDATE t_commodityshopinfo SET F_NO = F_NO - iNO WHERE F_CommodityID = iCommodityID AND F_ShopID = iShopID;
		   			-- 插入商品修改历史表
		   			SELECT Func_CreateCommodityHistory(iCommodityID, '$', '$', '$', -1, -1, -100000000, oldNO, iStaffID, '$', iShopID) INTO iFuncReturnCode;	
		   		END IF;
		   		
		   		SELECT Func_CreateRetailTradeCommoditySource(iCommodityID, iNO, currentWarehousingID, @retailTradeCommodityID) INTO iFuncReturnCode;
	   		END IF;
		
			IF iFuncReturnCode = 2 THEN
	   			SET iErrorCode := 2;
	   			SET sErrorMsg := '该商品没有入库单，插入商品修改历史表失败';
	   		END IF;
		END IF;
	
	COMMIT;
END;