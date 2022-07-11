DROP PROCEDURE IF EXISTS `SP_POS_Update`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_POS_Update` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN iShopID INT,
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
	
		IF NOT EXISTS(SELECT 1 FROM t_shop WHERE F_ID = iShopID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '不能使用不存在的shopID进行修改';
		ELSE 
			UPDATE t_pos SET F_ShopID = iShopID, F_UpdateDatetime = now() WHERE F_ID = iID;
			
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