SELECT '++++++++++++++++++ Test_SP_Inventory_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:������� -------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 2;  
SET @iWarehouseID = 1;  
SET @iScope = 1;
SET @iStaffID = 2;
SET @sRemark = '..........................';

CALL SP_Inventory_Create(@iErrorCode, @sErrorMsg, @iShopID, @iWarehouseID, @iScope, @iStaffID, @sRemark);

SELECT @sErrorMsg;
SELECT 1 
	FROM t_inventorysheet 
	WHERE F_WarehouseID = @iWarehouseID
		  AND F_Scope = @iScope
	      AND F_StaffID = @iStaffID
		  AND F_Remark = @sRemark;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE1 Testing Result';

DELETE FROM t_inventorysheet WHERE F_ID = LAST_INSERT_ID();

SELECT '-------------------- Case2:�ò����ڵĲֿ�ID�����̵㵥 -------------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 2;  
SET @iWarehouseID = -99;  
SET @iScope = 1;
SET @iStaffID = 2;
SET @sRemark = '..........................';

CALL SP_Inventory_Create(@iErrorCode, @sErrorMsg, @iShopID, @iWarehouseID, @iScope, @iStaffID, @sRemark);
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'CASE2 Testing Result';

SELECT '-------------------- Case3:�ò����ڵ�staffID�����̵㵥 -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 2;
SET @iWarehouseID = 1;  
SET @iScope = 1;
SET @iStaffID = -99;
SET @sRemark = '..........................';

CALL SP_Inventory_Create(@iErrorCode, @sErrorMsg, @iShopID, @iWarehouseID, @iScope, @iStaffID, @sRemark);
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'CASE3 Testing Result';

SELECT '-------------------- Case3:�ò����ڵ�ShopID�����̵㵥 -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = -1;  
SET @iWarehouseID = 1;  
SET @iScope = 1;
SET @iStaffID = 2;
SET @sRemark = '..........................';

CALL SP_Inventory_Create(@iErrorCode, @sErrorMsg, @iShopID, @iWarehouseID, @iScope, @iStaffID, @sRemark);
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '�����̵㵥��ShopID������', '���Գɹ�', '����ʧ��') AS 'CASE3 Testing Result';