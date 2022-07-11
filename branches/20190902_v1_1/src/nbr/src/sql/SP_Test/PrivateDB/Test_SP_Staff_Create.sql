SELECT '++++++++++++++++++Test_SP_Staff_Create.sql+++++++++++++++++++++++';

SELECT '-----------------CASE1: 正常添加------------------' AS 'CASE1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '123132113';
SET @sPhone = '12341567891';
SET @sName = '123131213';
SET @sName = '店长1123';
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
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';
DELETE FROM t_staffrole WHERE F_ID = last_insert_id();
DELETE FROM t_staff WHERE F_Phone = '12341567891';

SELECT '-----------------CASE2: 没有传入正确的iRoleID------------------' AS 'CASE2';

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
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'Case2 Testing Result';

SELECT '-----------------CASE3: 使用status为1的店员手机注册新店员------------------' AS 'CASE3';
INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('测试用户', '215613215352', '123457321d8234', 'a2dasf', 'JHSGDF5ASFA1SF85321', now(), 1, 1, 1, 1, now(), now());

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '123132ces113';
SET @sPhone = '215613215352';
SET @sName = '123131213ces';
SET @sName = '店长1ce123';
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
SELECT IF(@iErrorCode = 0 AND FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case3 Testing Result';
DELETE FROM t_staffrole WHERE F_StaffID IN (SELECT F_ID FROM t_staff WHERE F_Phone = '215613215352');
DELETE FROM t_staff WHERE F_Phone = '215613215352';

SELECT '-----------------CASE4: 使用已有的手机号注册------------------' AS 'CASE4';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '1231ceces113';
SET @sPhone = '13144496272';
SET @sName = '1231ce13ces';
SET @sName = '店长cee123';
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
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'Case4 Testing Result';


SELECT '-----------------CASE5: 使用已有的身份证号注册------------------' AS 'CASE5';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '1231ceceass113';
SET @sPhone = '17254874589';
SET @sName = '1231ces1a3ces';
SET @sName = '店长csee123';
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
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'Case5 Testing Result';

SELECT '-----------------CASE7: 使用已有微信号进行注册------------------' AS 'CASE7';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '1231zceceass113';
SET @sPhone = '15119114463';
SET @sName = '1231cesc1a3ces';
SET @sName = '店长zcsee123';
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
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'Case7 Testing Result';

SELECT '-----------------CASE8: 使用已有的店员名称进行注册------------------' AS 'CASE8';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '店员1号';
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
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case8 Testing Result';
DELETE FROM t_staffrole WHERE F_ID = last_insert_id();
DELETE FROM t_staff WHERE F_WeChat = 'f326dsd';


SELECT '-----------------CASE9: 用不存在的shopID创建------------------' AS 'CASE9';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '1231321131';
SET @sPhone = '123415678912';
SET @sName = '12313121322';
SET @sName = '店长11123';
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
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'Case9 Testing Result';


SELECT '-----------------CASE10: 正常添加,iRoleID为4的时候，该角色是公司老板，对应的shopID为1，iDepartmentID为1------------------' AS 'CASE10';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '123132113';
SET @sPhone = '12341567891';
SET @sName = '123131213';
SET @sName = '店长1123';
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
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case10 Testing Result';
DELETE FROM t_staffrole WHERE F_ID = last_insert_id();
DELETE FROM t_staff WHERE F_Phone = '12341567891';


SELECT '-----------------CASE11: 正常添加,iRoleID不为4的时候，说明该角色不是公司老板，对应的shopID\iDepartmentID为任意数据库已存在的值------------------' AS 'CASE11';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '123132113';
SET @sPhone = '12341567891';
SET @sName = '123131213';
SET @sName = '店长1123';
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
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case11 Testing Result';
DELETE FROM t_staffrole WHERE F_ID = last_insert_id();
DELETE FROM t_staff WHERE F_Phone = '12341567891';

SELECT '-----------------CASE12: 用不存在的DepartmentID创建------------------' AS 'CASE12';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '1231321131';
SET @sPhone = '123415678912';
SET @sName = '12313121322';
SET @sName = '店长11123';
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
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'Case12 Testing Result';

SELECT '-----------------CASE13: 创建一个staff，staff的身份证、微信、手机号码与离职的staff相同，能创建成功------------------' AS 'CASE8';

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
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case13 Testing Result';
DELETE FROM t_staffrole WHERE F_ID = last_insert_id();
DELETE FROM t_staff WHERE F_Name = "case13";