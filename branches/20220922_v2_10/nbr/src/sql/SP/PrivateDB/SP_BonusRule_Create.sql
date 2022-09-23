-- 只用于测试
DROP PROCEDURE IF EXISTS `SP_BonusRule_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_BonusRule_Create`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iVipCardID INT,
	IN iAmountUnit INT,
	IN iIncreaseBonus INT,
	IN iMaxIncreaseBonus INT,
	IN iInitIncreaseBonus INT
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
		
	    INSERT INTO t_bonusrule (F_VipCardID, F_AmountUnit, F_IncreaseBonus, F_MaxIncreaseBonus, F_InitIncreaseBonus)
		VALUES (iVipCardID, iAmountUnit, iIncreaseBonus, iMaxIncreaseBonus, iInitIncreaseBonus);
		
		SELECT F_ID, F_VipCardID, F_AmountUnit, F_IncreaseBonus, F_MaxIncreaseBonus, F_InitIncreaseBonus
		FROM t_bonusrule WHERE F_ID = last_insert_id();

	COMMIT;
END;