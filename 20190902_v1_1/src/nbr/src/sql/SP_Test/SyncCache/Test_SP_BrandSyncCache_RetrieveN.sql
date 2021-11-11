SELECT '++++++++++++++++++Test_SP_BrandSyncCache_RetrieveN.sql+++++++++++++++++++++++';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iTotalRecord = 0;
CALL SP_brandSyncCache_RetrieveN(@iErrorCode, @sErrorMsg, @iTotalRecord);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'Case1 Testing Result';