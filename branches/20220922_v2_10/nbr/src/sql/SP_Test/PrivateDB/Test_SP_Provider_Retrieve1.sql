SELECT '++++++++++++++++++ Test_SP_Provider_Retrieve1.sql ++++++++++++++++++++';

SELECT '----------------------------case1:��������һ����Ӧ��---------------------------------------------' AS 'case1';
INSERT INTO t_provider (F_Name, F_DistrictID, F_Address, F_ContactName, F_Mobile)
VALUES ('���Ź�Ӧ��', '1','�������������ʮ����ѧ','Ada','13129366666');
SET @iID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_Provider_Retrieve1(@iErrorCode, @sErrorMsg, @iID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_Provider WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

DELETE FROM t_provider WHERE F_ID = @iID;

SELECT '----------------------------case2:����һ�������ڵĹ�Ӧ��---------------------------------------------' AS 'case2';
SET @iID = -99;
SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_Provider_Retrieve1(@iErrorCode, @sErrorMsg, @iID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_Provider WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';