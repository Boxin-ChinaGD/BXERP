SELECT '++++++++++++++++++ Test_SP_MessageItem_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:������� -------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iMessageID = 1;
SET @iMessageCategoryID = 8;
SET @iCommodityID = 3;
-- 
CALL SP_MessageItem_Create(@iErrorCode, @sErrorMsg, @iMessageID, @iMessageCategoryID, @iCommodityID);
-- 
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';



SELECT '-------------------- Case2:���벻���ڵ���ƷID -------------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iMessageID = 1;
SET @iMessageCategoryID = 8;
SET @iCommodityID = 100000000;
-- 
CALL SP_MessageItem_Create(@iErrorCode, @sErrorMsg, @iMessageID, @iMessageCategoryID, @iCommodityID);
-- 
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '�������ƷID����ȷ�������ڸ���Ʒ', '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';


SELECT '-------------------- Case3:���벻���ڵ���ϢID -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iMessageID = 100000000;
SET @iMessageCategoryID = 8;
SET @iCommodityID = 1;
-- 
CALL SP_MessageItem_Create(@iErrorCode, @sErrorMsg, @iMessageID, @iMessageCategoryID, @iCommodityID);
-- 
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '�������ϢID����ȷ�������ڸ���Ϣ', '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';



SELECT '-------------------- Case4:���벻���ڵ���Ϣ����ID -------------------------' AS 'Case4';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iMessageID = 1;
SET @iMessageCategoryID = 100000000;
SET @iCommodityID = 1;
-- 
CALL SP_MessageItem_Create(@iErrorCode, @sErrorMsg, @iMessageID, @iMessageCategoryID, @iCommodityID);
-- 
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '�������Ϣ����ID����ȷ�������ڸ���Ϣ����', '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';