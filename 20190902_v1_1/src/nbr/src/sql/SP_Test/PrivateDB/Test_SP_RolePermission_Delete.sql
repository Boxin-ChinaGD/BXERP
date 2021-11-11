SELECT '-----------------------------Test_SP_RolePermission_Delete.sql--------------------------------------------------';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 2;

CALL SP_RolePermission_Delete(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT 1 FROM t_role_permission WHERE F_RoleID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'Testing Result';

-- DELETE FROM t_role WHERE F_ID = last_insert_id();