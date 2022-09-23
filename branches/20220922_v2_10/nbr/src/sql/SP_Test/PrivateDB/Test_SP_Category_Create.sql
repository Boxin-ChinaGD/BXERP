SELECT '++++++++++++++++++Test_SP_Category_Create.sql+++++++++++++++++++++++';

SELECT '-----------------Case1: ������------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '��������';
SET @iParentID = 1;

CALL SP_Category_Create(@iErrorCode, @sErrorMsg, @sName, @iParentID);

DELETE FROM t_category WHERE F_ID = last_insert_id();

SELECT @sErrorMsg;
SELECT 1 FROM t_category WHERE F_Name = @sName;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

SELECT '-----------------Case2: ����ظ����------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '����';
SET @iParentID = 1;

CALL SP_Category_Create(@iErrorCode, @sErrorMsg, @sName, @iParentID);

SELECT @sErrorMsg;
SELECT 1 FROM t_category WHERE F_Name = @sName;
SELECT IF(found_rows() = 1 AND @iErrorCode = 1, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

DELETE FROM t_category WHERE F_ID = last_insert_id();

SELECT '-----------------Case3: ��Ӳ����ڵ����------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '����99999';
SET @iParentID = -999;

CALL SP_Category_Create(@iErrorCode, @sErrorMsg, @sName, @iParentID);

SELECT @iErrorCode; 
SELECT @sErrorMsg;
SELECT IF( @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';