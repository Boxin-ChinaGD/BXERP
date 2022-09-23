DROP PROCEDURE IF EXISTS `SPD_ReturnCommoditySheet_CheckProviderID`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_ReturnCommoditySheet_CheckProviderID`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	-- ...��ʱģ�壬�����ֵ��Ҫ��������仯
	DECLARE iReturnCommoditySheetID INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- ��ʱģ�壬����list��Ҫ��������仯
	DECLARE list CURSOR FOR (SELECT F_ID AS iReturnCommoditySheetID FROM t_returncommoditysheet);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iReturnCommoditySheetID;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- ��������˻����Ƿ��ж�Ӧ��providerID
			IF EXISTS (SELECT 1 FROM t_provider WHERE F_ID IN (SELECT F_ProviderID FROM t_returncommoditysheet WHERE F_ID = iReturnCommoditySheetID)) THEN 
				SET iErrorCode = 0;
				SET sErrorMsg = '';
			-- ���û�У���ô����Ϊ���ݲ�����
			ELSE
				SET done = TRUE;
				SET iErrorCode = 7;
				SET sErrorMsg = concat('�˻���', iReturnCommoditySheetID, 'û�ж�Ӧ�Ĺ�Ӧ��ID');
			END IF;
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;