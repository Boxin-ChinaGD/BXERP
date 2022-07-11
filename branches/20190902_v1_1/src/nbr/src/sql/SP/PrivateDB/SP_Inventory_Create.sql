DROP PROCEDURE IF EXISTS `SP_Inventory_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Inventory_Create`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iShopID INT,  
	IN iWarehouseID INT,  
	IN iScope INT,
	IN iStaffID INT,
	IN sRemark VARCHAR(128)
	)
BEGIN
	DECLARE sSN VARCHAR(20);
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		IF NOT EXISTS(SELECT 1 FROM t_staff WHERE F_ID = iStaffID) OR NOT EXISTS (SELECT 1 FROM t_warehouse WHERE F_ID = iWarehouseID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '不存在的StaffID或者WarehouseID创建盘点单';
		ELSEIF NOT EXISTS(SELECT 1 FROM t_shop WHERE F_ID = iShopID) THEN 
			SET iErrorCode := 7;
			SET sErrorMsg := '创建盘点单的ShopID不存在';
		ELSE
			SELECT F_SN INTO sSN FROM t_inventorysheet ORDER BY F_ID DESC LIMIT 1;
			SELECT Func_GenerateSN('PD', sSN) INTO sSN;
			
			INSERT INTO t_inventorysheet (
				F_SN,
				F_ShopID,
				F_WarehouseID, 
				F_Scope, 
				F_Status, 
				F_StaffID, 
				F_CreateDatetime, 
				F_Remark
			)
			VALUES (
				sSN,
				iShopID,
				iWarehouseID, 
				iScope, 
				0, 
				iStaffID, 
				now(), 
				sRemark
			);
				
				
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
			FROM t_inventorysheet
			WHERE F_ID = LAST_INSERT_ID();
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		END IF;
		
	COMMIT;
END;