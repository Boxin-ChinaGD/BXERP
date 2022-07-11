
DROP PROCEDURE IF EXISTS `SP_CommodityProperty_Update`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_CommodityProperty_Update`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN sName1 VARCHAR(16),
	IN sName2 VARCHAR(16),
	IN sName3 VARCHAR(16),
	IN sName4 VARCHAR(16)
	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := 'Êý¾Ý¿â´íÎó';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		UPDATE t_commodityproperty
		SET F_Name1 = sName1,
			F_Name2 = sName2,
			F_Name3 = sName3,
			F_Name4 = sName4
		WHERE F_ID = 1;
		
		SELECT F_ID, F_Name1, F_Name2, F_Name3, F_Name4 FROM t_commodityproperty WHERE F_ID = 1;
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END;