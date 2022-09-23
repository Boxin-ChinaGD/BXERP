SELECT '++++++++++++++++++ Test_SPD_WarehousingCommodity_CheckSalableNO.sql ++++++++++++++++++++';
SELECT '------------------ 正常测试 --------------------' AS 'CASE1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_WarehousingCommodity_CheckSalableNO(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT '------------------ 未审核的入库单对应的入库商品的可售数量不正确 --------------------' AS 'CASE2';
-- 插入商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, F_LatestPricePurchase, F_PriceRetail, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_NO, F_Type, F_NOStart, F_PurchasingPriceStart, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4, F_CurrentWarehousingID)
VALUES (0/*F_Status*/, '可比克薯片111', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 8, 12, 11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', -17/*F_NO*/, 0/*F_Type*/, -1, -1, NULL, '2019-10-29 16:28:25', '2019-10-29 16:28:25', NULL, NULL, NULL, NULL, NULL);
SET @iCommodityID = LAST_INSERT_ID();
-- 
INSERT INTO t_warehousing (F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (0/* F_Status */, 3, 1, 1, now(), 1, now());
SET @iWarehousingID = Last_insert_id();
-- 
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID, @iCommodityID, 100/*F_NO*/, 1, '可比克薯片111', 56, 11.1, 11.1, now(), 36, now(), now(), now(), 1);
SET @iWarehousingCommodityID = Last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
--  
CALL SPD_WarehousingCommodity_CheckSalableNO(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
--  
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = CONCAT('ID为',@iWarehousingCommodityID, '未审核入库商品可售数量不正确'), '测试成功', '测试失败') AS 'Case2 Testing Result';
--  
DELETE FROM t_warehousingcommodity WHERE F_ID = @iWarehousingCommodityID;
DELETE FROM t_warehousing WHERE F_ID = @iWarehousingID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '------------------ 已审核的入库单对应的入库商品的可售数量不正确 --------------------' AS 'CASE3';
-- 插入商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, F_LatestPricePurchase, F_PriceRetail, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_NO, F_Type, F_NOStart, F_PurchasingPriceStart, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4, F_CurrentWarehousingID)
VALUES (0, '可比克薯片111', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 8, 12, 11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', -17/*F_NO*/, 0/*F_Type*/, -1, -1, NULL, '2019-10-29 16:28:25', '2019-10-29 16:28:25', NULL, NULL, NULL, NULL, NULL);
SET @iCommodityID = LAST_INSERT_ID();
-- 插入入库单
INSERT INTO t_warehousing (F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (1/*F_Status*/, 3, 1, 1, now(), 1, now());
SET @iWarehousingID = Last_insert_id();
-- 插入入库单商品
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID, @iCommodityID, 100/*F_NO*/, 1, '可比克薯片111', 56, 11.1, 11.1, now(), 36, now(), now(), now(), 50);
-- 插入零售单
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (1, 'LS200380601010100011234', 1, 1, 'url=ashasoadigmnalskd', '2017-08-16', 2, 4, '0', 1, '........', -1, '2019-10-29 16:28:28', 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iTradeID = LAST_INSERT_ID();
-- 插入零售单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iTradeID, @iCommodityID, '可比克薯片111', 1, 30/*F_NO*/, 321, 500, 300, 300, NULL);
SET @iTradeCommodityID = LAST_INSERT_ID();
-- 插入零售单来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iTradeCommodityID, @iCommodityID, 30/*F_NO*/, @iWarehousingID);
-- 插入退货单
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (4, 'LS200380601010100011234_1', 1, 1, 'url=ashasoadigmnalskd', '2017-08-26', 2, 4, '0', 1, '........', @iTradeID, '2019-10-29 16:28:28', 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iReturnTradeID = LAST_INSERT_ID();
-- 插入退货单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iReturnTradeID, @iCommodityID, '可比克薯片111', 1, 10/*F_NO*/, 321, 500, 300, 300, NULL);
SET @iReturnTradeCommodityID = LAST_INSERT_ID();
-- 插入退货去向表
INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
VALUES (@iReturnTradeCommodityID, @iCommodityID, 10/*F_NO*/, @iWarehousingID);
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sID = (SELECT group_concat(F_ID) FROM t_warehousingcommodity WHERE F_CommodityID = @iCommodityID AND  F_WarehousingID IN  (SELECT F_ID FROM t_warehousing WHERE F_Status = 1)); 
--  
CALL SPD_WarehousingCommodity_CheckSalableNO(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
--  
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = CONCAT('ID为',@sID, '已审核入库商品可售数量不正确'), '测试成功', '测试失败') AS 'Case3 Testing Result';
--  
DELETE FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID = @iReturnTradeCommodityID;
DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @iTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID IN (@iTradeCommodityID,@iReturnTradeCommodityID);
DELETE FROM t_retailtrade WHERE F_ID IN (@iTradeID,@iReturnTradeID);
DELETE FROM t_warehousingcommodity WHERE F_WarehousingID = @iWarehousingID;
DELETE FROM t_warehousing WHERE F_ID = @iWarehousingID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 
SELECT '------------------ 检查入库商品对应的入库单状态不是0和1 --------------------' AS 'CASE3';
-- 插入商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, F_LatestPricePurchase, F_PriceRetail, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_NO, F_Type, F_NOStart, F_PurchasingPriceStart, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4, F_CurrentWarehousingID)
VALUES (0, '可比克薯片111', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 8, 12, 11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', -17/*F_NO*/, 0/*F_Type*/, -1, -1, NULL, '2019-10-29 16:28:25', '2019-10-29 16:28:25', NULL, NULL, NULL, NULL, NULL);
SET @iCommodityID = LAST_INSERT_ID();
-- 插入入库单
INSERT INTO t_warehousing (F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (3/*F_Status*/, 3, 1, 1, now(), 1, now());
SET @iWarehousingID = Last_insert_id();
-- 插入入库单商品
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID, @iCommodityID, 100/*F_NO*/, 1, '可比克薯片111', 56, 11.1, 11.1, now(), 36, now(), now(), now(), 50);
SET @iWarehousingCommodityID = Last_insert_id();
--
SET @iErrorCode = 0;
SET @sErrorMsg = '';
--  
CALL SPD_WarehousingCommodity_CheckSalableNO(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
--  
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = CONCAT('ID为',@iWarehousingCommodityID, '入库商品对应的入库单的状态只能是0和1'), '测试成功', '测试失败') AS 'Case3 Testing Result';
--  
DELETE FROM t_warehousingcommodity WHERE F_WarehousingID = @iWarehousingID;
DELETE FROM t_warehousing WHERE F_ID = @iWarehousingID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '------------------ (跨库)已审核的入库单对应的入库商品的可售数量不正确 --------------------' AS 'CASE4';
-- 插入商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, F_LatestPricePurchase, F_PriceRetail, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_NO, F_Type, F_NOStart, F_PurchasingPriceStart, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4, F_CurrentWarehousingID)
VALUES (0/*F_Status*/, '可比克薯片111', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 8, 12, 11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', -17/*F_NO*/, 0/*F_Type*/, -1, -1, NULL, '2019-10-29 16:28:25', '2019-10-29 16:28:25', NULL, NULL, NULL, NULL, NULL);
SET @iCommodityID = LAST_INSERT_ID();
-- 插入入库单1
INSERT INTO t_warehousing (F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (1/*F_Status*/, 3, 1, 1, now(), 1, now());
SET @iWarehousingID = Last_insert_id();
-- 插入入库单2
INSERT INTO t_warehousing (F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (1/*F_Status*/, 3, 1, 1, now(), 1, now());
SET @iWarehousingID2 = Last_insert_id();
-- 插入入库单商品1
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID, @iCommodityID, 100/*F_NO*/, 1, '可比克薯片111', 56, 11.1, 11.1, now(), 36, now(), now(), now(), 50);
-- 插入入库单商品2
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID2, @iCommodityID, 100/*F_NO*/, 1, '可比克薯片111', 56, 11.1, 11.1, now(), 36, now(), now(), now(), 50);

-- 插入零售单
INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID)
VALUES (1, 'LS200380601010100011234', 1, 1, 'url=ashasoadigmnalskd', '2017-08-16', 2, 4, '0', 1, '........', -1, '2019-10-29 16:28:28', 1.5, 0, 0, 1.5, 0, 0, 0, 0, 0, 2, NULL, NULL, NULL, NULL, NULL, NULL);
SET @iTradeID = LAST_INSERT_ID();
-- 插入零售单商品
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (@iTradeID, @iCommodityID, '可比克薯片111', 1, 200/*F_NO*/, 321, 500, 300, 300, NULL);
SET @iTradeCommodityID = LAST_INSERT_ID();
-- 插入零售单来源
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iTradeCommodityID, @iCommodityID, 100/*F_NO*/, @iWarehousingID);
--
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@iTradeCommodityID, @iCommodityID, 100/*F_NO*/, @iWarehousingID2);
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sID = (SELECT group_concat(F_ID) FROM t_warehousingcommodity WHERE F_CommodityID = @iCommodityID AND  F_WarehousingID IN  (SELECT F_ID FROM t_warehousing WHERE F_Status = 1));   
--  
CALL SPD_WarehousingCommodity_CheckSalableNO(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
--  
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = CONCAT('ID为',@sID, '已审核入库商品可售数量不正确'), '测试成功', '测试失败') AS 'Case4 Testing Result';
--  
DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @iTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID IN (@iTradeCommodityID);
DELETE FROM t_retailtrade WHERE F_ID IN (@iTradeID,@iReturnTradeID);
DELETE FROM t_warehousingcommodity WHERE F_WarehousingID IN (@iWarehousingID,@iWarehousingID2);
DELETE FROM t_warehousing WHERE F_ID IN (@iWarehousingID,@iWarehousingID2);
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;