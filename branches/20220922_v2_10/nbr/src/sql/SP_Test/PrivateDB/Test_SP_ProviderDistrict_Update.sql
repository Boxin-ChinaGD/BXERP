SELECT '++++++++++++++++++ Test_SP_ProviderDistrict_Update.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: �޸����ظ��������� -------------------------' AS 'Case1';

INSERT INTO t_providerdistrict (F_Name) VALUES ("����");

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();
SET @sName = "����3";

CALL SP_ProviderDistrict_Update(@iErrorCode, @sErrorMsg, @iID, @sName);

SELECT @sErrorMsg;
SELECT 1 FROM t_ProviderDistrict WHERE F_ID = @iID AND F_Name = @sName;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE1 Testing Result';

SELECT '-------------------- Case2: ���ĳ���������� -------------------------' AS 'Case2';
SET @sErrorMsg = '';
SET @sName = "����3";

CALL SP_ProviderDistrict_Update(@iErrorCode, @sErrorMsg, @iID, @sName);

SELECT @sErrorMsg;
SELECT 1 FROM t_ProviderDistrict WHERE F_ID = @iID AND F_Name = @sName;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE2 Testing Result';

SELECT '-------------------- Case3: �޸��������� -------------------------' AS 'Case3';
SET @sErrorMsg = '';
SET @sName = "�㶫";

CALL SP_ProviderDistrict_Update(@iErrorCode, @sErrorMsg, @iID, @sName);

SELECT @sErrorMsg;
SELECT 1 FROM t_ProviderDistrict WHERE F_ID = @iID AND F_Name = @sName;
SELECT IF(found_rows() = 0 AND @iErrorCode = 1, '���Գɹ�', '����ʧ��') AS 'CASE3 Testing Result';

DELETE FROM t_providerdistrict WHERE F_ID = @iID;

SELECT '-------------------- Case4: �޸�һ�������ڵ�ID -------------------------' AS 'Case4';
SET @sErrorMsg = '';
SET @sName = "�㶫1";
SET @iID = 999;
CALL SP_ProviderDistrict_Update(@iErrorCode, @sErrorMsg, @iID, @sName);

SELECT @sErrorMsg;
SELECT 1 FROM t_ProviderDistrict WHERE F_ID = @iID AND F_Name = @sName;
SELECT IF(found_rows() = 0 AND @iErrorCode = 4, '���Գɹ�', '����ʧ��') AS 'CASE4 Testing Result';

DELETE FROM t_providerdistrict WHERE F_ID = @iID;