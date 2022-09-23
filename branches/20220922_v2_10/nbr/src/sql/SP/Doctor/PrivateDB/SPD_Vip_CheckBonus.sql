DROP PROCEDURE IF EXISTS `SPD_Vip_CheckBonus`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_Vip_CheckBonus`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iID INT;
	DECLARE iBonus INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- ��ʱģ�壬����list��Ҫ��������仯
	DECLARE list CURSOR FOR (SELECT F_ID AS iID, F_Bonus AS iBonus FROM t_vip);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN 
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iID,iBonus;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- ��Ա�Ļ��ֲ���С��0
			IF iBonus < 0 THEN 
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('IDΪ', iID ,'�Ļ�Ա�Ļ��ֲ���С��0');
			ELSE 
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			END IF;
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;