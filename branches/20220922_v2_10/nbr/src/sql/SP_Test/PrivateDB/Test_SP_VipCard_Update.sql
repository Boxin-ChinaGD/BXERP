SELECT '++++++++++++++++++ Test_SP_VipCard_Update.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:��Ա������������������Ϸ�����������������ڷǷ� -------------------------' AS 'Case1';
INSERT INTO t_vipcard (F_Title, F_BackgroundColor, F_ClearBonusDay, F_ClearBonusDatetime, F_CreateDatetime)
VALUES ('��Ա��', '255,255,255;255,255,255', 3650, NULL, now());
SET @iID = LAST_INSERT_ID();
-- 
SET @sTitle = '��Ա��1';
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
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

DELETE FROM t_vipcard WHERE F_ID = @iID;

SELECT '-------------------- Case2:��Ա������������������Ƿ�����������������ںϷ� -------------------------' AS 'Case2';
INSERT INTO t_vipcard (F_Title, F_BackgroundColor, F_ClearBonusDay, F_ClearBonusDatetime, F_CreateDatetime)
VALUES ('��Ա��', '255,255,255;255,255,255', 3650, NULL, now());
SET @iID = LAST_INSERT_ID();
-- 
SET @sTitle = '��Ա��1';
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
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

DELETE FROM t_vipcard WHERE F_ID = @iID;

SELECT '-------------------- Case3:��Ա�������� -------------------------' AS 'Case3';
SET @iID = -1;
SET @sTitle = '��Ա��1';
SET @sBackgroundColor = '255,255,255;255,255,255';
SET @iClearBonusDay = 100;
SET @dtClearBonusDatetime = NULL ;
SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_VipCard_Update(@iErrorCode, @sErrorMsg, @iID, @sTitle, @sBackgroundColor, @iClearBonusDay, @dtClearBonusDatetime);
-- 
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 2 AND @sErrorMsg = '��Ա��������', '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';