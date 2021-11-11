SELECT '++++++++++++++++++Test_SP_PromotionSyncCacheDispatcher_RetriveN.sql+++++++++++++++++++++++';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iTotalRecord = 0;
CALL SP_PromotionSyncCacheDispatcher_RetriveN(@iErrorCode, @sErrorMsg, @iTotalRecord);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = 0, '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'Case1 Testing Result';