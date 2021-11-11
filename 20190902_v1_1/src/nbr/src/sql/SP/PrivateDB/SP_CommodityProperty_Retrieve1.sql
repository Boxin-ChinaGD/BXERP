DROP PROCEDURE IF EXISTS `SP_CommodityProperty_Retrieve1`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_CommodityProperty_Retrieve1`(
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
	
		SELECT 
			F_ID, 
			F_Name1, 
			F_Name2, 
			F_Name3, 
			F_Name4
		FROM t_commodityproperty;
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
	COMMIT;
END;