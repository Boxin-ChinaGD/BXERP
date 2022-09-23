SELECT '++++++++++++++++++ Test_SP_SmallSheetFrame_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:正常添加 -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sLogo = 'kjSDHfihfuisdbuihfgisdnfoshfoihsdhihsdo;fhsddfiANSfuihASifguHASfpoASJfxxx';
SET @iCountOfBlankLineAtBottom = 0;
SET @dtCreateDatetime = now();
SET @sDelimiterToRepeat = '-';
CALL SP_SmallSheetFrame_Create(@iErrorCode, @sErrorMsg, @sLogo, @iCountOfBlankLineAtBottom, @sDelimiterToRepeat, @dtCreateDatetime);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_SmallSheetFrame WHERE F_Logo = @sLogo;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

DELETE FROM t_smallsheetframe WHERE F_ID = LAST_INSERT_ID();

SELECT '-------------------- Case2:分隔符为空串 -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sLogo = 'kjSDHfihfuisdbuihfgisdnfoshfoihsdhihsdo;fhsddfiANSfuihASifguHASfpoASJfopi';
SET @iCountOfBlankLineAtBottom = 0;
SET @sDelimiterToRepeat = '';
SET @dtCreateDatetime = now();
CALL SP_SmallSheetFrame_Create(@iErrorCode, @sErrorMsg, @sLogo, @iCountOfBlankLineAtBottom, @sDelimiterToRepeat, @dtCreateDatetime);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_SmallSheetFrame WHERE F_Logo = @sLogo;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';

DELETE FROM t_smallsheetframe WHERE F_ID = LAST_INSERT_ID();


SELECT '-------------------- Case3:分隔符为空格 -------------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sLogo = 'kjSDHfihfuisdbuihfgisdnfoshfoihsdhihsdo;fhsddfiANSfuihASifguHASfpoASJfopi';
SET @iCountOfBlankLineAtBottom = 0;
SET @sDelimiterToRepeat = ' ';
SET @dtCreateDatetime = now();
CALL SP_SmallSheetFrame_Create(@iErrorCode, @sErrorMsg, @sLogo, @iCountOfBlankLineAtBottom, @sDelimiterToRepeat, @dtCreateDatetime);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_SmallSheetFrame WHERE F_Logo = @sLogo;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case3 Testing Result';

DELETE FROM t_smallsheetframe WHERE F_ID = LAST_INSERT_ID();

--	插入多了当没有的，这个就不用测了
--	SELECT '-------------------- Case4:分隔符为空格加-，即" -" -------------------------' AS 'Case4';
--	
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @sLogo = 'kjSDHfihfuisdbuihfgisdnfoshfoihsdhihsdo;fhsddfiANSfuihASifguHASfpoASJfopi';
--	SET @iCountOfBlankLineAtBottom = 0;
--	SET @sDelimiterToRepeat = ' -';
--	CALL SP_SmallSheetFrame_Create(@iErrorCode, @sErrorMsg, @sLogo, @iCountOfBlankLineAtBottom, @sDelimiterToRepeat);
--	SELECT @iErrorCode;
--	SELECT @sErrorMsg;
--	SELECT 1 FROM t_SmallSheetFrame WHERE F_Logo = @sLogo;
--	SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case4 Testing Result';
--	
--	DELETE FROM t_smallsheetframe WHERE F_ID = LAST_INSERT_ID();


SELECT '-------------------- Case5:分隔符为中文的"一" -------------------------' AS 'Case5';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sLogo = 'kjSDHfihfuisdbuihfgisdnfoshfoihsdhihsdo;fhsddfiANSfuihASifguHASfpoASJfopi';
SET @iCountOfBlankLineAtBottom = 0;
SET @sDelimiterToRepeat = '一';
SET @dtCreateDatetime = now();
CALL SP_SmallSheetFrame_Create(@iErrorCode, @sErrorMsg, @sLogo, @iCountOfBlankLineAtBottom, @sDelimiterToRepeat, @dtCreateDatetime);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_SmallSheetFrame WHERE F_Logo = @sLogo;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case5 Testing Result';

DELETE FROM t_smallsheetframe WHERE F_ID = LAST_INSERT_ID();

SELECT '-------------------- Case6:分隔符为null -------------------------' AS 'Case6';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sLogo = 'kjSDHfihfuisdbuihfgisdnfoshfoihsdhihsdo;fhsddfiANSfuihASifguHASfpoASJfopi';
SET @iCountOfBlankLineAtBottom = 0;
SET @sDelimiterToRepeat = NULL;
SET @dtCreateDatetime = now();
CALL SP_SmallSheetFrame_Create(@iErrorCode, @sErrorMsg, @sLogo, @iCountOfBlankLineAtBottom, @sDelimiterToRepeat, @dtCreateDatetime);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_SmallSheetFrame WHERE F_Logo = @sLogo;
SELECT IF(found_rows() = 0 AND @iErrorCode = 3, '测试成功', '测试失败') AS 'Case6 Testing Result'; 

SELECT '-------------------- Case7:重复添加（相同的createDatetime） -------------------------' AS 'Case7';

SET @dtCreateDatetime = now();
INSERT INTO t_smallsheetframe (F_Logo, F_CountOfBlankLineAtBottom, F_UpdateDatetime) 
VALUES ('kjSDHfihfuisdbuihfgisdnfoshfoihsdhihsdo;fhsddfiANSfuihASifguHASfpoASJfopi', 0, @dtCreateDatetime);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sLogo = 'kjSDHfihfuisdbuihfgisdnfoshfoihsdhihsdo;fhsddfiANSfuihASifguHASfpoASJfopi';
SET @iCountOfBlankLineAtBottom = 0;
CALL SP_SmallSheetFrame_Create(@iErrorCode, @sErrorMsg, @sLogo, @iCountOfBlankLineAtBottom, @sDelimiterToRepeat, @dtCreateDatetime);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 1, '测试成功', '测试失败') AS 'Case7 Testing Result';

DELETE FROM t_smallsheetframe WHERE F_ID = LAST_INSERT_ID();