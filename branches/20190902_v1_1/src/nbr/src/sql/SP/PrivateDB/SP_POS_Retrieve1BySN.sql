DROP PROCEDURE IF EXISTS `SP_POS_Retrieve1BySN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_POS_Retrieve1BySN`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN sPOS_SN VARCHAR(32),
	IN iReturnSalt INT
)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := 'Êý¾Ý¿â´íÎó';
		ROLLBACK;
	END;
	
	START TRANSACTION;

		SELECT 
			F_ID, 
			F_POS_SN, 
			F_ShopID, 
			F_pwdEncrypted, 
			IF(iReturnSalt = 0, NULL, F_Salt) AS F_Salt,  
			F_PasswordInPOS, 
			F_Status, 
			F_CreateDatetime, 
			F_UpdateDatetime
		FROM t_pos WHERE F_POS_SN = sPOS_SN AND F_Status <> 1;
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
	COMMIT;
END;