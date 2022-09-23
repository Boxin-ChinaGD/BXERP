DROP PROCEDURE IF EXISTS `SP_RolePermission_Delete`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_RolePermission_Delete`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT
	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		DELETE FROM t_role_permission WHERE F_RoleID = iID;
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END;