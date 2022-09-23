SELECT '++++++++++++++++++ Test_SP_Staff_Retrieve1.sql ++++++++++++++++++++';

SELECT '-------------------- case 1������id��ѯ���ŵ�ͻ� -------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;
SET @sPhone = '';
SET @iInvolvedResigned = 1;
SET @iReturnSalt = 0;

CALL SP_Staff_Retrieve1(@iErrorCode, @sErrorMsg, @iID, @sPhone, @iInvolvedResigned, @iReturnSalt);

SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

SELECT '-------------------- case2�����ݵ绰�����ѯ���ŵ�ͻ� -------------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @sPhone = '15854320895';
SET @iInvolvedResigned = 1;
SET @iReturnSalt = 0;

CALL SP_Staff_Retrieve1(@iErrorCode, @sErrorMsg, @iID, @sPhone, @iInvolvedResigned, @iReturnSalt);

SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

SELECT '-------------------- case3������״̬�룬��ѯ��������ְ���ŵ�ͻ� -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @sPhone = '';
SET @iInvolvedResigned = 0;
SET @iReturnSalt = 0;

CALL SP_Staff_Retrieve1(@iErrorCode, @sErrorMsg, @iID, @sPhone, @iInvolvedResigned, @iReturnSalt);

SELECT @sErrorMsg;
SELECT IF(found_rows() >= 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';

SELECT '-------------------- case4������״̬�룬�绰���룬id����ѯ����ְ���ŵ�ͻ� -------------------------' AS 'Case4';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;
SET @sPhone = '13144496272';
SET @iInvolvedResigned = 0;
SET @iReturnSalt = 0;

CALL SP_Staff_Retrieve1(@iErrorCode, @sErrorMsg, @iID, @sPhone, @iInvolvedResigned, @iReturnSalt);

SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';

SELECT '-------------------- case5�����ݵ绰���룬id����ѯ���ŵ�ͻ� -------------------------' AS 'Case5';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;
SET @sPhone = '13144496272';
SET @iInvolvedResigned = 1;
SET @iReturnSalt = 0;

CALL SP_Staff_Retrieve1(@iErrorCode, @sErrorMsg, @iID, @sPhone, @iInvolvedResigned, @iReturnSalt);

SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';

SELECT '-------------------- case6������״̬�룬id����ѯ����ְ���ŵ�ͻ� -------------------------' AS 'Case6';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;
SET @sPhone = '';
SET @iInvolvedResigned = 1;
SET @iReturnSalt = 0;

CALL SP_Staff_Retrieve1(@iErrorCode, @sErrorMsg, @iID, @sPhone, @iInvolvedResigned, @iReturnSalt);

SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case6 Testing Result';

SELECT '-------------------- case7�����ݵ绰���룬״̬�룬��ѯ����ְ�ŵ�ͻ� -------------------------' AS 'Case7';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @sPhone = '13144496272';
SET @iInvolvedResigned = 1;
SET @iReturnSalt = 0;

CALL SP_Staff_Retrieve1(@iErrorCode, @sErrorMsg, @iID, @sPhone, @iInvolvedResigned, @iReturnSalt);

SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case7 Testing Result';

SELECT '--------------------- case8��û��������ȫ��ʹ��Ĭ�ϲ��� -------------------------' AS 'Case8';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @sPhone = '';
SET @iInvolvedResigned = 1;
SET @iReturnSalt = 0;

CALL SP_Staff_Retrieve1(@iErrorCode, @sErrorMsg, @iID, @sPhone, @iInvolvedResigned, @iReturnSalt);

SELECT @sErrorMsg;
SELECT IF(found_rows() >= 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case8 Testing Result';

SELECT '--------------------- case9��ʹ�ò����ڵ�ID���в�ѯ -------------------------' AS 'Case9';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = -1;
SET @sPhone = '';
SET @iInvolvedResigned = 1;
SET @iReturnSalt = 0;

CALL SP_Staff_Retrieve1(@iErrorCode, @sErrorMsg, @iID, @sPhone, @iInvolvedResigned, @iReturnSalt);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case9 Testing Result';

SELECT '--------------------- case10��ʹ�ò����ڵĵ绰������в�ѯ -------------------------' AS 'Case10';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @sPhone = '333333333333333333';
SET @iInvolvedResigned = 1;
SET @iReturnSalt = 0;

CALL SP_Staff_Retrieve1(@iErrorCode, @sErrorMsg, @iID, @sPhone, @iInvolvedResigned, @iReturnSalt);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case10 Testing Result';