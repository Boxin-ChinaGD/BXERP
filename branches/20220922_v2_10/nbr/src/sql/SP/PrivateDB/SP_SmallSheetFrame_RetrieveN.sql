DROP PROCEDURE IF EXISTS `SP_SmallSheetFrame_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_SmallSheetFrame_RetrieveN` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iPageIndex INT,
	IN iPageSize INT,
	OUT iTotalRecord INT
	)
BEGIN
	DECLARE recordIndex INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := 'Êý¾Ý¿â´íÎó';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SET iPageIndex = iPageIndex - 1;
	  	SET recordIndex = iPageIndex * iPageSize;
	  	
	  	SELECT F_ID, F_Logo, F_CountOfBlankLineAtBottom, F_DelimiterToRepeat, F_CreateDatetime, F_UpdateDatetime FROM t_smallsheetframe
	  	ORDER BY F_ID DESC
		LIMIT recordIndex, iPageSize;
	   	
	   	SELECT 
	   	F_ID, 
	   	F_Content, 
	   	F_Size, 
	   	F_Bold, 
	   	F_Gravity, 
	   	F_FrameID, 
	   	F_CreateDatetime,
	   	F_UpdateDatetime
		FROM t_smallsheettext
		ORDER BY F_ID DESC
		LIMIT recordIndex, iPageSize;
	   	
	   	SELECT COUNT(1) INTO iTotalRecord
	  	FROM t_smallsheetframe;
	  	
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END