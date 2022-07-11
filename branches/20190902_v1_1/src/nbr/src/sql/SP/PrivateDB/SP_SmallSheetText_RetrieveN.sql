DROP PROCEDURE IF EXISTS `SP_SmallSheetText_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_SmallSheetText_RetrieveN` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iFrameID INT,
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
	  	
	  	SELECT F_ID, F_Content, F_Size, F_Bold, F_Gravity, F_FrameID
		FROM t_smallsheettext WHERE F_FrameID = iFrameID
		ORDER BY F_ID DESC
		LIMIT recordIndex, iPageSize;
	
	   	
	   	SELECT COUNT(1) INTO iTotalRecord
	  	FROM t_smallsheettext WHERE F_FrameID = iFrameID;
	  	
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END