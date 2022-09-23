SELECT '++++++++++++++++++ Test_SP_SmallSheetFrame_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:������� -------------------------' AS 'Case1';

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
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

DELETE FROM t_smallsheetframe WHERE F_ID = LAST_INSERT_ID();

SELECT '-------------------- Case2:�ָ���Ϊ�մ� -------------------------' AS 'Case2';

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
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

DELETE FROM t_smallsheetframe WHERE F_ID = LAST_INSERT_ID();


SELECT '-------------------- Case3:�ָ���Ϊ�ո� -------------------------' AS 'Case3';

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
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';

DELETE FROM t_smallsheetframe WHERE F_ID = LAST_INSERT_ID();

--	������˵�û�еģ�����Ͳ��ò���
--	SELECT '-------------------- Case4:�ָ���Ϊ�ո��-����" -" -------------------------' AS 'Case4';
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
--	SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';
--	
--	DELETE FROM t_smallsheetframe WHERE F_ID = LAST_INSERT_ID();


SELECT '-------------------- Case5:�ָ���Ϊ���ĵ�"һ" -------------------------' AS 'Case5';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sLogo = 'kjSDHfihfuisdbuihfgisdnfoshfoihsdhihsdo;fhsddfiANSfuihASifguHASfpoASJfopi';
SET @iCountOfBlankLineAtBottom = 0;
SET @sDelimiterToRepeat = 'һ';
SET @dtCreateDatetime = now();
CALL SP_SmallSheetFrame_Create(@iErrorCode, @sErrorMsg, @sLogo, @iCountOfBlankLineAtBottom, @sDelimiterToRepeat, @dtCreateDatetime);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_SmallSheetFrame WHERE F_Logo = @sLogo;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';

DELETE FROM t_smallsheetframe WHERE F_ID = LAST_INSERT_ID();

SELECT '-------------------- Case6:�ָ���Ϊnull -------------------------' AS 'Case6';

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
SELECT IF(found_rows() = 0 AND @iErrorCode = 3, '���Գɹ�', '����ʧ��') AS 'Case6 Testing Result'; 

SELECT '-------------------- Case7:�ظ���ӣ���ͬ��createDatetime�� -------------------------' AS 'Case7';

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
SELECT IF(found_rows() = 1 AND @iErrorCode = 1, '���Գɹ�', '����ʧ��') AS 'Case7 Testing Result';

DELETE FROM t_smallsheetframe WHERE F_ID = LAST_INSERT_ID();