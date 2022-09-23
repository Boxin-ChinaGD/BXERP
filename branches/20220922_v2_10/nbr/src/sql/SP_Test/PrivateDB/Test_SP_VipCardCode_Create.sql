SELECT '++++++++++++++++++ Test_SP_VipCardCode_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:������� -------------------------' AS 'Case1';
INSERT INTO t_vipcard (F_Title, F_BackgroundColor, F_ClearBonusDay, F_ClearBonusDatetime, F_CreateDatetime)
VALUES ('��Ա��', '255,255,255;255,255,255', 3650, NULL, now());
SET @iVipCardID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iVipID = 1;
SET @sCompanySN = '668866';
-- 
CALL SP_VipCardCode_Create(@iErrorCode, @sErrorMsg, @iVipID, @iVipCardID, @sCompanySN);
-- 
SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT 1 FROM t_vipcardcode WHERE F_VipID = @iVipID AND F_VipCardID = @iVipCardID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';
--
DELETE FROM t_vipcardcode WHERE F_ID = LAST_INSERT_ID();
DELETE FROM t_vipcard WHERE F_ID = @iVipCardID;

SELECT '-------------------- Case2:��ԱID������ -------------------------' AS 'Case2';
INSERT INTO t_vipcard (F_Title, F_BackgroundColor, F_ClearBonusDay, F_ClearBonusDatetime, F_CreateDatetime)
VALUES ('��Ա��', '255,255,255;255,255,255', 3650, NULL, now());
SET @iVipCardID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iVipID = -1;
SET @sCompanySN = '668866';
-- 
CALL SP_VipCardCode_Create(@iErrorCode, @sErrorMsg, @iVipID, @iVipCardID, @sCompanySN);
-- 
SELECT @sErrorMsg;
SELECT 1 FROM t_vipcardcode WHERE F_VipID = @iVipID AND F_VipCardID = @iVipCardID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 2 AND @sErrorMsg = '�û�Ա������', '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
--
DELETE FROM t_vipcard WHERE F_ID = @iVipCardID;

SELECT '-------------------- Case3:��Ա��ID������ -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iVipID = 1;
SET @iVipCardID = -1;
SET @sCompanySN = '668866';
-- 
CALL SP_VipCardCode_Create(@iErrorCode, @sErrorMsg, @iVipID, @iVipCardID, @sCompanySN);
-- 
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 2 AND @sErrorMsg = '�û�Ա��������', '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';