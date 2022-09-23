SELECT '++++++++++++++++++ Test_SP_Inventory_Submit.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: �̵㵥��״̬����Ϊ0ʱ -------------------------' AS 'Case1';

INSERT INTO T_InventorySheet (F_ShopID,F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
VALUES (2,1,200,0,1,'2017-04-06','...........................');
SET @ID = last_insert_id();  

INSERT INTO t_inventorycommodity (F_InventorySheetID, F_CommodityID, F_CommodityName, F_Specification, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem, F_CreateDatetime)
VALUES (@ID, 1, '��Ƭ', '��', 1, 1, -1, -1, now());

SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_Inventory_Submit(@iErrorCode, @sErrorMsg, @ID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM T_InventorySheet WHERE F_ID = @ID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'CASE1 Testing Result';

DELETE FROM t_inventorycommodity WHERE F_InventorySheetID = @ID;
DELETE FROM T_InventorySheet WHERE F_ID = @ID;

SELECT '-------------------- Case2: �̵㵥״̬Ϊ1ʱ -------------------------' AS 'Case2';

INSERT INTO T_InventorySheet (F_ShopID,F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
VALUES (2,1,200,1,1,'2017-04-06','...........................');
SET @InventorySheetID = last_insert_id();     

INSERT INTO t_inventorycommodity (F_InventorySheetID, F_CommodityID, F_CommodityName, F_Specification, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem, F_CreateDatetime)
VALUES (@InventorySheetID, 1, '��Ƭ', '��', 1, 1, -1, -1, now());
SET @InventorySheetCommodityID = last_insert_id();  

SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_Inventory_Submit(@iErrorCode, @sErrorMsg, @InventorySheetID);
SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT 1 FROM T_InventorySheet WHERE F_ID = @InventorySheetID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7 AND @sErrorMsg = '�̵㵥״̬��Ϊ0�����ܽ����ύ', '���Գɹ�', '����ʧ��') AS 'CASE2 Testing Result';

DELETE FROM t_inventorycommodity WHERE F_ID = @InventorySheetCommodityID;
DELETE FROM T_InventorySheet WHERE F_ID = @InventorySheetID;

SELECT '-------------------- Case3: �̵㵥״̬Ϊ2ʱ -------------------------' AS 'Case3';

INSERT INTO T_InventorySheet (F_ShopID,F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
VALUES (2,1,200,2,1,'2017-04-06','...........................');
SET @InventorySheetID = last_insert_id();     

INSERT INTO t_inventorycommodity (F_InventorySheetID, F_CommodityID, F_CommodityName, F_Specification, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem, F_CreateDatetime)
VALUES (@InventorySheetID, 1, '��Ƭ', '��', 1, 1, -1, -1, now());
SET @InventorySheetCommodityID = last_insert_id();  

SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_Inventory_Submit(@iErrorCode, @sErrorMsg, @InventorySheetID);
SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT 1 FROM T_InventorySheet WHERE F_ID = @InventorySheetID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7 AND @sErrorMsg = '�̵㵥״̬��Ϊ0�����ܽ����ύ', '���Գɹ�', '����ʧ��') AS 'CASE3 Testing Result';

DELETE FROM t_inventorycommodity WHERE F_ID = @InventorySheetCommodityID;
DELETE FROM T_InventorySheet WHERE F_ID = @InventorySheetID;

SELECT '-------------------- Case4: �̵㵥״̬Ϊ����ֵʱ -------------------------' AS 'Case4';

INSERT INTO T_InventorySheet (F_ShopID,F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
VALUES (2,1,200,-1,1,'2017-04-06','...........................');
SET @InventorySheetID = last_insert_id();     

INSERT INTO t_inventorycommodity (F_InventorySheetID, F_CommodityID, F_CommodityName, F_Specification, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem, F_CreateDatetime)
VALUES (@InventorySheetID, 1, '��Ƭ', '��', 1, 1, -1, -1, now());
SET @InventorySheetCommodityID = last_insert_id();  

SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_Inventory_Submit(@iErrorCode, @sErrorMsg, @InventorySheetID);
SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT 1 FROM T_InventorySheet WHERE F_ID = @InventorySheetID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7 AND @sErrorMsg = '�̵㵥״̬��Ϊ0�����ܽ����ύ', '���Գɹ�', '����ʧ��') AS 'CASE4 Testing Result';

DELETE FROM t_inventorycommodity WHERE F_ID = @InventorySheetCommodityID;
DELETE FROM T_InventorySheet WHERE F_ID = @InventorySheetID;


SELECT '-------------------- Case5: �ύû���̵���Ʒ���̵㵥 -------------------------' AS 'Case5';

INSERT INTO T_InventorySheet (F_ShopID,F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
VALUES (2,1,200,0,1,'2017-04-06','...........................');
SET @ID = last_insert_id();  


SET @iErrorCode = 0; 
SET @sErrorMsg = '';

CALL SP_Inventory_Submit(@iErrorCode, @sErrorMsg, @ID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7 AND @sErrorMsg = '���̵㵥û���̵���Ʒ', '���Գɹ�', '����ʧ��') AS 'CASE5 Testing Result';

DELETE FROM T_InventorySheet WHERE F_ID = @ID;


SELECT '-------------------- Case6: �ύ�����ڵ��̵㵥 -------------------------' AS 'Case6';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @ID = 99999999999;

CALL SP_Inventory_Submit(@iErrorCode, @sErrorMsg, @ID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM T_InventorySheet WHERE F_ID = @ID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '�̵㵥������', '���Գɹ�', '����ʧ��') AS 'CASE6 Testing Result';
