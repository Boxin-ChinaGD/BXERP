SELECT '++++++++++++++++++Test_SP_Category_RetrieveNByParent.sql+++++++++++++++++++++++';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 2; -- �������id �õ���Ӧ��С��id������


CALL SP_Category_RetrieveNByParent(@iErrorCode, @sErrorMsg, @iID);
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';