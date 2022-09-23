SELECT '++++++++++++++++++ Test_SP_Warehouse_Delete.sql ++++++++++++++++++++';

SELECT '++++++++++++++++++ Case1:����ɾ�� ++++++++++++++++++++' AS 'Case1';
INSERT INTO t_warehouse (F_Name, F_Address, F_Status) 
VALUES ('�ֿ�994', 'ֲ��԰', 0);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID =last_insert_id();

CALL SP_Warehouse_Delete(@iErrorCode, @sErrorMsg, @iID);

SELECT 1 FROM t_Warehouse WHERE F_ID = @iID AND F_Status = 1;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';
DELETE FROM t_Warehouse WHERE F_ID = @iID;

SELECT '++++++++++++++++++ Case2:������, ����ɾ�� ++++++++++++++++++++' AS 'Case2';
INSERT INTO t_warehouse (F_Name, F_Address, F_Status) 
VALUES ('�ֿ�994', 'ֲ��԰', 0);
SET @iID =last_insert_id();

INSERT INTO T_Warehousing (F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_CreateDatetime)
VALUES (0, 3, @iID, 5, now());
SET @iID2 = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_Warehouse_Delete(@iErrorCode, @sErrorMsg, @iID);

SELECT 1 FROM t_Warehouse WHERE F_ID = @iID AND F_Status = 0;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

DELETE FROM T_Warehousing WHERE F_ID = @iID2;
DELETE FROM t_Warehouse WHERE F_ID = @iID;

SELECT '++++++++++++++++++ Case3:�ظ�ɾ�� ++++++++++++++++++++' AS 'Case3';
INSERT INTO t_warehouse (F_Name, F_Address, F_Status) 
VALUES ('�ֿ�994', 'ֲ��԰', 1);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID =last_insert_id();

CALL SP_Warehouse_Delete(@iErrorCode, @sErrorMsg, @iID);

SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';
DELETE FROM t_Warehouse WHERE F_ID = @iID;