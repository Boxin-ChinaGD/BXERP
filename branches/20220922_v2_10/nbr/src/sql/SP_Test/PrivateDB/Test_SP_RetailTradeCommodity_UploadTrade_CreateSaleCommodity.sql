SELECT '++++++++++++++++++ Test_SP_RetailTradeCommodity_UploadTrade_CreateSaleCommodity.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: @iSourceID = -1 正常添加零售单商品 赠品ID为-1 -------------------------' AS 'Case1';
-- 插入单品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type,F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比克薯片111', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111',0/*F_Type*/, NULL, '2019-10-29 16:28:25', '2019-10-29 16:28:25', NULL, NULL, NULL, NULL);
SET @iID = LAST_INSERT_ID();
INSERT INTO t_commodityshopinfo(F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@iID, 2, 8, 12, 500, -1, -1, NULL);
SET @iCommShopInfoID = last_insert_id();
--
INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID,F_Status,F_SyncDatetime,F_Amount,F_SmallSheetID, F_ShopID)
VALUES (now(),123456,3,'url=ashasoadigmnalskd','2017-08-06',1,1,0,'........',-1,1,now(),128.6,1,2);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iTradeID = LAST_INSERT_ID();
SET @iCommodityID = @iID;
SET @iBarcodeID = 1;
SET @iNO = 20;
SET @fPrice = 222.6;
SET @iNOCanReturn = 0;
SET @iStaffID = 1;
SET @fPriceReturn = 0;
SET @fPriceSpecialOffer = 0;
SET @fPriceVIPOriginal = 0;
CALL SP_RetailTradeCommodity_UploadTrade_CreateSaleCommodity(@iErrorCode, @sErrorMsg, @iTradeID, @iCommodityID, @iBarcodeID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
SELECT IF((SELECT F_NO FROM t_commodityshopinfo WHERE F_CommodityID = @iCommodityID AND F_ShopID = 2) = 480 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result'; -- 由于没有入库单信息
SELECT * FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = (SELECT F_ID FROM t_retailtradecommodity WHERE F_CommodityID = @iID AND F_TradeID = @iTradeID);
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @iTradeID;
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;
DELETE FROM t_retailtradedailyreportbycommodity WHERE F_CommodityID = @iID;
DELETE FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
DELETE FROM t_commodity WHERE F_ID = @iID;


SELECT '-------------------- Case2: @iSourceID = -1 正常添加零售单商品 商品ID和赠品ID都为-1 返回7 -------------------------' AS 'Case2';

INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID, F_ShopID)
VALUES (now(),42854,3,'url=ashasoadigmnalskd','2017-08-06',1,'A123456',0,'........',-1,2);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iTradeID = LAST_INSERT_ID();
SET @iCommodityID = -1;
SET @iBarcodeID = 1;
SET @iNO = 20;
SET @fPrice = 222.6;
SET @iNOCanReturn = 0;
SET @iStaffID = 1;
SET @fPriceReturn = 0;
SET @fPriceSpecialOffer = 0;
SET @fPriceVIPOriginal = 0;
CALL SP_RetailTradeCommodity_UploadTrade_CreateSaleCommodity(@iErrorCode, @sErrorMsg, @iTradeID, @iCommodityID, @iBarcodeID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
SELECT IF(@sErrorMsg = '该商品不存在' AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case2 Testing Result';
DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = (SELECT F_ID FROM t_retailtradecommodity WHERE F_CommodityID = 63 AND F_TradeID = @iTradeID);
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @iTradeID;

SELECT '-------------------- Case3: @iSourceID = -1 添加多包装商品 -------------------------' AS 'Case3';
-- 插入单品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比克薯片111', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111',0/*F_Type*/, NULL, '2019-10-29 16:28:25', '2019-10-29 16:28:25', NULL, NULL, NULL, NULL);
SET @iID = LAST_INSERT_ID();
INSERT INTO t_commodityshopinfo(F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@iID, 2, 8, 12, 500, -1, -1, NULL);
SET @iCommShopInfoID = last_insert_id();
-- 插入多包装商品品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多包装', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, @iID, 12, '1111111',2/*F_Type*/, NULL, '2019-10-29 16:28:25', '2019-10-29 16:28:25', NULL, NULL, NULL, NULL);
SET @iID1 = LAST_INSERT_ID();
INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID, F_ShopID)
VALUES (now(),872275,3,'url=ashasoadigmnalskd','2017-08-06',1,'A123456',0,'........',-1,2);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iTradeID = LAST_INSERT_ID();
SET @iCommodityID = @iID1;
SET @iBarcodeID = 1;
SET @iNO = 10;
SET @fPrice = 222.6;
SET @iNOCanReturn = 0;
SET @iStaffID = 1;
SET @fPriceReturn = 0;
SET @fPriceSpecialOffer = 0;
SET @fPriceVIPOriginal = 0;
CALL SP_RetailTradeCommodity_UploadTrade_CreateSaleCommodity(@iErrorCode, @sErrorMsg, @iTradeID, @iCommodityID, @iBarcodeID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
SELECT IF((SELECT F_NO FROM t_commodityshopinfo WHERE F_ShopID = 2 AND F_CommodityID = (SELECT F_RefCommodityID FROM t_commodity WHERE F_ID = @iCommodityID)) = 380 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case3 Testing Result';
DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = (SELECT F_ID FROM t_retailtradecommodity WHERE F_CommodityID = @iCommodityID AND F_TradeID = @iTradeID);
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @iTradeID;
DELETE FROM t_commodityhistory WHERE F_CommodityID IN (@iID,@iID1) ;
DELETE FROM t_retailtradedailyreportbycommodity WHERE F_CommodityID IN (@iID,@iID1) ;
DELETE FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
DELETE FROM t_commodity WHERE F_ID IN (@iID,@iID1) ;

SELECT '-------------------- Case4: @iSourceID = -1 添加组合商品 -------------------------' AS 'Case4';
-- 插入单品1
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比克薯片111', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111',0/*F_Type*/, NULL, '2019-10-29 16:28:25', '2019-10-29 16:28:25', NULL, NULL, NULL, NULL);
SET @iID1 = LAST_INSERT_ID();
INSERT INTO t_commodityshopinfo(F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@iID1, 2, 8, 12, 200, -1, -1, NULL);
SET @iCommShopInfoID1 = last_insert_id();
-- 插入单品2
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比克薯片222', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111',0/*F_Type*/, NULL, '2019-10-29 16:28:25', '2019-10-29 16:28:25', NULL, NULL, NULL, NULL);
SET @iID2 = LAST_INSERT_ID();
INSERT INTO t_commodityshopinfo(F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@iID2, 2, 8, 12, 200, -1, -1, NULL);
SET @iCommShopInfoID2 = last_insert_id();
-- 插入组合商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '组合商品', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, @iID, 10, '1111111',1/*F_Type*/, NULL, '2019-10-29 16:28:25', '2019-10-29 16:28:25', NULL, NULL, NULL, NULL);
SET @iID3 = LAST_INSERT_ID();
INSERT INTO t_commodityshopinfo(F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@iID3, 2, 8, 12, 0, -1, -1, NULL);
SET @iCommShopInfoID3 = last_insert_id();
-- 插入子商品1
INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iID3, @iID1, 2, 8, '2019-11-18 10:53:49', '2019-11-18 10:53:49');
-- 插入子商品2
INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iID3, @iID2, 3, 9, '2019-11-18 10:53:49', '2019-11-18 10:53:49');
--
INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID, F_ShopID)
VALUES (now(),872275,3,'url=ashasoadigmnalskd','2017-08-06',1,'A123456',0,'........',-1,2);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iTradeID = LAST_INSERT_ID();
SET @iCommodityID = @iID3; -- ...
SET @iBarcodeID = 1;
SET @iNO = 10;
SET @fPrice = 222.6;
SET @iNOCanReturn = 0;
SET @iStaffID = 1;
SET @fPriceReturn = 0;
SET @fPriceSpecialOffer = 0;
SET @fPriceVIPOriginal = 0;
CALL SP_RetailTradeCommodity_UploadTrade_CreateSaleCommodity(@iErrorCode, @sErrorMsg, @iTradeID, @iCommodityID, @iBarcodeID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
SELECT IF((SELECT F_NO FROM t_commodityshopinfo WHERE F_CommodityID = @iID1 AND F_ShopID = 2) = 180 
	AND (SELECT F_NO FROM t_commodityshopinfo WHERE F_CommodityID = @iID2 AND F_ShopID = 2) = 170 
	AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case4 Testing Result';
DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = (SELECT F_ID FROM t_retailtradecommodity WHERE F_CommodityID = @iCommodityID AND F_TradeID = @iTradeID);
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @iTradeID;
DELETE FROM t_commodityhistory WHERE F_CommodityID IN (@iID1,@iID2,@iID3);
DELETE FROM t_retailtradedailyreportbycommodity WHERE F_CommodityID IN (@iID1,@iID2,@iID3);
DELETE FROM t_subcommodity WHERE F_CommodityID = @iID3;
DELETE FROM t_commodityshopinfo WHERE F_ID IN (@iCommShopInfoID1, @iCommShopInfoID2, @iCommShopInfoID3);
DELETE FROM t_commodity WHERE F_ID IN (@iID1,@iID2,@iID3);


SELECT '-------------------- Case5: @iSourceID = -1 添加服务商品 -------------------------' AS 'Case5';
INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID, F_ShopID)
VALUES (now(),87212750,3,'url=ashasoadigmxnalskd','2017-08-06',1,'A123456',0,'........',-1,2);
SET @iTradeID = LAST_INSERT_ID();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'可比克薯片ss','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111',3/*F_Type*/);
SET @iCommodityID = LAST_INSERT_ID();
INSERT INTO t_commodityshopinfo(F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@iCommodityID, 2, 8, 12, 0, -1, -1, NULL);
SET @iCommShopInfoID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iBarcodeID = 1;
SET @iNO = 1;
SET @fPrice = 222.6;
SET @iNOCanReturn = 0;
SET @iStaffID = 1;
SET @fPriceReturn = 0;
SET @fPriceSpecialOffer = 0;
SET @fPriceVIPOriginal = 0;
-- 
SET @iOldCommHistorySize := 0;
SELECT count(1) INTO @iOldCommHistorySize FROM t_commodityhistory;
SET @iOldCommNO := 0;
SELECT F_NO INTO @iOldCommNO FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
-- 
CALL SP_RetailTradeCommodity_UploadTrade_CreateSaleCommodity(@iErrorCode, @sErrorMsg, @iTradeID, @iCommodityID, @iBarcodeID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SET @iNewCommHistorySize := 0;
SELECT count(1) INTO @iNewCommHistorySize FROM t_commodityhistory;
SET @iNewCommNO := 0;
SELECT F_NO INTO @iNewCommNO FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
--
SELECT IF(@iErrorCode = 0 AND @iNewCommNO = @iOldCommNO AND @iNewCommHistorySize = @iOldCommHistorySize, '测试成功', '测试失败') AS 'Case5 Testing Result';
-- 
DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = (SELECT F_ID FROM t_retailtradecommodity WHERE F_CommodityID = @iCommodityID AND F_TradeID = @iTradeID);
DELETE FROM t_retailtradecommodity WHERE F_ID = last_insert_id();
DELETE FROM t_retailtrade WHERE F_ID = @iTradeID;
DELETE FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;