DROP PROCEDURE IF EXISTS `SPD_Pos_CheckStatus`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_Pos_CheckStatus`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iID INT;
	DECLARE iStatus INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- 临时模板，下面list需要根据需求变化
	DECLARE list CURSOR FOR (SELECT F_ID AS iID, F_Status AS iStatus FROM t_pos);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN 
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iID,iStatus;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- 
			IF iStatus IN (0,1) THEN 
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			ELSE 
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('ID为', iID ,'的POS机的状态异常');
			END IF;
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;