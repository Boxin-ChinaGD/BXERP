SELECT '++++++++++++++++++Test_SP_Company_RetrieveN.sql+++++++++++++++++++++++';

SELECT '-----------------CASE1: ������ѯ------------------' AS 'CASE1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStatus = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @sSN = '';
SET @iTotalRecord = 0;

CALL SP_Company_RetrieveN (@iErrorCode, @sErrorMsg, @iStatus, @sSN, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(found_rows() >= 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';


SELECT '-----------------CASE3: ����SN��ѯ��˾��Ϣ ------------------' AS 'CASE2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStatus = 0;
SET @sSN = '668866';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Company_RetrieveN (@iErrorCode, @sErrorMsg, @iStatus, @sSN, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';