SELECT '++++++++++++++++++ Test_SP_ConfigCacheSize_Update.sql ++++++++++++++++++++';

INSERT INTO t_configcachesize (F_Name, F_Value)
VALUES ("aaaaa", "100000000");

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = last_insert_id();
SET @sValue = "2222222222";

CALL SP_ConfigCacheSize_Update(@iErrorCode, @sErrorMsg, @iID, @sValue);

DELETE FROM t_configcachesize WHERE F_ID = @iID AND F_Value = @sValue;
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_configcachesize WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'Case1 Testing Result';