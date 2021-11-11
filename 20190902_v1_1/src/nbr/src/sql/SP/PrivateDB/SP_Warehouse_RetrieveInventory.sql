
-- 计算仓库中的以下值 ：
-- 本仓库的库存总额，按进货价(即采购价)计算
-- 库存总额最高的商品的库存总额
-- 库存总额最高的商品
DROP PROCEDURE IF EXISTS `SP_Warehouse_RetrieveInventory`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Warehouse_RetrieveInventory` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iShopID INT 
	)
BEGIN  
	DECLARE iNO INT;
	DECLARE iCommID INT;
	DECLARE fPrice Decimal(20, 6); 
	DECLARE fTotalInventory Decimal(20, 6); 
	DECLARE fMaxTotalInventory Decimal(20, 6); 
    DECLARE fTmpAmount Decimal(20, 6);
    DECLARE	sTargetCommodityName VARCHAR(32);
    DECLARE	sCommodityName VARCHAR(32);
    DECLARE iMaxTotalInventoryNO INT;
    DECLARE fMaxTotalInventoryPrice Decimal(20, 6); 
    
    DECLARE done INT DEFAULT  false;  
    
    -- 拿最近进货价来计算，而不是拿历史平均值，也不是拿入库单的进货价计算
    -- 只计算单品
--	DECLARE list CURSOR FOR (SELECT F_NO AS iNO, F_LatestPricePurchase AS fPrice, F_Name AS sTargetCommodityName FROM t_commodity WHERE F_Status != 2 AND F_Type = 0);
	DECLARE list CURSOR FOR (SELECT F_ID AS iCommID, F_Name AS sTargetCommodityName FROM t_commodity WHERE F_Status != 2 AND F_Type = 0);
	
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = true;  
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
    
    START TRANSACTION;
    -- 最大库存总额当有未删除的单品时，以第一条作为初始值，否则就为0
    IF EXISTS(SELECT 1 FROM t_commodity WHERE F_Status != 2 AND F_Type = 0) THEN 
--		SELECT F_NO,F_LatestPricePurchase,F_Name into iMaxTotalInventoryNO, fMaxTotalInventoryPrice,sCommodityName FROM t_commodity WHERE F_Status != 2 AND F_Type = 0 limit 1;
    	SELECT F_Name into sCommodityName FROM t_commodity WHERE F_Status != 2 AND F_Type = 0 limit 1;
    	SELECT F_NO,F_LatestPricePurchase into iMaxTotalInventoryNO, fMaxTotalInventoryPrice FROM t_commodityshopinfo WHERE F_ShopID = iShopID AND F_CommodityID = (SELECT F_ID FROM t_commodity WHERE F_Status != 2 AND F_Type = 0 limit 1);
    	SET fMaxTotalInventory = iMaxTotalInventoryNO * fMaxTotalInventoryPrice;
    ELSE		  
	    SET fMaxTotalInventory = 0.000000;  
    END IF;
    
	    SET fTotalInventory = 0.000000;
--	    OPEN  list;  
--	    	read_loop: LOOP	  
--	    FETCH list INTO iNO, fPrice, sTargetCommodityName;   
--		    IF done THEN  
--		        LEAVE read_loop;   
--		    END IF;  
--		    IF fPrice - -1.000000 < 0.000001 THEN 
--		    	SET fPrice = 0.000000;-- 由于商品的最近采购价（进货价）在没有任何进货的情况下默认值是-1，为了避免计算错误，将fPrice设为0
--		    END IF;
--
--	    	SET fTmpAmount = iNO * fPrice;
--	   		SET fTotalInventory = fTotalInventory + fTmpAmount;
--	   		IF fMaxTotalInventory < fTmpAmount THEN 
--	   			SET fMaxTotalInventory = fTmpAmount;
--	   			SET sCommodityName = sTargetCommodityName;
--	   		END IF;
--	 	END LOOP read_loop;
--	   	CLOSE list;
	    OPEN  list;  
	    	read_loop: LOOP	  
	    FETCH list INTO iCommID, sTargetCommodityName;   
		    IF done THEN  
		        LEAVE read_loop;   
		    END IF;  
		    SELECT F_NO, F_LatestPricePurchase INTO iNO, fPrice FROM t_commodityshopinfo WHERE F_CommodityID = iCommID AND F_ShopID = iShopID;
		    IF fPrice - -1.000000 < 0.000001 THEN 
		    	SET fPrice = 0.000000;-- 由于商品的最近采购价（进货价）在没有任何进货的情况下默认值是-1，为了避免计算错误，将fPrice设为0
		    END IF;

	    	SET fTmpAmount = iNO * fPrice;
	   		SET fTotalInventory = fTotalInventory + fTmpAmount;
	   		IF fMaxTotalInventory < fTmpAmount THEN 
	   			SET fMaxTotalInventory = fTmpAmount;
	   			SET sCommodityName = sTargetCommodityName;
	   		END IF;
	 	END LOOP read_loop;
	   	CLOSE list;
	   	SELECT fMaxTotalInventory, fTotalInventory, sCommodityName;
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END;  