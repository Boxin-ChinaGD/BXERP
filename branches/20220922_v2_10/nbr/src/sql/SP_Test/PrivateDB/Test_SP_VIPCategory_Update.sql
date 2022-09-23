-- case 1:��Ա��������ظ���������Ӧ����1
SET @sName1 = '�ƽ��Ա1';
INSERT INTO t_vip_category(F_Name) VALUES(@sName1);
SET @iID1 = LAST_INSERT_ID();

SET @sName2 = '�ƽ��Ա';
INSERT INTO t_vip_category(F_Name) VALUES(@sName2);
SET @iID2 = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_VIPCategory_Update(@iErrorCode, @sErrorMsg, @iID1, @sName2);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_vip_category WHERE F_ID = @iID1 AND F_Name = @sName2;
SELECT IF(found_rows() = 0 AND @iErrorCode = 1, '���Գɹ�', '����ʧ��') AS 'Testing Result';

DELETE FROM t_vip_category WHERE F_ID = @iID1;
DELETE FROM t_vip_category WHERE F_ID = @iID2;


-- case 2: ��Ա������Ʋ��ظ���������Ӧ����0
SET @sName3 = '��ʯ��Ա';
INSERT INTO t_vip_category(F_Name) VALUES(@sName3);
SET @iID3 = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName4 = '������Ա';

call SP_VIPCategory_Update(@iErrorCode, @sErrorMsg, @iID3, @sName4);
SELECT @sErrorMsg;
SELECT 1 FROM t_vip_category WHERE F_ID = @iID3 AND F_Name = @sName4;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';

DELETE FROM t_vip_category WHERE F_ID = @iID3;