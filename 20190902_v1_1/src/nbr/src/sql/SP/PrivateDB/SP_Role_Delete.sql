DROP PROCEDURE IF EXISTS `SP_Role_Delete`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Role_Delete`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN bForceDelete INT
	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		IF bForceDelete = 1 THEN
			DELETE FROM T_staffrole WHERE F_RoleID = iID;
			DELETE FROM t_role_permission WHERE F_RoleID = iID;
			DELETE FROM t_role WHERE F_ID = iID;
			
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		ELSE IF EXISTS (SELECT 1 FROM T_staffrole WHERE F_RoleID = iID) 
			OR EXISTS (SELECT 1 FROM t_role_permission WHERE F_RoleID = iID) THEN
			
				SET iErrorCode := 7;
				SET sErrorMsg := '已在员工角色存在的角色，不能删除';
			ELSE		
				DELETE FROM T_staffrole WHERE F_RoleID = iID;	 
				DELETE FROM t_role_permission WHERE F_RoleID = iID;
				DELETE FROM t_role WHERE F_ID = iID;
				
		   		SET iErrorCode := 0;
		   		SET sErrorMsg := '';
	   		END IF;
		END IF;
	
	COMMIT;
END;