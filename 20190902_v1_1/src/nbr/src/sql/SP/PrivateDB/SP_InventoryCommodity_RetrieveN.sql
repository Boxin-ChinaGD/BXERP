DROP PROCEDURE IF EXISTS `SP_InventoryCommodity_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_InventoryCommodity_RetrieveN`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iInventorySheetID INT,
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
			F_InventorySheetID, 
			F_CommodityID, 
			F_CommodityName, 
			F_Specification, 
			F_BarcodeID, 
			F_PackageUnitID, 
			F_NOReal, 
			F_NOSystem, 
			F_CreateDatetime, 
			F_UpdateDatetime
		FROM t_inventorycommodity WHERE F_InventorySheetID = iInventorySheetID
		ORDER BY F_ID DESC	
		LIMIT recordIndex,iPageSize;
		
		SELECT count(1) into iTotalRecord
		FROM t_inventorycommodity WHERE F_InventorySheetID = iInventorySheetID	;
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
	COMMIT;
END;