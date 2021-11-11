DROP PROCEDURE IF EXISTS `SP_Barcodes_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Barcodes_RetrieveN`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iCommodityID INT,
	IN sBarcode VARCHAR(64),
	IN iPageIndex INT,
	IN iPageSize INT,
 	OUT iTotalRecord INT
)
BEGIN
	DECLARE recordIndex INT;
	DECLARE INVALID_ID INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SELECT F_Value INTO INVALID_ID FROM t_nbrconstant WHERE F_Key = 'INVALID_ID';
		
		SET iPageIndex = iPageIndex - 1;
		
		SET recordIndex = iPageIndex * iPageSize;
		
		SELECT F_ID, F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime
		FROM t_barcodes WHERE 1 = 1 
		AND F_CommodityID IN (SELECT F_ID FROM t_commodity WHERE F_Status != 2)
		AND (CASE iCommodityID WHEN INVALID_ID THEN 1=1 ELSE F_CommodityID = iCommodityID END)
		AND (CASE sBarcode WHEN '' THEN 1=1 ELSE F_Barcode LIKE CONCAT('%',sBarcode,'%') END)
		ORDER BY F_ID DESC
		LIMIT recordIndex, iPageSize;	-- ... 格式化、效率优化
		
		SELECT count(1) into iTotalRecord
		FROM t_barcodes WHERE 1 = 1 AND F_CommodityID IN (SELECT F_ID FROM t_commodity WHERE F_Status != 2)
		AND (CASE iCommodityID WHEN INVALID_ID THEN 1=1 ELSE F_CommodityID = iCommodityID END)
		AND (CASE sBarcode WHEN '' THEN 1=1 ELSE F_Barcode LIKE CONCAT('%',sBarcode,'%') END);
	
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END;