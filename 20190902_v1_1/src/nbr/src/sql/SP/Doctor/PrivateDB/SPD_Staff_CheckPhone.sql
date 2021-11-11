DROP PROCEDURE IF EXISTS `SPD_Staff_CheckPhone`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_Staff_CheckPhone`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iID INT;
	DECLARE sPhone VARCHAR(32);
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- 临时模板，下面list需要根据需求变化
	DECLARE list CURSOR FOR (SELECT F_ID AS iID, F_Phone AS sPhone FROM t_staff);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN 
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iID,sPhone;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- 在职员工的手机号不能重复，必须以1开头
			IF LEFT(sPhone,1) != 1 THEN 
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('ID为', iID ,'的员工的手机号不是以1开头');
			ELSEIF length(sPhone) != 11 THEN 
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('ID为', iID ,'的员工的手机号不是11位');
			ELSEIF length(0 + sPhone) != length(sPhone) THEN 
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('ID为', iID ,'的员工的手机号不是纯数字');
			ELSEIF length(sPhone) != 0 THEN 
				IF EXISTS (SELECT 1 FROM t_staff WHERE F_ID <> iID AND F_Phone = sPhone AND F_status = 0) THEN 
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('手机号为', sPhone ,'的在职员工有多个，在职员工的手机号是唯一的不能重复');
				ELSE 
				    SET iErrorCode := 0;
					SET sErrorMsg := '';	
				END IF;
			ELSE 
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			END IF;
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;