DROP PROCEDURE IF EXISTS `SPD_Warehousing_CheckWarehouseID`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_Warehousing_CheckWarehouseID`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	-- ...临时模板，下面的值需要根据需求变化
	DECLARE iWarehousingID INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- 临时模板，下面list需要根据需求变化
	DECLARE list CURSOR FOR (SELECT F_ID AS iWarehousingID FROM t_warehousing);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iWarehousingID;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- 检查所有入库单是否有对应的WarehouseID
			SELECT 1 FROM t_warehouse WHERE F_ID IN (SELECT F_WarehouseID FROM t_warehousing WHERE F_ID = iWarehousingID);
			-- 如果没有，那么则认为数据不健康
			IF FOUND_ROWS() = 1 THEN
				SET iErrorCode = 0;
				SET sErrorMsg = '';
			ELSE
				SET done = TRUE;
				SET iErrorCode = 7;
				SET sErrorMsg = concat('入库单',iWarehousingID,'没有对应的收货仓库ID');
			END IF;

		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;