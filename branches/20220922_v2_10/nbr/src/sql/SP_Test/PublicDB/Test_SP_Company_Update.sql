SELECT '++++++++++++++++++Test_SP_Company_Update.sql+++++++++++++++++++++++';
USE nbr_bx;
SELECT '-----------------CASE1: ��������------------------' AS 'CASE1';
INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword)
VALUES ('1�ŷֹ�˾', 'FB452352354516', 'url=dftsfd4h5sfl',  '/p/11.jpg', '�ϰ�1��', '13146455881', '000000', 'sdf1h4sfh', 'ceshi1', '123456u1', 0, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI');

SET @iID = LAST_INSERT_ID();
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '1�ŷֹ�˾';
SET @sBossName = '����һ��';
SET @sBusinessLicenseSN = 'UPDATE1';
SET @sBusinessLicensePicture = 'UPDATE1';
SET @sBossPhone = 'UPDATE1';
SET @sBossWechat = 'UPDATE1';
SET @sBrandName = '�޹���';
SET @sLogo = '';
-- SET @sKey = 'UPDATE1';

CALL SP_Company_Update(@iErrorCode, @sErrorMsg, @iID, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossWechat, @sKey, @sName, @sBossName, @sBrandName, @sLogo);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM nbr_bx.t_company WHERE F_Name = @sName AND F_BossName = @sBossName AND F_BusinessLicenseSN = @sBusinessLicenseSN 
								AND F_BusinessLicensePicture = @sBusinessLicensePicture AND F_BossPhone = @sBossPhone AND F_BossWechat = @sBossWechat AND F_BrandName = @sBrandName; -- AND F_Key = @sKey;
SELECT IF(FOUND_ROWS() = 1 AND @iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

DELETE FROM nbr_bx.t_company WHERE F_ID = @iID;

--	SELECT '-----------------CASE2: ���������ֶ�Ϊnull ���ش�����Ϊ7------------------' AS 'CASE2';-- �Ѿ��ƶ���java�������
--	INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword)
--	VALUES ('1�ŷֹ�˾', 'FB452352354516', 'url=dftsfd4h5sfl', '�ϰ�1��', '13146455881', '000000', 'sdf1h4sfh', 'ceshi1', '123456u1', 0, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI');
--	
--	SET @iID = LAST_INSERT_ID();
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @sBusinessLicenseSN = '';
--	SET @sName = '2�ŷֹ�˾';
--	SET @sBossName = '����һ��';
--	SET @sBusinessLicensePicture = 'url=FB232323323';
--	SET @sBossPhone = '13324544444';
--	SET @sBossWechat = 'cjs123456';
--	SET @sKey = '123456u2';
--	SET @sAuthorizerAppid = '';
--	SET @sAuthorizerRefreshToken = '';
--	SET @iFuncInfo = 0;
--	
--	CALL SP_Company_Update(@iErrorCode, @sErrorMsg, @iID, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossWechat, @sKey, @sName, @sBossName,@sAuthorizerAppid,@sAuthorizerRefreshToken,@iFuncInfo);
--	SELECT @sErrorMsg;
--	SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
--	
--	DELETE FROM nbr_bx.t_company WHERE F_ID = @iID;

--	SELECT '-----------------CASE3: ����������Ҫ�� ���ش�����Ϊ7 ------------------' AS 'CASE3';-- �Ѿ��ƶ���java�������
--	INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword)
--	VALUES ('1�ŷֹ�˾', 'FB452352354516', 'url=dftsfd4h5sfl', '�ϰ�1��', '13146455881', '000000', 'sdf1h4sfh', 'ceshi1', '123456u1', 0, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI');
--	
--	SET @iID = LAST_INSERT_ID();
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @sBusinessLicenseSN = '';
--	SET @sBusinessLicensePicture = '';
--	SET @sName = '3�ŷֹ�˾';
--	SET @sBossName = '����һ��';
--	SET @sBossPhone = '1665416';
--	SET @sBossWechat = 'cjs123456';
--	SET @sKey = '123456u3';
--	SET @sAuthorizerAppid = '';
--	SET @sAuthorizerRefreshToken = '';
--	SET @iFuncInfo = 0;
--	
--	CALL SP_Company_Update(@iErrorCode, @sErrorMsg, @iID, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossWechat, @sKey, @sName, @sBossName,@sAuthorizerAppid,@sAuthorizerRefreshToken,@iFuncInfo);
--	SELECT @sErrorMsg;
--	SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';
--	
--	DELETE FROM t_company WHERE F_ID = @iID;

--	SELECT '-----------------CASE4:��˾����Ϊ��------------------' AS 'CASE4';-- �Ѿ��ƶ���java�������
--	INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword)
--	VALUES ('1�ŷֹ�˾', 'FB452352354516', 'url=dftsfd4h5sfl', '�ϰ�1��', '13146455881', '000000', 'sdf1h4sfh', 'ceshi1', '123456u1', 0, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI');
--	
--	SET @iID = LAST_INSERT_ID();
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @sBusinessLicenseSN = '';
--	SET @sBusinessLicensePicture = '';
--	SET @sName = '';
--	SET @sBossName = '����һ��';
--	SET @sBossPhone = '1665416';
--	SET @sBossWechat = 'cjs123456';
--	SET @sKey = '123456u4';
--	SET @sAuthorizerAppid = '';
--	SET @sAuthorizerRefreshToken = '';
--	SET @iFuncInfo = 0;
--	
--	CALL SP_Company_Update(@iErrorCode, @sErrorMsg, @iID, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossWechat, @sKey, @sName, @sBossName,@sAuthorizerAppid,@sAuthorizerRefreshToken,@iFuncInfo);
--	
--	SELECT IF(@sErrorMsg = '����Ϊ�ջ����ݲ�����Ҫ��' AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';
--	
--	DELETE FROM nbr_bx.t_company WHERE F_ID = @iID;

--	SELECT '-----------------CASE5:�ϰ�����Ϊ��------------------' AS 'CASE5';-- �Ѿ��ƶ���java�������
--	INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword)
--	VALUES ('1�ŷֹ�˾', 'FB452352354516', 'url=dftsfd4h5sfl', '�ϰ�1��', '13146455881', '000000', 'sdf1h4sfh', 'ceshi1', '123456u1', 0, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI');
--	
--	SET @iID = LAST_INSERT_ID();
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @sBusinessLicenseSN = '';
--	SET @sBusinessLicensePicture = '';
--	SET @sName = '5�ŷֹ�˾';
--	SET @sBossName = '';
--	SET @sBossPhone = '1665416';
--	SET @sBossWechat = 'cjs123456';
--	SET @sKey = '123456u5';
--	SET @sAuthorizerAppid = '';
--	SET @sAuthorizerRefreshToken = '';
--	SET @iFuncInfo = 0;
--	
--	CALL SP_Company_Update(@iErrorCode, @sErrorMsg, @iID, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossWechat, @sKey, @sName, @sBossName,@sAuthorizerAppid,@sAuthorizerRefreshToken,@iFuncInfo);
--	
--	SELECT IF(@sErrorMsg = '����Ϊ�ջ����ݲ�����Ҫ��' AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';
--	
--	DELETE FROM nbr_bx.t_company WHERE F_ID = @iID;

--	SELECT '-----------------CASE6:��˾���ƺ��ϰ����ֶ�Ϊ��------------------' AS 'CASE6';-- �Ѿ��ƶ���java�������
--	INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword)
--	VALUES ('1�ŷֹ�˾', 'FB452352354516', 'url=dftsfd4h5sfl', '�ϰ�1��', '13146455881', '000000', 'sdf1h4sfh', 'ceshi1', '123456u1', 0, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI');
--	
--	SET @iID = LAST_INSERT_ID();
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @sBusinessLicenseSN = '';
--	SET @sBusinessLicensePicture = '';
--	SET @sName = '6�ŷֹ�˾';
--	SET @sBossName = '����һ��';
--	SET @sBossPhone = '1665416';
--	SET @sBossWechat = 'cjs123456';
--	SET @sKey = '123456u6';
--	SET @sAuthorizerAppid = '';
--	SET @sAuthorizerRefreshToken = '';
--	SET @iFuncInfo = 0;
--	
--	CALL SP_Company_Update(@iErrorCode, @sErrorMsg, @iID, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossWechat, @sKey, @sName, @sBossName,@sAuthorizerAppid,@sAuthorizerRefreshToken,@iFuncInfo);
--	
--	SELECT IF(@sErrorMsg = '����Ϊ�ջ����ݲ�����Ҫ��' AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case6 Testing Result';
--	
--	DELETE FROM t_company WHERE F_ID = @iID;

SELECT '-----------------CASE7: ����ʱ���Ѵ��ڹ�˾����------------------' AS 'CASE7';
INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword)
VALUES ('7�ŷֹ�˾', 'FB452352354516', 'url=dftsfd4h5sfl', '/p/11.jpg', '�ϰ�1��', '13146455881', '000000', 'sdf1h4sfh', 'ceshi1', '123456u1', 0, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI');
SET @iID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = 'BXһ�ŷֹ�˾';
SET @sBossName = '����һ��';
SET @sBusinessLicenseSN = 'UPDATE1';
SET @sBusinessLicensePicture = 'UPDATE1';
SET @sBossPhone = 'UPDATE1';
SET @sBossWechat = 'UPDATE1';
SET @sKey = 'UPDATE1';
SET @sBrandName = '�޹���';
SET @sLogo = 'fff';

CALL SP_Company_Update(@iErrorCode, @sErrorMsg, @iID, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossWechat, @sKey, @sName, @sBossName, @sBrandName, @sLogo);
SELECT IF(@sErrorMsg = '��˾�����ظ�' AND @iErrorCode = 1, '���Գɹ�', '����ʧ��') AS 'Case7 Testing Result';

DELETE FROM t_company WHERE F_ID = @iID;

SELECT '-----------------CASE8: ����ʱ��������˾�Ѵ��ڷǿյ�Ӫҵִ��------------------' AS 'CASE8';
INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword)
VALUES ('8�ŷֹ�˾', 'FB452352354516', NULL, '/p/11.jpg', '�ϰ�1��', '13146455881', '000000', 'sdf1h4sfh', 'ceshi1', '123456u1', 0, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI');
SET @iID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '8-1�ŷֹ�˾';
SET @sBossName = '����һ��';
SET @sBusinessLicenseSN = '111111111111111';
SET @sBusinessLicensePicture = 'UPDATE1';
SET @sBossPhone = 'UPDATE1';
SET @sBossWechat = 'UPDATE1';
SET @sKey = 'UPDATE1';
SET @sBrandName = '�޹���';
SET @sLogo = 'fafsf';

CALL SP_Company_Update(@iErrorCode, @sErrorMsg, @iID, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossWechat, @sKey, @sName, @sBossName, @sBrandName, @sLogo);
SELECT IF(@sErrorMsg = '������˾�Ѿ�������ͬ��Ӫҵִ��' AND @iErrorCode = 1, '���Գɹ�', '����ʧ��') AS 'Case8 Testing Result';
DELETE FROM t_company WHERE F_ID = @iID;

SELECT '-----------------CASE9: ����ʱ��������˾�Ѵ��ڷǿյ�Ӫҵִ����Ƭ------------------' AS 'CASE9';
SET @sBusinessLicensePicture = '/common_db/license/1.png';
INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword)
VALUES ('9�ŷֹ�˾A', 'FB4523523FS54516', @sBusinessLicensePicture, '/p/11.jpg', '�ϰ�1F��', '13146F455881', '000000', 'sdf1h4Fsfh', 'ceshi1', '123456u1', 0, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI');
SET @iIDA = LAST_INSERT_ID();
-- 
INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword)
VALUES ('9�ŷֹ�˾B', 'FB4523F52F3S54516', '111.jpg', '/p/11F.jpg', '�ϰ�1F��F', '131464F55881', '000000', 'sdf1hF4sfh', 'cesFhi1', '1234F56u1', 0, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI');
SET @iIDB = LAST_INSERT_ID();

-- 
SET @iErrorCode = 0;
SET @sErrorMsg= '';
SET @sName = '9-1�ŷֹ�˾S';
SET @sBossName = '����һ��S';
SET @sBusinessLicenseSN = 'TB2318232S3483';
SET @sBossPhone = 'UPDATSE1';
SET @sBossWechat = 'UPDATSE1';
SET @sKey = 'UPDSATE1';
SET @sBrandName = '�޹�S��';
SET @sLogo = 'sfadSssf';
-- 
CALL SP_Company_Update(@iErrorCode, @sErrorMsg, @iIDB, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossWechat, @sKey, @sName, @sBossName, @sBrandName, @sLogo);
-- 
SELECT IF(@sErrorMsg = '�ù�˾Ӫҵִ��ͼƬ�Ѵ��ڣ�����ϵ����Ա' AND @iErrorCode = 1, '���Գɹ�', '����ʧ��') AS 'Case9 Testing Result';
DELETE FROM t_company WHERE F_ID = @iIDA;
DELETE FROM t_company WHERE F_ID = @iIDB;
-- 

SELECT '-----------------CASE10: ����ʱ���Ѵ��ڹ�˾Կ��key------------------' AS 'CASE10';
INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword)
VALUES ('10�ŷֹ�˾', 'FB452352354516', 'url=dftsfd4h5sfl', '/p/11.jpg', '�ϰ�1��', '13146455881', '000000', 'sdf1h4sfh', 'ceshi1', '12345610', 0, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI');

SET @iID1 = LAST_INSERT_ID();

INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword)
VALUES ('11�ŷֹ�˾', 'FB452352354517', 'url=dftsfd4h5s99', '/p/1122.jpg', '�ϰ�2��', '13146455899', '000000', 'sdf1h4999', 'ceshi2', '66666666', 0, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI');

SET @iID2 = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '11�ŷֹ�˾';
SET @sBossName = '���ж���';
SET @sBusinessLicenseSN = 'FB452352354516case10';
SET @sBusinessLicensePicture = 'url=dftsfd4h5sflcase10';
SET @sBossPhone = '13146455881';
SET @sBossWechat = 'sdf1h4sfh';
SET @sKey = '12345610';
SET @sBrandName = '�޹���';
SET @sLogo = 'sfasq';
-- 
CALL SP_Company_Update(@iErrorCode, @sErrorMsg, @iID2, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossWechat, @sKey, @sName, @sBossName, @sBrandName, @sLogo);
SELECT IF(@sErrorMsg = '������˾�Ѵ������key' AND @iErrorCode = 1, '���Գɹ�', '����ʧ��') AS 'Case10 Testing Result';
-- 
DELETE FROM t_company WHERE F_ID = @iID1;
DELETE FROM t_company WHERE F_ID = @iID2;



SELECT '-----------------CASE12: ��˾���ں�ȡ����Ȩ ------------------' AS 'CASE12';
INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword)
VALUES ('12�ŷֹ�˾', 'FB452352354516', 'url=dftsfd4h5sFfl', '/p/11F.jpg', '�ϰ�1��', '13146455881', '000000', 'sdf1h4sfh', 'ceshi1', '12345610', 0, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI');
SET @iID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '10-12�ŷֹ�˾';
SET @sBossName = '����һ��';
SET @sBusinessLicenseSN = 'TB231823F23483';
SET @sBusinessLicensePicture = 'url=95454F534354';
SET @sBossPhone = 'UPDATE1';
SET @sBossWechat = 'UPDATE1';
SET @sKey = 'AS28ss248';
SET @sBrandName = '�޹���';
SET @sLogo = 'fadsq';
-- 
CALL SP_Company_Update(@iErrorCode, @sErrorMsg, @iID, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossWechat, @sKey, @sName, @sBossName, @sBrandName, @sLogo);
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case12 Testing Result';
DELETE FROM t_company WHERE F_ID = @iID;

SELECT '-----------------CASE13: ���ҹ�˾��Ӫҵִ�պ�logo���޸�Ϊ''  ------------------' AS 'CASE13';
INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword)
VALUES ('13�ŷֹ�˾', 'FB452352354516', 'url=dftsfd4h5sfl', '/p/11.jpg', '�ϰ�1��', '13146455881', '000000', 'sdf1h4sfh', 'ceshi1', '12345610', 0, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI');
SET @iIDA = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = 'FDAFAD';
SET @sBossName = '����FFһ��';
SET @sBusinessLicenseSN = 'TB231F82323483';
SET @sBusinessLicensePicture = '';
SET @sBossPhone = 'UPDFATE1';
SET @sBossWechat = 'UPDATFE1';
SET @sKey = 'AS28ss2F48';
SET @sBrandName = '�޹���F';
SET @sLogo = '';
-- 
CALL SP_Company_Update(@iErrorCode, @sErrorMsg, @iIDA, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossWechat, @sKey, @sName, @sBossName, @sBrandName, @sLogo);
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case13 Testing Result';

-- 
INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword)
VALUES ('13�ŷֹ�˾2', 'FB4522352354516', 'url=dftsfd4h35sfl', '/p/11.3jpg', '�ϰ�21��', '131462455881', '000000', 'sdf1h24sfh', 'cesh2i1', '122345610', 0, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI');
SET @iIDB = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = 'FDAFAfD';
SET @sBossName = '����FfFһ��';
SET @sBusinessLicenseSN = 'TB231F8f2323483';
SET @sBusinessLicensePicture = '';
SET @sBossPhone = 'UPDFfATE1';
SET @sBossWechat = 'UPDAfTFE1';
SET @sKey = 'AS28ss2fF48';
SET @sBrandName = '��f����F';
SET @sLogo = '';
-- 
CALL SP_Company_Update(@iErrorCode, @sErrorMsg, @iIDB, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossWechat, @sKey, @sName, @sBossName, @sBrandName, @sLogo);
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case13 Testing Result';

DELETE FROM t_company WHERE F_ID = @iIDA;
DELETE FROM t_company WHERE F_ID = @iIDB;

SELECT '-----------------CASE14: �޸Ĺ�˾��LogoΪ�Ѿ����ڵ�  ------------------' AS 'CASE14';
SET @sLogo = '12346ae';
INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword)
VALUES ('14�ŷֹ�˾', 'FB4522f352354516', 'url=dftsffd4hf35sfl', @sLogo, '�ϰ�21��', '131462455881', '000000', 'sdf1h24sfh', 'cesh2i1', '122345610', 0, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI');
SET @iIDA = LAST_INSERT_ID();
-- 
INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword)
VALUES ('14�ŷֹ�˾2', 'FB324522f352354516', 'url=d3ftsffd4hf35sfl', '4565WE', '�ϰ�21E��', '1314624R55881', '000000', 'sdf1Rh24sfh', 'ceshR2i1', '1223R45610', 0, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI');
SET @iIDB = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = 'FDAFAfD';
SET @sBossName = '����FfFһ��';
SET @sBusinessLicenseSN = 'TB231F8f2323483';
SET @sBusinessLicensePicture = '';
SET @sBossPhone = 'UPDFfATE1';
SET @sBossWechat = 'UPDAfTFE1';
SET @sKey = 'AS28ss2fF48';
SET @sBrandName = '��f����F';
-- 
CALL SP_Company_Update(@iErrorCode, @sErrorMsg, @iIDB, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossWechat, @sKey, @sName, @sBossName, @sBrandName, @sLogo);
SELECT IF(@sErrorMsg = '�ù�˾LogoͼƬ�Ѵ��ڣ�����ϵ����Ա' AND @iErrorCode = 1, '���Գɹ�', '����ʧ��') AS 'Case14 Testing Result';

DELETE FROM t_company WHERE F_ID = @iIDA;
DELETE FROM t_company WHERE F_ID = @iIDB;