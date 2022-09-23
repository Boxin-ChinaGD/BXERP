SELECT '++++++++++++++++++ Test_SP_ProviderDistrict_Delete.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: ����ɾ�� -------------------------' AS 'Case1';

INSERT INTO t_providerdistrict (F_Name) VALUES ("����");

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();

CALL SP_ProviderDistrict_Delete(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT 1 FROM t_ProviderDistrict WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE1 Testing Result';

SELECT '-------------------- Case2: ɾ���������й�Ӧ��ʹ�� -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;

CALL SP_ProviderDistrict_Delete(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT 1 FROM t_ProviderDistrict WHERE F_ID = @iID;

SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'CASE2 Testing Result';

SELECT '-------------------- Case3: ɾ�������򲻴��� -------------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = -99;

CALL SP_ProviderDistrict_Delete(@iErrorCode, @sErrorMsg, @iID);

SELECT 1 FROM t_providerdistrict WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE3 Testing Result';