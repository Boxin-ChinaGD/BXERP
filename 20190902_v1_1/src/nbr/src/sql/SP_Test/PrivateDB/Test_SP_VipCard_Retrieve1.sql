SELECT '++++++++++++++++++ Test_SP_VipCard_Retrieve1.sql ++++++++++++++++++++';
INSERT INTO t_vipcard (F_Title, F_BackgroundColor, F_ClearBonusDay, F_ClearBonusDatetime, F_CreateDatetime)
VALUES ('ª·‘±ø®', '255,255,255;255,255,255', 3650, NULL, now());
SET @iID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_VipCard_Retrieve1(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT 1 FROM t_vipcard WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'Case1 Testing Result';

DELETE FROM t_vipcard WHERE F_ID = @iID;