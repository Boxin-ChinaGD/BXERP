SELECT '++++++++++++++++++Test_SP_Company_Create.sql+++++++++++++++++++++++';

SELECT '-----------------CASE1: �������------------------' AS 'CASE1';

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
SET @sName = '1�Ź�˾';
SET @sBossName = '���ϰ�';
-- -- SET @iBxStaffID = 0;
SET @sDBUserName = 'root';
SET @sDBUserPassword = 'WEF#EGEHEH$$^*DI';
SET @sSubmchid = 'FT232323323';
SET @sBrandName = '�޹���';

CALL SP_Company_Create(@iErrorCode, @sErrorMsg, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossPassword, @sBossWechat, @sDBName, @sKey, @sName, @sBossName, @sDBUserName, @sDBUserPassword, @sSubmchid, @sBrandName, @sLogo);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

DELETE FROM nbr_bx.t_company WHERE F_DBName = @sDBName;

SELECT '-----------------CASE2: �ظ���� ���ش�����Ϊ1------------------' AS 'CASE2';
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
SET @sName = '2�Ź�˾';
SET @sBossName = '2�Ŵ��ϰ�';
-- -- SET @iBxStaffID = 0;
SET @sDBUserName = 'root';
SET @sDBUserPassword = 'WEF#EGEHEH$$^*DI';
SET @sSubmchid = NULL;
SET @sBrandName = '�޹���';

CALL SP_Company_Create (@iErrorCode, @sErrorMsg, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossPassword, @sBossWechat, @sDBName, @sKey, @sName, @sBossName, @sDBUserName, @sDBUserPassword, @sSubmchid, @sBrandName, @sLogo);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 1, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';


--	SELECT '-----------------CASE3: ����Ϊ�� ���ش�����Ϊ1 ------------------' AS 'CASE3';
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
--	SET @sName = '3�Ź�˾';
--	SET @sBossName = '3�Ŵ��ϰ�';
--	-- --SET @iBxStaffID = 0;
--	SET @sDBUserName = 'root';
--	SET @sDBUserPassword = 'WEF#EGEHEH$$^*DI';
--	
--	CALL SP_Company_Create (@iErrorCode, @sErrorMsg, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossPassword, @sBossWechat, @sDBName, @sKey, @sName, @sBossName,@sDBUserName, @sDBUserPassword);
--	SELECT @iErrorCode;
--	SELECT @sErrorMsg;
--	SELECT IF(@iErrorCode = 1, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';

--	SELECT '-----------------CASE4:��˾����Ϊ��------------------' AS 'CASE4'; -- �Ѿ��ƶ���java�������
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
--	SET @sBossName = '���ϰ�';
--	-- -- SET @iBxStaffID = 0;
--	SET @sDBUserName = 'root';
--	SET @sDBUserPassword = 'WEF#EGEHEH$$^*DI';
--	
--	CALL SP_Company_Create (@iErrorCode, @sErrorMsg, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossPassword, @sBossWechat, @sDBName, @sKey, @sName, @sBossName, @sDBUserName, @sDBUserPassword);
--	
--	SELECT IF(@sErrorMsg = '����Ϊ�ջ����ݲ�����Ҫ��' AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';

--	SELECT '-----------------CASE5:�ϰ�����Ϊ��------------------' AS 'CASE5';-- �Ѿ��ƶ���java�������
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @sBusinessLicenseSN = 'FB232323323';
--	SET @sBusinessLicensePicture = 'url=FB232323323';
--	SET @sBossPhone = '13324544444';
--	SET @sBossPassword = '000000';
--	SET @sBossWechat = 'cjs123456';
--	SET @sDBName = 'bxnbr11l2';
--	SET @sKey = '123456';
--	SET @sName = '4�Ź�˾' ;
--	SET @sBossName = '';
--	--	-- SET @iBxStaffID = 0;
--	SET @sDBUserName = 'root';
--	SET @sDBUserPassword = 'WEF#EGEHEH$$^*DI';
--	
--	CALL SP_Company_Create (@iErrorCode, @sErrorMsg, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossPassword, @sBossWechat, @sDBName, @sKey, @sName, @sBossName, @sDBUserName, @sDBUserPassword);
--	
--	SELECT IF(@sErrorMsg = '����Ϊ�ջ����ݲ�����Ҫ��' AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';

--	SELECT '-----------------CASE6:��˾���ƺ��ϰ����ֶ�Ϊ��------------------' AS 'CASE6';-- �Ѿ��ƶ���java�������
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
--	SELECT IF(@sErrorMsg = '����Ϊ�ջ����ݲ�����Ҫ��' AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case6 Testing Result';

--	SELECT '-----------------CASE7:�ϰ��¼����Ϊ��------------------' AS 'CASE7';-- �Ѿ��ƶ���java�������
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @sBusinessLicenseSN = 'FB232323323';
--	SET @sBusinessLicensePicture = 'url=FB232323323';
--	SET @sBossPhone = '13324544444';
--	SET @sBossPassword = '';
--	SET @sBossWechat = 'cjs123456';
--	SET @sDBName = 'bxnbr11l27';
--	SET @sKey = '123457';
--	SET @sName = '7�Ź�˾' ;
--	SET @sBossName = '';
--	-- SET @iBxStaffID = 0;
--	SET @sDBUserName = 'root';
--	SET @sDBUserPassword = 'WEF#EGEHEH$$^*DI';
--	
--	CALL SP_Company_Create (@iErrorCode, @sErrorMsg, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossPassword, @sBossWechat, @sDBName, @sKey, @sName, @sBossName, @sDBUserName, @sDBUserPassword);
--	SELECT @iErrorCode;
--	SELECT @sErrorMsg;
--	SELECT IF(@sErrorMsg = '����Ϊ�ջ����ݲ�����Ҫ��' AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case7 Testing Result';


SELECT '-----------------CASE8: �ظ����Ӫҵִ�գ� ���ش�����Ϊ1------------------' AS 'CASE8';
INSERT INTO nbr_bx.t_company (F_Name, F_SN, F_BusinessLicenseSN, F_BusinessLicensePicture, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_DBUserName, F_DBUserPassword, F_Status, F_ExpireDatetime)
VALUES ('BX���ŷֹ�˾', '668866', '77777777777777', '/p/123.jpg', '�ϰ�1��', '13123615881', '000000', 'a13123615881', 'nbr11', '12345678901234567890123456789099', 'nbr11', 'WEF#EGEHEH$$^*DI', 0, '2030-12-02 01:01:01');
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
SET @sName = '2�Ź�˾';
SET @sBossName = '2�Ŵ��ϰ�';
-- SET @iBxStaffID = 0;
SET @sDBUserName = 'root';
SET @sDBUserPassword = 'WEF#EGEHEH$$^*DI';
SET @sSubmchid = NULL;
SET @sBrandName = '�޹���';

CALL SP_Company_Create (@iErrorCode, @sErrorMsg, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossPassword, @sBossWechat, @sDBName, @sKey, @sName, @sBossName, @sDBUserName, @sDBUserPassword, @sSubmchid, @sBrandName, @sLogo);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 1, '���Գɹ�', '����ʧ��') AS 'Case8 Testing Result';

DELETE FROM  nbr_bx.t_company WHERE F_ID = @iID;

SELECT '-----------------CASE9: �ظ����Ӫҵִ����Ƭ�� ���ش�����Ϊ1------------------' AS 'CASE9';
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
SET @sName = '2�Ź�˾';
SET @sBossName = '2�Ŵ��ϰ�';
-- SET @iBxStaffID = 0;
SET @sDBUserName = 'root';
SET @sDBUserPassword = 'WEF#EGEHEH$$^*DI';
SET @sSubmchid = NULL;
SET @sBrandName = '�޹���';

CALL SP_Company_Create (@iErrorCode, @sErrorMsg, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossPassword, @sBossWechat, @sDBName, @sKey, @sName, @sBossName, @sDBUserName, @sDBUserPassword, @sSubmchid, @sBrandName, @sLogo);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 1, '���Գɹ�', '����ʧ��') AS 'Case9 Testing Result';

UPDATE nbr_bx.t_company SET F_BusinessLicensePicture = NULL WHERE F_ID = 1;

SELECT '-----------------CASE10: �ظ���ӹ�˾���ƣ� ���ش�����Ϊ1------------------' AS 'CASE10';
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
SET @sName = 'BXһ�ŷֹ�˾';
SET @sBossName = '2�Ŵ��ϰ�';
-- SET @iBxStaffID = 0;
SET @sDBUserName = 'root';
SET @sDBUserPassword = 'WEF#EGEHEH$$^*DI';
SET @sSubmchid = NULL;
SET @sBrandName = '�޹���';

CALL SP_Company_Create (@iErrorCode, @sErrorMsg, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossPassword, @sBossWechat, @sDBName, @sKey, @sName, @sBossName, @sDBUserName, @sDBUserPassword, @sSubmchid, @sBrandName, @sLogo);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 1, '���Գɹ�', '����ʧ��') AS 'Case10 Testing Result';


SELECT '-----------------CASE11: �ظ���ӹ�˾Կ�ף� ���ش�����Ϊ1------------------' AS 'CASE11';
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
SET @sName = 'TXһ�ŷֹ�˾';
SET @sBossName = '2�Ŵ��ϰ�';
-- SET @iBxStaffID = 0;
SET @sDBUserName = 'root';
SET @sDBUserPassword = 'WEF#EGEHEH$$^*DI';
SET @sSubmchid = NULL;
SET @sBrandName = '�޹���';

CALL SP_Company_Create (@iErrorCode, @sErrorMsg, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossPassword, @sBossWechat, @sDBName, @sKey, @sName, @sBossName, @sDBUserName, @sDBUserPassword, @sSubmchid, @sBrandName, @sLogo);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 1, '���Գɹ�', '����ʧ��') AS 'Case11 Testing Result';

SELECT '-----------------CASE12: �ظ�������̻��ţ� ���ش�����Ϊ1------------------' AS 'CASE12';
INSERT INTO nbr_bx.t_company (F_Name, F_SN, F_BusinessLicenseSN, F_BusinessLicensePicture, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_DBUserName, F_DBUserPassword, F_Status,F_Submchid, F_ExpireDatetime)
VALUES ('BX���ŷֹ�˾', '668869', '77777777777777', '/p/123.jpg', '�ϰ�1��', '13123615881', '000000', 'a13123615881', 'nbr11', '12345678901234567890123456789099', 'nbr11', 'WEF#EGEHEH$$^*DI', 0, 'F7235D195E', '2030-12-02 01:01:01');
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
SET @sName = 'c�Ź�˾';
SET @sBossName = 'cjs';
-- -- SET @iBxStaffID = 0;
SET @sDBUserName = 'root';
SET @sDBUserPassword = 'WEF#EGEHEH$$^*DI';
SET @sSubmchid = 'F7235D195E';
SET @sBrandName = '�޹���';

CALL SP_Company_Create(@iErrorCode, @sErrorMsg, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossPassword, @sBossWechat, @sDBName, @sKey, @sName, @sBossName, @sDBUserName, @sDBUserPassword, @sSubmchid, @sBrandName, @sLogo);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 1 AND @sErrorMsg = '������˾�Ѵ���������̻���', '���Գɹ�', '����ʧ��') AS 'Case12 Testing Result';

DELETE FROM nbr_bx.t_company WHERE F_ID = @iID;


SELECT '-----------------CASE13: ����һ�����̻���Ϊ�մ��Ĺ�˾�����½�һ�����̻���Ϊ�մ��Ĺ�˾�����Ϊ������˾�����ɹ�------------------' AS 'CASE13';
INSERT INTO nbr_bx.t_company (F_Name, F_SN, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_DBUserName, F_DBUserPassword, F_Status,F_Submchid, F_ExpireDatetime)
VALUES ('BX���ŷֹ�˾', '668869', '77777777777777', '/p/123.jpg', '/p/11.jpg', '�ϰ�1��', '13123615881', '000000', 'a13123615881', 'nbr11', '12345678901234567890123456789099', 'nbr11', 'WEF#EGEHEH$$^*DI', 0, '', '2030-12-02 01:01:01');
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
SET @sName = 'c�Ź�˾';
SET @sBossName = 'cjs';
-- -- SET @iBxStaffID = 0;
SET @sDBUserName = 'root';
SET @sDBUserPassword = 'WEF#EGEHEH$$^*DI';
SET @sSubmchid = '';
SET @sBrandName = '�޹���';

CALL SP_Company_Create(@iErrorCode, @sErrorMsg, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossPassword, @sBossWechat, @sDBName, @sKey, @sName, @sBossName, @sDBUserName, @sDBUserPassword, @sSubmchid, @sBrandName, @sLogo);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case13 Testing Result';

DELETE FROM nbr_bx.t_company WHERE F_ID = @iID;
DELETE FROM nbr_bx.t_company WHERE F_DBName = @sDBName;




SELECT '-----------------CASE14: ��ͬ�Ĺ�˾��������ͬ��brandName ------------------' AS 'CASE14';
-- 
INSERT INTO nbr_bx.t_company (F_Name, F_SN, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_DBUserName, F_DBUserPassword, F_Status,F_Submchid, F_BrandName, F_ExpireDatetime)
VALUES ('BX���ŷֹ�˾', '668869', '77777777777777', '/p/123.jpg', '/p/11.jpg', '�ϰ�1��', '13123615881', '000000', 'a13123615881', 'nbr11', '12345678901234567890123456789099', 'nbr11', 'WEF#EGEHEH$$^*DI', 0, 'F7235D195E', '�޹���', '2030-12-02 01:01:01');
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
SET @sName = 'c�Ź�˾';
SET @sBossName = 'wahaha';
-- -- SET @iBxStaffID = 0;
SET @sDBUserName = 'root';
SET @sDBUserPassword = 'WEF#EGEHEH$$^*DI';
SET @sSubmchid = 'Q7235D195E';
SET @sBrandName = '�޹���';
-- 
CALL SP_Company_Create(@iErrorCode, @sErrorMsg, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossPassword, @sBossWechat, @sDBName, @sKey, @sName, @sBossName, @sDBUserName, @sDBUserPassword, @sSubmchid, @sBrandName, @sLogo);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case14 Testing Result';
-- 
DELETE FROM nbr_bx.t_company WHERE F_ID = @iID;
DELETE FROM nbr_bx.t_company WHERE F_DBName = @sDBName;


SELECT '-----------------CASE15: ����2����˾��Ӫҵִ��ͼƬ��logo���ǿմ������ȫ���ɹ� ------------------' AS 'CASE15';
-- �Ȳ���һ����˾����ȡ��ǰ���F_ID
INSERT INTO nbr_bx.t_company (F_Name, F_SN, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_DBUserName, F_DBUserPassword, F_Status,F_Submchid, F_BrandName, F_ExpireDatetime)
VALUES ('BX���ŷֹ�˾RR', '668869', '77717771368777', '/p/nbxxxr_s11.jpg', '/p/1241.jpg', '�ϰ�1��', '13123615881', '000000', 'a13123615881', 'nbxxxr_s11', '123456789014567890123456789099', 'nbxxxr_s11', 'WEF#EGEHEH$$^*DI', 0, 'F7235D1915E', '�޹���', '2030-12-02 01:01:01');
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
SET @sName = concat('��꿿Ƽ�', ceiling(rand() * 176000));
SET @sBossName = 'wahaha';
SET @sDBUserName = 'root';
SET @sDBUserPassword = 'WEF#EGEHEH$$^*DI';
SET @sSubmchid = concat('Q72T35D5E', ceiling(rand() * 7800));
SET @sBrandName = '�޹���';
-- 
CALL SP_Company_Create(@iErrorCode, @sErrorMsg, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossPassword, @sBossWechat, @sDBName, @sKey, @sName, @sBossName, @sDBUserName, @sDBUserPassword, @sSubmchid, @sBrandName, @sLogo);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND EXISTS (SELECT 1 FROM t_company WHERE F_ID = @iID + 1 AND F_BusinessLicensePicture IS NULL AND F_Logo IS NULL), '���Գɹ�', '����ʧ��') AS 'Case15 Testing Result';
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
SET @sName = concat('��꿿Ƽ�', ceiling(rand() * 1053400));
SET @sBossName = 'wahaha';
SET @sDBUserName = 'root';
SET @sDBUserPassword = 'WEF#EGEHEH$$^*DI';
SET @sSubmchid = concat('A72T35D5E', ceiling(rand() * 10770));
SET @sBrandName = '�޹���';
-- 
CALL SP_Company_Create(@iErrorCode, @sErrorMsg, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossPassword, @sBossWechat, @sDBName, @sKey, @sName, @sBossName, @sDBUserName, @sDBUserPassword, @sSubmchid, @sBrandName, @sLogo);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND EXISTS (SELECT 1 FROM t_company WHERE F_ID = @iID + 2 AND F_BusinessLicensePicture IS NULL AND F_Logo IS NULL), '���Գɹ�', '����ʧ��') AS 'Case15 Testing Result';
-- 
DELETE FROM nbr_bx.t_company WHERE F_ID = @iID;
DELETE FROM nbr_bx.t_company WHERE F_ID = @iID + 1;
DELETE FROM nbr_bx.t_company WHERE F_ID = @iID + 2;

SELECT '-----------------CASE16: �����ظ���logo ------------------' AS 'CASE16';
SET @sLogo = '/p/11.jpg';
INSERT INTO nbr_bx.t_company (F_Name, F_SN, F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_DBUserName, F_DBUserPassword, F_Status,F_Submchid, F_BrandName, F_ExpireDatetime)
VALUES ('BX���ŷֹ�˾', '668869', '77777777777777', '/p/123.jpg', @sLogo, '�ϰ�1��', '13123615881', '000000', 'a13123615881', 'nbr_s11', '12345678901234567890123456789099', 'nbr11', 'WEF#EGEHEH$$^*DI', 0, 'F7235D195E', '�޹���', '2030-12-02 01:01:01');
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
SET @sName = concat('��꿿Ƽ�', ceiling(rand() * 1053400));
SET @sBossName = 'wahaha';
SET @sDBUserName = 'root';
SET @sDBUserPassword = 'WEF#EGEHEH$$^*DI';
SET @sSubmchid = concat('Q72T35D5E', ceiling(rand() * 10770));
SET @sBrandName = '�޹���';
-- 
CALL SP_Company_Create(@iErrorCode, @sErrorMsg, @sBusinessLicenseSN, @sBusinessLicensePicture, @sBossPhone, @sBossPassword, @sBossWechat, @sDBName, @sKey, @sName, @sBossName, @sDBUserName, @sDBUserPassword, @sSubmchid, @sBrandName, @sLogo);
SELECT IF(@iErrorCode = 1, '���Գɹ�', '����ʧ��') AS 'Case16 Testing Result';
-- 
DELETE FROM nbr_bx.t_company WHERE F_ID = @iID;