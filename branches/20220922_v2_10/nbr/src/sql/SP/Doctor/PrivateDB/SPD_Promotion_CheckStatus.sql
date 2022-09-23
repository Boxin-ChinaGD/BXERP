
DROP PROCEDURE IF EXISTS `SPD_Promotion_CheckStatus`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_Promotion_CheckStatus`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iID INT;
	DECLARE sStatus INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- ��ʱģ�壬����list��Ҫ��������仯
	DECLARE list CURSOR FOR (SELECT F_ID AS iID, F_Status AS sStatus FROM t_promotion);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SET iErrorCode := 0; 
		SET sErrorMsg := '';
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iID,sStatus;
			IF done THEN
				LEAVE read_loop;
			END IF;
		   
			-- ����״ֻ̬������Ч״̬0,����ɾ��״̬1
			IF sStatus NOT IN (0,1) THEN 
				SET done := TRUE;
			    SET iErrorCode := 7; 
			    SET sErrorMsg := CONCAT('����', iID, 'ֻ������Ч״̬0,����ɾ��״̬1');
			END IF;
						   	
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;