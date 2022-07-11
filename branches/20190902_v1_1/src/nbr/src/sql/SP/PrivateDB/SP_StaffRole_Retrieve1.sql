DROP PROCEDURE IF EXISTS `SP_StaffRole_Retrieve1`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_StaffRole_Retrieve1`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN iStaffID INT
	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := 'Êý¾Ý¿â´íÎó';
		ROLLBACK;
	END;
	
	START TRANSACTION;
 
		SELECT F_ID, F_StaffID, F_RoleID FROM t_staffrole WHERE (CASE iID WHEN 0 THEN 1=1 ELSE F_ID = iID END)
	   		AND (CASE iStaffID WHEN 0 THEN 1=1 ELSE F_StaffID = iStaffID END);
		
   		SET iErrorCode := 0;
   		SET sErrorMsg := '';
   		
	COMMIT;
END;