-- case1:当bForceDelete=0的时删除已有权限的角色，错误码为7
SET @iID = 3;
SET @sErrorMsg = '';
SET @iErrorCode = 0;
SET @bForceDelete = 0;

CALL SP_Role_Delete(@iErrorCode, @sErrorMsg, @iID, @bForceDelete);
SELECT @sErrorMsg;

SELECT 1 FROM t_role WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Testing Result';

-- case2:当bForceDelete=0的时删除没有权限的角色
INSERT INTO t_role (F_Name, F_CreateDatetime, F_UpdateDatetime)
VALUES ('测试角色', now(), now());
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();
SET @bForceDelete = 0;
SET @iErrorCode = 0;


CALL SP_Role_Delete(@iErrorCode, @sErrorMsg, @iID, @bForceDelete);
SELECT @sErrorMsg;

SELECT 1 FROM t_role WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';

-- case3:当bForceDelete=1的时候可以强制删除已有权限的角色
INSERT INTO t_role (F_Name, F_CreateDatetime, F_UpdateDatetime)
VALUES ('测试角色', now(), now());
SET @sErrorMsg = '';
SET @iID = Last_insert_id();

INSERT INTO t_role_permission (F_RoleID, F_PermissionID)
VALUES (@iID, 5);

SET @iErrorCode = 0;
SET @bForceDelete = 1;

CALL SP_Role_Delete(@iErrorCode, @sErrorMsg, @iID, @bForceDelete);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_role WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';

-- case4:当bForceDelete=1的时候可以强制删除已在员工角色存在的角色
INSERT INTO t_role (F_Name, F_CreateDatetime, F_UpdateDatetime)
VALUES ('测试角色', now(), now());
SET @iID = Last_insert_id();
SET @sErrorMsg = '';
INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('测试用户', '12345646234789122', '124126554235', 'a2dasf', 'JHSG123DF5ASFA1SF85321', now(), 1, 1, 1, 0, now(), now());
SET @staffID = Last_insert_id();

INSERT INTO t_staffrole (F_StaffID, F_RoleID)
VALUES (@staffID, @iID);

SET @iErrorCode = 0;
SET @bForceDelete = 1;

CALL SP_Role_Delete(@iErrorCode, @sErrorMsg, @iID, @bForceDelete);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_role WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';

DELETE FROM t_staff WHERE F_ID = @staffID;

-- case5:当bForceDelete=1的时删除没有权限的角色
INSERT INTO t_role (F_Name, F_CreateDatetime, F_UpdateDatetime)
VALUES ('测试角色', now(), now());
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();
SET @bForceDelete = 0;
SET @iErrorCode = 0;


CALL SP_Role_Delete(@iErrorCode, @sErrorMsg, @iID, @bForceDelete);

SELECT @sErrorMsg;
SELECT 1 FROM t_role WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';

-- case6:当bForceDelete=0的时删除已有员工在使用的角色，错误码为7
SET @iID = 3;
SET @sErrorMsg = '';
SET @iErrorCode = 0;
SET @bForceDelete = 0;

CALL SP_Role_Delete(@iErrorCode, @sErrorMsg, @iID, @bForceDelete);

SELECT @sErrorMsg;
SELECT 1 FROM t_role WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Testing Result';