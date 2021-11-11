SELECT '++++++++++++++++++ Test_SP_MessageItem_RetrieveN.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:根据类别为滞销商品查询 -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iMessageCategoryID = 8;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
--	SET @iShopID = 2;	

CALL SP_MessageItem_RetrieveN(@iErrorCode, @sErrorMsg, @iMessageCategoryID, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @sErrorMsg = '', '测试成功', '测试失败') AS 'Case1 Testing Result';



SELECT '-------------------- Case2:传入不存在的消息分类ID，查询失败 -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iMessageCategoryID = 100000000;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
--	SET @iShopID = 2;

CALL SP_MessageItem_RetrieveN(@iErrorCode, @sErrorMsg, @iMessageCategoryID, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '传入的消息分类ID不正确，不存在该消息分类', '测试成功', '测试失败') AS 'Case2 Testing Result';