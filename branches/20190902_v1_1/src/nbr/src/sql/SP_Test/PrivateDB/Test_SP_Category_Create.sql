SELECT '++++++++++++++++++Test_SP_Category_Create.sql+++++++++++++++++++++++';

SELECT '-----------------Case1: 添加类别------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '腌制腊肉';
SET @iParentID = 1;

CALL SP_Category_Create(@iErrorCode, @sErrorMsg, @sName, @iParentID);

DELETE FROM t_category WHERE F_ID = last_insert_id();

SELECT @sErrorMsg;
SELECT 1 FROM t_category WHERE F_Name = @sName;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT '-----------------Case2: 添加重复类别------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '饮料';
SET @iParentID = 1;

CALL SP_Category_Create(@iErrorCode, @sErrorMsg, @sName, @iParentID);

SELECT @sErrorMsg;
SELECT 1 FROM t_category WHERE F_Name = @sName;
SELECT IF(found_rows() = 1 AND @iErrorCode = 1, '测试成功', '测试失败') AS 'Case2 Testing Result';

DELETE FROM t_category WHERE F_ID = last_insert_id();

SELECT '-----------------Case3: 添加不存在的类别------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '饮料99999';
SET @iParentID = -999;

CALL SP_Category_Create(@iErrorCode, @sErrorMsg, @sName, @iParentID);

SELECT @iErrorCode; 
SELECT @sErrorMsg;
SELECT IF( @iErrorCode = 7, '测试成功', '测试失败') AS 'Case3 Testing Result';