SELECT '++++++++++++++++++ Test_SPD_Warehousing_CheckProviderID.sql ++++++++++++++++++++';
SELECT '------------------ ’˝≥£≤‚ ‘ --------------------' AS 'CASE1';

SET @iErrorCode = 0;
SET @iErrorMsg = '';
--
CALL SPD_Warehousing_CheckProviderID(@iErrorCode, @iErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
--
SELECT IF(@iErrorCode = 0 AND @iErrorMsg = '', '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'CASE1 TEST RESULT';
--