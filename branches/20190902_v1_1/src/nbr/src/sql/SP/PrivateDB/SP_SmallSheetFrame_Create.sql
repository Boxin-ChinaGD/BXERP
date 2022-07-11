DROP PROCEDURE IF EXISTS `SP_SmallSheetFrame_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_SmallSheetFrame_Create` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN sLogo MEDIUMTEXT,
	IN iCountOfBlankLineAtBottom INT,
	IN sDelimiterToRepeat VARCHAR(1),
	IN dtCreateDatetime DATETIME
	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		IF EXISTS(SELECT 1 FROM t_smallsheetframe WHERE F_CreateDatetime = dtCreateDatetime) THEN
			SELECT F_ID, F_Logo, F_CountOfBlankLineAtBottom, F_DelimiterToRepeat, F_CreateDatetime,F_UpdateDatetime 
			FROM t_smallsheetframe WHERE F_CreateDatetime = dtCreateDatetime;
	   		
			SET iErrorCode := 1;
			SET sErrorMsg := '该小票格式已经存在';
		ELSE
	
		   	INSERT INTO t_smallsheetframe (F_Logo, F_CountOfBlankLineAtBottom, F_DelimiterToRepeat, F_CreateDatetime) VALUES (sLogo, iCountOfBlankLineAtBottom, sDelimiterToRepeat, dtCreateDatetime);
	   	
	   		SELECT F_ID, F_Logo, F_CountOfBlankLineAtBottom, F_DelimiterToRepeat, F_CreateDatetime, F_UpdateDatetime FROM t_smallsheetframe WHERE F_ID = LAST_INSERT_ID();		
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
	
	COMMIT;
END