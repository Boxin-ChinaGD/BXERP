SELECT '++++++++++++++++++ Test_SP_RetailTrade_UploadTrade.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: SN和POSID和SaleDatetime都相同的情况直接返回该零售单 -------------------------' AS 'Case1';
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, '1231233211223', '88866888', 1, 'url=xxxxxxxxxxxxx', '2019/7/15 13:30:00', 4, 1, 1, 1, 'xxxxxxx', -1, now(), 50, 50, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iID = Last_insert_id(); 

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iVipID = 1; 
SET @iLocalSN = '88866888';
SET @iPOS_ID = 1;
SET @sLogo = 'url=asha420adigmnalskd';
SET @dtSaleDatetime = '2019/7/15 13:30:00';
SET @iStaffID = 2;
SET @iPaymentType = 1;
SET @iPaymentAccount = 'Q1234156';
SET @iStatus = 1;
SET @sRemark = '...............';
SET @iSourceID = -1;
SET @fAmount = 285;
SET @fAmountCash = 0;
SET @fAmountAlipay = 0;
SET @fAmountWeChat = 0;
SET @fAmount1 = 99;
SET @fAmount2 = 0;
SET @fAmount3 = 0;
SET @fAmount4 = 8;
SET @fAmount5 = 0;
SET @iSmallSheetID = 1;
SET @sAliPayOrderSN = '123456';
SET @sWxOrderSN = '123456';
SET @sWxTradeNO = NULL;
SET @sWxRefundNO = NULL;
SET @sWxRefundDesc = '14445565';
SET @sWxRefundSubMchID = '1';
SET @sSN = '1231233211223';
SET @dCouponAmount = 0.1;
SET @sConsumerOpenID = NULL;
SET @shopID = 2;

CALL SP_RetailTrade_UploadTrade(@iErrorCode, @sErrorMsg, @iVipID, @iLocalSN, @iPOS_ID, @sLogo, @dtSaleDatetime, @iStaffID, @iPaymentType, @iPaymentAccount, @iStatus, @sRemark, @iSourceID, @fAmount, @fAmountCash, @fAmountAlipay, @fAmountWeChat
	, @fAmount1, @fAmount2, @fAmount3, @fAmount4, @fAmount5, @iSmallSheetID, @sAliPayOrderSN, @sWxOrderSN, @sWxTradeNO, @sWxRefundNO, @sWxRefundDesc, @sWxRefundSubMchID, @sSN, @dCouponAmount, @sConsumerOpenID, @shopID);
SELECT IF(found_rows() = 1 AND @iErrorCode = 1 AND @sErrorMsg = '该零售单已经存在', '测试成功', '测试失败') AS 'Case1 Testing Result';

DELETE FROM t_retailtrade WHERE F_ID = @iID;

SELECT '-------------------- Case2: SN和POSID和SaleDatetime不相同的情况下是正常创建	 -------------------------' AS 'Case2';

INSERT INTO T_VIP (F_ICID,F_CardID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile)
VALUES ('124124',1,'Ada','1232226@bx.vip',0,0,'广州',1,
'2017-08-06',0,'2017-08-08 23:59:10','123235232312');
SET @iVipID = Last_insert_id(); 
	
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iLocalSN = 2222333;
SET @iPOS_ID = 5;
SET @dtSaleDatetime = NOW();
SET @sLogo = 'url=asha42soadigmnalskd';
SET @iStaffID = 2;
SET @iPaymentType = 1;
SET @iPaymentAccount = 'Q123456';
SET @iStatus = 1;
SET @sRemark = '...............';
SET @iSourceID = -1;
SET @fAmount = 100;
SET @fAmountCash = 0;
SET @fAmountAlipay = 0;
SET @fAmountWeChat = 0;
SET @fAmount1 = 100;
SET @fAmount2 = 0;
SET @fAmount3 = 0;
SET @fAmount4 = 8;
SET @fAmount5 = 0;
SET @iSmallSheetID = 1;
SET @sAliPayOrderSN = '123456';
SET @sWxOrderSN = '123456';
SET @sWxTradeNO = NULL;
SET @sWxRefundNO = '123456';
SET @sWxRefundDesc = '14445565';
SET @sWxRefundSubMchID = NULL;
SET @sSN = 'LS2018010101010100011234';
SET @dCouponAmount = 0.1;
SET @sConsumerOpenID = NULL;
SET @shopID = 2;

CALL SP_RetailTrade_UploadTrade(@iErrorCode, @sErrorMsg, @iVipID, @iLocalSN, @iPOS_ID, @sLogo, @dtSaleDatetime, @iStaffID, @iPaymentType, @iPaymentAccount, @iStatus, @sRemark, @iSourceID, @fAmount, @fAmountCash, @fAmountAlipay, @fAmountWeChat
	, @fAmount1, @fAmount2, @fAmount3, @fAmount4, @fAmount5, @iSmallSheetID, @sAliPayOrderSN, @sWxOrderSN, @sWxTradeNO, @sWxRefundNO, @sWxRefundDesc, @sWxRefundSubMchID, @sSN, @dCouponAmount, @sConsumerOpenID, @shopID);

SELECT @sErrorMsg;
SELECT 1 FROM t_RetailTrade 
	WHERE F_LocalSN = 2222333
	AND F_POS_ID = 5
	AND F_Logo = 'url=asha42soadigmnalskd'
	AND F_StaffID = 2
	AND F_PaymentType = 1
	AND F_PaymentAccount = 'Q123456'
	AND F_Remark = '...............'
	AND F_SourceID = -1
	AND F_Amount = 100
	AND F_AmountCash = 0
	AND F_AmountAlipay = 0
	AND F_AmountWeChat = 0
	AND F_Amount1 = 100
	AND F_Amount2 = 0
	AND F_Amount3 = 0
	AND F_Amount4 = 8
	AND F_Amount5 = 0
	AND F_SmallSheetID = 1
	AND F_AliPayOrderSN = '123456'
	AND F_WxOrderSN = '123456'
	AND F_WxRefundNO = '123456'
	AND F_WxRefundDesc = '14445565'
	AND F_CouponAmount = @dCouponAmount
	AND F_ConsumerOpenID IS NULL;
	
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';

SELECT 1 FROM t_vip WHERE F_ConsumeTimes = 1 AND F_ConsumeAmount = 100 AND F_Bonus = 1000; 
SELECT IF(found_rows() = 1 , '测试成功', '测试失败') AS 'Case2 Testing Result';

DELETE FROM t_retailtrade WHERE F_VipID = @iVipID;
DELETE FROM t_bonusconsumehistory WHERE F_VipID = @iVipID;
DELETE FROM t_vip WHERE F_ID = @iVipID;

SELECT '-------------------- Case3: iSourceID为不存在的零售单ID，返回错误码7 -------------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iVipID = 1; 
SET @iLocalSN = 33;
SET @iPOS_ID = 5;
SET @sLogo = 'url=asha42soadigmnalskd';
SET @dtSaleDatetime = NOW();
SET @iStaffID = 2;
SET @iPaymentType = 1;
SET @iPaymentAccount = 'Q123456';
SET @iStatus = 1;
SET @sRemark = '...............';
SET @iSourceID = 999;
SET @fAmount = 285;
SET @fAmountCash = 0;
SET @fAmountAlipay = 0;
SET @fAmountWeChat = 0;
SET @fAmount1 = 99;
SET @fAmount2 = 0;
SET @fAmount3 = 0;
SET @fAmount4 = 8;
SET @fAmount5 = 0;
SET @iSmallSheetID = 1;
SET @sAliPayOrderSN = '123456';
SET @sWxOrderSN = NULL;
SET @sWxTradeNO = NULL;
SET @sWxRefundNO = NULL;
SET @sWxRefundDesc = '14445565';
SET @sWxRefundSubMchID = '1';
SET @sSN = 'LS2018010101010100014321';
SET @dCouponAmount = 0.1;
SET @sConsumerOpenID = NULL;
SET @shopID = 2;

CALL SP_RetailTrade_UploadTrade(@iErrorCode, @sErrorMsg, @iVipID, @iLocalSN, @iPOS_ID, @sLogo, @dtSaleDatetime, @iStaffID, @iPaymentType, @iPaymentAccount, @iStatus, @sRemark, @iSourceID, @fAmount, @fAmountCash, @fAmountAlipay, @fAmountWeChat
	, @fAmount1, @fAmount2, @fAmount3, @fAmount4, @fAmount5, @iSmallSheetID, @sAliPayOrderSN, @sWxOrderSN, @sWxTradeNO, @sWxRefundNO, @sWxRefundDesc, @sWxRefundSubMchID, @sSN, @dCouponAmount, @sConsumerOpenID, @shopID);

SELECT @sErrorMsg;
SELECT 1 FROM t_RetailTrade 
	WHERE F_LocalSN = @iLocalSN
	AND F_POS_ID = @iPOS_ID
	AND F_Logo = @sLogo
	AND F_StaffID = @iStaffID
	AND F_PaymentType = @iPaymentType
	AND F_PaymentAccount = @iPaymentAccount
	AND F_Remark = @sRemark
	AND F_SourceID = @iSourceID
	AND F_Amount = @fAmount
	AND F_AmountCash = @fAmountCash
	AND F_AmountAlipay = @fAmountAlipay
	AND F_AmountWeChat = @fAmountWeChat
	AND F_Amount1 = @fAmount1
	AND F_Amount2 = @fAmount2
	AND F_Amount3 = @fAmount3
	AND F_Amount4 = @fAmount4
	AND F_Amount5 = @fAmount5
	AND F_SmallSheetID = @iSmallSheetID
	AND F_AliPayOrderSN = @sAliPayOrderSN
	AND F_WxRefundDesc = @sWxRefundDesc
	AND F_WxRefundSubMchID = @sWxRefundSubMchID
	AND F_CouponAmount = @dCouponAmount
	AND F_ConsumerOpenID IS NULL;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case3 Testing Result';

SELECT '-------------------- Case4: iSourceID为存在的零售单ID，并且交易时间没有超过一年，进行退货操作,退货成功 -------------------------' AS 'Case4';

INSERT INTO T_VIP (F_ICID,F_CardID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile)
VALUES ('1244424',1,'Ada','1224126@bx.vip',10,100,'广州',1,
'2017-08-06',100,'2017-08-08 23:59:10','123343432312');
SET @iVipID = Last_insert_id(); 

INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID, F_ShopID)
VALUES (@iVipID,'LS201906100001',198,1,'url=ashasoadigmxnalskd',now(),2,1,0,1,'........',now(),1.5,1.1,0,0,0,0,0,0,0,2, 2);
SET @RetailTradeID_A = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iLocalSN = 44;
SET @iPOS_ID = 5;
SET @sLogo = 'url=asha42soadigmnalskd';
SET @dtSaleDatetime = NOW();
SET @iStaffID = 2;
SET @iPaymentType = 1;
SET @iPaymentAccount = 'Q123456';
SET @iStatus = 1;
SET @sRemark = '...............';
SET @iSourceID = @RetailTradeID_A;
SET @fAmount = 50;
SET @fAmountCash = 0;
SET @fAmountAlipay = 0;
SET @fAmountWeChat = 0;
SET @fAmount1 = 50;
SET @fAmount2 = 0;
SET @fAmount3 = 0;
SET @fAmount4 = 8;
SET @fAmount5 = 0;
SET @iSmallSheetID = 1;
SET @sAliPayOrderSN = '123456';
SET @sWxOrderSN = '123456';
SET @sWxTradeNO = NULL;
SET @sWxRefundNO = NULL;
SET @sWxRefundDesc = NULL;
SET @sWxRefundSubMchID = NULL;
SET @sSN = 'LS2018010101010100018888';
SET @dCouponAmount = 0.1;
SET @sConsumerOpenID = NULL;
SET @shopID = 2;

CALL SP_RetailTrade_UploadTrade(@iErrorCode, @sErrorMsg, @iVipID, @iLocalSN, @iPOS_ID, @sLogo, @dtSaleDatetime, @iStaffID, @iPaymentType, @iPaymentAccount, @iStatus, @sRemark, @iSourceID, @fAmount, @fAmountCash, @fAmountAlipay, @fAmountWeChat
	, @fAmount1, @fAmount2, @fAmount3, @fAmount4, @fAmount5, @iSmallSheetID, @sAliPayOrderSN, @sWxOrderSN, @sWxTradeNO, @sWxRefundNO, @sWxRefundDesc, @sWxRefundSubMchID, @sSN, @dCouponAmount, @sConsumerOpenID, @shopID);
SELECT @iErrorCode;
	
SELECT 1 FROM t_RetailTrade 
	WHERE F_LocalSN = @iLocalSN
	AND F_POS_ID = @iPOS_ID
	AND F_Logo = @sLogo
	AND F_StaffID = @iStaffID
	AND F_PaymentType = @iPaymentType
	AND F_PaymentAccount = @iPaymentAccount
	AND F_Remark = @sRemark
	AND F_SourceID = @iSourceID
	AND F_Amount = @fAmount
	AND F_AmountCash = @fAmountCash
	AND F_AmountAlipay = @fAmountAlipay
	AND F_AmountWeChat = @fAmountWeChat
	AND F_Amount1 = @fAmount1
	AND F_Amount2 = @fAmount2
	AND F_Amount3 = @fAmount3
	AND F_Amount4 = @fAmount4
	AND F_Amount5 = @fAmount5
	AND F_SmallSheetID = @iSmallSheetID
	AND F_AliPayOrderSN = @sAliPayOrderSN
	AND F_WxOrderSN = @sWxOrderSN
	AND F_CouponAmount = @dCouponAmount
	AND F_ConsumerOpenID IS NULL;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case4 Testing Result';

SELECT 1 FROM t_vip WHERE F_ConsumeTimes = 9 AND F_ConsumeAmount = 50 AND F_Bonus = 100; 
SELECT IF(found_rows() = 1 , '测试成功', '测试失败') AS 'Case4 Testing Result';

DELETE FROM t_retailtrade WHERE F_VipID = @iVipID;
DELETE FROM t_retailtrade WHERE F_ID = @RetailTradeID_A;
DELETE FROM t_vip WHERE F_ID = @iVipID;

SELECT '-------------------- Case5: 对一张退货类型的零售单再次进行退货-------------------------' AS 'Case5';
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID, F_ShopID)
VALUES (NULL,'SN1234832fff',198,1,'url=ashasoadigmxnalskd','2017-08-06',2,1,0,1,'........',now(),1.5,1.1,0,0,0,0,0,0,0,2,2);
SET @RetailTradeID_A = last_insert_id();
-- 
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark, F_SourceID,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID, F_ShopID)
VALUES (NULL,'SN123483218xf',199,1,'url=ashasoadigmxnalskd','2017-08-06',2,1,0,1,'........', @RetailTradeID_A, now(),1.5,1.1,0,0,0,0,0,0,0,2, 2);
SET @retailTradeID_B = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iLocalSN = 55;
SET @iPOS_ID = 5;
SET @sLogo = 'urxl=asha42soadigmnalskd';
SET @dtSaleDatetime = NOW();
SET @iStaffID = 2;
SET @iPaymentType = 1;
SET @iPaymentAccount = 'Q123456';
SET @iStatus = 1;
SET @sRemark = '...............';
SET @iSourceID = @retailTradeID_B;
SET @fAmount = 50;
SET @fAmountCash = 0;
SET @fAmountAlipay = 0;
SET @fAmountWeChat = 0;
SET @fAmount1 = 50;
SET @fAmount2 = 0;
SET @fAmount3 = 0;
SET @fAmount4 = 8;
SET @fAmount5 = 0;
SET @iSmallSheetID = 1;
SET @sAliPayOrderSN = '123456';
SET @sWxOrderSN = '123456';
SET @sWxTradeNO = NULL;
SET @sWxRefundNO = NULL;
SET @sWxRefundDesc = NULL;
SET @sWxRefundSubMchID = NULL;
SET @sSN = 'LS2018010101010100017777';
SET @dCouponAmount = 0.1;
SET @sConsumerOpenID = NULL;
SET @iVipID = 1;
SET @shopID = 2;
-- 
CALL SP_RetailTrade_UploadTrade(@iErrorCode, @sErrorMsg, @iVipID, @iLocalSN, @iPOS_ID, @sLogo, @dtSaleDatetime, @iStaffID, @iPaymentType, @iPaymentAccount, @iStatus, @sRemark, @iSourceID, @fAmount, @fAmountCash, @fAmountAlipay, @fAmountWeChat
	, @fAmount1, @fAmount2, @fAmount3, @fAmount4, @fAmount5, @iSmallSheetID, @sAliPayOrderSN, @sWxOrderSN, @sWxTradeNO, @sWxRefundNO, @sWxRefundDesc, @sWxRefundSubMchID, @sSN, @dCouponAmount, @sConsumerOpenID, @shopID);
SELECT IF(@sErrorMsg = '不能对退货类型的零售单进行退货' AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case5 Testing Result';
-- 
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID_B;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID_A;


--	SELECT '-------------------- Case6: 对一张零售单进行N次退货操作，那么它的sn则是源单SN加_N-------------------------' AS 'Case6';
--	INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
--	F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
--	VALUES (NULL,'LS201906100001',198,1,'url=ashasoadigmxnalskd','2017-08-06',2,1,0,1,'........',now(),1.5,1.1,0,0,0,0,0,0,0,2);
--	SET @RetailTradeID_A = last_insert_id();
--	-- 
--	INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
--	F_Status,F_Remark, F_SourceID,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
--	VALUES (NULL,'LS201906100001_1',197,1,'url=ashasoadigmxnalskd','2017-08-06',2,1,0,1,'........', @RetailTradeID_A, now(),1.5,1.1,0,0,0,0,0,0,0,2);
--	SET @retailTradeID_B = last_insert_id();
--	-- 
--	SET @iVipID = 0; 
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @iLocalSN = 55;
--	SET @iPOS_ID = 5;
--	SET @sLogo = 'urxl=asha42soadigmnalskd';
--	SET @dtSaleDatetime = NOW();
--	SET @iStaffID = 2;
--	SET @iPaymentType = 1;
--	SET @iPaymentAccount = 'Q123456';
--	SET @sRemark = '...............';
--	SET @iSourceID = @RetailTradeID_A;
--	SET @fAmount = 50;
--	SET @fAmountCash = 0;
--	SET @fAmountAlipay = 0;
--	SET @fAmountWeChat = 0;
--	SET @fAmount1 = 50;
--	SET @fAmount2 = 0;
--	SET @fAmount3 = 0;
--	SET @fAmount4 = 8;
--	SET @fAmount5 = 0;
--	SET @iSmallSheetID = 1;
--	SET @sAliPayOrderSN = '123456';
--	SET @sWxOrderSN = '123456';
--	SET @sWxTradeNO = NULL;
--	SET @sWxRefundNO = NULL;
--	SET @sWxRefundDesc = NULL;
--	SET @sWxRefundSubMchID = NULL;
--	-- 
--	CALL SP_RetailTrade_UploadTrade(@iErrorCode, @sErrorMsg, @iVipID, @iLocalSN, @iPOS_ID, @sLogo, @dtSaleDatetime, @iStaffID, @iPaymentType, @iPaymentAccount, @sRemark, @iSourceID, @fAmount, @fAmountCash, @fAmountAlipay, @fAmountWeChat
--		, @fAmount1, @fAmount2, @fAmount3, @fAmount4, @fAmount5, @iSmallSheetID, @sAliPayOrderSN, @sWxOrderSN, @sWxTradeNO, @sWxRefundNO, @sWxRefundDesc, @sWxRefundSubMchID);
--	SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case6 Testing Result';
--	SET @retailTradeID_C = last_insert_id();
--	SET @sSN = '';
--	SELECT F_SN INTO @sSN FROM t_retailtrade WHERE F_ID = @retailTradeID_C;
--	SELECT IF(RIGHT(@sSN, 1) = '2', '测试成功', '测试失败') AS 'Case6 Testing Result';
--	-- 
--	DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID_C;
--	DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID_B;
--	DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID_A;


SELECT '-------------------- Case6: 一张零售单，最多只能进行一次退货操作,进行第二次退货退货失败-------------------------' AS 'Case6';
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID, F_ShopID)
VALUES (NULL,'LS201906100001',198,1,'url=ashasoadigmxnalskd','2017-08-06',2,1,0,1,'........',now(),1.5,1.1,0,0,0,0,0,0,0,2,2);
SET @RetailTradeID_A = last_insert_id();
-- 
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark, F_SourceID,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID, F_ShopID)
VALUES (NULL,'LS201906100001_1',197,1,'url=ashasoadigmxnalskd','2017-08-06',2,1,0,1,'........', @RetailTradeID_A, now(),1.5,1.1,0,0,0,0,0,0,0,2,2);
SET @retailTradeID_B = last_insert_id();
-- 
SET @iVipID = 0; 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iLocalSN = 55;
SET @iPOS_ID = 5;
SET @sLogo = 'urxl=asha42soadigmnalskd';
SET @dtSaleDatetime = NOW();
SET @iStaffID = 2;
SET @iPaymentType = 1;
SET @iPaymentAccount = 'Q123456';
SET @iStatus = 1;
SET @sRemark = '...............';
SET @iSourceID = @RetailTradeID_A;
SET @fAmount = 50;
SET @fAmountCash = 0;
SET @fAmountAlipay = 0;
SET @fAmountWeChat = 0;
SET @fAmount1 = 50;
SET @fAmount2 = 0;
SET @fAmount3 = 0;
SET @fAmount4 = 8;
SET @fAmount5 = 0;
SET @iSmallSheetID = 1;
SET @sAliPayOrderSN = '123456';
SET @sWxOrderSN = '123456';
SET @sWxTradeNO = NULL;
SET @sWxRefundNO = NULL;
SET @sWxRefundDesc = NULL;
SET @sWxRefundSubMchID = NULL;
SET @sSN = 'LS2018010101010100012222';
SET @dCouponAmount = 0.1;
SET @sConsumerOpenID = NULL;
SET @shopID = 2;
-- 
CALL SP_RetailTrade_UploadTrade(@iErrorCode, @sErrorMsg, @iVipID, @iLocalSN, @iPOS_ID, @sLogo, @dtSaleDatetime, @iStaffID, @iPaymentType, @iPaymentAccount, @iStatus, @sRemark, @iSourceID, @fAmount, @fAmountCash, @fAmountAlipay, @fAmountWeChat
	, @fAmount1, @fAmount2, @fAmount3, @fAmount4, @fAmount5, @iSmallSheetID, @sAliPayOrderSN, @sWxOrderSN, @sWxTradeNO, @sWxRefundNO, @sWxRefundDesc, @sWxRefundSubMchID, @sSN, @dCouponAmount, @sConsumerOpenID, @shopID);
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '一张零售单，最多只能进行一次退货操作', '测试成功', '测试失败') AS 'Case6 Testing Result';
--	SET @retailTradeID_C = last_insert_id();
--	SET @sSN = '';
--	SELECT F_SN INTO @sSN FROM t_retailtrade WHERE F_ID = @retailTradeID_C;
--	SELECT IF(RIGHT(@sSN, 1) = '2', '测试成功', '测试失败') AS 'Case6 Testing Result';
-- 
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID_C;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID_B;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID_A;



SELECT '------- Case7: 没有子商户号的公司进行零售，使用的是微信支付，错误信息：支付失败，公司的子商户号未设置！不能进行微信支付 ---------' AS 'Case7';
UPDATE nbr_bx.t_company SET F_Submchid = NULL WHERE F_ID = 1;
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iVipID = 1; 
SET @iLocalSN = 33;
SET @iPOS_ID = 5;
SET @sLogo = 'url=asha42soadigmnalskd';
SET @dtSaleDatetime = NOW();
SET @iStaffID = 2;
SET @iPaymentType = 5;
SET @iPaymentAccount = 'Q123456';
SET @iStatus = 1;
SET @sRemark = '...............';
SET @iSourceID = -1;
SET @fAmount = 15;
SET @fAmountCash = 5;
SET @fAmountAlipay = 0;
SET @fAmountWeChat = 10;
SET @fAmount1 = 0;
SET @fAmount2 = 0;
SET @fAmount3 = 0;
SET @fAmount4 = 0;
SET @fAmount5 = 0;
SET @iSmallSheetID = 1;
SET @sAliPayOrderSN = '123456';
SET @sWxOrderSN = NULL;
SET @sWxTradeNO = NULL;
SET @sWxRefundNO = NULL;
SET @sWxRefundDesc = '14445565';
SET @sWxRefundSubMchID = '1';
SET @sSN = 'LS2018010101010100013333';
SET @dCouponAmount = 0.1;
SET @sConsumerOpenID = NULL;
SET @shopID = 2;

CALL SP_RetailTrade_UploadTrade(@iErrorCode, @sErrorMsg, @iVipID, @iLocalSN, @iPOS_ID, @sLogo, @dtSaleDatetime, @iStaffID, @iPaymentType, @iPaymentAccount, @iStatus, @sRemark, @iSourceID, @fAmount, @fAmountCash, @fAmountAlipay, @fAmountWeChat
	, @fAmount1, @fAmount2, @fAmount3, @fAmount4, @fAmount5, @iSmallSheetID, @sAliPayOrderSN, @sWxOrderSN, @sWxTradeNO, @sWxRefundNO, @sWxRefundDesc, @sWxRefundSubMchID, @sSN, @dCouponAmount, @sConsumerOpenID, @shopID);

SELECT @sErrorMsg;
SELECT 1 FROM t_RetailTrade 
	WHERE F_LocalSN = @iLocalSN
	AND F_POS_ID = @iPOS_ID
	AND F_Logo = @sLogo
	AND F_StaffID = @iStaffID
	AND F_PaymentType = @iPaymentType
	AND F_PaymentAccount = @iPaymentAccount
	AND F_Remark = @sRemark
	AND F_SourceID = @iSourceID
	AND F_Amount = @fAmount
	AND F_AmountCash = @fAmountCash
	AND F_AmountAlipay = @fAmountAlipay
	AND F_AmountWeChat = @fAmountWeChat
	AND F_Amount1 = @fAmount1
	AND F_Amount2 = @fAmount2
	AND F_Amount3 = @fAmount3
	AND F_Amount4 = @fAmount4
	AND F_Amount5 = @fAmount5
	AND F_SmallSheetID = @iSmallSheetID
	AND F_AliPayOrderSN = @sAliPayOrderSN
	AND F_WxRefundDesc = @sWxRefundDesc
	AND F_WxRefundSubMchID = @sWxRefundSubMchID
	AND F_CouponAmount = @dCouponAmount
AND F_ConsumerOpenID IS NULL;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '支付失败，公司的子商户号未设置！不能进行微信支付', '测试成功', '测试失败') AS 'Case7 Testing Result';

UPDATE nbr_bx.t_company SET F_Submchid = '1523999791' WHERE F_ID = 1;



SELECT '-------------------- Case8: iSourceID为存在的零售单ID，但是该零售单的交易时间已经大于一年，退货失败 -------------------------' AS 'Case8';

INSERT INTO T_VIP (F_ICID,F_CardID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile)
VALUES ('1244424',1,'Ada','1224126@bx.vip',10,100,'广州',1,
'2017-08-06',100,'2017-08-08 23:59:10','123343432312');
SET @iVipID = Last_insert_id(); 

INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID, F_ShopID)
VALUES (NULL,'LS201906100001',198,1,'url=ashasoadigmxnalskd',DATE_SUB(now(), INTERVAL 367 DAY),2,1,0,1,'........',now(),1.5,1.1,0,0,0,0,0,0,0,2, 2);
SET @RetailTradeID_A = last_insert_id();
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iLocalSN = 44;
SET @iPOS_ID = 5;
SET @sLogo = 'url=asha42soadigmnalskd';
SET @dtSaleDatetime = NOW();
SET @iStaffID = 2;
SET @iPaymentType = 1;
SET @iPaymentAccount = 'Q123456';
SET @iStatus = 1;
SET @sRemark = '...............';
SET @iSourceID = @RetailTradeID_A;
SET @fAmount = 50;
SET @fAmountCash = 0;
SET @fAmountAlipay = 0;
SET @fAmountWeChat = 0;
SET @fAmount1 = 50;
SET @fAmount2 = 0;
SET @fAmount3 = 0;
SET @fAmount4 = 8;
SET @fAmount5 = 0;
SET @iSmallSheetID = 1;
SET @sAliPayOrderSN = '123456';
SET @sWxOrderSN = '123456';
SET @sWxTradeNO = NULL;
SET @sWxRefundNO = NULL;
SET @sWxRefundDesc = NULL;
SET @sWxRefundSubMchID = NULL;
SET @sSN = 'LS2018010101010100013333';
SET @dCouponAmount = 0.1;
SET @sConsumerOpenID = NULL;
SET @shopID = 2;

CALL SP_RetailTrade_UploadTrade(@iErrorCode, @sErrorMsg, @iVipID, @iLocalSN, @iPOS_ID, @sLogo, @dtSaleDatetime, @iStaffID, @iPaymentType, @iPaymentAccount, @iStatus, @sRemark, @iSourceID, @fAmount, @fAmountCash, @fAmountAlipay, @fAmountWeChat
	, @fAmount1, @fAmount2, @fAmount3, @fAmount4, @fAmount5, @iSmallSheetID, @sAliPayOrderSN, @sWxOrderSN, @sWxTradeNO, @sWxRefundNO, @sWxRefundDesc, @sWxRefundSubMchID, @sSN, @dCouponAmount, @sConsumerOpenID, @shopID);
	
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '交易时间超过一年的订单禁止退款', '测试成功', '测试失败') AS 'Case8 Testing Result';

DELETE FROM t_vippointhistory WHERE F_VIP_ID = @iVipID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID_A;
DELETE FROM t_vip WHERE F_ID = @iVipID;


SELECT '-------------------- Case9: iSourceID为存在的零售单ID，支付宝退款金额大于零售时的支付宝支付金额 -------------------------' AS 'Case9';
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID, F_ShopID)
VALUES (NULL,'LS201906100001',198,1,'url=ashasoadigmxnalskd',DATE_SUB(now(), INTERVAL 1 DAY),2,6,0,1,'........',now(),100,0,50,50,0,0,0,0,0,2,2);
SET @RetailTradeID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iVipID = NULL;
SET @iLocalSN = 44;
SET @iPOS_ID = 5;
SET @sLogo = 'url=asha42soadigmnalskd';
SET @dtSaleDatetime = NOW();
SET @iStaffID = 2;
SET @iPaymentType = 6;
SET @iPaymentAccount = 'Q123456';
SET @iStatus = 1;
SET @sRemark = '...............';
SET @iSourceID = @RetailTradeID;
SET @fAmount = 100;
SET @fAmountCash = 0;
SET @fAmountAlipay = 60;
SET @fAmountWeChat = 40;
SET @fAmount1 = 0;
SET @fAmount2 = 0;
SET @fAmount3 = 0;
SET @fAmount4 = 0;
SET @fAmount5 = 0;
SET @iSmallSheetID = 1;
SET @sAliPayOrderSN = '123456';
SET @sWxOrderSN = '123456';
SET @sWxTradeNO = NULL;
SET @sWxRefundNO = NULL;
SET @sWxRefundDesc = NULL;
SET @sWxRefundSubMchID = NULL;
SET @sSN = 'LS2018010101010100015555';
SET @dCouponAmount = 0.1;
SET @sConsumerOpenID = NULL;
SET @shopID = 2;

CALL SP_RetailTrade_UploadTrade(@iErrorCode, @sErrorMsg, @iVipID, @iLocalSN, @iPOS_ID, @sLogo, @dtSaleDatetime, @iStaffID, @iPaymentType, @iPaymentAccount, @iStatus, @sRemark, @iSourceID, @fAmount, @fAmountCash, @fAmountAlipay, @fAmountWeChat
	, @fAmount1, @fAmount2, @fAmount3, @fAmount4, @fAmount5, @iSmallSheetID, @sAliPayOrderSN, @sWxOrderSN, @sWxTradeNO, @sWxRefundNO, @sWxRefundDesc, @sWxRefundSubMchID, @sSN, @dCouponAmount, @sConsumerOpenID, @shopID);
	
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '退款失败，支付宝退款金额不能比零售时支付宝支付的金额多', '测试成功', '测试失败') AS 'Case9 Testing Result';

DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;

SELECT '-------------------- Case10: iSourceID为存在的零售单ID，微信退款金额大于零售时的微信金额 -------------------------' AS 'Case10';
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID, F_ShopID)
VALUES (NULL,'LS201906100001',198,1,'url=ashasoadigmxnalskd',DATE_SUB(now(), INTERVAL 1 DAY),2,6,0,1,'........',now(),100,0,50,50,0,0,0,0,0,2, 2);
SET @RetailTradeID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iVipID = NULL;
SET @iLocalSN = 44;
SET @iPOS_ID = 5;
SET @sLogo = 'url=asha42soadigmnalskd';
SET @dtSaleDatetime = NOW();
SET @iStaffID = 2;
SET @iPaymentType = 6;
SET @iPaymentAccount = 'Q123456';
SET @iStatus = 1;
SET @sRemark = '...............';
SET @iSourceID = @RetailTradeID;
SET @fAmount = 100;
SET @fAmountCash = 0;
SET @fAmountAlipay = 40;
SET @fAmountWeChat = 60;
SET @fAmount1 = 0;
SET @fAmount2 = 0;
SET @fAmount3 = 0;
SET @fAmount4 = 0;
SET @fAmount5 = 0;
SET @iSmallSheetID = 1;
SET @sAliPayOrderSN = '123456';
SET @sWxOrderSN = '123456';
SET @sWxTradeNO = NULL;
SET @sWxRefundNO = NULL;
SET @sWxRefundDesc = NULL;
SET @sWxRefundSubMchID = NULL;
SET @sSN = 'LS2018010101010100016666';
SET @dCouponAmount = 0.1;
SET @sConsumerOpenID = NULL;
SET @shopID = 2;

CALL SP_RetailTrade_UploadTrade(@iErrorCode, @sErrorMsg, @iVipID, @iLocalSN, @iPOS_ID, @sLogo, @dtSaleDatetime, @iStaffID, @iPaymentType, @iPaymentAccount, @iStatus, @sRemark, @iSourceID, @fAmount, @fAmountCash, @fAmountAlipay, @fAmountWeChat
	, @fAmount1, @fAmount2, @fAmount3, @fAmount4, @fAmount5, @iSmallSheetID, @sAliPayOrderSN, @sWxOrderSN, @sWxTradeNO, @sWxRefundNO, @sWxRefundDesc, @sWxRefundSubMchID, @sSN, @dCouponAmount, @sConsumerOpenID, @shopID);
	
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '退款失败，微信退款金额不能比零售时微信支付的金额多', '测试成功', '测试失败') AS 'Case10 Testing Result';

DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_vip WHERE F_ID = @iVipID;

SELECT '-------------------- Case11: iSourceID为SQLite中的临时零售单的F_ID字段（断网情况），进行退货操作,退货成功 -------------------------' AS 'Case11';

INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID, F_ShopID)
VALUES (NULL,'LS2019061001010100011239',100000000,1,'url=ashasoadigmxnalskd',now(),2,1,0,1,'........',now(),1.5,1.1,0,0,0,0,0,0,0,2,2);
SET @RetailTradeID_A = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iLocalSN = 44;
SET @iPOS_ID = 5;
SET @sLogo = 'url=asha42soadigmnalskd';
SET @dtSaleDatetime = NOW();
SET @iStaffID = 2;
SET @iPaymentType = 1;
SET @iPaymentAccount = 'Q123456';
SET @iStatus = 1;
SET @sRemark = '...............';
SET @iSourceID = 100000000;
SET @fAmount = 50;
SET @fAmountCash = 0;
SET @fAmountAlipay = 0;
SET @fAmountWeChat = 0;
SET @fAmount1 = 50;
SET @fAmount2 = 0;
SET @fAmount3 = 0;
SET @fAmount4 = 8;
SET @fAmount5 = 0;
SET @iSmallSheetID = 1;
SET @sAliPayOrderSN = '123456';
SET @sWxOrderSN = '123456';
SET @sWxTradeNO = NULL;
SET @sWxRefundNO = NULL;
SET @sWxRefundDesc = NULL;
SET @sWxRefundSubMchID = NULL;
SET @sSN = 'LS2019061001010100011239_1';
SET @dCouponAmount = 0.1;
SET @sConsumerOpenID = NULL;
SET @shopID = 2;

CALL SP_RetailTrade_UploadTrade(@iErrorCode, @sErrorMsg, @iVipID, @iLocalSN, @iPOS_ID, @sLogo, @dtSaleDatetime, @iStaffID, @iPaymentType, @iPaymentAccount, @iStatus, @sRemark, @iSourceID, @fAmount, @fAmountCash, @fAmountAlipay, @fAmountWeChat
	, @fAmount1, @fAmount2, @fAmount3, @fAmount4, @fAmount5, @iSmallSheetID, @sAliPayOrderSN, @sWxOrderSN, @sWxTradeNO, @sWxRefundNO, @sWxRefundDesc, @sWxRefundSubMchID, @sSN, @dCouponAmount, @sConsumerOpenID, @shopID);
SELECT @iErrorCode;
SET @last_id = 0;
SELECT F_ID INTO @last_id FROM t_RetailTrade WHERE F_SN = @sSN;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case11 Testing Result';

DELETE FROM t_retailtrade WHERE F_ID = @RetailTradeID_A;
DELETE FROM t_retailtrade WHERE F_ID = @last_id;

SELECT '-------------------- Case12: 状态为2(包含已删除商品)的零售单和零售型退货单正常创建	 -------------------------' AS 'Case12';

INSERT INTO T_VIP (F_ICID,F_CardID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile)
VALUES ('124124',1,'Ada','1232226@bx.vip',0,0,'广州',1,
'2017-08-06',0,'2017-08-08 23:59:10','123235232312');
SET @iVipID = Last_insert_id(); 
--		
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iLocalSN = 2222333;
SET @iPOS_ID = 5;
SET @dtSaleDatetime = NOW();
SET @sLogo = 'url=asha42soadigmnalskd';
SET @iStaffID = 2;
SET @iPaymentType = 1;
SET @iPaymentAccount = 'Q123456';
SET @iStatus = 2;
SET @sRemark = '...............';
SET @iSourceID = -1;
SET @fAmount = 100;
SET @fAmountCash = 0;
SET @fAmountAlipay = 0;
SET @fAmountWeChat = 0;
SET @fAmount1 = 100;
SET @fAmount2 = 0;
SET @fAmount3 = 0;
SET @fAmount4 = 8;
SET @fAmount5 = 0;
SET @iSmallSheetID = 1;
SET @sAliPayOrderSN = '123456';
SET @sWxOrderSN = '123456';
SET @sWxTradeNO = NULL;
SET @sWxRefundNO = '123456';
SET @sWxRefundDesc = '14445565';
SET @sWxRefundSubMchID = NULL;
SET @sSN = 'LS2018010101010100011235';
SET @dCouponAmount = 0.1;
SET @sConsumerOpenID = NULL;
SET @shopID = 2;

CALL SP_RetailTrade_UploadTrade(@iErrorCode, @sErrorMsg, @iVipID, @iLocalSN, @iPOS_ID, @sLogo, @dtSaleDatetime, @iStaffID, @iPaymentType, @iPaymentAccount, @iStatus, @sRemark, @iSourceID, @fAmount, @fAmountCash, @fAmountAlipay, @fAmountWeChat
	, @fAmount1, @fAmount2, @fAmount3, @fAmount4, @fAmount5, @iSmallSheetID, @sAliPayOrderSN, @sWxOrderSN, @sWxTradeNO, @sWxRefundNO, @sWxRefundDesc, @sWxRefundSubMchID, @sSN, @dCouponAmount, @sConsumerOpenID, @shopID);

SET @iRetailTradeID = 0; 
SELECT F_ID into @iRetailTradeID FROM t_retailtrade WHERE F_VipID = @iVipID;
SELECT @iRetailTradeID;
SET @sSN = 'LS2018010101010100011235_1';
SET @iLocalSN = 2222334;
SELECT @sErrorMsg;

CALL SP_RetailTrade_UploadTrade(@iErrorCode, @sErrorMsg, @iVipID, @iLocalSN, @iPOS_ID, @sLogo, @dtSaleDatetime, @iStaffID, @iPaymentType, @iPaymentAccount, @iStatus, @sRemark, @iRetailTradeID, @fAmount, @fAmountCash, @fAmountAlipay, @fAmountWeChat
	, @fAmount1, @fAmount2, @fAmount3, @fAmount4, @fAmount5, @iSmallSheetID, @sAliPayOrderSN, @sWxOrderSN, @sWxTradeNO, @sWxRefundNO, @sWxRefundDesc, @sWxRefundSubMchID, @sSN, @dCouponAmount, @sConsumerOpenID, @shopID);

SET @iReturnRetailTradeID = 0;
SELECT F_ID into @iReturnRetailTradeID FROM t_retailtrade WHERE F_SourceID = @iRetailTradeID;
SELECT @iReturnRetailTradeID;
SELECT @sErrorMsg;
--	
SELECT 1 FROM t_RetailTrade WHERE F_ID = @iRetailTradeID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case12 Testing Result';

SELECT 1 FROM t_RetailTrade WHERE F_ID = @iReturnRetailTradeID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case12 Testing Result';


DELETE FROM t_vippointhistory WHERE F_VIP_ID = @iVipID;
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_bonusconsumehistory WHERE F_VipID = @iVipID;
DELETE FROM t_vip WHERE F_ID = @iVipID;

SELECT '-------------------- Case13: VIP不存在 -------------------------' AS 'Case13';	
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iLocalSN = 2222333;
SET @iPOS_ID = 5;
SET @dtSaleDatetime = NOW();
SET @sLogo = 'url=asha42soadigmnalskd';
SET @iStaffID = 2;
SET @iPaymentType = 1;
SET @iPaymentAccount = 'Q123456';
SET @iStatus = 2;
SET @sRemark = '...............';
SET @iSourceID = -1;
SET @fAmount = 100;
SET @fAmountCash = 0;
SET @fAmountAlipay = 0;
SET @fAmountWeChat = 0;
SET @fAmount1 = 100;
SET @fAmount2 = 0;
SET @fAmount3 = 0;
SET @fAmount4 = 8;
SET @fAmount5 = 0;
SET @iSmallSheetID = 1;
SET @sAliPayOrderSN = '123456';
SET @sWxOrderSN = '123456';
SET @sWxTradeNO = NULL;
SET @sWxRefundNO = '123456';
SET @sWxRefundDesc = '14445565';
SET @sWxRefundSubMchID = NULL;
SET @sSN = 'LS2018010101010100011235';
SET @iVipID = 9999999;
SET @shopID = 2;
-- 
CALL SP_RetailTrade_UploadTrade(@iErrorCode, @sErrorMsg, @iVipID, @iLocalSN, @iPOS_ID, @sLogo, @dtSaleDatetime, @iStaffID, @iPaymentType, @iPaymentAccount, @iStatus, @sRemark, @iRetailTradeID, @fAmount, @fAmountCash, @fAmountAlipay, @fAmountWeChat
	, @fAmount1, @fAmount2, @fAmount3, @fAmount4, @fAmount5, @iSmallSheetID, @sAliPayOrderSN, @sWxOrderSN, @sWxTradeNO, @sWxRefundNO, @sWxRefundDesc, @sWxRefundSubMchID, @sSN, @dCouponAmount, @sConsumerOpenID, @shopID);
-- 
SELECT IF(@iErrorCode = 2, '测试成功', '测试失败') AS 'Case13 Testing Result';

SELECT '-------------------- Case14: POS不存在 -------------------------' AS 'Case14';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iLocalSN = 2222333;
SET @iPOS_ID = -1;
SET @dtSaleDatetime = NOW();
SET @sLogo = 'url=asha42soadigmnalskd';
SET @iStaffID = 2;
SET @iPaymentType = 1;
SET @iPaymentAccount = 'Q123456';
SET @iStatus = 2;
SET @sRemark = '...............';
SET @iSourceID = -1;
SET @fAmount = 100;
SET @fAmountCash = 0;
SET @fAmountAlipay = 0;
SET @fAmountWeChat = 0;
SET @fAmount1 = 100;
SET @fAmount2 = 0;
SET @fAmount3 = 0;
SET @fAmount4 = 8;
SET @fAmount5 = 0;
SET @iSmallSheetID = 1;
SET @sAliPayOrderSN = '123456';
SET @sWxOrderSN = '123456';
SET @sWxTradeNO = NULL;
SET @sWxRefundNO = '123456';
SET @sWxRefundDesc = '14445565';
SET @sWxRefundSubMchID = NULL;
SET @sSN = 'LS2018010101010100011235';
SET @iVipID = 1;
SET @shopID = 2;
-- 
CALL SP_RetailTrade_UploadTrade(@iErrorCode, @sErrorMsg, @iVipID, @iLocalSN, @iPOS_ID, @sLogo, @dtSaleDatetime, @iStaffID, @iPaymentType, @iPaymentAccount, @iStatus, @sRemark, @iRetailTradeID, @fAmount, @fAmountCash, @fAmountAlipay, @fAmountWeChat
	, @fAmount1, @fAmount2, @fAmount3, @fAmount4, @fAmount5, @iSmallSheetID, @sAliPayOrderSN, @sWxOrderSN, @sWxTradeNO, @sWxRefundNO, @sWxRefundDesc, @sWxRefundSubMchID, @sSN, @dCouponAmount, @sConsumerOpenID, @shopID);
-- 
SELECT IF(@iErrorCode = 2, '测试成功', '测试失败') AS 'Case14 Testing Result';


SELECT '-------------------- Case16: 员工不存在 -------------------------' AS 'Case16';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iLocalSN = 2222333;
SET @iPOS_ID = 1;
SET @dtSaleDatetime = NOW();
SET @sLogo = 'url=asha42soadigmnalskd';
SET @iStaffID = -2;
SET @iPaymentType = 1;
SET @iPaymentAccount = 'Q123456';
SET @iStatus = 2;
SET @sRemark = '...............';
SET @iSourceID = -1;
SET @fAmount = 100;
SET @fAmountCash = 0;
SET @fAmountAlipay = 0;
SET @fAmountWeChat = 0;
SET @fAmount1 = 100;
SET @fAmount2 = 0;
SET @fAmount3 = 0;
SET @fAmount4 = 8;
SET @fAmount5 = 0;
SET @iSmallSheetID = 1;
SET @sAliPayOrderSN = '123456';
SET @sWxOrderSN = '123456';
SET @sWxTradeNO = NULL;
SET @sWxRefundNO = '123456';
SET @sWxRefundDesc = '14445565';
SET @sWxRefundSubMchID = NULL;
SET @sSN = 'LS2018010101010100011235';
SET @iVipID = 1;
SET @shopID = 2;
-- 
CALL SP_RetailTrade_UploadTrade(@iErrorCode, @sErrorMsg, @iVipID, @iLocalSN, @iPOS_ID, @sLogo, @dtSaleDatetime, @iStaffID, @iPaymentType, @iPaymentAccount, @iStatus, @sRemark, @iRetailTradeID, @fAmount, @fAmountCash, @fAmountAlipay, @fAmountWeChat
	, @fAmount1, @fAmount2, @fAmount3, @fAmount4, @fAmount5, @iSmallSheetID, @sAliPayOrderSN, @sWxOrderSN, @sWxTradeNO, @sWxRefundNO, @sWxRefundDesc, @sWxRefundSubMchID, @sSN, @dCouponAmount, @sConsumerOpenID, @shopID);
-- 
SELECT IF(@iErrorCode = 2, '测试成功', '测试失败') AS 'Case16 Testing Result';

SELECT '-------------------- Case17: 退会员A的零售单。传递的VipID为会员B.实际操作的还是会员A -------------------------' AS 'Case17';
-- 
INSERT INTO T_VIP (F_SN, F_ICID,F_Name,F_Email,F_District,F_Category,F_Birthday, F_Mobile, F_ConsumeTimes, F_ConsumeAmount, F_CardID)
VALUES ('F_SN1'/*F_SN*/,'1241224'/*F_ICID*/,'Ad4a'/*F_Name*/,'12322246@bx.vip'/*F_Email*/,'广州'/*F_District*/,1/*F_Category*/,'2017-08-06'/*F_Birthday*/,'1232435232312'/*F_Mobile*/, 1/*F_CousumeTimes*/, 150/*F_CousumeAmount*/, 1/*F_CardID*/);
SET @iVipID_A = Last_insert_id(); 
-- 
INSERT INTO T_VIP (F_SN,F_ICID,F_Name,F_Email,F_District,F_Category,F_Birthday, F_Mobile, F_CardID)
VALUES ('F_SN2'/*F_SN*/,'12412242'/*F_ICID*/,'Ad44a'/*F_Name*/,'123222246@bx.vip'/*F_Email*/,'广州'/*F_District*/,1/*F_Category*/,'2017-08-06'/*F_Birthday*/,'12324325232312'/*F_Mobile*/, 1/*F_CardID*/);
SET @iVipID_B = Last_insert_id(); 
-- 
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_StaffID,F_PaymentType,F_PaymentAccount,F_Status,F_Remark,F_Amount,F_AmountCash,F_SmallSheetID, F_SourceID, F_SaleDatetime,F_AmountAlipay, F_AmountWeChat, F_ShopID)
VALUES (@iVipID_A/*F_VipID*/,'LS201906100101010001111239'/*F_SN*/,100000000/*F_LocalSN*/,1/*F_POS_ID*/,'url=ashasoadigmxnalskd'/*F_Logo*/,2/*F_StaffID*/,1/*F_PaymentType*/,0/*F_PaymentAccount*/,
		1/*F_Status*/,'........'/*F_Remark*/,150/*F_Amount*/,150/*F_AmountCash*/,2/*F_SmallSheetID*/, -1/*F_SourceID*/, now()/*F_SaleDatetime*/,0/*F_AmountAlipay*/,0/*F_AmountWeChat*/, 2);
SET @RetailTradeID = last_insert_id();
-- 退货前,获取VIP A,B的消费次数，总金额。
SET @iBeforeConsumeTimes_VipA = 0;
SELECT F_ConsumeTimes INTO @iBeforeConsumeTimes_VipA FROM t_vip WHERE F_ID = @iVipID_A;
SET @dBeforeConsumeAmount_VipA = 0;
SELECT F_ConsumeAmount INTO @dBeforeConsumeAmount_VipA FROM t_vip WHERE F_ID = @iVipID_A;

SET @iBeforeConsumeTimes_VipB = 0;
SELECT F_ConsumeTimes INTO @iBeforeConsumeTimes_VipB FROM t_vip WHERE F_ID = @iVipID_B;
SET @dBeforeConsumeAmount_VipB = 0;
SELECT F_ConsumeAmount INTO @dBeforeConsumeAmount_VipB FROM t_vip WHERE F_ID = @iVipID_B;
-- 
SET @iVipID = @iVipID_A; 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iLocalSN = 222433;
SET @iPOS_ID = 5;
SET @dtSaleDatetime = NOW();
SET @sLogo = 'url=asha42soadigmnalskd';
SET @iStaffID = 2;
SET @iPaymentType = 1;
SET @iPaymentAccount = 'Q123456';
SET @iStatus = 1;
SET @sRemark = '...............';
SET @iSourceID = @RetailTradeID;
SET @fAmount = 100;
SET @fAmountCash = 0;
SET @fAmountAlipay = 0;
SET @fAmountWeChat = 0;
SET @fAmount1 = 100;
SET @fAmount2 = 0;
SET @fAmount3 = 0;
SET @fAmount4 = 8;
SET @fAmount5 = 0;
SET @iSmallSheetID = 1;
SET @sAliPayOrderSN = '123456';
SET @sWxOrderSN = '123456';
SET @sWxTradeNO = NULL;
SET @sWxRefundNO = '123456';
SET @sWxRefundDesc = '14445565';
SET @sWxRefundSubMchID = NULL;
SET @sSN = 'LS2018010101010100011234';
SET @dCouponAmount = 0.1;
SET @sConsumerOpenID = NULL;
SET @shopID = 2;

CALL SP_RetailTrade_UploadTrade(@iErrorCode, @sErrorMsg, @iVipID, @iLocalSN, @iPOS_ID, @sLogo, @dtSaleDatetime, @iStaffID, @iPaymentType, @iPaymentAccount, @iStatus, @sRemark, @iSourceID, @fAmount, @fAmountCash, @fAmountAlipay, @fAmountWeChat
	, @fAmount1, @fAmount2, @fAmount3, @fAmount4, @fAmount5, @iSmallSheetID, @sAliPayOrderSN, @sWxOrderSN, @sWxTradeNO, @sWxRefundNO, @sWxRefundDesc, @sWxRefundSubMchID, @sSN, @dCouponAmount, @sConsumerOpenID, @shopID);
SET @iReturnRetailTradeID = 0;
SELECT F_ID INTO @iReturnRetailTradeID FROM t_retailtrade WHERE F_LocalSN = @iLocalSN AND F_POS_ID = @iPOS_ID;
-- 
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case17 Testing Result';
-- 检查VIP,A,B的消费次数和消费金额是否正确的改变
SET @iAfterConsumeTimes_VipA = 0;
SELECT F_ConsumeTimes INTO @iAfterConsumeTimes_VipA FROM t_vip WHERE F_ID = @iVipID_A;
SET @dAfterConsumeAmount_VipA = 0;
SELECT F_ConsumeAmount INTO @dAfterConsumeAmount_VipA FROM t_vip WHERE F_ID = @iVipID_A;

SET @iAfterConsumeTimes_VipB = 0;
SELECT F_ConsumeTimes INTO @iAfterConsumeTimes_VipB FROM t_vip WHERE F_ID = @iVipID_B;
SET @dAfterConsumeAmount_VipB = 0;
SELECT F_ConsumeAmount INTO @dAfterConsumeAmount_VipB FROM t_vip WHERE F_ID = @iVipID_B;

SELECT IF(@iBeforeConsumeTimes_VipA = @iAfterConsumeTimes_VipA + 1 AND @dBeforeConsumeAmount_VipA = @dAfterConsumeAmount_VipA + @fAmount, '测试成功', '测试失败,未能成功的修改会员A的消费信息') AS 'Case17 Testing Result';
SELECT IF(@iBeforeConsumeTimes_VipB = @iAfterConsumeTimes_VipB AND @dBeforeConsumeAmount_VipB = @dAfterConsumeAmount_VipB, '测试成功', '测试失败,修改到了会员B的信息') AS 'Case17 Testing Result';
-- 
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @RetailTradeID;
DELETE FROM t_vip WHERE F_ID = @iVipID_B;
DELETE FROM t_vip WHERE F_ID = @iVipID_A;

SELECT '-------------------- Case18: iSourceID为存在的零售单ID，微信和现金混合退款，大于源单的现金和微信混合支付，退款失败  -------------------------' AS 'Case4';

INSERT INTO T_VIP (F_ICID,F_CardID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile)
VALUES ('1244424',1,'Ada','1224126@bx.vip',10,100,'广州',1,
'2017-08-06',100,'2017-08-08 23:59:10','123343432312');
SET @iVipID = Last_insert_id(); 

INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID, F_ShopID)
VALUES (@iVipID,'LS201906100001',198,1,'url=ashasoadigmxnalskd',now(),2,1,0,1,'........',now(),50,25,0,25,0,0,0,0,0,2, 2);
SET @RetailTradeID_A = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iLocalSN = 44;
SET @iPOS_ID = 5;
SET @sLogo = 'url=asha42soadigmnalskd';
SET @dtSaleDatetime = NOW();
SET @iStaffID = 2;
SET @iPaymentType = 5;
SET @iPaymentAccount = 'Q123456';
SET @iStatus = 1;
SET @sRemark = '...............';
SET @iSourceID = @RetailTradeID_A;
SET @fAmount = 60;
SET @fAmountCash = 30;
SET @fAmountAlipay = 0;
SET @fAmountWeChat = 30;
SET @fAmount1 = 50;
SET @fAmount2 = 0;
SET @fAmount3 = 0;
SET @fAmount4 = 8;
SET @fAmount5 = 0;
SET @iSmallSheetID = 1;
SET @sAliPayOrderSN = '123456';
SET @sWxOrderSN = '123456';
SET @sWxTradeNO = NULL;
SET @sWxRefundNO = NULL;
SET @sWxRefundDesc = NULL;
SET @sWxRefundSubMchID = NULL;
SET @sSN = 'LS2018010101010100018888';
SET @dCouponAmount = 0.1;
SET @sConsumerOpenID = NULL;
SET @shopID = 2;

CALL SP_RetailTrade_UploadTrade(@iErrorCode, @sErrorMsg, @iVipID, @iLocalSN, @iPOS_ID, @sLogo, @dtSaleDatetime, @iStaffID, @iPaymentType, @iPaymentAccount, @iStatus, @sRemark, @iSourceID, @fAmount, @fAmountCash, @fAmountAlipay, @fAmountWeChat
	, @fAmount1, @fAmount2, @fAmount3, @fAmount4, @fAmount5, @iSmallSheetID, @sAliPayOrderSN, @sWxOrderSN, @sWxTradeNO, @sWxRefundNO, @sWxRefundDesc, @sWxRefundSubMchID, @sSN, @dCouponAmount, @sConsumerOpenID, @shopID);
SELECT @iErrorCode;
	
SELECT 1 FROM t_RetailTrade 
	WHERE F_LocalSN = @iLocalSN
	AND F_POS_ID = @iPOS_ID
	AND F_Logo = @sLogo
	AND F_StaffID = @iStaffID
	AND F_PaymentType = @iPaymentType
	AND F_PaymentAccount = @iPaymentAccount
	AND F_Remark = @sRemark
	AND F_SourceID = @iSourceID
	AND F_Amount = @fAmount
	AND F_AmountCash = @fAmountCash
	AND F_AmountAlipay = @fAmountAlipay
	AND F_AmountWeChat = @fAmountWeChat
	AND F_Amount1 = @fAmount1
	AND F_Amount2 = @fAmount2
	AND F_Amount3 = @fAmount3
	AND F_Amount4 = @fAmount4
	AND F_Amount5 = @fAmount5
	AND F_SmallSheetID = @iSmallSheetID
	AND F_AliPayOrderSN = @sAliPayOrderSN
	AND F_WxOrderSN = @sWxOrderSN
	AND F_CouponAmount = @dCouponAmount
	AND F_ConsumerOpenID IS NULL;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '退款失败，退款金额不能比零售时微信支付和现金支付的总金额多', '测试成功', '测试失败') AS 'Case4 Testing Result';

DELETE FROM t_retailtrade WHERE F_VipID = @iVipID;
DELETE FROM t_retailtrade WHERE F_ID = @RetailTradeID_A;
DELETE FROM t_vip WHERE F_ID = @iVipID;