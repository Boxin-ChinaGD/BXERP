SELECT '++++++++++++++++++ Test_SP_Barcodes_Retrieve1.sql ++++++++++++++++++++';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 7;

CALL SP_Barcodes_Retrieve1(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT 1 FROM t_barcodes WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'Case1 Testing Result';