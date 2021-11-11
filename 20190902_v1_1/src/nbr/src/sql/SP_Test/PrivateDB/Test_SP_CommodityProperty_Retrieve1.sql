SELECT '++++++++++++++++++ Test_SP_CommodityProperty_Retrieve1.sql ++++++++++++++++++++';

SET @sErrorMsg = '';
SET @iID = 1;

CALL SP_CommodityProperty_Retrieve1(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT 1 FROM t_commodityproperty WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'Case1 Testing Result';