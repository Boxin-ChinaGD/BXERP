SELECT '++++++++++++++++++ Test_SP_SmallSheetText_Create.sql ++++++++++++++++++++';
-- CASE1:正常添加
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sContent = '支付宝支付：';
SET @iSize = 12;
SET @iBold = 0;
SET @iGravity = 153;
SET @iFrameID = 2;

CALL SP_SmallSheetText_Create(@iErrorCode, @sErrorMsg, @sContent, @iSize, @iBold, @iGravity, @iFrameID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_SmallSheetText WHERE F_Content = @sContent AND F_Size = @iSize AND F_Bold = @iBold AND F_Gravity = @iGravity AND F_FrameID = @iFrameID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';
DELETE FROM t_smallsheettext WHERE F_ID = LAST_INSERT_ID();