SELECT '--------------------------------Test_SP_RetailTradePromoting_Create---------------------------------------';
SELECT '----------------Case1:正常添加----------------------' AS 'Case1';
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID, F_ShopID)
VALUES (1,'SN123483218x',99999,1,'url=ashasoadigmnalskd','2017-08-06',1,1,0,1,'........',now(),1.5,1.1,0,0,0,0,0,0,0,2,2);
SET @iTradeID = last_insert_id();
SET @iErrorCode = 0;
SET @sErrorMsg = '';


CALL SP_RetailTradePromoting_Create(@iErrorCode, @sErrorMsg, @iTradeID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_RetailTradePromoting WHERE F_TradeID = @iTradeID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Testing CASE1 Result';

SELECT '----------------Case2:重复添加----------------------' AS 'Case2';
SET @sErrorMsg = '';

CALL SP_RetailTradePromoting_Create(@iErrorCode, @sErrorMsg, @iTradeID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_RetailTradePromoting WHERE F_TradeID = @iTradeID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Testing CASE2 Result';


DELETE FROM t_retailtradepromoting WHERE F_ID = LAST_INSERT_ID();
DELETE FROM t_retailtrade WHERE F_ID = @iTradeID;

SELECT '----------------Case3:零售单ID不存在----------------------' AS 'Case3';
SET @iTradeID = -2;
SET @iErrorCode = 0;
SET @sErrorMsg = '';


CALL SP_RetailTradePromoting_Create(@iErrorCode, @sErrorMsg, @iTradeID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 2, '测试成功', '测试失败') AS 'Testing CASE1 Result';