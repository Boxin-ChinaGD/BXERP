SELECT '++++++++++++++++++ Test_SP_SmallSheetText_RetrieveN.sql ++++++++++++++++++++';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iFrameID = 2;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_SmallSheetText_RetrieveN(@iErrorCode, @sErrorMsg, @iFrameID, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT 1 FROM t_SmallSheetText WHERE F_FrameID = @iFrameID;
SELECT IF(found_rows() = 20 AND @iErrorCode = 0, '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'Testing Result';