DROP PROCEDURE IF EXISTS `SPD_Warehousing_CheckStatus`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_Warehousing_CheckStatus`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	-- ...临时模板，下面的值需要根据需求变化
	DECLARE iWarehousingID INT;
	DECLARE iStatus INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- 临时模板，下面list需要根据需求变化
	DECLARE list CURSOR FOR (SELECT F_ID AS iWarehousingID, F_Status AS iStatus FROM t_warehousing);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iWarehousingID,iStatus;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- 检查所有入库单的状态，状态只能为0,1
			-- 如果为其他状态，则认为数据不健康
			IF iStatus IN (0,1) THEN
				SET iErrorCode = 0;
				SET sErrorMsg = '';
			ELSE
				SET done = TRUE;
				SET iErrorCode = 7;
				SET sErrorMsg = CONCAT('入库单', iWarehousingID, '状态异常');
			END IF;

		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;