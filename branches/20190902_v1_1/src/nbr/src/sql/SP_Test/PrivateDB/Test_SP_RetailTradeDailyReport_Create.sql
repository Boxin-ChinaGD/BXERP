SELECT '++++++++++++++++++Test_SP_RetailTradeDailyReport_Create.sql+++++++++++++++++++++++';

SELECT '-------------------- case1 ���۵������ۼ�¼ ���۵��У���Ʒ�����װ����ϡ���Ʒ+���װ����Ʒ+��ϡ����װ+��� --------------------' AS 'Case1';
-- ����������۶��Ƕ��װ��Ʒ������ı䵱��������۶���Ҫ�ı�����Ľ������
SET @iSubCommodityNO1 = 2; -- ����Ʒ1����
SET @iSubCommodityNO2 = 3; -- ����Ʒ2����
SET @iSubCommodityPrice1 = 5; -- ����Ʒ1�۸�
SET @iSubCommodityPrice2 = 7; -- ����Ʒ2�۸�
SET @iMultiple = 10; -- ���װ����
SET @iPriceSimpleCommodity1 = 122; -- ��Ʒ1�۸�
SET @iPriceSimpleCommodity2 = 133; -- ��Ʒ2�۸�
SET @iPriceSimpleCommodity3 = 144; -- ��Ʒ3�۸�
SET @iPriceSimpleCommodity4 = 15; -- ��Ʒ4�۸�
SET @iPriceCompositionCommodity5 = (@iSubCommodityNO1 * @iSubCommodityPrice1 + @iSubCommodityNO2 * @iSubCommodityPrice2); -- ���5�۸�
SET @iPriceMutiplePackagingCommodity6 = (@iMultiple * @iPriceSimpleCommodity4); -- ���װ6�۸�
SET @iWarehousingCommodityPrice1 = 2.1;--  �����Ʒ1�۸�
SET @iWarehousingCommodityPrice1_1 = 2.2;--  �����Ʒ1_1�۸�
SET @iWarehousingCommodityPrice2 = 2.3;--  �����Ʒ2�۸�
SET @iWarehousingCommodityPrice3 = 2.5;--  �����Ʒ3�۸�
SET @iWarehousingCommodityPrice4 = 3.5;--  �����Ʒ4�۸�
SET @iWarehousingCommodityNO1 = 40;--  �����Ʒ1�������
SET @iWarehousingCommodityNO1_1 = 10;--  �����Ʒ1_1�������
SET @iRetailtradeCommodityNO1 = (@iWarehousingCommodityNO1 + @iWarehousingCommodityNO1_1); -- ������Ʒ1����
SET @iRetailtradeCommodityNO2 = 10; -- ������Ʒ2����
SET @iRetailtradeCommodityNO3 = 10; -- ������Ʒ3����
SET @iRetailtradeCommodityNO4 = 10; -- ������Ʒ4����
SET @iRetailtradeCommodityNO5 = 10; -- ������Ʒ5����
SET @iRetailtradeCommodityNO6 = 30; -- ������Ʒ6����
SET @iWarehousingCommodityNO2 = (@iRetailtradeCommodityNO2 * @iSubCommodityNO1 + @iRetailtradeCommodityNO4 + @iRetailtradeCommodityNO5 * @iSubCommodityNO1 + @iRetailtradeCommodityNO6 * @iSubCommodityNO1);--  �����Ʒ2�������
SET @iWarehousingCommodityNO3 = (@iRetailtradeCommodityNO2 * @iSubCommodityNO2 + @iRetailtradeCommodityNO5 * @iSubCommodityNO2 + @iRetailtradeCommodityNO5 + @iRetailtradeCommodityNO6 * @iSubCommodityNO2);--  �����Ʒ3�������
SET @iWarehousingCommodityNO4 = (@iRetailtradeCommodityNO3 * @iMultiple + @iRetailtradeCommodityNO4 * @iMultiple + @iRetailtradeCommodityNO6 * @iMultiple );--  �����Ʒ4�������
SET @iAmount1 = (@iRetailtradeCommodityNO1 * @iPriceSimpleCommodity1); -- ���۵�1�ܽ��
SET @iAmount2 = (@iRetailtradeCommodityNO2 * @iPriceSimpleCommodity2); -- ���۵�2�ܽ��
SET @iAmount3 = (@iRetailtradeCommodityNO3 * @iPriceSimpleCommodity3); -- ���۵�3�ܽ��
SET @iAmount4 = (@iRetailtradeCommodityNO4 * @iPriceSimpleCommodity4); -- ���۵�4�ܽ��
SET @iAmount5 = (@iRetailtradeCommodityNO5 * @iPriceCompositionCommodity5); -- ���۵�5�ܽ��
SET @iAmount6 = (@iRetailtradeCommodityNO6 * @iPriceMutiplePackagingCommodity6); -- ���۵�6�ܽ��
SET @saleDatetime = DATE_ADD(now(),INTERVAL 3000 DAY); -- ��������
-- ���뵥Ʒ1
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0/*F_Status*/, '�ɱȿ�1111', '��Ƭ', '��', 1, '��', 3, 1, 'SP', 1, 
@iPriceSimpleCommodity1-1,@iPriceSimpleCommodity1-2, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0/*F_Type*/, NULL, '2019-11-22 14:48:19', '2019-11-22 14:48:19', NULL, NULL, NULL, NULL);
SET @iCommodityID1 = last_insert_id();
-- ���뵥Ʒ2
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0/*F_Status*/, '�ɱȿ�2222', '��Ƭ', '��', 1, '��', 3, 1, 'SP', 1, 
@iPriceSimpleCommodity2-1,@iPriceSimpleCommodity2-2, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0/*F_Type*/, NULL, '2019-11-22 14:48:19', '2019-11-22 14:48:19', NULL, NULL, NULL, NULL);
SET @iCommodityID2 = last_insert_id();
-- ���뵥Ʒ3
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0/*F_Status*/, '�ɱȿ�3333', '��Ƭ', '��', 1, '��', 3, 1, 'SP', 1, 
@iPriceSimpleCommodity3-1,@iPriceSimpleCommodity3-2, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0/*F_Type*/, NULL, '2019-11-22 14:48:19', '2019-11-22 14:48:19', NULL, NULL, NULL, NULL);
SET @iCommodityID3 = last_insert_id();
-- ���뵥Ʒ4
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0/*F_Status*/, '�ɱȿ�4444', '��Ƭ', '��', 1, '��', 3, 1, 'SP', 1, 
@iPriceSimpleCommodity4-1,@iPriceSimpleCommodity4-2, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0/*F_Type*/, NULL, '2019-11-22 14:48:19', '2019-11-22 14:48:19', NULL, NULL, NULL, NULL);
SET @iCommodityID4 = last_insert_id();
-- ���������Ʒ5
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0/*F_Status*/, '�ɱȿˣ���ϣ�', '��Ƭ', '��', 1, '��', 3, 1, 'SP', 1, 
@iPriceCompositionCommodity5-1,@iPriceCompositionCommodity5-2, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 1/*F_Type*/, NULL, '2019-11-22 14:48:19', '2019-11-22 14:48:19', NULL, NULL, NULL, NULL);
SET @iCommodityID5 = last_insert_id();
-- ������װ��Ʒ6
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0/*F_Status*/, '�ɱȿˣ����װ��', '��Ƭ', '��', 1, '��', 3, 1, 'SP', 1, 
@iPriceMutiplePackagingCommodity6-1,@iPriceMutiplePackagingCommodity6-2, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, @iCommodityID4, @iMultiple, '1111111', 2/*F_Type*/, NULL, '2019-11-22 14:48:19', '2019-11-22 14:48:19', NULL, NULL, NULL, NULL);
SET @iCommodityID6 = last_insert_id();
-- ����Ʒ1
INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iCommodityID5,@iCommodityID2,@iSubCommodityNO1,@iSubCommodityPrice1, '2019-11-22 15:31:38', '2019-11-22 15:31:38');
-- ����Ʒ2
INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iCommodityID5,@iCommodityID3,@iSubCommodityNO2,@iSubCommodityPrice2, '2019-11-22 15:31:38', '2019-11-22 15:31:38');
-- ������ⵥ1
INSERT INTO t_warehousing (F_ShopID, F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (2/*F_ShopID*/, 1/*F_Status*/, 'RK201909050001', 1, 1, 4, NULL, '2017-08-06 01:01:01', 1, '2019-11-22 15:31:39');
SET @iWarehousingID1 = last_insert_id();
-- ������ⵥ2
INSERT INTO t_warehousing (F_ShopID, F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (2/*F_ShopID*/, 1/*F_Status*/, 'RK201909050002', 1, 1, 4, NULL, '2017-08-06 01:01:01', 1, '2019-11-22 15:31:39');
SET @iWarehousingID2 = last_insert_id();
-- ������ⵥ��Ʒ1
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID1, @iCommodityID1, @iWarehousingCommodityNO1, 1, '�ɱȿ�1111', 1, @iWarehousingCommodityPrice1, 2000, '2017-08-06 01:01:01', 36, '2020-08-06 01:01:01', '2019-11-22 15:31:39', '2019-11-22 15:31:39',0/*F_NOSalable*/);
-- ������ⵥ��Ʒ1_1
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID2, @iCommodityID1, @iWarehousingCommodityNO1_1, 1, '�ɱȿ�1111', 1, @iWarehousingCommodityPrice1_1, 2000, '2017-08-06 01:01:01', 36, '2020-08-06 01:01:01', '2019-11-22 15:31:39', '2019-11-22 15:31:39',0/*F_NOSalable*/);
-- ������ⵥ��Ʒ2
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID1, @iCommodityID2, @iWarehousingCommodityNO2, 1, '�ɱȿ�2222', 1, @iWarehousingCommodityPrice2, 2000, '2017-08-06 01:01:01', 36, '2020-08-06 01:01:01', '2019-11-22 15:31:39', '2019-11-22 15:31:39', 0/*F_NOSalable*/);
-- ������ⵥ��Ʒ3
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID1, @iCommodityID3, @iWarehousingCommodityNO3, 1, '�ɱȿ�3333', 1, @iWarehousingCommodityPrice3, 2000, '2017-08-06 01:01:01', 36, '2020-08-06 01:01:01', '2019-11-22 15:31:39', '2019-11-22 15:31:39', 0/*F_NOSalable*/);
-- ������ⵥ��Ʒ4
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID1, @iCommodityID4, @iWarehousingCommodityNO4, 1, '�ɱȿ�4444', 1, @iWarehousingCommodityPrice4, 2000, '2017-08-06 01:01:01', 36, '2020-08-06 01:01:01', '2019-11-22 15:31:39', '2019-11-22 15:31:39', 0/*F_NOSalable*/);
-- �������۵�1������Ʒ1(���)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019110601010100011201', 1, 1, 'url=ashasoadigmnalskd',DATE_ADD(@saleDatetime,INTERVAL 1 minute), 2, 4, '0', 1, '........', -1/*F_SourceID*/, '2019-11-22 14:48:21', @iAmount1, 0, 0, @iAmount1, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iTradeID1 = last_insert_id();
-- �������۵���Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iTradeID1,@iCommodityID1, '�ɱȿ�1111', 1, @iRetailtradeCommodityNO1, @iPriceSimpleCommodity1, @iRetailtradeCommodityNO1, @iPriceSimpleCommodity1, 300, @iPriceSimpleCommodity1);
SET @iRetailtradeCommodityID1 = last_insert_id();
-- ����������Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailtradeCommodityID1, @iCommodityID1,@iWarehousingCommodityNO1, @iWarehousingID1);
--
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailtradeCommodityID1, @iCommodityID1,@iWarehousingCommodityNO1_1, @iWarehousingID2);
-- �������۵�2���������Ʒ
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019110601010100011202', 1, 2, 'url=ashasoadigmnalskd',DATE_ADD(@saleDatetime,INTERVAL 1 minute), 2, 4, '0', 1, '........', -1/*F_SourceID*/, '2019-11-22 14:48:21', @iAmount2, 0, 0, @iAmount2, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iTradeID2 = last_insert_id();
-- �������۵���Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iTradeID2,@iCommodityID5, '�ɱȿˣ���ϣ�', 1, @iRetailtradeCommodityNO2, @iPriceCompositionCommodity5, @iRetailtradeCommodityNO2, @iPriceCompositionCommodity5, 300, @iPriceCompositionCommodity5);
SET @iRetailtradeCommodityID2 = last_insert_id();
-- ����������Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailtradeCommodityID2, @iCommodityID2,(@iRetailtradeCommodityNO2 * @iSubCommodityNO1), @iWarehousingID1);
--
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailtradeCommodityID2, @iCommodityID3,(@iRetailtradeCommodityNO2 * @iSubCommodityNO2), @iWarehousingID1);
-- �������۵�3�������װ��Ʒ
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019110601010100011203', 1, 3, 'url=ashasoadigmnalskd',DATE_ADD(@saleDatetime,INTERVAL 1 minute), 2, 4, '0', 1, '........', -1/*F_SourceID*/, '2019-11-22 14:48:21', @iAmount3, 0, 0, @iAmount3, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iTradeID3 = last_insert_id();
-- �������۵���Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iTradeID3,@iCommodityID6, '�ɱȿˣ����װ��', 1, @iRetailtradeCommodityNO3, @iPriceMutiplePackagingCommodity6, @iRetailtradeCommodityNO3, @iPriceMutiplePackagingCommodity6, 300, @iPriceMutiplePackagingCommodity6);
SET @iRetailtradeCommodityID3 = last_insert_id();
-- ����������Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailtradeCommodityID3, @iCommodityID4,(@iRetailtradeCommodityNO3 * @iMultiple), @iWarehousingID1);
-- �������۵�4������Ʒ2 + ���װ��Ʒ
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019110601010100011204', 1, 4, 'url=ashasoadigmnalskd',DATE_ADD(@saleDatetime,INTERVAL 1 minute), 2, 4, '0', 1, '........', -1/*F_SourceID*/, '2019-11-22 14:48:21', @iAmount4, 0, 0, @iAmount4, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iTradeID4 = last_insert_id();
-- �������۵���Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iTradeID4,@iCommodityID2, '�ɱȿ�2222', 1, @iRetailtradeCommodityNO4, @iPriceSimpleCommodity2, @iRetailtradeCommodityNO4, @iPriceSimpleCommodity2, 300, @iPriceSimpleCommodity2);
SET @iRetailtradeCommodityID4 = last_insert_id();
--
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iTradeID4,@iCommodityID6, '�ɱȿˣ����װ��', 1, @iRetailtradeCommodityNO4, @iPriceMutiplePackagingCommodity6, @iRetailtradeCommodityNO4, @iPriceMutiplePackagingCommodity6, 300, @iPriceMutiplePackagingCommodity6);
SET @iRetailtradeCommodityID4_1 = last_insert_id();
-- ����������Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailtradeCommodityID4, @iCommodityID2,@iRetailtradeCommodityNO4, @iWarehousingID1);
--
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailtradeCommodityID4_1, @iCommodityID4,(@iRetailtradeCommodityNO4 * @iMultiple), @iWarehousingID1);
-- �������۵�5���������Ʒ + ��Ʒ3
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019110601010100011205', 1, 5, 'url=ashasoadigmnalskd',DATE_ADD(@saleDatetime,INTERVAL 1 minute), 2, 4, '0', 1, '........', -1/*F_SourceID*/, '2019-11-22 14:48:21', @iAmount5, 0, 0, @iAmount5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iTradeID5 = last_insert_id();
-- �������۵���Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iTradeID5,@iCommodityID5, '�ɱȿˣ���ϣ�', 1, @iRetailtradeCommodityNO5, @iPriceCompositionCommodity5, @iRetailtradeCommodityNO5, @iPriceCompositionCommodity5, 300, @iPriceCompositionCommodity5);
SET @iRetailtradeCommodityID5 = last_insert_id();
--
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iTradeID5,@iCommodityID3, '�ɱȿ�3333', 1, @iRetailtradeCommodityNO5, @iPriceSimpleCommodity3, @iRetailtradeCommodityNO5, @iPriceSimpleCommodity3, 300, @iPriceSimpleCommodity3);
SET @iRetailtradeCommodityID5_1 = last_insert_id();
-- ����������Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailtradeCommodityID5, @iCommodityID2,(@iRetailtradeCommodityNO5 * @iSubCommodityNO1), @iWarehousingID1);
--
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailtradeCommodityID5, @iCommodityID3,(@iRetailtradeCommodityNO5 * @iSubCommodityNO2), @iWarehousingID1);
--
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailtradeCommodityID5_1, @iCommodityID3,@iRetailtradeCommodityNO5, @iWarehousingID1);
-- �������۵�6���������Ʒ + ���װ��Ʒ
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019110601010100011206', 1,6, 'url=ashasoadigmnalskd',DATE_ADD(@saleDatetime,INTERVAL 1 minute), 2, 4, '0', 1, '........', -1/*F_SourceID*/, '2019-11-22 14:48:21', @iAmount6, 0, 0, @iAmount6, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iTradeID6 = last_insert_id();
-- �������۵���Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iTradeID6,@iCommodityID5, '�ɱȿˣ���ϣ�', 1, @iRetailtradeCommodityNO6, @iPriceCompositionCommodity5, @iRetailtradeCommodityNO6, @iPriceCompositionCommodity5, 300, @iPriceCompositionCommodity5);
SET @iRetailtradeCommodityID6 = last_insert_id();
--
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iTradeID6,@iCommodityID6, '�ɱȿˣ����װ��', 1, @iRetailtradeCommodityNO6, @iPriceMutiplePackagingCommodity6, @iRetailtradeCommodityNO6, @iPriceMutiplePackagingCommodity6, 300, @iPriceMutiplePackagingCommodity6);
SET @iRetailtradeCommodityID6_1 = last_insert_id();
-- ����������Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailtradeCommodityID6, @iCommodityID2,(@iRetailtradeCommodityNO6 * @iSubCommodityNO1), @iWarehousingID1);
--
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailtradeCommodityID6, @iCommodityID3,(@iRetailtradeCommodityNO6 * @iSubCommodityNO2), @iWarehousingID1);
--
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailtradeCommodityID6_1, @iCommodityID4,(@iRetailtradeCommodityNO6 * @iMultiple), @iWarehousingID1);
--
SET @dSaleDatetime = DATE_FORMAT(@saleDatetime,'%Y/%m/%d');-- ��������
SET @iTotalNO = (SELECT count(F_ID) FROM t_retailtrade WHERE DATE_FORMAT(F_SaleDatetime,'%Y/%m/%d') = @dSaleDatetime);
SET @iPricePurchase = (@iWarehousingCommodityNO1 * @iWarehousingCommodityPrice1 + @iWarehousingCommodityNO1_1 * @iWarehousingCommodityPrice1_1
                    + @iWarehousingCommodityNO2 * @iWarehousingCommodityPrice2 + @iWarehousingCommodityNO3 * @iWarehousingCommodityPrice3
                    + @iWarehousingCommodityNO4 * @iWarehousingCommodityPrice4);-- һ��������Ʒ���ܽ�����                    
SET @iTotalAmount = (@iAmount1 + @iAmount2 + @iAmount3 + @iAmount4 + @iAmount5 + @iAmount6);-- �������۶�
SET @iTopSaleCommodityAmount = (@iWarehousingCommodityNO4 * @iPriceSimpleCommodity4);-- ����������۶�
SET @iShopID = 2;
SET @sErrorMsg = '';
SET @deleteOldData = 0;
CALL SP_RetailTradeDailyReport_Create(@iErrorCode, @sErrorMsg, @iShopID, @dSaleDatetime, @deleteOldData);
-- SELECT @dSaleDatetime,@iTotalNO,@iPricePurchase,@iTotalAmount,@iTopSaleCommodityAmount;
SELECT 1 FROM t_retailtradedailyreportsummary WHERE F_Datetime = @dSaleDatetime
													AND F_TotalNO = @iTotalNO  
													AND F_PricePurchase = @iPricePurchase
													AND F_TotalAmount = @iTotalAmount 
													AND F_TopSaleCommodityAmount = @iTopSaleCommodityAmount;
SET @found_row1 = found_rows();
SELECT 1 FROM t_retailtradedailyreportbycommodity WHERE F_Datetime = @dSaleDatetime;
SET @found_row2 = found_rows();

SELECT IF(@iErrorCode = 0 AND @found_row1 <> 0 AND @found_row2 <> 0,'���Գɹ�','����ʧ��') AS 'Test Case1 Result';

DELETE FROM t_retailtradedailyreportsummary WHERE F_Datetime = @dSaleDatetime;
DELETE FROM t_retailtradedailyreportbycommodity WHERE F_Datetime = @dSaleDatetime;
DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID IN (@iRetailtradeCommodityID1,@iRetailtradeCommodityID2,
                                                                            @iRetailtradeCommodityID3,@iRetailtradeCommodityID4,
                                                                            @iRetailtradeCommodityID4_1,@iRetailtradeCommodityID5,
                                                                            @iRetailtradeCommodityID5_1,@iRetailtradeCommodityID6,
                                                                            @iRetailtradeCommodityID6_1);
DELETE FROM t_retailtradecommodity WHERE F_TradeID IN (@iTradeID1,@iTradeID2,@iTradeID3,@iTradeID4,@iTradeID5,@iTradeID6);
DELETE FROM t_retailtrade WHERE F_ID IN (@iTradeID1,@iTradeID2,@iTradeID3,@iTradeID4,@iTradeID5,@iTradeID6);
DELETE FROM t_warehousingcommodity WHERE F_WarehousingID IN(@iWarehousingID1,@iWarehousingID2);
DELETE FROM t_warehousing WHERE F_ID IN(@iWarehousingID1,@iWarehousingID2);
DELETE FROM t_subcommodity WHERE F_CommodityID IN(@iCommodityID5);
DELETE FROM t_commodity WHERE F_ID IN(@iCommodityID1,@iCommodityID2,@iCommodityID3,@iCommodityID4,@iCommodityID5,@iCommodityID6);

SELECT '-------------------- case2 ���۵�û�����ۼ�¼ --------------------' AS 'Case2';
SET @dSaleDatetime = '2029-1-15 00:00:00';
SET @sErrorMsg = '';
SET @deleteOldData = 0;
CALL SP_RetailTradeDailyReport_Create(@iErrorCode, @sErrorMsg, @iShopID, @dSaleDatetime, @deleteOldData);
SELECT @iErrorCode;
SELECT 1 FROM t_retailtradedailyreportsummary WHERE F_Datetime = '2029-1-15 00:00:00' AND F_TotalNO > 0;
SET @found_row1 = found_rows();
SELECT 1 FROM t_retailtradedailyreportbycommodity WHERE F_Datetime = '2029-1-15 00:00:00';
SET @found_row2 = found_rows();
SELECT @found_row1, @found_row2, @iErrorCode;
SELECT IF(@iErrorCode = 0 AND @found_row1 = 0 AND @found_row2 = 0,'���Գɹ�','����ʧ��') AS 'Test Case2 Result';

SELECT '-------------------- case3 �˻���ر���  ���۵���:��Ʒ�����װ����� �˻�����:��Ʒ(��ȫ��,����)�����װ(�˲���,����)�������Ʒ(�˲���,����) --------------------' AS 'Case3';
--  ����������۶��������Ʒ������ı䵱��������۶���Ҫ�ı�����Ľ����֤
SET @iSubCommodityNO1 = 3; -- ����Ʒ1����
SET @iSubCommodityNO2 = 4; -- ����Ʒ2����
SET @iSubCommodityPrice1 = 5.79; -- ����Ʒ1�۸�
SET @iSubCommodityPrice2 = 7.71; -- ����Ʒ2�۸�
SET @iMultiple = 10; -- ���װ����
SET @iPriceSimpleCommodity1= 182; -- ��Ʒ1�۸�
SET @iPriceSimpleCommodity2= 173; -- ��Ʒ2�۸�
SET @iPriceSimpleCommodity3= 144; -- ��Ʒ3�۸�
SET @iPriceSimpleCommodity4= 15; -- ��Ʒ4�۸�
SET @iPriceCompositionCommodity5= (@iSubCommodityNO1 * @iSubCommodityPrice1 + @iSubCommodityNO2 * @iSubCommodityPrice2); -- ���5�۸�
SET @iPriceMutiplePackagingCommodity6= (@iMultiple * @iPriceSimpleCommodity4); -- ���װ6�۸�
SET @iWarehousingCommodityPrice1 = 2.2;--  �����Ʒ1�۸�
SET @iWarehousingCommodityPrice1_1 = 2.33;--  �����Ʒ1_1�۸�
SET @iWarehousingCommodityPrice2 = 2.53;--  �����Ʒ2�۸�
SET @iWarehousingCommodityPrice3 = 2.62;--  �����Ʒ3�۸�
SET @iWarehousingCommodityPrice4 = 3.56;--  �����Ʒ4�۸�
SET @iWarehousingCommodityNO1 = 40;--  �����Ʒ1�������
SET @iWarehousingCommodityNO1_1 = 10;--  �����Ʒ1_1�������
SET @iRetailtradeCommodityNO1 = (@iWarehousingCommodityNO1 + @iWarehousingCommodityNO1_1); -- ������Ʒ1����
SET @iRetailtradeCommodityNO2 = 50; -- ������Ʒ2����
SET @iRetailtradeCommodityNO2_1 = 5; -- �˻���Ʒ2����
SET @iRetailtradeCommodityNO3 = 10; -- ������Ʒ3����
SET @iRetailtradeCommodityNO3_1 = 5; -- �˻���Ʒ3����
SET @iWarehousingCommodityNO2 = (@iRetailtradeCommodityNO2 * @iSubCommodityNO1);--  �����Ʒ2�������
SET @iWarehousingCommodityNO3 = (@iRetailtradeCommodityNO2 * @iSubCommodityNO2);--  �����Ʒ3�������
SET @iWarehousingCommodityNO4 = ((@iRetailtradeCommodityNO3 - @iRetailtradeCommodityNO3_1) * @iMultiple);--  �����Ʒ4�������
SET @iNOSalable2 = (@iRetailtradeCommodityNO2 * @iSubCommodityNO1); -- �����Ʒ2��������
SET @iNOSalable3 = (@iRetailtradeCommodityNO2 * @iSubCommodityNO2); -- �����Ʒ3��������
SELECT @iWarehousingCommodityNO1,@iWarehousingCommodityNO1_1,@iWarehousingCommodityNO2,@iWarehousingCommodityNO3,@iWarehousingCommodityNO4;
SET @iAmount1 = (@iRetailtradeCommodityNO1 * @iPriceSimpleCommodity1); -- ���۵�1�ܽ��
SET @iAmount1_1 = @iAmount1; -- �˻���1�ܽ��
SET @iAmount2 = (@iRetailtradeCommodityNO2 * @iPriceSimpleCommodity2); -- ���۵�2�ܽ��
SET @iAmount2_1 = (@iRetailtradeCommodityNO2_1 * @iPriceSimpleCommodity2); -- �˻���2�ܽ��
SET @iAmount3 = (@iRetailtradeCommodityNO3 * @iPriceSimpleCommodity3); -- ���۵�3�ܽ��
SET @iAmount3_1 =(@iRetailtradeCommodityNO3_1 * @iPriceSimpleCommodity3) ; -- �˻���3�ܽ��
SET @saleDatetime = DATE_ADD(now(),INTERVAL 3000 DAY); -- ��������
-- ���뵥Ʒ1
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0/*F_Status*/, '�ɱȿ�1111', '��Ƭ', '��', 1, '��', 3, 1, 'SP', 1, 
@iPriceSimpleCommodity1-1,@iPriceSimpleCommodity1-2, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0/*F_Type*/, NULL, '2019-11-22 14:48:19', '2019-11-22 14:48:19', NULL, NULL, NULL, NULL);
SET @iCommodityID1 = last_insert_id();
-- ���뵥Ʒ2
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0/*F_Status*/, '�ɱȿ�2222', '��Ƭ', '��', 1, '��', 3, 1, 'SP', 1, 
@iPriceSimpleCommodity2-1,@iPriceSimpleCommodity2-2, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0/*F_Type*/, NULL, '2019-11-22 14:48:19', '2019-11-22 14:48:19', NULL, NULL, NULL, NULL);
SET @iCommodityID2 = last_insert_id();
-- ���뵥Ʒ3
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0/*F_Status*/, '�ɱȿ�3333', '��Ƭ', '��', 1, '��', 3, 1, 'SP', 1, 
@iPriceSimpleCommodity3-1,@iPriceSimpleCommodity3-2, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0/*F_Type*/, NULL, '2019-11-22 14:48:19', '2019-11-22 14:48:19', NULL, NULL, NULL, NULL);
SET @iCommodityID3 = last_insert_id();
-- ���뵥Ʒ4
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0/*F_Status*/, '�ɱȿ�4444', '��Ƭ', '��', 1, '��', 3, 1, 'SP', 1, 
@iPriceSimpleCommodity4-1,@iPriceSimpleCommodity4-2, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0/*F_Type*/, NULL, '2019-11-22 14:48:19', '2019-11-22 14:48:19', NULL, NULL, NULL, NULL);
SET @iCommodityID4 = last_insert_id();
-- ���������Ʒ5
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0/*F_Status*/, '�ɱȿˣ���ϣ�', '��Ƭ', '��', 1, '��', 3, 1, 'SP', 1, 
@iPriceCompositionCommodity5-1,@iPriceCompositionCommodity5-2, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 1/*F_Type*/, NULL, '2019-11-22 14:48:19', '2019-11-22 14:48:19', NULL, NULL, NULL, NULL);
SET @iCommodityID5 = last_insert_id();
-- ������װ��Ʒ6
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0/*F_Status*/, '�ɱȿˣ����װ��', '��Ƭ', '��', 1, '��', 3, 1, 'SP', 1, 
@iPriceMutiplePackagingCommodity6-1,@iPriceMutiplePackagingCommodity6-2, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, @iCommodityID4, @iMultiple, '1111111', 2/*F_Type*/, NULL, '2019-11-22 14:48:19', '2019-11-22 14:48:19', NULL, NULL, NULL, NULL);
SET @iCommodityID6 = last_insert_id();
-- ����Ʒ1
INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iCommodityID5,@iCommodityID2,@iSubCommodityNO1,@iSubCommodityPrice1, '2019-11-22 15:31:38', '2019-11-22 15:31:38');
-- ����Ʒ2
INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iCommodityID5,@iCommodityID3,@iSubCommodityNO2,@iSubCommodityPrice2, '2019-11-22 15:31:38', '2019-11-22 15:31:38');
-- ������ⵥ1
INSERT INTO t_warehousing (F_ShopID, F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (2, 1/*F_Status*/, 'RK201909050001', 1, 1, 4, NULL, '2017-08-06 01:01:01', 1, '2019-11-22 15:31:39');
SET @iWarehousingID1 = last_insert_id();
-- ������ⵥ2
INSERT INTO t_warehousing (F_ShopID, F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (2, 1/*F_Status*/, 'RK201909050002', 1, 1, 4, NULL, '2017-08-06 01:01:01', 1, '2019-11-22 15:31:39');
SET @iWarehousingID2 = last_insert_id();
-- ������ⵥ��Ʒ1
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID1, @iCommodityID1, @iWarehousingCommodityNO1, 1, '�ɱȿ�1111', 1, @iWarehousingCommodityPrice1, 2000, '2017-08-06 01:01:01', 36, '2020-08-06 01:01:01', '2019-11-22 15:31:39', '2019-11-22 15:31:39',@iWarehousingCommodityNO1);
-- ������ⵥ��Ʒ1_1
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID2, @iCommodityID1, @iWarehousingCommodityNO1_1, 1, '�ɱȿ�1111', 1, @iWarehousingCommodityPrice1_1, 2000, '2017-08-06 01:01:01', 36, '2020-08-06 01:01:01', '2019-11-22 15:31:39', '2019-11-22 15:31:39',@iWarehousingCommodityNO1_1);
-- ������ⵥ��Ʒ2
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID1, @iCommodityID2, @iWarehousingCommodityNO2, 1, '�ɱȿ�2222', 1, @iWarehousingCommodityPrice2, 2000, '2017-08-06 01:01:01', 36, '2020-08-06 01:01:01', '2019-11-22 15:31:39', '2019-11-22 15:31:39', @iNOSalable2);
-- ������ⵥ��Ʒ3
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID1, @iCommodityID3, @iWarehousingCommodityNO3, 1, '�ɱȿ�3333', 1, @iWarehousingCommodityPrice3, 2000, '2017-08-06 01:01:01', 36, '2020-08-06 01:01:01', '2019-11-22 15:31:39', '2019-11-22 15:31:39', @iNOSalable3);
-- ������ⵥ��Ʒ4
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID1, @iCommodityID4, @iWarehousingCommodityNO4, 1, '�ɱȿ�4444', 1, @iWarehousingCommodityPrice4, 2000, '2017-08-06 01:01:01', 36, '2020-08-06 01:01:01', '2019-11-22 15:31:39', '2019-11-22 15:31:39', 0);
-- �������۵�1������Ʒ1(���)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019110601010100011201', 1, 1, 'url=ashasoadigmnalskd',DATE_ADD(@saleDatetime,INTERVAL 1 minute), 2, 4, '0', 1, '........', -1/*F_SourceID*/, '2019-11-22 14:48:21', @iAmount1, 0, 0, @iAmount1, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iTradeID1 = last_insert_id();
-- �������۵���Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iTradeID1,@iCommodityID1, '�ɱȿ�1111', 1, @iRetailtradeCommodityNO1, @iPriceSimpleCommodity1,0, @iPriceSimpleCommodity1, 300, @iPriceSimpleCommodity1);
SET @iRetailtradeCommodityID1 = last_insert_id();
-- ����������Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailtradeCommodityID1, @iCommodityID1,@iWarehousingCommodityNO1, @iWarehousingID1);
--
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailtradeCommodityID1, @iCommodityID1,@iWarehousingCommodityNO1_1, @iWarehousingID2);
-- �����˻���1��(����)�˵�Ʒ1ȫ��(���)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019110601010100011201_1', 1, 4, 'url=ashasoadigmnalskd',DATE_ADD(@saleDatetime,INTERVAL 1 minute), 2, 4, '0', 1, '........',@iTradeID1, '2019-11-22 14:48:21', @iAmount1_1, 0, 0, @iAmount1_1, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iTradeID1_1 = last_insert_id();
-- �����˻�����Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iTradeID1_1,@iCommodityID1, '�ɱȿ�1111', 1, @iRetailtradeCommodityNO1, @iPriceSimpleCommodity1, 0, @iPriceSimpleCommodity1, 300, @iPriceSimpleCommodity1);
SET @iRetailtradeCommodityID1_1 = last_insert_id();
-- �����˻�ȥ���
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailtradeCommodityID1_1, @iCommodityID1,@iWarehousingCommodityNO1, @iWarehousingID1);
--
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailtradeCommodityID1_1, @iCommodityID1,@iWarehousingCommodityNO1_1, @iWarehousingID2);
-- �������۵�2���������Ʒ
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019110601010100011202', 1, 2, 'url=ashasoadigmnalskd',DATE_ADD(@saleDatetime,INTERVAL 1 minute), 2, 4, '0', 1, '........', -1/*F_SourceID*/, '2019-11-22 14:48:21', @iAmount2, 0, 0, @iAmount2, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iTradeID2 = last_insert_id();
-- �������۵���Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iTradeID2,@iCommodityID5, '�ɱȿˣ���ϣ�', 1, @iRetailtradeCommodityNO2, @iPriceCompositionCommodity5, (@iRetailtradeCommodityNO2-@iRetailtradeCommodityNO2_1), @iPriceCompositionCommodity5, 300, @iPriceCompositionCommodity5);
SET @iRetailtradeCommodityID2 = last_insert_id();
-- ����������Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailtradeCommodityID2, @iCommodityID2,(@iRetailtradeCommodityNO2 * @iSubCommodityNO1), @iWarehousingID1);
--
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailtradeCommodityID2, @iCommodityID3,(@iRetailtradeCommodityNO2 * @iSubCommodityNO2), @iWarehousingID1);
-- �����˻���2��(����)�������Ʒ(����)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019110601010100011202_1', 1,5, 'url=ashasoadigmnalskd',DATE_ADD(@saleDatetime,INTERVAL 2 DAY), 2, 4, '0', 1, '........',@iTradeID2, '2019-11-22 14:48:21', @iAmount2_1, 0, 0, @iAmount2_1, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iTradeID2_1 = last_insert_id();
-- �����˻�����Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iTradeID2_1,@iCommodityID5, '�ɱȿˣ���ϣ�', 1, @iRetailtradeCommodityNO2_1, @iPriceCompositionCommodity5, 0, @iPriceCompositionCommodity5, 300, @iPriceCompositionCommodity5);
SET @iRetailtradeCommodityID2_1 = last_insert_id();
-- �����˻�ȥ���
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailtradeCommodityID2_1, @iCommodityID2,(@iRetailtradeCommodityNO2_1 * @iSubCommodityNO1), @iWarehousingID1);
--
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailtradeCommodityID2_1, @iCommodityID3,(@iRetailtradeCommodityNO2_1 * @iSubCommodityNO2), @iWarehousingID1);
-- �������۵�3�������װ��Ʒ
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019110601010100011203', 1, 3, 'url=ashasoadigmnalskd',DATE_ADD(@saleDatetime,INTERVAL 1 minute), 2, 4, '0', 1, '........', -1/*F_SourceID*/, '2019-11-22 14:48:21', @iAmount3, 0, 0, @iAmount3, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iTradeID3 = last_insert_id();
-- �������۵���Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iTradeID3,@iCommodityID6, '�ɱȿˣ����װ��', 1, @iRetailtradeCommodityNO3, @iPriceMutiplePackagingCommodity6,  (@iRetailtradeCommodityNO3-@iRetailtradeCommodityNO3_1), @iPriceMutiplePackagingCommodity6, 300, @iPriceMutiplePackagingCommodity6);
SET @iRetailtradeCommodityID3 = last_insert_id();
-- ����������Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailtradeCommodityID3, @iCommodityID4,(@iRetailtradeCommodityNO3 * @iMultiple), @iWarehousingID1);
-- �����˻���3���˶��װ��Ʒ�����첿�֣�
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019110601010100011203_1', 1,6, 'url=ashasoadigmnalskd',DATE_ADD(@saleDatetime,INTERVAL 1 minute), 2, 4, '0', 1, '........',@iTradeID3, '2019-11-22 14:48:21', @iAmount3_1, 0, 0, @iAmount3_1, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iTradeID3_1 = last_insert_id();
-- �����˻�����Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iTradeID3_1,@iCommodityID6, '�ɱȿˣ����װ��', 1, @iRetailtradeCommodityNO3_1, @iPriceMutiplePackagingCommodity6, 0, @iPriceMutiplePackagingCommodity6, 300, @iPriceMutiplePackagingCommodity6);
SET @iRetailtradeCommodityID3_1 = last_insert_id();
-- �����˻�ȥ���
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailtradeCommodityID3_1, @iCommodityID4,(@iRetailtradeCommodityNO3_1 * @iMultiple), @iWarehousingID1);
SET @dSaleDatetime = DATE_FORMAT(@saleDatetime,'%Y/%m/%d');-- ��������

SET @iTotalNO = (SELECT count(F_ID) FROM t_retailtrade WHERE DATE_FORMAT(F_SaleDatetime,'%Y/%m/%d') = @dSaleDatetime AND F_SourceID = -1);

SET @iPricePurchase = (@iWarehousingCommodityNO2 * @iWarehousingCommodityPrice2 + @iWarehousingCommodityNO3 * @iWarehousingCommodityPrice3
                       + @iWarehousingCommodityNO4 * @iWarehousingCommodityPrice4);-- һ��������Ʒ���ܽ�����
                    
SET @iTotalAmount = (@iAmount2 + @iAmount3-@iAmount3_1);-- �������۶�
SET @iTopSaleCommodityAmount = ((@iRetailtradeCommodityNO2 - @iRetailtradeCommodityNO2_1)  * @iPriceCompositionCommodity5);-- ����������۶�
SET @iTopSaleCommodityID = @iCommodityID5;
SET @iTopSaleCommodityNO = (@iRetailtradeCommodityNO2 - @iRetailtradeCommodityNO2_1);
SET @iShopID = 2;
SET @sErrorMsg = '';
SET @deleteOldData = 0;
-- SELECT @dSaleDatetime,@iTotalNO,@iPricePurchase,@iTotalAmount,@iTopSaleCommodityAmount,@iTopSaleCommodityID,@iTopSaleCommodityNO;
CALL SP_RetailTradeDailyReport_Create(@iErrorCode, @sErrorMsg, @iShopID, @dSaleDatetime, @deleteOldData);
SELECT 1 FROM t_retailtradedailyreportsummary WHERE F_Datetime = @dSaleDatetime
													AND F_TotalNO = @iTotalNO  
													AND F_PricePurchase = @iPricePurchase
													AND F_TotalAmount = @iTotalAmount 
													AND F_TopSaleCommodityAmount = @iTopSaleCommodityAmount
													AND F_TopSaleCommodityID = @iTopSaleCommodityID
													AND F_TopSaleCommodityNO = @iTopSaleCommodityNO;
SET @found_row1 = found_rows();
SELECT 1 FROM t_retailtradedailyreportbycommodity WHERE F_Datetime = @dSaleDatetime;
SET @found_row2 = found_rows();

SELECT IF(@iErrorCode = 0 AND @found_row1 <> 0 AND @found_row2 <> 0,'���Գɹ�','����ʧ��') AS 'Test Case1 Result';
DELETE FROM t_retailtradedailyreportsummary WHERE F_Datetime = @dSaleDatetime;
DELETE FROM t_retailtradedailyreportbycommodity WHERE F_Datetime = @dSaleDatetime;
DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID IN (@iRetailtradeCommodityID1,@iRetailtradeCommodityID2,@iRetailtradeCommodityID3);
DELETE FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID IN (@iRetailtradeCommodityID1_1,@iRetailtradeCommodityID2_1,@iRetailtradeCommodityID3_1);
DELETE FROM t_retailtradecommodity WHERE F_TradeID IN (@iTradeID1,@iTradeID1_1,@iTradeID2,@iTradeID2_1,@iTradeID3,@iTradeID3_1);
DELETE FROM t_retailtrade WHERE F_ID IN (@iTradeID1,@iTradeID1_1,@iTradeID2,@iTradeID2_1,@iTradeID3,@iTradeID3_1);
DELETE FROM t_warehousingcommodity WHERE F_WarehousingID IN(@iWarehousingID1,@iWarehousingID2);
DELETE FROM t_warehousing WHERE F_ID IN(@iWarehousingID1,@iWarehousingID2);
DELETE FROM t_subcommodity WHERE F_CommodityID IN(@iCommodityID5);
DELETE FROM t_commodity WHERE F_ID IN(@iCommodityID1,@iCommodityID2,@iCommodityID3,@iCommodityID4,@iCommodityID5,@iCommodityID6);

SELECT '-------------------- Case4���������δ�����Ʒ -------------------------' AS 'Case4';
SET @iPriceRetail = 19; -- ��Ʒ�۸�
SET @iRetailtradeCommodityNO = 100; -- ������Ʒ����
SET @iAmount = (@iRetailtradeCommodityNO * @iPriceRetail); -- ���۵����
SET @saleDatetime = DATE_ADD(now(),INTERVAL 3000 DAY); -- ��������
-- ������Ʒ
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0/*F_Status*/, '�ɱȿ�1111', '��Ƭ', '��', 1, '��', 3, 1, 'SP', 1, 
@iPriceRetail-1,@iPriceRetail-2, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0/*F_Type*/, NULL, '2019-11-22 14:48:19', '2019-11-22 14:48:19', NULL, NULL, NULL, NULL);
SET @iCommodityID = last_insert_id();
-- �������۵�
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019080601010100011201', 1, 1, 'url=ashasoadigmnalskd',DATE_ADD(@saleDatetime,INTERVAL 1 minute), 2, 4, '0', 1, '........', -1/*F_SourceID*/, '2019-11-22 14:48:21', @iAmount, 0, 0, @iAmount, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iTradeID = last_insert_id();
-- �������۵���Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iTradeID, 1, '�ɱȿ�1111', 1, @iRetailtradeCommodityNO, @iPriceRetail, @iRetailtradeCommodityNO, @iPriceRetail, 300, @iPriceRetail);
SET @iRetailtradeCommodityID = last_insert_id();
-- ����������Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailtradeCommodityID, @iCommodityID,@iRetailtradeCommodityNO, NULL);

SET @dSaleDatetime = DATE_FORMAT(@saleDatetime,'%Y-%m-%d');
SET @iShopID = 2;
SET @sErrorMsg = '';
SET @deleteOldData = 0;
CALL SP_RetailTradeDailyReport_Create(@iErrorCode, @sErrorMsg, @iShopID, @dSaleDatetime, @deleteOldData);
SELECT 1 FROM t_retailtradedailyreportsummary WHERE F_Datetime = @dSaleDatetime;
SET @found_row1 = found_rows();
SELECT 1 FROM t_retailtradedailyreportbycommodity WHERE F_Datetime = @dSaleDatetime;
SET @found_row2 = found_rows();
--
SELECT IF(@iErrorCode = 0 AND @found_row1 <> 0 AND @found_row2 <> 0,'���Գɹ�','����ʧ��') AS 'Test Case4 Result';
DELETE FROM t_retailtradedailyreportsummary WHERE F_Datetime = @dSaleDatetime;
DELETE FROM t_retailtradedailyreportbycommodity WHERE F_Datetime = @dSaleDatetime;
DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @iRetailtradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @iTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
--	-- ... �����ٸ���Java��Ĳ��Դ������Ӳ���������


SELECT '-------------------- Case5������û���κ����ۼ�¼���д����ձ��� -------------------------' AS 'Case4';
SET @dSaleDatetime = '2022-09-05';
SET @iShopID = 2;
SET @sErrorMsg = '';
SET @iErrorCode = 0;
SET @deleteOldData = 0;
CALL SP_RetailTradeDailyReport_Create(@iErrorCode, @sErrorMsg, @iShopID, @dSaleDatetime, @deleteOldData);

SELECT 1 FROM t_retailtradedailyreportsummary WHERE F_Datetime = @dSaleDatetime;
SET @found_row1 = found_rows();

DELETE FROM t_retailtradedailyreportsummary WHERE F_Datetime = @dSaleDatetime;