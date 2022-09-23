SELECT '++++++++++++++++++ Test_SP_Commodity_RetrieveN_CheckUniqueField.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:��ѯһ�������ڵ���Ʒ���� -------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iInt1 = 1;
SET @sString1 = '�����·�û��';

CALL SP_Commodity_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iInt1, @sString1);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

SELECT '-------------------- Case2:��ѯһ���Ѿ����ڵ���Ʒ���� -------------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iInt1 = 1;
SET @sString1 = '�ɱȿ���Ƭ';

CALL SP_Commodity_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iInt1, @sString1);

SELECT IF(@sErrorMsg = '����Ʒ�����Ѿ�����' AND @iErrorCode = 1, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

SELECT '-------------------- Case3:��ѯһ���Ѿ�ɾ������Ʒ���� -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iInt1 = 1;
SET @sString1 = '�����ཷζ��Ƭ1';

CALL SP_Commodity_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iInt1, @sString1);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';


SELECT '-------------------- Case4:��ѯһ���Ѿ����ڵ���Ʒ����,�������iID�ǵ����Ѵ��ڵ���Ʒ���Ƶ���ƷID -------------------------' AS 'Case4';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;
SET @iInt1 = 1;
SET @sString1 = '�ɱȿ���Ƭ';

CALL SP_Commodity_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iInt1, @sString1);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';


SELECT '-------------------- Case5:��ѯһ���Ѿ����ڵ���Ʒ����,�����iID�������Ѵ��ڵ���Ʒ���Ƶ���ƷID -------------------------' AS 'Case5';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 2;
SET @iInt1 = 1;
SET @sString1 = '�ɱȿ���Ƭ';

CALL SP_Commodity_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iInt1, @sString1);

SELECT IF(@sErrorMsg = '����Ʒ�����Ѿ�����' AND @iErrorCode = 1, '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';


SELECT '------- Case6:��ѯһ���Ѿ����ڵ���Ʒ����,�����iID�ǵ����Ѵ��ڵ���Ʒ���Ƶ���ƷID,���Ǹ���Ʒ�Ѿ�ɾ���ˣ�״̬Ϊ2��-------' AS 'Case6';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 49;
SET @iInt1 = 1;
SET @sString1 = '�����ཷζ��Ƭ1';

CALL SP_Commodity_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iInt1, @sString1);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case6 Testing Result';