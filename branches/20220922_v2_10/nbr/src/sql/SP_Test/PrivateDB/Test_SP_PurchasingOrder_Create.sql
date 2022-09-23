SELECT '++++++++++++++++++ Test_SP_PurchasingOrder_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: �������� -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sRemark = 'purchasingPlanTable CASE 1';
SET @iStaffID = 3;
SET @iProviderID = 1;
SET @iShopID = 2;

CALL SP_PurchasingOrder_Create(@iErrorCode, @sErrorMsg,@iShopID,@iStaffID,@iProviderID,@sRemark);

SELECT 1 FROM t_PurchasingOrder WHERE 1=1
	AND F_StaffID = @iStaffID AND F_ProviderID = @iProviderID AND F_Remark = @sRemark;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE1 Testing Result';
SELECT @sErrorMsg;
SELECT @iErrorCode;
DELETE FROM T_PurchasingOrder WHERE F_Remark = 'purchasingPlanTable CASE 1';

SELECT '-------------------- Case2: iStaffID �����ڣ����ش�����3 -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sRemark = '������';
SET @iStaffID = 999;
SET @iProviderID = 1;
SET @iShopID = 2;

CALL SP_PurchasingOrder_Create(@iErrorCode, @sErrorMsg,@iShopID,@iStaffID,@iProviderID,@sRemark);
SELECT 1 FROM t_PurchasingOrder WHERE F_StaffID = @iStaffID AND F_ProviderID = @iProviderID AND F_Remark = @sRemark;

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'CASE2 Testing Result';

SELECT '-------------------- Case3: iProviderID �����ڣ����ش�����7 -------------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sRemark = '������';
SET @iStaffID = 1;
SET @iProviderID = 999;
SET @iShopID = 2;

CALL SP_PurchasingOrder_Create(@iErrorCode, @sErrorMsg,@iShopID,@iStaffID,@iProviderID,@sRemark);
SELECT 1 FROM t_PurchasingOrder WHERE F_StaffID = @iStaffID AND F_ProviderID = @iProviderID AND F_Remark = @sRemark;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'CASE3 Testing Result';

SELECT '-------------------- Case4: @sRemark�ɹ��ܽ��ֶγ������ݿ��ַ�����,������������(���ݿ��������ֶγ�����ȵ��ַ�) -------------------------' AS 'Case4';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sRemark = '������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������';
SET @iStaffID = 3;
SET @iProviderID = 1;
SET @iShopID = 2;

CALL SP_PurchasingOrder_Create(@iErrorCode, @sErrorMsg,@iShopID,@iStaffID,@iProviderID,@sRemark);
SELECT @sErrorMsg;
SELECT 1 FROM t_PurchasingOrder WHERE F_StaffID = @iStaffID AND F_ProviderID = @iProviderID;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE4 Testing Result';
DELETE FROM t_purchasingorder WHERE F_Remark = '����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������';

SELECT '-------------------- Case5: iStaffIDΪ��ְԱ�������ش�����7 -------------------------' AS 'Case5';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sRemark = '������';
SET @iStaffID = 5;
SET @iProviderID = 1;
SET @iShopID = 2;

CALL SP_PurchasingOrder_Create(@iErrorCode, @sErrorMsg, @iShopID, @iStaffID, @iProviderID, @sRemark);
SELECT 1 FROM t_PurchasingOrder WHERE F_StaffID = @iStaffID AND F_ProviderID = @iProviderID AND F_Remark = @sRemark;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg='��ǰ�ʻ����������ɹ���', '���Գɹ�', '����ʧ��') AS 'CASE5 Testing Result';

SELECT '-------------------- Case5: �����ɹ������ŵ겻���ڣ����ش�����7 -------------------------' AS 'Case5';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sRemark = '';
SET @iStaffID = 3;
SET @iProviderID = 1;
SET @iShopID = -1;

CALL SP_PurchasingOrder_Create(@iErrorCode, @sErrorMsg,@iShopID,@iStaffID,@iProviderID,@sRemark);


SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '���ŵ겻���ڣ�������ѡ���ŵ�', '���Գɹ�', '����ʧ��') AS 'CASE1 Testing Result';
SELECT @sErrorMsg;
SELECT @iErrorCode;
DELETE FROM T_PurchasingOrder WHERE F_Remark = 'purchasingPlanTable CASE 5';