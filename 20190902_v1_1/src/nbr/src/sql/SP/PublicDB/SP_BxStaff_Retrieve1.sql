DROP PROCEDURE IF EXISTS `SP_BxStaff_Retrieve1`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_BxStaff_Retrieve1`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN sMobile VARCHAR(11)
)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := 'Êý¾Ý¿â´íÎó';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SELECT 
			F_ID, 
			F_Mobile, 
			F_PwdEncrypted, 
			F_Salt, 
			F_RoleID, 
			F_DepartmentID, 
			F_Name, 
			F_Sex, 
			F_ICID 
		FROM nbr_bx.t_bxstaff WHERE (CASE iID WHEN 0 THEN 1 = 1 ELSE F_ID = iID END) 
			AND (CASE sMobile WHEN '' THEN 1 = 1 ELSE F_Mobile = sMobile END);
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
	COMMIT;
END;