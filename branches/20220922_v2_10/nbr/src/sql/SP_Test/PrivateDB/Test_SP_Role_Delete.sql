-- case1:��bForceDelete=0��ʱɾ������Ȩ�޵Ľ�ɫ��������Ϊ7
SET @iID = 3;
SET @sErrorMsg = '';
SET @iErrorCode = 0;
SET @bForceDelete = 0;

CALL SP_Role_Delete(@iErrorCode, @sErrorMsg, @iID, @bForceDelete);
SELECT @sErrorMsg;

SELECT 1 FROM t_role WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Testing Result';

-- case2:��bForceDelete=0��ʱɾ��û��Ȩ�޵Ľ�ɫ
INSERT INTO t_role (F_Name, F_CreateDatetime, F_UpdateDatetime)
VALUES ('���Խ�ɫ', now(), now());
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();
SET @bForceDelete = 0;
SET @iErrorCode = 0;


CALL SP_Role_Delete(@iErrorCode, @sErrorMsg, @iID, @bForceDelete);
SELECT @sErrorMsg;

SELECT 1 FROM t_role WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';

-- case3:��bForceDelete=1��ʱ�����ǿ��ɾ������Ȩ�޵Ľ�ɫ
INSERT INTO t_role (F_Name, F_CreateDatetime, F_UpdateDatetime)
VALUES ('���Խ�ɫ', now(), now());
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
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';

-- case4:��bForceDelete=1��ʱ�����ǿ��ɾ������Ա����ɫ���ڵĽ�ɫ
INSERT INTO t_role (F_Name, F_CreateDatetime, F_UpdateDatetime)
VALUES ('���Խ�ɫ', now(), now());
SET @iID = Last_insert_id();
SET @sErrorMsg = '';
INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('�����û�', '12345646234789122', '124126554235', 'a2dasf', 'JHSG123DF5ASFA1SF85321', now(), 1, 1, 1, 0, now(), now());
SET @staffID = Last_insert_id();

INSERT INTO t_staffrole (F_StaffID, F_RoleID)
VALUES (@staffID, @iID);

SET @iErrorCode = 0;
SET @bForceDelete = 1;

CALL SP_Role_Delete(@iErrorCode, @sErrorMsg, @iID, @bForceDelete);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_role WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';

DELETE FROM t_staff WHERE F_ID = @staffID;

-- case5:��bForceDelete=1��ʱɾ��û��Ȩ�޵Ľ�ɫ
INSERT INTO t_role (F_Name, F_CreateDatetime, F_UpdateDatetime)
VALUES ('���Խ�ɫ', now(), now());
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();
SET @bForceDelete = 0;
SET @iErrorCode = 0;


CALL SP_Role_Delete(@iErrorCode, @sErrorMsg, @iID, @bForceDelete);

SELECT @sErrorMsg;
SELECT 1 FROM t_role WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';

-- case6:��bForceDelete=0��ʱɾ������Ա����ʹ�õĽ�ɫ��������Ϊ7
SET @iID = 3;
SET @sErrorMsg = '';
SET @iErrorCode = 0;
SET @bForceDelete = 0;

CALL SP_Role_Delete(@iErrorCode, @sErrorMsg, @iID, @bForceDelete);

SELECT @sErrorMsg;
SELECT 1 FROM t_role WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Testing Result';