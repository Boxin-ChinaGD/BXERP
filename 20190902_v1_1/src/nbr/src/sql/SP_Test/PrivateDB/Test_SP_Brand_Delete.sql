SELECT '++++++++++++++++++Test_SP_Brand_Delete.sql+++++++++++++++++++++++';

SELECT '-----------------Case1: 品牌表中已有商品的品牌。删除不了，错误代码为7------------------' AS 'Case1';

SET @iID = 3;
SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_Brand_Delete(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT 1 FROM t_brand WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT '-----------------Case2: 品牌表中没有这种品牌的商品。可以直接删除，错误代码为0------------------' AS 'Case2';

SET @sName = '阿森';
SET @iErrorCode = 0;
SET @sErrorMsg = '';

INSERT INTO t_brand (F_Name)
VALUES (@sName);

SET @iID = LAST_INSERT_ID();

CALL SP_Brand_Delete(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT 1 FROM t_brand WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';