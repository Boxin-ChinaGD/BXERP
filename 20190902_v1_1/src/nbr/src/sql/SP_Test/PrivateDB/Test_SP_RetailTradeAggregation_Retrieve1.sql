SELECT '++++++++++++++++++Test_SP_RetailTradeAggregation_Retrieve1.sql+++++++++++++++++++++++';

SELECT '-----------------Case1: 收银员交班后，查询收银汇总------------------' AS 'Case1';
INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
VALUES (6, 1, now(), now(), 16, 15, 20, 10, 5, 0, 0, 0, 0, 0, 0, now());
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStaffID = 6;
-- 
CALL SP_RetailTradeAggregation_Retrieve1(@iErrorCode, @sErrorMsg, @iStaffID);
-- 
SELECT IF(@iErrorCode = 0 AND @sErrorMsg = '' AND found_rows() = 1 , '测试成功', '测试失败') AS 'Case1 Testing Result';

DELETE FROM t_retailtradeaggregation WHERE F_ID = last_insert_id();

SELECT '-----------------Case2: 收银员交班后，不在一分钟查询收银汇总------------------' AS 'Case2';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStaffID = 1;
-- 
CALL SP_RetailTradeAggregation_Retrieve1(@iErrorCode, @sErrorMsg, @iStaffID);
-- 
SELECT IF(@iErrorCode = 0 AND @sErrorMsg = '' AND found_rows() = 0 , '测试成功', '测试失败') AS 'Case2 Testing Result';