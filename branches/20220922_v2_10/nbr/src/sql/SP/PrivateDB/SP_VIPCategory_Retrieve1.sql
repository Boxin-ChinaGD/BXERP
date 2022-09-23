DROP PROCEDURE IF EXISTS `SP_VIPCategory_Retrieve1`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_VIPCategory_Retrieve1` (
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
	
		SELECT 
			F_ID,
			F_Name,
			F_CreateDatetime,
			F_UpdateDatetime
		FROM t_vip_category WHERE F_ID = iID;
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
	COMMIT;
END;