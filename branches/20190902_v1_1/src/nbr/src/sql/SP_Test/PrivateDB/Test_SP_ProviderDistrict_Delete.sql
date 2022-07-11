SELECT '++++++++++++++++++ Test_SP_ProviderDistrict_Delete.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: 正常删除 -------------------------' AS 'Case1';

INSERT INTO t_providerdistrict (F_Name) VALUES ("昆明");

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();

CALL SP_ProviderDistrict_Delete(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT 1 FROM t_ProviderDistrict WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'CASE1 Testing Result';

SELECT '-------------------- Case2: 删除的区域还有供应商使用 -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;

CALL SP_ProviderDistrict_Delete(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT 1 FROM t_ProviderDistrict WHERE F_ID = @iID;

SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '测试成功', '测试失败') AS 'CASE2 Testing Result';

SELECT '-------------------- Case3: 删除的区域不存在 -------------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = -99;

CALL SP_ProviderDistrict_Delete(@iErrorCode, @sErrorMsg, @iID);

SELECT 1 FROM t_providerdistrict WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'CASE3 Testing Result';