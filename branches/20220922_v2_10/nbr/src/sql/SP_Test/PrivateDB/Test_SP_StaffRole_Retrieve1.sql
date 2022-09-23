SELECT '++++++++++++++++++Test_SP_StaffRole_Retrieve1.sql+++++++++++++++++++++++';

INSERT INTO t_role (F_Name, F_CreateDatetime, F_UpdateDatetime)
VALUES ('测试角色', now(), now());
SET @roleID = LAST_INSERT_ID();

INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('测试用户', '12345678912', '12345678912345670', '88884100', 'J354FGH988SDF1BHX5', now(), 1, 1, 1, 0, now(), now());
SET @staffID = LAST_INSERT_ID();

INSERT INTO t_staffrole (F_StaffID, F_RoleID)
VALUES (@staffID, @roleID);
SET @srID = LAST_INSERT_ID();


SELECT '-----------------CASE1: 根据StaffID查找------------------' AS 'CASE1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iStaffID = @staffID;

CALL SP_StaffRole_Retrieve1(@iErrorCode, @sErrorMsg, @iID, @iStaffID);

SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT '-----------------CASE2: 根据ID查找------------------' AS 'CASE2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = @srID;
SET @iStaffID = 0;

CALL SP_StaffRole_Retrieve1(@iErrorCode, @sErrorMsg, @iID, @iStaffID);

SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';

SELECT '-----------------CASE3: 根据id与StaffID查找------------------' AS 'CASE3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = @srID;
SET @iStaffID = @staffID;

CALL SP_StaffRole_Retrieve1(@iErrorCode, @sErrorMsg, @iID, @iStaffID);

SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case3 Testing Result';

DELETE FROM t_staffrole WHERE F_ID = @srID;
DELETE FROM t_staff WHERE F_ID = @staffID;
DELETE FROM t_role WHERE F_ID = @roleID;