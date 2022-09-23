DROP PROCEDURE IF EXISTS `SP_Staff_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Staff_Create` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN sPhone VARCHAR(32),
   	IN sName VARCHAR(12),
	IN sICID VARCHAR(20),
	IN sWeChat VARCHAR(20),
	IN sSalt VARCHAR(32),
	IN dPasswordExpireDate DATETIME,
	IN iIsFirstTimeLogin INT,
	IN iShopID INT,
	IN iDepartmentID INT,
	IN iRoleID INT,
	IN iStatus INT,
	IN iReturnSalt INT
)
BEGIN
	DECLARE staffID INT;	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	SET iErrorCode := 0;
	SET sErrorMsg := '';
	
   	START TRANSACTION;
	
		IF EXISTS (SELECT 1 FROM t_staff WHERE F_ICID = sICID AND F_status = 0) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '在职员工中已经存在相同身份证，不能再创建这样的员工';
		ELSEIF EXISTS (SELECT 1 FROM t_staff WHERE F_WeChat = sWeChat AND F_status = 0) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '在职员工中已经存在相同微信员工，不能再创建这样的员工';
		ELSEIF EXISTS (SELECT 1 FROM t_staff WHERE F_Phone = sPhone AND F_status = 0) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '在职员工中已经存在相同电话的员工，不能再创建这样的员工';
		ELSEIF NOT EXISTS(SELECT 1 FROM t_shop WHERE F_ID = iShopID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '不能使用不存在的门店进行创建';
		ELSEIF NOT EXISTS (SELECT 1 FROM t_department WHERE F_ID = iDepartmentID) THEN
			SET iErrorCode := 7;
	   		SET sErrorMsg := '不能使用不存在的部门进行创建';
		ELSEIF NOT EXISTS (SELECT 1 FROM t_role WHERE F_ID = iRoleID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '不能使用不存在的角色进行创建';
		ELSE
			INSERT INTO t_staff (
				F_Phone, 
				F_Name,
				F_ICID, 
				F_WeChat, 
				F_Salt, 
				F_PasswordExpireDate, 
				F_IsFirstTimeLogin,
				F_ShopID, 
				F_DepartmentID, 
				F_Status)
			VALUES (
				sPhone, 
				sName,
				sICID, 
				sWeChat, 
				sSalt, 
				dPasswordExpireDate, 
				iIsFirstTimeLogin,
				iShopID, 
				iDepartmentID,
				iStatus);
				
			SET staffID = last_insert_id();
			INSERT INTO t_staffrole (F_StaffID, F_RoleID) VALUES (staffID,iRoleID);
				
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
				F_UpdateDatetime,
				iRoleID AS F_RoleID
	   		FROM t_staff WHERE F_ID = staffID;
	   			   		
		END IF;
		
	COMMIT;
END;