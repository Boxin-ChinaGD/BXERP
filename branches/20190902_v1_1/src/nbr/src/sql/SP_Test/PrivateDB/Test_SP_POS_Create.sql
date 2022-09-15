SELECT '++++++++++++++++++ Test_SP_POS_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: 正常添加 -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sPOS_SN = 'SN13649875151010';
SET @iShopID = 1;
SET @sSaft = 'B1AFC07474C37C5AEC4199ED28E09705';
SET @iStatus = 0 ;
SET @iReturnSalt = 1;
SET @sPasswordInPOS = 00000;

CALL SP_POS_Create(@iErrorCode, @sErrorMsg, @sPOS_SN, @iShopID, @sSaft, @iStatus, @iReturnSalt, @sPasswordInPOS);

SELECT @sErrorMsg;
SELECT 1 FROM t_POS WHERE F_POS_SN = @sPOS_SN AND F_ShopID = @iShopID AND F_Salt = @sSaft AND F_Status = @iStatus;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT '-------------------- Case2: 添加t_pos中已有的SN码 -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @SPOS_SN = 'SN23346348';
SET @iShopID = 2;
SET @sSaft = 'B1AFC07474C37C5AEC4199ED28E09705';
SET @iStatus = 0 ;
SET @iReturnSalt = 1;
SET @sPasswordInPOS = 00000;

CALL SP_POS_Create(@iErrorCode, @sErrorMsg, @sPOS_SN, @iShopID, @sSaft, @iStatus, @iReturnSalt, @sPasswordInPOS);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_POS WHERE F_POS_SN = @sPOS_SN AND F_ShopID = @iShopID AND F_Salt = @sSaft AND F_Status = @iStatus;
SELECT IF(found_rows() = 1 AND @iErrorCode = 1 AND @sErrorMsg = '该SN码已经被其他POS机引用', '测试成功', '测试失败') AS 'Case2 Testing Result';

DELETE FROM t_pos WHERE F_ID = LAST_INSERT_ID();

SELECT '-------------------- Case3:用不存在的shopID创建POS -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sPOS_SN = 'SN13649875151010';
SET @iShopID = -99;
SET @sSaft = 'B1AFC07474C37C5AEC4199ED28E09705';
SET @iStatus = 0 ;
SET @iReturnSalt = 1;
SET @sPasswordInPOS = 00000;

CALL SP_POS_Create(@iErrorCode, @sErrorMsg, @sPOS_SN, @iShopID, @sSaft, @iStatus, @iReturnSalt, @sPasswordInPOS);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT '-------------------- Case4: 添加t_pos中已有删除的pos的SN码  -------------------------' AS 'Case4';
SET @sPOS_SN = 'K1234567890';
INSERT INTO t_pos (F_POS_SN, F_ShopID, F_pwdEncrypted, F_Salt, F_PasswordInPOS, F_Status)
VALUES (@sPOS_SN, 1, NULL, 'B1AFC07474C37C5AEC4199ED28E09705', '000000', 0);
SET @iID = LAST_INSERT_ID();
-- 
UPDATE t_pos SET F_Status = 1 WHERE F_ID = @iID; 
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 1;
SET @sSaft = 'B1AFC07474C37C5AEC4199ED28E09705';
SET @iStatus = 0 ;
SET @iReturnSalt = 1;
SET @sPasswordInPOS = 00000;
-- 
CALL SP_POS_Create(@iErrorCode, @sErrorMsg, @sPOS_SN, @iShopID, @sSaft, @iStatus, @iReturnSalt, @sPasswordInPOS);
-- 
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case4 Testing Result';
-- 
DELETE FROM t_pos WHERE F_POS_SN = 'K1234567890';