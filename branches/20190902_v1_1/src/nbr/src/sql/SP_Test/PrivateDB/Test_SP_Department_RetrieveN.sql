SELECT '++++++++++++++++++ Test_SP_Department_RetrieveN.sql ++++++++++++++++++++';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Department_RetrieveN(@iErrorCode, @sErrorMsg, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'Case1 Testing Result';