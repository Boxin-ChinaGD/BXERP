SELECT '++++++++++++++++++ Test_SP_PackageUnit_RetrieveN.sql ++++++++++++++++++++';

SELECT '++++++++++++++++++++++ case1:û���ö��װ��ƷID���в�ѯ  ++++++++++++++++++++++++++++++++++' AS 'case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iNormalCommodityID = 0;

CALL SP_PackageUnit_RetrieveN(@iErrorCode, @sErrorMsg, @iNormalCommodityID, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

SELECT @iTotalRecord;
 
-- ...???? Ϊʲôû��д���԰������� 

SELECT '++++++++++++++++++++++ case2:�ò�����ƷID��ѯ���װ��Ʒ��λ ++++++++++++++++++++++++++++++++++' AS 'case2';
 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iNormalCommodityID = 47;

CALL SP_PackageUnit_RetrieveN(@iErrorCode, @sErrorMsg, @iNormalCommodityID, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

SELECT @iTotalRecord;