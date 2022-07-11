SELECT '++++++++++++++++++Test_SP_RetailTradeDailyReportByStaff_Create_Case12.sql+++++++++++++++++++++++';

SELECT '-----------------Case12:员工A,B上班进行售卖，员工A退B隔天的货。------------------' AS 'Case12';
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
SET @rt1CommotidyNO_B = 19;
SET @rt1CommotidyPrice_B = 18.320000;
SET @rt1AmuontCash_B = @rt1CommotidyNO_B * @rt1CommotidyPrice_B; -- 183.200000;全部现金给
SET @rt1AmuontWeChat_B = 0.000000;
SET @rt1AmuontAliPay_B = 0.000000;
SET @rt1TotalAmount_B = @rt1CommotidyNO_B * @rt1CommotidyPrice_B;
-- 
SET @rt1CommotidyNO1_A = 39;
SET @rt1CommotidyPrice1_A = 18.320000;
SET @rt1AmuontCash_A = @rt1CommotidyNO1_A * @rt1CommotidyPrice1_A; -- 183.200000;全部现金给
SET @rt1AmuontWeChat_A = 0.000000;
SET @rt1AmuontAliPay_A = 0.000000;
SET @rt1TotalAmount_A = @rt1CommotidyNO1_A * @rt1CommotidyPrice1_A;
-- 
SET @rt2CommotidyNO1_B = 23;
SET @rt2CommotidyPrice1_B = 18.320000;
SET @rt2AmuontCash_B = @rt2CommotidyNO1_B * @rt2CommotidyPrice1_B; -- 183.200000;全部现金给
SET @rt2AmuontWeChat_B = 0.000000;
SET @rt2AmuontAliPay_B = 0.000000;
SET @rt2TotalAmount_B = @rt2CommotidyNO1_B * @rt2CommotidyPrice1_B;
-- 退货单(退零售单1)
SET @rrt1CommotidyNO_B = 7;
SET @rrt1CommotidyPrice_B = 18.320000;
SET @rrt1AmuontCash_B = @rrt1CommotidyNO_B * @rrt1CommotidyPrice_B;	-- 183.200000;全部退现金
SET @rrt1AmuontWeChat_B = 0.000000;
SET @rrt1AmuontAliPay_B = 0.000000;
SET @rrt1TotalAmount_B = @rrt1CommotidyNO_B * @rrt1CommotidyPrice_B;
-- 零售单商品来源表
SET @commodityID216 = @iCommodityID1;
-- 
SET @rtcs1NO_B = 10;
SET @rtcs1WarehousingID_B = @iWarehousingID;
-- 
SET @rtcs1NO_A = 20;
SET @rtcs1WarehousingID_A = @iWarehousingID;
-- 
SET @rtcs2NO_B = 20;
SET @rtcs2WarehousingID_B = @iWarehousingID;
-- 商品去向表
SET @destinationCommodityID216 = @iCommodityID1;
SET @rtcd1NO_B = 10;
SET @rtcd1WarehousingID_B = @iWarehousingID;
-- 零售单收银汇总
SET @rggWorkTimeStartYesterDay_B='3030-03-01 00:00:00';
SET @rggWorkTimeEndYesterDay_B='3030-03-01 23:59:59';
SET @rggTotalAmount_B = @rt1TotalAmount_B; 
SET @rggTotalAmountCash_B = @rt1AmuontCash_B;
SET @rggTotalAmountWeChat_B = @rt1AmuontWeChat_B;
SET @rggTotalAmountAliPay_B = @rt1AmuontAliPay_B;
SET @rggNO1_B = 1;
-- 
SET @rggWorkTimeStart2_A='3030-03-02 00:00:00';
SET @rggWorkTimeEnd2_A='3030-03-02 23:59:59';
SET @rggTotalAmount2_A = @rt1TotalAmount_A;
SET @rggTotalAmountCash2_A = @rt1AmuontCash_A;
SET @rggTotalAmountWeChat2_A = @rt1AmuontWeChat_A;
SET @rggTotalAmountAliPay2_A = @rt1AmuontAliPay_A;
SET @rggNO2_A = 1;
-- 
SET @rggWorkTimeStart2_B='3030-03-02 00:00:00';
SET @rggWorkTimeEnd2_B='3030-03-02 23:59:59';
SET @rggTotalAmount2_B = @rt2TotalAmount_B;
SET @rggTotalAmountCash2_B = @rt2AmuontCash_B;
SET @rggTotalAmountWeChat2_B = @rt2AmuontWeChat_B;
SET @rggTotalAmountAliPay2_B = @rt2AmuontAliPay_B;
SET @rggNO2_B = 1;
-- 收银员
SET @iStaffIDA = 8;
SET @iStaffIDB = 9;

INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
VALUES (@iStaffIDB, 2, @rggWorkTimeStartYesterDay_B, @rggWorkTimeEndYesterDay_B, @rggNO1_B, @rggTotalAmount_B, @rggTotalAmount_B, @rggTotalAmountCash_B, @rggTotalAmountWeChat_B, @rggTotalAmountAliPay_B, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, now());
SET @rtgID1_B = last_insert_id();
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1,'LS3030030100000500010001',11068,1,'url=ashasoadigmnalskd','3030/03/01 06:00:00',@iStaffIDB,1,'0',1,'…',-1,'3030/03/01 06:00:00',@rt1TotalAmount_B,@rt1AmuontCash_B,@rt1AmuontAliPay_B,@rt1AmuontWeChat_B,0.000000,0.000000,0.000000,0.000000,0.000000,1,NULL,NULL,NULL,NULL,NULL,NULL,2);
SET @rtID1_B = last_insert_id();
-- 零售单商品表 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtID1_B,@iCommodityID1,'长虹剑',245,@rt1CommotidyNO_B,18.320000,10,@rt1CommotidyPrice_B,NULL,18.320000);
SET @rtCommID1_B = last_insert_id();
-- 零售单来源表   
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommID1_B,@commodityID216,@rtcs1NO_B,@rtcs1WarehousingID_B);
SET @rtCommSourceID1_B = last_insert_id();

INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
VALUES (@iStaffIDA, 2, @rggWorkTimeStart2_A, @rggWorkTimeEnd2_A, @rggNO2_A, @rggTotalAmount2_A, 1119.990000, @rggTotalAmountCash2_A, @rggTotalAmountWeChat2_A, @rggTotalAmountAliPay2_A, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, now());
SET @rtgID2_A = last_insert_id();
INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
VALUES (@iStaffIDB, 2, @rggWorkTimeStart2_B, @rggWorkTimeEnd2_B, @rggNO2_B, @rggTotalAmount2_B, 200, @rggTotalAmountCash2_B, @rggTotalAmountWeChat2_B, @rggTotalAmountAliPay2_B, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, now());
SET @rtgID2_B = last_insert_id();
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1,'LS3030030200000500010001_1',11069,1,'url=ashasoadigmnalskd','3030/03/02 07:00:01',@iStaffIDA,1,'0',1,'…',@rtID1_B,'3030/03/02 07:00:01',@rrt1TotalAmount_B,@rrt1AmuontCash_B,@rrt1AmuontWeChat_B,@rrt1AmuontAliPay_B,0.000000,0.000000,0.000000,0.000000,0.000000,1,NULL,NULL,NULL,NULL,NULL,NULL,2);
SET @rrtID1_B = last_insert_id();
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1,'LS3030030200000500010004',11069,1,'url=ashasoadigmnalskd','3030/03/02 08:00:01',@iStaffIDA,1,'0',1,'…',-1,'3030/03/02 08:00:01',@rt1TotalAmount_A,@rt1AmuontCash_A,@rt1AmuontAliPay_A,@rt1AmuontWeChat_A,0.000000,0.000000,0.000000,0.000000,0.000000,1,NULL,NULL,NULL,NULL,NULL,NULL,2);
SET @rtID3_A = last_insert_id();
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1,'LS3030030200000500010005',11069,1,'url=ashasoadigmnalskd','3030/03/02 09:00:01',@iStaffIDB,1,'0',1,'…',-1,'3030/03/02 09:00:01',@rt2TotalAmount_B,@rt2AmuontCash_B,@rt2AmuontAliPay_B,@rt2AmuontWeChat_B,0.000000,0.000000,0.000000,0.000000,0.000000,1,NULL,NULL,NULL,NULL,NULL,NULL,2);
SET @rtID2_B = last_insert_id();
-- 零售单商品表 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rrtID1_B,@iCommodityID1,'长虹剑',245,@rrt1CommotidyNO_B,18.320000,10,@rrt1CommotidyPrice_B,NULL,18.320000);
SET @rrtCommID1_B = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtID3_A,@iCommodityID1,'长虹剑',245,@rt1CommotidyNO1_A,18.320000,10,@rt1CommotidyPrice1_A,NULL,18.320000);
SET @rtCommID1_A = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtID2_B,@iCommodityID1,'长虹剑',245,@rt2CommotidyNO1_B,18.320000,10,@rt2CommotidyPrice1_B,NULL,18.320000);
SET @rtCommID2_B = last_insert_id();
-- 插入退货单商品去向表
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@rrtCommID1_B, @destinationCommodityID216, @rtcd1NO_B, @rtcd1WarehousingID_B);
SET @rtcd1_B = last_insert_id();
-- 插入零售单商品来源表
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommID1_A,@commodityID216,@rtcs1NO_A,@rtcs1WarehousingID_A);
SET @rtCommSourceID1_A = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommID2_B,@commodityID216,@rtcs2NO_B,@rtcs2WarehousingID_B);
SET @rtCommSourceID2_B = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime = '3030-03-02 00:00:00';
SET @deleteOldData = 1; -- 仅测试使用 1，删除旧的数据。只能在测试代码中使用。0，不删除旧的数据。只能在功能代码中使用。
SET @iShopID = 2;

CALL SP_RetailTradeDailyReportByStaff_Create(@iErrorCode, @sErrorMsg, @iShopID, @dSaleDatetime, @deleteOldData);
SELECT @iErrorCode;
SELECT @sErrorMsg;

-- 进行业绩报表的数据结果验证

-- 售卖总金额
SET @saleTotalAmount_A = (@rt1CommotidyNO1_A * @rt1CommotidyPrice1_A);
-- 售卖商品总成本
SET @saleTotalCost_A = (@rtcs1NO_A * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs1WarehousingID_A AND F_CommodityID = @commodityID216));
-- 售卖商品总毛利
SET @saleTotalMargin_A = @saleTotalAmount_A - @saleTotalCost_A;
-- 退货总金额
SET @returnTotalAmount_A = 0.000000;
-- 退货商品总成本
SET @returnTotalCost_A = 0.000000;
-- 退货商品总毛利
SET @returnTotalMargin_A = @returnTotalAmount_A - @returnTotalCost_A;
-- 总金额（售卖总金额 - 退货总金额）
SET @totalAmount_A = @saleTotalAmount_A - @returnTotalAmount_A;
-- 总毛利（售卖商品总毛利 - 退货商品总毛利）
SET @grossMargin_A = @saleTotalMargin_A - @returnTotalMargin_A;

SET @ResultVerification1=0;
-- 进行店员A业绩报表的数据结果验证
SELECT 1 INTO @ResultVerification1
FROM t_retailtradedailyreportbystaff 
WHERE F_StaffID = @iStaffIDA 
AND F_Datetime = @dSaleDatetime
AND F_TotalAmount = @totalAmount_A
AND F_GrossMargin = @grossMargin_A
AND F_NO = (
	SELECT COUNT(1) 
	FROM t_retailtrade 
	WHERE F_SourceID = -1 
	AND F_StaffID = @iStaffIDA
	AND datediff(F_SaleDatetime, @dSaleDatetime) = 0
);

SELECT IF(@iErrorCode = 0 AND @ResultVerification1 = 1,'测试成功','测试失败') AS 'Test Case12 Result';


-- 售卖总金额
SET @saleTotalAmount_B = @rt2CommotidyNO1_B * @rt2CommotidyPrice1_B;
-- 售卖商品总成本
SET @saleTotalCost_B = (@rtcs2NO_B * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs2WarehousingID_B AND F_CommodityID = @commodityID216));
-- 售卖商品总毛利
SET @saleTotalMargin_B = @saleTotalAmount_B - @saleTotalCost_B;
-- 退货总金额
SET @returnTotalAmount_B = (@rrt1CommotidyNO_B * @rrt1CommotidyPrice_B);
-- 退货商品总成本
SET @returnTotalCost_B =(@rtcd1NO_B * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcd1WarehousingID_B AND F_CommodityID = @destinationCommodityID216));
-- 退货商品总毛利
SET @returnTotalMargin_B = @returnTotalAmount_B - @returnTotalCost_B;
-- 总金额（售卖总金额 - 退货总金额）
SET @totalAmount_B = @saleTotalAmount_B - @returnTotalAmount_B;
-- 总毛利（售卖商品总毛利 - 退货商品总毛利）
SET @grossMargin_B = @saleTotalMargin_B - @returnTotalMargin_B;

SET @ResultVerification2=0;
-- 进行店员B业绩报表的数据结果验证
SELECT 1 INTO @ResultVerification2
FROM t_retailtradedailyreportbystaff 
WHERE F_StaffID = @iStaffIDB 
AND F_Datetime = @dSaleDatetime
AND F_TotalAmount = @totalAmount_B
AND F_GrossMargin = @grossMargin_B
AND F_NO = (
	SELECT COUNT(1) 
	FROM t_retailtrade 
	WHERE F_SourceID = -1 
	AND F_StaffID = @iStaffIDB
	AND datediff(F_SaleDatetime, @dSaleDatetime) = 0
 );

SELECT IF(@iErrorCode = 0 AND @ResultVerification2 = 1,'测试成功','测试失败') AS 'Test Case12 Result';

DELETE FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime;
DELETE FROM t_retailtradeaggregation WHERE F_ID = @rtgID1_B;
DELETE FROM t_retailtradeaggregation WHERE F_ID = @rtgID2_A;
DELETE FROM t_retailtradeaggregation WHERE F_ID = @rtgID2_B;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommSourceID1_B;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtCommID1_B;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @rtcd1_B;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rrtCommID1_B;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommSourceID1_A;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommSourceID2_B;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtCommID1_A;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtCommID2_B;
DELETE FROM t_retailtrade WHERE F_ID = @rtID1_B;
DELETE FROM t_retailtrade WHERE F_ID = @rrtID1_B;
DELETE FROM t_retailtrade WHERE F_ID = @rtID3_A;
DELETE FROM t_retailtrade WHERE F_ID = @rtID2_B;
DELETE FROM t_warehousingcommodity WHERE F_WarehousingID = @iWarehousingID;
DELETE FROM t_warehousing WHERE F_ID = @iWarehousingID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID1;