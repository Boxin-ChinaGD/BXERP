SELECT '++++++++++++++++++ Test_SP_Coupon_RetrieveN.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:web��ѯ���� -------------------------' AS 'Case1';
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, 
F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0, 0, 0, 0, 10, 1, '�ֽ�ȯ', 'xxxxxxxx', 'ʹ��˵��', 1, 0, '00:00:00', '23:59:59', '1970/01/01', '2030/01/01', 1000, 1000, 0);
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

DELETE FROM t_coupon WHERE F_ID = @iID;

SELECT '-------------------- Case2:pos��ѯ���� -------------------------' AS 'Case2';
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

SELECT '-------------------- Csse1 && Case2��ͬ�Ľ����֤ -------------------------' AS 'Case1 & Case2��ͬ�Ľ����֤';
SELECT IF(@iTotalRecord1 >= @iTotalRecord2, '���Գɹ�', '����ʧ��') AS 'Case1 && Case2 Testing Result';



SELECT '-------------------- Case3:��iPosID=-2,bonus=-1, F_Type = -1,����С�������󣬲�ѯ�������Ż�ȯ(����δ��ʼ�ģ����������ڡ�����ɾ����) -------------------------' AS 'Case3';
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';





SELECT '-------------------- Case4:��iPosID=-2,bonus=-1, F_Type = 0,����С�������󣬲�ѯ�������ֽ�ȯ(����δ��ʼ�ģ����������ڡ�����ɾ����) -------------------------' AS 'Case4';
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';
-- �½������ֽ�ȯ��1���ۿ�ȯ���ڲ���
-- �ֽ�ȯ1
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0/*F_Status*/, 0/*F_Type*/, 0/*F_Bonus*/, 1.00/*F_LeastAmount*/, 4.00/*F_ReduceAmount*/, 1/*F_Discount*/, '�����õ��Ż�ȯ'/*F_Title*/, 'Color010'/*F_Color*/, ''/*F_Description*/, 1/*F_PersonalLimit*/, 0/*F_WeekDayAvailable*/, '9:00:00'/*F_BeginTime*/, '12:00:00'/*F_EndTime*/, '1998/03/03 15:15:15'/*F_BeginDateTime*/, 
		'3333/03/20 15:15:15'/*F_EndDateTime*/, 1000/*F_Quantity*/, 1000/*F_RemainingQuantity*/, 0/*F_Scope*/);
SET @couponID1 = last_insert_id();
-- �ֽ�ȯ2
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0/*F_Status*/, 0/*F_Type*/, 0/*F_Bonus*/, 1.00/*F_LeastAmount*/, 4.00/*F_ReduceAmount*/, 1/*F_Discount*/, '�����õ��Ż�ȯ'/*F_Title*/, 'Color010'/*F_Color*/, ''/*F_Description*/, 1/*F_PersonalLimit*/, 0/*F_WeekDayAvailable*/, '9:00:00'/*F_BeginTime*/, '12:00:00'/*F_EndTime*/, '1998/03/03 15:15:15'/*F_BeginDateTime*/, 
		'3333/03/20 15:15:15'/*F_EndDateTime*/, 1000/*F_Quantity*/, 1000/*F_RemainingQuantity*/, 0/*F_Scope*/);
SET @couponID2 = last_insert_id();
-- �ۿ�ȯ
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0/*F_Status*/, 1/*F_Type*/, 0/*F_Bonus*/, 10.00/*F_LeastAmount*/, 0.00/*F_ReduceAmount*/, 0.9/*F_Discount*/, '�����Ż�ȯ'/*F_Title*/, 'Color010'/*F_Color*/, ''/*F_Description*/, 1/*F_PersonalLimit*/, 0/*F_WeekDayAvailable*/, '9:00:00'/*F_BeginTime*/, '12:00:00'/*F_EndTime*/, '1998/03/03 15:15:15'/*F_BeginDateTime*/, 
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
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = @iOldTotalRecord + 2, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';
-- 
DELETE FROM t_coupon WHERE F_ID = @couponID1;
DELETE FROM t_coupon WHERE F_ID = @couponID2;
DELETE FROM t_coupon WHERE F_ID = @couponID3;





SELECT '-------------------- Case5:��iPosID=-2,bonus=-1, F_Type = 1,����С�������󣬲�ѯ�������ۿ�ȯ(����δ��ʼ�ģ����������ڡ�����ɾ����) -------------------------' AS 'Case5';
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';
-- �½�1���ֽ�ȯ��2���ۿ�ȯ���ڲ���
-- �ֽ�ȯ1
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0/*F_Status*/, 0/*F_Type*/, 0/*F_Bonus*/, 1.00/*F_LeastAmount*/, 4.00/*F_ReduceAmount*/, 1/*F_Discount*/, '�����õ��Ż�ȯ'/*F_Title*/, 'Color010'/*F_Color*/, ''/*F_Description*/, 1/*F_PersonalLimit*/, 0/*F_WeekDayAvailable*/, '9:00:00'/*F_BeginTime*/, '12:00:00'/*F_EndTime*/, '1998/03/03 15:15:15'/*F_BeginDateTime*/, 
		'3333/03/20 15:15:15'/*F_EndDateTime*/, 1000/*F_Quantity*/, 1000/*F_RemainingQuantity*/, 0/*F_Scope*/);
SET @couponID1 = last_insert_id();
-- �ۿ�ȯ1
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0/*F_Status*/, 1/*F_Type*/, 0/*F_Bonus*/, 10.00/*F_LeastAmount*/, 0.00/*F_ReduceAmount*/, 0.9/*F_Discount*/, '�����Ż�ȯ'/*F_Title*/, 'Color010'/*F_Color*/, ''/*F_Description*/, 1/*F_PersonalLimit*/, 0/*F_WeekDayAvailable*/, '9:00:00'/*F_BeginTime*/, '12:00:00'/*F_EndTime*/, '1998/03/03 15:15:15'/*F_BeginDateTime*/, 
		'3333/03/20 15:15:15'/*F_EndDateTime*/, 1000/*F_Quantity*/, 1000/*F_RemainingQuantity*/, 0/*F_Scope*/);
SET @couponID2 = last_insert_id();
-- �ۿ�ȯ2
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0/*F_Status*/, 1/*F_Type*/, 0/*F_Bonus*/, 10.00/*F_LeastAmount*/, 0.00/*F_ReduceAmount*/, 0.9/*F_Discount*/, '�����Ż�ȯ'/*F_Title*/, 'Color010'/*F_Color*/, ''/*F_Description*/, 1/*F_PersonalLimit*/, 0/*F_WeekDayAvailable*/, '9:00:00'/*F_BeginTime*/, '12:00:00'/*F_EndTime*/, '1998/03/03 15:15:15'/*F_BeginDateTime*/, 
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
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = @iOldTotalRecord + 2, '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';
-- 
DELETE FROM t_coupon WHERE F_ID = @couponID1;
DELETE FROM t_coupon WHERE F_ID = @couponID2;
DELETE FROM t_coupon WHERE F_ID = @couponID3;



SELECT '-------------------- Case6:��iPosID=-2,bonus=0, F_Type = -1,����С�������󣬲�ѯ�����л���Ϊ0���Ż�ȯ(����δ��ʼ�ģ����������ڡ�����ɾ����) -------------------------' AS 'Case6';
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case6 Testing Result';
-- 
-- �½����Ż���Ϊ0���Ż�ȯ��1�Ż��ִ���0���Ż�ȯ���ڲ���
-- �Ż�ȯ1
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0/*F_Status*/, 0/*F_Type*/, 0/*F_Bonus*/, 1.00/*F_LeastAmount*/, 4.00/*F_ReduceAmount*/, 1/*F_Discount*/, '�����õ��Ż�ȯ'/*F_Title*/, 'Color010'/*F_Color*/, ''/*F_Description*/, 1/*F_PersonalLimit*/, 0/*F_WeekDayAvailable*/, '9:00:00'/*F_BeginTime*/, '12:00:00'/*F_EndTime*/, '1998/03/03 15:15:15'/*F_BeginDateTime*/, 
		'3333/03/20 15:15:15'/*F_EndDateTime*/, 1000/*F_Quantity*/, 1000/*F_RemainingQuantity*/, 0/*F_Scope*/);
SET @couponID1 = last_insert_id();
-- �Ż�ȯ2
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0/*F_Status*/, 0/*F_Type*/, 0/*F_Bonus*/, 1.00/*F_LeastAmount*/, 4.00/*F_ReduceAmount*/, 1/*F_Discount*/, '�����õ��Ż�ȯ'/*F_Title*/, 'Color010'/*F_Color*/, ''/*F_Description*/, 1/*F_PersonalLimit*/, 0/*F_WeekDayAvailable*/, '9:00:00'/*F_BeginTime*/, '12:00:00'/*F_EndTime*/, '1998/03/03 15:15:15'/*F_BeginDateTime*/, 
		'3333/03/20 15:15:15'/*F_EndDateTime*/, 1000/*F_Quantity*/, 1000/*F_RemainingQuantity*/, 0/*F_Scope*/);
SET @couponID2 = last_insert_id();
-- �Ż�ȯ3
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0/*F_Status*/, 1/*F_Type*/, 1/*F_Bonus*/, 10.00/*F_LeastAmount*/, 0.00/*F_ReduceAmount*/, 0.9/*F_Discount*/, '�����Ż�ȯ'/*F_Title*/, 'Color010'/*F_Color*/, ''/*F_Description*/, 1/*F_PersonalLimit*/, 0/*F_WeekDayAvailable*/, '9:00:00'/*F_BeginTime*/, '12:00:00'/*F_EndTime*/, '1998/03/03 15:15:15'/*F_BeginDateTime*/, 
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
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = @iOldTotalRecord + 2, '���Գɹ�', '����ʧ��') AS 'Case6 Testing Result';
-- 
DELETE FROM t_coupon WHERE F_ID = @couponID1;
DELETE FROM t_coupon WHERE F_ID = @couponID2;
DELETE FROM t_coupon WHERE F_ID = @couponID3;



SELECT '-------------------- Case7:��iPosID=-2,bonus����0, F_Type = -1,����С�������󣬲�ѯ�����л��ִ���0���Ż�ȯ(����δ��ʼ�ģ����������ڡ�����ɾ����) -------------------------' AS 'Case7';
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case7 Testing Result';
-- 
-- �½�1�Ż���Ϊ0���Ż�ȯ��2�Ż��ִ���0���Ż�ȯ���ڲ���
-- �Ż�ȯ1
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0/*F_Status*/, 0/*F_Type*/, 0/*F_Bonus*/, 1.00/*F_LeastAmount*/, 4.00/*F_ReduceAmount*/, 1/*F_Discount*/, '�����õ��Ż�ȯ'/*F_Title*/, 'Color010'/*F_Color*/, ''/*F_Description*/, 1/*F_PersonalLimit*/, 0/*F_WeekDayAvailable*/, '9:00:00'/*F_BeginTime*/, '12:00:00'/*F_EndTime*/, '1998/03/03 15:15:15'/*F_BeginDateTime*/, 
		'3333/03/20 15:15:15'/*F_EndDateTime*/, 1000/*F_Quantity*/, 1000/*F_RemainingQuantity*/, 0/*F_Scope*/);
SET @couponID1 = last_insert_id();
-- �Ż�ȯ2
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0/*F_Status*/, 0/*F_Type*/, 1/*F_Bonus*/, 1.00/*F_LeastAmount*/, 4.00/*F_ReduceAmount*/, 1/*F_Discount*/, '�����õ��Ż�ȯ'/*F_Title*/, 'Color010'/*F_Color*/, ''/*F_Description*/, 1/*F_PersonalLimit*/, 0/*F_WeekDayAvailable*/, '9:00:00'/*F_BeginTime*/, '12:00:00'/*F_EndTime*/, '1998/03/03 15:15:15'/*F_BeginDateTime*/, 
		'3333/03/20 15:15:15'/*F_EndDateTime*/, 1000/*F_Quantity*/, 1000/*F_RemainingQuantity*/, 0/*F_Scope*/);
SET @couponID2 = last_insert_id();
-- �Ż�ȯ3
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0/*F_Status*/, 1/*F_Type*/, 1/*F_Bonus*/, 10.00/*F_LeastAmount*/, 0.00/*F_ReduceAmount*/, 0.9/*F_Discount*/, '�����Ż�ȯ'/*F_Title*/, 'Color010'/*F_Color*/, ''/*F_Description*/, 1/*F_PersonalLimit*/, 0/*F_WeekDayAvailable*/, '9:00:00'/*F_BeginTime*/, '12:00:00'/*F_EndTime*/, '1998/03/03 15:15:15'/*F_BeginDateTime*/, 
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
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = @iOldTotalRecord + 2, '���Գɹ�', '����ʧ��') AS 'Case7 Testing Result';
-- 
DELETE FROM t_coupon WHERE F_ID = @couponID1;
DELETE FROM t_coupon WHERE F_ID = @couponID2;
DELETE FROM t_coupon WHERE F_ID = @couponID3;





SELECT '-------------------- Case8:��iPosID=-2,bonus=-1, F_Type = -1,����С�������󣬲�ѯ�����Ż�ȯ����δ��ʼ�ģ����������ڡ�����ɾ���� -------------------------' AS 'Case8';
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case8 Testing Result';
-- 
-- �½�2���Ż�ȯ��1�Ź��ڵģ�1����ɾ�����Ż�ȯ���ڲ���
-- �Ż�ȯ2
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0/*F_Status*/, 0/*F_Type*/, 1/*F_Bonus*/, 1.00/*F_LeastAmount*/, 4.00/*F_ReduceAmount*/, 1/*F_Discount*/, '�����õ��Ż�ȯ'/*F_Title*/, 'Color010'/*F_Color*/, ''/*F_Description*/, 1/*F_PersonalLimit*/, 0/*F_WeekDayAvailable*/, '9:00:00'/*F_BeginTime*/, '12:00:00'/*F_EndTime*/, '1998/03/03 15:15:15'/*F_BeginDateTime*/, 
		'2000/03/20 15:15:15'/*F_EndDateTime*/, 1000/*F_Quantity*/, 1000/*F_RemainingQuantity*/, 0/*F_Scope*/);
SET @couponID2 = last_insert_id();
-- �Ż�ȯ3
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (1/*F_Status*/, 1/*F_Type*/, 1/*F_Bonus*/, 10.00/*F_LeastAmount*/, 0.00/*F_ReduceAmount*/, 0.9/*F_Discount*/, '�����Ż�ȯ'/*F_Title*/, 'Color010'/*F_Color*/, ''/*F_Description*/, 1/*F_PersonalLimit*/, 0/*F_WeekDayAvailable*/, '9:00:00'/*F_BeginTime*/, '12:00:00'/*F_EndTime*/, '1998/03/03 15:15:15'/*F_BeginDateTime*/, 
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
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = @iOldTotalRecord, '���Գɹ�', '����ʧ��') AS 'Case8 Testing Result';
-- 
DELETE FROM t_coupon WHERE F_ID = @couponID2;
DELETE FROM t_coupon WHERE F_ID = @couponID3;



SELECT '-------------------- Case9:��iPosID=-2,bonus=-1, F_Type = -1,����С�������󣬲�ѯ�����Ż�ȯ����δ��ʼ�� -------------------------' AS 'Case8';
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case9 Testing Result';
-- 
-- �½�1���Ż�ȯ��1��δ��ʼ�����ڲ���

-- �Ż�ȯ1
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0/*F_Status*/, 0/*F_Type*/, 0/*F_Bonus*/, 1.00/*F_LeastAmount*/, 4.00/*F_ReduceAmount*/, 1/*F_Discount*/, '�����õ��Ż�ȯ'/*F_Title*/, 'Color010'/*F_Color*/, ''/*F_Description*/, 1/*F_PersonalLimit*/, 0/*F_WeekDayAvailable*/, '9:00:00'/*F_BeginTime*/, '12:00:00'/*F_EndTime*/, '3000/03/03 15:15:15'/*F_BeginDateTime*/, 
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
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = @iOldTotalRecord + 1, '���Գɹ�', '����ʧ��') AS 'Case9 Testing Result';
DELETE FROM t_coupon WHERE F_ID = @couponID1;