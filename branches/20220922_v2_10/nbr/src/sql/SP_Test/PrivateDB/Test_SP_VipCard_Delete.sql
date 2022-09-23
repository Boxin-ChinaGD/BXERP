SELECT '++++++++++++++++++ Test_SP_VipCard_Delete ++++++++++++++++++++';

SELECT '-------------------- Case1:����ɾ�� -------------------------' AS 'Case1';
INSERT INTO t_vipcard (F_Title, F_BackgroundColor, F_ClearBonusDay, F_ClearBonusDatetime, F_CreateDatetime)
VALUES ('��Ա��', '255,255,255;255,255,255', 3650, NULL, now());
SET @iID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_VipCard_Delete(@iErrorCode, @sErrorMsg, @iID);
SELECT @iErrorCode;
SELECT @sErrorMsg;

SELECT 1 FROM t_vipcard WHERE F_ID = @iID;
SELECT IF(FOUND_ROWS() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE1 Testing Result';