SELECT '++++++++++++++++++Test_SP_BarcodesSyncCache_POSUpload.sql+++++++++++++++++++++++';

SELECT '-----------------Case1: ������ӣ�C�ͣ�iPOSID > 0 ------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iSyncData_ID = 5;
SET @cSyncType = "C";
SET @iPOSID = 1;

CALL SP_BarcodesSyncCache_POSUpload(@iErrorCode, @sErrorMsg, @iSyncData_ID, 1, @cSyncType, @iPOSID);

SELECT @sErrorMsg;
SELECT 1 FROM t_barcodessynccache WHERE F_SyncData_ID = @iSyncData_ID AND F_SyncType = @cSyncType;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case 1 Testing Result';
SELECT 1 FROM t_barcodessynccachedispatcher WHERE F_POS_ID = @iPOSID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case 1 Testing Result';

SELECT '-----------------Case2: �ظ���ӣ�C�ͣ�iPOSID > 0 ------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iSyncData_ID = 5;
SET @cSyncType = "C";
SET @iPOSID = 1;

CALL SP_BarcodesSyncCache_POSUpload(@iErrorCode, @sErrorMsg, @iSyncData_ID, 1, @cSyncType, @iPOSID);

SELECT @sErrorMsg;
SELECT 1 FROM t_barcodessynccache WHERE F_SyncData_ID = @iSyncData_ID AND F_SyncType = @cSyncType;
SELECT IF(found_rows() = 2 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case 2 Testing Result';
SELECT 1 FROM t_barcodessynccachedispatcher WHERE F_POS_ID = @iPOSID;
SELECT IF(found_rows() = 2 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case 2 Testing Result';

SELECT '-----------------Case3: ������ӣ�D�ͣ�iPOSID > 0 ------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iSyncData_ID = 5;
SET @cSyncType = "D";
SET @iPOSID = 2;

CALL SP_BarcodesSyncCache_POSUpload(@iErrorCode, @sErrorMsg, @iSyncData_ID, 1, @cSyncType, @iPOSID);

SELECT @sErrorMsg;
SELECT 1 FROM t_barcodessynccache WHERE F_SyncData_ID = @iSyncData_ID AND F_SyncType = @cSyncType;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case 3 Testing Result';
SELECT 1 FROM t_barcodessynccachedispatcher WHERE F_POS_ID = @iPOSID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case 3 Testing Result';

SELECT '-----------------Case4: �ظ���ӣ�D�ͣ�iPOSID > 0 ------------------' AS 'Case4';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iSyncData_ID = 5;
SET @cSyncType = "D";
SET @iPOSID = 2;

CALL SP_BarcodesSyncCache_POSUpload(@iErrorCode, @sErrorMsg, @iSyncData_ID, 1, @cSyncType, @iPOSID);

SELECT @sErrorMsg;
SELECT 1 FROM t_barcodessynccache WHERE F_SyncData_ID = @iSyncData_ID AND F_SyncType = @cSyncType;
SELECT IF(found_rows() = 2 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case 4 Testing Result';
SELECT 1 FROM t_barcodessynccachedispatcher WHERE F_POS_ID = @iPOSID;
SELECT IF(found_rows() = 2 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case 4 Testing Result';

SELECT '-----------------Case5: ������ӣ�U�ͣ�iPOSID > 0 ------------------' AS 'Case5';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iSyncData_ID = 5;
SET @cSyncType = "U";
SET @iPOSID = 3;

CALL SP_BarcodesSyncCache_POSUpload(@iErrorCode, @sErrorMsg, @iSyncData_ID, 1, @cSyncType, @iPOSID);

SELECT @sErrorMsg;
SELECT 1 FROM t_barcodessynccache WHERE F_SyncData_ID = @iSyncData_ID AND F_SyncType = @cSyncType;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case 5 Testing Result';
SELECT 1 FROM t_barcodessynccachedispatcher WHERE F_POS_ID = @iPOSID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case 5 Testing Result';

SELECT '-----------------Case6: �ظ���ӣ�U�ͣ�iPOSID > 0 ------------------' AS 'Case6';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iSyncData_ID = 5;
SET @cSyncType = "U";
SET @iPOSID = 3;

CALL SP_BarcodesSyncCache_POSUpload(@iErrorCode, @sErrorMsg, @iSyncData_ID, 1, @cSyncType, @iPOSID);

SELECT @sErrorMsg;
SELECT 1 FROM t_barcodessynccache WHERE F_SyncData_ID = @iSyncData_ID AND F_SyncType = @cSyncType;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case 6 Testing Result';
SELECT 1 FROM t_barcodessynccachedispatcher WHERE F_POS_ID = @iPOSID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case 6 Testing Result';

SELECT '-----------------Case7: ������ӣ�C�ͣ�iPOSID <= 0 ------------------' AS 'Case7';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iSyncData_ID = 4;
SET @cSyncType = "C";
SET @iPOSID = 0;

CALL SP_BarcodesSyncCache_POSUpload(@iErrorCode, @sErrorMsg, @iSyncData_ID, 1, @cSyncType, @iPOSID);

SELECT @sErrorMsg;
SELECT 1 FROM t_barcodessynccache WHERE F_SyncData_ID = @iSyncData_ID AND F_SyncType = @cSyncType;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case 7 Testing Result';
SELECT 1 FROM t_barcodessynccachedispatcher WHERE F_POS_ID = @iPOSID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case 7 Testing Result';

SELECT '-----------------Case8: �ظ���ӣ�C�ͣ�iPOSID <= 0 ------------------' AS 'Case8';
-- Case8:�ظ���ӣ�C�ͣ�iPOSID <= 0
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iSyncData_ID = 4;
SET @cSyncType = "C";
SET @iPOSID = 0;

CALL SP_BarcodesSyncCache_POSUpload(@iErrorCode, @sErrorMsg, @iSyncData_ID, 1, @cSyncType, @iPOSID);

SELECT @sErrorMsg;
SELECT 1 FROM t_barcodessynccache WHERE F_SyncData_ID = @iSyncData_ID AND F_SyncType = @cSyncType;
SELECT IF(found_rows() = 2 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case 8 Testing Result';
SELECT 1 FROM t_barcodessynccachedispatcher WHERE F_SyncCacheID = last_insert_id() AND F_POS_ID = @iPOSID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case 8 Testing Result';

SELECT '-----------------Case9: ������ӣ�D�ͣ�iPOSID <= 0 ------------------' AS 'Case9';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iSyncData_ID = 4;
SET @cSyncType = "D";
SET @iPOSID = -1;

CALL SP_BarcodesSyncCache_POSUpload(@iErrorCode, @sErrorMsg, @iSyncData_ID, 1, @cSyncType, @iPOSID);

SELECT @sErrorMsg;
SELECT 1 FROM t_barcodessynccache WHERE F_SyncData_ID = @iSyncData_ID AND F_SyncType = @cSyncType;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case 9 Testing Result';
SELECT 1 FROM t_barcodessynccachedispatcher WHERE F_SyncCacheID = last_insert_id() AND F_POS_ID = @iPOSID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case 9 Testing Result';

SELECT '-----------------Case10: �ظ���ӣ�D�ͣ�iPOSID <= 0 ------------------' AS 'Case10';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iSyncData_ID = 4;
SET @cSyncType = "D";
SET @iPOSID = -1;

CALL SP_BarcodesSyncCache_POSUpload(@iErrorCode, @sErrorMsg, @iSyncData_ID, 1, @cSyncType, @iPOSID);

SELECT @sErrorMsg;
SELECT 1 FROM t_barcodessynccache WHERE F_SyncData_ID = @iSyncData_ID AND F_SyncType = @cSyncType;
SELECT IF(found_rows() = 2 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case 10 Testing Result';
SELECT 1 FROM t_barcodessynccachedispatcher WHERE F_SyncCacheID = last_insert_id() AND F_POS_ID = @iPOSID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case 10 Testing Result';

SELECT '-----------------Case11: ������ӣ�U�ͣ�iPOSID <= 0 ------------------' AS 'Case11';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iSyncData_ID = 5;
SET @cSyncType = "U";
SET @iPOSID = -1;

CALL SP_BarcodesSyncCache_POSUpload(@iErrorCode, @sErrorMsg, @iSyncData_ID, 1, @cSyncType, @iPOSID);

SELECT @sErrorMsg;
SELECT 1 FROM t_barcodessynccache WHERE F_SyncData_ID = @iSyncData_ID AND F_SyncType = @cSyncType;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case 11 Testing Result';
SELECT 1 FROM t_barcodessynccachedispatcher WHERE F_SyncCacheID = last_insert_id() AND F_POS_ID = @iPOSID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case  11 Testing Result';

SELECT '-----------------Case12: �ظ���ӣ�U�ͣ�iPOSID <= 0 ------------------' AS 'Case12';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iSyncData_ID = 5;
SET @cSyncType = "U";
SET @iPOSID = -1;

CALL SP_BarcodesSyncCache_POSUpload(@iErrorCode, @sErrorMsg, @iSyncData_ID, 1, @cSyncType, @iPOSID);

SELECT @sErrorMsg;
SELECT 1 FROM t_barcodessynccache WHERE F_SyncData_ID = @iSyncData_ID AND F_SyncType = @cSyncType;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case 12 Testing Result';
SELECT 1 FROM t_barcodessynccachedispatcher WHERE F_SyncCacheID = last_insert_id() AND F_POS_ID = @iPOSID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case 12 Testing Result'; 

DELETE FROM t_barcodessynccachedispatcher;
DELETE FROM t_barcodessynccache;