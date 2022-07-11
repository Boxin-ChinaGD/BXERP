SELECT '++++++++++++++++++ Test_SP_RetailTradeCoupon_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:正常添加 -------------------------' AS 'Case1';
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_SmallSheetID, F_ShopID) VALUES (1 /* F_VipID */,'LS2017080601010100011234'/*F_SN*/,158/*F_LocalSN*/,1/*F_POS_ID*/,'url=ashasoadigmnalskd'/*F_Logo*/,'2017-08-06'/*F_SaleDatetime*/,
							2/*F_StaffID*/,4/*F_PaymentType*/,0/*F_PaymentAccount*/,1/*F_Status*/,'........'/*F_Remark*/,now()/*F_SyncDatetime*/,150000/*F_Amount*/,0/*F_AmountCash*/,0/*F_AmountAlipay*/,150000/*F_AmountWeChat*/,2/*F_SmallSheetID*/,2);
SET @iRetailTradeID = last_insert_id();
-- 
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0 /* F_Status */, 1/* F_CardType */, 0/*F_Bonus*/, 20/* F_LeastAmount */, 20/*F_ReduceAmount*/, 1/*F_Discount*/, '1'/*F_Title*/, '111'/*F_Color*/, '110'/*F_Description*/, 1/*F_PersonalLimit*/, 1/*F_Type*/, '1'/*F_BeginTime*/, 1/*F_EndTime*/, 
		now()/*F_BeginDateTime*/, now()/* F_EndDateTime*/, 1/*F_Quantity*/, 1/*F_RemainingQuantity*/, 1/*F_Scope*/);
SET @iCouponID = LAST_INSERT_ID();
-- 创建一张优惠券
INSERT INTO t_couponcode (F_VipID, F_CouponID, F_Status, F_SN) VALUES (1, @iCouponID, 1, '111');
SET @iCouponCodeID = last_insert_id();


SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SP_RetailTradeCoupon_Create(@iErrorCode, @sErrorMsg, @iRetailTradeID, @iCouponCodeID);
-- 
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', 's测试失败') AS 'Case1 Testing Result';
SELECT IF((SELECT F_VIPID FROM t_couponcode WHERE F_ID = @iCouponCodeID) = (SELECT F_VIPID FROM t_retailtrade WHERE F_ID = @iRetailTradeID), '优惠券VIPID跟零售单的VIPID一样,测试成功', '优惠券VIPID跟零售单的VIPID不一样,测试失败') AS 'Case1 Testing Result';
-- 
DELETE FROM t_retailtradecoupon WHERE F_ID = last_insert_id();
DELETE FROM t_couponcode WHERE F_ID = @iCouponCodeID;
DELETE FROM t_coupon WHERE F_ID = @iCouponID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;

SELECT '-------------------- Case2:零售单ID不存在 -------------------------' AS 'Case2';
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0 /* F_Status */, 1/* F_CardType */, 0/*F_Bonus*/, 20/* F_LeastAmount */, 20/*F_ReduceAmount*/, 1/*F_Discount*/, '1'/*F_Title*/, '111'/*F_Color*/, '110'/*F_Description*/, 1/*F_PersonalLimit*/, 1/*F_Type*/, '1'/*F_BeginTime*/, 1/*F_EndTime*/, 
		now()/*F_BeginDateTime*/, now()/* F_EndDateTime*/, 1/*F_Quantity*/, 1/*F_RemainingQuantity*/, 1/*F_Scope*/);
SET @iCouponID = LAST_INSERT_ID();
-- 创建一张优惠券
INSERT INTO t_couponcode (F_VipID, F_CouponID, F_Status, F_SN) VALUES (1, @iCouponID, 1, '111');
SET @iCouponCodeID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iRetailTradeID = -1;
-- 
CALL SP_RetailTradeCoupon_Create(@iErrorCode, @sErrorMsg, @iRetailTradeID, @iCouponCodeID);
-- 
SELECT IF(found_rows() = 0 AND @iErrorCode = 2, '测试成功', 's测试失败') AS 'Case2 Testing Result';
-- 
DELETE FROM t_retailtradecoupon WHERE F_ID = last_insert_id();
DELETE FROM t_couponcode WHERE F_ID = @iCouponCodeID;
DELETE FROM t_coupon WHERE F_ID = @iCouponID;

SELECT '-------------------- Case3:优惠券ID不存在 -------------------------' AS 'Case3';
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_SmallSheetID, F_ShopID) VALUES (1 /* F_VipID */,'LS2017080601010100011234'/*F_SN*/,158/*F_LocalSN*/,1/*F_POS_ID*/,'url=ashasoadigmnalskd'/*F_Logo*/,'2017-08-06'/*F_SaleDatetime*/,
							2/*F_StaffID*/,4/*F_PaymentType*/,0/*F_PaymentAccount*/,1/*F_Status*/,'........'/*F_Remark*/,now()/*F_SyncDatetime*/,150000/*F_Amount*/,0/*F_AmountCash*/,0/*F_AmountAlipay*/,150000/*F_AmountWeChat*/,2/*F_SmallSheetID*/,2);
SET @iRetailTradeID = last_insert_id();
-- 
SET @iCouponCodeID = -1;
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SP_RetailTradeCoupon_Create(@iErrorCode, @sErrorMsg, @iRetailTradeID, @iCouponCodeID);
-- 
SELECT IF(found_rows() = 0 AND @iErrorCode = 2, '测试成功', 's测试失败') AS 'Case3 Testing Result';
-- 
DELETE FROM t_retailtradecoupon WHERE F_ID = last_insert_id();
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;

SELECT '-------------------- Case4:优惠券的VIPID和零售单的VIPID不一样 -------------------------' AS 'Case4';
INSERT INTO T_RetailTrade (F_VipID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Status,F_Remark,F_SyncDatetime,F_Amount,F_AmountCash,F_AmountAlipay,F_AmountWeChat,F_SmallSheetID, F_ShopID) VALUES (2 /* F_VipID */,'LS2017080601010100011234'/*F_SN*/,158/*F_LocalSN*/,1/*F_POS_ID*/,'url=ashasoadigmnalskd'/*F_Logo*/,'2017-08-06'/*F_SaleDatetime*/,
							2/*F_StaffID*/,4/*F_PaymentType*/,0/*F_PaymentAccount*/,1/*F_Status*/,'........'/*F_Remark*/,now()/*F_SyncDatetime*/,150000/*F_Amount*/,0/*F_AmountCash*/,0/*F_AmountAlipay*/,150000/*F_AmountWeChat*/,2/*F_SmallSheetID*/,2);
SET @iRetailTradeID = last_insert_id();
-- 
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0 /* F_Status */, 1/* F_CardType */, 0/*F_Bonus*/, 20/* F_LeastAmount */, 20/*F_ReduceAmount*/, 1/*F_Discount*/, '1'/*F_Title*/, '111'/*F_Color*/, '110'/*F_Description*/, 1/*F_PersonalLimit*/, 1/*F_Type*/, '1'/*F_BeginTime*/, 1/*F_EndTime*/, 
		now()/*F_BeginDateTime*/, now()/* F_EndDateTime*/, 1/*F_Quantity*/, 1/*F_RemainingQuantity*/, 1/*F_Scope*/);
SET @iCouponID = LAST_INSERT_ID();
-- 创建一张优惠券
INSERT INTO t_couponcode (F_VipID, F_CouponID, F_Status, F_SN) VALUES (1, @iCouponID, 1, '111');
SET @iCouponCodeID = last_insert_id();


SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SP_RetailTradeCoupon_Create(@iErrorCode, @sErrorMsg, @iRetailTradeID, @iCouponCodeID);
-- 
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'Case4 Testing Result';
SELECT IF((SELECT F_VIPID FROM t_couponcode WHERE F_ID = @iCouponCodeID) <> (SELECT F_VIPID FROM t_retailtrade WHERE F_ID = @iRetailTradeID), '优惠券VIPID跟零售单的VIPID不一样,测试成功', '优惠券VIPID跟零售单的VIPID一样,测试失败') AS 'Case4 Testing Result';
-- 
DELETE FROM t_retailtradecoupon WHERE F_ID = last_insert_id();
DELETE FROM t_couponcode WHERE F_ID = @iCouponCodeID;
DELETE FROM t_coupon WHERE F_ID = @iCouponID;
DELETE FROM t_retailtrade WHERE F_ID = @iRetailTradeID;