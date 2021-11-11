SELECT '++++++++++++++++++Test_SP_RetailTradeDailyReportByStaff_Create_Case10.sql+++++++++++++++++++++++';

SELECT '-----------------Case10:员工A,B上班进行售卖，员工A退A隔天的货。 ------------------' AS 'Case10';
-- 插入商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0/*F_Status*/, '长虹剑', '薯片', '克', 1, '千克', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0, NULL, '2019-12-23 10:01:33', '2019-12-23 10:07:37', NULL, NULL, NULL, NULL);
SET @iCommodityID1 = last_insert_id();
-- 插入入库单
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (1, 'RK190003010001', 1, 1, 4, 4, '2000-01-01 13:30:00', NULL, '2000-01-01 13:30:00');
SET @iWarehousingID = last_insert_id();
-- 插入入库单商品
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID, @iCommodityID1, 100, 1, '长虹剑', 13, 1.7, 100, '2017-10-06 01:01:01', 36, '2020-10-06 01:01:01', '2019-12-23 10:01:35', '2019-12-23 10:01:35', 100);
-- 零售单
SET @rt1CommotidyNOYesterday_A = 10;
SET @rt1CommotidyPriceYesterday_A = 18.320000;
SET @rt1AmuontCashYesterday_A = 183.200000;
SET @rt1AmuontWeChatYesterday_A = 0.000000;
SET @rt1AmuontAliPayYesterday_A = 0.000000;
SET @rt1TotalAmountYesterday_A = @rt1CommotidyNOYesterday_A * @rt1CommotidyPriceYesterday_A;
-- 退货单(退零售单1)
SET @rrt1CommotidyNO_A = 5;
SET @rrt1CommotidyPrice_A = 18.320000;
SET @rrt1AmuontCash_A = 183.200000;
SET @rrt1AmuontWeChat_A = 0.000000;
SET @rrt1AmuontAliPay_A = 0.000000;
SET @rrt1TotalAmount_A = @rrt1CommotidyNO_A * @rrt1CommotidyPrice_A;
-- 零售单商品来源表
SET @commodityID216 = @iCommodityID1;
-- 
SET @rtcs1NO_A = 10;
SET @rtcs1WarehousingID_A = @iWarehousingID;
-- 商品去向表
SET @destinationCommodityID216 = @iCommodityID1;
SET @rtcd1NO_A = 5;
SET @rtcd1WarehousingID_A = @iWarehousingID;
-- 零售单收银汇总
SET @rggWorkTimeStartYesterday_A='3030-01-03 00:00:00';
SET @rggWorkTimeEndYesterday_A='3030-01-03 23:59:59';
SET @rggTotalAmountYesterday_A = @rt1TotalAmountYesterday_A; 
SET @rggTotalAmountCashYesterday_A = @rt1AmuontCashYesterday_A;
SET @rggTotalAmountWeChatYesterday_A = @rt1AmuontWeChatYesterday_A;
SET @rggTotalAmountAliPayYesterday_A = @rt1AmuontAliPayYesterday_A;
SET @rggNOYesterday_A = 1;
-- 店员8当天只退货，不售卖
SET @rggWorkTimeStart_A='3030-01-04 00:00:00';
SET @rggWorkTimeEnd_A='3030-01-04 23:59:59';
SET @rggTotalAmount_A = 0.000000; 
SET @rggTotalAmountCash_A = 0.000000;
SET @rggTotalAmountWeChat_A = 0.000000;
SET @rggTotalAmountAliPay_A = 0.000000;
SET @rggNO_A = 0;
-- 店员9不进行售卖
SET @rggWorkTimeStart_B='3030-01-04 00:00:00';
SET @rggWorkTimeEnd_B='3030-01-04 23:59:59';
SET @rggTotalAmount_B = 0.000000; 
SET @rggTotalAmountCash_B = 0.000000;
SET @rggTotalAmountWeChat_B = 0.000000;
SET @rggTotalAmountAliPay_B = 0.000000;
SET @rggNO_B = 0;
-- 收银员
SET @iStaffID_A = 8;
SET @iStaffID_B = 9;

INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
VALUES (@iStaffID_A, 2, @rggWorkTimeStartYesterday_A, @rggWorkTimeEndYesterday_A, @rggNOYesterday_A, @rggTotalAmountYesterday_A, 183.200000, @rggTotalAmountCashYesterday_A, @rggTotalAmountWeChatYesterday_A, @rggTotalAmountAliPayYesterday_A, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, now());
SET @rtgIDYesterday_A = last_insert_id();
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1,'LS3030010300000500010001',11068,1,'url=ashasoadigmnalskd','3030/01/03 07:00:00',@iStaffID_A,1,'0',1,'…',-1,'3030/01/01 06:00:00',@rt1TotalAmountYesterday_A,183.200000,@rt1AmuontCashYesterday_A,@rt1AmuontAliPayYesterday_A,@rt1AmuontWeChatYesterday_A,0.000000,0.000000,0.000000,0.000000,1,NULL,NULL,NULL,NULL,NULL,NULL,2);
SET @rtIDYesterday_A = last_insert_id();
-- 插入零售单商品表
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtIDYesterday_A,216,'长虹剑',245,@rt1CommotidyNOYesterday_A,18.320000,10,@rt1CommotidyPriceYesterday_A,NULL,18.320000);
SET @rtCommID1Yesterday_A = last_insert_id();
-- 插入零售单商品来源表
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommID1Yesterday_A,@commodityID216,@rtcs1NO_A,@rtcs1WarehousingID_A);
SET @rtCommSourceIDYesterday_A = last_insert_id();

INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
VALUES (@iStaffID_A, 2, @rggWorkTimeStart_A, @rggWorkTimeEnd_A, @rggNO_A, @rggTotalAmount_A, 91.6, @rggTotalAmountCash_A, @rggTotalAmountWeChat_A, @rggTotalAmountAliPay_A, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, now());
SET @rtgID_A = last_insert_id();
INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
VALUES (@iStaffID_B, 2, @rggWorkTimeStart_B, @rggWorkTimeEnd_B, @rggNO_B, @rggTotalAmount_B, 0.000000, @rggTotalAmountCash_B, @rggTotalAmountWeChat_B, @rggTotalAmountAliPay_B, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, now());
SET @rtgID_B = last_insert_id();
 
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1,'LS3030010400000500010001_1',11069,1,'url=ashasoadigmnalskd','3030/01/04 07:00:01',@iStaffID_A,1,'0',1,'…',@rtIDYesterday_A,'3030/01/04 07:00:01',@rrt1TotalAmount_A,@rrt1AmuontCash_A,@rrt1AmuontAliPay_A,@rrt1AmuontWeChat_A,0.000000,0.000000,0.000000,0.000000,0.000000,1,NULL,NULL,NULL,NULL,NULL,NULL,2);
SET @rrtID_A = last_insert_id();
-- 插入零售单商品表
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rrtID_A,@iCommodityID1,'长虹剑',245,@rrt1CommotidyNO_A,18.320000,5,@rrt1CommotidyPrice_A,NULL,18.320000);
SET @rrtCommID_A = last_insert_id();
-- 插入退货单商品去向表
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@rrtCommID_A, @destinationCommodityID216, @rtcd1NO_A, @rtcd1WarehousingID_A);
SET @rtcd_A = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime = '3030-01-04 00:00:00';
SET @deleteOldData = 1; -- 仅测试使用 1，删除旧的数据。只能在测试代码中使用。0，不删除旧的数据。只能在功能代码中使用。
SET @iShopID = 2;

CALL SP_RetailTradeDailyReportByStaff_Create(@iErrorCode, @sErrorMsg, @iShopID, @dSaleDatetime, @deleteOldData);
SELECT @iErrorCode; 
SELECT @sErrorMsg;

-- 进行业绩报表的数据结果验证

-- 售卖总金额(店员8当天只进行昨天单退货，不零售)
SET @saleTotalAmount_A = 0.00000;
-- 售卖商品总成本
SET @saleTotalCost_A = 0.000000;
-- 售卖商品总毛利
SET @saleTotalMargin_A = @saleTotalAmount_A - @saleTotalCost_A;
-- 退货总金额
SET @returnTotalAmount_A = (@rrt1CommotidyNO_A * @rrt1CommotidyPrice_A);
-- 退货商品总成本
SET @returnTotalCost_A = (@rtcd1NO_A * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcd1WarehousingID_A AND F_CommodityID = @destinationCommodityID216));
-- 退货商品总毛利
SET @returnTotalMargin_A = @returnTotalAmount_A - @returnTotalCost_A;
-- 总金额（售卖总金额 - 退货总金额）
SET @totalAmount_A = @saleTotalAmount_A - @returnTotalAmount_A;
-- 总毛利（售卖商品总毛利 - 退货商品总毛利）
SET @grossMargin_A = @saleTotalMargin_A - @returnTotalMargin_A;

SET @ResultVerification1=0;
-- 进行店员8业绩报表的数据结果验证
SELECT 1 INTO @ResultVerification1
FROM t_retailtradedailyreportbystaff tt1
WHERE F_StaffID = @iStaffID_A 
AND F_Datetime = @dSaleDatetime
AND F_TotalAmount = @totalAmount_A
AND F_GrossMargin = @grossMargin_A
AND F_NO = (
	SELECT COUNT(1) 
	FROM t_retailtrade 
	WHERE F_SourceID = -1 
	AND F_StaffID = @iStaffID_A
	AND datediff(F_SaleDatetime, @dSaleDatetime) = 0
);

SELECT IF(@iErrorCode = 0 AND @ResultVerification1 = 1,'测试成功','测试失败') AS 'Test Case10 Result';


-- 售卖总金额(店员9不售卖，故无零售记录)
SET @saleTotalAmount_B = 0.000000;
-- 售卖商品总成本
SET @saleTotalCost_B = 0.000000;
-- 售卖商品总毛利
SET @saleTotalMargin_B = @saleTotalAmount_B - @saleTotalCost_B;
-- 退货总金额
SET @returnTotalAmount_B = 0.000000;
-- 退货商品总成本
SET @returnTotalCost_B = 0.000000;
-- 退货商品总毛利
SET @returnTotalMargin_B = @returnTotalAmount_B - @returnTotalCost_B;
-- 总金额（售卖总金额 - 退货总金额）
SET @totalAmount_B = @saleTotalAmount_B - @returnTotalAmount_B;
-- 总毛利（售卖商品总毛利 - 退货商品总毛利）
SET @grossMargin_B = @saleTotalMargin_B - @returnTotalMargin_B;

SET @ResultVerification2=0;
-- 进行店员9业绩报表的数据结果验证
SELECT 1 INTO @ResultVerification2
FROM t_retailtradedailyreportbystaff 
WHERE F_StaffID = @iStaffID_B 
AND F_Datetime = @dSaleDatetime
AND F_TotalAmount = @totalAmount_B
AND F_GrossMargin = @grossMargin_B
AND F_NO = (
	SELECT COUNT(1) 
	FROM t_retailtrade 
	WHERE F_SourceID = -1 
	AND F_StaffID = @iStaffID_B
	AND datediff(F_SaleDatetime, @dSaleDatetime) = 0
 );

SELECT IF(@iErrorCode = 0 AND @ResultVerification2 = 1,'测试成功','测试失败') AS 'Test Case10 Result';

DELETE FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime;
DELETE FROM t_retailtradeaggregation WHERE F_ID = @rtgIDYesterday_A;
DELETE FROM t_retailtradeaggregation WHERE F_ID = @rtgID_A;
DELETE FROM t_retailtradeaggregation WHERE F_ID = @rtgID_B;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommSourceIDYesterday_A;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @rtcd_A;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtCommID1Yesterday_A;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rrtCommID_A;
DELETE FROM t_retailtrade WHERE F_ID = @rtIDYesterday_A;
DELETE FROM t_retailtrade WHERE F_ID = @rrtID_A;
DELETE FROM t_warehousingcommodity WHERE F_WarehousingID = @iWarehousingID;
DELETE FROM t_warehousing WHERE F_ID = @iWarehousingID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID1;