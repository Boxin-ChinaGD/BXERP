-- ��Ӳ��ظ��Ļ�Ա��𣬴�����Ϊ0
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '��ͭ��Ա';

CALL SP_VIPCategory_Create(@iErrorCode, @sErrorMsg, @sName);

SELECT 1 FROM t_VIP_Category WHERE F_Name = @sName;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';

SELECT @sErrorMsg;
DELETE FROM t_vip_category WHERE F_ID = last_insert_id();
SELECT @iErrorCode;

-- ��ӻ�Ա��𣬲�����ӣ�������Ϊ1
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '�ƽ��Ա';

CALL SP_VIPCategory_Create(@iErrorCode, @sErrorMsg, @sName);

SELECT @sErrorMsg;
SELECT 1 FROM t_VIP_Category WHERE F_Name = @sName;
SELECT IF(found_rows() = 1 AND @iErrorCode = 1, '���Գɹ�', '����ʧ��') AS 'Testing Result';