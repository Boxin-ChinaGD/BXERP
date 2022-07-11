SELECT '++++++++++++++++++ Test_SP_ProviderCommodity_Delete.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: 传入供应商ID和商品ID的时候删除对应的供应商商品 -------------------------' AS 'Case1';

INSERT INTO nbr.t_provider (F_Name, F_DistrictID, F_Address, F_ContactName, F_Mobile)
VALUES ('台湾雅方食品有限公司333', 3, '台湾33', 'angel33', '13129355772');
SET @iProviderID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 1;


INSERT INTO t_providercommodity (F_CommodityID, F_ProviderID)
VALUES (@iCommodityID, @iProviderID);

CALL SP_ProviderCommodity_Delete(@iErrorCode, @sErrorMsg, @iCommodityID, @iProviderID);

SELECT @sErrorMsg;
SELECT 1 FROM t_providercommodity WHERE F_CommodityID = @iCommodityID AND F_ProviderID = @iProviderID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'CASE1 Testing Result';

SELECT '-------------------- Case2: 只传入商品ID，删除所有与该商品相关的供应商商品 -------------------------' AS 'Case2';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'星巴克AB123','咖啡123','个',4,'支',4,4,'SP',1,
5.8,5.5,1,1,null,
3,30,'2019-01-14','20',0,0,'星巴克AB',0);
SET @iCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = @iCommodityID;
SET @iProviderID = 0;

CALL SP_ProviderCommodity_Delete(@iErrorCode, @sErrorMsg, @iCommodityID, @iProviderID);

SELECT @sErrorMsg;
SELECT 1 FROM t_providercommodity WHERE F_CommodityID = @iCommodityID AND F_ProviderID = @iProviderID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'CASE2 Testing Result';

DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- Case3:不传入商品ID和供应商ID进行删除-------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 0;
SET @iProviderID = 0;

CALL SP_ProviderCommodity_Delete(@iErrorCode, @sErrorMsg, @iCommodityID, @iProviderID);

SELECT @sErrorMsg;
SELECT 1 FROM t_providercommodity WHERE F_CommodityID = @iCommodityID AND F_ProviderID = @iProviderID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'CASE3 Testing Result';