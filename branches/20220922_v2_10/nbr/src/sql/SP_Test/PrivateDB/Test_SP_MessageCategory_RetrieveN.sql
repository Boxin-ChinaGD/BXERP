SELECT '++++++++++++++++++ Test_SP_MessageCategory_RetrieveN.sql ++++++++++++++++++++';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;


CALL SP_MessageCategory_RetrieveN(@iErrorCode, @sErrorMsg, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0 , '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';