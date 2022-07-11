SELECT '++++++++++++++++++ Test_SP_ShopDistrict_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: 正常添加 -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = "昆明1";

CALL SP_ShopDistrict_Create(@iErrorCode, @sErrorMsg, @sName);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_ShopDistrict WHERE F_Name = @sName;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'CASE1 Testing Result';

SELECT '-------------------- Case2: 重复添加 -------------------------' AS 'Case2';
SET @sErrorMsg = '';

CALL SP_ShopDistrict_Create(@iErrorCode, @sErrorMsg, @sName);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_shopDistrict WHERE F_Name = @sName;
SELECT IF(found_rows() = 1 AND @iErrorCode = 1, '测试成功', '测试失败') AS 'CASE2 Testing Result';	-- ... found_rows() = 1判断无效。侥幸通过测试

DELETE FROM t_providerdistrict WHERE F_ID = LAST_INSERT_ID();