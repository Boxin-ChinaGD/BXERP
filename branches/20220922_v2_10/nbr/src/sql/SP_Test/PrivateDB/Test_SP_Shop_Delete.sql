SELECT '++++++++++++++++++Test_SP_Shop_Delete.sql+++++++++++++++++++++++';

SELECT '-----------------Case1:	����ɾ���ŵ� ------------------' AS 'Case1';
--  �����ŵ��빫˾�Ĺ�����ʱû��
-- ...

-- ����һ���ŵ�
INSERT INTO t_shop (F_Name, F_BxStaffID, F_CompanyID, F_Address, F_Status, F_Longitude, F_Latitude, F_Key)
VALUES ('�ŵ�1��777', 1, 1, '�ڶ�', 1, 12.2, 22.2, '123456');
SET @sErrorMsg = '';
SET @iShopID = last_insert_id();

-- ����һ����Ӧ�ŵ���staff
INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('aa', '12345678911', '123456789123456', '123456', 'asdefggsdjfasgyf', now(), 1, @iShopID, 1, 0, now(), now());

-- ����һ����Ӧ�ŵ��µ�pos��
INSERT INTO t_pos (F_POS_SN, F_ShopID, F_Salt, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('SN15618912316', @iShopID, 'DFSDGSDKJG546S8FDH', 0, now(), now());

-- ������ŵ���ص�pos����staff�ҳ����赽��Ӧ��ֵ����
SET @iPosSync = 0;
SET @iStaffSync = 0;
SET @iShopStatus = 0;
SET @iPosStatus = 0;
SET @iStaffStatus = 0;
SET @iErrorCode = 0;
SET @iCompanyID = 1;

CALL SP_Shop_Delete(@iErrorCode, @sErrorMsg, @iShopID, @iCompanyID);
SELECT @iErrorCode;
SELECT @sErrorMsg;

-- �ҳ���Ӧ״̬���ŵ�������Ϣ
SELECT 1 INTO @iShopStatus FROM t_shop WHERE F_ID = @iShopID AND F_Status = 3;
SELECT 1 INTO @iPosStatus FROM t_pos WHERE F_ShopID = @iShopID AND F_Status = 1;
SELECT 1 INTO @iStaffStatus FROM t_staff WHERE F_ShopID = @iShopID AND F_Status = 1;

-- ����Ӧ�������Ƿ���ȷ
SELECT IF(@iShopStatus = 1 AND @iPosStatus = 1 AND @iStaffStatus = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

-- ɾ�������������ŵ�������Ϣ��������Ⱦ���ݿ�
DELETE FROM t_possynccache WHERE F_SyncData_ID IN (SELECT F_ID FROM t_pos WHERE F_ShopID = @iShopID);
DELETE FROM t_pos WHERE F_ShopID = @iShopID;
DELETE FROM t_staff WHERE F_ShopID = @iShopID;
DELETE FROM t_shop WHERE F_ID = @iShopID;

SELECT '-----------Case2: ɾ����������staff������pos�����ŵ�-------------' AS 'Case2';
INSERT INTO t_shop (F_Name, F_CompanyID, F_Address, F_Status, F_Longitude, F_Latitude, F_Key, F_BxStaffID)VALUES ('�ŵ�1��4444', 1, '�ڶ�', 1, 12.2, 22.2, '123456', 1);
SET @iShopID = last_insert_id();

INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('aa1', '12345678911', '12345671891234561', '123456', 'asdefggsdjfasgyf', now(), 1, @iShopID, 1, 0, now(), now());

INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('aa2', '12345678912', '12345678914234552', '1234561', 'asdefggsdjfasgyf', now(), 1, @iShopID, 1, 0, now(), now());

INSERT INTO t_pos (F_POS_SN, F_ShopID, F_Salt, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('SN15618912353', @iShopID, 'DFSDGSDKJG546S8FDH', 0, now(), now());

INSERT INTO t_pos (F_POS_SN, F_ShopID, F_Salt, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('SN15618912354', @iShopID, 'DFSDGSDKJG546S8FDH', 0, now(), now());

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCompanyID = 1;

CALL SP_Shop_Delete(@iErrorCode, @sErrorMsg, @iShopID, @iCompanyID);
SELECT @iErrorCode;
SELECT @sErrorMsg;

SET @iShopStatus = 0;
SET @iPosStatus = 0;
SET @IStaffStatus = 0;
SET @iPosSync = 0;
SET @iStaffSync = 0;

SELECT 1 INTO @iShopStatus FROM t_shop WHERE F_ID = @iShopID AND F_Status = 3;
SELECT 1 FROM t_pos WHERE F_ShopID = @iShopID AND F_Status = 1;
SET @iPosStatus = FOUND_ROWS();
SELECT 1 FROM t_staff WHERE F_ShopID = @iShopID AND F_Status = 1;
SET @IStaffStatus = FOUND_ROWS();

--	SELECT @iShopStatus, @iPosStatus, @IStaffStatus, @iPosSync, @iStaffSync;

SELECT IF (@iShopStatus = 1 AND @iPosStatus = 2 AND @iStaffStatus = 2  AND @iErrorCode = 0,'���Գɹ�','����ʧ��') AS 'Case Testind Result';

DELETE FROM t_possynccache WHERE F_SyncData_ID IN (SELECT F_ID FROM t_pos WHERE F_ShopID = @iShopID);
DELETE FROM t_pos WHERE F_ShopID = @iShopID;
DELETE FROM t_staff WHERE F_ShopID = @iShopID;
DELETE FROM t_shop WHERE F_ID = @iShopID;

SELECT '-----------------Case3:	������companyID ------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @iCompanyID = 0;

CALL SP_Shop_Delete(@iErrorCode, @sErrorMsg, @iShopID, @iCompanyID);
SELECT @iErrorCode;

SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';

SELECT '-----------------Case4:	��������0������ɾ�� ------------------' AS 'Case4';
--  �����ŵ��빫˾�Ĺ�����ʱû��
-- ...

-- ����һ���ŵ�
INSERT INTO t_shop (F_Name, F_BxStaffID, F_CompanyID, F_Address, F_Status, F_Longitude, F_Latitude, F_Key)
VALUES ('�ŵ�1��777', 1, 1, '�ڶ�', 4, 12.2, 22.2, '123456');
SET @sErrorMsg = '';
SET @iShopID = last_insert_id();

-- ����һ����Ӧ�ŵ���staff
INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('aa', '12345678911', '123456789123456', '123456', 'asdefggsdjfasgyf', now(), 1, @iShopID, 1, 0, now(), now());

-- ����һ����Ӧ�ŵ��µ�pos��
INSERT INTO t_pos (F_POS_SN, F_ShopID, F_Salt, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('SN15618912316', @iShopID, 'DFSDGSDKJG546S8FDH', 0, now(), now());

-- ������ŵ���ص�pos����staff�ҳ����赽��Ӧ��ֵ����
SET @iPosSync = 0;
SET @iStaffSync = 0;
SET @iShopStatus = 0;
SET @iPosStatus = 0;
SET @iStaffStatus = 0;
SET @iErrorCode = 0;
SET @iCompanyID = 1;

CALL SP_Shop_Delete(@iErrorCode, @sErrorMsg, @iShopID, @iCompanyID);
SELECT @iErrorCode;
SELECT @sErrorMsg;

-- �ҳ���Ӧ״̬���ŵ�������Ϣ
SELECT 1 INTO @iShopStatus FROM t_shop WHERE F_ID = @iShopID AND F_Status = 4;
SELECT 1 INTO @iPosStatus FROM t_pos WHERE F_ShopID = @iShopID AND F_Status = 0;
SELECT 1 INTO @iStaffStatus FROM t_staff WHERE F_ShopID = @iShopID AND F_Status = 0;
SELECT @iShopStatus;
SELECT @iPosStatus;
SELECT @iStaffStatus;
SELECT @iErrorCode;

-- ����Ӧ�������Ƿ���ȷ
SELECT IF(@iShopStatus = 1 AND @iPosStatus = 1 AND @iStaffStatus = 1 AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';

-- ɾ�������������ŵ�������Ϣ��������Ⱦ���ݿ�
DELETE FROM t_possynccache WHERE F_SyncData_ID IN (SELECT F_ID FROM t_pos WHERE F_ShopID = @iShopID);
DELETE FROM t_pos WHERE F_ShopID = @iShopID;
DELETE FROM t_staff WHERE F_ShopID = @iShopID;
DELETE FROM t_shop WHERE F_ID = @iShopID;


SELECT '-----------------Case5:	ɾ��Ĭ���ŵ�,ɾ��ʧ�� ------------------' AS 'Case5';
-- 
SET @iErrorCode = 0;
SET @iCompanyID = 1;
SET @iShopID = 1;
SET @sErrorMsg = '';

CALL SP_Shop_Delete(@iErrorCode, @sErrorMsg, @iShopID, @iCompanyID);
SELECT @iErrorCode;
SELECT @sErrorMsg;

-- �ҳ���Ӧ״̬���ŵ�������Ϣ
SELECT 1 INTO @iShopStatus FROM t_shop WHERE F_ID = @iShopID AND F_Status = 3;
SELECT 1 INTO @iPosStatus FROM t_pos WHERE F_ShopID = @iShopID AND F_Status = 1;
SELECT 1 INTO @iStaffStatus FROM t_staff WHERE F_ShopID = @iShopID AND F_Status = 1;

-- ����Ӧ�������Ƿ���ȷ
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = 'Ĭ���ŵ겻�ܶ���', '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';