SELECT '++++++++++++++++++ Test_SP_ProviderDistrict_Retrieve1.sql ++++++++++++++++++++';

SELECT '-----------------------------case1:������ѯ-----------------------------------' AS 'case1';
INSERT INTO t_providerdistrict (F_Name) VALUES ("����");

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();

CALL SP_ProviderDistrict_Retrieve1(@iErrorCode, @sErrorMsg, @iID);

SELECT IF(found_rows() = 1 AND @iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

DELETE FROM t_providerdistrict WHERE F_ID = @iID;

SELECT '-----------------------------case2:��ѯһ�������ڵ�ID-----------------------------------' AS 'case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();

CALL SP_ProviderDistrict_Retrieve1(@iErrorCode, @sErrorMsg, @iID);

SELECT IF(found_rows() = 0 AND @iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';