SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_StateMachine_RetrieveN(@iErrorCode, @sErrorMsg);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 3, '���Գɹ�', '����ʧ��') AS 'Testing Result';