DROP PROCEDURE IF EXISTS `SP_POS_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_POS_Create` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN sPOS_SN VARCHAR(32),
	IN iShopID INT,
	IN sSalt VARCHAR(32),
	IN iStatus INT,
	IN iReturnSalt INT,
	IN sPasswordInPOS VARCHAR(16)
)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;

		IF EXISTS (SELECT 1 FROM t_pos WHERE F_POS_SN = sPOS_SN AND F_Status <> 1) THEN
			SET iErrorCode := 1;
			SET sErrorMsg := '该SN码已经被其他POS机引用';
		ELSEIF NOT EXISTS(SELECT 1 FROM t_shop WHERE F_ID = iShopID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '不能使用不存在的shopID创建POS';
		ELSE
			INSERT INTO t_pos (F_POS_SN, F_ShopID, F_Salt, F_Status, F_pwdEncrypted, F_PasswordInPOS) VALUES (sPOS_SN, iShopID, sSalt, iStatus, '', sPasswordInPOS);
			
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
			FROM t_pos WHERE F_ID = LAST_INSERT_ID();
	
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
	
	COMMIT;
END;