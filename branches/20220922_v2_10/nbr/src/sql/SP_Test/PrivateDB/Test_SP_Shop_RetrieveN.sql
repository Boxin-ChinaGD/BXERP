SELECT '++++++++++++++++++ Test_SP_Shop_RetrieveN.sql ++++++++++++++++++++';

SELECT '+++++++++++++++++++++++ case1:������ѯ ++++++++++++++++++++++++++++++++++++' AS 'case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iDistrictID= -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Shop_RetrieveN(@iErrorCode, @sErrorMsg, @iPageIndex, @iPageSize, @iDistrictID, @iTotalRecord);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

SELECT '+++++++++++++++++++++++ case2:����iDistrictID��Ϊ������ѯ ++++++++++++++++++++++++++++++++++++' AS 'case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iDistrictID= 1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Shop_RetrieveN(@iErrorCode, @sErrorMsg, @iPageIndex, @iPageSize, @iDistrictID, @iTotalRecord);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';