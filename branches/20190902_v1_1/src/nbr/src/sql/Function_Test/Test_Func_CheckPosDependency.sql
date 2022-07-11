SELECT '++++++++++++++++++++++++++++++++++Test_Func_CheckPosDependency.sql+++++++++++++++++++++++++++++++++++++++++';

SELECT '-------------------- Case1:正常删除POS-------------------------' AS 'Case1';
INSERT INTO t_pos (F_POS_SN, F_ShopID, F_Salt, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('SN1561891231', 1, 'DFSDGSDKJG546S8FDH', 0, now(), now());
SET @iID = LAST_INSERT_ID();


SET @sErrorMsg = '';
SELECT Func_CheckPosDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '', '测试成功', '测试失败') AS 'Case1 Testing Result';

DELETE FROM t_pos WHERE F_ID = @iID;

SELECT '-------------------- Case2:该POS机已被员工使用，不能删除-------------------------' AS 'Case2';
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
--	SELECT IF(@sErrorMsg = '该POS机已被员工使用，不能删除', '测试成功', '测试失败') AS 'Case2 Testing Result';
--	
--	DELETE FROM t_staff WHERE F_ID = @iID2;
--	DELETE FROM t_pos WHERE F_ID = @iID;

SELECT '-------------------- Case3:该POS机有零售单依赖，不能删除-------------------------' AS 'Case3';
INSERT INTO t_pos (F_POS_SN, F_ShopID, F_Salt, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('SN1561891231', 1, 'DFSDGSDKJG546S8FDH', 0, now(), now());
SET @iID = LAST_INSERT_ID();

INSERT INTO T_RetailTrade (F_ShopID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_VipID)
VALUES (2,'LS2019090414230000011234', 11,@iID,'url=ashasouuuuunalskd','2017-8-10',5,2,'A123460',1,'双击777', 5);
SET @iID2 = LAST_INSERT_ID();


SET @sErrorMsg = '';
SELECT Func_CheckPosDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '该POS机有零售单依赖，不能删除', '测试成功', '测试失败') AS 'Case3 Testing Result';

DELETE FROM T_RetailTrade WHERE F_ID = @iID2;
DELETE FROM t_pos WHERE F_ID = @iID;

SELECT '-------------------- Case4:该POS机有收银汇总依赖，不能删除-------------------------' AS 'Case4';
INSERT INTO t_pos (F_POS_SN, F_ShopID, F_Salt, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('SN1561891231', 1, 'DFSDGSDKJG546S8FDH', 0, now(), now());
SET @iID = LAST_INSERT_ID();

INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
VALUES (1, @iID, now(), now(), 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, now());
SET @iID2 = LAST_INSERT_ID();


SET @sErrorMsg = '';
SELECT Func_CheckPosDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '该POS机有收银汇总依赖，不能删除', '测试成功', '测试失败') AS 'Case4 Testing Result';

DELETE FROM T_RetailTradeAggregation WHERE F_ID = @iID2;
DELETE FROM t_pos WHERE F_ID = @iID;

--	SELECT '-------------------- Case5:该POS机有服务器端使用的同步表依赖，不能删除-------------------------' AS 'Case5';
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
--	SELECT IF(@sErrorMsg = '该POS机有服务器端使用的同步表依赖，不能删除', '测试成功', '测试失败') AS 'Case5 Testing Result';
--	
--	DELETE FROM T_SegmentDownloadSyncServer WHERE F_ID = @iID2;
--	DELETE FROM t_pos WHERE F_ID = @iID;

SELECT '-------------------- Case6:该POS机有促销同步缓存调度表依赖，不能删除-------------------------' AS 'Case6';
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
SELECT IF(@sErrorMsg = '该POS机有促销同步缓存调度表依赖，不能删除', '测试成功', '测试失败') AS 'Case6 Testing Result';

DELETE FROM T_PromotionSyncCacheDispatcher WHERE F_ID = @iIDA2;
DELETE FROM t_promotionsynccache WHERE F_ID = @iIDA1;
DELETE FROM t_pos WHERE F_ID = @iID;


SELECT '-------------------- Case7:该POS机有会员同步缓存调度表依赖，不能删除-------------------------' AS 'Case7';
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
--	SELECT IF(@sErrorMsg = '该POS机有会员同步缓存调度表依赖，不能删除', '测试成功', '测试失败') AS 'Case7 Testing Result';
--	
--	DELETE FROM T_VipSyncCacheDispatcher WHERE F_ID = @iIDA2;
--	DELETE FROM t_Vipsynccache WHERE F_ID = @iIDA1;
--	DELETE FROM t_pos WHERE F_ID = @iID;

SELECT '-------------------- Case8:该POS机有会员类别同步缓存调度表依赖，不能删除-------------------------' AS 'Case8';
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
--	SELECT IF(@sErrorMsg = '该POS机有会员类别同步缓存调度表依赖，不能删除', '测试成功', '测试失败') AS 'Case8 Testing Result';
--	
--	DELETE FROM T_VipCategorySyncCacheDispatcher WHERE F_ID = @iIDA2;
--	DELETE FROM t_VipCategorysynccache WHERE F_ID = @iIDA1;
--	DELETE FROM t_pos WHERE F_ID = @iID;

SELECT '-------------------- Case9:该POS机有商品同步缓存调度表依赖，不能删除-------------------------' AS 'Case9';
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
SELECT IF(@sErrorMsg = '该POS机有商品同步缓存调度表依赖，不能删除', '测试成功', '测试失败') AS 'Case9 Testing Result';

DELETE FROM T_CommoditySyncCacheDispatcher WHERE F_ID = @iIDA2;
DELETE FROM t_Commoditysynccache WHERE F_ID = @iIDA1;
DELETE FROM t_pos WHERE F_ID = @iID;

SELECT '-------------------- Case11:该POS机有品牌同步缓存调度表依赖，不能删除-------------------------' AS 'Case11';
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
SELECT IF(@sErrorMsg = '该POS机有品牌同步缓存调度表依赖，不能删除', '测试成功', '测试失败') AS 'Case11 Testing Result';

DELETE FROM T_BrandSyncCacheDispatcher WHERE F_ID = @iIDA2;
DELETE FROM t_BrandSynccache WHERE F_ID = @iIDA1;
DELETE FROM t_pos WHERE F_ID = @iID;


SELECT '-------------------- Case12:该POS机有小票同步缓存调度表依赖，不能删除-------------------------' AS 'Case12';
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
--	SELECT IF(@sErrorMsg = '该POS机有小票同步缓存调度表依赖，不能删除', '测试成功', '测试失败') AS 'Case12 Testing Result';
--	
--	DELETE FROM T_SmallSheetSyncCacheDispatcher WHERE F_ID = @iIDA2;
--	DELETE FROM t_SmallSheetSynccache WHERE F_ID = @iIDA1;
--	DELETE FROM t_pos WHERE F_ID = @iID;

SELECT '-------------------- Case13:该POS机有类别同步缓存调度表依赖，不能删除-------------------------' AS 'Case13';
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
SELECT IF(@sErrorMsg = '该POS机有类别同步缓存调度表依赖，不能删除', '测试成功', '测试失败') AS 'Case13 Testing Result';

DELETE FROM T_CategorySyncCacheDispatcher WHERE F_ID = @iIDA2;
DELETE FROM t_CategorySynccache WHERE F_ID = @iIDA1;
DELETE FROM t_pos WHERE F_ID = @iID;

SELECT '-------------------- Case14:该POS机有条形码同步缓存调度表依赖，不能删除-------------------------' AS 'Case14';
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
SELECT IF(@sErrorMsg = '该POS机有条形码同步缓存调度表依赖，不能删除', '测试成功', '测试失败') AS 'Case14 Testing Result';

DELETE FROM T_BarcodesSyncCacheDispatcher WHERE F_ID = @iIDA2;
DELETE FROM t_BarcodesSynccache WHERE F_ID = @iIDA1;
DELETE FROM t_pos WHERE F_ID = @iID;