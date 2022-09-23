SELECT '++++++++++++++++++ Test_SPD_InventoryCommodity_CheckNOReal.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:�������� -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_InventoryCommodtiy_CheckNOReal(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

SELECT '-------------------- Case2:�̵㵥��Ʒ��ʵ������С��0 -------------------------' AS 'Case2';
INSERT INTO T_InventorySheet (F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
VALUES (1,200,4,4,'2017-12-06','...........................zz');
SET @iInventorysheetID = LAST_INSERT_ID();
-- 
INSERT INTO t_inventorycommodity ( F_InventorySheetID, F_CommodityID, F_CommodityName, F_Specification, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem)
VALUES (@iInventorysheetID, 1, '��Ƭ', 1, 1, 1, -1, 0);
SET @iInventoryCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_InventoryCommodtiy_CheckNOReal(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('�̵㵥��Ʒ', @iInventoryCommodityID ,'ʵ����������ȷ����Ҫ���ڵ���0') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
-- 
DELETE FROM t_Inventorycommodity WHERE F_ID = @iInventoryCommodityID;
DELETE FROM T_InventorySheet WHERE F_ID = @iInventorysheetID;