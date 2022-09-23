DROP PROCEDURE IF EXISTS `SPD_Warehousing_CheckStaffID`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_Warehousing_CheckStaffID`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	-- ...��ʱģ�壬�����ֵ��Ҫ��������仯
	DECLARE iWarehousingID INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- ��ʱģ�壬����list��Ҫ��������仯
	DECLARE list CURSOR FOR (SELECT F_ID AS iWarehousingID FROM t_warehousing);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iWarehousingID;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- ���������ⵥ�Ƿ��ж�Ӧ��staffID
			SELECT 1 FROM t_staff WHERE F_ID IN (SELECT F_StaffID FROM t_warehousing WHERE F_ID = iWarehousingID);
			-- ���û�У���ô����Ϊ���ݲ�����
			IF FOUND_ROWS() = 1 THEN
				SET iErrorCode = 0;
				SET sErrorMsg = '';
			ELSE
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('��ⵥ', iWarehousingID ,'û�ж�Ӧ��ҵ��ԱID');
			END IF;

		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;