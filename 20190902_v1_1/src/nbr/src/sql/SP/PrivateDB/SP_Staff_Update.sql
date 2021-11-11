DROP PROCEDURE IF EXISTS `SP_Staff_Update`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Staff_Update` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN sName VARCHAR(12),
	IN sPhone VARCHAR(32),
	IN sICID VARCHAR(20),
	IN sWeChat VARCHAR(20),
	IN dPasswordExpireDate DATETIME,
	IN iShopID INT,
	IN iDepartmentID INT,
	IN iRoleID INT,	-- ... 亟需重构
	IN iStatus INT,
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
		IF iStatus <> 0 THEN 
			SET iErrorCode := 7;
			SET sErrorMsg := '状态只能修改为在职';
		ELSEIF EXISTS (SELECT 1 FROM t_staff WHERE F_ICID = sICID AND F_ID <> iID AND F_Status = 0) THEN 
			SET iErrorCode := 7;
	   		SET sErrorMsg := '在职员工中已经存在相同身份证的员工，不能修改';
		ELSEIF EXISTS (SELECT 1 FROM t_staff WHERE F_Phone = sPhone AND F_ID <> iID AND F_Status = 0) THEN
			SET iErrorCode := 7;
	   		SET sErrorMsg := '在职员工中已经存在相同电话的员工，不能修改';
	   	ELSEIF EXISTS (SELECT 1 FROM t_staff WHERE F_WeChat = sWeChat AND F_ID <> iID AND F_Status = 0) THEN
	   		SET iErrorCode := 7;
	   		SET sErrorMsg := '在职员工中已经存在相同微信的员工，不能修改';
	   	ELSEIF NOT EXISTS (SELECT 1 FROM t_role WHERE F_ID = iRoleID) THEN
	   	 	SET iErrorCode := 7;
	   		SET sErrorMsg := '不能修改成不存在的角色';	
	   	ELSEIF NOT EXISTS (SELECT 1 FROM t_shop WHERE F_ID = iShopID) THEN
	   		SET iErrorCode := 7;
	   		SET sErrorMsg := '不能修改成不存在的门店';
	   	ELSEIF NOT EXISTS (SELECT 1 FROM t_department WHERE F_ID = iDepartmentID) THEN
			SET iErrorCode := 7;
	   		SET sErrorMsg := '不能修改成不存在的部门';
	   	ELSE
	   		UPDATE t_staff
				SET F_Name = sName,
					F_Phone = sPhone,
					F_ICID = sICID,
					F_WeChat = sWeChat,
					F_PasswordExpireDate = dPasswordExpireDate,
					F_ShopID = iShopID,
					F_Status = iStatus,
					F_DepartmentID = iDepartmentID,
					F_UpdateDatetime = now()
				WHERE F_ID = iID;
		   		--				IF iStatus = 0 THEN
				DELETE FROM t_staffrole WHERE F_StaffID = iID;	
				INSERT INTO t_staffrole (F_StaffID, F_RoleID) VALUES (iID, iRoleID);
--				ELSE
--					DELETE FROM t_staffrole WHERE F_StaffID = iID;	-- ... 如果走到这，上面的update没有意义。这里需要重构。
--				END IF;
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
				FROM t_staff WHERE F_ID = iID;
				

--               SELECT F_StaffID, F_RoleID FROM t_staffrole WHERE F_StaffID = iID;
		
		   		SET iErrorCode := 0;
		   		SET sErrorMsg := '';
	   		
	   	END IF;
   	
   	COMMIT;
END;