SELECT '++++++++++++++++++Test_SP_BarcodesSyncCache_RetrieveN.sql+++++++++++++++++++++++';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iTotalRecord = 0;
CALL SP_BarcodesSyncCache_RetrieveN(@iErrorCode, @sErrorMsg, @iTotalRecord);

SELECT @sErrorMsg;
SELECT @iTotalRecord;

SELECT IF(@iErrorCode = 0, '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'Case1 Testing Result';