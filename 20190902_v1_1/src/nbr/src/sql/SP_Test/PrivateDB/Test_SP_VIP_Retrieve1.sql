INSERT INTO T_VIP (F_ICID,F_CardID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile)
VALUES ('1232456',1,'giggs','1232456@bx.vip',5,99.8,'广州',1,
'2017-08-06',44.9,'2017-08-08 23:59:10','12345612312');

-- CASE1:用存在的ID查询
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();

CALL SP_VIP_Retrieve1(@iErrorCode, @sErrorMsg, @iID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_VIP WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';

DELETE FROM t_vip WHERE F_ID = LAST_INSERT_ID();


-- CASE2:用不存在的ID查询
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = -2;

CALL SP_VIP_Retrieve1(@iErrorCode, @sErrorMsg, @iID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';