SELECT '++++++++++++++++++Test_SP_RetailTradeDailyReportByStaff_Create_case82.sql+++++++++++++++++++++++';


SELECT '----- case82:当天员工A上班，只进行销售，员工B上班，进行销售和退货（退自己或退别人），员工C上班，只进行退货，退员工A和员工B售出的零售单，查看数据统计及报表显示是否正常。[ABC]-------' AS 'Case82';

-- 员工A创建零售单A1，零售商品A，对应两个入库单，零售商品B，对应一个入库单，零售商品C，对应一个入库单
-- 员工A创建零售单A2，零售商品A，对应两个入库单，零售商品B，对应一个入库单，零售商品C，对应一个入库单
-- 员工B创建零售单B1，零售商品A，对应两个入库单，零售商品B，对应一个入库单，零售商品C，对应一个入库单
-- 员工B创建退货型零售单returnA1，部分退货商品A、B、C
-- 员工C创建退货型零售单returnA2，部分退货商品A、B、C
-- 员工C创建退货型零售单returnB1，部分退货商品A、B、C
-- 
--  员工A创建一个零售单,售卖A、B、C商品
-- 员工A新建零售单
SET @staffIDA = 9;
SET @staffIDB = 10;
SET @staffIDC = 11;
SET @dtStart = '2041/02/02 00:00:00';
SET @dtEnd = '2041/02/02 23:59:59';
INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
VALUES (@staffIDA, 2, @dtStart, @dtEnd, 1, 240000.000000, 240000.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, now());
SET @rtgID1 = last_insert_id();
INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
VALUES (@staffIDB, 2, @dtStart, @dtEnd, 1, -240000.000000, -240000.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, now());
SET @rtgID2 = last_insert_id();
INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
VALUES (@staffIDC, 2, @dtStart, @dtEnd, 1, -240000.000000, -240000.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, now());
SET @rtgID3 = last_insert_id();
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1,'LS2041020200000500010001',11068,1,'url=ashasoadigmnalskd','2041/02/02 06:00:00',@staffIDA,1,'0',1,'…',-1,'2041/02/02 06:00:00', 240000.000000, 240000.000000, 0.000000,0.000000,0.000000,0.000000,0.000000,0.000000,0.000000,1,NULL,NULL,NULL,NULL,NULL,NULL,2);
SET @rtID1 = last_insert_id();
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1,'LS2041020200000500010002',11069,1,'url=ashasoadigmnalske','2041/02/02 06:00:00',@staffIDB,1,'0',1,'…',-1,'2041/02/02 06:00:00', 240000.000000, 240000.000000, 0.000000,0.000000,0.000000,0.000000,0.000000,0.000000,0.000000,1,NULL,NULL,NULL,NULL,NULL,NULL,2);
SET @rtBID2 = last_insert_id();
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1,'LS2041020200000500010003',11078,1,'url=ashasoadigmnalske','2041/02/02 06:00:00',@staffIDA,1,'0',1,'…',-1,'2041/02/02 06:00:00', 240000.000000, 240000.000000, 0.000000,0.000000,0.000000,0.000000,0.000000,0.000000,0.000000,1,NULL,NULL,NULL,NULL,NULL,NULL,2);
SET @rtA2ID3 = last_insert_id();
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
VALUES (@warehouseA1, @commIDA, 500, 3, '跨库商品A', @barcodeIDA, 10, 5000, '2119-01-01 06:00:00', 36, '2119-01-09 06:00:00', '2119-01-01 06:00:00', '2119-01-01 06:00:00', 5);
SET @warehouseCommA1 = last_insert_id();
-- 
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehouseA2, @commIDA, 500, 3, '跨库商品A', @barcodeIDA, 15, 7500, '2119-01-01 06:00:00', 36, '2119-01-09 06:00:00', '2119-01-01 06:00:00', '2119-01-01 06:00:00', 5);
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
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehouseB1, @commIDB, 500, 3, '跨库商品B', @barcodeIDB, 10, 5000, '2119-01-01 06:00:00', 36, '2119-01-09 06:00:00', '2119-01-01 06:00:00', '2119-01-01 06:00:00', 5);
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
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehouseC1, @commIDC, 500, 3, '跨库商品C', @barcodeIDC, 10, 5000, '2119-01-01 06:00:00', 36, '2119-01-09 06:00:00', '2119-01-01 06:00:00', '2119-01-01 06:00:00', 5);
SET @warehouseCommC1 = last_insert_id();
-- 零售商品A,员工A的零售单
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtID1, @commIDA, '普通商品A', @barcodeIDA, 500, 20, 500, 300, 300, NULL);
SET @rtCommID1 = last_insert_id();
-- 零售商品A,员工B的零售单
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtBID2, @commIDA, '普通商品A', @barcodeIDA, 500, 20, 500, 300, 300, NULL);
SET @rtBCommID1 = last_insert_id();
-- 零售商品A,员工A的另一个零售单
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtA2ID3, @commIDA, '普通商品A', @barcodeIDA, 500, 20, 500, 300, 300, NULL);
SET @rtA2CommID1 = last_insert_id();
-- 插入零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommID1, @commIDA, 200, @warehouseA1);
SET @rtCommsourceIDA1 = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommID1, @commIDA, 300, @warehouseA2);
SET @rtCommsourceIDA2 = last_insert_id();
-- B的零售单来源表
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtBCommID1, @commIDA, 200, @warehouseA1);
SET @rtBCommsourceIDA1 = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtBCommID1, @commIDA, 300, @warehouseA2);
SET @rtBCommsourceIDA2 = last_insert_id();
-- A的零售单2来源表
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtA2CommID1, @commIDA, 200, @warehouseA1);
SET @rtA2CommsourceIDA1 = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtA2CommID1, @commIDA, 300, @warehouseA2);
SET @rtA2CommsourceIDA2 = last_insert_id();
-- 
-- 零售商品B
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtID1, @commIDB, '普通商品B', @barcodeIDB, 100, 254, 100, 200, 200, NULL);
SET @rtCommID2 = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtBID2, @commIDB, '普通商品B', @barcodeIDB, 100, 254, 100, 200, 200, NULL);
SET @rtBCommID2 = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtA2ID3, @commIDB, '普通商品B', @barcodeIDB, 100, 254, 100, 200, 200, NULL);
SET @rtA2CommID2 = last_insert_id();
-- 插入零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommID1, @commIDB, 100, @warehouseB1);
SET @rtCommsourceIDB1 = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtBCommID2, @commIDB, 100, @warehouseB1);
SET @rtBCommsourceIDB1 = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtA2CommID2, @commIDB, 100, @warehouseB1);
SET @rtA2CommsourceIDB1 = last_insert_id();
-- 零售商品C
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtID1, @commIDC, '普通商品C', @barcodeIDC, 100, 754, 100, 700, 700, NULL);
SET @rtCommID3 = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtBID2, @commIDC, '普通商品C', @barcodeIDC, 100, 754, 100, 700, 700, NULL);
SET @rtBCommID3 = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtA2ID3, @commIDC, '普通商品C', @barcodeIDC, 100, 754, 100, 700, 700, NULL);
SET @rtA2CommID3 = last_insert_id();
-- 插入零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommID3, @commIDC, 100, @warehouseC1);
SET @rtCommsourceIDC1 = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtBCommID3, @commIDC, 100, @warehouseC1);
SET @rtBCommsourceIDC1 = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtA2CommID3, @commIDC, 100, @warehouseC1);
SET @rtA2CommsourceIDC1 = last_insert_id();
-- 员工B新建退货单,退员工A的零售单A1
SET @returnAmountA = 120000.000000;
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1,'LS2041020200000500010001_1',11069,1,'url=ashasoadigmnalskd','2041/02/02 15:00:01',@staffIDB,1,'0',1,'…',@rtID1,'2041/02/02 15:00:01',@returnAmountA,@returnAmountA,0.000000,0.000000,0.000000,0.000000,0.000000,0.000000,0.000000,1,NULL,NULL,NULL,NULL,NULL,NULL,2);
SET @returnID2 = last_insert_id();
-- 员工C新建另一张退货单,退员工A的零售单A2
SET @returnAmountA = 120000.000000;
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1,'LS2041020200000500010003_1',11070,1,'url=ashasoadigmnalskd','2041/02/02 15:00:01',@staffIDC,1,'0',1,'…',@rtA2ID3,'2041/02/02 15:00:01',@returnAmountA,@returnAmountA,0.000000,0.000000,0.000000,0.000000,0.000000,0.000000,0.000000,1,NULL,NULL,NULL,NULL,NULL,NULL,2);
SET @returnA2ID2 = last_insert_id();
-- 员工C新建另一张退货单,退员工B的零售单
SET @returnAmountA = 120000.000000;
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1,'LS2041020200000500010002_1',11071,1,'url=ashasoadigmnalskd','2041/02/02 15:00:01',@staffIDC,1,'0',1,'…',@rtBID2,'2041/02/02 15:00:01',@returnAmountA,@returnAmountA,0.000000,0.000000,0.000000,0.000000,0.000000,0.000000,0.000000,1,NULL,NULL,NULL,NULL,NULL,NULL,2);
SET @returnBID2 = last_insert_id();
-- 零售商品A
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnID2, @commIDA, '普通商品A', 1, 500, 321, 250, 300, 300, NULL);
SET @returnCommID1 = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnA2ID2, @commIDA, '普通商品A', 1, 500, 321, 250, 300, 300, NULL);
SET @returnA2CommID1 = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnBID2, @commIDA, '普通商品A', 1, 500, 321, 250, 300, 300, NULL);
SET @returnBCommID1 = last_insert_id();
-- 插入零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@returnCommID1, @commIDA, 100, @warehouseA1);
SET @returnCommsourceIDA1 = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@returnCommID1, @commIDA, 150, @warehouseA2);
SET @returnCommsourceIDA2 = last_insert_id();
-- 
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@returnA2CommID1, @commIDA, 100, @warehouseA1);
SET @returnA2CommsourceIDA1 = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@returnA2CommID1, @commIDA, 150, @warehouseA2);
SET @returnA2CommsourceIDA2 = last_insert_id();
-- 
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@returnBCommID1, @commIDA, 100, @warehouseA1);
SET @returnBCommsourceIDA1 = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@returnBCommID1, @commIDA, 150, @warehouseA2);
SET @returnBCommsourceIDA2 = last_insert_id();
-- 零售商品B
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnID2, @commIDB, '可口可乐', 3, 100, 254, 50, 200, 200, NULL);
SET @returnCommID2 = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnA2ID2, @commIDB, '可口可乐', 3, 100, 254, 50, 200, 200, NULL);
SET @returnA2CommID2 = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnBID2, @commIDB, '可口可乐', 3, 100, 254, 50, 200, 200, NULL);
SET @returnBCommID2 = last_insert_id();
-- 插入零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@returnCommID2, @commIDB, 50, @warehouseB1);
SET @returnCommsourceIDB1 = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@returnA2CommID2, @commIDB, 50, @warehouseB1);
SET @returnA2CommsourceIDB1 = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@returnBCommID2, @commIDB, 50, @warehouseB1);
SET @returnBCommsourceIDB1 = last_insert_id();
-- 零售商品C
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnID2, @commIDC, '普通商品C', @barcodeIDC, 100, 754, 50, 700, 700, NULL);
SET @returnCommID3 = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnA2ID2, @commIDC, '普通商品C', @barcodeIDC, 100, 754, 50, 700, 700, NULL);
SET @returnA2CommID3 = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnBID2, @commIDC, '普通商品C', @barcodeIDC, 100, 754, 50, 700, 700, NULL);
SET @returnBCommID3 = last_insert_id();
-- 插入零售单商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@returnCommID3, @commIDC, 50, @warehouseC1);
SET @returnCommsourceIDC1 = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@returnA2CommID3, @commIDC, 50, @warehouseC1);
SET @returnA2CommsourceIDC1 = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@returnBCommID3, @commIDC, 50, @warehouseC1);
SET @returnBCommsourceIDC1 = last_insert_id();
-- 
-- B创建的退货零售单A1的退货去向表，商品A
INSERT INTO t_returnretailtradecommoditydestination(F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@returnCommID1, @commIDA, 150, @warehouseA2);
SET @returnA1AggA1 = last_insert_id();
INSERT INTO t_returnretailtradecommoditydestination(F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@returnCommID1, @commIDA, 100, @warehouseA1);
SET @returnA1AggA2 = last_insert_id();
-- 商品B
INSERT INTO t_returnretailtradecommoditydestination(F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@returnCommID2, @commIDB, 50, @warehouseB1);
SET @returnA1AggB1 = last_insert_id();
-- 商品C
INSERT INTO t_returnretailtradecommoditydestination(F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@returnCommID3, @commIDC, 50, @warehouseC1);
SET @returnA1AggC1 = last_insert_id();
-- 退货零售单A2的退货去向表，商品A
INSERT INTO t_returnretailtradecommoditydestination(F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@returnA2CommID1, @commIDA, 150, @warehouseA2);
SET @returnA2AggA1 = last_insert_id();
INSERT INTO t_returnretailtradecommoditydestination(F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@returnA2CommID1, @commIDA, 100, @warehouseA1);
SET @returnA2AggA2 = last_insert_id();
-- 商品B
INSERT INTO t_returnretailtradecommoditydestination(F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@returnA2CommID2, @commIDB, 50, @warehouseB1);
SET @returnA2AggB1 = last_insert_id();
-- 商品C
INSERT INTO t_returnretailtradecommoditydestination(F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@returnA2CommID3, @commIDC, 50, @warehouseC1);
SET @returnA2AggC1 = last_insert_id();
-- C创建的退货零售单退B的退货去向表，商品A
INSERT INTO t_returnretailtradecommoditydestination(F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@returnBCommID1, @commIDA, 150, @warehouseA2);
SET @returnBAggA1 = last_insert_id();
INSERT INTO t_returnretailtradecommoditydestination(F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@returnBCommID1, @commIDA, 100, @warehouseA1);
SET @returnBAggA2 = last_insert_id();
-- 商品B
INSERT INTO t_returnretailtradecommoditydestination(F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@returnBCommID2, @commIDB, 50, @warehouseB1);
SET @returnBAggB1 = last_insert_id();
-- 商品C
INSERT INTO t_returnretailtradecommoditydestination(F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@returnBCommID3, @commIDC, 50, @warehouseC1);
SET @returnBAggC1 = last_insert_id();
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
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case82.1 Testing Result';
-- 
-- 验证A的员工业绩(1)员工业绩报表是否有B的默认数据插入(2)销售总额(3)销售笔数(4)销售毛利
SET @countDefaultDataA = 0;
SELECT COUNT(1) INTO @countDefaultDataA FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime AND F_StaffID = @staffIDA;
SELECT IF(@countDefaultDataA = 1, '测试成功', '测试失败') AS 'Case82.2 Testing Result';
SET @totalAmountA = 0;
SELECT F_TotalAmount INTO @totalAmountA FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime AND F_StaffID = @staffIDA;
SELECT IF(@totalAmountA = 240000.000000, '测试成功', '测试失败') AS 'Case82.3 Testing Result';
SET @retailtradeNOA = 0;
SELECT count(1) INTO @retailtradeNOA FROM t_retailtrade WHERE F_SourceID = -1 AND F_StaffID = @staffIDA AND F_SaleDatetime BETWEEN @dtStart AND @dtEnd;
SET @rtNoByStaffA = 0;
SELECT F_NO INTO @rtNoByStaffA FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime AND F_StaffID = @staffIDA;
SELECT IF(@retailtradeNOA = @rtNoByStaffA, '测试成功', '测试失败') AS 'Case82.4 Testing Result';
SET @grossMarginA = 0.000000;
-- 销售毛利 = 销售总额 - 进货价 = 500 * 300 + 100 * 200 + 100 * 700 - (200 * 10 + 300 * 15)- 100*10 - 100*10 = 150000+20000+70000-6500-1000-1000=240000-8500=231500
-- 退货毛利 = 退货总额 - 进货价 = 120000 - 4250 = 115750
SELECT F_GrossMargin INTO @grossMarginA FROM t_retailtradedailyreportbystaff WHERE F_StaffID = @staffIDA AND F_Datetime = @dSaleDatetime;
SELECT IF(@grossMarginA = 231500.000000, '测试成功', '测试失败') AS 'Case82.5 Testing Result';
-- 验证B的员工业绩(1)员工业绩报表是否有B的默认数据插入(2)销售总额(3)销售笔数(4)销售毛利
SET @countDefaultDataB = 0;
SELECT COUNT(1) INTO @countDefaultDataB FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime AND F_StaffID = @staffIDB;
SELECT IF(@countDefaultDataB = 1, '测试成功', '测试失败') AS 'Case82.6 Testing Result';
SET @totalAmountB = 0;
SELECT F_TotalAmount INTO @totalAmountB FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime AND F_StaffID = @staffIDB;
SELECT IF(@totalAmountB = 120000.000000, '测试成功', '测试失败') AS 'Case82.7 Testing Result';
SET @retailtradeNOB = 0;
SELECT count(1) INTO @retailtradeNOB FROM t_retailtrade WHERE F_SourceID = -1 AND F_StaffID = @staffIDB AND F_SaleDatetime BETWEEN @dtStart AND @dtEnd;
SET @rtNoByStaffB = 0;
SELECT F_NO INTO @rtNoByStaffB FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime AND F_StaffID = @staffIDB;
SELECT IF(@retailtradeNOB = @rtNoByStaffB, '测试成功', '测试失败') AS 'Case82.8 Testing Result';
SET @grossMarginB = 0.000000;
SELECT F_GrossMargin INTO @grossMarginB FROM t_retailtradedailyreportbystaff WHERE F_StaffID = @staffIDB AND F_Datetime = @dSaleDatetime;
SELECT IF(@grossMarginB = 115750.000000, '测试成功', '测试失败') AS 'Case82.9 Testing Result';
-- 验证C的员工业绩(1)员工业绩报表是否有B的默认数据插入(2)销售总额(3)销售笔数(4)销售毛利
SET @countDefaultDataC = 0;
SELECT COUNT(1) INTO @countDefaultDataC FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime AND F_StaffID = @staffIDC;
SELECT IF(@countDefaultDataC = 1, '测试成功', '测试失败') AS 'Case82.10 Testing Result';
SET @totalAmountC = 0;
SELECT F_TotalAmount INTO @totalAmountC FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime AND F_StaffID = @staffIDC;
SELECT IF(@totalAmountC = 0.000000, '测试成功', '测试失败') AS 'Case82.11 Testing Result';
SET @retailtradeNOC = 0;
SELECT count(1) INTO @retailtradeNOC FROM t_retailtrade WHERE F_SourceID = -1 AND F_StaffID = @staffIDC AND F_SaleDatetime BETWEEN @dtStart AND @dtEnd;
SET @rtNoByStaffC = 0;
SELECT F_NO INTO @rtNoByStaffC FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime AND F_StaffID = @staffIDC;
SELECT IF(@retailtradeNOC = @rtNoByStaffC, '测试成功', '测试失败') AS 'Case82.12 Testing Result';
SET @grossMarginC = 0.000000;
SELECT F_GrossMargin INTO @grossMarginC FROM t_retailtradedailyreportbystaff WHERE F_StaffID = @staffIDC AND F_Datetime = @dSaleDatetime;
SELECT IF(@grossMarginC = 0.000000, '测试成功', '测试失败') AS 'Case82.13 Testing Result';
-- 
-- 
DELETE FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime;
DELETE FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime2;
DELETE FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime3;
DELETE FROM t_retailtradeaggregation WHERE F_ID = @rtgID1;
DELETE FROM t_retailtradeaggregation WHERE F_ID = @rtgID2;
DELETE FROM t_retailtradeaggregation WHERE F_ID = @rtgID3;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommsourceIDA1;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommsourceIDA2;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtBCommsourceIDA1;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtBCommsourceIDA2;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtA2CommsourceIDA1;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtA2CommsourceIDA2;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommsourceIDB1;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtBCommsourceIDB1;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtA2CommsourceIDB1;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommsourceIDC1;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtBCommsourceIDC1;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtA2CommsourceIDC1;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @returnCommsourceIDA1;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @returnCommsourceIDA2;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @returnA2CommsourceIDA1;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @returnA2CommsourceIDA2;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @returnBCommsourceIDA1;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @returnBCommsourceIDA2;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @returnCommsourceIDB1;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @returnA2CommsourceIDB1;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @returnBCommsourceIDB1;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @returnCommsourceIDC1;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @returnA2CommsourceIDC1;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @returnBCommsourceIDC1;
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnA1AggA1;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnA1AggA2;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnA1AggB1;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnA1AggC1;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnA2AggA1;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnA2AggA2;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnA2AggB1;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnA2AggC1;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnBAggA1;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnBAggA2;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnBAggB1;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnBAggC1;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtCommID1;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtBCommID1;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtA2CommID1;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtCommID2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtBCommID2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtA2CommID2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtCommID3;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtBCommID3;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtA2CommID3;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnCommID1;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnCommID2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnCommID3;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnA2CommID1;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnA2CommID2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnA2CommID3;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnBCommID1;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnBCommID2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnBCommID3;
DELETE FROM t_retailtrade WHERE F_ID = @rtID1;
DELETE FROM t_retailtrade WHERE F_ID = @rtBID2;
DELETE FROM t_retailtrade WHERE F_ID = @rtA2ID3;
DELETE FROM t_retailtrade WHERE F_ID = @returnID2;
DELETE FROM t_retailtrade WHERE F_ID = @returnA2ID2;
DELETE FROM t_retailtrade WHERE F_ID = @returnBID2;
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
