SELECT '++++++++++++++++++ Test_SP_CouponCode_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:������� -------------------------' AS 'Case1';

SET @iQuantity = 1000;
SET @iRemainingQuantity = 1000;
-- 
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, 
F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0, 0, 0, 0, 10, 1, '�ֽ�ȯ', 'xxxxxxxx', 'ʹ��˵��', 1, 0, '00:00:00', '23:59:59', '1970/01/01', '2030/01/01', @iQuantity, @iRemainingQuantity, 0);
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
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';
-- 
SELECT IF((SELECT F_RemainingQuantity FROM t_coupon WHERE F_ID = @iCouponID) = @iQuantity - 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';
-- 
DELETE FROM t_couponcode WHERE F_ID = LAST_INSERT_ID();
DELETE FROM t_coupon WHERE F_ID = @iCouponID;

SELECT '-------------------- Case2:��ԱID������ -------------------------' AS 'Case2';
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, 
F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0, 0, 0, 0, 10, 1, '�ֽ�ȯ', 'xxxxxxxx', 'ʹ��˵��', 1, 0, '00:00:00', '23:59:59', '1970/01/01', '2030/01/01', 1000, 1000, 0);
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
SELECT IF(@iErrorCode = 2 AND @sErrorMsg = '�û�Ա������', '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
-- 
DELETE FROM t_coupon WHERE F_ID = @iCouponID;

SELECT '-------------------- Case3:�Ż�ȯID������ -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iVipID = 1;
SET @iCouponID = -1;
SET @iStatus = 0;
-- 
CALL SP_CouponCode_Create(@iErrorCode, @sErrorMsg, @iVipID, @iCouponID, @iStatus);
-- 
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 2 AND @sErrorMsg = '���Ż�ȯ������', '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';

SELECT '-------------------- Case4:�Ż�ȯ��治�� -------------------------' AS 'Case4';
SET @iQuantity = 1000;
SET @iRemainingQuantity = 0;
-- 
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, 
F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0, 0, 0, 0, 10, 1, '�ֽ�ȯ', 'xxxxxxxx', 'ʹ��˵��', 1, 0, '00:00:00', '23:59:59', '1970/01/01', '2030/01/01', @iQuantity, @iRemainingQuantity, 0);
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
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '�Ż�ȯ��治��,�޷���ȡ', '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';
-- 
DELETE FROM t_couponcode WHERE F_ID = LAST_INSERT_ID();
DELETE FROM t_coupon WHERE F_ID = @iCouponID;


SELECT '-------------------- Case5:�Ż�ȯ�Ѿ���ɾ���������ٱ���ȡ -------------------------' AS 'Case5';
-- 
SET @iQuantity = 1000;
SET @iRemainingQuantity = 1000;
--
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, 
F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (1, 0, 0, 0, 10, 1, '�ֽ�ȯ', 'xxxxxxxx', 'ʹ��˵��', 1, 0, '00:00:00', '23:59:59', '1970/01/01', '2030/01/01', @iQuantity, @iRemainingQuantity, 0);
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
SELECT IF(@iErrorCode = 2 AND @sErrorMsg = '���Ż�ȯ������', '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';
DELETE FROM t_coupon WHERE F_ID = @iCouponID;

SELECT '-------------------- Case6:��Ա�ѳ�����ȡ���Ż�ȯ�ĸ�����Ŀ���ޣ��޷���ȡ -------------------------' AS 'Case1';

SET @iQuantity = 1000;
SET @iRemainingQuantity = 1000;
-- 
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, 
F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0, 0, 0, 0, 10, 1, '�ֽ�ȯ', 'xxxxxxxx', 'ʹ��˵��', 1, 0, '00:00:00', '23:59:59', '1970/01/01', '2030/01/01', @iQuantity, @iRemainingQuantity, 0);
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
SELECT IF(found_rows() = 1 AND @iErrorCode = 7 AND @sErrorMsg = '��Ա�ѳ�����ȡ���Ż�ȯ�ĸ�����Ŀ���ޣ��޷���ȡ', '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';
-- 
SELECT IF((SELECT F_RemainingQuantity FROM t_coupon WHERE F_ID = @iCouponID) = @iQuantity - 1 , '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';
-- 
DELETE FROM t_couponcode WHERE F_ID = LAST_INSERT_ID();
DELETE FROM t_coupon WHERE F_ID = @iCouponID;

SELECT '-------------------- Case7:ʹ�û�����ȡ�Ż�ȯ -------------------------' AS 'Case1';
SET @iVipBonus = 50;
INSERT INTO T_VIP (F_ICID,F_CardID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile, F_CreateDatetime)
VALUES ('1232456',1,'giggs','1232456@bx.vip',5,99.8,'����',1,
'2017-08-06',@iVipBonus,'2017-08-08 23:59:10','12345612312', now());
SET @iVipID = last_insert_id();
-- 
SET @iQuantity = 1000;
SET @iRemainingQuantity = 1000;
SET @iCouponBonus = 20;
-- 
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, 
F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0, 0, @iCouponBonus, 0, 10, 1, '�ֽ�ȯ', 'xxxxxxxx', 'ʹ��˵��', 1, 0, '00:00:00', '23:59:59', '1970/01/01', '2030/01/01', @iQuantity, @iRemainingQuantity, 0);
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
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';
-- 
SELECT IF((SELECT F_RemainingQuantity FROM t_coupon WHERE F_ID = @iCouponID) = @iQuantity - 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';
-- 
SELECT IF((SELECT F_Bonus FROM t_Vip WHERE F_ID = @iVipID) = @iVipBonus - @iCouponBonus AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';
-- 
DELETE FROM t_couponcode WHERE F_ID = LAST_INSERT_ID();
DELETE FROM t_coupon WHERE F_ID = @iCouponID;
DELETE FROM t_bonusconsumehistory WHERE F_VipID = @iVipID;
DELETE FROM t_vip WHERE F_ID = @iVipID;

SELECT '-------------------- Case8:��Ա���ֲ���,�޷���ȡ���Ż�ȯ -------------------------' AS 'Case1';
SET @iVipBonus = 0;
INSERT INTO T_VIP (F_ICID,F_CardID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile, F_CreateDatetime)
VALUES ('1232456',1,'giggs','1232456@bx.vip',5,99.8,'����',1,
'2017-08-06',@iVipBonus,'2017-08-08 23:59:10','12345612312', now());
SET @iVipID = last_insert_id();
-- 
SET @iQuantity = 1000;
SET @iRemainingQuantity = 1000;
SET @iCouponBonus = 20;
-- 
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, 
F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0, 0, @iCouponBonus, 0, 10, 1, '�ֽ�ȯ', 'xxxxxxxx', 'ʹ��˵��', 1, 0, '00:00:00', '23:59:59', '1970/01/01', '2030/01/01', @iQuantity, @iRemainingQuantity, 0);
SET @iCouponID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCouponID = @iCouponID;
SET @iStatus = 0;
-- 
CALL SP_CouponCode_Create(@iErrorCode, @sErrorMsg, @iVipID, @iCouponID, @iStatus);
-- 
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '��Ա���ֲ���,�޷���ȡ���Ż�ȯ', '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';
-- 
DELETE FROM t_vip WHERE F_ID = @iVipID;

