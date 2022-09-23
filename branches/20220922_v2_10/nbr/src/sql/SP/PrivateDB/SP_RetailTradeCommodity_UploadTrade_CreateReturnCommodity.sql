DROP PROCEDURE IF EXISTS `SP_RetailTradeCommodity_UploadTrade_CreateReturnCommodity`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_RetailTradeCommodity_UploadTrade_CreateReturnCommodity`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iTradeID INT,
	IN iCommodityID INT,
	IN iBarcodeID INT,
	IN iNO INT,
	IN fPriceOriginal Decimal(20,6),
	IN iNOCanReturn INT,
	IN iStaffID INT, -- int2
	IN fPriceReturn Decimal(20,6),
	IN fPriceSpecialOffer Decimal(20,6),
	IN fPriceVIPOriginal Decimal(20,6)
	)
BEGIN
	DECLARE currentWarehousingID INT;
	DECLARE iShopID INT;
	DECLARE iSourceTradeID INT;
	DECLARE iSourceRtcID INT; -- Դ����Ʒ��ID
	DECLARE sourceDate DATE; -- Դ����������
	DECLARE iSourceNOCanReturn INT; -- Դ���Ŀ�������
	DECLARE commID INT;
	DECLARE sCommodityName VARCHAR(32);
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
		
		SELECT F_SourceID, F_SaleDatetime, F_ShopID INTO iSourceTradeID, sourceDate, iShopID FROM t_retailtrade WHERE F_ID = iTradeID;
		
		IF NOT EXISTS(SELECT 1 FROM t_retailtrade WHERE F_ID = iSourceTradeID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := 'Դ��ID������';
		ELSEIF NOT EXISTS(SELECT 1 FROM t_retailtradecommodity WHERE F_TradeID = iSourceTradeID AND F_CommodityID = iCommodityID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '���۵���û�и���Ʒ�������˻�';
		ELSEIF iNO > (SELECT F_NOCanReturn FROM t_retailtradecommodity WHERE F_TradeID = iSourceTradeID AND F_CommodityID = iCommodityID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '�˻��������ڿ��˻������������˻�';
		ELSE
			-- ��ȡ�˻�����Դ����Ʒ��ID
			SET iSourceRtcID := (SELECT F_ID FROM t_retailtradecommodity WHERE F_CommodityID = iCommodityID AND F_TradeID = iSourceTradeID); 
			-- ����ǰ�Ŀ��˻�����
			SET iSourceNOCanReturn := (SELECT F_NOCanReturn FROM t_retailtradecommodity WHERE F_ID = iSourceRtcID);
			-- �������۵���Ʒ��Ŀ��˻�����
			UPDATE t_retailtradecommodity SET F_NOCanReturn = F_NOCanReturn - iNO WHERE F_ID = iSourceRtcID;
			-- 
			SELECT F_CommodityName INTO sCommodityName FROM t_retailtradecommodity WHERE F_ID = iSourceRtcID;
			INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
			VALUES (iTradeID, iCommodityID, sCommodityName, iBarcodeID, iNO, fPriceOriginal,0, fPriceReturn, fPriceSpecialOffer, fPriceVIPOriginal);	
			-- 
			SELECT F_ID, F_TradeID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal
			FROM t_retailtradecommodity WHERE F_ID = LAST_INSERT_ID();
			SELECT F_Type INTO iType FROM t_commodity WHERE F_ID = iCommodityID;
			-- ���װ��Ʒ 
			IF iType = 2 THEN
				-- ��ȡ���װ��Ʒ�ĵ�ƷID�ͱ���
				SELECT F_refCommodityMultiple, F_RefCommodityID INTO cm, commID FROM t_commodity WHERE F_ID = iCommodityID;
				-- ��ȡ��Ʒ�ĳ�ʼ���͵�ֵ���ID
				SELECT F_NO, F_CurrentWarehousingID INTO oldNO, currentWarehousingID FROM t_commodityshopinfo WHERE F_CommodityID = commID AND F_ShopID = iShopID;
					
				UPDATE t_commodityshopinfo SET F_NO = F_NO + iNO * cm WHERE F_CommodityID = commID AND F_ShopID = iShopID;
				SELECT Func_DeleteRetailTradeCommoditySource(commID, iNO * cm, iSourceNOCanReturn * cm, currentWarehousingID, iSourceRtcID) INTO iFuncReturnCode;
				
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
	
						-- ��ȡ��Ʒ�޸�ǰ�Ŀ��
						SELECT F_NO, F_CurrentWarehousingID INTO oldNO, currentWarehousingID FROM t_commodityshopinfo WHERE F_CommodityID = iSubCommodityID AND F_ShopID = iShopID;
							
						UPDATE t_commodityshopinfo AS t SET t.F_NO = t.F_NO + iNO * iSubCommodityNO WHERE t.F_CommodityID = iSubCommodityID AND F_ShopID = iShopID;
						
						-- ������Ʒ�޸���ʷ��
			   			SELECT Func_CreateCommodityHistory(iSubCommodityID, '$', '$', '$', -1, -1, -100000000, oldNO, iStaffID, '$', iShopID) INTO iFuncReturnCode;
			   			
			   			SELECT Func_DeleteRetailTradeCommoditySource(iSubCommodityID, iNO * iSubCommodityNO, iSourceNOCanReturn * iSubCommodityNO, currentWarehousingID, iSourceRtcID) INTO iFuncReturnCode;
		
			   		END LOOP read_loop;
				
			   	CLOSE list;
			-- ��ͨ��Ʒ
			ELSE	
				-- ��ȡ��Ʒ�ĳ�ʼ���͵�ֵ���ID
				SELECT F_NO, F_CurrentWarehousingID INTO oldNO, currentWarehousingID FROM t_commodityshopinfo WHERE F_CommodityID = iCommodityID AND F_ShopID = iShopID;
				
				-- ������Ʒ������Ҫ�޸���Ʒ�Ŀ���������Ʒ��ʷ
				IF iType <> 3 THEN
					UPDATE t_commodityshopinfo SET F_NO = F_NO + iNO WHERE F_CommodityID = iCommodityID AND F_ShopID = iShopID;
					-- ������Ʒ�޸���ʷ��
			   		SELECT Func_CreateCommodityHistory(iCommodityID, '$', '$', '$', -1, -1, -100000000, oldNO, iStaffID, '$', iShopID) INTO iFuncReturnCode;  
				END IF;
				SELECT Func_DeleteRetailTradeCommoditySource(iCommodityID, iNO, iSourceNOCanReturn, currentWarehousingID, iSourceRtcID) INTO iFuncReturnCode;
			END IF;
			
			IF iErrorCode <> 0 THEN
				ROLLBACK;
			END IF;
		END IF;
	COMMIT;
END;