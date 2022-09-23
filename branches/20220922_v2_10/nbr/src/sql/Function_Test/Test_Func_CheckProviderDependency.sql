SELECT '++++++++++++++++++++++++++++++++++Test_Func_CheckProviderDependency.sql+++++++++++++++++++++++++++++++++++++++++';

SELECT '-------------------- Case1:��Ӧ���Ѿ�������Ʒ������ɾ��-------------------------' AS 'Case1';
INSERT INTO t_provider ( F_Name, F_DistrictID, F_Address, F_ContactName, F_Mobile)
VALUES  ('��Ӧ��007', 1, 'china', 'kkk', '13726498500');
SET @iID = LAST_INSERT_ID();

INSERT INTO t_providercommodity (F_CommodityID, F_ProviderID)
VALUES (1, @iID);

SET @sErrorMsg = '';
SELECT Func_CheckProviderDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '��Ӧ���Ѿ�������Ʒ������ɾ��', '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

DELETE FROM t_providercommodity WHERE F_ProviderID = @iID;
DELETE FROM t_provider WHERE F_ID = @iID;

SELECT '-------------------- Case2:��Ӧ���ѱ��˻������ã�����ɾ��-------------------------' AS 'Case2';
INSERT INTO t_provider (F_Name, F_DistrictID, F_Address, F_ContactName, F_Mobile)
VALUES  ('��Ӧ��777', 1, 'china', 'kkk', '13532644012');
SET @iID = LAST_INSERT_ID();

INSERT INTO t_returncommoditysheet (F_StaffID, F_ProviderID, F_Status, F_ShopID)
VALUES (1, @iID, 1,2);

SET @sErrorMsg = '';
SELECT Func_CheckProviderDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '��Ӧ���ѱ��˻������ã�����ɾ��', '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

DELETE FROM t_returncommoditysheet WHERE F_ProviderID = @iID;
DELETE FROM t_provider WHERE F_ID = @iID;

SELECT '-------------------- Case3:��Ӧ���ѱ��ɹ��������ã�����ɾ��-------------------------' AS 'Case3';
INSERT INTO t_provider (F_Name, F_DistrictID, F_Address, F_ContactName, F_Mobile)
VALUES  ('��Ӧ��770', 1, 'china', 'kkk', '13532644011');
SET @iID = LAST_INSERT_ID();

INSERT INTO t_purchasingorder (F_Status, F_StaffID, F_ProviderID, F_ProviderName, F_ApproverID, F_Remark)
VALUES (1, 1, @iID, 'Ĭ�Ϲ�Ӧ��', NULL, '�����л���');

SET @sErrorMsg = '';
SELECT Func_CheckProviderDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '��Ӧ���ѱ��ɹ��������ã�����ɾ��', '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';

DELETE FROM t_purchasingorder WHERE F_ProviderID = @iID;
DELETE FROM t_provider WHERE F_ID = @iID;

SELECT '-------------------- Case4:��Ӧ���ѱ�������ã�����ɾ��-------------------------' AS 'Case4';
INSERT INTO t_provider (F_Name, F_DistrictID, F_Address, F_ContactName, F_Mobile)
VALUES  ('��Ӧ��774', 1, 'china', 'kkk', '13532644016');
SET @iID = LAST_INSERT_ID();

INSERT INTO t_warehousing (F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_PurchasingOrderID)
VALUES (0, @iID, 1, 3, NULL, 1);

SET @sErrorMsg = '';
SELECT Func_CheckProviderDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '��Ӧ���ѱ���ⵥ���ã�����ɾ��', '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';

DELETE FROM t_warehousing WHERE F_ProviderID = @iID;
DELETE FROM t_provider WHERE F_ID = @iID;

SELECT '-------------------- Case5����Ӧ��û�б�����-------------------------' AS 'Case5';
INSERT INTO t_provider (F_Name, F_DistrictID, F_Address, F_ContactName, F_Mobile)
VALUES  ('��Ӧ��776', 1, 'china', 'kkk', '13532644016');
SET @iID = LAST_INSERT_ID();

SET @sErrorMsg = '';
SELECT Func_CheckProviderDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';

DELETE FROM t_provider WHERE F_ID = @iID;