DROP PROCEDURE IF EXISTS `SPD_Commodity_CheckStatus`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_Commodity_CheckStatus`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iCommodityID INT;
	DECLARE iStatus INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- ��ʱģ�壬����list��Ҫ��������仯
	DECLARE list CURSOR FOR (SELECT F_ID AS iCommodityID, F_Status AS iStatus FROM t_commodity);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iCommodityID, iStatus;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- �����������Ʒ��״̬��״ֻ̬��Ϊ0,1,2
			IF iStatus IN (0,1,2) THEN
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			ELSE 
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('��Ʒ', iCommodityID, '��״̬��������״ֻ̬��Ϊ0��1��2');
			END IF;
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;