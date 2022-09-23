DROP PROCEDURE IF EXISTS `SP_SmallSheetText_Update`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_SmallSheetText_Update`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN sContent VARCHAR(100),
	IN fSize Decimal(20,6),
	IN iBold INT,
	IN iGravity INT,
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
	
	   	UPDATE t_smallsheettext 
	   	SET 
	   	  F_Content = sContent,
	   	  F_Size = fSize, 
	   	  F_Bold = iBold,
	   	  F_Gravity = iGravity, 
	   	  F_FrameID = iFrameID,
	   	  F_UpdateDatetime = now()
	   	WHERE F_ID = iID;
	
		
		SELECT F_ID, F_Content, F_Size, F_Bold, F_Gravity, F_FrameID
		FROM t_smallsheettext WHERE F_ID = iID;
	
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END;