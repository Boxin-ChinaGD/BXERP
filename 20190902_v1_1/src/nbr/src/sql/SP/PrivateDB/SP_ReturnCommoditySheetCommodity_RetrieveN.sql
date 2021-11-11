DROP PROCEDURE IF EXISTS `SP_ReturnCommoditySheetCommodity_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_ReturnCommoditySheetCommodity_RetrieveN`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iReturnCommoditySheetID INT,
	IN iPageIndex INT,
	IN iPageSize INT,
	OUT iTotalRecord INT
	)
BEGIN
	DECLARE recordIndex INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := 'Êý¾Ý¿â´íÎó';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		SET iPageIndex = iPageIndex - 1;
		
		SET recordIndex = iPageIndex * iPageSize;
		
		SELECT 
			F_ID, 
			F_ReturnCommoditySheetID, 
			F_CommodityID, 
			IF((SELECT F_Status FROM t_returncommoditysheet WHERE F_ID = iReturnCommoditySheetID) = 1 ,F_CommodityName,(SELECT F_Name FROM t_commodity WHERE F_ID = F_CommodityID)) AS F_CommodityName,
			F_BarcodeID,
			F_NO, 
			F_Specification,
			F_PurchasingPrice
		FROM t_returncommoditysheetcommodity AS rcsc
		WHERE F_ReturnCommoditySheetID = iReturnCommoditySheetID
		ORDER BY rcsc.F_ID DESC
		LIMIT recordIndex, iPageSize;
		
		SELECT count(1) INTO iTotalRecord
		FROM t_returncommoditysheetcommodity WHERE F_ReturnCommoditySheetID = iReturnCommoditySheetID;
	
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END