SELECT '++++++++++++++++++Test_SP_Staff_Create.sql+++++++++++++++++++++++';

SELECT '-----------------CASE1: �������------------------' AS 'CASE1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '123132113';
SET @sPhone = '12341567891';
SET @sName = '123131213';
SET @sName = '�곤1123';
SET @sICID = '4310251971841233';
SET @sWeChat = '';
SET @sPassword = '12341167';
SET @sSalt = 'E10ADC3949BA59ABBE56E057F20F883E';
SET @dPasswordExpireDate = '2017-05-03';
SET @iIsFirstTimeLogin = 1;
SET @iShopID = 1;


SET @iDepartmentID = 1;
SET @iRoleID = 1;
SET @iStatus = 0;
SET @iReturnSalt = 1;

CALL SP_Staff_Create(@iErrorCode, @sErrorMsg, @sPhone, @sName, @sICID, @sWeChat, @sSalt, @dPasswordExpireDate, @iIsFirstTimeLogin, 
	@iShopID, @iDepartmentID,@iRoleID, @iStatus, @iReturnSalt);

SELECT @sErrorMsg;	
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';
DELETE FROM t_staffrole WHERE F_ID = last_insert_id();
DELETE FROM t_staff WHERE F_Phone = '12341567891';

SELECT '-----------------CASE2: û�д�����ȷ��iRoleID------------------' AS 'CASE2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '123132x113';
SET @sPhone = '123x41567891';
SET @sICID = '4310251x971841233';
SET @sWeChat = '1321xfq1r1rf';
SET @sPassword = '12341167';
SET @sSalt = 'E10ADC3949BA59ABBE56E057F20F883E';
SET @dPasswordExpireDate = '2017-05-03';
SET @iIsFirstTimeLogin = 1;
SET @iShopID = 1;
SET @iDepartmentID = 1;
SET @iRoleID = -1;
SET @iStatus = 0;
SET @iReturnSalt = 1;

CALL SP_Staff_Create(@iErrorCode, @sErrorMsg, @sPhone, @sName, @sICID, @sWeChat, @sSalt, @dPasswordExpireDate, @iIsFirstTimeLogin, 
	@iShopID, @iDepartmentID,@iRoleID, @iStatus, @iReturnSalt);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

SELECT '-----------------CASE3: ʹ��statusΪ1�ĵ�Ա�ֻ�ע���µ�Ա------------------' AS 'CASE3';
INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('�����û�', '215613215352', '123457321d8234', 'a2dasf', 'JHSGDF5ASFA1SF85321', now(), 1, 1, 1, 1, now(), now());

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '123132ces113';
SET @sPhone = '215613215352';
SET @sName = '123131213ces';
SET @sName = '�곤1ce123';
SET @sICID = '4310251971ces841233';
SET @sWeChat = 'ces';
SET @sPassword = '12341167';
SET @sSalt = 'E10ADC3949BA59ABBE56E057F20F883E';
SET @dPasswordExpireDate = '2017-05-04';
SET @iIsFirstTimeLogin = 1;
SET @iShopID = 1;
SET @iDepartmentID = 1;
SET @iRoleID = 1;
SET @iStatus = 0;
SET @iReturnSalt = 1;

CALL SP_Staff_Create(@iErrorCode, @sErrorMsg, @sPhone, @sName, @sICID, @sWeChat, @sSalt, @dPasswordExpireDate, @iIsFirstTimeLogin, 
	@iShopID, @iDepartmentID,@iRoleID, @iStatus, @iReturnSalt);

SELECT @sErrorMsg;
SELECT 1 FROM t_staff WHERE F_Phone = '215613215352' AND F_Status = 0;  
SELECT IF(@iErrorCode = 0 AND FOUND_ROWS() = 1, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';
DELETE FROM t_staffrole WHERE F_StaffID IN (SELECT F_ID FROM t_staff WHERE F_Phone = '215613215352');
DELETE FROM t_staff WHERE F_Phone = '215613215352';

SELECT '-----------------CASE4: ʹ�����е��ֻ���ע��------------------' AS 'CASE4';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '1231ceces113';
SET @sPhone = '13144496272';
SET @sName = '1231ce13ces';
SET @sName = '�곤cee123';
SET @sICID = '431025ce1ces841233';
SET @sWeChat = 'cess';
SET @sPassword = '12341167';
SET @sSalt = 'E10ADC3949BA59ABBE56E057F20F883E';
SET @dPasswordExpireDate = '2017-05-04';
SET @iIsFirstTimeLogin = 1;
SET @iShopID = 1;
SET @iDepartmentID = 1;
SET @iRoleID = 1;
SET @iStatus = 0;
SET @iReturnSalt = 1;

CALL SP_Staff_Create(@iErrorCode, @sErrorMsg, @sPhone, @sName, @sICID, @sWeChat, @sSalt, @dPasswordExpireDate, @iIsFirstTimeLogin, 
	@iShopID, @iDepartmentID,@iRoleID, @iStatus, @iReturnSalt);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';


SELECT '-----------------CASE5: ʹ�����е����֤��ע��------------------' AS 'CASE5';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '1231ceceass113';
SET @sPhone = '17254874589';
SET @sName = '1231ces1a3ces';
SET @sName = '�곤csee123';
SET @sICID = '440883198412111666';
SET @sWeChat = 'cess';
SET @sPassword = '123411167';
SET @sSalt = 'E10ADC3949BB59ABBE56E057F20F883E';
SET @dPasswordExpireDate = '2017-05-04';
SET @iIsFirstTimeLogin = 1;
SET @iShopID = 1;
SET @iDepartmentID = 1;
SET @iRoleID = 1;
SET @iStatus = 0;
SET @iReturnSalt = 1;

CALL SP_Staff_Create(@iErrorCode, @sErrorMsg, @sPhone, @sName, @sICID, @sWeChat, @sSalt, @dPasswordExpireDate, @iIsFirstTimeLogin, 
	@iShopID, @iDepartmentID,@iRoleID, @iStatus, @iReturnSalt);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';

SELECT '-----------------CASE7: ʹ������΢�źŽ���ע��------------------' AS 'CASE7';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '1231zceceass113';
SET @sPhone = '15119114463';
SET @sName = '1231cesc1a3ces';
SET @sName = '�곤zcsee123';
SET @sICID = '440883196412111666';
SET @sWeChat = 'a326dsd';
SET @sPassword = '123411167';
SET @sSalt = 'E10ADC3949BB59ABBE56E057F20F883E';
SET @dPasswordExpireDate = '2017-05-04';
SET @iIsFirstTimeLogin = 1;
SET @iShopID = 1;
SET @iDepartmentID = 1;
SET @iRoleID = 1;
SET @iStatus = 0;
SET @iReturnSalt = 1;

CALL SP_Staff_Create(@iErrorCode, @sErrorMsg, @sPhone, @sName, @sICID, @sWeChat, @sSalt, @dPasswordExpireDate, @iIsFirstTimeLogin, 
	@iShopID, @iDepartmentID,@iRoleID, @iStatus, @iReturnSalt);
	
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case7 Testing Result';

SELECT '-----------------CASE8: ʹ�����еĵ�Ա���ƽ���ע��------------------' AS 'CASE8';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '��Ա1��';
SET @sPhone = '11165465465';
SET @sICID = '440883196412111666';
SET @sWeChat = 'f326dsd';
SET @sPassword = '123411167';
SET @sSalt = 'E10ADC3949BB59ABBE56E057F20F883E';
SET @dPasswordExpireDate = '2017-05-04';
SET @iIsFirstTimeLogin = 1;
SET @iShopID = 1;
SET @iDepartmentID = 1;
SET @iRoleID = 1;
SET @iStatus = 0;
SET @iReturnSalt = 1;

CALL SP_Staff_Create(@iErrorCode, @sErrorMsg, @sPhone, @sName, @sICID, @sWeChat, @sSalt, @dPasswordExpireDate, @iIsFirstTimeLogin, 
	@iShopID, @iDepartmentID,@iRoleID, @iStatus, @iReturnSalt);
	
SELECT @sErrorMsg;	
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case8 Testing Result';
DELETE FROM t_staffrole WHERE F_ID = last_insert_id();
DELETE FROM t_staff WHERE F_WeChat = 'f326dsd';


SELECT '-----------------CASE9: �ò����ڵ�shopID����------------------' AS 'CASE9';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '1231321131';
SET @sPhone = '123415678912';
SET @sName = '12313121322';
SET @sName = '�곤11123';
SET @sICID = '4310251971541233';
SET @sWeChat = '';
SET @sPassword = '12341467';
SET @sSalt = 'E10ADC3949BA59ABBE56E057F20F883E4';
SET @dPasswordExpireDate = '2017-05-03';
SET @iIsFirstTimeLogin = 1;
SET @iShopID = -99;
SET @iDepartmentID = 1;
SET @iRoleID = 1;
SET @iStatus = 0;
SET @iReturnSalt = 1;

CALL SP_Staff_Create(@iErrorCode, @sErrorMsg, @sPhone, @sName, @sICID, @sWeChat, @sSalt, @dPasswordExpireDate, @iIsFirstTimeLogin, 
	@iShopID, @iDepartmentID,@iRoleID, @iStatus, @iReturnSalt);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case9 Testing Result';


SELECT '-----------------CASE10: �������,iRoleIDΪ4��ʱ�򣬸ý�ɫ�ǹ�˾�ϰ壬��Ӧ��shopIDΪ1��iDepartmentIDΪ1------------------' AS 'CASE10';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '123132113';
SET @sPhone = '12341567891';
SET @sName = '123131213';
SET @sName = '�곤1123';
SET @sICID = '4310251971841233';
SET @sWeChat = '';
SET @sPassword = '12341167';
SET @sSalt = 'E10ADC3949BA59ABBE56E057F20F883E';
SET @dPasswordExpireDate = '2017-05-03';
SET @iIsFirstTimeLogin = 1;
SET @iShopID = 1;
SET @iDepartmentID = 1;
SET @iRoleID = 4;
SET @iStatus = 0;
SET @iReturnSalt = 1;

CALL SP_Staff_Create(@iErrorCode, @sErrorMsg, @sPhone, @sName, @sICID, @sWeChat, @sSalt, @dPasswordExpireDate, @iIsFirstTimeLogin, 
	@iShopID, @iDepartmentID,@iRoleID, @iStatus, @iReturnSalt);

SELECT @sErrorMsg;	
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case10 Testing Result';
DELETE FROM t_staffrole WHERE F_ID = last_insert_id();
DELETE FROM t_staff WHERE F_Phone = '12341567891';


SELECT '-----------------CASE11: �������,iRoleID��Ϊ4��ʱ��˵���ý�ɫ���ǹ�˾�ϰ壬��Ӧ��shopID\iDepartmentIDΪ�������ݿ��Ѵ��ڵ�ֵ------------------' AS 'CASE11';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '123132113';
SET @sPhone = '12341567891';
SET @sName = '123131213';
SET @sName = '�곤1123';
SET @sICID = '4310251971841233';
SET @sWeChat = '';
SET @sPassword = '12341167';
SET @sSalt = 'E10ADC3949BA59ABBE56E057F20F883E';
SET @dPasswordExpireDate = '2017-05-03';
SET @iIsFirstTimeLogin = 1;
SET @iShopID = 2;
SET @iDepartmentID = 2;
SET @iRoleID = 1;
SET @iStatus = 0;
SET @iReturnSalt = 1;

CALL SP_Staff_Create(@iErrorCode, @sErrorMsg, @sPhone, @sName, @sICID, @sWeChat, @sSalt, @dPasswordExpireDate, @iIsFirstTimeLogin, 
	@iShopID, @iDepartmentID,@iRoleID, @iStatus, @iReturnSalt);

SELECT @sErrorMsg;	
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case11 Testing Result';
DELETE FROM t_staffrole WHERE F_ID = last_insert_id();
DELETE FROM t_staff WHERE F_Phone = '12341567891';

SELECT '-----------------CASE12: �ò����ڵ�DepartmentID����------------------' AS 'CASE12';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '1231321131';
SET @sPhone = '123415678912';
SET @sName = '12313121322';
SET @sName = '�곤11123';
SET @sICID = '4310251971541233';
SET @sWeChat = '';
SET @sPassword = '12341467';
SET @sSalt = 'E10ADC3949BA59ABBE56E057F20F883E4';
SET @dPasswordExpireDate = '2017-05-03';
SET @iIsFirstTimeLogin = 1;
SET @iShopID = 1;
SET @iDepartmentID = 999999999;
SET @iRoleID = 1;
SET @iStatus = 0;
SET @iReturnSalt = 1;

CALL SP_Staff_Create(@iErrorCode, @sErrorMsg, @sPhone, @sName, @sICID, @sWeChat, @sSalt, @dPasswordExpireDate, @iIsFirstTimeLogin, 
	@iShopID, @iDepartmentID,@iRoleID, @iStatus, @iReturnSalt);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case12 Testing Result';

SELECT '-----------------CASE13: ����һ��staff��staff�����֤��΢�š��ֻ���������ְ��staff��ͬ���ܴ����ɹ�------------------' AS 'CASE8';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = 'case13';
SET @sPhone = '13196721886';
SET @sICID = '341522198412111666';
SET @sWeChat = 'd2sasb4';
SET @sPassword = '123411167';
SET @sSalt = 'E10ADC3949BB59ABBE56E057F20F883E';
SET @dPasswordExpireDate = '2017-05-04';
SET @iIsFirstTimeLogin = 1;
SET @iShopID = 1;
SET @iDepartmentID = 1;
SET @iRoleID = 1;
SET @iStatus = 0;
SET @iReturnSalt = 1;

CALL SP_Staff_Create(@iErrorCode, @sErrorMsg, @sPhone, @sName, @sICID, @sWeChat, @sSalt, @dPasswordExpireDate, @iIsFirstTimeLogin, 
	@iShopID, @iDepartmentID,@iRoleID, @iStatus, @iReturnSalt);
	
SELECT @sErrorMsg;	
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case13 Testing Result';
DELETE FROM t_staffrole WHERE F_ID = last_insert_id();
DELETE FROM t_staff WHERE F_Name = "case13";