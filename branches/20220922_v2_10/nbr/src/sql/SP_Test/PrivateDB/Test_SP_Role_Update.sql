-- case1:�����޸�
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 3;
SET @sName = '��๤';

CALL SP_Role_Update(@iErrorCode, @sErrorMsg, @iID, @sName);

SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';

-- case2���޸ĵĽ�ɫȨ���Ѵ��ڣ�������Ϊ1
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 3;
SET @sName = '�곤';

CALL SP_Role_Update(@iErrorCode, @sErrorMsg, @iID, @sName);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 1, '���Գɹ�', '����ʧ��') AS 'Testing Result';
