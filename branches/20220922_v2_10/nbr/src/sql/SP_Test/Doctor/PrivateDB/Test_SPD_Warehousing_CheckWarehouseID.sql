SELECT '++++++++++++++++++ Test_SPD_Warehousing_CheckWarehouseID.sql ++++++++++++++++++++';
SELECT '------------------ ’˝≥£≤‚ ‘ --------------------' AS 'CASE1';
--
SET @iErrorCode = 0;
SET @sErrorMsg = '';
--
CALL SPD_Warehousing_CheckWarehouseID(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'Case1 Testing Result';
--
