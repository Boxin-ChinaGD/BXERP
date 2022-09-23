-- Ĭ�ϻ�Ա��������Ҫ����
SELECT '++++++++++++++++++ Test_SP_VipCard_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:������� -------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sTitle = '��Ա��';
SET @sBackgroundColor = '255,255,255;255,255,255';
SET @iClearBonusDay = 100;
SET @dtClearBonusDatetime = NULL ;
-- 
CALL SP_VipCard_Create(@iErrorCode, @sErrorMsg, @sTitle, @sBackgroundColor, @iClearBonusDay, @dtClearBonusDatetime);
-- 
SELECT @sErrorMsg;
SELECT 1 FROM t_vipcard WHERE F_Title = @sTitle AND F_BackgroundColor = @sBackgroundColor AND F_ClearBonusDay = @iClearBonusDay;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';
--
DELETE FROM t_vipcard WHERE F_ID = LAST_INSERT_ID();