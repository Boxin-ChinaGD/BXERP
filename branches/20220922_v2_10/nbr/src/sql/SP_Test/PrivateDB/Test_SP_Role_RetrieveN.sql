SELECT '-----------------Test_SP_Role_RetrieveN.sql---------------------------------';
SELECT '----------------- case1�������ɫ���ƽ��в�ѯ---------------------------------';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '����Ա';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Role_RetrieveN(@iErrorCode, @sErrorMsg, @sName, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT 1 FROM t_role WHERE F_Name = @sName;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';

SELECT '-----------------case2:�������κβ������в�ѯ---------------------------------';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Role_RetrieveN(@iErrorCode, @sErrorMsg, @sName, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT 1 FROM t_role WHERE F_Name = @sName;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';