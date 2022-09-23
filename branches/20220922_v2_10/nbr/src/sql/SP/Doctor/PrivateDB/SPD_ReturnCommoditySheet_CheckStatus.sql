DROP PROCEDURE IF EXISTS `SPD_ReturnCommoditySheet_CheckStatus`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_ReturnCommoditySheet_CheckStatus`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	-- ...��ʱģ�壬�����ֵ��Ҫ��������仯
	DECLARE iReturnCommoditySheetID INT;
	DECLARE iStatus INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- ��ʱģ�壬����list��Ҫ��������仯
	DECLARE list CURSOR FOR (SELECT F_ID AS iReturnCommoditySheetID, F_Status AS iStatus FROM t_returncommoditysheet);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iReturnCommoditySheetID, iStatus;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- ������е��˻�����״ֻ̬��Ϊ0��1
			IF iStatus IN (0, 1) THEN 
				SET iErrorCode = 0;
				SET sErrorMsg = '';
			-- �������0��1֮�䣬��ô����Ϊ���ݲ�����
			ELSE
				SET iErrorCode = 7;
				SET sErrorMsg = concat('�˻���', iReturnCommoditySheetID, '״̬�쳣');
				SET done = TRUE;
			END IF;
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;