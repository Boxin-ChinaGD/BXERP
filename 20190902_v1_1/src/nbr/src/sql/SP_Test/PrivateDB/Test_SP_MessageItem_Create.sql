SELECT '++++++++++++++++++ Test_SP_MessageItem_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:正常添加 -------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iMessageID = 1;
SET @iMessageCategoryID = 8;
SET @iCommodityID = 3;
-- 
CALL SP_MessageItem_Create(@iErrorCode, @sErrorMsg, @iMessageID, @iMessageCategoryID, @iCommodityID);
-- 
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @sErrorMsg = '', '测试成功', '测试失败') AS 'Case1 Testing Result';



SELECT '-------------------- Case2:传入不存在的商品ID -------------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iMessageID = 1;
SET @iMessageCategoryID = 8;
SET @iCommodityID = 100000000;
-- 
CALL SP_MessageItem_Create(@iErrorCode, @sErrorMsg, @iMessageID, @iMessageCategoryID, @iCommodityID);
-- 
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '传入的商品ID不正确，不存在该商品', '测试成功', '测试失败') AS 'Case2 Testing Result';


SELECT '-------------------- Case3:传入不存在的消息ID -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iMessageID = 100000000;
SET @iMessageCategoryID = 8;
SET @iCommodityID = 1;
-- 
CALL SP_MessageItem_Create(@iErrorCode, @sErrorMsg, @iMessageID, @iMessageCategoryID, @iCommodityID);
-- 
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '传入的消息ID不正确，不存在该消息', '测试成功', '测试失败') AS 'Case3 Testing Result';



SELECT '-------------------- Case4:传入不存在的消息分类ID -------------------------' AS 'Case4';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iMessageID = 1;
SET @iMessageCategoryID = 100000000;
SET @iCommodityID = 1;
-- 
CALL SP_MessageItem_Create(@iErrorCode, @sErrorMsg, @iMessageID, @iMessageCategoryID, @iCommodityID);
-- 
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '传入的消息分类ID不正确，不存在该消息分类', '测试成功', '测试失败') AS 'Case4 Testing Result';