
SELECT '++++++++++++++++++Test_SP_BarcodesSyncCache_DeleteAll.sql+++++++++++++++++++++++';

SELECT '-----------------Case1: 删除全部的条形码的同步块缓存------------------' AS 'Case1';

INSERT INTO t_Barcodessynccache (F_SyncData_ID, F_SyncSequence, F_SyncType)
VALUES (3, 1, "U");
INSERT INTO t_Barcodessynccache (F_SyncData_ID, F_SyncSequence, F_SyncType)
VALUES (4, 1, "U");
INSERT INTO t_Barcodessynccache (F_SyncData_ID, F_SyncSequence, F_SyncType)
VALUES (5, 1, "U");

SET @cscdID = LAST_INSERT_ID();
SET @iErrorCode = 0;
SET @sErrorMsg = '';

INSERT INTO t_Barcodessynccachedispatcher (F_SyncCacheID, F_POS_ID)
VALUES (@cscdID, 2);

CALL SP_BarcodesSyncCache_DeleteAll(@iErrorCode, @sErrorMsg);

SELECT @sErrorMsg;
SELECT 1 FROM t_BarcodesSyncCache WHERE F_SyncData_ID = @iSyncData_ID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';