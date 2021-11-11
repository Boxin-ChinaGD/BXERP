SELECT '++++++++++++++++++ Test_SPD_Shop_CheckStatus.sql ++++++++++++++++++++';
SELECT '------------------ 正常测试 --------------------' AS 'CASE1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Shop_CheckStatus(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';
-- 
SELECT '------------------ 状态异常 --------------------' AS 'CASE2';
-- 
INSERT INTO T_Shop (F_Name, F_CompanyID, F_Address, F_Status, F_Longitude, F_Latitude, F_Key, F_BXStaffID, F_DistrictID)
VALUES ('博昕杂粮', 1, '佛山', 999999999, 123.12, 231.21, '12345678901234567890123456789012', 1, 5);
SET @iID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Shop_CheckStatus(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iID ,'的门店的状态异常') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case2 Testing Result';
DELETE FROM t_shop WHERE F_ID = @iID;
-- 