SELECT '++++++++++++++++++ Test_SP_MessageHandlerSetting_Retrieve1.sql ++++++++++++++++++++';

INSERT INTO T_MessageHandlerSetting (F_CategoryID,F_Template,F_Link)
VALUES (1,'消息10','http://www.bxit.vip/shop/order/ID/10'); 

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID =LAST_INSERT_ID();

CALL SP_MessageHandlerSetting_Retrieve1(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT 1 FROM t_MessageHandlerSetting WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

DELETE FROM T_MessageHandlerSetting WHERE F_ID = @iID;