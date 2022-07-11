SELECT '++++++++++++++++++ Test_SP_CommoditySyncCache_DeleteAll.sql ++++++++++++++++++++';

SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_CommoditySyncCache_DeleteAll(@iErrorCode ,@sErrorMsg);

SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'CASE1 Testing Result';