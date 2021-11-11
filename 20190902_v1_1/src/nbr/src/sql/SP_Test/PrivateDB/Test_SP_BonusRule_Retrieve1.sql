SELECT '++++++++++++++++++ Test_SP_BonusRule_Retrieve1.sql ++++++++++++++++++++';
INSERT INTO t_bonusrule (F_VipCardID, F_AmountUnit, F_IncreaseBonus, F_MaxIncreaseBonus, F_InitIncreaseBonus)
VALUES (1, 1, 1, 1, 1);
SET @iID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_BonusRule_Retrieve1(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT 1 FROM t_bonusrule WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'Case1 Testing Result';

DELETE FROM t_bonusrule WHERE F_ID = @iID;