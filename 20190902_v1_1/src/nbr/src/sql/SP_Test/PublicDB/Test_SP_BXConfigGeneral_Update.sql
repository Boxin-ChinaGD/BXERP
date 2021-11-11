SELECT '++++++++++++++++++ Test_SP_bxconfiggeneral_Update.sql ++++++++++++++++++++';

INSERT INTO nbr_bx.t_bxconfiggeneral (F_Name, F_Value)
VALUES ("aaaaa", "100000000");

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();
SET @sValue = "2222222222";

CALL SP_bxconfiggeneral_Update(@iErrorCode, @sErrorMsg, @iID, @sValue);

DELETE FROM nbr_bx.t_bxconfiggeneral WHERE F_ID = @iID;
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM nbr_bx.t_bxconfiggeneral WHERE F_ID = @iID AND F_Value = @sValue;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'Case1 Testing Result';