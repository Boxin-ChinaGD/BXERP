SELECT '++++++++++++++++++ Test_SP_SmallSheetText_Update.sql ++++++++++++++++++++';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 2;
SET @sContent = '111111';
SET @fSize = 25;
SET @iBold = 1;
SET @iGravity = 15;
SET @iFrameID = 1;

CALL SP_SmallSheetText_Update(@iErrorCode, @sErrorMsg, @iID, @sContent, @fSize, @iBold, @iGravity, @iFrameID);

SELECT @sErrorMsg;
SELECT 1 FROM t_SmallSheetText WHERE F_ID = @iID AND F_Content = @sContent AND F_Size = @fSize AND F_Bold = @iBold AND F_Gravity = @iGravity AND F_FrameID = @iFrameID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'Testing Result';