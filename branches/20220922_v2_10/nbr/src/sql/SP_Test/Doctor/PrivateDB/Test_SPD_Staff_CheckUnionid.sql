SELECT '++++++++++++++++++ Test_SPD_Staff_CheckUnionid.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:�������� -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Staff_CheckUnionid(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';



SELECT '-------------------- Case2:F_Unionid�ظ��ظ� -------------------------' AS 'Case2';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sUnionid = 'abc123456789';
-- 
INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_OpenID, F_Unionid, F_pwdEncrypted, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('����Ա���','18915460943','440883198412110001','a325sds',NULL,@sUnionid,NULL,'B1AFC07474C37C5AEC4199ED28E09708','2001/01/01 9:14:09',0,1,1,0,'2000/01/01 9:14:09','2000/01/01 9:14:09');
SET @iID1 = LAST_INSERT_ID();
INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_OpenID, F_Unionid, F_pwdEncrypted, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('����Ա��','18915460959','440883198412110012','a3221ds',NULL,@sUnionid,NULL,'B1AFC07474C37C5AEC4199ED28E09708','2001/01/01 9:14:09',0,1,1,0,'2000/01/01 9:14:09','2000/01/01 9:14:09');
SET @iID2 = LAST_INSERT_ID();
-- 
CALL SPD_Staff_CheckUnionid(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('UnionidΪ', @sUnionid ,'����ְԱ���ж������ְԱ����Unionid��Ψһ�Ĳ����ظ�') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
-- 
DELETE FROM t_staff WHERE F_ID = @iID1;
DELETE FROM t_staff WHERE F_ID = @iID2;