SELECT '++++++++++++++++++ Test_SP_SmallSheetFrame_Delete.sql ++++++++++++++++++++';
INSERT INTO t_smallsheetframe (F_Logo)
VALUES ('xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx');

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();

CALL SP_SmallSheetFrame_Delete(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT 1 FROM t_smallsheetframe WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'Testing Result';

SELECT 1 FROM t_smallsheettext WHERE F_FrameID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'Testing Result';