SELECT '++++++++++++++++++ Test_SPD_Commodity_CheckCouponScope.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:正常测试 -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckCouponScope(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

-- 
SELECT '-------------------- Case2:组合商品有优惠券范围依赖，SPD报错 -------------------------' AS 'Case2';
-- 新建普通商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'星巴克AB123','咖啡123','个',4,'支',4,4,'SP',1,
5,38,5.8,5.5,1,1,null,
3,30,'2019-01-14','20',0,0,'星巴克AB',0,1);
SET @iCommodityID = LAST_INSERT_ID();
-- 
-- 创建优惠券
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0, 0, 0, 1, 1, 1, '未起用的优惠券', 'Color010', '', 1, 0, '9:00:00', '12:00:00', '3333-03-03 15:15:15', '3333-03-20 15:15:15', 1000, 1000, 0);
SET @couponID = last_insert_id();
-- 
-- 创建优惠券范围
INSERT INTO t_couponscope (F_CouponID, F_CommodityID, F_CommodityName)
VALUES (@couponID, @iCommodityID, '星巴克AB123');
SET @couponscopeID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckCouponScope(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('商品', @iCommodityID ,'不是单品，不能参与优惠券范围') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case2 Testing Result';
-- 
-- 
DELETE FROM t_couponscope WHERE F_ID = @couponscopeID;
DELETE FROM t_coupon WHERE F_ID = @couponID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;


SELECT '-------------------- Case3:多包装商品有优惠券范围依赖，SPD报错 -------------------------' AS 'Case3';
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'星巴克AB123','咖啡123','个',4,'支',4,4,'SP',1,
5,38,5.8,5.5,1,1,null,
3,30,'2019-01-14','20',0,0,'星巴克AB',0,2);
SET @iCommodityID = LAST_INSERT_ID();
-- 
-- 创建优惠券
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0, 0, 0, 1, 1, 1, '未起用的优惠券', 'Color010', '', 1, 0, '9:00:00', '12:00:00', '3333-03-03 15:15:15', '3333-03-20 15:15:15', 1000, 1000, 0);
SET @couponID = last_insert_id();
-- 
-- 创建优惠券范围
INSERT INTO t_couponscope (F_CouponID, F_CommodityID, F_CommodityName)
VALUES (@couponID, @iCommodityID, '星巴克AB123');
SET @couponscopeID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckCouponScope(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('商品', @iCommodityID ,'不是单品，不能参与优惠券范围') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case3 Testing Result';
-- 
-- 
DELETE FROM t_couponscope WHERE F_ID = @couponscopeID;
DELETE FROM t_coupon WHERE F_ID = @couponID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;



SELECT '-------------------- Case4:服务商品有优惠券范围依赖，SPD报错 -------------------------' AS 'Case4';
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'中通','快递','个',4,NULL,4,4,'ZT',1,
0,38,5.8,5.5,1,1,null,
0,30,'2019-01-14','0',0,0,'中通快递',0,3);
SET @iCommodityID = LAST_INSERT_ID();
-- 
-- 创建优惠券
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0, 0, 0, 1, 1, 1, '未起用的优惠券', 'Color010', '', 1, 0, '9:00:00', '12:00:00', '3333-03-03 15:15:15', '3333-03-20 15:15:15', 1000, 1000, 0);
SET @couponID = last_insert_id();
-- 
-- 创建优惠券范围
INSERT INTO t_couponscope (F_CouponID, F_CommodityID, F_CommodityName)
VALUES (@couponID, @iCommodityID, '星巴克AB123');
SET @couponscopeID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckCouponScope(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('商品', @iCommodityID ,'不是单品，不能参与优惠券范围') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case4 Testing Result';
-- 
-- 
DELETE FROM t_couponscope WHERE F_ID = @couponscopeID;
DELETE FROM t_coupon WHERE F_ID = @couponID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;


SELECT '-------------------- Case5:参与优惠券范围的普通商品是已删除状态的，SPD报错 -------------------------' AS 'Case5';
-- 
-- 新建普通商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, F_LatestPricePurchase, F_PriceRetail, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_NO, F_Type, F_NOStart, F_PurchasingPriceStart, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4, F_CurrentWarehousingID)
VALUES (2/*F_Status*/, '优惠券普通商品', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 8, 12, 11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0, 0, -1, -1, NULL, '2020-04-20 08:57:14', '2020-04-20 08:57:14', NULL, NULL, NULL, NULL, NULL);
SET @iCommodityID = last_insert_id();
-- 
-- 创建优惠券
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0, 0, 0, 1, 1, 1, '未起用的优惠券', 'Color010', '', 1, 0, '9:00:00', '12:00:00', '3333-03-03 15:15:15', '3333-03-20 15:15:15', 1000, 1000, 0);
SET @couponID = last_insert_id();
-- 
-- 创建优惠券范围
INSERT INTO t_couponscope (F_CouponID, F_CommodityID, F_CommodityName)
VALUES (@couponID, @iCommodityID, '优惠券普通商品');
SET @couponscopeID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckCouponScope(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('商品', @iCommodityID ,'参与了优惠券范围, 不能是已删除状态(不能被删除)') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case5 Testing Result';
-- 
-- 
DELETE FROM t_couponscope WHERE F_ID = @couponscopeID;
DELETE FROM t_coupon WHERE F_ID = @couponID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;