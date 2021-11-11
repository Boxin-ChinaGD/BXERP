SELECT '++++++++++++++++++Test_SP_BrandSyncCacheDispatcher_UpdatePOSStatus.sql+++++++++++++++++++++++';

SELECT '-----------------Case1: 正常添加------------------' AS 'Case1';

INSERT INTO t_brandsynccache (F_SyncData_ID, F_SyncType, F_SyncSequence) VALUES (1,'C',3);

SET @iSyncCacheID = last_insert_id();
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPOS_ID = 3;

CALL SP_BrandSyncCacheDispatcher_UpdatePOSStatus(@iErrorCode, @sErrorMsg, @iSyncCacheID, @iPOS_ID);

SELECT @iErrorCode;
SELECT @sErrorMsg;

SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT '-----------------Case2: 重复添加------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPOS_ID = 3;
CALL SP_BrandSyncCacheDispatcher_UpdatePOSStatus(@iErrorCode, @sErrorMsg, @iSyncCacheID, @iPOS_ID);

SELECT @iErrorCode;
SELECT @sErrorMsg;

SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';

DELETE FROM t_brandsynccachedispatcher;
DELETE FROM t_brandsynccache;