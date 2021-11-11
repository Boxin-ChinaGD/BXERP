SELECT '++++++++++++++++++ Test_SP_MessageHandlerSetting_Delete.sql ++++++++++++++++++++';

INSERT INTO T_MessageHandlerSetting (F_CategoryID,F_Template,F_Link)
VALUES (1,'消息10','http://www.bxit.vip/shop/order/ID/10'); 

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID =LAST_INSERT_ID();

CALL SP_MessageHandlerSetting_Delete(@iErrorCode, @sErrorMsg, @iID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_MessageHandlerSetting WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';