SELECT '++++++++++++++++++Test_SP_CategoryParent_Retrieve1.sql+++++++++++++++++++++++';

SELECT '-----------------Case1: ��ѯ������Ʒ����------------------' AS 'Case1';
SET @iErrorcode = 0;
SET @sErrorMsg = '';
SET @iID = 1;

CALL SP_CategoryParent_Retrieve1(@iErrorcode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT 1 FROM t_categoryparent WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';