SELECT '++++++++++++++++++ Test_SP_CouponCode_RetrieveN.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:��ѯ���� -------------------------' AS 'Case1';
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, 
F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0, 0, 0, 0, 10, 1, '�ֽ�ȯ', 'xxxxxxxx', 'ʹ��˵��', 1, 0, '00:00:00', '23:59:59', '1970/01/01', '2030/01/01', 1000, 1000, 0);
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

DELETE FROM t_couponcode WHERE F_ID = @iID;
DELETE FROM t_coupon WHERE F_ID = @iCouponID;

SELECT '-------------------- Case2:����vipID��ѯ -------------------------' AS 'Case2';
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, 
F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0, 0, 0, 0, 10, 1, '�ֽ�ȯ', 'xxxxxxxx', 'ʹ��˵��', 1, 0, '00:00:00', '23:59:59', '1970/01/01', '2030/01/01', 1000, 1000, 0);
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

DELETE FROM t_couponcode WHERE F_ID = @iID;
DELETE FROM t_coupon WHERE F_ID = @iCouponID;


SELECT '-------------------- Case3:����couponID��ѯ -------------------------' AS 'Case3';
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, 
F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0, 0, 0, 0, 10, 1, '�ֽ�ȯ', 'xxxxxxxx', 'ʹ��˵��', 1, 0, '00:00:00', '23:59:59', '1970/01/01', '2030/01/01', 1000, 1000, 0);
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';

DELETE FROM t_couponcode WHERE F_ID = @iID;
DELETE FROM t_coupon WHERE F_ID = @iCouponID;

SELECT '-------------------- Case4:���ݶ��������ѯ -------------------------' AS 'Case4';
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, 
F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0, 0, 0, 0, 10, 1, '�ֽ�ȯ', 'xxxxxxxx', 'ʹ��˵��', 1, 0, '00:00:00', '23:59:59', '1970/01/01', '2030/01/01', 1000, 1000, 0);
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';

DELETE FROM t_couponcode WHERE F_ID = @iID;
DELETE FROM t_coupon WHERE F_ID = @iCouponID;

SELECT '-------------------- Case5: ����iStatus���в��� -------------------------' AS 'Case5';
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, 
F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0, 0, 0, 0, 10, 1, '�ֽ�ȯ', 'xxxxxxxx', 'ʹ��˵��', 1, 0, '00:00:00', '23:59:59', '1970/01/01', '2030/01/01', 1000, 1000, 0);
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
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';

DELETE FROM t_couponcode WHERE F_ID = @iID;
DELETE FROM t_coupon WHERE F_ID = @iCouponID;