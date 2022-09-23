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
	-- ��ʱģ�壬����list��Ҫ��������仯
	DECLARE list CURSOR FOR (SELECT F_ID AS iID, F_Phone AS sPhone FROM t_staff);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN 
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iID,sPhone;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- ��ְԱ�����ֻ��Ų����ظ���������1��ͷ
			IF LEFT(sPhone,1) != 1 THEN 
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('IDΪ', iID ,'��Ա�����ֻ��Ų�����1��ͷ');
			ELSEIF length(sPhone) != 11 THEN 
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('IDΪ', iID ,'��Ա�����ֻ��Ų���11λ');
			ELSEIF length(0 + sPhone) != length(sPhone) THEN 
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('IDΪ', iID ,'��Ա�����ֻ��Ų��Ǵ�����');
			ELSEIF length(sPhone) != 0 THEN 
				IF EXISTS (SELECT 1 FROM t_staff WHERE F_ID <> iID AND F_Phone = sPhone AND F_status = 0) THEN 
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('�ֻ���Ϊ', sPhone ,'����ְԱ���ж������ְԱ�����ֻ�����Ψһ�Ĳ����ظ�');
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