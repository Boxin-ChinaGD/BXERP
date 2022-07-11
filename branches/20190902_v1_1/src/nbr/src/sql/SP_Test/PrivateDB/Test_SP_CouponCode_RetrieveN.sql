SELECT '++++++++++++++++++ Test_SP_CouponCode_RetrieveN.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:查询所有 -------------------------' AS 'Case1';
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, 
F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0, 0, 0, 0, 10, 1, '现金券', 'xxxxxxxx', '使用说明', 1, 0, '00:00:00', '23:59:59', '1970/01/01', '2030/01/01', 1000, 1000, 0);
SET @iCouponID = last_insert_id();

INSERT INTO t_couponcode (F_VipID, F_CouponID, F_Status, F_SN, F_CreateDatetime, F_UsedDatetime)
VALUES (1, @iCouponID, 0, '123456789012345', now(), null);
SET @iID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iVipID = -1;
SET @iCouponID = -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iStatus = -1;
SET @iTotalRecord = 0;

CALL SP_CouponCode_RetrieveN(@iErrorCode, @sErrorMsg, @iVipID, @iCouponID, @iStatus, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

DELETE FROM t_couponcode WHERE F_ID = @iID;
DELETE FROM t_coupon WHERE F_ID = @iCouponID;

SELECT '-------------------- Case2:根据vipID查询 -------------------------' AS 'Case2';
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, 
F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0, 0, 0, 0, 10, 1, '现金券', 'xxxxxxxx', '使用说明', 1, 0, '00:00:00', '23:59:59', '1970/01/01', '2030/01/01', 1000, 1000, 0);
SET @iCouponID = last_insert_id();

INSERT INTO t_couponcode (F_VipID, F_CouponID, F_Status, F_SN, F_CreateDatetime, F_UsedDatetime)
VALUES (1, @iCouponID, 0, '123456789012345', now(), null);
SET @iID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iVipID = 1;
SET @iCouponID = -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iStatus = -1;
SET @iTotalRecord = 0;

CALL SP_CouponCode_RetrieveN(@iErrorCode, @sErrorMsg, @iVipID, @iCouponID, @iStatus, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';

DELETE FROM t_couponcode WHERE F_ID = @iID;
DELETE FROM t_coupon WHERE F_ID = @iCouponID;


SELECT '-------------------- Case3:根据couponID查询 -------------------------' AS 'Case3';
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, 
F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0, 0, 0, 0, 10, 1, '现金券', 'xxxxxxxx', '使用说明', 1, 0, '00:00:00', '23:59:59', '1970/01/01', '2030/01/01', 1000, 1000, 0);
SET @iCouponID = last_insert_id();

INSERT INTO t_couponcode (F_VipID, F_CouponID, F_Status, F_SN, F_CreateDatetime, F_UsedDatetime)
VALUES (1, @iCouponID, 0, '123456789012345', now(), null);
SET @iID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iVipID = -1;
SET @iCouponID = @iCouponID;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStatus = -1;

CALL SP_CouponCode_RetrieveN(@iErrorCode, @sErrorMsg, @iVipID, @iCouponID, @iStatus, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case3 Testing Result';

DELETE FROM t_couponcode WHERE F_ID = @iID;
DELETE FROM t_coupon WHERE F_ID = @iCouponID;

SELECT '-------------------- Case4:根据多个条件查询 -------------------------' AS 'Case4';
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, 
F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0, 0, 0, 0, 10, 1, '现金券', 'xxxxxxxx', '使用说明', 1, 0, '00:00:00', '23:59:59', '1970/01/01', '2030/01/01', 1000, 1000, 0);
SET @iCouponID = last_insert_id();

INSERT INTO t_couponcode (F_VipID, F_CouponID, F_Status, F_SN, F_CreateDatetime, F_UsedDatetime)
VALUES (1, @iCouponID, 0, '123456789012345', now(), null);
SET @iID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iVipID = 1;
SET @iCouponID = @iCouponID;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStatus = -1;

CALL SP_CouponCode_RetrieveN(@iErrorCode, @sErrorMsg, @iVipID, @iCouponID, @iStatus, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case4 Testing Result';

DELETE FROM t_couponcode WHERE F_ID = @iID;
DELETE FROM t_coupon WHERE F_ID = @iCouponID;

SELECT '-------------------- Case5: 根据iStatus进行查找 -------------------------' AS 'Case5';
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, 
F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0, 0, 0, 0, 10, 1, '现金券', 'xxxxxxxx', '使用说明', 1, 0, '00:00:00', '23:59:59', '1970/01/01', '2030/01/01', 1000, 1000, 0);
SET @iCouponID = last_insert_id();

INSERT INTO t_couponcode (F_VipID, F_CouponID, F_Status, F_SN, F_CreateDatetime, F_UsedDatetime)
VALUES (1, @iCouponID, 1, '123456789012345', now(), null);
SET @iID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iVipID = -1;
SET @iCouponID = -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iStatus = 1;

CALL SP_CouponCode_RetrieveN(@iErrorCode, @sErrorMsg, @iVipID, @iCouponID, @iStatus, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case4 Testing Result';

DELETE FROM t_couponcode WHERE F_ID = @iID;
DELETE FROM t_coupon WHERE F_ID = @iCouponID;