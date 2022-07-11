DROP PROCEDURE IF EXISTS `SPD_ReturnCommoditySheet_CheckStaffID`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_ReturnCommoditySheet_CheckStaffID`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	-- ...��ʱģ�壬�����ֵ��Ҫ��������仯
	DECLARE iReturnCommoditySheetID INT;
	DECLARE iStaffID INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- ��ʱģ�壬����list��Ҫ��������仯
	DECLARE list CURSOR FOR (SELECT F_ID AS iReturnCommoditySheetID, F_StaffID AS iStaffID FROM t_returncommoditysheet);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iReturnCommoditySheetID, iStaffID;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- ������е��˻�����staffID����Ϊ�գ�����˻�����staffID�Ƿ����Staff����
			IF iStaffID IS NOT NULL THEN 
				IF EXISTS (SELECT 1 FROM t_staff WHERE F_ID = iStaffID) THEN 
					SET iErrorCode = 0;
					SET sErrorMsg = '';
				ELSE
					SET done = TRUE;
					SET iErrorCode = 7;
					SET sErrorMsg = concat('�˻���', iReturnCommoditySheetID, 'û�ж�Ӧ�Ĳ�����ID');
				END IF;
			ELSE
				SET done = TRUE;
				SET iErrorCode = 7;
				SET sErrorMsg = concat('�˻���', iReturnCommoditySheetID, '������IDΪ��');
			END IF;

		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;