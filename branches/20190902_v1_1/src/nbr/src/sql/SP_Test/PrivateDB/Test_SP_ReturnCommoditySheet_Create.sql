SELECT '++++++++++++++++++ Test_SP_ReturnCommoditySheet_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:正常创建 -------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStaffID = 3;
SET @iProviderID = 5;
SET @iShopID = 2;

CALL SP_ReturnCommoditySheet_Create(@iErrorCode, @sErrorMsg, @iStaffID, @iProviderID, @iShopID);

SELECT @sErrorMsg;
DELETE FROM t_returncommoditysheet WHERE F_ID = last_insert_id();
SELECT @iErrorCode;

SELECT 1 FROM t_returncommoditysheet 
	WHERE F_StaffID = @iStaffID 
	AND F_ProviderID = @iProviderID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'case1 Testing Result';

SELECT '-------------------- Case2:创建退货单时 传入一个不存在 staffID -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStaffID = -3;
SET @iProviderID = 5;
SET @iShopID = 2;

CALL SP_ReturnCommoditySheet_Create(@iErrorCode, @sErrorMsg, @iStaffID, @iProviderID, @iShopID);

SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'case2 Testing Result';
SELECT @sErrorMsg;

SELECT '-------------------- Case3:创建退货单时 传入一个不存在 providerID -------------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';

SET @iStaffID = 3;
SET @iProviderID = -5;

CALL SP_ReturnCommoditySheet_Create(@iErrorCode, @sErrorMsg, @iStaffID, @iProviderID, @iShopID);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'case3 Testing Result';