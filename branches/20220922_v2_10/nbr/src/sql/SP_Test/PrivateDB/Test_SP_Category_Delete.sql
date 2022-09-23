SELECT '++++++++++++++++++Test_SP_Category_Delete.sql+++++++++++++++++++++++';

SELECT '-----------------Case1: ��Ʒ����������Ʒ�����ɾ�����ˣ��������Ϊ7------------------' AS 'Case1';

SET @iCategoryID = 2;
SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_Category_Delete(@iErrorCode, @sErrorMsg, @iCategoryID);

SELECT @sErrorMsg;
SELECT 1 FROM t_category WHERE F_ID = @iCategoryID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

SELECT '-----------------Case2: ��Ʒ����û������������Ʒ������ֱ��ɾ�����������Ϊ0------------------' AS 'Case2';

SET @sName = '�ɻ�';
SET @iParentID = 1;
SET @iErrorCode = 0;
SET @sErrorMsg = '';

INSERT INTO t_category (F_Name, F_ParentID)
VALUES (@sName, @iParentID);

SET @iID = LAST_INSERT_ID();

CALL SP_Category_Delete(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT * FROM t_category WHERE F_ID = @iID;

SELECT 1 FROM t_category WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';