SELECT '++++++++++++++++++Test_SP_CategoryParent_Create.sql+++++++++++++++++++++++';

SELECT '-----------------Case1: 添加已有的大类,错误码应该为1------------------' AS 'Case1';

INSERT INTO t_categoryparent (F_Name)
VALUES ('制造品');
SET @iID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '制造品';

CALL SP_CategoryParent_Create(@iErrorCode, @sErrorMsg, @sName);

SELECT @sErrorMsg;
DELETE FROM t_categoryparent WHERE F_ID = last_insert_id();

SELECT 1 FROM t_categoryparent WHERE F_Name = @sName;
SELECT IF(found_rows() = 0 AND @iErrorCode = 1, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT '-----------------Case2: 添加一个没有大类，错误码为0------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '生活用品';

CALL SP_CategoryParent_Create(@iErrorCode, @sErrorMsg, @sName);

SELECT @sErrorMsg;
DELETE FROM t_categoryparent WHERE F_ID = last_insert_id();

SELECT 1 FROM t_categoryparent WHERE F_Name = @sName;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';