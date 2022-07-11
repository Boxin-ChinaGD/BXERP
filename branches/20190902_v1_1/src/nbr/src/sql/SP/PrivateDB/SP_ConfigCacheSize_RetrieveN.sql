DROP PROCEDURE IF EXISTS `SP_ConfigCacheSize_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_ConfigCacheSize_RetrieveN` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	OUT iTotalRecord INT
	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := 'Êý¾Ý¿â´íÎó';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SELECT F_ID, F_Name, F_Value FROM t_configcachesize;
		
		SET iTotalRecord := 0;
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END;