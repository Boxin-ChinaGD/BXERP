-- case1:��Ա�������иû�Ա�����ɾ�����ˣ��������Ϊ7
SET @iID = 3;
SET @sErrorMsg = '';
SET @iErrorCode = 0;

CALL SP_VIPCategory_Delete(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT 1 FROM t_VIP_Category WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Testing Result';

-- case2:��Ա����û�����ֻ�Ա����𡣿���ֱ��ɾ�����������Ϊ0
SET @sName = '������Ա1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';

INSERT INTO t_vip_category (F_Name)
VALUES (@sName);

SET @iID = LAST_INSERT_ID();

CALL SP_VIPCategory_Delete(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT 1 FROM t_VIP_Category WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';

SELECT * FROM t_vip_category WHERE F_ID = @iID;

-- case3:ɾ��һ�������ڵ�Id
SET @iID = -22;
SET @sErrorMsg = '';
SET @iErrorCode = 0;

CALL SP_VIPCategory_Delete(@iErrorCode, @sErrorMsg, @iID);
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';