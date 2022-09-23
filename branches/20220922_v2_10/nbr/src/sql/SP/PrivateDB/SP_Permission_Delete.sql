DROP PROCEDURE IF EXISTS `SP_Permission_Delete`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Permission_Delete`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN bForceDelete INT
	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		IF bForceDelete = 1 THEN  
			DELETE FROM T_Role_Permission WHERE F_PermissionID = iID;
			DELETE FROM T_Permission WHERE F_ID = iID;
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		ELSE IF EXISTS (SELECT 1 FROM T_Role_Permission WHERE F_PermissionID = iID) THEN
				SET iErrorCode := 7;
				SET sErrorMsg := '����ɾ�����н�ɫ��ʹ�õ�Ȩ��';
			ELSE		
				DELETE FROM T_Permission WHERE F_ID = iID;
		   		SET iErrorCode := 0;
		   		SET sErrorMsg := '';
	   		END IF;
		END IF;
		
	COMMIT;
END;