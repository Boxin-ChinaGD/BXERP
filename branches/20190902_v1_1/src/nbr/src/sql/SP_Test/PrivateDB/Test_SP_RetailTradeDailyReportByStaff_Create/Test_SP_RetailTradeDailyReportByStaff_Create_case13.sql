SELECT '++++++++++++++++++Test_SP_RetailTradeDailyReportByStaff_Create_case13.sql+++++++++++++++++++++++';
-- 当B 2030-01-05 9.00 进行售卖一单
-- A 2030-01-06 9.00-12.00 上班 进行对B昨天售卖的单进行退货 此时B未上班，另外A在这个时间段还销售两单
-- B 2030-01-06 12.00-16.00 上班 进行对A上午销售的其中一单进行退货，此时A未上班;B在这个时间段售卖两单
-- A,B 2030-01-06 18.00-20.00 上班 A,B分别售卖一单，A对B下午售卖的其中一单进行退货，B对A上午售卖的另外一单进行退货，B对自己这个时间段售卖的一单进行退货.

SELECT '-----------------Case13:混乱情况：当A对B进行隔天退货，B不在上班;B对A进行当天退货，A不在上班;A,B同时上班，B退A今天的货，B退自己今天的货，查看员工业绩报表是否正常 A、B、(AB)  ------------------' AS 'Case13';
-- SELECT '2030-01-05: B上班，有收银汇总AGG_A1，B售出RTA1';
-- SELECT '2030-01-06 9.00-12.00: A上班，有收银汇总AGG_B1，A对RTA1退货成RRTB1,A售出RTA2,RTA3';
-- SELECT '2030-01-06 12.00-16.00: B上班有收银汇总AGG_A2, B对RTA2退货成RRTB1,B售出RTB1,RTB2';
-- SELECT '2030-01-06 18.00-20.00: A上班有收银汇总AGG_A3,A售出RTA4; B上班有收银汇总AGG_B1,B售出RTB3; A对RTA4退成RRTA2,B对RTA3退货成RRTB2,B对RTB1退货成RRTB3';
-- 插入商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0/*F_Status*/, '长虹剑', '薯片', '克', 1, '千克', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0, NULL, '2019-12-23 10:01:33', '2019-12-23 10:07:37', NULL, NULL, NULL, NULL);
SET @iCommodityID1 = last_insert_id();
-- 插入入库单
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (1, 'RK190003010001', 1, 1, 4, 4, '2000-01-01 13:30:00', NULL, '2000-01-01 13:30:00');
SET @iWarehousingID = last_insert_id();
-- 插入入库单商品
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID, @iCommodityID1, 100, 1, '长虹剑', 13, 1.7, 100, '2017-10-06 01:01:01', 36, '2020-10-06 01:01:01', '2019-12-23 10:01:35', '2019-12-23 10:01:35', 100);
-- 
SET @iStaffAID = 8;
SET @iStaffBID = 9;

-- B 2030-01-05 进行售卖一单
-- 零售单
SET @rt1CommotidyNO_B = 11;
SET @rt1CommotidyPrice_B = 18.320000;
SET @rt1AmuontCash_B = @rt1CommotidyNO_B * @rt1CommotidyPrice_B;
SET @rt1AmuontWeChat_B = 0.000000;
SET @rt1AmuontAliPay_B = 0.000000;
SET @rt1TotalAmount_B = @rt1CommotidyNO_B * @rt1CommotidyPrice_B;
-- 零售单收银汇总
SET @rggWorkTimeStartYesterday_B='3030-01-05 00:00:00';
SET @rggWorkTimeEndYesterday_B='3030-01-05 20:00:00';
SET @rgg1TotalAmount_B = @rt1TotalAmount_B; 
SET @rgg1TotalAmountCash_B = @rt1AmuontCash_B;
SET @rgg1TotalAmountWeChat_B = @rt1AmuontWeChat_B;
SET @rgg1TotalAmountAliPay_B = @rt1AmuontAliPay_B;
SET @rgg1NO_B = 1;
-- 零售单商品来源表
SET @commodityID216 = @iCommodityID1;
SET @rtcs1NO_B = @rt1CommotidyNO_B; -- 10;不跨库入库
SET @rtcs1WarehousingID_B = @iWarehousingID;

INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
VALUES (@iStaffBID, 2, @rggWorkTimeStartYesterday_B, @rggWorkTimeEndYesterday_B, @rgg1NO_B, @rgg1TotalAmount_B, 183.200000, @rgg1TotalAmountCash_B, @rgg1TotalAmountWeChat_B, @rgg1TotalAmountAliPay_B, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, now());
SET @rtgID1_B = last_insert_id();
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1,'LS3030010500000500010001',110681,1,'url=ashasoadigmnalskd','3030-01-05 09:00:00',@iStaffBID,1,'0',1,'…',-1,'3030-01-05 09:00:00',@rt1TotalAmount_B,@rt1AmuontCash_B,@rt1AmuontWeChat_B,@rt1AmuontAliPay_B,0.000000,0.000000,0.000000,0.000000,0.000000,1,NULL,NULL,NULL,NULL,NULL,NULL, 2);
SET @rtID1_B = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtID1_B,@iCommodityID1,'长虹剑',245,@rt1CommotidyNO_B,18.320000,10,@rt1CommotidyPrice_B,NULL,18.320000);
SET @rtCommID1_B = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommID1_B,@commodityID216,@rtcs1NO_B,@rtcs1WarehousingID_B);
SET @rtCommSourceID1_B = last_insert_id();

-- A 第二天即2030-01-06 9.00-12.00 A上班 进行对B昨天售卖的单进行退货 此时B未上班，另外A在这个时间段还销售两单
-- 零售单
SET @rrt1CommotidyNO_A = 9;
SET @rrt1CommotidyPrice_A = 18.320000;
SET @rrt1AmuontCash_A = @rrt1CommotidyPrice_A * @rrt1CommotidyNO_A;
SET @rrt1AmuontWeChat_A = 0.000000;
SET @rrt1AmuontAliPay_A = 0.000000;
SET @rrt1TotalAmount_A = @rrt1AmuontCash_A + @rrt1AmuontWeChat_A + @rrt1AmuontAliPay_A;
-- 
SET @rt1CommotidyNO_A = 11;
SET @rt1CommotidyPrice_A = 12.520000;
SET @rt1AmuontCash_A = @rt1CommotidyPrice_A * @rt1CommotidyNO_A;
SET @rt1AmuontWeChat_A = 0.000000;
SET @rt1AmuontAliPay_A = 0.000000;
SET @rt1TotalAmount_A = @rt1AmuontCash_A + @rt1AmuontWeChat_A + @rt1AmuontAliPay_A;
-- 
SET @rt2CommotidyNO_A = 13;
SET @rt2CommotidyPrice_A = 17.360000;
SET @rt2AmuontCash_A = @rt2CommotidyNO_A * @rt2CommotidyPrice_A;
SET @rt2AmuontWeChat_A = 0.000000;
SET @rt2AmuontAliPay_A = 0.000000;
SET @rt2TotalAmount_A = @rt2AmuontCash_A + @rt2AmuontWeChat_A + @rt2AmuontAliPay_A;
-- 零售单收银汇总
SET @rgg1WorkTimeStart_A='3030-01-06 09:00:00';
SET @rgg1WorkTimeEnd_A='3030-01-06 12:00:00';
SET @rgg1TotalAmount_A =  @rt1TotalAmount_A + @rt2TotalAmount_A; -- 不理退货的数目
SET @rgg1TotalAmountCash_A = @rt1AmuontCash_A + @rt2AmuontCash_A;
SET @rgg1TotalAmountWeChat_A = @rt1AmuontWeChat_A + @rt2AmuontWeChat_A;
SET @rgg1TotalAmountAliPay_A = @rt1AmuontAliPay_A + @rt2AmuontAliPay_A;
SET @rgg1NO_A = 3;
-- 零售单商品来源表
SET @commodityID216 = @iCommodityID1;
SET @rtcs1NO_A = 10;
SET @rtcs1WarehousingID_A = @iWarehousingID;
-- 
SET @rtcs2NO_A = 10;
SET @rtcs2WarehousingID_A = @iWarehousingID;
-- 商品去向表
SET @destinationCommodityID216 = @iCommodityID1;
SET @rtcd1NO_A = 10;
SET @rtcd1WarehousingID_A = @iWarehousingID;

INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
VALUES (@iStaffAID, 2, @rgg1WorkTimeStart_A, @rgg1WorkTimeEnd_A, @rgg1NO_A, @rgg1TotalAmount_A, 183.200000, @rgg1TotalAmountCash_A, @rgg1TotalAmountWeChat_A, @rgg1TotalAmountAliPay_A, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, now());
SET @rtgID1_A = last_insert_id();
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1,'LS3030010600000500010001_1',11068,1,'url=ashasoadigmnalskd','3030-01-06 10:00:00',@iStaffAID,1,'0',1,'…',@rtID1_B,'3030-01-06 10:00:00',@rrt1TotalAmount_A,@rrt1AmuontCash_A,@rrt1AmuontAliPay_A,@rrt1AmuontWeChat_A,0.000000,0.000000,0.000000,0.000000,0.000000,1,NULL,NULL,NULL,NULL,NULL,NULL, 2);
SET @rrtID1_A = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rrtID1_A,216,'长虹剑',245,@rrt1CommotidyNO_A,18.320000,10,@rrt1CommotidyPrice_A,NULL,18.320000);
SET @rrtCommID1_A = last_insert_id();
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@rrtCommID1_A, @destinationCommodityID216, @rtcd1NO_A, @rtcd1WarehousingID_A);
SET @rtcd1_A = last_insert_id();
-- 
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1,'LS3030010600000500010001',11068,1,'url=ashasoadigmnalskd','3030-01-06 10:20:00',@iStaffAID,1,'0',1,'…',-1,'3030-01-06 10:20:00',@rt1TotalAmount_A,@rt1AmuontCash_A,@rt1AmuontWeChat_A,@rt1AmuontAliPay_A,0.000000,0.000000,0.000000,0.000000,0.000000,1,NULL,NULL,NULL,NULL,NULL,NULL,2);
SET @rtID1_A = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtID1_A,216,'长虹剑',245,@rt1CommotidyNO_A,18.320000,10,@rt1CommotidyPrice_A,NULL,18.320000);
SET @rtCommID1_A = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommID1_A,@commodityID216,@rtcs1NO_A,@rtcs1WarehousingID_A);
SET @rtCommSourceID1_A = last_insert_id();
-- 
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1,'LS3030010600000500010002',11068,1,'url=ashasoadigmnalskd','3030-01-06 10:25:00',@iStaffAID,1,'0',1,'…',-1,'3030-01-06 10:25:00',@rt2TotalAmount_A,@rt2AmuontCash_A,@rt2AmuontWeChat_A,@rt2AmuontAliPay_A,0.000000,0.000000,0.000000,0.000000,0.000000,1,NULL,NULL,NULL,NULL,NULL,NULL,2);
SET @rtID2_A = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtID2_A,216,'长虹剑',245,@rt2CommotidyNO_A,18.320000,10,@rt2CommotidyPrice_A,NULL,18.320000);
SET @rtCommID2_A = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommID2_A,@commodityID216,@rtcs2NO_A,@rtcs2WarehousingID_A);
SET @rtCommSourceID2_A = last_insert_id();

-- B 2030-01-06 12.00-16.00 上班 进行对A上午销售的其中一单进行退货，此时A未上班;B在这个时间段售卖两单
-- 零售单
SET @rrt1CommotidyNO_B = 7;
SET @rrt1CommotidyPrice_B = @rt1CommotidyPrice_A;
SET @rrt1AmuontCash_B = @rrt1CommotidyNO_B * @rrt1CommotidyPrice_B;
SET @rrt1AmuontWeChat_B = 0.000000;
SET @rrt1AmuontAliPay_B = 0.000000;
SET @rrt1TotalAmount_B = @rrt1AmuontWeChat_B + @rrt1AmuontWeChat_B + @rrt1AmuontAliPay_B;
-- 
SET @rt2CommotidyNO_B = 17;
SET @rt2CommotidyPrice_B = 19.530000;
SET @rt2AmuontCash_B = @rt2CommotidyNO_B * @rt2CommotidyPrice_B;
SET @rt2AmuontWeChat_B = 0.000000;
SET @rt2AmuontAliPay_B = 0.000000;
SET @rt2TotalAmount_B = @rt2AmuontCash_B + @rt2AmuontWeChat_B + @rt2AmuontAliPay_B;
-- 
SET @rt3CommotidyNO_B = 13;
SET @rt3CommotidyPrice_B = 17.320000;
SET @rt3AmuontCash_B = @rt3CommotidyPrice_B * @rt3CommotidyNO_B;
SET @rt3AmuontWeChat_B = 0.000000;
SET @rt3AmuontAliPay_B = 0.000000;
SET @rt3TotalAmount_B = @rt3AmuontCash_B + @rt3AmuontWeChat_B + @rt3AmuontAliPay_B;
-- 零售单收银汇总
SET @rgg2WorkTimeStart_B='3030-01-06 12:00:00';
SET @rgg2WorkTimeEnd_B='3030-01-06 16:00:00';
SET @rgg2TotalAmount_B = @rt2TotalAmount_B + @rt3TotalAmount_B; 
SET @rgg2TotalAmountCash_B = @rt2AmuontCash_B + @rt3AmuontCash_B;
SET @rgg2TotalAmountWeChat_B = @rt2AmuontWeChat_B + @rt3AmuontWeChat_B;
SET @rgg2TotalAmountAliPay_B = @rt2AmuontAliPay_B + @rt3AmuontAliPay_B;
SET @rggNO2_B = 1;
-- 零售单商品来源表
SET @commodityID216 = @iCommodityID1;
-- 
SET @rtcs2NO_B = 10;
SET @rtcs2WarehousingID_B = @iWarehousingID;
-- 
SET @rtcs3NO_B = 10;
SET @rtcs3WarehousingID_B = @iWarehousingID;
-- 商品去向表
SET @destination2CommodityID216 = @iCommodityID1;
SET @rtcd1NO_B = 10;
SET @rtcd1WarehousingID_B = @iWarehousingID;

INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
VALUES (@iStaffBID, 2, @rgg2WorkTimeStart_B, @rgg2WorkTimeEnd_B, @rggNO2_B, @rgg2TotalAmount_B, 183.200000, @rgg2TotalAmountCash_B, @rgg2TotalAmountWeChat_B, @rgg2TotalAmountAliPay_B, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, now());
SET @rtgID2_B = last_insert_id();
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1,'LS3030010600000500010001_1',11068,1,'url=ashasoadigmnalskd','3030-01-06 13:00:00',@iStaffBID,1,'0',1,'…',@rtID1_A,'3030-01-06 13:00:00',@rrt1TotalAmount_B,@rrt1AmuontCash_B,@rrt1AmuontAliPay_B,@rrt1AmuontWeChat_B,0.000000,0.000000,0.000000,0.000000,0.000000,1,NULL,NULL,NULL,NULL,NULL,NULL,2);
SET @rrtID1_B = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rrtID1_B,216,'长虹剑',245,@rrt1CommotidyNO_B,18.320000,10,@rrt1CommotidyPrice_B,NULL,18.320000);
SET @rrtCommID1_B = last_insert_id();
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@rrtCommID1_B, @destination2CommodityID216, @rtcd1NO_B, @rtcd1WarehousingID_B);
SET @rtcd1_B = last_insert_id();
-- 
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1,'LS3030010600000500010011',11068,1,'url=ashasoadigmnalskd','3030-01-06 14:20:00',@iStaffBID,1,'0',1,'…',-1,'3030-01-06 14:20:00',@rt2TotalAmount_B,@rt2AmuontCash_B,@rt2AmuontAliPay_B,@rt2AmuontWeChat_B,0.000000,0.000000,0.000000,0.000000,0.000000,1,NULL,NULL,NULL,NULL,NULL,NULL,2);
SET @rtID3_B = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtID3_B,216,'长虹剑',245,@rt2CommotidyNO_B,18.320000,10,@rt2CommotidyPrice_B,NULL,18.320000);
SET @rtCommID3_B = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommID3_B,@commodityID216,@rtcs2NO_B,@rtcs2WarehousingID_B);
SET @rtCommSourceID3_B = last_insert_id();
-- 
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1,'LS3030010600000500010012',11068,1,'url=ashasoadigmnalskd','3030-01-06 14:25:00',@iStaffBID,1,'0',1,'…',-1,'3030-01-06 14:25:00',@rt3TotalAmount_B,@rt3AmuontCash_B,@rt3AmuontAliPay_B,@rt3AmuontWeChat_B,0.000000,0.000000,0.000000,0.000000,0.000000,1,NULL,NULL,NULL,NULL,NULL,NULL,2);
SET @rtID4_B = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtID4_B,216,'长虹剑',245,@rt3CommotidyNO_B,18.320000,10,@rt3CommotidyPrice_B,NULL,18.320000);
SET @rtCommID4_B = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommID4_B,@commodityID216,@rtcs3NO_B,@rtcs3WarehousingID_B);
SET @rtCommSourceID4_B = last_insert_id();

-- A,B 2030-01-06 18.00-20.00 上班 A,B分别售卖一单，A对A下午售卖的其中一单进行退货，B对A上午售卖的另外一单进行退货，B对自己这个时间段售卖的一单进行退货.
-- 零售单
SET @rt3CommotidyNO_A = 11;
SET @rt3CommotidyPrice_A = 19.3700;
SET @rt3AmuontCash_A = @rt3CommotidyPrice_A * @rt3CommotidyNO_A;
SET @rt3AmuontWeChat_A = 0.000000;
SET @rt3AmuontAliPay_A = 0.000000;
SET @rt3TotalAmount_A = @rt3AmuontCash_A + @rt3AmuontWeChat_A + @rt3AmuontAliPay_A;
-- 
SET @rt4CommotidyNO_B = 13;
SET @rt4CommotidyPrice_B = 17.320000;
SET @rt4AmuontCash_B = @rt4CommotidyNO_B * @rt4CommotidyPrice_B;
SET @rt4AmuontWeChat_B = 0.000000;
SET @rt4AmuontAliPay_B = 0.000000;
SET @rt4TotalAmount_B = @rt4AmuontCash_B + @rt4AmuontWeChat_B + @rt4AmuontAliPay_B;
-- B退B下午自己的单
SET @rrt2CommotidyNO_B = 7;
SET @rrt2CommotidyPrice_B = @rt4CommotidyPrice_B;
SET @rrt2AmuontCash_B = @rrt2CommotidyNO_B * @rrt2CommotidyPrice_B;
SET @rrt2AmuontWeChat_B = 0.000000;
SET @rrt2AmuontAliPay_B = 0.000000;
SET @rrt2TotalAmount_B = @rrt2AmuontCash_B + @rrt2AmuontWeChat_B + @rrt2AmuontAliPay_B;
-- A退A自己的单
SET @rrt2CommotidyNO_A = 7;
SET @rrt2CommotidyPrice_A = @rt3CommotidyPrice_A;
SET @rrt2AmuontCash_A = @rrt2CommotidyNO_A * @rrt2CommotidyPrice_A;
SET @rrt2AmuontWeChat_A = 0.000000;
SET @rrt2AmuontAliPay_A = 0.000000;
SET @rrt2TotalAmount_A =@rrt2AmuontCash_A + @rrt2AmuontWeChat_A + @rrt2AmuontAliPay_A;
-- B退A早上的单
SET @rrt3CommotidyNO_A = 7;
SET @rrt3CommotidyPrice_A = @rt2CommotidyPrice_A;
SET @rrt3AmuontCash_A =  @rrt3CommotidyNO_A * @rrt3CommotidyPrice_A;
SET @rrt3AmuontWeChat_A = 0.000000;
SET @rrt3AmuontAliPay_A = 0.000000;
SET @rrt3TotalAmount_A = @rrt3AmuontCash_A + @rrt3AmuontWeChat_A + @rrt3AmuontAliPay_A;
-- 零售单收银汇总
SET @rgg2WorkTimeStart_A='3030-01-06 18:00:00';
SET @rgg2WorkTimeEnd_A='3030-01-06 20:00:00';
SET @rgg2TotalAmount_A = @rt3TotalAmount_A; 
SET @rgg2TotalAmountCash_A = @rt3AmuontCash_A;
SET @rgg2TotalAmountWeChat_A = @rt3AmuontWeChat_A;
SET @rgg2TotalAmountAliPay_A = @rt3AmuontAliPay_A;
SET @rgg2NO_A = 1;
-- 
SET @rgg3WorkTimeStart_B='3030-01-06 18:00:00';
SET @rgg3WorkTimeEnd_B='3030-01-06 20:00:00';
SET @rgg3TotalAmount_B = @rt4TotalAmount_B; 
SET @rgg3TotalAmountCash_B = @rt4AmuontCash_B;
SET @rgg3TotalAmountWeChat_B = @rt4AmuontWeChat_B;
SET @rgg3TotalAmountAliPay_B = @rt4AmuontAliPay_B;
SET @rgg3NO_B = 1;
-- 零售单商品来源表
SET @commodityID216 = @iCommodityID1;
SET @rtcs3NO_A = 10;
SET @rtcs3WarehousingID_A = @iWarehousingID;
-- 
SET @rtcs5NO_B = 10;
SET @rtcs5WarehousingID_B = @iWarehousingID;
-- 商品去向表
SET @destination3CommodityID216 = @iCommodityID1;
SET @rtcd2NO_B = 10;
SET @rtcd2WarehousingID_B = @iWarehousingID;
-- 
SET @rtcd2NO_A = 10;
SET @rtcd2WarehousingID_A = @iWarehousingID;
-- 
SET @rtcd3NO_A = 10;
SET @rtcd3WarehousingID_A = @iWarehousingID;

INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
VALUES (@iStaffAID, 2, @rgg2WorkTimeStart_A, @rgg2WorkTimeEnd_A, @rgg2NO_A, @rgg2TotalAmount_A, @rgg2TotalAmountCash_A, @rgg2TotalAmountWeChat_A, @rgg2TotalAmountAliPay_A, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, now());
SET @rtgID2_A = last_insert_id();
INSERT INTO t_retailtradeaggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
VALUES (@iStaffBID, 2, @rgg3WorkTimeStart_B, @rgg3WorkTimeEnd_B, @rgg3NO_B, @rgg3TotalAmount_B, @rgg3TotalAmountCash_B, @rgg3TotalAmountWeChat_B, @rgg3TotalAmountAliPay_B, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, now());
SET @rtgID3_B = last_insert_id();
-- 
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1,'LS3030010600000500010111',11068,1,'url=ashasoadigmnalskd','3030-01-06 19:25:00',@iStaffAID,1,'0',1,'…',-1,'3030-01-06 19:25:00',@rt3TotalAmount_A,@rt3AmuontCash_A,@rt3AmuontAliPay_A,@rt3AmuontWeChat_A,0.000000,0.000000,0.000000,0.000000,0.000000,1,NULL,NULL,NULL,NULL,NULL,NULL,2);
SET @rtID3_A = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtID3_A,216,'长虹剑',245,@rt3CommotidyNO_A,18.320000,10,@rt3CommotidyPrice_A,NULL,18.320000);
SET @rtCommID3_A = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommID3_A,@commodityID216,@rtcs3NO_A,@rtcs3WarehousingID_A);
SET @rtCommSourceID3_A = last_insert_id();
-- 
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1,'LS3030010600000500010112',11068,1,'url=ashasoadigmnalskd','3030-01-06 19:30:00',@iStaffBID,1,'0',1,'…',-1,'3030-01-06 19:30:00',@rt4TotalAmount_B,@rt4AmuontCash_B,@rt4AmuontAliPay_B,@rt4AmuontWeChat_B,0.000000,0.000000,0.000000,0.000000,0.000000,1,NULL,NULL,NULL,NULL,NULL,NULL,2);
SET @rtID5_B = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rtID5_B,216,'长虹剑',245,@rt4CommotidyNO_B,18.320000,10,@rt4CommotidyPrice_B,NULL,18.320000);
SET @rtCommID5_B = last_insert_id();
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@rtCommID5_B,@commodityID216,@rtcs5NO_B,@rtcs5WarehousingID_B);
SET @rtCommSourceID5_B = last_insert_id();
-- B退B自己的下午的单
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1,'LS3030010600000500010112_1',11068,1,'url=ashasoadigmnalskd','3030-01-06 19:32:00',@iStaffBID,1,'0',1,'…',@rtID5_B,'3030-01-06 19:32:00',@rrt2TotalAmount_B,@rrt2AmuontCash_B,@rrt2AmuontAliPay_B,@rrt2AmuontWeChat_B,0.000000,0.000000,0.000000,0.000000,0.000000,1,NULL,NULL,NULL,NULL,NULL,NULL,2);
SET @rrtID2_A = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rrtID2_A,216,'长虹剑',245,@rrt2CommotidyNO_B,18.320000,10,@rrt2CommotidyPrice_B,NULL,18.320000);
SET @rrtCommID2_A = last_insert_id();
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@rrtCommID2_A, @destination3CommodityID216, @rtcd2NO_B, @rtcd2WarehousingID_B);
SET @rtcd2_A = last_insert_id();
-- B退A早上的单
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1,'LS3030010600000500010111_1',11069,1,'url=ashasoadigmnalskd','3030-01-06 19:36:00',@iStaffBID,1,'0',1,'…',@rtID2_A,'3030-01-06 19:36:00',@rrt3TotalAmount_A,@rrt3AmuontCash_A,@rrt3AmuontAliPay_A,@rrt3AmuontWeChat_A,0.000000,0.000000,0.000000,0.000000,0.000000,1,NULL,NULL,NULL,NULL,NULL,NULL,2);
SET @rrtID2_B = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rrtID2_B,216,'长虹剑',245,@rrt3CommotidyNO_A,18.320000,10,@rrt3CommotidyPrice_A,NULL,18.320000);
SET @rrtCommID2_B = last_insert_id();
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@rrtCommID2_B, @destination3CommodityID216, @rtcd2NO_A, @rtcd2WarehousingID_A);
SET @rtcd2_B = last_insert_id();
-- A退A下午的单
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1,'LS3030010600000500010002_1',11070,1,'url=ashasoadigmnalskd','3030-01-06 19:36:00',@iStaffAID,1,'0',1,'…',@rtID3_A,'3030-01-06 19:36:00',@rrt2TotalAmount_A,@rrt2AmuontCash_A,@rrt2AmuontAliPay_A,@rrt2AmuontWeChat_A,0.000000,0.000000,0.000000,0.000000,0.000000,1,NULL,NULL,NULL,NULL,NULL,NULL,2);
SET @rrtID3_A = last_insert_id();
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@rrtID3_A,216,'长虹剑',245,@rrt2CommotidyNO_A,18.320000,10,@rrt2CommotidyPrice_A,NULL,18.320000);
SET @rrtCommID3_A = last_insert_id();
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@rrtCommID3_A, @destination3CommodityID216, @rtcd3NO_A, @rtcd3WarehousingID_A);
SET @rtcd3_A = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dSaleDatetime = '3030-01-06 00:00:00';
SET @deleteOldData = 1; -- 仅测试使用 1，删除旧的数据。只能在测试代码中使用。0，不删除旧的数据。只能在功能代码中使用。
SET @iShopID  = 2;

CALL SP_RetailTradeDailyReportByStaff_Create(@iErrorCode, @sErrorMsg, @iShopID, @dSaleDatetime, @deleteOldData);
SELECT @iErrorCode;
SELECT @sErrorMsg;

-- 进行业绩报表的数据结果验证

-- 售卖总金额
SET @saleTotalAmount_A = (@rt1CommotidyNO_A * @rt1CommotidyPrice_A + @rt2CommotidyNO_A * @rt2CommotidyPrice_A + @rt3CommotidyNO_A * @rt3CommotidyPrice_A);

-- 售卖商品总成本
SET @saleTotalCost_A = (@rtcs1NO_A * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs1WarehousingID_A AND F_CommodityID = @commodityID216))
					 + (@rtcs2NO_A * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs2WarehousingID_A AND F_CommodityID = @commodityID216))
					 + (@rtcs3NO_A * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs3WarehousingID_A AND F_CommodityID = @commodityID216)) ;
-- 售卖商品总毛利
SET @saleTotalMargin_A = @saleTotalAmount_A - @saleTotalCost_A;
-- 退货总金额
SET @returnTotalAmount_A = (@rrt1CommotidyNO_A * @rrt1CommotidyPrice_A + @rrt2CommotidyNO_A * @rrt2CommotidyPrice_A + @rrt3CommotidyNO_A * @rrt3CommotidyPrice_A );

-- 退货商品总成本
SET @returnTotalCost_A = (@rtcd1NO_A * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcd1WarehousingID_A AND F_CommodityID = @destinationCommodityID216))
						+ (@rtcd2NO_A * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcd2WarehousingID_A AND F_CommodityID = @destination3CommodityID216))
					    + (@rtcd3NO_A * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcd3WarehousingID_A AND F_CommodityID = @destination3CommodityID216));
-- 退货商品总毛利
SET @returnTotalMargin_A = @returnTotalAmount_A - @returnTotalCost_A;
-- 总金额（售卖总金额 - 退货总金额）
SET @totalAmount_A = @saleTotalAmount_A - @returnTotalAmount_A;
-- 总毛利（售卖商品总毛利 - 退货商品总毛利）
SET @grossMargin_A = @saleTotalMargin_A - @returnTotalMargin_A;

SET @ResultVerification1=0;
-- 进行店员A业绩报表的数据结果验证
SELECT 1 INTO @ResultVerification1
FROM t_retailtradedailyreportbystaff 
WHERE F_StaffID = @iStaffAID
AND F_Datetime = @dSaleDatetime
AND F_TotalAmount = @totalAmount_A
AND F_GrossMargin = @grossMargin_A
AND F_NO = (
	SELECT COUNT(1) 
	FROM t_retailtrade 
	WHERE F_SourceID = -1 
	AND F_StaffID = @iStaffAID
	AND datediff(F_SaleDatetime, @dSaleDatetime) = 0
);

SELECT IF(@iErrorCode = 0 AND @ResultVerification1 = 1,'测试成功','测试失败') AS 'Test Case13 Result';


-- 售卖总金额
SET @saleTotalAmount_B = (@rt2CommotidyNO_B * @rt2CommotidyPrice_B + @rt3CommotidyNO_B * @rt3CommotidyPrice_B 
						 + @rt4CommotidyNO_B * @rt4CommotidyPrice_B);
-- 售卖商品总成本
SET @saleTotalCost_B = (@rtcs2NO_B * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs2WarehousingID_B AND F_CommodityID = @commodityID216))
					   +(@rtcs3NO_B * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs3WarehousingID_B AND F_CommodityID = @commodityID216))
					   + (@rtcs5NO_B * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcs5WarehousingID_B AND F_CommodityID = @commodityID216));
-- 售卖商品总毛利
SET @saleTotalMargin_B = @saleTotalAmount_B - @saleTotalCost_B;
-- 退货总金额
SET @returnTotalAmount_B = (@rrt1CommotidyNO_B * @rrt1CommotidyPrice_B + @rrt2CommotidyNO_B * @rrt2CommotidyPrice_B); 
-- 退货商品总成本
SET @returnTotalCost_B =(@rtcd1NO_B * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcd1WarehousingID_B AND F_CommodityID = @destination2CommodityID216))
				        +(@rtcd2NO_B * (SELECT F_Price FROM t_warehousingcommodity WHERE F_WarehousingID = @rtcd2WarehousingID_B AND F_CommodityID = @destination3CommodityID216));

-- 退货商品总毛利
SET @returnTotalMargin_B = @returnTotalAmount_B - @returnTotalCost_B;
-- 总金额（售卖总金额 - 退货总金额）
SET @totalAmount_B = @saleTotalAmount_B - @returnTotalAmount_B;
-- 总毛利（售卖商品总毛利 - 退货商品总毛利）
SET @grossMargin_B = @saleTotalMargin_B - @returnTotalMargin_B;
SELECT @totalAmount_B;
SELECT @grossMargin_B;
-- 进行店员B业绩报表的数据结果验证
SELECT 1 INTO @ResultVerification2
FROM t_retailtradedailyreportbystaff 
WHERE F_StaffID = @iStaffBID 
AND F_Datetime = @dSaleDatetime
AND F_TotalAmount = @totalAmount_B
AND F_GrossMargin = @grossMargin_B
AND F_NO = (
	SELECT COUNT(1) 
	FROM t_retailtrade 
	WHERE F_SourceID = -1 
	AND F_StaffID = @iStaffBID
	AND datediff(F_SaleDatetime, @dSaleDatetime) = 0
 );

SELECT IF(@iErrorCode = 0 AND @ResultVerification2 = 1,'测试成功','测试失败') AS 'Test Case13 Result';

DELETE FROM t_retailtradedailyreportbystaff WHERE F_Datetime = @dSaleDatetime;
DELETE FROM t_retailtradeaggregation WHERE F_ID = @rtgID1_B;
DELETE FROM t_retailtradeaggregation WHERE F_ID = @rtgID1_A;
DELETE FROM t_retailtradeaggregation WHERE F_ID = @rtgID2_B;
DELETE FROM t_retailtradeaggregation WHERE F_ID = @rtgID2_A;
DELETE FROM t_retailtradeaggregation WHERE F_ID = @rtgID3_B;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommSourceID1_B;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommSourceID1_A;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommSourceID2_A;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommSourceID3_B;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommSourceID4_B;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommSourceID3_A;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @rtCommSourceID5_B;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @rtcd1_A;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @rtcd1_B;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @rtcd2_A;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @rtcd2_B;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_ID = @rtcd3_A;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtCommID1_B;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rrtCommID1_A;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtCommID1_A;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtCommID2_A;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtCommID3_B;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtCommID4_B;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtCommID3_A;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtCommID5_B;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rrtCommID2_A;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rrtCommID1_B;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rrtCommID2_B;
DELETE FROM t_retailtradecommodity WHERE F_ID = @rrtCommID3_A;
DELETE FROM t_retailtrade WHERE F_ID = @rtID1_B;
DELETE FROM t_retailtrade WHERE F_ID = @rrtID1_A;
DELETE FROM t_retailtrade WHERE F_ID = @rtID1_A;
DELETE FROM t_retailtrade WHERE F_ID = @rtID2_A;
DELETE FROM t_retailtrade WHERE F_ID = @rrtID1_B;
DELETE FROM t_retailtrade WHERE F_ID = @rtID3_B;
DELETE FROM t_retailtrade WHERE F_ID = @rtID4_B;
DELETE FROM t_retailtrade WHERE F_ID = @rtID3_A;
DELETE FROM t_retailtrade WHERE F_ID = @rtID5_B;
DELETE FROM t_retailtrade WHERE F_ID = @rrtID2_A;
DELETE FROM t_retailtrade WHERE F_ID = @rrtID2_B;
DELETE FROM t_retailtrade WHERE F_ID = @rrtID3_A;
DELETE FROM t_warehousingcommodity WHERE F_WarehousingID = @iWarehousingID;
DELETE FROM t_warehousing WHERE F_ID = @iWarehousingID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID1;