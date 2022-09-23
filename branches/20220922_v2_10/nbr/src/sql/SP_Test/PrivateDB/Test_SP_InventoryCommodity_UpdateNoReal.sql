SELECT '++++++++++++++++++ SP_InventoryCommodity_UpdateNoReal.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:修改成功 -------------------------' AS 'Case1';
SET @iID = 11;
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iNoReal = 1;
SET @iNOSystem = 100;
CALL SP_InventoryCommodity_UpdateNoReal(@iErrorCode, @iErrorCode, @iID, @iNoReal, @iNOSystem);

SELECT @iErrorCode;
SELECT 1 FROM T_InventoryCommodity WHERE F_ID = @iID AND F_NoReal = @iNoReal AND F_NOSystem = @iNOSystem;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'CASE1 Testing Result';


SELECT '-------------------- Case2:要修改的ID不存在，修改失败 -------------------------' AS 'Case2';
SET @iID = 999;
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iNoReal = 1;
SET @iNOSystem = 0;

CALL SP_InventoryCommodity_UpdateNoReal(@iErrorCode, @iErrorCode, @iID, @iNoReal, @iNOSystem);

SELECT @iErrorCode;
SELECT 1 FROM T_InventoryCommodity WHERE F_ID = @iID AND F_NoReal = @iNoReal AND F_NOSystem = @iNOSystem;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'CASE1 Testing Result';