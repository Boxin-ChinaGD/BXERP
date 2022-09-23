SELECT '++++++++++++++++++ Test_SP_Provider_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: �½����ظ��Ĺ�Ӧ�� -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '�Ա�';
SET @iDistrictID = 1;
SET @sAddress = '�������������ʮ����ѧ';
SET @sContactName = 'ada';
SET @iMobile = '13122455442';

CALL SP_Provider_Create(@iErrorCode, @sErrorMsg, @sName ,@iDistrictID, @sAddress, @sContactName, @iMobile);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_Provider WHERE F_Name = @sName AND F_DistrictID = @iDistrictID AND F_Address = @sAddress AND F_ContactName = @sContactName AND F_Mobile = @iMobile;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE1 Testing Result';

DELETE FROM t_provider WHERE F_ID = last_insert_id();

SELECT '-------------------- Case2: �½��ظ��Ĺ�Ӧ�����ƣ�������Ϊ1 -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '��è';
SET @iDistrictID = 1;
SET @sAddress = '�������������ʮ����ѧ';
SET @sContactName = 'ada1';
SET @iMobile = '13122455441';

CALL SP_Provider_Create(@iErrorCode, @sErrorMsg, @sName ,@iDistrictID, @sAddress, @sContactName, @iMobile);
SELECT @sErrorMsg;
SELECT 1 FROM t_Provider WHERE F_Name = @sName AND F_DistrictID = @iDistrictID AND F_Address = @sAddress AND F_ContactName = @sContactName AND F_Mobile = @iMobile;
SELECT IF(found_rows() = 0 AND @iErrorCode = 1, '���Գɹ�', '����ʧ��') AS 'CASE2 Testing Result';

DELETE FROM t_provider WHERE F_ID = last_insert_id();

SELECT '-------------------- Case3: �����ظ��Ĺ�Ӧ�̵绰��������Ϊ1 -------------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '�Ա�';
SET @iDistrictID = 1;
SET @sAddress = '�������������ʮ����ѧ';
SET @sContactName = 'ada';
SET @iMobile = '13129355442';

CALL SP_Provider_Create(@iErrorCode, @sErrorMsg, @sName ,@iDistrictID, @sAddress, @sContactName, @iMobile);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_Provider WHERE F_Name = @sName AND F_DistrictID = @iDistrictID AND F_Address = @sAddress AND F_ContactName = @sContactName AND F_Mobile = @iMobile;
SELECT IF(found_rows() = 0 AND @iErrorCode = 1, '���Գɹ�', '����ʧ��') AS 'CASE3 Testing Result';

DELETE FROM t_provider WHERE F_ID = last_insert_id();

SELECT '-------------------- Case4: ������Ӧ��ʱֻ��д���֣�������Ϊ0 -------------------------' AS 'Case4';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '�Ա�1212';
SET @iDistrictID = 2;
SET @sAddress = '';
SET @sContactName = '';
SET @iMobile = '';

CALL SP_Provider_Create(@iErrorCode, @sErrorMsg, @sName ,@iDistrictID, @sAddress, @sContactName, @iMobile);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_Provider WHERE F_Name = @sName AND F_DistrictID IS NULL AND F_Address = @sAddress AND F_ContactName = @sContactName AND F_Mobile IS NULL;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE4 Testing Result';

DELETE FROM t_provider WHERE F_ID = last_insert_id();

SELECT '-------------------- Case5:û�д���绰������������ -------------------------' AS 'Case5';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '�Ա�3';
SET @iDistrictID = 1;
SET @sAddress = '�������������ʮ����ѧ11';
SET @sContactName = 'ada11';
SET @iMobile = '';

CALL SP_Provider_Create(@iErrorCode, @sErrorMsg, @sName ,@iDistrictID, @sAddress, @sContactName, @iMobile);
SELECT 1 FROM t_provider WHERE F_Name = @sName AND F_DistrictID = @iDistrictID AND F_ContactName = @sContactName AND F_Address = @sAddress AND F_Mobile = @iMobile;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'CASE5 Testing Result';

DELETE FROM t_provider WHERE F_ID = LAST_INSERT_ID(); 

SELECT '-------------------- Case6:ʹ�ò����ڵ�iDistrictID���д�����������Ϊ7-------------------------' AS 'Case6';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '����';
SET @iDistrictID = -999;
SET @sAddress = '�������������ʮ����ѧ';
SET @sContactName = 'ada';
SET @iMobile = '12312312311';

CALL SP_Provider_Create(@iErrorCode, @sErrorMsg, @sName ,@iDistrictID, @sAddress, @sContactName, @iMobile);
SELECT 1 FROM t_Provider WHERE F_Name = @sName AND F_DistrictID = @iDistrictID AND F_Address = @sAddress AND F_ContactName = @sContactName AND F_Mobile = @iMobile;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '�����½������ڵĹ�Ӧ������', '���Գɹ�', '����ʧ��') AS 'CASE6 Testing Result';