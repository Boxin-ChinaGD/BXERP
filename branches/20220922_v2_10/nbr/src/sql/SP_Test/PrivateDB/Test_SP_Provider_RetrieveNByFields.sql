SELECT '++++++++++++++++++ Test_SP_Provider_RetrieveNByFields.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: ���빩Ӧ������ģ��������Ӧ�� -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '��';
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_Provider_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1,@iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

SELECT '-------------------- Case2: ������ϵ��ģ��������Ӧ�� -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = 'k';
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_Provider_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1,@iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

SELECT '-------------------- Case3: ���������λ�Ĺ�Ӧ�̵绰ģ��������Ӧ�� -------------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '1312';
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_Provider_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1,@iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';

SELECT '-------------------- Case4: û������ģ��������Ӧ�� -------------------------' AS 'Case4';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_Provider_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1,@iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';