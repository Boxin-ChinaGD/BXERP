SELECT '++++++++++++++++++ Test_SPD_Staff_CheckIsFirstTimeLogin.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:正常测试 -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Staff_CheckIsFirstTimeLogin(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';


SELECT '-------------------- Case2:F_IsFirstTimeLogin的值不是0或1 -------------------------' AS 'Case2';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
INSERT INTO T_Staff (F_Name,F_Phone,F_ICID,F_WeChat,F_Salt,F_PasswordExpireDate,F_IsFirstTimeLogin,
F_ShopID,F_Status,F_DepartmentID)
VALUES ('重复','13144496666','440883198412111231' ,'a326dsd12','B1AFC07474C37C5AEC4199ED28E09705','2019-01-01',2,1,0,1);
SET @iID = LAST_INSERT_ID();
-- 
CALL SPD_Staff_CheckIsFirstTimeLogin(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iID ,'的员工的F_IsFirstTimeLogin的值不是0或1') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case2 Testing Result';
-- 
DELETE FROM t_staff WHERE F_ID = @iID;