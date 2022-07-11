SELECT '++++++++++++++++++Test_SP_RetailTradeDailyReportByStaff_Create_case19.sql+++++++++++++++++++++++';

SELECT '----- case19:员工A创建一个零售单卖A商品，对应两个入库单 ,B商品，对应两个入库单 , C商品，对应两个入库单[A]-------' AS 'Case19';
-- 
SET @rt1CommotidyNO1 = 500;
SET @rt1CommotidyPrice1 = 300;
SET @rt1CommotidyNO2 = 100;
SET @rt1CommotidyPrice2 = 210;
SET @rt1CommotidyNO3 = 100;
SET @rt1CommotidyPrice3 = 700;
SET @rt1AmuontCash = 240000.000000;
SET @rt1AmuontWeChat = 0.000000;
SET @rt1AmuontAliPay = 0.000000;
SET @rt1TotalAmount = @rt1CommotidyNO1 * @rt1CommotidyPrice1 + @rt1CommotidyNO2 * @rt1CommotidyPrice2
					  +@rt1CommotidyNO3 * @rt1CommotidyPrice3;
-- 零售单收银汇总
SET @rggWorkTimeStart1='2041/02/02 00:00:00';
SET @rggWorkTimeEnd1='2041/02/02 23:59:59';
SET @rggTotalAmount1 =  @rt1TotalAmount;
SET @rggTotalAmountCash1 = @rt1AmuontCash;
SET @rggTotalAmountWeChat1 = @rt1AmuontWeChat;
SET @rggTotalAmountAliPay1 = @rt1AmuontAliPay;
SET @rggNO1 = 1;
-- 零售单商品来源表
SET @rtcs1NO = 211; 
SET @rtcs2NO = @rt1CommotidyNO1 - @rtcs1NO;  -- 
SET @rtcs3NO = 51;
SET @rtcs4NO = @rt1CommotidyNO2 - @rtcs3NO;  -- 
SET @rtcs5NO = 47;
SET @rtcs6NO = @rt1CommotidyNO3 - @rtcs6NO;  -- 
-- 入库单
SET @warehousingACommodityNO1=500;
SET @warehousingACommodityPrice1=10;
-- 
SET @warehousingACommodityNO2=500;
SET @warehousingACommodityPrice2=15;
-- 
SET @warehousingBCommodityNO1=500;
SET @warehousingBCommodityPrice1=10;
-- 
SET @warehousingBCommodityNO2=500;
SET @warehousingBCommodityPrice2=15;
-- 
SET @warehousingCCommodityNO1=500;
SET @warehousingCCommodityPrice1=10;
-- 
SET @warehousingCCommodityNO2=500;
SET @warehousingCCommodityPrice2=15;

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
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehouseA1, @commIDA, @warehousingACommodityNO1, 3, '跨库商品A', @barcodeIDA, @warehousingACommodityPrice1, 5000, '2119-01-01 06:00:00', 36, '2119-01-09 06:00:00', '2119-01-01 06:00:00', '2119-01-01 06:00:00', 5);
SET @warehouseCommA1 = last_insert_id();
-- 
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehouseA2, @commIDA, @warehousingACommodityNO2, 3, '跨库商品A', @barcodeIDA, @warehousingACommodityPrice2, 7500, '2119-01-01 06:00:00', 36, '2119-01-09 06:00:00', '2119-01-01 06:00:00', '2119-01-01 06:00:00', 5);
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
-- 需要一个商品来自两张不同的入库单，价格不同，进行售卖，然后进行退货
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_UpdateDatetime)
VALUES (2,'rk211901010001', 1, 1, 2, 4, '2119-01-01 06:00:00', '2119-01-01 06:00:00');
SET @warehouseB1 = last_insert_id();
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_UpdateDatetime)
VALUES (2,'rk211901010001', 1, 1, 2, 4, '2119-01-01 06:00:00', '2119-01-01 06:00:00');
SET @warehouseB2 = last_insert_id();
-- 插入入库单商品表
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehouseB1, @commIDB, @warehousingBCommodityNO1, 3, '跨库商品B', @barcodeIDB, @warehousingBCommodityPrice1, 5000, '2119-01-01 06:00:00', 36, '2119-01-09 06:00:00', '2119-01-01 06:00:00', '2119-01-01 06:00:00', 5);
SET @warehouseCommB1 = last_insert_id();
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehouseB2, @commIDB, @warehousingBCommodityNO2, 3, '跨库商品B', @barcodeIDB, @warehousingBCommodityPrice2, 7500, '2119-01-01 06:00:00', 36, '2119-01-09 06:00:00', '2119-01-01 06:00:00', '2119-01-01 06:00:00', 5);
SET @warehouseCommB2 = last_insert_id();
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
-- 需要一个商品来自两张不同的入库单，价格不同，进行售卖，然后进行退货
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_UpdateDatetime)
VALUES (2,'rk211901010001', 1, 1, 2, 4, '2119-01-01 06:00:00', '2119-01-01 06:00:00');
SET @warehouseC1 = last_insert_id();
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_UpdateDatetime)
VALUES (2,'rk211901010001', 1, 1, 2, 4, '2119-01-01 06:00:00', '2119-01-01 06:00:00');
SET @warehouseC2 = last_insert_id();
-- 插入入库单商品表
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehouseC1, @commIDC, @warehousingCCommodityNO1, 3, '跨库商品C', @barcodeIDC, @warehousingCCommodityPrice1, 5000, '2119-01-01 06:00:00', 36, '2119-01-09 06:00:00', '2119-01-01 06:00:00', '2119-01-01 06:00:00', 5);
SET @warehouseCommC1 = last_insert_id();
-- 
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehouseC2, @commIDC, @warehousingCCommodityNO2, 3, '跨库商品C', @barcodeIDC, @warehousingCCommodityPrice2, 7500, '2119-01-01 06:00:00', 36, '2119-01-09 06:00:00', '2119-01-01 06:00:00', '2119-01-01 06:00:00', 5);
SET @warehouseCommC2 = last_insert_id();

--  员工A创建一个零售单,售卖A、B、C商品
-- 员工A新建零售单
SET @staffIDA = 9;
INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
VALUES (@staffIDA, 2, @rggWorkTimeStart1, @rggWorkTimeEnd1, @rggNO1, @rggTotalAmount1, 240000.000000, @rggTotalAmountCash1, @rggTotalAmountWeChat1, @rggTotalAmountAliPay1, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, now());
SET @rtgID1 = last_insert_id();
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1,'LS2041020200000500010001',11068,1,'url=ashasoadigmnalskd','2041/02/02 06:00:00',@staffIDA,1,'0',1,'…',-1,'2041/02/01 06:00:00', @rt1TotalAmount, @rt1AmuontCash, @rt1AmuontWeChat,@rt1AmuontAliPay,0.000000,0.000000,0.000000,0.000000,0.000000,1,NULL,NULL,NULL,NULL,NULL,NULL,2);
SET @rtID1 = last_insert_id();

-- 零售商品A
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtID1, @commIDA, '普通商品A', @barcodeIDA, @rt1CommotidyNO1, 20, 500, @rt1CommotidyPrice1, 300, NULL);
SET @rtCommID1 = last_insert_id();
-- 插入零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommID1, @commIDA, @rtcs1NO, @warehouseA1);
SET @rtCommsourceIDA1 = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommID1, @commIDA, @rtcs2NO, @warehouseA2);
SET @rtCommsourceIDA2 = last_insert_id();
-- 零售商品B
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtID1, @commIDB, '普通商品B', @barcodeIDB, @rt1CommotidyNO2, 254, 100, @rt1CommotidyPrice2, 200, NULL);
SET @rtCommID2 = last_insert_id();
-- 插入零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommID1, @commIDB, @rtcs3NO, @warehouseB1);
SET @rtCommsourceIDB1 = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommID1, @commIDB, @rtcs4NO, @warehouseB2);
SET @rtCommsourceIDB2 = last_insert_id();
-- 零售商品C
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtID1, @commIDC, '普通商品C', @barcodeIDC, @rt1CommotidyNO3, 754, 100, @rt1CommotidyPrice3, 700, NULL);
SET @rtCommID3 = last_insert_id();
-- 插入零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommID3, @commIDC, @rtcs5NO, @warehouseC1);
SET @rtCommsourceIDC1 = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommID3, @commIDC, @rtcs6NO, @warehouseC2);
SET @rtCommsourceIDC2 = last_insert_id();
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
-- 
-- 售卖总金额
SET @saleTotalAmount = (@rt1CommotidyNO1 * @rt1CommotidyPrice1 + @rt1CommotidyNO2 * @rt1CommotidyPrice2 + @rt1CommotidyNO3 * @rt1CommotidyPrice3);
-- 售卖商品总成本
SET @saleTotalCost = (@rtcs1NO * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @warehouseA1 AND F_CommodityID = @commIDA))
					 +(@rtcs2NO * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @warehouseA2 AND F_CommodityID = @commIDA))
					 +(@rtcs3NO * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @warehouseB1 AND F_CommodityID = @commIDB))
					 +(@rtcs4NO * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @warehouseB2 AND F_CommodityID = @commIDB))
					 +(@rtcs5NO * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @warehouseC1 AND F_CommodityID = @commIDC))
					 +(@rtcs6NO * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @warehouseC2 AND F_CommodityID = @commIDC));
-- 售卖商品总毛利
SET @saleTotalMargin = @saleTotalAmount - @saleTotalCost;
-- 退货总金额
SET @returnTotalAmount = 0.00000;
-- 退货商品总成本
SET @returnTotalCost = 0.000000;
-- 退货商品总毛利
SET @returnTotalMargin = @returnTotalAmount - @returnTotalCost;
-- 总金额（售卖总金额 - 退货总金额）
SET @totalAmount = @saleTotalAmount - @returnTotalAmount;
-- 总毛利（售卖商品总毛利 - 退货商品总毛利）
SET @grossMargin = @saleTotalMargin - @returnTotalMargin;

SET @ResultVerification1=0;
-- 进行店员业绩报表的数据结果验证
SELECT 1 INTO @ResultVerification1
FROM t_retailtradedailyreportbystaff 
WHERE F_StaffID = @staffIDA
AND F_Datetime = @dSaleDatetime
AND F_TotalAmount = @totalAmount
AND F_GrossMargin = @grossMargin
AND F_NO = (
	SELECT COUNT(1) 
	FROM t_retailtrade 
	WHERE F_SourceID = -1 
	AND F_StaffID = @staffIDA
	AND datediff(F_SaleDatetime, @dSaleDatetime) = 0
);

SELECT IF(@iErrorCode = 0 AND @ResultVerification1 = 1,'测试成功','测试失败') AS 'Test Case19 Result';
-- 
DELETE FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime;
DELETE FROM t_retailtradeaggregation WHERE F_ID = @rtgID1;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommsourceIDA1;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommsourceIDA2;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommsourceIDB1;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommsourceIDB2;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommsourceIDC1;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommsourceIDC2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtCommID1;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtCommID2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtCommID3;
DELETE FROM t_retailtrade WHERE F_ID = @rtID1;
--	DELETE FROM t_retailtrade WHERE F_ID = @returnID2;
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