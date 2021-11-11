DROP PROCEDURE IF EXISTS `SP_SmallSheetText_Delete`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_SmallSheetText_Delete`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iFrameID INT
	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := 'Êý¾Ý¿â´íÎó';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
	   	DELETE from t_smallsheettext WHERE F_FrameID = iFrameID;
	
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
	COMMIT;
END;