SELECT '++++++++++++++++++ Test_SP_InventoryCommodity_RetrieveN.sql ++++++++++++++++++++';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iInventorySheetID = 1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_InventoryCommodity_RetrieveN(@iErrorCode, @sErrorMsg, @iInventorySheetID,@iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT 1 FROM t_inventorycommodity WHERE F_InventorySheetID = @iInventorySheetID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'CASE1 Testing Result';