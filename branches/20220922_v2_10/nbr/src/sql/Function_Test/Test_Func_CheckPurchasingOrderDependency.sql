SELECT '++++++++++++++++++++++++++++++++++Test_Func_CheckPurchasingOrderDependency.sql+++++++++++++++++++++++++++++++++++++++++';

SELECT '-------------------- Case1:û������������ɾ��-------------------------' AS 'Case1';
INSERT INTO T_PurchasingOrder(F_Status, F_StaffID, F_ProviderID, F_ProviderName, F_CreateDatetime, F_ApproveDatetime, F_EndDatetime) 
VALUES(0, 1, 1, 'Ĭ�Ϲ�Ӧ��', now(), now(), now());
SET @iID = LAST_INSERT_ID();

SET @sErrorMsg = '';
SELECT Func_CheckPurchasingOrderDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

DELETE FROM T_PurchasingOrder WHERE F_ID = @iID;

SELECT '-------------------- Case2:�òɹ����ѱ���ⵥʹ�ã�����ɾ��-------------------------' AS 'Case2';
INSERT INTO T_PurchasingOrder(F_Status, F_StaffID, F_ProviderID, F_ProviderName, F_CreateDatetime, F_ApproveDatetime, F_EndDatetime) 
VALUES(0, 1, 1, 'Ĭ�Ϲ�Ӧ��', now(), now(), now());
SET @iID = LAST_INSERT_ID();

INSERT INTO T_Warehousing (F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_CreateDatetime, F_PurchasingOrderID)
VALUES (0, 3, 1, 5, now(), @iID);
SET @iID2 = LAST_INSERT_ID();

SET @sErrorMsg = '';
SELECT Func_CheckPurchasingOrderDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '�òɹ�������⣬������ɾ��', '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

DELETE FROM T_Warehousing WHERE F_ID = @iID2;
DELETE FROM T_PurchasingOrder WHERE F_ID = @iID;
