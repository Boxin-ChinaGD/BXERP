SELECT '++++++++++++++++++ Test_SP_Vip_ResetBonus.sql ++++++++++++++++++++';

SET @dtClearBonusDatetime = NULL;
SET @iClearBonusDay = 3650;

SELECT '-------------------- Case1:���ݹ̶��������л������㣬��Ա����ʱ�䵽����δ�ﵽ���������������� -------------------------' AS 'Case1';
SET @dtCreateDatetime = now();
SET @iBonus = 50;
-- 
INSERT INTO T_VIP (F_ICID,F_CardID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile, F_CreateDatetime)
VALUES ('1232456',1,'giggs','1232456@bx.vip',5,99.8,'����',1,
'2017-08-06',@iBonus,'2017-08-08 23:59:10','12345612312', @dtCreateDatetime);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();
-- 
CALL SP_Vip_ResetBonus(@iErrorCode, @sErrorMsg);
-- 
SELECT @sErrorMsg;
SELECT 1 FROM t_VIP WHERE F_ID = @iID AND F_Bonus = @iBonus;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

DELETE FROM t_vip WHERE F_ID = @iID;

SELECT '-------------------- Case2:���ݹ̶��������л������㣬��Ա����ʱ�䵽������������������������� -------------------------' AS 'Case2';
UPDATE t_vipcard SET 
	F_ClearBonusDatetime = NULL,
	F_ClearBonusDay = 3
WHERE F_ID = 1;
-- 
SET @dtCreateDatetime = date_sub(now(),interval 3 day); -- ��ǰ��3��
SET @iBonus = 50;
-- 
INSERT INTO T_VIP (F_ICID,F_CardID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile, F_CreateDatetime)
VALUES ('1232456',1,'giggs','1232456@bx.vip',5,99.8,'����',1,
'2017-08-06',@iBonus,'2017-08-08 23:59:10','12345612312', @dtCreateDatetime);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();
SELECT * FROM t_VIP WHERE F_ID = @iID ;
-- 
CALL SP_Vip_ResetBonus(@iErrorCode, @sErrorMsg);
-- 
SELECT @sErrorMsg;

SELECT 1 FROM t_VIP WHERE F_ID = @iID AND F_Bonus = 0;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

DELETE FROM t_vip WHERE F_ID = @iID;
-- ��ԭ�����������
UPDATE t_vipcard
SET F_ClearBonusDatetime = @dtClearBonusDatetime,
F_ClearBonusDay = @iClearBonusDay
WHERE F_ID = 1;

SELECT '-------------------- Case3:���ݹ̶��������л������㣬��Ա����ʱ�䵽���쳬���������������ܱ������������� -------------------------' AS 'Case3';
UPDATE t_vipcard SET 
	F_ClearBonusDatetime = NULL,
	F_ClearBonusDay = 3
WHERE F_ID = 1;
--
SET @dtCreateDatetime = now() - 07000000; -- ��ǰ��7��
SET @iBonus = 50;
--
INSERT INTO T_VIP (F_ICID,F_CardID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile, F_CreateDatetime)
VALUES ('1232456',1,'giggs','1232456@bx.vip',5,99.8,'����',1,
'2017-08-06',@iBonus,'2017-08-08 23:59:10','12345612312', @dtCreateDatetime);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();
-- 
CALL SP_Vip_ResetBonus(@iErrorCode, @sErrorMsg);
-- 
SELECT @sErrorMsg;
SELECT 1 FROM t_VIP WHERE F_ID = @iID AND F_Bonus = @iBonus;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

DELETE FROM t_vip WHERE F_ID = @iID;
-- ��ԭ�����������
UPDATE t_vipcard
SET F_ClearBonusDatetime = @dtClearBonusDatetime,
F_ClearBonusDay = @iClearBonusDay
WHERE F_ID = 1;

SELECT '-------------------- Case4:���ݹ̶����ڽ��л������㣬��Ա���괴�����ﵽ�������ڣ��������� -------------------------' AS 'Case4';
-- �޸Ļ�Ա�������������Ϊ���ݹ̶���������
UPDATE t_vipcard
SET F_ClearBonusDatetime = now(),
F_ClearBonusDay = NULL
WHERE F_ID = 1;

SET @dtCreateDatetime = now();
SET @iBonus = 50;
INSERT INTO T_VIP (F_ICID,F_CardID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile, F_CreateDatetime)
VALUES ('1232456',1,'giggs','1232456@bx.vip',5,99.8,'����',1,
'2017-08-06',@iBonus,'2017-08-08 23:59:10','12345612312', @dtCreateDatetime);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();

CALL SP_Vip_ResetBonus(@iErrorCode, @sErrorMsg);

SELECT @sErrorMsg;
SELECT 1 FROM t_VIP WHERE F_ID = @iID AND F_Bonus = 0;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';

DELETE FROM t_vip WHERE F_ID = @iID;

-- ��ԭ�����������
UPDATE t_vipcard
SET F_ClearBonusDatetime = @dtClearBonusDatetime,
F_ClearBonusDay = @iClearBonusDay
WHERE F_ID = 1;

SELECT '-------------------- Case5:���ݹ̶����ڽ��л������㣬��Ա��һ�괴�����ﵽ�������ڣ��������� -------------------------' AS 'Case5';
-- �޸Ļ�Ա�������������Ϊ���ݹ̶���������
UPDATE t_vipcard
SET F_ClearBonusDatetime = now() - 010000000000, -- ��ǰ��1��
F_ClearBonusDay = NULL
WHERE F_ID = 1;

SET @dtCreateDatetime = now();
SET @iBonus = 50;
INSERT INTO T_VIP (F_ICID,F_CardID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile, F_CreateDatetime)
VALUES ('1232456',1,'giggs','1232456@bx.vip',5,99.8,'����',1,
'2017-08-06',@iBonus,'2017-08-08 23:59:10','12345612312', @dtCreateDatetime);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();

CALL SP_Vip_ResetBonus(@iErrorCode, @sErrorMsg);

SELECT @sErrorMsg;
SELECT 1 FROM t_VIP WHERE F_ID = @iID AND F_Bonus = 0;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';

DELETE FROM t_vip WHERE F_ID = @iID;

-- ��ԭ�����������
UPDATE t_vipcard
SET F_ClearBonusDatetime = @dtClearBonusDatetime,
F_ClearBonusDay = @iClearBonusDay
WHERE F_ID = 1;

SELECT '-------------------- Case6:��Ա��������������쳣 -------------------------' AS 'Case6';
-- �޸Ļ�Ա�������������Ϊ���ݹ̶���������
UPDATE t_vipcard
SET F_ClearBonusDatetime = NULL,
F_ClearBonusDay = NULL
WHERE F_ID = 1;

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();

CALL SP_Vip_ResetBonus(@iErrorCode, @sErrorMsg);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '��Ա��������������쳣', '���Գɹ�', '����ʧ��') AS 'Case6 Testing Result';

DELETE FROM t_vip WHERE F_ID = @iID;

-- ��ԭ�����������
UPDATE t_vipcard
SET F_ClearBonusDatetime = @dtClearBonusDatetime,
F_ClearBonusDay = @iClearBonusDay
WHERE F_ID = 1;