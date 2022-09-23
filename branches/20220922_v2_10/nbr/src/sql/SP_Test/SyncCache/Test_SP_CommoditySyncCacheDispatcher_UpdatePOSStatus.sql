SELECT '++++++++++++++++++ Test_SP_CommoditySyncCacheDispatcher_UpdatePOSStatus.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: ’˝≥£ÃÌº” -------------------------' AS 'Case1';

INSERT INTO t_commoditysyncCache (F_SyncData_ID, F_SyncType, F_SyncSequence) VALUES (1,'C',3);
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iSyncCacheID = last_insert_id();
SET @iPOS_ID = 3;

CALL SP_commoditySyncCacheDispatcher_UpdatePOSStatus(@iErrorCode, @sErrorMsg, @iSyncCacheID, @iPOS_ID);

SELECT @sErrorMsg;
SELECT 1 FROM t_commoditySyncCacheDispatcher WHERE F_SyncCacheID = @iSyncCacheID AND F_POS_ID = @iPOS_ID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'Case1 Testing Result';

SELECT '-------------------- Case2: ÷ÿ∏¥ÃÌº” -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPOS_ID = 3;
CALL SP_commoditySyncCacheDispatcher_UpdatePOSStatus(@iErrorCode, @sErrorMsg, @iSyncCacheID, @iPOS_ID);

SELECT @sErrorMsg;
SELECT 1 FROM t_commoditySyncCacheDispatcher WHERE F_SyncCacheID = @iSyncCacheID AND F_POS_ID = @iPOS_ID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'Case2 Testing Result';

DELETE FROM t_commoditysynccachedispatcher;
DELETE FROM t_commoditysyncCache;