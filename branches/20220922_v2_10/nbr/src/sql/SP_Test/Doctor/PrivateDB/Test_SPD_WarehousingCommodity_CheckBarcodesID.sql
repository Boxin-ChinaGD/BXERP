SELECT '++++++++++++++++++ Test_SPD_WarehousingCommodity_CheckBarcodesID.sql ++++++++++++++++++++';
SELECT '------------------ ’˝≥£≤‚ ‘ --------------------' AS 'CASE1';
--
SET @iErrorCode = 0;
SET @sErrorMsg = '';
--
CALL SPD_WarehousingCommodity_CheckBarcodesID(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
--
SELECT IF(@iErrorCode = 0 AND @sErrorMsg = '', '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'Case1 Testing Result';
--
