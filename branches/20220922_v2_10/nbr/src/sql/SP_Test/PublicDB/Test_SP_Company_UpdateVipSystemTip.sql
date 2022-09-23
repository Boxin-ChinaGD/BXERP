SELECT '++++++++++++++++++Test_SP_Company_UpdateVipSystemTip.sql+++++++++++++++++++++++';
USE nbr_bx;
SELECT '-----------------CASE1: 修改公司的会员提示字段------------------' AS 'CASE1';

INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword,F_Submchid)
VALUES ('1号分公司', 'FB452352354516', 'url=dftsfd4h5sfl', '/p/11.jpg', '老板1号', '13146455881', '000000', 'sdf1h4sfh', 'ceshi1', '123456u1', 0, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI','1934561239');

SET @iID = LAST_INSERT_ID();
SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_Company_UpdateVipSystemTip(@iErrorCode, @sErrorMsg, @iID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM nbr_bx.t_company WHERE F_ShowVipSystemTip = 0 AND F_ID = @iID;
SELECT IF(FOUND_ROWS() = 1 AND @iErrorCode = 0 AND @sErrorMsg = '', '测试成功', '测试失败') AS 'Case1 Testing Result';

DELETE FROM nbr_bx.t_company WHERE F_ID = @iID;