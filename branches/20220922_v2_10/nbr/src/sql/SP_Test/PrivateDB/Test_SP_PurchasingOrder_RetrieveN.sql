SELECT '++++++++++++++++++ Test_SP_PurchasingOrder_RetrieveN.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: ��ѯ״̬Ϊ0�Ĳɹ��� -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = 0;
SET @string1 = '';
SET @iStaffID = -1;
SET @dtDate1 ='';
SET @dtDate2 = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE1 Testing Result';

SELECT '-------------------- Case2: ��ѯ״̬Ϊ1�Ĳɹ��� -------------------------' AS 'Case2';

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = 1;
SET @string1 = '';
SET @iStaffID = -1;
SET @dtDate1 ='';
SET @dtDate2 = '';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE2 Testing Result';

SELECT '-------------------- Case3: ��ѯ״̬Ϊ2�Ĳɹ��� -------------------------' AS 'Case3';

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = 2;
SET @string1 = '';
SET @iStaffID = -1;
SET @dtDate1 ='';
SET @dtDate2 = '';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE3 Testing Result';

SELECT '-------------------- Case4: ��ѯ״̬Ϊ3�Ĳɹ��� -------------------------' AS 'Case4';

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = 3;
SET @string1 = '';
SET @iStaffID = -1;
SET @dtDate1 ='';
SET @dtDate2 = '';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE4 Testing Result';

SELECT '-------------------- Case5: ��ѯ״̬��Ϊ4�Ĳɹ���,����-1 -------------------------' AS 'Case5';

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = -1;
SET @string1 = '';
SET @iStaffID = -1;
SET @dtDate1 ='';
SET @dtDate2 = '';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE5 Testing Result';

SELECT '-------------------- Case6: ��ѯ״̬Ϊ4�Ĳɹ��������ش�����7 -------------------------' AS 'Case6';

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = 4;
SET @string1 = '';
SET @iStaffID = -1;
SET @dtDate1 ='';
SET @dtDate2 = '';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'CASE6 Testing Result';

SELECT '-------------------- Case7: ��ѯδ����Ĳɹ�����״̬�����ش�����7 -------------------------' AS 'Case7';

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = 999;
SET @string1 = '';
SET @iStaffID = -1;
SET @dtDate1 ='';
SET @dtDate2 = '';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'CASE7 Testing Result';

SELECT '-------------------- Case8: ����string1����ֵ����ģ����ѯ��������Ʒ���ƣ� -------------------------' AS 'Case8';

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = -1;
SET @string1 = '����';
SET @iStaffID = -1;
SET @dtDate1 ='';
SET @dtDate2 = '';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE8 Testing Result';

SELECT '-------------------- Case9: ����string1����ֵ����ģ����ѯ�����ݹ�Ӧ�����ƣ� -------------------------' AS 'Case9';

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = -1;
SET @string1 = 'Ĭ�Ϲ�Ӧ��';
SET @iStaffID = -1;
SET @dtDate1 ='';
SET @dtDate2 = '';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE9 Testing Result';

SELECT '-------------------- Case10: ����string1����ֵ����ģ����ѯ��������С���ȵĲɹ���������,����10λ�� -------------------------' AS 'Case10';

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = -1;
SET @string1 = 'CG20190604';
SET @iStaffID = -1;
SET @dtDate1 ='';
SET @dtDate2 = '';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE10 Testing Result';

SELECT '-------------------- Case11: ����string1����ֵ����ģ����ѯ��������С���ȵĲɹ���������ƥ�������Ʒ����ƥ����߹�Ӧ������ƥ��Ŀ��Բ�ѯ��,����10λ�� -------------------------' AS 'Case11';

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = -1;
SET @string1 = 'CG20190604';
SET @iStaffID = -1;
SET @dtDate1 ='';
SET @dtDate2 = '';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE11 Testing Result';

SELECT '-------------------- Case12: ����string1����ֵ����ģ����ѯ����ƥ��� -------------------------' AS 'Case12';

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = -1;
SET @string1 = '-999999999';
SET @iStaffID = -1;
SET @dtDate1 ='';
SET @dtDate2 = '';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE12 Testing Result';

SELECT '-------------------- Case13: ����ʱ��ν��в�ѯ��һ�죩 -------------------------' AS 'Case13';

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = -1;
SET @string1 = '';
SET @iStaffID = -1;
SET @dtDate1 ='2016/12/06 00:00:00';
SET @dtDate2 = '2016/12/06 23:59:59';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE13 Testing Result';

SELECT '-------------------- Case14: ����ʱ��ν��в�ѯ��һ���ڣ� -------------------------' AS 'Case14';

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = -1;
SET @string1 = '';
SET @iStaffID = -1;
SET @dtDate1 ='2016/12/06 00:00:00';
SET @dtDate2 = '2016/12/13 23:59:59';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE14 Testing Result';

SELECT '-------------------- Case15: ����ʱ��ν��в�ѯ���޼�¼ʱ��Σ� -------------------------' AS 'Case15';

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = -1;
SET @string1 = '';
SET @iStaffID = -1;
SET @dtDate1 ='1998/12/06 00:00:00';
SET @dtDate2 = '1999/12/13 23:59:59';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE15 Testing Result';

SELECT '-------------------- Case16: ����ʱ��ν��в�ѯ���п�ʼʱ��,�޽���ʱ�䣩 -------------------------' AS 'Case16';

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = -1;
SET @string1 = '';
SET @iStaffID = -1;
SET @dtDate1 ='2016/12/07 00:00:00';
SET @dtDate2 = '';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE16 Testing Result';

SELECT '-------------------- Case17: ����ʱ��ν��в�ѯ���н���ʱ��,�޿�ʼʱ�䣩 -------------------------' AS 'Case17';

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = -1;
SET @string1 = '';
SET @iStaffID = -1;
SET @dtDate1 ='';
SET @dtDate2 = '2016/12/07 00:00:00';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE17 Testing Result';

SELECT '-------------------- Case18: ���ݲ����˵�ID��iStaffID=1����ѯ�ɹ����� -------------------------' AS 'Case18';

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = -1;
SET @string1 = '';
SET @iStaffID = 6;
SET @dtDate1 ='';
SET @dtDate2 = '';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE18 Testing Result';

SELECT '-------------------- Case19: ���ݲ����˵�ID��iStaffID=-999999,ID�����ڣ�����0���ɹ���������ѯ�ɹ�����  -------------------------' AS 'Case19';

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = -1;
SET @string1 = '';
SET @iStaffID = -999999;
SET @dtDate1 ='';
SET @dtDate2 = '';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE19 Testing Result';

SELECT '-------------------- Case20: ȫ������ѯ  -------------------------' AS 'Case20';

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = 1;
SET @string1 = '��Ӧ��';
SET @iStaffID = 1;
SET @dtDate1 ='2016/12/06 00:00:00';
SET @dtDate2 = '2016/12/13 23:59:59';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE20 Testing Result';

SELECT '-------------------- Case21: ��������ѯ  -------------------------' AS 'Case21';

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = -1;
SET @string1 = '';
SET @iStaffID = -1;
SET @dtDate1 ='';
SET @dtDate2 = '';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE21 Testing Result';

SELECT '-------------------- Case22: ����string1����ֵ����ģ����ѯ��С����С���ȵĲɹ���������,С��10λ�� -------------------------' AS 'Case22';

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = -1;
SET @string1 = 'CG2019060';
SET @iStaffID = -1;
SET @dtDate1 ='';
SET @dtDate2 = '';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = 0, '���Գɹ�', '����ʧ��') AS 'CASE22 Testing Result';

SELECT '-------------------- Case23: ����string1����ֵ����ģ����ѯ��������󳤶ȵĲɹ���������,����20λ�� -------------------------' AS 'Case23';

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = -1;
SET @string1 = 'CG20190604123451234512345';
SET @iStaffID = -1;
SET @dtDate1 ='';
SET @dtDate2 = '';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT @iErrorCode;

SELECT IF(@iErrorCode = 0 AND @iTotalRecord = 0, '���Գɹ�', '����ʧ��') AS 'CASE23 Testing Result';

SELECT '-------------------- Case24: ����string1����ֵ����ģ����ѯ��������󳤶ȵĲɹ���������,����20λ�� -------------------------' AS 'Case24';

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = -1;
SET @string1 = 'CG201906041234512345';
SET @iStaffID = -1;
SET @dtDate1 ='';
SET @dtDate2 = '';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT @iErrorCode;

SELECT IF(@iErrorCode = 0 AND @iTotalRecord = 0, '���Գɹ�', '����ʧ��') AS 'CASE24 Testing Result';

SELECT '-------------------- Case25:����string1����_�������ַ�����ģ������  -------------------------' AS 'Case25';

INSERT INTO t_purchasingordercommodity (F_PurchasingOrderID, F_CommodityID, F_CommodityNO, F_CommodityName, F_BarcodeID, F_PackageUnitID, F_PriceSuggestion)
VALUES (1,5,200,'������2_3��',1,1,11.1);
SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @iStatus = -1;
SET @string1 = '_';
SET @iStaffID = -1;
SET @dtDate1 ='';
SET @dtDate2 = '';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord > 0, '���Գɹ�', '����ʧ��') AS 'CASE25 Testing Result';

DELETE FROM t_purchasingordercommodity WHERE F_CommodityName = '������2_3��';


SELECT '-------------------- Case26: �����ŵ�ID��ѯ�ɹ��� -------------------------' AS 'Case26';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 2;
SET @iStatus = -1;
SET @string1 = '';
SET @iStaffID = -1;
SET @dtDate1 ='';
SET @dtDate2 = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord =0;

CALL SP_PurchasingOrder_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @iStatus, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE26 Testing Result';