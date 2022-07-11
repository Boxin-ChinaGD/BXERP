SELECT '++++++++++++++++++Test_SP_Company_UpdateSubmchid.sql+++++++++++++++++++++++';
USE nbr_bx;
SELECT '-----------------CASE1: 修改公司的子商户号(正确)------------------' AS 'CASE1';

INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword,F_Submchid)
VALUES ('1号分公司', 'FB452352354516', 'url=dftsfd4h5sfl', '/p/11.jpg', '老板1号', '13146455881', '000000', 'sdf1h4sfh', 'ceshi1', '123456u1', 0, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI','1934561239');

SET @iID = LAST_INSERT_ID();
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sSubmchid = '1934569999';

CALL SP_Company_UpdateSubmchid(@iErrorCode, @sErrorMsg, @iID, @sSubmchid);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM nbr_bx.t_company WHERE F_Submchid = @sSubmchid AND F_ID = @iID;
SELECT IF(FOUND_ROWS() = 1 AND @iErrorCode = 0 AND @sErrorMsg = '', '测试成功', '测试失败') AS 'Case1 Testing Result';

DELETE FROM nbr_bx.t_company WHERE F_ID = @iID;


SELECT '-----------------CASE2: 修改不存在的公司它的子商户号(失败)------------------' AS 'CASE2';
SET @iID = -1;
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sSubmchid = 'FT232323323';

CALL SP_Company_UpdateSubmchid(@iErrorCode, @sErrorMsg, @iID, @sSubmchid);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM nbr_bx.t_company WHERE F_Submchid = @sSubmchid AND F_ID = @iID;
SELECT IF(@sErrorMsg = '修改的公司不存在' AND @iErrorCode = 2, '测试成功', '测试失败') AS 'Case2 Testing Result';


SELECT '-----------------CASE3: 修改为已有的其他公司它有的子商户号(失败)------------------' AS 'CASE3';
INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword,F_Submchid)
VALUES ('1号分公司', 'FB452352354516', 'url=dftsfd4h5sfl', '/p/11.jpg', '老板1号', '13146455881', '000000', 'sdf1h4sfh', 'ceshi1', '123456u1', 0, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI','1934561239');

SET @iID1 = LAST_INSERT_ID();
INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword,F_Submchid)
VALUES ('2号分公司', 'FB452352354599', 'url=dftsfd4h5s99', '/p/121.jpg', '老板2号', '13146455899', '000000', 'sdf1h4s99', 'ceshi2', '123499u1', 0, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI','1934561211');

SET @iID2 = LAST_INSERT_ID();
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sSubmchid = '1934561239';

CALL SP_Company_UpdateSubmchid(@iErrorCode, @sErrorMsg, @iID2, @sSubmchid);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM nbr_bx.t_company WHERE F_Submchid = @sSubmchid AND F_ID = @iID2;
SELECT IF(@sErrorMsg = '其它公司已存在这个子商户号' AND @iErrorCode = 1, '测试成功', '测试失败') AS 'Case3 Testing Result';

DELETE FROM nbr_bx.t_company WHERE F_ID = @iID1;
DELETE FROM nbr_bx.t_company WHERE F_ID = @iID2;


SELECT '-----------------CASE4: 修改的子商户号是已删除的公司它的子商户号(正确)------------------' AS 'CASE4';
INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword,F_Submchid)
VALUES ('1号分公司', 'FB452352354516', 'url=dftsfd4h5sfl', '/p/11.jpg', '老板1号', '13146455881', '000000', 'sdf1h4sfh', 'ceshi1', '123456u1', 1, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI','1934561239');

SET @iID1 = LAST_INSERT_ID();

INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword,F_Submchid)
VALUES ('2号分公司', 'FB452352354599', 'url=dftsfd4h5s99', '/p/123.jpg', '老板2号', '13146455899', '000000', 'sdf1h4s99', 'ceshi2', '123499u1', 0, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI','1934561211');

SET @iID2 = LAST_INSERT_ID();
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sSubmchid = '1934561239';

CALL SP_Company_UpdateSubmchid(@iErrorCode, @sErrorMsg, @iID2, @sSubmchid);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM nbr_bx.t_company WHERE F_Submchid = @sSubmchid AND F_ID = @iID2;
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case4 Testing Result';

DELETE FROM nbr_bx.t_company WHERE F_ID = @iID1;
DELETE FROM nbr_bx.t_company WHERE F_ID = @iID2;


SELECT '-----------------CASE5: 修改的公司是删除状态的 ------------------' AS 'CASE5';
INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword,F_Submchid)
VALUES ('2号分公司', 'FB452352354599', 'url=dftsfd4h5s99', '/p/11.jpg', '老板2号', '13146455899', '000000', 'sdf1h4s99', 'ceshi2', '123499u1', 1, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI','1934561211');

SET @iID = LAST_INSERT_ID();
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sSubmchid = '1934561239';

CALL SP_Company_UpdateSubmchid(@iErrorCode, @sErrorMsg, @iID, @sSubmchid);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM nbr_bx.t_company WHERE F_Submchid = @sSubmchid AND F_ID = @iID;
SELECT IF(@sErrorMsg = '修改的公司不存在' AND @iErrorCode = 2, '测试成功', '测试失败') AS 'Case4 Testing Result';

DELETE FROM nbr_bx.t_company WHERE F_ID = @iID;


SELECT '-----------------CASE6: 创建一个子商户为空串的公司A，再创建一个公司B，将公司B的子商户号修改为空串.结果为修改成功.------------------' AS 'CASE6';
INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword,F_Submchid)
VALUES ('1号分公司', 'FB452352354516', 'url=dftsfd4h5sfl', '/p/112.jpg', '老板1号', '13146455881', '000000', 'sdf1h4sfh', 'ceshi1', '123456u1', 1, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI','');

SET @iID1 = LAST_INSERT_ID();

INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword,F_Submchid)
VALUES ('2号分公司', 'FB452352354599', 'url=dftsfd4h5s99', '/p/131.jpg', '老板2号', '13146455899', '000000', 'sdf1h4s99', 'ceshi2', '123499u1', 0, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI','1234567893');

SET @iID2 = LAST_INSERT_ID();
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sSubmchid = '';

CALL SP_Company_UpdateSubmchid(@iErrorCode, @sErrorMsg, @iID2, @sSubmchid);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM nbr_bx.t_company WHERE F_Submchid = @sSubmchid AND F_ID = @iID2;
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case6 Testing Result';

DELETE FROM nbr_bx.t_company WHERE F_ID = @iID1;
DELETE FROM nbr_bx.t_company WHERE F_ID = @iID2;