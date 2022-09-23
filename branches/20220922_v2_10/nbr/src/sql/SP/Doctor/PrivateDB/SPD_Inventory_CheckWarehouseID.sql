DROP PROCEDURE IF EXISTS `SPD_Inventory_CheckWarehouseID`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_Inventory_CheckWarehouseID`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iInventorysheetID INT;
	DECLARE iWarehouseID INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	DECLARE list CURSOR FOR (SELECT F_ID AS iInventorysheetID, F_WarehouseID AS iWarehouseID FROM t_inventorysheet);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iInventorysheetID,iWarehouseID;
			IF done THEN
				LEAVE read_loop;
			END IF;
						
			-- ��������̵㵥�Ĳֿ�ID�������̵㶼��Ҫ���ڲֿ⣬���û������Ϊ���ݲ�����
			IF EXISTS(SELECT 1 FROM t_warehouse WHERE F_ID = iWarehouseID) THEN
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			ELSE
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('�̵㵥', iInventorysheetID ,'��Ӧ�Ĳֿ�ID����ȷ');
			END IF;
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;