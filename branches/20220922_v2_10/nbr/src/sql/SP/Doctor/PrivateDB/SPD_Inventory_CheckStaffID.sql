DROP PROCEDURE IF EXISTS `SPD_Inventory_CheckStaffID`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_Inventory_CheckStaffID`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iInventorysheetID INT;
	DECLARE iStaffID INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	DECLARE list CURSOR FOR (SELECT F_ID AS iInventorysheetID, F_StaffID AS iStaffID FROM t_inventorysheet);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iInventorysheetID,iStaffID;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- ��������̵㵥��staffID,���û��staffID,��ô����Ϊ���ݲ�����
			IF EXISTS(SELECT 1 FROM t_staff WHERE F_ID = iStaffID) THEN
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			ELSE
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('�̵㵥', iInventorysheetID ,'��Ӧ�ľ�����ID����ȷ');
			END IF;
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;