SELECT '++++++++++++++++++Test_SP_Category_Update.sql+++++++++++++++++++++++';

SELECT '-----------------Case1: 类别名称重复，错误码应该是1------------------' AS 'Case1';

SET @sName2 = '香水2';
SET @iParentID = 1;
INSERT INTO T_Category(F_Name, F_ParentID) VALUES(@sName2, @iParentID);
SET @iID2 = LAST_INSERT_ID();

SET @sName3 = '香水3';
INSERT INTO T_Category(F_Name, F_ParentID) VALUES(@sName3, @iParentID);
SET @iID3 = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_Category_Update(@iErrorCode, @sErrorMsg, @iID2, @sName3, @iParentID);
SELECT @sErrorMsg;

DELETE FROM t_category WHERE F_ID = @iID2;
DELETE FROM t_category WHERE F_ID = @iID3;

SELECT 1 FROM t_category WHERE F_ID = @iID2 AND F_Name = @sName3;
SELECT IF(found_rows() = 0 AND @iErrorCode = 1, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT '-----------------Case2: 类别名称不重复，错误码应该是0------------------' AS 'Case2';

SET @sName4 = '香水4';
INSERT INTO T_Category(F_Name, F_ParentID) VALUES(@sName4, @iParentID);
SET @iID4 = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName5 = '香水5';

CALL SP_Category_Update(@iErrorCode, @sErrorMsg, @iID4, @sName5, @iParentID);

SELECT @sErrorMsg;
DELETE FROM t_category WHERE F_ID = @iID4;

SELECT 1 FROM t_category WHERE F_ID = @iID4 AND F_Name = @sName5;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';

SELECT '-----------------Case3: 只修改小类的大类，错误码应该是0------------------' AS 'Case3';

SET @sName = '默认分类';
SET @iID = 1;
SET @iParentID = 2;
SET @sErrorMsg = '';

CALL SP_Category_Update(@iErrorCode, @sErrorMsg, @iID, @sName, @iParentID);

SELECT @sErrorMsg;
SELECT 1 FROM t_category WHERE F_ID = @iID AND F_Name = @sName;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case3 Testing Result';

SELECT '-----------------Case4: 修改成不存在的大类------------------' AS 'Case4';
SET @sName = '默认分类';
SET @iID = 3;
SET @iParentID = -999;
SET @sErrorMsg = '';

CALL SP_Category_Update(@iErrorCode, @sErrorMsg, @iID, @sName, @iParentID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = "该商品大类不存在", '测试成功', '测试失败') AS 'Case4 Testing Result';