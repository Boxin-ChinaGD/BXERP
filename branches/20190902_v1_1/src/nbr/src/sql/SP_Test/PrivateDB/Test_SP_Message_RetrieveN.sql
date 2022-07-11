SELECT '++++++++++++++++++ Test_SP_Message_RetrieveN.sql ++++++++++++++++++++';

INSERT INTO T_Message (F_CategoryID,F_IsRead,F_Parameter,F_CreateDatetime,F_SenderID,F_ReceiverID,F_CompanyID)
VALUES (1,0,'FDAFF',now(),1,2,1); 

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Message_RetrieveN(@iErrorCode, @sErrorMsg, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'Case1 Testing Result';

DELETE FROM t_message WHERE F_ID = LAST_INSERT_ID();