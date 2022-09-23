SELECT '++++++++++++++++++ Test_SP_CommoditySyncCache_Delete.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: POS����ûȫ��ͬ��,�޷�ɾ�� -------------------------' AS 'Case1';

INSERT INTO T_CommoditySyncCache (F_SyncData_ID, F_SyncSequence, F_SyncType)
VALUES (3, 1, "U");
SET @cscdID = LAST_INSERT_ID();
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iSyncData_ID = 3;

INSERT INTO T_CommoditySyncCacheDispatcher (F_SyncCacheID, F_POS_ID)
VALUES (@cscdID, 1);

CALL SP_CommoditySyncCache_Delete(@iErrorCode, @sErrorMsg, @iSyncData_ID);

SELECT @sErrorMsg;
SELECT 1 FROM T_CommoditySyncCacheDispatcher WHERE F_SyncCacheID = @iSyncData_ID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

INSERT INTO T_CommoditySyncCacheDispatcher (F_SyncCacheID, F_POS_ID) 
VALUES (@cscdID, 2);
INSERT INTO T_CommoditySyncCacheDispatcher (F_SyncCacheID, F_POS_ID) 
VALUES (@cscdID, 3);
INSERT INTO T_CommoditySyncCacheDispatcher (F_SyncCacheID, F_POS_ID) 
VALUES (@cscdID, 4);
INSERT INTO T_CommoditySyncCacheDispatcher (F_SyncCacheID, F_POS_ID) 
VALUES (@cscdID, 5);
INSERT INTO T_CommoditySyncCacheDispatcher (F_SyncCacheID, F_POS_ID) 
VALUES (@cscdID, 7);
INSERT INTO T_CommoditySyncCacheDispatcher (F_SyncCacheID, F_POS_ID) 
VALUES (@cscdID, 8);
INSERT INTO T_CommoditySyncCacheDispatcher (F_SyncCacheID, F_POS_ID) 
VALUES (@cscdID, 9);


SELECT '-------------------- Case2: POS���Ѿ�ȫ��ͬ��,ɾ������ʹӱ� -------------------------' AS 'Case2';
SET @sErrorMsg = '';
CALL SP_CommoditySyncCache_Delete(@iErrorCode, @sErrorMsg, @iSyncData_ID);

SELECT @sErrorMsg;
SELECT 1 FROM T_CommoditySyncCacheDispatcher WHERE F_SyncCacheID = @iSyncData_ID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';