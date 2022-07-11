SELECT '++++++++++++++++++ Test_SPD_InventoryCommodity_CheckCommodity.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:正常测试 -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_InventoryCommodtiy_CheckCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT '-------------------- Case2:盘点单商品相同的商品在同一个盘点单 -------------------------' AS 'Case2';
INSERT INTO T_InventorySheet (F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
VALUES (1,200,4,4,'2017-12-06','...........................zz');
SET @iInventorysheetID = LAST_INSERT_ID();
-- 
INSERT INTO t_inventorycommodity ( F_InventorySheetID, F_CommodityID, F_CommodityName, F_Specification, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem)
VALUES (@iInventorysheetID, 1, '薯片', 1, 1, 1, 0, 0);
SET @iInventoryCommodityID = LAST_INSERT_ID();
-- 
INSERT INTO t_inventorycommodity ( F_InventorySheetID, F_CommodityID, F_CommodityName, F_Specification, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem)
VALUES (@iInventorysheetID, 1, '薯片', 1, 1, 1, 0, 0);
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_InventoryCommodtiy_CheckCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('盘点单商品', @iInventoryCommodityID ,'不能有相同的商品在同一个盘点单') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case2 Testing Result';
-- 
DELETE FROM t_Inventorycommodity WHERE F_ID = @iInventoryCommodityID;
DELETE FROM t_Inventorycommodity WHERE F_ID = @iInventoryCommodityID + 1;
DELETE FROM T_InventorySheet WHERE F_ID = @iInventorysheetID;

SELECT '-------------------- Case3:盘点单商品对应的商品的商品类型不是单品 -------------------------' AS 'Case3';
INSERT INTO T_InventorySheet (F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
VALUES (1,200,4,4,'2017-12-06','...........................zz');
SET @iInventorysheetID = LAST_INSERT_ID();
-- 
INSERT INTO t_inventorycommodity ( F_InventorySheetID, F_CommodityID, F_CommodityName, F_Specification, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem)
VALUES (@iInventorysheetID, 45, '薯愿香辣味薯片', 1, 49, 1, 200, 0);
SET @iInventoryCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_InventoryCommodtiy_CheckCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('盘点单商品', @iInventoryCommodityID ,'对应的商品的商品类型不是单品') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case3 Testing Result';
-- 
DELETE FROM t_Inventorycommodity WHERE F_ID = @iInventoryCommodityID;
DELETE FROM T_InventorySheet WHERE F_ID = @iInventorysheetID;

--	做不到
--	SELECT '-------------------- Case4:盘点单商品对应的商品不存在 -------------------------' AS 'Case4';
--	INSERT INTO T_InventorySheet (F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
--	VALUES (1,200,4,4,'2017-12-06','...........................zz');
--	SET @iInventorysheetID = LAST_INSERT_ID();
--	-- 
--	INSERT INTO t_inventorycommodity ( F_InventorySheetID, F_CommodityID, F_CommodityName, F_Specification, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem)
--	VALUES (@iInventorysheetID, -1, '薯愿香辣味薯片', 1, 49, 1, 200, 0);
--	SET @iInventorysheetCommodityID = LAST_INSERT_ID();
--	-- 
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	-- 
--	CALL SPD_InventoryCommodtiy_CheckCommodity(@iErrorCode, @sErrorMsg);
--	SELECT @iErrorCode, @sErrorMsg;
--	-- 
--	SELECT IF(@sErrorMsg = CONCAT('盘点单商品', @iInventoryCommodityID ,'对应的商品不存在') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case4 Testing Result';
--	-- 
--	DELETE FROM t_Inventorycommodity WHERE F_ID = @iInventoryCommodityID;
--	DELETE FROM T_InventorySheet WHERE F_ID = @iInventorysheetID;

SELECT '-------------------- Case5:盘点单商品对应的商品的商品类型是服务商品不是单品 -------------------------' AS 'Case5';
INSERT INTO T_InventorySheet (F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
VALUES (1,200,4,4,'2017-12-06','...........................zz');
SET @iInventorysheetID = LAST_INSERT_ID();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'顺丰','快递','个',4,NULL,4,4,'SF',1,
0,38,5.8,5.5,1,1,null,
0,30,'2019-01-14','0',0,0,'顺丰快递',0,3);
SET @iCommodityID = LAST_INSERT_ID();
-- 
INSERT INTO t_inventorycommodity ( F_InventorySheetID, F_CommodityID, F_CommodityName, F_Specification, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem)
VALUES (@iInventorysheetID, @iCommodityID, '顺丰', 1, 49, 1, 200, 0);
SET @iInventoryCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_InventoryCommodtiy_CheckCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('盘点单商品', @iInventoryCommodityID ,'对应的商品的商品类型不是单品') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case5 Testing Result';
-- 
DELETE FROM t_Inventorycommodity WHERE F_ID = @iInventoryCommodityID;
DELETE FROM T_InventorySheet WHERE F_ID = @iInventorysheetID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;