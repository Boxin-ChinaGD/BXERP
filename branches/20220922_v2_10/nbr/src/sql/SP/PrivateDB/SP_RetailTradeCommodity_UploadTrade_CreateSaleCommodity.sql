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
	DECLARE cm INT; -- ���װ��Ʒ����Ʒ����
	DECLARE oldNO INT; -- ��Ʒ��ʼ�Ŀ��
	DECLARE iSubCommodityID INT; -- �����Ʒ����ƷID
	DECLARE iSubCommodityNO INT; -- �����Ʒ����Ʒ����
	DECLARE iType INT; -- ��Ʒ������
	DECLARE iFuncReturnCode INT;
	DECLARE done INT DEFAULT FALSE;					-- ����������־����
	DECLARE list CURSOR FOR (
		SELECT  
		    F_SubCommodityID AS iSubCommodityID, 
			F_SubCommodityNO AS iSubCommodityNO
		FROM t_subcommodity 
		WHERE F_CommodityID = iCommodityID);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  -- ָ���α�ѭ������ʱ����ֵ
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SET sErrorMsg := '';
		SET iErrorCode := 0;
		SELECT F_ShopID INTO iShopID FROM t_retailtrade WHERE F_ID = iTradeID;
		
		IF NOT EXISTS(SELECT 1 FROM t_commodity WHERE F_ID = iCommodityID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '����Ʒ������';
		ELSE 
			INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
			VALUES (iTradeID, iCommodityID,(SELECT F_Name FROM t_commodity WHERE F_ID = iCommodityID), iBarcodeID, iNO, fPriceOriginal, iNO, fPriceReturn, fPriceSpecialOffer, fPriceVIPOriginal);
			SET @retailTradeCommodityID := Last_insert_id();
			
			SELECT F_ID, F_TradeID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal
			FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
			
			SELECT F_Type INTO iType FROM t_commodity WHERE F_ID = iCommodityID;

			-- ���װ��Ʒ 
	  		IF iType = 2 THEN 
		  		-- ��ȡ���װ��Ʒ�ĵ�ƷID�ͱ���
		  		SELECT F_refCommodityMultiple, F_RefCommodityID INTO cm, commID FROM t_commodity WHERE F_ID = iCommodityID;
		 		-- ��ȡ��Ʒ�ĳ�ʼ���͵�ֵ���ID
--		 		SELECT F_NO, F_CurrentWarehousingID INTO oldNO, currentWarehousingID FROM t_commodity WHERE F_ID = commID; 
				SELECT F_NO, F_CurrentWarehousingID INTO oldNO, currentWarehousingID FROM t_commodityshopinfo WHERE F_CommodityID = commID AND F_ShopID = iShopID;
				    	   
		  		UPDATE t_commodityshopinfo AS t SET t.F_NO = t.F_NO - iNO * cm WHERE t.F_CommodityID = commID AND t.F_ShopID = iShopID;
			
		  		SELECT Func_CreateRetailTradeCommoditySource(commID, iNO * cm, currentWarehousingID, @retailTradeCommodityID) INTO iFuncReturnCode;	
		  		-- ������Ʒ�޸���ʷ��
		  		SELECT Func_CreateCommodityHistory(commID, '$', '$', '$', -1, -1, -100000000, oldNO, iStaffID, '$', iShopID) INTO iFuncReturnCode;
			
	   		-- �����Ʒ
	  		ELSEIF iType = 1 THEN
	   			OPEN list;
					read_loop: LOOP
					FETCH list INTO iSubCommodityID,iSubCommodityNO;
						IF done THEN
				  		LEAVE read_loop;
		   			END IF;
					
				   	SELECT iSubCommodityID;
				 	-- ��ȡ��Ʒ�޸�ǰ�Ŀ��
					SELECT F_NO, F_CurrentWarehousingID INTO oldNO, currentWarehousingID FROM t_commodityshopinfo WHERE F_CommodityID = iSubCommodityID AND F_ShopID = iShopID;
						
					UPDATE t_commodityshopinfo AS t SET t.F_NO = t.F_NO - iNO * iSubCommodityNO WHERE t.F_CommodityID = iSubCommodityID AND F_ShopID = iShopID;
			
			   		-- ������Ʒ�޸���ʷ��
		   	   		SELECT Func_CreateCommodityHistory(iSubCommodityID, '$', '$', '$', -1, -1, -100000000, oldNO, iStaffID, '$', iShopID) INTO iFuncReturnCode;

		   	   		SELECT Func_CreateRetailTradeCommoditySource(iSubCommodityID, iNO * iSubCommodityNO, currentWarehousingID, @retailTradeCommodityID) INTO iFuncReturnCode;	
					
		   	   		END LOOP read_loop;
			
		   		CLOSE list;
	  		-- ��ͨ��Ʒ
	  		ELSE
	  			-- ��ȡ��Ʒ�ĳ�ʼ���͵�ֵ���ID
		   		SELECT F_NO, F_CurrentWarehousingID INTO oldNO, currentWarehousingID FROM t_commodityshopinfo WHERE F_CommodityID = iCommodityID AND F_ShopID = iShopID;
	  		   
	  			-- ������Ʒ������Ҫ�޸Ŀ���������Ʒ��ʷ
		   		IF iType <> 3 THEN
		   			UPDATE t_commodityshopinfo SET F_NO = F_NO - iNO WHERE F_CommodityID = iCommodityID AND F_ShopID = iShopID;
		   			-- ������Ʒ�޸���ʷ��
		   			SELECT Func_CreateCommodityHistory(iCommodityID, '$', '$', '$', -1, -1, -100000000, oldNO, iStaffID, '$', iShopID) INTO iFuncReturnCode;	
		   		END IF;
		   		
		   		SELECT Func_CreateRetailTradeCommoditySource(iCommodityID, iNO, currentWarehousingID, @retailTradeCommodityID) INTO iFuncReturnCode;
	   		END IF;
		
			IF iFuncReturnCode = 2 THEN
	   			SET iErrorCode := 2;
	   			SET sErrorMsg := '����Ʒû����ⵥ��������Ʒ�޸���ʷ��ʧ��';
	   		END IF;
		END IF;
	
	COMMIT;
END;