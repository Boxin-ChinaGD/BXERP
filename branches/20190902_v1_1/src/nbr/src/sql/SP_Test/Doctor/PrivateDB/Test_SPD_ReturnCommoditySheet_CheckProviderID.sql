SELECT '++++++++++++++++++ Test_SPD_ReturnCommoditySheet_CheckProviderID.sql ++++++++++++++++++++';
SELECT '------------------ ’˝≥£≤‚ ‘ --------------------' AS 'CASE1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_ReturnCommoditySheet_CheckProviderID(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@iErrorCode = 0 AND @sErrorMsg = '', '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'CASE1 RESULT';
-- 
