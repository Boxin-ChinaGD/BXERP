SELECT '++++++++++++++++++ Test_SP_Permission_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: ��Ӳ��ظ���Ȩ�� -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sSP = 'SP_VIP_Delete';
SET @sName = 'ɾ��cz';
SET @sDomain = '��Ա����';
SET @sRemark = '��Ա�Ĵ���';

CALL SP_Permission_Create(@iErrorCode, @sErrorMsg, @sSP, @sName, @sDomain, @sRemark);

SELECT @sErrorMsg;
SELECT 1 FROM t_Permission WHERE F_SP = @sSP AND F_Name = @sName AND F_Domain = @sDomain AND F_Remark = @sRemark;
SELECT @iErrorCode;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

DELETE FROM t_Permission WHERE F_ID = last_insert_id();

SELECT '-------------------- Case2: ����ظ���Ȩ�� -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sSP = 'SP_VIP_Delete';
SET @sName = '���������';
SET @sDomain = '��Ա����';
SET @sRemark = '��Ա�Ĵ���';

CALL SP_Permission_Create(@iErrorCode, @sErrorMsg, @sSP, @sName, @sDomain, @sRemark);

SELECT @sErrorMsg;
SELECT 1 FROM t_Permission  WHERE F_SP = @sSP AND F_Name = @sName AND F_Domain = @sDomain AND F_Remark = @sRemark;
SELECT IF(found_rows() = 0 AND @iErrorCode = 1, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

DELETE FROM t_Permission WHERE F_ID = last_insert_id();