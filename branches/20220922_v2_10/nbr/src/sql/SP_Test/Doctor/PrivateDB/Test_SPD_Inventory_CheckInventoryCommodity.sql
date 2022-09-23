SELECT '++++++++++++++++++ Test_SPD_Inventory_CheckInventoryCommodtiy.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:�������� -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Inventory_CheckInventoryCommodtiy(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';


SELECT '-------------------- Case2:�̵㵥�������̵���Ʒ -------------------------' AS 'Case2';
-- 
INSERT INTO t_inventorysheet (F_WarehouseID, F_Scope, F_Status, F_StaffID, F_CreateDatetime, F_Remark, F_UpdateDatetime)
VALUES (1, 0, 0, 1, now(), '...', now());
SET @iID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Inventory_CheckInventoryCommodtiy(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('�̵㵥', @iID ,'û���̵���Ʒ') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
-- 
DELETE FROM t_inventorysheet WHERE F_ID = @iID;


SELECT '-------------------- Case3:ɾ�����̵㵥û���̵���Ʒ -------------------------' AS 'Case3';
-- 
INSERT INTO t_inventorysheet (F_WarehouseID, F_Scope, F_Status, F_StaffID, F_CreateDatetime, F_Remark, F_UpdateDatetime)
VALUES (1, 0, 3, 1, now(), '...', now());
SET @iID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Inventory_CheckInventoryCommodtiy(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';
-- 
DELETE FROM t_inventorysheet WHERE F_ID = @iID;