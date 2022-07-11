DROP PROCEDURE IF EXISTS `SP_Role_RetrieveAlsoStaff`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Role_RetrieveAlsoStaff`(
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
	
		SELECT F_ID, F_Name FROM t_role WHERE F_ID = iID;
		SELECT F_ID, F_Name, F_OpenID, F_Unionid, F_ShopID FROM t_staff WHERE F_ID IN (SELECT F_StaffID FROM t_staffrole WHERE F_RoleID = iID);
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END;