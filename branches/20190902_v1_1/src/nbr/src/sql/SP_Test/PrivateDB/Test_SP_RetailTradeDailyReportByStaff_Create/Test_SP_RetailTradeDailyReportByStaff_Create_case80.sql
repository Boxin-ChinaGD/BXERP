SELECT '++++++++++++++++++Test_SP_RetailTradeDailyReportByStaff_Create_case80.sql+++++++++++++++++++++++';

-- Ա�������ϰ࣬������2���������ܡ���һ����������ܵĽ���ʱ����23:59:30���ڶ�����������ܵ���ʼʱ����00:00:30����23:59:30~00:00:30ʱ����ڣ��̼ҵ�Ա�����ϰ岻�ܵ�¼App������Ѿ�
-- ��App����App�ᵯ��һ���Ի���һֱ�ȵ�00:00:30�������Ե���������Ҫ������2��ʱ��Ҫ��
-- ������Ϊ�������ܿ��Կ��죬������֧�ֿ��죬���Ա�������������Ҫʵ���ˣ���Ϊ�������������Ѿ�ʵ�֡�

--	SELECT '----- case80:day1Ա��A�ϰֻ࣬�������ۣ�Ա��B�ϰֻ࣬�����˻�����A�۳������۵�����day2�賿0:00��8:00Ա��B��δ�°࣬������Ա��A��day1�۳������۵����鿴����ͳ�Ƽ�������ʾ�Ƿ�������[AB��B]-------' AS 'Case80';
--	-- Ա��A��day1�������۵�A1��������ƷA����Ӧ������ⵥ��������ƷB����Ӧһ����ⵥ��������ƷC����Ӧһ����ⵥ
--	-- Ա��A��day1�������۵�A2��������ƷA����Ӧ������ⵥ��������ƷB����Ӧһ����ⵥ��������ƷC����Ӧһ����ⵥ
--	-- Ա��B��day1�����˻����������۵�A1������ƷA��B��C���в����˻�
--	-- Ա��B��day2�����˻����������۵�A2������ƷA��B��C���в����˻�
--	-- 
--	-- Ա��ID
--	SET @staffIDA = 9;
--	SET @staffIDB = 10;
--	
--	-- ����һ����ƷA
--	INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, F_LatestPricePurchase, F_PriceRetail, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_NO, F_Type, F_NOStart, F_PurchasingPriceStart, F_CreateDatetime, F_UpdateDatetime)
--	VALUES (0, '�����ƷA', '��ͨ��ƷA', '��', 3, '��', 1, 1, 'CJS666', 1, -1, 20, 19, 19, 0, 1, 365, 3, '2119-01-01 09:00:00', 1, 0, 0, 0, 20, 0, -1, -1, '2119-01-01 09:00:00', '2019-05-19 09:00:00');
--	SET @commIDA = last_insert_id();
--	-- ����A��һ��������
--	INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
--	VALUES (@commIDA, 'cjs66666', '2119-01-01 09:00:00', '2119-01-01 09:00:00');
--	SET @barcodeIDA = last_insert_id();
--	-- ��Ӧ������Ʒ���й���
--	INSERT INTO t_providercommodity (F_CommodityID, F_ProviderID)
--	VALUES (@commIDA, 1);
--	SET @providerCommIDA = last_insert_id();
--	-- ��Ҫһ����Ʒ�������Ų�ͬ����ⵥ���۸�ͬ������������Ȼ������˻�
--	INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_UpdateDatetime)
--	VALUES (2,'rk211901010001', 1, 1, 2, 4, '2119-01-01 06:00:00', '2119-01-01 06:00:00');
--	SET @warehouseA1 = last_insert_id();
--	INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_UpdateDatetime)
--	VALUES (2,'rk211901010001', 1, 1, 2, 4, '2119-01-01 06:00:00', '2119-01-01 06:00:00');
--	SET @warehouseA2 = last_insert_id();
--	-- ������ⵥ��Ʒ��
--	SET @warehouseA1NO = 400; -- ��ƷA��һ�����400
--	SET @warehouseA2NO = 600; -- ��ƷA�ڶ������600
--	SET @warehouseA1Price = 10; -- ��ƷA��һ������10Ԫ
--	SET @warehouseA2Price = 15; -- ��ƷA�ڶ�������15Ԫ
--	INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
--	VALUES (@warehouseA1, @commIDA, @warehouseA1NO, 3, '�����ƷA', @barcodeIDA, @warehouseA1Price, 5000, '2119-01-01 06:00:00', 36, '2119-01-09 06:00:00', '2119-01-01 06:00:00', '2119-01-01 06:00:00', 5);
--	SET @warehouseCommA1 = last_insert_id();
--	-- 
--	INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
--	VALUES (@warehouseA2, @commIDA, @warehouseA2NO, 3, '�����ƷA', @barcodeIDA, @warehouseA2Price, 7500, '2119-01-01 06:00:00', 36, '2119-01-09 06:00:00', '2119-01-01 06:00:00', '2119-01-01 06:00:00', 5);
--	SET @warehouseCommA2 = last_insert_id();
--	-- ����һ����ƷB
--	INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, F_LatestPricePurchase, F_PriceRetail, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_NO, F_Type, F_NOStart, F_PurchasingPriceStart, F_CreateDatetime, F_UpdateDatetime)
--	VALUES (0, '�����ƷB', '��ͨ��ƷB', '��', 3, '��', 1, 1, 'CJS666', 1, -1, 20, 19, 19, 0, 1, 365, 3, '2119-01-01 09:00:00', 1, 0, 0, 0, 20, 0, -1, -1, '2119-01-01 09:00:00', '2019-05-19 09:00:00');
--	SET @commIDB = last_insert_id();
--	-- ����B��һ��������
--	INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
--	VALUES (@commIDB, 'cjs77777', '2119-01-01 09:00:00', '2119-01-01 09:00:00');
--	SET @barcodeIDB = last_insert_id();
--	-- ��Ӧ������Ʒ���й���
--	INSERT INTO t_providercommodity (F_CommodityID, F_ProviderID)
--	VALUES (@commIDB, 1);
--	SET @providerCommIDB = last_insert_id();
--	-- ��ƷB��Ӧһ����ⵥ
--	INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_UpdateDatetime)
--	VALUES (2,'rk211901010001', 1, 1, 2, 4, '2119-01-01 06:00:00', '2119-01-01 06:00:00');
--	SET @warehouseB = last_insert_id();
--	-- ������ⵥ��Ʒ��
--	SET @warehouseBNO = 500; -- ��ƷB���500
--	SET @warehouseBPrice = 10; -- ��ƷB����10
--	INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
--	VALUES (@warehouseB, @commIDB, @warehouseBNO, 3, '�����ƷB', @barcodeIDB, @warehouseBPrice, 5000, '2119-01-01 06:00:00', 36, '2119-01-09 06:00:00', '2119-01-01 06:00:00', '2119-01-01 06:00:00', 5);
--	SET @warehouseCommB = last_insert_id();
--	-- ����һ����ƷC
--	INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, F_LatestPricePurchase, F_PriceRetail, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_NO, F_Type, F_NOStart, F_PurchasingPriceStart, F_CreateDatetime, F_UpdateDatetime)
--	VALUES (0, '�����ƷC', '��ͨ��ƷC', '��', 3, '��', 1, 1, 'CJS666', 1, -1, 20, 19, 19, 0, 1, 365, 3, '2119-01-01 09:00:00', 1, 0, 0, 0, 20, 0, -1, -1, '2119-01-01 09:00:00', '2019-05-19 09:00:00');
--	SET @commIDC = last_insert_id();
--	-- ����C��һ��������
--	INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
--	VALUES (@commIDC, 'cjs88888', '2119-01-01 09:00:00', '2119-01-01 09:00:00');
--	SET @barcodeIDC = last_insert_id();
--	-- ��Ӧ������Ʒ���й���
--	INSERT INTO t_providercommodity (F_CommodityID, F_ProviderID)
--	VALUES (@commIDC, 1);
--	SET @providerCommIDC = last_insert_id();
--	-- ��ƷB��Ӧһ����ⵥ
--	INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_UpdateDatetime)
--	VALUES (2,'rk211901010001', 1, 1, 2, 4, '2119-01-01 06:00:00', '2119-01-01 06:00:00');
--	SET @warehouseC = last_insert_id();
--	-- ������ⵥ��Ʒ��
--	SET @warehouseCNO = 500; -- ��ƷC���500
--	SET @warehouseCPrice = 10; -- ��ƷC����10
--	INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
--	VALUES (@warehouseC, @commIDC, @warehouseCNO, 3, '�����ƷC', @barcodeIDC, @warehouseCPrice, 5000, '2119-01-01 06:00:00', 36, '2119-01-09 06:00:00', '2119-01-01 06:00:00', '2119-01-01 06:00:00', 5);
--	SET @warehouseCommC = last_insert_id();
--	
--	-- Ա��A������һ�����۵�
--	SET @saleCommANO1_A = 500; -- ��500��
--	SET @aCommodityPrice = 300.000000; -- ��ƷA�۸�
--	SET @saleCommBNO1_A = 100;	-- ��100��
--	SET @bCommodityPrice = 200.000000; -- ��ƷB�۸�
--	SET @saleCommCNO1_A = 100;	-- ��100��
--	SET @cCommodityPrice = 700.000000; -- ��ƷC�۸�
--	SET @rt1AmuontCash_A = @saleCommANO1_A * @aCommodityPrice + @saleCommBNO1_A * @bCommodityPrice + @saleCommCNO1_A * @cCommodityPrice;
--	SET @rt1AmuontWeChat_A = 0.000000;
--	SET @rt1AmuontAliPay_A = 0.000000;
--	SET @rt1TotalAmount_A = @rt1AmuontCash_A + @rt1AmuontWeChat_A + @rt1AmuontAliPay_A;
--	SET @saleDatetime1_A = '2041/02/02 06:00:00';
--	SET @syncDatetime1_A = '2041/02/02 05:00:00';
--	INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
--	VALUES (1,'LS2041020200000500010001',11068,1,'url=ashasoadigmnalskd',@saleDatetime1_A,@staffIDA,1,'0',1,'��',-1,@syncDatetime1_A, @rt1TotalAmount_A, @rt1AmuontCash_A, @rt1AmuontWeChat_A,@rt1AmuontAliPay_A,0.000000,0.000000,0.000000,0.000000,0.000000,1,NULL,NULL,NULL,NULL,NULL,NULL);
--	SET @rt1ID_A = last_insert_id();
--	-- ���۵���ƷA
--	INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
--	VALUES (@rt1ID_A, @commIDA, '��ͨ��ƷA', @barcodeIDA, @saleCommANO1_A, @aCommodityPrice, @saleCommANO1_A, @aCommodityPrice, 300, NULL);
--	SET @rt1CommAID_A = last_insert_id();
--	-- ���۵���ƷB
--	INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
--	VALUES (@rt1ID_A, @commIDB, '��ͨ��ƷB', @barcodeIDB, @saleCommBNO1_A, @bCommodityPrice, @saleCommBNO1_A, @bCommodityPrice, 200, NULL);
--	SET @rt1CommBID_A = last_insert_id();
--	-- ���۵���ƷC
--	INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
--	VALUES (@rt1ID_A, @commIDC, '��ͨ��ƷC', @barcodeIDC, @saleCommCNO1_A, @cCommodityPrice, @saleCommCNO1_A, @cCommodityPrice, 700, NULL);
--	SET @rt1CommCID_A = last_insert_id();
--	-- ���۵���Ʒ��Դ
--	SET @NO1SaleFromWarehousingA1_A = 200; -- ����ⵥA1����200
--	SET @NO1SaleFromWarehousingA2_A = 300; -- ����ⵥA2����300
--	SET @NO1SaleFromWarehousingB_A = 100;
--	SET @NO1SaleFromWarehousingC_A = 100;
--	-- ���۵���ƷA��Դ
--	INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
--	VALUES (@rt1CommAID_A, @commIDA, @NO1SaleFromWarehousingA1_A, @warehouseA1);
--	SET @rt1CommsourceIDA1_A = last_insert_id();
--	INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
--	VALUES (@rt1CommAID_A, @commIDA, @NO1SaleFromWarehousingA2_A, @warehouseA2);
--	SET @rt1CommsourceIDA2_A = last_insert_id();
--	-- ���۵���ƷB��Դ
--	INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
--	VALUES (@rt1CommAID_A, @commIDB, @NO1SaleFromWarehousingB_A, @warehouseB);
--	SET @rt1CommsourceIDB_A = last_insert_id();
--	-- ���۵���ƷC��Դ
--	INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
--	VALUES (@rt1CommCID_A, @commIDC, @NO1SaleFromWarehousingC_A, @warehouseC);
--	SET @rt1CommsourceIDC_A = last_insert_id();
--	-- 
--	-- Ա��A�����ڶ������۵�
--	SET @saleCommANO2_A = 500; -- ��500��
--	SET @saleCommBNO2_A = 100;	-- ��100��
--	SET @saleCommCNO2_A = 100;	-- ��100��
--	SET @rt2AmuontCash_A = @saleCommANO2_A * @aCommodityPrice + @saleCommBNO2_A * @bCommodityPrice + @saleCommCNO2_A * @cCommodityPrice;
--	SET @rt2AmuontWeChat_A = 0.000000;
--	SET @rt2AmuontAliPay_A = 0.000000;
--	SET @rt2TotalAmount_A = @rt2AmuontCash_A + @rt2AmuontWeChat_A + @rt2AmuontAliPay_A;
--	SET @saleDatetime2_A = '2041/02/02 07:00:00';
--	SET @syncDatetime2_A = '2041/02/02 05:00:00';
--	INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
--	VALUES (1,'LS2041020200000500010002',11069,1,'url=ashasoadigmnalske',@saleDatetime2_A,@staffIDA,1,'0',1,'��',-1,@syncDatetime2_A, @rt2TotalAmount_A, @rt2AmuontCash_A, @rt2AmuontAliPay_A,@rt2AmuontWeChat_A,0.000000,0.000000,0.000000,0.000000,0.000000,1,NULL,NULL,NULL,NULL,NULL,NULL);
--	SET @rt2ID_A = last_insert_id();
--	-- ������ƷA,Ա��A�����۵�2
--	INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
--	VALUES (@rt2ID_A, @commIDA, '��ͨ��ƷA', @barcodeIDA, @saleCommANO2_A, @aCommodityPrice, @saleCommANO2_A, @aCommodityPrice, 300, NULL);
--	SET @rt2CommAID_A = last_insert_id();
--	-- ������ƷB
--	INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
--	VALUES (@rt2ID_A, @commIDB, '��ͨ��ƷB', @barcodeIDB, @saleCommBNO2_A, @bCommodityPrice, @saleCommBNO2_A, @bCommodityPrice, 200, NULL);
--	SET @rt2CommBID_A = last_insert_id();
--	-- ������ƷC
--	INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
--	VALUES (@rt2ID_A, @commIDC, '��ͨ��ƷC', @barcodeIDC, @saleCommCNO2_A, @cCommodityPrice, @saleCommCNO2_A, @cCommodityPrice, 700, NULL);
--	SET @rt2CommCID_A = last_insert_id();
--	-- ���۵���Ʒ��Դ
--	SET @NO2SaleFromWarehousingA1_A = 200; -- ����ⵥA1����200
--	SET @NO2SaleFromWarehousingA2_A = 300;	-- ����ⵥA2����300
--	SET @NO2SaleFromWarehousingB_A = 100;
--	SET @NO2SaleFromWarehousingC_A = 100;
--	-- ���۵���ƷA��Դ
--	INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
--	VALUES (@rt2CommAID_A, @commIDA, @NO2SaleFromWarehousingA1_A, @warehouseA1);
--	SET @rt2CommsourceIDA1_A = last_insert_id();
--	INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
--	VALUES (@rt2CommAID_A, @commIDA, @NO2SaleFromWarehousingA2_A, @warehouseA2);
--	SET @rt2CommsourceIDA2_A = last_insert_id();
--	-- ���۵���ƷB��Դ
--	INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
--	VALUES (@rt2CommBID_A, @commIDB, @NO2SaleFromWarehousingB_A, @warehouseB);
--	SET @rt2CommsourceIDB_A = last_insert_id();
--	-- ���۵���ƷC��Դ
--	INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
--	VALUES (@rt2CommCID_A, @commIDC, @NO2SaleFromWarehousingC_A, @warehouseC);
--	SET @rt2CommsourceIDC_A = last_insert_id();
--	
--	-- Ա��B�½���һ���˻���
--	SET @returnCommANO1_B = 250; -- ��250
--	SET @returnCommBNO1_B = 50; -- ��50
--	SET @returnCommCNO1_B = 50; -- ��50
--	SET @rrt1AmuontCash_B = @returnCommANO1_B * @aCommodityPrice + @returnCommBNO1_B * @bCommodityPrice + @returnCommCNO1_B * @cCommodityPrice;
--	SET @rrt1AmuontWeChat_B = 0.000000;
--	SET @rrt1AmuontAliPay_B = 0.000000;
--	SET @rrt1TotalAmount_B = @rrt1AmuontCash_B + @rrt1AmuontWeChat_B + @rrt1AmuontAliPay_B;
--	SET @saleDatetime1_B = '2041/02/02 07:00:01';
--	SET @syncDatetime1_B = '2041/02/02 07:00:01';
--	
--	INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
--	VALUES (1,'LS2041020200000500010001_1',11069,1,'url=ashasoadigmnalskd',@saleDatetime1_B,@staffIDB,1,'0',1,'��',@rt1ID_A,@syncDatetime1_B,@rrt1TotalAmount_B,@rrt1AmuontCash_B,@rrt1AmuontAliPay_B,@rrt1AmuontWeChat_B,0.000000,0.000000,0.000000,0.000000,0.000000,1,NULL,NULL,NULL,NULL,NULL,NULL);
--	SET @rrt1ID_B = last_insert_id();
--	-- ������ƷA
--	INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
--	VALUES (@rrt1ID_B, @commIDA, '��ͨ��ƷA', 1, @returnCommANO1_B, @aCommodityPrice, @returnCommANO1_B, @aCommodityPrice, 300, NULL);
--	SET @return1CommAID_B = last_insert_id();
--	-- ������ƷB
--	INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
--	VALUES (@rrt1ID_B, @commIDB, '�ɿڿ���', 3, @returnCommBNO1_B, @bCommodityPrice, @returnCommBNO1_B, @bCommodityPrice, 200, NULL);
--	SET @return1CommBID_B = last_insert_id();
--	-- ������ƷC
--	INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
--	VALUES (@rrt1ID_B, @commIDC, '��ͨ��ƷC', @barcodeIDC, @returnCommCNO1_B, @cCommodityPrice, @returnCommCNO1_B, @cCommodityPrice, 700, NULL);
--	SET @return1CommCID_B = last_insert_id();
--	-- �˻�ȥ��
--	SET @NO1ReturnToWarehousingA1_B = @NO1SaleFromWarehousingA1_A / 2;
--	SET @NO1ReturnToWarehousingA2_B = @NO1SaleFromWarehousingA2_A / 2;
--	SET @NO1ReturnToWarehousingB_B = @NO1SaleFromWarehousingB_A / 2;
--	SET @NO1ReturnToWarehousingC_B = @NO1SaleFromWarehousingC_A / 2;
--	-- ��ƷA
--	INSERT INTO t_returnretailtradecommoditydestination(F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
--	VALUES (@return1CommAID_B, @commIDA, @NO1ReturnToWarehousingA2_B, @warehouseA2);
--	SET @returnCommA1Destn1_B = last_insert_id();
--	INSERT INTO t_returnretailtradecommoditydestination(F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
--	VALUES (@return1CommAID_B, @commIDA, @NO1ReturnToWarehousingA1_B, @warehouseA1);
--	SET @returnCommA2Destn1_B = last_insert_id();
--	-- ��ƷB
--	INSERT INTO t_returnretailtradecommoditydestination(F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
--	VALUES (@return1CommBID_B, @commIDB, @NO1ReturnToWarehousingB_B, @warehouseB);
--	SET @returnCommBDestn1_B = last_insert_id();
--	-- ��ƷC
--	INSERT INTO t_returnretailtradecommoditydestination(F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
--	VALUES (@return1CommCID_B, @commIDC, @NO1ReturnToWarehousingC_B, @warehouseC);
--	SET @returnCommCDestn1_B = last_insert_id();
--	
--	-- Ա��B�½���һ���˻���
--	SET @returnCommANO2_B = 250; -- ��250
--	SET @returnCommBNO2_B = 50; -- ��50
--	SET @returnCommCNO2_B = 50; -- ��50
--	SET @rrt2AmuontCash_B = @returnCommANO2_B * @aCommodityPrice + @returnCommBNO2_B * @bCommodityPrice + @returnCommCNO2_B * @cCommodityPrice;
--	SET @rrt2AmuontWeChat_B = 0.000000;
--	SET @rrt2AmuontAliPay_B = 0.000000;
--	SET @rrt2TotalAmount_B = @rrt2AmuontCash_B + @rrt2AmuontWeChat_B + @rrt2AmuontAliPay_B;
--	SET @saleDatetime2_B = '2041/02/03 02:00:01';
--	SET @syncDatetime2_B = '2041/02/03 02:00:01';
--	
--	INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
--	VALUES (1,'LS2041020200000500010002_1',11070,1,'url=ashasoadigmnalskd',@saleDatetime2_B,@staffIDB,1,'0',1,'��',@rt2ID_A,@syncDatetime2_B,@rrt2TotalAmount_B,@rrt2AmuontCash_B,@rrt2AmuontAliPay_B,@rrt2AmuontWeChat_B,0.000000,0.000000,0.000000,0.000000,0.000000,1,NULL,NULL,NULL,NULL,NULL,NULL);
--	SET @rrt2ID_B = last_insert_id();
--	-- ������ƷA
--	INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
--	VALUES (@rrt2ID_B, @commIDA, '��ͨ��ƷA', 1, @returnCommANO2_B, @aCommodityPrice, @returnCommANO2_B, @aCommodityPrice, 300, NULL);
--	SET @return2CommAID_B = last_insert_id();
--	-- ������ƷB
--	INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
--	VALUES (@rrt2ID_B, @commIDB, '�ɿڿ���', 3, @returnCommBNO2_B, @bCommodityPrice, @returnCommBNO2_B, @bCommodityPrice, 200, NULL);
--	SET @return2CommBID_B = last_insert_id();
--	-- ������ƷC
--	INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
--	VALUES (@rrt2ID_B, @commIDC, '��ͨ��ƷC', @barcodeIDC, @returnCommCNO2_B, @cCommodityPrice, @returnCommCNO2_B, @cCommodityPrice, 700, NULL);
--	SET @return2CommCID_B = last_insert_id();
--	-- �˻����۵�A2���˻�ȥ���
--	SET @NO2ReturnToWarehousingA1 = @NO2SaleFromWarehousingA1_A / 2;
--	SET @NO2ReturnToWarehousingA2 = @NO2SaleFromWarehousingA2_A / 2;
--	SET @NO2ReturnToWarehousingB = @NO2SaleFromWarehousingB_A / 2;
--	SET @NO2ReturnToWarehousingC = @NO2SaleFromWarehousingC_A / 2;
--	-- ��ƷA
--	INSERT INTO t_returnretailtradecommoditydestination(F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
--	VALUES (@return2CommAID_B, @commIDA, @NO2ReturnToWarehousingA2, @warehouseA2);
--	SET @returnCommA1Destn2_B = last_insert_id();
--	INSERT INTO t_returnretailtradecommoditydestination(F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
--	VALUES (@return2CommAID_B, @commIDA, @NO2ReturnToWarehousingA1, @warehouseA1);
--	SET @returnCommA2Destn2_B = last_insert_id();
--	-- ��ƷB
--	INSERT INTO t_returnretailtradecommoditydestination(F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
--	VALUES (@return2CommBID_B, @commIDB, @NO2ReturnToWarehousingB, @warehouseB);
--	SET @returnCommBDestn2_B = last_insert_id();
--	-- ��ƷC
--	INSERT INTO t_returnretailtradecommoditydestination(F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
--	VALUES (@return2CommCID_B, @commIDC, @NO2ReturnToWarehousingC, @warehouseC);
--	SET @returnCommCDestn2_B = last_insert_id();
--	-- 
--	-- Ա��AB�����ۻ���
--	SET @rgg1WorkTimeStart_A='2041/02/02 00:00:00';
--	SET @rgg1WorkTimeEnd_A='2041/02/02 23:59:59';
--	SET @rgg1TotalAmountCash_A = @rt1AmuontCash_A + @rt2AmuontCash_A;
--	SET @rgg1TotalAmountWeChat_A = @rt1AmuontWeChat_A + @rt2AmuontWeChat_A;
--	SET @rgg1TotalAmountAliPay_A = @rt1AmuontAliPay_A + @rt2AmuontAliPay_A;
--	SET @rgg1TotalAmount_A = @rgg1TotalAmountCash_A + @rgg1TotalAmountWeChat_A + @rgg1TotalAmountAliPay_A;
--	
--	SET @rgg2WorkTimeStart_B='2041/02/02 00:00:00';
--	SET @rgg2WorkTimeEnd_B='2041/02/03 08:00:00';
--	SET @rgg2TotalAmountCash_B = @rrt1AmuontCash_B + @rrt2AmuontCash_B;
--	SET @rgg2TotalAmountWeChat_B = @rrt1AmuontWeChat_B + @rrt2AmuontWeChat_B;
--	SET @rgg2TotalAmountAliPay_B = @rrt1AmuontAliPay_B + @rrt2AmuontAliPay_B;
--	SET @rgg2TotalAmount_B = @rgg2TotalAmountCash_B + @rgg2TotalAmountWeChat_B + @rgg2TotalAmountAliPay_B;
--	
--	INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
--	VALUES (@staffIDA, 2, @rgg1WorkTimeStart_A, @rgg1WorkTimeEnd_A, 1, @rgg1TotalAmount_A, 200.000000, @rgg1TotalAmountCash_A, @rgg1TotalAmountWeChat_A, @rgg1TotalAmountAliPay_A, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, now());
--	SET @rtgID_A = last_insert_id();
--	INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
--	VALUES (@staffIDB, 2, @rgg2WorkTimeStart_B, @rgg2WorkTimeEnd_B, 1, @rgg2TotalAmount_B, 200.000000, @rgg2TotalAmountCash_B, @rgg2TotalAmountWeChat_B, @rgg2TotalAmountAliPay_B, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, now());
--	SET @rtgID_B = last_insert_id();
--	-- 
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @dSaleDatetime = '2041-02-02 00:00:00';
--	SET @deleteOldData = 1; -- ������ʹ�� 1��ɾ���ɵ����ݡ�ֻ���ڲ��Դ�����ʹ�á�0����ɾ���ɵ����ݡ�ֻ���ڹ��ܴ�����ʹ�á�
--	-- 
--	CALL SP_RetailTradeDailyReportByStaff_Create(@iErrorCode, @sErrorMsg, @dSaleDatetime, @deleteOldData);
--	SELECT @iErrorCode;
--	SELECT @sErrorMsg;
--	-- 
--	-- �����֤
--	-- ��֤Ա��A��02/02����������,���˻�
--	-- �����ܽ��
--	SET @saleTotalAmount_A = @rt1TotalAmount_A + @rt2TotalAmount_A;
--	-- ������Ʒ�ܳɱ�
--	SET @saleTotalCost_A = 	(@NO1SaleFromWarehousingA1_A * @warehouseA1Price + @NO1SaleFromWarehousingA2_A * @warehouseA2Price) + 
--							(@NO1SaleFromWarehousingB_A * @warehouseBPrice) + 
--							(@NO1SaleFromWarehousingC_A * @warehouseCPrice) +
--							(@NO2SaleFromWarehousingA1_A * @warehouseA1Price + @NO2SaleFromWarehousingA2_A * @warehouseA2Price) + 
--							(@NO2SaleFromWarehousingB_A * @warehouseBPrice) + 
--							(@NO2SaleFromWarehousingC_A * @warehouseCPrice);
--	-- ������Ʒ��ë��
--	SET @saleTotalMargin_A = @saleTotalAmount_A - @saleTotalCost_A;
--	-- �˻��ܽ��
--	SET @returnTotalAmount_A = @rrt1TotalAmount_B;
--	-- �˻���Ʒ�ܳɱ�
--	SET @returnTotalCost_A = 	(@NO1ReturnToWarehousingA1_B * @warehouseA1Price + @NO1ReturnToWarehousingA2_B * @warehouseA2Price) + 
--								(@NO1ReturnToWarehousingB_B * @warehouseBPrice) +
--								(@NO1ReturnToWarehousingC_B * @warehouseCPrice);
--	-- �˻���Ʒ��ë��
--	SET @returnTotalMargin_A = @returnTotalAmount_A - @returnTotalCost_A;
--	-- �ܽ������ܽ�� - �˻��ܽ�
--	SET @totalAmount_A = @saleTotalAmount_A - @returnTotalAmount_A;
--	-- ��ë����������Ʒ��ë�� - �˻���Ʒ��ë����
--	SET @grossMargin_A = @saleTotalMargin_A - @returnTotalMargin_A;
--	
--	SET @ResultVerification_A=0;
--	-- ����Ա��Aҵ�����������ݽ����֤
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
--	SELECT IF(@iErrorCode = 0 AND @ResultVerification_A = 1,'���Գɹ�','����ʧ��') AS 'Test Case80 Result';
--	-- 
--	-- TODO Ա��B����������࣬��������ı��������ݻ�δʵ�֣��Ժ�ʵ�����ٲ�������֤��
--	-- 
--	DELETE FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime;
--	DELETE FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime2;
--	DELETE FROM t_retailtradeaggregation WHERE F_ID = @rtgID_A;
--	DELETE FROM t_retailtradeaggregation WHERE F_ID = @rtgID_B;
--	DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rt1CommsourceIDA1_A;
--	DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rt1CommsourceIDA2_A;
--	DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rt2CommsourceIDA1_A;
--	DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rt2CommsourceIDA2_A;
--	DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rt1CommsourceIDB_A;
--	DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rt2CommsourceIDB_A;
--	DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rt1CommsourceIDC_A;
--	DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rt2CommsourceIDC_A;
--	-- 
--	DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnCommA1Destn1_B;
--	DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnCommA2Destn1_B;
--	DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnCommBDestn1_B;
--	DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnCommCDestn1_B;
--	DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnCommA1Destn2_B;
--	DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnCommA2Destn2_B;
--	DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnCommBDestn2_B;
--	DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnCommCDestn2_B;
--	DELETE FROM t_retailtradecommodity WHERE F_ID = @rt1CommAID_A;
--	DELETE FROM t_retailtradecommodity WHERE F_ID = @rt2CommAID_A;
--	DELETE FROM t_retailtradecommodity WHERE F_ID = @rt1CommBID_A;
--	DELETE FROM t_retailtradecommodity WHERE F_ID = @rt2CommBID_A;
--	DELETE FROM t_retailtradecommodity WHERE F_ID = @rt1CommCID_A;
--	DELETE FROM t_retailtradecommodity WHERE F_ID = @rt2CommCID_A;
--	DELETE FROM t_retailtradecommodity WHERE F_ID = @return1CommAID_B;
--	DELETE FROM t_retailtradecommodity WHERE F_ID = @return1CommBID_B;
--	DELETE FROM t_retailtradecommodity WHERE F_ID = @return1CommCID_B;
--	DELETE FROM t_retailtradecommodity WHERE F_ID = @return2CommAID_B;
--	DELETE FROM t_retailtradecommodity WHERE F_ID = @return2CommBID_B;
--	DELETE FROM t_retailtradecommodity WHERE F_ID = @return2CommCID_B;
--	DELETE FROM t_retailtrade WHERE F_ID = @rt1ID_A;
--	DELETE FROM t_retailtrade WHERE F_ID = @rt2ID_A;
--	DELETE FROM t_retailtrade WHERE F_ID = @rrt1ID_B;
--	DELETE FROM t_retailtrade WHERE F_ID = @rrt2ID_B;
--	-- ��ƷA���
--	DELETE FROM t_warehousingcommodity WHERE F_ID = @warehouseCommA1;
--	DELETE FROM t_warehousingcommodity WHERE F_ID = @warehouseCommA2;
--	DELETE FROM t_warehousing WHERE F_ID = @warehouseA1;
--	DELETE FROM t_warehousing WHERE F_ID = @warehouseA2;
--	DELETE FROM t_providercommodity WHERE F_ID = @providerCommIDA;
--	DELETE FROM t_barcodes WHERE F_ID = @barcodeIDA;
--	DELETE FROM t_commodity WHERE F_ID = @commIDA;
--	-- ��ƷB���
--	DELETE FROM t_warehousingcommodity WHERE F_ID = @warehouseCommB;
--	DELETE FROM t_warehousing WHERE F_ID = @warehouseB;
--	DELETE FROM t_providercommodity WHERE F_ID = @providerCommIDB;
--	DELETE FROM t_barcodes WHERE F_ID = @barcodeIDB;
--	DELETE FROM t_commodity WHERE F_ID = @commIDB;
--	-- ��ƷC���
--	DELETE FROM t_warehousingcommodity WHERE F_ID = @warehouseCommC;
--	DELETE FROM t_warehousing WHERE F_ID = @warehouseC;
--	DELETE FROM t_providercommodity WHERE F_ID = @providerCommIDC;
--	DELETE FROM t_barcodes WHERE F_ID = @barcodeIDC;
--	DELETE FROM t_commodity WHERE F_ID = @commIDC;