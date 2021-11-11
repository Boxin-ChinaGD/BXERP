DROP PROCEDURE IF EXISTS `SP_BonusConsumeHistory_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_BonusConsumeHistory_Create`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iVipID INT,
	IN iStaffID INT,
	IN iBonus INT,
	IN iAddedBonus INT,
	IN sRecordBonus VARCHAR(48)
)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
		IF NOT EXISTS(SELECT 1 FROM t_vip WHERE F_ID = iVipID) THEN
			SET iErrorCode := 2;
			SET sErrorMsg := '会员ID不存在';
		ELSE
		    INSERT INTO t_bonusconsumehistory (F_VipID, F_StaffID, F_Bonus, F_AddedBonus, F_Remark, F_CreateDatetime)
			VALUES (iVipID, iStaffID, iBonus, iAddedBonus, sRecordBonus, now());
			
			SELECT F_ID, F_VipID, F_StaffID, F_Bonus, F_AddedBonus, F_Remark, F_CreateDatetime
			FROM t_bonusconsumehistory WHERE F_ID = last_insert_id();
		END IF;

	COMMIT;
END;