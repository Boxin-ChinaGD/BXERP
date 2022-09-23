SELECT '++++++++++++++++++ Test_SP_MessageHandlerSetting_Create.sql ++++++++++++++++++++';

SELECT '+++++++++++++++++++++ CASE1 ��������+++++++++++++++++++++++++++++++++++++++++++' AS 'CASE1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCategoryID =1;
SET @sTemplate = '��Ϣ10';
SET @sLink = 'http://www.bxit.vip/shop/order/ID/10';

CALL SP_MessageHandlerSetting_Create(@iErrorCode, @sErrorMsg, @iCategoryID, @sTemplate, @sLink);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_messagehandlersetting WHERE F_CategoryID = @iCategoryID AND F_Template = @sTemplate AND F_Link = @sLink;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

DELETE FROM t_messagehandlersetting WHERE F_ID = LAST_INSERT_ID();

SELECT '+++++++++++++++++++++ CASE2  �ò�����categoryID����+++++++++++++++++++++++++++++++++++++++++++' AS 'CASE2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCategoryID = -99;
SET @sTemplate = '��Ϣ10';
SET @sLink = 'http://www.bxit.vip/shop/order/ID/10';

CALL SP_MessageHandlerSetting_Create(@iErrorCode, @sErrorMsg, @iCategoryID, @sTemplate, @sLink);

SELECT @sErrorMsg;
SELECT IF( @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';