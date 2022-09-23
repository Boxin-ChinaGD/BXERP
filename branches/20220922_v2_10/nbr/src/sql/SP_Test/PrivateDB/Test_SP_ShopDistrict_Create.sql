SELECT '++++++++++++++++++ Test_SP_ShopDistrict_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: ������� -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = "����1";

CALL SP_ShopDistrict_Create(@iErrorCode, @sErrorMsg, @sName);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_ShopDistrict WHERE F_Name = @sName;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE1 Testing Result';

SELECT '-------------------- Case2: �ظ���� -------------------------' AS 'Case2';
SET @sErrorMsg = '';

CALL SP_ShopDistrict_Create(@iErrorCode, @sErrorMsg, @sName);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_shopDistrict WHERE F_Name = @sName;
SELECT IF(found_rows() = 1 AND @iErrorCode = 1, '���Գɹ�', '����ʧ��') AS 'CASE2 Testing Result';	-- ... found_rows() = 1�ж���Ч������ͨ������

DELETE FROM t_providerdistrict WHERE F_ID = LAST_INSERT_ID();