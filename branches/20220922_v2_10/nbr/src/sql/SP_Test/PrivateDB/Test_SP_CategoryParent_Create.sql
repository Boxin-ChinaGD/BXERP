SELECT '++++++++++++++++++Test_SP_CategoryParent_Create.sql+++++++++++++++++++++++';

SELECT '-----------------Case1: ������еĴ���,������Ӧ��Ϊ1------------------' AS 'Case1';

INSERT INTO t_categoryparent (F_Name)
VALUES ('����Ʒ');
SET @iID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '����Ʒ';

CALL SP_CategoryParent_Create(@iErrorCode, @sErrorMsg, @sName);

SELECT @sErrorMsg;
DELETE FROM t_categoryparent WHERE F_ID = last_insert_id();

SELECT 1 FROM t_categoryparent WHERE F_Name = @sName;
SELECT IF(found_rows() = 0 AND @iErrorCode = 1, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

SELECT '-----------------Case2: ���һ��û�д��࣬������Ϊ0------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '������Ʒ';

CALL SP_CategoryParent_Create(@iErrorCode, @sErrorMsg, @sName);

SELECT @sErrorMsg;
DELETE FROM t_categoryparent WHERE F_ID = last_insert_id();

SELECT 1 FROM t_categoryparent WHERE F_Name = @sName;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';