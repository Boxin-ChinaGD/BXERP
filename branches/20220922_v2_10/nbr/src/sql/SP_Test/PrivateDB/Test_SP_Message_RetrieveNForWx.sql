SELECT '++++++++++++++++++ Test_SP_Message_RetrieveNForWx.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:����status��ѯ -------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @status = 0;
SET @iCompanyID = -1;

CALL SP_Message_RetrieveNForWx(@iErrorCode, @sErrorMsg, @status , @iCompanyID);
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

SELECT '-------------------- Case2:����companyID��ѯ -------------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @status = -1;
SET @iCompanyID = 1;

CALL SP_Message_RetrieveNForWx(@iErrorCode, @sErrorMsg, @status , @iCompanyID);
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

SELECT '-------------------- Case3:��ѯȫ�� -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @status = -1;
SET @iCompanyID = -1;

CALL SP_Message_RetrieveNForWx(@iErrorCode, @sErrorMsg, @status , @iCompanyID);
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';

SELECT '-------------------- Case4:����companyID��Status��ѯ -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @status = 1;
SET @iCompanyID = 1;

CALL SP_Message_RetrieveNForWx(@iErrorCode, @sErrorMsg, @status , @iCompanyID);
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result'
