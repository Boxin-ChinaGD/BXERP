DROP PROCEDURE IF EXISTS `SPD_Staff_CheckIsFirstTimeLogin`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_Staff_CheckIsFirstTimeLogin`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iID INT;
	DECLARE iIsFirstTimeLogin INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- ��ʱģ�壬����list��Ҫ��������仯
	DECLARE list CURSOR FOR (SELECT F_ID AS iID, F_IsFirstTimeLogin AS iIsFirstTimeLogin FROM t_staff);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN 
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iID,iIsFirstTimeLogin;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- �Ƿ��״ε�½��ֵ������0��1
			IF iIsFirstTimeLogin IN (0,1) THEN 
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			ELSE 
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('IDΪ', iID ,'��Ա����F_IsFirstTimeLogin��ֵ����0��1');
			END IF;
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;