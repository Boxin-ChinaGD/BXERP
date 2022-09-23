-- case1:会员表中已有该会员的类别。删除不了，错误代码为7
SET @iID = 3;
SET @sErrorMsg = '';
SET @iErrorCode = 0;

CALL SP_VIPCategory_Delete(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT 1 FROM t_VIP_Category WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Testing Result';

-- case2:会员表中没有这种会员的类别。可以直接删除，错误代码为0
SET @sName = '白银会员1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';

INSERT INTO t_vip_category (F_Name)
VALUES (@sName);

SET @iID = LAST_INSERT_ID();

CALL SP_VIPCategory_Delete(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT 1 FROM t_VIP_Category WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';

SELECT * FROM t_vip_category WHERE F_ID = @iID;

-- case3:删除一个不存在的Id
SET @iID = -22;
SET @sErrorMsg = '';
SET @iErrorCode = 0;

CALL SP_VIPCategory_Delete(@iErrorCode, @sErrorMsg, @iID);
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';