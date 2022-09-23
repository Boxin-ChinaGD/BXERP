SELECT '++++++++++++++++++ Test_SP_Warehouse_RetrieveInventory.sql ++++++++++++++++++++';
SELECT '-------------------- Case1:正常查询  -------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 2;
--	SET @fTotalInventory = 0.000000;
--	SET @fMaxTotalInventory = 0.000000;
--	SET @sName = '';

--	CALL SP_Warehouse_RetrieveInventory(@iErrorCode, @sErrorMsg, @fTotalInventory, @fMaxTotalInventory, @sName);
CALL SP_Warehouse_RetrieveInventory(@iErrorCode, @sErrorMsg, @iShopID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT '-------------------- Case2:查询创建单品后库存总额最高的商品及其库存总额-------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_Type)
VALUES (0,'可比克薯片22','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,30,'2018-04-14 01:00:01','20',0,0,'1111111',0);

SET @commodityID=last_insert_id();
SET @iShopID = 2;
INSERT INTO t_commodityshopinfo (F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@commodityID, 2,10000000/*F_LatestPricePurchase*/,12, 2, -1, -1, NULL);
SET @iCommShopInfoID = last_insert_id();

CALL SP_Warehouse_RetrieveInventory(@iErrorCode, @sErrorMsg, @iShopID);

SELECT @iErrorCode;
SELECT @sErrorMsg;

SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';

DELETE FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
DELETE FROM t_commodity WHERE F_ID = @commodityID;

SELECT '-------------------- Case3:查询创建单品后其库存总额为负-------------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_Type)
VALUES (0,'可比克薯片22','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,30,'2018-04-14 01:00:01','20',0,0,'1111111',0);

SET @commodityID=last_insert_id();
SET @iShopID = 2;
INSERT INTO t_commodityshopinfo (F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@commodityID, 2,10000000/*F_LatestPricePurchase*/,12, -2, -1, -1, NULL);
SET @iCommShopInfoID = last_insert_id();

CALL SP_Warehouse_RetrieveInventory(@iErrorCode, @sErrorMsg, @iShopID);

SELECT @iErrorCode;
SELECT @sErrorMsg;

SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case3 Testing Result';
DELETE FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
DELETE FROM t_commodity WHERE F_ID = @commodityID;

-- 下面测试做不到，需要先把其他测试商品删掉
-- SELECT '-------------------- Case4:查询创建单品后其最大库存总额为负-------------------------' AS 'Case4';
-- 需要手动测试这个case。步骤：
-- 删除所有商品(就是运行下面这一行代码)：
-- update T_Commodity set F_Status = 2;
-- 然后再跑case 3。
-- SELECT '-------------------- Case5:无未删除的单品时，最大库存总额为0-------------------------' AS 'Case5';
-- 模仿case 4，只跑case 3中的CALL SP_Warehouse_RetrieveInventory(@iErrorCode, @sErrorMsg);和结果验证即可