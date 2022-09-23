DROP PROCEDURE IF EXISTS `SP_Barcodes_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Barcodes_Create`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iCommodityID INT,
	IN sBarcode VARCHAR(64),
	IN iStaffID INT
)
BEGIN
	DECLARE iFuncReturnCode INT;
	DECLARE barcodeNO INT; -- ��Ÿ���Ʒһ���ж���������
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SELECT count(1) INTO barcodeNO FROM t_barcodes WHERE F_CommodityID = iCommodityID;
		
		IF EXISTS(SELECT 1 FROM t_barcodes WHERE F_Barcode = sBarcode AND F_CommodityID = iCommodityID) THEN
			SELECT F_ID, F_CommodityID, F_Barcode,F_CreateDatetime FROM t_barcodes WHERE F_Barcode = sBarcode AND F_CommodityID = iCommodityID;
			SET iErrorCode := 1;
			SET sErrorMsg := '���������Ѵ���';
		ELSEIF  length(sBarcode) < 7 or length(sBarcode) > 64 THEN 
			SET iErrorCode := 8;
			SET sErrorMsg := '�����볤��ֻ����7��64λ';
		ELSEIF NOT EXISTS(SELECT 1 FROM t_commodity WHERE F_ID = iCommodityID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '����ʹ�ò����ڵ���Ʒ����������';
		ELSE
			INSERT INTO t_barcodes (F_CommodityID, F_Barcode) VALUES (iCommodityID, sBarcode);
			
			SELECT F_ID, F_CommodityID, F_Barcode,F_CreateDatetime FROM t_barcodes WHERE F_ID = LAST_INSERT_ID();
			
			SELECT Func_CreateCommodityHistory(
			iCommodityID, 
			'$', 
			sBarcode, 
			'$', 
			-1, 
			-1, 
			-100000000, 
			-100000000,
			iStaffID, 
			'',
			'') INTO iFuncReturnCode; -- 0
			
--			SELECT iFuncReturnCode;
			
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
	
	COMMIT;
END;