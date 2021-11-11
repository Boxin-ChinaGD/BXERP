DROP PROCEDURE IF EXISTS `SP_SmallSheetText_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_SmallSheetText_Create` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
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
	
	   	INSERT INTO t_smallsheettext (F_Content, F_Size, F_Bold, F_Gravity, F_FrameID)
		VALUES (sContent, fSize, iBold, iGravity, iFrameID);
		
		SELECT F_ID, F_Content, F_Size, F_Bold, F_Gravity, F_FrameID,F_CreateDatetime,F_UpdateDatetime
		FROM t_smallsheettext WHERE F_ID = LAST_INSERT_ID();
	
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END