SELECT '++++++++++++++++++Test_SP_RetailTradeDailyReportByStaff_Create_case79.sql+++++++++++++++++++++++';

-- 员工连续上两天班，生成两天的报表的数据还未实现，先注释该用例
-- 本来以为收银汇总可以跨天，后来不支持跨天，所以本测试用例不需要实现了，因为在其它用例中已经实现。

--	SELECT '----- case79:day1员工A和员工B上班，均只进行销售，其中B通宵上班直接上到day2中午12:00，查看数据统计及报表显示是否正常。 [AB、B]-------' AS 'Case79';
--	-- 员工A销售A商品，对应两个入库单，商品B，对应一个个入库单，商品C，对应一个入库单
--	-- 员工B销售A商品，对应两个入库单，商品B，对应一个个入库单，商品C，对应一个入库单
--	-- 
--	-- 员工ID
--	SET @staffIDA = 9;
--	SET @staffIDB = 10;
--	
--	-- 创建一个商品A
--	INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, F_LatestPricePurchase, F_PriceRetail, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_NO, F_Type, F_NOStart, F_PurchasingPriceStart, F_CreateDatetime, F_UpdateDatetime)
--	VALUES (0, '跨库商品A', '普通商品A', '个', 3, '包', 1, 1, 'CJS666', 1, -1, 20, 19, 19, 0, 1, 365, 3, '2119-01-01 09:00:00', 1, 0, 0, 0, 20, 0, -1, -1, '2119-01-01 09:00:00', '2019-05-19 09:00:00');
--	SET @commIDA = last_insert_id();
--	-- 创建A的一个条形码
--	INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
--	VALUES (@commIDA, 'cjs66666', '2119-01-01 09:00:00', '2119-01-01 09:00:00');
--	SET @barcodeIDA = last_insert_id();
--	-- 供应商与商品进行关联
--	INSERT INTO t_providercommodity (F_CommodityID, F_ProviderID)
--	VALUES (@commIDA, 1);
--	SET @providerCommIDA = last_insert_id();
--	-- 需要一个商品来自两张不同的入库单，价格不同，进行售卖，然后进行退货
--	INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_UpdateDatetime)
--	VALUES (2,'rk211901010001', 1, 1, 2, 4, '2119-01-01 06:00:00', '2119-01-01 06:00:00');
--	SET @warehouseA1 = last_insert_id();
--	INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_UpdateDatetime)
--	VALUES (2,'rk211901010001', 1, 1, 2, 4, '2119-01-01 06:00:00', '2119-01-01 06:00:00');
--	SET @warehouseA2 = last_insert_id();
--	-- 插入入库单商品表
--	SET @warehouseA1NO = 400; -- 商品A第一次入库400
--	SET @warehouseA2NO = 600; -- 商品A第二次入库600
--	SET @warehouseA1Price = 10; -- 商品A第一次入库价10元
--	SET @warehouseA2Price = 15; -- 商品A第二次入库价15元
--	INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
--	VALUES (@warehouseA1, @commIDA, @warehouseA1NO, 3, '跨库商品A', @barcodeIDA, @warehouseA1Price, 4000, '2119-01-01 06:00:00', 36, '2119-01-09 06:00:00', '2119-01-01 06:00:00', '2119-01-01 06:00:00', 5);
--	SET @warehouseCommA1 = last_insert_id();
--	-- 
--	INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
--	VALUES (@warehouseA2, @commIDA, @warehouseA2NO, 3, '跨库商品A', @barcodeIDA, @warehouseA2Price, 9000, '2119-01-01 06:00:00', 36, '2119-01-09 06:00:00', '2119-01-01 06:00:00', '2119-01-01 06:00:00', 5);
--	SET @warehouseCommA2 = last_insert_id();
--	-- 创建一个商品B
--	INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, F_LatestPricePurchase, F_PriceRetail, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_NO, F_Type, F_NOStart, F_PurchasingPriceStart, F_CreateDatetime, F_UpdateDatetime)
--	VALUES (0, '跨库商品B', '普通商品B', '个', 3, '包', 1, 1, 'CJS666', 1, -1, 20, 19, 19, 0, 1, 365, 3, '2119-01-01 09:00:00', 1, 0, 0, 0, 20, 0, -1, -1, '2119-01-01 09:00:00', '2019-05-19 09:00:00');
--	SET @commIDB = last_insert_id();
--	-- 创建B的一个条形码
--	INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
--	VALUES (@commIDB, 'cjs77777', '2119-01-01 09:00:00', '2119-01-01 09:00:00');
--	SET @barcodeIDB = last_insert_id();
--	-- 供应商与商品进行关联
--	INSERT INTO t_providercommodity (F_CommodityID, F_ProviderID)
--	VALUES (@commIDB, 1);
--	SET @providerCommIDB = last_insert_id();
--	-- 商品B对应一个入库单
--	INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_UpdateDatetime)
--	VALUES (2,'rk211901010001', 1, 1, 2, 4, '2119-01-01 06:00:00', '2119-01-01 06:00:00');
--	SET @warehouseB = last_insert_id();
--	-- 插入入库单商品表
--	SET @warehouseBNO = 500; -- 商品B入库500
--	SET @warehouseBPrice = 10; -- 商品B入库价10
--	INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
--	VALUES (@warehouseB, @commIDB, @warehouseBNO, 3, '跨库商品B', @barcodeIDB, @warehouseBPrice, 5000, '2119-01-01 06:00:00', 36, '2119-01-09 06:00:00', '2119-01-01 06:00:00', '2119-01-01 06:00:00', 5);
--	SET @warehouseCommB = last_insert_id();
--	-- 创建一个商品C
--	INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, F_LatestPricePurchase, F_PriceRetail, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_NO, F_Type, F_NOStart, F_PurchasingPriceStart, F_CreateDatetime, F_UpdateDatetime)
--	VALUES (0, '跨库商品C', '普通商品C', '个', 3, '包', 1, 1, 'CJS666', 1, -1, 20, 19, 19, 0, 1, 365, 3, '2119-01-01 09:00:00', 1, 0, 0, 0, 20, 0, -1, -1, '2119-01-01 09:00:00', '2019-05-19 09:00:00');
--	SET @commIDC = last_insert_id();
--	-- 创建C的一个条形码
--	INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
--	VALUES (@commIDC, 'cjs88888', '2119-01-01 09:00:00', '2119-01-01 09:00:00');
--	SET @barcodeIDC = last_insert_id();
--	-- 供应商与商品进行关联
--	INSERT INTO t_providercommodity (F_CommodityID, F_ProviderID)
--	VALUES (@commIDC, 1);
--	SET @providerCommIDC = last_insert_id();
--	-- 商品C对应一个入库单
--	INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_UpdateDatetime)
--	VALUES (2,'rk211901010001', 1, 1, 2, 4, '2119-01-01 06:00:00', '2119-01-01 06:00:00');
--	SET @warehouseC = last_insert_id();
--	-- 插入入库单商品表
--	SET @warehouseCNO = 500; -- 商品C入库500
--	SET @warehouseCPrice = 10; -- 商品C入库价10
--	INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
--	VALUES (@warehouseC, @commIDC, @warehouseCNO, 3, '跨库商品C', @barcodeIDC, @warehouseCPrice, 5000, '2119-01-01 06:00:00', 36, '2119-01-09 06:00:00', '2119-01-01 06:00:00', '2119-01-01 06:00:00', 5);
--	SET @warehouseCommC = last_insert_id();
--	-- 员工A创建零售单
--	SET @saleCommANO_A = 500; -- 卖500件
--	SET @aCommotidyPrice = 300.000000; -- 商品A价格
--	SET @saleCommBNO_A = 100;	-- 卖100件
--	SET @bCommotidyPrice = 200.000000; -- 商品B价格
--	SET @saleCommCNO_A = 100;	-- 卖100件
--	SET @cCommodityPrice = 700.000000; -- 商品C价格
--	SET @rt1AmuontCash_A = @saleCommANO_A * @aCommotidyPrice + @saleCommBNO_A * @bCommotidyPrice + @saleCommCNO_A * @cCommodityPrice;
--	SET @rt1AmuontWeChat_A = 0.000000;
--	SET @rt1AmuontAliPay_A = 0.000000;
--	SET @rt1TotalAmount_A = @rt1AmuontCash_A + @rt1AmuontWeChat_A + @rt1AmuontAliPay_A;
--	SET @saleDatetime1_A = '2041/02/02 06:00:00';
--	SET @syncDatetime1_A = '2041/02/02 05:00:00';
--	
--	INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
--	VALUES (1,'LS2041020200000500010001',11068,1,'url=ashasoadigmnalskd',@saleDatetime1_A,@staffIDA,1,'0',1,'…',-1,@syncDatetime1_A, @rt1TotalAmount_A, @rt1AmuontCash_A, @rt1AmuontAliPay_A,@rt1AmuontWeChat_A,0.000000,0.000000,0.000000,0.000000,0.000000,1,NULL,NULL,NULL,NULL,NULL,NULL);
--	SET @rtID_A = last_insert_id();
--	-- 零售商品A
--	INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
--	VALUES (@rtID_A, @commIDA, '普通商品A', @barcodeIDA, @saleCommANO_A, @aCommotidyPrice, @saleCommANO_A, @aCommotidyPrice, 300, NULL);
--	SET @rtCommAID_A = last_insert_id();
--	-- 插入零售来源
--	SET @NOSaleFromWarehousingA1_A = 200; -- 从入库单A1卖出200
--	SET @NOSaleFromWarehousingA2_A = 300;	-- 从入库单A2卖出300
--	SET @NOSaleFromWarehousingB_A = 100;
--	SET @NOSaleFromWarehousingC_A = 100;
--	INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
--	VALUES (@rtCommAID_A, @commIDA, @NOSaleFromWarehousingA1_A, @warehouseA1);
--	SET @rtCommsourceIDA1_A = last_insert_id();
--	INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
--	VALUES (@rtCommAID_A, @commIDA, @NOSaleFromWarehousingA2_A, @warehouseA2);
--	SET @rtCommsourceIDA2_A = last_insert_id();
--	-- 零售商品B
--	INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
--	VALUES (@rtID_A, @commIDB, '普通商品B', @barcodeIDB, @saleCommBNO_A, @bCommotidyPrice, @saleCommBNO_A, @bCommotidyPrice, 200, NULL);
--	SET @rtCommBID_A = last_insert_id();
--	-- 插入零售来源
--	INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
--	VALUES (@rtCommAID_A, @commIDB, @NOSaleFromWarehousingB_A, @warehouseB);
--	SET @rtCommsourceIDB_A = last_insert_id();
--	-- 零售商品C
--	INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
--	VALUES (@rtID_A, @commIDC, '普通商品C', @barcodeIDC, @saleCommCNO_A, @cCommodityPrice, @saleCommCNO_A, @cCommodityPrice, 700, NULL);
--	SET @rtCommCID_A = last_insert_id();
--	-- 插入零售来源
--	INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
--	VALUES (@rtCommCID_A, @commIDC, @NOSaleFromWarehousingC_A, @warehouseC);
--	SET @rtCommsourceIDC_A = last_insert_id();
--	
--	-- 员工B的零售单
--	SET @saleCommANO_B = 500; -- 卖500件
--	SET @saleCommBNO_B = 100;	-- 卖100件
--	SET @saleCommCNO_B = 100;	-- 卖100件
--	SET @rt2AmuontCash_B = @saleCommANO_B * @aCommotidyPrice + @saleCommBNO_B * @bCommotidyPrice + @saleCommCNO_B * @cCommodityPrice;
--	SET @rt2AmuontWeChat_B = 0.000000;
--	SET @rt2AmuontAliPay_B = 0.000000;
--	SET @rt2TotalAmount_B = @rt2AmuontCash_B + @rt2AmuontWeChat_B + @rt2AmuontAliPay_B;
--	SET @saleDatetime2_B = '2041/02/02 07:00:00';
--	SET @syncDatetime2_B = '2041/02/02 05:00:00';
--	
--	INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
--	VALUES (1,'LS2041020200000500010002',11069,1,'url=ashasoadigmnalske',@saleDatetime2_B,@staffIDB,1,'0',1,'…',-1,@syncDatetime2_B, @rt2TotalAmount_B, @rt2AmuontCash_B, @rt2AmuontAliPay_B,@rt2AmuontWeChat_B,0.000000,0.000000,0.000000,0.000000,0.000000,1,NULL,NULL,NULL,NULL,NULL,NULL);
--	SET @rtID_B = last_insert_id();
--	-- 零售商品A
--	INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
--	VALUES (@rtID_B, @commIDA, '普通商品A', @barcodeIDA, @saleCommANO_B, @aCommotidyPrice, @saleCommANO_B, @aCommotidyPrice, 300, NULL);
--	SET @rtCommAID_B = last_insert_id();
--	-- 插入零售单商品来源
--	SET @NOSaleFromWarehousingA1_B = 200; -- 从入库单A1卖出200
--	SET @NOSaleFromWarehousingA2_B = 300;	-- 从入库单A2卖出300
--	SET @NOSaleFromWarehousingB_B = 100;
--	SET @NOSaleFromWarehousingC_B = 100;
--	INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
--	VALUES (@rtCommAID_B, @commIDA, @NOSaleFromWarehousingA1_B, @warehouseA1);
--	SET @rtCommsourceIDA1_B = last_insert_id();
--	INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
--	VALUES (@rtCommAID_B, @commIDA, @NOSaleFromWarehousingA2_B, @warehouseA2);
--	SET @rtCommsourceIDA2_B = last_insert_id();
--	-- 零售商品B
--	INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
--	VALUES (@rtID_B, @commIDB, '普通商品B', @barcodeIDB, @saleCommBNO_B, @bCommotidyPrice, @saleCommBNO_B, @bCommotidyPrice, 200, NULL);
--	SET @rtCommBID_B = last_insert_id();
--	-- 插入零售单商品来源
--	INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
--	VALUES (@rtCommBID_B, @commIDB, @NOSaleFromWarehousingB_B, @warehouseB);
--	SET @rtCommsourceIDB_B = last_insert_id();
--	-- 零售商品C
--	INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
--	VALUES (@rtID_B, @commIDC, '普通商品C', @barcodeIDC, @saleCommCNO_B, @cCommodityPrice, @saleCommCNO_B, @cCommodityPrice, 700, NULL);
--	SET @rtCommCID_B = last_insert_id();
--	-- 插入零售单商品来源
--	INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
--	VALUES (@rtCommCID_B, @commIDC, @NOSaleFromWarehousingC_B, @warehouseC);
--	SET @rtCommsourceIDC_B = last_insert_id();
--	-- 
--	-- 零售汇总
--	SET @rgg1WorkTimeStart_A='2041/02/02 00:00:00';
--	SET @rgg1WorkTimeEnd_A='2041/02/02 23:59:59';
--	SET @rgg1TotalAmountCash_A = @rt1AmuontCash_A;
--	SET @rgg1TotalAmountWeChat_A = @rt1AmuontWeChat_A;
--	SET @rgg1TotalAmountAliPay_A = @rt1AmuontAliPay_A;
--	SET @rgg1TotalAmount_A = @rgg1TotalAmountCash_A + @rgg1TotalAmountWeChat_A + @rgg1TotalAmountAliPay_A;
--	
--	SET @rgg2WorkTimeStart_B='2041/02/02 00:00:00';
--	SET @rgg2WorkTimeEnd_B='2041/02/03 12:00:00';
--	SET @rgg2TotalAmountCash_B = @rt2AmuontCash_B;
--	SET @rgg2TotalAmountWeChat_B = @rt2AmuontWeChat_B;
--	SET @rgg2TotalAmountAliPay_B = @rt2AmuontAliPay_B;
--	SET @rgg2TotalAmount_B = @rgg2TotalAmountCash_B + @rgg2TotalAmountWeChat_B + @rgg2TotalAmountAliPay_B;
--	
--	INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
--	VALUES (@staffIDA, 2, @rgg1WorkTimeStart_A, @rgg1WorkTimeEnd_A, 1, @rgg1TotalAmount_A, 2400.000000, @rgg1TotalAmountCash_A, @rgg1TotalAmountWeChat_A, @rgg1TotalAmountAliPay_A, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, now());
--	SET @rtgID_A = last_insert_id();
--	INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
--	VALUES (@staffIDB, 2, @rgg2WorkTimeStart_B, @rgg2WorkTimeEnd_B, 1, @rgg2TotalAmount_B, 2400.000000, @rgg2TotalAmountCash_B, @rgg2TotalAmountWeChat_B, @rgg2TotalAmountAliPay_B, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, now());
--	SET @rtgID_B = last_insert_id();
--	-- 
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @dSaleDatetime = '2041-02-02 00:00:00';
--	SET @deleteOldData = 1; -- 仅测试使用 1，删除旧的数据。只能在测试代码中使用。0，不删除旧的数据。只能在功能代码中使用。
--	-- 
--	CALL SP_RetailTradeDailyReportByStaff_Create(@iErrorCode, @sErrorMsg, @dSaleDatetime, @deleteOldData);
--	SELECT @iErrorCode;
--	SELECT @sErrorMsg;
--	-- 
--	-- 结果验证
--	-- 验证员工A在02/02当天只有零售
--	-- 售卖总金额
--	SET @saleTotalAmount_A = @rt1TotalAmount_A;
--	-- 售卖商品总成本
--	SET @saleTotalCost_A = 	(@NOSaleFromWarehousingA1_A * @warehouseA1Price + @NOSaleFromWarehousingA2_A * @warehouseA2Price) + 
--							(@NOSaleFromWarehousingB_A * @warehouseBPrice) + 
--							(@NOSaleFromWarehousingC_A * @warehouseCPrice);
--	-- 售卖商品总毛利
--	SET @saleTotalMargin_A = @saleTotalAmount_A - @saleTotalCost_A;
--	-- 退货总金额
--	SET @returnTotalAmount_A = 0.000000;
--	-- 退货商品总成本
--	SET @returnTotalCost_A = 0.000000;
--	-- 退货商品总毛利
--	SET @returnTotalMargin_A = @returnTotalAmount_A - @returnTotalCost_A;
--	-- 总金额（售卖总金额 - 退货总金额）
--	SET @totalAmount_A = @saleTotalAmount_A - @returnTotalAmount_A;
--	-- 总毛利（售卖商品总毛利 - 退货商品总毛利）
--	SET @grossMargin_A = @saleTotalMargin_A - @returnTotalMargin_A;
--	
--	SET @ResultVerification_A=0;
--	-- 进行员工A业绩报表的数据结果验证
--	SELECT 1 INTO @ResultVerification_A
--	FROM t_retailtradedailyreportbystaff 
--	WHERE F_StaffID = @staffIDA 
--	AND F_Datetime = @dSaleDatetime
--	AND F_TotalAmount = @totalAmount_A
--	AND F_GrossMargin = @grossMargin_A
--	AND F_NO = (
--		SELECT COUNT(1) 
--		FROM t_retailtrade 
--		WHERE F_SourceID = -1 
--		AND F_StaffID = @staffIDA
--		AND datediff(F_SaleDatetime, @dSaleDatetime) = 0
--	 );
--	
--	SELECT IF(@iErrorCode = 0 AND @ResultVerification_A = 1,'测试成功','测试失败') AS 'Test Case79 Result';
--	-- 
--	-- TODO 员工B连续上两天班，生成两天的报表的数据还未实现，以后实现了再补充结果验证。
--	-- 
--	DELETE FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime;
--	DELETE FROM t_retailtradeaggregation WHERE F_ID = @rtgID_A;
--	DELETE FROM t_retailtradeaggregation WHERE F_ID = @rtgID_B;
--	DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommsourceIDA1_A;
--	DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommsourceIDA2_A;
--	DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommsourceIDA1_B;
--	DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommsourceIDA2_B;
--	DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommsourceIDB_A;
--	DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommsourceIDB_B;
--	DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommsourceIDC_A;
--	DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommsourceIDC_B;
--	DELETE FROM t_retailtradecommodity WHERE F_ID = @rtCommAID_A;
--	DELETE FROM t_retailtradecommodity WHERE F_ID = @rtCommAID_B;
--	DELETE FROM t_retailtradecommodity WHERE F_ID = @rtCommBID_A;
--	DELETE FROM t_retailtradecommodity WHERE F_ID = @rtCommBID_B;
--	DELETE FROM t_retailtradecommodity WHERE F_ID = @rtCommCID_A;
--	DELETE FROM t_retailtradecommodity WHERE F_ID = @rtCommCID_B;
--	DELETE FROM t_retailtrade WHERE F_ID = @rtID_A;
--	DELETE FROM t_retailtrade WHERE F_ID = @rtID_B;
--	-- 商品A相关
--	DELETE FROM t_warehousingcommodity WHERE F_ID = @warehouseCommA1;
--	DELETE FROM t_warehousingcommodity WHERE F_ID = @warehouseCommA2;
--	DELETE FROM t_warehousing WHERE F_ID = @warehouseA1;
--	DELETE FROM t_warehousing WHERE F_ID = @warehouseA2;
--	DELETE FROM t_providercommodity WHERE F_ID = @providerCommIDA;
--	DELETE FROM t_barcodes WHERE F_ID = @barcodeIDA;
--	DELETE FROM t_commodity WHERE F_ID = @commIDA;
--	-- 商品B相关
--	DELETE FROM t_warehousingcommodity WHERE F_ID = @warehouseCommB;
--	DELETE FROM t_warehousing WHERE F_ID = @warehouseB;
--	DELETE FROM t_providercommodity WHERE F_ID = @providerCommIDB;
--	DELETE FROM t_barcodes WHERE F_ID = @barcodeIDB;
--	DELETE FROM t_commodity WHERE F_ID = @commIDB;
--	-- 商品C相关
--	DELETE FROM t_warehousingcommodity WHERE F_ID = @warehouseCommC;
--	DELETE FROM t_warehousing WHERE F_ID = @warehouseC;
--	DELETE FROM t_providercommodity WHERE F_ID = @providerCommIDC;
--	DELETE FROM t_barcodes WHERE F_ID = @barcodeIDC;
--	DELETE FROM t_commodity WHERE F_ID = @commIDC;