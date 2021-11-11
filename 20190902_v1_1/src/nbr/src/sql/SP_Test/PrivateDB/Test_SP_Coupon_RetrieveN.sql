SELECT '++++++++++++++++++ Test_SP_Coupon_RetrieveN.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:web查询所有 -------------------------' AS 'Case1';
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, 
F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0, 0, 0, 0, 10, 1, '现金券', 'xxxxxxxx', '使用说明', 1, 0, '00:00:00', '23:59:59', '1970/01/01', '2030/01/01', 1000, 1000, 0);
SET @iID = last_insert_id();

SET @iPosID = -1;
SET @iBonus = -1;
SET @iType = -1;
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord1 = 0;

CALL SP_Coupon_RetrieveN(@iErrorCode, @sErrorMsg, @iPosID, @iBonus, @iType, @iPageIndex, @iPageSize, @iTotalRecord1);
SELECT @iTotalRecord1;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

DELETE FROM t_coupon WHERE F_ID = @iID;

SELECT '-------------------- Case2:pos查询所有 -------------------------' AS 'Case2';
SET @iPosID = 1;
SET @iBonus = -1;
SET @iType = -1;
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 1000000;
SET @iTotalRecord2 = 0;

CALL SP_Coupon_RetrieveN(@iErrorCode, @sErrorMsg, @iPosID, @iBonus, @iType, @iPageIndex, @iPageSize, @iTotalRecord2);
SELECT @iTotalRecord2;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';

SELECT '-------------------- Csse1 && Case2共同的结果验证 -------------------------' AS 'Case1 & Case2共同的结果验证';
SELECT IF(@iTotalRecord1 >= @iTotalRecord2, '测试成功', '测试失败') AS 'Case1 && Case2 Testing Result';



SELECT '-------------------- Case3:传iPosID=-2,bonus=-1, F_Type = -1,代表小程序请求，查询出所有优惠券(包括未开始的，不包括过期、和已删除的) -------------------------' AS 'Case3';
-- 
SET @iPosID = -2;
SET @iBonus = -1;
SET @iType = -1;
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 1000000;
SET @iTotalRecord = 0;

CALL SP_Coupon_RetrieveN(@iErrorCode, @sErrorMsg, @iPosID, @iBonus, @iType, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case3 Testing Result';





SELECT '-------------------- Case4:传iPosID=-2,bonus=-1, F_Type = 0,代表小程序请求，查询出所有现金券(包括未开始的，不包括过期、和已删除的) -------------------------' AS 'Case4';
-- 
SET @iPosID = -2;
SET @iBonus = -1;
SET @iType = 0;
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 1000000;
SET @iOldTotalRecord = 0;

CALL SP_Coupon_RetrieveN(@iErrorCode, @sErrorMsg, @iPosID, @iBonus, @iType, @iPageIndex, @iPageSize, @iOldTotalRecord);
SELECT @iOldTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case4 Testing Result';
-- 新建两张现金券、1张折扣券用于测试
-- 现金券1
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0/*F_Status*/, 0/*F_Type*/, 0/*F_Bonus*/, 1.00/*F_LeastAmount*/, 4.00/*F_ReduceAmount*/, 1/*F_Discount*/, '已起用的优惠券'/*F_Title*/, 'Color010'/*F_Color*/, ''/*F_Description*/, 1/*F_PersonalLimit*/, 0/*F_WeekDayAvailable*/, '9:00:00'/*F_BeginTime*/, '12:00:00'/*F_EndTime*/, '1998/03/03 15:15:15'/*F_BeginDateTime*/, 
		'3333/03/20 15:15:15'/*F_EndDateTime*/, 1000/*F_Quantity*/, 1000/*F_RemainingQuantity*/, 0/*F_Scope*/);
SET @couponID1 = last_insert_id();
-- 现金券2
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0/*F_Status*/, 0/*F_Type*/, 0/*F_Bonus*/, 1.00/*F_LeastAmount*/, 4.00/*F_ReduceAmount*/, 1/*F_Discount*/, '已起用的优惠券'/*F_Title*/, 'Color010'/*F_Color*/, ''/*F_Description*/, 1/*F_PersonalLimit*/, 0/*F_WeekDayAvailable*/, '9:00:00'/*F_BeginTime*/, '12:00:00'/*F_EndTime*/, '1998/03/03 15:15:15'/*F_BeginDateTime*/, 
		'3333/03/20 15:15:15'/*F_EndDateTime*/, 1000/*F_Quantity*/, 1000/*F_RemainingQuantity*/, 0/*F_Scope*/);
SET @couponID2 = last_insert_id();
-- 折扣券
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0/*F_Status*/, 1/*F_Type*/, 0/*F_Bonus*/, 10.00/*F_LeastAmount*/, 0.00/*F_ReduceAmount*/, 0.9/*F_Discount*/, '九折优惠券'/*F_Title*/, 'Color010'/*F_Color*/, ''/*F_Description*/, 1/*F_PersonalLimit*/, 0/*F_WeekDayAvailable*/, '9:00:00'/*F_BeginTime*/, '12:00:00'/*F_EndTime*/, '1998/03/03 15:15:15'/*F_BeginDateTime*/, 
		'3333/03/20 15:15:15'/*F_EndDateTime*/, 1000/*F_Quantity*/, 1000/*F_RemainingQuantity*/, 0/*F_Scope*/);
SET @couponID3 = last_insert_id();
-- 
SET @iPosID = -2;
SET @iBonus = -1;
SET @iType = 0;
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 1000000;
SET @iTotalRecord = 0;

CALL SP_Coupon_RetrieveN(@iErrorCode, @sErrorMsg, @iPosID, @iBonus, @iType, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = @iOldTotalRecord + 2, '测试成功', '测试失败') AS 'Case4 Testing Result';
-- 
DELETE FROM t_coupon WHERE F_ID = @couponID1;
DELETE FROM t_coupon WHERE F_ID = @couponID2;
DELETE FROM t_coupon WHERE F_ID = @couponID3;





SELECT '-------------------- Case5:传iPosID=-2,bonus=-1, F_Type = 1,代表小程序请求，查询出所有折扣券(包括未开始的，不包括过期、和已删除的) -------------------------' AS 'Case5';
-- 
SET @iPosID = -2;
SET @iBonus = -1;
SET @iType = 1;
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 1000000;
SET @iOldTotalRecord = 0;

CALL SP_Coupon_RetrieveN(@iErrorCode, @sErrorMsg, @iPosID, @iBonus, @iType, @iPageIndex, @iPageSize, @iOldTotalRecord);
SELECT @iOldTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case5 Testing Result';
-- 新建1张现金券、2张折扣券用于测试
-- 现金券1
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0/*F_Status*/, 0/*F_Type*/, 0/*F_Bonus*/, 1.00/*F_LeastAmount*/, 4.00/*F_ReduceAmount*/, 1/*F_Discount*/, '已起用的优惠券'/*F_Title*/, 'Color010'/*F_Color*/, ''/*F_Description*/, 1/*F_PersonalLimit*/, 0/*F_WeekDayAvailable*/, '9:00:00'/*F_BeginTime*/, '12:00:00'/*F_EndTime*/, '1998/03/03 15:15:15'/*F_BeginDateTime*/, 
		'3333/03/20 15:15:15'/*F_EndDateTime*/, 1000/*F_Quantity*/, 1000/*F_RemainingQuantity*/, 0/*F_Scope*/);
SET @couponID1 = last_insert_id();
-- 折扣券1
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0/*F_Status*/, 1/*F_Type*/, 0/*F_Bonus*/, 10.00/*F_LeastAmount*/, 0.00/*F_ReduceAmount*/, 0.9/*F_Discount*/, '九折优惠券'/*F_Title*/, 'Color010'/*F_Color*/, ''/*F_Description*/, 1/*F_PersonalLimit*/, 0/*F_WeekDayAvailable*/, '9:00:00'/*F_BeginTime*/, '12:00:00'/*F_EndTime*/, '1998/03/03 15:15:15'/*F_BeginDateTime*/, 
		'3333/03/20 15:15:15'/*F_EndDateTime*/, 1000/*F_Quantity*/, 1000/*F_RemainingQuantity*/, 0/*F_Scope*/);
SET @couponID2 = last_insert_id();
-- 折扣券2
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0/*F_Status*/, 1/*F_Type*/, 0/*F_Bonus*/, 10.00/*F_LeastAmount*/, 0.00/*F_ReduceAmount*/, 0.9/*F_Discount*/, '九折优惠券'/*F_Title*/, 'Color010'/*F_Color*/, ''/*F_Description*/, 1/*F_PersonalLimit*/, 0/*F_WeekDayAvailable*/, '9:00:00'/*F_BeginTime*/, '12:00:00'/*F_EndTime*/, '1998/03/03 15:15:15'/*F_BeginDateTime*/, 
		'3333/03/20 15:15:15'/*F_EndDateTime*/, 1000/*F_Quantity*/, 1000/*F_RemainingQuantity*/, 0/*F_Scope*/);
SET @couponID3 = last_insert_id();
-- 
SET @iPosID = -2;
SET @iBonus = -1;
SET @iType = 1;
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 1000000;
SET @iTotalRecord = 0;

CALL SP_Coupon_RetrieveN(@iErrorCode, @sErrorMsg, @iPosID, @iBonus, @iType, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = @iOldTotalRecord + 2, '测试成功', '测试失败') AS 'Case5 Testing Result';
-- 
DELETE FROM t_coupon WHERE F_ID = @couponID1;
DELETE FROM t_coupon WHERE F_ID = @couponID2;
DELETE FROM t_coupon WHERE F_ID = @couponID3;



SELECT '-------------------- Case6:传iPosID=-2,bonus=0, F_Type = -1,代表小程序请求，查询出所有积分为0的优惠券(包括未开始的，不包括过期、和已删除的) -------------------------' AS 'Case6';
-- 
SET @iPosID = -2;
SET @iBonus = 0;
SET @iType = -1;
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 1000000;
SET @iOldTotalRecord = 0;

CALL SP_Coupon_RetrieveN(@iErrorCode, @sErrorMsg, @iPosID, @iBonus, @iType, @iPageIndex, @iPageSize, @iOldTotalRecord);
SELECT @iOldTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case6 Testing Result';
-- 
-- 新建两张积分为0的优惠券、1张积分大于0的优惠券用于测试
-- 优惠券1
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0/*F_Status*/, 0/*F_Type*/, 0/*F_Bonus*/, 1.00/*F_LeastAmount*/, 4.00/*F_ReduceAmount*/, 1/*F_Discount*/, '已起用的优惠券'/*F_Title*/, 'Color010'/*F_Color*/, ''/*F_Description*/, 1/*F_PersonalLimit*/, 0/*F_WeekDayAvailable*/, '9:00:00'/*F_BeginTime*/, '12:00:00'/*F_EndTime*/, '1998/03/03 15:15:15'/*F_BeginDateTime*/, 
		'3333/03/20 15:15:15'/*F_EndDateTime*/, 1000/*F_Quantity*/, 1000/*F_RemainingQuantity*/, 0/*F_Scope*/);
SET @couponID1 = last_insert_id();
-- 优惠券2
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0/*F_Status*/, 0/*F_Type*/, 0/*F_Bonus*/, 1.00/*F_LeastAmount*/, 4.00/*F_ReduceAmount*/, 1/*F_Discount*/, '已起用的优惠券'/*F_Title*/, 'Color010'/*F_Color*/, ''/*F_Description*/, 1/*F_PersonalLimit*/, 0/*F_WeekDayAvailable*/, '9:00:00'/*F_BeginTime*/, '12:00:00'/*F_EndTime*/, '1998/03/03 15:15:15'/*F_BeginDateTime*/, 
		'3333/03/20 15:15:15'/*F_EndDateTime*/, 1000/*F_Quantity*/, 1000/*F_RemainingQuantity*/, 0/*F_Scope*/);
SET @couponID2 = last_insert_id();
-- 优惠券3
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0/*F_Status*/, 1/*F_Type*/, 1/*F_Bonus*/, 10.00/*F_LeastAmount*/, 0.00/*F_ReduceAmount*/, 0.9/*F_Discount*/, '九折优惠券'/*F_Title*/, 'Color010'/*F_Color*/, ''/*F_Description*/, 1/*F_PersonalLimit*/, 0/*F_WeekDayAvailable*/, '9:00:00'/*F_BeginTime*/, '12:00:00'/*F_EndTime*/, '1998/03/03 15:15:15'/*F_BeginDateTime*/, 
		'3333/03/20 15:15:15'/*F_EndDateTime*/, 1000/*F_Quantity*/, 1000/*F_RemainingQuantity*/, 0/*F_Scope*/);
SET @couponID3 = last_insert_id();
-- 
SET @iPosID = -2;
SET @iBonus = 0;
SET @iType = -1;
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 1000000;
SET @iTotalRecord = 0;
-- 
CALL SP_Coupon_RetrieveN(@iErrorCode, @sErrorMsg, @iPosID, @iBonus, @iType, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = @iOldTotalRecord + 2, '测试成功', '测试失败') AS 'Case6 Testing Result';
-- 
DELETE FROM t_coupon WHERE F_ID = @couponID1;
DELETE FROM t_coupon WHERE F_ID = @couponID2;
DELETE FROM t_coupon WHERE F_ID = @couponID3;



SELECT '-------------------- Case7:传iPosID=-2,bonus大于0, F_Type = -1,代表小程序请求，查询出所有积分大于0的优惠券(包括未开始的，不包括过期、和已删除的) -------------------------' AS 'Case7';
-- 
SET @iPosID = -2;
SET @iBonus = 1;
SET @iType = -1;
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 1000000;
SET @iOldTotalRecord = 0;

CALL SP_Coupon_RetrieveN(@iErrorCode, @sErrorMsg, @iPosID, @iBonus, @iType, @iPageIndex, @iPageSize, @iOldTotalRecord);
SELECT @iOldTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case7 Testing Result';
-- 
-- 新建1张积分为0的优惠券、2张积分大于0的优惠券用于测试
-- 优惠券1
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0/*F_Status*/, 0/*F_Type*/, 0/*F_Bonus*/, 1.00/*F_LeastAmount*/, 4.00/*F_ReduceAmount*/, 1/*F_Discount*/, '已起用的优惠券'/*F_Title*/, 'Color010'/*F_Color*/, ''/*F_Description*/, 1/*F_PersonalLimit*/, 0/*F_WeekDayAvailable*/, '9:00:00'/*F_BeginTime*/, '12:00:00'/*F_EndTime*/, '1998/03/03 15:15:15'/*F_BeginDateTime*/, 
		'3333/03/20 15:15:15'/*F_EndDateTime*/, 1000/*F_Quantity*/, 1000/*F_RemainingQuantity*/, 0/*F_Scope*/);
SET @couponID1 = last_insert_id();
-- 优惠券2
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0/*F_Status*/, 0/*F_Type*/, 1/*F_Bonus*/, 1.00/*F_LeastAmount*/, 4.00/*F_ReduceAmount*/, 1/*F_Discount*/, '已起用的优惠券'/*F_Title*/, 'Color010'/*F_Color*/, ''/*F_Description*/, 1/*F_PersonalLimit*/, 0/*F_WeekDayAvailable*/, '9:00:00'/*F_BeginTime*/, '12:00:00'/*F_EndTime*/, '1998/03/03 15:15:15'/*F_BeginDateTime*/, 
		'3333/03/20 15:15:15'/*F_EndDateTime*/, 1000/*F_Quantity*/, 1000/*F_RemainingQuantity*/, 0/*F_Scope*/);
SET @couponID2 = last_insert_id();
-- 优惠券3
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0/*F_Status*/, 1/*F_Type*/, 1/*F_Bonus*/, 10.00/*F_LeastAmount*/, 0.00/*F_ReduceAmount*/, 0.9/*F_Discount*/, '九折优惠券'/*F_Title*/, 'Color010'/*F_Color*/, ''/*F_Description*/, 1/*F_PersonalLimit*/, 0/*F_WeekDayAvailable*/, '9:00:00'/*F_BeginTime*/, '12:00:00'/*F_EndTime*/, '1998/03/03 15:15:15'/*F_BeginDateTime*/, 
		'3333/03/20 15:15:15'/*F_EndDateTime*/, 1000/*F_Quantity*/, 1000/*F_RemainingQuantity*/, 0/*F_Scope*/);
SET @couponID3 = last_insert_id();
-- 
SET @iPosID = -2;
SET @iBonus = 1;
SET @iType = -1;
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 1000000;
SET @iTotalRecord = 0;
-- 
CALL SP_Coupon_RetrieveN(@iErrorCode, @sErrorMsg, @iPosID, @iBonus, @iType, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = @iOldTotalRecord + 2, '测试成功', '测试失败') AS 'Case7 Testing Result';
-- 
DELETE FROM t_coupon WHERE F_ID = @couponID1;
DELETE FROM t_coupon WHERE F_ID = @couponID2;
DELETE FROM t_coupon WHERE F_ID = @couponID3;





SELECT '-------------------- Case8:传iPosID=-2,bonus=-1, F_Type = -1,代表小程序请求，查询出的优惠券包括未开始的，不包括过期、和已删除的 -------------------------' AS 'Case8';
-- 
SET @iPosID = -2;
SET @iBonus = -1;
SET @iType = -1;
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 1000000;
SET @iOldTotalRecord = 0;

CALL SP_Coupon_RetrieveN(@iErrorCode, @sErrorMsg, @iPosID, @iBonus, @iType, @iPageIndex, @iPageSize, @iOldTotalRecord);
SELECT @iOldTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case8 Testing Result';
-- 
-- 新建2张优惠券，1张过期的，1张已删除的优惠券用于测试
-- 优惠券2
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0/*F_Status*/, 0/*F_Type*/, 1/*F_Bonus*/, 1.00/*F_LeastAmount*/, 4.00/*F_ReduceAmount*/, 1/*F_Discount*/, '已起用的优惠券'/*F_Title*/, 'Color010'/*F_Color*/, ''/*F_Description*/, 1/*F_PersonalLimit*/, 0/*F_WeekDayAvailable*/, '9:00:00'/*F_BeginTime*/, '12:00:00'/*F_EndTime*/, '1998/03/03 15:15:15'/*F_BeginDateTime*/, 
		'2000/03/20 15:15:15'/*F_EndDateTime*/, 1000/*F_Quantity*/, 1000/*F_RemainingQuantity*/, 0/*F_Scope*/);
SET @couponID2 = last_insert_id();
-- 优惠券3
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (1/*F_Status*/, 1/*F_Type*/, 1/*F_Bonus*/, 10.00/*F_LeastAmount*/, 0.00/*F_ReduceAmount*/, 0.9/*F_Discount*/, '九折优惠券'/*F_Title*/, 'Color010'/*F_Color*/, ''/*F_Description*/, 1/*F_PersonalLimit*/, 0/*F_WeekDayAvailable*/, '9:00:00'/*F_BeginTime*/, '12:00:00'/*F_EndTime*/, '1998/03/03 15:15:15'/*F_BeginDateTime*/, 
		'3333/03/20 15:15:15'/*F_EndDateTime*/, 1000/*F_Quantity*/, 1000/*F_RemainingQuantity*/, 0/*F_Scope*/);
SET @couponID3 = last_insert_id();
-- 
SET @iPosID = -2;
SET @iBonus = -1;
SET @iType = -1;
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 1000000;
SET @iTotalRecord = 0;
-- 
CALL SP_Coupon_RetrieveN(@iErrorCode, @sErrorMsg, @iPosID, @iBonus, @iType, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = @iOldTotalRecord, '测试成功', '测试失败') AS 'Case8 Testing Result';
-- 
DELETE FROM t_coupon WHERE F_ID = @couponID2;
DELETE FROM t_coupon WHERE F_ID = @couponID3;



SELECT '-------------------- Case9:传iPosID=-2,bonus=-1, F_Type = -1,代表小程序请求，查询出的优惠券包括未开始的 -------------------------' AS 'Case8';
-- 
SET @iPosID = -2;
SET @iBonus = -1;
SET @iType = -1;
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 1000000;
SET @iOldTotalRecord = 0;

CALL SP_Coupon_RetrieveN(@iErrorCode, @sErrorMsg, @iPosID, @iBonus, @iType, @iPageIndex, @iPageSize, @iOldTotalRecord);
SELECT @iOldTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case9 Testing Result';
-- 
-- 新建1张优惠券，1张未开始的用于测试

-- 优惠券1
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0/*F_Status*/, 0/*F_Type*/, 0/*F_Bonus*/, 1.00/*F_LeastAmount*/, 4.00/*F_ReduceAmount*/, 1/*F_Discount*/, '已起用的优惠券'/*F_Title*/, 'Color010'/*F_Color*/, ''/*F_Description*/, 1/*F_PersonalLimit*/, 0/*F_WeekDayAvailable*/, '9:00:00'/*F_BeginTime*/, '12:00:00'/*F_EndTime*/, '3000/03/03 15:15:15'/*F_BeginDateTime*/, 
		'3333/03/20 15:15:15'/*F_EndDateTime*/, 1000/*F_Quantity*/, 1000/*F_RemainingQuantity*/, 0/*F_Scope*/);
SET @couponID1 = last_insert_id();
-- 
SET @iPosID = -2;
SET @iBonus = -1;
SET @iType = -1;
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 1000000;
SET @iTotalRecord = 0;
-- 
CALL SP_Coupon_RetrieveN(@iErrorCode, @sErrorMsg, @iPosID, @iBonus, @iType, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = @iOldTotalRecord + 1, '测试成功', '测试失败') AS 'Case9 Testing Result';
DELETE FROM t_coupon WHERE F_ID = @couponID1;