SELECT '++++++++++++++++++ Test_SP_Inventory_RetrieveN.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: ��״̬Ϊ0ʱ -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord =0;

CALL SP_Inventory_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus,@iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE1 Testing Result';

SELECT '-------------------- Case2: ��״̬Ϊ1ʱ -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = 1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord =0;

CALL SP_Inventory_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus,@iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE2 Testing Result';

SELECT '-------------------- Case3: ��״̬Ϊ2ʱ -------------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = 2;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord =0;

CALL SP_Inventory_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus,@iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE3 Testing Result';

SELECT '-------------------- Case4: ��״̬Ϊ-1ʱ -------------------------' AS 'Case4';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord =0;

CALL SP_Inventory_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus,@iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF( @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE4 Testing Result';

SELECT '-------------------- Case5: �����ŵ�ID���в�ѯ -------------------------' AS 'Case5';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 2;
SET @iStatus = -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord =0;

CALL SP_Inventory_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus,@iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE5 Testing Result';

SELECT '-------------------- Case6: �����ŵ�ID��״̬���в�ѯ -------------------------' AS 'Case6';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 2;
SET @iStatus = 1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord =0;

CALL SP_Inventory_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus,@iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE6 Testing Result';