SELECT '++++++++++++++++++ Test_SPD_PurchasingOrderCommodity_CheckCommodity.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:正常测试 -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_PurchasingOrderCommodity_CheckCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

-- t_purchasingordercommodity的F_commodityID是t_commodity的外键，不能插入不存在的商品ID


SELECT '-------------------- Case2:采购订单商品对应的商品不能为已删除商品 -------------------------' AS 'Case2';
-- 
INSERT INTO t_purchasingordercommodity (F_PurchasingOrderID, F_CommodityID, F_CommodityNO, F_CommodityName, F_BarcodeID, F_PackageUnitID, F_PriceSuggestion, F_CreateDatetime, F_UpdateDatetime)
VALUES (12, 49, 300, '百事青椒味薯片1', 101, 1, 11.1, '2019/5/30 9:17:01', '2019/5/30 9:17:01');
-- 
SET @iPurchasingOrderCommodityID = LAST_INSERT_ID();
SET @iErrorCode := 0;
SET @sErrorMsg := '';
-- 
CALL SPD_PurchasingOrderCommodity_CheckCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('采购订单商品', @iPurchasingOrderCommodityID, '对应的商品不能为已删除商品') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case2 Testing Result';
-- 
DELETE FROM t_purchasingordercommodity WHERE F_ID = @iPurchasingOrderCommodityID;



SELECT '-------------------- Case3:采购订单商品对应的商品不能为多包装商品 -------------------------' AS 'Case3';
-- 
INSERT INTO t_purchasingordercommodity (F_PurchasingOrderID, F_CommodityID, F_CommodityNO, F_CommodityName, F_BarcodeID, F_PackageUnitID, F_PriceSuggestion, F_CreateDatetime, F_UpdateDatetime)
VALUES (12, 51, 300, '乐事牛肉味薯片2', 101, 1, 11.1, '2019/5/30 9:17:01', '2019/5/30 9:17:01');
-- 
SET @iPurchasingOrderCommodityID = LAST_INSERT_ID();
SET @iErrorCode := 0;
SET @sErrorMsg := '';
-- 
CALL SPD_PurchasingOrderCommodity_CheckCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('采购订单商品', @iPurchasingOrderCommodityID, '对应的商品不能为多包装商品') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case3 Testing Result';
-- 
DELETE FROM t_purchasingordercommodity WHERE F_ID = @iPurchasingOrderCommodityID;


SELECT '-------------------- Case4:采购订单商品对应的商品不能为组合商品 -------------------------' AS 'Case3';
-- 
INSERT INTO t_purchasingordercommodity (F_PurchasingOrderID, F_CommodityID, F_CommodityNO, F_CommodityName, F_BarcodeID, F_PackageUnitID, F_PriceSuggestion, F_CreateDatetime, F_UpdateDatetime)
VALUES (12, 45, 300, '薯愿香辣味薯片', 101, 1, 11.1, '2019/5/30 9:17:01', '2019/5/30 9:17:01');
-- 
SET @iPurchasingOrderCommodityID = LAST_INSERT_ID();
SET @iErrorCode := 0;
SET @sErrorMsg := '';
-- 
CALL SPD_PurchasingOrderCommodity_CheckCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('采购订单商品', @iPurchasingOrderCommodityID, '对应的商品不能为组合型商品') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case4 Testing Result';
-- 
DELETE FROM t_purchasingordercommodity WHERE F_ID = @iPurchasingOrderCommodityID;


SELECT '-------------------- Case5:采购订单商品中的商品数量必须大于0 -------------------------' AS 'Case3';
-- 
INSERT INTO t_purchasingordercommodity (F_PurchasingOrderID, F_CommodityID, F_CommodityNO, F_CommodityName, F_BarcodeID, F_PackageUnitID, F_PriceSuggestion, F_CreateDatetime, F_UpdateDatetime)
VALUES (12, 2, 0, '可口可乐', 101, 1, 11.1, '2019/5/30 9:17:01', '2019/5/30 9:17:01');
-- 
SET @iPurchasingOrderCommodityID = LAST_INSERT_ID();
SET @iErrorCode := 0;
SET @sErrorMsg := '';
-- 
CALL SPD_PurchasingOrderCommodity_CheckCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('采购订单商品', @iPurchasingOrderCommodityID, '的商品数量必须大于0') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case4 Testing Result';
-- 
DELETE FROM t_purchasingordercommodity WHERE F_ID = @iPurchasingOrderCommodityID;

SELECT '-------------------- Case6:采购订单商品对应的商品不能为服务商品 -------------------------' AS 'Case6';
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'顺丰','快递','个',4,NULL,4,4,'SF',1,
0,38,5.8,5.5,1,1,null,
0,30,'2019-01-14','0',0,0,'顺丰快递',0,3);
SET @iCommodityID = LAST_INSERT_ID();
-- 
INSERT INTO t_purchasingordercommodity (F_PurchasingOrderID, F_CommodityID, F_CommodityNO, F_CommodityName, F_BarcodeID, F_PackageUnitID, F_PriceSuggestion, F_CreateDatetime, F_UpdateDatetime)
VALUES (12, @iCommodityID, 300, '顺丰', 101, 1, 11.1, '2019/5/30 9:17:01', '2019/5/30 9:17:01');
-- 
SET @iPurchasingOrderCommodityID = LAST_INSERT_ID();
SET @iErrorCode := 0;
SET @sErrorMsg := '';
-- 
CALL SPD_PurchasingOrderCommodity_CheckCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('采购订单商品', @iPurchasingOrderCommodityID, '对应的商品不能为服务型商品') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case6 Testing Result';
-- 
DELETE FROM t_purchasingordercommodity WHERE F_ID = @iPurchasingOrderCommodityID;
