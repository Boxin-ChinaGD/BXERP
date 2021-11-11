SELECT '++++++++++++++++++++++++++++++++++Test_Func_DeleteRetailTradeCommoditySource.sql+++++++++++++++++++++++++++++++++++++++++';

SELECT '-------------------- Case1：进行退货操作(入库单已审核，增加入库单可售数量) -------------------------' AS 'Case1';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'公仔面333','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',56,3,'1111111',2);
SET @commID = LAST_INSERT_ID();
INSERT INTO nbr.t_commodityshopinfo (F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@commID, 2, 8, 12, 0, -1, -1, NULL);
SET @iCommShopInfoID = last_insert_id();
SET @iShopID = 2;

INSERT INTO t_warehousing (F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_ShopID)
VALUES (1, 1, 1, 1, 1, NOW(), @iShopID);
SET @warehousingID = LAST_INSERT_ID();

INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_NOSalable)
VALUES (@warehousingID, @commID, 15, 1, '公仔面333', 1, 10, 150, now(), 12, now(), NOW(), 10);
SET @warehousingCommodityID = LAST_INSERT_ID();

INSERT INTO t_retailtrade (F_ShopID, F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID)
VALUES (2, 1, 'LS2019090414230000011234', 2132185, 1, 'url=ashasoadigmnalskd', now(), 1, 1, 0, 1, '......', -1, now(), 200, 200, 0, 0, 0, 0, 0, 0, 0, 1);
SET @retailTradeID = LAST_INSERT_ID();

INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@retailTradeID, @commID, 1, 10, 200, 10, 200, 200);
SET @retailTradeCommodityID = LAST_INSERT_ID();

-- 创建退货单和退货单从表
INSERT INTO t_retailtrade (F_ShopID, F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID)
VALUES (2, 1, 'LS2019090414230000011234_1', 2132195, 1, 'url=ashasoadigmnalskd', now(), 1, 1, 0, 1, '......', @retailTradeID, now(), 200, 200, 0, 0, 0, 0, 0, 0, 0, 1);
SET @retailTradeID2 = LAST_INSERT_ID();

SET @returnCommNO1 = 10;
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@retailTradeID2, @commID, 1, @returnCommNO1, 200, 10, 200, 200);
SET @retailTradeCommodityID2 = LAST_INSERT_ID();


SET @iCommodityID = @commID;
SET @saleNO = 10;
SET @currentWarehousingID = @warehousingID;
SET @retailTradeCommodityID = @retailTradeCommodityID;

SELECT Func_CreateRetailTradeCommoditySource(@iCommodityID, @saleNO, @currentWarehousingID, @retailTradeCommodityID) INTO @is;

SET @returnNO = 10;
SET @iNOCanReturn = 10;
SELECT Func_DeleteRetailTradeCommoditySource(@iCommodityID, @returnNO, @iNOCanReturn, @currentWarehousingID, @retailTradeCommodityID) INTO @is;

SELECT 1 FROM t_commodityshopinfo WHERE F_CurrentWarehousingID = @warehousingID AND F_CommodityID = @commID; -- 检查商品表当值入库ID是否更新
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT 1 FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID AND F_NOSalable = 10; -- 检查入库商品表可售数量是否正确计算
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case1 Testing Result';

-- 检查是否正确的将退货数据插入退货商品去向表
SET @ResultVerification1 = 0;
SELECT 1 INTO @ResultVerification1
FROM t_returnretailtradecommoditydestination 
WHERE F_RetailTradeCommodityID = @retailTradeCommodityID2 AND F_WarehousingID = @warehousingID AND F_IncreasingCommodityID = @commID AND F_NO = @returnCommNO1;
SELECT IF(@ResultVerification1 = 1, '测试成功', '测试失败') AS 'Case1 Testing Result';

DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @retailTradeCommodityID;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID = @retailTradeCommodityID2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID2;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID;
DELETE FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
DELETE FROM t_commodity WHERE F_ID = @commID;
DELETE FROM t_warehousing WHERE F_ID = @warehousingID;

SELECT '-------------------- Case2：创建退货单，退一部分商品 -------------------------' AS 'Case2';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'公仔面333','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',56,3,'1111111',2);
SET @commID = LAST_INSERT_ID();
INSERT INTO nbr.t_commodityshopinfo (F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@commID, 2, 8, 12, 0, -1, -1, NULL);
SET @iCommShopInfoID = last_insert_id();
SET @iShopID = 2;

INSERT INTO t_warehousing (F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_ShopID)
VALUES (1, 1, 1, 1, 1, NOW(), @iShopID);
SET @warehousingID = LAST_INSERT_ID();

INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_NOSalable)
VALUES (@warehousingID, @commID, 15, 1, '公仔面333', 1, 10, 150, now(), 12, now(), NOW(), 15);
SET @warehousingCommodityID = LAST_INSERT_ID();

INSERT INTO t_retailtrade (F_ShopID, F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID)
VALUES (2, 1, 'LS2019090414230000011235', 2132185, 1, 'url=ashasoadigmnalskd', now(), 1, 1, 0, 1, '......', -1, now(), 200, 200, 0, 0, 0, 0, 0, 0, 0, 1);
SET @retailTradeID = LAST_INSERT_ID();

INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@retailTradeID, @commID, 1, 10, 200, 10, 200, 200);
SET @retailTradeCommodityID = LAST_INSERT_ID();

-- 创建退货单和退货单从表
INSERT INTO t_retailtrade (F_ShopID, F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID)
VALUES (2, 1, 'LS2019090414230000011235_1', 2132195, 1, 'url=ashasoadigmnalskd', now(), 1, 1, 0, 1, '......', @retailTradeID, now(), 200, 200, 0, 0, 0, 0, 0, 0, 0, 1);
SET @retailTradeID2 = LAST_INSERT_ID();

SET @returnCommNO2 = 5;
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@retailTradeID2, @commID, 1, @returnCommNO2, 200, 10, 200, 200);
SET @retailTradeCommodityID2 = LAST_INSERT_ID();

SET @iCommodityID = @commID;
SET @saleNO = 10;
SET @currentWarehousingID = @warehousingID;
SET @retailTradeCommodityID = @retailTradeCommodityID;

SELECT Func_CreateRetailTradeCommoditySource(@iCommodityID, @saleNO, @currentWarehousingID, @retailTradeCommodityID) INTO @is;

SET @returnNO = 5;
SET @iNOCanReturn = 10;
SELECT Func_DeleteRetailTradeCommoditySource(@iCommodityID, @returnNO, @iNOCanReturn, @currentWarehousingID, @retailTradeCommodityID) INTO @is;

SELECT 1 FROM t_commodityshopinfo WHERE F_CurrentWarehousingID = @warehousingID AND F_CommodityID = @commID; -- 检查商品表当值入库ID是否更新
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case2 Testing Result';

SELECT 1 FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID AND F_NOSalable = 10; -- 检查入库商品表可售数量是否正确计算
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case2 Testing Result';

-- 检查是否正确的将退货数据插入退货商品去向表
SET @ResultVerification1 = 0;
SELECT 1 INTO @ResultVerification1
FROM t_returnretailtradecommoditydestination 
WHERE F_RetailTradeCommodityID = @retailTradeCommodityID2 AND F_WarehousingID = @warehousingID AND F_IncreasingCommodityID = @commID AND F_NO = @returnCommNO2;
SELECT IF(@ResultVerification1 = 1, '测试成功', '测试失败') AS 'Case2 Testing Result';

DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @retailTradeCommodityID;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID = @retailTradeCommodityID2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID2;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID;
DELETE FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
DELETE FROM t_commodity WHERE F_ID = @commID;
DELETE FROM t_warehousing WHERE F_ID = @warehousingID;

SELECT '-------------------- Case3：创建退货单，(退货数量+入库单商品可售数量)大于入库数量,循环下一张入库单 -------------------------' AS 'Case3';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'公仔面333','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',56,3,'1111111',2);
SET @commID = LAST_INSERT_ID();
INSERT INTO nbr.t_commodityshopinfo (F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@commID, 2, 8, 12, 0, -1, -1, NULL);
SET @iCommShopInfoID = last_insert_id();
SET @iShopID = 2;

INSERT INTO t_warehousing (F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_ShopID)
VALUES (1, 1, 1, 1, 1, NOW(), @iShopID);
SET @warehousingID = LAST_INSERT_ID();

SET @warehousingcommodityNO = 15;
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_NOSalable)
VALUES (@warehousingID, @commID, @warehousingcommodityNO, 1, '公仔面333', 1, 10, 150, now(), 12, now(), NOW(), 15);
SET @warehousingCommodityID = LAST_INSERT_ID();

INSERT INTO t_warehousing (F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_ShopID)
VALUES (1, 1, 1, 1, 1, NOW(), @iShopID);
SET @warehousingID2 = LAST_INSERT_ID();

INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_NOSalable)
VALUES (@warehousingID2, @commID, 20, 1, '公仔面333', 1, 10, 200, now(), 12, now(), NOW(), 20);
SET @warehousingCommodityID2 = LAST_INSERT_ID();

INSERT INTO t_retailtrade (F_ShopID, F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID)
VALUES (2, 1, 'LS2019090414230000011236', 2132185, 1, 'url=ashasoadigmnalskd', now(), 1, 1, 0, 1, '......', -1, now(), 200, 200, 0, 0, 0, 0, 0, 0, 0, 1);
SET @retailTradeID = LAST_INSERT_ID();

INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@retailTradeID, @commID, 1, 20, 200, 20, 200, 200);
SET @retailTradeCommodityID = LAST_INSERT_ID();

-- 创建退货单和退货单从表
INSERT INTO t_retailtrade (F_ShopID, F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID)
VALUES (2, 1, 'LS2019090414230000011236_1', 2132195, 1, 'url=ashasoadigmnalskd', now(), 1, 1, 0, 1, '......', @retailTradeID, now(), 200, 200, 0, 0, 0, 0, 0, 0, 0, 1);
SET @retailTradeID2 = LAST_INSERT_ID();

SET @returnCommNO3 = 20;
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@retailTradeID2, @commID, 1, @returnCommNO3, 200, 20, 200, 200);
SET @retailTradeCommodityID2 = LAST_INSERT_ID();

SET @iCommodityID = @commID;
SET @saleNO = 20;
SET @currentWarehousingID = @warehousingID;
SET @retailTradeCommodityID = @retailTradeCommodityID;

SELECT Func_CreateRetailTradeCommoditySource(@iCommodityID, @saleNO, @currentWarehousingID, @retailTradeCommodityID) INTO @is;

-- 这个时候，商品的当值入库ID已经改变
SET @currentWarehousingID = @warehousingID2;
SET @returnNO = 20;
SET @iNOCanReturn = 20;
SELECT Func_DeleteRetailTradeCommoditySource(@iCommodityID, @returnNO, @iNOCanReturn, @currentWarehousingID, @retailTradeCommodityID) INTO @is;

SELECT 1 FROM t_commodityshopinfo WHERE F_CurrentWarehousingID = @warehousingID AND F_CommodityID = @commID; -- 检查商品表当值入库ID是否更新
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case3 Testing Result';

SELECT 1 FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID AND F_NOSalable = 15; -- 检查入库商品表可售数量是否正确计算
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case3 Testing Result';

SELECT 1 FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID2 AND F_NOSalable = 20; -- 检查入库商品表可售数量是否正确计算
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case3 Testing Result';

-- 检查是否正确的将退货数据插入退货商品去向表
-- 售卖的商品20件，其中15件来自@warehousingID，5件来自@warehousingID2
-- 退货商品去向表记录的是当时卖的时候多少件来自哪张入库单
SET @ResultVerification1 = 0;
SELECT 1 INTO @ResultVerification1
FROM t_returnretailtradecommoditydestination 
WHERE F_RetailTradeCommodityID = @retailTradeCommodityID2 AND F_WarehousingID = @warehousingID AND F_IncreasingCommodityID = @commID AND F_NO = @warehousingcommodityNO;

SET @ResultVerification2 = 0;
SELECT 1 INTO @ResultVerification2
FROM t_returnretailtradecommoditydestination 
WHERE F_RetailTradeCommodityID = @retailTradeCommodityID2 AND F_IncreasingCommodityID = @commID AND F_NO = @returnCommNO3 - @warehousingcommodityNO;
SELECT IF(@ResultVerification1 = 1 AND @ResultVerification2 = 1, '测试成功', '测试失败') AS 'Case3 Testing Result';

DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @retailTradeCommodityID;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID = @retailTradeCommodityID2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID2;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID2;
DELETE FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
DELETE FROM t_commodity WHERE F_ID = @commID;
DELETE FROM t_warehousing WHERE F_ID = @warehousingID;
DELETE FROM t_warehousing WHERE F_ID = @warehousingID2;

SELECT '-------------------- Case4：创建退货单，(退货数量+入库单商品可售数量)大于入库数量,可售数量溢出，直接增加到最早入库的单 -------------------------' AS 'Case3';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'公仔面333','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',56,3,'1111111',2);
SET @commID = LAST_INSERT_ID();
INSERT INTO nbr.t_commodityshopinfo (F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@commID, 2, 8, 12, 0, -1, -1, NULL);
SET @iCommShopInfoID = last_insert_id();
SET @iShopID = 2;

INSERT INTO t_warehousing (F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_ShopID)
VALUES (1, 1, 1, 1, 1, NOW(), @iShopID);
SET @warehousingID = LAST_INSERT_ID();

INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_NOSalable)
VALUES (@warehousingID, @commID, 15, 1, '公仔面333', 1, 10, 150, now(), 12, now(), NOW(), 15);
SET @warehousingCommodityID = LAST_INSERT_ID();

INSERT INTO t_retailtrade (F_ShopID, F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID)
VALUES (2, 1, 'LS2019090414230000011237', 2132185, 1, 'url=ashasoadigmnalskd', now(), 1, 1, 0, 1, '......', -1, now(), 200, 200, 0, 0, 0, 0, 0, 0, 0, 1);
SET @retailTradeID = LAST_INSERT_ID();

INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@retailTradeID, @commID, 1, 10, 200, 10, 200, 200);
SET @retailTradeCommodityID = LAST_INSERT_ID();

INSERT INTO t_retailtrade (F_ShopID, F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID)
VALUES (2, 1, 'LS2019090414230000011237_1', 2132195, 1, 'url=ashasoadigmnalskd', now(), 1, 1, 0, 1, '......', @retailTradeID, now(), 200, 200, 0, 0, 0, 0, 0, 0, 0, 1);
SET @retailTradeID2 = LAST_INSERT_ID();

SET @returnCommNO4 = 10;
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@retailTradeID2, @commID, 1, @returnCommNO4, 200, 10, 200, 200);
SET @retailTradeCommodityID2 = LAST_INSERT_ID();

INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_ReducingCommodityID, F_NO, F_WarehousingID)
VALUES (@retailTradeCommodityID, @commID, 10, @warehousingID);

SET @returnNO = 10;
SET @iCommodityID = @commID;
SET @currentWarehousingID = @warehousingID;
SET @retailTradeCommodityID = @retailTradeCommodityID;
SET @iNOCanReturn = 10;
SELECT @retailTradeCommodityID;
SELECT Func_DeleteRetailTradeCommoditySource(@iCommodityID, @returnNO, @iNOCanReturn, @currentWarehousingID, @retailTradeCommodityID) INTO @is;

SELECT @is;

SELECT 1 FROM t_commodityshopinfo WHERE F_CurrentWarehousingID = @warehousingID AND F_CommodityID = @commID; -- 检查商品表当值入库ID是否更新
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case4 Testing Result';

SELECT 1 FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID AND F_NOSalable = 25; -- 检查入库商品表可售数量是否正确计算
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case4 Testing Result';

SET @ResultVerification1 = 0;
SELECT 1 INTO @ResultVerification1
FROM t_returnretailtradecommoditydestination 
WHERE F_RetailTradeCommodityID = @retailTradeCommodityID2 AND F_WarehousingID = @warehousingID AND F_IncreasingCommodityID = @commID AND F_NO = @returnCommNO4;
SELECT IF(@ResultVerification1 = 1, '测试成功', '测试失败') AS 'Case4 Testing Result';

DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @retailTradeCommodityID;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID = @retailTradeCommodityID2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID2;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID;
DELETE FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
DELETE FROM t_commodity WHERE F_ID = @commID;
DELETE FROM t_warehousing WHERE F_ID = @warehousingID;

SELECT '-------------------- Case5：进行退货操作(入库单未审核，不增加入库单可售数量) -------------------------' AS 'Case5';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'公仔面333','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',56,3,'1111111',2);
SET @commID = LAST_INSERT_ID();
INSERT INTO nbr.t_commodityshopinfo (F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@commID, 2, 8, 12, 0, -1, -1, NULL);
SET @iCommShopInfoID = last_insert_id();
SET @iShopID = 2;

INSERT INTO t_warehousing (F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_ShopID)
VALUES (0, 1, 1, 1, 1, NOW(), @iShopID);
SET @warehousingID = LAST_INSERT_ID();

INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_NOSalable)
VALUES (@warehousingID, @commID, 15, 1, '公仔面333', 1, 10, 150, now(), 12, now(), NOW(), 15);
SET @warehousingCommodityID = LAST_INSERT_ID();

INSERT INTO t_retailtrade (F_ShopID, F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID)
VALUES (2, 1, 'LS2019090414230000011238', 2132185, 1, 'url=ashasoadigmnalskd', now(), 1, 1, 0, 1, '......', -1, now(), 200, 200, 0, 0, 0, 0, 0, 0, 0, 1);
SET @retailTradeID = LAST_INSERT_ID();

INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@retailTradeID, @commID, 1, 10, 200, 10, 200, 200);
SET @retailTradeCommodityID = LAST_INSERT_ID();

INSERT INTO t_retailtrade (F_ShopID, F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID)
VALUES (2, 1, 'LS2019090414230000011238_1', 2132195, 1, 'url=ashasoadigmnalskd', now(), 1, 1, 0, 1, '......', @retailTradeID, now(), 200, 200, 0, 0, 0, 0, 0, 0, 0, 1);
SET @retailTradeID2 = LAST_INSERT_ID();

SET @returnCommNO5 = 10;
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@retailTradeID2, @commID, 1, @returnCommNO5, 200, 10, 200, 200);
SET @retailTradeCommodityID2 = LAST_INSERT_ID();

SET @iCommodityID = @commID;
SET @saleNO = 15;
SET @currentWarehousingID = @warehousingID;
SET @retailTradeCommodityID = @retailTradeCommodityID;

SELECT Func_CreateRetailTradeCommoditySource(@iCommodityID, @saleNO, @currentWarehousingID, @retailTradeCommodityID) INTO @is;

SET @returnNO = 10;
SET @iNOCanReturn = 10;
SELECT Func_DeleteRetailTradeCommoditySource(@iCommodityID, @returnNO, @iNOCanReturn, @currentWarehousingID, @retailTradeCommodityID) INTO @is;

SELECT 1 FROM t_commodityshopinfo WHERE F_CurrentWarehousingID = @warehousingID AND F_CommodityID = @commID; -- 检查商品表当值入库ID是否更新
SELECT IF(FOUND_ROWS() = 0, '测试成功', '测试失败') AS 'Case5 Testing Result';

SELECT 1 FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID AND F_NOSalable = 15; -- 检查入库商品表可售数量是否正确计算
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case5 Testing Result'; 

SET @ResultVerification1 = 0;
SELECT 1 INTO @ResultVerification1
FROM t_returnretailtradecommoditydestination 
WHERE F_RetailTradeCommodityID = @retailTradeCommodityID2 AND F_IncreasingCommodityID = @commID AND F_NO = @returnCommNO5;
SELECT @ResultVerification1;
SELECT IF(@ResultVerification1 = 1, '测试成功', '测试失败') AS 'Case5 Testing Result';

DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @retailTradeCommodityID;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID = @retailTradeCommodityID2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID2;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID;
DELETE FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
DELETE FROM t_commodity WHERE F_ID = @commID;
DELETE FROM t_warehousing WHERE F_ID = @warehousingID;

SELECT '-------------------- Case6：进行退货操作(一条入库单已审核，一条入库单未审核，前者减少入库单可售数量，后者不减少入库单可售数量) -------------------------' AS 'Case6';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'公仔面333','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',56,3,'1111111',2);
SET @commID = LAST_INSERT_ID();
INSERT INTO t_commodityshopinfo (F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@commID, 2, 8, 12, 0, -1, -1, NULL);
SET @iCommShopInfoID = last_insert_id();
SET @iShopID = 2;

INSERT INTO t_warehousing (F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_ShopID)
VALUES (1, 1, 1, 1, 1, NOW(), @iShopID);
SET @warehousingID = LAST_INSERT_ID();

INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_NOSalable)
VALUES (@warehousingID, @commID, 15, 1, '公仔面333', 1, 10, 150, now(), 12, now(), NOW(), 15);
SET @warehousingCommodityID = LAST_INSERT_ID();

INSERT INTO t_warehousing (F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_ShopID)
VALUES (0, 1, 1, 1, 1, NOW(), @iShopID);
SET @warehousingID2 = LAST_INSERT_ID();

INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_NOSalable)
VALUES (@warehousingID2, @commID, 20, 1, '公仔面333', 1, 10, 200, now(), 12, now(), NOW(), 20);
SET @warehousingCommodityID2 = LAST_INSERT_ID();

INSERT INTO t_retailtrade (F_ShopID, F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID)
VALUES (2, 1, 'LS2019090414230000011239', 2132185, 1, 'url=ashasoadigmnalskd', now(), 1, 1, 0, 1, '......', -1, now(), 200, 200, 0, 0, 0, 0, 0, 0, 0, 1);
SET @retailTradeID = LAST_INSERT_ID();

INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@retailTradeID, @commID, 1, 20, 200, 20, 200, 200);
SET @retailTradeCommodityID = LAST_INSERT_ID();

INSERT INTO t_retailtrade (F_ShopID, F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID)
VALUES (2, 1, 'LS2019090414230000011239_1', 2132195, 1, 'url=ashasoadigmnalskd', now(), 1, 1, 0, 1, '......', @retailTradeID, now(), 200, 200, 0, 0, 0, 0, 0, 0, 0, 1);
SET @retailTradeID2 = LAST_INSERT_ID();

SET @returnCommNO6 = 20;
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@retailTradeID2, @commID, 1, @returnCommNO6, 200, 20, 200, 200);
SET @retailTradeCommodityID2 = LAST_INSERT_ID();

SET @iCommodityID = @commID;
SET @saleNO = 25;
SET @currentWarehousingID = @warehousingID;

SELECT Func_CreateRetailTradeCommoditySource(@iCommodityID, @saleNO, @currentWarehousingID, @retailTradeCommodityID) INTO @is;

SET @returnNO = 20;
SET @iNOCanReturn = 20;
SELECT Func_DeleteRetailTradeCommoditySource(@iCommodityID, @returnNO, @iNOCanReturn, @currentWarehousingID, @retailTradeCommodityID) INTO @is; 
SELECT @retailTradeCommodityID;
SELECT @warehousingID;
SELECT 1 FROM t_commodityshopinfo WHERE F_CurrentWarehousingID = @warehousingID AND F_CommodityID = @commID; -- 检查商品表当值入库ID是否更新
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case6 Testing Result';

SELECT 1 FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID AND F_NOSalable = 10; -- 检查入库商品表可售数量是否正确计算
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case6 Testing Result';

SELECT 1 FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID2 AND F_NOSalable = 20; -- 检查入库商品表可售数量是否正确计算
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case6 Testing Result';

SET @ResultVerification1 = 0;
SELECT 1 INTO @ResultVerification1
FROM t_returnretailtradecommoditydestination 
WHERE F_RetailTradeCommodityID = @retailTradeCommodityID2 AND F_WarehousingID = @warehousingID AND F_IncreasingCommodityID = @commID AND F_NO = @returnCommNO6;
SELECT IF(@ResultVerification1 = 1, '测试成功', '测试失败') AS 'Case6 Testing Result';

DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @retailTradeCommodityID;
DELETE FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID = @retailTradeCommodityID2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID2;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID2;
DELETE FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
DELETE FROM t_commodity WHERE F_ID = @commID;
DELETE FROM t_warehousing WHERE F_ID = @warehousingID;
DELETE FROM t_warehousing WHERE F_ID = @warehousingID2;