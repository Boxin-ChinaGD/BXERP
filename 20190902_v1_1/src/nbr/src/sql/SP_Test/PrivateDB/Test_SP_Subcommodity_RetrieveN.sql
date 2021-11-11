SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 49;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Subcommodity_RetrieveN(@iErrorCode, @sErrorMsg, @iCommodityID, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iErrorCode;
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'Testing Result';