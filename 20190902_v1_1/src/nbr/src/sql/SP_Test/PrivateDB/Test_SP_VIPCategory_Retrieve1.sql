INSERT INTO t_vip_category (F_Name) VALUES ('蓝砖会员');

-- CASE1:用ID查询
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();

CALL SP_VIPCategory_Retrieve1(@iErrorCode, @sErrorMsg, @iID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_vip_category WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';

DELETE FROM t_vip_category WHERE F_ID = LAST_INSERT_ID();

-- CASE2:用不存在的ID查询
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = -22;

CALL SP_VIPCategory_Retrieve1(@iErrorCode, @sErrorMsg, @iID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_vip_category WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';