SELECT '++++++++++++++++++ Test_SP_Vip_UpdateBonus ++++++++++++++++++++';

SET @iAmountUnit = (SELECT F_AmountUnit FROM t_bonusrule WHERE F_ID = 1);
SET @iIncreaseBonus = (SELECT F_IncreaseBonus FROM t_bonusrule WHERE F_ID = 1);

SELECT '-------------------- Case1:�������ӻ��� -------------------------' AS 'Case1';
INSERT INTO T_VIP (F_SN,F_ICID,F_CardID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Birthday,F_Bonus,F_LastConsumeDatetime,F_Mobile)
VALUES ('VIP999999','1234256',1,'giggs','1234526@bx.vip',5,99.8,'����',1,
'2017-08-06',0,'2017-08-08 23:59:10','12313212332');

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();
SET @iStaffID = 3;
SET @iAmount = 5000;
SET @iBonus = 0;
SET @sRemark = '';
SET @iManuallyAdded = 0;
SET @iIsIncreaseBonus = 1;

CALL SP_Vip_UpdateBonus(@iErrorCode, @sErrorMsg, @iID, @iStaffID, @iAmount, @iBonus, @sRemark, @iManuallyAdded, @iIsIncreaseBonus);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT @iAmount;
SELECT @iAmountUnit;
SELECT @iIncreaseBonus;
SELECT @iAmount / @iAmountUnit * @iIncreaseBonus;
SELECT 1 FROM t_VIP WHERE F_ID = @iID AND F_Bonus = @iAmount / @iAmountUnit * @iIncreaseBonus;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

-- ��֤������ʷ�Ƿ����
SELECT 1 FROM t_bonusconsumehistory WHERE F_VipID = @iID 
AND F_Bonus = @iAmount / @iAmountUnit * @iIncreaseBonus
AND F_AddedBonus = @iAmount / @iAmountUnit * @iIncreaseBonus;
SELECT IF(found_rows() = 1 , '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

DELETE FROM t_bonusconsumehistory WHERE F_VipID = @iID;
DELETE FROM t_vip WHERE F_ID = @iID;

SELECT '-------------------- Case2:���ӻ��ֳ������λ�ȡ�������� -------------------------' AS 'Case2';
INSERT INTO T_VIP (F_SN,F_ICID,F_CardID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Birthday,F_Bonus,F_LastConsumeDatetime,F_Mobile)
VALUES ('VIP999999','1234256',1,'giggs','1234526@bx.vip',5,99.8,'����',1,
'2017-08-06',0,'2017-08-08 23:59:10','12313212332');

SET @iMaxIncreaseBonus = 1000;
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();
SET @iStaffID = 3;
SET @iAmount = 5000000;
SET @iBonus = 0;
SET @sRemark = '';
SET @iManuallyAdded = 0;
SET @iIsIncreaseBonus = 1;

CALL SP_Vip_UpdateBonus(@iErrorCode, @sErrorMsg, @iID, @iStaffID, @iAmount, @iBonus, @sRemark, @iManuallyAdded, @iIsIncreaseBonus);
SELECT @sErrorMsg;
SELECT 1 FROM t_VIP WHERE F_ID = @iID AND F_Bonus = @iMaxIncreaseBonus;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

-- ��֤������ʷ�Ƿ����
SELECT 1 FROM t_bonusconsumehistory WHERE F_VipID = @iID 
AND F_Bonus = @iMaxIncreaseBonus
AND F_AddedBonus = @iMaxIncreaseBonus;
SELECT IF(found_rows() = 1 , '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

DELETE FROM t_bonusconsumehistory WHERE F_VipID = @iID;
DELETE FROM t_vip WHERE F_ID = @iID;

SELECT '-------------------- Case3:��ԱID������ -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = -1;
SET @iStaffID = 3;
SET @iAmount = 5000000;
SET @iBonus = 0;
SET @sRemark = '';
SET @iManuallyAdded = 0;
SET @iIsIncreaseBonus = 1;
CALL SP_Vip_UpdateBonus(@iErrorCode, @sErrorMsg, @iID, @iStaffID, @iAmount, @sRemark, @iBonus, @iManuallyAdded, @iIsIncreaseBonus);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 2 AND @sErrorMsg = '��Ա������', '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';


SELECT '-------------------- Case4:����ʹ�û��ֵֿ� -------------------------' AS 'Case4';
SET @iInitialBonus = 100;
INSERT INTO T_VIP (F_SN,F_ICID,F_CardID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Birthday,F_Bonus,F_LastConsumeDatetime,F_Mobile)
VALUES ('VIP999999','1234256',1,'giggs','1234526@bx.vip',5,99.8,'����',1,
'2017-08-06',@iInitialBonus,'2017-08-08 23:59:10','12313212332');

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();
SET @iStaffID = 3;
SET @iAmount = 0;
SET @iBonus = -50;
SET @sRemark = '';
SET @iManuallyAdded = 0;
SET @iIsIncreaseBonus = 0;

CALL SP_Vip_UpdateBonus(@iErrorCode, @sErrorMsg, @iID, @iStaffID, @iAmount, @iBonus, @sRemark, @iManuallyAdded, @iIsIncreaseBonus);

SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT 1 FROM t_VIP WHERE F_ID = @iID AND F_Bonus = @iBonus + @iInitialBonus;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';

-- ��֤������ʷ�Ƿ����
SELECT 1 FROM t_bonusconsumehistory WHERE F_VipID = @iID 
AND F_Bonus = @iInitialBonus + @iBonus
AND F_AddedBonus = @iBonus;
SELECT IF(found_rows() = 1 , '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';

DELETE FROM t_bonusconsumehistory WHERE F_VipID = @iID;
DELETE FROM t_vip WHERE F_ID = @iID;

SELECT '-------------------- Case5:ʹ�õĻ��ֳ�����Աԭ���Ļ��� -------------------------' AS 'Case5';
SET @iInitialBonus = 0;
INSERT INTO T_VIP (F_SN,F_ICID,F_CardID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Birthday,F_Bonus,F_LastConsumeDatetime,F_Mobile)
VALUES ('VIP999999','1234256',1,'giggs','1234526@bx.vip',5,99.8,'����',1,
'2017-08-06',0,'2017-08-08 23:59:10','12313212332');

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();
SET @iStaffID = 3;
SET @iAmount = 0;
SET @iBonus = -50;
SET @sRemark = '';
SET @iManuallyAdded = 0;
SET @iIsIncreaseBonus = 0;

CALL SP_Vip_UpdateBonus(@iErrorCode, @sErrorMsg, @iID, @iStaffID, @iAmount, @iBonus, @sRemark, @iManuallyAdded, @iIsIncreaseBonus);

SELECT @sErrorMsg;
SELECT 1 FROM t_VIP WHERE F_ID = @iID AND F_Bonus = @iInitialBonus;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';

DELETE FROM t_vip WHERE F_ID = @iID;

SELECT '-------------------- Case6:�ϰ���л�Ա�����޸� -------------------------' AS 'Case6';
SET @iInitialBonus= 0;
INSERT INTO T_VIP (F_SN,F_CardID,F_ICID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Birthday,F_Bonus,F_LastConsumeDatetime,F_Mobile, F_Sex, F_Logo, F_Remark)
VALUES ('VIP999999',1,'1234256','giggs','1234526@bx.vip',5,99.8,'����',1,
'2017-08-06',@iInitialBonus,'2017-08-08 23:59:10','12313212332', 1, '123456123', '');

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();
SET @iStaffID = 3;
SET @iAmount = 0;
SET @iBonus = 500;
SET @sRemark = '';
SET @iManuallyAdded = 1;
SET @iIsIncreaseBonus = 0;

CALL SP_Vip_UpdateBonus(@iErrorCode, @sErrorMsg, @iID, @iStaffID, @iAmount, @iBonus, @sRemark, @iManuallyAdded, @iIsIncreaseBonus);

SELECT @sErrorMsg;
SELECT 1 FROM t_VIP WHERE F_ID = @iID AND F_Bonus = @iBonus;
SELECT IF(found_rows() = 1, '���Գɹ�', '����ʧ��') AS 'Case6 Testing Result';

-- ��֤������ʷ�Ƿ����
SELECT 1 FROM t_bonusconsumehistory WHERE F_VipID = @iID 
AND F_Bonus = @iBonus
AND F_AddedBonus = @iBonus - @iInitialBonus;
SELECT IF(found_rows() = 1 , '���Գɹ�', '����ʧ��') AS 'Case6 Testing Result';

DELETE FROM t_bonusconsumehistory WHERE F_VipID = @iID;
DELETE FROM t_vip WHERE F_ID = @iID;

SELECT '-------------------- Case7:�ϰ��޸ĵĻ�Ա����С��0 -------------------------' AS 'Case7';
INSERT INTO T_VIP (F_SN,F_ICID,F_CardID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Birthday,F_Bonus,F_LastConsumeDatetime,F_Mobile)
VALUES ('VIP999999','1234256',1,'giggs','1234526@bx.vip',5,99.8,'����',1,
'2017-08-06',0,'2017-08-08 23:59:10','12313212332');

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();
SET @iStaffID = 3;
SET @iAmount = 0;
SET @iBonus = -1;
SET @sRemark = '';
SET @iManuallyAdded = 1;
SET @iIsIncreaseBonus = 0;

CALL SP_Vip_UpdateBonus(@iErrorCode, @sErrorMsg, @iID, @iStaffID, @iAmount, @iBonus, @sRemark, @iManuallyAdded, @iIsIncreaseBonus);

SELECT @sErrorMsg;

SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '�޸ĺ�Ļ��ֲ���С��0', '���Գɹ�', '����ʧ��') AS 'Case7 Testing Result';

DELETE FROM t_bonusconsumehistory WHERE F_VipID = @iID;
DELETE FROM t_vip WHERE F_ID = @iID;

SELECT '-------------------- Case8:�������ӻ��֣�������Ϊ0���������ֲ��� -------------------------' AS 'Case1';
INSERT INTO T_VIP (F_SN,F_ICID,F_CardID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Birthday,F_Bonus,F_LastConsumeDatetime,F_Mobile)
VALUES ('VIP999999','1234256',1,'giggs','1234526@bx.vip',5,99.8,'����',1,
'2017-08-06',0,'2017-08-08 23:59:10','12313212332');

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();
SET @iStaffID = 3;
SET @iAmount = 0;
SET @iBonus = 0;
SET @sRemark = '';
SET @iManuallyAdded = 0;
SET @iIsIncreaseBonus = 1;

CALL SP_Vip_UpdateBonus(@iErrorCode, @sErrorMsg, @iID, @iStaffID, @iAmount, @iBonus, @sRemark, @iManuallyAdded, @iIsIncreaseBonus);
SELECT 1 FROM t_VIP WHERE F_ID = @iID AND F_Bonus = 0;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

-- ��֤������ʷ�Ƿ����
SELECT 1 FROM t_bonusconsumehistory WHERE F_VipID = @iID;
SELECT IF(found_rows() = 0 , '���Գɹ�', '����ʧ��') AS 'Case8 Testing Result';

DELETE FROM t_bonusconsumehistory WHERE F_VipID = @iID;
DELETE FROM t_vip WHERE F_ID = @iID;