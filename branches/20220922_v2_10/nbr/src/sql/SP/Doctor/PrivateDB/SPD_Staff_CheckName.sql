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
	-- ��ʱģ�壬����list��Ҫ��������仯
	DECLARE list CURSOR FOR (SELECT F_ID AS iID, F_Name AS sName FROM t_staff);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN 
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iID,sName;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- Ա�����Ʋ��ܴ�������
			IF (sName REGEXP '[0-9.]') = 1 THEN 
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('IDΪ', iID ,'��Ա�����ƴ�������');
			ELSE 
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			END IF;
			
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;