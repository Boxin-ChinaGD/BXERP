SELECT '++++++++++++++++++ Test_SPD_Commodity_CheckNO.sql ++++++++++++++++++++';
SELECT '------------------ 正常测试 --------------------' AS 'CASE1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckNO(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';
--
SELECT '------------------ 多包装商品库存不为0 --------------------' AS 'CASE2';
-- 插入商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, F_LatestPricePurchase, F_PriceRetail, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_NO, F_Type, F_NOStart, F_PurchasingPriceStart, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4, F_CurrentWarehousingID)
VALUES (0/*F_Status*/, '可比克薯片1', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 8, 12, 11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0/* F_NO */, 0/*F_Type*/, -1, -1, NULL, '2019-10-29 16:28:25', '2019-10-29 16:28:25', NULL, NULL, NULL, NULL, NULL);
SET @iCommodityID = LAST_INSERT_ID();
-- 插入多包装商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, F_LatestPricePurchase, F_PriceRetail, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_NO, F_Type, F_NOStart, F_PurchasingPriceStart, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4, F_CurrentWarehousingID)
VALUES (0/*F_Status*/, '可比克薯片111', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 8, 12, 11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, @iCommodityID, 5/*F_RefCommodityMultiple*/, '1111111', 7/* F_NO */, 2/*F_Type*/, -1, -1, NULL, '2019-10-29 16:28:25', '2019-10-29 16:28:25', NULL, NULL, NULL, NULL, NULL);
SET @iCommodityID1 = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
--  
CALL SPD_Commodity_CheckNO(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
--  
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = CONCAT('ID为',@iCommodityID1, '多包装,组合,服务型商品的商品库存只能为0'), '测试成功', '测试失败') AS 'Case2 Testing Result';
--  
DELETE FROM t_commodity WHERE F_ID IN (@iCommodityID1,@iCommodityID) ;
--
SELECT '------------------ 组合商品库存不为0 --------------------' AS 'CASE3';
-- 插入商品1
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, F_LatestPricePurchase, F_PriceRetail, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_NO, F_Type, F_NOStart, F_PurchasingPriceStart, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4, F_CurrentWarehousingID)
VALUES (0/*F_Status*/, '可比克薯片1', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 8, 12, 11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0/* F_NO */, 0/*F_Type*/, -1, -1, NULL, '2019-10-29 16:28:25', '2019-10-29 16:28:25', NULL, NULL, NULL, NULL, NULL);
SET @iCommodityID1 = LAST_INSERT_ID();
-- 插入商品2
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, F_LatestPricePurchase, F_PriceRetail, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_NO, F_Type, F_NOStart, F_PurchasingPriceStart, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4, F_CurrentWarehousingID)
VALUES (0/*F_Status*/, '可比克薯片2', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 8, 12, 11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0/* F_NO */, 0/*F_Type*/, -1, -1, NULL, '2019-10-29 16:28:25', '2019-10-29 16:28:25', NULL, NULL, NULL, NULL, NULL);
SET @iCommodityID2 = LAST_INSERT_ID();
-- 插入组合商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, F_LatestPricePurchase, F_PriceRetail, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_NO, F_Type, F_NOStart, F_PurchasingPriceStart, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4, F_CurrentWarehousingID)
VALUES (0/*F_Status*/, '可比克薯片111', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 8, 12, 11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 7/* F_NO */, 1/*F_Type*/, -1, -1, NULL, '2019-10-29 16:28:25', '2019-10-29 16:28:25', NULL, NULL, NULL, NULL, NULL);
SET @iCommodityID3 = LAST_INSERT_ID();
-- 
INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iCommodityID3, @iCommodityID1, 5/*F_SubCommodityNO*/, 8, '2019-11-22 12:12:47', '2019-11-22 12:12:47');
--
INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iCommodityID3, @iCommodityID2, 4/*F_SubCommodityNO*/, 8, '2019-11-22 12:12:47', '2019-11-22 12:12:47');

SET @iErrorCode = 0;
SET @sErrorMsg = '';
--  
CALL SPD_Commodity_CheckNO(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
--  
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = CONCAT('ID为',@iCommodityID3, '多包装,组合,服务型商品的商品库存只能为0'), '测试成功', '测试失败') AS 'Case3 Testing Result';
--  
DELETE FROM t_subcommodity WHERE F_CommodityID = @iCommodityID3;
DELETE FROM t_commodity WHERE F_ID IN (@iCommodityID3,@iCommodityID1,@iCommodityID2) ;
--
SELECT '------------------ 服务型商品库存不为0 --------------------' AS 'CASE4';
-- 插入服务型商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, F_LatestPricePurchase, F_PriceRetail, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_NO, F_Type, F_NOStart, F_PurchasingPriceStart, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4, F_CurrentWarehousingID)
VALUES (0/*F_Status*/, '可比克薯片111', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 8, 12, 11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 7/* F_NO */, 3/*F_Type*/, -1, -1, NULL, '2019-10-29 16:28:25', '2019-10-29 16:28:25', NULL, NULL, NULL, NULL, NULL);
SET @iCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
--  
CALL SPD_Commodity_CheckNO(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
--  
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = CONCAT('ID为',@iCommodityID, '多包装,组合,服务型商品的商品库存只能为0'), '测试成功', '测试失败') AS 'Case4 Testing Result';
--  
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
--
SELECT '------------------ 商品类型不正确 --------------------' AS 'CASE5';
-- 插入商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, F_LatestPricePurchase, F_PriceRetail, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_NO, F_Type, F_NOStart, F_PurchasingPriceStart, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4, F_CurrentWarehousingID)
VALUES (0/*F_Status*/, '可比克薯片111', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 8, 12, 11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 7/* F_NO */, 10/*F_Type*/, -1, -1, NULL, '2019-10-29 16:28:25', '2019-10-29 16:28:25', NULL, NULL, NULL, NULL, NULL);
SET @iCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
--  
CALL SPD_Commodity_CheckNO(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
--  
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = CONCAT('ID为',@iCommodityID, '的商品类型不正确'), '测试成功', '测试失败') AS 'Case5 Testing Result';
--  
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
--
SELECT '------------------ 检查未入库商品库存不正确 --------------------' AS 'CASE6';
-- 插入商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, F_LatestPricePurchase, F_PriceRetail, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_NO, F_Type, F_NOStart, F_PurchasingPriceStart, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4, F_CurrentWarehousingID)
VALUES (0/*F_Status*/, '可比克薯片222', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 8, 12, 11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 7/* F_NO */, 0/*F_Type*/, -1, -1, NULL, '2019-10-29 16:28:25', '2019-10-29 16:28:25', NULL, NULL, NULL, NULL, NULL);
SET @iCommodityID = LAST_INSERT_ID();
-- 插入零售单
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (1, 'LS200380601010100011234', 1, 1, 'url=ashasoadigmnalskd', '2017-08-16', 2, 4, '0', 1, '........', -1, '2019-10-29 16:28:28', 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iTradeID = LAST_INSERT_ID();
-- 插入零售单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iTradeID, @iCommodityID, '可比克薯片222', 1, 30, 321, 500, 300, 300, NULL);
SET @iTradeCommodityID = LAST_INSERT_ID();
-- 插入零售单来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iTradeCommodityID, @iCommodityID, 30, NULL);
-- 插入退货单
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (4, 'LS200380601010100011234_1', 1, 1, 'url=ashasoadigmnalskd', '2017-08-26', 2, 4, '0', 1, '........', @iTradeID, '2019-10-29 16:28:28', 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iReturnTradeID = LAST_INSERT_ID();
-- 插入退货单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnTradeID, @iCommodityID, '可比克薯片222', 1, 10, 321, 500, 300, 300, NULL);
SET @iReturnTradeCommodityID = LAST_INSERT_ID();
-- 插入退货去向表
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iReturnTradeCommodityID, @iCommodityID, 10, NULL);
SET @iErrorCode = 0;
SET @sErrorMsg = '';
--  
CALL SPD_Commodity_CheckNO(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
--  
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = CONCAT('ID为',@iCommodityID, '的商品,是未入库商品,商品库存 = 去向表F_NO - 来源表F_NO'), '测试成功', '测试失败') AS 'Case6 Testing Result';
--  
DELETE FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID = @iReturnTradeCommodityID;
DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @iTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID IN (@iTradeCommodityID,@iReturnTradeCommodityID);
DELETE FROM t_retailtrade WHERE F_ID IN (@iTradeID,@iReturnTradeID);
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
--
SELECT '------------------ 检查入库单已审核的入库商品库存不正确 --------------------' AS 'CASE7';
-- 插入商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, F_LatestPricePurchase, F_PriceRetail, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_NO, F_Type, F_NOStart, F_PurchasingPriceStart, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4, F_CurrentWarehousingID)
VALUES (0/*F_Status*/, '可比克薯片222', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 8, 12, 11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 7/* F_NO */, 0/*F_Type*/, -1, -1, NULL, '2019-10-29 16:28:25', '2019-10-29 16:28:25', NULL, NULL, NULL, NULL, NULL);
SET @iCommodityID = LAST_INSERT_ID();
-- 插入入库单
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (1/*F_Status*/, 'RK201909050001', 1, 1, 4, NULL, '2017-08-06 01:01:01', 1, '2019-10-29 16:28:27');
SET @iWarehousingID = LAST_INSERT_ID();
-- 插入入库单商品
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID, @iCommodityID, 10, 1, '可比克薯片222', 1, 10, 2000, '2017-08-06 01:01:01', 36, '2020-08-06 01:01:01', '2019-10-29 16:28:27', '2019-10-29 16:28:27', 50);
-- 插入零售单
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (1, 'LS200380601010100011234', 1, 1, 'url=ashasoadigmnalskd', '2017-08-16', 2, 4, '0', 1, '........', -1, '2019-10-29 16:28:28', 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iTradeID = LAST_INSERT_ID();
-- 插入零售单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iTradeID, @iCommodityID, '可比克薯片222', 1, 30, 321, 500, 300, 300, NULL);
SET @iTradeCommodityID = LAST_INSERT_ID();
-- 插入零售单来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iTradeCommodityID, @iCommodityID, 30, @iWarehousingID);
-- 插入退货单
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (4, 'LS200380601010100011234_1', 1, 1, 'url=ashasoadigmnalskd', '2017-08-26', 2, 4, '0', 1, '........', @iTradeID, '2019-10-29 16:28:28', 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iReturnTradeID = LAST_INSERT_ID();
-- 插入退货单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnTradeID, @iCommodityID, '可比克薯片222', 1, 10, 321, 500, 300, 300, NULL);
SET @iReturnTradeCommodityID = LAST_INSERT_ID();
-- 插入退货去向表
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iReturnTradeCommodityID, @iCommodityID, 10, @iWarehousingID);
SET @iErrorCode = 0;
SET @sErrorMsg = '';
--  
CALL SPD_Commodity_CheckNO(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
--  
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = CONCAT('ID为',@iCommodityID, '的商品,入库单有已审核和未审核的商品,商品库存 = 入库单已审核商品F_NO + 去向表F_NO - 来源表F_NO'), '测试成功', '测试失败') AS 'Case7 Testing Result';
--  
DELETE FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID = @iReturnTradeCommodityID;
DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @iTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID IN (@iTradeCommodityID,@iReturnTradeCommodityID);
DELETE FROM t_retailtrade WHERE F_ID IN (@iTradeID,@iReturnTradeID);
DELETE FROM t_warehousingcommodity WHERE F_WarehousingID = @iWarehousingID;
DELETE FROM t_warehousing WHERE F_ID = @iWarehousingID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

--
SELECT '------------------ 检查入库单有已审核和未审核的同一个入库商品库存不正确 --------------------' AS 'CASE8';
-- 插入商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, F_LatestPricePurchase, F_PriceRetail, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_NO, F_Type, F_NOStart, F_PurchasingPriceStart, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4, F_CurrentWarehousingID)
VALUES (0/*F_Status*/, '可比克薯片222', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 8, 12, 11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 7/* F_NO */, 0/*F_Type*/, -1, -1, NULL, '2019-10-29 16:28:25', '2019-10-29 16:28:25', NULL, NULL, NULL, NULL, NULL);
SET @iCommodityID = LAST_INSERT_ID();
-- 插入已审核入库单
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (1/*F_Status*/, 'RK201909050001', 1, 1, 4, NULL, '2017-08-06 01:01:01', 1, '2019-10-29 16:28:27');
SET @iWarehousingID1 = LAST_INSERT_ID();
-- 插入未审核入库单
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (0/*F_Status*/, 'RK201909050001', 1, 1, 4, NULL, '2017-08-06 01:01:01', 1, '2019-10-29 16:28:27');
SET @iWarehousingID2 = LAST_INSERT_ID();
-- 插入已审核入库单商品
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID1, @iCommodityID, 10, 1, '可比克薯片222', 1, 10, 2000, '2017-08-06 01:01:01', 36, '2020-08-06 01:01:01', '2019-10-29 16:28:27', '2019-10-29 16:28:27', 50);
-- 插入未审核入库单商品
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID2, @iCommodityID, 10, 1, '可比克薯片222', 1, 10, 2000, '2017-08-06 01:01:01', 36, '2020-08-06 01:01:01', '2019-10-29 16:28:27', '2019-10-29 16:28:27', 50);
-- 插入零售单
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (1, 'LS200380601010100011234', 1, 1, 'url=ashasoadigmnalskd', '2017-08-16', 2, 4, '0', 1, '........', -1, '2019-10-29 16:28:28', 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iTradeID = LAST_INSERT_ID();
-- 插入零售单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iTradeID, @iCommodityID, '可比克薯片222', 1, 30, 321, 500, 300, 300, NULL);
SET @iTradeCommodityID = LAST_INSERT_ID();
-- 插入零售单来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iTradeCommodityID, @iCommodityID, 30, @iWarehousingID1);
-- 插入退货单
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (4, 'LS200380601010100011234_1', 1, 1, 'url=ashasoadigmnalskd', '2017-08-26', 2, 4, '0', 1, '........', @iTradeID, '2019-10-29 16:28:28', 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iReturnTradeID = LAST_INSERT_ID();
-- 插入退货单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnTradeID, @iCommodityID, '可比克薯片222', 1, 10, 321, 500, 300, 300, NULL);
SET @iReturnTradeCommodityID = LAST_INSERT_ID();
-- 插入退货去向表
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iReturnTradeCommodityID, @iCommodityID, 10, @iWarehousingID1);
SET @iErrorCode = 0;
SET @sErrorMsg = '';
--  
CALL SPD_Commodity_CheckNO(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
--  
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = CONCAT('ID为',@iCommodityID, '的商品,入库单有已审核和未审核的商品,商品库存 = 入库单已审核商品F_NO + 去向表F_NO - 来源表F_NO'), '测试成功', '测试失败') AS 'Case8 Testing Result';
--  
DELETE FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID = @iReturnTradeCommodityID;
DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @iTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID IN (@iTradeCommodityID,@iReturnTradeCommodityID);
DELETE FROM t_retailtrade WHERE F_ID IN (@iTradeID,@iReturnTradeID);
DELETE FROM t_warehousingcommodity WHERE F_WarehousingID IN(@iWarehousingID1,@iWarehousingID2);
DELETE FROM t_warehousing WHERE F_ID IN(@iWarehousingID1,@iWarehousingID2);
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
--
SELECT '------------------ 检查入库商品全是未审核的商品的库存不准确 --------------------' AS 'CASE9';
-- 插入商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, F_LatestPricePurchase, F_PriceRetail, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_NO, F_Type, F_NOStart, F_PurchasingPriceStart, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4, F_CurrentWarehousingID)
VALUES (0/*F_Status*/, '可比克薯片222', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 8, 12, 11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 7/* F_NO */, 0/*F_Type*/, -1, -1, NULL, '2019-10-29 16:28:25', '2019-10-29 16:28:25', NULL, NULL, NULL, NULL, NULL);
SET @iCommodityID = LAST_INSERT_ID();
-- 插入未审核入库单1
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (0/*F_Status*/, 'RK201909050001', 1, 1, 4, NULL, '2017-08-06 01:01:01', 1, '2019-10-29 16:28:27');
SET @iWarehousingID1 = LAST_INSERT_ID();
-- 插入未审核入库单2
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (0/*F_Status*/, 'RK201909050001', 1, 1, 4, NULL, '2017-08-06 01:01:01', 1, '2019-10-29 16:28:27');
SET @iWarehousingID2 = LAST_INSERT_ID();
-- 插入未审核入库单商品1
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID1, @iCommodityID, 10, 1, '可比克薯片222', 1, 10, 2000, '2017-08-06 01:01:01', 36, '2020-08-06 01:01:01', '2019-10-29 16:28:27', '2019-10-29 16:28:27', 10);
-- 插入未审核入库单商品2
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID2, @iCommodityID, 10, 1, '可比克薯片222', 1, 10, 2000, '2017-08-06 01:01:01', 36, '2020-08-06 01:01:01', '2019-10-29 16:28:27', '2019-10-29 16:28:27', 10);
-- 插入零售单
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (1, 'LS200380601010100011234', 1, 1, 'url=ashasoadigmnalskd', '2017-08-16', 2, 4, '0', 1, '........', -1, '2019-10-29 16:28:28', 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iTradeID = LAST_INSERT_ID();
-- 插入零售单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iTradeID, @iCommodityID, '可比克薯片222', 1, 30, 321, 500, 300, 300, NULL);
SET @iTradeCommodityID = LAST_INSERT_ID();
-- 插入零售单来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iTradeCommodityID, @iCommodityID, 30, NULL);
-- 插入退货单
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (4, 'LS200380601010100011234_1', 1, 1, 'url=ashasoadigmnalskd', '2017-08-26', 2, 4, '0', 1, '........', @iTradeID, '2019-10-29 16:28:28', 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iReturnTradeID = LAST_INSERT_ID();
-- 插入退货单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnTradeID, @iCommodityID, '可比克薯片222', 1, 10, 321, 500, 300, 300, NULL);
SET @iReturnTradeCommodityID = LAST_INSERT_ID();
-- 插入退货去向表
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iReturnTradeCommodityID, @iCommodityID, 10, NULL);
SET @iErrorCode = 0;
SET @sErrorMsg = '';
--  
CALL SPD_Commodity_CheckNO(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
--  
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = CONCAT('ID为',@iCommodityID, '的商品,入库单商品全是未审核,商品库存 = 去向表F_NO - 来源表F_NO'), '测试成功', '测试失败') AS 'Case9 Testing Result';
--  
DELETE FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID = @iReturnTradeCommodityID;
DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @iTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID IN (@iTradeCommodityID,@iReturnTradeCommodityID);
DELETE FROM t_retailtrade WHERE F_ID IN (@iTradeID,@iReturnTradeID);
DELETE FROM t_warehousingcommodity WHERE F_WarehousingID IN(@iWarehousingID1,@iWarehousingID2);
DELETE FROM t_warehousing WHERE F_ID IN(@iWarehousingID1,@iWarehousingID2);
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '------------------ 检查已删除的商品库存不为0 --------------------' AS 'CASE10';
-- 插入商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, F_LatestPricePurchase, F_PriceRetail, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_NO, F_Type, F_NOStart, F_PurchasingPriceStart, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4, F_CurrentWarehousingID)
VALUES (2/*F_Status*/, '可比克薯片222', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 8, 12, 11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 7/* F_NO */, 0/*F_Type*/, -1, -1, NULL, '2019-10-29 16:28:25', '2019-10-29 16:28:25', NULL, NULL, NULL, NULL, NULL);
SET @iCommodityID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
--  
CALL SPD_Commodity_CheckNO(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
--  
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = CONCAT('ID为',@iCommodityID, '已删除的商品库存只能为0'), '测试成功', '测试失败') AS 'Case10 Testing Result';
--  
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;














