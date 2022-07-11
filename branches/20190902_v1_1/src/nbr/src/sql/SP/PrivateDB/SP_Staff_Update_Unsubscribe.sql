DROP PROCEDURE IF EXISTS `SP_Staff_Update_Unsubscribe`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Staff_Update_Unsubscribe`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iStaffID INT
)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN 
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		IF NOT EXISTS (SELECT 1 FROM t_staff WHERE F_ID = iStaffID) THEN
			SET iErrorCode := 2;
			SET sErrorMsg := '员工不存在';
	   	ELSE
			   	UPDATE t_staff
				SET
					F_OpenID = NULL
				WHERE F_ID = iStaffID;
		   		
		   		SELECT 
		   			F_ID, 
		   			F_Name,
		   			F_Phone, 
		   			F_ICID, 
		   			F_WeChat, 
		   			F_OpenID,
		   			F_Unionid,
		   			F_pwdEncrypted, 
		   			F_PasswordExpireDate, 
		   			F_IsFirstTimeLogin, 
		   			F_ShopID, 
		   			F_DepartmentID, 
		   			F_Status,
		   			F_CreateDatetime,
		   			F_UpdateDatetime
				FROM t_staff WHERE F_ID = iStaffID;
				
		   		SET iErrorCode := 0;
		   		SET sErrorMsg := '';
	   	END IF;
	COMMIT;
END;