SELECT '++++++++++++++++++Test_SP_RetailTradeDailyReportByStaff_Create_case42.sql+++++++++++++++++++++++';
SELECT '----- case42:Ա��A����һ�����۵���A��Ʒ����Ӧ������ⵥ ,B��Ʒ����Ӧ������ⵥ , C��Ʒ����Ӧ������ⵥ,A���촴��һ���˻���������ƷA��B��Cȫ���˻�[A��A]-------' AS 'Case42';
-- 
--  Ա��A����һ�����۵�,����A��B��C��Ʒ
-- Ա��A�½����۵�
SET @staffIDA = 9;

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
SET @warehouseA1NO = 507;
SET @warehouseA2NO = 499;
SET @warehouseA1Price = 10.001000;
SET @warehouseA2Price = 15.230000;
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
SET @warehouseB1NO = 201;
SET @warehouseB2NO = 301;
SET @warehouseB1Price = 12.340000;
SET @warehouseB2Price = 15.510000;
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
SET @warehouseC1NO = 49;
SET @warehouseC2NO = 403;
SET @warehouseC1Price = 11.110000;
SET @warehouseC2Price = 12.120000;
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehouseC1, @commIDC, @warehouseC1NO, 3, '�����ƷC', @barcodeIDC, @warehouseC1Price, 5000, '2119-01-01 06:00:00', 36, '2119-01-09 06:00:00', '2119-01-01 06:00:00', '2119-01-01 06:00:00', 5);
SET @warehouseCommC1 = last_insert_id();
-- 
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehouseC2, @commIDC, @warehouseC2NO, 3, '�����ƷC', @barcodeIDC, @warehouseC2Price, 7500, '2119-01-01 06:00:00', 36, '2119-01-09 06:00:00', '2119-01-01 06:00:00', '2119-01-01 06:00:00', 5);
SET @warehouseCommC2 = last_insert_id();

SET @saleCommANO_A = @warehouseA1NO + @warehouseA2NO;
SET @aCommotidyPrice = 307.100000;
SET @saleCommBNO_A = @warehouseB1NO + @warehouseB2NO;
SET @bCommotidyPrice = 201.200000;
SET @saleCommCNO_A = @warehouseC1NO + @warehouseC2NO;
SET @cCommotidyPrice = 717.300000;
SET @rtAmuontCash_A = @saleCommANO_A * @aCommotidyPrice + @saleCommBNO_A * @bCommotidyPrice + @saleCommCNO_A * @cCommotidyPrice;
SET @rtAmuontWeChat_A = 0.000000;
SET @rtAmuontAliPay_A = 0.000000;
SET @rtTotalAmount_A = @rtAmuontCash_A + @rtAmuontWeChat_A + @rtAmuontAliPay_A;
SET @saleDatetime_A = '2041/02/01 06:00:00';
SET @syncDatetime_A = '2041/02/01 06:00:00';
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1,'LS2041020200000500010001',11068,1,'url=ashasoadigmnalskd',@saleDatetime_A,@staffIDA,1,'0',1,'��',-1,@syncDatetime_A, @rtTotalAmount_A, 2400.000000, @rtAmuontCash_A,@rtAmuontAliPay_A,@rtAmuontWeChat_A,0.000000,0.000000,0.000000,0.000000,1,NULL,NULL,NULL,NULL,NULL,NULL,2);
SET @rtIDYesterday_A = last_insert_id();
-- ������ƷA
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtIDYesterday_A, @commIDA, '��ͨ��ƷA', @barcodeIDA, @saleCommANO_A, @aCommotidyPrice, @saleCommANO_A, @aCommotidyPrice, 300, NULL);
SET @rtCommAID_A = last_insert_id();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommAID_A, @commIDA, @warehouseA1NO, @warehouseA1);
SET @rtCommsourceIDA1 = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommAID_A, @commIDA, @warehouseA2NO, @warehouseA2);
SET @rtCommsourceIDA2 = last_insert_id();
-- ������ƷB
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtIDYesterday_A, @commIDB, '��ͨ��ƷB', @barcodeIDB, @saleCommBNO_A, @bCommotidyPrice, @saleCommBNO_A, @bCommotidyPrice, 200, NULL);
SET @rtCommBID_A = last_insert_id();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommBID_A, @commIDB, @warehouseB1NO, @warehouseB1);
SET @rtCommsourceIDB1 = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommBID_A, @commIDB, @warehouseB2NO, @warehouseB2);
SET @rtCommsourceIDB2 = last_insert_id();
-- ������ƷC
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtIDYesterday_A, @commIDC, '��ͨ��ƷC', @barcodeIDC, @saleCommCNO_A, @cCommotidyPrice, @saleCommCNO_A, @cCommotidyPrice, 700, NULL);
SET @rtCommCID_A = last_insert_id();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommCID_A, @commIDC, @warehouseC1NO, @warehouseC1);
SET @rtCommsourceIDC1 = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommCID_A, @commIDC, @warehouseC2NO, @warehouseC2);
SET @rtCommsourceIDC2 = last_insert_id();

-- Ա��A�½��˻���
SET @returnCommANO_A = @saleCommANO_A; -- ȫ��
SET @returnCommBNO_A = @saleCommBNO_A; -- ȫ��
SET @returnCommCNO_A = @saleCommCNO_A;
SET @rrtAmuontCash_A = @returnCommANO_A * @aCommotidyPrice + @returnCommBNO_A * @bCommotidyPrice + @returnCommCNO_A * @cCommotidyPrice;
SET @rrtAmuontWeChat_A = 0.000000;
SET @rrtAmuontAliPay_A = 0.000000;
SET @rrtTotalAmount_A = @rrtAmuontCash_A + @rrtAmuontWeChat_A + @rrtAmuontAliPay_A;
SET @saleDatetime_A = '2041/02/02 07:00:01';
SET @syncDatetime_A = '2041/02/02 07:00:01';

INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1,'LS2041020200000500010001_1',11069,1,'url=ashasoadigmnalskd',@saleDatetime_A,@staffIDA,1,'0',1,'��',@rtIDYesterday_A,@syncDatetime_A,@rrtTotalAmount_A,@rrtAmuontCash_A,@rrtAmuontAliPay_A,@rrtAmuontWeChat_A,0.000000,0.000000,0.000000,0.000000,0.000000,1,NULL,NULL,NULL,NULL,NULL,NULL,2);
SET @returnID2 = last_insert_id();
-- ������ƷA
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnID2, @commIDA, '��ͨ��ƷA', 1, @returnCommANO_A, @aCommotidyPrice, @returnCommANO_A, @aCommotidyPrice, 300, NULL);
SET @returnCommAID_A = last_insert_id();

-- ������ƷB
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnID2, @commIDB, '�ɿڿ���', 3, @returnCommBNO_A, @bCommotidyPrice, @returnCommBNO_A, @bCommotidyPrice, 200, NULL);
SET @returnCommBID_A = last_insert_id();

-- ������ƷC
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnID2, @commIDC, '��ͨ��ƷC', @barcodeIDC, @returnCommCNO_A, @cCommotidyPrice, @returnCommCNO_A, @cCommotidyPrice, 700, NULL);
SET @returnCommCID_A = last_insert_id();

-- �����˻�ȥ�����ƷA
SET @NOReturnToA1 = @warehouseA1NO;
SET @NOReturnToA2 = @warehouseA2NO;
SET @NOReturnToB1 = @warehouseB1NO;
SET @NOReturnToB2 = @warehouseB2NO;
SET @NOReturnToC1 = @warehouseC1NO;
SET @NOReturnToC2 = @warehouseC2NO;

INSERT INTO t_returnretailtradecommoditydestination(F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@returnCommAID_A, @commIDA, @NOReturnToA1, @warehouseA1);
SET @rrtCommA1Destination_A = last_insert_id();
INSERT INTO t_returnretailtradecommoditydestination(F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@returnCommAID_A, @commIDA, @NOReturnToA2, @warehouseA2);
SET @rrtCommA2Destination_A = last_insert_id();
-- ��ƷB
INSERT INTO t_returnretailtradecommoditydestination(F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@returnCommBID_A, @commIDB, @NOReturnToB1, @warehouseB1);
SET @rrtCommB1Destination_A = last_insert_id();
INSERT INTO t_returnretailtradecommoditydestination(F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@returnCommBID_A, @commIDB, @NOReturnToB2, @warehouseB2);
SET @rrtCommB2Destination_A = last_insert_id();
-- ��ƷC
INSERT INTO t_returnretailtradecommoditydestination(F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@returnCommCID_A, @commIDC, @NOReturnToC1, @warehouseC1);
SET @rrtCommC1Destination_A = last_insert_id();
INSERT INTO t_returnretailtradecommoditydestination(F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@returnCommCID_A, @commIDC, @NOReturnToC2, @warehouseC2);
SET @rrtCommC2Destination_A = last_insert_id();
-- 
SET @rggTotalAmountYesterday_A = @rtTotalAmount_A;
SET @rggTotalAmountCashYesterday_A = @rtAmuontCash_A;
SET @rggTotalAmountWeChatYesterday_A = @rtAmuontWeChat_A;
SET @rggTotalAmountAliPayYesterday_A = @rtAmuontAliPay_A;
SET @dtStartYesterday = '2041/02/01 00:00:00';
SET @dtEndYesterday = '2041/02/01 23:59:59';
SET @rggNOYesterday = 1;
INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
VALUES (@staffIDA, 2, @dtStartYesterday, @dtEndYesterday, @rggNOYesterday, @rggTotalAmountYesterday_A, 2400.000000, @rggTotalAmountCashYesterday_A, @rggTotalAmountWeChatYesterday_A, @rggTotalAmountAliPayYesterday_A, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, now());
SET @rtgIDYesterday_A = last_insert_id();
-- 
SET @rggTotalAmount_A = @rrtTotalAmount_A;
SET @rggTotalAmountCash_A = @rrtAmuontCash_A;
SET @rggTotalAmountWeChat_A = @rrtAmuontWeChat_A;
SET @rggTotalAmountAliPay_A = @rrtAmuontAliPay_A;
SET @rggWorkTimeStart_A = '2041/02/02 14:00:00';
SET @rggWorkTimeEnd_A = '2041/02/02 23:59:59';
SET @rggNO_A = 0;
INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
VALUES (@staffIDA, 2, @rggWorkTimeStart_A, @rggWorkTimeEnd_A, 0, @rggTotalAmount_A, 2400.000000, @rggTotalAmountCash_A, @rggTotalAmountWeChat_A, @rggTotalAmountAliPay_A, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, now());
SET @rtgID1 = last_insert_id();
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case42.1 Testing Result';
-- �����֤
-- ��֤Ա��A�������ۣ����˻�
-- �����ܽ��
SET @saleTotalAmount_A = @rtTotalAmount_A;
-- ������Ʒ�ܳɱ�
SET @saleTotalCost_A = (@warehouseA1NO * @warehouseA1Price + @warehouseA2NO * @warehouseA2Price) + 
						(@warehouseB1NO * @warehouseB1Price + @warehouseB2NO * @warehouseB2Price) + 
						(@warehouseC1NO * @warehouseC1Price + @warehouseC2NO * @warehouseC2Price);
-- ������Ʒ��ë��
SET @saleTotalMargin_A = @saleTotalAmount_A - @saleTotalCost_A;
-- �˻��ܽ��
SET @returnTotalAmount_A = @rrtTotalAmount_A;
-- �˻���Ʒ�ܳɱ�
SET @returnTotalCost_A = (@NOReturnToA1 * @warehouseA1Price + @NOReturnToA2 * @warehouseA2Price) + 
						 (@NOReturnToB1 * @warehouseB1Price + @NOReturnToB2 * @warehouseB2Price) + 
						 (@NOReturnToC1 * @warehouseC1Price + @NOReturnToC2 * @warehouseC2Price);
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

SELECT IF(@iErrorCode = 0 AND @ResultVerification_A = 1,'���Գɹ�','����ʧ��') AS 'Test Case42 Result';
-- 
DELETE FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime;
DELETE FROM t_retailtradeaggregation WHERE F_ID = @rtgID1;
DELETE FROM t_retailtradeaggregation WHERE F_ID = @rtgIDYesterday_A;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommsourceIDA1;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommsourceIDA2;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommsourceIDB1;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommsourceIDB2;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommsourceIDC1;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommsourceIDC2;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @rrtCommA1Destination_A;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @rrtCommA2Destination_A;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @rrtCommB1Destination_A;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @rrtCommB2Destination_A;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @rrtCommC1Destination_A;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @rrtCommC2Destination_A;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtCommAID_A;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtCommBID_A;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtCommCID_A;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnCommAID_A;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnCommBID_A;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnCommCID_A;
DELETE FROM t_retailtrade WHERE F_ID = @rtIDYesterday_A;
DELETE FROM t_retailtrade WHERE F_ID = @returnID2;
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