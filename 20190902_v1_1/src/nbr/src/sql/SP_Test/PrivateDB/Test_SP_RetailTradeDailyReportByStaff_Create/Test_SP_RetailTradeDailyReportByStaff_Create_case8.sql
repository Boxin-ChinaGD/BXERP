SELECT '++++++++++++++++++Test_SP_RetailTradeDailyReportByStaff_Create_Case8.sql+++++++++++++++++++++++';

-- 测试跨库售卖，退货
-- 售卖金额；8*20 = 160
-- 售卖成本：5*10+3*15 = 95
-- 售卖毛利：160-95 = 65
-- 退货金额：5*20 = 100
-- 退货成本：3*15+2*10 = 65
-- 退货毛利：100-65 = 35

-- 销售金额：160-100 = 60
-- 销售毛利：65-35 = 30

SELECT '-----------------Case8:当退货单中商品来自不同入库单时，入库价格可能不一样（跨库） ------------------' AS 'Case8';
-- 零售单
SET @rt1CommotidyNO1 = 8;
SET @rt1CommotidyPrice1 = 20.000000;
SET @rt1AmuontCash = 160.000000;
SET @rt1AmuontWeChat = 0.000000;
SET @rt1AmuontAliPay = 0.000000;
SET @rt1TotalAmount = @rt1CommotidyNO1 * @rt1CommotidyPrice1;
-- 退货单(退零售单1)
SET @rrt1CommotidyNO = 5;
SET @rrt1CommotidyPrice = 20.000000;
SET @rrt1AmuontCash = 100.000000;
SET @rrt1AmuontWeChat = 0.000000;
SET @rrt1AmuontAliPay = 0.000000;
SET @rrt1TotalAmount = @rrt1CommotidyNO * @rrt1CommotidyPrice;
-- 零售单商品来源表
SET @rtcs1NO1 = 5;
SET @rtcs1NO2 = 3;
-- 商品去向表
SET @destinationCommodityID216 = 216;
SET @rtcd1NO1 = 3;
SET @rtcd1NO2 = 2;
-- 零售单收银汇总
SET @rggWorkTimeStart='2119-01-01 00:00:00';
SET @rggWorkTimeEnd='2119-01-01 23:59:59';
SET @rggTotalAmount1 = @rt1TotalAmount; 
SET @rggTotalAmountCash1 = @rt1AmuontCash; 
SET @rggTotalAmountWeChat1 = @rt1AmuontWeChat;
SET @rggTotalAmountAliPay1 = @rt1AmuontAliPay;
SET @rggNO1 = 1; 
-- 收银员
SET @iStaffID = 2;
-- 商品的入库价格
SET @warehousingCommodityPrice1 = 10.000000;
SET @warehousingCommodityPrice2 = 15.000000;

INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
VALUES (@iStaffID, 2, @rggWorkTimeStart, @rggWorkTimeEnd, 1, @rt1TotalAmount, 200.000000, @rggTotalAmountCash1, @rggTotalAmountWeChat1, @rggTotalAmountAliPay1, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, now());
SET @rtgID = last_insert_id();

-- 创建一个商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_CreateDatetime, F_UpdateDatetime)
VALUES (0, '未入库商品', '普通商品', '个', 3, '包', 1, 1, 'ztq666', 1, 
19, 19, 0, 1, 365, 3, '2119-01-01 09:00:00', 1, 0, 0, 0, 0, '2119-01-01 09:00:00', '2019-05-19 09:00:00');
SET @commID = last_insert_id();
-- 创建一个条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commID, 'tzq66666', '2119-01-01 09:00:00', '2119-01-01 09:00:00');
SET @barcodeID = last_insert_id();
-- 供应商与商品进行关联
INSERT INTO t_providercommodity (F_CommodityID, F_ProviderID)
VALUES (@commID, 1);
SET @providerCommID = last_insert_id();

-- 需要一个商品来自两张不同的入库单，价格不同，进行售卖，然后进行退货
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_UpdateDatetime)
VALUES (2,'rk211901010001', 1, 1, 2, 4, '2119-01-01 06:00:00', '2119-01-01 06:00:00');
SET @ws1 = last_insert_id();

INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_UpdateDatetime)
VALUES (2,'rk211901010001', 1, 1, 2, 4, '2119-01-01 06:00:00', '2119-01-01 06:00:00');
SET @ws2 = last_insert_id();
-- 插入入库单商品表
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@ws1, @commID, 5, 3, '未入库商品', @barcodeID, @warehousingCommodityPrice1, 50, '2119-01-01 06:00:00', 36, '2119-01-09 06:00:00', '2119-01-01 06:00:00', '2119-01-01 06:00:00', 5);
SET @wsc1 = last_insert_id();

INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@ws2, @commID, 5, 3, '未入库商品', @barcodeID, @warehousingCommodityPrice2, 75, '2119-01-01 06:00:00', 36, '2119-01-09 06:00:00', '2119-01-01 06:00:00', '2119-01-01 06:00:00', 5);
SET @wsc2 = last_insert_id();

-- 进行售卖
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Status,F_Remark,F_SourceID,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID, F_ShopID) 
VALUES (1,'LS21190010101010100011244', 2,1,'url=ashasoadigmnalskd','2119-01-01 09:00:00',@iStaffID,1,0,1,'........',-1,'2119-01-01 09:00:00',@rt1TotalAmount,@rt1AmuontCash,@rt1AmuontAliPay,@rt1AmuontWeChat,0,0,0,0,0,1,2);
SET @rtID1 = last_insert_id();
-- 零售单商品表
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@rtID1, @commID, '未入库商品', @barcodeID, @rt1CommotidyNO1, 20, 8, @rt1CommotidyPrice1, 19);
SET @rtCommID1 = last_insert_id();
-- 插入零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommID1, @commID, @rtcs1NO1, @ws1);
SET @rtCommsourceID1 = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommID1, @commID, @rtcs1NO2, @ws2);
SET @rtCommsourceID2 = last_insert_id();

-- 进行退货
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Status,F_Remark,F_SourceID,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID, F_ShopID) 
VALUES (1,'LS21190010101010100011244', 2,1,'url=ashasoadigmnalskd','2119-01-01 10:00:00',@iStaffID,1,0,1,'........',@rtID1,'2119-01-01 10:00:00',@rrt1TotalAmount,@rrt1AmuontCash,@rrt1AmuontAliPay,@rrt1AmuontWeChat,0,0,0,0,0,1,2);
SET @rtID2 = last_insert_id();
-- 插入零售单商品表
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@rtID2, @commID, '未入库商品', @barcodeID, @rrt1CommotidyNO, 20, 5, @rrt1CommotidyPrice, 19);
SET @rtCommID2 = last_insert_id();
-- 插入退货商品去向表
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommID2, @commID, @rtcd1NO1, @ws2);
SET @rtcd1 = last_insert_id();
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommID2, @commID, @rtcd1NO2, @ws1);
SET @rtcd2 = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime = '2119-01-01 0:00:00';
SET @deleteOldData = 1; -- 仅测试使用 1，删除旧的数据。只能在测试代码中使用。0，不删除旧的数据。只能在功能代码中使用。
SET @iShopID = 2;

CALL SP_RetailTradeDailyReportByStaff_Create(@iErrorCode, @sErrorMsg, @iShopID, @dSaleDatetime, @deleteOldData);
SELECT @iErrorCode;
SELECT @sErrorMsg;

-- 进行业绩报表的数据结果验证

-- 售卖总金额
SET @saleTotalAmount = (@rt1CommotidyNO1 * @rt1CommotidyPrice1);
-- 售卖商品总成本
SET @saleTotalCost = (@rtcs1NO1 * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @ws1 AND F_CommodityID = @commID))
					 + (@rtcs1NO2 * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @ws2 AND F_CommodityID = @commID));
-- 售卖商品总毛利
SET @saleTotalMargin = @saleTotalAmount - @saleTotalCost;
-- 退货总金额
SET @returnTotalAmount = (@rrt1CommotidyNO * @rrt1CommotidyPrice);
-- 退货商品总成本
SET @returnTotalCost= (@rtcd1NO1 * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @ws2 AND F_CommodityID = @commID))
					   +(@rtcd1NO2 * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @ws1 AND F_CommodityID = @commID)) ;
-- 退货商品总毛利
SET @returnTotalMargin = @returnTotalAmount - @returnTotalCost;
-- 总金额（售卖总金额 - 退货总金额）
SET @totalAmount = @saleTotalAmount - @returnTotalAmount;
-- 总毛利（售卖商品总毛利 - 退货商品总毛利）
SET @grossMargin = @saleTotalMargin - @returnTotalMargin;

SET @ResultVerification=0;
-- 进行店员9业绩报表的数据结果验证
SELECT 1 INTO @ResultVerification
FROM t_retailtradedailyreportbystaff 
WHERE F_StaffID = @iStaffID
AND F_Datetime = @dSaleDatetime
AND F_TotalAmount = @totalAmount
AND F_GrossMargin = @grossMargin
AND F_NO = (
	SELECT COUNT(1) 
	FROM t_retailtrade 
	WHERE F_SourceID = -1 
	AND F_StaffID = @iStaffID
	AND datediff(F_SaleDatetime, @dSaleDatetime) = 0
 );

SELECT IF(@iErrorCode = 0 AND @ResultVerification = 1,'测试成功','测试失败') AS 'Test Case8 Result';

DELETE FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime;
DELETE FROM t_retailtradeaggregation WHERE F_ID = @rtgID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommsourceID1;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommsourceID2;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @rtcd1;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @rtcd2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtCommID1;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtCommID2;
DELETE FROM t_warehousingcommodity WHERE F_ID = @wsc1;
DELETE FROM t_warehousingcommodity WHERE F_ID = @wsc2;
DELETE FROM t_warehousing WHERE F_ID = @ws1;
DELETE FROM t_warehousing WHERE F_ID = @ws2;
DELETE FROM t_providercommodity WHERE F_ID = @providerCommID;
DELETE FROM T_RetailTrade WHERE F_ID = @rtID1;
DELETE FROM T_RetailTrade WHERE F_ID = @rtID2;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID;
DELETE FROM t_commodity WHERE F_ID = @commID;