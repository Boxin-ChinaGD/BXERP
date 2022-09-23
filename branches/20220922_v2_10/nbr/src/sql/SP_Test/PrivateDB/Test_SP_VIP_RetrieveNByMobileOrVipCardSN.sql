SELECT '++++++++++++++++++Test_SP_VIP_RetrieveNByMobileOrVipCardSN.sql+++++++++++++++++++++++';

SELECT '-----------------CASE1: ����5λ�����ϵ��ֻ����뾫ȷ����vip------------------' AS 'CASE1';
-- case1:����5λ�����ϵ��ֻ����뾫ȷ����vip
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sVipCardSN = '';
SET @sMobile = '135456';
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_VIP_RetrieveNByMobileOrVipCardSN(@iErrorCode, @sErrorMsg, @sVipCardSN, @sMobile,@iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

SELECT '-----------------CASE2: ����5λ�����µ��ֻ����뾫ȷ����vip,û������------------------' AS 'CASE2';
-- case2:����5λ�����µ��ֻ����뾫ȷ����vip
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sVipCardSN = '';
SET @sMobile = '13545';
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_VIP_RetrieveNByMobileOrVipCardSN(@iErrorCode, @sErrorMsg, @sVipCardSN, @sMobile,@iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

SELECT '-----------------CASE3: ������ֵ�������л�Ա��Ϣ------------------' AS 'CASE3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sMobile = '';
SET @sVipCardSN = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_VIP_RetrieveNByMobileOrVipCardSN(@iErrorCode, @sErrorMsg, @sVipCardSN, @sMobile,@iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';

SELECT '-------------------- CASE5:�����ֻ����룬���Ҵ��ֻ�����Ļ�Ա -------------------------' AS 'Case5';
SET @sMobile = '15022587951';
INSERT INTO T_VIP (F_ICID, F_CardID, F_SN, F_Name, F_Mobile, F_Email, F_ConsumeTimes, F_ConsumeAmount,F_District, F_Category, F_Birthday, F_Bonus, F_LastConsumeDatetime)
VALUES ('123222456'/*F_ICID*/, 1/*F_CardID*/, '123455677'/*F_SN*/,'gig1gs'/*F_Name*/, @sMobile/*F_Mobile*/, '12324536@bx.vip'/*F_Email*/, 5/*F_ConsumeTimes*/,99.8/*F_ConsumeAmount*/, '����'/*F_District*/, 1/*F_Category*/,
'2017-08-06'/*F_Birthday*/, 0/*F_Bonus*/, '2017-08-08 23:59:10'/*F_LastConsumeDatetime*/);
SET @VIPID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sVipCardSN = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
-- 
CALL SP_VIP_RetrieveNByMobileOrVipCardSN(@iErrorCode, @sErrorMsg, @sVipCardSN, @sMobile,@iPageIndex,@iPageSize,@iTotalRecord);
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';
-- 
DELETE FROM t_vip WHERE F_ID = @VIPID;

SELECT '-------------------- CASE6:���ݲ����ڵ��ֻ����룬��������Ϊ�� -------------------------' AS 'Case6';
SET @sMobile = '15022587951';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sVipCardSN = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
-- 
CALL SP_VIP_RetrieveNByMobileOrVipCardSN(@iErrorCode, @sErrorMsg, @sVipCardSN, @sMobile,@iPageIndex,@iPageSize,@iTotalRecord);
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';

SELECT '-------------------- CASE7:���ݻ�Ա���ź��룬���Ҵ˿��ź���Ļ�Ա -------------------------' AS 'Case7';
INSERT INTO T_VIP (F_ICID, F_CardID, F_SN, F_Name, F_Mobile, F_Email, F_ConsumeTimes, F_ConsumeAmount,F_District, F_Category, F_Birthday, F_Bonus, F_LastConsumeDatetime)
VALUES ('1231222456'/*F_ICID*/, 1/*F_CardID*/, '12346516789'/*F_SN*/,'gi7ggs'/*F_Name*/, '1520710487645'/*F_Mobile*/, '123712456@bx.vip'/*F_Email*/, 5/*F_ConsumeTimes*/,99.8/*F_ConsumeAmount*/, '����'/*F_District*/, 1/*F_Category*/,
'2017-08-06'/*F_Birthday*/, 0/*F_Bonus*/, '2017-08-08 23:59:10'/*F_LastConsumeDatetime*/);
SET @iVipID = last_insert_id();
-- 
INSERT INTO t_vipcard (F_Title, F_BackgroundColor, F_ClearBonusDay, F_ClearBonusDatetime, F_CreateDatetime)
VALUES ('��Ա��', '255,255,255;255,255,255', 3650, NULL, now());
SET @iVipCardID = last_insert_id();
-- 
SET @sVipCardSN = '12342567879';
INSERT INTO T_VipCardCode (F_VipID, F_VipCardID, F_SN) VALUES (@iVipID, @iVipCardID, @sVipCardSN);
SET @VipCardCodeID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sMobile = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
-- 
CALL SP_VIP_RetrieveNByMobileOrVipCardSN(@iErrorCode, @sErrorMsg, @sVipCardSN, @sMobile,@iPageIndex,@iPageSize,@iTotalRecord);
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';
-- 

DELETE FROM T_VipCardCode WHERE F_ID = @VipCardCodeID;
DELETE FROM t_vipcard WHERE F_ID = @iVipCardID;
DELETE FROM t_vip WHERE F_ID = @iVipID;

SELECT '-------------------- CASE8:���ݲ����ڵĻ�Ա�����ź��룬��������Ϊ�� -------------------------' AS 'Case8';
SET @sVipCardSN = '12342567879';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sMobile = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
-- 
CALL SP_VIP_RetrieveNByMobileOrVipCardSN(@iErrorCode, @sErrorMsg, @sVipCardSN, @sMobile,@iPageIndex,@iPageSize,@iTotalRecord);
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';