SELECT '++++++++++++++++++ Test_SPD_ReturnCommoditySheet_CheckStatus.sql ++++++++++++++++++++';
SELECT '------------------ ’˝≥£≤‚ ‘ --------------------' AS 'CASE1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_ReturnCommoditySheet_CheckStatus(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@iErrorCode = 0 AND @sErrorMsg = '', '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'CASE1 RESULT';
-- 
SELECT '------------------ ◊¥Ã¨“Ï≥£ --------------------' AS 'CASE2';
-- 
INSERT INTO T_ReturnCommoditySheet (F_StaffID,F_ProviderID,F_Status) VALUES (5,5,5);
SET @iReturnCommoditySheetID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_ReturnCommoditySheet_CheckStatus(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = concat('ÕÀªıµ•', @iReturnCommoditySheetID, '◊¥Ã¨“Ï≥£'), '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'CASE2 RESULT';
-- 
DELETE FROM t_returncommoditysheet WHERE F_ID = @iReturnCommoditySheetID;
-- 