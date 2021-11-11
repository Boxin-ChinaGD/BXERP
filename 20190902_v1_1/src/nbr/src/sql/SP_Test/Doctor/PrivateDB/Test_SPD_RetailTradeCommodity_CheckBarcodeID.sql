SELECT '++++++++++++++++++ Test_SPD_RetailTradeCommodity_CheckBarcodeID.sql ++++++++++++++++++++';

-- 正常测试
SELECT '-------------------- Case1:正常测试 -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTradeCommodity_CheckBarcodeID(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';
-- 

-- 该测试存在外键约束
-- 零售单商品的BarcodeID没有对应的Barcode
-- SELECT '-------------------- Case2:零售单商品的BarcodeID没有对应的Barcode -------------------------' AS 'Case2';
-- 
-- SET @iErrorCode = 0;
-- SET @sErrorMsg = '';
-- 插入一条商品数据，测试完会删除
-- INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
-- F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
-- F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
-- VALUES (0,'可比克薯片aaa','薯片','克',1,'箱',3,1,'SP',1,
-- 8,12,11.8,11,1,1,null,
-- 3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 0,-1);
-- SET @iCommodityID = last_insert_id();
-- 插入一条零售单数据，测试完会删除
-- INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID,F_Status,F_SyncDatetime,F_Amount,F_SmallSheetID)
-- VALUES (now(),654321,3,'url=ashasoadigmnalskd','2017-08-06',1,1,0,'........',-1,1,now(),128.6,1);
-- SET @iRetailTradeID = LAST_INSERT_ID();
-- 插入一条零售单商品数据，测试完会删除
-- INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_Discount, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
-- VALUES (@iRetailTradeID, @iCommodityID,'可比克薯片aaa', -1, 20, 222.6, 0.9, 20, 222.6, 200);
-- SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 插入一条零售单商品来源数据，测试完会删除
-- INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID)
-- VALUES (@iRetailTradeCommodityID, 20, 24, @iCommodityID);
-- SET @iRetailtradecommoditysourceID = Last_insert_id();
-- 
-- CALL SPD_RetailTradeCommodity_CheckBarcodeID(@iErrorCode, @sErrorMsg);
-- SELECT @iErrorCode;
-- SELECT @sErrorMsg;
-- 
-- SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailTradeCommodityID, '的零售单商品的BarcodeID没有对应的Barcode') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case2 Testing Result';
-- DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailtradecommoditysourceID;
-- DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
-- DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
-- DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 
