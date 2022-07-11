SELECT '++++++++++++++++++ Test_SP_Permission_RetrieveAlsoRoleStaff.sql ++++++++++++++++++++';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 3;

CALL SP_Permission_RetrieveAlsoRoleStaff(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'Case1 Testing Result';