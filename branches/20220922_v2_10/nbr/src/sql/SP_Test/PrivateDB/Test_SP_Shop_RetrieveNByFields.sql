SELECT '++++++++++++++++++ Test_SP_Shop_RetrieveN.sql ++++++++++++++++++++';

SELECT '+++++++++++++++++++++++ case1:正常查询 ++++++++++++++++++++++++++++++++++++' AS 'case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '';
SET @iDistrictID= -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Shop_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iDistrictID, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT '+++++++++++++++++++++++ case2:根据门店名称查询 ++++++++++++++++++++++++++++++++++++' AS 'case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '默认';
SET @iDistrictID= -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Shop_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iDistrictID, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';

SELECT '+++++++++++++++++++++++ case3:根据地域ID查询 ++++++++++++++++++++++++++++++++++++' AS 'case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '';
SET @iDistrictID= 1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Shop_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iDistrictID, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case3 Testing Result';

SELECT '+++++++++++++++++++++++ case4:根据地域ID和名称查询查询 ++++++++++++++++++++++++++++++++++++' AS 'case4';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '默认';
SET @iDistrictID= 1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Shop_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iDistrictID, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case4 Testing Result';