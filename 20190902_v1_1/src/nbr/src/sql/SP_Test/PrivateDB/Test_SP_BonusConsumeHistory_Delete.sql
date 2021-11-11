SELECT '++++++++++++++++++ Test_SP_BonusConsumeHistory_Delete.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:’˝≥£…æ≥˝ -------------------------' AS 'Case1';

INSERT INTO t_bonusconsumehistory (F_VipID, F_StaffID, F_Bonus, F_AddedBonus, F_Remark)
VALUES (1, null, 10, 10, '**********');
SET @iID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_BonusConsumeHistory_Delete(@iErrorCode, @sErrorMsg, @iID);
-- 
SELECT @sErrorMsg;
SELECT 1 FROM t_bonusconsumehistory WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'Case1 Testing Result';