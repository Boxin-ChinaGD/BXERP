SELECT '++++++++++++++++++ Test_SP_BonusConsumeHistory_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:正常添加 -------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iVipID = 1;
SET @iStaffID = 1;
SET @iBonus = 10;
SET @iAddedBonus = 10;
SET @sRecordBonus ='**********';
-- 
CALL SP_BonusConsumeHistory_Create(@iErrorCode, @sErrorMsg, @iVipID, @iStaffID, @iBonus, @iAddedBonus, @sRecordBonus);
-- 
SELECT @sErrorMsg;
SELECT 1 FROM t_bonusconsumehistory 
WHERE F_VipID = @iVipID 
AND F_StaffID = @iStaffID 
AND F_Bonus = @iBonus
AND F_AddedBonus = @iAddedBonus
AND F_Remark= @sRecordBonus;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';
-- 
DELETE FROM t_bonusconsumehistory WHERE F_ID = LAST_INSERT_ID();

SELECT '-------------------- Case2:会员ID不存在 -------------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iVipID = -1;
SET @iStaffID = 1;
SET @iBonus = 10;
SET @iAddedBonus = 10;
SET @sRecordBonus ='**********';
-- 
CALL SP_BonusConsumeHistory_Create(@iErrorCode, @sErrorMsg, @iVipID, @iStaffID, @iBonus, @iAddedBonus, @sRecordBonus);
-- 
SELECT @sErrorMsg;
SELECT 1 FROM t_bonusconsumehistory 
WHERE F_VipID = @iVipID 
AND F_StaffID = @iStaffID 
AND F_Bonus = @iBonus
AND F_AddedBonus = @iAddedBonus
AND F_Remark= @sRecordBonus;
SELECT IF(found_rows() = 0 AND @iErrorCode = 2 AND @sErrorMsg = '会员ID不存在', '测试成功', '测试失败') AS 'Case2 Testing Result';