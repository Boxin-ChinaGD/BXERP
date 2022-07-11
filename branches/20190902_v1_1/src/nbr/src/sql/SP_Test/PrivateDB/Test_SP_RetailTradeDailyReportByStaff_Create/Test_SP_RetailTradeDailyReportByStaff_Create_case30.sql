SELECT '++++++++++++++++++Test_SP_RetailTradeDailyReportByStaff_Create_case5.sql+++++++++++++++++++++++';


SELECT '----- case30:员工A创建一个零售单卖A商品，对应两个入库单 ,B商品，对应两个入库单 , C商品，对应两个入库单,员工B隔天创建一张退货单，对商品A、B部分退货[AB]-------' AS 'Case30';
-- 收银员
SET @staffID_A = 9;
SET @staffID_B = 10;
SET @ReserveAmount = 500.000000;

-- 商品信息
SET @commodityCreateTime = '2119-01-01 09:00:00';
SET @warehousingTime = '2119-01-01 06:00:00';
SET @commodityPriceA = 330;
SET @warehousingCommodityA_Price1 = 3.999999; -- 入库单1
SET @warehousingCommodityA_NO1 = 500;
SET @warehousingCommodityA_Price2 = 4.888888; -- 入库单2
SET @warehousingCommodityA_NO2 = 1000;
SET @commodityPriceB = 220;
SET @warehousingCommodityB_Price1 = 5.777777;
SET @warehousingCommodityB_NO1 = 500;
SET @warehousingCommodityB_Price2 = 7.666666;
SET @warehousingCommodityB_NO2 = 1000;
SET @commodityPriceC = 770;
SET @warehousingCommodityC_Price1 = 8.555555;
SET @warehousingCommodityC_NO1 = 500;
SET @warehousingCommodityC_Price2 = 9.444444;
SET @warehousingCommodityC_NO2 = 1000;

-- 零售单1
SET @rt1SaleDatetimeA = '2041/02/01 06:00:00'; -- 创建零售单时间
SET @rt1CommotidyNO1Yesterday_A = 99; -- 入库单1
SET @rt1CommotidyNO2Yesterday_A = 999; -- 入库单2
SET @rt1CommotidyPriceYesterday_A = 199.9;
SET @rt1CommotidyNO1Yesterday_B = 88; -- 入库单1
SET @rt1CommotidyNO2Yesterday_B = 888; -- 入库单2
SET @rt1CommotidyPriceYesterday_B = 188.8;
SET @rt1CommotidyNO1Yesterday_C = 77; -- 入库单1
SET @rt1CommotidyNO2Yesterday_C = 777; -- 入库单2
SET @rt1CommotidyPriceYesterday_C = 177.7;
SET @rt1TotalAmountYesterday_A = (@rt1CommotidyNO1Yesterday_A + @rt1CommotidyNO2Yesterday_A) * @rt1CommotidyPriceYesterday_A -- 
		+ (@rt1CommotidyNO1Yesterday_B + @rt1CommotidyNO2Yesterday_B) * @rt1CommotidyPriceYesterday_B -- 
		+ (@rt1CommotidyNO1Yesterday_C + @rt1CommotidyNO2Yesterday_C) * @rt1CommotidyPriceYesterday_C; -- 零售总额
SET @rt1AmuontCashYesterday_A = @rt1TotalAmountYesterday_A;
SET @rt1AmuontWeChatYesterday_A = 0.000000;
SET @rt1AmuontAliPayYesterday_A = 0.000000;

-- 退货单(退零售单1)
SET @rrt1SaleDatetimeB = '2041/02/02 07:00:01'; -- 创建零售退货单时间
SET @rrt1CommotidyNO2_A = @rt1CommotidyNO2Yesterday_A; -- 入库单2
SET @rrt1CommotidyNO2_B = @rt1CommotidyNO2Yesterday_B;
SET @rrt1TotalAmount_A = @rrt1CommotidyNO2_A * @rt1CommotidyPriceYesterday_A + @rrt1CommotidyNO2_B * @rt1CommotidyPriceYesterday_B; -- 退货总额
SET @rrt1AmuontCash_A = @rrt1TotalAmount_A;
SET @rrt1AmuontWeChat_A = 0.000000;
SET @rrt1AmuontAliPay_A = 0.000000;

-- 收银员A昨天的收银汇总信息(只售卖)
SET @rggWorkTimeStartYesterday_A='2041/02/01 00:00:00';
SET @rggWorkTimeEndYesterday_A='2041/02/01 23:59:59';
SET @rggTotalAmountYesterday_A = @rt1TotalAmountYesterday_A; 
SET @rggTotalAmountCashYesterday_A = @rt1AmuontCashYesterday_A;
SET @rggTotalAmountWeChatYesterday_A = @rt1AmuontWeChatYesterday_A;
SET @rggTotalAmountAliPayYesterday_A = @rt1AmuontAliPayYesterday_A;
SET @rggNOYesterday_A = 1;

-- 收银员A今天的收银汇总信息
SET @rggWorkTimeStart_A='2041/02/02 00:00:00';
SET @rggWorkTimeEnd_A='2041/02/02 23:59:59';
SET @rggTotalAmount_A = 0 - @rrt1TotalAmount_A; 
SET @rggTotalAmountCash_A = 0 - @rrt1AmuontCash_A;
SET @rggTotalAmountWeChat_A = 0.000000;
SET @rggTotalAmountAliPay_A = 0.000000;
SET @rggNO_A = 0;

-- 收银员B今天的收银汇总信息(无售卖只退货)
SET @rggWorkTimeStart_B='2041/02/02 00:00:00';
SET @rggWorkTimeEnd_B='2041/02/02 23:59:59';
SET @rggTotalAmount_B = 0.000000; 
SET @rggTotalAmountCash_B = 0.000000;
SET @rggTotalAmountWeChat_B = 0.000000;
SET @rggTotalAmountAliPay_B = 0.000000;
SET @rggNO_B = 0;

-- 员工A昨天的收银汇总
INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
VALUES (@staffID_A, 2, @rggWorkTimeStartYesterday_A, @rggWorkTimeEndYesterday_A, @rggNOYesterday_A, @rggTotalAmountYesterday_A, @ReserveAmount, @rggTotalAmountCashYesterday_A, @rggTotalAmountWeChatYesterday_A, @rggTotalAmountAliPayYesterday_A, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, now());
SET @rtgIDYesterday_A = last_insert_id();

-- 员工A今天的收银汇总
INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
VALUES (@staffID_A, 2, @rggWorkTimeStart_A, @rggWorkTimeEnd_A, @rggNO_A, @rggTotalAmount_A, @ReserveAmount, @rggTotalAmountCash_A, @rggTotalAmountWeChat_A, @rggTotalAmountAliPay_A, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, now());
SET @rtgIDA = last_insert_id();

-- 员工B今天的收银汇总
INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
VALUES (@staffID_B, 2, @rggWorkTimeStart_B, @rggWorkTimeEnd_B, @rggNO_B, @rggTotalAmount_B, @ReserveAmount, @rggTotalAmountCash_B, @rggTotalAmountWeChat_B, @rggTotalAmountAliPay_B, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, now());
SET @rtgIDB = last_insert_id();

-- 创建商品A
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, -- 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_ShelfLife, F_ReturnDays, -- 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_CreateDatetime, F_UpdateDatetime)
VALUES (0, '跨库商品A', '普通商品A', '个', 3, '包', 1, 1, 'CJS666', -- 
1, 19, 19, 0, 1, 365, 3, -- 
@commodityCreateTime, 1, 0, 0, 0, 0, @commodityCreateTime, @commodityCreateTime);
SET @commIDA = last_insert_id();
-- 创建商品A的条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commIDA, 'cjs66666', @commodityCreateTime, @commodityCreateTime);
SET @barcodeIDA = last_insert_id();
-- 商品A关联供应商
INSERT INTO t_providercommodity (F_CommodityID, F_ProviderID)
VALUES (@commIDA, 1);
SET @providerCommIDA = last_insert_id();
-- 创建商品A的入库单1(入库500件商品A,入库价10元,共5000元)
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_UpdateDatetime)
VALUES (2,'rk211901010001', 1, 1, 2, 4, @warehousingTime, @warehousingTime);
SET @warehouseA1 = last_insert_id();
-- 创建入库商品1
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, -- 
F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehouseA1, @commIDA, @warehousingCommodityA_NO1, 3, '跨库商品A', @barcodeIDA, @warehousingCommodityA_Price1, (@warehousingCommodityA_Price1 * @warehousingCommodityA_NO1), -- 
@warehousingTime, 36, @warehousingTime, @warehousingTime, @warehousingTime, 5);
SET @warehouseCommA1 = last_insert_id();
-- 创建商品A的入库单2(入库500件商品A,入库价15元,共7500元)
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_UpdateDatetime)
VALUES (2,'rk211901010001', 1, 1, 2, 4, @warehousingTime, @warehousingTime);
SET @warehouseA2 = last_insert_id();
-- 创建入库商品2
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, -- 
F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehouseA2, @commIDA, @warehousingCommodityA_NO2, 3, '跨库商品A', @barcodeIDA, @warehousingCommodityA_Price2, (@warehousingCommodityA_Price2 * @warehousingCommodityA_NO2), -- 
@warehousingTime, 36, @warehousingTime, @warehousingTime, @warehousingTime, 5);
SET @warehouseCommA2 = last_insert_id();
-- 创建商品B
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, -- 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_ShelfLife, F_ReturnDays, -- 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_CreateDatetime, F_UpdateDatetime)
VALUES (0, '跨库商品B', '普通商品B', '个', 3, '包', 1, 1, 'CJS666', -- 
1, 19, 19, 0, 1, 365, 3, -- 
@commodityCreateTime, 1, 0, 0, 0, 0, @commodityCreateTime, @commodityCreateTime);
SET @commIDB = last_insert_id();
-- 创建商品B的条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commIDB, 'cjs77777', @commodityCreateTime, @commodityCreateTime);
SET @barcodeIDB = last_insert_id();
-- 商品B关联供应商
INSERT INTO t_providercommodity (F_CommodityID, F_ProviderID)
VALUES (@commIDB, 1);
SET @providerCommIDB = last_insert_id();
-- 创建商品B的入库单1(入库500件商品B,入库价10元,共5000元)
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_UpdateDatetime)
VALUES (2,'rk211901010001', 1, 1, 2, 4, @warehousingTime, @warehousingTime);
SET @warehouseB1 = last_insert_id();
-- 创建入库商品B1
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, -- 
F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehouseB1, @commIDB, @warehousingCommodityB_NO1, 3, '跨库商品B', @barcodeIDB, @warehousingCommodityB_Price1, (@warehousingCommodityB_Price1 * @warehousingCommodityB_NO1), -- 
@warehousingTime, 36, @warehousingTime, @warehousingTime, @warehousingTime, 5);
SET @warehouseCommB1 = last_insert_id();
-- 创建商品B的入库单2(入库500件商品B,入库价15元,共7500元)
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_UpdateDatetime)
VALUES (2,'rk211901010001', 1, 1, 2, 4, @warehousingTime, @warehousingTime);
SET @warehouseB2 = last_insert_id();
-- 创建入库商品B2
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, -- 
F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehouseB2, @commIDB, @warehousingCommodityB_NO2, 3, '跨库商品B', @barcodeIDB, @warehousingCommodityB_Price2, (@warehousingCommodityB_Price2 * @warehousingCommodityB_NO2), -- 
@warehousingTime, 36, @warehousingTime, @warehousingTime, @warehousingTime, 5);
SET @warehouseCommB2 = last_insert_id();
-- 创建商品C
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, -- 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_ShelfLife, F_ReturnDays, -- 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_CreateDatetime, F_UpdateDatetime)
VALUES (0, '跨库商品C', '普通商品C', '个', 3, '包', 1, 1, 'CJS666', -- 
1, 19, 19, 0, 1, 365, 3, -- 
@commodityCreateTime, 1, 0, 0, 0, 0, @commodityCreateTime, @commodityCreateTime);
SET @commIDC = last_insert_id();
-- 创建商品C的条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commIDC, 'cjs88888', @commodityCreateTime, @commodityCreateTime);
SET @barcodeIDC = last_insert_id();
-- 商品C关联供应商
INSERT INTO t_providercommodity (F_CommodityID, F_ProviderID)
VALUES (@commIDC, 1);
SET @providerCommIDC = last_insert_id();
-- 创建商品C的入库单1(入库500件商品C,入库价10元,共5000元)
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_UpdateDatetime)
VALUES (2,'rk211901010001', 1, 1, 2, 4, @warehousingTime, @warehousingTime);
SET @warehouseC1 = last_insert_id();
-- 创建入库商品C1
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, -- 
F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehouseC1, @commIDC, @warehousingCommodityC_NO1, 3, '跨库商品C', @barcodeIDC, @warehousingCommodityC_Price1, (@warehousingCommodityC_Price1 * @warehousingCommodityC_NO1), -- 
@warehousingTime, 36, @warehousingTime, @warehousingTime, @warehousingTime, 5);
SET @warehouseCommC1 = last_insert_id();
-- 创建商品C的入库单2(入库500件商品C,入库价15元,共7500元)
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_UpdateDatetime)
VALUES (2,'rk211901010001', 1, 1, 2, 4, @warehousingTime, @warehousingTime);
SET @warehouseC2 = last_insert_id();
-- 创建入库商品C2
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, -- 
F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehouseC2, @commIDC, @warehousingCommodityC_NO2, 3, '跨库商品C', @barcodeIDC, @warehousingCommodityC_Price2, (@warehousingCommodityC_Price2 * @warehousingCommodityC_NO2), -- 
@warehousingTime, 36, @warehousingTime, @warehousingTime, @warehousingTime, 5);
SET @warehouseCommC2 = last_insert_id();
-- 员工A售卖商品A、商品B、商品C
-- 创建零售单A
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, -- 
F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, -- 
F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1,'LS2041020200000500010001',11068,1,'url=ashasoadigmnalskd',@rt1SaleDatetimeA,@staffID_A,1,'0',1,'…',-1, -- 
@rt1SaleDatetimeA, @rt1TotalAmountYesterday_A, @rt1AmuontCashYesterday_A, @rt1AmuontAliPayYesterday_A,@rt1AmuontWeChatYesterday_A,0.000000,0.000000,0.000000,0.000000,0.000000,1,NULL, -- 
NULL,NULL,NULL,NULL,NULL,2);
SET @rtIDA = last_insert_id();
-- 售卖商品A
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtIDA, @commIDA, '普通商品A', @barcodeIDA, (@rt1CommotidyNO1Yesterday_A + @rt1CommotidyNO2Yesterday_A), @commodityPriceA, (@rt1CommotidyNO1Yesterday_A + @rt1CommotidyNO2Yesterday_A), @rt1CommotidyPriceYesterday_A, 300, NULL);
SET @rtA_CommIDA = last_insert_id();
-- 创建零售单商品A的来源表1(对应的是入库单1)
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtA_CommIDA, @commIDA, @rt1CommotidyNO1Yesterday_A, @warehouseA1);
SET @rtA_CommsourceIDA1 = last_insert_id();
-- 创建零售单商品A的来源表2(对应的是入库单2)
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtA_CommIDA, @commIDA, @rt1CommotidyNO2Yesterday_A, @warehouseA2);
SET @rtA_CommsourceIDA2 = last_insert_id();
-- 售卖商品B
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtIDA, @commIDB, '普通商品B', @barcodeIDB, (@rt1CommotidyNO1Yesterday_B + @rt1CommotidyNO2Yesterday_B), @commodityPriceB, (@rt1CommotidyNO1Yesterday_B + @rt1CommotidyNO2Yesterday_B), @rt1CommotidyPriceYesterday_B, 200, NULL);
SET @rtA_CommIDB = last_insert_id();
-- 创建零售单商品B的来源表1(对应的是入库单1)
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtA_CommIDB, @commIDB, @rt1CommotidyNO1Yesterday_B, @warehouseB1);
SET @rtA_CommsourceIDB1 = last_insert_id();
-- 创建零售单商品B的来源表2(对应的是入库单2)
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtA_CommIDB, @commIDB, @rt1CommotidyNO2Yesterday_B, @warehouseB2);
SET @rtA_CommsourceIDB2 = last_insert_id();
-- 售卖商品C
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtIDA, @commIDC, '普通商品C', @barcodeIDC, (@rt1CommotidyNO1Yesterday_C + @rt1CommotidyNO2Yesterday_C), @commodityPriceC, (@rt1CommotidyNO1Yesterday_C + @rt1CommotidyNO2Yesterday_C), @rt1CommotidyPriceYesterday_C, 700, NULL);
SET @rtA_CommIDC = last_insert_id();
-- 创建零售单商品C的来源表1(对应的是入库单1)
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtA_CommIDC, @commIDC, @rt1CommotidyNO1Yesterday_C, @warehouseC1);
SET @rtA_CommsourceIDC1 = last_insert_id();
-- 创建零售单商品C的来源表2(对应的是入库单2)
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtA_CommIDC, @commIDC, @rt1CommotidyNO2Yesterday_C, @warehouseC2);
SET @rtA_CommsourceIDC2 = last_insert_id();

-- 员工B新建零售退货单B
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1,'LS2041020200000500010001_1',11069,1,'url=ashasoadigmnalskd',@rrt1SaleDatetimeB,@staffID_B,1,'0',1,'…',@rtIDA,@rrt1SaleDatetimeB,@rrt1TotalAmount_A,@rrt1AmuontCash_A,@rrt1AmuontAliPay_A,@rrt1AmuontWeChat_A,0.000000,0.000000,0.000000,0.000000,0.000000,1,NULL,NULL,NULL,NULL,NULL,NULL,2);
SET @returnIDB = last_insert_id();
-- 创建零售退货单商品A
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnIDB, @commIDA, '普通商品A', @barcodeIDA, @rrt1CommotidyNO2_A, @commodityPriceA, @rrt1CommotidyNO2_A, @rt1CommotidyPriceYesterday_A, 300, NULL);
SET @returnCommIDA = last_insert_id();
-- 创建零售退货单商品A的退货去向数据2(对应的是入库单2)
INSERT INTO t_returnretailtradecommoditydestination(F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@returnCommIDA, @commIDA, @rrt1CommotidyNO2_A, @warehouseA2);
SET @returnAggA = last_insert_id();
-- 创建零售退货单商品B
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnIDB, @commIDB, '普通商品B', @barcodeIDB, @rrt1CommotidyNO2_B, @commodityPriceB, @rrt1CommotidyNO2_B, @rt1CommotidyPriceYesterday_B, 200, NULL);
SET @returnCommIDB = last_insert_id();
-- 创建零售退货单商品B的退货去向数据2
INSERT INTO t_returnretailtradecommoditydestination(F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@returnCommIDB, @commIDB, @rrt1CommotidyNO2_B, @warehouseB2);
SET @returnAggB = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime = '2041-02-02 00:00:00';
SET @iDeleteOldData = 1;
SET @iShopID = 2;
-- 
CALL SP_RetailTradeDailyReportByStaff_Create(@iErrorCode, @sErrorMsg, @iShopID, @dSaleDatetime, @iDeleteOldData);
SELECT @iErrorCode, @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case21.1 Testing Result';
-- 
-- 验证员工业绩报表是否有对员工A有默认的数据的插入,根据时间、员工ID查询员工业绩报表
SET @countDefaultData = 0;
SELECT COUNT(1) INTO @countDefaultData FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime AND F_StaffID = @staffID_A;
SELECT IF(@countDefaultData = 1, '测试成功', '测试失败') AS 'Case21.2 Testing Result';
SET @totalAmountA = 0;
SELECT F_TotalAmount INTO @totalAmountA FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime AND F_StaffID = @staffID_A;
SELECT IF(@totalAmountA = @rggTotalAmount_A, '测试成功', '测试失败') AS 'Case21.3 Testing Result';
-- 
-- 验证零售单数，统计总的零售单和员工业绩报表的F_NO是否一致
SET @retailtradeNOA = 0;
SELECT count(1) INTO @retailtradeNOA FROM t_retailtrade WHERE F_SourceID = -1 AND F_StaffID = @staffID_A AND F_SaleDatetime BETWEEN @rggWorkTimeStart_A AND @rggWorkTimeEnd_A;
SET @rtNoByStaffA = 0;
SELECT F_NO INTO @rtNoByStaffA FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime AND F_StaffID = @staffID_A;
SELECT IF(@retailtradeNOA = @rtNoByStaffA, '测试成功', '测试失败') AS 'Case21.4 Testing Result';
-- 
-- 进行业绩报表的毛利结果验证
-- 销售毛利 = 销售总额 - 进货价 = 500 * 300 + 100 * 200 + 100 * 700 - (200 * 10 + 300 * 15) - (50 * 10 + 50 * 15) - (50 * 10 + 50*15) = 150000+20000+70000-6500-1250-1250=240000-9000=231000
-- 退货毛利 = 退货总额 - 退货成本 = 150000 + 20000 - (200 * 10 + 300 * 15) - (50 * 10 + 50 * 15) = 170000 - 7750 = 162250
SET @grossMargin = 0.000000;
SELECT F_GrossMargin INTO @grossMargin FROM t_retailtradedailyreportbystaff WHERE F_StaffID = @staffID_A AND F_Datetime = @dSaleDatetime;
SET @returnGrossMargin = (@rrt1CommotidyNO2_A * @warehousingCommodityA_Price2) + (@rrt1CommotidyNO2_B * @warehousingCommodityB_Price2) - @rrt1TotalAmount_A; -- 退货毛利
SELECT IF(@grossMargin = @returnGrossMargin, '测试成功', '测试失败') AS 'Case21.5 Testing Result';
-- 
-- 验证B的员工业绩(1)员工业绩报表是否有B的默认数据插入(2)销售总额(3)销售笔数(4)销售毛利
SET @countDefaultDataB = 0;
SELECT COUNT(1) INTO @countDefaultDataB FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime AND F_StaffID = @staffID_B;
SELECT IF(@countDefaultDataB = 1, '测试成功', '测试失败') AS 'Case29.6 Testing Result';
SET @totalAmountB = 0;
SELECT F_TotalAmount INTO @totalAmountB FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime AND F_StaffID = @staffID_B;
SELECT IF(@totalAmountB = 0.000000, '测试成功', '测试失败') AS 'Case29.7 Testing Result';
SET @retailtradeNOB = 0;
SELECT count(1) INTO @retailtradeNOB FROM t_retailtrade WHERE F_SourceID = -1 AND F_StaffID = @staffID_B AND F_SaleDatetime BETWEEN @rggWorkTimeStart_B AND @rggWorkTimeEnd_B;
SET @rtNoByStaffB = 0;
SELECT F_NO INTO @rtNoByStaffB FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime AND F_StaffID = @staffID_B;
SELECT IF(@retailtradeNOB = @rtNoByStaffB, '测试成功', '测试失败') AS 'Case29.8 Testing Result';
SET @grossMarginB = 0.000000;
SELECT F_GrossMargin INTO @grossMarginB FROM t_retailtradedailyreportbystaff WHERE F_StaffID = @staffID_B AND F_Datetime = @dSaleDatetime;
SELECT IF(@grossMarginB = 0.000000, '测试成功', '测试失败') AS 'Case29.9 Testing Result';
-- 
DELETE FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime;
DELETE FROM t_retailtradeaggregation WHERE F_ID = @rtgIDYesterday_A;
DELETE FROM t_retailtradeaggregation WHERE F_ID = @rtgIDA;
DELETE FROM t_retailtradeaggregation WHERE F_ID = @rtgIDB;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtA_CommsourceIDA1;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtA_CommsourceIDA2;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtA_CommsourceIDB1;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtA_CommsourceIDB2;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtA_CommsourceIDC1;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtA_CommsourceIDC2;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnAggA;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnAggB;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtA_CommIDA;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtA_CommIDB;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtA_CommIDC;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnCommIDA;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnCommIDB;
DELETE FROM t_retailtrade WHERE F_ID = @rtIDA;
DELETE FROM t_retailtrade WHERE F_ID = @returnIDB;
-- 商品A相关
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehouseCommA1;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehouseCommA2;
DELETE FROM t_warehousing WHERE F_ID = @warehouseA1;
DELETE FROM t_warehousing WHERE F_ID = @warehouseA2;
DELETE FROM t_providercommodity WHERE F_ID = @providerCommIDA;
DELETE FROM t_barcodes WHERE F_ID = @barcodeIDA;
DELETE FROM t_commodity WHERE F_ID = @commIDA;
-- 商品B相关
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehouseCommB1;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehouseCommB2;
DELETE FROM t_warehousing WHERE F_ID = @warehouseB1;
DELETE FROM t_warehousing WHERE F_ID = @warehouseB2;
DELETE FROM t_providercommodity WHERE F_ID = @providerCommIDB;
DELETE FROM t_barcodes WHERE F_ID = @barcodeIDB;
DELETE FROM t_commodity WHERE F_ID = @commIDB;
-- 商品C相关
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehouseCommC1;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehouseCommC2;
DELETE FROM t_warehousing WHERE F_ID = @warehouseC1;
DELETE FROM t_warehousing WHERE F_ID = @warehouseC2;
DELETE FROM t_providercommodity WHERE F_ID = @providerCommIDC;
DELETE FROM t_barcodes WHERE F_ID = @barcodeIDC;
DELETE FROM t_commodity WHERE F_ID = @commIDC;