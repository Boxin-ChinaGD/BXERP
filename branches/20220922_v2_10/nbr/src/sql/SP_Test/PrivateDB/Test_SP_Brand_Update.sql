SELECT '++++++++++++++++++Test_SP_Brand_Update.sql+++++++++++++++++++++++';

SELECT '-----------------Case1: 品牌名称重复，错误码应该是1------------------' AS 'Case1';

SET @sErrorMsg = '';
SET @sName1 = '上好佳1';
INSERT INTO T_Brand(F_Name) VALUES(@sName1);
SET @iID1 = LAST_INSERT_ID();

SET @sName2 = '上好佳2';
INSERT INTO T_Brand(F_Name) VALUES(@sName2);
SET @iID2 = LAST_INSERT_ID();

CALL SP_Brand_Update(@iErrorCode, @sErrorMsg, @iID1, @sName2);
DELETE FROM t_brand WHERE F_ID = @iID;

SELECT @iErrorCode;
SELECT @sErrorMsg;
DELETE FROM t_brand WHERE F_ID = @iID1;
DELETE FROM t_brand WHERE F_ID = @iID2;

SELECT 1 FROM t_brand WHERE F_ID = @iID1 AND F_Name = @sName2;
SELECT IF(found_rows() = 0 AND @iErrorCode = 1, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT '-----------------Case2: 品牌名称不重复，错误码应该是0------------------' AS 'Case2';

SET @sErrorMsg = '';
SET @sName3 = '未来星';
INSERT INTO T_Brand(F_Name) VALUES(@sName3);
SET @iID3 = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sName4 = '探探';

CALL SP_Brand_Update(@iErrorCode, @sErrorMsg, @iID3, @sName4);

SELECT @sErrorMsg;
DELETE FROM t_brand WHERE F_ID = @iID3;

SELECT 1 FROM t_brand WHERE F_ID = @iID3 AND F_Name = @sName4;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';