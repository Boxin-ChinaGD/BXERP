SELECT '++++++++++++++++++ Test_SP_Message_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:������� -------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCategoryID = 1;
SET @iIsRead = 0;
SET @sParameter = 'Json';
SET @iSenderID = 4;
SET @iReceiverID = 1;
SET @iCompanyID = 1;

CALL SP_Message_Create(@iErrorCode, @sErrorMsg, @iCategoryID, @iIsRead, @sParameter, @iSenderID, @iReceiverID, @iCompanyID);
SELECT @sErrorMsg;
SELECT 1 FROM t_message WHERE F_CategoryID = @iCategoryID AND F_IsRead = @iIsRead AND F_Parameter = @sParameter AND F_SenderID = @iSenderID AND F_ReceiverID = @iReceiverID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

DELETE FROM t_message WHERE F_ID = LAST_INSERT_ID();

SELECT '-------------------- Case2:�ò����ڵ�MessageCategoryID����Message -------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCategoryID = -1;
SET @iIsRead = 0;
SET @sParameter = 'Json';
SET @iSenderID = 4;
SET @iReceiverID = 1;
SET @iCompanyID = 1;

CALL SP_Message_Create(@iErrorCode, @sErrorMsg, @iCategoryID, @iIsRead, @sParameter, @iSenderID, @iReceiverID, @iCompanyID);
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

SELECT '-------------------- Case3:����һ�������ڵ�companyID-------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCategoryID = 1;
SET @iIsRead = 0;
SET @sParameter = 'Json';
SET @iSenderID = 4;
SET @iReceiverID = 1;
SET @iCompanyID = 999999999;

CALL SP_Message_Create(@iErrorCode, @sErrorMsg, @iCategoryID, @iIsRead, @sParameter, @iSenderID, @iReceiverID, @iCompanyID);
SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';