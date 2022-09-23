SELECT '++++++++++++++++++ Test_SP_CouponCode_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:正常添加 -------------------------' AS 'Case1';

SET @iQuantity = 1000;
SET @iRemainingQuantity = 1000;
-- 
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, 
F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0, 0, 0, 0, 10, 1, '现金券', 'xxxxxxxx', '使用说明', 1, 0, '00:00:00', '23:59:59', '1970/01/01', '2030/01/01', @iQuantity, @iRemainingQuantity, 0);
SET @iCouponID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iVipID = 1;
SET @iCouponID = @iCouponID;
SET @iStatus = 0;
-- 
CALL SP_CouponCode_Create(@iErrorCode, @sErrorMsg, @iVipID, @iCouponID, @iStatus);
-- 
SELECT @sErrorMsg;
SELECT 1 FROM t_couponcode WHERE F_VipID = @iVipID AND F_CouponID = @iCouponID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';
-- 
SELECT IF((SELECT F_RemainingQuantity FROM t_coupon WHERE F_ID = @iCouponID) = @iQuantity - 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';
-- 
DELETE FROM t_couponcode WHERE F_ID = LAST_INSERT_ID();
DELETE FROM t_coupon WHERE F_ID = @iCouponID;

SELECT '-------------------- Case2:会员ID不存在 -------------------------' AS 'Case2';
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, 
F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0, 0, 0, 0, 10, 1, '现金券', 'xxxxxxxx', '使用说明', 1, 0, '00:00:00', '23:59:59', '1970/01/01', '2030/01/01', 1000, 1000, 0);
SET @iCouponID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iVipID = -1;
SET @iCouponID = @iCouponID;
SET @iStatus = 0;
-- 
CALL SP_CouponCode_Create(@iErrorCode, @sErrorMsg, @iVipID, @iCouponID, @iStatus);
-- 
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 2 AND @sErrorMsg = '该会员不存在', '测试成功', '测试失败') AS 'Case2 Testing Result';
-- 
DELETE FROM t_coupon WHERE F_ID = @iCouponID;

SELECT '-------------------- Case3:优惠券ID不存在 -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iVipID = 1;
SET @iCouponID = -1;
SET @iStatus = 0;
-- 
CALL SP_CouponCode_Create(@iErrorCode, @sErrorMsg, @iVipID, @iCouponID, @iStatus);
-- 
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 2 AND @sErrorMsg = '该优惠券不存在', '测试成功', '测试失败') AS 'Case3 Testing Result';

SELECT '-------------------- Case4:优惠券库存不足 -------------------------' AS 'Case4';
SET @iQuantity = 1000;
SET @iRemainingQuantity = 0;
-- 
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, 
F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0, 0, 0, 0, 10, 1, '现金券', 'xxxxxxxx', '使用说明', 1, 0, '00:00:00', '23:59:59', '1970/01/01', '2030/01/01', @iQuantity, @iRemainingQuantity, 0);
SET @iCouponID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iVipID = 1;
SET @iCouponID = @iCouponID;
SET @iStatus = 0;
-- 
CALL SP_CouponCode_Create(@iErrorCode, @sErrorMsg, @iVipID, @iCouponID, @iStatus);
-- 
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '优惠券库存不足,无法领取', '测试成功', '测试失败') AS 'Case4 Testing Result';
-- 
DELETE FROM t_couponcode WHERE F_ID = LAST_INSERT_ID();
DELETE FROM t_coupon WHERE F_ID = @iCouponID;


SELECT '-------------------- Case5:优惠券已经被删除，不能再被领取 -------------------------' AS 'Case5';
-- 
SET @iQuantity = 1000;
SET @iRemainingQuantity = 1000;
--
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, 
F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (1, 0, 0, 0, 10, 1, '现金券', 'xxxxxxxx', '使用说明', 1, 0, '00:00:00', '23:59:59', '1970/01/01', '2030/01/01', @iQuantity, @iRemainingQuantity, 0);
SET @iCouponID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iVipID = 1;
SET @iCouponID = @iCouponID;
SET @iStatus = 0;
-- 
CALL SP_CouponCode_Create(@iErrorCode, @sErrorMsg, @iVipID, @iCouponID, @iStatus);
-- 
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 2 AND @sErrorMsg = '该优惠券不存在', '测试成功', '测试失败') AS 'Case5 Testing Result';
DELETE FROM t_coupon WHERE F_ID = @iCouponID;

SELECT '-------------------- Case6:会员已超过领取该优惠券的个人数目上限，无法领取 -------------------------' AS 'Case1';

SET @iQuantity = 1000;
SET @iRemainingQuantity = 1000;
-- 
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, 
F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0, 0, 0, 0, 10, 1, '现金券', 'xxxxxxxx', '使用说明', 1, 0, '00:00:00', '23:59:59', '1970/01/01', '2030/01/01', @iQuantity, @iRemainingQuantity, 0);
SET @iCouponID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iVipID = 1;
SET @iCouponID = @iCouponID;
SET @iStatus = 0;
-- 
CALL SP_CouponCode_Create(@iErrorCode, @sErrorMsg, @iVipID, @iCouponID, @iStatus);
-- 
CALL SP_CouponCode_Create(@iErrorCode, @sErrorMsg, @iVipID, @iCouponID, @iStatus);
SELECT @sErrorMsg;
SELECT 1 FROM t_couponcode WHERE F_VipID = @iVipID AND F_CouponID = @iCouponID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7 AND @sErrorMsg = '会员已超过领取该优惠券的个人数目上限，无法领取', '测试成功', '测试失败') AS 'Case1 Testing Result';
-- 
SELECT IF((SELECT F_RemainingQuantity FROM t_coupon WHERE F_ID = @iCouponID) = @iQuantity - 1 , '测试成功', '测试失败') AS 'Case1 Testing Result';
-- 
DELETE FROM t_couponcode WHERE F_ID = LAST_INSERT_ID();
DELETE FROM t_coupon WHERE F_ID = @iCouponID;

SELECT '-------------------- Case7:使用积分领取优惠券 -------------------------' AS 'Case1';
SET @iVipBonus = 50;
INSERT INTO T_VIP (F_ICID,F_CardID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile, F_CreateDatetime)
VALUES ('1232456',1,'giggs','1232456@bx.vip',5,99.8,'广州',1,
'2017-08-06',@iVipBonus,'2017-08-08 23:59:10','12345612312', now());
SET @iVipID = last_insert_id();
-- 
SET @iQuantity = 1000;
SET @iRemainingQuantity = 1000;
SET @iCouponBonus = 20;
-- 
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, 
F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0, 0, @iCouponBonus, 0, 10, 1, '现金券', 'xxxxxxxx', '使用说明', 1, 0, '00:00:00', '23:59:59', '1970/01/01', '2030/01/01', @iQuantity, @iRemainingQuantity, 0);
SET @iCouponID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCouponID = @iCouponID;
SET @iStatus = 0;
-- 
CALL SP_CouponCode_Create(@iErrorCode, @sErrorMsg, @iVipID, @iCouponID, @iStatus);
-- 
SELECT @sErrorMsg;
SELECT 1 FROM t_couponcode WHERE F_VipID = @iVipID AND F_CouponID = @iCouponID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';
-- 
SELECT IF((SELECT F_RemainingQuantity FROM t_coupon WHERE F_ID = @iCouponID) = @iQuantity - 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';
-- 
SELECT IF((SELECT F_Bonus FROM t_Vip WHERE F_ID = @iVipID) = @iVipBonus - @iCouponBonus AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';
-- 
DELETE FROM t_couponcode WHERE F_ID = LAST_INSERT_ID();
DELETE FROM t_coupon WHERE F_ID = @iCouponID;
DELETE FROM t_bonusconsumehistory WHERE F_VipID = @iVipID;
DELETE FROM t_vip WHERE F_ID = @iVipID;

SELECT '-------------------- Case8:会员积分不足,无法领取该优惠券 -------------------------' AS 'Case1';
SET @iVipBonus = 0;
INSERT INTO T_VIP (F_ICID,F_CardID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile, F_CreateDatetime)
VALUES ('1232456',1,'giggs','1232456@bx.vip',5,99.8,'广州',1,
'2017-08-06',@iVipBonus,'2017-08-08 23:59:10','12345612312', now());
SET @iVipID = last_insert_id();
-- 
SET @iQuantity = 1000;
SET @iRemainingQuantity = 1000;
SET @iCouponBonus = 20;
-- 
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, 
F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0, 0, @iCouponBonus, 0, 10, 1, '现金券', 'xxxxxxxx', '使用说明', 1, 0, '00:00:00', '23:59:59', '1970/01/01', '2030/01/01', @iQuantity, @iRemainingQuantity, 0);
SET @iCouponID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCouponID = @iCouponID;
SET @iStatus = 0;
-- 
CALL SP_CouponCode_Create(@iErrorCode, @sErrorMsg, @iVipID, @iCouponID, @iStatus);
-- 
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '会员积分不足,无法领取该优惠券', '测试成功', '测试失败') AS 'Case1 Testing Result';
-- 
DELETE FROM t_vip WHERE F_ID = @iVipID;

