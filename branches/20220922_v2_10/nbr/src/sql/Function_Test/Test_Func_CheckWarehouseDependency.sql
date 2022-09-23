SELECT '++++++++++++++++++++++++++++++++++Test_Func_CheckWarehouseDependency.sql+++++++++++++++++++++++++++++++++++++++++';

SELECT '-------------------- Case1:û������������ɾ��-------------------------' AS 'Case1';
INSERT INTO t_warehouse (F_Name, F_Address, F_Status) 
VALUES ('�ֿ�994', 'ֲ��԰', 0);
SET @iID = LAST_INSERT_ID();

SET @sErrorMsg = '';
SELECT Func_CheckWarehouseDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

DELETE FROM t_warehouse WHERE F_ID = @iID;

SELECT '-------------------- Case2:�òֿ��ѱ���ⵥʹ�ã�����ɾ��-------------------------' AS 'Case2';
INSERT INTO t_warehouse (F_Name, F_Address, F_Status) 
VALUES ('�ֿ�994', 'ֲ��԰', 0);
SET @iID = LAST_INSERT_ID();

INSERT INTO T_Warehousing (F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_CreateDatetime)
VALUES (0, 3, @iID, 5, now());
SET @iID2 = LAST_INSERT_ID();

SET @sErrorMsg = '';
SELECT Func_CheckWarehouseDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '�òֿ��ѱ���ⵥʹ�ã�����ɾ��', '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

DELETE FROM T_Warehousing WHERE F_ID = @iID2;
DELETE FROM t_warehouse WHERE F_ID = @iID;


SELECT '-------------------- Case3:�òֿ��ѱ��̵㵥ʹ�ã�����ɾ��-------------------------' AS 'Case3';
INSERT INTO t_warehouse (F_Name, F_Address, F_Status) 
VALUES ('�ֿ�994', 'ֲ��԰', 0);
SET @iID = LAST_INSERT_ID();

INSERT INTO T_InventorySheet (F_ShopID,F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
VALUES (2,@iID,200,0,3,'2017-12-06','...........................zz');
SET @iID2 = LAST_INSERT_ID();

SET @sErrorMsg = '';
SELECT Func_CheckWarehouseDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '�òֿ��ѱ��̵㵥ʹ�ã�����ɾ��', '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';

DELETE FROM T_InventorySheet WHERE F_ID = @iID2;
DELETE FROM t_warehouse WHERE F_ID = @iID;