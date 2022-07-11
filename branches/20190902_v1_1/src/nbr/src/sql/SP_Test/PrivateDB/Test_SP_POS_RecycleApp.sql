SELECT '++++++++++++++++++ Test_SP_POS_RecycleApp.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:重置一个已经登录过app，未删除的POS机 -------------------------' AS 'Case1';
INSERT INTO t_pos (F_POS_SN, F_ShopID, F_pwdEncrypted, F_Salt, F_PasswordInPOS, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('T109179800012', 1, NULL, 'B1AFC07474C37C5AEC4199ED28E09705', NULL, 0, '2019-03-07 10:01:38', '2019-03-07 10:01:38');
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = last_insert_id();
SET @sSalt = 'B1AFC07474C37C5AEC4199ED28E09705';
SET @sPasswordInPOS = '000000';
SET @iReturnSalt = 1;
-- 
CALL SP_POS_RecycleApp(@iErrorCode, @sErrorMsg, @iID, @sPasswordInPOS, @sSalt, @iReturnSalt);
-- 
SELECT 1 FROM t_pos WHERE F_ID = @iID AND F_Salt = @sSalt AND F_PasswordInPOS = @sPasswordInPOS ;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';
-- 
DELETE FROM t_pos WHERE F_ID = @iID;

SELECT '-------------------- Case2:重置一个未登录过app，未删除的POS机 -------------------------' AS 'Case2';
INSERT INTO t_pos (F_POS_SN, F_ShopID, F_pwdEncrypted, F_Salt, F_PasswordInPOS, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('T109179800012', 1, NULL, 'B1AFC07474C37C5AEC4199ED28E09705', '000000', 0, '2019-03-07 10:01:38', '2019-03-07 10:01:38');
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = last_insert_id();
SET @sSalt = 'B1AFC07474C37C5AEC4199ED28E09705';
SET @sPasswordInPOS = '000000';
SET @iReturnSalt = 1;
-- 
CALL SP_POS_RecycleApp(@iErrorCode, @sErrorMsg, @iID, @sPasswordInPOS, @sSalt, @iReturnSalt);
-- 
SELECT 1 FROM t_pos WHERE F_ID = @iID AND F_Salt = @sSalt AND F_PasswordInPOS = @sPasswordInPOS ;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';
-- 
DELETE FROM t_pos WHERE F_ID = @iID;

SELECT '-------------------- Case3:重置已经登录过app，已删除的POS机 -------------------------' AS 'Case3';
INSERT INTO t_pos (F_POS_SN, F_ShopID, F_pwdEncrypted, F_Salt, F_PasswordInPOS, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('T109179800012', 1, NULL, 'B1AFC07474C37C5AEC4199ED28E09705', NULL, 1, '2019-03-07 10:01:38', '2019-03-07 10:01:38');
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = last_insert_id();
SET @sSalt = 'B1AFC07474C37C5AEC4199ED28E09705';
SET @sPasswordInPOS = '000000';
SET @iReturnSalt = 1;
-- 
CALL SP_POS_RecycleApp(@iErrorCode, @sErrorMsg, @iID, @sPasswordInPOS, @sSalt, @iReturnSalt);
-- 
SELECT 1 FROM t_pos WHERE F_ID = @iID AND F_Salt = @sSalt AND F_PasswordInPOS IS NULL AND F_Status = 1;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case3 Testing Result';
-- 
DELETE FROM t_pos WHERE F_ID = @iID;

SELECT '-------------------- Case4:重置一个不存在POS机 -------------------------' AS 'Case4';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 100000000;
SET @sSalt = 'B1AFC07474C37C5AEC4199ED28E09705';
SET @sPasswordInPOS = '000000';
SET @iReturnSalt = 1;
-- 
CALL SP_POS_RecycleApp(@iErrorCode, @sErrorMsg, @iID, @sPasswordInPOS, @sSalt, @iReturnSalt);
-- 
SELECT 1 FROM t_pos WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case4 Testing Result';