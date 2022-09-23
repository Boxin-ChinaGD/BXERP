DROP PROCEDURE IF EXISTS `SP_POS_Retrieve1`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_POS_Retrieve1` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN iReturnSalt INT,
	IN iResetPasswordInPos INT  -- 1, ����ΪNULL������������
)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		SELECT 
			F_ID, 
			F_POS_SN, 
			F_ShopID, 
			F_pwdEncrypted, 
			IF(iReturnSalt = 0, NULL, F_Salt) AS F_Salt, 
			F_Status, 
			F_PasswordInPOS 
		FROM t_pos WHERE F_ID = iID ;
		
		IF iResetPasswordInPos = 1 THEN
			-- ��POS���״���֤��ݺ�,��F_PasswordInPOS��ΪNULL
			UPDATE t_pos SET F_PasswordInPOS = NULL WHERE F_ID = iID;
		END IF;
	
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END;