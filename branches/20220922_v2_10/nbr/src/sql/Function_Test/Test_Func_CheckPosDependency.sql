SELECT '++++++++++++++++++++++++++++++++++Test_Func_CheckPosDependency.sql+++++++++++++++++++++++++++++++++++++++++';

SELECT '-------------------- Case1:����ɾ��POS-------------------------' AS 'Case1';
INSERT INTO t_pos (F_POS_SN, F_ShopID, F_Salt, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('SN1561891231', 1, 'DFSDGSDKJG546S8FDH', 0, now(), now());
SET @iID = LAST_INSERT_ID();


SET @sErrorMsg = '';
SELECT Func_CheckPosDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

DELETE FROM t_pos WHERE F_ID = @iID;

SELECT '-------------------- Case2:��POS���ѱ�Ա��ʹ�ã�����ɾ��-------------------------' AS 'Case2';
--	INSERT INTO t_pos (F_POS_SN, F_ShopID, F_Salt, F_Status, F_CreateDatetime, F_UpdateDatetime)
--	VALUES ('SN1561891231', 1, 'DFSDGSDKJG546S8FDH', 0, now(), now());
--	SET @iID = LAST_INSERT_ID();
--	
--	INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
--	VALUES ('aa', '12345678911', '123456789123456', '123456', 'asdefggsdjfasgyf', now(), 1, 1, 1, 0, now(), now());
--	SET @iID2 = LAST_INSERT_ID();
--	
--	
--	SET @sErrorMsg = '';
--	SELECT Func_CheckPosDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
--	SELECT IF(@sErrorMsg = '��POS���ѱ�Ա��ʹ�ã�����ɾ��', '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
--	
--	DELETE FROM t_staff WHERE F_ID = @iID2;
--	DELETE FROM t_pos WHERE F_ID = @iID;

SELECT '-------------------- Case3:��POS�������۵�����������ɾ��-------------------------' AS 'Case3';
INSERT INTO t_pos (F_POS_SN, F_ShopID, F_Salt, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('SN1561891231', 1, 'DFSDGSDKJG546S8FDH', 0, now(), now());
SET @iID = LAST_INSERT_ID();

INSERT INTO T_RetailTrade (F_ShopID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_VipID)
VALUES (2,'LS2019090414230000011234', 11,@iID,'url=ashasouuuuunalskd','2017-8-10',5,2,'A123460',1,'˫��777', 5);
SET @iID2 = LAST_INSERT_ID();


SET @sErrorMsg = '';
SELECT Func_CheckPosDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '��POS�������۵�����������ɾ��', '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';

DELETE FROM T_RetailTrade WHERE F_ID = @iID2;
DELETE FROM t_pos WHERE F_ID = @iID;

SELECT '-------------------- Case4:��POS����������������������ɾ��-------------------------' AS 'Case4';
INSERT INTO t_pos (F_POS_SN, F_ShopID, F_Salt, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('SN1561891231', 1, 'DFSDGSDKJG546S8FDH', 0, now(), now());
SET @iID = LAST_INSERT_ID();

INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
VALUES (1, @iID, now(), now(), 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, now());
SET @iID2 = LAST_INSERT_ID();


SET @sErrorMsg = '';
SELECT Func_CheckPosDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '��POS����������������������ɾ��', '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';

DELETE FROM T_RetailTradeAggregation WHERE F_ID = @iID2;
DELETE FROM t_pos WHERE F_ID = @iID;

--	SELECT '-------------------- Case5:��POS���з�������ʹ�õ�ͬ��������������ɾ��-------------------------' AS 'Case5';
--	INSERT INTO t_pos (F_POS_SN, F_ShopID, F_Salt, F_Status, F_CreateDatetime, F_UpdateDatetime)
--	VALUES ('SN1561891231', 1, 'DFSDGSDKJG546S8FDH', 0, now(), now());
--	SET @iID = LAST_INSERT_ID();
--	
--	INSERT INTO t_segmentdownloadsyncserver (F_FileName, F_POS_ID, F_ActionCode, F_ActionDatetime, F_MD5)
--	VALUES ('a', @iID, 0, now(), '');
--	SET @iID2 = LAST_INSERT_ID();

--	
--	SET @sErrorMsg = '';
--	SELECT Func_CheckPosDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
--	SELECT IF(@sErrorMsg = '��POS���з�������ʹ�õ�ͬ��������������ɾ��', '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';
--	
--	DELETE FROM T_SegmentDownloadSyncServer WHERE F_ID = @iID2;
--	DELETE FROM t_pos WHERE F_ID = @iID;

SELECT '-------------------- Case6:��POS���д���ͬ��������ȱ�����������ɾ��-------------------------' AS 'Case6';
INSERT INTO t_pos (F_POS_SN, F_ShopID, F_Salt, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('SN1561891231', 1, 'DFSDGSDKJG546S8FDH', 0, now(), now());
SET @iID = LAST_INSERT_ID();

INSERT INTO t_promotionsynccache (F_SyncData_ID, F_SyncType, F_SyncSequence, F_CreateDatetime)
VALUES (1, 'U', 0, now());
SET @iIDA1 = LAST_INSERT_ID();

INSERT INTO t_promotionsynccachedispatcher (F_SyncCacheID, F_POS_ID, F_CreateDatetime)
VALUES (@iIDA1, @iID, now());
SET @iIDA2 = LAST_INSERT_ID();


SET @sErrorMsg = '';
SELECT Func_CheckPosDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '��POS���д���ͬ��������ȱ�����������ɾ��', '���Գɹ�', '����ʧ��') AS 'Case6 Testing Result';

DELETE FROM T_PromotionSyncCacheDispatcher WHERE F_ID = @iIDA2;
DELETE FROM t_promotionsynccache WHERE F_ID = @iIDA1;
DELETE FROM t_pos WHERE F_ID = @iID;


SELECT '-------------------- Case7:��POS���л�Աͬ��������ȱ�����������ɾ��-------------------------' AS 'Case7';
--	INSERT INTO t_pos (F_POS_SN, F_ShopID, F_Salt, F_Status, F_CreateDatetime, F_UpdateDatetime)
--	VALUES ('SN1561891231', 1, 'DFSDGSDKJG546S8FDH', 0, now(), now());
--	SET @iID = LAST_INSERT_ID();
--	
--	INSERT INTO t_Vipsynccache (F_SyncData_ID, F_SyncType, F_SyncSequence, F_CreateDatetime)
--	VALUES (1, 'U', 0, now());
--	SET @iIDA1 = LAST_INSERT_ID();
--	
--	INSERT INTO T_VipSyncCacheDispatcher (F_SyncCacheID, F_POS_ID, F_CreateDatetime)
--	VALUES (@iIDA1, @iID, now());
--	SET @iIDA2 = LAST_INSERT_ID();
--	
--	
--	SET @sErrorMsg = '';
--	SELECT Func_CheckPosDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
--	SELECT IF(@sErrorMsg = '��POS���л�Աͬ��������ȱ�����������ɾ��', '���Գɹ�', '����ʧ��') AS 'Case7 Testing Result';
--	
--	DELETE FROM T_VipSyncCacheDispatcher WHERE F_ID = @iIDA2;
--	DELETE FROM t_Vipsynccache WHERE F_ID = @iIDA1;
--	DELETE FROM t_pos WHERE F_ID = @iID;

SELECT '-------------------- Case8:��POS���л�Ա���ͬ��������ȱ�����������ɾ��-------------------------' AS 'Case8';
--	INSERT INTO t_pos (F_POS_SN, F_ShopID, F_Salt, F_Status, F_CreateDatetime, F_UpdateDatetime)
--	VALUES ('SN1561891231', 1, 'DFSDGSDKJG546S8FDH', 0, now(), now());
--	SET @iID = LAST_INSERT_ID();
--	
--	INSERT INTO t_VipCategorysynccache (F_SyncData_ID, F_SyncType, F_SyncSequence, F_CreateDatetime)
--	VALUES (1, 'U', 0, now());
--	SET @iIDA1 = LAST_INSERT_ID();
--	
--	INSERT INTO T_VipCategorySyncCacheDispatcher (F_SyncCacheID, F_POS_ID, F_CreateDatetime)
--	VALUES (@iIDA1, @iID, now());
--	SET @iIDA2 = LAST_INSERT_ID();
--	
--	
--	SET @sErrorMsg = '';
--	SELECT Func_CheckPosDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
--	SELECT IF(@sErrorMsg = '��POS���л�Ա���ͬ��������ȱ�����������ɾ��', '���Գɹ�', '����ʧ��') AS 'Case8 Testing Result';
--	
--	DELETE FROM T_VipCategorySyncCacheDispatcher WHERE F_ID = @iIDA2;
--	DELETE FROM t_VipCategorysynccache WHERE F_ID = @iIDA1;
--	DELETE FROM t_pos WHERE F_ID = @iID;

SELECT '-------------------- Case9:��POS������Ʒͬ��������ȱ�����������ɾ��-------------------------' AS 'Case9';
INSERT INTO t_pos (F_POS_SN, F_ShopID, F_Salt, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('SN1561891231', 1, 'DFSDGSDKJG546S8FDH', 0, now(), now());
SET @iID = LAST_INSERT_ID();

INSERT INTO t_Commoditysynccache (F_SyncData_ID, F_SyncType, F_SyncSequence, F_CreateDatetime)
VALUES (1, 'U', 0, now());
SET @iIDA1 = LAST_INSERT_ID();

INSERT INTO T_CommoditySyncCacheDispatcher (F_SyncCacheID, F_POS_ID, F_CreateDatetime)
VALUES (@iIDA1, @iID, now());
SET @iIDA2 = LAST_INSERT_ID();


SET @sErrorMsg = '';
SELECT Func_CheckPosDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '��POS������Ʒͬ��������ȱ�����������ɾ��', '���Գɹ�', '����ʧ��') AS 'Case9 Testing Result';

DELETE FROM T_CommoditySyncCacheDispatcher WHERE F_ID = @iIDA2;
DELETE FROM t_Commoditysynccache WHERE F_ID = @iIDA1;
DELETE FROM t_pos WHERE F_ID = @iID;

SELECT '-------------------- Case11:��POS����Ʒ��ͬ��������ȱ�����������ɾ��-------------------------' AS 'Case11';
INSERT INTO t_pos (F_POS_SN, F_ShopID, F_Salt, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('SN1561891231', 1, 'DFSDGSDKJG546S8FDH', 0, now(), now());
SET @iID = LAST_INSERT_ID();

INSERT INTO t_BrandSynccache (F_SyncData_ID, F_SyncType, F_SyncSequence, F_CreateDatetime)
VALUES (1, 'U', 0, now());
SET @iIDA1 = LAST_INSERT_ID();

INSERT INTO T_BrandSyncCacheDispatcher (F_SyncCacheID, F_POS_ID, F_CreateDatetime)
VALUES (@iIDA1, @iID, now());
SET @iIDA2 = LAST_INSERT_ID();


SET @sErrorMsg = '';
SELECT Func_CheckPosDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '��POS����Ʒ��ͬ��������ȱ�����������ɾ��', '���Գɹ�', '����ʧ��') AS 'Case11 Testing Result';

DELETE FROM T_BrandSyncCacheDispatcher WHERE F_ID = @iIDA2;
DELETE FROM t_BrandSynccache WHERE F_ID = @iIDA1;
DELETE FROM t_pos WHERE F_ID = @iID;


SELECT '-------------------- Case12:��POS����СƱͬ��������ȱ�����������ɾ��-------------------------' AS 'Case12';
--	INSERT INTO t_pos (F_POS_SN, F_ShopID, F_Salt, F_Status, F_CreateDatetime, F_UpdateDatetime)
--	VALUES ('SN1561891231', 1, 'DFSDGSDKJG546S8FDH', 0, now(), now());
--	SET @iID = LAST_INSERT_ID();
--	
--	INSERT INTO t_SmallSheetSynccache (F_SyncData_ID, F_SyncType, F_SyncSequence, F_CreateDatetime)
--	VALUES (1, 'U', 0, now());
--	SET @iIDA1 = LAST_INSERT_ID();
--	
--	INSERT INTO T_SmallSheetSyncCacheDispatcher (F_SyncCacheID, F_POS_ID, F_CreateDatetime)
--	VALUES (@iIDA1, @iID, now());
--	SET @iIDA2 = LAST_INSERT_ID();
--	
--	
--	SET @sErrorMsg = '';
--	SELECT Func_CheckPosDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
--	SELECT IF(@sErrorMsg = '��POS����СƱͬ��������ȱ�����������ɾ��', '���Գɹ�', '����ʧ��') AS 'Case12 Testing Result';
--	
--	DELETE FROM T_SmallSheetSyncCacheDispatcher WHERE F_ID = @iIDA2;
--	DELETE FROM t_SmallSheetSynccache WHERE F_ID = @iIDA1;
--	DELETE FROM t_pos WHERE F_ID = @iID;

SELECT '-------------------- Case13:��POS�������ͬ��������ȱ�����������ɾ��-------------------------' AS 'Case13';
INSERT INTO t_pos (F_POS_SN, F_ShopID, F_Salt, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('SN1561891231', 1, 'DFSDGSDKJG546S8FDH', 0, now(), now());
SET @iID = LAST_INSERT_ID();

INSERT INTO t_CategorySynccache (F_SyncData_ID, F_SyncType, F_SyncSequence, F_CreateDatetime)
VALUES (1, 'U', 0, now());
SET @iIDA1 = LAST_INSERT_ID();

INSERT INTO T_CategorySyncCacheDispatcher (F_SyncCacheID, F_POS_ID, F_CreateDatetime)
VALUES (@iIDA1, @iID, now());
SET @iIDA2 = LAST_INSERT_ID();


SET @sErrorMsg = '';
SELECT Func_CheckPosDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '��POS�������ͬ��������ȱ�����������ɾ��', '���Գɹ�', '����ʧ��') AS 'Case13 Testing Result';

DELETE FROM T_CategorySyncCacheDispatcher WHERE F_ID = @iIDA2;
DELETE FROM t_CategorySynccache WHERE F_ID = @iIDA1;
DELETE FROM t_pos WHERE F_ID = @iID;

SELECT '-------------------- Case14:��POS����������ͬ��������ȱ�����������ɾ��-------------------------' AS 'Case14';
INSERT INTO t_pos (F_POS_SN, F_ShopID, F_Salt, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('SN1561891231', 1, 'DFSDGSDKJG546S8FDH', 0, now(), now());
SET @iID = LAST_INSERT_ID();

INSERT INTO t_BarcodesSynccache (F_SyncData_ID, F_SyncType, F_SyncSequence, F_CreateDatetime)
VALUES (1, 'U', 0, now());
SET @iIDA1 = LAST_INSERT_ID();

INSERT INTO T_BarcodesSyncCacheDispatcher (F_SyncCacheID, F_POS_ID, F_CreateDatetime)
VALUES (@iIDA1, @iID, now());
SET @iIDA2 = LAST_INSERT_ID();


SET @sErrorMsg = '';
SELECT Func_CheckPosDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '��POS����������ͬ��������ȱ�����������ɾ��', '���Գɹ�', '����ʧ��') AS 'Case14 Testing Result';

DELETE FROM T_BarcodesSyncCacheDispatcher WHERE F_ID = @iIDA2;
DELETE FROM t_BarcodesSynccache WHERE F_ID = @iIDA1;
DELETE FROM t_pos WHERE F_ID = @iID;