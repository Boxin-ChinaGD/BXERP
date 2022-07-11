SELECT '++++++++++++++++++ Test_SP_RetailTradeCommodity_UploadTrade_CreateReturnCommodity.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:退货单品 -------------------------' AS 'Case1';

-- 插入商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比克薯片111', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111',0/*F_Type*/, NULL, '2019-10-29 16:28:25', '2019-10-29 16:28:25', NULL, NULL, NULL, NULL);
SET @iID = LAST_INSERT_ID();
INSERT INTO t_commodityshopinfo(F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@iID, 2, 8, 12, 500, -1, -1, NULL);
SET @iCommShopInfoID = last_insert_id();

INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID,F_Status,F_SyncDatetime,F_Amount,F_SmallSheetID, F_ShopID)
VALUES (now(),123456,3,'url=ashasoadigmnalskd','2017-08-06',1,1,0,'........',-1/*F_SourceID*/,1,now(),128.6,1,2);
SET @retailTradeID = LAST_INSERT_ID();

INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@retailTradeID, @iID,'可比克薯片11', 1, 20/*F_NO*/, 222.6, 20, 222.6, 200);
SET @retailTradeCommodityID = LAST_INSERT_ID();

INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID,F_Status,F_SyncDatetime,F_Amount,F_SmallSheetID, F_ShopID)
VALUES (now(),84621385,3,'url=ashasoadigmnalskd','2017-08-06',1,1,0,'........',@retailTradeID,1,now(),128.6,1,2);
SET @returnRetailTradeID = LAST_INSERT_ID();

INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID)
VALUES (@retailTradeCommodityID, 20/*F_NO*/, 24, @iID);
SET @retailtradecommoditysource = Last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iTradeID = @returnRetailTradeID;
SET @iCommodityID = @iID;
SET @iBarcodeID = 1;
SET @iNO = 20;
SET @fPrice = 222.6;
SET @iNOCanReturn = 0;
SET @iStaffID = 1;
SET @fPriceReturn = 0;
SET @fPriceSpecialOffer = 0;
SET @fPriceVIPOriginal = 0;
CALL SP_RetailTradeCommodity_UploadTrade_CreateReturnCommodity(@iErrorCode, @sErrorMsg, @iTradeID, @iCommodityID, @iBarcodeID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
SELECT IF((SELECT F_NO FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID) = 520 
	AND (SELECT F_NOCanReturn FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID) = 0 
	AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';
-- 
SELECT 1 FROM t_retailtradecommodity WHERE F_TradeID = @iTradeID AND F_CommodityName = '可比克薯片11';
SELECT IF(found_rows() = 1,'测试成功', '测试失败') AS 'Case1 Testing Result';
-- 
DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = (SELECT F_ID FROM t_retailtradecommodity WHERE F_CommodityID = @iID AND F_TradeID = @iTradeID);
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailtradecommoditysource;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID = (SELECT F_ID FROM t_retailtradecommodity WHERE F_TradeID = @returnRetailTradeID);
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @returnRetailTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @retailTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;
DELETE FROM t_retailtradedailyreportbycommodity WHERE F_CommodityID = @iID;
DELETE FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case2: @iSourceID = -1 退货多包装商品 -------------------------' AS 'Case2';
-- 插入单品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比克薯片111', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111',0/*F_Type*/, NULL, '2019-10-29 16:28:25', '2019-10-29 16:28:25', NULL, NULL, NULL, NULL);
SET @iID = LAST_INSERT_ID();
INSERT INTO t_commodityshopinfo(F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@iID, 2, 8, 12, 500, -1, -1, NULL);
SET @iCommShopInfoID = last_insert_id();
-- 插入多包装商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '多包装', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, @iID, 10, '1111111',2/*F_Type*/, NULL, '2019-10-29 16:28:25', '2019-10-29 16:28:25', NULL, NULL, NULL, NULL);
SET @iID2 = LAST_INSERT_ID();
--
INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID,F_Status,F_SyncDatetime,F_Amount,F_SmallSheetID, F_ShopID)
VALUES (now(),123456,3,'url=ashasoadigmnalskd','2017-08-06',1,1,0,'........',-1/*F_SourceID*/,1,now(),128.6,1,2);
SET @retailTradeID = LAST_INSERT_ID();
--
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@retailTradeID, @iID2,'多包装', 1, 10/*F_NO*/, 222.6, 10, 222.6, 200);
SET @retailTradeCommodityID = LAST_INSERT_ID();
--
INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID,F_Status,F_SyncDatetime,F_Amount,F_SmallSheetID, F_ShopID)
VALUES (now(),84621385,3,'url=ashasoadigmnalskd','2017-08-06',1,1,0,'........',@retailTradeID,1,now(),128.6,1,2);
SET @returnRetailTradeID = LAST_INSERT_ID();
--
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID)
VALUES (@retailTradeCommodityID, 10/*F_NO*/, 24, @iID);
SET @retailtradecommoditysource = Last_insert_id();
--
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iTradeID = @returnRetailTradeID;
SET @iCommodityID = @iID2;
SET @iBarcodeID = 1;
SET @iNO = 10;
SET @fPrice = 222.6;
SET @iNOCanReturn = 0;
SET @iStaffID = 1;
SET @fPriceReturn = 0;
SET @fPriceSpecialOffer = 0;
SET @fPriceVIPOriginal = 0;
CALL SP_RetailTradeCommodity_UploadTrade_CreateReturnCommodity(@iErrorCode, @sErrorMsg, @iTradeID, @iCommodityID, @iBarcodeID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
SELECT IF((SELECT F_NO FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID) = 600 
	AND (SELECT F_NOCanReturn FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID) = 0 
	AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result'; -- 由于没有入库单信息
-- 
SELECT 1 FROM t_retailtradecommodity WHERE F_TradeID = @iTradeID AND F_CommodityName = '多包装';
SELECT IF(found_rows() = 1,'测试成功', '测试失败') AS 'Case2 Testing Result';
-- 
DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = (SELECT F_ID FROM t_retailtradecommodity WHERE F_CommodityID = @iID2 AND F_TradeID = @iTradeID);
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailtradecommoditysource;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID = (SELECT F_ID FROM t_retailtradecommodity WHERE F_TradeID = @returnRetailTradeID);
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @returnRetailTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @retailTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_commodityhistory WHERE F_CommodityID IN (@iID2,@iID);
DELETE FROM t_retailtradedailyreportbycommodity WHERE F_CommodityID IN (@iID2,@iID);
DELETE FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
DELETE FROM t_commodity WHERE F_ID IN (@iID2,@iID);

SELECT '-------------------- Case3: 退货组合商品 -------------------------' AS 'Case3';
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
-- 插入子商品1
INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iID3, @iID1, 2, 8, '2019-11-18 10:53:49', '2019-11-18 10:53:49');
-- 插入子商品2
INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iID3, @iID2, 3, 9, '2019-11-18 10:53:49', '2019-11-18 10:53:49');
--
INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID,F_Status,F_SyncDatetime,F_Amount,F_SmallSheetID, F_ShopID)
VALUES (now(),123456,3,'url=ashasoadigmnalskd','2017-08-06',1,1,0,'........',-1/*F_SourceID*/,1,now(),128.6,1,2);
SET @retailTradeID = LAST_INSERT_ID();
--
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@retailTradeID, @iID3,'组合商品', 1, 10/*F_NO*/, 222.6, 10, 222.6, 200);
SET @retailTradeCommodityID = LAST_INSERT_ID();
--
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID)
VALUES (@retailTradeCommodityID, 10/*F_NO*/, 24, @iID1);
SET @retailtradecommoditysource1 = Last_insert_id();

INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID)
VALUES (@retailTradeCommodityID, 10/*F_NO*/, 24, @iID2);
SET @retailtradecommoditysource2 = Last_insert_id();

INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID,F_Status,F_SyncDatetime,F_Amount,F_SmallSheetID, F_ShopID)
VALUES (now(),84621385,3,'url=ashasoadigmnalskd','2017-08-06',1,1,0,'........',@retailTradeID,1,now(),128.6,1,2);
SET @returnRetailTradeID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iTradeID = @returnRetailTradeID;
SET @iCommodityID = @iID3; -- ...
SET @iBarcodeID = 1;
SET @iNO = 10;
SET @fPrice = 222.6;
SET @iNOCanReturn = 0;
SET @iStaffID = 1;
SET @fPriceReturn = 0;
SET @fPriceSpecialOffer = 0;
SET @fPriceVIPOriginal = 0;
CALL SP_RetailTradeCommodity_UploadTrade_CreateReturnCommodity(@iErrorCode, @sErrorMsg, @iTradeID, @iCommodityID, @iBarcodeID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
SELECT F_NO FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID1;
SELECT F_NO FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID2;
SELECT F_NOCanReturn FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
SELECT IF((SELECT F_NO FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID1) = 220 
	AND (SELECT F_NO FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID2) = 230 
	AND (SELECT F_NOCanReturn FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID) = 0 
	AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case3 Testing Result';
-- 
SELECT 1 FROM t_retailtradecommodity WHERE F_TradeID = @iTradeID AND F_CommodityName = '组合商品';
SELECT IF(found_rows() = 1,'测试成功', '测试失败') AS 'Case3 Testing Result';
-- 
UPDATE t_commodityshopinfo SET F_NO = 200 WHERE F_ID = @iCommShopInfoID1;
UPDATE t_commodityshopinfo SET F_NO = 200 WHERE F_ID = @iCommShopInfoID2;
-- 
DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = (SELECT F_ID FROM t_retailtradecommodity WHERE F_CommodityID = @iID3 AND F_TradeID = @iTradeID);
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailtradecommoditysource1;
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailtradecommoditysource2;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID IN (SELECT F_ID FROM t_retailtradecommodity WHERE F_TradeID = @returnRetailTradeID);
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @returnRetailTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @retailTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_commodityhistory WHERE F_CommodityID IN (@iID1,@iID2,@iID3);
DELETE FROM t_retailtradedailyreportbycommodity WHERE F_CommodityID IN (@iID1,@iID2,@iID3);
DELETE FROM t_subcommodity WHERE F_CommodityID = @iID3;
DELETE FROM t_commodityshopinfo WHERE F_ID IN (@iCommShopInfoID1, @iCommShopInfoID2);
DELETE FROM t_commodity WHERE F_ID IN (@iID1,@iID2,@iID3);

SELECT '-------------------- Case4: 源单ID不存在，返回错误码7 -------------------------' AS 'Case4';
-- 插入商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'可比克薯片aaa','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111',3/*F_Type*/);
SET @CommodityID = last_insert_id();
--
INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID, F_ShopID)
VALUES (now(),42854,3,'url=ashasoadigmnalskd','2017-08-06',1,'A123456',0,'........',999999/*F_SourceID*/,2);
--
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iTradeID = LAST_INSERT_ID();
SET @iCommodityID = @CommodityID;
SET @iBarcodeID = 1;
SET @iNO = 20;
SET @fPrice = 222.6;
SET @iNOCanReturn = 0;
SET @iStaffID = 1;
SET @fPriceReturn = 0;
SET @fPriceSpecialOffer = 0;
SET @fPriceVIPOriginal = 0;
CALL SP_RetailTradeCommodity_UploadTrade_CreateReturnCommodity(@iErrorCode, @sErrorMsg, @iTradeID, @iCommodityID, @iBarcodeID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
SELECT IF(@sErrorMsg = '源单ID不存在' AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case5 Testing Result';
DELETE FROM t_retailtradecommoditysource WHERE F_ReducingCommodityID = @CommodityID;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_IncreasingCommodityID = @CommodityID;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @iTradeID OR F_SourceID = @iTradeID;
DELETE FROM t_commodityhistory WHERE F_CommodityID = @CommodityID;
DELETE FROM t_commodity WHERE F_ID = @CommodityID;


SELECT '-------------------- Case6: 退货数量大于可退货数量，返回错误码7 -------------------------' AS 'Case6';
INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID,F_Status,F_SyncDatetime,F_Amount,F_SmallSheetID, F_ShopID)
VALUES (now(),123456,3,'url=ashasoadigmnalskd','2017-08-06',1,1,0,'........',-1/*F_SourceID*/,1,now(),128.6,1,2);
SET @retailTradeID = LAST_INSERT_ID();

INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@retailTradeID, 165,'旺仔牛奶二', 1, 10/*F_NO*/, 222.6, 10, 222.6, 200);
SET @retailTradeCommodityID = LAST_INSERT_ID();

INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID,F_Status,F_SyncDatetime,F_Amount,F_SmallSheetID, F_ShopID)
VALUES (now(),84621385,3,'url=ashasoadigmnalskd','2017-08-06',1,1,0,'........',@retailTradeID,1,now(),128.6,1,2);
SET @returnRetailTradeID = LAST_INSERT_ID();

INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID)
VALUES (@retailTradeCommodityID, 10/*F_NO*/, 24, 164);
SET @retailtradecommoditysource = Last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iTradeID = @returnRetailTradeID;
SET @iCommodityID = 165;
SET @iBarcodeID = 1;
SET @iNO = 50;
SET @fPrice = 222.6;
SET @iNOCanReturn = 0;
SET @iStaffID = 1;
SET @fPriceReturn = 0;
SET @fPriceSpecialOffer = 0;
SET @fPriceVIPOriginal = 0;
CALL SP_RetailTradeCommodity_UploadTrade_CreateReturnCommodity(@iErrorCode, @sErrorMsg, @iTradeID, @iCommodityID, @iBarcodeID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
SELECT IF(@sErrorMsg = '退货数量大于可退货数量，不能退货' AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case6 Testing Result';
-- 
DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = (SELECT F_ID FROM t_retailtradecommodity WHERE F_CommodityID = 165 AND F_TradeID = @iTradeID);
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailtradecommoditysource;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID = (SELECT F_ID FROM t_retailtradecommodity WHERE F_TradeID = @returnRetailTradeID);
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @returnRetailTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @retailTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;

SELECT '-------------------- Case7: 退一个源单不存在的商品，返回错误码7 -------------------------' AS 'Case7';
INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID,F_Status,F_SyncDatetime,F_Amount,F_SmallSheetID, F_ShopID)
VALUES (now(),123456,3,'url=ashasoadigmnalskd','2017-08-06',1,1,0,'........',-1/*F_SourceID*/,1,now(),128.6,1,2);
SET @retailTradeID = LAST_INSERT_ID();

INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@retailTradeID, 165,'星巴克AB', 1, 10/*F_NO*/, 222.6, 10, 222.6, 200);
SET @retailTradeCommodityID = LAST_INSERT_ID();

INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID,F_Status,F_SyncDatetime,F_Amount,F_SmallSheetID, F_ShopID)
VALUES (now(),84621385,3,'url=ashasoadigmnalskd','2017-08-06',1,1,0,'........',@retailTradeID,1,now(),128.6,1,2);
SET @returnRetailTradeID = LAST_INSERT_ID();

INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID)
VALUES (@retailTradeCommodityID, 10/*F_NO*/, 24, 164);
SET @retailtradecommoditysource = Last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iTradeID = @returnRetailTradeID;
SET @iCommodityID = 2;
SET @iBarcodeID = 1;
SET @iNO = 50;
SET @fPrice = 222.6;
SET @iNOCanReturn = 0;
SET @iStaffID = 1;
SET @fPriceReturn = 0;
SET @fPriceSpecialOffer = 0;
SET @fPriceVIPOriginal = 0;
CALL SP_RetailTradeCommodity_UploadTrade_CreateReturnCommodity(@iErrorCode, @sErrorMsg, @iTradeID, @iCommodityID, @iBarcodeID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
SELECT IF(@sErrorMsg = '零售单中没有该商品，不能退货' AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case7 Testing Result';
DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = (SELECT F_ID FROM t_retailtradecommodity WHERE F_CommodityID = 165 AND F_TradeID = @iTradeID);
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailtradecommoditysource;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID = (SELECT F_ID FROM t_retailtradecommodity WHERE F_TradeID = @returnRetailTradeID);
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @returnRetailTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @retailTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;

SELECT '-------------------- Case8: 退一个服务商品 -------------------------' AS 'Case8';
INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID,F_Status,F_SyncDatetime,F_Amount,F_SmallSheetID, F_ShopID)
VALUES (now(),1234586,3,'url=ashasoadigmnalskd','2017-08-06',1,1,0,'........',-1/*F_SourceID*/,1,now(),128.6,1,2);
SET @retailTradeID = LAST_INSERT_ID();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'可比克薯片aaa','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111',3/*F_Type*/);
SET @iCommodityID = last_insert_id();
INSERT INTO t_commodityshopinfo(F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@iCommodityID, 2, 8, 12, 200, -1, -1, NULL);
SET @iCommShopInfoID = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@retailTradeID, @iCommodityID,'可比克薯片aaa', 1, 10/*F_NO*/, 222.6, 10, 222.6, 200);
SET @retailTradeCommodityID = LAST_INSERT_ID();
-- 
INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID)
VALUES (@retailTradeCommodityID, 10/*F_NO*/, NULL, @iCommodityID);
-- 
INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID,F_Status,F_SyncDatetime,F_Amount,F_SmallSheetID, F_ShopID)
VALUES (now(),846255,3,'url=ashasoadigmnalskd','2017-08-06',1,1,0,'........',@retailTradeID,1,now(),128.6,1,2);
SET @returnRetailTradeID = LAST_INSERT_ID();
-- 
SET @iOldCommHistorySize := 0;
SELECT count(1) INTO @iOldCommHistorySize FROM t_commodityhistory;
SET @iOldCommNO := 0;
SELECT F_NO INTO @iOldCommNO FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iTradeID = @returnRetailTradeID;
SET @iCommodityID = @iCommodityID;
SET @iBarcodeID = 1;
SET @iNO = 1;
SET @fPrice = 222.6;
SET @iNOCanReturn = 0;
SET @iStaffID = 1;
SET @fPriceReturn = 0;
SET @fPriceSpecialOffer = 0;
SET @fPriceVIPOriginal = 0;
CALL SP_RetailTradeCommodity_UploadTrade_CreateReturnCommodity(@iErrorCode, @sErrorMsg, @iTradeID, @iCommodityID, @iBarcodeID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
-- 
SET @iNewCommHistorySize := 0;
SELECT count(1) INTO @iNewCommHistorySize FROM t_commodityhistory;
SET @iNewCommNO := 0;
SELECT F_NO INTO @iNewCommNO FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
SELECT IF(@iErrorCode = 0 AND @iNewCommNO = @iOldCommNO AND @iNewCommHistorySize = @iOldCommHistorySize, '测试成功', '测试失败') AS 'Case8 Testing Result';
-- 
DELETE FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID = (SELECT F_ID FROM t_retailtradecommodity WHERE F_TradeID = @returnRetailTradeID);
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @returnRetailTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID;
DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = (SELECT F_ID FROM t_retailtradecommodity WHERE F_CommodityID = @iCommodityID AND F_TradeID = @retailTradeID);
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @retailTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtradedailyreportbycommodity WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- Case9:退货数量大于可退货数量 -------------------------' AS 'Case9';
-- 插入商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'可比克薯片aaa','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,null,
3,30,'2018-04-14 01:00:01','20',0,0,'1111111',0/*F_Type*/);
SET @CommodityID = last_insert_id();
INSERT INTO t_commodityshopinfo(F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@CommodityID, 2, 8, 12, 0, -1, -1, NULL);
SET @iCommShopInfoID = last_insert_id();

INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID,F_Status,F_SyncDatetime,F_Amount,F_SmallSheetID, F_ShopID)
VALUES (now(),123456,3,'url=ashasoadigmnalskd','2017-08-06',1,1,0,'........',-1/*F_SourceID*/,1,now(),128.6,1,2);
SET @retailTradeID = LAST_INSERT_ID();

INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@retailTradeID, @CommodityID,'旺仔牛奶二二', 1, 20/*F_NO*/, 222.6, 20/*F_NOCanReturn*/, 222.6, 200);
SET @retailTradeCommodityID = LAST_INSERT_ID();

INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID,F_Status,F_SyncDatetime,F_Amount,F_SmallSheetID, F_ShopID)
VALUES (now(),84621385,3,'url=ashasoadigmnalskd','2017-08-06',1,1,0,'........',@retailTradeID,1,now(),128.6,1,2);
SET @returnRetailTradeID = LAST_INSERT_ID();

INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID)
VALUES (@retailTradeCommodityID, 20/*F_NO*/, 24, @CommodityID);
SET @retailtradecommoditysource = Last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iTradeID = @returnRetailTradeID;
SET @iCommodityID = @CommodityID;
SET @iBarcodeID = 1;
SET @iNO = 21;
SET @fPrice = 222.6;
SET @iNOCanReturn = 0;
SET @iStaffID = 1;
SET @fPriceReturn = 0;
SET @fPriceSpecialOffer = 0;
SET @fPriceVIPOriginal = 0;
CALL SP_RetailTradeCommodity_UploadTrade_CreateReturnCommodity(@iErrorCode, @sErrorMsg, @iTradeID, @iCommodityID, @iBarcodeID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@sErrorMsg = '退货数量大于可退货数量，不能退货' AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case9 Testing Result';
-- 
DELETE FROM t_retailtradecommoditysource WHERE F_ID = @retailtradecommoditysource;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID = (SELECT F_ID FROM t_retailtradecommodity WHERE F_TradeID = @returnRetailTradeID);
DELETE FROM t_retailtradedailyreportbycommodity WHERE F_CommodityID = @CommodityID;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @returnRetailTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @returnRetailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @retailTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_commodityhistory WHERE F_CommodityID = @CommodityID;
DELETE FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
DELETE FROM t_commodity WHERE F_ID = @CommodityID;