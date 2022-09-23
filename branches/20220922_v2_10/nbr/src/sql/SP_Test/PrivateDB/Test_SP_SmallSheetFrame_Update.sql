SELECT '++++++++++++++++++ Test_SP_SmallSheetFrame_Update.sql ++++++++++++++++++++';


SELECT '-------------------- Case1:�������� -------------------------' AS 'Case1';

INSERT INTO t_smallsheetframe (F_Logo) VALUES ('hhhhhhhhhhhhhhhhhhhhh');
SET @iID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sLogo = 'xxxxxxxxxxxxxsf';
SET @iCountOfBlankLineAtBottom = 1;
SET @sDelimiterToRepeat = 'һ';
CALL SP_SmallSheetFrame_Update(@iErrorCode, @sErrorMsg, @iID, @sLogo, @iCountOfBlankLineAtBottom, @sDelimiterToRepeat);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_smallsheetframe WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';
DELETE FROM t_smallsheetframe WHERE F_ID = @iID;

SELECT '-------------------- Case2:���²����ڵ�ID -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @ID = 10000000;
SET @sLogo = 'xxxxxxxxxxxxxsf';
SET @iCountOfBlankLineAtBottom = 1;
SET @sDelimiterToRepeat = 'һ';
CALL SP_SmallSheetFrame_Update(@iErrorCode, @sErrorMsg, @iID, @sLogo, @iCountOfBlankLineAtBottom, @sDelimiterToRepeat);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_smallsheetframe WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 2, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';