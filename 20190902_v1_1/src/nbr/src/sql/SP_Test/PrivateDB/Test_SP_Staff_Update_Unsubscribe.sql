SELECT '++++++++++++++++++ Test_SP_Staff_Update_Unsubscribe.sql ++++++++++++++++++++';

SELECT '-------------------- CASE1:正常更新-------------------------' AS 'Case1'; 
-- case1: 正常更新
INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_OpenID, F_Unionid, F_pwdEncrypted, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('收银员一号', '13915460955', '440883198412115051', 'e123fasf1', 'dsafsd', 'fdsafds', NULL, 'B1AFC07474C37C5AEC4199ED28E09705', '01/01/2001 09:14:09 上午', 0, 1, 1, 0, '01/01/2000 09:14:09 上午', '01/01/2000 09:14:09 上午');
SET @staffID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SP_Staff_Update_Unsubscribe(@iErrorCode, @sErrorMsg, @staffID);
-- 
SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT 1 FROM t_staff WHERE F_ID = @staffID AND F_OpenID IS NULL;
SELECT IF(@iErrorCode = 0 AND FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case1 Testing Result';
DELETE FROM t_staff WHERE F_ID = @staffID;



SELECT '-------------------- CASE2:传入不存在的staffID-------------------------' AS 'Case2'; 
-- 
SET @staffID = 999999;
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SP_Staff_Update_Unsubscribe(@iErrorCode, @sErrorMsg, @staffID);
-- 
SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT IF(@iErrorCode = 2 AND @sErrorMsg = '员工不存在', '测试成功', '测试失败') AS 'Case2 Testing Result';