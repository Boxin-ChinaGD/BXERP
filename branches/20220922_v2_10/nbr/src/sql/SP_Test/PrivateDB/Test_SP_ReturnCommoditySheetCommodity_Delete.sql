SELECT '++++++++++++++++++ Test_SP_ReturnCommoditySheetCommodity_Delete.sql ++++++++++++++++++++';	

SELECT '-------------------- Case1:ɾ��һ���˻������˻�����Ʒ���һ������ -------------------------' AS 'Case1';

INSERT INTO t_returncommoditysheet (F_StaffID, F_ProviderID, F_ShopID)
VALUES (3, 3,2);

SET @iReturnCommoditySheetID = last_insert_id();
-- �������Ͳ�����Ĭ��Ϊ0���ò���F_PurchasingPrice�����Դ�Tset�Ͳ�������
INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification)
VALUES (@iReturnCommoditySheetID, 5,'��ʦ��ţ����', 7, 100, '��');

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 5;

CALL SP_ReturnCommoditySheetCommodity_Delete (@iErrorCode, @sErrorMsg, @iReturnCommoditySheetID, @iCommodityID);

SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT 1 FROM t_returncommoditysheetcommodity WHERE F_ID = last_insert_id();

SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'case1 Testing Result';

DELETE FROM t_ReturnCommoditySheet WHERE F_ID = @iReturnCommoditySheetID;

SELECT '-------------------- Case2:ɾ��һ���˻������˻�����Ʒ����������� -------------------------' AS 'Case2';

INSERT INTO t_returncommoditysheet (F_StaffID, F_ProviderID, F_ShopID)
VALUES (3, 3,2);

SET @iReturnCommoditySheetID = last_insert_id();

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification)
VALUES (@iReturnCommoditySheetID, 6,'����ʪ��ˮ����', 8, 100, '��');

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification)
VALUES (@iReturnCommoditySheetID, 7,'����ʪԭҺ', 9, 100, '��');

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification)
VALUES (@iReturnCommoditySheetID, 8,'����ʪȪ����', 10, 100, '��');

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 0;

CALL SP_ReturnCommoditySheetCommodity_Delete (@iErrorCode, @sErrorMsg, @iReturnCommoditySheetID, @iCommodityID);

SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT 1 FROM t_returncommoditysheetcommodity WHERE F_ReturnCommoditySheetID = @iReturnCommoditySheetID;

SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'case2 Testing Result';

DELETE FROM t_ReturnCommoditySheet WHERE F_ID = @iReturnCommoditySheetID;

SELECT '-------------------- Case3:ɾ��һ������˵��˻�����Ӧ���˻�����Ʒ������ -------------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 0;
SET @iReturnCommoditySheetID = 2;

CALL SP_ReturnCommoditySheetCommodity_Delete (@iErrorCode, @sErrorMsg, @iReturnCommoditySheetID, @iCommodityID);

SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT 1 FROM t_returncommoditysheetcommodity WHERE F_ReturnCommoditySheetID = @iReturnCommoditySheetID;

SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'case3 Testing Result';

SELECT '-------------------- Case4:ɾ��һ�Ų����ڵ��˻�����Ӧ���˻�����Ʒ������ -------------------------' AS 'Case4';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 0;
SET @iReturnCommoditySheetID = -2;

CALL SP_ReturnCommoditySheetCommodity_Delete (@iErrorCode, @sErrorMsg, @iReturnCommoditySheetID, @iCommodityID);

SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT 1 FROM t_returncommoditysheetcommodity WHERE F_ReturnCommoditySheetID = @iReturnCommoditySheetID;

SELECT IF(found_rows() = 0 AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'case4 Testing Result';