SELECT '++++++++++++++++++Test_SP_PromotionSyncCacheDispatcher_UpdatePOSStatus.sql+++++++++++++++++++++++';

SELECT '-----------------Case1: �������------------------' AS 'Case1';

INSERT INTO t_Promotionsynccache (F_SyncData_ID, F_SyncType, F_SyncSequence) VALUES (1,'C',3);

SET @iSyncCacheID = last_insert_id();
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPOS_ID = 3;

CALL SP_PromotionSyncCacheDispatcher_UpdatePOSStatus(@iErrorCode, @sErrorMsg, @iSyncCacheID, @iPOS_ID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

SELECT '-----------------Case2: �ظ����------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPOS_ID = 3;
CALL SP_PromotionSyncCacheDispatcher_UpdatePOSStatus(@iErrorCode, @sErrorMsg, @iSyncCacheID, @iPOS_ID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

DELETE FROM t_Promotionsynccachedispatcher;
DELETE FROM t_Promotionsynccache;