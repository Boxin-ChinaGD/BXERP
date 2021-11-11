-- case 1:会员类别名称重复，错误码应该是1
SET @sName1 = '黄金会员1';
INSERT INTO t_vip_category(F_Name) VALUES(@sName1);
SET @iID1 = LAST_INSERT_ID();

SET @sName2 = '黄金会员';
INSERT INTO t_vip_category(F_Name) VALUES(@sName2);
SET @iID2 = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_VIPCategory_Update(@iErrorCode, @sErrorMsg, @iID1, @sName2);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_vip_category WHERE F_ID = @iID1 AND F_Name = @sName2;
SELECT IF(found_rows() = 0 AND @iErrorCode = 1, '测试成功', '测试失败') AS 'Testing Result';

DELETE FROM t_vip_category WHERE F_ID = @iID1;
DELETE FROM t_vip_category WHERE F_ID = @iID2;


-- case 2: 会员类别名称不重复，错误码应该是0
SET @sName3 = '钻石会员';
INSERT INTO t_vip_category(F_Name) VALUES(@sName3);
SET @iID3 = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName4 = '白银会员';

call SP_VIPCategory_Update(@iErrorCode, @sErrorMsg, @iID3, @sName4);
SELECT @sErrorMsg;
SELECT 1 FROM t_vip_category WHERE F_ID = @iID3 AND F_Name = @sName4;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';

DELETE FROM t_vip_category WHERE F_ID = @iID3;