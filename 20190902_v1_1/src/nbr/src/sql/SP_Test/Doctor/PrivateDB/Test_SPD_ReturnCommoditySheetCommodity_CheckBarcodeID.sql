SELECT '++++++++++++++++++ Test_SPD_ReturnCommoditySheet_CheckStaffID.sql ++++++++++++++++++++';
SELECT '------------------ ’˝≥£≤‚ ‘ --------------------' AS 'CASE1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_ReturnCommoditySheetCommodity_CheckBarcodeID(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode,@sErrorMsg;
-- 
SELECT IF(@iErrorCode = 0 AND @sErrorMsg = '', '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'CASE1 RESULT';
-- 