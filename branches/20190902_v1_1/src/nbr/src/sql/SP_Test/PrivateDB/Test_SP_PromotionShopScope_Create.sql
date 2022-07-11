SELECT '++++++++++++++++++ Test_SP_PromotionShopScope_Create.sql ++++++++++++++++++++';
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
SET @shopScope = 1;
SET @staff = 1 ;
INSERT INTO t_promotion (F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime, F_ShopScope)
VALUES (@name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @staff, now(), now(), @shopScope);
SET @iID = last_insert_id();

SET @iErrorCode = 0;
SET @iPromotionID = @iID;
SET @iShopID = 2;
CALL SP_PromotionShopScope_Create(@iErrorCode, @sErrorMsg, @iPromotionID, @iShopID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PromotionShopScope WHERE F_ID = last_insert_id();
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

DELETE FROM t_promotionShopScope WHERE F_ID = LAST_INSERT_ID();
DELETE FROM t_promotion WHERE F_ID = @iID;

SELECT '------------------- Case2 用不存在的ShopID创建 ------------------' as 'Case 2';
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
SET @shopScope = 1;
SET @staff = 1 ;
INSERT INTO t_promotion (F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime, F_ShopScope)
VALUES (@name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @staff, now(), now(), @shopScope);
SET @iID = last_insert_id();

SET @iErrorCode = 0;
SET @iPromotionID = @iID;
SET @iShopID = -999;
CALL SP_PromotionShopScope_Create(@iErrorCode, @sErrorMsg, @iPromotionID, @iShopID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PromotionShopScope WHERE F_PromotionID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '不能使用不存在的shopID进行创建', '测试成功', '测试失败') AS 'Case2 Testing Result';

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
SET @iShopID = 1;
CALL SP_PromotionShopScope_Create(@iErrorCode, @sErrorMsg, @iPromotionID, @iShopID);
-- 
SELECT @sErrorMsg;
SELECT IF( @iErrorCode = 7, '测试成功', '测试失败') AS 'Case3 Testing Result';


SELECT '------------------- Case4:创建一个促销范围,主表正常 ------------------' as 'Case 4';
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
SET @shopScope = 1;
SET @staff = 1 ;
INSERT INTO t_promotion (F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime, F_ShopScope)
VALUES (@name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @staff, now(), now(), @shopScope);
SET @iID = last_insert_id();

SET @iErrorCode = 0;
SET @iPromotionID = @iID;
SET @iShopID = 1;
CALL SP_PromotionShopScope_Create(@iErrorCode, @sErrorMsg, @iPromotionID, @iShopID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PromotionShopScope WHERE F_ID = last_insert_id();
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case4 Testing Result';

DELETE FROM t_promotionShopScope WHERE F_ID = LAST_INSERT_ID();
DELETE FROM t_promotion WHERE F_ID = @iID;

SELECT '------------------- Case5:创建一个促销范围，主表被删除 ------------------' as 'Case 5';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @name = '满10-2';
SET @status = 1;
SET @type = 1;
SET @datetimestart = now();
SET @datetimeend = now();
SET @excecutionthreshold = 10;
SET @excecutionamount = 1;
SET @excecutiondiscount = 1;
SET @scope = 0;
SET @shopScope = 1;
SET @staff = 1 ;
INSERT INTO t_promotion (F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime, F_ShopScope)
VALUES (@name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @staff, now(), now(), @shopScope);
SET @iID = last_insert_id();

SET @iErrorCode = 0;
SET @iPromotionID = @iID;
SET @iShopID = 2;
CALL SP_PromotionShopScope_Create(@iErrorCode, @sErrorMsg, @iPromotionID, @iShopID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PromotionShopScope WHERE F_ID = last_insert_id();
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'Case5 Testing Result';
DELETE FROM t_promotionShopScope WHERE F_ID = LAST_INSERT_ID();
DELETE FROM t_promotion WHERE F_ID = @iID;


SELECT '------------------- Case6:创建一个促销范围,主表不存在 ------------------' as 'Case 6';
SET @iErrorCode = 0;
SET @iPromotionID = 99999;
SET @iShopID = 1;
CALL SP_PromotionShopScope_Create(@iErrorCode, @sErrorMsg, @iPromotionID, @iShopID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'Case6 Testing Result';



SELECT '------------------- Case7:创建一个促销,其指定商品中包含重复的门店 ------------------' as 'Case 10';
INSERT INTO t_promotion (F_Name, F_SN, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES ('开始的全场满10-1x', 'CX201906050001x',0, 0, now(),  DATE_ADD(now(),INTERVAL 7 DAY), 10, 1, 8, 0, 2, now(), now());
SET @iPromotionID = last_insert_id();
SET @iShopID = 2;
-- 
INSERT INTO t_promotionShopScope (F_PromotionID, F_ShopID)
VALUES (@iPromotionID, @iShopID);
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
SET @shopScope = 1;
SET @staff = 1 ;
SET @iErrorCode = 0;
CALL SP_PromotionShopScope_Create(@iErrorCode, @sErrorMsg, @iPromotionID, @iShopID);
-- 
SELECT @sErrorMsg;
SELECT IF( @iErrorCode = 1, '测试成功', '测试失败') AS 'Case7 Testing Result';
-- 
DELETE FROM t_promotionShopScope WHERE F_ID = @iPromotionscopeID; 
DELETE FROM t_promotion WHERE F_ID = @iPromotionID;
