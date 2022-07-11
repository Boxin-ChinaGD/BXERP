SELECT '++++++++++++++++++Test_SP_PromotionSyncCache_Delete.sql+++++++++++++++++++++++';

SELECT '-----------------Case1: POS机还没全部同步,无法删除------------------' AS 'Case1';

INSERT INTO t_Promotionsynccache (F_SyncData_ID, F_SyncSequence, F_SyncType)
VALUES (3, 1, "U");
SET @cscdID = LAST_INSERT_ID();
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iSyncData_ID = 3;

INSERT INTO t_Promotionsynccachedispatcher (F_SyncCacheID, F_POS_ID)
VALUES (@cscdID, 1);

CALL SP_PromotionSyncCache_Delete(@iErrorCode, @sErrorMsg, @iSyncData_ID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PromotionSyncCache WHERE F_SyncData_ID = @iSyncData_ID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case1 Testing Result';

INSERT INTO t_Promotionsynccachedispatcher (F_SyncCacheID, F_POS_ID)
VALUES (@cscdID, 2);
INSERT INTO t_Promotionsynccachedispatcher (F_SyncCacheID, F_POS_ID)
VALUES (@cscdID, 3);
INSERT INTO t_Promotionsynccachedispatcher (F_SyncCacheID, F_POS_ID)
VALUES (@cscdID, 4);
INSERT INTO t_Promotionsynccachedispatcher (F_SyncCacheID, F_POS_ID)
VALUES (@cscdID, 5);
INSERT INTO t_Promotionsynccachedispatcher (F_SyncCacheID, F_POS_ID)
VALUES (@cscdID, 7);
INSERT INTO t_Promotionsynccachedispatcher (F_SyncCacheID, F_POS_ID)
VALUES (@cscdID, 8);
INSERT INTO t_Promotionsynccachedispatcher (F_SyncCacheID, F_POS_ID)
VALUES (@cscdID, 9);

SELECT '-----------------Case2: POS机已经全部同步,删除主表和从表------------------' AS 'Case2';
SET @sErrorMsg = '';
CALL SP_PromotionSyncCache_Delete(@iErrorCode, @sErrorMsg, @iSyncData_ID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PromotionSyncCache WHERE F_SyncData_ID = @iSyncData_ID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';