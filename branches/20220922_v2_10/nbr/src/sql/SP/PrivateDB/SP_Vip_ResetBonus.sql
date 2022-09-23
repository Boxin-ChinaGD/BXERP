DROP PROCEDURE IF EXISTS `SP_Vip_ResetBonus`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Vip_ResetBonus` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN 
	DECLARE iClearBonusDay INT;
	DECLARE dtClearBonusDatetime DATETIME;	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
		SELECT F_ClearBonusDay, F_ClearBonusDatetime INTO iClearBonusDay, dtClearBonusDatetime FROM t_vipcard WHERE F_ID = 1;
		
		IF iClearBonusDay IS NOT NULL THEN -- ���ݹ̶����������û���
		   	UPDATE t_vip SET F_Bonus = 0 
		   	WHERE datediff(NOW(), F_CreateDatetime) <> 0 -- ����0 % �κ�������0,������Ҫ���Ӹ��ж�
		   	AND (datediff(NOW(), F_CreateDatetime) % iClearBonusDay = 0);
		ELSEIF dtClearBonusDatetime IS NOT NULL THEN -- ���ݹ̶����������û���,ֻ�ж��º���
			IF MONTH(now()) = MONTH(dtClearBonusDatetime) AND DAY(now()) = DAY(dtClearBonusDatetime) THEN
				UPDATE t_vip SET F_Bonus = 0;
			END IF;
		ELSE
			SET iErrorCode := 7;
			SET sErrorMsg := '��Ա��������������쳣';
		END IF;
		
	COMMIT;
END;