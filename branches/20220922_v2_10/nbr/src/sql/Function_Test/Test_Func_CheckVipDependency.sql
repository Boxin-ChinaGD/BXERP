SELECT '++++++++++++++++++++++++++++++++++Test_Func_CheckVipDependency.sql+++++++++++++++++++++++++++++++++++++++++';

SELECT '-------------------- Case1:û������������ɾ��-------------------------' AS 'Case1';
INSERT INTO T_VIP (F_ICID,F_CardID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Birthday,F_Bonus,F_LastConsumeDatetime)
VALUES ('12322456',1,'giggs','1232456@bx.vip',5,99.8,'����',1,
'2017-08-06',44.9,'2017-08-08 23:59:10');
SET @iID = LAST_INSERT_ID();

SET @sErrorMsg = '';
SELECT Func_CheckVipDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

DELETE FROM T_VIP WHERE F_ID = @iID;

SELECT '-------------------- Case2:�û�Ա�����۵�����������ɾ��-------------------------' AS 'Case2';
INSERT INTO T_VIP (F_ICID,F_CardID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Birthday,F_Bonus,F_LastConsumeDatetime)
VALUES ('12322456',1,'giggs','1232456@bx.vip',5,99.8,'����',1,
'2017-08-06',44.9,'2017-08-08 23:59:10');
SET @iID = LAST_INSERT_ID();

INSERT INTO T_RetailTrade (F_ShopID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status, F_Remark, F_VipID)
VALUES (2,'LS2019090414230000011234', 11,2,'url=ashasouuuuunalskd','2017-8-10',5,2,'A123460',1,'˫��777', @iID);
SET @iID2 = last_insert_id();

SET @sErrorMsg = '';
SELECT Func_CheckVipDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '�û�Ա�����۵�����������ɾ��', '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

DELETE FROM T_RetailTrade WHERE F_ID = @iID2;
DELETE FROM T_VIP WHERE F_ID = @iID;

SELECT '-------------------- Case3:�û�Ա�л�Ա��������������ɾ��-------------------------' AS 'Case3';
INSERT INTO T_VIP (F_ICID,F_CardID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Birthday,F_Bonus,F_LastConsumeDatetime)
VALUES ('12322456',1,'giggs','1232456@bx.vip',5,99.8,'����',1,
'2017-08-06',44.9,'2017-08-08 23:59:10');
SET @iID = LAST_INSERT_ID();

INSERT INTO nbr.T_VIPPointHistory (F_VIP_ID, F_PointChanged, F_RetailTradeID, F_CreateDatetime)
VALUES (@iID, 1, 1, now());
SET @iID2 = last_insert_id();

SET @sErrorMsg = '';
SELECT Func_CheckVipDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '�û�Ա�л�Ա��������������ɾ��', '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';

DELETE FROM T_VIPPointHistory WHERE F_ID = @iID2;
DELETE FROM T_VIP WHERE F_ID = @iID;