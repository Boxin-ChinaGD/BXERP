SELECT '++++++++++++++++++ Test_SPD_Staff_CheckPhone.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:正常测试 -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Staff_CheckPhone(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';


SELECT '-------------------- Case2:手机号不是以1开头 -------------------------' AS 'Case2';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sPhone = '23456789101';
-- 
INSERT INTO T_Staff (F_Name,F_Phone,F_ICID,F_WeChat,F_Salt,F_PasswordExpireDate,F_IsFirstTimeLogin,
F_ShopID,F_Status,F_DepartmentID)
VALUES ('重复',@sPhone,'440883198412111333' ,'a326dsd12','B1AFC07474C37C5AEC4199ED28E09705','2019-01-01',1,1,0,1);
SET @iID = LAST_INSERT_ID();
-- 
CALL SPD_Staff_CheckPhone(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iID ,'的员工的手机号不是以1开头') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case2 Testing Result';
-- 
DELETE FROM t_staff WHERE F_ID = @iID;



SELECT '-------------------- Case3:手机号不是11位 -------------------------' AS 'Case3';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sPhone = '1345678910';
-- 
INSERT INTO T_Staff (F_Name,F_Phone,F_ICID,F_WeChat,F_Salt,F_PasswordExpireDate,F_IsFirstTimeLogin,
F_ShopID,F_Status,F_DepartmentID)
VALUES ('重复',@sPhone,'440883198412111333' ,'a326dsd12','B1AFC07474C37C5AEC4199ED28E09705','2019-01-01',1,1,0,1);
SET @iID = LAST_INSERT_ID();
-- 
CALL SPD_Staff_CheckPhone(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iID ,'的员工的手机号不是11位') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case3 Testing Result';
-- 
DELETE FROM t_staff WHERE F_ID = @iID;



SELECT '-------------------- Case4:手机号不是纯数字 -------------------------' AS 'Case4';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sPhone = '13456789abc';
-- 
INSERT INTO T_Staff (F_Name,F_Phone,F_ICID,F_WeChat,F_Salt,F_PasswordExpireDate,F_IsFirstTimeLogin,
F_ShopID,F_Status,F_DepartmentID)
VALUES ('重复',@sPhone,'440883198412111333' ,'a326dsd12','B1AFC07474C37C5AEC4199ED28E09705','2019-01-01',1,1,0,1);
SET @iID = LAST_INSERT_ID();
-- 
CALL SPD_Staff_CheckPhone(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iID ,'的员工的手机号不是纯数字') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case4 Testing Result';
-- 
DELETE FROM t_staff WHERE F_ID = @iID;



SELECT '-------------------- Case5:手机号重复 -------------------------' AS 'Case5';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sPhone = '13888888888';
-- 
INSERT INTO T_Staff (F_Name,F_Phone,F_ICID,F_WeChat,F_Salt,F_PasswordExpireDate,F_IsFirstTimeLogin,
F_ShopID,F_Status,F_DepartmentID)
VALUES ('重复',@sPhone,'440883198412111333' ,'a326dsd12','B1AFC07474C37C5AEC4199ED28E09705','2019-01-01',1,1,0,1);
SET @iID = LAST_INSERT_ID();
-- 
CALL SPD_Staff_CheckPhone(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('手机号为', @sPhone ,'的在职员工有多个，在职员工的手机号是唯一的不能重复') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case5 Testing Result';
-- 
DELETE FROM t_staff WHERE F_ID = @iID;