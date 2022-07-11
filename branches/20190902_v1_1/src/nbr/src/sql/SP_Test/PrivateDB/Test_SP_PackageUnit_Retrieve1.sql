SELECT '++++++++++++++++++ Test_SP_PackageUnit_Retrieve1.sql ++++++++++++++++++++';

INSERT INTO t_packageunit(F_Name)
VALUES('∫–1234');

SET @iID = last_insert_id();
SET @sErrorMsg = '';

CALL SP_PackageUnit_Retrieve1(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT 1 FROM t_packageunit WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'Case1 Testing Result';

DELETE FROM t_packageunit WHERE F_ID = @iID;