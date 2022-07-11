DROP PROCEDURE IF EXISTS `SP_Inventory_Retrieve1`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Inventory_Retrieve1`(
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
	
		IF EXISTS (SELECT 1 FROM t_inventorysheet WHERE F_ID = iID AND F_Status = 3) THEN
			SET iErrorCode := 2;
			SET sErrorMsg := 'É¾³ýÓÐ×´Ì¬µÄ';
		ELSE
			SELECT
				F_ID,
				F_ShopID,
				F_SN,
				F_WarehouseID,
				F_Scope, 
				F_Status, 
				F_StaffID, 
				F_ApproverID,
				F_CreateDatetime, 
				F_Remark
			FROM t_inventorysheet WHERE F_ID = iID;
			
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
			FROM t_inventorycommodity
			WHERE F_InventorySheetID = iID ;
		
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
	
	COMMIT;
END;