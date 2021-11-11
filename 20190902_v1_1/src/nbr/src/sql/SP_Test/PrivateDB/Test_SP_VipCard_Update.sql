SELECT '++++++++++++++++++ Test_SP_VipCard_Update.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:会员卡积分清零规则天数合法，积分清零规则日期非法 -------------------------' AS 'Case1';
INSERT INTO t_vipcard (F_Title, F_BackgroundColor, F_ClearBonusDay, F_ClearBonusDatetime, F_CreateDatetime)
VALUES ('会员卡', '255,255,255;255,255,255', 3650, NULL, now());
SET @iID = LAST_INSERT_ID();
-- 
SET @sTitle = '会员卡1';
SET @sBackgroundColor = '255,255,255;255,255,255';
SET @iClearBonusDay = 100;
SET @dtClearBonusDatetime = NULL ;
SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_VipCard_Update(@iErrorCode, @sErrorMsg, @iID, @sTitle, @sBackgroundColor, @iClearBonusDay, @dtClearBonusDatetime);
-- 
SELECT @sErrorMsg;
SELECT 1 FROM t_vipcard WHERE F_ID = @iID 
AND F_Title = @sTitle
AND F_BackgroundColor = @sBackgroundColor
AND F_ClearBonusDay = @iClearBonusDay
AND F_ClearBonusDatetime IS NULL;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

DELETE FROM t_vipcard WHERE F_ID = @iID;

SELECT '-------------------- Case2:会员卡积分清零规则天数非法，积分清零规则日期合法 -------------------------' AS 'Case2';
INSERT INTO t_vipcard (F_Title, F_BackgroundColor, F_ClearBonusDay, F_ClearBonusDatetime, F_CreateDatetime)
VALUES ('会员卡', '255,255,255;255,255,255', 3650, NULL, now());
SET @iID = LAST_INSERT_ID();
-- 
SET @sTitle = '会员卡1';
SET @sBackgroundColor = '255,255,255;255,255,255';
SET @iClearBonusDay = -1;
SET @dtClearBonusDatetime = now() ;
SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_VipCard_Update(@iErrorCode, @sErrorMsg, @iID, @sTitle, @sBackgroundColor, @iClearBonusDay, @dtClearBonusDatetime);
-- 
SELECT @sErrorMsg;
SELECT 1 FROM t_vipcard WHERE F_ID = @iID 
AND F_Title = @sTitle
AND F_BackgroundColor = @sBackgroundColor
AND F_ClearBonusDay is NULL 
AND F_ClearBonusDatetime = @dtClearBonusDatetime;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';

DELETE FROM t_vipcard WHERE F_ID = @iID;

SELECT '-------------------- Case3:会员卡不存在 -------------------------' AS 'Case3';
SET @iID = -1;
SET @sTitle = '会员卡1';
SET @sBackgroundColor = '255,255,255;255,255,255';
SET @iClearBonusDay = 100;
SET @dtClearBonusDatetime = NULL ;
SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_VipCard_Update(@iErrorCode, @sErrorMsg, @iID, @sTitle, @sBackgroundColor, @iClearBonusDay, @dtClearBonusDatetime);
-- 
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 2 AND @sErrorMsg = '会员卡不存在', '测试成功', '测试失败') AS 'Case3 Testing Result';