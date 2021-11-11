DROP PROCEDURE IF EXISTS `SP_SmallSheetFrame_Delete`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_SmallSheetFrame_Delete` (
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
	
		DELETE FROM t_smallsheettext WHERE F_FrameID = iID;
		DELETE FROM t_smallsheetframe WHERE F_ID = iID;
	
		SET iErrorCode = 0;
		SET sErrorMsg := '';
	
	COMMIT;
END