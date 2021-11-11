DROP PROCEDURE IF EXISTS `SP_BonusRule_Retrieve1`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_BonusRule_Retrieve1`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT
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
	
		SELECT F_ID, F_VipCardID, F_AmountUnit, F_IncreaseBonus, F_MaxIncreaseBonus, F_InitIncreaseBonus
		FROM t_bonusrule WHERE F_ID = iID;

	COMMIT;
END;
	