SELECT '++++++++++++++++++ Test_SP_PurchasingOrder_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: 正常创建 -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sRemark = 'purchasingPlanTable CASE 1';
SET @iStaffID = 3;
SET @iProviderID = 1;
SET @iShopID = 2;

CALL SP_PurchasingOrder_Create(@iErrorCode, @sErrorMsg,@iShopID,@iStaffID,@iProviderID,@sRemark);

SELECT 1 FROM t_PurchasingOrder WHERE 1=1
	AND F_StaffID = @iStaffID AND F_ProviderID = @iProviderID AND F_Remark = @sRemark;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'CASE1 Testing Result';
SELECT @sErrorMsg;
SELECT @iErrorCode;
DELETE FROM T_PurchasingOrder WHERE F_Remark = 'purchasingPlanTable CASE 1';

SELECT '-------------------- Case2: iStaffID 不存在，返回错误码3 -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sRemark = '哈哈哈';
SET @iStaffID = 999;
SET @iProviderID = 1;
SET @iShopID = 2;

CALL SP_PurchasingOrder_Create(@iErrorCode, @sErrorMsg,@iShopID,@iStaffID,@iProviderID,@sRemark);
SELECT 1 FROM t_PurchasingOrder WHERE F_StaffID = @iStaffID AND F_ProviderID = @iProviderID AND F_Remark = @sRemark;

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'CASE2 Testing Result';

SELECT '-------------------- Case3: iProviderID 不存在，返回错误码7 -------------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sRemark = '哈哈哈';
SET @iStaffID = 1;
SET @iProviderID = 999;
SET @iShopID = 2;

CALL SP_PurchasingOrder_Create(@iErrorCode, @sErrorMsg,@iShopID,@iStaffID,@iProviderID,@sRemark);
SELECT 1 FROM t_PurchasingOrder WHERE F_StaffID = @iStaffID AND F_ProviderID = @iProviderID AND F_Remark = @sRemark;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'CASE3 Testing Result';

SELECT '-------------------- Case4: @sRemark采购总结字段超过数据库字符限制,超过长度限制(数据库会存入与字段长度相等的字符) -------------------------' AS 'Case4';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sRemark = '哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈';
SET @iStaffID = 3;
SET @iProviderID = 1;
SET @iShopID = 2;

CALL SP_PurchasingOrder_Create(@iErrorCode, @sErrorMsg,@iShopID,@iStaffID,@iProviderID,@sRemark);
SELECT @sErrorMsg;
SELECT 1 FROM t_PurchasingOrder WHERE F_StaffID = @iStaffID AND F_ProviderID = @iProviderID;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE4 Testing Result';
DELETE FROM t_purchasingorder WHERE F_Remark = '哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈';

SELECT '-------------------- Case5: iStaffID为离职员工，返回错误码7 -------------------------' AS 'Case5';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sRemark = '哈哈哈';
SET @iStaffID = 5;
SET @iProviderID = 1;
SET @iShopID = 2;

CALL SP_PurchasingOrder_Create(@iErrorCode, @sErrorMsg, @iShopID, @iStaffID, @iProviderID, @sRemark);
SELECT 1 FROM t_PurchasingOrder WHERE F_StaffID = @iStaffID AND F_ProviderID = @iProviderID AND F_Remark = @sRemark;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg='当前帐户不允许创建采购单', '测试成功', '测试失败') AS 'CASE5 Testing Result';

SELECT '-------------------- Case5: 创建采购单的门店不存在，返回错误码7 -------------------------' AS 'Case5';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sRemark = '';
SET @iStaffID = 3;
SET @iProviderID = 1;
SET @iShopID = -1;

CALL SP_PurchasingOrder_Create(@iErrorCode, @sErrorMsg,@iShopID,@iStaffID,@iProviderID,@sRemark);


SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '该门店不存在，请重新选择门店', '测试成功', '测试失败') AS 'CASE1 Testing Result';
SELECT @sErrorMsg;
SELECT @iErrorCode;
DELETE FROM T_PurchasingOrder WHERE F_Remark = 'purchasingPlanTable CASE 5';