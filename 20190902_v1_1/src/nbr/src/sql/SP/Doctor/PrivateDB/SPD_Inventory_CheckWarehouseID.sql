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
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iInventorysheetID,iWarehouseID;
			IF done THEN
				LEAVE read_loop;
			END IF;
						
			-- 检查所有盘点单的仓库ID，所有盘点都需要存在仓库，如果没有则认为数据不健康
			IF EXISTS(SELECT 1 FROM t_warehouse WHERE F_ID = iWarehouseID) THEN
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			ELSE
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('盘点单', iInventorysheetID ,'对应的仓库ID不正确');
			END IF;
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;