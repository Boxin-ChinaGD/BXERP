SELECT '++++++++++++++++++Test_SP_Staff_ResetPassword.sql+++++++++++++++++++++++';

SELECT '-----------------CASE1: �����޸�����------------------' AS 'CASE1';
INSERT INTO T_Staff (F_Name,F_Phone,F_ICID,F_WeChat,F_Salt,F_PasswordExpireDate,F_IsFirstTimeLogin,
F_ShopID,F_DepartmentID,F_Status)
VALUES ('��Ա16��','13196481779','440880s12s1411ff','123f1sss111s0f','7DBCB7F47751CB4C224C6B862BA2BE04','2019-01-01','1',5,1,0);
SET @iID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sPhone = '13196481779';
SET @sSalt = '7DBCB7F477514654654654';
SET @iReturnSalt = 1;
SET @iIsFirstTimeLogin = 1;

CALL SP_Staff_ResetPassword(@iErrorCode, @sErrorMsg, @sSalt,@sPhone, @iReturnSalt, @iIsFirstTimeLogin);
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

DELETE FROM t_staffrole WHERE F_StaffID = @iID;
DELETE FROM t_staff WHERE F_ID = @iID;

SELECT '-----------------CASE2: ����ְԱ���޸�����------------------' AS 'CASE2';
INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('�����û�001', '15279254465', '341522198412111666', 'd2sasb4', 'JHSG123DF5ASFA1SF85321', now(), 1, 1, 1, 1, now(), now());
SET @staffID1 = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sPhone = '15279254465';
SET @sSalt = '7DBCB7F477514654654654';
SET @iReturnSalt = 0;
SET @iIsFirstTimeLogin = 1;

CALL SP_Staff_ResetPassword(@iErrorCode, @sErrorMsg, @sSalt,@sPhone, @iReturnSalt, @iIsFirstTimeLogin);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

SELECT '-----------------CASE3: ����һ������ְԱ����ͬ�ֻ���Ա�����޸ĸ�Ա������------------------' AS 'CASE3';
INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('�����û�001', '15279254465', '341522198412111666', 'd2sasb4', 'JHSG123DF5ASFA1SF85321', now(), 1, 1, 1, 0, now(), now());
SET @staffID2 = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sPhone = '15279254465';
SET @sSalt = '7DBCB7F477514654654654';
SET @iReturnSalt = 0;
SET @iIsFirstTimeLogin = 1;

CALL SP_Staff_ResetPassword(@iErrorCode, @sErrorMsg, @sSalt,@sPhone, @iReturnSalt, @iIsFirstTimeLogin);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
DELETE FROM t_staffrole WHERE F_StaffID = @staffID1;
DELETE FROM t_staff WHERE F_ID = @staffID1;
DELETE FROM t_staffrole WHERE F_StaffID = @staffID2;
DELETE FROM t_staff WHERE F_ID = @staffID2;