DROP PROCEDURE IF EXISTS `SP_Permission_RetrieveAlsoRoleStaff`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Permission_RetrieveAlsoRoleStaff`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT
	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := 'Êý¾Ý¿â´íÎó';
		ROLLBACK;
	END;

	START TRANSACTION;
	
		SELECT F_ID, F_SP, F_Name, F_Domain, F_Remark, F_CreateDatetime FROM t_permission WHERE F_ID = iID;
		SELECT F_ID, F_Name FROM t_role WHERE F_ID IN(SELECT F_RoleID FROM t_role_permission WHERE F_PermissionID = iID);
		SELECT F_ID, F_Name FROM t_staff WHERE F_ID IN 
			(SELECT F_StaffID FROM t_staffrole WHERE F_RoleID IN 
			(SELECT F_RoleID FROM t_role_permission WHERE F_PermissionID = iID));
	
		SET iErrorCode := 0;
		SET sErrorMsg := '';	
	
	COMMIT;
END;