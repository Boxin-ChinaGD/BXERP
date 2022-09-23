SELECT '++++++++++++++++++ Test_SP_ReturnCommoditySheet_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:�������� -------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStaffID = 3;
SET @iProviderID = 5;
SET @iShopID = 2;

CALL SP_ReturnCommoditySheet_Create(@iErrorCode, @sErrorMsg, @iStaffID, @iProviderID, @iShopID);

SELECT @sErrorMsg;
DELETE FROM t_returncommoditysheet WHERE F_ID = last_insert_id();
SELECT @iErrorCode;

SELECT 1 FROM t_returncommoditysheet 
	WHERE F_StaffID = @iStaffID 
	AND F_ProviderID = @iProviderID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'case1 Testing Result';

SELECT '-------------------- Case2:�����˻���ʱ ����һ�������� staffID -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStaffID = -3;
SET @iProviderID = 5;
SET @iShopID = 2;

CALL SP_ReturnCommoditySheet_Create(@iErrorCode, @sErrorMsg, @iStaffID, @iProviderID, @iShopID);

SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'case2 Testing Result';
SELECT @sErrorMsg;

SELECT '-------------------- Case3:�����˻���ʱ ����һ�������� providerID -------------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';

SET @iStaffID = 3;
SET @iProviderID = -5;

CALL SP_ReturnCommoditySheet_Create(@iErrorCode, @sErrorMsg, @iStaffID, @iProviderID, @iShopID);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'case3 Testing Result';