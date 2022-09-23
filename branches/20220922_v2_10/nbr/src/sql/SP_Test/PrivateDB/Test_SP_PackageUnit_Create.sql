SELECT '++++++++++++++++++ Test_SP_PackageUnit_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: ��Ӳ��ظ��İ�װ��λ��������Ϊ0 -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '��1';

CALL SP_PackageUnit_Create(@iErrorCode, @sErrorMsg, @sName);

SELECT @sErrorMsg;
SELECT 1 FROM t_packageunit WHERE F_Name = @sName;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

DELETE FROM t_packageunit WHERE F_ID = last_insert_id();


SELECT '-------------------- Case2: ����ظ��İ�װ��λ��������ӣ�������Ϊ1 -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '��';

CALL SP_PackageUnit_Create(@iErrorCode, @sErrorMsg, @sName);

SELECT @sErrorMsg;
SELECT 1 FROM t_packageunit WHERE F_Name = @sName;
SELECT IF(found_rows() = 1 AND @iErrorCode = 1, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

DELETE FROM t_packageunit WHERE F_ID = last_insert_id();