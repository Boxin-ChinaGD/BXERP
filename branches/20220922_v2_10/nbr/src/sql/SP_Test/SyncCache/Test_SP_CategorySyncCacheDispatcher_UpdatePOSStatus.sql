SELECT '++++++++++++++++++ Test_SP_CategorySyncCacheDispatcher_UpdatePOSStatus.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:������� -------------------------' AS 'Case1';

INSERT INTO t_categorysynccache (F_SyncData_ID, F_SyncType, F_SyncSequence) VALUES (1,'C',3);
SET @iSyncCacheID = last_insert_id();
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPOS_ID = 3;

CALL SP_CategorySyncCacheDispatcher_UpdatePOSStatus(@iErrorCode, @sErrorMsg, @iSyncCacheID, @iPOS_ID);

SELECT @iErrorCode;
SELECT @sErrorMsg;

SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE1 Testing Result';

SELECT '-------------------- Case2:�ظ���� -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPOS_ID = 3;
CALL SP_CategorySyncCacheDispatcher_UpdatePOSStatus(@iErrorCode, @sErrorMsg, @iSyncCacheID, @iPOS_ID);

SELECT @iErrorCode;
SELECT @sErrorMsg;

SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE2 Testing Result';

DELETE FROM t_categorysynccachedispatcher;
DELETE FROM t_categorysynccache;