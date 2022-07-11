SELECT '++++++++++++++++++ Test_SP_BonusRule_Update.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:正常修改 -------------------------' AS 'Case1';
INSERT INTO nbr.t_bonusrule (F_VipCardID, F_AmountUnit, F_IncreaseBonus, F_MaxIncreaseBonus, F_InitIncreaseBonus)
VALUES (1, 100, 1, 1, 1);
SET @iID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- SET @iVipCardID = 1;
SET @iAmountUnit = 100;
SET @iIncreaseBonus = 1;
SET @iMaxIncreaseBonus = 99999;
SET @iInitIncreaseBonus = 0;
-- 
CALL SP_BonusRule_Update(@iErrorCode, @sErrorMsg, @iID, @iAmountUnit, @iIncreaseBonus, @iMaxIncreaseBonus, @iInitIncreaseBonus);
-- 
SELECT @sErrorMsg;
SELECT 1 FROM t_bonusrule 
WHERE F_AmountUnit = @iAmountUnit
AND F_IncreaseBonus = @iIncreaseBonus
AND F_MaxIncreaseBonus = @iMaxIncreaseBonus
AND F_InitIncreaseBonus = @iInitIncreaseBonus;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';
-- 
DELETE FROM t_bonusrule WHERE F_ID = LAST_INSERT_ID();