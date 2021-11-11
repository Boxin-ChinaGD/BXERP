SELECT '++++++++++++++++++ Test_SPD_ReturnCommoditySheetCommodity_CheckCommodity.sql ++++++++++++++++++++';
SELECT '------------------ CASE1:正常测试 --------------------' AS 'CASE1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_ReturnCommoditySheetCommodity_CheckCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode,@sErrorMsg;
-- 
SELECT IF(@iErrorCode = 0 AND @sErrorMsg = '', '测试成功', '测试失败') AS 'CASE1 RESULT';
-- 
SELECT '------------------ CASE2:退货商品是组合商品 --------------------' AS 'CASE2';
INSERT INTO t_returncommoditysheet (F_StaffID, F_ProviderID)
VALUES (3, 3);
SET @iReturnCommoditySheetID = last_insert_id();
-- 
INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID, F_BarcodeID, F_NO, F_Specification)
VALUES (@iReturnCommoditySheetID, 45, 7, 100, '箱');
SET @iReturnCommoditySheetCommodityID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_ReturnCommoditySheetCommodity_CheckCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode,@sErrorMsg;
-- 
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = concat('退货单商品', @iReturnCommoditySheetCommodityID, '只能是未删除的普通商品'), '测试成功', '测试失败') AS 'CASE2 RESULT';
-- 
DELETE FROM t_returncommoditysheetcommodity WHERE F_ID = @iReturnCommoditySheetCommodityID;
DELETE FROM t_ReturnCommoditySheet WHERE F_ID = @iReturnCommoditySheetID;
-- 
SELECT '------------------ CASE3:退货商品是多包装商品 --------------------' AS 'CASE3';
INSERT INTO t_returncommoditysheet (F_StaffID, F_ProviderID)
VALUES (3, 3);
SET @iReturnCommoditySheetID = last_insert_id();
-- 
INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID, F_BarcodeID, F_NO, F_Specification)
VALUES (@iReturnCommoditySheetID, 52, 7, 100, '箱');
SET @iReturnCommoditySheetCommodityID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_ReturnCommoditySheetCommodity_CheckCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode,@sErrorMsg;
-- 
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = concat('退货单商品', @iReturnCommoditySheetCommodityID, '只能是未删除的普通商品'), '测试成功', '测试失败') AS 'CASE2 RESULT';
-- 
DELETE FROM t_returncommoditysheetcommodity WHERE F_ID = @iReturnCommoditySheetCommodityID;
DELETE FROM t_ReturnCommoditySheet WHERE F_ID = @iReturnCommoditySheetID;
-- 
SELECT '------------------ CASE4:退货商品是已删除商品 --------------------' AS 'CASE4';
INSERT INTO t_returncommoditysheet (F_StaffID, F_ProviderID)
VALUES (3, 3);
SET @iReturnCommoditySheetID = last_insert_id();
-- 
INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID, F_BarcodeID, F_NO, F_Specification)
VALUES (@iReturnCommoditySheetID, 50, 7, 100, '箱');
SET @iReturnCommoditySheetCommodityID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_ReturnCommoditySheetCommodity_CheckCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode,@sErrorMsg;
-- 
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = concat('退货单商品', @iReturnCommoditySheetCommodityID, '只能是未删除的普通商品'), '测试成功', '测试失败') AS 'CASE4 RESULT';
-- 
DELETE FROM t_returncommoditysheetcommodity WHERE F_ID = @iReturnCommoditySheetCommodityID;
DELETE FROM t_ReturnCommoditySheet WHERE F_ID = @iReturnCommoditySheetID;
-- 
SELECT '------------------ CASE5:退货商品数量为0 --------------------' AS 'CASE5';
INSERT INTO t_returncommoditysheet (F_StaffID, F_ProviderID)
VALUES (3, 3);
SET @iReturnCommoditySheetID = last_insert_id();
-- 
INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID, F_BarcodeID, F_NO, F_Specification)
VALUES (@iReturnCommoditySheetID, 5, 7, 0, '箱');
SET @iReturnCommoditySheetCommodityID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_ReturnCommoditySheetCommodity_CheckCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode,@sErrorMsg;
-- 
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = concat('退货单商品', @iReturnCommoditySheetCommodityID, '的商品数量必须大于0'), '测试成功', '测试失败') AS 'CASE5 RESULT';
-- 
DELETE FROM t_returncommoditysheetcommodity WHERE F_ID = @iReturnCommoditySheetCommodityID;
DELETE FROM t_ReturnCommoditySheet WHERE F_ID = @iReturnCommoditySheetID;
-- 
SELECT '------------------ CASE6:退货商品是服务商品 --------------------' AS 'CASE6';
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'顺丰','快递','个',4,NULL,4,4,'SF',1,
0,38,5.8,5.5,1,1,null,
0,30,'2019-01-14','0',0,0,'顺丰快递',0,3);
SET @iCommodityID = LAST_INSERT_ID();
-- 
INSERT INTO t_returncommoditysheet (F_StaffID, F_ProviderID)
VALUES (3, 3);
SET @iReturnCommoditySheetID = last_insert_id();
-- 
INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID, F_BarcodeID, F_NO, F_Specification)
VALUES (@iReturnCommoditySheetID, @iCommodityID, 7, 100, '箱');
SET @iReturnCommoditySheetCommodityID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_ReturnCommoditySheetCommodity_CheckCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode,@sErrorMsg;
-- 
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = concat('退货单商品', @iReturnCommoditySheetCommodityID, '只能是未删除的普通商品'), '测试成功', '测试失败') AS 'CASE6 RESULT';
-- 
DELETE FROM t_returncommoditysheetcommodity WHERE F_ID = @iReturnCommoditySheetCommodityID;
DELETE FROM t_ReturnCommoditySheet WHERE F_ID = @iReturnCommoditySheetID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;