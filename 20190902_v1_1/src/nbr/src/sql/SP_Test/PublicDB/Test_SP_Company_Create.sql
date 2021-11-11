SELECT '++++++++++++++++++Test_SP_Company_Create.sql+++++++++++++++++++++++';

SELECT '-----------------CASE1: 正常添加------------------' AS 'CASE1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sBusinessLicenseSN = 'TE232323323';
SET @sBusinessLicensePicture = 'url=FB232323323';
SET @sLogo = '1234666';
SET @sBossPhone = '13324544444';
SET @sBossPassword = '000000';
SET @sBossWechat = 'cjs123456';
SET @sDBName = 'nbr3';
SET @sKey = '923456';
SET @sName = '1号公司';
SET @sBossName = '大老板';
-- -- SET @iBxStaffID = 0;
SET @sDBUserName = 'root';
SET @sDBUserPassword = 'WEF#EGEHEH$$^*DI';
SET @sSubmchid = 'FT232323323';
SET @sBrandName = '娃哈哈';

CALL SP_Company_Create(@iErrorCode, @sErrorMsg, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossPassword, @sBossWechat, @sDBName, @sKey, @sName, @sBossName, @sDBUserName, @sDBUserPassword, @sSubmchid, @sBrandName, @sLogo);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

DELETE FROM nbr_bx.t_company WHERE F_DBName = @sDBName;

SELECT '-----------------CASE2: 重复添加 返回错误码为1------------------' AS 'CASE2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sBusinessLicenseSN = 'FB232323323';
SET @sBusinessLicensePicture = 'url=FB232323323';
SET @sLogo = '1234666';
SET @sBossPhone = '13324544444';
SET @sBossPassword = '000000';
SET @sBossWechat = 'cjs123456';
SET @sDBName = 'nbr';
SET @sKey = '123456';
SET @sName = '2号公司';
SET @sBossName = '2号大老板';
-- -- SET @iBxStaffID = 0;
SET @sDBUserName = 'root';
SET @sDBUserPassword = 'WEF#EGEHEH$$^*DI';
SET @sSubmchid = NULL;
SET @sBrandName = '娃哈哈';

CALL SP_Company_Create (@iErrorCode, @sErrorMsg, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossPassword, @sBossWechat, @sDBName, @sKey, @sName, @sBossName, @sDBUserName, @sDBUserPassword, @sSubmchid, @sBrandName, @sLogo);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 1, '测试成功', '测试失败') AS 'Case2 Testing Result';


--	SELECT '-----------------CASE3: 参数为空 返回错误码为1 ------------------' AS 'CASE3';
--	
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @sBusinessLicenseSN = 'FB232323323';
--	SET @sBusinessLicensePicture = 'url=FB232323323';
--	SET @sBossPhone = '';
--	SET @sBossPassword = '000000';
--	SET @sBossWechat = 'cjs123456';
--	SET @sDBName = 'bxnbr11';
--	SET @sKey = '';
--	SET @sName = '3号公司';
--	SET @sBossName = '3号大老板';
--	-- --SET @iBxStaffID = 0;
--	SET @sDBUserName = 'root';
--	SET @sDBUserPassword = 'WEF#EGEHEH$$^*DI';
--	
--	CALL SP_Company_Create (@iErrorCode, @sErrorMsg, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossPassword, @sBossWechat, @sDBName, @sKey, @sName, @sBossName,@sDBUserName, @sDBUserPassword);
--	SELECT @iErrorCode;
--	SELECT @sErrorMsg;
--	SELECT IF(@iErrorCode = 1, '测试成功', '测试失败') AS 'Case3 Testing Result';

--	SELECT '-----------------CASE4:公司名字为空------------------' AS 'CASE4'; -- 已经移动到java层做检查
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @sBusinessLicenseSN = 'FB232323323';
--	SET @sBusinessLicensePicture = 'url=FB232323323';
--	SET @sBossPhone = '13324544444';
--	SET @sBossPassword = '000000';
--	SET @sBossWechat = 'cjs123456';
--	SET @sDBName = 'bxnbr11l2';
--	SET @sKey = '123456';
--	SET @sName = '';
--	SET @sBossName = '大老板';
--	-- -- SET @iBxStaffID = 0;
--	SET @sDBUserName = 'root';
--	SET @sDBUserPassword = 'WEF#EGEHEH$$^*DI';
--	
--	CALL SP_Company_Create (@iErrorCode, @sErrorMsg, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossPassword, @sBossWechat, @sDBName, @sKey, @sName, @sBossName, @sDBUserName, @sDBUserPassword);
--	
--	SELECT IF(@sErrorMsg = '参数为空或数据不符合要求' AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case4 Testing Result';

--	SELECT '-----------------CASE5:老板名字为空------------------' AS 'CASE5';-- 已经移动到java层做检查
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @sBusinessLicenseSN = 'FB232323323';
--	SET @sBusinessLicensePicture = 'url=FB232323323';
--	SET @sBossPhone = '13324544444';
--	SET @sBossPassword = '000000';
--	SET @sBossWechat = 'cjs123456';
--	SET @sDBName = 'bxnbr11l2';
--	SET @sKey = '123456';
--	SET @sName = '4号公司' ;
--	SET @sBossName = '';
--	--	-- SET @iBxStaffID = 0;
--	SET @sDBUserName = 'root';
--	SET @sDBUserPassword = 'WEF#EGEHEH$$^*DI';
--	
--	CALL SP_Company_Create (@iErrorCode, @sErrorMsg, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossPassword, @sBossWechat, @sDBName, @sKey, @sName, @sBossName, @sDBUserName, @sDBUserPassword);
--	
--	SELECT IF(@sErrorMsg = '参数为空或数据不符合要求' AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case5 Testing Result';

--	SELECT '-----------------CASE6:公司名称和老板名字都为空------------------' AS 'CASE6';-- 已经移动到java层做检查
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @sBusinessLicenseSN = 'FB232323323';
--	SET @sBusinessLicensePicture = 'url=FB232323323';
--	SET @sBossPhone = '13324544444';
--	SET @sBossPassword = '000000';
--	SET @sBossWechat = 'cjs123456';
--	SET @sDBName = 'bxnbr11l2';
--	SET @sKey = '123456';
--	SET @sName = '' ;
--	SET @sBossName = '';
--	-- SET @iBxStaffID = 0;
--	SET @sDBUserName = 'root';
--	SET @sDBUserPassword = 'WEF#EGEHEH$$^*DI';
--	
--	CALL SP_Company_Create (@iErrorCode, @sErrorMsg, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossPassword, @sBossWechat, @sDBName, @sKey, @sName, @sBossName, @sDBUserName, @sDBUserPassword);
--	
--	SELECT IF(@sErrorMsg = '参数为空或数据不符合要求' AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case6 Testing Result';

--	SELECT '-----------------CASE7:老板登录密码为空------------------' AS 'CASE7';-- 已经移动到java层做检查
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @sBusinessLicenseSN = 'FB232323323';
--	SET @sBusinessLicensePicture = 'url=FB232323323';
--	SET @sBossPhone = '13324544444';
--	SET @sBossPassword = '';
--	SET @sBossWechat = 'cjs123456';
--	SET @sDBName = 'bxnbr11l27';
--	SET @sKey = '123457';
--	SET @sName = '7号公司' ;
--	SET @sBossName = '';
--	-- SET @iBxStaffID = 0;
--	SET @sDBUserName = 'root';
--	SET @sDBUserPassword = 'WEF#EGEHEH$$^*DI';
--	
--	CALL SP_Company_Create (@iErrorCode, @sErrorMsg, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossPassword, @sBossWechat, @sDBName, @sKey, @sName, @sBossName, @sDBUserName, @sDBUserPassword);
--	SELECT @iErrorCode;
--	SELECT @sErrorMsg;
--	SELECT IF(@sErrorMsg = '参数为空或数据不符合要求' AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case7 Testing Result';


SELECT '-----------------CASE8: 重复添加营业执照， 返回错误码为1------------------' AS 'CASE8';
INSERT INTO nbr_bx.t_company (F_Name, F_SN, F_BusinessLicenseSN, F_BusinessLicensePicture, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_DBUserName, F_DBUserPassword, F_Status, F_ExpireDatetime)
VALUES ('BX三号分公司', '668866', '77777777777777', '/p/123.jpg', '老板1号', '13123615881', '000000', 'a13123615881', 'nbr11', '12345678901234567890123456789099', 'nbr11', 'WEF#EGEHEH$$^*DI', 0, '2030-12-02 01:01:01');
SET @iID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sBusinessLicenseSN = '111111111111111';
SET @sBusinessLicensePicture = '/p/123.jpg';
SET @sBossPhone = '13324544444';
SET @sBossPassword = '000000';
SET @sBossWechat = 'cjs123456';
SET @sLogo = '1234666';
SET @sDBName = 'nbr3';
SET @sKey = '923456';
SET @sName = '2号公司';
SET @sBossName = '2号大老板';
-- SET @iBxStaffID = 0;
SET @sDBUserName = 'root';
SET @sDBUserPassword = 'WEF#EGEHEH$$^*DI';
SET @sSubmchid = NULL;
SET @sBrandName = '娃哈哈';

CALL SP_Company_Create (@iErrorCode, @sErrorMsg, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossPassword, @sBossWechat, @sDBName, @sKey, @sName, @sBossName, @sDBUserName, @sDBUserPassword, @sSubmchid, @sBrandName, @sLogo);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 1, '测试成功', '测试失败') AS 'Case8 Testing Result';

DELETE FROM  nbr_bx.t_company WHERE F_ID = @iID;

SELECT '-----------------CASE9: 重复添加营业执照照片， 返回错误码为1------------------' AS 'CASE9';
UPDATE nbr_bx.t_company SET F_BusinessLicensePicture = '/private_db/nbr/1.jpg' WHERE F_ID = 1;

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sBusinessLicenseSN = 'FT232323323';
SET @sBusinessLicensePicture = '/private_db/nbr/1.jpg' ;
SET @sBossPhone = '13324544444';
SET @sBossPassword = '000000';
SET @sBossWechat = 'cjs123456';
SET @sLogo = '1234666';
SET @sDBName = 'nbr3';
SET @sKey = '923456';
SET @sName = '2号公司';
SET @sBossName = '2号大老板';
-- SET @iBxStaffID = 0;
SET @sDBUserName = 'root';
SET @sDBUserPassword = 'WEF#EGEHEH$$^*DI';
SET @sSubmchid = NULL;
SET @sBrandName = '娃哈哈';

CALL SP_Company_Create (@iErrorCode, @sErrorMsg, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossPassword, @sBossWechat, @sDBName, @sKey, @sName, @sBossName, @sDBUserName, @sDBUserPassword, @sSubmchid, @sBrandName, @sLogo);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 1, '测试成功', '测试失败') AS 'Case9 Testing Result';

UPDATE nbr_bx.t_company SET F_BusinessLicensePicture = NULL WHERE F_ID = 1;

SELECT '-----------------CASE10: 重复添加公司名称， 返回错误码为1------------------' AS 'CASE10';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sBusinessLicenseSN = 'FT232323323';
SET @sBusinessLicensePicture = 'url=ddgdfhdfha';
SET @sBossPhone = '13324544444';
SET @sBossPassword = '000000';
SET @sLogo = '1234666';
SET @sBossWechat = 'cjs123456';
SET @sDBName = 'nbr3';
SET @sKey = '923456';
SET @sName = 'BX一号分公司';
SET @sBossName = '2号大老板';
-- SET @iBxStaffID = 0;
SET @sDBUserName = 'root';
SET @sDBUserPassword = 'WEF#EGEHEH$$^*DI';
SET @sSubmchid = NULL;
SET @sBrandName = '娃哈哈';

CALL SP_Company_Create (@iErrorCode, @sErrorMsg, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossPassword, @sBossWechat, @sDBName, @sKey, @sName, @sBossName, @sDBUserName, @sDBUserPassword, @sSubmchid, @sBrandName, @sLogo);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 1, '测试成功', '测试失败') AS 'Case10 Testing Result';


SELECT '-----------------CASE11: 重复添加公司钥匙， 返回错误码为1------------------' AS 'CASE11';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sBusinessLicenseSN = 'FT232323323';
SET @sBusinessLicensePicture = 'url=ddgdfhdfha';
SET @sLogo = '1234666';
SET @sBossPhone = '13324544444';
SET @sBossPassword = '000000';
SET @sBossWechat = 'cjs123456';
SET @sDBName = 'nbr3';
SET @sKey = '12345678901234567890123456789012';
SET @sName = 'TX一号分公司';
SET @sBossName = '2号大老板';
-- SET @iBxStaffID = 0;
SET @sDBUserName = 'root';
SET @sDBUserPassword = 'WEF#EGEHEH$$^*DI';
SET @sSubmchid = NULL;
SET @sBrandName = '娃哈哈';

CALL SP_Company_Create (@iErrorCode, @sErrorMsg, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossPassword, @sBossWechat, @sDBName, @sKey, @sName, @sBossName, @sDBUserName, @sDBUserPassword, @sSubmchid, @sBrandName, @sLogo);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 1, '测试成功', '测试失败') AS 'Case11 Testing Result';

SELECT '-----------------CASE12: 重复添加子商户号， 返回错误码为1------------------' AS 'CASE12';
INSERT INTO nbr_bx.t_company (F_Name, F_SN, F_BusinessLicenseSN, F_BusinessLicensePicture, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_DBUserName, F_DBUserPassword, F_Status,F_Submchid, F_ExpireDatetime)
VALUES ('BX三号分公司', '668869', '77777777777777', '/p/123.jpg', '老板1号', '13123615881', '000000', 'a13123615881', 'nbr11', '12345678901234567890123456789099', 'nbr11', 'WEF#EGEHEH$$^*DI', 0, 'F7235D195E', '2030-12-02 01:01:01');
SET @iID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sBusinessLicenseSN = 'TE2323233232';
SET @sBusinessLicensePicture = 'url=FB2323233232';
SET @sLogo = '1234666';
SET @sBossPhone = '13324544442';
SET @sBossPassword = '000000';
SET @sBossWechat = 'cjs123456';
SET @sDBName = 'nbr3';
SET @sKey = '923456';
SET @sName = 'c号公司';
SET @sBossName = 'cjs';
-- -- SET @iBxStaffID = 0;
SET @sDBUserName = 'root';
SET @sDBUserPassword = 'WEF#EGEHEH$$^*DI';
SET @sSubmchid = 'F7235D195E';
SET @sBrandName = '娃哈哈';

CALL SP_Company_Create(@iErrorCode, @sErrorMsg, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossPassword, @sBossWechat, @sDBName, @sKey, @sName, @sBossName, @sDBUserName, @sDBUserPassword, @sSubmchid, @sBrandName, @sLogo);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 1 AND @sErrorMsg = '其它公司已存在这个子商户号', '测试成功', '测试失败') AS 'Case12 Testing Result';

DELETE FROM nbr_bx.t_company WHERE F_ID = @iID;


SELECT '-----------------CASE13: 创建一个子商户号为空串的公司，再新建一个字商户号为空串的公司。结果为俩个公司创建成功------------------' AS 'CASE13';
INSERT INTO nbr_bx.t_company (F_Name, F_SN, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_DBUserName, F_DBUserPassword, F_Status,F_Submchid, F_ExpireDatetime)
VALUES ('BX三号分公司', '668869', '77777777777777', '/p/123.jpg', '/p/11.jpg', '老板1号', '13123615881', '000000', 'a13123615881', 'nbr11', '12345678901234567890123456789099', 'nbr11', 'WEF#EGEHEH$$^*DI', 0, '', '2030-12-02 01:01:01');
SET @iID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sBusinessLicenseSN = 'TE2323233232';
SET @sBusinessLicensePicture = 'url=FB2323233232';
SET @sBossPhone = '13324544442';
SET @sLogo = '1234666';
SET @sBossPassword = '000000';
SET @sBossWechat = 'cjs123456';
SET @sDBName = 'nbr3';
SET @sKey = '923456';
SET @sName = 'c号公司';
SET @sBossName = 'cjs';
-- -- SET @iBxStaffID = 0;
SET @sDBUserName = 'root';
SET @sDBUserPassword = 'WEF#EGEHEH$$^*DI';
SET @sSubmchid = '';
SET @sBrandName = '娃哈哈';

CALL SP_Company_Create(@iErrorCode, @sErrorMsg, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossPassword, @sBossWechat, @sDBName, @sKey, @sName, @sBossName, @sDBUserName, @sDBUserPassword, @sSubmchid, @sBrandName, @sLogo);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case13 Testing Result';

DELETE FROM nbr_bx.t_company WHERE F_ID = @iID;
DELETE FROM nbr_bx.t_company WHERE F_DBName = @sDBName;




SELECT '-----------------CASE14: 不同的公司可以有相同的brandName ------------------' AS 'CASE14';
-- 
INSERT INTO nbr_bx.t_company (F_Name, F_SN, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_DBUserName, F_DBUserPassword, F_Status,F_Submchid, F_BrandName, F_ExpireDatetime)
VALUES ('BX三号分公司', '668869', '77777777777777', '/p/123.jpg', '/p/11.jpg', '老板1号', '13123615881', '000000', 'a13123615881', 'nbr11', '12345678901234567890123456789099', 'nbr11', 'WEF#EGEHEH$$^*DI', 0, 'F7235D195E', '娃哈哈', '2030-12-02 01:01:01');
SET @iID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sLogo = '1234666';
SET @sBusinessLicenseSN = 'TE2323233232';
SET @sBusinessLicensePicture = 'url=FB2323233232';
SET @sBossPhone = '13324544442';
SET @sBossPassword = '000000';
SET @sBossWechat = 'cjs123456';
SET @sDBName = 'nbr3';
SET @sKey = '923456';
SET @sName = 'c号公司';
SET @sBossName = 'wahaha';
-- -- SET @iBxStaffID = 0;
SET @sDBUserName = 'root';
SET @sDBUserPassword = 'WEF#EGEHEH$$^*DI';
SET @sSubmchid = 'Q7235D195E';
SET @sBrandName = '娃哈哈';
-- 
CALL SP_Company_Create(@iErrorCode, @sErrorMsg, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossPassword, @sBossWechat, @sDBName, @sKey, @sName, @sBossName, @sDBUserName, @sDBUserPassword, @sSubmchid, @sBrandName, @sLogo);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case14 Testing Result';
-- 
DELETE FROM nbr_bx.t_company WHERE F_ID = @iID;
DELETE FROM nbr_bx.t_company WHERE F_DBName = @sDBName;


SELECT '-----------------CASE15: 插入2个公司，营业执照图片和logo都是空串，结果全部成功 ------------------' AS 'CASE15';
-- 先插入一个公司，获取当前最大F_ID
INSERT INTO nbr_bx.t_company (F_Name, F_SN, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_DBUserName, F_DBUserPassword, F_Status,F_Submchid, F_BrandName, F_ExpireDatetime)
VALUES ('BX三号分公司RR', '668869', '77717771368777', '/p/nbxxxr_s11.jpg', '/p/1241.jpg', '老板1号', '13123615881', '000000', 'a13123615881', 'nbxxxr_s11', '123456789014567890123456789099', 'nbxxxr_s11', 'WEF#EGEHEH$$^*DI', 0, 'F7235D1915E', '娃哈哈', '2030-12-02 01:01:01');
SET @iID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sBusinessLicenseSN = concat('TE3545423', ceiling(rand() * 43000));

SET @sBusinessLicensePicture = '';
SET @sBossPhone = '13324544442';
SET @sLogo = '';
SET @sBossPassword = '000000';
SET @sBossWechat = 'cjs123456';
SET @sDBName = concat('nbr_2', ceiling(rand() * 105750));
SET @sKey = concat('9232456', ceiling(rand() * 15600));
SET @sName = concat('博昕科技', ceiling(rand() * 176000));
SET @sBossName = 'wahaha';
SET @sDBUserName = 'root';
SET @sDBUserPassword = 'WEF#EGEHEH$$^*DI';
SET @sSubmchid = concat('Q72T35D5E', ceiling(rand() * 7800));
SET @sBrandName = '娃哈哈';
-- 
CALL SP_Company_Create(@iErrorCode, @sErrorMsg, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossPassword, @sBossWechat, @sDBName, @sKey, @sName, @sBossName, @sDBUserName, @sDBUserPassword, @sSubmchid, @sBrandName, @sLogo);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND EXISTS (SELECT 1 FROM t_company WHERE F_ID = @iID + 1 AND F_BusinessLicensePicture IS NULL AND F_Logo IS NULL), '测试成功', '测试失败') AS 'Case15 Testing Result';
-- 
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sBusinessLicenseSN = 'TE232323663215';
SET @sBusinessLicensePicture = '';
SET @sLogo = '';
SET @sBossPhone = '13324544472';
SET @sBossPassword = '000010';
SET @sBossWechat = 'cjs122456';
SET @sDBName = concat('nbr_2', ceiling(rand() * 11100));
SET @sKey = concat('92322456', ceiling(rand() * 10232300));
SET @sName = concat('博昕科技', ceiling(rand() * 1053400));
SET @sBossName = 'wahaha';
SET @sDBUserName = 'root';
SET @sDBUserPassword = 'WEF#EGEHEH$$^*DI';
SET @sSubmchid = concat('A72T35D5E', ceiling(rand() * 10770));
SET @sBrandName = '娃哈哈';
-- 
CALL SP_Company_Create(@iErrorCode, @sErrorMsg, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossPassword, @sBossWechat, @sDBName, @sKey, @sName, @sBossName, @sDBUserName, @sDBUserPassword, @sSubmchid, @sBrandName, @sLogo);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND EXISTS (SELECT 1 FROM t_company WHERE F_ID = @iID + 2 AND F_BusinessLicensePicture IS NULL AND F_Logo IS NULL), '测试成功', '测试失败') AS 'Case15 Testing Result';
-- 
DELETE FROM nbr_bx.t_company WHERE F_ID = @iID;
DELETE FROM nbr_bx.t_company WHERE F_ID = @iID + 1;
DELETE FROM nbr_bx.t_company WHERE F_ID = @iID + 2;

SELECT '-----------------CASE16: 插入重复的logo ------------------' AS 'CASE16';
SET @sLogo = '/p/11.jpg';
INSERT INTO nbr_bx.t_company (F_Name, F_SN, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_DBUserName, F_DBUserPassword, F_Status,F_Submchid, F_BrandName, F_ExpireDatetime)
VALUES ('BX三号分公司', '668869', '77777777777777', '/p/123.jpg', @sLogo, '老板1号', '13123615881', '000000', 'a13123615881', 'nbr_s11', '12345678901234567890123456789099', 'nbr11', 'WEF#EGEHEH$$^*DI', 0, 'F7235D195E', '娃哈哈', '2030-12-02 01:01:01');
SET @iID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sBusinessLicenseSN = 'TE232323663215';
SET @sBusinessLicensePicture = '';
SET @sBossPhone = '13324544472';
SET @sBossPassword = '000010';
SET @sBossWechat = 'cjs122456';
SET @sDBName = concat('nbr_2', ceiling(rand() * 11100));
SET @sKey = concat('92322456', ceiling(rand() * 10232300));
SET @sName = concat('博昕科技', ceiling(rand() * 1053400));
SET @sBossName = 'wahaha';
SET @sDBUserName = 'root';
SET @sDBUserPassword = 'WEF#EGEHEH$$^*DI';
SET @sSubmchid = concat('Q72T35D5E', ceiling(rand() * 10770));
SET @sBrandName = '娃哈哈';
-- 
CALL SP_Company_Create(@iErrorCode, @sErrorMsg, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossPassword, @sBossWechat, @sDBName, @sKey, @sName, @sBossName, @sDBUserName, @sDBUserPassword, @sSubmchid, @sBrandName, @sLogo);
SELECT IF(@iErrorCode = 1, '测试成功', '测试失败') AS 'Case16 Testing Result';
-- 
DELETE FROM nbr_bx.t_company WHERE F_ID = @iID;