SELECT '++++++++++++++++++Test_SP_CategorySyncCache_Delete.sql+++++++++++++++++++++++';

SELECT '-----------------Case1: POS机还没全部同步,无法删除------------------' AS 'Case1';

SET @posCount = (SELECT COUNT(1) FROM t_pos WHERE F_Status = 0);

INSERT INTO t_categorysynccache (F_SyncData_ID, F_SyncSequence, F_SyncType)
VALUES (3, 1, "U");
SET @cscdID = LAST_INSERT_ID();
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iSyncData_ID = 3;

INSERT INTO t_categorysynccachedispatcher (F_SyncCacheID, F_POS_ID)
VALUES (@cscdID, 1);

CALL SP_CategorySyncCache_Delete(@iErrorCode, @sErrorMsg, @iSyncData_ID);

SELECT @iErrorCode;
SELECT @sErrorMsg;

SELECT 1 FROM t_categorySyncCache WHERE F_SyncData_ID = @iSyncData_ID;
SELECT IF(@iErrorCode = 7 AND FOUND_ROWS() = 1 AND @posCount > 1, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT '-----------------Case2: POS机已经全部同步,删除主表和从表------------------' AS 'Case2';
SET @sErrorMsg = '';

INSERT INTO t_categorysynccachedispatcher (F_SyncCacheID, F_POS_ID)
VALUES (@cscdID, 2);
INSERT INTO t_categorysynccachedispatcher (F_SyncCacheID, F_POS_ID)
VALUES (@cscdID, 3);
INSERT INTO t_categorysynccachedispatcher (F_SyncCacheID, F_POS_ID)
VALUES (@cscdID, 4);
INSERT INTO t_categorysynccachedispatcher (F_SyncCacheID, F_POS_ID)
VALUES (@cscdID, 5);
INSERT INTO t_categorysynccachedispatcher (F_SyncCacheID, F_POS_ID)
VALUES (@cscdID, 7);
INSERT INTO t_categorysynccachedispatcher (F_SyncCacheID, F_POS_ID)
VALUES (@cscdID, 8);
INSERT INTO t_categorysynccachedispatcher (F_SyncCacheID, F_POS_ID)
VALUES (@cscdID, 9);

CALL SP_CategorySyncCache_Delete(@iErrorCode, @sErrorMsg, @iSyncData_ID);

SELECT @iErrorCode;
SELECT @sErrorMsg;

SELECT 1 FROM t_categorySyncCache sc, t_categorysynccachedispatcher scd WHERE sc.F_ID = @cscdID OR scd.F_SyncCacheID = @cscdID;
SELECT IF(@iErrorCode = 0 AND FOUND_ROWS() = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';