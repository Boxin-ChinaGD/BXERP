DROP PROCEDURE IF EXISTS `SPD_Staff_CheckStatus`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_Staff_CheckStatus`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iID INT;
	DECLARE iStatus INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- 临时模板，下面list需要根据需求变化
	DECLARE list CURSOR FOR (SELECT F_ID AS iID, F_Status AS iStatus FROM t_staff);
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
			
			-- 员工状态只能是0或1
			IF iStatus IN (0,1) THEN 
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			ELSE 
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('ID为', iID ,'的员工的状态只能是在职状态和离职状态');
			END IF;
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;