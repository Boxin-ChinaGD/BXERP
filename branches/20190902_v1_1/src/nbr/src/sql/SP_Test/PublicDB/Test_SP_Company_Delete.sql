SELECT '++++++++++++++++++++++++++++++SP_Company_Delete.sql++++++++++++++++++++++++++++++++';

SELECT '-----------------------CASE1 : ’˝≥£…æ≥˝--------------------------' AS 'CASE 1';
INSERT INTO nbr_bx.T_Company (F_BusinessLicenseSN,F_BusinessLicensePicture,F_Logo,F_BossPhone, F_BossPassword, F_BossWechat,F_DBName,F_Key,F_Status,F_DBUserName,F_DBUserPassword)
VALUES ('FB45235235456','url=dftsfdh5sfl','/p/11.jpg','13146455881','000000','sdfh4sfh','bx2nbrA2SA7','123456d',0,'root','WEF#EGEHEH$$^*DI');
SET @iID = LAST_INSERT_ID();
SET @sErrorMsg = '';
SET @iErrorCode = 0;

CALL SP_Company_Delete(@iErrorCode, @sErrorMsg, @iID);
SELECT @iErrorCode;
SELECT 1 FROM nbr_bx.t_company WHERE F_ID = @iID AND F_Status = 1;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'Case 1 Test Resullt';

DELETE FROM nbr_bx.t_company WHERE F_ID = @iID;

SELECT '-----------------------CASE2 : …æ≥˝≤ª¥Ê‘⁄µƒID--------------------------' AS 'CASE 2';
SET @iID = -1;
SET @sErrorMsg = '';
SET @iErrorCode = 0;
CALL SP_Company_Delete(@iErrorCode, @sErrorMsg, @iID);
SELECT @iErrorCode;
SELECT 1 FROM nbr_bx.t_company WHERE F_ID = @iID AND F_Status = 1;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'Case 2 Test Resullt';
DELETE FROM nbr_bx.t_company WHERE F_ID = @iID;