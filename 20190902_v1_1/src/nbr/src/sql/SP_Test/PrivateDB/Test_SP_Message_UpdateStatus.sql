SELECT '++++++++++++++++++ Test_SP_Message_UpdateStatus.sql ++++++++++++++++++++';

SELECT '-------------------- CASE1:更新status-------------------------' AS 'Case2'; 
INSERT INTO T_Message (F_CategoryID,F_IsRead,F_Parameter,F_CreateDatetime,F_SenderID,F_ReceiverID,F_CompanyID)
VALUES (1,0,'FDAFF',now(),1,2,1); 

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID =LAST_INSERT_ID();

CALL SP_Message_UpdateStatus(@iErrorCode, @sErrorMsg, @iID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_message WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

DELETE FROM t_message WHERE F_ID = @iID;