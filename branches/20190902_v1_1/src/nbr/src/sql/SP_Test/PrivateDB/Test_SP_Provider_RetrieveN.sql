SELECT '++++++++++++++++++ Test_SP_Provider_RetrieveN.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: 输入sName作为条件查询 -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '华南';
SET @iDistrictID = -1;
SET @sAddress = '';
SET @sContactName = '';
SET @sMobile ='';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Provider_RetrieveN(@iErrorCode, @sErrorMsg, @sName, @iDistrictID, @sAddress, @sContactName, @sMobile, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @sErrorMsg;
SELECT 1 FROM t_Provider WHERE F_DistrictID = @iDistrictID AND F_Address = @sAddress AND F_ContactName = @sContactName AND F_Mobile = @sMobile;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT @iTotalRecord;

SELECT '-------------------- Case2: 输入iDistrictID作为条件查询 -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '';
SET @iDistrictID = 1;
SET @sAddress = '';
SET @sContactName = '';
SET @sMobile ='';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Provider_RetrieveN(@iErrorCode, @sErrorMsg, @sName, @iDistrictID, @sAddress, @sContactName, @sMobile, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @sErrorMsg;
SELECT 1 FROM t_Provider WHERE F_DistrictID = @iDistrictID AND F_Address = @sAddress AND F_ContactName = @sContactName AND F_Mobile = @sMobile;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';
SELECT @iTotalRecord;

SELECT '-------------------- Case3: 输入sAddress作为条件查询 -------------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '';
SET @iDistrictID = -1;
SET @sAddress = '广州';
SET @sContactName = '';
SET @sMobile ='';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Provider_RetrieveN(@iErrorCode, @sErrorMsg, @sName, @iDistrictID, @sAddress, @sContactName, @sMobile, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @sErrorMsg;
SELECT 1 FROM t_Provider WHERE F_DistrictID = @iDistrictID AND F_Address = @sAddress AND F_ContactName = @sContactName AND F_Mobile = @sMobile;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case3 Testing Result';

SELECT @iTotalRecord;

SELECT '-------------------- Case4: 输入sContactName作为条件查询 -------------------------' AS 'Case4';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '';
SET @iDistrictID = -1;
SET @sAddress = '';
SET @sContactName = 'Tom';
SET @sMobile ='';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Provider_RetrieveN(@iErrorCode, @sErrorMsg, @sName, @iDistrictID, @sAddress, @sContactName, @sMobile, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @sErrorMsg;
SELECT 1 FROM t_Provider WHERE F_DistrictID = @iDistrictID AND F_Address = @sAddress AND F_ContactName = @sContactName AND F_Mobile = @sMobile;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case4 Testing Result';
SELECT @iTotalRecord;

SELECT '-------------------- Case5: 输入sMobile作为条件查询 -------------------------' AS 'Case5';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '';
SET @iDistrictID = -1;
SET @sAddress = '';
SET @sContactName = '';
SET @sMobile ='13129355444';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Provider_RetrieveN(@iErrorCode, @sErrorMsg, @sName, @iDistrictID, @sAddress, @sContactName, @sMobile, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @sErrorMsg;
SELECT 1 FROM t_Provider WHERE F_DistrictID = @iDistrictID AND F_Address = @sAddress AND F_ContactName = @sContactName AND F_Mobile = @sMobile;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case5 Testing Result';
SELECT @iTotalRecord;

SELECT '-------------------- Case6: 不传任何值的情况 -------------------------' AS 'Case6';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '';
SET @iDistrictID = -1;
SET @sAddress = '';
SET @sContactName = '';
SET @sMobile ='';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Provider_RetrieveN(@iErrorCode, @sErrorMsg, @sName, @iDistrictID, @sAddress, @sContactName, @sMobile, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @sErrorMsg;
SELECT 1 FROM t_Provider WHERE F_DistrictID = @iDistrictID AND F_Address = @sAddress AND F_ContactName = @sContactName AND F_Mobile = @sMobile;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case6 Testing Result';
SELECT @iTotalRecord;