SELECT '++++++++++++++++++ Test_SP_Category_RetrieveN_CheckUniqueField.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:��ѯһ�������ڵ���ƷС�� -------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iFieldToCheck = 1;
SET @sString1 = '�����·�û��';

CALL SP_Category_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iFieldToCheck, @sString1);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

SELECT '-------------------- Case2:��ѯһ���Ѿ����ڵ���ƷС�� -------------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iInt1 = 1;
SET @sString1 = '����';

CALL SP_Category_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iFieldToCheck, @sString1);

SELECT IF(@sErrorMsg = '����ƷС���Ѵ���' AND @iErrorCode = 1, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';


SELECT '-------------------- Case3:��ѯһ���Ѿ����ڵ���ƷС��,�������ID���Ѵ��ڵ���ƷС���ID��ͬ -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 2;
SET @iFieldToCheck = 1;
SET @sString1 = '����';

CALL SP_Category_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iFieldToCheck, @sString1);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';



SELECT '-------------------- Case4:��ѯһ���Ѿ����ڵ���ƷС��,�������ID���Ѵ��ڵ���ƷС���ID����ͬ -------------------------' AS 'Case4';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 3;
SET @iFieldToCheck = 1;
SET @sString1 = '����';

CALL SP_Category_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iFieldToCheck, @sString1);

SELECT IF(@sErrorMsg = '����ƷС���Ѵ���' AND @iErrorCode = 1, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';