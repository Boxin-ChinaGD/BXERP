SELECT '++++++++++++++++++ Test_SP_Staff_Update_OpenidAndUnionid ++++++++++++++++++++';

SELECT '-------------------- CASE1:正常更新-------------------------' AS 'Case1'; 
-- case1: 正常更新
INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('测试用户', '1234564623478912212', '1123213131', '1232132131', 'JHSG123DF5ASFA1SF85321', now(), 1, 1, 1, 0, now(), now());

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sPhone = '15854320895';
SET @sOpenId = 'oOjkQt_1hkKWIeCNVWhOAu8zQ8S8';
SET @sUnionid = '13453fsdr2312edsfre6';
SET @iReturnSalt = 1;

CALL SP_Staff_Update_OpenidAndUnionid(@iErrorCode, @sErrorMsg, @sPhone,@sOpenId,@sUnionid, @iReturnSalt);

SELECT @sErrorMsg;
SELECT 1 FROM t_staff WHERE F_Phone = @sPhone AND F_OpenID = @sOpenId AND F_Unionid = @sUnionid;
SELECT @iErrorCode;
SELECT IF(@iErrorCode = 0 AND FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case1 Testing Result';
DELETE FROM t_staff WHERE F_ID = last_insert_id();

SELECT '-------------------- CASE2:phone不存在-------------------------' AS 'Case2'; 
-- case2:phone不存在
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sPhone = '123';
SET @sOpenId = 'qwesf123324df31e546';
SET @sUnionid = '13453fsd12352edsfre6';
SET @iReturnSalt = 1;

CALL SP_Staff_Update_OpenidAndUnionid(@iErrorCode, @sErrorMsg, @sPhone,@sOpenId,@sUnionid, @iReturnSalt);

SELECT @sErrorMsg;
SELECT 1 FROM t_staff WHERE F_Phone = @sPhone AND F_OpenID = @sOpenId AND F_Unionid = @sUnionid;
SELECT @iErrorCode;
SELECT IF(@iErrorCode = 1 AND FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case2 Testing Result';

SELECT '-------------------- CASE3:phone为空-------------------------' AS 'Case3'; 
-- case3:phone为空
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sPhone = '';
SET @sOpenId = 'qwesf123324df31e546';
SET @sUnionid = '13453fsd12352edsfre6';
SET @iReturnSalt = 1;

CALL SP_Staff_Update_OpenidAndUnionid(@iErrorCode, @sErrorMsg, @sPhone,@sOpenId,@sUnionid, @iReturnSalt);

SELECT @sErrorMsg;
SELECT 1 FROM t_staff WHERE F_Phone = @sPhone AND F_OpenID = @sOpenId AND F_Unionid = @sUnionid;
SELECT @iErrorCode;
SELECT IF(@iErrorCode = 7 AND FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case3 Testing Result';

SELECT '-------------------- CASE4:openid为空-------------------------' AS 'Case4'; 
-- case4:openid为空
INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('测试用户', '12345646234789122', '112321313123', '123213213123', 'JHSG123DF5ASFA1SF85321', now(), 1, 1, 1, 0, now(), now());

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sPhone = '12345646234789122';
SET @sOpenId = '';
SET @sUnionid = '13453fsdr2312edsfre6';
SET @iReturnSalt = 1;

CALL SP_Staff_Update_OpenidAndUnionid(@iErrorCode, @sErrorMsg, @sPhone,@sOpenId,@sUnionid, @iReturnSalt);

SELECT @sErrorMsg;
SELECT 1 FROM t_staff WHERE F_Phone = @sPhone AND F_OpenID = @sOpenId AND F_Unionid = @sUnionid;
SELECT @iErrorCode;
SELECT IF(@iErrorCode = 7 AND FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case4 Testing Result';

DELETE FROM t_staff WHERE F_ID = last_insert_id();

SELECT '-------------------- CASE5:更新的OpenID已存在 -------------------------' AS 'Case5'; 
SET @sOpenID = '1234567777777';
INSERT INTO t_staff (F_OpenID, F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES (@sOpenID/* F_OpenID */, '测试用户' /* F_Name */, '12345646234789122'/* F_Phone */, '112321313123'/* F_ICID */, '123213213123'/* F_WeChat */, 'JHSG123DF5ASFA1SF85321'/* F_Salt */, now()/* F_PasswordExpireDate */,
		 1/* F_IsFirstTimeLogin */, 1/* F_ShopID */, 1/* F_DepartmentID */, 0/* F_Status */, now()/* F_CreateDatetime */, now()/* F_UpdateDatetime */);
SET @iID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sPhone = '12345646234789122';
SET @sUnionid = '13453fsdr2312edsfre6';
SET @iReturnSalt = 1;
-- 
CALL SP_Staff_Update_OpenidAndUnionid(@iErrorCode, @sErrorMsg, @sPhone,@sOpenId,@sUnionid, @iReturnSalt);
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'Case5 Testing Result';
-- 
DELETE FROM t_staff WHERE F_ID = @iID;