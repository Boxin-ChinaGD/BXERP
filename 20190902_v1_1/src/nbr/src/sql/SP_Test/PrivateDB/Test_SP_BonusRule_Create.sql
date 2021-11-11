SELECT '++++++++++++++++++ Test_SP_BonusRule_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:正常添加 -------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iVipCardID = 1;
SET @iAmountUnit = 100;
SET @iIncreaseBonus = 1;
SET @iMaxIncreaseBonus = 99999;
SET @iInitIncreaseBonus = 0;
-- 
CALL SP_BonusRule_Create(@iErrorCode, @sErrorMsg, @iVipCardID, @iAmountUnit, @iIncreaseBonus, @iMaxIncreaseBonus, @iInitIncreaseBonus);
-- 
SELECT @sErrorMsg;
SELECT 1 FROM t_bonusrule 
WHERE F_VipCardID = @iVipCardID
AND F_AmountUnit = @iAmountUnit
AND F_IncreaseBonus = @iIncreaseBonus
AND F_MaxIncreaseBonus = @iMaxIncreaseBonus
AND F_InitIncreaseBonus = @iInitIncreaseBonus;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';
-- 
DELETE FROM t_bonusrule WHERE F_ID = LAST_INSERT_ID();
