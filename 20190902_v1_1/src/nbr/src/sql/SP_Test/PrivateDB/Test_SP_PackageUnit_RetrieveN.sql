SELECT '++++++++++++++++++ Test_SP_PackageUnit_RetrieveN.sql ++++++++++++++++++++';

SELECT '++++++++++++++++++++++ case1:没有用多包装商品ID进行查询  ++++++++++++++++++++++++++++++++++' AS 'case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iNormalCommodityID = 0;

CALL SP_PackageUnit_RetrieveN(@iErrorCode, @sErrorMsg, @iNormalCommodityID, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT @iTotalRecord;
 
-- ...???? 为什么没有写测试案例名称 

SELECT '++++++++++++++++++++++ case2:用参照商品ID查询多包装商品单位 ++++++++++++++++++++++++++++++++++' AS 'case2';
 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iNormalCommodityID = 47;

CALL SP_PackageUnit_RetrieveN(@iErrorCode, @sErrorMsg, @iNormalCommodityID, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';

SELECT @iTotalRecord;