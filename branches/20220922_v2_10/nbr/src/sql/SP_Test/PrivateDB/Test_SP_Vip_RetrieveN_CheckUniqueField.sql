SELECT '++++++++++++++++++ Test_SP_Vip_RetrieveN_CheckUniqueField.sql ++++++++++++++++++++';

SELECT '--------------------- case1: ��ѯһ�������ڵĻ�Ա���ֻ�����------------------------------' AS 'case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @fieldToCheckUnique = 1;
SET @queryKeyword = '33144496272';

CALL SP_Vip_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @fieldToCheckUnique, @queryKeyword);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'���Գɹ�','����ʧ��') AS 'Case1 Testing Result';


SELECT '--------------------- case2: ��ѯһ���Ѵ��ڵĻ�Ա���ֻ�����------------------------------' AS 'case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @fieldToCheckUnique = 1;
SET @queryKeyword = '13545678110';

CALL SP_Vip_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @fieldToCheckUnique, @queryKeyword);

SELECT IF(@sErrorMsg = '���ֻ����Ѵ���' AND @iErrorCode = 1,'���Գɹ�','����ʧ��') AS 'Case2 Testing Result';



SELECT '--------------------- case3: ��ѯһ�������ڵĻ�Ա�����֤��------------------------------' AS 'case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @fieldToCheckUnique = 2;
SET @queryKeyword = '540883198412111666';

CALL SP_Vip_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @fieldToCheckUnique, @queryKeyword);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'���Գɹ�','����ʧ��') AS 'Case4 Testing Result';


SELECT '--------------------- case4: ��ѯһ���Ѵ��ڵĻ�Ա�����֤��------------------------------' AS 'case4';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @fieldToCheckUnique = 2;
SET @queryKeyword = '320803199707016031';

CALL SP_Vip_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @fieldToCheckUnique, @queryKeyword);

SELECT IF(@sErrorMsg = '�����֤���Ѵ���' AND @iErrorCode = 1,'���Գɹ�','����ʧ��') AS 'Case4 Testing Result';




SELECT '--------------------- case9: ��ѯһ�������ڵĻ�Ա������------------------------------' AS 'case9';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @fieldToCheckUnique = 5;
SET @queryKeyword = '623456@bx.vip';

CALL SP_Vip_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @fieldToCheckUnique, @queryKeyword);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'���Գɹ�','����ʧ��') AS 'Case9 Testing Result';


SELECT '--------------------- case10: ��ѯһ���Ѵ��ڵĻ�Ա������------------------------------' AS 'case10';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @fieldToCheckUnique = 5;
SET @queryKeyword = '123456@bx.vip';

CALL SP_Vip_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @fieldToCheckUnique, @queryKeyword);

SELECT IF(@sErrorMsg = '�������Ѵ���' AND @iErrorCode = 1,'���Գɹ�','����ʧ��') AS 'Case10 Testing Result';

-- account�ֶ���ɾ��
--	SELECT '--------------------- case11: ��ѯһ�������ڵĻ�Ա�ĵ�¼�˺�------------------------------' AS 'case11';
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @iID = 0;
--	SET @fieldToCheckUnique = 6;
--	SET @queryKeyword = '623456';
--	
--	CALL SP_Vip_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @fieldToCheckUnique, @queryKeyword);
--	
--	SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'���Գɹ�','����ʧ��') AS 'Case9 Testing Result';
--	
--	
--	SELECT '--------------------- case12: ��ѯһ���Ѵ��ڵĻ�Ա�ĵ�¼�˺�------------------------------' AS 'case12';
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @iID = 0;
--	SET @fieldToCheckUnique = 6;
--	SET @queryKeyword = '123456';
--	
--	CALL SP_Vip_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @fieldToCheckUnique, @queryKeyword);
--	
--	SELECT IF(@sErrorMsg = '�õ�¼�˺��Ѵ���' AND @iErrorCode = 1,'���Գɹ�','����ʧ��') AS 'Case12 Testing Result';



SELECT '--------------------- case13: ��ѯһ���Ѵ��ڵĻ�Ա���ֻ�����,�����ID���Ѵ��ڵĻ�Ա�ֻ������ID��ͬ------------------------------' AS 'case13';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;
SET @fieldToCheckUnique = 1;
SET @queryKeyword = '13545678110';

CALL SP_Vip_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @fieldToCheckUnique, @queryKeyword);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'���Գɹ�','����ʧ��') AS 'Case13 Testing Result';



SELECT '--------------------- case14: ��ѯһ���Ѵ��ڵĻ�Ա�����֤�ţ��������ID���Ѵ��ڵĻ�Ա�����֤�ŵ�ID��ͬ------------------------------' AS 'case14';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;
SET @fieldToCheckUnique = 2;
SET @queryKeyword = '123456';

CALL SP_Vip_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @fieldToCheckUnique, @queryKeyword);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'���Գɹ�','����ʧ��') AS 'Case14 Testing Result';





SELECT '--------------------- case17: ��ѯһ���Ѵ��ڵĻ�Ա�����䣬�������ID���Ѵ��ڵĻ�Ա�������ID��ͬ------------------------------' AS 'case17';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;
SET @fieldToCheckUnique = 5;
SET @queryKeyword = '123456@bx.vip';

CALL SP_Vip_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @fieldToCheckUnique, @queryKeyword);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'���Գɹ�','����ʧ��') AS 'Case17 Testing Result';


-- account�ֶ���ɾ��
--	SELECT '--------------------- case18: ��ѯһ���Ѵ��ڵĻ�Ա�ĵ�¼�˺ţ��������ID���Ѵ��ڵĻ�Ա��¼�˺ŵ�¼------------------------------' AS 'case18';
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @iID = 1;
--	SET @fieldToCheckUnique = 6;
--	SET @queryKeyword = '123456';
--	
--	CALL SP_Vip_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @fieldToCheckUnique, @queryKeyword);
--	
--	SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'���Գɹ�','����ʧ��') AS 'Case18 Testing Result';