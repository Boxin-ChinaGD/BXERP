SELECT '++++++++++++++++++++++++++++++++++Test_Func_CreateRetailTradeCommoditySource.sql+++++++++++++++++++++++++++++++++++++++++';
SELECT '-------------------- Case1：正常添加来源表(入库单已审核，减少入库单可售数量) -------------------------' AS 'Case1';
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

INSERT INTO t_warehousing (F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime)
VALUES (1, 1, 1, 1, 1, NOW());
SET @warehousingID2 = LAST_INSERT_ID();

INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_NOSalable)
VALUES (@warehousingID2, @commID, 20, 1, '公仔面333', 1, 10, 200, now(), 12, now(), NOW(), 20);
SET @warehousingCommodityID2 = LAST_INSERT_ID();

INSERT INTO t_retailtrade (F_ShopID, F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID)
VALUES (2, 1, 'LS2019090414230000011234', 2132185, 1, 'url=ashasoadigmnalskd', now(), 1, 1, 0, 1, '......', -1, now(), 200, 200, 0, 0, 0, 0, 0, 0, 0, 1);
SET @retailTradeID = LAST_INSERT_ID();

INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@retailTradeID, @commID, 1, 10, 200, 10, 200, 200);
SET @retailTradeCommodityID = LAST_INSERT_ID();

SET @iCommodityID = @commID;
SET @saleNO = 10;
SET @currentWarehousingID = @warehousingID;
SET @retailTradeCommodityID = @retailTradeCommodityID;

SELECT Func_CreateRetailTradeCommoditySource(@iCommodityID, @saleNO, @currentWarehousingID, @retailTradeCommodityID) INTO @is;

SELECT 1 FROM t_commodityshopinfo WHERE F_CurrentWarehousingID = @warehousingID AND F_CommodityID = @iCommodityID; -- 检查商品表当值入库ID是否更新
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT 1 FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID AND F_NOSalable = 5; -- 检查入库商品表可售数量是否正确计算
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT 1 FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @retailTradeCommodityID AND F_WarehousingID = @warehousingID AND F_NO = @saleNO; -- 检查来源单是否创建
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case1 Testing Result';


SELECT '-------------------- Case2：购买数量超过可售数量，查找入库单商品B -------------------------' AS 'Case2';
INSERT INTO t_retailtrade (F_ShopID, F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID)
VALUES (2, 1, 'LS2019090414230000011235', 248321, 1, 'url=ashasoadigmnalskd', now(), 1, 1, 0, 1, '......', -1, now(), 200, 200, 0, 0, 0, 0, 0, 0, 0, 1);
SET @retailTradeID2 = LAST_INSERT_ID();

INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@retailTradeID2, @commID, 1, 10, 200, 10, 200, 200);
SET @retailTradeCommodityID2 = LAST_INSERT_ID();

SET @iCommodityID = @commID;
SET @saleNO = 10;
SET @currentWarehousingID = @warehousingID;
SET @rtcID = @retailTradeCommodityID2;


SELECT Func_CreateRetailTradeCommoditySource(@iCommodityID, @saleNO, @currentWarehousingID, @rtcID) INTO @is;

SELECT 1 FROM t_commodityshopinfo WHERE F_CurrentWarehousingID = @warehousingID2 AND F_CommodityID = @commID; -- 检查商品表当值入库ID是否更新
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case2 Testing Result';

SELECT 1 FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID AND F_NOSalable = 0; -- 检查入库商品表A可售数量是否正确计算
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case2 Testing Result';

SELECT 1 FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID2 AND F_NOSalable = 15; -- 检查入库商品表B可售数量是否正确计算
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case2 Testing Result';

SELECT 1 FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @rtcID AND F_WarehousingID = @warehousingID AND F_NO = 5; -- 检查来源单A是否创建
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case2 Testing Result';

SELECT 1 FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @rtcID AND F_WarehousingID = @warehousingID2 AND F_NO = 5; -- 检查来源单B是否创建
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case2 Testing Result';


SELECT '-------------------- Case3：购买数量超过可售数量，没有下一个入库单商品 -------------------------' AS 'Case3';
INSERT INTO t_retailtrade (F_ShopID, F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID)
VALUES (2, 1, 'LS2019090414230000011236', 2454821, 1, 'url=ashasoadigmnalskd', now(), 1, 1, 0, 1, '......', -1, now(), 200, 200, 0, 0, 0, 0, 0, 0, 0, 1);
SET @retailTradeID3 = LAST_INSERT_ID();

INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@retailTradeID3, @commID, 1, 20, 200, 20, 200, 200);
SET @retailTradeCommodityID3 = LAST_INSERT_ID();

SET @iCommodityID = @commID;
SET @saleNO = 20;
SET @currentWarehousingID = @warehousingID2;
SET @rtcID = @retailTradeCommodityID3;

SELECT Func_CreateRetailTradeCommoditySource(@iCommodityID, @saleNO, @currentWarehousingID, @rtcID) INTO @is;

SELECT 1 FROM t_commodityshopinfo WHERE F_CurrentWarehousingID = @warehousingID2 AND F_CommodityID = @commID; -- 检查商品表当值入库ID是否更新
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case3 Testing Result';

SELECT 1 FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID AND F_NOSalable = 0; -- 检查入库商品表A可售数量是否正确计算
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case3 Testing Result';

SELECT 1 FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID2 AND F_NOSalable = -5; -- 检查入库商品表B可售数量是否正确计算
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case3 Testing Result';

SELECT 1 FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @rtcID AND F_WarehousingID = @warehousingID2 AND F_NO = 20; -- 检查来源单是否创建
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case3 Testing Result';


DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @retailTradeCommodityID;
DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @retailTradeCommodityID2;
DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @retailTradeCommodityID3;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID2;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID2;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID3;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID3;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID2;
DELETE FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
DELETE FROM t_commodity WHERE F_ID = @commID;
DELETE FROM t_warehousing WHERE F_ID = @warehousingID;
DELETE FROM t_warehousing WHERE F_ID = @warehousingID2;

SELECT '-------------------- Case4：购买商品没有入库信息，只插入数据到零售单来源表 -------------------------' AS 'Case4';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'公仔面333','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',56,3,'1111111',2);
SET @commID = LAST_INSERT_ID();

INSERT INTO t_retailtrade (F_ShopID, F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID)
VALUES (2, 1, 'LS2019090414230000011237', 2454821, 1, 'url=ashasoadigmnalskd', now(), 1, 1, 0, 1, '......', -1, now(), 200, 200, 0, 0, 0, 0, 0, 0, 0, 1);
SET @retailTradeID4 = LAST_INSERT_ID();

INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@retailTradeID4, @commID, 1, 20, 200, 20, 200, 200);
SET @retailTradeCommodityID4 = LAST_INSERT_ID();

SET @iCommodityID = @commID;
SET @saleNO = 20;
SET @currentWarehousingID = NULL;
SET @retailTradeCommodityID = @retailTradeCommodityID4;

SELECT Func_CreateRetailTradeCommoditySource(@iCommodityID, @saleNO, @currentWarehousingID, @retailTradeCommodityID) INTO @is;

SELECT 1 FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @retailTradeCommodityID AND F_ReducingCommodityID = @iCommodityID; -- 检查来源单是否创建
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case4 Testing Result';

DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @retailTradeCommodityID4;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID4;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID4;
DELETE FROM t_commodity WHERE F_ID = @commID;

SELECT '-------------- Case5：正常添加来源表(入库单未审核，不减少入库单可售数量)，相当于商品没有入库信息，只插入数据到零售单来源表 --------------' AS 'Case5';

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

INSERT INTO t_warehousing (F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime)
VALUES (0, 1, 1, 1, 1, NOW());
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

SET @iCommodityID = @commID;
SET @saleNO = 10;
SET @currentWarehousingID = @warehousingID;

SELECT Func_CreateRetailTradeCommoditySource(@iCommodityID, @saleNO, @currentWarehousingID, @retailTradeCommodityID) INTO @is;

SELECT 1 FROM t_commodityshopinfo WHERE F_CurrentWarehousingID = @warehousingID AND F_CommodityID = @commID; -- 检查商品表当值入库ID是否更新
SELECT IF(FOUND_ROWS() = 0, '测试成功', '测试失败') AS 'Case5 Testing Result';

SELECT 1 FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID AND F_NOSalable = 15; -- 检查入库商品表可售数量是否正确计算
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case5 Testing Result';

SELECT 1 FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @retailTradeCommodityID AND F_NO = @saleNO; -- 检查来源单是否创建
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case5 Testing Result';

DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @retailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID;
DELETE FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
DELETE FROM t_commodity WHERE F_ID = @commID;
DELETE FROM t_warehousing WHERE F_ID = @warehousingID;

SELECT '-------------- Case6：正常添加来源表(一条入库单已审核，一条入库单未审核，前者减少入库单可售数量，后者不减少入库单可售数量) --------------' AS 'Case6';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'公仔面333','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',56,3,'1111111',2);
SET @commID = LAST_INSERT_ID();
INSERT INTO t_commodityshopinfo (F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@commID, 2, 8, 12, 15, -1, -1, NULL);
SET @iCommShopInfoID = last_insert_id();

INSERT INTO t_warehousing (F_ShopID, F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime)
VALUES (2, 1, 1, 1, 1, 1, NOW());
SET @warehousingID = LAST_INSERT_ID();

INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_NOSalable)
VALUES (@warehousingID, @commID, 15, 1, '公仔面333', 1, 10, 150, now(), 12, now(), NOW(), 15);
SET @warehousingCommodityID = LAST_INSERT_ID();

INSERT INTO t_warehousing (F_ShopID, F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime)
VALUES (2, 0, 1, 1, 1, 1, NOW());
SET @warehousingID2 = LAST_INSERT_ID();

INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_NOSalable)
VALUES (@warehousingID2, @commID, 20, 1, '公仔面333', 1, 10, 200, now(), 12, now(), NOW(), 20);
SET @warehousingCommodityID2 = LAST_INSERT_ID();

INSERT INTO t_retailtrade (F_ShopID, F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID)
VALUES (2, 1, 'LS2019090414230000011239', 2132185, 1, 'url=ashasoadigmnalskd', now(), 1, 1, 0, 1, '......', -1, now(), 200, 200, 0, 0, 0, 0, 0, 0, 0, 1);
SET @retailTradeID = LAST_INSERT_ID();

INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@retailTradeID, @commID, 1, 10, 200, 10, 200, 200);
SET @retailTradeCommodityID = LAST_INSERT_ID();

SET @iCommodityID = @commID;
SET @saleNO = 25;
SET @currentWarehousingID = @warehousingID;
SET @retailTradeCommodityID = @retailTradeCommodityID;

SELECT Func_CreateRetailTradeCommoditySource(@iCommodityID, @saleNO, @currentWarehousingID, @retailTradeCommodityID) INTO @is;

SELECT 1 FROM t_commodityshopinfo WHERE F_CurrentWarehousingID = @warehousingID AND F_CommodityID = @commID; -- 检查商品表当值入库ID是否更新
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case6 Testing Result';

SELECT 1 FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID AND F_WarehousingID = @warehousingID AND F_NOSalable = -10; -- 检查入库商品表可售数量是否正确计算
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case6 Testing Result';

SELECT 1 FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @retailTradeCommodityID AND F_NO = @saleNO; -- 检查来源单是否创建
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case6 Testing Result';

SELECT 1 FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID2 AND F_WarehousingID = @warehousingID2 AND F_NOSalable = 20; -- 检查入库商品表可售数量是否正确计算
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case6 Testing Result';

DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @retailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID2;
DELETE FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
DELETE FROM t_commodity WHERE F_ID = @commID;
DELETE FROM t_warehousing WHERE F_ID = @warehousingID;
DELETE FROM t_warehousing WHERE F_ID = @warehousingID2;



SELECT '-------------------- Case7：售卖没有入库单的商品，零售来源表的warehousingID应为null -------------------------' AS 'Case7';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'公仔面333','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',56,3,'1111111',2);
SET @commID = LAST_INSERT_ID();

INSERT INTO t_retailtrade (F_ShopID, F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID)
VALUES (2, 1, 'LS2019090414230000011234', 2132185, 1, 'url=ashasoadigmnalskd', now(), 1, 1, 0, 1, '......', -1, now(), 200, 200, 0, 0, 0, 0, 0, 0, 0, 1);
SET @retailTradeID = LAST_INSERT_ID();

INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer)
VALUES (@retailTradeID, @commID, 1, 10, 200, 10, 200, 200);
SET @retailTradeCommodityID = LAST_INSERT_ID();

SET @iCommodityID = @commID;
SET @saleNO = 10;
SET @currentWarehousingID = @warehousingID;
SET @retailTradeCommodityID = @retailTradeCommodityID;

SELECT Func_CreateRetailTradeCommoditySource(@iCommodityID, @saleNO, @currentWarehousingID, @retailTradeCommodityID) INTO @is;

SELECT 1 FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @retailTradeCommodityID;
SELECT 1 FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @retailTradeCommodityID AND F_WarehousingID IS NULL AND F_NO = @saleNO; -- 检查来源单是否创建
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case7.3 Testing Result';

DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = @retailTradeCommodityID;
DELETE FROM t_retailtradecommodity WHERE F_ID = @retailTradeCommodityID;
DELETE FROM t_retailtrade WHERE F_ID = @retailTradeID;
DELETE FROM t_commodity WHERE F_ID = @commID;
-- 12099