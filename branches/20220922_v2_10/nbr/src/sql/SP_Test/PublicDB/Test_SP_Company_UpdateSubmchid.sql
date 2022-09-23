SELECT '++++++++++++++++++Test_SP_Company_UpdateSubmchid.sql+++++++++++++++++++++++';
USE nbr_bx;
SELECT '-----------------CASE1: �޸Ĺ�˾�����̻���(��ȷ)------------------' AS 'CASE1';

INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword,F_Submchid)
VALUES ('1�ŷֹ�˾', 'FB452352354516', 'url=dftsfd4h5sfl', '/p/11.jpg', '�ϰ�1��', '13146455881', '000000', 'sdf1h4sfh', 'ceshi1', '123456u1', 0, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI','1934561239');

SET @iID = LAST_INSERT_ID();
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sSubmchid = '1934569999';

CALL SP_Company_UpdateSubmchid(@iErrorCode, @sErrorMsg, @iID, @sSubmchid);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM nbr_bx.t_company WHERE F_Submchid = @sSubmchid AND F_ID = @iID;
SELECT IF(FOUND_ROWS() = 1 AND @iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

DELETE FROM nbr_bx.t_company WHERE F_ID = @iID;


SELECT '-----------------CASE2: �޸Ĳ����ڵĹ�˾�������̻���(ʧ��)------------------' AS 'CASE2';
SET @iID = -1;
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sSubmchid = 'FT232323323';

CALL SP_Company_UpdateSubmchid(@iErrorCode, @sErrorMsg, @iID, @sSubmchid);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM nbr_bx.t_company WHERE F_Submchid = @sSubmchid AND F_ID = @iID;
SELECT IF(@sErrorMsg = '�޸ĵĹ�˾������' AND @iErrorCode = 2, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';


SELECT '-----------------CASE3: �޸�Ϊ���е�������˾���е����̻���(ʧ��)------------------' AS 'CASE3';
INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword,F_Submchid)
VALUES ('1�ŷֹ�˾', 'FB452352354516', 'url=dftsfd4h5sfl', '/p/11.jpg', '�ϰ�1��', '13146455881', '000000', 'sdf1h4sfh', 'ceshi1', '123456u1', 0, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI','1934561239');

SET @iID1 = LAST_INSERT_ID();
INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword,F_Submchid)
VALUES ('2�ŷֹ�˾', 'FB452352354599', 'url=dftsfd4h5s99', '/p/121.jpg', '�ϰ�2��', '13146455899', '000000', 'sdf1h4s99', 'ceshi2', '123499u1', 0, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI','1934561211');

SET @iID2 = LAST_INSERT_ID();
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sSubmchid = '1934561239';

CALL SP_Company_UpdateSubmchid(@iErrorCode, @sErrorMsg, @iID2, @sSubmchid);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM nbr_bx.t_company WHERE F_Submchid = @sSubmchid AND F_ID = @iID2;
SELECT IF(@sErrorMsg = '������˾�Ѵ���������̻���' AND @iErrorCode = 1, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';

DELETE FROM nbr_bx.t_company WHERE F_ID = @iID1;
DELETE FROM nbr_bx.t_company WHERE F_ID = @iID2;


SELECT '-----------------CASE4: �޸ĵ����̻�������ɾ���Ĺ�˾�������̻���(��ȷ)------------------' AS 'CASE4';
INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword,F_Submchid)
VALUES ('1�ŷֹ�˾', 'FB452352354516', 'url=dftsfd4h5sfl', '/p/11.jpg', '�ϰ�1��', '13146455881', '000000', 'sdf1h4sfh', 'ceshi1', '123456u1', 1, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI','1934561239');

SET @iID1 = LAST_INSERT_ID();

INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword,F_Submchid)
VALUES ('2�ŷֹ�˾', 'FB452352354599', 'url=dftsfd4h5s99', '/p/123.jpg', '�ϰ�2��', '13146455899', '000000', 'sdf1h4s99', 'ceshi2', '123499u1', 0, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI','1934561211');

SET @iID2 = LAST_INSERT_ID();
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sSubmchid = '1934561239';

CALL SP_Company_UpdateSubmchid(@iErrorCode, @sErrorMsg, @iID2, @sSubmchid);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM nbr_bx.t_company WHERE F_Submchid = @sSubmchid AND F_ID = @iID2;
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';

DELETE FROM nbr_bx.t_company WHERE F_ID = @iID1;
DELETE FROM nbr_bx.t_company WHERE F_ID = @iID2;


SELECT '-----------------CASE5: �޸ĵĹ�˾��ɾ��״̬�� ------------------' AS 'CASE5';
INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword,F_Submchid)
VALUES ('2�ŷֹ�˾', 'FB452352354599', 'url=dftsfd4h5s99', '/p/11.jpg', '�ϰ�2��', '13146455899', '000000', 'sdf1h4s99', 'ceshi2', '123499u1', 1, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI','1934561211');

SET @iID = LAST_INSERT_ID();
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sSubmchid = '1934561239';

CALL SP_Company_UpdateSubmchid(@iErrorCode, @sErrorMsg, @iID, @sSubmchid);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM nbr_bx.t_company WHERE F_Submchid = @sSubmchid AND F_ID = @iID;
SELECT IF(@sErrorMsg = '�޸ĵĹ�˾������' AND @iErrorCode = 2, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';

DELETE FROM nbr_bx.t_company WHERE F_ID = @iID;


SELECT '-----------------CASE6: ����һ�����̻�Ϊ�մ��Ĺ�˾A���ٴ���һ����˾B������˾B�����̻����޸�Ϊ�մ�.���Ϊ�޸ĳɹ�.------------------' AS 'CASE6';
INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword,F_Submchid)
VALUES ('1�ŷֹ�˾', 'FB452352354516', 'url=dftsfd4h5sfl', '/p/112.jpg', '�ϰ�1��', '13146455881', '000000', 'sdf1h4sfh', 'ceshi1', '123456u1', 1, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI','');

SET @iID1 = LAST_INSERT_ID();

INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword,F_Submchid)
VALUES ('2�ŷֹ�˾', 'FB452352354599', 'url=dftsfd4h5s99', '/p/131.jpg', '�ϰ�2��', '13146455899', '000000', 'sdf1h4s99', 'ceshi2', '123499u1', 0, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI','1234567893');

SET @iID2 = LAST_INSERT_ID();
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sSubmchid = '';

CALL SP_Company_UpdateSubmchid(@iErrorCode, @sErrorMsg, @iID2, @sSubmchid);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM nbr_bx.t_company WHERE F_Submchid = @sSubmchid AND F_ID = @iID2;
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case6 Testing Result';

DELETE FROM nbr_bx.t_company WHERE F_ID = @iID1;
DELETE FROM nbr_bx.t_company WHERE F_ID = @iID2;