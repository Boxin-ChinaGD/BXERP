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
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SET iErrorCode := 7;
		SET sErrorMsg := 'ֻ��ɾ����¼����̵㵥';
		
		-- 	���ࣺ0=��¼�롢1=���ύ��2=����ˡ�3=��ɾ��
		SELECT F_Status INTO iStatus FROM t_inventorysheet WHERE F_ID = iID;
		IF (SELECT Func_ValidateStateChange(1, iStatus, 3) = 1)  THEN 
		
	   		DELETE FROM t_inventorycommodity WHERE F_InventorySheetID = iID;
	   		
			UPDATE t_inventorysheet SET F_Status = 3, F_UpdateDatetime = now() WHERE F_ID = iID;
		
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
	
	COMMIT;
END;