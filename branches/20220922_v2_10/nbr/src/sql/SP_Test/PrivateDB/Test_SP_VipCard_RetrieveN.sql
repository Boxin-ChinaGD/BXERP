SELECT '++++++++++++++++++ Test_SP_VipCard_RetrieveN.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:��ѯ���� -------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_VipCard_RetrieveN(@iErrorCode, @sErrorMsg, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

DELETE FROM t_vipcard WHERE F_ID = @iID;