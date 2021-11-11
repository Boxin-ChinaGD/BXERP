DROP PROCEDURE IF EXISTS `SPD_Staff_CheckICID`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_Staff_CheckICID`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iID INT;
	DECLARE sICID VARCHAR(20);
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- 临时模板，下面list需要根据需求变化
	DECLARE list CURSOR FOR (SELECT F_ID AS iID, F_ICID AS sICID FROM t_staff);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN 
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iID,sICID;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- 身份证是唯一的，在职员工是不能重复
			IF EXISTS (SELECT 1 FROM t_staff WHERE F_ID <> iID AND F_ICID = sICID AND F_status = 0) AND length(sICID) != 0 THEN 
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('身份证为', sICID ,'的在职员工有多个，在职员工的身份证是唯一的不能重复');
			ELSE 
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			END IF;
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;