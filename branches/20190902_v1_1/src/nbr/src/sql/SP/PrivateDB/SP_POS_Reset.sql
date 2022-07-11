DROP PROCEDURE IF EXISTS `SP_POS_Reset`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_POS_Reset`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN iReturnSalt INT
)
BEGIN
	DECLARE iCurrentStatus INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		IF NOT EXISTS (SELECT 1 FROM t_pos WHERE F_ID = iID) THEN 
			SET iErrorCode := 7;
			SET sErrorMsg := 'POS机不存在，无法重置';
		ELSE
			UPDATE t_pos SET F_Status = 0 WHERE F_ID = iID;
					
			SELECT 
				F_ID, 
				F_POS_SN, 
				F_ShopID, 
				F_pwdEncrypted, 
				IF(iReturnSalt = 0, NULL, F_Salt) AS F_Salt,
				F_PasswordInPOS, 
				F_Status
			FROM t_pos WHERE F_ID = iID;
			
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
	
	COMMIT;
END;