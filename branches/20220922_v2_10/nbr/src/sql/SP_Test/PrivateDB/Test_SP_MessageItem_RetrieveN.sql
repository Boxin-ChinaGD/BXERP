SELECT '++++++++++++++++++ Test_SP_MessageItem_RetrieveN.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:�������Ϊ������Ʒ��ѯ -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iMessageCategoryID = 8;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
--	SET @iShopID = 2;	

CALL SP_MessageItem_RetrieveN(@iErrorCode, @sErrorMsg, @iMessageCategoryID, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';



SELECT '-------------------- Case2:���벻���ڵ���Ϣ����ID����ѯʧ�� -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iMessageCategoryID = 100000000;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
--	SET @iShopID = 2;

CALL SP_MessageItem_RetrieveN(@iErrorCode, @sErrorMsg, @iMessageCategoryID, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '�������Ϣ����ID����ȷ�������ڸ���Ϣ����', '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';