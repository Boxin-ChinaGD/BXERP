SELECT '++++++++++++++++++ Test_SP_StaffBelonging_RetrieveN.sql ++++++++++++++++++++';

SELECT '--------------------- case1:��ѯ����openID������˾-------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_StaffBelonging_RetrieveN(@iErrorCode, @sErrorMsg);
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'case1 Testing Result';