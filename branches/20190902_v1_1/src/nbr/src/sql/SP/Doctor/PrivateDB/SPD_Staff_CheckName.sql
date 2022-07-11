DROP PROCEDURE IF EXISTS `SPD_Staff_CheckName`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_Staff_CheckName`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iID INT;
	DECLARE sName VARCHAR(12);
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- 临时模板，下面list需要根据需求变化
	DECLARE list CURSOR FOR (SELECT F_ID AS iID, F_Name AS sName FROM t_staff);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN 
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iID,sName;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- 员工名称不能带有数字
			IF (sName REGEXP '[0-9.]') = 1 THEN 
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('ID为', iID ,'的员工名称带有数字');
			ELSE 
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			END IF;
			
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;