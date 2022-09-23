SELECT '++++++++++++++++++ Test_SPD_ReturnCommoditySheet_CheckReturnCommoditySheetCommodity.sql ++++++++++++++++++++';
SELECT '------------------ CASE1:正常测试 --------------------' AS 'CASE1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_ReturnCommoditySheet_CheckReturnCommoditySheetCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode,@sErrorMsg;
-- 
SELECT IF(@iErrorCode = 0 AND @sErrorMsg = '', '测试成功', '测试失败') AS 'CASE1 RESULT';
-- 

SELECT '------------------ CASE2:没有退货商品的退货单 --------------------' AS 'CASE2';
-- 
INSERT INTO t_returncommoditysheet (F_StaffID, F_ProviderID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES (1, 1, 0, now(), now());
SET @iID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_ReturnCommoditySheet_CheckReturnCommoditySheetCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode,@sErrorMsg;
-- 
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = concat('退货单:', @iID, '没有退货商品'), '测试成功', '测试失败') AS 'CASE2 RESULT';
-- 
DELETE FROM t_returncommoditysheet WHERE F_ID = @iID;
-- 