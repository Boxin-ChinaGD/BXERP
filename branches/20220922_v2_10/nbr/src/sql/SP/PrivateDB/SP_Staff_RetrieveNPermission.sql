DROP PROCEDURE IF EXISTS `SP_Staff_RetrieveNPermission`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Staff_RetrieveNPermission` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iPageIndex INT,
	IN iPageSize INT,
	OUT iTotalRecord INT
	)
BEGIN
	DECLARE recordIndex INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := 'Êý¾Ý¿â´íÎó';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SET iPageIndex = iPageIndex - 1;
		
		SET recordIndex = iPageIndex * iPageSize;
		SET @i = 1;
	
		SELECT @i:=@i+1 AS F_ID, StaffID, StaffName, RoleID, RoleName, F_SP, PermissionName, F_Remark 
		FROM V_Staff_Permission 
		ORDER BY F_ID DESC LIMIT recordIndex,iPageSize;
		
		SELECT count(1) into iTotalRecord
		FROM V_Staff_Permission;
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END;