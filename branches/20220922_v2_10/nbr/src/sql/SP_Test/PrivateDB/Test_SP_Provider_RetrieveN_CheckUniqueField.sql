SELECT '++++++++++++++++++ Test_SP_Provider_RetrieveN_CheckUniqueField.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:��ѯһ�������ڵĹ�Ӧ������ -------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @fieldToCheckUnique = 1;
SET @uniqueField = '�����·�û��';

CALL SP_Provider_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @fieldToCheckUnique, @uniqueField);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

SELECT '-------------------- Case2:��ѯһ���Ѿ����ڵĹ�Ӧ������ -------------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @fieldToCheckUnique = 1;
SET @uniqueField = '���Ϲ�Ӧ��';

CALL SP_Provider_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @fieldToCheckUnique, @uniqueField);

SELECT IF(@sErrorMsg = '�ù�Ӧ�������Ѵ���' AND @iErrorCode = 1, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';


SELECT '-------------------- Case3:��ѯһ���Ѿ����ڵĹ�Ӧ������,�������ID���Ѵ��ڵĹ�Ӧ�����ƵĹ�Ӧ��ID��ͬ -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 2;
SET @fieldToCheckUnique = 1;
SET @uniqueField = '���Ϲ�Ӧ��';

CALL SP_Provider_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @fieldToCheckUnique, @uniqueField);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';


SELECT '-------------------- Case4:��ѯһ�������ڵ���ϵ�˵绰 -------------------------' AS 'Case4';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @fieldToCheckUnique = 2;
SET @uniqueField = '93129355441';

CALL SP_Provider_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @fieldToCheckUnique, @uniqueField);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';

SELECT '-------------------- Case5:��ѯһ���Ѿ����ڵ���ϵ�˵绰 -------------------------' AS 'Case5';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @fieldToCheckUnique = 2;
SET @uniqueField = '13129355441';

CALL SP_Provider_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @fieldToCheckUnique, @uniqueField);

SELECT IF(@sErrorMsg = '����ϵ�˵绰�Ѵ���' AND @iErrorCode = 1, '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';


SELECT '-------------------- Case6:��ѯһ���Ѿ����ڵ���ϵ�˵绰,�������ID���Ѵ��ڵ���ϵ�˵绰�Ĺ�Ӧ��ID��ͬ -------------------------' AS 'Case6';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 2;
SET @fieldToCheckUnique = 2;
SET @uniqueField = '13129355441';

CALL SP_Provider_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @fieldToCheckUnique, @uniqueField);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case6 Testing Result';



SELECT '-------------------- Case7:��ѯһ���Ѿ����ڵĹ�Ӧ������,�����ID���Ѵ��ڵĹ�Ӧ�����ƵĹ�Ӧ��ID����ͬ -------------------------' AS 'Case7';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 3;
SET @fieldToCheckUnique = 1;
SET @uniqueField = '���Ϲ�Ӧ��';

CALL SP_Provider_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @fieldToCheckUnique, @uniqueField);

SELECT IF(@sErrorMsg = '�ù�Ӧ�������Ѵ���' AND @iErrorCode = 1, '���Գɹ�', '����ʧ��') AS 'Case7 Testing Result';


SELECT '-------------------- Case8:��ѯһ���Ѿ����ڵ���ϵ�˵绰,�����ID���Ѵ��ڵ���ϵ�˵绰�Ĺ�Ӧ��ID����ͬ -------------------------' AS 'Case8';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 3;
SET @fieldToCheckUnique = 2;
SET @uniqueField = '13129355441';

CALL SP_Provider_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @fieldToCheckUnique, @uniqueField);

SELECT IF(@sErrorMsg = '����ϵ�˵绰�Ѵ���' AND @iErrorCode = 1, '���Գɹ�', '����ʧ��') AS 'Case8 Testing Result'