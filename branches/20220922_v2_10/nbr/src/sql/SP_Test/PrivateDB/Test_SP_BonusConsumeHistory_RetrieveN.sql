SELECT '++++++++++++++++++ Test_SP_BonusConsumeHistory_RetrieveN.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:��ѯ���� -------------------------' AS 'Case1';
INSERT INTO t_bonusconsumehistory (F_VipID, F_StaffID, F_Bonus, F_AddedBonus, F_Remark)
VALUES (1, null, 10, 10, '**********');
SET @iID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iVip = -1;
SET @sVipMobile = '';
SET @sVipName = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_BonusConsumeHistory_RetrieveN(@iErrorCode, @sErrorMsg, @iVip, @sVipMobile, @sVipName, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

DELETE FROM t_bonusconsumehistory WHERE F_ID = @iID;

SELECT '-------------------- Case2:���ݻ�ԱID���в�ѯ -------------------------' AS 'Case2';
INSERT INTO t_bonusconsumehistory (F_VipID, F_StaffID, F_Bonus, F_AddedBonus, F_Remark)
VALUES (1, null, 10, 10, '**********');
SET @iID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iVip = 1;
SET @sVipMobile = '';
SET @sVipName = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_BonusConsumeHistory_RetrieveN(@iErrorCode, @sErrorMsg, @iVip, @sVipMobile, @sVipName, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

DELETE FROM t_bonusconsumehistory WHERE F_ID = @iID;

SELECT '-------------------- Case3:��ԱID������ -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iVip = 0;
SET @sVipMobile = '';
SET @sVipName = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_BonusConsumeHistory_RetrieveN(@iErrorCode, @sErrorMsg, @iVip, @sVipMobile, @sVipName, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 2 AND @sErrorMsg = '��ԱID������', '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';


SELECT '-------------------- Case4:��vipID = -1, mobile = ��Ա�ֻ���,sName = '',����ݻ�Ա�ֻ��Ų�ѯ -------------------------' AS 'Case4';
-- 
SET @vipMobile = '12020040316';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iVip = -1;
SET @sVipMobile = '';
SET @sVipName = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iOldTotalRecord = 0;

CALL SP_BonusConsumeHistory_RetrieveN(@iErrorCode, @sErrorMsg, @iVip, @sVipMobile, @sVipName, @iPageIndex, @iPageSize, @iOldTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';
-- 
-- �½�Vip
INSERT INTO T_VIP (F_SN,F_CardID,F_ICID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile)
VALUES ('VIP202000',1,'202000403807016031','������ʷ','jfls666@bx.vip',7,100.8,'����',5,
'2017-12-06',0,'2017-02-9 23:15:10',@vipMobile);
SET @vipID = last_insert_id();
-- �½�bonusconsumehistory
INSERT INTO t_bonusconsumehistory (F_VipID, F_StaffID, F_Bonus, F_AddedBonus, F_Remark)
VALUES (@vipID/*F_VipID*/, 1/*F_StaffID*/, 1/*F_Bonus*/, 1/*F_AddedBonus*/, 1/*F_Remark*/);
SET @bonusconsumehistoryID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iVip = -1;
SET @sVipMobile = @vipMobile;
SET @sVipName = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_BonusConsumeHistory_RetrieveN(@iErrorCode, @sErrorMsg, @iVip, @sVipMobile, @sVipName, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @sErrorMsg = '' AND @iTotalRecord = @iOldTotalRecord + 1, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';
-- 
DELETE FROM t_bonusconsumehistory WHERE F_ID = @bonusconsumehistoryID;
DELETE FROM t_vip WHERE F_ID = @vipID;





SELECT '-------------------- Case5:��vipID = -1, mobile = '',sName��Ϊ��,����ݻ�Ա�ǳ�ģ����ѯ -------------------------' AS 'Case5';
-- 
SET @sVipName = '����';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iVip = -1;
SET @sVipMobile = '';
SET @sVipName = @sVipName;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iOldTotalRecord = 0;

CALL SP_BonusConsumeHistory_RetrieveN(@iErrorCode, @sErrorMsg, @iVip, @sVipMobile, @sVipName, @iPageIndex, @iPageSize, @iOldTotalRecord);
SELECT @iOldTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';
-- 
-- �½�vip1, vip2, vip3, vip1��vip2ƥ��ؼ��֣�vip3��ƥ��
INSERT INTO T_VIP (F_SN,F_CardID,F_ICID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile)
VALUES ('VIP202001',1,'202000403807016031','������','jfls666@bx.vip',7,100.8,'����',5,
'2017-12-06',0,'2017-02-9 23:15:10','12020040317');
SET @vipID1 = last_insert_id();
-- 
INSERT INTO T_VIP (F_SN,F_CardID,F_ICID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile)
VALUES ('VIP202002',1,'202000403807016032','������','jfls667@bx.vip',7,100.8,'����',5,
'2017-12-06',0,'2017-02-9 23:15:10', '12020040318');
SET @vipID2 = last_insert_id();
-- 
INSERT INTO T_VIP (F_SN,F_CardID,F_ICID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile)
VALUES ('VIP202003',1,'202000403807016033','��˽�','jfls668@bx.vip',7,100.8,'����',5,
'2017-12-06',0,'2017-02-9 23:15:10','12020040319');
SET @vipID3 = last_insert_id();
-- 
-- �½�bonusconsumehistory
INSERT INTO t_bonusconsumehistory (F_VipID, F_StaffID, F_Bonus, F_AddedBonus, F_Remark)
VALUES (@vipID1/*F_VipID*/, 1/*F_StaffID*/, 1/*F_Bonus*/, 1/*F_AddedBonus*/, 1/*F_Remark*/);
SET @bonusconsumehistoryID1 = last_insert_id();
-- 
-- �½�bonusconsumehistory
INSERT INTO t_bonusconsumehistory (F_VipID, F_StaffID, F_Bonus, F_AddedBonus, F_Remark)
VALUES (@vipID2/*F_VipID*/, 1/*F_StaffID*/, 1/*F_Bonus*/, 1/*F_AddedBonus*/, 1/*F_Remark*/);
SET @bonusconsumehistoryID2 = last_insert_id();
-- 
-- �½�bonusconsumehistory
INSERT INTO t_bonusconsumehistory (F_VipID, F_StaffID, F_Bonus, F_AddedBonus, F_Remark)
VALUES (@vipID3/*F_VipID*/, 1/*F_StaffID*/, 1/*F_Bonus*/, 1/*F_AddedBonus*/, 1/*F_Remark*/);
SET @bonusconsumehistoryID3 = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iVip = -1;
SET @sVipMobile = '';
SET @sVipName = @sVipName;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_BonusConsumeHistory_RetrieveN(@iErrorCode, @sErrorMsg, @iVip, @sVipMobile, @sVipName, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @sErrorMsg = '' AND @iTotalRecord = @iOldTotalRecord + 2, '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';
-- 
DELETE FROM t_bonusconsumehistory WHERE F_ID = @bonusconsumehistoryID1;
DELETE FROM t_bonusconsumehistory WHERE F_ID = @bonusconsumehistoryID2;
DELETE FROM t_bonusconsumehistory WHERE F_ID = @bonusconsumehistoryID3;
DELETE FROM t_vip WHERE F_ID = @vipID1;
DELETE FROM t_vip WHERE F_ID = @vipID2;
DELETE FROM t_vip WHERE F_ID = @vipID3;




SELECT '-------------------- Case6:��vipID������-1, mobile = '',sName = '',����ݻ�ԱID��ѯ -------------------------' AS 'Case6';
-- 
-- �½�Vip
INSERT INTO T_VIP (F_SN,F_CardID,F_ICID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile)
VALUES ('VIP202000',1,'202000403807016031','������ʷ','jfls666@bx.vip',7,100.8,'����',5,
'2017-12-06',0,'2017-02-9 23:15:10','12020040316');
SET @vipID = last_insert_id();
-- �½�bonusconsumehistory
INSERT INTO t_bonusconsumehistory (F_VipID, F_StaffID, F_Bonus, F_AddedBonus, F_Remark)
VALUES (@vipID/*F_VipID*/, 0/*F_StaffID*/, 1/*F_Bonus*/, 1/*F_AddedBonus*/, 1/*F_Remark*/);
SET @bonusconsumehistoryID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iVip = @vipID;
SET @sVipMobile = '';
SET @sVipName = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_BonusConsumeHistory_RetrieveN(@iErrorCode, @sErrorMsg, @iVip, @sVipMobile, @sVipName, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SET @record = 0;
SELECT count(*) INTO @record FROM t_bonusconsumehistory WHERE F_VipID = @vipID;
SELECT IF(@iErrorCode = 0 AND @sErrorMsg = '' AND @iTotalRecord = 1 AND @record = 1, '���Գɹ�', '����ʧ��') AS 'Case6 Testing Result';
-- 
DELETE FROM t_bonusconsumehistory WHERE F_ID = @bonusconsumehistoryID;
DELETE FROM t_vip WHERE F_ID = @vipID;