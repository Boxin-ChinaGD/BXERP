SELECT '++++++++++++++++++ Test_SP_Inventory_Approve.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: �̵㵥��״̬����Ϊ0ʱ -------------------------' AS 'Case1';

INSERT INTO T_InventorySheet (F_ShopID, F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
VALUES (2,1,200,0,1,'2017-04-06','...........................');
SET @iID = last_insert_id();

INSERT INTO t_inventorycommodity (F_InventorySheetID, F_CommodityID, F_CommodityName, F_Specification, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem, F_CreateDatetime)
VALUES (@iID, 1, '��Ƭ', '��', 1, 1, -1, -1, now());
SET @iID2 = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';     
SET @iApproverID = 1;

CALL SP_Inventory_Approve(@iErrorCode, @sErrorMsg, @iID ,@iApproverID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM T_InventorySheet WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7 AND @sErrorMsg = '��������ҵ�߼�', '���Գɹ�', '����ʧ��') AS 'CASE1 Testing Result';

DELETE FROM t_inventorycommodity WHERE F_ID = @iID2;
DELETE FROM T_InventorySheet WHERE F_ID = @iID;

SELECT '-------------------- Case2: �̵㵥״̬Ϊ1ʱ -------------------------' AS 'Case2';

INSERT INTO T_InventorySheet (F_ShopID, F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
VALUES (2,1,200,1,1,'2017-04-06','...........................');
SET @iID = last_insert_id();  

INSERT INTO t_inventorycommodity (F_InventorySheetID, F_CommodityID, F_CommodityName, F_Specification, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem, F_CreateDatetime)
VALUES (@iID, 1, '��Ƭ', '��', 1, 1, -1, -1, now());
   
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iApproverID = 1;

CALL SP_Inventory_Approve(@iErrorCode, @sErrorMsg, @iID, @iApproverID);

SELECT @sErrorMsg;
SELECT 1 FROM T_InventorySheet WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE2 Testing Result';

DELETE FROM t_inventorycommodity WHERE F_InventorySheetID = @iID;
DELETE FROM T_InventorySheet WHERE F_ID = @iID;

SELECT '-------------------- Case3: �̵㵥״̬Ϊ2ʱ -------------------------' AS 'Case3';

INSERT INTO T_InventorySheet (F_ShopID, F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
VALUES (2,1,200,2,1,'2017-04-06','...........................');
SET @iID = last_insert_id();

INSERT INTO t_inventorycommodity (F_InventorySheetID, F_CommodityID, F_CommodityName, F_Specification, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem, F_CreateDatetime)
VALUES (@iID, 1, '��Ƭ', '��', 1, 1, -1, -1, now());
SET @ID2 = last_insert_id();
  
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iApproverID = 1;

CALL SP_Inventory_Approve(@iErrorCode, @sErrorMsg, @iID, @iApproverID);

SELECT @sErrorMsg;
SELECT 1 FROM T_InventorySheet WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7 AND @sErrorMsg = '��������ҵ�߼�', '���Գɹ�', '����ʧ��') AS 'CASE3 Testing Result';

DELETE FROM t_inventorycommodity WHERE F_ID = @ID2;
DELETE FROM T_InventorySheet WHERE F_ID = @iID;

SELECT '-------------------- Case4: ���û���ж���Ʒ���̵㵥 -------------------------' AS 'Case4';

INSERT INTO T_InventorySheet (F_ShopID, F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
VALUES (2,1,200,1,1,'2017-04-06','...........................');
SET @iID = last_insert_id();


SET @iErrorCode = 0;
SET @sErrorMsg = '';     
SET @iApproverID = 1;

CALL SP_Inventory_Approve(@iErrorCode, @sErrorMsg, @iID ,@iApproverID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM T_InventorySheet WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7 AND @sErrorMsg = '���̵㵥û���̵���Ʒ', '���Գɹ�', '����ʧ��') AS 'CASE4 Testing Result';

DELETE FROM T_InventorySheet WHERE F_ID = @iID;


SELECT '-------------------- Case5: ��˲����ڵ��̵㵥 -------------------------' AS 'Case5';

SET @iErrorCode = 0;
SET @sErrorMsg = '';     
SET @iApproverID = 1;
SET @iID = 999999999999;

CALL SP_Inventory_Approve(@iErrorCode, @sErrorMsg, @iID ,@iApproverID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM T_InventorySheet WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '�̵㵥������', '���Գɹ�', '����ʧ��') AS 'CASE5 Testing Result';

DELETE FROM t_inventorycommodity WHERE F_ID = @iID2;
DELETE FROM T_InventorySheet WHERE F_ID = @iID;