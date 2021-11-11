SELECT '++++++++++++++++++Test_SP_Staff_Update.sql+++++++++++++++++++++++';

SELECT '-----------------CASE1: 正常更新------------------' AS 'CASE1';
INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('测试用户', '12345646234789122', '124126554235', 'a2dasf', 'JHSG123DF5ASFA1SF85321', now(), 1, 1, 1, 0, now(), now());

SET @iID = Last_insert_id();
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sPhone = '34512346547235';
SET @sICID = '13642356546x2313s';
SET @sWeChat = '1232341313144xh';
SET @sName = '黄晓明22'; 
SET @dPasswordExpireDate = '2019-01-01';
SET @iShopID = 1;
SET @iDepartmentID = 1;
SET @iRoleID = 1;
SET @iStatus = 0;
SET @iReturnSalt = 1;

CALL SP_Staff_Update(@iErrorCode, @sErrorMsg, @iID, @sName, @sPhone, @sICID, @sWeChat, @dPasswordExpireDate, @iShopID, @iDepartmentID, @iRoleID, @iStatus, @iReturnSalt);

SELECT @sErrorMsg;
SELECT 1 FROM t_staff WHERE F_ID = @iID AND F_Phone = @sPhone AND F_ICID = @sICID AND F_Name = @sName;
SELECT IF(@iErrorCode = 0 AND FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case1 Testing Result';

DELETE FROM t_staffrole WHERE F_StaffID = @iID;
DELETE FROM t_staff WHERE F_ID = @iID;

SELECT '-----------------case2: 查询出此次更改的身份证号码 电话号码 微信号 是否除了自己其他人在使用  以上选其一 身份证号码已经存在------------------' AS 'CASE2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;
SET @sPhone = '651651461464';
SET @sICID = '440883198412111666';			  
SET @sWeChat = '1213144ssx55645h';
SET @sName = '黄晓4565明1s3'; 
SET @dPasswordExpireDate = '2019-01-01';
SET @iShopID = 1;
SET @iDepartmentID = 1;
SET @iRoleID = 1;
SET @iStatus = 0;
SET @iReturnSalt = 1;

CALL SP_Staff_Update(@iErrorCode, @sErrorMsg, @iID, @sName, @sPhone, @sICID, @sWeChat, @dPasswordExpireDate, @iShopID, @iDepartmentID, @iRoleID, @iStatus, @iReturnSalt);

SELECT @sErrorMsg;
SELECT 1 FROM t_staff WHERE F_ID = @iID AND F_Phone = @sPhone AND F_ICID = @sICID AND F_Name = @sName;
SELECT IF(@iErrorCode = 7 AND FOUND_ROWS() = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';

SELECT '-----------------case3：输入一个不存在的角色（-1）------------------' AS 'CASE3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;
SET @sPhone = '6789879797987987987984';
SET @sICID = '12341646';
SET @sWeChat = '1213144ssx55645h';
SET @sName = '黄晓4565明1s3'; 
SET @dPasswordExpireDate = '2019-01-01';

SET @iShopID = 1;
SET @iDepartmentID = NULL;
SET @iRoleID = 9999999991;
SET @iStatus = 0;
SET @iReturnSalt = 1;

CALL SP_Staff_Update(@iErrorCode, @sErrorMsg, @iID, @sName, @sPhone, @sICID, @sWeChat, @dPasswordExpireDate, @iShopID, @iDepartmentID, @iRoleID, @iStatus, @iReturnSalt);

SELECT @sErrorMsg;
SELECT 1 FROM t_staff WHERE F_ID = @iID AND F_Phone = @sPhone AND F_ICID = @sICID AND F_Name = @sName;
SELECT IF(@iErrorCode = 7 AND FOUND_ROWS() = 0, '测试成功', '测试失败') AS 'Case3 Testing Result';

SELECT '-----------------case4: 更改的电话号码已经存在------------------' AS 'CASE5';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;
SET @sPhone = '13144496272';
SET @sICID = '441522198413214569';
SET @sWeChat = '1213144ssx55645h';
SET @sName = '黄晓4565明1s3'; 
SET @dPasswordExpireDate = '2019-01-01';
SET @iShopID = 1;
SET @iDepartmentID = 1;
SET @iRoleID = 1;
SET @iStatus = 0;
SET @iReturnSalt = 1;

CALL SP_Staff_Update(@iErrorCode, @sErrorMsg, @iID, @sName, @sPhone, @sICID, @sWeChat, @dPasswordExpireDate, @iShopID, @iDepartmentID, @iRoleID, @iStatus, @iReturnSalt);

SELECT @sErrorMsg;
SELECT 1 FROM t_staff WHERE F_ID = @iID AND F_Phone = @sPhone AND F_ICID = @sICID AND F_Name = @sName;
SELECT IF(@iErrorCode = 7 AND FOUND_ROWS() = 0, '测试成功', '测试失败') AS 'Case4 Testing Result';

SELECT '-----------------case5: 更改的微信已经存在------------------' AS 'CASE5';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;
SET @sPhone = '11234567872';
SET @sICID = '440985675497614569';
SET @sWeChat = 'b56sdgr';
SET @sName = '黄晓4565明1s3'; 
SET @dPasswordExpireDate = '2019-01-01';
SET @iShopID = 1;
SET @iDepartmentID = 1;
SET @iRoleID = 1;
SET @iStatus = 0;
SET @iReturnSalt = 1;

CALL SP_Staff_Update(@iErrorCode, @sErrorMsg, @iID, @sName, @sPhone, @sICID, @sWeChat, @dPasswordExpireDate, @iShopID, @iDepartmentID, @iRoleID, @iStatus, @iReturnSalt);

SELECT @sErrorMsg;
SELECT 1 FROM t_staff WHERE F_ID = @iID AND F_Phone = @sPhone AND F_ICID = @sICID AND F_Name = @sName;
SELECT IF(@iErrorCode = 7 AND FOUND_ROWS() = 0, '测试成功', '测试失败') AS 'Case5 Testing Result';

SELECT '-----------------case6:用不存在的shopID修改------------------' AS 'CASE6';
SET @iID = 1;
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sPhone = '34512346547235';
SET @sICID = '13642356546x2313s';
SET @sWeChat = '1232341313144xh';
SET @sName = '黄晓明22'; 
SET @dPasswordExpireDate = '2019-01-01';
SET @iShopID = 99999999999;
SET @iDepartmentID = 1;
SET @iRoleID = 1;
SET @iStatus = 0;
SET @iReturnSalt = 1;

CALL SP_Staff_Update(@iErrorCode, @sErrorMsg, @iID, @sName, @sPhone, @sICID, @sWeChat, @dPasswordExpireDate, @iShopID, @iDepartmentID, @iRoleID, @iStatus, @iReturnSalt);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'Case6 Testing Result';

SELECT '-----------------case7:用不存在的departmentID,部门ID修改------------------' AS 'CASE7';
SET @iID = 1;
SET @sErrorMsg = '';
SET @iErrorCode = 0;
SET @sPhone = '34512346547235';
SET @sICID = '13642356546x2313s';
SET @sWeChat = '1232341313144xh';
SET @sName = '黄晓明22'; 
SET @dPasswordExpireDate = '2019-01-01';
SET @iShopID = 1;
SET @iDepartmentID = 99999999999;
SET @iRoleID = 1;
SET @iStatus = 0;
SET @iReturnSalt = 1;

CALL SP_Staff_Update(@iErrorCode, @sErrorMsg, @iID, @sName, @sPhone, @sICID, @sWeChat, @dPasswordExpireDate, @iShopID, @iDepartmentID, @iRoleID, @iStatus, @iReturnSalt);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'Case7 Testing Result';



SELECT '-----------------CASE8: 更改staff的名字，修改后的名字与在职的staff名字重复，但也能修改成功------------------' AS 'CASE8';
INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('测试用户', '12345646234789122', '124126554235', 'a2dasf', 'JHSG123DF5ASFA1SF85321', now(), 1, 1, 1, 0, now(), now());

SET @iID = Last_insert_id();
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sPhone = '34512346547235';
SET @sICID = '13642356546x2313s';
SET @sWeChat = '1232341313144xh';
SET @sName = '店员1号'; 
SET @dPasswordExpireDate = '2019-01-01';
SET @iShopID = 1;
SET @iDepartmentID = 1;
SET @iRoleID = 1;
SET @iStatus = 0;
SET @iReturnSalt = 1;

CALL SP_Staff_Update(@iErrorCode, @sErrorMsg, @iID, @sName, @sPhone, @sICID, @sWeChat, @dPasswordExpireDate, @iShopID, @iDepartmentID, @iRoleID, @iStatus, @iReturnSalt);

SELECT @sErrorMsg;
SELECT 1 FROM t_staff WHERE F_ID = @iID AND F_Phone = @sPhone AND F_ICID = @sICID AND F_Name = @sName;
SELECT IF(@iErrorCode = 0 AND FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case8 Testing Result';

DELETE FROM t_staffrole WHERE F_StaffID = @iID;
DELETE FROM t_staff WHERE F_ID = @iID;

SELECT '-----------------CASE9: 新增员工时，可以填写离职员工的手机号、微信、身份证，新增成功后，再修改这个员工的信息时，也能修改成功------------------' AS 'CASE9';
INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('测试用户', '13196721886', '341522198412111666', 'd2sasb4', 'JHSG123DF5ASFA1SF85321', now(), 1, 1, 1, 0, now(), now());

SET @iID = Last_insert_id();
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sPhone = '13196721886';
SET @sICID = '341522198412111666';
SET @sWeChat = 'd2sasb4';
SET @sName = '店员1号'; 
SET @dPasswordExpireDate = '2019-01-01';
SET @iShopID = 1;
SET @iDepartmentID = 1;
SET @iRoleID = 1;
SET @iStatus = 0;
SET @iReturnSalt = 1;

CALL SP_Staff_Update(@iErrorCode, @sErrorMsg, @iID, @sName, @sPhone, @sICID, @sWeChat, @dPasswordExpireDate, @iShopID, @iDepartmentID, @iRoleID, @iStatus, @iReturnSalt);

SELECT @sErrorMsg;
SELECT 1 FROM t_staff WHERE F_ID = @iID AND F_Phone = @sPhone AND F_ICID = @sICID AND F_Name = @sName;
SELECT IF(@iErrorCode = 0 AND FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case9 Testing Result';

DELETE FROM t_staffrole WHERE F_StaffID = @iID;
DELETE FROM t_staff WHERE F_ID = @iID;

SELECT '-----------------CASE10:修改员工状态为离职，错误码7------------------' AS 'CASE10';
SET @iID = 1;
SET @sErrorMsg = '';
SET @iErrorCode = 0;
SET @sPhone = '34512346547235';
SET @sICID = '13642356546x2313s';
SET @sWeChat = '1232341313144xh';
SET @sName = '黄晓明22'; 
SET @dPasswordExpireDate = '2019-01-01';
SET @iShopID = 1;
SET @iDepartmentID = 1;
SET @iRoleID = 1;
SET @iStatus = 1;
SET @iReturnSalt = 1;

CALL SP_Staff_Update(@iErrorCode, @sErrorMsg, @iID, @sName, @sPhone, @sICID, @sWeChat, @dPasswordExpireDate, @iShopID, @iDepartmentID, @iRoleID, @iStatus, @iReturnSalt);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'Case10 Testing Result';

SELECT '-----------------CASE11:离职转在职------------------' AS 'CASE11';
INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('测试用户001', '15279254465', '341522198412111666', 'd2sasb4', 'JHSG123DF5ASFA1SF85321', now(), 1, 1, 1, 1, now(), now());

SET @iID = last_insert_id();
SET @sErrorMsg = '';
SET @iErrorCode = 0;
SET @sPhone = '34512346547235';
SET @sICID = '13642356546x2313s';
SET @sWeChat = '1232341313144xh';
SET @sName = '黄晓明22'; 
SET @dPasswordExpireDate = '2019-01-01';
SET @iShopID = 1;
SET @iDepartmentID = 1;
SET @iRoleID = 1;
SET @iStatus = 0;
SET @iReturnSalt = 1;

CALL SP_Staff_Update(@iErrorCode, @sErrorMsg, @iID, @sName, @sPhone, @sICID, @sWeChat, @dPasswordExpireDate, @iShopID, @iDepartmentID, @iRoleID, @iStatus, @iReturnSalt);

SELECT @sErrorMsg;
SELECT 1 FROM t_staff WHERE F_ID = @iID AND F_Phone = @sPhone AND F_ICID = @sICID AND F_Name = @sName;
SELECT IF(@iErrorCode = 0 AND FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case11 Testing Result';

DELETE FROM t_staffrole WHERE F_StaffID = @iID;
DELETE FROM t_staff WHERE F_ID = @iID;