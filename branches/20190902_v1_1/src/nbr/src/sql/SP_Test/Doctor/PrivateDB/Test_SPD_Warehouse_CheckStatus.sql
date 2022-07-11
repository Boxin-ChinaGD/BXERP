SELECT '++++++++++++++++++ Test_SPD_Warehouse_CheckStatus.sql ++++++++++++++++++++';
SELECT '------------------ 正常测试 --------------------' AS 'CASE1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Warehouse_CheckStatus(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';
-- 
SELECT '------------------ 状态异常 --------------------' AS 'CASE2';
-- 
INSERT INTO t_warehouse (F_Name, F_Address, F_Status, F_StaffID, F_Phone, F_CreateDatetime, F_UpdateDatetime)
VALUES ('异常的仓库','植物园',2,NULL,'',now(),now());
SET @iID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Warehouse_CheckStatus(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iID ,'的仓库的状态异常') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case2 Testing Result';
DELETE FROM t_warehouse WHERE F_ID = @iID;
-- 