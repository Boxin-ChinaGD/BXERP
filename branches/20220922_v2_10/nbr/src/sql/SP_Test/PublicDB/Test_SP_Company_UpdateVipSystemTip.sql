SELECT '++++++++++++++++++Test_SP_Company_UpdateVipSystemTip.sql+++++++++++++++++++++++';
USE nbr_bx;
SELECT '-----------------CASE1: �޸Ĺ�˾�Ļ�Ա��ʾ�ֶ�------------------' AS 'CASE1';

INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword,F_Submchid)
VALUES ('1�ŷֹ�˾', 'FB452352354516', 'url=dftsfd4h5sfl', '/p/11.jpg', '�ϰ�1��', '13146455881', '000000', 'sdf1h4sfh', 'ceshi1', '123456u1', 0, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI','1934561239');

SET @iID = LAST_INSERT_ID();
SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_Company_UpdateVipSystemTip(@iErrorCode, @sErrorMsg, @iID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM nbr_bx.t_company WHERE F_ShowVipSystemTip = 0 AND F_ID = @iID;
SELECT IF(FOUND_ROWS() = 1 AND @iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

DELETE FROM nbr_bx.t_company WHERE F_ID = @iID;