SELECT '++++++++++++++++++ Test_SP_Commodity_RetrieveInventory.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:查询未删除状态商品的库存 -------------------------' AS 'Case1';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_Type)
VALUES (0,'可比克薯片22','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,30,'2018-04-14 01:00:01','20',0,0,'1111111',0);
SET @iID = last_insert_id();

SET @iErrorCode=0;
SET @sErrorMsg = '';
SET @iShopID = 2;

CALL SP_Commodity_RetrieveInventory(@iErrorCode, @sErrorMsg, @iID, @iShopID);

SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case2:查询已经删除状态商品的库存 -------------------------' AS 'Case2';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (2,'公仔面5','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,30,'2018-04-14','20',56,3,'1111111',2);

SET @iID = last_insert_id();

SET @iErrorCode=0;
SET @sErrorMsg = '';
SET @iShopID = 2;

CALL SP_Commodity_RetrieveInventory(@iErrorCode, @sErrorMsg, @iID, @iShopID);

SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 2, '测试成功', '测试失败') AS 'Case2 Testing Result';

DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case3:查询不存在的商品库存 -------------------------' AS 'Case3';

SET @iID = -1;
SET @sErrorMsg = '';
SET @iShopID = 2;
CALL SP_Commodity_RetrieveInventory(@iErrorCode, @sErrorMsg, @iID, @iShopID);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case3 Testing Result';