SELECT '++++++++++++++++++Test_SP_RetailTradeDailyReportByStaff_Create_Case8.sql+++++++++++++++++++++++';

-- ���Կ���������˻�
-- ������8*20 = 160
-- �����ɱ���5*10+3*15 = 95
-- ����ë����160-95 = 65
-- �˻���5*20 = 100
-- �˻��ɱ���3*15+2*10 = 65
-- �˻�ë����100-65 = 35

-- ���۽�160-100 = 60
-- ����ë����65-35 = 30

SELECT '-----------------Case8:���˻�������Ʒ���Բ�ͬ��ⵥʱ�����۸���ܲ�һ������⣩ ------------------' AS 'Case8';
-- ���۵�
SET @rt1CommotidyNO1 = 8;
SET @rt1CommotidyPrice1 = 20.000000;
SET @rt1AmuontCash = 160.000000;
SET @rt1AmuontWeChat = 0.000000;
SET @rt1AmuontAliPay = 0.000000;
SET @rt1TotalAmount = @rt1CommotidyNO1 * @rt1CommotidyPrice1;
-- �˻���(�����۵�1)
SET @rrt1CommotidyNO = 5;
SET @rrt1CommotidyPrice = 20.000000;
SET @rrt1AmuontCash = 100.000000;
SET @rrt1AmuontWeChat = 0.000000;
SET @rrt1AmuontAliPay = 0.000000;
SET @rrt1TotalAmount = @rrt1CommotidyNO * @rrt1CommotidyPrice;
-- ���۵���Ʒ��Դ��
SET @rtcs1NO1 = 5;
SET @rtcs1NO2 = 3;
-- ��Ʒȥ���
SET @destinationCommodityID216 = 216;
SET @rtcd1NO1 = 3;
SET @rtcd1NO2 = 2;
-- ���۵���������
SET @rggWorkTimeStart='2119-01-01 00:00:00';
SET @rggWorkTimeEnd='2119-01-01 23:59:59';
SET @rggTotalAmount1 = @rt1TotalAmount; 
SET @rggTotalAmountCash1 = @rt1AmuontCash; 
SET @rggTotalAmountWeChat1 = @rt1AmuontWeChat;
SET @rggTotalAmountAliPay1 = @rt1AmuontAliPay;
SET @rggNO1 = 1; 
-- ����Ա
SET @iStaffID = 2;
-- ��Ʒ�����۸�
SET @warehousingCommodityPrice1 = 10.000000;
SET @warehousingCommodityPrice2 = 15.000000;

INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
VALUES (@iStaffID, 2, @rggWorkTimeStart, @rggWorkTimeEnd, 1, @rt1TotalAmount, 200.000000, @rggTotalAmountCash1, @rggTotalAmountWeChat1, @rggTotalAmountAliPay1, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, now());
SET @rtgID = last_insert_id();

-- ����һ����Ʒ
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_CreateDatetime, F_UpdateDatetime)
VALUES (0, 'δ�����Ʒ', '��ͨ��Ʒ', '��', 3, '��', 1, 1, 'ztq666', 1, 
19, 19, 0, 1, 365, 3, '2119-01-01 09:00:00', 1, 0, 0, 0, 0, '2119-01-01 09:00:00', '2019-05-19 09:00:00');
SET @commID = last_insert_id();
-- ����һ��������
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commID, 'tzq66666', '2119-01-01 09:00:00', '2119-01-01 09:00:00');
SET @barcodeID = last_insert_id();
-- ��Ӧ������Ʒ���й���
INSERT INTO t_providercommodity (F_CommodityID, F_ProviderID)
VALUES (@commID, 1);
SET @providerCommID = last_insert_id();

-- ��Ҫһ����Ʒ�������Ų�ͬ����ⵥ���۸�ͬ������������Ȼ������˻�
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_UpdateDatetime)
VALUES (2,'rk211901010001', 1, 1, 2, 4, '2119-01-01 06:00:00', '2119-01-01 06:00:00');
SET @ws1 = last_insert_id();

INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_UpdateDatetime)
VALUES (2,'rk211901010001', 1, 1, 2, 4, '2119-01-01 06:00:00', '2119-01-01 06:00:00');
SET @ws2 = last_insert_id();
-- ������ⵥ��Ʒ��
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@ws1, @commID, 5, 3, 'δ�����Ʒ', @barcodeID, @warehousingCommodityPrice1, 50, '2119-01-01 06:00:00', 36, '2119-01-09 06:00:00', '2119-01-01 06:00:00', '2119-01-01 06:00:00', 5);
SET @wsc1 = last_insert_id();

INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@ws2, @commID, 5, 3, 'δ�����Ʒ', @barcodeID, @warehousingCommodityPrice2, 75, '2119-01-01 06:00:00', 36, '2119-01-09 06:00:00', '2119-01-01 06:00:00', '2119-01-01 06:00:00', 5);
SET @wsc2 = last_insert_id();

-- ��������
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Status,F_Remark,F_SourceID,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID, F_ShopID) 
VALUES (1,'LS21190010101010100011244', 2,1,'url=ashasoadigmnalskd','2119-01-01 09:00:00',@iStaffID,1,0,1,'........',-1,'2119-01-01 09:00:00',@rt1TotalAmount,@rt1AmuontCash,@rt1AmuontAliPay,@rt1AmuontWeChat,0,0,0,0,0,1,2);
SET @rtID1 = last_insert_id();
-- ���۵���Ʒ��
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@rtID1, @commID, 'δ�����Ʒ', @barcodeID, @rt1CommotidyNO1, 20, 8, @rt1CommotidyPrice1, 19);
SET @rtCommID1 = last_insert_id();
-- �������۵���Ʒ��Դ
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommID1, @commID, @rtcs1NO1, @ws1);
SET @rtCommsourceID1 = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommID1, @commID, @rtcs1NO2, @ws2);
SET @rtCommsourceID2 = last_insert_id();

-- �����˻�
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Status,F_Remark,F_SourceID,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID, F_ShopID) 
VALUES (1,'LS21190010101010100011244', 2,1,'url=ashasoadigmnalskd','2119-01-01 10:00:00',@iStaffID,1,0,1,'........',@rtID1,'2119-01-01 10:00:00',@rrt1TotalAmount,@rrt1AmuontCash,@rrt1AmuontAliPay,@rrt1AmuontWeChat,0,0,0,0,0,1,2);
SET @rtID2 = last_insert_id();
-- �������۵���Ʒ��
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@rtID2, @commID, 'δ�����Ʒ', @barcodeID, @rrt1CommotidyNO, 20, 5, @rrt1CommotidyPrice, 19);
SET @rtCommID2 = last_insert_id();
-- �����˻���Ʒȥ���
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommID2, @commID, @rtcd1NO1, @ws2);
SET @rtcd1 = last_insert_id();
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommID2, @commID, @rtcd1NO2, @ws1);
SET @rtcd2 = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime = '2119-01-01 0:00:00';
SET @deleteOldData = 1; -- ������ʹ�� 1��ɾ���ɵ����ݡ�ֻ���ڲ��Դ�����ʹ�á�0����ɾ���ɵ����ݡ�ֻ���ڹ��ܴ�����ʹ�á�
SET @iShopID = 2;

CALL SP_RetailTradeDailyReportByStaff_Create(@iErrorCode, @sErrorMsg, @iShopID, @dSaleDatetime, @deleteOldData);
SELECT @iErrorCode;
SELECT @sErrorMsg;

-- ����ҵ����������ݽ����֤

-- �����ܽ��
SET @saleTotalAmount = (@rt1CommotidyNO1 * @rt1CommotidyPrice1);
-- ������Ʒ�ܳɱ�
SET @saleTotalCost = (@rtcs1NO1 * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @ws1 AND F_CommodityID = @commID))
					 + (@rtcs1NO2 * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @ws2 AND F_CommodityID = @commID));
-- ������Ʒ��ë��
SET @saleTotalMargin = @saleTotalAmount - @saleTotalCost;
-- �˻��ܽ��
SET @returnTotalAmount = (@rrt1CommotidyNO * @rrt1CommotidyPrice);
-- �˻���Ʒ�ܳɱ�
SET @returnTotalCost= (@rtcd1NO1 * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @ws2 AND F_CommodityID = @commID))
					   +(@rtcd1NO2 * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @ws1 AND F_CommodityID = @commID)) ;
-- �˻���Ʒ��ë��
SET @returnTotalMargin = @returnTotalAmount - @returnTotalCost;
-- �ܽ������ܽ�� - �˻��ܽ�
SET @totalAmount = @saleTotalAmount - @returnTotalAmount;
-- ��ë����������Ʒ��ë�� - �˻���Ʒ��ë����
SET @grossMargin = @saleTotalMargin - @returnTotalMargin;

SET @ResultVerification=0;
-- ���е�Ա9ҵ����������ݽ����֤
SELECT 1 INTO @ResultVerification
FROM t_retailtradedailyreportbystaff 
WHERE F_StaffID = @iStaffID
AND F_Datetime = @dSaleDatetime
AND F_TotalAmount = @totalAmount
AND F_GrossMargin = @grossMargin
AND F_NO = (
	SELECT COUNT(1) 
	FROM t_retailtrade 
	WHERE F_SourceID = -1 
	AND F_StaffID = @iStaffID
	AND datediff(F_SaleDatetime, @dSaleDatetime) = 0
 );

SELECT IF(@iErrorCode = 0 AND @ResultVerification = 1,'���Գɹ�','����ʧ��') AS 'Test Case8 Result';

DELETE FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime;
DELETE FROM t_retailtradeaggregation WHERE F_ID = @rtgID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommsourceID1;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommsourceID2;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @rtcd1;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @rtcd2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtCommID1;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtCommID2;
DELETE FROM t_warehousingcommodity WHERE F_ID = @wsc1;
DELETE FROM t_warehousingcommodity WHERE F_ID = @wsc2;
DELETE FROM t_warehousing WHERE F_ID = @ws1;
DELETE FROM t_warehousing WHERE F_ID = @ws2;
DELETE FROM t_providercommodity WHERE F_ID = @providerCommID;
DELETE FROM T_RetailTrade WHERE F_ID = @rtID1;
DELETE FROM T_RetailTrade WHERE F_ID = @rtID2;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID;
DELETE FROM t_commodity WHERE F_ID = @commID;