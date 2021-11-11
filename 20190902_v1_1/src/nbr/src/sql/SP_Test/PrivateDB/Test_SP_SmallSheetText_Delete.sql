SELECT '++++++++++++++++++ Test_SP_SmallSheetText_Delete.sql ++++++++++++++++++++';
INSERT INTO t_smallsheettext (F_Content, F_Size, F_Bold, F_Gravity, F_FrameID)
VALUES ('ª∂”≠π‚¡Ÿ', 12, 0, 128, 4);
SET @iID = last_insert_id();
SET @iFrameID = 4;
SET @sErrorMsg = '';
SET @iErrorCode = 0;

CALL SP_SmallSheetText_Delete(@iErrorCode, @sErrorMsg, @iFrameID);

SELECT @sErrorMsg;
SELECT 1 FROM t_smallsheettext WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'Testing Result';

DELETE FROM t_smallsheettext WHERE F_FrameID = @iFrameID;