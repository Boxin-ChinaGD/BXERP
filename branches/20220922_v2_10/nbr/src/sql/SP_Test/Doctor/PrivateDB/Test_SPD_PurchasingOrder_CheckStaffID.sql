SELECT '++++++++++++++++++ Test_SPD_PurchasingOrder_CheckStaffID.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:�������� -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_PurchasingOrder_CheckStaffID(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';


-- t_purchasingorder��F_StaffIDΪt_staff����������ܲ��벻���ڵ�StaffID