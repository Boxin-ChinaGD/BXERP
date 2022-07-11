SELECT '++++++++++++++++++Test_SP_CategoryParent_Update.sql+++++++++++++++++++++++';

SELECT '-----------------Case1: 已有重复的名字，不可进行修改,错误码应该为1------------------' AS 'Case1';

SET @sName = '香水2';
INSERT INTO T_CategoryParent(F_Name) VALUES(@sName);
SET @iID1 = LAST_INSERT_ID();

SET @sName2 = '香水3';
INSERT INTO T_CategoryParent(F_Name) VALUES(@sName2);
SET @iID2 = LAST_INSERT_ID();

SET @iErrorcode = 0;
SET @sErrorMsg = '';
SET @sName3 = '香水2';

CALL SP_CategoryParent_Update(@iErrorcode, @sErrorMsg, @iID2, @sName3);

SELECT @sErrorMsg;
DELETE FROM T_CategoryParent WHERE F_ID = @iID1;
DELETE FROM T_CategoryParent WHERE F_ID = @iID2;

SELECT 1 FROM t_category WHERE F_ID = @iID2 AND F_Name = @sName2;
SELECT IF(found_rows() = 0 AND @iErrorCode = 1, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT '-----------------Case2: 还没重复的名字，可进行修改，错误应该为0------------------' AS 'Case2';

SET @sName5 = '香水5';
INSERT INTO T_CategoryParent(F_Name) VALUES(@sName5);
SET @iID5 = LAST_INSERT_ID();

SET @iErrorcode = 0;
SET @sErrorMsg = '';
SET @sName6 = '香水6';
CALL SP_CategoryParent_Update(@iErrorcode, @sErrorMsg, @iID5, @sName6);

SELECT @sErrorMsg;
DELETE FROM T_CategoryParent WHERE F_ID = @iID5;

SELECT 1 FROM t_category WHERE F_ID = @iID5 AND F_Name = @sName6;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';