SELECT '++++++++++++++++++ Test_SP_CouponScope_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:������� -------------------------' AS 'Case1';
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, 
F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0, 0, 0, 0, 10, 1, '�ֽ�ȯ', 'xxxxxxxx', 'ʹ��˵��', 1, 0, '00:00:00', '23:59:59', '1970/01/01', '2030/01/01', 1000, 1000, 0);
SET @iCouponID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCouponID = @iCouponID;
SET @iCommodityID = 1;
-- 
CALL SP_CouponScope_Create(@iErrorCode, @sErrorMsg, @iCouponID, @iCommodityID);
-- 
SELECT @sErrorMsg;
SELECT 1 FROM t_couponscope WHERE F_CouponID = @iCouponID AND F_CommodityID = @iCommodityID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';
-- 

DELETE FROM t_couponscope WHERE F_ID = LAST_INSERT_ID();
DELETE FROM t_coupon WHERE F_ID = @iCouponID;

SELECT '-------------------- Case2:�Ż�ȯ������ -------------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCouponID = -1;
SET @iCommodityID = 1;
-- 
CALL SP_CouponScope_Create(@iErrorCode, @sErrorMsg, @iCouponID, @iCommodityID);
-- 
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 2 AND @sErrorMsg = '���Ż�ȯ������', '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

SELECT '-------------------- Case3:��Ʒ������ -------------------------' AS 'Case3';
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, 
F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0, 0, 0, 0, 10, 1, '�ֽ�ȯ', 'xxxxxxxx', 'ʹ��˵��', 1, 0, '00:00:00', '23:59:59', '1970/01/01', '2030/01/01', 1000, 1000, 0);
SET @iCouponID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCouponID = @iCouponID;
SET @iCommodityID = -1;
-- 
CALL SP_CouponScope_Create(@iErrorCode, @sErrorMsg, @iCouponID, @iCommodityID);
-- 
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 2 AND @sErrorMsg = '����Ʒ������', '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';

DELETE FROM t_coupon WHERE F_ID = @iCouponID;