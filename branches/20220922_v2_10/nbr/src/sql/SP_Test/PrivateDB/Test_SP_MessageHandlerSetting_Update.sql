SELECT '++++++++++++++++++ Test_SP_MessageHandlerSetting_Update.sql ++++++++++++++++++++';

INSERT INTO T_MessageHandlerSetting (F_CategoryID,F_Template,F_Link)
VALUES (1,'消息10','http://www.bxit.vip/shop/order/ID/10'); 

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID =LAST_INSERT_ID();
SET @iCategoryID = 5;
SET @sTemplate = '消息11';
SET @sLink = 'http://www.bxit.vip/shop/order/ID/11';


CALL SP_MessageHandlerSetting_Update(@iErrorCode, @sErrorMsg, @iID, @iCategoryID, @sTemplate, @sLink);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM T_MessageHandlerSetting WHERE F_ID = @iID AND F_CategoryID = @iCategoryID AND F_Template = @sTemplate AND F_Link = @sLink;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

DELETE FROM T_MessageHandlerSetting WHERE F_ID = @iID;