DROP PROCEDURE IF EXISTS `SP_Role_Update`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Role_Update`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN sName VARCHAR(20)
	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
	 	SET sErrorMsg := '数据库错误';
	 	ROLLBACK;
 	END;
 	
 	START TRANSACTION;
 	
		IF EXISTS (SELECT 1 FROM t_role WHERE F_ID <> iID AND F_Name = sName) THEN 
			SET iErrorCode := 1;
			SET sErrorMsg := '不能修改已存在的角色权限';
		ELSE
			UPDATE t_role SET F_Name = sName WHERE F_ID = iID;
		
			SELECT F_ID, F_Name,F_CreateDatetime,F_UpdateDatetime FROM t_role WHERE F_ID = iID;
		
	   		SET iErrorCode := 0;
	   		SET sErrorMsg := '';
		END IF;
	
	COMMIT;
END;