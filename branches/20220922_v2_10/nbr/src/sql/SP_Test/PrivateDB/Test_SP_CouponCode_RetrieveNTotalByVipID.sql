SELECT '++++++++++++++++++ Test_SP_CouponCode_RetrieveNTotalByVipID.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:��ѯĳ����Ա���Ż�ȯ���� -------------------------' AS 'Case1';
INSERT INTO T_VIP (F_SN,F_CardID,F_ICID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,F_District,F_Category,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile)
VALUES ('VIP000101'/*F_SN*/,1/*F_CardID*/,'3208031997070216031'/*F_ICID*/,'g4iggs'/*F_Name*/,'12323456@bx.vip'/*F_Email*/,5/*F_ConsumeTimes*/,99.8/*F_ConsumeAmount*/,
'����'/*F_District*/,1/*F_Category*/,'2017-08-06'/*F_Birthday*/,0/*F_Bonus*/,'2017-08-08 23:59:10'/*F_LastConsumeDatetime*/,'13545678170'/*F_Mobile*/);
SET @iVipID = last_insert_id();
-- 
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0/*F_Status*/, 0/*F_Type*/, 0/*F_Bonus*/, 1.00/*F_LeastAmount*/, 4.00/*F_ReduceAmount*/, 1/*F_Discount*/, '�����õ��Ż�ȯ'/*F_Title*/, 'Color010'/*F_Color*/, ''/*F_Description*/, 1/*F_PersonalLimit*/, 0/*F_WeekDayAvailable*/, '9:00:00'/*F_BeginTime*/, '12:00:00'/*F_EndTime*/, '1998/03/03 15:15:15'/*F_BeginDateTime*/, 
		'3333/03/20 15:15:15'/*F_EndDateTime*/, 1000/*F_Quantity*/, 1000/*F_RemainingQuantity*/, 0/*F_Scope*/);
SET @iCouponID = LAST_INSERT_ID();
-- 
INSERT INTO t_couponcode (F_VipID, F_CouponID, F_Status, F_SN, F_CreateDatetime, F_UsedDatetime)
VALUES (@iVipID, @iCouponID, 0, '1231224w646', now(), now());
SET @iCouponCodeID_A = last_insert_id();

INSERT INTO t_couponcode (F_VipID, F_CouponID, F_Status, F_SN, F_CreateDatetime, F_UsedDatetime)
VALUES (@iVipID, @iCouponID, 0, '22222222', now(), now());
SET @iCouponCodeID_B = last_insert_id();
--
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iTotalRecord = 0;
CALL SP_CouponCode_RetrieveNTotalByVipID(@iErrorCode, @sErrorMsg, @iVipID, @iTotalRecord);
SELECT IF(@iTotalRecord = 2 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE1 Testing Result';
-- 
DELETE FROM t_couponcode WHERE F_ID = @iCouponCodeID_A;
DELETE FROM t_couponcode WHERE F_ID = @iCouponCodeID_B;
DELETE FROM t_coupon WHERE F_ID = @iCouponID;
DELETE FROM t_vip WHERE F_ID = @iVipID;

SELECT '-------------------- Case2:��ѯ�����ڵĻ�Ա�Ż�ȯ���� -------------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iTotalRecord = 0;
SET @iVipID = -1;
CALL SP_CouponCode_RetrieveNTotalByVipID(@iErrorCode, @sErrorMsg, @iVipID, @iTotalRecord);
SELECT IF(@iErrorCode = 2, '���Գɹ�', '����ʧ��') AS 'CASE2 Testing Result';

SELECT '-------------------- Case3:��ѯ�Ļ�Ա���Ż�ȯȫ�����Ѻ��� -------------------------' AS 'Case3';
INSERT INTO T_VIP (F_SN,F_CardID,F_ICID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,F_District,F_Category,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile)
VALUES ('VIP000101'/*F_SN*/,1/*F_CardID*/,'3208031997070216031'/*F_ICID*/,'g4iggs'/*F_Name*/,'12323456@bx.vip'/*F_Email*/,5/*F_ConsumeTimes*/,99.8/*F_ConsumeAmount*/,
'����'/*F_District*/,1/*F_Category*/,'2017-08-06'/*F_Birthday*/,0/*F_Bonus*/,'2017-08-08 23:59:10'/*F_LastConsumeDatetime*/,'13545678170'/*F_Mobile*/);
SET @iVipID = last_insert_id();
-- 
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0/*F_Status*/, 0/*F_Type*/, 0/*F_Bonus*/, 1.00/*F_LeastAmount*/, 4.00/*F_ReduceAmount*/, 1/*F_Discount*/, '�����õ��Ż�ȯ'/*F_Title*/, 'Color010'/*F_Color*/, ''/*F_Description*/, 1/*F_PersonalLimit*/, 0/*F_WeekDayAvailable*/, '9:00:00'/*F_BeginTime*/, '12:00:00'/*F_EndTime*/, '1998/03/03 15:15:15'/*F_BeginDateTime*/, 
		'3333/03/20 15:15:15'/*F_EndDateTime*/, 1000/*F_Quantity*/, 1000/*F_RemainingQuantity*/, 0/*F_Scope*/);
SET @iCouponID = LAST_INSERT_ID();
-- 
INSERT INTO t_couponcode (F_VipID, F_CouponID, F_Status, F_SN, F_CreateDatetime, F_UsedDatetime)
VALUES (@iVipID, @iCouponID, 1, '1231224w646', now(), now());
SET @iCouponCodeID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iTotalRecord = 0;
CALL SP_CouponCode_RetrieveNTotalByVipID(@iErrorCode, @sErrorMsg, @iVipID, @iTotalRecord);
SELECT IF(@iTotalRecord = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE3 Testing Result';
-- 
DELETE FROM t_couponcode WHERE F_ID = @iCouponCodeID;
DELETE FROM t_coupon WHERE F_ID = @iCouponID;
DELETE FROM t_vip WHERE F_ID = @iVipID;

SELECT '-------------------- Case4:�����Ż�ȯ�Ѿ�ɾ��,���ǻ�Ա��ȡ�Ż�ȯ��δʹ��(�ܲ�ѯ����) -------------------------' AS 'Case4';
INSERT INTO T_VIP (F_SN,F_CardID,F_ICID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,F_District,F_Category,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile)
VALUES ('VIP000101'/*F_SN*/,1/*F_CardID*/,'3208031997070216031'/*F_ICID*/,'g4iggs'/*F_Name*/,'12323456@bx.vip'/*F_Email*/,5/*F_ConsumeTimes*/,99.8/*F_ConsumeAmount*/,
'����'/*F_District*/,1/*F_Category*/,'2017-08-06'/*F_Birthday*/,0/*F_Bonus*/,'2017-08-08 23:59:10'/*F_LastConsumeDatetime*/,'13545678170'/*F_Mobile*/);
SET @iVipID = last_insert_id();
-- 
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (1/*F_Status*/, 0/*F_Type*/, 0/*F_Bonus*/, 1.00/*F_LeastAmount*/, 4.00/*F_ReduceAmount*/, 1/*F_Discount*/, '�����õ��Ż�ȯ'/*F_Title*/, 'Color010'/*F_Color*/, ''/*F_Description*/, 1/*F_PersonalLimit*/, 0/*F_WeekDayAvailable*/, '9:00:00'/*F_BeginTime*/, '12:00:00'/*F_EndTime*/, '1998/03/03 15:15:15'/*F_BeginDateTime*/, 
		'3333/03/20 15:15:15'/*F_EndDateTime*/, 1000/*F_Quantity*/, 1000/*F_RemainingQuantity*/, 0/*F_Scope*/);
SET @iCouponID = LAST_INSERT_ID();
-- 
INSERT INTO t_couponcode (F_VipID, F_CouponID, F_Status, F_SN, F_CreateDatetime, F_UsedDatetime)
VALUES (@iVipID, @iCouponID, 0, '1231224w646', now(), now());
SET @iCouponCodeID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iTotalRecord = 0;
CALL SP_CouponCode_RetrieveNTotalByVipID(@iErrorCode, @sErrorMsg, @iVipID, @iTotalRecord);
SELECT IF(@iTotalRecord = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE4 Testing Result';
-- 
DELETE FROM t_couponcode WHERE F_ID = @iCouponCodeID;
DELETE FROM t_coupon WHERE F_ID = @iCouponID;
DELETE FROM t_vip WHERE F_ID = @iVipID;

SELECT '-------------------- Case5:�����Ż�ȯ�Ѿ�����,���ǻ�Ա��ȡ�Ż�ȯ��δʹ��(���ܲ�ѯ����) -------------------------' AS 'Case5';
INSERT INTO T_VIP (F_SN,F_CardID,F_ICID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,F_District,F_Category,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile)
VALUES ('VIP000101'/*F_SN*/,1/*F_CardID*/,'3208031997070216031'/*F_ICID*/,'g4iggs'/*F_Name*/,'12323456@bx.vip'/*F_Email*/,5/*F_ConsumeTimes*/,99.8/*F_ConsumeAmount*/,
'����'/*F_District*/,1/*F_Category*/,'2017-08-06'/*F_Birthday*/,0/*F_Bonus*/,'2017-08-08 23:59:10'/*F_LastConsumeDatetime*/,'13545678170'/*F_Mobile*/);
SET @iVipID = last_insert_id();
-- 
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0/*F_Status*/, 0/*F_Type*/, 0/*F_Bonus*/, 1.00/*F_LeastAmount*/, 4.00/*F_ReduceAmount*/, 1/*F_Discount*/, '�����õ��Ż�ȯ'/*F_Title*/, 'Color010'/*F_Color*/, ''/*F_Description*/, 1/*F_PersonalLimit*/, 0/*F_WeekDayAvailable*/, '9:00:00'/*F_BeginTime*/, '12:00:00'/*F_EndTime*/, '1998/03/03 15:15:15'/*F_BeginDateTime*/, 
		'20000/03/20 15:15:15'/*F_EndDateTime*/, 1000/*F_Quantity*/, 1000/*F_RemainingQuantity*/, 0/*F_Scope*/);
SET @iCouponID = LAST_INSERT_ID();
-- 
INSERT INTO t_couponcode (F_VipID, F_CouponID, F_Status, F_SN, F_CreateDatetime, F_UsedDatetime)
VALUES (@iVipID, @iCouponID, 0, '1231224w646', now(), now());
SET @iCouponCodeID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iTotalRecord = 0;
CALL SP_CouponCode_RetrieveNTotalByVipID(@iErrorCode, @sErrorMsg, @iVipID, @iTotalRecord);
SELECT IF(@iTotalRecord = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE5 Testing Result';
-- 
DELETE FROM t_couponcode WHERE F_ID = @iCouponCodeID;
DELETE FROM t_coupon WHERE F_ID = @iCouponID;
DELETE FROM t_vip WHERE F_ID = @iVipID;