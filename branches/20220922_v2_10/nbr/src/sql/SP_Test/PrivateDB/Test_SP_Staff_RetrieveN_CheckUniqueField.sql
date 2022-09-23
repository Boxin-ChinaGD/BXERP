SELECT '++++++++++++++++++ Test_SP_staff_RetrieveN_CheckUniqueField.sql ++++++++++++++++++++';

SELECT '--------------------- case1: ��ѯһ�������ڵ�Ա�����ֻ�����------------------------------' AS 'case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iInt1 = 1;
SET @sString1 = '33144496272';

CALL SP_Staff_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iInt1, @sString1);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'���Գɹ�','����ʧ��') AS 'Case1 Testing Result';


SELECT '--------------------- case2: ��ѯһ���Ѵ��ڵ�Ա�����ֻ�����------------------------------' AS 'case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iInt1 = 1;
SET @sString1 = '13144496272';

CALL SP_Staff_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iInt1, @sString1);

SELECT IF(@sErrorMsg = '���ֻ����Ѵ���' AND @iErrorCode = 1,'���Գɹ�','����ʧ��') AS 'Case2 Testing Result';


SELECT '--------------------- case3: ��ѯһ���Ѵ��ڵ���ְԱ�����ֻ�����------------------------------' AS 'case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iInt1 = 1;
SET @sString1 = '13196721886';

CALL SP_Staff_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iInt1, @sString1);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'���Գɹ�','����ʧ��') AS 'Case3 Testing Result';


SELECT '--------------------- case4: ��ѯһ�������ڵ�Ա�������֤��------------------------------' AS 'case4';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iInt1 = 2;
SET @sString1 = '540883198412111666';

CALL SP_Staff_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iInt1, @sString1);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'���Գɹ�','����ʧ��') AS 'Case4 Testing Result';


SELECT '--------------------- case5: ��ѯһ���Ѵ��ڵ�Ա�������֤��------------------------------' AS 'case5';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iInt1 = 2;
SET @sString1 = '440883198412111666';

CALL SP_Staff_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iInt1, @sString1);

SELECT IF(@sErrorMsg = '�����֤���Ѵ���' AND @iErrorCode = 1,'���Գɹ�','����ʧ��') AS 'Case5 Testing Result';


SELECT '--------------------- case6: ��ѯһ���Ѵ��ڵ���ְԱ�������֤��------------------------------' AS 'case6';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iInt1 = 2;
SET @sString1 = '341522198412111666';

CALL SP_Staff_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iInt1, @sString1);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'���Գɹ�','����ʧ��') AS 'Case6 Testing Result';


SELECT '--------------------- case7: ��ѯһ�������ڵ�Ա����΢�ź�------------------------------' AS 'case7';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iInt1 = 3;
SET @sString1 = 'fffff2f';

CALL SP_Staff_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iInt1, @sString1);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'���Գɹ�','����ʧ��') AS 'Case7 Testing Result';


SELECT '--------------------- case8: ��ѯһ���Ѵ��ڵ�Ա����΢�ź�------------------------------' AS 'case8';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iInt1 = 3;
SET @sString1 = 'a326dsd';

CALL SP_Staff_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iInt1, @sString1);

SELECT IF(@sErrorMsg = '��΢�ź��Ѵ���' AND @iErrorCode = 1,'���Գɹ�','����ʧ��') AS 'Case8 Testing Result';


SELECT '--------------------- case9: ��ѯһ���Ѵ��ڵ���ְԱ����΢�ź�------------------------------' AS 'case9';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iInt1 = 3;
SET @sString1 = 'd2sasb4';

CALL SP_Staff_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iInt1, @sString1);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'���Գɹ�','����ʧ��') AS 'Case9 Testing Result';


SELECT '--------------------- case10: ��ѯһ���Ѵ��ڵ�Ա�����ֻ�����,�������ID���Ѵ��ڵ�Ա�����ֻ������Ӧ��Ա��ID��ͬ------------------------------' AS 'case10';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 2;
SET @iInt1 = 1;
SET @sString1 = '13144496272';

CALL SP_Staff_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iInt1, @sString1);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'���Գɹ�','����ʧ��') AS 'Case10 Testing Result';


SELECT '--------------------- case11: ��ѯһ���Ѵ��ڵ�Ա�������֤��,�������ID���Ѵ��ڵ�Ա�������֤�Ŷ�Ӧ��Ա��ID��ͬ------------------------------' AS 'case11';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 2;
SET @iInt1 = 2;
SET @sString1 = '440883198412111666';

CALL SP_Staff_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iInt1, @sString1);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'���Գɹ�','����ʧ��') AS 'Case11 Testing Result';


SELECT '--------------------- case12: ��ѯһ���Ѵ��ڵ�Ա����΢�ź�,�������ID���Ѵ��ڵ�Ա����΢�źŶ�Ӧ��Ա��ID��ͬ------------------------------' AS 'case12';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 2;
SET @iInt1 = 3;
SET @sString1 = 'a326dsd';

CALL SP_Staff_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iInt1, @sString1);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'���Գɹ�','����ʧ��') AS 'Case12 Testing Result';


SELECT '--------------------- case13: ��ѯһ���Ѵ��ڵ�Ա����΢�ź�,�����ID���Ѵ��ڵ�Ա����΢�źŶ�Ӧ��Ա��ID����ͬ------------------------------' AS 'case13';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 3;
SET @iInt1 = 3;
SET @sString1 = 'a326dsd';

CALL SP_Staff_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iInt1, @sString1);

SELECT IF(@sErrorMsg = '��΢�ź��Ѵ���' AND @iErrorCode = 1,'���Գɹ�','����ʧ��') AS 'Case13 Testing Result';


SELECT '--------------------- case14: ��ѯһ���Ѵ��ڵ�Ա�������֤��,�����ID���Ѵ��ڵ�Ա�������֤�Ŷ�Ӧ��Ա��ID����ͬ------------------------------' AS 'case14';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 3;
SET @iInt1 = 2;
SET @sString1 = '440883198412111666';

CALL SP_Staff_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iInt1, @sString1);

SELECT IF(@sErrorMsg = '�����֤���Ѵ���' AND @iErrorCode = 1,'���Գɹ�','����ʧ��') AS 'Case14 Testing Result';


SELECT '--------------------- case15: ��ѯһ���Ѵ��ڵ�Ա�����ֻ�����,�����ID���Ѵ��ڵ�Ա�����ֻ������Ӧ��Ա��ID����ͬ------------------------------' AS 'case15';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 3;
SET @iInt1 = 1;
SET @sString1 = '13144496272';

CALL SP_Staff_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iInt1, @sString1);

SELECT IF(@sErrorMsg = '���ֻ����Ѵ���' AND @iErrorCode = 1,'���Գɹ�','����ʧ��') AS 'Case15 Testing Result';