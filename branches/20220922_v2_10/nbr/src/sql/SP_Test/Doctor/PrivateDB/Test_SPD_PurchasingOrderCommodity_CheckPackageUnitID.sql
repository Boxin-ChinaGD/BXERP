SELECT '++++++++++++++++++ Test_Test_SPD_PurchasingOrderCommodity_CheckPackageUnitID.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:�������� -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_PurchasingOrderCommodity_CheckPackageUnitID(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

-- t_purchasingordercommodity��F_PackageUnitIDΪt_packageunit����������ܲ��벻���ڵ�packageUnitID


