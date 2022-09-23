DROP PROCEDURE IF EXISTS `SP_ReturnCommoditySheet_Retrieve1`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_ReturnCommoditySheet_Retrieve1`(
 	OUT iErrorCode INT,
 	OUT sErrorMsg VARCHAR(64),
	IN iID INT
	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := 'Êý¾Ý¿â´íÎó';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SELECT 
			F_ID, 
			F_SN,
			F_StaffID, 
			F_ProviderID, 
			F_Status, 
			F_CreateDatetime, 
			F_UpdateDatetime,
			F_ShopID
		FROM t_returncommoditysheet
		WHERE F_ID = iID;
		
		SELECT 
			F_ID, 
			F_ReturnCommoditySheetID, 
			F_CommodityID, 
			IF((SELECT F_Status FROM t_returncommoditysheet WHERE F_ID = iID) = 1 ,F_CommodityName,(SELECT F_Name FROM t_commodity WHERE F_ID = F_CommodityID)) AS F_CommodityName,
			F_BarcodeID,
			F_NO, 
			F_Specification,
			F_PurchasingPrice
		FROM t_returncommoditysheetcommodity 
		WHERE F_ReturnCommoditySheetID = iID;
	
		SET iErrorCode := 0;
	    SET sErrorMsg := '';
    
    COMMIT;
END;