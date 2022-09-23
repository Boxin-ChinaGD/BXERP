SELECT '++++++++++++++++++ Test_SP_PurchasingOrder_RetrieveNByFields.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: ������Ʒ���Ʋ�ѯ��Ʒ -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '����';
SET @iStaffID = 0;
SET @dtDate1 ='2000/01/01 00:00:00';
SET @dtDate2 = "2020/01/31 23:59:59";
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_PurchasingOrder_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'case1 Testing Result';

SELECT '-------------------- Case2: ���ݹ�Ӧ�����Ʋ�ѯ��Ʒ -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = 'Ĭ�Ϲ�Ӧ��';
SET @iStaffID = 0;
SET @dtDate1 ='2000/01/01 00:00:00';
SET @dtDate2 = "2020/01/31 23:59:59";
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_PurchasingOrder_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'case2 Testing Result';

SELECT '-------------------- Case3: ������Ʒ���ƺ͹�Ӧ�����Ʋ�ѯ��Ʒ -------------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = 'ĳĳ';
SET @iStaffID = 0;
SET @dtDate1 ='2000/01/01 00:00:00';
SET @dtDate2 = "2020/01/31 23:59:59";
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_PurchasingOrder_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'case3 Testing Result';

SELECT '-------------------- Case4: ����ʱ��β�ѯ�ɹ���������ѯһ�� -------------------------' AS 'Case4';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '';
SET @iStaffID = 0;
SET @dtDate1 ='2016/12/06 00:00:00';
SET @dtDate2 = "2016/12/06 23:59:59";
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_PurchasingOrder_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'case4 Testing Result';

SELECT '-------------------- Case5: ����ʱ��β�ѯ�ɹ���������ѯһ������ -------------------------' AS 'Case5';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '';
SET @iStaffID = 0;
SET @dtDate1 ='2016/12/06 00:00:00';
SET @dtDate2 = "2016/12/12 23:59:59";
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_PurchasingOrder_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'case5 Testing Result';

SELECT '-------------------- Case6: ����ʱ��β�ѯ�ɹ���������ѯһ��û����Ϣ��ʱ��� -------------------------' AS 'Case6';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '';
SET @string2 = '';
SET @dtDate1 ='2000/12/06 00:00:00';
SET @dtDate2 = "2010/12/12 23:59:59";
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_PurchasingOrder_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'case6 Testing Result';

SELECT '-------------------- Case7: ���ݲ����˵�ID��iStaffID=1����ѯ�ɹ����� -------------------------' AS 'Case7';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '';
SET @iStaffID = 2;
SET @dtDate1 =NULL;
SET @dtDate2 = NULL;
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_PurchasingOrder_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'case7 Testing Result';

SELECT '-------------------- Case8: ���ݲ����˵�ID��iStaffID=-999999,ID�����ڣ�����0���ɹ���������ѯ�ɹ����� -------------------------' AS 'Case8';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '';
SET @iStaffID = -999999;
SET @dtDate1 ='2000/01/01 00:00:00';
SET @dtDate2 = "2020/01/31 23:59:59";
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_PurchasingOrder_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'case8 Testing Result';

SELECT '-------------------- Case9: ����ʱ��ν��в�ѯ�ɹ�����.��ѯ2019/3/13 10:35:47�Ժ�Ĳɹ����� -------------------------' AS 'Case9';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '';
SET @iStaffID = 0;
SET @dtDate1 = '2019/3/13 10:35:47';
SET @dtDate2 = NULL;
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_PurchasingOrder_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'case9 Testing Result';

SELECT '-------------------- Case10: ����ʱ��ν��в�ѯ�ɹ�����.��ѯ2016/12/6 0:00:00֮ǰ�Ĳɹ����� -------------------------' AS 'Case10';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '';
SET @iStaffID = 0;
SET @dtDate1 = NULL;
SET @dtDate2 = '2016/12/6 0:00:00';
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_PurchasingOrder_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'case10 Testing Result';

SELECT '-------------------- Case11: ���ݲ����ڵĲɹ�����ID���в�ѯ -------------------------' AS 'Case11';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '-99999999';
SET @iStaffID = 0;
SET @dtDate1 ='2000/01/01 00:00:00';
SET @dtDate2 = "2020/01/31 23:59:59";
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_PurchasingOrder_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'case11 Testing Result';

SELECT '-------------------- Case12: ����string1(�ɹ�����IDƥ�������Ʒ����ƥ����߹�Ӧ������ƥ��Ŀ��Բ�ѯ��)���в�ѯ -------------------------' AS 'Case12';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '1';
SET @iStaffID = 0;
SET @dtDate1 ='2000/01/01 00:00:00';
SET @dtDate2 = "2020/01/31 23:59:59";
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_PurchasingOrder_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'case12 Testing Result';

SELECT '-------------------- Case13: ���ݿ��������в�ѯ -------------------------' AS 'Case13';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '';
SET @iStaffID = 0;
SET @dtDate1 ='2000/01/01 00:00:00';
SET @dtDate2 = "2020/01/31 23:59:59";
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_PurchasingOrder_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'case13 Testing Result';

SELECT '-------------------- Case14: ����ȫ�������в�ѯ -------------------------' AS 'Case14';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '5';
SET @iStaffID = 5;
SET @dtDate1 ='2000/01/01 00:00:00';
SET @dtDate2 = "2020/01/31 23:59:59";
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_PurchasingOrder_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'case14 Testing Result';

SELECT '-------------------- Case15:����string1����_�������ַ�����ģ������ -------------------------' AS 'Case15';
INSERT INTO t_purchasingordercommodity (F_PurchasingOrderID, F_CommodityID, F_CommodityNO, F_CommodityName, F_BarcodeID, F_PackageUnitID, F_PriceSuggestion)
VALUES (1,5,200,'������2_3��',1,1,11.1);
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1 = '_';
SET @iStaffID = 0;
SET @dtDate1 ='2000/01/01 00:00:00';
SET @dtDate2 = "2020/01/31 23:59:59";
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_PurchasingOrder_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iStaffID, @dtDate1, @dtDate2, @iPageIndex,@iPageSize,@iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord > 0, '���Գɹ�', '����ʧ��') AS 'CASE15 Testing Result';

DELETE FROM t_purchasingordercommodity WHERE F_CommodityName = '������2_3��';