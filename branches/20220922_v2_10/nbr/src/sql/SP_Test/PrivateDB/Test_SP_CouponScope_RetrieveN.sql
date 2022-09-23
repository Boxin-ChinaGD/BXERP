SELECT '++++++++++++++++++ Test_SP_CouponScope_RetrieveN.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:查询所有 -------------------------' AS 'Case1';
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, 
F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0, 0, 0, 0, 10, 1, '现金券', 'xxxxxxxx', '使用说明', 1, 0, '00:00:00', '23:59:59', '1970/01/01', '2030/01/01', 1000, 1000, 0);
SET @iCouponID = last_insert_id();

INSERT INTO t_couponscope (F_CouponID, F_CommodityID, F_CommodityName)
VALUES (@iCouponID, 1, 'xxxxxxx');
SET @iID = last_insert_id();


SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCouponID = -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_CouponScope_RetrieveN(@iErrorCode, @sErrorMsg, @iCouponID, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

DELETE FROM t_couponscope WHERE F_ID = @iID;
DELETE FROM t_coupon WHERE F_ID = @iCouponID;

SELECT '-------------------- Case2:根据CouponID查询 -------------------------' AS 'Case2';
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, 
F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0, 0, 0, 0, 10, 1, '现金券', 'xxxxxxxx', '使用说明', 1, 0, '00:00:00', '23:59:59', '1970/01/01', '2030/01/01', 1000, 1000, 0);
SET @iCouponID = last_insert_id();

INSERT INTO t_couponscope (F_CouponID, F_CommodityID, F_CommodityName)
VALUES (@iCouponID, 1, 'xxxxxxx');
SET @iID = last_insert_id();


SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCouponID = @iCouponID;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_CouponScope_RetrieveN(@iErrorCode, @sErrorMsg, @iCouponID, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';

DELETE FROM t_couponscope WHERE F_ID = @iID;
DELETE FROM t_coupon WHERE F_ID = @iCouponID;

SELECT '-------------------- Case3:根据不存在的CouponID查询 -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCouponID = -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_CouponScope_RetrieveN(@iErrorCode, @sErrorMsg, @iCouponID, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case3 Testing Result';