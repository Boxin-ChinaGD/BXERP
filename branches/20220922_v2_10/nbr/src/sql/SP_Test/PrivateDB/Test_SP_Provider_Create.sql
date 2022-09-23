SELECT '++++++++++++++++++ Test_SP_Provider_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: 新建不重复的供应商 -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '淘宝';
SET @iDistrictID = 1;
SET @sAddress = '广州市天河区二十八中学';
SET @sContactName = 'ada';
SET @iMobile = '13122455442';

CALL SP_Provider_Create(@iErrorCode, @sErrorMsg, @sName ,@iDistrictID, @sAddress, @sContactName, @iMobile);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_Provider WHERE F_Name = @sName AND F_DistrictID = @iDistrictID AND F_Address = @sAddress AND F_ContactName = @sContactName AND F_Mobile = @iMobile;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'CASE1 Testing Result';

DELETE FROM t_provider WHERE F_ID = last_insert_id();

SELECT '-------------------- Case2: 新建重复的供应商名称，错误码为1 -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '天猫';
SET @iDistrictID = 1;
SET @sAddress = '广州市天河区二十八中学';
SET @sContactName = 'ada1';
SET @iMobile = '13122455441';

CALL SP_Provider_Create(@iErrorCode, @sErrorMsg, @sName ,@iDistrictID, @sAddress, @sContactName, @iMobile);
SELECT @sErrorMsg;
SELECT 1 FROM t_Provider WHERE F_Name = @sName AND F_DistrictID = @iDistrictID AND F_Address = @sAddress AND F_ContactName = @sContactName AND F_Mobile = @iMobile;
SELECT IF(found_rows() = 0 AND @iErrorCode = 1, '测试成功', '测试失败') AS 'CASE2 Testing Result';

DELETE FROM t_provider WHERE F_ID = last_insert_id();

SELECT '-------------------- Case3: 新增重复的供应商电话，错误码为1 -------------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '淘宝';
SET @iDistrictID = 1;
SET @sAddress = '广州市天河区二十八中学';
SET @sContactName = 'ada';
SET @iMobile = '13129355442';

CALL SP_Provider_Create(@iErrorCode, @sErrorMsg, @sName ,@iDistrictID, @sAddress, @sContactName, @iMobile);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_Provider WHERE F_Name = @sName AND F_DistrictID = @iDistrictID AND F_Address = @sAddress AND F_ContactName = @sContactName AND F_Mobile = @iMobile;
SELECT IF(found_rows() = 0 AND @iErrorCode = 1, '测试成功', '测试失败') AS 'CASE3 Testing Result';

DELETE FROM t_provider WHERE F_ID = last_insert_id();

SELECT '-------------------- Case4: 新增供应商时只填写名字，错误码为0 -------------------------' AS 'Case4';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '淘宝1212';
SET @iDistrictID = 2;
SET @sAddress = '';
SET @sContactName = '';
SET @iMobile = '';

CALL SP_Provider_Create(@iErrorCode, @sErrorMsg, @sName ,@iDistrictID, @sAddress, @sContactName, @iMobile);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_Provider WHERE F_Name = @sName AND F_DistrictID IS NULL AND F_Address = @sAddress AND F_ContactName = @sContactName AND F_Mobile IS NULL;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'CASE4 Testing Result';

DELETE FROM t_provider WHERE F_ID = last_insert_id();

SELECT '-------------------- Case5:没有传入电话号码正常创建 -------------------------' AS 'Case5';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '淘宝3';
SET @iDistrictID = 1;
SET @sAddress = '广州市天河区二十八中学11';
SET @sContactName = 'ada11';
SET @iMobile = '';

CALL SP_Provider_Create(@iErrorCode, @sErrorMsg, @sName ,@iDistrictID, @sAddress, @sContactName, @iMobile);
SELECT 1 FROM t_provider WHERE F_Name = @sName AND F_DistrictID = @iDistrictID AND F_ContactName = @sContactName AND F_Address = @sAddress AND F_Mobile = @iMobile;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0 AND @sErrorMsg = '', '测试成功', '测试失败') AS 'CASE5 Testing Result';

DELETE FROM t_provider WHERE F_ID = LAST_INSERT_ID(); 

SELECT '-------------------- Case6:使用不存在的iDistrictID进行创建，错误码为7-------------------------' AS 'Case6';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '哈哈';
SET @iDistrictID = -999;
SET @sAddress = '广州市天河区二十八中学';
SET @sContactName = 'ada';
SET @iMobile = '12312312311';

CALL SP_Provider_Create(@iErrorCode, @sErrorMsg, @sName ,@iDistrictID, @sAddress, @sContactName, @iMobile);
SELECT 1 FROM t_Provider WHERE F_Name = @sName AND F_DistrictID = @iDistrictID AND F_Address = @sAddress AND F_ContactName = @sContactName AND F_Mobile = @iMobile;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '不能新建不存在的供应商区域', '测试成功', '测试失败') AS 'CASE6 Testing Result';