SELECT '++++++++++++++++++ Test_SP_BXConfigGeneral_Retrieve1.sql ++++++++++++++++++++';


INSERT INTO nbr_bx.t_bxconfiggeneral (F_Name, F_Value)
VALUES ("aaaaa", "100000000");
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = last_insert_id();

CALL SP_BXConfigGeneral_Retrieve1(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
DELETE FROM nbr_bx.t_bxconfiggeneral WHERE F_ID = @iID;

SELECT 1 FROM nbr_bx.t_bxconfiggeneral WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'Case1 Testing Result';