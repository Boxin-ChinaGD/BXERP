SELECT '++++++++++++++++++ Test_SP_Shop_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:正常添加 -------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '博昕小卖部1';
SET @iCompanyID = 1;
SET @sAddress = '岗顶1';
SET @sStatus = 1;
SET @fLongitude = 12.12;
SET @fLatitude = 122.11;
SET @sKey = '123456';
SET @sRemark = '';
SET @iBxStaffID = 1;
SET @iDistrictID = 1;

CALL SP_Shop_Create(@iErrorCode, @sErrorMsg, @sName, @iCompanyID, @sAddress, @iDistrictID, @sStatus, @fLongitude, @fLatitude, @sKey, @sRemark, @iBxStaffID);

SELECT 1 FROM t_shop WHERE F_Name = @sName AND F_Address = @sAddress AND F_Status = @sStatus AND F_Longitude = @fLongitude AND F_Key = @sKey;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0 AND @sErrorMsg = '','测试成功','测试失败') AS 'Case1 Testing Result';

DELETE FROM t_shop WHERE F_ID = last_insert_id();

SELECT '-------------------- Case2:添加重复公司名称的重复名称门店 -------------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '博昕小卖部';
SET @iCompanyID = 1;
SET @sAddress = '岗顶1';
SET @sStatus = 1;
SET @fLongitude = 12.12;
SET @fLatitude = 122.11;
SET @sKey = '123456';
SET @sRemark = '';
SET @iBxStaffID = 1;
SET @iDistrictID = 1;

CALL SP_Shop_Create(@iErrorCode, @sErrorMsg, @sName, @iCompanyID, @sAddress, @iDistrictID, @sStatus, @fLongitude, @fLatitude, @sKey, @sRemark, @iBxStaffID);

SELECT 1 FROM t_shop WHERE F_Name = @sName;
SELECT IF(found_rows() = 1 AND @iErrorCode = 1 AND @sErrorMsg = '该门店名称已存在','测试成功','测试失败') AS 'Case2 Testing Result';

SELECT '-------------------- Case3:添加不存在的公司的门店 -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '博昕小卖部';
SET @iCompanyID = -1;
SET @sAddress = '岗顶1';
SET @sStatus = 1;
SET @fLongitude = 12.12;
SET @fLatitude = 122.11;
SET @sKey = '123456';
SET @sRemark = '';
SET @iBxStaffID = 1;
SET @iDistrictID = 1;

CALL SP_Shop_Create(@iErrorCode, @sErrorMsg, @sName, @iCompanyID, @sAddress, @iDistrictID, @sStatus, @fLongitude, @fLatitude, @sKey, @sRemark, @iBxStaffID);

SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '查无该公司','测试成功','测试失败') AS 'Case3 Testing Result';

SELECT '-------------------- Case4:用不存在的业务经理进行创建 -------------------------' AS 'Case4';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '博昕小卖部';
SET @iCompanyID = 1;
SET @sAddress = '岗顶1';
SET @sStatus = 1;
SET @fLongitude = 12.12;
SET @fLatitude = 122.11;
SET @sKey = '123456';
SET @sRemark = '';
SET @iBxStaffID = -99999;
SET @iDistrictID = 1;

CALL SP_Shop_Create(@iErrorCode, @sErrorMsg, @sName, @iCompanyID, @sAddress, @iDistrictID, @sStatus, @fLongitude, @fLatitude, @sKey, @sRemark, @iBxStaffID);

SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '查无该业务经理','测试成功','测试失败') AS 'Case4 Testing Result';

SELECT '-------------------- Case5:用不存在的门店区域进行创建 -------------------------' AS 'Case5';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '博昕小卖部1';
SET @iCompanyID = 1;
SET @sAddress = '岗顶1';
SET @sStatus = 1;
SET @fLongitude = 12.12;
SET @fLatitude = 122.11;
SET @sKey = '123456';
SET @sRemark = '';
SET @iBxStaffID = 1;
SET @iDistrictID = -1;

CALL SP_Shop_Create(@iErrorCode, @sErrorMsg, @sName, @iCompanyID, @sAddress, @iDistrictID, @sStatus, @fLongitude, @fLatitude, @sKey, @sRemark, @iBxStaffID);

SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '查无该门店区域','测试成功','测试失败') AS 'Case5 Testing Result';

