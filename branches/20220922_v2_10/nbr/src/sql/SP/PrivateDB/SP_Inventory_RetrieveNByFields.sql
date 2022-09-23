DROP PROCEDURE IF EXISTS `SP_Inventory_RetrieveNByFields`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Inventory_RetrieveNByFields`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN string1 VARCHAR(32),
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
	
	  	SET iPageIndex = iPageIndex -1;
	  	
	  	SET recordIndex = iPageIndex * iPageSize;
	  	
		SELECT 
			F_ID, 
			F_SN,
			F_ShopID,
			F_WarehouseID, 
			F_Scope, 
			F_Status, 
			F_StaffID, 
			F_ApproverID, 
			F_CreateDatetime, 
			F_ApproveDatetime, 
			F_Remark, 
			F_UpdateDatetime 
		FROM 
		(
			SELECT 
			    F_ID, 
				F_SN,
				F_ShopID,
				F_WarehouseID, 
				F_Scope, 
				F_Status, 
				F_StaffID, 
				F_ApproverID, 
				F_CreateDatetime, 
				F_ApproveDatetime, 
				F_Remark, 
				F_UpdateDatetime 
			FROM t_inventorysheet ws 
			WHERE length(string1) > 9 AND ws.F_SN LIKE CONCAT('%',replace(string1, '_', '\_'),'%') 
			UNION
			SELECT 
			    F_ID, 
				F_SN,
				F_ShopID,
				F_WarehouseID, 
				F_Scope, 
				F_Status, 
				F_StaffID, 
				F_ApproverID, 
				F_CreateDatetime, 
				F_ApproveDatetime, 
				F_Remark, 
				F_UpdateDatetime 
			FROM t_inventorysheet ws
			WHERE F_ID IN (
				SELECT F_InventorySheetID FROM t_inventorycommodity WHERE F_CommodityName LIKE CONCAT('%', replace(string1, '_', '\_'), '%') 
			)	 
		)AS TMP
		WHERE F_Status IN (0,1,2)
		
		ORDER BY F_ID DESC
		LIMIT recordIndex, iPageSize;
	
		SELECT count(1) into iTotalRecord
		FROM 
		(
			SELECT 
			    F_ID, 
				F_SN,
				F_ShopID,
				F_WarehouseID, 
				F_Scope, 
				F_Status, 
				F_StaffID, 
				F_ApproverID, 
				F_CreateDatetime, 
				F_ApproveDatetime, 
				F_Remark, 
				F_UpdateDatetime 
			FROM t_inventorysheet ws 
			WHERE length(string1) > 9 AND ws.F_SN LIKE CONCAT('%',replace(string1, '_', '\_'),'%') 
			UNION
			SELECT 
			    F_ID, 
				F_SN,
				F_ShopID,
				F_WarehouseID, 
				F_Scope, 
				F_Status, 
				F_StaffID, 
				F_ApproverID, 
				F_CreateDatetime, 
				F_ApproveDatetime, 
				F_Remark, 
				F_UpdateDatetime 
			FROM t_inventorysheet ws
			WHERE F_ID IN (
				SELECT F_InventorySheetID FROM t_inventorycommodity WHERE F_CommodityName LIKE CONCAT('%', replace(string1, '_', '\_'), '%') 
			)	 
		)AS TMP
		WHERE F_Status IN (0,1,2);
	
		SET iErrorCode=0;
		SET sErrorMsg := '';
	
	COMMIT;
END;