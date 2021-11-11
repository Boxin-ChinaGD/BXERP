SELECT '++++++++++++++++++ Test_SPD_PurchasingOrderCommodity_CheckBarcodesID.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:正常测试 -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_PurchasingOrderCommodity_CheckBarcodesID(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

-- 表PurchasingOrderCommodity的F_barcodeID字段是表t_barcodes的外键，不能插入不存在的barcodeID


