DROP PROCEDURE IF EXISTS `SP_Inventory_Delete`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Inventory_Delete`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT
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
	
		SET iErrorCode := 7;
		SET sErrorMsg := '只能删除待录入的盘点单';
		
		-- 	种类：0=待录入、1=已提交、2=已审核、3=已删除
		SELECT F_Status INTO iStatus FROM t_inventorysheet WHERE F_ID = iID;
		IF (SELECT Func_ValidateStateChange(1, iStatus, 3) = 1)  THEN 
		
	   		DELETE FROM t_inventorycommodity WHERE F_InventorySheetID = iID;
	   		
			UPDATE t_inventorysheet SET F_Status = 3, F_UpdateDatetime = now() WHERE F_ID = iID;
		
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
	
	COMMIT;
END;