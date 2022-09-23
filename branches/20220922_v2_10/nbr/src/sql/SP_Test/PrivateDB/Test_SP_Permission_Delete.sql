SELECT '++++++++++++++++++ Test_SP_Permission_Delete.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: ɾ�����н�ɫ��ʹ��Ȩ�ޣ�������Ϊ7 -------------------------' AS 'Case1';

SET @iID = 1;
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @bForceDelete = 0;

CALL SP_Permission_Delete(@iErrorCode, @sErrorMsg, @iID, @bForceDelete);

SELECT @sErrorMsg;
SELECT 1 FROM T_Permission WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

SELECT '-------------------- Case2: ɾ��û�н�ɫ��ʹ�õ�Ȩ�� -------------------------' AS 'Case2';

SET @sName = '�޸�1';
SET @sRemark = '�޸Ļ�Ա';
SET @bForceDelete = 0;
SET @iErrorCode = 0;
SET @sErrorMsg = '';

INSERT INTO T_Permission (F_Name, F_Remark)
VALUES (@sName, @sRemark);

SET @iID = LAST_INSERT_ID();

CALL SP_Permission_Delete(@iErrorCode, @sErrorMsg, @iID, @bForceDelete);

SELECT @sErrorMsg;
SELECT 1 FROM T_Permission WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

SELECT '-------------------- Case3: ��bForceDelete=1��ʱ�����ǿ��ɾ�� -------------------------' AS 'Case3';

INSERT INTO T_Permission (F_SP, F_Name, F_Domain, F_Remark) VALUES ('SP_Barcodes','���������',"������","");

SET @iID = last_insert_id();
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @bForceDelete = 1;

CALL SP_Permission_Delete(@iErrorCode, @sErrorMsg, @iID, @bForceDelete);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM T_Permission WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';