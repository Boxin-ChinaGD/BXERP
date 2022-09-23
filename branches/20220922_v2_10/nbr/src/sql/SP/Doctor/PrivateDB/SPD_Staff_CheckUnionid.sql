DROP PROCEDURE IF EXISTS `SPD_Staff_CheckUnionid`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_Staff_CheckUnionid`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iID INT;
	DECLARE sUnionid VARCHAR(100);
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- 临时模板，下面list需要根据需求变化
	DECLARE list CURSOR FOR (SELECT F_ID AS iID, F_Unionid AS sUnionid FROM t_staff);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN 
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iID,sUnionid;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- F_Unionid是唯一的，在职员工是不能重复
			IF EXISTS (SELECT 1 FROM t_staff WHERE F_ID <> iID AND F_UnioniD = sUnionid AND F_status = 0) AND length(sUnionid) != 0 THEN 
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('Unionid为', sUnionid ,'的在职员工有多个，在职员工的Unionid是唯一的不能重复');
			ELSE 
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			END IF;
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;