SELECT '++++++++++++++++++Test_SP_Company_Update.sql+++++++++++++++++++++++';
USE nbr_bx;
SELECT '-----------------CASE1: 正常更新------------------' AS 'CASE1';
INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword)
VALUES ('1号分公司', 'FB452352354516', 'url=dftsfd4h5sfl',  '/p/11.jpg', '老板1号', '13146455881', '000000', 'sdf1h4sfh', 'ceshi1', '123456u1', 0, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI');

SET @iID = LAST_INSERT_ID();
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '1号分公司';
SET @sBossName = '大佬一号';
SET @sBusinessLicenseSN = 'UPDATE1';
SET @sBusinessLicensePicture = 'UPDATE1';
SET @sBossPhone = 'UPDATE1';
SET @sBossWechat = 'UPDATE1';
SET @sBrandName = '娃哈哈';
SET @sLogo = '';
-- SET @sKey = 'UPDATE1';

CALL SP_Company_Update(@iErrorCode, @sErrorMsg, @iID, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossWechat, @sKey, @sName, @sBossName, @sBrandName, @sLogo);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM nbr_bx.t_company WHERE F_Name = @sName AND F_BossName = @sBossName AND F_BusinessLicenseSN = @sBusinessLicenseSN 
								AND F_BusinessLicensePicture = @sBusinessLicensePicture AND F_BossPhone = @sBossPhone AND F_BossWechat = @sBossWechat AND F_BrandName = @sBrandName; -- AND F_Key = @sKey;
SELECT IF(FOUND_ROWS() = 1 AND @iErrorCode = 0 AND @sErrorMsg = '', '测试成功', '测试失败') AS 'Case1 Testing Result';

DELETE FROM nbr_bx.t_company WHERE F_ID = @iID;

--	SELECT '-----------------CASE2: 更新所需字段为null 返回错误码为7------------------' AS 'CASE2';-- 已经移动到java层做检查
--	INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword)
--	VALUES ('1号分公司', 'FB452352354516', 'url=dftsfd4h5sfl', '老板1号', '13146455881', '000000', 'sdf1h4sfh', 'ceshi1', '123456u1', 0, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI');
--	
--	SET @iID = LAST_INSERT_ID();
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @sBusinessLicenseSN = '';
--	SET @sName = '2号分公司';
--	SET @sBossName = '大佬一号';
--	SET @sBusinessLicensePicture = 'url=FB232323323';
--	SET @sBossPhone = '13324544444';
--	SET @sBossWechat = 'cjs123456';
--	SET @sKey = '123456u2';
--	SET @sAuthorizerAppid = '';
--	SET @sAuthorizerRefreshToken = '';
--	SET @iFuncInfo = 0;
--	
--	CALL SP_Company_Update(@iErrorCode, @sErrorMsg, @iID, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossWechat, @sKey, @sName, @sBossName,@sAuthorizerAppid,@sAuthorizerRefreshToken,@iFuncInfo);
--	SELECT @sErrorMsg;
--	SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'Case2 Testing Result';
--	
--	DELETE FROM nbr_bx.t_company WHERE F_ID = @iID;

--	SELECT '-----------------CASE3: 参数不符合要求 返回错误码为7 ------------------' AS 'CASE3';-- 已经移动到java层做检查
--	INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword)
--	VALUES ('1号分公司', 'FB452352354516', 'url=dftsfd4h5sfl', '老板1号', '13146455881', '000000', 'sdf1h4sfh', 'ceshi1', '123456u1', 0, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI');
--	
--	SET @iID = LAST_INSERT_ID();
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @sBusinessLicenseSN = '';
--	SET @sBusinessLicensePicture = '';
--	SET @sName = '3号分公司';
--	SET @sBossName = '大佬一号';
--	SET @sBossPhone = '1665416';
--	SET @sBossWechat = 'cjs123456';
--	SET @sKey = '123456u3';
--	SET @sAuthorizerAppid = '';
--	SET @sAuthorizerRefreshToken = '';
--	SET @iFuncInfo = 0;
--	
--	CALL SP_Company_Update(@iErrorCode, @sErrorMsg, @iID, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossWechat, @sKey, @sName, @sBossName,@sAuthorizerAppid,@sAuthorizerRefreshToken,@iFuncInfo);
--	SELECT @sErrorMsg;
--	SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'Case3 Testing Result';
--	
--	DELETE FROM t_company WHERE F_ID = @iID;

--	SELECT '-----------------CASE4:公司名字为空------------------' AS 'CASE4';-- 已经移动到java层做检查
--	INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword)
--	VALUES ('1号分公司', 'FB452352354516', 'url=dftsfd4h5sfl', '老板1号', '13146455881', '000000', 'sdf1h4sfh', 'ceshi1', '123456u1', 0, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI');
--	
--	SET @iID = LAST_INSERT_ID();
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @sBusinessLicenseSN = '';
--	SET @sBusinessLicensePicture = '';
--	SET @sName = '';
--	SET @sBossName = '大佬一号';
--	SET @sBossPhone = '1665416';
--	SET @sBossWechat = 'cjs123456';
--	SET @sKey = '123456u4';
--	SET @sAuthorizerAppid = '';
--	SET @sAuthorizerRefreshToken = '';
--	SET @iFuncInfo = 0;
--	
--	CALL SP_Company_Update(@iErrorCode, @sErrorMsg, @iID, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossWechat, @sKey, @sName, @sBossName,@sAuthorizerAppid,@sAuthorizerRefreshToken,@iFuncInfo);
--	
--	SELECT IF(@sErrorMsg = '参数为空或数据不符合要求' AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case4 Testing Result';
--	
--	DELETE FROM nbr_bx.t_company WHERE F_ID = @iID;

--	SELECT '-----------------CASE5:老板名字为空------------------' AS 'CASE5';-- 已经移动到java层做检查
--	INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword)
--	VALUES ('1号分公司', 'FB452352354516', 'url=dftsfd4h5sfl', '老板1号', '13146455881', '000000', 'sdf1h4sfh', 'ceshi1', '123456u1', 0, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI');
--	
--	SET @iID = LAST_INSERT_ID();
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @sBusinessLicenseSN = '';
--	SET @sBusinessLicensePicture = '';
--	SET @sName = '5号分公司';
--	SET @sBossName = '';
--	SET @sBossPhone = '1665416';
--	SET @sBossWechat = 'cjs123456';
--	SET @sKey = '123456u5';
--	SET @sAuthorizerAppid = '';
--	SET @sAuthorizerRefreshToken = '';
--	SET @iFuncInfo = 0;
--	
--	CALL SP_Company_Update(@iErrorCode, @sErrorMsg, @iID, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossWechat, @sKey, @sName, @sBossName,@sAuthorizerAppid,@sAuthorizerRefreshToken,@iFuncInfo);
--	
--	SELECT IF(@sErrorMsg = '参数为空或数据不符合要求' AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case5 Testing Result';
--	
--	DELETE FROM nbr_bx.t_company WHERE F_ID = @iID;

--	SELECT '-----------------CASE6:公司名称和老板名字都为空------------------' AS 'CASE6';-- 已经移动到java层做检查
--	INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword)
--	VALUES ('1号分公司', 'FB452352354516', 'url=dftsfd4h5sfl', '老板1号', '13146455881', '000000', 'sdf1h4sfh', 'ceshi1', '123456u1', 0, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI');
--	
--	SET @iID = LAST_INSERT_ID();
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @sBusinessLicenseSN = '';
--	SET @sBusinessLicensePicture = '';
--	SET @sName = '6号分公司';
--	SET @sBossName = '大佬一号';
--	SET @sBossPhone = '1665416';
--	SET @sBossWechat = 'cjs123456';
--	SET @sKey = '123456u6';
--	SET @sAuthorizerAppid = '';
--	SET @sAuthorizerRefreshToken = '';
--	SET @iFuncInfo = 0;
--	
--	CALL SP_Company_Update(@iErrorCode, @sErrorMsg, @iID, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossWechat, @sKey, @sName, @sBossName,@sAuthorizerAppid,@sAuthorizerRefreshToken,@iFuncInfo);
--	
--	SELECT IF(@sErrorMsg = '参数为空或数据不符合要求' AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case6 Testing Result';
--	
--	DELETE FROM t_company WHERE F_ID = @iID;

SELECT '-----------------CASE7: 更新时，已存在公司名称------------------' AS 'CASE7';
INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword)
VALUES ('7号分公司', 'FB452352354516', 'url=dftsfd4h5sfl', '/p/11.jpg', '老板1号', '13146455881', '000000', 'sdf1h4sfh', 'ceshi1', '123456u1', 0, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI');
SET @iID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = 'BX一号分公司';
SET @sBossName = '大佬一号';
SET @sBusinessLicenseSN = 'UPDATE1';
SET @sBusinessLicensePicture = 'UPDATE1';
SET @sBossPhone = 'UPDATE1';
SET @sBossWechat = 'UPDATE1';
SET @sKey = 'UPDATE1';
SET @sBrandName = '娃哈哈';
SET @sLogo = 'fff';

CALL SP_Company_Update(@iErrorCode, @sErrorMsg, @iID, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossWechat, @sKey, @sName, @sBossName, @sBrandName, @sLogo);
SELECT IF(@sErrorMsg = '公司名称重复' AND @iErrorCode = 1, '测试成功', '测试失败') AS 'Case7 Testing Result';

DELETE FROM t_company WHERE F_ID = @iID;

SELECT '-----------------CASE8: 更新时，其它公司已存在非空的营业执照------------------' AS 'CASE8';
INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword)
VALUES ('8号分公司', 'FB452352354516', NULL, '/p/11.jpg', '老板1号', '13146455881', '000000', 'sdf1h4sfh', 'ceshi1', '123456u1', 0, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI');
SET @iID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '8-1号分公司';
SET @sBossName = '大佬一号';
SET @sBusinessLicenseSN = '111111111111111';
SET @sBusinessLicensePicture = 'UPDATE1';
SET @sBossPhone = 'UPDATE1';
SET @sBossWechat = 'UPDATE1';
SET @sKey = 'UPDATE1';
SET @sBrandName = '娃哈哈';
SET @sLogo = 'fafsf';

CALL SP_Company_Update(@iErrorCode, @sErrorMsg, @iID, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossWechat, @sKey, @sName, @sBossName, @sBrandName, @sLogo);
SELECT IF(@sErrorMsg = '其它公司已经存在相同的营业执照' AND @iErrorCode = 1, '测试成功', '测试失败') AS 'Case8 Testing Result';
DELETE FROM t_company WHERE F_ID = @iID;

SELECT '-----------------CASE9: 更新时，其它公司已存在非空的营业执照照片------------------' AS 'CASE9';
SET @sBusinessLicensePicture = '/common_db/license/1.png';
INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword)
VALUES ('9号分公司A', 'FB4523523FS54516', @sBusinessLicensePicture, '/p/11.jpg', '老板1F号', '13146F455881', '000000', 'sdf1h4Fsfh', 'ceshi1', '123456u1', 0, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI');
SET @iIDA = LAST_INSERT_ID();
-- 
INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword)
VALUES ('9号分公司B', 'FB4523F52F3S54516', '111.jpg', '/p/11F.jpg', '老板1F号F', '131464F55881', '000000', 'sdf1hF4sfh', 'cesFhi1', '1234F56u1', 0, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI');
SET @iIDB = LAST_INSERT_ID();

-- 
SET @iErrorCode = 0;
SET @sErrorMsg= '';
SET @sName = '9-1号分公司S';
SET @sBossName = '大佬一号S';
SET @sBusinessLicenseSN = 'TB2318232S3483';
SET @sBossPhone = 'UPDATSE1';
SET @sBossWechat = 'UPDATSE1';
SET @sKey = 'UPDSATE1';
SET @sBrandName = '娃哈S哈';
SET @sLogo = 'sfadSssf';
-- 
CALL SP_Company_Update(@iErrorCode, @sErrorMsg, @iIDB, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossWechat, @sKey, @sName, @sBossName, @sBrandName, @sLogo);
-- 
SELECT IF(@sErrorMsg = '该公司营业执照图片已存在，请联系管理员' AND @iErrorCode = 1, '测试成功', '测试失败') AS 'Case9 Testing Result';
DELETE FROM t_company WHERE F_ID = @iIDA;
DELETE FROM t_company WHERE F_ID = @iIDB;
-- 

SELECT '-----------------CASE10: 更新时，已存在公司钥匙key------------------' AS 'CASE10';
INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword)
VALUES ('10号分公司', 'FB452352354516', 'url=dftsfd4h5sfl', '/p/11.jpg', '老板1号', '13146455881', '000000', 'sdf1h4sfh', 'ceshi1', '12345610', 0, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI');

SET @iID1 = LAST_INSERT_ID();

INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword)
VALUES ('11号分公司', 'FB452352354517', 'url=dftsfd4h5s99', '/p/1122.jpg', '老板2号', '13146455899', '000000', 'sdf1h4999', 'ceshi2', '66666666', 0, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI');

SET @iID2 = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '11号分公司';
SET @sBossName = '大佬二号';
SET @sBusinessLicenseSN = 'FB452352354516case10';
SET @sBusinessLicensePicture = 'url=dftsfd4h5sflcase10';
SET @sBossPhone = '13146455881';
SET @sBossWechat = 'sdf1h4sfh';
SET @sKey = '12345610';
SET @sBrandName = '娃哈哈';
SET @sLogo = 'sfasq';
-- 
CALL SP_Company_Update(@iErrorCode, @sErrorMsg, @iID2, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossWechat, @sKey, @sName, @sBossName, @sBrandName, @sLogo);
SELECT IF(@sErrorMsg = '其它公司已存在这个key' AND @iErrorCode = 1, '测试成功', '测试失败') AS 'Case10 Testing Result';
-- 
DELETE FROM t_company WHERE F_ID = @iID1;
DELETE FROM t_company WHERE F_ID = @iID2;



SELECT '-----------------CASE12: 公司公众号取消授权 ------------------' AS 'CASE12';
INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword)
VALUES ('12号分公司', 'FB452352354516', 'url=dftsfd4h5sFfl', '/p/11F.jpg', '老板1号', '13146455881', '000000', 'sdf1h4sfh', 'ceshi1', '12345610', 0, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI');
SET @iID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '10-12号分公司';
SET @sBossName = '大佬一号';
SET @sBusinessLicenseSN = 'TB231823F23483';
SET @sBusinessLicensePicture = 'url=95454F534354';
SET @sBossPhone = 'UPDATE1';
SET @sBossWechat = 'UPDATE1';
SET @sKey = 'AS28ss248';
SET @sBrandName = '娃哈哈';
SET @sLogo = 'fadsq';
-- 
CALL SP_Company_Update(@iErrorCode, @sErrorMsg, @iID, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossWechat, @sKey, @sName, @sBossName, @sBrandName, @sLogo);
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case12 Testing Result';
DELETE FROM t_company WHERE F_ID = @iID;

SELECT '-----------------CASE13: 两家公司的营业执照和logo都修改为''  ------------------' AS 'CASE13';
INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword)
VALUES ('13号分公司', 'FB452352354516', 'url=dftsfd4h5sfl', '/p/11.jpg', '老板1号', '13146455881', '000000', 'sdf1h4sfh', 'ceshi1', '12345610', 0, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI');
SET @iIDA = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = 'FDAFAD';
SET @sBossName = '大佬FF一号';
SET @sBusinessLicenseSN = 'TB231F82323483';
SET @sBusinessLicensePicture = '';
SET @sBossPhone = 'UPDFATE1';
SET @sBossWechat = 'UPDATFE1';
SET @sKey = 'AS28ss2F48';
SET @sBrandName = '娃哈哈F';
SET @sLogo = '';
-- 
CALL SP_Company_Update(@iErrorCode, @sErrorMsg, @iIDA, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossWechat, @sKey, @sName, @sBossName, @sBrandName, @sLogo);
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case13 Testing Result';

-- 
INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword)
VALUES ('13号分公司2', 'FB4522352354516', 'url=dftsfd4h35sfl', '/p/11.3jpg', '老板21号', '131462455881', '000000', 'sdf1h24sfh', 'cesh2i1', '122345610', 0, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI');
SET @iIDB = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = 'FDAFAfD';
SET @sBossName = '大佬FfF一号';
SET @sBusinessLicenseSN = 'TB231F8f2323483';
SET @sBusinessLicensePicture = '';
SET @sBossPhone = 'UPDFfATE1';
SET @sBossWechat = 'UPDAfTFE1';
SET @sKey = 'AS28ss2fF48';
SET @sBrandName = '娃f哈哈F';
SET @sLogo = '';
-- 
CALL SP_Company_Update(@iErrorCode, @sErrorMsg, @iIDB, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossWechat, @sKey, @sName, @sBossName, @sBrandName, @sLogo);
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case13 Testing Result';

DELETE FROM t_company WHERE F_ID = @iIDA;
DELETE FROM t_company WHERE F_ID = @iIDB;

SELECT '-----------------CASE14: 修改公司的Logo为已经存在的  ------------------' AS 'CASE14';
SET @sLogo = '12346ae';
INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword)
VALUES ('14号分公司', 'FB4522f352354516', 'url=dftsffd4hf35sfl', @sLogo, '老板21号', '131462455881', '000000', 'sdf1h24sfh', 'cesh2i1', '122345610', 0, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI');
SET @iIDA = LAST_INSERT_ID();
-- 
INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword)
VALUES ('14号分公司2', 'FB324522f352354516', 'url=d3ftsffd4hf35sfl', '4565WE', '老板21E号', '1314624R55881', '000000', 'sdf1Rh24sfh', 'ceshR2i1', '1223R45610', 0, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI');
SET @iIDB = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = 'FDAFAfD';
SET @sBossName = '大佬FfF一号';
SET @sBusinessLicenseSN = 'TB231F8f2323483';
SET @sBusinessLicensePicture = '';
SET @sBossPhone = 'UPDFfATE1';
SET @sBossWechat = 'UPDAfTFE1';
SET @sKey = 'AS28ss2fF48';
SET @sBrandName = '娃f哈哈F';
-- 
CALL SP_Company_Update(@iErrorCode, @sErrorMsg, @iIDB, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossWechat, @sKey, @sName, @sBossName, @sBrandName, @sLogo);
SELECT IF(@sErrorMsg = '该公司Logo图片已存在，请联系管理员' AND @iErrorCode = 1, '测试成功', '测试失败') AS 'Case14 Testing Result';

DELETE FROM t_company WHERE F_ID = @iIDA;
DELETE FROM t_company WHERE F_ID = @iIDB;