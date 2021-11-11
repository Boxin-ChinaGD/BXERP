SELECT '++++++++++++++++++Test_SP_RetailTradeDailyReportByStaff_Create_case45.sql+++++++++++++++++++++++';

SELECT '----- case45:员工A创建一个零售单卖A商品，对应两个入库单(一次入库价位15，一次为10) ,B商品，对应一个入库单 , C商品，对应一个入库单,创建一张退货单，对商品A全部退货,对B部分退货 [A]-------' AS 'Case45';
-- 
-- 创建一个商品A
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_CreateDatetime, F_UpdateDatetime)
VALUES (0, '跨库商品A', '普通商品A', '个', 3, '包', 1, 1, 'CJS666', 1, 
19, 19, 0, 1, 365, 3, '2119-01-01 09:00:00', 1, 0, 0, 0, 0, '2119-01-01 09:00:00', '2019-05-19 09:00:00');
SET @commIDA = last_insert_id();
-- 创建A的一个条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commIDA, 'cjs66666', '2119-01-01 09:00:00', '2119-01-01 09:00:00');
SET @barcodeIDA = last_insert_id();
-- 供应商与商品进行关联
INSERT INTO t_providercommodity (F_CommodityID, F_ProviderID)
VALUES (@commIDA, 1);
SET @providerCommIDA = last_insert_id();
-- 需要一个商品来自两张不同的入库单，价格不同，进行售卖，然后进行退货
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_UpdateDatetime)
VALUES (2,'rk211901010001', 1, 1, 2, 4, '2119-01-01 06:00:00', '2119-01-01 06:00:00');
SET @warehouseA1 = last_insert_id();
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_UpdateDatetime)
VALUES (2,'rk211901010001', 1, 1, 2, 4, '2119-01-01 06:00:00', '2119-01-01 06:00:00');
SET @warehouseA2 = last_insert_id();
-- 插入入库单商品表
SET @warehouseA1NO = 490;
SET @warehouseA2NO = 501;
SET @warehouseA1Price = 10.000000;
SET @warehouseA2Price = 15.000000;
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehouseA1, @commIDA, @warehouseA1NO, 3, '跨库商品A', @barcodeIDA, @warehouseA1Price, 5000, '2119-01-01 06:00:00', 36, '2119-01-09 06:00:00', '2119-01-01 06:00:00', '2119-01-01 06:00:00', 5);
SET @warehouseCommA1 = last_insert_id();
-- 
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehouseA2, @commIDA, @warehouseA2NO, 3, '跨库商品A', @barcodeIDA, @warehouseA2Price, 7500, '2119-01-01 06:00:00', 36, '2119-01-09 06:00:00', '2119-01-01 06:00:00', '2119-01-01 06:00:00', 5);
SET @warehouseCommA2 = last_insert_id();

-- 创建一个商品B
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_CreateDatetime, F_UpdateDatetime)
VALUES (0, '跨库商品B', '普通商品B', '个', 3, '包', 1, 1, 'CJS666', 1, 
19, 19, 0, 1, 365, 3, '2119-01-01 09:00:00', 1, 0, 0, 0, 0, '2119-01-01 09:00:00', '2019-05-19 09:00:00');
SET @commIDB = last_insert_id();
-- 创建B的一个条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commIDB, 'cjs77777', '2119-01-01 09:00:00', '2119-01-01 09:00:00');
SET @barcodeIDB = last_insert_id();
-- 供应商与商品进行关联
INSERT INTO t_providercommodity (F_CommodityID, F_ProviderID)
VALUES (@commIDB, 1);
SET @providerCommIDB = last_insert_id();
-- 商品B对应一个入库单
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_UpdateDatetime)
VALUES (2,'rk211901010001', 1, 1, 2, 4, '2119-01-01 06:00:00', '2119-01-01 06:00:00');
SET @warehouseB1 = last_insert_id();
-- 插入入库单商品表
SET @warehouseB1NO = 201;
SET @warehouseB1Price = 17.760000;
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehouseB1, @commIDB, @warehouseB1NO, 3, '跨库商品B', @barcodeIDB, @warehouseB1Price, 5000, '2119-01-01 06:00:00', 36, '2119-01-09 06:00:00', '2119-01-01 06:00:00', '2119-01-01 06:00:00', 5);
SET @warehouseCommB1 = last_insert_id();

-- 创建一个商品C
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_CreateDatetime, F_UpdateDatetime)
VALUES (0, '跨库商品C', '普通商品C', '个', 3, '包', 1, 1, 'CJS666', 1, 
19, 19, 0, 1, 365, 3, '2119-01-01 09:00:00', 1, 0, 0, 0, 0, '2119-01-01 09:00:00', '2019-05-19 09:00:00');
SET @commIDC = last_insert_id();
-- 创建C的一个条形码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commIDC, 'cjs88888', '2119-01-01 09:00:00', '2119-01-01 09:00:00');
SET @barcodeIDC = last_insert_id();
-- 供应商与商品进行关联
INSERT INTO t_providercommodity (F_CommodityID, F_ProviderID)
VALUES (@commIDC, 1);
SET @providerCommIDC = last_insert_id();
-- 商品B对应一个入库单
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_UpdateDatetime)
VALUES (2,'rk211901010001', 1, 1, 2, 4, '2119-01-01 06:00:00', '2119-01-01 06:00:00');
SET @warehouseC1 = last_insert_id();
-- 插入入库单商品表
SET @warehouseC1NO = 555;
SET @warehouseC1Price = 17.56;
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehouseC1, @commIDC, @warehouseC1NO, 3, '跨库商品C', @barcodeIDC, @warehouseC1Price, 5000, '2119-01-01 06:00:00', 36, '2119-01-09 06:00:00', '2119-01-01 06:00:00', '2119-01-01 06:00:00', 5);
SET @warehouseCommC1 = last_insert_id();

SET @staffIDA = 9;
-- 员工A创建一个零售单,售卖A、B、C商品
-- 员工A新建零售单  
-- 全部售卖
SET @saleCommANO_A = @warehouseA1NO + @warehouseA2NO;
SET @aCommotidyPrice = 300.000000;
SET @saleCommBNO_A = @warehouseB1NO ;
SET @bCommotidyPrice = 200.000000;
SET @saleCommCNO_A = @warehouseC1NO;
SET @cCommotidyPrice = 700.000000;
SET @rtAmuontCash_A = @saleCommANO_A * @aCommotidyPrice + @saleCommBNO_A * @bCommotidyPrice + @saleCommCNO_A * @cCommotidyPrice;
SET @rtAmuontWeChat_A = 0.000000;
SET @rtAmuontAliPay_A = 0.000000;
SET @rtTotalAmount_A = @rtAmuontCash_A + @rtAmuontWeChat_A + @rtAmuontAliPay_A;
SET @saleDatetime_A = '2041/02/02 06:00:00';
SET @syncDatetime_A = '2041/02/02 06:03:00';

INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1,'LS2041020200000500010001',11068,1,@saleDatetime_A,'2041/02/02 06:00:00',@staffIDA,1,'0',1,'…',-1,@syncDatetime_A, @rtTotalAmount_A, @rtAmuontCash_A, @rtAmuontAliPay_A,@rtAmuontWeChat_A,0.000000,0.000000,0.000000,0.000000,0.000000,1,NULL,NULL,NULL,NULL,NULL,NULL,2);
SET @rtID_A = last_insert_id();
-- 零售商品A
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtID_A, @commIDA, '普通商品A', @barcodeIDA, @saleCommANO_A, @aCommotidyPrice, @saleCommANO_A, @aCommotidyPrice, 300, NULL);
SET @rtCommAID1_A = last_insert_id();
-- 插入零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommAID1_A, @commIDA, @warehouseA1NO, @warehouseA1);
SET @rtCommsourceIDA1 = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommAID1_A, @commIDA, @warehouseA2NO, @warehouseA2);
SET @rtCommsourceIDA2 = last_insert_id();
-- 零售商品B
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtID_A, @commIDB, '普通商品B', @barcodeIDB, @saleCommBNO_A, @bCommotidyPrice, @saleCommBNO_A, @bCommotidyPrice, 200, NULL);
SET @rtCommBID_A = last_insert_id();
-- 插入零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommBID_A, @commIDB, @warehouseB1NO, @warehouseB1);
SET @rtCommsourceIDB1 = last_insert_id();
-- 零售商品C
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtID_A, @commIDC, '普通商品C', @barcodeIDC, @saleCommCNO_A, @cCommotidyPrice, @saleCommCNO_A, @cCommotidyPrice, 700, NULL);
SET @rtCommCID_A = last_insert_id();
-- 插入零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommCID_A, @commIDC, @warehouseC1NO, @warehouseC1);
SET @rtCommsourceIDC1 = last_insert_id();

-- 员工A新建退货单
-- 商品A全部退货,对B部分退货,C不退货
SET @returnCommANO_A = @saleCommANO_A; -- 全退
SET @returnCommBNO_A = 12; -- 部分退,总201
SET @rrtAmuontCash_A = @returnCommANO_A * @aCommotidyPrice + @returnCommBNO_A * @bCommotidyPrice;
SET @rrtAmuontWeChat_A = 0.000000;
SET @rrtAmuontAliPay_A = 0.000000;
SET @rrtTotalAmount_A = @rrtAmuontCash_A + @rrtAmuontWeChat_A + @rrtAmuontAliPay_A;
SET @saleDatetime2_A = '2041/02/02 7:00:01';
SET @syncDatetime2_A = '2041/02/02 7:00:01';

INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1,'LS2041020200000500010001_1',11069,1,'url=ashasoadigmnalskd',@saleDatetime2_A,@staffIDA,1,'0',1,'…',@rtID_A,@syncDatetime2_A,@rrtTotalAmount_A,@rrtAmuontCash_A,@rrtAmuontAliPay_A,@rrtAmuontWeChat_A,0.000000,0.000000,0.000000,0.000000,0.000000,1,NULL,NULL,NULL,NULL,NULL,NULL,2);
SET @returnID_A = last_insert_id();
-- 零售商品A
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnID_A, @commIDA, '普通商品A', @barcodeIDA, @returnCommANO_A, @aCommotidyPrice, @returnCommANO_A, @aCommotidyPrice, 300, NULL);
SET @returnCommAID_A = last_insert_id();
-- 零售商品B
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnID_A, @commIDB, '可口可乐', @barcodeIDB, @returnCommBNO_A, @bCommotidyPrice, @returnCommBNO_A, @bCommotidyPrice, 200, NULL);
SET @returnCommBID_A = last_insert_id();
-- 
-- 插入退货去向表，商品A
SET @NOReturnToA1 = @warehouseA1NO;
SET @NOReturnToA2 = @warehouseA2NO;
SET @NOReturnToB1 = @returnCommBNO_A;

INSERT INTO t_returnretailtradecommoditydestination(F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@returnCommAID_A, @commIDA, @warehouseA1NO, @warehouseA1);
SET @rrtCommA1Destination_A = last_insert_id();
INSERT INTO t_returnretailtradecommoditydestination(F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@returnCommAID_A, @commIDA, @warehouseA2NO, @warehouseA2);
SET @rrtCommA2Destination_A = last_insert_id();
-- 商品B
INSERT INTO t_returnretailtradecommoditydestination(F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@returnCommBID_A, @commIDB, @NOReturnToB1, @warehouseB1);
SET @rrtCommB1Destination_A = last_insert_id();
-- 
-- 零售单收银汇总
-- 用于02/02员工A的业绩验证
SET @rggTotalAmount_A1 = @rtTotalAmount_A;
SET @rggTotalAmountCash_A1 = @rtAmuontCash_A;
SET @rggTotalAmountWeChat_A1 = @rtAmuontWeChat_A;
SET @rggTotalAmountAliPay_A1 = @rtAmuontAliPay_A;
SET @rggWorkTimeStart1='2041/02/02 00:00:00';
SET @rggWorkTimeEnd1='2041/02/02 23:59:59';
SET @rggNO_A = 1;
INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
VALUES (@staffIDA, 2, @rggWorkTimeStart1, @rggWorkTimeEnd1, @rggNO_A, @rggTotalAmount_A1, 80000.000000, @rggTotalAmountCash_A1, @rggTotalAmountWeChat_A1, @rggTotalAmountAliPay_A1, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, now());
SET @rtgID1_A = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime = '2041-02-02 00:00:00';
SET @deleteOldData = 1; -- 仅测试使用 1，删除旧的数据。只能在测试代码中使用。0，不删除旧的数据。只能在功能代码中使用。
SET @iShopID = 2;
-- 
CALL SP_RetailTradeDailyReportByStaff_Create(@iErrorCode, @sErrorMsg, @iShopID, @dSaleDatetime, @deleteOldData);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 结果验证
-- 验证员工A在02/02当天零售
-- 售卖总金额
SET @saleTotalAmount_A = @rtTotalAmount_A;
-- 售卖商品总成本
SET @saleTotalCost_A = (@warehouseA1NO * @warehouseA1Price + @warehouseA2NO * @warehouseA2Price)
 					   +(@warehouseB1NO * @warehouseB1Price) 
 					   +(@warehouseC1NO * @warehouseC1Price);
-- 售卖商品总毛利
SET @saleTotalMargin_A = @saleTotalAmount_A - @saleTotalCost_A;
-- 退货总金额
SET @returnTotalAmount_A = @rrtTotalAmount_A;
-- 退货商品总成本
SET @returnTotalCost_A = (@NOReturnToA1 * @warehouseA1Price + @NOReturnToA2 * @warehouseA2Price) + 
						 (@NOReturnToB1 * @warehouseB1Price);
-- 退货商品总毛利
SET @returnTotalMargin_A = @returnTotalAmount_A - @returnTotalCost_A;
-- 总金额（售卖总金额 - 退货总金额）
SET @totalAmount_A = @saleTotalAmount_A - @returnTotalAmount_A;
-- 总毛利（售卖商品总毛利 - 退货商品总毛利）
SET @grossMargin_A = @saleTotalMargin_A - @returnTotalMargin_A;

SET @ResultVerification_A=0;
-- 进行员工A业绩报表的数据结果验证
SELECT 1 INTO @ResultVerification_A
FROM t_retailtradedailyreportbystaff 
WHERE F_StaffID = @staffIDA 
AND F_Datetime = @dSaleDatetime
AND F_TotalAmount = @totalAmount_A
AND F_GrossMargin = @grossMargin_A
AND F_NO = (
	SELECT COUNT(1) 
	FROM t_retailtrade 
	WHERE F_SourceID = -1 
	AND F_StaffID = @staffIDA
	AND datediff(F_SaleDatetime, @dSaleDatetime) = 0
 );

SELECT IF(@iErrorCode = 0 AND @ResultVerification_A = 1,'测试成功','测试失败') AS 'Test Case45 Result';
-- 
DELETE FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime;
DELETE FROM t_retailtradeaggregation WHERE F_ID = @rtgID1_A;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommsourceIDA1;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommsourceIDA2;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommsourceIDB1;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommsourceIDC1;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @rrtCommA1Destination_A;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @rrtCommA2Destination_A;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @rrtCommB1Destination_A;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtCommAID1_A;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtCommBID_A;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtCommCID_A;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnCommAID_A;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnCommBID_A;
DELETE FROM t_retailtrade WHERE F_ID = @rtID_A;
DELETE FROM t_retailtrade WHERE F_ID = @returnID_A;
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
DELETE FROM t_warehousing WHERE F_ID = @warehouseB1;
DELETE FROM t_providercommodity WHERE F_ID = @providerCommIDB;
DELETE FROM t_barcodes WHERE F_ID = @barcodeIDB;
DELETE FROM t_commodity WHERE F_ID = @commIDB;
-- 商品C相关
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehouseCommC1;
DELETE FROM t_warehousing WHERE F_ID = @warehouseC1;
DELETE FROM t_providercommodity WHERE F_ID = @providerCommIDC;
DELETE FROM t_barcodes WHERE F_ID = @barcodeIDC;
DELETE FROM t_commodity WHERE F_ID = @commIDC;