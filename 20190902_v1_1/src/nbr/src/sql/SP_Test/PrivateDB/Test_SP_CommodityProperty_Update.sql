SELECT '++++++++++++++++++ Test_SP_CommodityProperty_Update.sql ++++++++++++++++++++';
SET @sErrorMsg = '';
SET @sName1 = "1";
SET @sName2 = "";
SET @sName3 = "";
SET @sName4 = "";

CALL SP_CommodityProperty_Update(@iErrorCode, @sErrorMsg, @sName1, @sName2, @sName3, @sName4);

SELECT @sErrorMsg;
SELECT 1 FROM t_commodityproperty WHERE F_ID = 1 AND F_Name1 = @sName1 AND F_Name2 = @sName2 AND F_Name3 = @sName3 AND F_Name4 = @sName4;
SELECT IF(@iErrorCode = 0, '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'Case1 Testing Result';