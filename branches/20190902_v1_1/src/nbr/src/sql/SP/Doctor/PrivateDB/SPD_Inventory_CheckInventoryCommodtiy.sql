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
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iID, iStatus;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- 检查盘点单是否存在盘点商品
			-- 如果不存在则认为数据不健康
			IF iStatus IN (0,1,2) THEN 
				IF NOT EXISTS(SELECT 1 FROM t_inventorycommodity WHERE F_InventorySheetID = iID) THEN 
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('盘点单', iID ,'没有盘点商品');
				ELSE 
					SET iErrorCode := 0;
					SET sErrorMsg := '';
				END IF;
			ELSE -- 为删除状态的盘点单可以没有盘点商品
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			END IF;
			
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;