SELECT '++++++++++++++++++Test_SP_Brand_Create.sql+++++++++++++++++++++++';

SELECT '-----------------Case1: ��Ӳ��ظ���Ʒ�ƣ�������Ϊ0(�������)------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '�Ϻü�';

CALL SP_Brand_Create(@iErrorCode, @sErrorMsg, @sName);

SELECT @sErrorMsg;
SELECT 1 FROM t_brand WHERE F_Name = @sName;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

DELETE FROM t_brand WHERE F_ID = last_insert_id();

SELECT '-----------------Case2: ����ظ�Ʒ�ƣ�������ӣ�������Ϊ1------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = 'ͳһ';

CALL SP_Brand_Create(@iErrorCode, @sErrorMsg, @sName);

SELECT @sErrorMsg;
SELECT 1 FROM t_brand WHERE F_Name = @sName;
SELECT IF(found_rows() = 1 AND @iErrorCode = 1, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

DELETE FROM t_brand WHERE F_ID = last_insert_id();