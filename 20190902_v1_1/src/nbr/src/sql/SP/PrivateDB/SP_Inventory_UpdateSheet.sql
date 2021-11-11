DROP PROCEDURE IF EXISTS `SP_Inventory_UpdateSheet`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Inventory_UpdateSheet`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,	   			  		    			   
	IN sRemark VARCHAR(128)
	)
BEGIN
	DECLARE iStatus INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SELECT F_Status INTO iStatus FROM t_inventorysheet WHERE F_ID = iID;
		
		IF NOT EXISTS(SELECT 1 FROM t_inventorysheet WHERE F_ID = iID) THEN 
			SET iErrorCode := 7;
			SET sErrorMsg := '盘点单不存在';
		ELSEIF NOT EXISTS(SELECT 1 FROM t_inventorycommodity WHERE F_InventorySheetID = iID) THEN 
			SET iErrorCode := 7;
			SET sErrorMsg := '该盘点单没有盘点商品';
		ELSEIF iStatus IN (0,1) THEN
			UPDATE t_inventorysheet SET 
				F_Remark = sRemark,
				F_UpdateDatetime = now()
			WHERE F_ID = iID;
			
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
			WHERE F_ID = iID;
			
			SET iErrorCode := 0;
	   		SET sErrorMsg := '';
		ELSE 
			SET iErrorCode := 7;
			SET sErrorMsg := '盘点单为已审核或者已删除时，修改盘点总结失败';
	   	END IF;
	
	COMMIT;
END;