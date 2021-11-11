
SELECT '++++++++++++++++++Test_SP_RetailTradeDailyReportByStaff_Create_case73.sql+++++++++++++++++++++++';

SELECT '----- case73:  员工A创建一个零售单卖A商品，商品A为未入库商品 ,卖B商品，对应一个入库单，对A部分退货，对商品B部分退货 [A] -------' AS 'Case73';
-- 
-- 员工ID
SET @staffIDA = 9;

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
-- 一张入库单
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_UpdateDatetime)
VALUES (2,'rk211901010001', 1, 1, 2, 4, '2119-01-01 06:00:00', '2119-01-01 06:00:00');
SET @warehouseB = last_insert_id();
-- 插入入库单商品表
SET @warehouseBNO = 500;
SET @warehouseBPrice = 13.333;
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehouseB, @commIDB, @warehouseBNO, 3, '跨库商品B', @barcodeIDB, @warehouseBPrice, 5000, '2119-01-01 06:00:00', 36, '2119-01-09 06:00:00', '2119-01-01 06:00:00', '2119-01-01 06:00:00', 5);
SET @warehouseCommB = last_insert_id();

-- 创建零售单
SET @saleCommANO_A = 251; -- 未入库，直接卖500
SET @aCommotidyPrice = 210.720000;
SET @saleCommBNO_A = 100;	-- 入库500，卖100
SET @bCommotidyPrice = 123.000000;
SET @rt1AmuontCash_A = @saleCommANO_A * @aCommotidyPrice + @saleCommBNO_A * @bCommotidyPrice;
SET @rt1AmuontWeChat_A = 0.000000;
SET @rt1AmuontAliPay_A = 0.000000;
SET @rt1TotalAmount_A = @rt1AmuontCash_A + @rt1AmuontWeChat_A + @rt1AmuontAliPay_A;
SET @saleDatetime1_A = '2041/02/02 06:00:00';
SET @syncDatetime1_A = '2041/02/02 05:00:00';

INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1,'LS2041020200000500010001',11068,1,'url=ashasoadigmnalskd',@saleDatetime1_A,@staffIDA,1,'0',1,'…',-1,@syncDatetime1_A, @rt1TotalAmount_A, @rt1AmuontCash_A, @rt1AmuontAliPay_A,@rt1AmuontWeChat_A,0.000000,0.000000,0.000000,0.000000,0.000000,1,NULL,NULL,NULL,NULL,NULL,NULL,2);
SET @rtID_A = last_insert_id();
-- 零售商品A
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtID_A, @commIDA, '普通商品A', @barcodeIDA, @saleCommANO_A, @aCommotidyPrice, @saleCommANO_A, @aCommotidyPrice, 300, NULL);
SET @rtCommAID_A = last_insert_id();
-- 零售商品B
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtID_A, @commIDB, '普通商品B', @barcodeIDB, @saleCommBNO_A, @bCommotidyPrice, @saleCommBNO_A, @bCommotidyPrice, 200, NULL);
SET @rtCommBID_A = last_insert_id();
-- 插入零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommAID_A, @commIDB, @saleCommBNO_A, @warehouseB);
SET @rtCommsourceIDB_A = last_insert_id();

-- 员工A新建退货单
SET @returnCommANO_A = 250; -- 部分退，退250
SET @returnCommBNO_A = 50;	-- 部分退，退50
SET @rrtAmuontCash_A = @returnCommANO_A * @aCommotidyPrice + @returnCommBNO_A * @bCommotidyPrice;
SET @rrtAmuontWeChat_A = 0.000000;
SET @rrtAmuontAliPay_A = 0.000000;
SET @rrtTotalAmount_A = @rrtAmuontCash_A + @rrtAmuontWeChat_A + @rrtAmuontAliPay_A;
SET @saleDatetime2_A = '2041/02/02 07:00:01';
SET @syncDatetime2_A = '2041/02/02 07:00:01';

INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1,'LS2041020200000500010001_1',11069,1,'url=ashasoadigmnalskd',@saleDatetime2_A,@staffIDA,1,'0',1,'…',@rtID_A,@syncDatetime2_A,@rrtTotalAmount_A,@rrtAmuontCash_A,@rrtAmuontWeChat_A,@rrtAmuontAliPay_A,0.000000,0.000000,0.000000,0.000000,0.000000,1,NULL,NULL,NULL,NULL,NULL,NULL,2);
SET @rrtID_A = last_insert_id();
-- 退货零售商品A
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rrtID_A, @commIDA, '普通商品A', @barcodeIDA, @returnCommANO_A, @aCommotidyPrice, @returnCommANO_A, @aCommotidyPrice, 300, NULL);
SET @returnCommAID_A = last_insert_id();
-- 退货零售商品B
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rrtID_A, @commIDB, '可口可乐', @barcodeIDB, @returnCommBNO_A, @bCommotidyPrice, @returnCommBNO_A, @bCommotidyPrice, 200, NULL);
SET @returnCommBID_A = last_insert_id();
-- 插入退货去向表，商品B
INSERT INTO t_returnretailtradecommoditydestination(F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@returnCommBID_A, @commIDB, @returnCommBNO_A, @warehouseB);
SET @returnCommBDestn = last_insert_id();

-- 零售汇总
SET @rgg1WorkTimeStart='2041/02/02 00:00:00';
SET @rgg1WorkTimeEnd='2041/02/02 23:59:59';
SET @rgg1TotalAmountCash_A = @rt1TotalAmount_A;
SET @rgg1TotalAmountWeChat_A = 0.000000;
SET @rgg1TotalAmountAliPay_A = 0.000000;
SET @rgg1TotalAmount_A = @rgg1TotalAmountCash_A + @rgg1TotalAmountWeChat_A + @rgg1TotalAmountAliPay_A;

INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
VALUES (@staffIDA, 2, @rgg1WorkTimeStart, @rgg1WorkTimeEnd, 1, @rgg1TotalAmount_A, 100.000000, @rgg1TotalAmountCash_A, @rgg1TotalAmountWeChat_A, @rgg1TotalAmountAliPay_A, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, now());
SET @rtgID_A = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime = '2041-02-02 00:00:00';
SET @deleteOldData = 1;
SET @iShopID = 2;
-- 
CALL SP_RetailTradeDailyReportByStaff_Create(@iErrorCode, @sErrorMsg, @iShopID, @dSaleDatetime, @deleteOldData);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 结果验证
-- 验证员工A有零售有退货
SET @saleTotalAmount_A = @rt1TotalAmount_A;
-- 售卖商品总成本
SET @saleTotalCost_A = 	@saleCommBNO_A * @warehouseBPrice; -- 商品A没入库，只有商品B入库了
-- 售卖商品总毛利
SET @saleTotalMargin_A = @saleTotalAmount_A - @saleTotalCost_A;
-- 退货总金额
SET @returnTotalAmount_A = @rrtTotalAmount_A;
-- 退货商品总成本
SET @returnTotalCost_A = @returnCommBNO_A * @warehouseBPrice;
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

SELECT IF(@iErrorCode = 0 AND @ResultVerification_A = 1,'测试成功','测试失败') AS 'Test Case73 Result';
-- 
DELETE FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime;
DELETE FROM t_retailtradeaggregation WHERE F_ID = @rtgID_A;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommsourceIDB_A;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnCommBDestn;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtCommAID_A;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtCommBID_A;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnCommAID_A;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnCommBID_A;
DELETE FROM t_retailtrade WHERE F_ID = @rtID_A;
DELETE FROM t_retailtrade WHERE F_ID = @rrtID_A;
-- 商品A相关
DELETE FROM t_providercommodity WHERE F_ID = @providerCommIDA;
DELETE FROM t_barcodes WHERE F_ID = @barcodeIDA;
DELETE FROM t_commodity WHERE F_ID = @commIDA;
-- 商品B相关
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehouseCommB;
DELETE FROM t_warehousing WHERE F_ID = @warehouseB;
DELETE FROM t_providercommodity WHERE F_ID = @providerCommIDB;
DELETE FROM t_barcodes WHERE F_ID = @barcodeIDB;
DELETE FROM t_commodity WHERE F_ID = @commIDB;