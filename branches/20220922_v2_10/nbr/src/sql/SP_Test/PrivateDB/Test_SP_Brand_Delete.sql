SELECT '++++++++++++++++++Test_SP_Brand_Delete.sql+++++++++++++++++++++++';

SELECT '-----------------Case1: Ʒ�Ʊ���������Ʒ��Ʒ�ơ�ɾ�����ˣ��������Ϊ7------------------' AS 'Case1';

SET @iID = 3;
SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_Brand_Delete(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT 1 FROM t_brand WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

SELECT '-----------------Case2: Ʒ�Ʊ���û������Ʒ�Ƶ���Ʒ������ֱ��ɾ�����������Ϊ0------------------' AS 'Case2';

SET @sName = '��ɭ';
SET @iErrorCode = 0;
SET @sErrorMsg = '';

INSERT INTO t_brand (F_Name)
VALUES (@sName);

SET @iID = LAST_INSERT_ID();

CALL SP_Brand_Delete(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT 1 FROM t_brand WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';