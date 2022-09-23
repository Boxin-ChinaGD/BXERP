DROP PROCEDURE IF EXISTS `SPD_Warehousing_CheckWarehousingCommodity`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_Warehousing_CheckWarehousingCommodity`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	-- ...��ʱģ�壬�����ֵ��Ҫ��������仯
	DECLARE iID INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- ��ʱģ�壬����list��Ҫ��������仯
	DECLARE list CURSOR FOR (SELECT F_ID AS iID FROM t_warehousing);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iID;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- ���������ⵥ�Ƿ���������Ʒ
			-- �������������Ϊ���ݲ�����
			IF EXISTS (SELECT 1 FROM t_warehousingcommodity WHERE F_WarehousingID = iID) THEN 
				SET iErrorCode = 0;
				SET sErrorMsg = '';
			ELSE
				SET done = TRUE;
				SET iErrorCode = 7;
				SET sErrorMsg = CONCAT('��ⵥ:', iID, 'û�������Ʒ');
			END IF;
			
			
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;