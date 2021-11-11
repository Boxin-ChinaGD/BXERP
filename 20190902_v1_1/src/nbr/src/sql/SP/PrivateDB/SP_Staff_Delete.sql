DROP PROCEDURE IF EXISTS `SP_Staff_Delete`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Staff_Delete` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT
)
BEGIN
	DECLARE iCheckDependency VARCHAR(32);
	DECLARE iStatus INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
   		SELECT Func_CheckStaffDependency(iID, sErrorMsg) INTO iCheckDependency;
		SELECT F_Status INTO iStatus FROM t_staff WHERE F_ID = iID;
		
		IF iCheckDependency <> '' THEN
			SET iErrorCode := 7;
			SET sErrorMsg := iCheckDependency;
		ELSEIF Func_ValidateStateChange(7, iStatus, 1) <> 1 THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '该员工已删除，不能重复删除';
		ELSE
			UPDATE t_staff SET F_Status = 1 WHERE F_ID = iID;
			
			SELECT F_ID, F_Name, F_Phone, F_ICID, F_WeChat, F_OpenID, F_Unionid, F_pwdEncrypted, 
				F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, 
				F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime,
				(SELECT sr.F_RoleID FROM t_staffrole sr WHERE sr.F_StaffID = st.F_ID) AS F_RoleID
				FROM t_staff st WHERE F_ID = iID;
			
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
		
	COMMIT;
END;