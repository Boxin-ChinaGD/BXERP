DROP PROCEDURE IF EXISTS `SP_Staff_ResetPassword`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Staff_ResetPassword` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN sSalt VARCHAR(32),
	IN sPhone VARCHAR(32),
	IN iReturnSalt INT,
	IN iIsFirstTimeLogin INT
)
BEGIN
	DECLARE status INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;	
		-- 获取最近创建员工的状态
		SELECT ifnull(F_Status, -1) INTO status FROM t_staff WHERE F_Phone = sPhone ORDER BY F_ID DESC LIMIT 1;
			
		IF status = 0 THEN
			UPDATE t_staff
			SET F_Salt = sSalt,
				F_IsFirstTimeLogin = iIsFirstTimeLogin, 
				F_UpdateDatetime = now() 
			WHERE F_Phone = sPhone;
		   	-- 
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
		   		(SELECT F_RoleID FROM t_staffrole WHERE F_StaffID = (SELECT F_ID FROM t_staff WHERE F_Phone = sPhone AND F_Status = 0)) AS F_RoleID,
		   		F_DepartmentID, 
		   		F_Status,
		   		F_CreateDatetime,
		   		F_UpdateDatetime
			FROM t_staff WHERE F_Phone = sPhone AND F_Status = 0;
			-- 
		   	SET iErrorCode := 0;
		   	SET sErrorMsg := '';
		ELSEIF status = -1 THEN 
			SET iErrorCode := 7;
			SET sErrorMsg := '该员工不存在';
		ELSE 
			SET iErrorCode := 7;
			SET sErrorMsg := '已离职员工不能修改密码';	
	   	END IF;
	COMMIT;
END;