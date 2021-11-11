SELECT '++++++++++++++++++ Test_SP_Provider_Update.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: 修改成重复的供应商名称，错误码为1 -------------------------' AS 'Case1';

INSERT INTO T_Provider(F_Name,F_DistrictID,F_Address,F_ContactName,F_Mobile)
VALUES('华丽供应商',1,'广州市天河区二十八中学','kim','1111112s');

SET @iID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '天猫';		          
SET @sDistrictID = 2;     
SET @sAddress = '广州市天河区';   
SET @sContactName = 'kkk';     
SET @sMobile='1241652s46565';

call SP_Provider_Update(
	@iErrorCode,
	@sErrorMsg,
	@iID,
	@sName,
	@sDistrictID,
	@sAddress,
	@sContactName,
	@sMobile
);

SELECT @sErrorMsg;
SELECT 1 FROM t_Provider WHERE F_Name = @sName;
SELECT IF(found_rows() = 1 AND @iErrorCode = 1, '测试成功', '测试失败') AS 'Case1 Testing Result';

DELETE FROM T_Provider WHERE F_ID = @iID;

SELECT '-------------------- Case2: 修改成重复的供应商电话号码，错误码为1 -------------------------' AS 'Case2';

INSERT INTO T_Provider(F_Name,F_DistrictID,F_Address,F_ContactName,F_Mobile)
VALUES('华丽供应商',1,'广州市天河区二十八中学','kim','1111112s');

SET @iID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '华夏供应商121';		          
SET @sDistrictID = 2;     
SET @sAddress = '广州市天河区';   
SET @sContactName = 'kkk';     
SET @sMobile='13129355441';

call SP_Provider_Update(
	@iErrorCode,
	@sErrorMsg,
	@iID,
	@sName,
	@sDistrictID,
	@sAddress,
	@sContactName,
	@sMobile
);

SELECT @sErrorMsg;
SELECT 1 FROM t_Provider WHERE F_ID = @iID AND F_Name = @sName AND F_DistrictID = @sDistrictID AND F_Address = @sAddress AND F_ContactName = @sContactName AND F_Mobile = @sMobile;
SELECT @iErrorCode;
SELECT IF(found_rows() = 1 AND @iErrorCode = 1, '测试成功', '测试失败') AS 'Case2 Testing Result';

DELETE FROM T_Provider WHERE F_ID = @iID;


SELECT '-------------------- Case3: 修改成不重复的供应商名称，错误码为0 -------------------------' AS 'Case3';

INSERT INTO T_Provider(F_Name,F_DistrictID,F_Address,F_ContactName,F_Mobile)
VALUES('华丽供应商',1,'广州市天河区二十八中学','kim','1111112s');

SET @iID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '华夏供应商121';		          
SET @sDistrictID = 2;     
SET @sAddress = '广州市天河区';   
SET @sContactName = 'kkk';     
SET @sMobile='1241652s465652';

call SP_Provider_Update(
	@iErrorCode,
	@sErrorMsg,
	@iID,
	@sName,
	@sDistrictID,
	@sAddress,
	@sContactName,
	@sMobile
);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case3 Testing Result';

DELETE FROM T_Provider WHERE F_ID = @iID;

SELECT '-------------------- Case4: 供应商ID为0的时候进行修改，错误码为0 -------------------------' AS 'Case4';

INSERT INTO T_Provider(F_Name,F_DistrictID,F_Address,F_ContactName,F_Mobile)
VALUES('华丽供应商99',1,'广州市天河区二十八中学','kim','1111112s');

SET @iID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '华夏供应商111';		          
SET @sDistrictID = 1;     
SET @sAddress = '广州市天河区';   
SET @sContactName = 'kkk';     
SET @sMobile='1241652s46565';

call SP_Provider_Update(
	@iErrorCode,
	@sErrorMsg,
	@iID,
	@sName,
	@sDistrictID,
	@sAddress,
	@sContactName,
	@sMobile
);

SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case4 Testing Result';

DELETE FROM T_Provider WHERE F_ID = @iID;

SELECT '-------------------- Case5: 供应商电话号码为NULL的时候进行修改，错误码为0 -------------------------' AS 'Case5';
INSERT INTO T_Provider(F_Name,F_DistrictID,F_Address,F_ContactName,F_Mobile)
VALUES('华丽供应商1',1,'广州市天河区二十八中学','kim','1111112s');

SET @iID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '华夏供应商122';		          
SET @sDistrictID = 1;     
SET @sAddress = '';   
SET @sContactName = '';     
SET @sMobile = '';

call SP_Provider_Update(
	@iErrorCode,
	@sErrorMsg,
	@iID,
	@sName,
	@sDistrictID,
	@sAddress,
	@sContactName,
	@sMobile
);

SELECT @sErrorMsg;
SELECT 1 FROM t_Provider WHERE F_ID = @iID AND F_Name = @sName AND F_DistrictID = @sDistrictID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case5 Testing Result';

DELETE FROM T_Provider WHERE F_ID = @iID;

SELECT '-------------------- Case6:只传入电话号码和供应商名称进行修改，错误码为0 -------------------------' AS 'Case6';
INSERT INTO T_Provider(F_Name,F_DistrictID,F_Address,F_ContactName,F_Mobile)
VALUES('华丽供应商1',1,'广州市天河区二十八中学','kim','1111112s');

SET @iID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '华夏供应商123';		          
SET @sDistrictID = 1;     
SET @sAddress = '';   
SET @sContactName = '';     
SET @sMobile = '1111111111';

call SP_Provider_Update(
	@iErrorCode,
	@sErrorMsg,
	@iID,
	@sName,
	@sDistrictID,
	@sAddress,
	@sContactName,
	@sMobile
);

SELECT @sErrorMsg;
SELECT 1 FROM t_Provider WHERE F_ID = @iID AND F_Name = @sName AND F_DistrictID = @sDistrictID AND F_Mobile = @sMobile;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case5 Testing Result';

DELETE FROM T_Provider WHERE F_ID = @iID;