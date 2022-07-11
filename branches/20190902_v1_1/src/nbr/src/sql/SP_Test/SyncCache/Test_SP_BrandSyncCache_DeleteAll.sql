
SELECT '++++++++++++++++++Test_SP_BrandSyncCache_DeleteAll.sql+++++++++++++++++++++++';

SELECT '-----------------Case1: ɾ��ȫ����Ʒ�Ƶ�ͬ���黺��------------------' AS 'Case1';

INSERT INTO t_brandsynccache (F_SyncData_ID, F_SyncSequence, F_SyncType)
VALUES (3, 1, "U");
INSERT INTO t_brandsynccache (F_SyncData_ID, F_SyncSequence, F_SyncType)
VALUES (4, 1, "U");
INSERT INTO t_brandsynccache (F_SyncData_ID, F_SyncSequence, F_SyncType)
VALUES (5, 1, "U");

SET @cscdID = LAST_INSERT_ID();
SET @iErrorCode = 0;
SET @sErrorMsg = '';

INSERT INTO t_brandsynccachedispatcher (F_SyncCacheID, F_POS_ID)
VALUES (@cscdID, 2);

CALL SP_BrandSyncCache_DeleteAll(@iErrorCode, @sErrorMsg);

SELECT @sErrorMsg;
SELECT 1 FROM t_brandSyncCache WHERE F_SyncData_ID = @iSyncData_ID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';