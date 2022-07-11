DROP PROCEDURE IF EXISTS `SP_ConfigGeneral_Update`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_ConfigGeneral_Update` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN iValue VARCHAR(128)
	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := 'Êý¾Ý¿â´íÎó';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		UPDATE t_configgeneral SET F_Value = iValue,F_UpdateDatetime = now() WHERE F_ID = iID;
		
		SELECT F_ID, F_Name, F_Value,F_CreateDatetime,F_UpdateDatetime FROM t_configgeneral WHERE F_ID = iID;
	
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
	COMMIT;
END;