SELECT '++++++++++++++++++ Test_SP_SmallSheetFrame_Retrieve1.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:正常查询 -------------------------' AS 'Case1';

INSERT INTO t_smallsheetframe (F_Logo)
VALUES ('logo');

SET @iID = LAST_INSERT_ID();

INSERT INTO t_smallsheettext (F_Content, F_Size, F_Bold, F_Gravity, F_FrameID)
VALUES ('小票内容',12,1,127,@iID);

SET @iSmallSheetFrameID = LAST_INSERT_ID(); 
SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_SmallSheetFrame_Retrieve1(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT 1 FROM t_SmallSheetFrame WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

DELETE FROM t_smallsheettext WHERE F_ID = @iSmallSheetFrameID;
DELETE FROM t_smallsheetframe WHERE F_ID = @iID;
 