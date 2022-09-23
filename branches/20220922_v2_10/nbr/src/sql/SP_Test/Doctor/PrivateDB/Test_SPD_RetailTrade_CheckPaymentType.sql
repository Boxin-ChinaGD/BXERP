SELECT '++++++++++++++++++ Test_SPD_RetailTrade_CheckPaymentType.sql ++++++++++++++++++++';

-- 正常测试
SELECT '-------------------- Case1:正常测试 -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckPaymentType(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';
-- 

-- 零售单的支付方式不是规定的8种支付方式
SELECT '-------------------- Case2:零售单的支付方式不是规定的8种支付方式 -------------------------' AS 'Case2';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
-- 插入一条零售单数据，测试完会删除
INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID,F_Status,F_SyncDatetime,F_Amount,F_SmallSheetID)
VALUES (now(),654321,3,'url=ashasoadigmnalskd','2017-08-06',1,-1,0,'........',-1,1,now(),128.6,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckPaymentType(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailTradeID, '的零售单的支付方式并不是规定8种的支付方式') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case2 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
-- 



SELECT '-------------------- Case3:源单是现金支付，那么退款方式不能为微信退款 -------------------------' AS 'Case3';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 零售单，现金支付
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (3, 'LS3017080801010100031236', 3, 2, 'url=ashasoadighhhhhhh', '11/30/2019 04:15:25 下午', 4, 1/*F_PaymentType*/, '0', 1, '很实用', -1, '11/28/2019 04:15:25 下午', 70000, 70000, 0, 0, 0, 0, 0, 0, 0, 3, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 退货单，微信退款
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (3, 'LS3017080801010100031236_1', 3, 1, 'url=ashasoadighhhhhhh', '11/30/2019 04:15:25 下午', 4, 4/*F_PaymentType*/, '0', 1, '很实用', @iRetailTradeID, '11/28/2019 04:15:25 下午', 70000, 0, 0, 70000, 0, 0, 0, 0, 0, 3, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iReturnRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckPaymentType(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@sErrorMsg = CONCAT('ID为', @iReturnRetailTradeID, '的退货型零售单的退款方式只能是现金退款，因为源单是现金支付') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case3 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;


-- 
SELECT '-------------------- Case4:源单是现金支付，那么退款方式不能为支付宝退款 -------------------------' AS 'Case4';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 零售单，现金支付
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (3, 'LS3017080801010100031236', 3, 2, 'url=ashasoadighhhhhhh', '11/30/2019 04:15:25 下午', 4, 1/*F_PaymentType*/, '0', 1, '很实用', -1, '11/28/2019 04:15:25 下午', 70000, 70000, 0, 0, 0, 0, 0, 0, 0, 3, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 退货单，支付宝退款
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (3, 'LS3017080801010100031236_1', 3, 1, 'url=ashasoadighhhhhhh', '11/30/2019 04:15:25 下午', 4, 2/*F_PaymentType*/, '0', 1, '很实用', @iRetailTradeID, '11/28/2019 04:15:25 下午', 70000, 0, 70000, 0, 0, 0, 0, 0, 0, 3, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iReturnRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckPaymentType(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@sErrorMsg = CONCAT('ID为', @iReturnRetailTradeID, '的退货型零售单的退款方式只能是现金退款，因为源单是现金支付') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case4 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;



SELECT '-------------------- Case5:源单是微信支付，那么退款方式不能为支付宝退款 -------------------------' AS 'Case5';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 零售单，微信支付
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (3, 'LS3017080801010100031236', 3, 2, 'url=ashasoadighhhhhhh', '11/30/2019 04:15:25 下午', 4, 4/*F_PaymentType*/, '0', 1, '很实用', -1, '11/28/2019 04:15:25 下午', 70000, 0, 0, 70000, 0, 0, 0, 0, 0, 3, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 退货单，支付宝退款
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (3, 'LS3017080801010100031236_1', 3, 1, 'url=ashasoadighhhhhhh', '11/30/2019 04:15:25 下午', 4, 2/*F_PaymentType*/, '0', 1, '很实用', @iRetailTradeID, '11/28/2019 04:15:25 下午', 70000, 0, 70000, 0, 0, 0, 0, 0, 0, 3, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iReturnRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckPaymentType(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@sErrorMsg = CONCAT('ID为', @iReturnRetailTradeID, '的退货型零售单的退款方式只能是微信或现金退款，因为源单是微信支付') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case5 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;


SELECT '-------------------- Case6:源单是微信支付，那么退款方式不能为现金加微信退款 -------------------------' AS 'Case6';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 零售单，微信支付
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (3, 'LS3017080801010100031236', 3, 2, 'url=ashasoadighhhhhhh', '11/30/2019 04:15:25 下午', 4, 4/*F_PaymentType*/, '0', 1, '很实用', -1, '11/28/2019 04:15:25 下午', 70000, 0, 0, 70000, 0, 0, 0, 0, 0, 3, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 退货单，微信加现金退款
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (3, 'LS3017080801010100031236_1', 3, 1, 'url=ashasoadighhhhhhh', '11/30/2019 04:15:25 下午', 4, 5/*F_PaymentType*/, '0', 1, '很实用', @iRetailTradeID, '11/28/2019 04:15:25 下午', 70000, 35000, 0, 35000, 0, 0, 0, 0, 0, 3, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iReturnRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckPaymentType(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@sErrorMsg = CONCAT('ID为', @iReturnRetailTradeID, '的退货型零售单的退款方式只能是微信或现金退款，因为源单是微信支付') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case6 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
-- 


SELECT '-------------------- Case7:源单是微信加现金支付，那么退款方式不能为支付宝退款 -------------------------' AS 'Case7';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 零售单，现金加微信支付
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (3, 'LS3017080801010100031236', 3, 2, 'url=ashasoadighhhhhhh', '11/30/2019 04:15:25 下午', 4, 5/*F_PaymentType*/, '0', 1, '很实用', -1, '11/28/2019 04:15:25 下午', 70000, 35000, 0, 35000, 0, 0, 0, 0, 0, 3, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 退货单，支付宝退款
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (3, 'LS3017080801010100031236_1', 3, 1, 'url=ashasoadighhhhhhh', '11/30/2019 04:15:25 下午', 4, 3/*F_PaymentType*/, '0', 1, '很实用', @iRetailTradeID, '11/28/2019 04:15:25 下午', 70000, 0, 70000, 0, 0, 0, 0, 0, 0, 3, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iReturnRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckPaymentType(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@sErrorMsg = CONCAT('ID为', @iReturnRetailTradeID, '的退货型零售单的退款方式只能是微信、现金或微信加现金退款，因为源单是微信+现金支付') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case7 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
-- 


SELECT '-------------------- Case8:源单是微信加现金支付，那么退款方式不能为支付宝加微信退款 -------------------------' AS 'Case8';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 零售单，现金加微信支付
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (3, 'LS3017080801010100031236', 3, 2, 'url=ashasoadighhhhhhh', '11/30/2019 04:15:25 下午', 4, 5/*F_PaymentType*/, '0', 1, '很实用', -1, '11/28/2019 04:15:25 下午', 70000, 35000, 0, 35000, 0, 0, 0, 0, 0, 3, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 退货单，支付宝加现金退款
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (3, 'LS3017080801010100031236_1', 3, 1, 'url=ashasoadighhhhhhh', '11/30/2019 04:15:25 下午', 4, 6/*F_PaymentType*/, '0', 1, '很实用', @iRetailTradeID, '11/28/2019 04:15:25 下午', 70000, 0, 35000, 35000, 0, 0, 0, 0, 0, 3, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iReturnRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckPaymentType(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@sErrorMsg = CONCAT('ID为', @iReturnRetailTradeID, '的退货型零售单的退款方式只能是微信、现金或微信加现金退款，因为源单是微信+现金支付') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case8 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iReturnRetailTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;