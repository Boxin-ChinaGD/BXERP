SELECT '++++++++++++++++++ Test_SPD_Inventory_CheckWarehouseID.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:�������� -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Inventory_CheckWarehouseID(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

--	������
--	SELECT '-------------------- Case2:�̵㵥��Ӧ�Ĳֿ�ID����ȷ -------------------------' AS 'Case2';
--	INSERT INTO T_InventorySheet (F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
--	VALUES (-1,200,0,3,'2017-12-06','...........................zz');
--	SET @iInventorysheetID = LAST_INSERT_ID();
--	-- 
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	-- 
--	CALL SPD_Inventory_CheckWarehouseID(@iErrorCode, @sErrorMsg);
--	SELECT @iErrorCode, @sErrorMsg;
--	-- 
--	SELECT IF(@sErrorMsg = CONCAT('�̵㵥', @iInventorysheetID ,'��Ӧ�Ĳֿ�ID����ȷ') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
--	-- 
--	DELETE FROM T_InventorySheet WHERE F_ID = @iInventorysheetID;