SELECT '++++++++++++++++++ Test_SP_Warehousing_RetrieveN.sql ++++++++++++++++++++';

SELECT '------------------- CASE1;û�в�����ѯ���� -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = -1;
SET @iProviderID = -1;
SET @iwarehouseID = -1;
SET @istaffID = -1;
SET @purchasingOrderID= -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveN(@iErrorCode, @sErrorMsg, @iID, @iProviderID, @iwarehouseID, @istaffID, @purchasingOrderID,@iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(found_rows() > 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

SELECT '------------------- CASE2:����ID��ѯ -------------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;
SET @iProviderID = -1;
SET @iwarehouseID = -1;
SET @istaffID = -1;
SET @purchasingOrderID= -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveN(@iErrorCode, @sErrorMsg, @iID, @iProviderID, @iwarehouseID, @istaffID, @purchasingOrderID,@iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(found_rows() > 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

SELECT '------------------- case3:���ݲֿ�ID�� -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = -1;
SET @iProviderID = -1;
SET @iwarehouseID = 1;
SET @istaffID = -1;
SET @purchasingOrderID= -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveN(@iErrorCode, @sErrorMsg, @iID, @iProviderID, @iwarehouseID, @istaffID, @purchasingOrderID,@iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(found_rows() > 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';

SELECT '--------------------- case4������ҵ��ԱID�� -------------------------' AS 'Case4';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = -1;
SET @iProviderID = -1;
SET @iwarehouseID = -1;
SET @istaffID = 1;
SET @purchasingOrderID= -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveN(@iErrorCode, @sErrorMsg, @iID, @iProviderID, @iwarehouseID, @istaffID, @purchasingOrderID,@iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(found_rows() > 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';

SELECT '----------------------- CASE5:���ݲɹ������� -------------------------' AS 'Case5';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = -1;
SET @iProviderID = -1;
SET @iwarehouseID = -1;
SET @istaffID = -1;
SET @purchasingOrderID= 1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveN(@iErrorCode, @sErrorMsg, @iID, @iProviderID, @iwarehouseID, @istaffID, @purchasingOrderID,@iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(found_rows() > 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';

SELECT '----------------------- case6: ���ݲɹ�����ID�Ͳֿ�ID�� -------------------------' AS 'case6';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = -1;
SET @iProviderID = -1;
SET @iwarehouseID = 1;
SET @istaffID = -1;
SET @purchasingOrderID= 1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveN(@iErrorCode, @sErrorMsg, @iID, @iProviderID, @iwarehouseID, @istaffID, @purchasingOrderID,@iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(found_rows() > 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case6 Testing Result';

SELECT '----------------------- case7: ����ҵ��Ա���ֿ�ID�� -------------------------' AS 'case7';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = -1;
SET @iProviderID = -1;
SET @iwarehouseID = 1;
SET @istaffID = 1;
SET @purchasingOrderID= -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveN(@iErrorCode, @sErrorMsg, @iID, @iProviderID, @iwarehouseID, @istaffID, @purchasingOrderID,@iPageIndex, @iPageSize, @iTotalRecord);

SELECT @sErrorMsg;
SELECT @iTotalRecord;
SELECT IF(found_rows() > 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case7 Testing Result';

SELECT '----------------------- case8�����ݲɹ�������ID�� -------------------------' AS 'case8';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 5;
SET @iProviderID = -1;
SET @iwarehouseID = -1;
SET @istaffID = -1;
SET @purchasingOrderID= 2;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveN(@iErrorCode, @sErrorMsg, @iID, @iProviderID, @iwarehouseID, @istaffID, @purchasingOrderID,@iPageIndex, @iPageSize, @iTotalRecord);

SELECT @sErrorMsg;
SELECT @iTotalRecord;

SELECT IF(found_rows() > 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case8 Testing Result';

SELECT '----------------------- case9:���ݲɹ�����ID���ֿ�ID��ҵ��ԱID��; -------------------------' AS 'case9';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = -1;
SET @iProviderID = -1;
SET @iwarehouseID = 1;
SET @istaffID = 3;
SET @purchasingOrderID= 1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveN(@iErrorCode, @sErrorMsg, @iID, @iProviderID, @iwarehouseID, @istaffID, @purchasingOrderID,@iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(found_rows() > 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case9 Testing Result';

SELECT '----------------------- case10:��������������ѯ -------------------------' AS 'case9';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;
SET @iProviderID = 3;
SET @iwarehouseID = 1;
SET @istaffID = 3;
SET @purchasingOrderID= 1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveN(@iErrorCode, @sErrorMsg, @iID, @iProviderID, @iwarehouseID, @istaffID, @purchasingOrderID,@iPageIndex, @iPageSize, @iTotalRecord);

SELECT @sErrorMsg;
SELECT @iTotalRecord;
SELECT IF(found_rows() > 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case10 Testing Result';

SELECT '------------------- CASE11:���ݲ����ڵ�ID��ѯ -------------------------' AS 'Case11';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 999;
SET @iProviderID = -1;
SET @iwarehouseID = -1;
SET @istaffID = -1;
SET @purchasingOrderID= -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveN(@iErrorCode, @sErrorMsg, @iID, @iProviderID, @iwarehouseID, @istaffID, @purchasingOrderID,@iPageIndex, @iPageSize, @iTotalRecord);

SELECT @sErrorMsg;
SELECT @iTotalRecord;

SELECT IF(@iTotalRecord = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case11 Testing Result';

SELECT '------------------- case12:���ݲ����ڵĲֿ�ID�� -------------------------' AS 'Case12';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = -1;
SET @iProviderID = -1;
SET @iwarehouseID = 999;
SET @istaffID = -1;
SET @purchasingOrderID= -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveN(@iErrorCode, @sErrorMsg, @iID, @iProviderID, @iwarehouseID, @istaffID, @purchasingOrderID,@iPageIndex, @iPageSize, @iTotalRecord);

SELECT @sErrorMsg;
SELECT @iTotalRecord;

SELECT IF(@iTotalRecord = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case12 Testing Result';

SELECT '--------------------- case13�����ݲ����ڵ�ҵ��ԱID�� -------------------------' AS 'Case13';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = -1;
SET @iProviderID = -1;
SET @iwarehouseID = -1;
SET @istaffID = 999;
SET @purchasingOrderID= -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveN(@iErrorCode, @sErrorMsg, @iID, @iProviderID, @iwarehouseID, @istaffID, @purchasingOrderID,@iPageIndex, @iPageSize, @iTotalRecord);

SELECT @sErrorMsg;
SELECT @iTotalRecord;

SELECT IF(@iTotalRecord = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case13 Testing Result';


SELECT '----------------------- CASE14:���ݲ����ڵĲɹ������� -------------------------' AS 'Case14';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = -1;
SET @iProviderID = -1;
SET @iwarehouseID = -1;
SET @istaffID = -1;
SET @purchasingOrderID= 999;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveN(@iErrorCode, @sErrorMsg, @iID, @iProviderID, @iwarehouseID, @istaffID, @purchasingOrderID,@iPageIndex, @iPageSize, @iTotalRecord);

SELECT @sErrorMsg;
SELECT @iTotalRecord;

SELECT IF(@iTotalRecord = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case14 Testing Result';

SELECT '----------------------- CASE15:���ݹ�Ӧ��ID�� -------------------------' AS 'Case15';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = -1;
SET @iProviderID = 1;
SET @iwarehouseID = -1;
SET @istaffID = -1;
SET @purchasingOrderID= -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveN(@iErrorCode, @sErrorMsg, @iID, @iProviderID, @iwarehouseID, @istaffID, @purchasingOrderID,@iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(found_rows() > 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case15 Testing Result';

SELECT '----------------------- CASE16:���ݹ�Ӧ��ID�Ͳɹ������� -------------------------' AS 'Case16';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = -1;
SET @iProviderID = 1;
SET @iwarehouseID = -1;
SET @istaffID = -1;
SET @purchasingOrderID= 2;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveN(@iErrorCode, @sErrorMsg, @iID, @iProviderID, @iwarehouseID, @istaffID, @purchasingOrderID,@iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(found_rows() > 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case16 Testing Result';

SELECT '----------------------- CASE17:���ݲ����ڵĹ�Ӧ��ID�� -------------------------' AS 'Case17';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = -1;
SET @iProviderID = 999;
SET @iwarehouseID = -1;
SET @istaffID = -1;
SET @purchasingOrderID= -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveN(@iErrorCode, @sErrorMsg, @iID, @iProviderID, @iwarehouseID, @istaffID, @purchasingOrderID,@iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(found_rows() > 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case17 Testing Result';