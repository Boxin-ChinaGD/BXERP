SELECT '++++++++++++++++++ Test_SP_BarcodesSyncCache_Delete.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:POS����ûȫ��ͬ��,�޷�ɾ�� -------------------------' AS 'Case1';

SET @sErrorMsg = '';
SET @posCount = (SELECT COUNT(1) FROM t_pos WHERE F_Status = 0);

INSERT INTO t_BarcodesSyncCache (F_SyncData_ID, F_SyncSequence, F_SyncType)
VALUES (3, 1, "U");
SET @cscdID = LAST_INSERT_ID();
SET @iErrorCode = 0;
SET @iSyncData_ID = 3;

INSERT INTO t_barcodessynccachedispatcher (F_SyncCacheID, F_POS_ID)
VALUES (@cscdID, 1);

CALL SP_BarcodesSyncCache_Delete(@iErrorCode, @sErrorMsg, @iSyncData_ID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_BarcodesSyncCache WHERE F_SyncData_ID = @iSyncData_ID;
SELECT IF(@iErrorCode = 7 AND FOUND_ROWS() = 1 AND @posCount > 1, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

SELECT '-------------------- Case2:POS���Ѿ�ȫ��ͬ��,ɾ������ʹӱ� -------------------------' AS 'Case2';

SET @sErrorMsg = '';
INSERT INTO t_barcodessynccachedispatcher (F_SyncCacheID, F_POS_ID)
VALUES (@cscdID, 2);
INSERT INTO t_barcodessynccachedispatcher (F_SyncCacheID, F_POS_ID)
VALUES (@cscdID, 3);
INSERT INTO t_barcodessynccachedispatcher (F_SyncCacheID, F_POS_ID)
VALUES (@cscdID, 4);
INSERT INTO t_barcodessynccachedispatcher (F_SyncCacheID, F_POS_ID)
VALUES (@cscdID, 5);
INSERT INTO t_barcodessynccachedispatcher (F_SyncCacheID, F_POS_ID)
VALUES (@cscdID, 7);
INSERT INTO t_barcodessynccachedispatcher (F_SyncCacheID, F_POS_ID)
VALUES (@cscdID, 8);
INSERT INTO t_barcodessynccachedispatcher (F_SyncCacheID, F_POS_ID)
VALUES (@cscdID, 9);

CALL SP_BarcodesSyncCache_Delete(@iErrorCode, @sErrorMsg, @iSyncData_ID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_BarcodesSyncCache sc, t_barcodessynccachedispatcher scd WHERE sc.F_ID = @cscdID OR scd.F_SyncCacheID = @cscdID;
SELECT IF(@iErrorCode = 0 AND FOUND_ROWS() = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

DELETE FROM t_barcodessynccachedispatcher;
DELETE FROM t_barcodessynccache;