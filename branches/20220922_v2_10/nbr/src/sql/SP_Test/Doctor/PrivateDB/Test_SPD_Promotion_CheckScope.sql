SELECT '++++++++++++++++++ Test_SPD_Promotion_CheckScope.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:正常测试 -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Promotion_CheckScope(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';




SELECT '-------------------- Case2:促销范围只能是有效状态0，跟已删除状态1-------------------------' AS 'Case2';

INSERT INTO t_promotion (F_SN, F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES ('CX201907220050', '指定商品1满减活动满20-5', 0, 0, '2019-07-22 11:32:09', '2021-07-22 11:32:09', 20, 5, NULL, 2, 4, '2019-07-22 11:32:09', '2019-07-22 11:32:09');

SET @iID = LAST_INSERT_ID();


SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Promotion_CheckScope(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
--  
SELECT IF(@sErrorMsg = CONCAT('促销范围', @iID, '只能是全场商品0,或指定商品1') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case2 Testing Result';
DELETE FROM t_promotion WHERE F_ID = @iID;