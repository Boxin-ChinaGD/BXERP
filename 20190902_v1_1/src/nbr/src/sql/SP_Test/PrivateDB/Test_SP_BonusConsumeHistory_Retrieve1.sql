SELECT '++++++++++++++++++ Test_SP_BonusConsumeHistory_Retrieve1.sql ++++++++++++++++++++';
INSERT INTO t_bonusconsumehistory (F_VipID, F_StaffID, F_Bonus, F_AddedBonus, F_Remark)
VALUES (1, null, 10, 10, '**********');
SET @iID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_BonusConsumeHistory_Retrieve1(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT 1 FROM t_bonusconsumehistory WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'Case1 Testing Result';

DELETE FROM t_bonusconsumehistory WHERE F_ID = @iID;