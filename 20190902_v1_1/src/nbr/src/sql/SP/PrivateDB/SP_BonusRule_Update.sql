DROP PROCEDURE IF EXISTS `SP_BonusRule_Update`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_BonusRule_Update`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
--	IN iVipCardID INT,
	IN iAmountUnit INT,
	IN iIncreaseBonus INT,
	IN iMaxIncreaseBonus INT,
	IN iInitIncreaseBonus INT
)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := 'Êý¾Ý¿â´íÎó';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
	    UPDATE t_bonusrule SET
		 --	F_VipCardID = iVipCardID,
			F_AmountUnit = iAmountUnit,
			F_IncreaseBonus = iIncreaseBonus,
			F_MaxIncreaseBonus = iMaxIncreaseBonus,
			F_InitIncreaseBonus = iInitIncreaseBonus
		WHERE F_ID = iID;
		
		SELECT F_ID, F_VipCardID, F_AmountUnit, F_IncreaseBonus, F_MaxIncreaseBonus, F_InitIncreaseBonus
		FROM t_bonusrule WHERE F_ID = iID;

	COMMIT;
END;