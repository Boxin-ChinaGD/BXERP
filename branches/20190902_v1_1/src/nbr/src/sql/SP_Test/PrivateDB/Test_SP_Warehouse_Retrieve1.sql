SELECT '++++++++++++++++++ Test_SP_Warehouse_Retrieve1.sql ++++++++++++++++++++';

INSERT INTO t_warehouse (F_Name, F_Address, F_Status, F_StaffID, F_Phone)
VALUES ('≤÷ø‚9099', '÷≤ŒÔ‘∞', 0, 1, '13246');

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID =LAST_INSERT_ID();


CALL SP_Warehouse_Retrieve1(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT 1 FROM t_Warehouse WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'Testing Result';

DELETE FROM t_warehouse WHERE F_ID = @iID;