SELECT '++++++++++++++++++++++++++++++++++Test_Func_CheckPromotionDependency.sql+++++++++++++++++++++++++++++++++++++++++';

SELECT '-------------------- Case1:û������������ɾ��-------------------------' AS 'Case1';
INSERT INTO nbr.T_Promotion (F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES ('aa', 0, 0, now(), now(), 10, 1, 8, 0, 1, now(), now());
SET @iID = LAST_INSERT_ID();

SET @sErrorMsg = '';
SELECT Func_CheckPromotionDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

DELETE FROM T_Promotion WHERE F_ID = @iID;

SELECT '-------------------- Case2:�ô����Ѿ����ɹ�������Ϣ������ɾ��-------------------------' AS 'Case2';
INSERT INTO nbr.T_Promotion (F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES ('aa', 0, 0, now(), now(), 10, 1, 8, 0, 1, now(), now());
SET @iID = LAST_INSERT_ID();

INSERT INTO nbr.t_retailtradepromotingflow (F_RetailTradePromotingID, F_PromotionID, F_ProcessFlow, F_CreateDatetime)
VALUES (1, @iID, 'aa', now());
SET @iID2 = LAST_INSERT_ID();

SET @sErrorMsg = '';
SELECT Func_CheckPromotionDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '�ô����Ѿ����ɹ�������Ϣ������ɾ��', '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

DELETE FROM t_retailtradepromotingflow WHERE F_ID = @iID2;
DELETE FROM T_Promotion WHERE F_ID = @iID;
