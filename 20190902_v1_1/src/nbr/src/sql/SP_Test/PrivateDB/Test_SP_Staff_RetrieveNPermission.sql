SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Staff_RetrieveNPermission(@iErrorCode, @sErrorMsg, @iPageIndex, @iPageSize, @iTotalRecord);
	
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT @iTotalRecord;

SELECT IF(@iErrorCode = 0, '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'Testing Result';