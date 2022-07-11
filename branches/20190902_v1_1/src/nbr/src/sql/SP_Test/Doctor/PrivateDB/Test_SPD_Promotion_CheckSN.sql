SELECT '++++++++++++++++++ Test_SPD_Promotion_CheckSN.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:正常测试 -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Promotion_CheckSN(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';



SELECT '-------------------- Case2:促销编号格式不以CX开头-------------------------' AS 'Case2';

INSERT INTO t_promotion (F_SN, F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES ('CL201907220001', '指定商品1满减活动满20-5', 0, 0, '2019-07-22 11:32:09', '2021-07-22 11:32:09', 20, 5, NULL, 1, 4, '2019-07-22 11:32:09', '2019-07-22 11:32:09');

SET @iID = LAST_INSERT_ID();


SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Promotion_CheckSN(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
--  
SELECT IF(@sErrorMsg = CONCAT('促销编号', @iID, '格式不正确') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case2 Testing Result';
DELETE FROM t_promotion WHERE F_ID = @iID;


SELECT '-------------------- Case3:促销编号长度小于14位字符-------------------------' AS 'Case3';

INSERT INTO t_promotion (F_SN, F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES ('Cx20190722000', '指定商品1满减活动满20-5', 0, 0, '2019-07-22 11:32:09', '2021-07-22 11:32:09', 20, 5, NULL, 1, 4, '2019-07-22 11:32:09', '2019-07-22 11:32:09');

SET @iID = LAST_INSERT_ID();


SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Promotion_CheckSN(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
--  
SELECT IF(@sErrorMsg = CONCAT('促销编号', @iID, '的长度不能小于14位字符') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case3 Testing Result';
DELETE FROM t_promotion WHERE F_ID = @iID;