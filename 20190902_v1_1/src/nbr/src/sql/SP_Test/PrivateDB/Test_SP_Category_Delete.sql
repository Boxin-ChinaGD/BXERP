SELECT '++++++++++++++++++Test_SP_Category_Delete.sql+++++++++++++++++++++++';

SELECT '-----------------Case1: 商品表中已有商品的类别。删除不了，错误代码为7------------------' AS 'Case1';

SET @iCategoryID = 2;
SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_Category_Delete(@iErrorCode, @sErrorMsg, @iCategoryID);

SELECT @sErrorMsg;
SELECT 1 FROM t_category WHERE F_ID = @iCategoryID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT '-----------------Case2: 商品表中没有这种类别的商品。可以直接删除，错误代码为0------------------' AS 'Case2';

SET @sName = '飞机';
SET @iParentID = 1;
SET @iErrorCode = 0;
SET @sErrorMsg = '';

INSERT INTO t_category (F_Name, F_ParentID)
VALUES (@sName, @iParentID);

SET @iID = LAST_INSERT_ID();

CALL SP_Category_Delete(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT * FROM t_category WHERE F_ID = @iID;

SELECT 1 FROM t_category WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';