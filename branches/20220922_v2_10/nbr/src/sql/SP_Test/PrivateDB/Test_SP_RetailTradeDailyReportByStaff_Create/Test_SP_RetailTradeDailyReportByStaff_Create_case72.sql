SELECT '++++++++++++++++++Test_SP_RetailTradeDailyReportByStaff_Create_case72.sql+++++++++++++++++++++++';

SELECT '----- case72:  Ա��A��A��Ʒ����ƷAΪδ�����Ʒ ,��B��Ʒ����Ӧһ����ⵥ����A�����˻�������ƷBȫ���˻� [A] -------' AS 'Case72';
-- 
--  Ա��ID
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
-- һ����ⵥ
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_UpdateDatetime)
VALUES (2,'rk211901010001', 1, 1, 2, 4, '2119-01-01 06:00:00', '2119-01-01 06:00:00');
SET @warehouseB = last_insert_id();
-- ������ⵥ��Ʒ��
SET @warehouseBNO = 500;
SET @warehouseBPrice = 12.12;
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@warehouseB, @commIDB, @warehouseBNO, 3, '�����ƷB', @barcodeIDB, @warehouseBPrice, 5000, '2119-01-01 06:00:00', 36, '2119-01-09 06:00:00', '2119-01-01 06:00:00', '2119-01-01 06:00:00', 5);
SET @warehouseCommB = last_insert_id();
-- 
-- �������۵�
SET @saleCommANO_A = 251; -- δ��⣬ֱ����500
SET @aCommotidyPrice = 231.8140000;
SET @saleCommBNO_A = 100;	-- ���500����100
SET @bCommotidyPrice = 200.000000;
SET @rt1AmuontCash_A = @saleCommANO_A * @aCommotidyPrice + @saleCommBNO_A * @bCommotidyPrice;
SET @rt1AmuontWeChat_A = 0.000000;
SET @rt1AmuontAliPay_A = 0.000000;
SET @rt1TotalAmount_A = @rt1AmuontCash_A + @rt1AmuontWeChat_A + @rt1AmuontAliPay_A;
SET @saleDatetime1_A = '2041/02/02 06:00:00';
SET @syncDatetime1_A = '2041/02/02 06:00:00';

INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1,'LS2041020200000500010001',11068,1,'url=ashasoadigmnalskd','2041/02/02 06:00:00',@staffIDA,1,'0',1,'��',-1,'2041/02/01 06:00:00', @rt1TotalAmount_A, @rt1AmuontCash_A, @rt1AmuontAliPay_A,@rt1AmuontWeChat_A,0.000000,0.000000,0.000000,0.000000,0.000000,1,NULL,NULL,NULL,NULL,NULL,NULL,2);
SET @rtID_A = last_insert_id();
-- ������ƷA
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtID_A, @commIDA, '��ͨ��ƷA', @barcodeIDA, @saleCommANO_A, @aCommotidyPrice, @saleCommANO_A, @aCommotidyPrice, 300, NULL);
SET @rtCommAID_A = last_insert_id();
-- ������ƷB
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtID_A, @commIDB, '��ͨ��ƷB', @barcodeIDB, @saleCommBNO_A, @bCommotidyPrice, @saleCommBNO_A, @bCommotidyPrice, 200, NULL);
SET @rtCommBID_A = last_insert_id();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommAID_A, @commIDB, @saleCommBNO_A, @warehouseB);
SET @rtCommsourceIDB_A = last_insert_id();

-- Ա��A�½��˻���
SET @returnCommANO_A = 250; -- ��500������250��
SET @returnCommBNO_A = @saleCommBNO_A;	-- ȫ��
SET @rrt1AmuontCash_A = @returnCommANO_A * @aCommotidyPrice + @returnCommBNO_A * @bCommotidyPrice;
SET @rrt1AmuontWeChat_A = 0.000000;
SET @rrt1AmuontAliPay_A = 0.000000;
SET @rrt1TotalAmount_A = @rrt1AmuontCash_A + @rrt1AmuontWeChat_A + @rrt1AmuontAliPay_A;
SET @saleDatetime2_A = '2041/02/02 06:00:00';
SET @syncDatetime2_A = '2041/02/02 06:00:00';

INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1,'LS2041020200000500010001_1',11069,1,'url=ashasoadigmnalskd',@saleDatetime2_A,@staffIDA,1,'0',1,'��',@rtID_A,@syncDatetime2_A,@rrt1TotalAmount_A,@rrt1AmuontCash_A,@rrt1AmuontAliPay_A,@rrt1AmuontWeChat_A,0.000000,0.000000,0.000000,0.000000,0.000000,1,NULL,NULL,NULL,NULL,NULL,NULL,2);
SET @rrtID_A = last_insert_id();
-- ������ƷA
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rrtID_A, @commIDA, '��ͨ��ƷA', @barcodeIDA, @returnCommANO_A, @aCommotidyPrice, @returnCommANO_A, @aCommotidyPrice, 300, NULL);
SET @returnCommAID_A = last_insert_id();
-- ������ƷB
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rrtID_A, @commIDB, '�ɿڿ���', @barcodeIDB, @returnCommBNO_A, @bCommotidyPrice, @returnCommBNO_A, @bCommotidyPrice, 200, NULL);
SET @returnCommBID_A = last_insert_id();
-- �����˻�ȥ�����ƷB
INSERT INTO t_returnretailtradecommoditydestination(F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@returnCommBID_A, @commIDB, @returnCommBNO_A, @warehouseB);
SET @returnCommBDestn = last_insert_id();
-- 
-- ���ۻ���
SET @rggWorkTimeStart_A='2041/02/02 00:00:00';
SET @rggWorkTimeEnd_A='2041/02/02 23:59:59';
SET @rggTotalAmountCash_A = @rt1TotalAmount_A;
SET @rggTotalAmountWeChat_A = 0.000000;
SET @rggTotalAmountAliPay_A = 0.000000;
SET @rggTotalAmount_A = @rggTotalAmountCash_A + @rggTotalAmountWeChat_A + @rggTotalAmountAliPay_A;

INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
VALUES (@staffIDA, 2, @rggWorkTimeStart_A, @rggWorkTimeEnd_A, 1, @rggTotalAmount_A, 100.000000, @rggTotalAmountCash_A, @rggTotalAmountWeChat_A, @rggTotalAmountAliPay_A, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, now());
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
-- 
-- �����֤
-- ��֤Ա��A���������˻�
SET @saleTotalAmount_A = @rt1TotalAmount_A;
-- ������Ʒ�ܳɱ�
SET @saleTotalCost_A = 	@saleCommBNO_A * @warehouseBPrice; -- ��ƷAû��⣬ֻ����ƷB�����
-- ������Ʒ��ë��
SET @saleTotalMargin_A = @saleTotalAmount_A - @saleTotalCost_A;
-- �˻��ܽ��
SET @returnTotalAmount_A = @rrt1TotalAmount_A;
-- �˻���Ʒ�ܳɱ�
SET @returnTotalCost_A = @returnCommBNO_A * @warehouseBPrice;
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

SELECT IF(@iErrorCode = 0 AND @ResultVerification_A = 1,'���Գɹ�','����ʧ��') AS 'Test Case72 Result';
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
-- ��ƷA���
DELETE FROM t_providercommodity WHERE F_ID = @providerCommIDA;
DELETE FROM t_barcodes WHERE F_ID = @barcodeIDA;
DELETE FROM t_commodity WHERE F_ID = @commIDA;
-- ��ƷB���
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehouseCommB;
DELETE FROM t_warehousing WHERE F_ID = @warehouseB;
DELETE FROM t_providercommodity WHERE F_ID = @providerCommIDB;
DELETE FROM t_barcodes WHERE F_ID = @barcodeIDB;
DELETE FROM t_commodity WHERE F_ID = @commIDB;