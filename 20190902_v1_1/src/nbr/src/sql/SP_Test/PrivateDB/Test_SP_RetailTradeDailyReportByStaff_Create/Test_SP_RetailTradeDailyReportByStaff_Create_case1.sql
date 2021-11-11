SELECT '++++++++++++++++++Test_SP_RetailTradeDailyReportByStaff_Create_case1.sql+++++++++++++++++++++++';

SELECT '-----------------Case1: 当某天只有一个员工进行销售，无退货，收银汇总存在交班记录 ------------------' AS 'Case1';
SET @rt1CommotidyNO = 10;
SET @rt1CommotidyPrice = 10.000000;
SET @rt1CommotidyNO2 = 10;
SET @rt1CommotidyPrice2 = 20.000000;
SET @rt1AmuontCash = 300.00000;
SET @rt1AmuontWeChat = 0.000000;
SET @rt1AmuontAliPay = 0.000000;
SET @rt1TotalAmount = @rt1CommotidyNO * @rt1CommotidyPrice + @rt1CommotidyNO2 * @rt1CommotidyPrice2;
SET @rt2CommotidyNO = 10;
SET @rt2CommotidyPrice = 30.000000;
SET @rt2AmuontCash = 300.00000;
SET @rt2AmuontWeChat = 0.000000;
SET @rt2AmuontAliPay = 0.000000;
SET @rt2TotalAmount = @rt2CommotidyNO * @rt2CommotidyPrice;
SET @rt3CommotidyNO = 5;
SET @rt3CommotidyPrice = 60.000000;
SET @rt3CommotidyNO2 = 10;
SET @rt3CommotidyPrice2 = 20.000000;
SET @rt3AmuontCash = 300.00000;
SET @rt3AmuontWeChat = 200.000000;
SET @rt3AmuontAliPay = 0.000000;
SET @rt3TotalAmount = @rt3CommotidyNO * @rt3CommotidyPrice + @rt3CommotidyNO2 * @rt3CommotidyPrice2 ;
SET @rt4CommotidyNO = 10;
SET @rt4CommotidyPrice = 10.000000;
SET @rt4CommotidyNO2 = 10;
SET @rt4CommotidyPrice2 = 30.000000;
SET @rt4AmuontCash = 100.00000;
SET @rt4AmuontWeChat = 300.000000;
SET @rt4AmuontAliPay = 0.000000;
SET @rt4TotalAmount = @rt4CommotidyNO * @rt4CommotidyPrice + @rt4CommotidyNO2 * @rt4CommotidyPrice2 ;
SET @rt5CommotidyNO = 10;
SET @rt5CommotidyPrice = 10.000000;
SET @rt5CommotidyNO2 = 5;
SET @rt5CommotidyPrice2 = 60.000000;
SET @rt5AmuontCash = 100.00000;
SET @rt5AmuontWeChat = 300.000000;
SET @rt5AmuontAliPay = 0.000000;
SET @rt5TotalAmount = @rt5CommotidyNO * @rt5CommotidyPrice + @rt5CommotidyNO2 * @rt5CommotidyPrice2 ;
SET @rt6CommotidyNO = 10;
SET @rt6CommotidyPrice = 30.000000;
SET @rt6CommotidyNO2 = 10;
SET @rt6CommotidyPrice2 = 20.000000;
SET @rt6AmuontCash = 300.00000;
SET @rt6AmuontWeChat = 200.000000;
SET @rt6AmuontAliPay = 0.000000;
SET @rt6TotalAmount = @rt6CommotidyNO * @rt6CommotidyPrice + @rt6CommotidyNO2 * @rt6CommotidyPrice2 ;
SET @rt7CommotidyNO = 10;
SET @rt7CommotidyPrice = 30.000000;
SET @rt7CommotidyNO2 = 10;
SET @rt7CommotidyPrice2 = 60.000000;
SET @rt7AmuontCash = 900.00000;
SET @rt7AmuontWeChat = 0.000000;
SET @rt7AmuontAliPay = 0.000000;
SET @rt7TotalAmount = @rt7CommotidyNO * @rt7CommotidyPrice + @rt7CommotidyNO2 * @rt7CommotidyPrice2 ;
-- 零售单商品来源表
SET @commodityID155 = 155;
SET @commodityID158 = 158;
SET @rtcs1NO = 5;
SET @rtcs1WarehousingID = 15;
SET @rtcs2NO = 5;
SET @rtcs2WarehousingID = 16;
SET @rtcs3NO = 10;
SET @rtcs3WarehousingID = 20;
SET @rtcs4NO = 20;
SET @rtcs4WarehousingID = 16;
SET @rtcs5NO = 10;
SET @rtcs5WarehousingID = 17;
SET @rtcs6NO = 10;
SET @rtcs6WarehousingID = 16;
SET @rtcs7NO = 15;
SET @rtcs7WarehousingID = 21;
SET @rtcs8NO = 10;
SET @rtcs8WarehousingID = 21;
SET @rtcs9NO = 10;
SET @rtcs9WarehousingID = 17;
SET @rtcs10NO = 30;
SET @rtcs10WarehousingID = 17;
SET @rtcs11NO = 10;
SET @rtcs11WarehousingID = 16;
SET @rtcs12NO = 10;
SET @rtcs12WarehousingID = 16;
SET @rtcs13NO = 15;
SET @rtcs13WarehousingID = 21;
SET @rtcs14NO = 30;
SET @rtcs14WarehousingID = 17;
SET @rtcs15NO = 10;
SET @rtcs15WarehousingID = 16;
SET @rtcs16NO = 15;
SET @rtcs16WarehousingID = 17;
SET @rtcs17NO = 20;
SET @rtcs17WarehousingID = 16;
SET @rtcs18NO = 30;
SET @rtcs18WarehousingID = 21;
-- 零售单收银汇总
SET @rggWorkTimeStart1='2119-01-15 00:00:00';
SET @rggWorkTimeEnd1='2119-01-15 23:59:59';
SET @rggTotalAmount = @rt1TotalAmount + @rt2TotalAmount + @rt3TotalAmount + @rt4TotalAmount + @rt5TotalAmount + @rt6TotalAmount + @rt7TotalAmount;
SET @rggTotalAmountCash = @rt1AmuontCash + @rt2AmuontCash + @rt3AmuontCash + @rt4AmuontCash + @rt5AmuontCash + @rt6AmuontCash + @rt7AmuontCash;
SET @rggTotalAmountWeChat = @rt1AmuontWeChat + @rt2AmuontWeChat + @rt3AmuontWeChat + @rt4AmuontWeChat + @rt5AmuontWeChat + @rt6AmuontWeChat + @rt7AmuontWeChat;
SET @rggTotalAmountAliPay = @rt1AmuontAliPay + @rt2AmuontAliPay + @rt3AmuontAliPay + @rt4AmuontAliPay + @rt5AmuontAliPay + @rt6AmuontAliPay + @rt7AmuontAliPay;
SET @rggNO = 7;
SET @iStaffID = 2;

INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
VALUES (@iStaffID, 2, @rggWorkTimeStart1, @rggWorkTimeEnd1, @rggNO, @rggTotalAmount, @rggTotalAmountCash, @rggTotalAmountWeChat, @rggTotalAmountAliPay, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, now());
SET @rtgID = last_insert_id();

SET @saleDatetime = '2119-1-15 17:42:31';
SET @syncDatetime = '2119-1-15 17:42:31';
-- 零售单
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID, F_ShopID) 
VALUES (1,'LS2119011501010100011244', 2,1,'url=ashasoadigmnalskd',@saleDatetime,@iStaffID,1,0,1,'........',@syncDatetime,@rt1TotalAmount,@rt1AmuontCash,@rt1AmuontWeChat,@rt1AmuontAliPay,0,0,0,0,0,1,2);
SET @rtID1 = last_insert_id();
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID, F_ShopID) 
VALUES (1,'LS2119011501010100021245', 3,2,'url=ashasoadigmnalskd',@saleDatetime,@iStaffID,1,0,1,'........',@syncDatetime,@rt2TotalAmount,@rt2AmuontCash,@rt2AmuontWeChat,@rt2AmuontAliPay,0,0,0,0,0,1,2);
SET @rtID2 = last_insert_id();
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID, F_ShopID) 
VALUES (1,'LS2119011501010100011247', 99991,1,'url=ashasoadigmnalskd',@saleDatetime,@iStaffID,3,0,1,'........',@syncDatetime,@rt3TotalAmount,@rt3AmuontCash,@rt3AmuontWeChat,@rt3AmuontAliPay,0,0,0,0,0,1,2);
SET @rtID3 = last_insert_id();
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID, F_ShopID) 
VALUES (1,'LS2119011501010100021248', 99992,2,'url=ashasoadigmnalskd',@saleDatetime,@iStaffID,5,0,1,'........',@syncDatetime,@rt4TotalAmount,@rt4AmuontCash,@rt4AmuontWeChat,@rt4AmuontAliPay,0,0,0,0,0,1,2);
SET @rtID4 = last_insert_id();
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID, F_ShopID) 
VALUES (1,'LS2119011501010100021249', 99993,2,'url=ashasoadigmnalskd',@saleDatetime,@iStaffID,1,0,1,'........',@syncDatetime,@rt5TotalAmount,@rt5AmuontCash,@rt5AmuontWeChat,@rt5AmuontAliPay,0,0,0,0,0,1,2);
SET @rtID5 = last_insert_id();
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID, F_ShopID) 
VALUES (1,'LS2119011501010100021250', 99994,2,'url=ashasoadigmnalskd',@saleDatetime,@iStaffID,1,0,1,'........',@syncDatetime,@rt6TotalAmount,@rt6AmuontCash,@rt6AmuontWeChat,@rt6AmuontAliPay,0,0,0,0,0,1,2);
SET @rtID6 = last_insert_id();
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_Amount1,F_Amount2,F_Amount3,F_Amount4,F_Amount5,F_SmallSheetID, F_ShopID) 
VALUES (1,'LS2119011501010100021251', 99995,2,'url=ashasoadigmnalskd',@saleDatetime,@iStaffID,5,0,1,'........',@syncDatetime,@rt7TotalAmount,@rt7AmuontCash,@rt7AmuontWeChat,@rt7AmuontAliPay,0,0,0,0,0,1,2);
SET @rtID7 = last_insert_id();

-- 零售单商品表
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@rtID1, 155,'小白兔牛轧糖', 177, @rt1CommotidyNO, 11, 10, @rt1CommotidyPrice, 10); 
SET @rtcID1 = last_insert_id();
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@rtID1, 158,'喜之郎牛轧糖', 180, @rt1CommotidyNO2, 21, 10, @rt1CommotidyPrice2, 20); 
SET @rtcID2 = last_insert_id();
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@rtID2, 156,'大白兔牛轧糖', 178, @rt2CommotidyNO, 31, 10, @rt2CommotidyPrice, 30); 
SET @rtcID3 = last_insert_id();
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@rtID3, 157,'零食大礼包', 179, @rt3CommotidyNO, 61, 5, @rt3CommotidyPrice, 60);
SET @rtcID4 = last_insert_id();
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@rtID3, 158,'喜之郎牛轧糖', 180, @rt3CommotidyNO2, 21, 10, @rt3CommotidyPrice2, 20);
SET @rtcID5 = last_insert_id();
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@rtID4, 155,'小白兔牛轧糖', 177, @rt4CommotidyNO, 11, 10, @rt4CommotidyPrice, 10); 
SET @rtcID6 = last_insert_id();
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@rtID4, 156,'大白兔牛轧糖', 178, @rt4CommotidyNO2, 31, 10, @rt4CommotidyPrice2, 30); 
SET @rtcID7 = last_insert_id();
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@rtID5, 155,'小白兔牛轧糖', 177, @rt5CommotidyNO, 11, 10, @rt5CommotidyPrice, 10); 
SET @rtcID8 = last_insert_id();
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@rtID5, 157,'零食大礼包', 179, @rt5CommotidyNO2, 61, 5, @rt5CommotidyPrice2, 60); 
SET @rtcID9 = last_insert_id();
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@rtID6, 156,'大白兔牛轧糖', 178, @rt6CommotidyNO, 31, 10, @rt6CommotidyPrice, 30);
SET @rtcID10 = last_insert_id();
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@rtID6, 155,'小白兔牛轧糖', 177, @rt6CommotidyNO2, 21, 10, @rt6CommotidyPrice2, 20);
SET @rtcID11 = last_insert_id();
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@rtID7, 156,'大白兔牛轧糖', 178, @rt7CommotidyNO, 31, 5, @rt7CommotidyPrice, 30); 
SET @rtcID12 = last_insert_id();
INSERT INTO T_RetailTradeCommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@rtID7, 157,'零食大礼包', 179, @rt7CommotidyNO2, 61, 10, @rt7CommotidyPrice2, 60); 
SET @rtcID13 = last_insert_id();

-- 零售单商品来源表
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) VALUES (@rtcID1, @rtcs1NO, @rtcs1WarehousingID, @commodityID155); 
SET @rtcSourceID1 = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) VALUES (@rtcID1, @rtcs2NO, @rtcs2WarehousingID, @commodityID155); 
SET @rtcSourceID2 = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) VALUES (@rtcID2, @rtcs3NO, @rtcs3WarehousingID, @commodityID158); 
SET @rtcSourceID3 = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) VALUES (@rtcID3, @rtcs4NO, @rtcs4WarehousingID, @commodityID155); 
SET @rtcSourceID4 = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) VALUES (@rtcID3, @rtcs5NO, @rtcs5WarehousingID, @commodityID155); 
SET @rtcSourceID5 = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) VALUES (@rtcID4, @rtcs6NO, @rtcs6WarehousingID, @commodityID155); 
SET @rtcSourceID6 = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) VALUES (@rtcID4, @rtcs7NO, @rtcs7WarehousingID, @commodityID158); 
SET @rtcSourceID7 = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) VALUES (@rtcID5, @rtcs8NO, @rtcs8WarehousingID, @commodityID158); 
SET @rtcSourceID8 = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) VALUES (@rtcID6, @rtcs9NO, @rtcs9WarehousingID, @commodityID155); 
SET @rtcSourceID9 = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) VALUES (@rtcID7, @rtcs10NO, @rtcs10WarehousingID, @commodityID155);
SET @rtcSourceID10 = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) VALUES (@rtcID8, @rtcs11NO, @rtcs11WarehousingID, @commodityID155); 
SET @rtcSourceID11 = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) VALUES (@rtcID9, @rtcs12NO, @rtcs12WarehousingID, @commodityID155);
SET @rtcSourceID12 = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) VALUES (@rtcID9, @rtcs13NO, @rtcs13WarehousingID, @commodityID158);
SET @rtcSourceID13 = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) VALUES (@rtcID10, @rtcs14NO, @rtcs14WarehousingID, @commodityID155); 
SET @rtcSourceID14 = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) VALUES (@rtcID11, @rtcs15NO, @rtcs15WarehousingID, @commodityID155); 
SET @rtcSourceID15 = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) VALUES (@rtcID12, @rtcs16NO, @rtcs16WarehousingID, @commodityID155); 
SET @rtcSourceID16 = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) VALUES (@rtcID13, @rtcs17NO, @rtcs17WarehousingID, @commodityID155); 
SET @rtcSourceID17 = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) VALUES (@rtcID13, @rtcs18NO, @rtcs18WarehousingID, @commodityID158);
SET @rtcSourceID18 = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime = '2119-1-15 0:00:00';
SET @deleteOldData = 1; -- 仅测试使用 1，删除旧的数据。只能在测试代码中使用。0，不删除旧的数据。只能在功能代码中使用。
SET @iShopID = 2;
CALL SP_RetailTradeDailyReportByStaff_Create(@iErrorCode, @sErrorMsg, @iShopID, @dSaleDatetime, @deleteOldData);
SELECT @iErrorCode;
SELECT @sErrorMsg;

-- 售卖总金额
SET @saleTotalAmount = (@rt1CommotidyNO * @rt1CommotidyPrice + @rt1CommotidyNO2 * @rt1CommotidyPrice2) + (@rt2CommotidyNO * @rt2CommotidyPrice) 
					   + (@rt3CommotidyNO * @rt3CommotidyPrice + @rt3CommotidyNO2 * @rt3CommotidyPrice2) + (@rt4CommotidyNO * @rt4CommotidyPrice + @rt4CommotidyNO2 * @rt4CommotidyPrice2)
					   + (@rt5CommotidyNO * @rt5CommotidyPrice + @rt5CommotidyNO2 * @rt5CommotidyPrice2) + (@rt6CommotidyNO * @rt6CommotidyPrice + @rt6CommotidyNO2 * @rt6CommotidyPrice2)
					   + (@rt7CommotidyNO * @rt7CommotidyPrice + @rt7CommotidyNO2 * @rt7CommotidyPrice2);
-- 售卖商品总成本
SET @saleTotalCost = (@rtcs1NO * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs1WarehousingID AND F_CommodityID = @commodityID155)) 
					 + (@rtcs2NO * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs2WarehousingID AND F_CommodityID = @commodityID155))
					 + (@rtcs3NO * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs3WarehousingID AND F_CommodityID = @commodityID158))
					 + (@rtcs4NO * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs4WarehousingID AND F_CommodityID = @commodityID155))
					 + (@rtcs5NO * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs5WarehousingID AND F_CommodityID = @commodityID155))
					 + (@rtcs6NO * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs6WarehousingID AND F_CommodityID = @commodityID155))
					 + (@rtcs7NO * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs7WarehousingID AND F_CommodityID = @commodityID158))
					 + (@rtcs8NO * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs8WarehousingID AND F_CommodityID = @commodityID158))
					 + (@rtcs9NO * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs9WarehousingID AND F_CommodityID = @commodityID155))
					 + (@rtcs10NO * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs10WarehousingID AND F_CommodityID = @commodityID155))
					 + (@rtcs11NO * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs11WarehousingID AND F_CommodityID = @commodityID155))
					 + (@rtcs12NO * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs12WarehousingID AND F_CommodityID = @commodityID155))
					 + (@rtcs13NO * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs13WarehousingID AND F_CommodityID = @commodityID158))
					 + (@rtcs14NO * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs14WarehousingID AND F_CommodityID = @commodityID155))
					 + (@rtcs15NO * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs15WarehousingID AND F_CommodityID = @commodityID155))
					 + (@rtcs16NO * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs16WarehousingID AND F_CommodityID = @commodityID155))
					 + (@rtcs17NO * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs17WarehousingID AND F_CommodityID = @commodityID155))
					 + (@rtcs18NO * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs18WarehousingID AND F_CommodityID = @commodityID158));
-- 售卖商品总毛利
SET @saleTotalMargin = @saleTotalAmount - @saleTotalCost;
-- 退货总金额
SET @returnTotalAmount = 0.000000;
-- 退货商品总成本
SET @returnTotalCost = 0.000000;
-- 退货商品总毛利
SET @returnTotalMargin = 0.000000;
-- 总金额（售卖总金额 - 退货总金额）
SET @totalAmount = @saleTotalAmount - @returnTotalAmount;
-- 总毛利（售卖商品总毛利 - 退货商品总毛利）
SET @grossMargin = @saleTotalMargin - @returnTotalMargin;

-- 进行业绩报表的数据结果验证
SET @ResultVerification1=0;
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

SELECT IF(@iErrorCode = 0 AND @ResultVerification1 = 1,'测试成功','测试失败') AS 'Test Case1 Result';


SELECT '-----------------Case2:零售单没有数据，收银汇总也没有该员工的交班记录 进行统计创建 ------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime = '2099-1-15 0:00:00';
SET @deleteOldData = 1; -- 仅测试使用 1，删除旧的数据。只能在测试代码中使用。0，不删除旧的数据。只能在功能代码中使用。
SET @iShopID = 2;

CALL SP_RetailTradeDailyReportByStaff_Create(@iErrorCode, @sErrorMsg, @iShopID, @dSaleDatetime, @deleteOldData);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_retailtradedailyreportbystaff WHERE F_Datetime = '2099-1-15 0:00:00';
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';


SELECT '-----------------Case3:deleteOldData=0时，重复创建员工业绩报表errorCode=3 ------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime = '2119-1-15 0:00:00';
SET @deleteOldData = 0; -- 仅测试使用 1，删除旧的数据。只能在测试代码中使用。0，不删除旧的数据。只能在功能代码中使用。
SET @iShopID = 2;

CALL SP_RetailTradeDailyReportByStaff_Create(@iErrorCode, @sErrorMsg, @iShopID, @dSaleDatetime, @deleteOldData);
SELECT IF(@iErrorCode = 3, '测试成功', '测试失败') AS 'Case3 Testing Result';

SELECT '-----------------Case4:deleteOldData=1时，重复创建员工业绩报表errorCode=0 ------------------' AS 'Case4';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime = '2119-1-15 0:00:00';
SET @deleteOldData = 1; -- 仅测试使用 1，删除旧的数据。只能在测试代码中使用。0，不删除旧的数据。只能在功能代码中使用。
SET @iShopID = 2;

CALL SP_RetailTradeDailyReportByStaff_Create(@iErrorCode, @sErrorMsg, @iShopID, @dSaleDatetime, @deleteOldData);
-- 售卖总金额
SET @saleTotalAmount = (@rt1CommotidyNO * @rt1CommotidyPrice + @rt1CommotidyNO2 * @rt1CommotidyPrice2) + (@rt2CommotidyNO * @rt2CommotidyPrice) 
					   + (@rt3CommotidyNO * @rt3CommotidyPrice + @rt3CommotidyNO2 * @rt3CommotidyPrice2) + (@rt4CommotidyNO * @rt4CommotidyPrice + @rt4CommotidyNO2 * @rt4CommotidyPrice2)
					   + (@rt5CommotidyNO * @rt5CommotidyPrice + @rt5CommotidyNO2 * @rt5CommotidyPrice2) + (@rt6CommotidyNO * @rt6CommotidyPrice + @rt6CommotidyNO2 * @rt6CommotidyPrice2)
					   + (@rt7CommotidyNO * @rt7CommotidyPrice + @rt7CommotidyNO2 * @rt7CommotidyPrice2);
-- 售卖商品总成本
SET @saleTotalCost = (@rtcs1NO * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs1WarehousingID AND F_CommodityID = @commodityID155)) 
					 + (@rtcs2NO * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs2WarehousingID AND F_CommodityID = @commodityID155))
					 + (@rtcs3NO * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs3WarehousingID AND F_CommodityID = @commodityID158))
					 + (@rtcs4NO * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs4WarehousingID AND F_CommodityID = @commodityID155))
					 + (@rtcs5NO * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs5WarehousingID AND F_CommodityID = @commodityID155))
					 + (@rtcs6NO * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs6WarehousingID AND F_CommodityID = @commodityID155))
					 + (@rtcs7NO * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs7WarehousingID AND F_CommodityID = @commodityID158))
					 + (@rtcs8NO * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs8WarehousingID AND F_CommodityID = @commodityID158))
					 + (@rtcs9NO * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs9WarehousingID AND F_CommodityID = @commodityID155))
					 + (@rtcs10NO * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs10WarehousingID AND F_CommodityID = @commodityID155))
					 + (@rtcs11NO * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs11WarehousingID AND F_CommodityID = @commodityID155))
					 + (@rtcs12NO * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs12WarehousingID AND F_CommodityID = @commodityID155))
					 + (@rtcs13NO * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs13WarehousingID AND F_CommodityID = @commodityID158))
					 + (@rtcs14NO * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs14WarehousingID AND F_CommodityID = @commodityID155))
					 + (@rtcs15NO * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs15WarehousingID AND F_CommodityID = @commodityID155))
					 + (@rtcs16NO * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs16WarehousingID AND F_CommodityID = @commodityID155))
					 + (@rtcs17NO * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs17WarehousingID AND F_CommodityID = @commodityID155))
					 + (@rtcs18NO * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs18WarehousingID AND F_CommodityID = @commodityID158));
-- 售卖商品总毛利
SET @saleTotalMargin = @saleTotalAmount - @saleTotalCost;
-- 退货总金额
SET @returnTotalAmount = 0.000000;
-- 退货商品总成本
SET @returnTotalCost = 0.000000;
-- 退货商品总毛利
SET @returnTotalMargin = 0.000000;
-- 总金额（售卖总金额 - 退货总金额）
SET @totalAmount = @saleTotalAmount - @returnTotalAmount;
-- 总毛利（售卖商品总毛利 - 退货商品总毛利）
SET @grossMargin = @saleTotalMargin - @returnTotalMargin;

-- 进行业绩报表的数据结果验证
SET @ResultVerification1=0;
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

SELECT IF(@iErrorCode = 0 AND @ResultVerification1 = 1,'测试成功','测试失败') AS 'Test Case4 Result';

-- 进行数据删除，避免污染数据库
DELETE FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime;
DELETE FROM t_retailtradeaggregation WHERE F_ID = @rtgID;

DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtcSourceID1;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtcSourceID2;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtcSourceID3;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtcSourceID4;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtcSourceID5;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtcSourceID6;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtcSourceID7;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtcSourceID8;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtcSourceID9;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtcSourceID10;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtcSourceID11;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtcSourceID12;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtcSourceID13;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtcSourceID14;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtcSourceID15;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtcSourceID16;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtcSourceID17;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtcSourceID18;

DELETE FROM T_RetailTradeCommodity WHERE F_ID = @rtcID1;
DELETE FROM T_RetailTradeCommodity WHERE F_ID = @rtcID2;
DELETE FROM T_RetailTradeCommodity WHERE F_ID = @rtcID3;
DELETE FROM T_RetailTradeCommodity WHERE F_ID = @rtcID4;
DELETE FROM T_RetailTradeCommodity WHERE F_ID = @rtcID5;
DELETE FROM T_RetailTradeCommodity WHERE F_ID = @rtcID6;
DELETE FROM T_RetailTradeCommodity WHERE F_ID = @rtcID7;
DELETE FROM T_RetailTradeCommodity WHERE F_ID = @rtcID8;
DELETE FROM T_RetailTradeCommodity WHERE F_ID = @rtcID9;
DELETE FROM T_RetailTradeCommodity WHERE F_ID = @rtcID10;
DELETE FROM T_RetailTradeCommodity WHERE F_ID = @rtcID11;
DELETE FROM T_RetailTradeCommodity WHERE F_ID = @rtcID12;
DELETE FROM T_RetailTradeCommodity WHERE F_ID = @rtcID13;

DELETE FROM t_retailtrade WHERE F_ID = @rtID1;
DELETE FROM t_retailtrade WHERE F_ID = @rtID2;
DELETE FROM t_retailtrade WHERE F_ID = @rtID3;
DELETE FROM t_retailtrade WHERE F_ID = @rtID4;
DELETE FROM t_retailtrade WHERE F_ID = @rtID5;
DELETE FROM t_retailtrade WHERE F_ID = @rtID6;
DELETE FROM t_retailtrade WHERE F_ID = @rtID7;