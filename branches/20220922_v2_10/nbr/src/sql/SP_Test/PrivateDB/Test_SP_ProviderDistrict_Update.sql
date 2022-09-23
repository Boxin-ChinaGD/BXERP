SELECT '++++++++++++++++++ Test_SP_ProviderDistrict_Update.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: 修改无重复地域名称 -------------------------' AS 'Case1';

INSERT INTO t_providerdistrict (F_Name) VALUES ("昆明");

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();
SET @sName = "厦门3";

CALL SP_ProviderDistrict_Update(@iErrorCode, @sErrorMsg, @iID, @sName);

SELECT @sErrorMsg;
SELECT 1 FROM t_ProviderDistrict WHERE F_ID = @iID AND F_Name = @sName;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'CASE1 Testing Result';

SELECT '-------------------- Case2: 更改成自身的名称 -------------------------' AS 'Case2';
SET @sErrorMsg = '';
SET @sName = "厦门3";

CALL SP_ProviderDistrict_Update(@iErrorCode, @sErrorMsg, @iID, @sName);

SELECT @sErrorMsg;
SELECT 1 FROM t_ProviderDistrict WHERE F_ID = @iID AND F_Name = @sName;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'CASE2 Testing Result';

SELECT '-------------------- Case3: 修改已有名称 -------------------------' AS 'Case3';
SET @sErrorMsg = '';
SET @sName = "广东";

CALL SP_ProviderDistrict_Update(@iErrorCode, @sErrorMsg, @iID, @sName);

SELECT @sErrorMsg;
SELECT 1 FROM t_ProviderDistrict WHERE F_ID = @iID AND F_Name = @sName;
SELECT IF(found_rows() = 0 AND @iErrorCode = 1, '测试成功', '测试失败') AS 'CASE3 Testing Result';

DELETE FROM t_providerdistrict WHERE F_ID = @iID;

SELECT '-------------------- Case4: 修改一个不存在的ID -------------------------' AS 'Case4';
SET @sErrorMsg = '';
SET @sName = "广东1";
SET @iID = 999;
CALL SP_ProviderDistrict_Update(@iErrorCode, @sErrorMsg, @iID, @sName);

SELECT @sErrorMsg;
SELECT 1 FROM t_ProviderDistrict WHERE F_ID = @iID AND F_Name = @sName;
SELECT IF(found_rows() = 0 AND @iErrorCode = 4, '测试成功', '测试失败') AS 'CASE4 Testing Result';

DELETE FROM t_providerdistrict WHERE F_ID = @iID;