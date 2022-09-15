SELECT '++++++++++++++++++Test_SP_RetailTradeDailyReport_Create.sql+++++++++++++++++++++++';

SELECT '-------------------- case1 零售单有销售记录 零售单有：单品、多包装、组合、单品+多包装、单品+组合、多包装+组合 --------------------' AS 'Case1';
-- 当天最高销售额是多包装商品：如果改变当天最高销售额需要改变下面的结果验正
SET @iSubCommodityNO1 = 2; -- 子商品1倍数
SET @iSubCommodityNO2 = 3; -- 子商品2倍数
SET @iSubCommodityPrice1 = 5; -- 子商品1价格
SET @iSubCommodityPrice2 = 7; -- 子商品2价格
SET @iMultiple = 10; -- 多包装倍数
SET @iPriceSimpleCommodity1 = 122; -- 单品1价格
SET @iPriceSimpleCommodity2 = 133; -- 单品2价格
SET @iPriceSimpleCommodity3 = 144; -- 单品3价格
SET @iPriceSimpleCommodity4 = 15; -- 单品4价格
SET @iPriceCompositionCommodity5 = (@iSubCommodityNO1 * @iSubCommodityPrice1 + @iSubCommodityNO2 * @iSubCommodityPrice2); -- 组合5价格
SET @iPriceMutiplePackagingCommodity6 = (@iMultiple * @iPriceSimpleCommodity4); -- 多包装6价格
SET @iWarehousingCommodityPrice1 = 2.1;--  入库商品1价格
SET @iWarehousingCommodityPrice1_1 = 2.2;--  入库商品1_1价格
SET @iWarehousingCommodityPrice2 = 2.3;--  入库商品2价格
SET @iWarehousingCommodityPrice3 = 2.5;--  入库商品3价格
SET @iWarehousingCommodityPrice4 = 3.5;--  入库商品4价格
SET @iWarehousingCommodityNO1 = 40;--  入库商品1入库数量
SET @iWarehousingCommodityNO1_1 = 10;--  入库商品1_1入库数量
SET @iRetailtradeCommodityNO1 = (@iWarehousingCommodityNO1 + @iWarehousingCommodityNO1_1); -- 零售商品1数量
SET @iRetailtradeCommodityNO2 = 10; -- 零售商品2数量
SET @iRetailtradeCommodityNO3 = 10; -- 零售商品3数量
SET @iRetailtradeCommodityNO4 = 10; -- 零售商品4数量
SET @iRetailtradeCommodityNO5 = 10; -- 零售商品5数量
SET @iRetailtradeCommodityNO6 = 30; -- 零售商品6数量
SET @iWarehousingCommodityNO2 = (@iRetailtradeCommodityNO2 * @iSubCommodityNO1 + @iRetailtradeCommodityNO4 + @iRetailtradeCommodityNO5 * @iSubCommodityNO1 + @iRetailtradeCommodityNO6 * @iSubCommodityNO1);--  入库商品2入库数量
SET @iWarehousingCommodityNO3 = (@iRetailtradeCommodityNO2 * @iSubCommodityNO2 + @iRetailtradeCommodityNO5 * @iSubCommodityNO2 + @iRetailtradeCommodityNO5 + @iRetailtradeCommodityNO6 * @iSubCommodityNO2);--  入库商品3入库数量
SET @iWarehousingCommodityNO4 = (@iRetailtradeCommodityNO3 * @iMultiple + @iRetailtradeCommodityNO4 * @iMultiple + @iRetailtradeCommodityNO6 * @iMultiple );--  入库商品4入库数量
SET @iAmount1 = (@iRetailtradeCommodityNO1 * @iPriceSimpleCommodity1); -- 零售单1总金额
SET @iAmount2 = (@iRetailtradeCommodityNO2 * @iPriceSimpleCommodity2); -- 零售单2总金额
SET @iAmount3 = (@iRetailtradeCommodityNO3 * @iPriceSimpleCommodity3); -- 零售单3总金额
SET @iAmount4 = (@iRetailtradeCommodityNO4 * @iPriceSimpleCommodity4); -- 零售单4总金额
SET @iAmount5 = (@iRetailtradeCommodityNO5 * @iPriceCompositionCommodity5); -- 零售单5总金额
SET @iAmount6 = (@iRetailtradeCommodityNO6 * @iPriceMutiplePackagingCommodity6); -- 零售单6总金额
SET @saleDatetime = DATE_ADD(now(),INTERVAL 3000 DAY); -- 销售日期
-- 插入单品1
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0/*F_Status*/, '可比克1111', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 
@iPriceSimpleCommodity1-1,@iPriceSimpleCommodity1-2, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0/*F_Type*/, NULL, '2019-11-22 14:48:19', '2019-11-22 14:48:19', NULL, NULL, NULL, NULL);
SET @iCommodityID1 = last_insert_id();
-- 插入单品2
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0/*F_Status*/, '可比克2222', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 
@iPriceSimpleCommodity2-1,@iPriceSimpleCommodity2-2, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0/*F_Type*/, NULL, '2019-11-22 14:48:19', '2019-11-22 14:48:19', NULL, NULL, NULL, NULL);
SET @iCommodityID2 = last_insert_id();
-- 插入单品3
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0/*F_Status*/, '可比克3333', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 
@iPriceSimpleCommodity3-1,@iPriceSimpleCommodity3-2, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0/*F_Type*/, NULL, '2019-11-22 14:48:19', '2019-11-22 14:48:19', NULL, NULL, NULL, NULL);
SET @iCommodityID3 = last_insert_id();
-- 插入单品4
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0/*F_Status*/, '可比克4444', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 
@iPriceSimpleCommodity4-1,@iPriceSimpleCommodity4-2, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0/*F_Type*/, NULL, '2019-11-22 14:48:19', '2019-11-22 14:48:19', NULL, NULL, NULL, NULL);
SET @iCommodityID4 = last_insert_id();
-- 插入组合商品5
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0/*F_Status*/, '可比克（组合）', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 
@iPriceCompositionCommodity5-1,@iPriceCompositionCommodity5-2, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 1/*F_Type*/, NULL, '2019-11-22 14:48:19', '2019-11-22 14:48:19', NULL, NULL, NULL, NULL);
SET @iCommodityID5 = last_insert_id();
-- 插入多包装商品6
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0/*F_Status*/, '可比克（多包装）', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 
@iPriceMutiplePackagingCommodity6-1,@iPriceMutiplePackagingCommodity6-2, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, @iCommodityID4, @iMultiple, '1111111', 2/*F_Type*/, NULL, '2019-11-22 14:48:19', '2019-11-22 14:48:19', NULL, NULL, NULL, NULL);
SET @iCommodityID6 = last_insert_id();
-- 子商品1
INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iCommodityID5,@iCommodityID2,@iSubCommodityNO1,@iSubCommodityPrice1, '2019-11-22 15:31:38', '2019-11-22 15:31:38');
-- 子商品2
INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iCommodityID5,@iCommodityID3,@iSubCommodityNO2,@iSubCommodityPrice2, '2019-11-22 15:31:38', '2019-11-22 15:31:38');
-- 插入入库单1
INSERT INTO t_warehousing (F_ShopID, F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (2/*F_ShopID*/, 1/*F_Status*/, 'RK201909050001', 1, 1, 4, NULL, '2017-08-06 01:01:01', 1, '2019-11-22 15:31:39');
SET @iWarehousingID1 = last_insert_id();
-- 插入入库单2
INSERT INTO t_warehousing (F_ShopID, F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (2/*F_ShopID*/, 1/*F_Status*/, 'RK201909050002', 1, 1, 4, NULL, '2017-08-06 01:01:01', 1, '2019-11-22 15:31:39');
SET @iWarehousingID2 = last_insert_id();
-- 插入入库单商品1
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID1, @iCommodityID1, @iWarehousingCommodityNO1, 1, '可比克1111', 1, @iWarehousingCommodityPrice1, 2000, '2017-08-06 01:01:01', 36, '2020-08-06 01:01:01', '2019-11-22 15:31:39', '2019-11-22 15:31:39',0/*F_NOSalable*/);
-- 插入入库单商品1_1
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID2, @iCommodityID1, @iWarehousingCommodityNO1_1, 1, '可比克1111', 1, @iWarehousingCommodityPrice1_1, 2000, '2017-08-06 01:01:01', 36, '2020-08-06 01:01:01', '2019-11-22 15:31:39', '2019-11-22 15:31:39',0/*F_NOSalable*/);
-- 插入入库单商品2
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID1, @iCommodityID2, @iWarehousingCommodityNO2, 1, '可比克2222', 1, @iWarehousingCommodityPrice2, 2000, '2017-08-06 01:01:01', 36, '2020-08-06 01:01:01', '2019-11-22 15:31:39', '2019-11-22 15:31:39', 0/*F_NOSalable*/);
-- 插入入库单商品3
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID1, @iCommodityID3, @iWarehousingCommodityNO3, 1, '可比克3333', 1, @iWarehousingCommodityPrice3, 2000, '2017-08-06 01:01:01', 36, '2020-08-06 01:01:01', '2019-11-22 15:31:39', '2019-11-22 15:31:39', 0/*F_NOSalable*/);
-- 插入入库单商品4
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID1, @iCommodityID4, @iWarehousingCommodityNO4, 1, '可比克4444', 1, @iWarehousingCommodityPrice4, 2000, '2017-08-06 01:01:01', 36, '2020-08-06 01:01:01', '2019-11-22 15:31:39', '2019-11-22 15:31:39', 0/*F_NOSalable*/);
-- 插入零售单1：卖单品1(跨库)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019110601010100011201', 1, 1, 'url=ashasoadigmnalskd',DATE_ADD(@saleDatetime,INTERVAL 1 minute), 2, 4, '0', 1, '........', -1/*F_SourceID*/, '2019-11-22 14:48:21', @iAmount1, 0, 0, @iAmount1, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iTradeID1 = last_insert_id();
-- 插入零售单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iTradeID1,@iCommodityID1, '可比克1111', 1, @iRetailtradeCommodityNO1, @iPriceSimpleCommodity1, @iRetailtradeCommodityNO1, @iPriceSimpleCommodity1, 300, @iPriceSimpleCommodity1);
SET @iRetailtradeCommodityID1 = last_insert_id();
-- 插入零售商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailtradeCommodityID1, @iCommodityID1,@iWarehousingCommodityNO1, @iWarehousingID1);
--
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailtradeCommodityID1, @iCommodityID1,@iWarehousingCommodityNO1_1, @iWarehousingID2);
-- 插入零售单2：卖组合商品
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019110601010100011202', 1, 2, 'url=ashasoadigmnalskd',DATE_ADD(@saleDatetime,INTERVAL 1 minute), 2, 4, '0', 1, '........', -1/*F_SourceID*/, '2019-11-22 14:48:21', @iAmount2, 0, 0, @iAmount2, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iTradeID2 = last_insert_id();
-- 插入零售单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iTradeID2,@iCommodityID5, '可比克（组合）', 1, @iRetailtradeCommodityNO2, @iPriceCompositionCommodity5, @iRetailtradeCommodityNO2, @iPriceCompositionCommodity5, 300, @iPriceCompositionCommodity5);
SET @iRetailtradeCommodityID2 = last_insert_id();
-- 插入零售商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailtradeCommodityID2, @iCommodityID2,(@iRetailtradeCommodityNO2 * @iSubCommodityNO1), @iWarehousingID1);
--
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailtradeCommodityID2, @iCommodityID3,(@iRetailtradeCommodityNO2 * @iSubCommodityNO2), @iWarehousingID1);
-- 插入零售单3：卖多包装商品
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019110601010100011203', 1, 3, 'url=ashasoadigmnalskd',DATE_ADD(@saleDatetime,INTERVAL 1 minute), 2, 4, '0', 1, '........', -1/*F_SourceID*/, '2019-11-22 14:48:21', @iAmount3, 0, 0, @iAmount3, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iTradeID3 = last_insert_id();
-- 插入零售单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iTradeID3,@iCommodityID6, '可比克（多包装）', 1, @iRetailtradeCommodityNO3, @iPriceMutiplePackagingCommodity6, @iRetailtradeCommodityNO3, @iPriceMutiplePackagingCommodity6, 300, @iPriceMutiplePackagingCommodity6);
SET @iRetailtradeCommodityID3 = last_insert_id();
-- 插入零售商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailtradeCommodityID3, @iCommodityID4,(@iRetailtradeCommodityNO3 * @iMultiple), @iWarehousingID1);
-- 插入零售单4：卖单品2 + 多包装商品
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019110601010100011204', 1, 4, 'url=ashasoadigmnalskd',DATE_ADD(@saleDatetime,INTERVAL 1 minute), 2, 4, '0', 1, '........', -1/*F_SourceID*/, '2019-11-22 14:48:21', @iAmount4, 0, 0, @iAmount4, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iTradeID4 = last_insert_id();
-- 插入零售单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iTradeID4,@iCommodityID2, '可比克2222', 1, @iRetailtradeCommodityNO4, @iPriceSimpleCommodity2, @iRetailtradeCommodityNO4, @iPriceSimpleCommodity2, 300, @iPriceSimpleCommodity2);
SET @iRetailtradeCommodityID4 = last_insert_id();
--
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iTradeID4,@iCommodityID6, '可比克（多包装）', 1, @iRetailtradeCommodityNO4, @iPriceMutiplePackagingCommodity6, @iRetailtradeCommodityNO4, @iPriceMutiplePackagingCommodity6, 300, @iPriceMutiplePackagingCommodity6);
SET @iRetailtradeCommodityID4_1 = last_insert_id();
-- 插入零售商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailtradeCommodityID4, @iCommodityID2,@iRetailtradeCommodityNO4, @iWarehousingID1);
--
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailtradeCommodityID4_1, @iCommodityID4,(@iRetailtradeCommodityNO4 * @iMultiple), @iWarehousingID1);
-- 插入零售单5：卖组合商品 + 单品3
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019110601010100011205', 1, 5, 'url=ashasoadigmnalskd',DATE_ADD(@saleDatetime,INTERVAL 1 minute), 2, 4, '0', 1, '........', -1/*F_SourceID*/, '2019-11-22 14:48:21', @iAmount5, 0, 0, @iAmount5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iTradeID5 = last_insert_id();
-- 插入零售单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iTradeID5,@iCommodityID5, '可比克（组合）', 1, @iRetailtradeCommodityNO5, @iPriceCompositionCommodity5, @iRetailtradeCommodityNO5, @iPriceCompositionCommodity5, 300, @iPriceCompositionCommodity5);
SET @iRetailtradeCommodityID5 = last_insert_id();
--
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iTradeID5,@iCommodityID3, '可比克3333', 1, @iRetailtradeCommodityNO5, @iPriceSimpleCommodity3, @iRetailtradeCommodityNO5, @iPriceSimpleCommodity3, 300, @iPriceSimpleCommodity3);
SET @iRetailtradeCommodityID5_1 = last_insert_id();
-- 插入零售商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailtradeCommodityID5, @iCommodityID2,(@iRetailtradeCommodityNO5 * @iSubCommodityNO1), @iWarehousingID1);
--
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailtradeCommodityID5, @iCommodityID3,(@iRetailtradeCommodityNO5 * @iSubCommodityNO2), @iWarehousingID1);
--
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailtradeCommodityID5_1, @iCommodityID3,@iRetailtradeCommodityNO5, @iWarehousingID1);
-- 插入零售单6：卖组合商品 + 多包装商品
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019110601010100011206', 1,6, 'url=ashasoadigmnalskd',DATE_ADD(@saleDatetime,INTERVAL 1 minute), 2, 4, '0', 1, '........', -1/*F_SourceID*/, '2019-11-22 14:48:21', @iAmount6, 0, 0, @iAmount6, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iTradeID6 = last_insert_id();
-- 插入零售单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iTradeID6,@iCommodityID5, '可比克（组合）', 1, @iRetailtradeCommodityNO6, @iPriceCompositionCommodity5, @iRetailtradeCommodityNO6, @iPriceCompositionCommodity5, 300, @iPriceCompositionCommodity5);
SET @iRetailtradeCommodityID6 = last_insert_id();
--
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iTradeID6,@iCommodityID6, '可比克（多包装）', 1, @iRetailtradeCommodityNO6, @iPriceMutiplePackagingCommodity6, @iRetailtradeCommodityNO6, @iPriceMutiplePackagingCommodity6, 300, @iPriceMutiplePackagingCommodity6);
SET @iRetailtradeCommodityID6_1 = last_insert_id();
-- 插入零售商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailtradeCommodityID6, @iCommodityID2,(@iRetailtradeCommodityNO6 * @iSubCommodityNO1), @iWarehousingID1);
--
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailtradeCommodityID6, @iCommodityID3,(@iRetailtradeCommodityNO6 * @iSubCommodityNO2), @iWarehousingID1);
--
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailtradeCommodityID6_1, @iCommodityID4,(@iRetailtradeCommodityNO6 * @iMultiple), @iWarehousingID1);
--
SET @dSaleDatetime = DATE_FORMAT(@saleDatetime,'%Y/%m/%d');-- 销售日期
SET @iTotalNO = (SELECT count(F_ID) FROM t_retailtrade WHERE DATE_FORMAT(F_SaleDatetime,'%Y/%m/%d') = @dSaleDatetime);
SET @iPricePurchase = (@iWarehousingCommodityNO1 * @iWarehousingCommodityPrice1 + @iWarehousingCommodityNO1_1 * @iWarehousingCommodityPrice1_1
                    + @iWarehousingCommodityNO2 * @iWarehousingCommodityPrice2 + @iWarehousingCommodityNO3 * @iWarehousingCommodityPrice3
                    + @iWarehousingCommodityNO4 * @iWarehousingCommodityPrice4);-- 一天销售商品的总进货价                    
SET @iTotalAmount = (@iAmount1 + @iAmount2 + @iAmount3 + @iAmount4 + @iAmount5 + @iAmount6);-- 当天销售额
SET @iTopSaleCommodityAmount = (@iWarehousingCommodityNO4 * @iPriceSimpleCommodity4);-- 当天最高销售额
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

SELECT IF(@iErrorCode = 0 AND @found_row1 <> 0 AND @found_row2 <> 0,'测试成功','测试失败') AS 'Test Case1 Result';

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

SELECT '-------------------- case2 零售单没有销售记录 --------------------' AS 'Case2';
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
SELECT IF(@iErrorCode = 0 AND @found_row1 = 0 AND @found_row2 = 0,'测试成功','测试失败') AS 'Test Case2 Result';

SELECT '-------------------- case3 退货相关报表  零售单有:单品、多包装、组合 退货单有:单品(退全部,当天)、多包装(退部分,当天)、组合商品(退部分,隔天) --------------------' AS 'Case3';
--  当天最高销售额是组合商品：如果改变当天最高销售额需要改变下面的结果验证
SET @iSubCommodityNO1 = 3; -- 子商品1倍数
SET @iSubCommodityNO2 = 4; -- 子商品2倍数
SET @iSubCommodityPrice1 = 5.79; -- 子商品1价格
SET @iSubCommodityPrice2 = 7.71; -- 子商品2价格
SET @iMultiple = 10; -- 多包装倍数
SET @iPriceSimpleCommodity1= 182; -- 单品1价格
SET @iPriceSimpleCommodity2= 173; -- 单品2价格
SET @iPriceSimpleCommodity3= 144; -- 单品3价格
SET @iPriceSimpleCommodity4= 15; -- 单品4价格
SET @iPriceCompositionCommodity5= (@iSubCommodityNO1 * @iSubCommodityPrice1 + @iSubCommodityNO2 * @iSubCommodityPrice2); -- 组合5价格
SET @iPriceMutiplePackagingCommodity6= (@iMultiple * @iPriceSimpleCommodity4); -- 多包装6价格
SET @iWarehousingCommodityPrice1 = 2.2;--  入库商品1价格
SET @iWarehousingCommodityPrice1_1 = 2.33;--  入库商品1_1价格
SET @iWarehousingCommodityPrice2 = 2.53;--  入库商品2价格
SET @iWarehousingCommodityPrice3 = 2.62;--  入库商品3价格
SET @iWarehousingCommodityPrice4 = 3.56;--  入库商品4价格
SET @iWarehousingCommodityNO1 = 40;--  入库商品1入库数量
SET @iWarehousingCommodityNO1_1 = 10;--  入库商品1_1入库数量
SET @iRetailtradeCommodityNO1 = (@iWarehousingCommodityNO1 + @iWarehousingCommodityNO1_1); -- 零售商品1数量
SET @iRetailtradeCommodityNO2 = 50; -- 零售商品2数量
SET @iRetailtradeCommodityNO2_1 = 5; -- 退货商品2数量
SET @iRetailtradeCommodityNO3 = 10; -- 零售商品3数量
SET @iRetailtradeCommodityNO3_1 = 5; -- 退货商品3数量
SET @iWarehousingCommodityNO2 = (@iRetailtradeCommodityNO2 * @iSubCommodityNO1);--  入库商品2入库数量
SET @iWarehousingCommodityNO3 = (@iRetailtradeCommodityNO2 * @iSubCommodityNO2);--  入库商品3入库数量
SET @iWarehousingCommodityNO4 = ((@iRetailtradeCommodityNO3 - @iRetailtradeCommodityNO3_1) * @iMultiple);--  入库商品4入库数量
SET @iNOSalable2 = (@iRetailtradeCommodityNO2 * @iSubCommodityNO1); -- 入库商品2可售数量
SET @iNOSalable3 = (@iRetailtradeCommodityNO2 * @iSubCommodityNO2); -- 入库商品3可售数量
SELECT @iWarehousingCommodityNO1,@iWarehousingCommodityNO1_1,@iWarehousingCommodityNO2,@iWarehousingCommodityNO3,@iWarehousingCommodityNO4;
SET @iAmount1 = (@iRetailtradeCommodityNO1 * @iPriceSimpleCommodity1); -- 零售单1总金额
SET @iAmount1_1 = @iAmount1; -- 退货单1总金额
SET @iAmount2 = (@iRetailtradeCommodityNO2 * @iPriceSimpleCommodity2); -- 零售单2总金额
SET @iAmount2_1 = (@iRetailtradeCommodityNO2_1 * @iPriceSimpleCommodity2); -- 退货单2总金额
SET @iAmount3 = (@iRetailtradeCommodityNO3 * @iPriceSimpleCommodity3); -- 零售单3总金额
SET @iAmount3_1 =(@iRetailtradeCommodityNO3_1 * @iPriceSimpleCommodity3) ; -- 退货单3总金额
SET @saleDatetime = DATE_ADD(now(),INTERVAL 3000 DAY); -- 销售日期
-- 插入单品1
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0/*F_Status*/, '可比克1111', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 
@iPriceSimpleCommodity1-1,@iPriceSimpleCommodity1-2, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0/*F_Type*/, NULL, '2019-11-22 14:48:19', '2019-11-22 14:48:19', NULL, NULL, NULL, NULL);
SET @iCommodityID1 = last_insert_id();
-- 插入单品2
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0/*F_Status*/, '可比克2222', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 
@iPriceSimpleCommodity2-1,@iPriceSimpleCommodity2-2, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0/*F_Type*/, NULL, '2019-11-22 14:48:19', '2019-11-22 14:48:19', NULL, NULL, NULL, NULL);
SET @iCommodityID2 = last_insert_id();
-- 插入单品3
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0/*F_Status*/, '可比克3333', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 
@iPriceSimpleCommodity3-1,@iPriceSimpleCommodity3-2, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0/*F_Type*/, NULL, '2019-11-22 14:48:19', '2019-11-22 14:48:19', NULL, NULL, NULL, NULL);
SET @iCommodityID3 = last_insert_id();
-- 插入单品4
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0/*F_Status*/, '可比克4444', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 
@iPriceSimpleCommodity4-1,@iPriceSimpleCommodity4-2, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0/*F_Type*/, NULL, '2019-11-22 14:48:19', '2019-11-22 14:48:19', NULL, NULL, NULL, NULL);
SET @iCommodityID4 = last_insert_id();
-- 插入组合商品5
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0/*F_Status*/, '可比克（组合）', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 
@iPriceCompositionCommodity5-1,@iPriceCompositionCommodity5-2, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 1/*F_Type*/, NULL, '2019-11-22 14:48:19', '2019-11-22 14:48:19', NULL, NULL, NULL, NULL);
SET @iCommodityID5 = last_insert_id();
-- 插入多包装商品6
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0/*F_Status*/, '可比克（多包装）', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 
@iPriceMutiplePackagingCommodity6-1,@iPriceMutiplePackagingCommodity6-2, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, @iCommodityID4, @iMultiple, '1111111', 2/*F_Type*/, NULL, '2019-11-22 14:48:19', '2019-11-22 14:48:19', NULL, NULL, NULL, NULL);
SET @iCommodityID6 = last_insert_id();
-- 子商品1
INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iCommodityID5,@iCommodityID2,@iSubCommodityNO1,@iSubCommodityPrice1, '2019-11-22 15:31:38', '2019-11-22 15:31:38');
-- 子商品2
INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iCommodityID5,@iCommodityID3,@iSubCommodityNO2,@iSubCommodityPrice2, '2019-11-22 15:31:38', '2019-11-22 15:31:38');
-- 插入入库单1
INSERT INTO t_warehousing (F_ShopID, F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (2, 1/*F_Status*/, 'RK201909050001', 1, 1, 4, NULL, '2017-08-06 01:01:01', 1, '2019-11-22 15:31:39');
SET @iWarehousingID1 = last_insert_id();
-- 插入入库单2
INSERT INTO t_warehousing (F_ShopID, F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (2, 1/*F_Status*/, 'RK201909050002', 1, 1, 4, NULL, '2017-08-06 01:01:01', 1, '2019-11-22 15:31:39');
SET @iWarehousingID2 = last_insert_id();
-- 插入入库单商品1
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID1, @iCommodityID1, @iWarehousingCommodityNO1, 1, '可比克1111', 1, @iWarehousingCommodityPrice1, 2000, '2017-08-06 01:01:01', 36, '2020-08-06 01:01:01', '2019-11-22 15:31:39', '2019-11-22 15:31:39',@iWarehousingCommodityNO1);
-- 插入入库单商品1_1
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID2, @iCommodityID1, @iWarehousingCommodityNO1_1, 1, '可比克1111', 1, @iWarehousingCommodityPrice1_1, 2000, '2017-08-06 01:01:01', 36, '2020-08-06 01:01:01', '2019-11-22 15:31:39', '2019-11-22 15:31:39',@iWarehousingCommodityNO1_1);
-- 插入入库单商品2
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID1, @iCommodityID2, @iWarehousingCommodityNO2, 1, '可比克2222', 1, @iWarehousingCommodityPrice2, 2000, '2017-08-06 01:01:01', 36, '2020-08-06 01:01:01', '2019-11-22 15:31:39', '2019-11-22 15:31:39', @iNOSalable2);
-- 插入入库单商品3
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID1, @iCommodityID3, @iWarehousingCommodityNO3, 1, '可比克3333', 1, @iWarehousingCommodityPrice3, 2000, '2017-08-06 01:01:01', 36, '2020-08-06 01:01:01', '2019-11-22 15:31:39', '2019-11-22 15:31:39', @iNOSalable3);
-- 插入入库单商品4
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID1, @iCommodityID4, @iWarehousingCommodityNO4, 1, '可比克4444', 1, @iWarehousingCommodityPrice4, 2000, '2017-08-06 01:01:01', 36, '2020-08-06 01:01:01', '2019-11-22 15:31:39', '2019-11-22 15:31:39', 0);
-- 插入零售单1：卖单品1(跨库)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019110601010100011201', 1, 1, 'url=ashasoadigmnalskd',DATE_ADD(@saleDatetime,INTERVAL 1 minute), 2, 4, '0', 1, '........', -1/*F_SourceID*/, '2019-11-22 14:48:21', @iAmount1, 0, 0, @iAmount1, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iTradeID1 = last_insert_id();
-- 插入零售单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iTradeID1,@iCommodityID1, '可比克1111', 1, @iRetailtradeCommodityNO1, @iPriceSimpleCommodity1,0, @iPriceSimpleCommodity1, 300, @iPriceSimpleCommodity1);
SET @iRetailtradeCommodityID1 = last_insert_id();
-- 插入零售商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailtradeCommodityID1, @iCommodityID1,@iWarehousingCommodityNO1, @iWarehousingID1);
--
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailtradeCommodityID1, @iCommodityID1,@iWarehousingCommodityNO1_1, @iWarehousingID2);
-- 插入退货单1：(当天)退单品1全部(跨库)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019110601010100011201_1', 1, 4, 'url=ashasoadigmnalskd',DATE_ADD(@saleDatetime,INTERVAL 1 minute), 2, 4, '0', 1, '........',@iTradeID1, '2019-11-22 14:48:21', @iAmount1_1, 0, 0, @iAmount1_1, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iTradeID1_1 = last_insert_id();
-- 插入退货单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iTradeID1_1,@iCommodityID1, '可比克1111', 1, @iRetailtradeCommodityNO1, @iPriceSimpleCommodity1, 0, @iPriceSimpleCommodity1, 300, @iPriceSimpleCommodity1);
SET @iRetailtradeCommodityID1_1 = last_insert_id();
-- 插入退货去向表
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailtradeCommodityID1_1, @iCommodityID1,@iWarehousingCommodityNO1, @iWarehousingID1);
--
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailtradeCommodityID1_1, @iCommodityID1,@iWarehousingCommodityNO1_1, @iWarehousingID2);
-- 插入零售单2：卖组合商品
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019110601010100011202', 1, 2, 'url=ashasoadigmnalskd',DATE_ADD(@saleDatetime,INTERVAL 1 minute), 2, 4, '0', 1, '........', -1/*F_SourceID*/, '2019-11-22 14:48:21', @iAmount2, 0, 0, @iAmount2, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iTradeID2 = last_insert_id();
-- 插入零售单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iTradeID2,@iCommodityID5, '可比克（组合）', 1, @iRetailtradeCommodityNO2, @iPriceCompositionCommodity5, (@iRetailtradeCommodityNO2-@iRetailtradeCommodityNO2_1), @iPriceCompositionCommodity5, 300, @iPriceCompositionCommodity5);
SET @iRetailtradeCommodityID2 = last_insert_id();
-- 插入零售商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailtradeCommodityID2, @iCommodityID2,(@iRetailtradeCommodityNO2 * @iSubCommodityNO1), @iWarehousingID1);
--
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailtradeCommodityID2, @iCommodityID3,(@iRetailtradeCommodityNO2 * @iSubCommodityNO2), @iWarehousingID1);
-- 插入退货单2：(部分)退组合商品(隔天)
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019110601010100011202_1', 1,5, 'url=ashasoadigmnalskd',DATE_ADD(@saleDatetime,INTERVAL 2 DAY), 2, 4, '0', 1, '........',@iTradeID2, '2019-11-22 14:48:21', @iAmount2_1, 0, 0, @iAmount2_1, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iTradeID2_1 = last_insert_id();
-- 插入退货单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iTradeID2_1,@iCommodityID5, '可比克（组合）', 1, @iRetailtradeCommodityNO2_1, @iPriceCompositionCommodity5, 0, @iPriceCompositionCommodity5, 300, @iPriceCompositionCommodity5);
SET @iRetailtradeCommodityID2_1 = last_insert_id();
-- 插入退货去向表
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailtradeCommodityID2_1, @iCommodityID2,(@iRetailtradeCommodityNO2_1 * @iSubCommodityNO1), @iWarehousingID1);
--
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailtradeCommodityID2_1, @iCommodityID3,(@iRetailtradeCommodityNO2_1 * @iSubCommodityNO2), @iWarehousingID1);
-- 插入零售单3：卖多包装商品
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019110601010100011203', 1, 3, 'url=ashasoadigmnalskd',DATE_ADD(@saleDatetime,INTERVAL 1 minute), 2, 4, '0', 1, '........', -1/*F_SourceID*/, '2019-11-22 14:48:21', @iAmount3, 0, 0, @iAmount3, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iTradeID3 = last_insert_id();
-- 插入零售单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iTradeID3,@iCommodityID6, '可比克（多包装）', 1, @iRetailtradeCommodityNO3, @iPriceMutiplePackagingCommodity6,  (@iRetailtradeCommodityNO3-@iRetailtradeCommodityNO3_1), @iPriceMutiplePackagingCommodity6, 300, @iPriceMutiplePackagingCommodity6);
SET @iRetailtradeCommodityID3 = last_insert_id();
-- 插入零售商品来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailtradeCommodityID3, @iCommodityID4,(@iRetailtradeCommodityNO3 * @iMultiple), @iWarehousingID1);
-- 插入退货单3：退多包装商品（当天部分）
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019110601010100011203_1', 1,6, 'url=ashasoadigmnalskd',DATE_ADD(@saleDatetime,INTERVAL 1 minute), 2, 4, '0', 1, '........',@iTradeID3, '2019-11-22 14:48:21', @iAmount3_1, 0, 0, @iAmount3_1, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iTradeID3_1 = last_insert_id();
-- 插入退货单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iTradeID3_1,@iCommodityID6, '可比克（多包装）', 1, @iRetailtradeCommodityNO3_1, @iPriceMutiplePackagingCommodity6, 0, @iPriceMutiplePackagingCommodity6, 300, @iPriceMutiplePackagingCommodity6);
SET @iRetailtradeCommodityID3_1 = last_insert_id();
-- 插入退货去向表
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iRetailtradeCommodityID3_1, @iCommodityID4,(@iRetailtradeCommodityNO3_1 * @iMultiple), @iWarehousingID1);
SET @dSaleDatetime = DATE_FORMAT(@saleDatetime,'%Y/%m/%d');-- 销售日期

SET @iTotalNO = (SELECT count(F_ID) FROM t_retailtrade WHERE DATE_FORMAT(F_SaleDatetime,'%Y/%m/%d') = @dSaleDatetime AND F_SourceID = -1);

SET @iPricePurchase = (@iWarehousingCommodityNO2 * @iWarehousingCommodityPrice2 + @iWarehousingCommodityNO3 * @iWarehousingCommodityPrice3
                       + @iWarehousingCommodityNO4 * @iWarehousingCommodityPrice4);-- 一天销售商品的总进货价
                    
SET @iTotalAmount = (@iAmount2 + @iAmount3-@iAmount3_1);-- 当天销售额
SET @iTopSaleCommodityAmount = ((@iRetailtradeCommodityNO2 - @iRetailtradeCommodityNO2_1)  * @iPriceCompositionCommodity5);-- 当天最高销售额
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

SELECT IF(@iErrorCode = 0 AND @found_row1 <> 0 AND @found_row2 <> 0,'测试成功','测试失败') AS 'Test Case1 Result';
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

SELECT '-------------------- Case4：当天存在未入库商品 -------------------------' AS 'Case4';
SET @iPriceRetail = 19; -- 商品价格
SET @iRetailtradeCommodityNO = 100; -- 零售商品数量
SET @iAmount = (@iRetailtradeCommodityNO * @iPriceRetail); -- 零售单金额
SET @saleDatetime = DATE_ADD(now(),INTERVAL 3000 DAY); -- 销售日期
-- 插入商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0/*F_Status*/, '可比克1111', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 
@iPriceRetail-1,@iPriceRetail-2, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0/*F_Type*/, NULL, '2019-11-22 14:48:19', '2019-11-22 14:48:19', NULL, NULL, NULL, NULL);
SET @iCommodityID = last_insert_id();
-- 插入零售单
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_ShopID)
VALUES (1, 'LS2019080601010100011201', 1, 1, 'url=ashasoadigmnalskd',DATE_ADD(@saleDatetime,INTERVAL 1 minute), 2, 4, '0', 1, '........', -1/*F_SourceID*/, '2019-11-22 14:48:21', @iAmount, 0, 0, @iAmount, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL,2);
SET @iTradeID = last_insert_id();
-- 插入零售单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iTradeID, 1, '可比克1111', 1, @iRetailtradeCommodityNO, @iPriceRetail, @iRetailtradeCommodityNO, @iPriceRetail, 300, @iPriceRetail);
SET @iRetailtradeCommodityID = last_insert_id();
-- 插入零售商品来源
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
SELECT IF(@iErrorCode = 0 AND @found_row1 <> 0 AND @found_row2 <> 0,'测试成功','测试失败') AS 'Test Case4 Result';
DELETE FROM t_retailtradedailyreportsummary WHERE F_Datetime = @dSaleDatetime;
DELETE FROM t_retailtradedailyreportbycommodity WHERE F_Datetime = @dSaleDatetime;
DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @iRetailtradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @iTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
--	-- ... 将来再根据Java层的测试代码增加测试用例。


SELECT '-------------------- Case5：当天没有任何零售记录进行创建日报表 -------------------------' AS 'Case4';
SET @dSaleDatetime = '2022-09-05';
SET @iShopID = 2;
SET @sErrorMsg = '';
SET @iErrorCode = 0;
SET @deleteOldData = 0;
CALL SP_RetailTradeDailyReport_Create(@iErrorCode, @sErrorMsg, @iShopID, @dSaleDatetime, @deleteOldData);

SELECT 1 FROM t_retailtradedailyreportsummary WHERE F_Datetime = @dSaleDatetime;
SET @found_row1 = found_rows();

DELETE FROM t_retailtradedailyreportsummary WHERE F_Datetime = @dSaleDatetime;