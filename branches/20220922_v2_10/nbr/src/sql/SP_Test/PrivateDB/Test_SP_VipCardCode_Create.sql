SELECT '++++++++++++++++++ Test_SP_VipCardCode_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:正常添加 -------------------------' AS 'Case1';
INSERT INTO t_vipcard (F_Title, F_BackgroundColor, F_ClearBonusDay, F_ClearBonusDatetime, F_CreateDatetime)
VALUES ('会员卡', '255,255,255;255,255,255', 3650, NULL, now());
SET @iVipCardID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iVipID = 1;
SET @sCompanySN = '668866';
-- 
CALL SP_VipCardCode_Create(@iErrorCode, @sErrorMsg, @iVipID, @iVipCardID, @sCompanySN);
-- 
SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT 1 FROM t_vipcardcode WHERE F_VipID = @iVipID AND F_VipCardID = @iVipCardID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';
--
DELETE FROM t_vipcardcode WHERE F_ID = LAST_INSERT_ID();
DELETE FROM t_vipcard WHERE F_ID = @iVipCardID;

SELECT '-------------------- Case2:会员ID不存在 -------------------------' AS 'Case2';
INSERT INTO t_vipcard (F_Title, F_BackgroundColor, F_ClearBonusDay, F_ClearBonusDatetime, F_CreateDatetime)
VALUES ('会员卡', '255,255,255;255,255,255', 3650, NULL, now());
SET @iVipCardID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iVipID = -1;
SET @sCompanySN = '668866';
-- 
CALL SP_VipCardCode_Create(@iErrorCode, @sErrorMsg, @iVipID, @iVipCardID, @sCompanySN);
-- 
SELECT @sErrorMsg;
SELECT 1 FROM t_vipcardcode WHERE F_VipID = @iVipID AND F_VipCardID = @iVipCardID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 2 AND @sErrorMsg = '该会员不存在', '测试成功', '测试失败') AS 'Case2 Testing Result';
--
DELETE FROM t_vipcard WHERE F_ID = @iVipCardID;

SELECT '-------------------- Case3:会员卡ID不存在 -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iVipID = 1;
SET @iVipCardID = -1;
SET @sCompanySN = '668866';
-- 
CALL SP_VipCardCode_Create(@iErrorCode, @sErrorMsg, @iVipID, @iVipCardID, @sCompanySN);
-- 
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 2 AND @sErrorMsg = '该会员卡不存在', '测试成功', '测试失败') AS 'Case3 Testing Result';