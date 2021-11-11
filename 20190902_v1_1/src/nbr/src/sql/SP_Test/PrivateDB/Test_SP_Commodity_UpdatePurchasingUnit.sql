SELECT '++++++++++++++++++ Test_SP_Commodity_UpdatePurchasingUnit.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:修改采购单位 -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;
SET @sPurchasingUnit = '千克';

CALL SP_Commodity_UpdatePurchasingUnit(@iErrorCode, @sErrorMsg, @iID, @sPurchasingUnit);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_commodity WHERE F_ID = @iID AND F_PurchasingUnit = @sPurchasingUnit;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';