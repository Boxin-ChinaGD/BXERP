SELECT '++++++++++++++++++ SPD_Warehousing_CheckStaffID.sql ++++++++++++++++++++';
SELECT '------------------ ’˝≥£≤‚ ‘ --------------------' AS 'CASE1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Warehousing_CheckStaffID(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'Case1 Testing Result';
--