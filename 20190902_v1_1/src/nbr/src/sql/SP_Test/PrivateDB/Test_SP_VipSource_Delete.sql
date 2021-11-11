SELECT '++++++++++++++++++ Test_SP_VipSource_Delete.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:’˝≥£…æ≥˝ -------------------------' AS 'Case1';
INSERT INTO t_vipsource (F_VipID, F_SourceCode, F_ID1, F_ID2, F_ID3)
VALUES (2, 11, '1111111111111111', '222222222222222222', '33333333333333333');
SET @iID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_VipSource_Delete(@iErrorCode, @sErrorMsg, @iID);
-- 
SELECT @sErrorMsg;
SELECT 1 FROM t_vipsource WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'Case1 Testing Result';