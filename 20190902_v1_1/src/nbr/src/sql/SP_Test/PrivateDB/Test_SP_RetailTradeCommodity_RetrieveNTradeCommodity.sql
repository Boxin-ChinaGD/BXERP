SELECT '++++++++++++++++++ Test_SP_RetailTradeCommodity_RetrieveNTradeCommodity.sql ++++++++++++++++++++';

SELECT '-------------------- case1:F_TradeID存在时 -------------------------' AS 'Case1';
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID, F_ShopID)
VALUES (1,'LS2019090412300300031220',1,3,'url=ashasoadigmnalskd','2017-08-06',2,1,0,1,'........',now(),1.5,1.1,0,0,0,0,0,0,0,2,2);
SET @iTradeID = last_insert_id();
-- 
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iTradeID,2,'可比克薯片',1,500,321,500, 300, 300);
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
-- 
CALL SP_RetailTradeCommodity_RetrieveNTradeCommodity(@iErrorCode, @sErrorMsg, @iTradeID, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT 1 FROM t_RetailTradeCommodity WHERE F_TradeID = @iTradeID AND F_CommodityName = '可比克薯片';
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';
-- 
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iTradeID AND F_CommodityID = 2;
DELETE FROM T_RetailTrade WHERE F_ID = @iTradeID;

SELECT '-------------------- case2:F_TradeID不存在时 -------------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iTradeID = -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_RetailTradeCommodity_RetrieveNTradeCommodity(@iErrorCode, @sErrorMsg, @iTradeID, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT 1 FROM t_RetailTradeCommodity WHERE F_TradeID = @iTradeID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';