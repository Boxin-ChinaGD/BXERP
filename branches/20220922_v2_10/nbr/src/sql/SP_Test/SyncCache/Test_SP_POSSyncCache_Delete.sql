SELECT '++++++++++++++++++Test_SP_POSSyncCache_Delete.sql+++++++++++++++++++++++';

SELECT '-----------------CASE1: POS����ûȫ��ͬ��,�޷�ɾ��------------------' AS 'CASE1';
SET @sErrorMsg = '';
SET @posCount = (SELECT COUNT(1) FROM t_pos WHERE F_Status = 0);

INSERT INTO t_possynccache (F_SyncData_ID, F_SyncSequence, F_SyncType)
VALUES (3, 1, "U");
SET @cscdID = LAST_INSERT_ID();
SET @iErrorCode = 0;
SET @iSyncData_ID = 3;

INSERT INTO t_possynccachedispatcher (F_SyncCacheID, F_POS_ID)
VALUES (@cscdID, 1);

CALL SP_POSSyncCache_Delete(@iErrorCode, @sErrorMsg, @iSyncData_ID);
SELECT @sErrorMsg;
SELECT 1 FROM t_possynccache WHERE F_ID = @cscdID;
SELECT IF(@iErrorCode = 7 AND FOUND_ROWS() = 1 AND @posCount > 1 , '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

SELECT '-----------------CASE2: POS���Ѿ�ȫ��ͬ��,ɾ������ʹӱ�------------------' AS 'CASE2';
SET @sErrorMsg = '';

INSERT INTO t_possynccachedispatcher (F_SyncCacheID, F_POS_ID)
VALUES (@cscdID, 2);
INSERT INTO t_possynccachedispatcher (F_SyncCacheID, F_POS_ID)
VALUES (@cscdID, 3);
INSERT INTO t_possynccachedispatcher (F_SyncCacheID, F_POS_ID)
VALUES (@cscdID, 4);
INSERT INTO t_possynccachedispatcher (F_SyncCacheID, F_POS_ID)
VALUES (@cscdID, 5);
INSERT INTO t_possynccachedispatcher (F_SyncCacheID, F_POS_ID)
VALUES (@cscdID, 7);
INSERT INTO t_possynccachedispatcher (F_SyncCacheID, F_POS_ID)
VALUES (@cscdID, 8);
INSERT INTO t_possynccachedispatcher (F_SyncCacheID, F_POS_ID)
VALUES (@cscdID, 9);

CALL SP_POSSyncCache_Delete(@iErrorCode, @sErrorMsg, @iSyncData_ID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_possynccache sc, t_possynccachedispatcher scd WHERE sc.F_ID = @cscdID OR scd.F_SyncCacheID = @cscdID;
SELECT IF(@iErrorCode = 0 AND FOUND_ROWS() = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';