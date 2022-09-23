DROP PROCEDURE IF EXISTS `SP_RetailTradeCommodity_UploadTrade_CreateCommodity`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_RetailTradeCommodity_UploadTrade_CreateCommodity`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iTradeID INT,
	IN iCommodityID INT,
	IN iBarcodeID INT,
	IN iNO INT,
	IN fPrice Decimal(20,6),
	IN iNOCanReturn INT,
	IN iStaffID INT, -- int2
	IN fPriceReturn Decimal(20,6),
	IN fPriceSpecialOffer Decimal(20,6),
	IN fPriceVIPOriginal Decimal(20,6)
	)
BEGIN
	DECLARE iSourceTradeID INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
		IF EXISTS (SELECT 1 FROM t_commodity WHERE F_ID = iCommodityID AND F_Status = 2) THEN
			SET iBarcodeID = NULL;
		END IF;
		
		IF NOT EXISTS (SELECT 1 FROM t_commodity WHERE F_ID = iCommodityID) THEN 
			SET iErrorCode := 2;
			SET sErrorMsg := '������Ʒ������';
		ELSEIF iBarcodeID IS NOT NULL AND NOT EXISTS (SELECT 1 FROM t_barcodes WHERE F_ID = iBarcodeID) THEN
			SET iErrorCode := 2;
			SET sErrorMsg := '������Ʒ�������벻����';
		ELSEIF NOT EXISTS (SELECT 1 FROM t_staff WHERE F_ID = iStaffID) THEN
			SET iErrorCode := 2;
			SET sErrorMsg := 'Ա��������';
		ELSEIF NOT EXISTS (SELECT 1 FROM t_retailtrade WHERE F_ID = iTradeID) THEN
			SET iErrorCode := 2;
			SET sErrorMsg := '���۵�������';
		ELSEIF EXISTS (SELECT 1 FROM t_retailtradecommodity WHERE F_TradeID = iTradeID AND F_CommodityID = iCommodityID) THEN
			SET iErrorCode := 1;
			SET sErrorMsg := 'һ�����۵��������ͬ�����͵���Ʒ';
		ELSE
			SELECT F_SourceID INTO iSourceTradeID FROM t_retailtrade WHERE F_ID = iTradeID;
			
			IF iSourceTradeID = -1 THEN 
				CALL SP_RetailTradeCommodity_UploadTrade_CreateSaleCommodity(iErrorCode, sErrorMsg, iTradeID, iCommodityID, iBarcodeID, iNO, fPrice, iNOCanReturn, iStaffID, fPriceReturn, fPriceSpecialOffer, fPriceVIPOriginal);	
			ELSEIF iSourceTradeID > 0 THEN
				CALL SP_RetailTradeCommodity_UploadTrade_CreateReturnCommodity(iErrorCode, sErrorMsg, iTradeID, iCommodityID, iBarcodeID, iNO, fPrice, iNOCanReturn, iStaffID, fPriceReturn, fPriceSpecialOffer, fPriceVIPOriginal);	 
			ELSE 
				SET iErrorCode := 1;
				SET sErrorMsg := '�ڿͲ�����';
			END IF;	
		END IF;
	COMMIT;
END;

-- ������ Transaction �� lock