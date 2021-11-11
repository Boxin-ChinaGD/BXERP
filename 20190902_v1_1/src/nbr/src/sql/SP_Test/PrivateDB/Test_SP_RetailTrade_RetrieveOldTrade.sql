SELECT '++++++++++++++++++ Test_SP_RetailTrade_RetrieveOldTrade.sql ++++++++++++++++++++';

-- INSERT INTO T_RetailTrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
-- F_Status, F_Remark, F_VipID)
-- VALUES ('SN123483222', 11,2,'url=ashasouuuuunalskd','2017-8-10',5,2,'A123460',1,'双击777', 5);
-- SET @iID = last_insert_id();

SELECT '-------------------- Case1: web端最小长度的零售单单号来查询零售单(等于10位) -------------------------' AS 'Case1';

INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS12348321218000', 2, 4, 'url=ashasoadigmnalskd', now(), 2, 4, '0', 1, '........', -1, now(), 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 0;
SET @queryKeyword = 'LS12348321';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;
SELECT IF(@sErrorMsg = '' AND @iTotalRecord > 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;

SELECT '-------------------- Case2: web端用POS_SN 和 POS_ID来查询零售单 -------------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 0;
SET @queryKeyword = '';
SET @iLocalSN = 1;
SET @iPOS_ID = 1;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord > 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';

SELECT '-------------------- Case3: web端用POS_ID来查询零售单 -------------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 0;
SET @queryKeyword = '';
SET @iLocalSN = 0;
SET @iPOS_ID = 2;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord > 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case3 Testing Result';

SELECT '-------------------- Case4: web端查询所有零售单 -------------------------' AS 'Case4';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 0;
SET @queryKeyword = '';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord > 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case4 Testing Result';

SELECT '-------------------- Case5: web端用时间@dStartDate来查零售单 -------------------------' AS 'Case5';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 0;
SET @queryKeyword = '';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '2017/8/6';
SET @dEndDate = now();
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord > 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case5 Testing Result';

SELECT '-------------------- Case6: web端用iLocalSN来查询零售单 -------------------------' AS 'Case6';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 0;
SET @queryKeyword = '';
SET @iLocalSN = 1;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord > 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case6 Testing Result';

SELECT '-------------------- Case7: web端用@iVipID来查询零售单 -------------------------' AS 'Case7';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 5;
SET @bRequestFromApp = 0;
SET @queryKeyword = '';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord > 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case7 Testing Result';

SELECT '-------------------- Case8: web端用@iPaymentType来查询零售单 -------------------------' AS 'Case8';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 0;
SET @queryKeyword = '';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 2;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord > 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case8 Testing Result';

SELECT '-------------------- Case9: web端用@iStaffID来查询零售单 -------------------------' AS 'Case9';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 0;
SET @queryKeyword = '';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 5;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord > 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case9 Testing Result';

DELETE FROM t_retailtrade WHERE F_ID = @iID;

-- 可以实现
--	SELECT '-------------------- Case10: web端用小于最小长度的零售单单号来查询零售单（小于10位）,期望是查询不到数据 -------------------------' AS 'Case10';
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @iVipID = 0;
--	SET @bRequestFromApp = 0;
--	SET @queryKeyword = 'LS1234832';
--	SET @iLocalSN = 0;
--	SET @iPOS_ID = 0;
--	SET @dStartDate = '';
--	SET @dEndDate = '';
--	SET @iPaymentType = 0;
--	SET @iPageIndex = 1;
--	SET @iPageSize = 10;
--	SET @iTotalRecord = 0;
--	SET @iStaffID = 0;
--	SET @iExcludeReturned = 0;
--	
--	CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
--	@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);
--	
--	SELECT @iErrorCode, @sErrorMsg;
--	
--	SELECT IF(@sErrorMsg = '搜索内容格式有误' AND @iTotalRecord = 0 AND @iErrorCode = 8, '测试成功', '测试失败') AS 'Case10 Testing Result';

--	SELECT '-------------------- Case11: web端用大于最大长度的零售单单号来查询零售单（大于26位）,期望是查询不到数据 -------------------------' AS 'Case11';
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @iVipID = 0;
--	SET @bRequestFromApp = 0;
--	SET @queryKeyword = 'LS123483218000011111245456465';
--	SET @iLocalSN = 0;
--	SET @iPOS_ID = 0;
--	SET @dStartDate = '';
--	SET @dEndDate = '';
--	SET @iPaymentType = 0;
--	SET @iPageIndex = 1;
--	SET @iPageSize = 10;
--	SET @iTotalRecord = 0;
--	SET @iStaffID = 0;
--	SET @iExcludeReturned = 0;
--	
--	CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
--	@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);
--	
--	SELECT @iErrorCode, @sErrorMsg;
--	
--	SELECT IF(@sErrorMsg = '搜索内容格式有误' AND @iTotalRecord = 0 AND @iErrorCode = 8, '测试成功', '测试失败') AS 'Case11 Testing Result';

SELECT '-------------------- Case12: web端用最大长度的零售单单号来查询零售单（等于26位） -------------------------' AS 'Case12';

INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS1234832180000111111234_1', 2, 4, 'url=ashasoadigmnalskd', now(), 2, 4, '0', 1, '........', -1, now(), 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 0;
SET @queryKeyword = 'LS1234832180000111111234_1';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord > 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case12 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;

SELECT '-------------------- Case13: web端使用零售单存在的商品名称进行模糊查询 -------------------------' AS 'Case13';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 0;
SET @queryKeyword = '可比克薯片';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord > 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case13 Testing Result';

SELECT '-------------------- Case14: web端使用零售单不存在的商品名称进行模糊查询 -------------------------' AS 'Case14';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 0;
SET @queryKeyword = '不存在的商品';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case14 Testing Result';

SELECT '-------------------- Case15: web端使用null进行模糊查询 -------------------------' AS 'Case15';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 0;
SET @queryKeyword = NULL;
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord > 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case15 Testing Result';

SELECT '-------------------- Case16: web端使用空串('')进行模糊查询 -------------------------' AS 'Case16';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 0;
SET @queryKeyword = '';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord > 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case16 Testing Result';

SELECT '-------------------- Case17: web端使用超长的字符(长度大于32位)进行模糊查询 -------------------------' AS 'Case17';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 0;
SET @queryKeyword = '雪晶灵肌密精华原雪晶灵肌密精华原雪晶灵肌密精华原';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case17 Testing Result';

SELECT '-------------------- Case18: web端使用非法字符进行迷糊查询 -------------------------' AS 'Case18';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 0;
SET @queryKeyword = '@#$%^!&*)*)*&%@!*^*^!%*';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case18 Testing Result';

SELECT '-------------------- Case19: web端使用商品名称的一部分进行查询进行迷糊查询 -------------------------' AS 'Case19';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 0;
SET @queryKeyword = 'A';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord > 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case19 Testing Result';

SELECT '-------------------- Case20: web端使用商品名称输入"_"特殊字符进行模糊查询 -------------------------' AS 'Case20';

INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark, F_SourceID,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID, F_ShopID)
VALUES (NULL,'SB201906100001',2,5,'url=ashasoadigmxnalskd','2017-08-06',2,1,0,1,'........',-1, now(),1.5,1.1,0,0,0,0,0,0,0,2,2);

SET @iTradeID = LAST_INSERT_ID();


INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iTradeID, 1,'莱恩的局面_hjjkkj', 63, 10, 1, 10, 10, 10);


SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 0;
SET @queryKeyword = '_';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord > 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case20 Testing Result';
DELETE FROM t_retailtradecommodity WHERE F_CommodityName = '莱恩的局面_hjjkkj';
DELETE FROM T_RetailTrade WHERE F_ID = @iTradeID;



SELECT '-------------------- Case21: web端iExcludeReturned = 0，查询出的零售单包含退货单 -------------------------' AS 'Case21';
-- 新建零售单A
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS123483218000', 2, 4, 'url=ashasoadigmnalskd', now(), 2, 4, '0', 1, '........', -1, now(), 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();

-- 新建A的零售商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, 1, '可比克薯片', 1, 2, 321, 500, 25, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();

-- 新建退货单B
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS223483218000', 3, 5, 'url=ashasoadigmnalskd', now(), 2, 4, '0', 1, '........', @retailTradeID, now(), 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID = last_insert_id();
-- 新建B的退货商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, 1, '可比克薯片', 1, 2, 321, 500, 15, 300, NULL);
SET @returnRetailTradeCommodityID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iVipID = 0;
SET @bRequestFromApp = 0;
SET @queryKeyword = '';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord > 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case21 Testing Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID;


SELECT '-------------------- Case22: web端iExcludeReturned = 1，查询出的零售单不包含退货单 -------------------------' AS 'Case22';

-- 新建零售单A
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS323483218000', 2, 4, 'url=ashasoadigmnalskd', now(), 2, 4, '0', 1, '........', -1, now(), 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();

-- 新建A的零售商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@retailTradeID, 1, '可比克薯片', 1, 2, 321, 500, 25, 300, NULL);
SET @retailTradeCommodityID = last_insert_id();

-- 新建退货单B
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS423483218000', 3, 5, 'url=ashasoadigmnalskd', now(), 2, 4, '0', 1, '........', @retailTradeID, now(), 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @returnRetailTradeID = last_insert_id();
-- 新建B的退货商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnRetailTradeID, 1, '可比克薯片', 1, 2, 321, 500, 15, 300, NULL);
SET @returnRetailTradeCommodityID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 0;
SET @queryKeyword = '';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 1;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord > 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case22 Testing Result';
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID;

SELECT '-------------------- Case23: 在pos端输入等于10位的sn进行搜索 -------------------------' AS 'Case23';

INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS12348321218000', 2, 4, 'url=ashasoadigmnalskd', now(), 2, 4, '0', 1, '........', -1, now(), 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 1;
SET @queryKeyword = 'LS12348321';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;
SELECT IF(@sErrorMsg = '' AND @iTotalRecord > 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case23 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;

SELECT '-------------------- Case24: 在pos端输入空串查询所有零售单 -------------------------' AS 'Case24';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 1;
SET @queryKeyword = '';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord > 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case24 Testing Result';

SELECT '-------------------- Case25:在pos端用大于最大长度的零售单单号来查询零售单（大于26位）,期望是查询不到数据 -------------------------' AS 'Case25';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 1;
SET @queryKeyword = 'LS123483218000011111245456465';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case25 Testing Result';

SELECT '-------------------- Case26: 在pos端用小于最小长度的零售单单号来查询零售单（小于10位）,期望是查询不到数据 -------------------------' AS 'Case26';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 1;
SET @queryKeyword = 'LS1234832';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case26 Testing Result';

SELECT '-------------------- Case27: 在pos端用最大长度的零售单单号来查询零售单（等于26位） -------------------------' AS 'Case27';

INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS1234832180000111111234_1', 2, 4, 'url=ashasoadigmnalskd', now(), 2, 4, '0', 1, '........', -1, now(), 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @retailTradeID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 1;
SET @queryKeyword = 'LS1234832180000111111234_1';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord > 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case27 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;

SELECT '-------------------- Case28: 在pos端使用中文查询sn -------------------------' AS 'Case28';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 1;
SET @queryKeyword = '可比克薯片';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case28 Testing Result';

SELECT '-------------------- Case29: 在pos端使用null进行模糊查询 -------------------------' AS 'Case29';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 1;
SET @queryKeyword = NULL;
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case29 Testing Result';

SELECT '-------------------- Case30: pos端使用超长的字符(长度大于32位)进行查询 -------------------------' AS 'Case30';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 1;
SET @queryKeyword = 'LS200901010001123456123456878985465';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case30 Testing Result';

SELECT '-------------------- Case31: web端使用非法字符进行迷糊查询 -------------------------' AS 'Case31';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 0;
SET @iVipID = 0;
SET @bRequestFromApp = 1;
SET @queryKeyword = '@#$%^!&*)*)*&%@!*^*^!%*';
SET @iLocalSN = 0;
SET @iPOS_ID = 0;
SET @dStartDate = '';
SET @dEndDate = '';
SET @iPaymentType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStaffID = 0;
SET @iExcludeReturned = 0;

CALL SP_RetailTrade_RetrieveOldTrade(@iErrorCode, @sErrorMsg, @iShopID, @iVipID, @bRequestFromApp, @queryKeyword, @iLocalSN, @iPOS_ID, @dStartDate, @dEndDate,
@iPaymentType, @iStaffID, @iPageIndex, @iPageSize, @iExcludeReturned,@iTotalRecord);

SELECT @iErrorCode, @sErrorMsg;

SELECT IF(@sErrorMsg = '' AND @iTotalRecord = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case31 Testing Result';