SELECT '++++++++++++++++++ Test_SPD_RetailTrade_CheckAmount.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:正常测试 -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTrade_CheckAmount(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT '-------------------- Case2:顾客支付金额不相等 -------------------------' AS 'Case2';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 插入一条零售单数据，测试完会删除
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300300031220', 741852963,2,'url=ashasoadigmnalskd','2019-5-18 18:45:31',2,7,0,
1,'........','2019-5-18 17:45:31',400,200,200,200,0,0,0,0,0,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckAmount(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailTradeID, '的零售单的支付金额和各种方式支付金额的总和不相等') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case2 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;

SELECT '-------------------- Case3:不存在现金支付，但是金额不为0 -------------------------' AS 'Case3';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 插入一条零售单数据，测试完会删除
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300300031220', 741852963,2,'url=ashasoadigmnalskd','2019-5-18 18:45:31',2,2,0,
1,'........','2019-5-18 17:45:31',400,200,200,0,0,0,0,0,0,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckAmount(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailTradeID, '的零售单的不存在现金支付方式，所以现金支付金额要为0') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case3 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;

SELECT '-------------------- Case4:存在混合支付包含现金支付，但是现金支付金额为0 -------------------------' AS 'Case4';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 插入一条零售单数据，测试完会删除
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300300031220', 741852963,2,'url=ashasoadigmnalskd','2019-5-18 18:45:31',2,3,0,
1,'........','2019-5-18 17:45:31',200,0,200,0,0,0,0,0,0,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckAmount(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailTradeID, '的零售单的存在现金支付方式，所以现金支付金额不能为0') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case4 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;

SELECT '-------------------- Case5:不存在支付宝支付，但是支付宝支付金额不为0 -------------------------' AS 'Case5';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 插入一条零售单数据，测试完会删除
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234', 741852963,2,'url=ashasoadigmnalskd','2019-5-18 18:45:31',2,4,0,
1,'........','2019-5-18 17:45:31',400,0,200,200,0,0,0,0,0,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckAmount(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailTradeID, '的零售单的不存在支付宝支付方式，所以支付宝支付金额要为0') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case5 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;

SELECT '-------------------- Case6:存在支付宝支付，但是支付宝支付金额为0 -------------------------' AS 'Case6';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 插入一条零售单数据，测试完会删除
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234', 741852963,2,'url=ashasoadigmnalskd','2019-5-18 18:45:31',2,3,0,
1,'........','2019-5-18 17:45:31',200,200,0,0,0,0,0,0,0,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckAmount(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailTradeID, '的零售单的存在支付宝支付方式，所以支付宝支付金额不能为0') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case6 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;

SELECT '-------------------- Case7:不存在微信支付，但是微信支付金额不为0 -------------------------' AS 'Case7';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 插入一条零售单数据，测试完会删除
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234', 741852963,2,'url=ashasoadigmnalskd','2019-5-18 18:45:31',2,2,0,
1,'........','2019-5-18 17:45:31',400,0,200,200,0,0,0,0,0,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckAmount(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailTradeID, '的零售单的不存在微信支付方式，所以微信支付金额要为0') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case7 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;

SELECT '-------------------- Case8:存在微信支付，但是微信支付金额为0 -------------------------' AS 'Case8';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 插入一条零售单数据，测试完会删除
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234', 741852963,2,'url=ashasoadigmnalskd','2019-5-18 18:45:31',2,4,0,
1,'........','2019-5-18 17:45:31',200,200,0,0,0,0,0,0,0,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckAmount(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailTradeID, '的零售单的存在微信支付方式，所以微信支付金额不能为0') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case8 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;

SELECT '-------------------- Case9:不存在Amount1支付，但是Amount1支付金额不为0 -------------------------' AS 'Case9';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 插入一条零售单数据，测试完会删除
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234', 741852963,2,'url=ashasoadigmnalskd','2019-5-18 18:45:31',2,4,0,
1,'........','2019-5-18 17:45:31',400,0,0,200,200,0,0,0,0,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckAmount(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailTradeID, '的零售单的不存在Amount1支付方式，所以Amount1支付金额要为0') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case9 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;

SELECT '-------------------- Case10:存在Amount1支付，但是Amount1支付金额为0 -------------------------' AS 'Case10';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 插入一条零售单数据，测试完会删除
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234', 741852963,2,'url=ashasoadigmnalskd','2019-5-18 18:45:31',2,9,0,
1,'........','2019-5-18 17:45:31',200,200,0,0,0,0,0,0,0,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckAmount(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailTradeID, '的零售单的存在Amount1支付方式，所以Amount1支付金额不能为0') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case10 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;

SELECT '-------------------- Case11:不存在Amount2支付，但是Amount2支付金额不为0 -------------------------' AS 'Case11';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 插入一条零售单数据，测试完会删除
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234', 741852963,2,'url=ashasoadigmnalskd','2019-5-18 18:45:31',2,4,0,
1,'........','2019-5-18 17:45:31',400,0,0,200,0,200,0,0,0,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckAmount(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailTradeID, '的零售单的不存在Amount2支付方式，所以Amount2支付金额要为0') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case11 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;

SELECT '-------------------- Case12:存在Amount2支付，但是Amount2支付金额为0 -------------------------' AS 'Case12';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 插入一条零售单数据，测试完会删除
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234', 741852963,2,'url=ashasoadigmnalskd','2019-5-18 18:45:31',2,17,0,
1,'........','2019-5-18 17:45:31',200,200,0,0,0,0,0,0,0,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckAmount(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailTradeID, '的零售单的存在Amount2支付方式，所以Amount2支付金额不能为0') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case12 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;

SELECT '-------------------- Case13:不存在Amount3支付，但是Amount3支付金额不为0 -------------------------' AS 'Case13';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 插入一条零售单数据，测试完会删除
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234', 741852963,2,'url=ashasoadigmnalskd','2019-5-18 18:45:31',2,4,0,
1,'........','2019-5-18 17:45:31',400,0,0,200,0,0,200,0,0,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckAmount(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailTradeID, '的零售单的不存在Amount3支付方式，所以Amount3支付金额要为0') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case13 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;

SELECT '-------------------- Case14:存在Amount3支付，但是Amount3支付金额为0 -------------------------' AS 'Case14';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 插入一条零售单数据，测试完会删除
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234', 741852963,2,'url=ashasoadigmnalskd','2019-5-18 18:45:31',2,33,0,
1,'........','2019-5-18 17:45:31',200,200,0,0,0,0,0,0,0,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckAmount(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailTradeID, '的零售单的存在Amount3支付方式，所以Amount3支付金额不能为0') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case14 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;

SELECT '-------------------- Case15:不存在Amount4支付，但是Amount4支付金额不为0 -------------------------' AS 'Case15';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 插入一条零售单数据，测试完会删除
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234', 741852963,2,'url=ashasoadigmnalskd','2019-5-18 18:45:31',2,4,0,
1,'........','2019-5-18 17:45:31',400,0,0,200,0,0,0,200,0,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckAmount(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailTradeID, '的零售单的不存在Amount4支付方式，所以Amount4支付金额要为0') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case15 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;

SELECT '-------------------- Case16:存在Amount4支付，但是Amount4支付金额为0 -------------------------' AS 'Case16';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 插入一条零售单数据，测试完会删除
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234', 741852963,2,'url=ashasoadigmnalskd','2019-5-18 18:45:31',2,65,0,
1,'........','2019-5-18 17:45:31',200,200,0,0,0,0,0,0,0,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckAmount(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailTradeID, '的零售单的存在Amount4支付方式，所以Amount4支付金额不能为0') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case16 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;


SELECT '-------------------- Case17:不存在Amount5支付，但是Amount5支付金额不为0 -------------------------' AS 'Case17';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 插入一条零售单数据，测试完会删除
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234', 741852963,2,'url=ashasoadigmnalskd','2019-5-18 18:45:31',2,4,0,
1,'........','2019-5-18 17:45:31',400,0,0,200,0,0,0,0,200,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckAmount(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailTradeID, '的零售单的不存在Amount5支付方式，所以Amount5支付金额要为0') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case17 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;

SELECT '-------------------- Case18:存在Amount5支付，但是Amount5支付金额为0 -------------------------' AS 'Case18';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 插入一条零售单数据，测试完会删除
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID)
VALUES (1,'LS2019090412300100011234', 741852963,2,'url=ashasoadigmnalskd','2019-5-18 18:45:31',2,129,0,
1,'........','2019-5-18 17:45:31',200,200,0,0,0,0,0,0,0,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 
CALL SPD_RetailTrade_CheckAmount(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailTradeID, '的零售单的存在Amount5支付方式，所以Amount5支付金额不能为0') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case18 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;