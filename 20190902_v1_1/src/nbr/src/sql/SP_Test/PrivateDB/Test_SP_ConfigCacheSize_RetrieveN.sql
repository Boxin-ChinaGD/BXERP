SELECT '++++++++++++++++++ Test_SP_ConfigCacheSize_RetrieveN.sql ++++++++++++++++++++';

INSERT INTO t_configcachesize (F_Name, F_Value)
VALUES ("aaaaa", "100000000");

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iTotalRecord = 0;

CALL SP_ConfigCacheSize_RetrieveN(@iErrorCode, @sErrorMsg, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'Case1 Testing Result';

DELETE FROM t_configcachesize WHERE F_ID = LAST_INSERT_ID();