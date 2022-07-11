SELECT '++++++++++++++++++ Test_SPD_PurchasingOrder_CheckPurchasingOrderCommodity.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:正常测试 -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_PurchasingOrder_CheckPurchasingOrderCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';



SELECT '-------------------- Case2:采购订单没有采购商品 -------------------------' AS 'Case2';
-- 
INSERT INTO t_purchasingorder (F_Status, F_StaffID, F_ProviderID, F_ProviderName, F_Remark, F_CreateDatetime, F_EndDatetime, F_UpdateDatetime)
VALUES (0, 1, 1, '默认供应商', '...', now(), now(), now());
SET @iID = LAST_INSERT_ID();
-- 
SET @iErrorCode := 0;
SET @sErrorMsg := '';
-- 
CALL SPD_PurchasingOrder_CheckPurchasingOrderCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('采购订单:', @iID, '没有采购商品') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case2 Testing Result';
-- 
DELETE FROM t_purchasingorder WHERE F_ID = @iID;



SELECT '-------------------- Case3:删除状态的采购订单没有采购商品 -------------------------' AS 'Case3';
-- 
INSERT INTO t_purchasingorder (F_Status, F_StaffID, F_ProviderID, F_ProviderName, F_Remark, F_CreateDatetime, F_EndDatetime, F_UpdateDatetime)
VALUES (4, 1, 1, '默认供应商', '...', now(), now(), now());
SET @iID = LAST_INSERT_ID();
-- 
SET @iErrorCode := 0;
SET @sErrorMsg := '';
-- 
CALL SPD_PurchasingOrder_CheckPurchasingOrderCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case3 Testing Result';
-- 
DELETE FROM t_purchasingorder WHERE F_ID = @iID;