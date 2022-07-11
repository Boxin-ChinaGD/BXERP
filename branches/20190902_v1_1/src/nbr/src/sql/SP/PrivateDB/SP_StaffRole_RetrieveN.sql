
DROP PROCEDURE IF EXISTS `SP_StaffRole_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_StaffRole_RetrieveN` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iRoleID INT,
	IN iStatus INT,
	IN iOperator INT, -- 1, OP在操作。0，Boss在操作
	IN iPageIndex INT,
	IN iPageSize INT,
	OUT iTotalRecord INT
)
BEGIN
	DECLARE recordIndex INT;
	DECLARE INVALID_ID INT;
	DECLARE INVALID_STATUS INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN	
		
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SELECT F_Value INTO INVALID_ID FROM t_nbrconstant WHERE F_Key = 'INVALID_ID';
		SELECT F_Value INTO INVALID_STATUS FROM t_nbrconstant WHERE F_Key = 'INVALID_STATUS';
		
		SET iPageIndex = iPageIndex - 1;
		
		SET recordIndex = iPageIndex * iPageSize;

		SELECT 
			tsr.F_ID, 
			tsr.F_StaffID AS staffID, 
			tsr.F_RoleID AS roleID,
			tr.F_Name AS roleName,
		  	ts.F_Name AS staffName,
		  	ts.F_Phone AS staffPhone,
		  	ts.F_ICID AS staffICID,
		  	ts.F_WeChat AS staffWeChat,
		  	ts.F_OpenID AS staffOpenID,
		  	ts.F_Unionid AS staffUnionid,
		  	ts.F_pwdEncrypted AS staffpwdEncrypted,
		  	ts.F_Salt AS staffSalt,
		  	ts.F_PasswordExpireDate AS staffPasswordExpireDate,
		  	ts.F_IsFirstTimeLogin AS staffIsFirstTimeLogin,
		  	ts.F_ShopID AS staffShopID,
		  	ts.F_DepartmentID AS staffDepartmentID,
		  	ts.F_Status AS staffStatus,
		  	ts.F_CreateDatetime AS staffCreateDatetime,
		  	ts.F_UpdateDatetime AS staffUpdateDatetime
		FROM t_staffrole tsr, t_staff ts, t_role tr
		WHERE 1 = 1
		AND ts.F_ID = tsr.F_StaffID 
		AND tr.F_ID = tsr.F_RoleID 
		AND (CASE iStatus WHEN INVALID_STATUS THEN 1 = 1 ELSE ts.F_Status = iStatus END) 
		AND (CASE iRoleID WHEN INVALID_ID THEN 1 = 1 ELSE tr.F_ID = iRoleID END) 
	   	AND (CASE iOperator WHEN 1 THEN 1 = 1 ELSE ts.F_Phone != '13888888888' END) -- OP才能查询出售前账号

		ORDER BY F_ID DESC
		LIMIT recordIndex,iPageSize;
		
	   	SELECT count(1) into iTotalRecord FROM t_staff WHERE F_ID IN(
	   		SELECT 
				ts.F_ID
			FROM t_staffrole tsr, t_staff ts, t_role tr
			WHERE 1 = 1
			AND ts.F_ID = tsr.F_StaffID AND tr.F_ID = tsr.F_RoleID 
			AND (CASE iStatus WHEN INVALID_STATUS THEN 1 = 1 ELSE ts.F_Status = iStatus END) 
		   	AND (CASE iRoleID WHEN INVALID_ID THEN 1 = 1 ELSE tr.F_ID = iRoleID END)
	   		AND (CASE iOperator WHEN 1 THEN 1 = 1 ELSE ts.F_Phone != '13888888888' END)); -- OP才能查询出售前账号
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
	COMMIT;
END;