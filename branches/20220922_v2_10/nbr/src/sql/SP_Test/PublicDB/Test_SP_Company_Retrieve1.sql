SELECT '++++++++++++++++++Test_SP_Company_Retrieve1.sql+++++++++++++++++++++++';
SELECT '-----------------------------Case1 : ������ѯ------------------------------------------' as 'Case 1';

INSERT INTO nbr_bx.t_company (F_Name, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_Status, F_ExpireDatetime,F_DBUserName,F_DBUserPassword)
VALUES ('BX666�ŷֹ�˾', 'FB452352465456', 'url=dftsfdah5sfl', '/p/11.jpg', '�ϰ�666��', '13186455841','000000', 'sdfh4sazfh', 'bxnbr4az', '123', 0, '2030-12-02 1:01:01','root','WEF#EGEHEH$$^*DI');

SET @iID = last_insert_id();
SET @sErrorMsg = '';
CALL SP_Company_Retrieve1(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;

SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';
DELETE FROM t_company WHERE F_ID = @iID;

SELECT '-----------------------------Case2 : ��ѯ�����ڵ�ID------------------------------------------' as 'Case 2';
SET @iID = -1;
SET @sErrorMsg = '';

CALL SP_Company_Retrieve1(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';