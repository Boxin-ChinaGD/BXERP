DROP PROCEDURE IF EXISTS `SP_ConfigCacheSize_Update`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_ConfigCacheSize_Update` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN iValue VARCHAR(20)
	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		UPDATE t_configcachesize SET F_Value = iValue,F_UpdateDatetime = now() WHERE F_ID = iID;
		
		SELECT F_ID, F_Name, F_Value,F_CreateDatetime,F_UpdateDatetime FROM t_configcachesize WHERE F_ID = iID;
	
	
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
	COMMIT;
END;