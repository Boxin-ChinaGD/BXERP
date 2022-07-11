SELECT '++++++++++++++++++ Test_SP_Shop_Update.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:正常修改 -------------------------' AS 'Case1';
INSERT INTO t_shop (F_Name, F_BxStaffID, F_CompanyID, F_Address, F_DistrictID, F_Status, F_Longitude, F_Latitude, F_Key)
VALUES ('博昕小卖部33', 1, 1 ,'岗顶1', 1, 1, 12.12, 122.11, '123456');

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '博昕小卖部656556';
SET @sAddress = '岗顶2';
SET @iDistrictID = 2;
SET @fLongitude = 12.12;
SET @fLatitude = 122.11;
-- SET @sKey = '123456';
SET @sRemark = '本门店为第一门店';
SET @iID = LAST_INSERT_ID();

CALL SP_Shop_Update(@iErrorCode, @sErrorMsg, @iID, @sName, @sAddress, @iDistrictID, @fLongitude, @fLatitude, @sRemark);

SELECT @iErrorCode;
SELECT 1 FROM t_shop WHERE F_Name = @sName AND F_Address = @sAddress AND F_Longitude = @fLongitude AND F_DistrictID = @iDistrictID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0 AND @sErrorMsg = '','测试成功','测试失败') AS 'Case1 Testing Result';

DELETE FROM t_shop WHERE F_ID = @iID;

SELECT '-------------------- Case2:修改成重复的门店名称 -------------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '博昕家用店';
SET @sAddress = '岗顶1';
SET @iDistrictID = 1;
SET @fLongitude = 12.12;
SET @fLatitude = 122.11;
SET @sRemark = '本门店为第二门店';
-- SET @sKey = '123456';
SET @iID = 3;

CALL SP_Shop_Update(@iErrorCode, @sErrorMsg, @iID, @sName, @sAddress, @iDistrictID, @fLongitude, @fLatitude, @sRemark);

SELECT IF(@iErrorCode = 1 AND @sErrorMsg = '门店名称重复','测试成功','测试失败') AS 'Case2 Testing Result';

SELECT '-------------------- Case3:修改成不存在的门店区域 -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '博昕小卖部1';
SET @sAddress = '岗顶1';
SET @iDistrictID = -1;
SET @fLongitude = 12.12;
SET @fLatitude = 122.11;
SET @sRemark = '本门店为第二门店';
-- SET @sKey = '123456';
SET @iID = 3;

CALL SP_Shop_Update(@iErrorCode, @sErrorMsg, @iID, @sName, @sAddress, @iDistrictID, @fLongitude, @fLatitude, @sRemark);

SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '不能修改成不存在的门店区域','测试成功','测试失败') AS 'Case3 Testing Result'; 