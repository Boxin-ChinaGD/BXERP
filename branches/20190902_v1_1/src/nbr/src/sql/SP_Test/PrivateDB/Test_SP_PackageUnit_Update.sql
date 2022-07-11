SELECT '++++++++++++++++++ Test_SP_PackageUnit_Update.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: 包装单位重复，错误码应该是1 -------------------------' AS 'Case1';

SET @sName1 = '盒32';
INSERT INTO t_PackageUnit(F_Name) VALUES(@sName1);
SET @iID1 = LAST_INSERT_ID();

SET @sName2 = '盒';
SET @sErrorMsg = '';

CALL SP_PackageUnit_Update(@iErrorCode, @sErrorMsg, @iID1, @sName2);
DELETE FROM t_PackageUnit WHERE F_ID = @iID;

DELETE FROM t_PackageUnit WHERE F_ID = @iID1;
SELECT @sErrorMsg;
SELECT 1 FROM t_PackageUnit WHERE F_ID = @iID1 AND F_Name = @sName2;
SELECT IF(found_rows() = 0 AND @iErrorCode = 1, '测试成功', '测试失败') AS 'Case1 Testing Result';


SELECT '-------------------- Case2: 包装单位不重复，错误码应该是0 -------------------------' AS 'Case2';

SET @sName3 = '盒121';
INSERT INTO t_PackageUnit(F_Name) VALUES(@sName3);
SET @iID3 = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sName4 = '瓶1231';
SET @sErrorMsg = '';

call SP_PackageUnit_Update(@iErrorCode, @sErrorMsg, @iID3, @sName4);

DELETE FROM t_PackageUnit WHERE F_ID = @iID3;
SELECT @sErrorMsg;
SELECT 1 FROM t_PackageUnit WHERE F_ID = @iID3 AND F_Name = @sName4;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';