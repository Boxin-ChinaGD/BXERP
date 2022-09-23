SELECT '++++++++++++++++++ Test_SP_POS_Reset.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:����һ���Ѿ�ɾ��POS�� -------------------------' AS 'Case1';
INSERT INTO t_pos (F_POS_SN, F_ShopID, F_pwdEncrypted, F_Salt, F_PasswordInPOS, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('T109179800012', 1, NULL, 'B1AFC07474C37C5AEC4199ED28E09705', NULL, 1, '2019-03-07 10:01:38', '2019-03-07 10:01:38');

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = last_insert_id();
SET @iReturnSalt = 1;

CALL SP_POS_Reset (@iErrorCode, @sErrorMsg, @iID, @iReturnSalt);

SELECT 1 FROM t_pos WHERE F_Status = 0 AND F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

DELETE FROM t_pos WHERE F_ID = @iID;

SELECT '-------------------- Case2:����һ������״̬POS�� -------------------------' AS 'Case2';
INSERT INTO t_pos (F_POS_SN, F_ShopID, F_pwdEncrypted, F_Salt, F_PasswordInPOS, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('T109179800012', 1, NULL, 'B1AFC07474C37C5AEC4199ED28E09705', NULL, 0, '2019-03-07 10:01:38', '2019-03-07 10:01:38');

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = last_insert_id();
SET @iReturnSalt = 1;

CALL SP_POS_Reset (@iErrorCode, @sErrorMsg, @iID, @iReturnSalt);

SELECT 1 FROM t_pos WHERE F_Status = 0 AND F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

DELETE FROM t_pos WHERE F_ID = @iID;

SELECT '-------------------- Case3:����һ��������POS�� -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = -99;
SET @iReturnSalt = 1;

CALL SP_POS_Reset (@iErrorCode, @sErrorMsg, @iID, @iReturnSalt);

SELECT 1 FROM t_pos WHERE F_Status = 0 AND F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';