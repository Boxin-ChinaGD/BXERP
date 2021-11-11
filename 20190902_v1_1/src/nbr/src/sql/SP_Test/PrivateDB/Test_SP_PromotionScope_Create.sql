SELECT '++++++++++++++++++ Test_SP_PromotionScope_Create.sql ++++++++++++++++++++';
SELECT '------------------- Case1 正常创建 ------------------' as 'Case 1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @name = '满10-1';
SET @status = 0;
SET @type = 0;
SET @datetimestart = now();
SET @datetimeend = now();
SET @excecutionthreshold = 10;
SET @excecutionamount = 1;
SET @excecutiondiscount = 1;
SET @scope = 0;
SET @staff = 1 ;
INSERT INTO t_promotion (F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES (@name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @staff, now(), now());
SET @iID = last_insert_id();

SET @iErrorCode = 0;
SET @iPromotionID = @iID;
SET @iCommodityID = 1;
CALL SP_PromotionScope_Create(@iErrorCode, @sErrorMsg, @iPromotionID, @iCommodityID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PromotionScope WHERE F_ID = last_insert_id();
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

DELETE FROM t_promotionScope WHERE F_ID = LAST_INSERT_ID();
DELETE FROM t_promotion WHERE F_ID = @iID;

SELECT '------------------- Case2 用不存在的CommodityID创建 ------------------' as 'Case 2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @name = '满10-1';
SET @status = 0;
SET @type = 0;
SET @datetimestart = now();
SET @datetimeend = now();
SET @excecutionthreshold = 10;
SET @excecutionamount = 1;
SET @excecutiondiscount = 1;
SET @scope = 0;
SET @staff = 1 ;
INSERT INTO t_promotion (F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES (@name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @staff, now(), now());
SET @iID = last_insert_id();

SET @iErrorCode = 0;
SET @iPromotionID = @iID;
SET @iCommodityID = -999;
CALL SP_PromotionScope_Create(@iErrorCode, @sErrorMsg, @iPromotionID, @iCommodityID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PromotionScope WHERE F_PromotionID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '不能使用不存在的CommodityID进行创建', '测试成功', '测试失败') AS 'Case2 Testing Result';

DELETE FROM t_promotion WHERE F_ID = @iID;

SELECT '------------------- Case3 用不存在的PromotionID创建 ------------------' as 'Case 3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @name = '满10-1';
SET @status = 0;
SET @type = 0;
SET @datetimestart = now();
SET @datetimeend = now();
SET @excecutionthreshold = 10;
SET @excecutionamount = 1;
SET @excecutiondiscount = 1;
SET @scope = 0;
SET @staff = 1 ;
SET @iErrorCode = 0;
SET @iPromotionID = -969;
SET @iCommodityID = 1;
CALL SP_PromotionScope_Create(@iErrorCode, @sErrorMsg, @iPromotionID, @iCommodityID);
-- 
SELECT @sErrorMsg;
SELECT IF( @iErrorCode = 7, '测试成功', '测试失败') AS 'Case3 Testing Result';

SELECT '------------------- Case4 服务商品参与促销 ------------------' as 'Case 4';
INSERT INTO t_promotion (F_Name, F_SN, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES ('开始的全场满10-1x', 'CX201906050001x',0, 0, now(),  DATE_ADD(now(),INTERVAL 7 DAY), 10, 1, 8, 0, 2, now(), now());
SET @iPromotionID = last_insert_id();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'蜘蛛侠1号aaa','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',3);
SET @iCommodityID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @name = '满10-1';
SET @status = 0;
SET @type = 0;
SET @datetimestart = now();
SET @datetimeend = now();
SET @excecutionthreshold = 10;
SET @excecutionamount = 1;
SET @excecutiondiscount = 1;
SET @scope = 0;
SET @staff = 1 ;
SET @iErrorCode = 0;
CALL SP_PromotionScope_Create(@iErrorCode, @sErrorMsg, @iPromotionID, @iCommodityID);
-- 
SELECT @sErrorMsg;
SELECT IF( @iErrorCode = 7, '测试成功', '测试失败') AS 'Case4 Testing Result';
-- 
DELETE FROM t_promotionscope WHERE F_ID = last_insert_id(); 
DELETE FROM t_promotion WHERE F_ID = @iPromotionID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '------------------- Case5:创建一个促销范围,主表正常 ------------------' as 'Case 5';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @name = '满10-2';
SET @status = 0;
SET @type = 0;
SET @datetimestart = now();
SET @datetimeend = now();
SET @excecutionthreshold = 10;
SET @excecutionamount = 1;
SET @excecutiondiscount = 1;
SET @scope = 0;
SET @staff = 1 ;
INSERT INTO t_promotion (F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES (@name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @staff, now(), now());
SET @iID = last_insert_id();

SET @iErrorCode = 0;
SET @iPromotionID = @iID;
SET @iCommodityID = 1;
CALL SP_PromotionScope_Create(@iErrorCode, @sErrorMsg, @iPromotionID, @iCommodityID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PromotionScope WHERE F_ID = last_insert_id();
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

DELETE FROM t_promotionScope WHERE F_ID = LAST_INSERT_ID();
DELETE FROM t_promotion WHERE F_ID = @iID;

SELECT '------------------- Case6:创建一个促销范围，主表被删除 ------------------' as 'Case 6';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @name = '满10-2';
SET @status = 0;
SET @type = 0;
SET @datetimestart = now();
SET @datetimeend = now();
SET @excecutionthreshold = 10;
SET @excecutionamount = 1;
SET @excecutiondiscount = 1;
SET @scope = 0;
SET @staff = 1 ;
INSERT INTO t_promotion (F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES (@name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @staff, now(), now());
SET @iID = last_insert_id();

DELETE FROM t_promotion WHERE F_ID = @iID;

SET @iErrorCode = 0;
SET @iPromotionID = @iID;
SET @iCommodityID = 1;
CALL SP_PromotionScope_Create(@iErrorCode, @sErrorMsg, @iPromotionID, @iCommodityID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PromotionScope WHERE F_ID = last_insert_id();
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'Case1 Testing Result';


SELECT '------------------- Case7:创建一个促销范围,主表不存在 ------------------' as 'Case 7';
SET @iErrorCode = 0;
SET @iPromotionID = 99999;
SET @iCommodityID = 1;
CALL SP_PromotionScope_Create(@iErrorCode, @sErrorMsg, @iPromotionID, @iCommodityID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'Case1 Testing Result';


SELECT '------------------- Case8:创建一个促销范围,商品类型为多包装商品 ------------------' as 'Case 8';
INSERT INTO t_promotion (F_Name, F_SN, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES ('开始的全场满10-1x', 'CX201906050001x',0, 0, now(),  DATE_ADD(now(),INTERVAL 7 DAY), 10, 1, 8, 0, 2, now(), now());
SET @iPromotionID = last_insert_id();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'蜘蛛侠1号aaa','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',2/*F_Type*/);
SET @iCommodityID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @name = '满10-1';
SET @status = 0;
SET @type = 0;
SET @datetimestart = now();
SET @datetimeend = now();
SET @excecutionthreshold = 10;
SET @excecutionamount = 1;
SET @excecutiondiscount = 1;
SET @scope = 0;
SET @staff = 1 ;
SET @iErrorCode = 0;
CALL SP_PromotionScope_Create(@iErrorCode, @sErrorMsg, @iPromotionID, @iCommodityID);
-- 
SELECT @sErrorMsg;
SELECT IF( @iErrorCode = 7, '测试成功', '测试失败') AS 'Case8 Testing Result';
-- 
DELETE FROM t_promotion WHERE F_ID = @iPromotionID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;


SELECT '------------------- Case9:创建一个促销范围,商品类型为组合商品 ------------------' as 'Case 9';
INSERT INTO t_promotion (F_Name, F_SN, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES ('开始的全场满10-1x', 'CX201906050001x',0, 0, now(),  DATE_ADD(now(),INTERVAL 7 DAY), 10, 1, 8, 0, 2, now(), now());
SET @iPromotionID = last_insert_id();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'蜘蛛侠1号aaa','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',1/*F_Type*/);
SET @iCommodityID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @name = '满10-1';
SET @status = 0;
SET @type = 0;
SET @datetimestart = now();
SET @datetimeend = now();
SET @excecutionthreshold = 10;
SET @excecutionamount = 1;
SET @excecutiondiscount = 1;
SET @scope = 0;
SET @staff = 1 ;
SET @iErrorCode = 0;
CALL SP_PromotionScope_Create(@iErrorCode, @sErrorMsg, @iPromotionID, @iCommodityID);
-- 
SELECT @sErrorMsg;
SELECT IF( @iErrorCode = 7, '测试成功', '测试失败') AS 'Case9 Testing Result';
-- 
DELETE FROM t_promotion WHERE F_ID = @iPromotionID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '------------------- Case10:创建一个促销,其指定商品中包含重复的商品 ------------------' as 'Case 10';
INSERT INTO t_promotion (F_Name, F_SN, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES ('开始的全场满10-1x', 'CX201906050001x',0, 0, now(),  DATE_ADD(now(),INTERVAL 7 DAY), 10, 1, 8, 0, 2, now(), now());
SET @iPromotionID = last_insert_id();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'蜘蛛侠1号aaa','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',0/*F_Type*/);
SET @iCommodityID = last_insert_id();
-- 
INSERT INTO t_promotionscope (F_PromotionID, F_CommodityID, F_CommodityName)
VALUES (@iPromotionID, @iCommodityID, '蜘蛛侠1号aaa');
SET @iPromotionscopeID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @name = '满10-1';
SET @status = 0;
SET @type = 0;
SET @datetimestart = now();
SET @datetimeend = now();
SET @excecutionthreshold = 10;
SET @excecutionamount = 1;
SET @excecutiondiscount = 1;
SET @scope = 0;
SET @staff = 1 ;
SET @iErrorCode = 0;
CALL SP_PromotionScope_Create(@iErrorCode, @sErrorMsg, @iPromotionID, @iCommodityID);
-- 
SELECT @sErrorMsg;
SELECT IF( @iErrorCode = 1, '测试成功', '测试失败') AS 'Case10 Testing Result';
-- 
DELETE FROM t_promotionscope WHERE F_ID = @iPromotionscopeID; 
DELETE FROM t_promotion WHERE F_ID = @iPromotionID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;