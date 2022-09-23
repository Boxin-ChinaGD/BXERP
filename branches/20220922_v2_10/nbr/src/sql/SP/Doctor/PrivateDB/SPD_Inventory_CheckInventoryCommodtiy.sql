DROP PROCEDURE IF EXISTS `SPD_Inventory_CheckInventoryCommodtiy`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_Inventory_CheckInventoryCommodtiy`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iID INT;
	DECLARE iStatus INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	DECLARE list CURSOR FOR (SELECT F_ID AS iID, F_Status AS iStatus FROM t_inventorysheet);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iID, iStatus;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- ����̵㵥�Ƿ�����̵���Ʒ
			-- �������������Ϊ���ݲ�����
			IF iStatus IN (0,1,2) THEN 
				IF NOT EXISTS(SELECT 1 FROM t_inventorycommodity WHERE F_InventorySheetID = iID) THEN 
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('�̵㵥', iID ,'û���̵���Ʒ');
				ELSE 
					SET iErrorCode := 0;
					SET sErrorMsg := '';
				END IF;
			ELSE -- Ϊɾ��״̬���̵㵥����û���̵���Ʒ
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			END IF;
			
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;