SELECT '++++++++++++++++++ Test_SP_Inventory_RetrieveNByFields.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: ������Ʒ������ģ�������̵���Ʒ�� -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '��Ƭ';
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_Inventory_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1,@iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE1 Testing Result';

SELECT '-------------------- Case2: û�������κ�ֵ�����̵���Ʒ�� -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_Inventory_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1,@iPageIndex,@iPageSize,@iTotalRecord);

SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE2 Testing Result';

SELECT '-------------------- Case3: ��ѯ��ɾ����Ʒ�Ĳɹ����� -------------------------' AS 'Case3';

INSERT INTO t_inventorysheet (F_ShopID, F_WarehouseID, F_Scope, F_Status, F_StaffID, F_ApproverID, F_CreateDatetime, F_ApproveDatetime, F_Remark, F_UpdateDatetime)
VALUES (2, 1, 100, 0, 1, 1, now(), now(), "111", now());

SET @iID = LAST_INSERT_ID();

INSERT INTO t_inventorycommodity ( F_InventorySheetID, F_CommodityID, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem)
VALUES ( @iID, 1, 1, 1, 0, 0);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '�����ཷζ��Ƭ1';
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_Inventory_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1,@iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE3 Testing Result';

DELETE FROM t_inventorycommodity WHERE F_InventorySheetID = @iID;
DELETE FROM t_inventorysheet WHERE F_ID = @iID;

SELECT '-------------------- Case4: ��ѯ�Ѿ���ɾ���Ĳɹ����� -------------------------' AS 'Case4';

INSERT INTO t_inventorycommodity ( F_InventorySheetID, F_CommodityID, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem)
VALUES ( 10, 1, 1, 1, 0, 0);

SET @iID = LAST_INSERT_ID();
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '����������Ĥ';
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_Inventory_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1,@iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE4 Testing Result';

DELETE FROM t_inventorycommodity WHERE F_InventorySheetID = @iID;

SELECT '-------------------- Case5: ���ݵ�����С���ȵ��̵㵥����(����10λ)ģ�������̵���Ʒ�� -------------------------' AS 'Case5';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = 'PD20190605';
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_Inventory_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1,@iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord > 0, '���Գɹ�', '����ʧ��') AS 'CASE5 Testing Result';

SELECT '-------------------- Case6: ����С����С���ȵ��̵㵥����(С��10λ)ģ�������̵���Ʒ�� -------------------------' AS 'Case6';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = 'PD2019060';
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_Inventory_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1,@iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = 0, '���Գɹ�', '����ʧ��') AS 'CASE6 Testing Result';

SELECT '-------------------- Case7: ���ݴ�����󳤶ȵ��̵㵥����(����20λ)ģ�������̵���Ʒ�� -------------------------' AS 'Case7';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = 'PD20190605123451234512345';
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_Inventory_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1,@iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = 0, '���Գɹ�', '����ʧ��') AS 'CASE7 Testing Result';

SELECT '-------------------- Case8: ���ݵ�����󳤶ȵ��̵㵥����(����20λ)ģ�������̵���Ʒ�� -------------------------' AS 'Case8';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = 'PD201906051234512345';
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_Inventory_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1,@iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = 0, '���Գɹ�', '����ʧ��') AS 'CASE8 Testing Result';


SELECT '-------------------- Case9:����string1����_�������ַ�����ģ������ -------------------------' AS 'Case9';
INSERT INTO t_inventorycommodity (F_InventorySheetID, F_CommodityID, F_CommodityName, F_Specification, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem, F_CreateDatetime)
VALUES (8, 1, '��Ƭ_)����', '��', 1, 1, -1, -1, now());
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '_)';
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_Inventory_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1,@iPageIndex,@iPageSize,@iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord > 0, '���Գɹ�', '����ʧ��') AS 'CASE9 Testing Result';

DELETE FROM t_inventorycommodity WHERE F_CommodityName = '��Ƭ_)����';
