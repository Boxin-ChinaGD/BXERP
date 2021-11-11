SELECT '++++++++++++++++++ Test_SP_ConfigGeneral_RetrieveN.sql ++++++++++++++++++++';

INSERT INTO nbr_bx.t_bxconfiggeneral (F_Name, F_Value)
VALUES ("aaaaa", "100000000");
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iTotalRecord = 0;

CALL SP_BXConfigGeneral_RetrieveN(@iErrorCode, @sErrorMsg, @iTotalRecord);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'Case1 Testing Result';

DELETE FROM nbr_bx.t_bxconfiggeneral WHERE F_ID = LAST_INSERT_ID();