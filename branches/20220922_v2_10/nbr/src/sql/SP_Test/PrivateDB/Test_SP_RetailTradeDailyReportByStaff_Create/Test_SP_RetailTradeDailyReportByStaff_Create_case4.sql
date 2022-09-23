SELECT '++++++++++++++++++Test_SP_RetailTradeDailyReportByStaff_Create_Case4.sql+++++++++++++++++++++++';

SELECT '-------------------- Case4���������δ�����Ʒ -------------------------' AS 'Case4';
-- ���۵�
SET @rt1CommotidyNO = 15;
SET @rt1CommotidyPrice = 20.000000;
SET @rt1AmuontCash = 300.00000;
SET @rt1AmuontWeChat = 0.000000;
SET @rt1AmuontAliPay = 0.000000;
SET @rt1TotalAmount = @rt1CommotidyNO * @rt1CommotidyPrice;
-- ���۵���Ʒ��Դ��
SET @rtcs1NO = 15;
SET @rtcs1WarehousingID = 15;
-- ��Ʒȥ���
SET @destinationCommodityID155 = 155;
SET @rtcd1NO = 8;
SET @rtcd1WarehousingID = 15;
-- ���۵���������
SET @rggWorkTimeStart1='2119-05-19 00:00:00';
SET @rggWorkTimeEnd1='2119-05-19 23:59:59';
SET @rggTotalAmount = @rt1TotalAmount;
SET @rggTotalAmountCash = @rt1AmuontCash;
SET @rggTotalAmountWeChat = @rt1AmuontWeChat;
SET @rggTotalAmountAliPay = @rt1AmuontAliPay;
SET @rggNO = 1;
-- ����Ա
SET @iStaffID = 2;

INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
VALUES (@iStaffID, 2, @rggWorkTimeStart1, @rggWorkTimeEnd1, @rggNO, @rggTotalAmount, 100.000000, @rggTotalAmountCash, @rggTotalAmountWeChat, @rggTotalAmountAliPay, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, now());
SET @rtgID = last_insert_id();

-- ����һ����Ʒ
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_CreateDatetime, F_UpdateDatetime)
VALUES (0, 'δ�����Ʒ', '��ͨ��Ʒ', '��', 3, '��', 1, 1, 'TZQ666', 1, 
19, 19, 0, 1, 365, 3, '2119-05-19 09:00:00', 1, 0, 0, 0, 0, '2119-05-19 09:00:00', '2019-05-19 09:00:00');
SET @commID = last_insert_id();
-- ����һ��������
INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@commID, 'tzq66666', '2119-05-19 09:00:00', '2119-05-19 09:00:00');
SET @barcodeID = last_insert_id();
-- ��Ӧ������Ʒ���й���
INSERT INTO t_providercommodity (F_CommodityID, F_ProviderID)
VALUES (@commID, 1);
SET @providerCommID = last_insert_id();

SET @saleDatetime = '2119-05-19 09:00:00';
SET @syncDatetime = '2119-05-19 09:00:00';
-- ��������
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID, F_ShopID) 
VALUES (1,'LS2119051901010100011244', 2,1,'url=ashasoadigmnalskd',@saleDatetime,@iStaffID,1,0,1,'........',@syncDatetime,@rt1TotalAmount,@rt1AmuontCash,@rrt1AmuontAliPay,@rrt1AmuontWeChat,0,0,0,0,0,1,2);
SET @rtID1 = last_insert_id();
-- ���۵���Ʒ��
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@rtID1, @commID, 'δ�����Ʒ', @barcodeID, @rt1CommotidyNO, 20, 15, @rt1CommotidyPrice, 19);
SET @rtCommID1 = last_insert_id();
-- �������۵���Ʒ��Դ��
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) VALUES (@rtCommID1, @rtcs1NO, NULL, @commID);  -- F_WarehousingIDΪNULL
SET @rtcSourceID1 = last_insert_id();

SET @dSaleDatetime = '2119-5-19 00:00:00';
SET @sErrorMsg = '';
SET @iErrorCode = 0;
SET @deleteOldData = 1; -- ������ʹ�� 1��ɾ���ɵ����ݡ�ֻ���ڲ��Դ�����ʹ�á�0����ɾ���ɵ����ݡ�ֻ���ڹ��ܴ�����ʹ�á�
SET @iShopID = 2;

CALL SP_RetailTradeDailyReportByStaff_Create(@iErrorCode, @sErrorMsg, @iShopID, @dSaleDatetime, @deleteOldData);

SELECT @iErrorCode;
SELECT @sErrorMsg;
-- �����ܽ��
SET @saleTotalAmount = (@rt1CommotidyNO * @rt1CommotidyPrice);
-- ������Ʒ�ܳɱ�
SET @saleTotalCost = (@rtcs1NO * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = NULL AND F_CommodityID = @commID));
-- ������Ʒ��ë��
SET @saleTotalMargin = @saleTotalAmount - (ifnull(@saleTotalCost, 0.00000));
-- �˻��ܽ��
SET @returnTotalAmount = 0.000000;
-- �˻���Ʒ�ܳɱ�
SET @returnTotalCost = 0.000000;
-- �˻���Ʒ��ë��
SET @returnTotalMargin = 0.000000;
-- �ܽ������ܽ�� - �˻��ܽ�
SET @totalAmount = @saleTotalAmount - @returnTotalAmount;
-- ��ë����������Ʒ��ë�� - �˻���Ʒ��ë����
SET @grossMargin = @saleTotalMargin - @returnTotalMargin;
SELECT @saleTotalMargin;
SET @ResultVerification1=0;
-- ����ҵ����������ݽ����֤
SELECT 1 INTO @ResultVerification1
FROM t_retailtradedailyreportbystaff 
WHERE F_StaffID = @iStaffID 
AND F_Datetime = @dSaleDatetime
AND F_TotalAmount = @totalAmount
AND F_GrossMargin = @grossMargin
AND F_NO = (
	SELECT COUNT(1) 
	FROM t_retailtrade 
	WHERE F_SourceID = -1 
	AND datediff(F_SaleDatetime, @dSaleDatetime) = 0
);

SELECT IF(@iErrorCode = 0 AND @ResultVerification1 = 1,'���Գɹ�','����ʧ��') AS 'Test Case4 Result';

DELETE FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime;
DELETE FROM t_retailtradeaggregation WHERE F_ID = @rtgID;
DELETE FROM t_retailtradecommoditysource WHERE F_ID=@rtcSourceID1;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtCommID1;
DELETE FROM T_RetailTrade WHERE F_ID = @rtID1;
DELETE FROM t_providercommodity WHERE F_ID = @providerCommID;
DELETE FROM t_barcodes WHERE F_ID = @barcodeID;
DELETE FROM t_commodity WHERE F_ID = @commID;