SELECT '++++++++++++++++++ Test_SP_CommodityShopInfo_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:根据商品ID查询商品门店信息 -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 1;
SET @iShopID = 0;
SET @iTotalRecord = 0;

CALL SP_CommodityShopInfo_RetrieveN(@iErrorCode, @sErrorMsg, @iCommodityID, @iShopID, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';



SELECT '-------------------- Case2:根据商品ID和门店ID查询商品门店信息 -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 1;
SET @iShopID = 2;
SET @iTotalRecord = 0;

CALL SP_CommodityShopInfo_RetrieveN(@iErrorCode, @sErrorMsg, @iCommodityID, @iShopID, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';



SELECT '-------------------- Case3:根据门店ID查询商品门店信息 -------------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = -1;
SET @iShopID = 2;
SET @iTotalRecord = 0;

CALL SP_CommodityShopInfo_RetrieveN(@iErrorCode, @sErrorMsg, @iCommodityID, @iShopID, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case3 Testing Result';