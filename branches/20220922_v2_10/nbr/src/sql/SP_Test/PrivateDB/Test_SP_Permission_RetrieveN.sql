SELECT '++++++++++++++++++ Test_SP_Permission_RetrieveN.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: ����sName��������� -------------------------' AS 'Case1';
-- case1: ����sName���������
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '���';
SET @sDomain = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Permission_RetrieveN(@iErrorCode, @sErrorMsg, @sName, @sDomain, @iPageIndex, @iPageSize, @iTotalRecord);  

SELECT @iTotalRecord;
SELECT @sErrorMsg;

SELECT 1 FROM t_Permission WHERE F_Name = @sName AND F_Domain = @sDomain;
SELECT @iErrorCode;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';	-- ... CCM What?! 

 
SELECT '-------------------- Case2: �޲�������� -------------------------' AS 'Case2';
-- case2: �޲��������
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '';
SET @sDomain = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Permission_RetrieveN(@iErrorCode, @sErrorMsg, @sName, @sDomain, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;

SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';	-- ... CCM What?! 