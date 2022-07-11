SELECT '++++++++++++++++++ Test_Test_SPD_PurchasingOrderCommodity_CheckPackageUnitID.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:正常测试 -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_PurchasingOrderCommodity_CheckPackageUnitID(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

-- t_purchasingordercommodity的F_PackageUnitID为t_packageunit的外键，不能插入不存在的packageUnitID


