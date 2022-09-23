DROP PROCEDURE IF EXISTS `SP_ReturnCommoditySheetCommodity_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_ReturnCommoditySheetCommodity_Create`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
   	IN iReturnCommoditySheetID INT,
   	IN iCommodityID INT,
   	IN iBarcodeID INT,
   	IN iNO INT,
   	IN sSpecification VARCHAR(8),
   	IN sPurchasingPrice Decimal(20,6)
	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		IF EXISTS (SELECT 1 FROM t_returncommoditysheetcommodity WHERE F_CommodityID = iCommodityID AND F_ReturnCommoditySheetID = iReturnCommoditySheetID) THEN 
			SET iErrorCode := 1;
			SET sErrorMsg := '��������ظ����˻�����Ʒ��ͬһ�˻�����';
		ELSEIF EXISTS (SELECT 1 FROM t_returncommoditysheet WHERE F_Status = 1 AND F_ID = iReturnCommoditySheetID) THEN 
			SET iErrorCode := 7;
			SET sErrorMsg := '������ӵ�������˻�����';
		ELSEIF NOT EXISTS (SELECT 1 FROM t_returncommoditysheet WHERE F_ID = iReturnCommoditySheetID)
		OR NOT EXISTS (SELECT 1 FROM t_commodity WHERE F_ID = iCommodityID AND (F_Status = 0 OR F_Status = 1)) THEN
			SET iErrorCode := 7;
	 	 	SET sErrorMsg := '�������һ�������ڵ��˻������˻�����Ʒ';
	 	ELSEIF NOT EXISTS (SELECT 1 FROM t_barcodes WHERE F_ID = iBarcodeID) THEN 
			SET iErrorCode := 7;
			SET sErrorMsg := '������������벻���ڵ��˻�����Ʒ';
		ELSEIF EXISTS (SELECT 1 FROM t_commodity WHERE F_ID = iCommodityID AND (F_Type = 1 OR F_Type = 3)) THEN 
			SET iErrorCode := 7;
			SET sErrorMsg := '��Ʒ����Ϊ�����Ʒ�������Ʒ,���ܼӵ��˻�����';
		ELSEIF NOT EXISTS (SELECT 1 FROM t_barcodes WHERE F_CommodityID = iCommodityID AND F_ID = iBarcodeID) THEN
	   		SET iErrorCode := 7;
	   		SET sErrorMsg := '������ID����Ʒʵ��������ID����Ӧ';
		ELSE
			INSERT INTO t_returncommoditysheetcommodity (
				F_ReturnCommoditySheetID, 
				F_CommodityID,
				F_CommodityName,
				F_BarcodeID,
				F_NO,
				F_Specification,
				F_PurchasingPrice
			)
			VALUES (
				iReturnCommoditySheetID, 
				iCommodityID, 
				(SELECT F_Name FROM t_commodity WHERE F_ID = iCommodityID),
				iBarcodeID,
				iNO,
				sSpecification,
				sPurchasingPrice
			);
			
			SELECT 
				F_ID, 
				F_ReturnCommoditySheetID, 
				F_CommodityID,
				F_CommodityName,
				F_BarcodeID,
				F_NO,
				F_Specification,
				F_PurchasingPrice
			FROM t_returncommoditysheetcommodity
			WHERE F_ID = last_insert_id();
			
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
	
	COMMIT;
END;