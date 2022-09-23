SELECT '++++++++++++++++++Test_SP_RetailTradeDailyReportByStaff_Create_Case3.sql+++++++++++++++++++++++';

SELECT '-----------------Case3:�������˻������Լ����������������۵������˻������������н����¼��------------------' AS 'Case3';
-- ���۵�
SET @rt1CommotidyNO = 10;
SET @rt1CommotidyPrice = 10.000000;
SET @rt1AmuontCash = 100.00000;
SET @rt1AmuontWeChat = 0.000000;
SET @rt1AmuontAliPay = 0.000000;
SET @rt1TotalAmount = @rt1CommotidyNO * @rt1CommotidyPrice;
-- 
SET @rt1CommotidyNO2 = 30;
SET @rt1CommotidyPrice2 = 10.000000;
SET @rt1AmuontCash2 = 100.00000;
SET @rt1AmuontWeChat2 = 200.000000;
SET @rt1AmuontAliPay2 = 0.000000;
SET @rt1TotalAmount2 = @rt1CommotidyNO2 * @rt1CommotidyPrice2;
-- �˻���
SET @rrt1CommotidyNO = 10;
SET @rrt1CommotidyPrice = 10.000000;
SET @rrt1AmuontCash = 0.00000;
SET @rrt1AmuontWeChat = 100.000000;
SET @rrt1AmuontAliPay = 0.000000;
SET @rrt1TotalAmount = @rrt1CommotidyNO * @rrt1CommotidyPrice;
-- ���۵���Ʒ��Դ��
SET @sourceCommodityID155 = 155;
SET @rtcs1NO = 10;
SET @rtcs1WarehousingID = 15;
-- 
SET @sourceCommodityID2155 = 155;
SET @rtcs1NO2 = 30;
SET @rtcs1WarehousingID2 = 15;
-- ��Ʒȥ���
SET @destinationCommodityID155 = 155;
SET @rtcd1NO = 10;
SET @rtcd1WarehousingID = 15;
-- ���۵���������
SET @rggWorkTimeStart1='2119-01-16 00:00:00';
SET @rggWorkTimeEnd1='2119-01-16 23:59:59';
SET @rggTotalAmount = @rt1TotalAmount + @rt1TotalAmount2;
SET @rggTotalAmountCash = @rt1AmuontCash + @rt1AmuontCash2;
SET @rggTotalAmountWeChat = @rt1AmuontWeChat + @rt1AmuontWeChat2;
SET @rggTotalAmountAliPay = @rt1AmuontAliPay + @rt1AmuontAliPay2;
SET @rggNO = 1;
-- ����Ա
SET @iStaffID = 2;

INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
VALUES (@iStaffID, 2, @rggWorkTimeStart1, @rggWorkTimeEnd1, 1, @rggTotalAmount, 1.00000, @rggTotalAmountCash, @rggTotalAmountWeChat, @rggTotalAmountAliPay, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, now());
SET @rtgID = last_insert_id();

SET @saleDatetime = '2119-1-16 17:42:31';
SET @syncDatetime = '2119-1-16 17:42:31';
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Status,F_Remark,F_SourceID,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID, F_ShopID) 
VALUES (1,'LS2119011601010100021252', 99996,2,'url=ashasoadigmnalskd',@saleDatetime,@iStaffID,1,0,1,'........',-1,@syncDatetime,@rrt1TotalAmount,@rrt1AmuontCash,@rt1AmuontAliPay,@rt1AmuontWeChat,0,0,0,0,0,1,2);
SET @rtID1 = last_insert_id();
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Status,F_Remark,F_SourceID,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID, F_ShopID) 
VALUES (1,'LS2119011601010100021253', 99997,2,'url=ashasoadigmnalskd',@saleDatetime,@iStaffID,5,0,1,'........',-1,@syncDatetime,@rt1TotalAmount2,@rt1AmuontCash2,@rt1AmuontAliPay2,@rt1AmuontWeChat2,0,0,0,0,0,1,2);
SET @rtID2 = last_insert_id();
-- �������۵���Ʒ��
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@rtID1, 155,'С����ţ����', 177, @rt1CommotidyNO, 10, 10, @rt1CommotidyPrice, 10); 
SET @rtcID1 = last_insert_id();
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@rtID2, 155,'С����ţ����', 177, @rt1CommotidyNO2, 10, 10, @rt1CommotidyPrice2, 10); 
SET @rtcID2 = last_insert_id();
-- �������۵���Ʒ��Դ��
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) VALUES (@rtcID1, @rtcs1NO, @rtcs1WarehousingID, @sourceCommodityID155); 
SET @rtcSourceID1 = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) VALUES (@rtcID2, @rtcs1NO2, @rtcs1WarehousingID2, @sourceCommodityID2155); 
SET @rtcSourceID2 = last_insert_id();

INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
VALUES (@iStaffID, 2, '2119-01-17 00:00:00', '2119-01-17 23:59:59', 1, 750.000000, 600.000000, 200.000000, -50.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, now());
SET @rtgID2 = last_insert_id();

SET @saleDatetime2 = '2119-1-17 17:42:31';
SET @syncDatetime2 = '2119-1-17 17:42:31';
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Status,F_Remark,F_SourceID,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID, F_ShopID) 
VALUES (1,'LS2119011701010100011255', 99999,2,'url=ashasoadigmnalskd',@saleDatetime2,@iStaffID,2,0,1,'........',@rtID1,@syncDatetime2,@rrt1TotalAmount,@rrt1AmuontCash,@rrt1AmuontAliPay,@rrt1AmuontWeChat,0,0,0,0,0,1,2);
SET @rtID3 = last_insert_id();
-- �������۵���Ʒ��
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@rtID3, 155,'С����ţ����', 177, @rrt1CommotidyNO, 10, 10, @rrt1CommotidyPrice, 10); 
SET @rtcID3 = last_insert_id();
-- �����˻���Ʒȥ���
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtcID3, @destinationCommodityID155, @rtcd1NO, @rtcd1WarehousingID);
SET @rtcd = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime = '2119-1-17 0:00:00';
SET @deleteOldData = 1; -- ������ʹ�� 1��ɾ���ɵ����ݡ�ֻ���ڲ��Դ�����ʹ�á�0����ɾ���ɵ����ݡ�ֻ���ڹ��ܴ�����ʹ�á�
SET @iShopID = 2;

CALL SP_RetailTradeDailyReportByStaff_Create(@iErrorCode, @sErrorMsg, @iShopID, @dSaleDatetime, @deleteOldData);
SELECT @iErrorCode;
SELECT @sErrorMsg;

-- �����ܽ��(�����˻���������۶�Ϊ0)
SET @saleTotalAmount = 0;
-- ������Ʒ�ܳɱ�(�����˻�������ܳɱ�Ϊ0)
SET @saleTotalCost = 0;
-- ������Ʒ��ë��
SET @saleTotalMargin = @saleTotalAmount - @saleTotalCost;
-- �˻��ܽ��
SET @returnTotalAmount = (@rrt1CommotidyNO * @rrt1CommotidyPrice);
-- �˻���Ʒ�ܳɱ�
SET @returnTotalCost = (@rtcd1NO * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcd1WarehousingID AND F_CommodityID = @destinationCommodityID155));
-- �˻���Ʒ��ë��
SET @returnTotalMargin = @returnTotalAmount - @returnTotalCost;
-- �ܽ������ܽ�� - �˻��ܽ�
SET @totalAmount = @saleTotalAmount - @returnTotalAmount;
-- ��ë����������Ʒ��ë�� - �˻���Ʒ��ë����
SET @grossMargin = @saleTotalMargin - @returnTotalMargin;

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

SELECT IF(@iErrorCode = 0 AND @ResultVerification1 = 1,'���Գɹ�','����ʧ��') AS 'Test Case2 Result';

DELETE FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime;
DELETE FROM t_retailtradeaggregation WHERE F_ID = @rtgID;
DELETE FROM t_retailtradeaggregation WHERE F_ID = @rtgID2;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtcSourceID1;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtcSourceID2;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @rtcd;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtcID1;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtcID2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtcID3;
DELETE FROM t_retailtrade WHERE F_ID = @rtID1;
DELETE FROM t_retailtrade WHERE F_ID = @rtID2;
DELETE FROM t_retailtrade WHERE F_ID = @rtID3;