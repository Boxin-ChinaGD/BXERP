SELECT '++++++++++++++++++ Test_SP_MessageHandlerSetting_Create.sql ++++++++++++++++++++';

SELECT '+++++++++++++++++++++ CASE1 正常创建+++++++++++++++++++++++++++++++++++++++++++' AS 'CASE1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCategoryID =1;
SET @sTemplate = '消息10';
SET @sLink = 'http://www.bxit.vip/shop/order/ID/10';

CALL SP_MessageHandlerSetting_Create(@iErrorCode, @sErrorMsg, @iCategoryID, @sTemplate, @sLink);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_messagehandlersetting WHERE F_CategoryID = @iCategoryID AND F_Template = @sTemplate AND F_Link = @sLink;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

DELETE FROM t_messagehandlersetting WHERE F_ID = LAST_INSERT_ID();

SELECT '+++++++++++++++++++++ CASE2  用不存在categoryID创建+++++++++++++++++++++++++++++++++++++++++++' AS 'CASE2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCategoryID = -99;
SET @sTemplate = '消息10';
SET @sLink = 'http://www.bxit.vip/shop/order/ID/10';

CALL SP_MessageHandlerSetting_Create(@iErrorCode, @sErrorMsg, @iCategoryID, @sTemplate, @sLink);

SELECT @sErrorMsg;
SELECT IF( @iErrorCode = 7, '测试成功', '测试失败') AS 'Case2 Testing Result';