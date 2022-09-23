DROP PROCEDURE IF EXISTS `SP_SmallSheetFrame_Retrieve1`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_SmallSheetFrame_Retrieve1` (
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
	
	   	SELECT F_ID, F_Logo, F_CountOfBlankLineAtBottom, F_DelimiterToRepeat, F_CreateDatetime, F_UpdateDatetime FROM t_smallsheetframe WHERE F_ID = iID;
	   	
	   	SELECT 
	   	F_ID, 
	   	F_Content, 
	   	F_Size, 
	   	F_Bold, 
	   	F_Gravity, 
	   	F_FrameID,
	   	F_CreateDatetime,
	   	F_UpdateDatetime
		FROM t_smallsheettext WHERE F_FrameID = iID;
	
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END