SELECT '++++++++++++++++++ Test_SP_POS_Retrieve1ByFields.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: ͨ��POS_SN���ҳ���ӦPOS -------------------------' AS 'Case1';
SET @SN = 'SN121567892';
INSERT INTO t_pos (F_POS_SN, F_ShopID,F_Salt,F_PasswordInPOS)VALUES (@SN, 1, '11231313','000000');
SET @id = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @POS_SN = @SN;
SET @iReturnSalt = 1;

CALL SP_POS_Retrieve1BySN(@iErrorCode, @sErrorMsg, @POS_SN, @iReturnSalt);
SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

DELETE FROM t_pos WHERE F_ID = @id;

SELECT '-------------------- Case2: ���벻���ڵ�POS_SN -------------------------' AS 'Case2';
SET @SN = '�����ڵ�SN';
SET @sErrorMsg = '';
SET @iReturnSalt = 1;
CALL SP_POS_Retrieve1BySN(@iErrorCode, @sErrorMsg, @POS_SN, @iReturnSalt);
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';


SELECT '-------------------- Case3: �����Ѿ���ɾ����POS_SN -------------------------' AS 'Case3';

SET @SN = 'SN2141920';
SET @sErrorMsg = '';
SET @iReturnSalt = 1;
CALL SP_POS_Retrieve1BySN(@iErrorCode, @sErrorMsg, @POS_SN, @iReturnSalt);
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';