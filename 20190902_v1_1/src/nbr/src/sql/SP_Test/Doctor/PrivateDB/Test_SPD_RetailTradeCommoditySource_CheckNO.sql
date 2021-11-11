SELECT '++++++++++++++++++ Test_SPD_RetailTradeCommoditySource_CheckNO.sql ++++++++++++++++++++';
-- 正常测试
SELECT '-------------------- Case1:正常测试 -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RetailTradeCommoditySource_CheckNO(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';


-- 零售单商品中的商品类型不存在
SELECT '-------------------- Case2:零售单商品中的商品类型不存在 -------------------------' AS 'Case2';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 插入一条商品数据，测试完会删除
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0/*F_Status*/,'可比克薯片aaa','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 0/*F_NO*/,-1/*F_Type*/);
SET @iCommodityID = last_insert_id();
-- 插入一条零售单数据，测试完会删除
INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID,F_Status,F_SyncDatetime,F_Amount,F_SmallSheetID)
VALUES (now(),654321,3,'url=ashasoadigmnalskd','2017-08-06',1,1,0,'........',-1,1,now(),128.6,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 插入一条零售单商品数据，测试完会删除
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID, @iCommodityID,'可比克薯片aaa', 1, 20/*F_NO*/, 222.6, 20, 222.6, 200);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 插入一条零售单商品来源数据，测试完会删除
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID)
VALUES (@iRetailTradeCommodityID, 20/*F_NO*/, 24, @iCommodityID);
SET @iRetailtradecommoditysourceID = Last_insert_id();
-- 
CALL SPD_RetailTradeCommoditySource_CheckNO(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailtradecommoditysourceID, '的零售单商品来源对应的商品的类型不正确') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case2 Testing Result';
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailtradecommoditysourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 

-- 零售单商品是普通商品，但普通商品的库存不正确
SELECT '-------------------- Case3:零售单商品是普通商品，但普通商品的库存不正确 -------------------------' AS 'Case3';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 插入一条商品数据，测试完会删除
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0/*F_Status*/,'可比克薯片aaa','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 0/*F_NO*/,0/*F_Type*/);
SET @iCommodityID = last_insert_id();
-- 插入一条零售单数据，测试完会删除
INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID,F_Status,F_SyncDatetime,F_Amount,F_SmallSheetID)
VALUES (now(),654321,3,'url=ashasoadigmnalskd','2017-08-06',1,1,0,'........',-1,1,now(),128.6,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 插入一条零售单商品数据，测试完会删除
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID, @iCommodityID,'可比克薯片aaa', 1, 20/*F_NO*/, 222.6, 20, 222.6, 200);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 插入一条零售单商品来源数据，测试完会删除
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID)
VALUES (@iRetailTradeCommodityID, 10/*F_NO*/, 24, @iCommodityID);
--
SET @sID =(SELECT group_concat(F_ID) FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @iRetailTradeCommodityID); 
-- 
CALL SPD_RetailTradeCommoditySource_CheckNO(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @sID, '的零售单商品来源的库存跟相应的零售单商品的库存不相等') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case3 Testing Result';
DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

-- 零售单商品是组合商品，但组合商品的库存不正确
SELECT '-------------------- Case4:零售单商品是组合商品，但组合商品的库存不正确 -------------------------' AS 'Case4';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 插入一条商品数据，测试完会删除(普通商品)
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0/*F_Status*/,'可比克薯片aaa','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 0/*F_NO*/,0/*F_Type*/);
SET @iCommodityID = last_insert_id();
-- 插入一条商品数据，测试完会删除(组合商品)
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0/*F_Status*/,'可比克薯片bbb','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 0/*F_NO*/,1/*F_Type*/);
SET @iCommodityID2 = last_insert_id();
-- 插入一条商品数据，测试完会删除(子商品)
INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iCommodityID2, @iCommodityID, 3/*F_SubCommodityNO*/, 10, now(), now());
SET @iSubCommodityID = last_insert_id();
-- 插入一条零售单数据，测试完会删除
INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID,F_Status,F_SyncDatetime,F_Amount,F_SmallSheetID)
VALUES (now(),654321,3,'url=ashasoadigmnalskd','2017-08-06',1,1,0,'........',-1,1,now(),128.6,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 插入一条零售单商品数据，测试完会删除
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID, @iCommodityID2,'可比克薯片aaa', 1, 10/*F_NO*/, 222.6, 20, 222.6, 200);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 插入一条零售单商品来源数据，测试完会删除
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID)
VALUES (@iRetailTradeCommodityID, 60/*F_NO*/, 24, @iCommodityID);
--
SET @sID =(SELECT group_concat(F_ID) FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @iRetailTradeCommodityID); 
--
CALL SPD_RetailTradeCommoditySource_CheckNO(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @sID, '的零售单商品来源的库存跟相应的零售单商品的库存不相等') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case4 Testing Result';
DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_subcommodity WHERE F_ID = @iSubCommodityID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID2;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

-- 零售单商品是多包装商品，但多包装商品的库存不正确
SELECT '-------------------- Case5:零售单商品是多包装商品，但多包装商品的库存不正确 -------------------------' AS 'Case5';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 插入一条商品数据，测试完会删除(普通商品)
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0/*F_Status*/,'可比克薯片aaa','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 0/*F_NO*/,0/*F_Type*/);
SET @iCommodityID = last_insert_id();
-- 插入一条商品数据，测试完会删除(多包装商品)
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0/*F_Status*/,'可比克薯片bbb','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',@iCommodityID,3/*F_RefCommodityMultiple*/,'1111111', 0/*F_NO*/,2/*F_Type*/);
SET @iCommodityID2 = last_insert_id();
-- 插入一条零售单数据，测试完会删除
INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID,F_Status,F_SyncDatetime,F_Amount,F_SmallSheetID)
VALUES (now(),654321,3,'url=ashasoadigmnalskd','2017-08-06',1,1,0,'........',-1,1,now(),128.6,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 插入一条零售单商品数据，测试完会删除
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID, @iCommodityID2,'可比克薯片aaa', 1, 10/*F_NO*/, 222.6, 20, 222.6, 200);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 插入一条零售单商品来源数据，测试完会删除
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID)
VALUES (@iRetailTradeCommodityID, 60/*F_NO*/, 24, @iCommodityID);
-- 
SET @sID =(SELECT group_concat(F_ID) FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @iRetailTradeCommodityID);
--
CALL SPD_RetailTradeCommoditySource_CheckNO(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @sID, '的零售单商品来源的库存跟相应的零售单商品的库存不相等') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case5 Testing Result';
DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID2;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

-- 零售单商品是服务商品，但服务商品的库存不正确
SELECT '-------------------- Case6:零售单商品是服务商品，但服务商品的库存不正确 -------------------------' AS 'Case6';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 插入一条商品数据，测试完会删除
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0/*F_Status*/,'快递AA','kd','个',4,NULL,4,4,'SP',1,
5,38,5.8,5.5,1,1,null,
0,30,'2019-01-14','20',0,0,'快递AA',0/*F_NO*/,3/*F_Type*/);
SET @iCommodityID = last_insert_id();
-- 插入一条零售单数据，测试完会删除
INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID,F_Status,F_SyncDatetime,F_Amount,F_SmallSheetID)
VALUES (now(),654321,3,'url=ashasoadigmnalskd','2017-08-06',1,1,0,'........',-1,1,now(),128.6,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 插入一条零售单商品数据，测试完会删除
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID, @iCommodityID,'快递AA', 1, 1/*F_NO*/, 222.6, 20, 222.6, 200);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 插入一条零售单商品来源数据，测试完会删除
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID)
VALUES (@iRetailTradeCommodityID, 2/*F_NO*/, 24, @iCommodityID);
SET @iRetailtradecommoditysourceID = Last_insert_id();
-- 
CALL SPD_RetailTradeCommoditySource_CheckNO(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iRetailtradecommoditysourceID, '的零售单商品来源的库存跟相应的零售单商品的库存不相等') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case6 Testing Result';
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @iRetailtradecommoditysourceID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- Case7:(跨库)零售单商品是普通商品，但普通商品的库存不正确 -------------------------' AS 'Case7';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 插入一条商品数据，测试完会删除
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0/*F_Status*/,'可比克薯片aaa','薯片','克',1,'箱',3,1,'SP',1,
8,12,11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111', 0/*F_NO*/,0/*F_Type*/);
SET @iCommodityID = last_insert_id();
-- 插入入库单1
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (1/*F_Status*/, 'RK201909050001', 1, 1, 4, NULL, '2017-08-06 01:01:01', 1, '2019-10-29 16:28:27');
SET @iWarehousingID = LAST_INSERT_ID();
-- 插入入库单2
INSERT INTO t_warehousing (F_Status, F_SN, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (1/*F_Status*/, 'RK201909050001', 1, 1, 4, NULL, '2017-08-06 01:01:01', 1, '2019-10-29 16:28:27');
SET @iWarehousingID2 = LAST_INSERT_ID();
-- 插入入库单商品1
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID, @iCommodityID, 10/*F_NO*/, 1, '可比克薯片hh2', 1, 10, 2000, '2017-08-06 01:01:01', 36, '2020-08-06 01:01:01', '2019-10-29 16:28:27', '2019-10-29 16:28:27', 1);
-- 插入入库单商品2
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID2, @iCommodityID, 10/*F_NO*/, 1, '可比克薯片hh2', 1, 10, 2000, '2017-08-06 01:01:01', 36, '2020-08-06 01:01:01', '2019-10-29 16:28:27', '2019-10-29 16:28:27', 1);
-- 插入零售单
-- 插入一条零售单数据，测试完会删除
INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID,F_Status,F_SyncDatetime,F_Amount,F_SmallSheetID)
VALUES (now(),654321,3,'url=ashasoadigmnalskd','2017-08-06',1,1,0,'........',-1,1,now(),128.6,1);
SET @iRetailTradeID = LAST_INSERT_ID();
-- 插入一条零售单商品数据，测试完会删除
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@iRetailTradeID, @iCommodityID,'可比克薯片aaa', 1, 20/*F_NO*/, 222.6, 20, 222.6, 200);
SET @iRetailTradeCommodityID = LAST_INSERT_ID();
-- 插入一条零售单商品来源数据，测试完会删除
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID)
VALUES (@iRetailTradeCommodityID, 10/*F_NO*/, @iWarehousingID, @iCommodityID);
--
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID)
VALUES (@iRetailTradeCommodityID, 5/*F_NO*/, @iWarehousingID2, @iCommodityID);
--
SET @sID =(SELECT group_concat(F_ID) FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @iRetailTradeCommodityID);
--
CALL SPD_RetailTradeCommoditySource_CheckNO(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @sID, '的零售单商品来源的库存跟相应的零售单商品的库存不相等') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case7 Testing Result';
DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @iRetailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @iRetailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;
DELETE FROM t_warehousingcommodity WHERE F_WarehousingID IN (@iWarehousingID,@iWarehousingID2);
DELETE FROM t_warehousing WHERE F_ID IN (@iWarehousingID,@iWarehousingID2);
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;