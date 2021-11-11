SELECT '++++++++++++++++++ Test_SP_POS_Retrieve1.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:正常查询 -----------------------';
INSERT INTO t_pos (F_POS_SN, F_ShopID, F_Salt, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('SN1561891231', 1, 'DFSDGSDKJG546S8FDH', 0, now(), now());

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();
SET @iReturnSalt = 1;
SET @iResetPasswordInPos = 0;

CALL SP_POS_Retrieve1(@iErrorCode, @sErrorMsg, @iID, @iReturnSalt, @iResetPasswordInPos);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_POS WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';
-- 
DELETE FROM t_pos WHERE F_ID = @iID;

SELECT '-------------------- Case2:首次登陆查询，设置F_PasswordInPOS为NULL -----------------------';
INSERT INTO t_pos (F_POS_SN, F_ShopID, F_Salt, F_PasswordInPOS, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('SN1561891231', 1, 'DFSDGSDKJG546S8FDH', '123456', 0, now(), now());

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();
SET @iReturnSalt = 1;
SET @iResetPasswordInPos = 1;

CALL SP_POS_Retrieve1(@iErrorCode, @sErrorMsg, @iID, @iReturnSalt, @iResetPasswordInPos);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_POS WHERE F_ID = @iID AND F_PasswordInPOS is NULL;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';
--  
DELETE FROM t_pos WHERE F_ID = @iID;