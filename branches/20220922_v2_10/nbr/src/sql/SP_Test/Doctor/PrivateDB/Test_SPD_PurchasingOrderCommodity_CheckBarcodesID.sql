SELECT '++++++++++++++++++ Test_SPD_PurchasingOrderCommodity_CheckBarcodesID.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:�������� -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_PurchasingOrderCommodity_CheckBarcodesID(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

-- ��PurchasingOrderCommodity��F_barcodeID�ֶ��Ǳ�t_barcodes����������ܲ��벻���ڵ�barcodeID


