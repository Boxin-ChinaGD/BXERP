DROP PROCEDURE IF EXISTS `SPD_InventoryCommodity_CheckInventorySheetID`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_InventoryCommodity_CheckInventorySheetID`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iInventoryCommodityID INT;
	DECLARE iInventorysheetID INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	DECLARE list CURSOR FOR (SELECT F_ID AS iInventoryCommodityID, F_InventorysheetID AS iInventorysheetID FROM t_inventorycommodity);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iInventoryCommodityID,iInventorysheetID;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- ��������̵㵥��Ʒ����̵㵥ID�Ƿ�Ϊ�̵㵥���е��̵㵥
			IF EXISTS(SELECT 1 FROM t_inventorysheet WHERE F_ID = iInventorysheetID) THEN
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			ELSE
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('�̵㵥��Ʒ', iInventoryCommodityID ,'��Ӧ���̵㵥ID����ȷ');
			END IF;
			
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;