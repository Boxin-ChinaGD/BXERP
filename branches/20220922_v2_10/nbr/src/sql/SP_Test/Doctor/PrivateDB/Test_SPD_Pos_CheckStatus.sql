SELECT '++++++++++++++++++ Test_SPD_Pos_CheckStatus.sql ++++++++++++++++++++';
SELECT '------------------ 正常测试 --------------------' AS 'CASE1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Pos_CheckStatus(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';
-- 
SELECT '------------------ 状态异常 --------------------' AS 'CASE2';
-- 
INSERT INTO T_POS (F_POS_SN,F_ShopID,F_Salt,F_Status,F_PasswordInPOS) VALUES ('TM18185S00096',1,'B1AFC07474C37C5AEC4199ED28E09705',9999999999,'000000');
SET @iID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Pos_CheckStatus(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iID ,'的POS机的状态异常') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case2 Testing Result';
DELETE FROM t_pos WHERE F_ID = @iID;
-- 