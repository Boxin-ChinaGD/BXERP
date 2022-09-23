SELECT '++++++++++++++++++ Test_SP_Warehousing_RetrieveNByFields.sql ++++++++++++++++++++';

SELECT '++++++++++++++++++ CASE1;������С���ȵ���ⵥ����(����10λ)ģ����ѯ��ⵥ ++++++++++++++++++++';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1= 'RK20190605';
SET @iShopID = -1;
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iShopID, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord > 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

SELECT '++++++++++++++++++ CASE2;������Ʒ����ģ����ѯ��ⵥ ++++++++++++++++++++';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1= 'A����18';
SET @iShopID = -1;
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iShopID, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

-- 
SELECT '++++++++++++++++++ CASE3;���ݹ�Ӧ������ģ����ѯ��ⵥ ++++++++++++++++++++';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1= 'Ĭ��';
SET @iShopID = -1;
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iShopID, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';

-- 
SELECT '++++++++++++++++++ CASE4;���޶�״̬�¸��ݹ�Ӧ������ģ����ѯ��ⵥ ++++++++++++++++++++';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1= 'Ĭ��';
SET @iShopID = -1;
SET @sStaffID = -1;
SET @iStatus = 1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iShopID, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';

-- 
SELECT '++++++++++++++++++ CASE5;���޶�������ID�¸��ݹ�Ӧ������ģ����ѯ��ⵥ ++++++++++++++++++++';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1= 'Ĭ��';
SET @iShopID = -1;
SET @sStaffID = 3;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iShopID, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';

-- 
SELECT '++++++++++++++++++ CASE6;���޶���Ӧ��ID�¸�����С���ȵ���ⵥ����(10λ)ģ����ѯ��ⵥ ++++++++++++++++++++';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1= 'RK20190605';
SET @iShopID = -1;
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = 2; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iShopID, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case6 Testing Result';

-- 
SELECT '++++++++++++++++++ CASE7;���޶�����ʱ����¸��ݹ�Ӧ�����Ʋ�ģ��ѯ��ⵥ ++++++++++++++++++++';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1= '����';
SET @iShopID = -1;
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = '2017/10/8 1:01:01';
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iShopID, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case7 Testing Result';

-- 
SELECT '++++++++++++++++++ CASE8;����״̬�Ľ���������������string1�� ++++++++++++++++++++';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1= '';
SET @iShopID = -1;
SET @sStaffID = -1;
SET @iStatus = 1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iShopID, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case8 Testing Result';

-- 
SELECT '++++++++++++++++++ CASE9;���ݾ�����ID�Ľ���������������string1) ++++++++++++++++++++';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1= '';
SET @iShopID = -1;
SET @sStaffID = 3;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iShopID, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case9 Testing Result';

-- 
SELECT '++++++++++++++++++ CASE10;���ݹ�Ӧ��ID�Ľ���������������string1�� ++++++++++++++++++++';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1= '';
SET @iShopID = -1;
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = 10; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iShopID, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case10 Testing Result';

-- 
SELECT '++++++++++++++++++ CASE11;���ݴ��봴��ʱ��εĽ���������������string1�� ++++++++++++++++++++';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1= '';
SET @iShopID = -1;
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = '2019/1/8 1:01:00';
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iShopID, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case11 Testing Result';

SELECT '++++++++++++++++++ CASE12:��ֵΪ�����ѯȫ�� ++++++++++++++++++++';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1= '';
SET @iShopID = -1;
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iShopID, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case12 Testing Result';

SELECT '++++++++++++++++++ CASE13;С����С���ȵ���ⵥ����(С��10λ)ģ����ѯ��ⵥ ++++++++++++++++++++';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1= 'RK2019060';
SET @iShopID = -1;
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iShopID, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = 0, '���Գɹ�', '����ʧ��') AS 'Case13 Testing Result';


SELECT '++++++++++++++++++ CASE14;������󳤶ȵ���ⵥ����(����20λ)ģ����ѯ��ⵥ ++++++++++++++++++++';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1= 'RK20190605123451234512345';
SET @iShopID = -1;
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iShopID, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = 0, '���Գɹ�', '����ʧ��') AS 'Case14 Testing Result';

SELECT '++++++++++++++++++ CASE15;������󳤶ȵ���ⵥ����(����20λ)ģ����ѯ��ⵥ ++++++++++++++++++++';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1= 'RK201906051234512345';
SET @iShopID = -1;
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iShopID, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = 0, '���Գɹ�', '����ʧ��') AS 'Case15 Testing Result';


SELECT '++++++++++++++++++ CASE16;����10λ���µĲɹ�����ģ����ѯ��ⵥ ++++++++++++++++++++';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1= 'CG2019';
SET @iShopID = -1;
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iShopID, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = 0, '���Գɹ�', '����ʧ��') AS 'Case16 Testing Result';

SELECT '++++++++++++++++++ CASE17;����20λ���ϵĲɹ�����ģ����ѯ��ⵥ ++++++++++++++++++++';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1= 'CG20190604000700000000';
SET @iShopID = -1;
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iShopID, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = 0, '���Գɹ�', '����ʧ��') AS 'Case18 Testing Result';

SELECT '++++++++++++++++++ CASE18;����10-20λ�Ĳɹ�����ģ����ѯ��ⵥ ++++++++++++++++++++';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1= 'CG201906040007';
SET @iShopID = -1;
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iShopID, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case18 Testing Result';



SELECT '-------------------- Case19:����string1����_�����������ַ�����ģ������ -------------------------' AS 'Case19';
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (5, 4, 20, 1, '���һ��_()�Ϳ���', 6, 3, 10, now(), 36, now(), now(), now(), 10);
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1= '_(';
SET @iShopID = -1;
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iShopID, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord > 0, '���Գɹ�', '����ʧ��') AS 'CASE19 Testing Result';

DELETE FROM t_warehousingcommodity WHERE F_CommodityName = '���һ��_()�Ϳ���';

-- 
SELECT '++++++++++++++++++ CASE20;�����ŵ�IDģ����ѯ��ⵥ ++++++++++++++++++++';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @string1= '';
SET @iShopID = 2;
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Warehousing_RetrieveNByFields(@iErrorCode, @sErrorMsg, @string1, @iShopID, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case20 Testing Result';