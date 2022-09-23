SELECT '++++++++++++++++++ SPD_Returnretailtradecommoditydestination_CheckNO.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:�������� -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Returnretailtradecommoditydestination_CheckNO (@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';


SELECT '-------------------- Case2:�˻�ȥ����F_NO����������ԴF_NO -------------------------' AS 'Case2';
-- ������Ʒ
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, F_LatestPricePurchase, F_PriceRetail, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_NO, F_Type, F_NOStart, F_PurchasingPriceStart, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4, F_CurrentWarehousingID)
VALUES (0/*F_Status*/, '�ɱȿ���Ƭhh', '��Ƭ', '��', 1, '��', 3, 1, 'SP', 1, 8, 12, 11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 10/*F_NO*/, 0/*F_Type*/, -1, -1, NULL, '2019-10-29 16:28:25', '2019-10-29 16:28:25', NULL, NULL, NULL, NULL, NULL);
SET @iCommodityID = LAST_INSERT_ID();
-- ������ⵥ
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (1/*F_Status*/, 'RK201909050001', 1, 1, 4, NULL, '2017-08-06 01:01:01', 1, '2019-10-29 16:28:27');
SET @iWarehousingID = LAST_INSERT_ID();
-- ������ⵥ��Ʒ
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID, @iCommodityID, 10/*F_NO*/, 1, '�ɱȿ���Ƭhh', 1, 10, 2000, '2017-08-06 01:01:01', 36, '2020-08-06 01:01:01', '2019-10-29 16:28:27', '2019-10-29 16:28:27', 50);
-- �������۵�
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (1, 'LS200380601010100011234', 1, 1, 'url=ashasoadigmnalskd', '2017-08-16', 2, 4, '0', 1, '........', -1, '2019-10-29 16:28:28', 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iTradeID, @iCommodityID, '�ɱȿ���Ƭhh', 1, 10/*F_NO*/, 321, 500, 300, 300, NULL);
SET @iTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iTradeCommodityID, @iCommodityID, 10/*F_NO*/, @iWarehousingID);
-- �����˻���
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (4, 'LS200380601010100011234_1', 1, 1, 'url=ashasoadigmnalskd', '2017-08-26', 2, 4, '0', 1, '........', @iTradeID, '2019-10-29 16:28:28', 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iReturnTradeID = LAST_INSERT_ID();
-- �����˻�����Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnTradeID, @iCommodityID, '�ɱȿ���Ƭhh', 1, 50/*F_NO*/, 321, 500, 300, 300, NULL);
SET @iReturnTradeCommodityID = LAST_INSERT_ID();
-- �����˻�ȥ���
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iReturnTradeCommodityID, @iCommodityID, 50/*F_NO*/, @iWarehousingID);
SET @iReturnretailtradecommoditydestinationID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Returnretailtradecommoditydestination_CheckNO (@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iReturnretailtradecommoditydestinationID, '�˻�ȥ����F_NO����С�ڻ����������ԴF_NO') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
DELETE FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID = @iReturnTradeCommodityID;
DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @iTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID IN (@iTradeCommodityID,@iReturnTradeCommodityID);
DELETE FROM t_retailtrade WHERE F_ID IN (@iTradeID,@iReturnTradeID);
DELETE FROM t_warehousingcommodity WHERE F_WarehousingID = @iWarehousingID;
DELETE FROM t_warehousing WHERE F_ID = @iWarehousingID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
--
SELECT '-------------------- Case3:����˻�ȥ����ͨ��Ʒ���������ڶ�Ӧ���˻�����Ʒ������-------------------------' AS 'Case3';
-- ������Ʒ
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, F_LatestPricePurchase, F_PriceRetail, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_NO, F_Type, F_NOStart, F_PurchasingPriceStart, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4, F_CurrentWarehousingID)
VALUES (0/*F_Status*/, '�ɱȿ���Ƭhh2', '��Ƭ', '��', 1, '��', 3, 1, 'SP', 1, 8, 12, 11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 10/*F_NO*/, 0/*F_Type*/, -1, -1, NULL, '2019-10-29 16:28:25', '2019-10-29 16:28:25', NULL, NULL, NULL, NULL, NULL);
SET @iCommodityID = LAST_INSERT_ID();
-- ������ⵥ
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (1/*F_Status*/, 'RK201909050001', 1, 1, 4, NULL, '2017-08-06 01:01:01', 1, '2019-10-29 16:28:27');
SET @iWarehousingID = LAST_INSERT_ID();
-- ������ⵥ��Ʒ
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID, @iCommodityID, 10/*F_NO*/, 1, '�ɱȿ���Ƭhh2', 1, 10, 2000, '2017-08-06 01:01:01', 36, '2020-08-06 01:01:01', '2019-10-29 16:28:27', '2019-10-29 16:28:27', 1);
-- �������۵�
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (1, 'LS200380601010100011234', 1, 1, 'url=ashasoadigmnalskd', '2017-08-16', 2, 4, '0', 1, '........', -1, '2019-10-29 16:28:28', 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iTradeID, @iCommodityID, '�ɱȿ���Ƭhh2', 1, 10/*F_NO*/, 321, 500, 300, 300, NULL);
SET @iTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iTradeCommodityID, @iCommodityID, 10/*F_NO*/, @iWarehousingID);
-- �����˻���
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (4, 'LS200380601010100011234_1', 1, 1, 'url=ashasoadigmnalskd', '2017-08-26', 2, 4, '0', 1, '........', @iTradeID, '2019-10-29 16:28:28', 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iReturnTradeID = LAST_INSERT_ID();
-- �����˻�����Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnTradeID, @iCommodityID, '�ɱȿ���Ƭhh2', 1, 10/*F_NO*/, 321, 500, 300, 300, NULL);
SET @iReturnTradeCommodityID = LAST_INSERT_ID();
-- �����˻�ȥ���
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iReturnTradeCommodityID, @iCommodityID, 1/*F_NO*/, @iWarehousingID);
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sID = (SELECT group_concat(F_ID) FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID = @iReturnTradeCommodityID); 
-- 
CALL SPD_Returnretailtradecommoditydestination_CheckNO (@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @sID, 'ȥ���,��ͨ��ƷF_NO���Ӧ���˻���ƷF_NO�����') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';
DELETE FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID = @iReturnTradeCommodityID;
DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @iTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID IN (@iTradeCommodityID,@iReturnTradeCommodityID);
DELETE FROM t_retailtrade WHERE F_ID IN (@iTradeID,@iReturnTradeID);
DELETE FROM t_warehousingcommodity WHERE F_WarehousingID = @iWarehousingID;
DELETE FROM t_warehousing WHERE F_ID = @iWarehousingID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- Case4:����˻�ȥ�������Ʒ���������ڶ�Ӧ���˻�����Ʒ*����������-------------------------' AS 'Case4';
-- ������Ʒ1
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, F_LatestPricePurchase, F_PriceRetail, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_NO, F_Type, F_NOStart, F_PurchasingPriceStart, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4, F_CurrentWarehousingID)
VALUES (0/*F_Status*/, '�ɱȿ���Ƭhh1', '��Ƭ', '��', 1, '��', 3, 1, 'SP', 1, 8, 12, 11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 10/*F_NO*/, 0/*F_Type*/, -1, -1, NULL, '2019-10-29 16:28:25', '2019-10-29 16:28:25', NULL, NULL, NULL, NULL, NULL);
SET @iCommodityID1 = LAST_INSERT_ID();
-- ������Ʒ2
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, F_LatestPricePurchase, F_PriceRetail, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_NO, F_Type, F_NOStart, F_PurchasingPriceStart, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4, F_CurrentWarehousingID)
VALUES (0/*F_Status*/, '�ɱȿ���Ƭhh2', '��Ƭ', '��', 1, '��', 3, 1, 'SP', 1, 8, 12, 11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 10/*F_NO*/, 0/*F_Type*/, -1, -1, NULL, '2019-10-29 16:28:25', '2019-10-29 16:28:25', NULL, NULL, NULL, NULL, NULL);
SET @iCommodityID2 = LAST_INSERT_ID();
-- �����Ʒ
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, F_LatestPricePurchase, F_PriceRetail, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_NO, F_Type, F_NOStart, F_PurchasingPriceStart, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4, F_CurrentWarehousingID)
VALUES (0/*F_Status*/, '�ɱȿ���Ƭ����', '���', '��', 1, '��', 3, 1, 'SP', 1, 8, 12, 11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 10/*F_NO*/, 1/*F_Type*/, -1, -1, NULL, '2019-10-29 16:28:25', '2019-10-29 16:28:25', NULL, NULL, NULL, NULL, NULL);
SET @iCommodityID = LAST_INSERT_ID();
-- ����Ʒ1
INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iCommodityID, @iCommodityID1, 5/*F_SubCommodityNO*/, 8, '2019-10-30 09:27:23', '2019-10-30 09:27:23');
-- ����Ʒ2
INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iCommodityID, @iCommodityID2, 1/*F_SubCommodityNO*/, 8, '2019-10-30 09:27:23', '2019-10-30 09:27:23');
-- ������ⵥ
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (1/*F_Status*/, 'RK201909050001', 1, 1, 4, NULL, '2017-08-06 01:01:01', 1, '2019-10-29 16:28:27');
SET @iWarehousingID = LAST_INSERT_ID();
-- ������ⵥ��Ʒ1
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID, @iCommodityID1, 10/*F_NO*/, 1, '�ɱȿ���Ƭhh1', 1, 10, 2000, '2017-08-06 01:01:01', 36, '2020-08-06 01:01:01', '2019-10-29 16:28:27', '2019-10-29 16:28:27', 0);
-- ������ⵥ��Ʒ2
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID, @iCommodityID2, 10/*F_NO*/, 1, '�ɱȿ���Ƭhh2', 1, 10, 2000, '2017-08-06 01:01:01', 36, '2020-08-06 01:01:01', '2019-10-29 16:28:27', '2019-10-29 16:28:27', 10);
-- �������۵�
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (1, 'LS200380601010100011234', 1, 1, 'url=ashasoadigmnalskd', '2017-08-16', 2, 4, '0', 1, '........', -1, '2019-10-29 16:28:28', 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iTradeID, @iCommodityID, '�ɱȿ���Ƭ����', 1, 10/*F_NO*/, 321, 500, 300, 300, NULL);
SET @iTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iTradeCommodityID, @iCommodityID1, 50/*F_NO*/, @iWarehousingID);
--
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iTradeCommodityID, @iCommodityID2, 10/*F_NO*/, @iWarehousingID);
-- �����˻���
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (4, 'LS200380601010100011234_1', 1, 1, 'url=ashasoadigmnalskd', '2017-08-26', 2, 4, '0', 1, '........', @iTradeID, '2019-10-29 16:28:28', 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iReturnTradeID = LAST_INSERT_ID();
-- �����˻�����Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnTradeID, @iCommodityID, '�ɱȿ���Ƭ����', 1, 10/*F_NO*/, 321, 500, 300, 300, NULL);
SET @iReturnTradeCommodityID = LAST_INSERT_ID();
-- �����˻�ȥ���
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iReturnTradeCommodityID, @iCommodityID1, 40/*F_NO*/, @iWarehousingID);
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sID = (SELECT group_concat(F_ID) FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID = @iReturnTradeCommodityID); 
-- 
CALL SPD_Returnretailtradecommoditydestination_CheckNO (@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @sID, '���˻�ȥ����F_NO����Ӧ���˻�����Ʒ��F_NO�����') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';
DELETE FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID = @iReturnTradeCommodityID;
DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @iTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID IN (@iTradeCommodityID,@iReturnTradeCommodityID);
DELETE FROM t_retailtrade WHERE F_ID IN (@iTradeID,@iReturnTradeID);
DELETE FROM t_warehousingcommodity WHERE F_WarehousingID = @iWarehousingID;
DELETE FROM t_warehousing WHERE F_ID = @iWarehousingID;
DELETE FROM t_subcommodity WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_commodity WHERE F_ID IN (@iCommodityID,@iCommodityID1,@iCommodityID2);
--
SELECT '-------------------- Case5:����˻�ȥ����װ��Ʒ���������ڶ�Ӧ���˻�����Ʒ������*����-------------------------' AS 'Case5';
-- ������Ʒ
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, F_LatestPricePurchase, F_PriceRetail, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_NO, F_Type, F_NOStart, F_PurchasingPriceStart, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4, F_CurrentWarehousingID)
VALUES (0/*F_Status*/, '�ɱȿ���Ƭhh2', '��Ƭ', '��', 1, '��', 3, 1, 'SP', 1, 8, 12, 11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', -17/*F_NO*/, 0/*F_Type*/, -1, -1, NULL, '2019-10-29 16:28:25', '2019-10-29 16:28:25', NULL, NULL, NULL, NULL, NULL);
SET @iCommodityID1 = LAST_INSERT_ID();
-- ������װ��Ʒ
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, F_LatestPricePurchase, F_PriceRetail, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_NO, F_Type, F_NOStart, F_PurchasingPriceStart, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4, F_CurrentWarehousingID)
VALUES (0/*F_Status*/, '�ɱȿ���Ƭ���װ', '���װ', '��', 1, '��', 3, 1, 'SP', 1, 8, 12, 11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, @iCommodityID1, 3/*F_RefCommodityMultiple*/, '1111111', 10/*F_NO*/, 2/*F_Type*/, -1, -1, NULL, '2019-10-29 16:28:25', '2019-10-29 16:28:25', NULL, NULL, NULL, NULL, NULL);
SET @iCommodityID = LAST_INSERT_ID();
-- ������ⵥ
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (1/*F_Status*/, 'RK201909050001', 1, 1, 4, NULL, '2017-08-06 01:01:01', 1, '2019-10-29 16:28:27');
SET @iWarehousingID = LAST_INSERT_ID();
-- ������ⵥ��Ʒ
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID, @iCommodityID1, 10/*F_NO*/, 1, '�ɱȿ���Ƭhh2', 1, 10, 2000, '2017-08-06 01:01:01', 36, '2020-08-06 01:01:01', '2019-10-29 16:28:27', '2019-10-29 16:28:27', -6);
-- �������۵�
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (1, 'LS200380601010100011234', 1, 1, 'url=ashasoadigmnalskd', '2017-08-16', 2, 4, '0', 1, '........', -1, '2019-10-29 16:28:28', 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iTradeID, @iCommodityID, '�ɱȿ���Ƭ���װ', 1, 10/*F_NO*/, 321, 500, 300, 300, NULL);
SET @iTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iTradeCommodityID, @iCommodityID1, 30/*F_NO*/, @iWarehousingID);
-- �����˻���
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (4, 'LS200380601010100011234_1', 1, 1, 'url=ashasoadigmnalskd', '2017-08-26', 2, 4, '0', 1, '........', @iTradeID, '2019-10-29 16:28:28', 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iReturnTradeID = LAST_INSERT_ID();
-- �����˻�����Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnTradeID, @iCommodityID, '�ɱȿ���Ƭ���װ', 1, 5/*F_NO*/, 321, 500, 300, 300, NULL);
SET @iReturnTradeCommodityID = LAST_INSERT_ID();
-- �����˻�ȥ���
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iReturnTradeCommodityID, @iCommodityID1, 14/*F_NO*/, @iWarehousingID);
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sID = (SELECT group_concat(F_ID) FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID = @iReturnTradeCommodityID); 
-- 
CALL SPD_Returnretailtradecommoditydestination_CheckNO (@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @sID, '���˻�ȥ���F_NO����Ӧ���˻�����Ʒ��F_NO�����') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';
DELETE FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID = @iReturnTradeCommodityID;
DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @iTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID IN (@iTradeCommodityID,@iReturnTradeCommodityID);
DELETE FROM t_retailtrade WHERE F_ID IN (@iTradeID,@iReturnTradeID);
DELETE FROM t_warehousingcommodity WHERE F_WarehousingID = @iWarehousingID;
DELETE FROM t_warehousing WHERE F_ID = @iWarehousingID;
DELETE FROM t_commodity WHERE F_ID IN (@iCommodityID,@iCommodityID1);
--
SELECT '-------------------- Case6:����˻�ȥ�������Ʒ���������ڶ�Ӧ���˻�����Ʒ������-------------------------' AS 'Case6';
-- �����������Ʒ

INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, F_LatestPricePurchase, F_PriceRetail, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_NO, F_Type, F_NOStart, F_PurchasingPriceStart, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4, F_CurrentWarehousingID)
VALUES (0/*F_Status*/, '������Ʒabc', '������Ʒ1', 'ǧ��', 7, NULL, 1, 47, 'FWSP6', 1, 0, 10, 9.8, 9.5, 0, 1, NULL, 365, 3, '2000-01-01 09:00:00', 10, 0, 0, '0', 0/*F_NO*/, 3/*F_Type*/, -1, -1, NULL, '2000-01-01 09:00:00', '2000-01-01 09:00:00', NULL, NULL, NULL, NULL, NULL);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (1, 'LS200380601010100011234', 1, 1, 'url=ashasoadigmnalskd', '2017-08-16', 2, 4, '0', 1, '........', -1, '2019-10-29 16:28:28', 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iTradeID, @iCommodityID, '������Ʒabc', 1, 10/*F_NO*/, 321, 500, 300, 300, NULL);
SET @iTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iTradeCommodityID, @iCommodityID, 10/*F_NO*/, NULL);
-- �����˻���
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (4, 'LS200380601010100011234_1', 1, 1, 'url=ashasoadigmnalskd', '2017-08-26', 2, 4, '0', 1, '........', @iTradeID, '2019-10-29 16:28:28', 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iReturnTradeID = LAST_INSERT_ID();
-- �����˻�����Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnTradeID, @iCommodityID, '������Ʒabc', 1, 10/*F_NO*/, 321, 500, 300, 300, NULL);
SET @iReturnTradeCommodityID = LAST_INSERT_ID();
-- �����˻�ȥ���
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iReturnTradeCommodityID, @iCommodityID, 2/*F_NO*/, NULL);
SET @iReturnretailtradecommoditydestinationID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Returnretailtradecommoditydestination_CheckNO (@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iReturnretailtradecommoditydestinationID, '���˻�ȥ���F_NO����Ӧ���˻�����Ʒ��F_NO�����') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case6 Testing Result';
DELETE FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID = @iReturnTradeCommodityID;
DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @iTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID IN (@iTradeCommodityID,@iReturnTradeCommodityID);
DELETE FROM t_retailtrade WHERE F_ID IN (@iTradeID,@iReturnTradeID);
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
--
SELECT '-------------------- Case7:����˻�ȥ������Ʒ���Ͳ���ȷ-------------------------' AS 'Case7';
-- �������Ͳ���0~3����Ʒ
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, F_LatestPricePurchase, F_PriceRetail, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_NO, F_Type, F_NOStart, F_PurchasingPriceStart, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4, F_CurrentWarehousingID)
VALUES (0/*F_Status*/, '������Ʒabc', '������Ʒ1', 'ǧ��', 7, NULL, 1, 47, 'FWSP6', 1, 0, 10, 9.8, 9.5, 0, 1, NULL, 365, 3, '2000-01-01 09:00:00', 10, 0, 0, '0', 0/*F_NO*/, 5/*F_Type*/, -1, -1, NULL, '2000-01-01 09:00:00', '2000-01-01 09:00:00', NULL, NULL, NULL, NULL, NULL);
SET @iCommodityID = LAST_INSERT_ID();
-- �������۵�
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (1, 'LS200380601010100011234', 1, 1, 'url=ashasoadigmnalskd', '2017-08-16', 2, 4, '0', 1, '........', -1, '2019-10-29 16:28:28', 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iTradeID, @iCommodityID, '������Ʒabc', 1, 10/*F_NO*/, 321, 500, 300, 300, NULL);
SET @iTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iTradeCommodityID, @iCommodityID, 10/*F_NO*/, NULL);
-- �����˻���
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (4, 'LS200380601010100011234_1', 1, 1, 'url=ashasoadigmnalskd', '2017-08-26', 2, 4, '0', 1, '........', @iTradeID, '2019-10-29 16:28:28', 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iReturnTradeID = LAST_INSERT_ID();
-- �����˻�����Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnTradeID, @iCommodityID, '������Ʒabc', 1, 10/*F_NO*/, 321, 500, 300, 300, NULL);
SET @iReturnTradeCommodityID = LAST_INSERT_ID();
-- �����˻�ȥ���
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iReturnTradeCommodityID, @iCommodityID, 1/*F_NO*/, NULL);
SET @iReturnretailtradecommoditydestinationID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Returnretailtradecommoditydestination_CheckNO (@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iReturnretailtradecommoditydestinationID, '���˻�ȥ������ƷID��Ӧ����Ʒ����ֻ����0��3����Ʒ') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case7 Testing Result';
DELETE FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID = @iReturnTradeCommodityID;
DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @iTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID IN (@iTradeCommodityID,@iReturnTradeCommodityID);
DELETE FROM t_retailtrade WHERE F_ID IN (@iTradeID,@iReturnTradeID);
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- Case8:(���)����˻�ȥ����Ʒ���������ڶ�Ӧ���˻�����Ʒ������-------------------------' AS 'Case8';
-- ������Ʒ
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, F_LatestPricePurchase, F_PriceRetail, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_NO, F_Type, F_NOStart, F_PurchasingPriceStart, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4, F_CurrentWarehousingID)
VALUES (0/*F_Status*/, '�ɱȿ���Ƭhh2', '��Ƭ', '��', 1, '��', 3, 1, 'SP', 1, 8, 12, 11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 10/*F_NO*/, 0/*F_Type*/, -1, -1, NULL, '2019-10-29 16:28:25', '2019-10-29 16:28:25', NULL, NULL, NULL, NULL, NULL);
SET @iCommodityID = LAST_INSERT_ID();
-- ������ⵥ1
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (1/*F_Status*/, 'RK201909050001', 1, 1, 4, NULL, '2017-08-06 01:01:01', 1, '2019-10-29 16:28:27');
SET @iWarehousingID = LAST_INSERT_ID();
-- ������ⵥ2
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (1/*F_Status*/, 'RK201909050001', 1, 1, 4, NULL, '2017-08-06 01:01:01', 1, '2019-10-29 16:28:27');
SET @iWarehousingID2 = LAST_INSERT_ID();
-- ������ⵥ��Ʒ1
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID, @iCommodityID, 10/*F_NO*/, 1, '�ɱȿ���Ƭhh2', 1, 10, 2000, '2017-08-06 01:01:01', 36, '2020-08-06 01:01:01', '2019-10-29 16:28:27', '2019-10-29 16:28:27', 1);
-- ������ⵥ��Ʒ2
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID2, @iCommodityID, 10/*F_NO*/, 1, '�ɱȿ���Ƭhh2', 1, 10, 2000, '2017-08-06 01:01:01', 36, '2020-08-06 01:01:01', '2019-10-29 16:28:27', '2019-10-29 16:28:27', 1);
-- �������۵�
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (1, 'LS200380601010100011234', 1, 1, 'url=ashasoadigmnalskd', '2017-08-16', 2, 4, '0', 1, '........', -1, '2019-10-29 16:28:28', 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iTradeID = LAST_INSERT_ID();
-- �������۵���Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iTradeID, @iCommodityID, '�ɱȿ���Ƭhh2', 1, 20/*F_NO*/, 321, 500, 300, 300, NULL);
SET @iTradeCommodityID = LAST_INSERT_ID();
-- �������۵���Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iTradeCommodityID, @iCommodityID, 10/*F_NO*/, @iWarehousingID);
-- 
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iTradeCommodityID, @iCommodityID, 10/*F_NO*/, @iWarehousingID2);
-- �����˻���
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (4, 'LS200380601010100011234_1', 1, 1, 'url=ashasoadigmnalskd', '2017-08-26', 2, 4, '0', 1, '........', @iTradeID, '2019-10-29 16:28:28', 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iReturnTradeID = LAST_INSERT_ID();
-- �����˻�����Ʒ
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnTradeID, @iCommodityID, '�ɱȿ���Ƭhh2', 1, 15/*F_NO*/, 321, 500, 300, 300, NULL);
SET @iReturnTradeCommodityID = LAST_INSERT_ID();
-- �����˻�ȥ���
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iReturnTradeCommodityID, @iCommodityID, 10/*F_NO*/, @iWarehousingID2);
--
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iReturnTradeCommodityID, @iCommodityID, 1/*F_NO*/, @iWarehousingID);

-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sID = (SELECT group_concat(F_ID) FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID = @iReturnTradeCommodityID); 		        
-- 
CALL SPD_Returnretailtradecommoditydestination_CheckNO (@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @sID, 'ȥ���,��ͨ��ƷF_NO���Ӧ���˻���ƷF_NO�����') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case8 Testing Result';
DELETE FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID = @iReturnTradeCommodityID;
DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @iTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID IN (@iTradeCommodityID,@iReturnTradeCommodityID);
DELETE FROM t_retailtrade WHERE F_ID IN (@iTradeID,@iReturnTradeID);
DELETE FROM t_warehousingcommodity WHERE F_WarehousingID IN (@iWarehousingID,@iWarehousingID2);
DELETE FROM t_warehousing WHERE F_ID IN (@iWarehousingID,@iWarehousingID2);
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;