SELECT '++++++++++++++++++ Test_SP_Inventory_UpdateSheet.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: ״̬Ϊ0ʱ,�޸��̵��ܽ� -------------------------' AS 'Case1';

INSERT INTO T_InventorySheet (F_ShopID,F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
VALUES (2,1,200,0,5,'2017-08-06','...........................');
SET @iInventorySheetID = last_insert_id();

INSERT INTO t_inventorycommodity (F_InventorySheetID, F_CommodityID, F_CommodityName, F_Specification, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem, F_CreateDatetime)
VALUES (@iInventorySheetID, 1, '��Ƭ', '��', 1, 1, -1, -1, now());
SET @iInventorycommodityID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sRemark = 'XXXX';

CALL SP_Inventory_UpdateSheet(@iErrorCode, @sErrorMsg, @iInventorySheetID, @sRemark);

SELECT @sErrorMsg;
SELECT 1 FROM t_inventorysheet WHERE F_ID = @iInventorySheetID AND F_Remark = @sRemark;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'CASE1 Testing Result';

DELETE FROM t_inventorycommodity WHERE F_ID = @iInventorycommodityID;
DELETE FROM T_InventorySheet WHERE F_ID = @iInventorySheetID;

SELECT '-------------------- Case2: ״̬Ϊ1ʱ��ֻ�����޸��̵��ܽ� -------------------------' AS 'Case2';

INSERT INTO T_InventorySheet (F_ShopID,F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
VALUES (2,1,200,1,5,'2017-08-06','...........................');
SET @iInventorySheetID = last_insert_id();

INSERT INTO t_inventorycommodity (F_InventorySheetID, F_CommodityID, F_CommodityName, F_Specification, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem, F_CreateDatetime)
VALUES (@iInventorySheetID, 1, '��Ƭ', '��', 1, 1, -1, -1, now());
SET @iInventorycommodityID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sRemark = 'XXXX';

CALL SP_Inventory_UpdateSheet(@iErrorCode, @sErrorMsg, @iInventorySheetID, @sRemark);

SELECT @sErrorMsg;
SELECT 1 FROM t_inventorysheet WHERE F_ID = @iInventorySheetID AND F_Remark = @sRemark;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'CASE2 Testing Result';

DELETE FROM t_inventorycommodity WHERE F_ID = @iInventorycommodityID;
DELETE FROM T_InventorySheet WHERE F_ID = @iInventorySheetID;

SELECT '-------------------- Case3: ״̬Ϊ2ʱ���޸�ʧ�ܣ�������Ϊ7 -------------------------' AS 'Case3';

INSERT INTO T_InventorySheet (F_ShopID,F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
VALUES (2,1,200,2,5,'2017-08-06','...........................');
SET @iInventorySheetID = last_insert_id();

INSERT INTO t_inventorycommodity (F_InventorySheetID, F_CommodityID, F_CommodityName, F_Specification, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem, F_CreateDatetime)
VALUES (@iInventorySheetID, 1, '��Ƭ', '��', 1, 1, -1, -1, now());
SET @iInventorycommodityID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sRemark = 'XXXX';

CALL SP_Inventory_UpdateSheet(@iErrorCode, @sErrorMsg, @iInventorySheetID, @sRemark);

SELECT @sErrorMsg;
SELECT 1 FROM t_inventorysheet WHERE F_ID = @iInventorySheetID AND F_Remark = @sRemark;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '�̵㵥Ϊ����˻�����ɾ��ʱ���޸��̵��ܽ�ʧ��', '���Գɹ�', '����ʧ��') AS 'CASE3 Testing Result';

DELETE FROM t_inventorycommodity WHERE F_ID = @iInventorycommodityID;
DELETE FROM T_InventorySheet WHERE F_ID = @iInventorySheetID;

SELECT '-------------------- Case4: ״̬Ϊ3ʱ���޸�ʧ�ܣ�������Ϊ7 -------------------------' AS 'Case4';

INSERT INTO T_InventorySheet (F_ShopID,F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
VALUES (2,1,200,3,5,'2017-08-06','...........................');
SET @iInventorySheetID = last_insert_id();

INSERT INTO t_inventorycommodity (F_InventorySheetID, F_CommodityID, F_CommodityName, F_Specification, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem, F_CreateDatetime)
VALUES (@iInventorySheetID, 1, '��Ƭ', '��', 1, 1, -1, -1, now());
SET @iInventorycommodityID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sRemark = 'XXXX';

CALL SP_Inventory_UpdateSheet(@iErrorCode, @sErrorMsg, @iInventorySheetID, @sRemark);

SELECT @sErrorMsg;
SELECT 1 FROM t_inventorysheet WHERE F_ID = @iInventorySheetID AND F_Remark = @sRemark;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '�̵㵥Ϊ����˻�����ɾ��ʱ���޸��̵��ܽ�ʧ��', '���Գɹ�', '����ʧ��') AS 'CASE4 Testing Result';

DELETE FROM t_inventorycommodity WHERE F_ID = @iInventorycommodityID;
DELETE FROM T_InventorySheet WHERE F_ID = @iInventorySheetID;



SELECT '-------------------- Case5: �޸�״̬0����û���̵���Ʒ���̵㵥 -------------------------' AS 'Case5';

INSERT INTO T_InventorySheet (F_ShopID,F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
VALUES (2,1,200,0,5,'2017-08-06','...........................');
SET @iInventorySheetID = last_insert_id();


SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sRemark = 'XXXX';

CALL SP_Inventory_UpdateSheet(@iErrorCode, @sErrorMsg, @iInventorySheetID, @sRemark);

SELECT @sErrorMsg;
SELECT 1 FROM t_inventorysheet WHERE F_ID = @iInventorySheetID AND F_Remark = @sRemark;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '���̵㵥û���̵���Ʒ', '���Գɹ�', '����ʧ��') AS 'CASE5 Testing Result';

DELETE FROM T_InventorySheet WHERE F_ID = @iInventorySheetID;


SELECT '-------------------- Case6: �޸�״̬1����û���̵���Ʒ���̵㵥 -------------------------' AS 'Case6';

INSERT INTO T_InventorySheet (F_ShopID,F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
VALUES (2,1,200,1,5,'2017-08-06','...........................');
SET @iInventorySheetID = last_insert_id();


SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sRemark = 'XXXX';

CALL SP_Inventory_UpdateSheet(@iErrorCode, @sErrorMsg, @iInventorySheetID, @sRemark);

SELECT @sErrorMsg;
SELECT 1 FROM t_inventorysheet WHERE F_ID = @iInventorySheetID AND F_Remark = @sRemark;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '���̵㵥û���̵���Ʒ', '���Գɹ�', '����ʧ��') AS 'CASE6 Testing Result';

DELETE FROM T_InventorySheet WHERE F_ID = @iInventorySheetID;

SELECT '-------------------- Case7: �޸Ĳ����ڵ��̵㵥 -------------------------' AS 'Case7';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sRemark = 'XXXX';
SET @iInventorySheetID = 999999999;

CALL SP_Inventory_UpdateSheet(@iErrorCode, @sErrorMsg, @iInventorySheetID, @sRemark);

SELECT @sErrorMsg;
SELECT 1 FROM t_inventorysheet WHERE F_ID = @iInventorySheetID AND F_Remark = @sRemark;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '�̵㵥������', '���Գɹ�', '����ʧ��') AS 'CASE7 Testing Result';

DELETE FROM T_InventorySheet WHERE F_ID = @iInventorySheetID;