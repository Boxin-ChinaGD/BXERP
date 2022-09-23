SELECT '++++++++++++++++++ Test_SP_Permission_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: 添加不重复的权限 -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sSP = 'SP_VIP_Delete';
SET @sName = '删除cz';
SET @sDomain = '会员档案';
SET @sRemark = '会员的创建';

CALL SP_Permission_Create(@iErrorCode, @sErrorMsg, @sSP, @sName, @sDomain, @sRemark);

SELECT @sErrorMsg;
SELECT 1 FROM t_Permission WHERE F_SP = @sSP AND F_Name = @sName AND F_Domain = @sDomain AND F_Remark = @sRemark;
SELECT @iErrorCode;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';

DELETE FROM t_Permission WHERE F_ID = last_insert_id();

SELECT '-------------------- Case2: 添加重复的权限 -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sSP = 'SP_VIP_Delete';
SET @sName = '添加条形码';
SET @sDomain = '会员档案';
SET @sRemark = '会员的创建';

CALL SP_Permission_Create(@iErrorCode, @sErrorMsg, @sSP, @sName, @sDomain, @sRemark);

SELECT @sErrorMsg;
SELECT 1 FROM t_Permission  WHERE F_SP = @sSP AND F_Name = @sName AND F_Domain = @sDomain AND F_Remark = @sRemark;
SELECT IF(found_rows() = 0 AND @iErrorCode = 1, '测试成功', '测试失败') AS 'Case1 Testing Result';

DELETE FROM t_Permission WHERE F_ID = last_insert_id();