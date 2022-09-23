SELECT '++++++++++++++++++Test_SP_RetailTradeDailyReportByStaff_Create_case15.sql+++++++++++++++++++++++';

SELECT '----- case15:  Ա��A����һ�����۵���Aһ����Ʒ����Ӧ������ⵥ -------' AS 'Case15';
-- 
SET @rt1CommotidyNO1 = 500;
SET @rt1CommotidyPrice1 = 300;
SET @rt1AmuontCash = 150000.000000;
SET @rt1AmuontWeChat = 0.000000;
SET @rt1AmuontAliPay = 0.000000;
SET @rt1TotalAmount = @rt1CommotidyNO1 * @rt1CommotidyPrice1;
-- ���۵���������
SET @rggWorkTimeStart1='2041/02/02 00:00:00';
SET @rggWorkTimeEnd1='2041/02/02 23:59:59';
SET @rggTotalAmount1 =  @rt1TotalAmount; 
SET @rggTotalAmountCash1 = @rt1AmuontCash;
SET @rggTotalAmountWeChat1 = @rt1AmuontWeChat;
SET @rggTotalAmountAliPay1 = @rt1AmuontAliPay;
SET @rggNO1 = 1;
-- ���۵���Ʒ��Դ��
SET @rtcs1NO = 200;
SET @rtcs2NO = 300;
-- ��ⵥ
SET @warehousingCommodityNO1=200;
SET @warehousingCommodityPrice1=10;
-- 
SET @warehousingCommodityNO2=300;
SET @warehousingCommodityPrice2=15;

-- ����һ����ƷA
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_CreateDatetime, F_UpdateDatetime)
VALUES (0, '�����ƷA', '��ͨ��ƷA', '��', 3, '��', 1, 1, 'TZQ666', 1, 
19, 19, 0, 1, 365, 3, '2119-01-01 09:00:00', 1, 0, 0, 0, 0, '2119-01-01 09:00:00', '2019-05-19 09:00:00');
SET @commIDA = last_insert_id();
-- ����A��һ��������
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commIDA, 'TZQ66666', '2119-01-01 09:00:00', '2119-01-01 09:00:00');
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
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehouseA1, @commIDA, @warehousingCommodityNO1, 3, '�����ƷA', @barcodeIDA, @warehousingCommodityPrice1, 5000, '2119-01-01 06:00:00', 36, '2119-01-09 06:00:00', '2119-01-01 06:00:00', '2119-01-01 06:00:00', 5);
SET @warehouseCommA1 = last_insert_id();
-- 
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehouseA2, @commIDA, @warehousingCommodityNO2, 3, '�����ƷA', @barcodeIDA, @warehousingCommodityPrice2, 7500, '2119-01-01 06:00:00', 36, '2119-01-09 06:00:00', '2119-01-01 06:00:00', '2119-01-01 06:00:00', 5);
SET @warehouseCommA2 = last_insert_id();

--  Ա��A����һ�����۵�,����A��B��C��Ʒ
-- Ա��A�½����۵�
SET @staffIDA = 9;
INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
VALUES (@staffIDA, 2, @rggWorkTimeStart1, @rggWorkTimeEnd1, @rggNO1, @rggTotalAmount1, 70000.000000, @rggTotalAmountCash1, @rggTotalAmountWeChat1, @rggTotalAmountAliPay1, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, now());
SET @rtgID1 = last_insert_id();
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1,'LS2041020200000500010001',11068,1,'url=ashasoadigmnalskd','2041/02/02 06:00:00',@staffIDA,1,'0',1,'��',-1,'2041/02/01 06:00:00', 150000.000000, 150000.000000, 0.000000,0.000000,0.000000,0.000000,0.000000,0.000000,0.000000,1,NULL,NULL,NULL,NULL,NULL,NULL,2);
SET @rtID1 = last_insert_id();

-- ������ƷA
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtID1, @commIDA, '��ͨ��ƷA', @barcodeIDA, @rt1CommotidyNO1, 20, 500, @rt1CommotidyPrice1, 300, NULL);
SET @rtCommID1 = last_insert_id();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommID1, @commIDA, @rtcs1NO, @warehouseA1);
SET @rtCommsourceIDA1 = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommID1, @commIDA, @rtcs2NO, @warehouseA2);
SET @rtCommsourceIDA2 = last_insert_id();
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

-- �����ܽ��
SET @saleTotalAmount = (@rt1CommotidyNO1 * @rt1CommotidyPrice1);
-- ������Ʒ�ܳɱ�
SET @saleTotalCost = (@rtcs1NO * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @warehouseA1 AND F_CommodityID = @commIDA))
					 +(@rtcs2NO * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @warehouseA2 AND F_CommodityID = @commIDA));
-- ������Ʒ��ë��
SET @saleTotalMargin = @saleTotalAmount - @saleTotalCost;
-- �˻��ܽ��
SET @returnTotalAmount = 0.00000;
-- �˻���Ʒ�ܳɱ�
SET @returnTotalCost = 0.000000;
-- �˻���Ʒ��ë��
SET @returnTotalMargin = @returnTotalAmount - @returnTotalCost;
-- �ܽ������ܽ�� - �˻��ܽ�
SET @totalAmount = @saleTotalAmount - @returnTotalAmount;
-- ��ë����������Ʒ��ë�� - �˻���Ʒ��ë����
SET @grossMargin = @saleTotalMargin - @returnTotalMargin;

SET @ResultVerification1=0;
-- ���е�Աҵ����������ݽ����֤
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

SELECT IF(@iErrorCode = 0 AND @ResultVerification1 = 1,'���Գɹ�','����ʧ��') AS 'Test Case14 Result';
-- 
DELETE FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime;
DELETE FROM t_retailtradeaggregation WHERE F_ID = @rtgID1;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommsourceIDA1;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommsourceIDA2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtCommID1;
DELETE FROM t_retailtrade WHERE F_ID = @rtID1;
-- ��ƷA���
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehouseCommA1;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehouseCommA2;
DELETE FROM t_warehousing WHERE F_ID = @warehouseA1;
DELETE FROM t_warehousing WHERE F_ID = @warehouseA2;
DELETE FROM t_providercommodity WHERE F_ID = @providerCommIDA;
DELETE FROM t_barcodes WHERE F_ID = @barcodeIDA;
DELETE FROM t_commodity WHERE F_ID = @commIDA;