SELECT '++++++++++++++++++ SP_InventoryCommodity_UpdateNoReal.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:�޸ĳɹ� -------------------------' AS 'Case1';
SET @iID = 11;
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iNoReal = 1;
SET @iNOSystem = 100;
CALL SP_InventoryCommodity_UpdateNoReal(@iErrorCode, @iErrorCode, @iID, @iNoReal, @iNOSystem);

SELECT @iErrorCode;
SELECT 1 FROM T_InventoryCommodity WHERE F_ID = @iID AND F_NoReal = @iNoReal AND F_NOSystem = @iNOSystem;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE1 Testing Result';


SELECT '-------------------- Case2:Ҫ�޸ĵ�ID�����ڣ��޸�ʧ�� -------------------------' AS 'Case2';
SET @iID = 999;
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iNoReal = 1;
SET @iNOSystem = 0;

CALL SP_InventoryCommodity_UpdateNoReal(@iErrorCode, @iErrorCode, @iID, @iNoReal, @iNOSystem);

SELECT @iErrorCode;
SELECT 1 FROM T_InventoryCommodity WHERE F_ID = @iID AND F_NoReal = @iNoReal AND F_NOSystem = @iNOSystem;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE1 Testing Result';