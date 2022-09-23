SELECT '++++++++++++++++++Test_SP_RetailTradeDailyReportByStaff_Create_case59.sql+++++++++++++++++++++++';


SELECT '----- case59:Ա��A����һ�����۵���A��Ʒ����Ӧ������ⵥ(һ������λ15��һ��Ϊ10) ,B��Ʒ����Ӧһ����ⵥ , C��Ʒ����Ӧһ����ⵥ,Ա��B����һ���˻���������ƷA��B��Cȫ���˻�,�������������� [A��AB]-------' AS 'Case59';
-- ����Ա
SET @staffID_A = 9;
SET @staffID_B = 10;
SET @ReserveAmount = 500.000000;

-- ��Ʒ��Ϣ
SET @commodityCreateTime = '2119-01-01 09:00:00';
SET @warehousingTime = '2119-01-01 06:00:00';
SET @commodityPriceA = 330;
SET @warehousingCommodityA_Price1 = 9.876543; -- ��ⵥ1
SET @warehousingCommodityA_NO1 = 500;
SET @warehousingCommodityA_Price2 = 8.765432; -- ��ⵥ2
SET @warehousingCommodityA_NO2 = 500;
SET @commodityPriceB = 220;
SET @warehousingCommodityB_Price = 7.654321;
SET @warehousingCommodityB_NO = 500;
SET @commodityPriceC = 770;
SET @warehousingCommodityC_Price = 6.543219;
SET @warehousingCommodityC_NO = 500;

-- ���۵�1
SET @rt1SaleDatetimeA = '2041/02/01 06:00:00'; -- �������۵�ʱ��
SET @rt1CommotidyNO1Yesterday_A = 99; -- ��ⵥ1
SET @rt1CommotidyNO2Yesterday_A = 158; -- ��ⵥ2
SET @rt1CommotidyPriceYesterday_A = 77;
SET @rt1CommotidyNOYesterday_B = 265;
SET @rt1CommotidyPriceYesterday_B = 123;
SET @rt1CommotidyNOYesterday_C = 465;
SET @rt1CommotidyPriceYesterday_C = 321;
SET @rt1TotalAmountYesterday_A = (@rt1CommotidyNO1Yesterday_A + @rt1CommotidyNO2Yesterday_A) * @rt1CommotidyPriceYesterday_A -- 
		+ (@rt1CommotidyNOYesterday_B * @rt1CommotidyPriceYesterday_B) + (@rt1CommotidyNOYesterday_C * @rt1CommotidyPriceYesterday_C);
SET @rt1AmuontCashYesterday_A = @rt1TotalAmountYesterday_A;
SET @rt1AmuontWeChatYesterday_A = 0.000000;
SET @rt1AmuontAliPayYesterday_A = 0.000000;

-- �˻���(�����۵�1) 
SET @rrt1SaleDatetimeB = '2041/02/02 16:00:01'; -- ���������˻���ʱ��
SET @rrt1CommotidyNO1_A = @rt1CommotidyNO1Yesterday_A; -- ��ⵥ1
SET @rrt1CommotidyNO2_A = @rt1CommotidyNO2Yesterday_A; -- ��ⵥ2
SET @rrt1CommotidyNO_B = @rt1CommotidyNOYesterday_B;
SET @returnRetailB_CommodityC_NO = @rt1CommotidyNOYesterday_C;
SET @returnRetailB_TotalAmount = (@rrt1CommotidyNO1_A + @rrt1CommotidyNO2_A) * @rt1CommotidyPriceYesterday_A -- 
						+ (@rrt1CommotidyNO_B * @rt1CommotidyPriceYesterday_B) + (@returnRetailB_CommodityC_NO * @rt1CommotidyPriceYesterday_C);
SET @rrt1AmuontCash_B = @rrt1TotalAmount_B;
SET @rrt1AmuontWeChat_B = 0.000000;
SET @rrt1AmuontAliPay_B = 0.000000;

-- ����ԱA���������������Ϣ(ֻ����)
SET @rggWorkTimeStartYesterday_A='2041/02/01 00:00:00';
SET @rggWorkTimeEndYesterday_A='2041/02/01 23:59:59';
SET @rggTotalAmountYesterday_A = @rt1TotalAmountYesterday_A; 
SET @rggTotalAmountCashYesterday_A = @rt1AmuontCashYesterday_A;
SET @rggTotalAmountWeChatYesterday_A = @rt1AmuontWeChatYesterday_A;
SET @rggTotalAmountAliPayYesterday_A = @rt1AmuontAliPayYesterday_A;
SET @rggNOYesterday_A = 1;

-- ����ԱA���������������Ϣ(���������˻�)
SET @rggWorkTimeStart_A='2041/02/02 00:00:00';
SET @rggWorkTimeEnd_A='2041/02/02 23:59:59';
SET @rggTotalAmount_A = 0 - @returnRetailB_TotalAmount; 
SET @rggTotalAmountCash_A = 0 - @rrt1AmuontCash_B;
SET @rggTotalAmountWeChat_A = 0.000000;
SET @rggTotalAmountAliPay_A = 0.000000;
SET @rggNO_A = 0;

-- ����ԱB���������������Ϣ(������ֻ�˻�)
SET @rggWorkTimeStart_B='2041/02/02 00:00:00';
SET @rggWorkTimeEnd_B='2041/02/02 23:59:59';
SET @rggTotalAmount_B = 0.000000; 
SET @rggTotalAmountCash_B = 0.000000;
SET @rggTotalAmountWeChat_B = 0.000000;
SET @rggTotalAmountAliPay_B = 0.000000;
SET @rggNO_B = 0;


-- ��������ԱA�������������
INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
VALUES (@staffID_A, 2, @rggWorkTimeStartYesterday_A, @rggWorkTimeEndYesterday_A, @rggNOYesterday_A, @rggTotalAmountYesterday_A, @ReserveAmount, @rggTotalAmountCashYesterday_A, @rggTotalAmountWeChatYesterday_A, @rggTotalAmountAliPayYesterday_A, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, now());
SET @rtIDYesterday_A = last_insert_id();
-- ��������ԱA�������������
INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
VALUES (@staffID_A, 2, @rggWorkTimeStart_A, @rggWorkTimeEnd_A, @rggNO_A, @rggTotalAmount_A, @ReserveAmount, @rggTotalAmountCash_A, @rggTotalAmountWeChat_A, @rggTotalAmountAliPay_A, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, now());
SET @rtgIDA = last_insert_id();
-- ��������ԱB������������
INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
VALUES (@staffID_B, 2, @rggWorkTimeStart_B, @rggWorkTimeEnd_B, @rggNO_B, @rggTotalAmount_B, @rggTotalAmountCash_B, @rggTotalAmountWeChat_B, @rggTotalAmountAliPay_B, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, now());
SET @rtgIDB = last_insert_id();
-- ������ƷA
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, -- 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_ShelfLife, F_ReturnDays, -- 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_CreateDatetime, F_UpdateDatetime)
VALUES (0, '�����ƷA', '��ͨ��ƷA', '��', 3, '��', 1, 1, 'CJS666', -- 
1, 19, 19, 0, 1, 365, 3, -- 
@commodityCreateTime, 1, 0, 0, 0, 0, @commodityCreateTime, @commodityCreateTime);
SET @commIDA = last_insert_id();
-- ������ƷA��������
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commIDA, 'cjs66666', @commodityCreateTime, @commodityCreateTime);
SET @barcodeIDA = last_insert_id();
-- ��ƷA������Ӧ��
INSERT INTO t_providercommodity (F_CommodityID, F_ProviderID)
VALUES (@commIDA, 1);
SET @providerCommIDA = last_insert_id();
-- ������ƷA����ⵥ1(���500����ƷA,����10Ԫ,��5000Ԫ)
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_UpdateDatetime)
VALUES (2,'rk211901010001', 1, 1, 2, 4, @warehousingTime, @warehousingTime);
SET @warehouseA1 = last_insert_id();
-- ���������Ʒ1
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, -- 
F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehouseA1, @commIDA, @warehousingCommodityA_NO1, 3, '�����ƷA', @barcodeIDA, @warehousingCommodityA_Price1, (@warehousingCommodityA_Price1 * @warehousingCommodityA_NO1), -- 
@warehousingTime, 36, @warehousingTime, @warehousingTime, @warehousingTime, 5);
SET @warehouseCommA1 = last_insert_id();
-- ������ƷA����ⵥ2(���500����ƷA,����15Ԫ,��7500Ԫ)
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_UpdateDatetime)
VALUES (2,'rk211901010001', 1, 1, 2, 4, @warehousingTime, @warehousingTime);
SET @warehouseA2 = last_insert_id();
-- ���������Ʒ2
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, -- 
F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehouseA2, @commIDA, (@warehousingCommodityA_Price2 * @warehousingCommodityA_NO2), 3, '�����ƷA', @barcodeIDA, @warehousingCommodityA_Price2, (@warehousingCommodityA_Price2 * @warehousingCommodityA_NO2), -- 
@warehousingTime, 36, @warehousingTime, @warehousingTime, @warehousingTime, 5);
SET @warehouseCommA2 = last_insert_id();
-- ������ƷB
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, -- 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_ShelfLife, F_ReturnDays, -- 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_CreateDatetime, F_UpdateDatetime)
VALUES (0, '�����ƷB', '��ͨ��ƷB', '��', 3, '��', 1, 1, 'CJS666', -- 
1, 19, 19, 0, 1, 365, 3, -- 
@commodityCreateTime, 1, 0, 0, 0, 0, @commodityCreateTime, @commodityCreateTime);
SET @commIDB = last_insert_id();
-- ������ƷB��������
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commIDB, 'cjs77777', @commodityCreateTime, @commodityCreateTime);
SET @barcodeIDB = last_insert_id();
-- ��ƷB������Ӧ��
INSERT INTO t_providercommodity (F_CommodityID, F_ProviderID)
VALUES (@commIDB, 1);
SET @providerCommIDB = last_insert_id();
-- ������ƷB����ⵥ
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_UpdateDatetime)
VALUES (2,'rk211901010001', 1, 1, 2, 4, @warehousingTime, @warehousingTime);
SET @warehouseB = last_insert_id();
-- ���������ƷB
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, -- 
F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehouseB, @commIDB, @warehousingCommodityB_NO, 3, '�����ƷB', @barcodeIDB, @warehousingCommodityB_Price, (@warehousingCommodityB_Price * @warehousingCommodityB_NO), -- 
@warehousingTime, 36, @warehousingTime, @warehousingTime, @warehousingTime, 5);
SET @warehouseCommB = last_insert_id();
-- ������ƷC
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, -- 
F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_ShelfLife, F_ReturnDays, -- 
F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_CreateDatetime, F_UpdateDatetime)
VALUES (0, '�����ƷC', '��ͨ��ƷC', '��', 3, '��', 1, 1, 'CJS666', -- 
1, 19, 19, 0, 1, 365, 3, -- 
@commodityCreateTime, 1, 0, 0, 0, 0, @commodityCreateTime, @commodityCreateTime);
SET @commIDC = last_insert_id();
-- ������ƷC��������
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commIDC, 'cjs88888', @commodityCreateTime, @commodityCreateTime);
SET @barcodeIDC = last_insert_id();
-- ��ƷC������Ӧ��
INSERT INTO t_providercommodity (F_CommodityID, F_ProviderID)
VALUES (@commIDC, 1);
SET @providerCommIDC = last_insert_id();
-- ������ƷC����ⵥ
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_UpdateDatetime)
VALUES (2,'rk211901010001', 1, 1, 2, 4, @warehousingTime, @warehousingTime);
SET @warehouseC = last_insert_id();
-- ���������ƷC
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, -- 
F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehouseC, @commIDC, @warehousingCommodityC_NO, 3, '�����ƷC', @barcodeIDC, @warehousingCommodityC_Price, (@warehousingCommodityC_Price * @warehousingCommodityC_NO), -- 
@warehousingTime, 36, @warehousingTime, @warehousingTime, @warehousingTime, 5);
SET @warehouseCommC = last_insert_id();
-- Ա��A������ƷA����ƷB����ƷC
-- �������۵�A
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, -- 
F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, -- 
F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1,'LS2041020200000500010001',11068,1,'url=ashasoadigmnalskd',@rt1SaleDatetimeA,@staffID_A,1,'0',1,'��',-1, -- 
@rt1SaleDatetimeA, @rt1TotalAmountYesterday_A, @rt1AmuontCashYesterday_A, @rt1AmuontAliPayYesterday_A,@rt1AmuontWeChatYesterday_A,0.000000,0.000000,0.000000,0.000000,0.000000,1,NULL, -- 
NULL,NULL,NULL,NULL,NULL,2);
SET @rtIDA = last_insert_id();
-- ������ƷA
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtIDA, @commIDA, '��ͨ��ƷA', @barcodeIDA, (@rt1CommotidyNO1Yesterday_A + @rt1CommotidyNO2Yesterday_A), @commodityPriceA, (@rt1CommotidyNO1Yesterday_A + @rt1CommotidyNO2Yesterday_A), @rt1CommotidyPriceYesterday_A, 300, NULL);
SET @rtA_CommIDA = last_insert_id();
-- �������۵���ƷA����Դ��1(��Ӧ������ⵥ1)
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtA_CommIDA, @commIDA, @rt1CommotidyNO1Yesterday_A, @warehouseA1);
SET @rtA_CommsourceIDA1 = last_insert_id();
-- �������۵���ƷA����Դ��2(��Ӧ������ⵥ2)
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtA_CommIDA, @commIDA, @rt1CommotidyNO2Yesterday_A, @warehouseA2);
SET @rtA_CommsourceIDA2 = last_insert_id();
-- ������ƷB
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtIDA, @commIDB, '��ͨ��ƷB', @barcodeIDB, @rt1CommotidyNOYesterday_B, @commodityPriceB, @rt1CommotidyNOYesterday_B, @rt1CommotidyPriceYesterday_B, 200, NULL);
SET @rtA_CommIDB = last_insert_id();
-- �������۵���ƷB����Դ��
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtA_CommIDB, @commIDB, @rt1CommotidyNOYesterday_B, @warehouseB);
SET @rtA_CommsourceIDB = last_insert_id();
-- ������ƷC
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtIDA, @commIDC, '��ͨ��ƷC', @barcodeIDC, @rt1CommotidyNOYesterday_C, @commodityPriceC, @rt1CommotidyNOYesterday_C, @rt1CommotidyPriceYesterday_C, 700, NULL);
SET @rtA_CommIDC = last_insert_id();
-- �������۵���ƷC����Դ��
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtA_CommIDC, @commIDC, @rt1CommotidyNOYesterday_C, @warehouseC);
SET @rtA_CommsourceIDC = last_insert_id();
-- Ա��B�����۵�A��������ƷA����ƷB����ƷCȫ���˻�
-- ���������˻���B
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1,'LS2041020200000500010001_1',11069,1,'url=ashasoadigmnalskd',@rrt1SaleDatetimeB,@staffID_B,1,'0',1,'��',@rtIDA,@rrt1SaleDatetimeB,@returnRetailB_TotalAmount,@rrt1AmuontCash_B,@rrt1AmuontAliPay_B,@rrt1AmuontWeChat_B,0.000000,0.000000,0.000000,0.000000,0.000000,1,NULL,NULL,NULL,NULL,NULL,NULL,2);
SET @returnIDB = last_insert_id();
-- ���������˻�����ƷA
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnIDB, @commIDA, '��ͨ��ƷA', @barcodeIDA, (@rrt1CommotidyNO1_A + @rrt1CommotidyNO2_A), @commodityPriceA, (@rrt1CommotidyNO1_A + @rrt1CommotidyNO2_A), @rt1CommotidyPriceYesterday_A, 300, NULL);
SET @returnCommIDA = last_insert_id();
-- ���������˻�����ƷA���˻�ȥ������1(��Ӧ������ⵥ1)
INSERT INTO t_returnretailtradecommoditydestination(F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@returnCommIDA, @commIDA, @rrt1CommotidyNO1_A, @warehouseA1);
SET @returnAggA1 = last_insert_id();
-- ���������˻�����ƷA���˻�ȥ������2(��Ӧ������ⵥ2)
INSERT INTO t_returnretailtradecommoditydestination(F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@returnCommIDA, @commIDA, @rrt1CommotidyNO2_A, @warehouseA2);
SET @returnAggA2 = last_insert_id();
-- ���������˻�����ƷB
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnIDB, @commIDB, '��ͨ��ƷB', @barcodeIDB, @rrt1CommotidyNO_B, @commodityPriceB, @rrt1CommotidyNO_B, @rt1CommotidyPriceYesterday_B, 200, NULL);
SET @returnCommIDB = last_insert_id();
-- ���������˻�����ƷB���˻�ȥ������
INSERT INTO t_returnretailtradecommoditydestination(F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@returnCommIDB, @commIDB, @rrt1CommotidyNO_B, @warehouseB);
SET @returnAggB = last_insert_id();
-- ���������˻�����ƷC
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@returnIDB, @commIDC, '��ͨ��ƷC', @barcodeIDC, @returnRetailB_CommodityC_NO, @commodityPriceC, @returnRetailB_CommodityC_NO, @rt1CommotidyPriceYesterday_C, 700, NULL);
SET @returnCommIDC = last_insert_id();
-- ���������˻�����ƷC���˻�ȥ������
INSERT INTO t_returnretailtradecommoditydestination(F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@returnCommIDC, @commIDC, @returnRetailB_CommodityC_NO, @warehouseC);
SET @returnAggC = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime = '2041-02-02 00:00:00';
SET @iDeleteOldData = 1;
SET @iShopID = 2;
-- 
CALL SP_RetailTradeDailyReportByStaff_Create(@iErrorCode, @sErrorMsg, @iShopID, @dSaleDatetime, @iDeleteOldData);
SELECT @iErrorCode, @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case59.1 Testing Result';
-- 
-- ��֤Ա��ҵ�������Ƿ��ж�Ա��A��Ĭ�ϵ����ݵĲ���,����ʱ�䡢Ա��ID��ѯԱ��ҵ������
SET @countDefaultData = 0;
SELECT COUNT(1) INTO @countDefaultData FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime AND F_StaffID = @staffID_A;
SELECT IF(@countDefaultData = 1, '���Գɹ�', '����ʧ��') AS 'Case59.2 Testing Result';
SET @totalAmountA = 0;
SELECT F_TotalAmount INTO @totalAmountA FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime AND F_StaffID = @staffID_A;
SELECT IF(@totalAmountA = 0 - @rt1TotalAmountYesterday_A, '���Գɹ�', '����ʧ��') AS 'Case59.3 Testing Result';
-- 
-- ��֤���۵�����ͳ���ܵ����۵���Ա��ҵ�������F_NO�Ƿ�һ��
SET @retailtradeNOA = 0;
SELECT count(1) INTO @retailtradeNOA FROM t_retailtrade WHERE F_SourceID = -1 AND F_StaffID = @staffID_A AND F_SaleDatetime BETWEEN @rggWorkTimeStart_A AND @rggWorkTimeEnd_A;
SET @rtNoByStaffA = 0;
SELECT F_NO INTO @rtNoByStaffA FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime AND F_StaffID = @staffID_A;
SELECT IF(@retailtradeNOA = @rtNoByStaffA, '���Գɹ�', '����ʧ��') AS 'Case59.4 Testing Result';
-- 
-- ����ҵ�������ë�������֤

-- ����ë�� = �����ܶ� - ������ = 500 * 300 + 100 * 200 + 100 * 700 - (200 * 10 + 300 * 15)- 100*10 - 100*10 = 150000+20000+70000-6500-1000-1000=240000-8500=231500
-- �˻�ë�� = �˻��ܶ� - ������ = 160000 - (200 * 10 + 300 * 15) - 100*10 = 160000 - 6500 - 1000 = 152500 = 79000
SET @grossMargin = 0.000000;
SELECT F_GrossMargin INTO @grossMargin FROM t_retailtradedailyreportbystaff WHERE F_StaffID = @staffID_A AND F_Datetime = @dSaleDatetime;
SELECT IF(@grossMargin = (@rrt1CommotidyNO1_A * @warehousingCommodityA_Price1) + (@rrt1CommotidyNO2_A * @warehousingCommodityA_Price2) -- 
						+ (@rrt1CommotidyNO_B * @warehousingCommodityB_Price) + (@returnRetailB_CommodityC_NO * @warehousingCommodityC_Price) - @returnRetailB_TotalAmount, '���Գɹ�', '����ʧ��') AS 'Case59.5 Testing Result';
-- 
-- ��֤B��Ա��ҵ��(1)Ա��ҵ�������Ƿ���B��Ĭ�����ݲ���(2)�����ܶ�(3)���۱���(4)����ë��
SET @countDefaultDataB = 0;
SELECT COUNT(1) INTO @countDefaultDataB FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime AND F_StaffID = @staffID_B;
SELECT IF(@countDefaultDataB = 1, '���Գɹ�', '����ʧ��') AS 'Case59.6 Testing Result';
SET @totalAmountB = 0;
SELECT F_TotalAmount INTO @totalAmountB FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime AND F_StaffID = @staffID_B;
SELECT IF(@totalAmountB = 0.000000, '���Գɹ�', '����ʧ��') AS 'Case59.7 Testing Result';
SET @retailtradeNOB = 0;
SELECT count(1) INTO @retailtradeNOB FROM t_retailtrade WHERE F_SourceID = -1 AND F_StaffID = @staffID_B AND F_SaleDatetime BETWEEN @rggWorkTimeStart_B AND @rggWorkTimeEnd_B;
SET @rtNoByStaffB = 0;
SELECT F_NO INTO @rtNoByStaffB FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime AND F_StaffID = @staffID_B;
SELECT IF(@retailtradeNOB = @rtNoByStaffB, '���Գɹ�', '����ʧ��') AS 'Case59.8 Testing Result';
SET @grossMarginB = 0.000000;
SELECT F_GrossMargin INTO @grossMarginB FROM t_retailtradedailyreportbystaff WHERE F_StaffID = @staffID_B AND F_Datetime = @dSaleDatetime;
SELECT IF(@grossMarginB = 0.000000, '���Գɹ�', '����ʧ��') AS 'Case59.9 Testing Result';
-- ɾ�����Դ���������
DELETE FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime;
DELETE FROM t_retailtradeaggregation WHERE F_ID = @rtIDYesterday_A;
DELETE FROM t_retailtradeaggregation WHERE F_ID = @rtgIDA;
DELETE FROM t_retailtradeaggregation WHERE F_ID = @rtgIDB;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtA_CommsourceIDA1;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtA_CommsourceIDA2;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtA_CommsourceIDB;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtA_CommsourceIDC;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnAggA1;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnAggA2;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnAggB;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @returnAggC;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtA_CommIDA;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtA_CommIDB;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtA_CommIDC;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnCommIDA;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnCommIDB;
DELETE FROM t_retailtradecommodity WHERE F_ID = @returnCommIDC;
DELETE FROM t_retailtrade WHERE F_ID = @rtIDA;
DELETE FROM t_retailtrade WHERE F_ID = @returnIDB;
-- ��ƷA���
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehouseCommA1;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehouseCommA2;
DELETE FROM t_warehousing WHERE F_ID = @warehouseA1;
DELETE FROM t_warehousing WHERE F_ID = @warehouseA2;
DELETE FROM t_providercommodity WHERE F_ID = @providerCommIDA;
DELETE FROM t_barcodes WHERE F_ID = @barcodeIDA;
DELETE FROM t_commodity WHERE F_ID = @commIDA;
-- ��ƷB���
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehouseCommB;
DELETE FROM t_warehousing WHERE F_ID = @warehouseB;
DELETE FROM t_providercommodity WHERE F_ID = @providerCommIDB;
DELETE FROM t_barcodes WHERE F_ID = @barcodeIDB;
DELETE FROM t_commodity WHERE F_ID = @commIDB;
-- ��ƷC���
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehouseCommC;
DELETE FROM t_warehousing WHERE F_ID = @warehouseC;
DELETE FROM t_providercommodity WHERE F_ID = @providerCommIDC;
DELETE FROM t_barcodes WHERE F_ID = @barcodeIDC;
DELETE FROM t_commodity WHERE F_ID = @commIDC;