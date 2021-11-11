SELECT '++++++++++++++++++ Test_SP_Commodity_RetrieveNMultiPackageCommodity.sql ++++++++++++++++++++';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iRefCommodityID = 47;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Commodity_RetrieveNMultiPackageCommodity(@iErrorCode, @sErrorMsg, @iRefCommodityID, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT @iTotalRecord;

SELECT 1 FROM t_commodity 
WHERE F_RefCommodityID = @iRefCommodityID;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE1 Testing Result';

-- case2:输入一个不存在的iRefCommodityID
SELECT '++++++++++++++++++ Test_SP_Commodity_RetrieveNMultiPackageCommodity.sql case2:输入一个不存在的iRefCommodityID ++++++++++++++++++++';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iRefCommodityID = -2222;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Commodity_RetrieveNMultiPackageCommodity(@iErrorCode, @sErrorMsg, @iRefCommodityID, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @sErrorMsg;

SELECT 1 FROM t_commodity 
WHERE F_RefCommodityID = @iRefCommodityID;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE2 Testing Result';