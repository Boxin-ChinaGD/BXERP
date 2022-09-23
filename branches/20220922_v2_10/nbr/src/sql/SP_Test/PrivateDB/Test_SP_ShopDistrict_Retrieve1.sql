SELECT '++++++++++++++++++ Test_SP_ShopDistrict_Retrieve1.sql ++++++++++++++++++++';

SELECT '+++++++++++++++++++++++ case1:������ѯ ++++++++++++++++++++++++++++++++++++' AS 'case1';
INSERT INTO t_shopdistrict (F_Name) VALUES ("����");

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();

CALL SP_shopDistrict_Retrieve1(@iErrorCode, @sErrorMsg, @iID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_shopdistrict WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

DELETE FROM t_shopdistrict WHERE F_ID = @iID;

SELECT '+++++++++++++++++++++++ case2:��ѯ�����ڵ�id ++++++++++++++++++++++++++++++++++++' AS 'case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = -1;

CALL SP_shopDistrict_Retrieve1(@iErrorCode, @sErrorMsg, @iID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_shopdistrict WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

DELETE FROM t_shopdistrict WHERE F_ID = @iID;