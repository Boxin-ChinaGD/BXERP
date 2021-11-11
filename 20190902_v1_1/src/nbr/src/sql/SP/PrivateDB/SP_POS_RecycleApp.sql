DROP PROCEDURE IF EXISTS `SP_POS_RecycleApp`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_POS_RecycleApp` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN sPasswordInPOS VARCHAR(16),
	IN sSalt VARCHAR(32),
	IN iReturnSalt INT
)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;

		IF NOT EXISTS(SELECT 1 FROM t_pos WHERE  F_ID = iID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '该pos机不存在';
		ELSEIF EXISTS(SELECT 1 FROM t_pos WHERE  F_ID = iID AND F_Status = 1) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '已经删除的pos机不能重新注册给app使用';
		ELSE
			UPDATE t_pos SET F_Salt = sSalt,  F_PasswordInPOS = sPasswordInPOS WHERE F_ID = iID;
			
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
			FROM t_pos WHERE F_ID = iID;
	
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
	
	COMMIT;
END;