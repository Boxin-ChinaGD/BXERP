DROP PROCEDURE IF EXISTS `SPD_RetailTrade_CheckStatus`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_RetailTrade_CheckStatus`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	-- ...��ʱģ�壬�����ֵ��Ҫ��������仯
	DECLARE iID INT;
	DECLARE iStatus INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- ��ʱģ�壬����list��Ҫ��������仯
	DECLARE list CURSOR FOR (SELECT F_ID AS iID, F_Status AS iStatus FROM t_retailtrade);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iID, iStatus;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- ����������۵���״̬��״ֻ̬��Ϊ0,1��2
			-- ���Ϊ����״̬������Ϊ���ݲ�����
			IF iStatus IN (0,1,2) THEN 
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			ELSE
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('IDΪ', iID, '�����۵���״ֻ̬��Ϊ0��1��2');
			END IF;

		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;