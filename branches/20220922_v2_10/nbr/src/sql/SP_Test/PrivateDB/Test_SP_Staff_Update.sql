SELECT '++++++++++++++++++Test_SP_Staff_Update.sql+++++++++++++++++++++++';

SELECT '-----------------CASE1: ��������------------------' AS 'CASE1';
INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('�����û�', '12345646234789122', '124126554235', 'a2dasf', 'JHSG123DF5ASFA1SF85321', now(), 1, 1, 1, 0, now(), now());

SET @iID = Last_insert_id();
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sPhone = '34512346547235';
SET @sICID = '13642356546x2313s';
SET @sWeChat = '1232341313144xh';
SET @sName = '������22'; 
SET @dPasswordExpireDate = '2019-01-01';
SET @iShopID = 1;
SET @iDepartmentID = 1;
SET @iRoleID = 1;
SET @iStatus = 0;
SET @iReturnSalt = 1;

CALL SP_Staff_Update(@iErrorCode, @sErrorMsg, @iID, @sName, @sPhone, @sICID, @sWeChat, @dPasswordExpireDate, @iShopID, @iDepartmentID, @iRoleID, @iStatus, @iReturnSalt);

SELECT @sErrorMsg;
SELECT 1 FROM t_staff WHERE F_ID = @iID AND F_Phone = @sPhone AND F_ICID = @sICID AND F_Name = @sName;
SELECT IF(@iErrorCode = 0 AND FOUND_ROWS() = 1, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

DELETE FROM t_staffrole WHERE F_StaffID = @iID;
DELETE FROM t_staff WHERE F_ID = @iID;

SELECT '-----------------case2: ��ѯ���˴θ��ĵ����֤���� �绰���� ΢�ź� �Ƿ�����Լ���������ʹ��  ����ѡ��һ ���֤�����Ѿ�����------------------' AS 'CASE2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;
SET @sPhone = '651651461464';
SET @sICID = '440883198412111666';			  
SET @sWeChat = '1213144ssx55645h';
SET @sName = '����4565��1s3'; 
SET @dPasswordExpireDate = '2019-01-01';
SET @iShopID = 1;
SET @iDepartmentID = 1;
SET @iRoleID = 1;
SET @iStatus = 0;
SET @iReturnSalt = 1;

CALL SP_Staff_Update(@iErrorCode, @sErrorMsg, @iID, @sName, @sPhone, @sICID, @sWeChat, @dPasswordExpireDate, @iShopID, @iDepartmentID, @iRoleID, @iStatus, @iReturnSalt);

SELECT @sErrorMsg;
SELECT 1 FROM t_staff WHERE F_ID = @iID AND F_Phone = @sPhone AND F_ICID = @sICID AND F_Name = @sName;
SELECT IF(@iErrorCode = 7 AND FOUND_ROWS() = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

SELECT '-----------------case3������һ�������ڵĽ�ɫ��-1��------------------' AS 'CASE3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;
SET @sPhone = '6789879797987987987984';
SET @sICID = '12341646';
SET @sWeChat = '1213144ssx55645h';
SET @sName = '����4565��1s3'; 
SET @dPasswordExpireDate = '2019-01-01';

SET @iShopID = 1;
SET @iDepartmentID = NULL;
SET @iRoleID = 9999999991;
SET @iStatus = 0;
SET @iReturnSalt = 1;

CALL SP_Staff_Update(@iErrorCode, @sErrorMsg, @iID, @sName, @sPhone, @sICID, @sWeChat, @dPasswordExpireDate, @iShopID, @iDepartmentID, @iRoleID, @iStatus, @iReturnSalt);

SELECT @sErrorMsg;
SELECT 1 FROM t_staff WHERE F_ID = @iID AND F_Phone = @sPhone AND F_ICID = @sICID AND F_Name = @sName;
SELECT IF(@iErrorCode = 7 AND FOUND_ROWS() = 0, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';

SELECT '-----------------case4: ���ĵĵ绰�����Ѿ�����------------------' AS 'CASE5';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;
SET @sPhone = '13144496272';
SET @sICID = '441522198413214569';
SET @sWeChat = '1213144ssx55645h';
SET @sName = '����4565��1s3'; 
SET @dPasswordExpireDate = '2019-01-01';
SET @iShopID = 1;
SET @iDepartmentID = 1;
SET @iRoleID = 1;
SET @iStatus = 0;
SET @iReturnSalt = 1;

CALL SP_Staff_Update(@iErrorCode, @sErrorMsg, @iID, @sName, @sPhone, @sICID, @sWeChat, @dPasswordExpireDate, @iShopID, @iDepartmentID, @iRoleID, @iStatus, @iReturnSalt);

SELECT @sErrorMsg;
SELECT 1 FROM t_staff WHERE F_ID = @iID AND F_Phone = @sPhone AND F_ICID = @sICID AND F_Name = @sName;
SELECT IF(@iErrorCode = 7 AND FOUND_ROWS() = 0, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';

SELECT '-----------------case5: ���ĵ�΢���Ѿ�����------------------' AS 'CASE5';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;
SET @sPhone = '11234567872';
SET @sICID = '440985675497614569';
SET @sWeChat = 'b56sdgr';
SET @sName = '����4565��1s3'; 
SET @dPasswordExpireDate = '2019-01-01';
SET @iShopID = 1;
SET @iDepartmentID = 1;
SET @iRoleID = 1;
SET @iStatus = 0;
SET @iReturnSalt = 1;

CALL SP_Staff_Update(@iErrorCode, @sErrorMsg, @iID, @sName, @sPhone, @sICID, @sWeChat, @dPasswordExpireDate, @iShopID, @iDepartmentID, @iRoleID, @iStatus, @iReturnSalt);

SELECT @sErrorMsg;
SELECT 1 FROM t_staff WHERE F_ID = @iID AND F_Phone = @sPhone AND F_ICID = @sICID AND F_Name = @sName;
SELECT IF(@iErrorCode = 7 AND FOUND_ROWS() = 0, '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';

SELECT '-----------------case6:�ò����ڵ�shopID�޸�------------------' AS 'CASE6';
SET @iID = 1;
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sPhone = '34512346547235';
SET @sICID = '13642356546x2313s';
SET @sWeChat = '1232341313144xh';
SET @sName = '������22'; 
SET @dPasswordExpireDate = '2019-01-01';
SET @iShopID = 99999999999;
SET @iDepartmentID = 1;
SET @iRoleID = 1;
SET @iStatus = 0;
SET @iReturnSalt = 1;

CALL SP_Staff_Update(@iErrorCode, @sErrorMsg, @iID, @sName, @sPhone, @sICID, @sWeChat, @dPasswordExpireDate, @iShopID, @iDepartmentID, @iRoleID, @iStatus, @iReturnSalt);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case6 Testing Result';

SELECT '-----------------case7:�ò����ڵ�departmentID,����ID�޸�------------------' AS 'CASE7';
SET @iID = 1;
SET @sErrorMsg = '';
SET @iErrorCode = 0;
SET @sPhone = '34512346547235';
SET @sICID = '13642356546x2313s';
SET @sWeChat = '1232341313144xh';
SET @sName = '������22'; 
SET @dPasswordExpireDate = '2019-01-01';
SET @iShopID = 1;
SET @iDepartmentID = 99999999999;
SET @iRoleID = 1;
SET @iStatus = 0;
SET @iReturnSalt = 1;

CALL SP_Staff_Update(@iErrorCode, @sErrorMsg, @iID, @sName, @sPhone, @sICID, @sWeChat, @dPasswordExpireDate, @iShopID, @iDepartmentID, @iRoleID, @iStatus, @iReturnSalt);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case7 Testing Result';



SELECT '-----------------CASE8: ����staff�����֣��޸ĺ����������ְ��staff�����ظ�����Ҳ���޸ĳɹ�------------------' AS 'CASE8';
INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('�����û�', '12345646234789122', '124126554235', 'a2dasf', 'JHSG123DF5ASFA1SF85321', now(), 1, 1, 1, 0, now(), now());

SET @iID = Last_insert_id();
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sPhone = '34512346547235';
SET @sICID = '13642356546x2313s';
SET @sWeChat = '1232341313144xh';
SET @sName = '��Ա1��'; 
SET @dPasswordExpireDate = '2019-01-01';
SET @iShopID = 1;
SET @iDepartmentID = 1;
SET @iRoleID = 1;
SET @iStatus = 0;
SET @iReturnSalt = 1;

CALL SP_Staff_Update(@iErrorCode, @sErrorMsg, @iID, @sName, @sPhone, @sICID, @sWeChat, @dPasswordExpireDate, @iShopID, @iDepartmentID, @iRoleID, @iStatus, @iReturnSalt);

SELECT @sErrorMsg;
SELECT 1 FROM t_staff WHERE F_ID = @iID AND F_Phone = @sPhone AND F_ICID = @sICID AND F_Name = @sName;
SELECT IF(@iErrorCode = 0 AND FOUND_ROWS() = 1, '���Գɹ�', '����ʧ��') AS 'Case8 Testing Result';

DELETE FROM t_staffrole WHERE F_StaffID = @iID;
DELETE FROM t_staff WHERE F_ID = @iID;

SELECT '-----------------CASE9: ����Ա��ʱ��������д��ְԱ�����ֻ��š�΢�š����֤�������ɹ������޸����Ա������Ϣʱ��Ҳ���޸ĳɹ�------------------' AS 'CASE9';
INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('�����û�', '13196721886', '341522198412111666', 'd2sasb4', 'JHSG123DF5ASFA1SF85321', now(), 1, 1, 1, 0, now(), now());

SET @iID = Last_insert_id();
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sPhone = '13196721886';
SET @sICID = '341522198412111666';
SET @sWeChat = 'd2sasb4';
SET @sName = '��Ա1��'; 
SET @dPasswordExpireDate = '2019-01-01';
SET @iShopID = 1;
SET @iDepartmentID = 1;
SET @iRoleID = 1;
SET @iStatus = 0;
SET @iReturnSalt = 1;

CALL SP_Staff_Update(@iErrorCode, @sErrorMsg, @iID, @sName, @sPhone, @sICID, @sWeChat, @dPasswordExpireDate, @iShopID, @iDepartmentID, @iRoleID, @iStatus, @iReturnSalt);

SELECT @sErrorMsg;
SELECT 1 FROM t_staff WHERE F_ID = @iID AND F_Phone = @sPhone AND F_ICID = @sICID AND F_Name = @sName;
SELECT IF(@iErrorCode = 0 AND FOUND_ROWS() = 1, '���Գɹ�', '����ʧ��') AS 'Case9 Testing Result';

DELETE FROM t_staffrole WHERE F_StaffID = @iID;
DELETE FROM t_staff WHERE F_ID = @iID;

SELECT '-----------------CASE10:�޸�Ա��״̬Ϊ��ְ��������7------------------' AS 'CASE10';
SET @iID = 1;
SET @sErrorMsg = '';
SET @iErrorCode = 0;
SET @sPhone = '34512346547235';
SET @sICID = '13642356546x2313s';
SET @sWeChat = '1232341313144xh';
SET @sName = '������22'; 
SET @dPasswordExpireDate = '2019-01-01';
SET @iShopID = 1;
SET @iDepartmentID = 1;
SET @iRoleID = 1;
SET @iStatus = 1;
SET @iReturnSalt = 1;

CALL SP_Staff_Update(@iErrorCode, @sErrorMsg, @iID, @sName, @sPhone, @sICID, @sWeChat, @dPasswordExpireDate, @iShopID, @iDepartmentID, @iRoleID, @iStatus, @iReturnSalt);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case10 Testing Result';

SELECT '-----------------CASE11:��ְת��ְ------------------' AS 'CASE11';
INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('�����û�001', '15279254465', '341522198412111666', 'd2sasb4', 'JHSG123DF5ASFA1SF85321', now(), 1, 1, 1, 1, now(), now());

SET @iID = last_insert_id();
SET @sErrorMsg = '';
SET @iErrorCode = 0;
SET @sPhone = '34512346547235';
SET @sICID = '13642356546x2313s';
SET @sWeChat = '1232341313144xh';
SET @sName = '������22'; 
SET @dPasswordExpireDate = '2019-01-01';
SET @iShopID = 1;
SET @iDepartmentID = 1;
SET @iRoleID = 1;
SET @iStatus = 0;
SET @iReturnSalt = 1;

CALL SP_Staff_Update(@iErrorCode, @sErrorMsg, @iID, @sName, @sPhone, @sICID, @sWeChat, @dPasswordExpireDate, @iShopID, @iDepartmentID, @iRoleID, @iStatus, @iReturnSalt);

SELECT @sErrorMsg;
SELECT 1 FROM t_staff WHERE F_ID = @iID AND F_Phone = @sPhone AND F_ICID = @sICID AND F_Name = @sName;
SELECT IF(@iErrorCode = 0 AND FOUND_ROWS() = 1, '���Գɹ�', '����ʧ��') AS 'Case11 Testing Result';

DELETE FROM t_staffrole WHERE F_StaffID = @iID;
DELETE FROM t_staff WHERE F_ID = @iID;