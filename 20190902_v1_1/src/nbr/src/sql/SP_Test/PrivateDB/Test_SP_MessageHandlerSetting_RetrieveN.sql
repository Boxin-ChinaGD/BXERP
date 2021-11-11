SELECT '++++++++++++++++++ Test_SP_MessageHandlerSetting_RetrieveN.sql ++++++++++++++++++++';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;


CALL SP_MessageHandlerSetting_RetrieveN(@iErrorCode, @sErrorMsg, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

DELETE FROM t_message WHERE F_ID = LAST_INSERT_ID();