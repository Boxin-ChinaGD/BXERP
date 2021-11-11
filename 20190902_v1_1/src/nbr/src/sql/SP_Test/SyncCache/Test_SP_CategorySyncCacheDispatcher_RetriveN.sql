SELECT '++++++++++++++++++ Test_SP_CategorySyncCacheDispatcher_RetriveN.sql ++++++++++++++++++++';

INSERT INTO t_categorysynccache (F_SyncData_ID, F_SyncType, F_SyncSequence, F_CreateDatetime)
VALUES (1, 'C', 0, NOW());

INSERT INTO t_categorysynccachedispatcher (F_SyncCacheID, F_POS_ID, F_CreateDatetime)
VALUES (LAST_INSERT_ID(), 1, NOW());

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iTotalRecord = 0;
CALL SP_CategorySyncCacheDispatcher_RetriveN(@iErrorCode, @sErrorMsg, @iTotalRecord);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord > 0, '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'Case1 Testing Result';

DELETE FROM t_categorysynccachedispatcher;
DELETE FROM t_categorysynccache;