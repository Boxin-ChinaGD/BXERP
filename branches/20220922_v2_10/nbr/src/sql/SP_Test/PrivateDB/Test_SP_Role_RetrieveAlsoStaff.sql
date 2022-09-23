SELECT '+++++++++++++++++++++++ Test_SP_Role_RetrieveAlsoStaff.sql ++++++++++++++++++++++++++++';

INSERT INTO t_role (F_Name, F_CreateDatetime, F_UpdateDatetime)
VALUES ('≤‚ ‘Ω«…´', now(), now());
SET @roleID = LAST_INSERT_ID();
SET @sErrorMsg = '';

INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('≤‚ ‘”√ªß', '12345678912', '12345678912345678', '8888414', 'J354FGH988SDF1BHX5', now(), 1, 1, 1, 0, now(), now());
SET @staffID = LAST_INSERT_ID();

INSERT INTO t_staffrole (F_StaffID, F_RoleID)
VALUES (@staffID, @roleID);
SET @srID = LAST_INSERT_ID();

SET @iErrorCode = 0;

CALL SP_Role_RetrieveAlsoStaff(@iErrorCode, @staffID, @roleID);

SELECT @staffID;
SELECT IF(@iErrorCode = 0, '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'Case1 Testing Result';

DELETE FROM t_staffrole WHERE F_ID = @srID;
DELETE FROM t_staff WHERE F_ID = @staffID;
DELETE FROM t_role WHERE F_ID = @roleID;