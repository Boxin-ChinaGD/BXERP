SELECT '----------------------------Test_SP_Role_Create.sql--------------------------------';
SELECT '----------------------case1:��Ӳ��ظ��Ľ�ɫ----------------------------';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '�곤1';

CALL SP_Role_Create(@iErrorCode, @sErrorMsg, @sName);

SELECT @sErrorMsg;
SELECT 1 FROM t_role WHERE F_Name = @sName;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';

DELETE FROM t_role WHERE F_ID = last_insert_id();


SELECT '----------------------case2:����ظ��Ľ�ɫ-----------------------------';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '����Ա';

CALL SP_Role_Create(@iErrorCode, @sErrorMsg, @sName);

SELECT @sErrorMsg;
SELECT 1 FROM t_role WHERE F_Name = @sName;
SELECT IF(found_rows() = 1 AND @iErrorCode = 1, '���Գɹ�', '����ʧ��') AS 'Testing Result';

DELETE FROM t_role WHERE F_ID = last_insert_id();