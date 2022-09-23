DROP PROCEDURE IF EXISTS `SPD_Promotion_CheckSN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_Promotion_CheckSN`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iID INT;
	DECLARE sSN VARCHAR(20);
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- ��ʱģ�壬����list��Ҫ��������仯
	DECLARE list CURSOR FOR (SELECT F_ID AS iID, F_SN AS sSN FROM t_promotion);
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
			FETCH list INTO iID,sSN;
			IF done THEN
				LEAVE read_loop;
			END IF;
			-- ������ű�����CX����cx��ͷ
			IF LEFT(sSN,2) <> 'CX' THEN -- ��Сд������
				SET done := TRUE;
			    SET iErrorCode := 7; 
			    SET sErrorMsg := CONCAT('�������', iID, '��ʽ����ȷ');
				-- ������ų��ȴ��ڵ���14λ
			ELSEIF LENGTH(sSN) < 14 THEN 
				SET done := TRUE;
			    SET iErrorCode := 7; 
			    SET sErrorMsg := CONCAT('�������', iID, '�ĳ��Ȳ���С��14λ�ַ�');		
			END IF;
			   	
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;