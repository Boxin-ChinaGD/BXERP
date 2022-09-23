DROP PROCEDURE IF EXISTS `SPD_Inventory_CheckStatus`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_Inventory_CheckStatus`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iInventorysheetID INT;
	DECLARE iStatus INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	DECLARE list CURSOR FOR (SELECT F_ID AS iInventorysheetID, F_Status AS iStatus FROM t_inventorysheet);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iInventorysheetID,iStatus;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- ��������̵㵥��״̬��״ֻ̬��Ϊ0,1,2,3
			IF iStatus IN (0,1,2,3) THEN
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			ELSE
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('�̵㵥', iInventorysheetID ,'��״̬����ȷ��״ֻ̬��Ϊ0,1,2,3');
			END IF;
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;