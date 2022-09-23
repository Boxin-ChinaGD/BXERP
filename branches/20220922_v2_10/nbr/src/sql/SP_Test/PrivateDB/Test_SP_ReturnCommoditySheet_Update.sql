SELECT '++++++++++++++++++ Test_SP_ReturnCommoditySheet_Update.sql ++++++++++++++++++++';

INSERT INTO T_ReturnCommoditySheet (F_StaffID,F_ProviderID, F_ShopID)
VALUES (5,5,2);
SET @iID = LAST_INSERT_ID();


INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iID, 5,'�ټٵ�����', 7, 100, '��', 5.0);
SET @iID2 = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iProviderID = 6;

SELECT '-------------------- CASE1:���˻���δ���,update�ɹ� -------------------------' AS 'Case1';

CALL SP_ReturnCommoditySheet_Update(@iErrorCode, @sErrorMsg, @iID, @iProviderID);

SELECT 1 FROM t_returncommoditysheet 
	WHERE F_ID = @iID 
	AND F_ProviderID = @iProviderID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';


SELECT '-------------------- CASE2:���˻���δ���,����ProviderID������,update���ɹ� -------------------------' AS 'Case2';

SET @iProviderID = -3;

CALL SP_ReturnCommoditySheet_Update(@iErrorCode, @sErrorMsg, @iID, @iProviderID);

SELECT 1 FROM t_returncommoditysheet 
	WHERE F_ID = @iID 
	AND F_ProviderID = @iProviderID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '�ù�Ӧ�̲����ڣ�������ѡ��Ӧ��', '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

SELECT '-------------------- CASE3:���˻��������,update���ɹ�  -------------------------' AS 'Case3';

UPDATE t_returncommoditysheet SET F_Status = 1 WHERE F_ID = @iID;

SET @sErrorMsg = '';
SET @iProviderID = 2;
CALL SP_ReturnCommoditySheet_Update(@iErrorCode, @sErrorMsg, @iID, @iProviderID);

SELECT 1 FROM t_returncommoditysheet 
	WHERE F_ID = @iID 
	AND F_ProviderID = @iProviderID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '���˻��������,�������޸�', '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';

DELETE FROM t_returncommoditysheetcommodity WHERE F_ReturnCommoditySheetID = @iID;
DELETE FROM t_returncommoditysheet WHERE F_ID = @iID;


SELECT '-------------------- CASE4:�޸�û���˻���Ʒ���˻���-------------------------' AS 'Case4';

INSERT INTO T_ReturnCommoditySheet (F_StaffID,F_ProviderID, F_ShopID) 
VALUES (1,2,2);
SET @iID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iProviderID = 6;

CALL SP_ReturnCommoditySheet_Update(@iErrorCode, @sErrorMsg, @iID, @iProviderID);

SELECT @iErrorCode;
SELECT @sErrorMsg;

SELECT 1 FROM t_returncommoditysheet 
	WHERE F_ID = @iID 
	AND F_ProviderID = @iProviderID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '���˻���û���˻���Ʒ', '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';

DELETE FROM t_returncommoditysheet WHERE F_ID = @iID;