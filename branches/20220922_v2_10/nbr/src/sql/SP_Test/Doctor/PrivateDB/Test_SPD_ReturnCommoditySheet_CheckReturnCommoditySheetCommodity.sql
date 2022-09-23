SELECT '++++++++++++++++++ Test_SPD_ReturnCommoditySheet_CheckReturnCommoditySheetCommodity.sql ++++++++++++++++++++';
SELECT '------------------ CASE1:�������� --------------------' AS 'CASE1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_ReturnCommoditySheet_CheckReturnCommoditySheetCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode,@sErrorMsg;
-- 
SELECT IF(@iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'CASE1 RESULT';
-- 

SELECT '------------------ CASE2:û���˻���Ʒ���˻��� --------------------' AS 'CASE2';
-- 
INSERT INTO t_returncommoditysheet (F_StaffID, F_ProviderID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES (1, 1, 0, now(), now());
SET @iID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_ReturnCommoditySheet_CheckReturnCommoditySheetCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode,@sErrorMsg;
-- 
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = concat('�˻���:', @iID, 'û���˻���Ʒ'), '���Գɹ�', '����ʧ��') AS 'CASE2 RESULT';
-- 
DELETE FROM t_returncommoditysheet WHERE F_ID = @iID;
-- 