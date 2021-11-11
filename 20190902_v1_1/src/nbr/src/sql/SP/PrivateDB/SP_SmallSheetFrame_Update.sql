DROP PROCEDURE IF EXISTS `SP_SmallSheetFrame_Update`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_SmallSheetFrame_Update`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN sLogo MEDIUMTEXT,
	IN iCountOfBlankLineAtBottom INT,
	IN sDelimiterToRepeat VARCHAR(1)
	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		IF EXISTS (SELECT 1 FROM t_smallsheetframe WHERE F_ID = iID) THEN 
		   	UPDATE t_smallsheetframe SET F_Logo = sLogo, F_CountOfBlankLineAtBottom = iCountOfBlankLineAtBottom, F_DelimiterToRepeat = sDelimiterToRepeat, F_UpdateDatetime = now() WHERE F_ID = iID;
		   	
		   	SELECT F_ID, F_Logo, F_CountOfBlankLineAtBottom, F_DelimiterToRepeat, F_CreateDatetime, F_UpdateDatetime FROM t_smallsheetframe WHERE F_ID = iID;
		
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		ELSE 
			SET iErrorCode := 2;
			SET sErrorMsg := '该小票格式不存在！';
		END IF;
	
	COMMIT;
END;