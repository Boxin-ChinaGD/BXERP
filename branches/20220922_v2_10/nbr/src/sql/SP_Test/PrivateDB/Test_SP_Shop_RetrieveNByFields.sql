SELECT '++++++++++++++++++ Test_SP_Shop_RetrieveN.sql ++++++++++++++++++++';

SELECT '+++++++++++++++++++++++ case1:������ѯ ++++++++++++++++++++++++++++++++++++' AS 'case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '';
SET @iDistrictID= -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Shop_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iDistrictID, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

SELECT '+++++++++++++++++++++++ case2:�����ŵ����Ʋ�ѯ ++++++++++++++++++++++++++++++++++++' AS 'case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = 'Ĭ��';
SET @iDistrictID= -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Shop_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iDistrictID, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

SELECT '+++++++++++++++++++++++ case3:���ݵ���ID��ѯ ++++++++++++++++++++++++++++++++++++' AS 'case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '';
SET @iDistrictID= 1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Shop_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iDistrictID, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';

SELECT '+++++++++++++++++++++++ case4:���ݵ���ID�����Ʋ�ѯ��ѯ ++++++++++++++++++++++++++++++++++++' AS 'case4';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = 'Ĭ��';
SET @iDistrictID= 1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Shop_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iDistrictID, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';