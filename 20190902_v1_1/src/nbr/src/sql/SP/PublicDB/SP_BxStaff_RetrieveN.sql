DROP PROCEDURE IF EXISTS `SP_BxStaff_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_BxStaff_RetrieveN`(
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
		
		
		SELECT 
			F_ID, 
			F_Mobile, 
			F_pwdEncrypted, 
			F_Salt, 
			F_RoleID, 
			F_DepartmentID, 
			F_Name, 
			F_Sex, 
			F_ICID
		FROM nbr_bx.t_bxstaff
		ORDER BY F_ID DESC 
		LIMIT recordIndex, iPageSize;
		
		SELECT count(1) into iTotalRecord FROM nbr_bx.t_bxstaff;
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
	COMMIT;
END;
