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
	DECLARE barcodeNO INT; -- 存放该商品一共有多少条形码
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SELECT count(1) INTO barcodeNO FROM t_barcodes WHERE F_CommodityID = iCommodityID;
		
		IF EXISTS(SELECT 1 FROM t_barcodes WHERE F_Barcode = sBarcode AND F_CommodityID = iCommodityID) THEN
			SELECT F_ID, F_CommodityID, F_Barcode,F_CreateDatetime FROM t_barcodes WHERE F_Barcode = sBarcode AND F_CommodityID = iCommodityID;
			SET iErrorCode := 1;
			SET sErrorMsg := '该条形码已存在';
		ELSEIF  length(sBarcode) < 7 or length(sBarcode) > 64 THEN 
			SET iErrorCode := 8;
			SET sErrorMsg := '条形码长度只能是7至64位';
		ELSEIF NOT EXISTS(SELECT 1 FROM t_commodity WHERE F_ID = iCommodityID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '不能使用不存在的商品创建条形码';
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