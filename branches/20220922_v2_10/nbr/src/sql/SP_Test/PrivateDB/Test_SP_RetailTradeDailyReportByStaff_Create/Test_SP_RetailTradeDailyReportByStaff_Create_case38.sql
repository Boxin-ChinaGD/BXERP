SELECT '----- case38:Ա��A����һ�����۵���A��Ʒ����Ӧ������ⵥ ,��B��Ʒ����Ӧ������ⵥ , ��C��Ʒ����Ӧ������ⵥ,Ա��B���촴��һ���˻���������ƷA��Bȫ���˻�,����ƷC�����˻�[A��B]-------' AS 'Case38';
-- 
-- Ա��ID
SET @staffIDA = 9;
SET @staffIDB = 10;
-- 
-- ����һ����ƷA
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_CreateDatetime, F_UpdateDatetime)
VALUES (0, '�����ƷA', '��ͨ��ƷA', '��', 3, '��', 1, 1, 'CJS666', 1, 
19, 19, 0, 1, 365, 3, '2119-01-01 09:00:00', 1, 0, 0, 0, 0, '2119-01-01 09:00:00', '2019-05-19 09:00:00');
SET @commIDA = last_insert_id();
-- ����A��һ��������
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commIDA, 'cjs66666', '2119-01-01 09:00:00', '2119-01-01 09:00:00');
SET @barcodeIDA = last_insert_id();
-- ��Ӧ������Ʒ���й���
INSERT INTO t_providercommodity (F_CommodityID, F_ProviderID)
VALUES (@commIDA, 1);
SET @providerCommIDA = last_insert_id();
-- ��Ҫһ����Ʒ�������Ų�ͬ����ⵥ���۸�ͬ������������Ȼ������˻�
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_UpdateDatetime)
VALUES (2,'rk211901010001', 1, 1, 2, 4, '2119-01-01 06:00:00', '2119-01-01 06:00:00');
SET @warehouseA1 = last_insert_id();
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_UpdateDatetime)
VALUES (2,'rk211901010001', 1, 1, 2, 4, '2119-01-01 06:00:00', '2119-01-01 06:00:00');
SET @warehouseA2 = last_insert_id();
-- ������ⵥ��Ʒ��
SET @warehouseA1NO = 200;
SET @warehouseA2NO = 300;
SET @warehouseA1Price = 10;
SET @warehouseA2Price = 15;
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehouseA1, @commIDA, @warehouseA1NO, 3, '�����ƷA', @barcodeIDA, @warehouseA1Price, 5000, '2119-01-01 06:00:00', 36, '2119-01-09 06:00:00', '2119-01-01 06:00:00', '2119-01-01 06:00:00', 5);
SET @warehouseCommA1 = last_insert_id();
-- 
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehouseA2, @commIDA, @warehouseA2NO, 3, '�����ƷA', @barcodeIDA, @warehouseA2Price, 7500, '2119-01-01 06:00:00', 36, '2119-01-09 06:00:00', '2119-01-01 06:00:00', '2119-01-01 06:00:00', 5);
SET @warehouseCommA2 = last_insert_id();

-- ����һ����ƷB
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_CreateDatetime, F_UpdateDatetime)
VALUES (0, '�����ƷB', '��ͨ��ƷB', '��', 3, '��', 1, 1, 'CJS666', 1, 
19, 19, 0, 1, 365, 3, '2119-01-01 09:00:00', 1, 0, 0, 0, 0, '2119-01-01 09:00:00', '2019-05-19 09:00:00');
SET @commIDB = last_insert_id();
-- ����B��һ��������
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commIDB, 'cjs77777', '2119-01-01 09:00:00', '2119-01-01 09:00:00');
SET @barcodeIDB = last_insert_id();
-- ��Ӧ������Ʒ���й���
INSERT INTO t_providercommodity (F_CommodityID, F_ProviderID)
VALUES (@commIDB, 1);
SET @providerCommIDB = last_insert_id();
-- ��Ҫһ����Ʒ�������Ų�ͬ����ⵥ���۸�ͬ������������Ȼ������˻�
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_UpdateDatetime)
VALUES (2,'rk211901010001', 1, 1, 2, 4, '2119-01-01 06:00:00', '2119-01-01 06:00:00');
SET @warehouseB1 = last_insert_id();
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_UpdateDatetime)
VALUES (2,'rk211901010001', 1, 1, 2, 4, '2119-01-01 06:00:00', '2119-01-01 06:00:00');
SET @warehouseB2 = last_insert_id();
-- ������ⵥ��Ʒ��
SET @warehouseB1NO = 50;
SET @warehouseB2NO = 50;
SET @warehouseB1Price = 10;
SET @warehouseB2Price = 15;
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehouseB1, @commIDB, @warehouseB1NO, 3, '�����ƷB', @barcodeIDB, @warehouseB1Price, 5000, '2119-01-01 06:00:00', 36, '2119-01-09 06:00:00', '2119-01-01 06:00:00', '2119-01-01 06:00:00', 5);
SET @warehouseCommB1 = last_insert_id();

INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehouseB2, @commIDB, @warehouseB2NO, 3, '�����ƷB', @barcodeIDB, @warehouseB2Price, 7500, '2119-01-01 06:00:00', 36, '2119-01-09 06:00:00', '2119-01-01 06:00:00', '2119-01-01 06:00:00', 5);
SET @warehouseCommB2 = last_insert_id();

-- ����һ����ƷC
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_CreateDatetime, F_UpdateDatetime)
VALUES (0, '�����ƷC', '��ͨ��ƷC', '��', 3, '��', 1, 1, 'CJS666', 1, 
19, 19, 0, 1, 365, 3, '2119-01-01 09:00:00', 1, 0, 0, 0, 0, '2119-01-01 09:00:00', '2019-05-19 09:00:00');
SET @commIDC = last_insert_id();
-- ����C��һ��������
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commIDC, 'cjs88888', '2119-01-01 09:00:00', '2119-01-01 09:00:00');
SET @barcodeIDC = last_insert_id();
-- ��Ӧ������Ʒ���й���
INSERT INTO t_providercommodity (F_CommodityID, F_ProviderID)
VALUES (@commIDC, 1);
SET @providerCommIDC = last_insert_id();
-- ��Ҫһ����Ʒ�������Ų�ͬ����ⵥ���۸�ͬ������������Ȼ������˻�
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_UpdateDatetime)
VALUES (2,'rk211901010001', 1, 1, 2, 4, '2119-01-01 06:00:00', '2119-01-01 06:00:00');
SET @warehouseC1 = last_insert_id();
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_UpdateDatetime)
VALUES (2,'rk211901010001', 1, 1, 2, 4, '2119-01-01 06:00:00', '2119-01-01 06:00:00');
SET @warehouseC2 = last_insert_id();
-- ������ⵥ��Ʒ��
SET @warehouseC1NO = 50;
SET @warehouseC2NO = 50;
SET @warehouseC1Price = 10;
SET @warehouseC2Price = 15;
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehouseC1, @commIDC, @warehouseC1NO, 3, '�����ƷC', @barcodeIDC, @warehouseC1Price, 5000, '2119-01-01 06:00:00', 36, '2119-01-09 06:00:00', '2119-01-01 06:00:00', '2119-01-01 06:00:00', 5);
SET @warehouseCommC1 = last_insert_id();
-- 
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehouseC2, @commIDC, @warehouseC2NO, 3, '�����ƷC', @barcodeIDC, @warehouseC2Price, 7500, '2119-01-01 06:00:00', 36, '2119-01-09 06:00:00', '2119-01-01 06:00:00', '2119-01-01 06:00:00', 5);
SET @warehouseCommC2 = last_insert_id();

-- �������۵�
SET @saleCommANO_A = @warehouseA1NO + @warehouseA2NO;
SET @aCommotidyPrice = 300.000000;
SET @saleCommBNO_A = @warehouseB1NO + @warehouseB2NO;
SET @bCommotidyPrice = 200.000000;
SET @saleCommCNO_A = @warehouseC1NO + @warehouseC2NO;
SET @cCommotidyPrice = 700.000000;
SET @rtAmuontCash_A = @saleCommANO_A * @aCommotidyPrice + @saleCommBNO_A * @bCommotidyPrice + @saleCommCNO_A * @cCommotidyPrice;
SET @rtAmuontWeChat_A = 0.000000;
SET @rtAmuontAliPay_A = 0.000000;
SET @rtTotalAmount_A = @saleCommANO_A * @aCommotidyPrice + @saleCommBNO_A * @bCommotidyPrice + @saleCommCNO_A * @cCommotidyPrice;
SET @saleDatetime_A = '2041/02/01 06:00:00';
SET @syncDatetime_A = '2041/02/01 06:00:00';

INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1,'LS2041020200000500010001',11068,1,'url=ashasoadigmnalskd',@saleDatetime_A,@staffIDA,1,'0',1,'��',-1,@syncDatetime_A, @rtTotalAmount_A, @rtAmuontCash_A, @rtAmuontAliPay_A,@rtAmuontWeChat_A,0.000000,0.000000,0.000000,0.000000,0.000000,1,NULL,NULL,NULL,NULL,NULL,NULL,2);
SET @rtID_A = last_insert_id();

-- ������ƷA
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtID_A, @commIDA, '��ͨ��ƷA', @barcodeIDA, @saleCommANO_A, @aCommotidyPrice, @saleCommANO_A, @aCommotidyPrice, 300, NULL);
SET @rtCommAID_A = last_insert_id();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommAID_A, @commIDA, @warehouseA1NO, @warehouseA1);
SET @rtCommsourceIDA1_A = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommAID_A, @commIDA, @warehouseA2NO, @warehouseA2);
SET @rtCommsourceIDA2_A = last_insert_id();
-- ������ƷB
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtID_A, @commIDB, '��ͨ��ƷB', @barcodeIDB, @saleCommBNO_A, @bCommotidyPrice, @saleCommBNO_A, @bCommotidyPrice, 200, NULL);
SET @rtCommBID_A = last_insert_id();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommAID_A, @commIDB, @warehouseB1NO, @warehouseB1);
SET @rtCommsourceIDB1_A = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommAID_A, @commIDB, @warehouseB2NO, @warehouseB2);
SET @rtCommsourceIDB2_A = last_insert_id();
-- ������ƷC
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtID_A, @commIDC, '��ͨ��ƷC', @barcodeIDC, @saleCommCNO_A, @cCommotidyPrice, @saleCommCNO_A, @cCommotidyPrice, 700, NULL);
SET @rtCommCID_A = last_insert_id();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommCID_A, @commIDC, @warehouseC1NO, @warehouseC1);
SET @rtCommsourceIDC1_A = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommCID_A, @commIDC, @warehouseC2NO, @warehouseC2);
SET @rtCommsourceIDC2_A = last_insert_id();

-- Ա��B�½��˻���
SET @returnCommANO_B = @saleCommANO_A; -- ȫ��
SET @returnCommBNO_B = @saleCommBNO_A; -- ȫ��
SET @returnCommCNO_B = 50;
SET @rrtAmuontCash_B = @returnCommANO_B * @aCommotidyPrice + @returnCommBNO_B * @bCommotidyPrice + @returnCommCNO_B * @cCommotidyPrice;
SET @rrtAmuontWeChat_B = 0.000000;
SET @rrtAmuontAliPay_B = 0.000000;
SET @rrtTotalAmount_B = @returnCommANO_B * @aCommotidyPrice + @returnCommBNO_B * @bCommotidyPrice + @returnCommCNO_B * @cCommotidyPrice;
SET @saleDatetime_B = '2041/02/02 07:00:01';
SET @syncDatetime_B = '2041/02/02 07:00:01';

INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1,'LS2041020200000500010001_1',11069,1,'url=ashasoadigmnalskd',@saleDatetime_B,@staffIDB,1,'0',1,'��',@rtID_A,@syncDatetime_B,@rrtTotalAmount_B,@rrtAmuontCash_B,@rrtAmuontAliPay_B,@rrtAmuontWeChat_B,0.000000,0.000000,0.000000,0.000000,0.000000,1,NULL,NULL,NULL,NULL,NULL,NULL,2);
SET @rrtID_B = last_insert_id();

-- ������ƷA
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rrtID_B, @commIDA, '��ͨ��ƷA', 1, @returnCommANO_B, @aCommotidyPrice, @returnCommANO_B, @aCommotidyPrice, 300, NULL);
SET @rrtCommAID_B = last_insert_id();
-- ������ƷB
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rrtID_B, @commIDB, '�ɿڿ���', 3, @returnCommBNO_B, @bCommotidyPrice, @returnCommBNO_B, @bCommotidyPrice, 200, NULL);
SET @rrtCommBID_B = last_insert_id();
-- ������ƷC
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rrtID_B, @commIDC, '��ͨ��ƷC', @barcodeIDC, @returnCommCNO_B, @cCommotidyPrice, @returnCommCNO_B, @cCommotidyPrice, 700, NULL);
SET @rrtCommCID_B = last_insert_id();
-- �����˻�ȥ���
SET @NOReturnToA1 = @warehouseA1NO;
SET @NOReturnToA2 = @warehouseA2NO;
SET @NOReturnToB1 = @warehouseB1NO;
SET @NOReturnToB2 = @warehouseB2NO;
SET @NOReturnToC1 = 25;
SET @NOReturnToC2 = 25;
-- ��ƷA
INSERT INTO t_returnretailtradecommoditydestination(F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@rrtCommAID_B, @commIDA, @NOReturnToA2, @warehouseA2);
SET @returnAggA1_B = last_insert_id();
INSERT INTO t_returnretailtradecommoditydestination(F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@rrtCommAID_B, @commIDA, @NOReturnToA1, @warehouseA1);
SET @returnAggA2_B = last_insert_id();
-- ��ƷB
INSERT INTO t_returnretailtradecommoditydestination(F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@rrtCommBID_B, @commIDB, @NOReturnToB2, @warehouseB2);
SET @returnAggB1_B = last_insert_id();
INSERT INTO t_returnretailtradecommoditydestination(F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@rrtCommBID_B, @commIDB, @NOReturnToB1, @warehouseB1);
SET @returnAggB2_B = last_insert_id();
-- ��ƷC
INSERT INTO t_returnretailtradecommoditydestination(F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@rrtCommCID_B, @commIDC, @NOReturnToC2, @warehouseC2);
SET @returnAggC1_B = last_insert_id();
INSERT INTO t_returnretailtradecommoditydestination(F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@rrtCommCID_B, @commIDC, @NOReturnToC1, @warehouseC1);
SET @returnAggC2_B = last_insert_id();
-- 
-- Ա��A�����۵���������
SET @rggWorkTimeStart_A='2041/02/01 00:00:00';
SET @rggWorkTimeEnd_A='2041/02/01 23:59:59';
SET @rggTotalAmount_A = @rtTotalAmount_A;
SET @rggTotalAmountCash_A = @rtAmuontCash_A;
SET @rggTotalAmountWeChat_A = @rtAmuontWeChat_A;
SET @rggTotalAmountAliPay_A = @rtAmuontAliPay_A;
SET @rggNO = 1;
-- 
INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
VALUES (@staffIDA, 2, @rggWorkTimeStart_A, @rggWorkTimeEnd_A, @rggNO, @rggTotalAmount_A, 0.000000, @rggTotalAmountCash_A, @rggTotalAmountWeChat_A, @rggTotalAmountAliPay_A, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, now());
SET @rtgID_A = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime = '2041-02-01 00:00:00';
SET @deleteOldData = 1; -- ������ʹ�� 1��ɾ���ɵ����ݡ�ֻ���ڲ��Դ�����ʹ�á�0����ɾ���ɵ����ݡ�ֻ���ڹ��ܴ�����ʹ�á�
SET @iShopID = 2;
-- 
CALL SP_RetailTradeDailyReportByStaff_Create(@iErrorCode, @sErrorMsg, @iShopID, @dSaleDatetime, @deleteOldData);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- �����֤
-- ��֤Ա��A��02/01����ֻ������
-- �����ܽ��
SET @saleTotalAmount_A = @rtTotalAmount_A;
-- ������Ʒ�ܳɱ�
SET @saleTotalCost_A = (@warehouseA1NO * @warehouseA1Price + @warehouseA2NO * @warehouseA2Price) + 
						(@warehouseB1NO * @warehouseB1Price + @warehouseB1NO * @warehouseB2Price) + 
						(@warehouseC1NO * @warehouseC1Price + @warehouseC1NO * @warehouseC2Price);
-- ������Ʒ��ë��
SET @saleTotalMargin_A = @saleTotalAmount_A - @saleTotalCost_A;
-- �˻��ܽ��
SET @returnTotalAmount_A = 0.000000;
-- �˻���Ʒ�ܳɱ�
SET @returnTotalCost_A = 0.000000;
-- �˻���Ʒ��ë��
SET @returnTotalMargin_A = @returnTotalAmount_A - @returnTotalCost_A;
-- �ܽ������ܽ�� - �˻��ܽ�
SET @totalAmount_A = @saleTotalAmount_A - @returnTotalAmount_A;
-- ��ë����������Ʒ��ë�� - �˻���Ʒ��ë����
SET @grossMargin_A = @saleTotalMargin_A - @returnTotalMargin_A;

SET @ResultVerification_A=0;
-- ����Ա��Aҵ����������ݽ����֤
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

SELECT IF(@iErrorCode = 0 AND @ResultVerification_A = 1,'���Գɹ�','����ʧ��') AS 'Test Case38 Result';

-- Ա��B�����۵���������
SET @rggWorkTimeStart_B='2041/02/02 00:00:00';
SET @rggWorkTimeEnd_B='2041/02/02 23:59:59';
SET @rggTotalAmount_B = @rrtTotalAmount_B;
SET @rggTotalAmountCash_B = @rrtAmuontCash_B;
SET @rggTotalAmountWeChat_B = @rrtAmuontWeChat_B;
SET @rggTotalAmountAliPay_B = @rrtAmuontAliPay_B;
-- 
INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
VALUES (@staffIDB, 2, @rggWorkTimeStart_B, @rggWorkTimeEnd_B, @rggNO, @rggTotalAmount_B, 0.000000, @rggTotalAmountCash_B, @rggTotalAmountWeChat_B, @rggTotalAmountAliPay_B, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, now());
SET @rtgID_B = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime = '2041-02-02 00:00:00';
SET @deleteOldData = 1; -- ������ʹ�� 1��ɾ���ɵ����ݡ�ֻ���ڲ��Դ�����ʹ�á�0����ɾ���ɵ����ݡ�ֻ���ڹ��ܴ�����ʹ�á�
SET @iShopID = 2;
-- 
CALL SP_RetailTradeDailyReportByStaff_Create(@iErrorCode, @sErrorMsg, @iShopID, @dSaleDatetime, @deleteOldData);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- �����֤
-- ��֤Ա��B��02/02����ֻ���˻�
-- �����ܽ��
SET @saleTotalAmount_B = 0.000000;
-- ������Ʒ�ܳɱ�
SET @saleTotalCost_B = 0.000000;
-- ������Ʒ��ë��
SET @saleTotalMargin_B = @saleTotalAmount_B - @saleTotalCost_B;
-- �˻��ܽ��
SET @returnTotalAmount_B = @rrtTotalAmount_B;
-- �˻���Ʒ�ܳɱ�
SET @returnTotalCost_B = (@NOReturnToA1 * @warehouseA1Price + @NOReturnToA2 * @warehouseA2Price) + 
						 (@NOReturnToB1 * @warehouseB1Price + @NOReturnToB2 * @warehouseB2Price) + 
						 (@NOReturnToC1 * @warehouseC1Price + @NOReturnToC2 * @warehouseC2Price);
-- �˻���Ʒ��ë��
SET @returnTotalMargin_B = @returnTotalAmount_B - @returnTotalCost_B;
-- �ܽ������ܽ�� - �˻��ܽ�
SET @totalAmount_B = @saleTotalAmount_B - @returnTotalAmount_B;
-- ��ë����������Ʒ��ë�� - �˻���Ʒ��ë����
SET @grossMargin_B = @saleTotalMargin_B - @returnTotalMargin_B;

SET @ResultVerification_B=0;
-- ����Ա��Bҵ����������ݽ����֤
SELECT 1 INTO @ResultVerification_B
FROM t_retailtradedailyreportbystaff 
WHERE F_StaffID = @staffIDB 
AND F_Datetime = @dSaleDatetime
AND F_TotalAmount = @totalAmount_B
AND F_GrossMargin = @grossMargin_B
AND F_NO = (
	SELECT COUNT(1) 
	FROM t_retailtrade 
	WHERE F_SourceID = -1 
	AND F_StaffID = @staffIDB
	AND datediff(F_SaleDatetime, @dSaleDatetime) = 0
 );

SELECT IF(@iErrorCode = 0 AND @ResultVerification_B = 1,'���Գɹ�','����ʧ��') AS 'Test Case38 Result';
-- 
DELETE FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime;
DELETE FROM t_retailtradeaggregation WHERE F_ID = @rtgID_A;
DELETE FROM t_retailtradeaggregation WHERE F_ID = @rtgID_B;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommsourceIDA1_A;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommsourceIDA2_A;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommsourceIDB1_A;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommsourceIDB2_A;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommsourceIDC1_A;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommsourceIDC2_A;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnAggA1_B;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnAggA2_B;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnAggB1_B;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnAggB2_B;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnAggC1_B;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnAggC2_B;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtCommAID_A;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtCommBID_A;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtCommCID_A;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rrtCommAID_B;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rrtCommBID_B;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rrtCommCID_B;
DELETE FROM t_retailtrade WHERE F_ID = @rtID_A;
DELETE FROM t_retailtrade WHERE F_ID = @rrtID_B;
-- ��ƷA���
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehouseCommA1;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehouseCommA2;
DELETE FROM t_warehousing WHERE F_ID = @warehouseA1;
DELETE FROM t_warehousing WHERE F_ID = @warehouseA2;
DELETE FROM t_providercommodity WHERE F_ID = @providerCommIDA;
DELETE FROM t_barcodes WHERE F_ID = @barcodeIDA;
DELETE FROM t_commodity WHERE F_ID = @commIDA;
-- ��ƷB���
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehouseCommB1;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehouseCommB2;
DELETE FROM t_warehousing WHERE F_ID = @warehouseB1;
DELETE FROM t_warehousing WHERE F_ID = @warehouseB2;
DELETE FROM t_providercommodity WHERE F_ID = @providerCommIDB;
DELETE FROM t_barcodes WHERE F_ID = @barcodeIDB;
DELETE FROM t_commodity WHERE F_ID = @commIDB;
-- ��ƷC���
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehouseCommC1;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehouseCommC2;
DELETE FROM t_warehousing WHERE F_ID = @warehouseC1;
DELETE FROM t_warehousing WHERE F_ID = @warehouseC2;
DELETE FROM t_providercommodity WHERE F_ID = @providerCommIDC;
DELETE FROM t_barcodes WHERE F_ID = @barcodeIDC;
DELETE FROM t_commodity WHERE F_ID = @commIDC;