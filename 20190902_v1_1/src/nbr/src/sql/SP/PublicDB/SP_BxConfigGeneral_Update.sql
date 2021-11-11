DROP PROCEDURE IF EXISTS `SP_BXConfigGeneral_Update`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_BXConfigGeneral_Update` (
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
	
		UPDATE nbr_bx.t_bxconfiggeneral SET F_Value = iValue, F_UpdateDatetime = now() WHERE F_ID = iID;
		
		SELECT F_ID, F_Name, F_Value,F_CreateDatetime,F_UpdateDatetime FROM nbr_bx.t_bxconfiggeneral WHERE F_ID = iID;
	
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
	COMMIT;
END;