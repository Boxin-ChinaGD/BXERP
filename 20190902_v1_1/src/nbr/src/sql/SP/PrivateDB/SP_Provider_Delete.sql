DROP PROCEDURE IF EXISTS `SP_Provider_Delete`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Provider_Delete`(
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
		SET sErrorMsg := Func_CheckProviderDependency(iID, sErrorMsg);
		IF (sErrorMsg <> '') THEN
			SET iErrorCode := 7;
		ELSE		
			DELETE FROM t_Provider WHERE F_ID = iID;
			SET iErrorCode := 0;
		END IF;
	
	COMMIT;
END;