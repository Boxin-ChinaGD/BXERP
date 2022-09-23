SELECT '++++++++++++++++++ Test_SP_Staff_Delete.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: ����ɾ�� -------------------------' AS 'Case1';

INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('aa', '12345678911', '123456789123456', '123456', 'asdefggsdjfasgyf', now(), 1, 1, 1, 0, now(), now());
SET @iID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_Staff_Delete(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT 1 FROM t_Staff WHERE F_ID = @iID AND F_Status = 1; 
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';

DELETE FROM  t_staff WHERE F_ID = @iID;

SELECT '-------------------- Case3: Staff�쳣״̬������ɾ��-------------------------' AS 'Case3';

INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('aa', '12345678911', '123456789123456', '123456', 'asdefggsdjfasgyf', now(), 1, 1, 1, -1, now(), now());
SET @iID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
CALL SP_Staff_Delete(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT @iErrorCode;

SELECT IF(@sErrorMsg = '��Ա����ɾ���������ظ�ɾ��' AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';

DELETE FROM t_staff WHERE F_ID = @iID;

SELECT '-------------------- Case4: StaffΪ��ǰ�˺�,û��������ɾ��-------------------------' AS 'Case4';
INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('aax', '12345678971', '123456789123456f', '123456f', 'asdefggsdjfasgfyf', now(), 1, 1, 1, 0, now(), now());
SET @iID = LAST_INSERT_ID();
-- 
INSERT INTO t_staffrole (F_StaffID, F_RoleID) VALUES (@iID, 3);
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
CALL SP_Staff_Delete(@iErrorCode, @sErrorMsg, @iID);
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';

SELECT '-------------------- Case5: StaffΪ��ǰ�˺�,����������ɾ��-------------------------' AS 'Case5';
INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('aax', '12345678971', '123456789123456f', '123456f', 'asdefggsdjfasgfyf', now(), 1, 1, 1, 0, now(), now());
SET @iID = LAST_INSERT_ID();
-- 
INSERT INTO t_staffrole (F_StaffID, F_RoleID) VALUES (@iID, 3);
-- 
INSERT INTO t_warehouse (F_Name, F_Address, F_Status, F_StaffID)
VALUES ('�ֿ�995', 'ֲ��԰1', 0, @iID);
SET @iID2 = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
CALL SP_Staff_Delete(@iErrorCode, @sErrorMsg, @iID);
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';
DELETE FROM t_warehouse WHERE F_ID = @iID2;