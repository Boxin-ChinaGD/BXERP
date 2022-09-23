DROP PROCEDURE IF EXISTS `SPD_PurchasingOrder_CheckStatus`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_PurchasingOrder_CheckStatus`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iPurchasingorderID INT;
	DECLARE iStatus INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	DECLARE list CURSOR FOR (SELECT F_ID AS iPurchasingorderID FROM t_purchasingorder);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
	  
			FETCH list INTO iPurchasingorderID;
			IF done THEN
				LEAVE read_loop;
			END IF;
			-- 
			-- ������еĲɹ�������״ֻ̬��Ϊ0��1,2,3,4
			-- �������0~4֮�䣬��ô����Ϊ���ݲ�����
			SELECT F_Status INTO iStatus FROM t_purchasingorder WHERE F_ID = iPurchasingorderID;
			IF iStatus IN (0, 1, 2, 3, 4) THEN
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			ELSE
				SET done = TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('�ɹ�����', iPurchasingorderID ,'��״̬����0~4֮��');
			END IF;

		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;