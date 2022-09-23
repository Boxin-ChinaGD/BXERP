SELECT '++++++++++++++++++Test_SP_Brand_RetrieveN.sql+++++++++++++++++++++++';

SELECT '-----------------Case1: ����sName���������------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '��';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Brand_RetrieveN(@iErrorCode, @sErrorMsg, @sName, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @sErrorMsg;
SELECT @iTotalRecord;

SELECT 1 FROM t_brand WHERE F_Name = @sName;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

SELECT '-----------------Case2: �޲��������------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Brand_RetrieveN(@iErrorCode, @sErrorMsg, @sName, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @sErrorMsg;
SELECT @iTotalRecord;

SELECT 1 FROM t_brand WHERE F_Name = @sName;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';