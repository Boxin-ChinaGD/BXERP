SELECT '++++++++++++++++++ Test_SP_RetailTradeAggregation_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: 唯一键StaffWorkTiome不重复 -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStaffID = 2;
SET @iPosID = 3;
SET @dtWorkTimeStart = str_to_date('2018-11-1 17:24:38','%Y-%m-%d %H:%i:%s'); 
SET @dtWorkTimeEnd = now();
SET @iTradeNO = 25;
SET @fAmount = 2562.2;
SET @fReserveAmount = 500;
SET @fCashAmount = 1000;
SET @fWechatAmount = 1500;
SET @fAlipayAmounth = 62.2;
SET @fAmount1 = 99;
SET @fAmount2 = 0;
SET @fAmount3 = 0;
SET @fAmount4 = 8;
SET @fAmount5 = 0;

CALL SP_RetailTradeAggregation_Create(@iErrorCode, @sErrorMsg, @iStaffID, @iPosID, @dtWorkTimeStart, @dtWorkTimeEnd, @iTradeNO, @fAmount, @fReserveAmount, @fCashAmount, @fWechatAmount, @fAlipayAmounth,
		@fAmount1, @fAmount2, @fAmount3, @fAmount4, @fAmount5);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_RetailTradeAggregation
	WHERE F_StaffID = @iStaffID
	AND F_PosID = @iPosID
	AND F_WorkTimeStart = @dtWorkTimeStart
	AND F_WorkTimeEnd = @dtWorkTimeEnd
	AND F_TradeNO = @iTradeNO
	AND F_Amount = @fAmount
	AND F_ReserveAmount = @fReserveAmount
	AND F_CashAmount = @fCashAmount
	AND F_WechatAmount = @fWechatAmount
	AND F_AlipayAmount = @fAlipayAmounth
	AND F_Amount1 = @fAmount1
	AND F_Amount2 = @fAmount2
	AND F_Amount3 = @fAmount3
	AND F_Amount4 = @fAmount4
	AND F_Amount5 = @fAmount5;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT '-------------------- Case2: 当唯一键StaffWorkTiome重复时 -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStaffID = 2;
SET @iPosID = 3;
SET @dtWorkTimeStart = str_to_date('2018-11-1 17:24:38','%Y-%m-%d %H:%i:%s'); 
SET @dtWorkTimeEnd = now();
SET @iTradeNO = 25;
SET @fAmount = 2562.2;
SET @fReserveAmount = 500;
SET @fCashAmount = 1000;
SET @fWechatAmount = 1500;
SET @fAlipayAmounth = 62.2;
SET @fAmount1 = 99;
SET @fAmount2 = 0;
SET @fAmount3 = 0;
SET @fAmount4 = 8;
SET @fAmount5 = 0;

CALL SP_RetailTradeAggregation_Create(@iErrorCode, @sErrorMsg, @iStaffID, @iPosID, @dtWorkTimeStart, @dtWorkTimeEnd, @iTradeNO, @fAmount, @fReserveAmount, @fCashAmount, @fWechatAmount, @fAlipayAmounth,
		@fAmount1, @fAmount2, @fAmount3, @fAmount4, @fAmount5);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_RetailTradeAggregation
	WHERE F_StaffID = @iStaffID
	AND F_PosID = @iPosID
	AND F_WorkTimeStart = @dtWorkTimeStart
 --	AND F_WorkTimeEnd = @dtWorkTimeEnd
	AND F_TradeNO = @iTradeNO
	AND F_Amount = @fAmount
	AND F_ReserveAmount = @fReserveAmount
	AND F_CashAmount = @fCashAmount
	AND F_WechatAmount = @fWechatAmount
	AND F_AlipayAmount = @fAlipayAmounth
	AND F_Amount1 = @fAmount1
	AND F_Amount2 = @fAmount2
	AND F_Amount3 = @fAmount3
	AND F_Amount4 = @fAmount4
	AND F_Amount5 = @fAmount5;
SELECT IF(found_rows() = 1 AND @iErrorCode = 1, '测试成功', '测试失败') AS 'Case2 Testing Result';

DELETE FROM t_RetailTradeAggregation WHERE F_StaffID = @iStaffID AND F_PosID = @iPosID AND F_WorkTimeStart = @dtWorkTimeStart;

-- DELETE FROM t_retailtrade WHERE F_ID = LAST_INSERT_ID();

-- ... 测试用例不足

SELECT '-------------------- Case3: 用不存在的StaffID进行创建 -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStaffID = -99;
SET @iPosID = 3;
SET @dtWorkTimeStart = str_to_date('2018-11-1 17:24:38','%Y-%m-%d %H:%i:%s'); 
SET @dtWorkTimeEnd = now();
SET @iTradeNO = 25;
SET @fAmount = 2562.2;
SET @fReserveAmount = 500;
SET @fCashAmount = 1000;
SET @fWechatAmount = 1500;
SET @fAlipayAmounth = 62.2;
SET @fAmount1 = 99;
SET @fAmount2 = 0;
SET @fAmount3 = 0;
SET @fAmount4 = 8;
SET @fAmount5 = 0;

CALL SP_RetailTradeAggregation_Create(@iErrorCode, @sErrorMsg, @iStaffID, @iPosID, @dtWorkTimeStart, @dtWorkTimeEnd, @iTradeNO, @fAmount, @fReserveAmount, @fCashAmount, @fWechatAmount, @fAlipayAmounth,
		@fAmount1, @fAmount2, @fAmount3, @fAmount4, @fAmount5);
		
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'Case3 Testing Result';

SELECT '-------------------- Case4: 用不存在的PosID进行创建 -------------------------' AS 'Case4';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStaffID = 1;
SET @iPosID = -99;
SET @dtWorkTimeStart = str_to_date('2018-11-1 17:24:38','%Y-%m-%d %H:%i:%s'); 
SET @dtWorkTimeEnd = now();
SET @iTradeNO = 25;
SET @fAmount = 2562.2;
SET @fReserveAmount = 500;
SET @fCashAmount = 1000;
SET @fWechatAmount = 1500;
SET @fAlipayAmounth = 62.2;
SET @fAmount1 = 99;
SET @fAmount2 = 0;
SET @fAmount3 = 0;
SET @fAmount4 = 8;
SET @fAmount5 = 0;

CALL SP_RetailTradeAggregation_Create(@iErrorCode, @sErrorMsg, @iStaffID, @iPosID, @dtWorkTimeStart, @dtWorkTimeEnd, @iTradeNO, @fAmount, @fReserveAmount, @fCashAmount, @fWechatAmount, @fAlipayAmounth,
		@fAmount1, @fAmount2, @fAmount3, @fAmount4, @fAmount5);
		
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'Case4 Testing Result';