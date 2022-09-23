SELECT '++++++++++++++++++ Test_SP_Shop_RetrieveN_CheckUniqueField.sql ++++++++++++++++++++';

SELECT '--------------------- case1: �����ŵ�ʱ������һ�������ڵ��ŵ����ƣ���������Ƿ��Ѵ���------------------------------' AS 'case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iFieldToCheckUnique = 1;
SET @sUniqueField = '����С꿲�';

CALL SP_Shop_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iFieldToCheckUnique, @sUniqueField);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'���Գɹ�','����ʧ��') AS 'Case1 Testing Result';



SELECT '--------------------- case2: �����ŵ�ʱ������һ���Ѵ��ڵ��ŵ����ƣ���������Ƿ��Ѵ���------------------------------' AS 'case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iFieldToCheckUnique = 1;
SET @sUniqueField = '���С����';

CALL SP_Shop_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iFieldToCheckUnique, @sUniqueField);

SELECT IF(@sErrorMsg = '���ŵ������Ѵ���' AND @iErrorCode = 1,'���Գɹ�','����ʧ��') AS 'Case2 Testing Result';



SELECT '--------------------- case3: �����ŵ�ʱ������һ���Ѵ��ڵ��ŵ����ƣ���������Ƿ��Ѵ���------------------------------' AS 'case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 3;
SET @iFieldToCheckUnique = 1;
SET @sUniqueField = '���С����';

CALL SP_Shop_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iFieldToCheckUnique, @sUniqueField);
SELECT @iErrorCode, @sErrorMsg;
SELECT IF(@iErrorCode = 0,'���Գɹ�','����ʧ��') AS 'Case3 Testing Result';


SELECT '--------------------- case4: �����ŵ�ʱ������һ���Ѵ��ڵ��ŵ����ƣ���������Ƿ��Ѵ���------------------------------' AS 'case4';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;
SET @iFieldToCheckUnique = 1;
SET @sUniqueField = '���С����';

CALL SP_Shop_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iFieldToCheckUnique, @sUniqueField);

SELECT IF(@sErrorMsg = '���ŵ������Ѵ���' AND @iErrorCode = 1,'���Գɹ�','����ʧ��') AS 'Case4 Testing Result';