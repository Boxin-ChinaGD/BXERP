SELECT '++++++++++++++++++ Test_SP_CommoditySyncCache_RetrieveN.sql ++++++++++++++++++++';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iTotalRecord = 0;
CALL SP_CommoditySyncCache_RetrieveN(@iErrorCode, @sErrorMsg, @iTotalRecord);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'Case1 Testing Result';