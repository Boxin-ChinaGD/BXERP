-- 添加不重复的会员类别，错误码为0
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '青铜会员';

CALL SP_VIPCategory_Create(@iErrorCode, @sErrorMsg, @sName);

SELECT 1 FROM t_VIP_Category WHERE F_Name = @sName;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';

SELECT @sErrorMsg;
DELETE FROM t_vip_category WHERE F_ID = last_insert_id();
SELECT @iErrorCode;

-- 添加会员类别，不能添加，错误码为1
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '黄金会员';

CALL SP_VIPCategory_Create(@iErrorCode, @sErrorMsg, @sName);

SELECT @sErrorMsg;
SELECT 1 FROM t_VIP_Category WHERE F_Name = @sName;
SELECT IF(found_rows() = 1 AND @iErrorCode = 1, '测试成功', '测试失败') AS 'Testing Result';