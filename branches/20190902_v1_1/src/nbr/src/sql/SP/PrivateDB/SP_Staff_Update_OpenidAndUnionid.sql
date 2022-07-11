DROP PROCEDURE IF EXISTS `SP_Staff_Update_OpenidAndUnionid`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Staff_Update_OpenidAndUnionid`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN sPhone VARCHAR(20),
	IN sOpenId VARCHAR(100),
	IN sUnionid VARCHAR(100),
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
	
		IF (sPhone is NULL OR sPhone = '') OR (sOpenId is NULL OR sOpenId = '') THEN
			 SET iErrorCode := 7;
			 SET sErrorMsg := '电话或者OpenId为空';
		ELSEIF NOT EXISTS (SELECT 1 FROM t_staff WHERE F_Phone = sPhone) THEN
			SET iErrorCode := 1;
			SET sErrorMsg := '员工不存在';
		ELSEIF EXISTS (SELECT 1 FROM t_staff WHERE F_OpenID = sOpenId) THEN
			SET iErrorCode = 7;
			SET sErrorMsg = '该微信号已登记，请勿重复绑定!';
	   	ELSE
			   	UPDATE t_staff
				SET
					F_Unionid = sUnionid,
					F_OpenID = sOpenId
				WHERE F_Phone = sPhone;
		   		
		   		SELECT 
		   			F_ID, 
		   			F_Name,
		   			F_Phone, 
		   			F_ICID, 
		   			F_WeChat, 
		   			F_OpenID,
		   			F_Unionid,
		   			F_pwdEncrypted,
		   			IF(iReturnSalt = 0, NULL, F_Salt) AS F_Salt,  
		   			F_PasswordExpireDate, 
		   			F_IsFirstTimeLogin, 
		   			F_ShopID, 
		   			F_DepartmentID, 
		   			F_Status,
		   			F_CreateDatetime,
		   			F_UpdateDatetime
				FROM t_staff WHERE F_Phone = sPhone;
				
		   		SET iErrorCode := 0;
		   		SET sErrorMsg := '';
	   	END IF;
	COMMIT;
END;