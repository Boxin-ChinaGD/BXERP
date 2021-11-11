SELECT '++++++++++++++++++ Test_SP_CouponCode_Delete.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:正常删除 -------------------------' AS 'Case1';

INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, 
F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0, 0, 0, 0, 10, 1, '现金券', 'xxxxxxxx', '使用说明', 1, 0, '00:00:00', '23:59:59', '1970/01/01', '2030/01/01', 1000, 1000, 0);
SET @iCouponID = last_insert_id();

INSERT INTO t_couponcode (F_VipID, F_CouponID, F_Status, F_SN, F_CreateDatetime, F_UsedDatetime)
VALUES (1, @iCouponID, 0, '123456789012345', now(), null);
SET @iID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_CouponCode_Delete(@iErrorCode, @sErrorMsg, @iID);
-- 
SELECT @sErrorMsg;
SELECT 1 FROM t_couponcode WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

DELETE FROM t_coupon WHERE F_ID = @iCouponID;