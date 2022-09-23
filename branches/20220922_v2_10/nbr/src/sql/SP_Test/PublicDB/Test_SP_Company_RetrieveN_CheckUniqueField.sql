SELECT '++++++++++++++++++ Test_SP_Company_RetrieveN_CheckUniqueField.sql ++++++++++++++++++++';

SELECT '--------------------- case1: ��ѯһ�������ڵĹ�˾����------------------------------' AS 'case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iFieldToCheckUnique = 1;
SET @sUniqueField = '��������';

CALL SP_Company_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iFieldToCheckUnique, @sUniqueField);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'���Գɹ�','����ʧ��') AS 'Case1 Testing Result';


SELECT '--------------------- case2: ��ѯһ���Ѵ��ڵĹ�˾����------------------------------' AS 'case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iFieldToCheckUnique = 1;
SET @sUniqueField = 'BXһ�ŷֹ�˾';

CALL SP_Company_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iFieldToCheckUnique, @sUniqueField);

SELECT IF(@sErrorMsg = '�ù�˾�����Ѵ���' AND @iErrorCode = 1,'���Գɹ�','����ʧ��') AS 'Case2 Testing Result';

SELECT '--------------------- case3: ��ѯһ�������ڵ�Ӫҵִ�պ�------------------------------' AS 'case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iFieldToCheckUnique = 2;
SET @sUniqueField = 'ZX987654321';

CALL SP_Company_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iFieldToCheckUnique, @sUniqueField);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'���Գɹ�','����ʧ��') AS 'Case3 Testing Result';


SELECT '--------------------- case4: ��ѯһ���Ѵ��ڵ�Ӫҵִ�պ�------------------------------' AS 'case4';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iFieldToCheckUnique = 2;
SET @sUniqueField = '111111111111111';

CALL SP_Company_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iFieldToCheckUnique, @sUniqueField);

SELECT IF(@sErrorMsg = '��Ӫҵִ�պ��Ѵ���' AND @iErrorCode = 1,'���Գɹ�','����ʧ��') AS 'Case4 Testing Result';

SELECT '--------------------- case9: ��ѯһ�������ڵĹ�˾��DB����------------------------------' AS 'case9';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iFieldToCheckUnique = 5;
SET @sUniqueField = 'zxr';

CALL SP_Company_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iFieldToCheckUnique, @sUniqueField);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'���Գɹ�','����ʧ��') AS 'Case9 Testing Result';


SELECT '--------------------- case10: ��ѯһ���Ѵ��ڵĹ�˾��DB����------------------------------' AS 'case10';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iFieldToCheckUnique = 5;
SET @sUniqueField = 'nbr';

CALL SP_Company_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iFieldToCheckUnique, @sUniqueField);

SELECT IF(@sErrorMsg = '��DB�����Ѵ���' AND @iErrorCode = 1,'���Գɹ�','����ʧ��') AS 'Case10 Testing Result';

SELECT '--------------------- case11: ��ѯһ���Ѵ��ڵĹ�˾����,�����ID���Ѵ��ڵĹ�˾���ƶ�Ӧ��ID��ͬ------------------------------' AS 'case11';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;
SET @iFieldToCheckUnique = 1;
SET @sUniqueField = 'BXһ�ŷֹ�˾';

CALL SP_Company_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iFieldToCheckUnique, @sUniqueField);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'���Գɹ�','����ʧ��') AS 'Case11 Testing Result';


SELECT '--------------------- case12: ��ѯһ���Ѵ��ڵ�Ӫҵִ�պţ��������ID���Ѵ��ڵ�Ӫҵִ�պŶ�Ӧ��ID��ͬ------------------------------' AS 'case12';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;
SET @iFieldToCheckUnique = 2;
SET @sUniqueField = '111111111111111';

CALL SP_Company_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iFieldToCheckUnique, @sUniqueField);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'���Գɹ�','����ʧ��') AS 'Case12 Testing Result';


SELECT '--------------------- case15: ��ѯһ���Ѵ��ڵĹ�˾DB���ƣ��������ID���Ѵ��ڵĹ�˾DB���ƶ�Ӧ��ID��ͬ------------------------------' AS 'case15';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;
SET @iFieldToCheckUnique = 5;
SET @sUniqueField = 'nbr';

CALL SP_Company_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iFieldToCheckUnique, @sUniqueField);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'���Գɹ�','����ʧ��') AS 'Case15 Testing Result';


SELECT '--------------------- case16: ��ѯһ�������ڵ����̻���------------------------------' AS 'case16';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iFieldToCheckUnique = 6;
SET @sUniqueField = '6666666666';

CALL SP_Company_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iFieldToCheckUnique, @sUniqueField);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'���Գɹ�','����ʧ��') AS 'Case16 Testing Result';


SELECT '--------------------- case17: ��ѯһ���Ѵ��ڵ����̻���------------------------------' AS 'case17';
-- 
SET @sSubmchid = 'test17subm';
INSERT INTO nbr_bx.t_company (F_Name, F_SN, F_BusinessLicenseSN, F_BusinessLicensePicture, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_DBUserName, F_DBUserPassword, F_Status,F_Submchid, F_BrandName, F_ExpireDatetime)
VALUES ('BX���ŷֹ�˾', '668869', '77777777777777', '/p/123.jpg', '�ϰ�1��', '13123615881', '000000', 'a13123615881', 'nbr11', '12345678901234567890123456789099', 'nbr11', 'WEF#EGEHEH$$^*DI', 0, @sSubmchid, '�޹���', '2030-12-02 01:01:01');
SET @iID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iFieldToCheckUnique = 6;
SET @sUniqueField = @sSubmchid;
SELECT F_Submchid FROM nbr_bx.t_company WHERE F_ID = 1;
CALL SP_Company_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iFieldToCheckUnique, @sUniqueField);

SELECT IF(@sErrorMsg = '�����̻����Ѵ���' AND @iErrorCode = 1,'���Գɹ�','����ʧ��') AS 'Case17 Testing Result';
DELETE FROM nbr_bx.t_company WHERE F_ID = @iID;