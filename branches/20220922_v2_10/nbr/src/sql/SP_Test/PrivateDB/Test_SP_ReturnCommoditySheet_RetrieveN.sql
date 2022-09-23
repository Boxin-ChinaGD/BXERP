SELECT '++++++++++++++++++ Test_SP_returnCommoditySheet_RetrieveN.sql ++++++++++++++++++++';
SELECT '-------------------- CASE1;������С���ȵ��˻�������ģ����ѯ��ⵥ(����10λ)��������@iTotalRecord >= 1 -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= 'TH20190605';
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord > 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

SELECT '-------------------- CASE2;������Ʒ��������ģ����ѯ��ⵥ��������@iTotalRecord>=1 -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= '����';
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord >= 1, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

SELECT '-------------------- CASE3;���ݾ�����IDģ����ѯ��ⵥ��������@iTotalRecord >= 1 -------------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= '';
SET @sStaffID = 6;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord >= 1, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';

SELECT '-------------------- CASE4;�����˻���״̬ģ����ѯ��ⵥ��������@iTotalRecord>=1 -------------------------' AS 'Case4';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= '';
SET @sStaffID = -1;
SET @iStatus = 1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord>=1, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';

SELECT '-------------------- CASE5;���ݹ�Ӧ��IDģ����ѯ��ⵥ��������@iTotalRecord>=1 -------------------------' AS 'Case5';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= '';
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = 3; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord >= 1, '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';

SELECT '-------------------- CASE6;���ݴ���ʱ��ģ����ѯ��ⵥ��������@iTotalRecord>=1 -------------------------' AS 'Case6';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= '';
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = '2017/12/5 0:00:00';
SET @dtEnd = '2017/12/7 0:00:00';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord >= 1, '���Գɹ�', '����ʧ��') AS 'Case6 Testing Result';

SELECT '-------------------- CASE7;���ݵ�����С���ȵ��˻����ź��˻���״̬ģ����ѯ��ⵥ(����10λ)��������@iTotalRecord>=1 -------------------------' AS 'Case7';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= 'TH20190605';
SET @sStaffID = -1;
SET @iStatus = 1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord >= 1, '���Գɹ�', '����ʧ��') AS 'Case7 Testing Result';

SELECT '-------------------- CASE8;���ݵ�����С���ȵ��˻����ź;�����ģ����ѯ��ⵥ(����10λ)��������@iTotalRecord>=1 -------------------------' AS 'Case8';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= 'TH20190605';
SET @sStaffID = 4;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord >= 1, '���Գɹ�', '����ʧ��') AS 'Case8 Testing Result';

SELECT '-------------------- CASE9;���ݵ�����С���ȵ��˻������ź͹�Ӧ��IDģ����ѯ��ⵥ(����10λ)��������@iTotalRecord>=1 -------------------------' AS 'Case9';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= 'TH20190605';
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = 3; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord >= 1, '���Գɹ�', '����ʧ��') AS 'Case9 Testing Result';

SELECT '-------------------- CASE10;���ݵ�����С���ȵ��˻����źʹ���ʱ��ģ����ѯ��ⵥ(����10λ)��������@iTotalRecord>=1 -------------------------' AS 'Case10';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= 'TH20190605';
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = '2017/12/5 0:00:00';
SET @dtEnd = '2017/12/7 0:00:00';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord >= 1, '���Գɹ�', '����ʧ��') AS 'Case10 Testing Result';

SELECT '-------------------- CASE11;�����˻���״̬���˻���������ģ����ѯ��ⵥ��������@iTotalRecord>=1 -------------------------' AS 'Case11';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= '';
SET @sStaffID = 4;
SET @iStatus = 1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord >= 1, '���Գɹ�', '����ʧ��') AS 'Case11 Testing Result';

SELECT '-------------------- CASE12;�����˻���״̬�͹�Ӧ��ģ����ѯ��ⵥ��������@iTotalRecord>=1 -------------------------' AS 'Case12';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= '';
SET @sStaffID = -1;
SET @iStatus = 1;
SET @iProviderID = 3; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord >= 1, '���Գɹ�', '����ʧ��') AS 'Case12 Testing Result';

SELECT '-------------------- CASE13;�����˻���״̬�ʹ���ʱ��ģ����ѯ��ⵥ��������@iTotalRecord>=1 -------------------------' AS 'Case13';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= '';
SET @sStaffID = -1;
SET @iStatus = 1;
SET @iProviderID = -1; 
SET @dtStart = '2017/12/5 0:00:00';
SET @dtEnd = '2017/12/7 0:00:00';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord >= 1, '���Գɹ�', '����ʧ��') AS 'Case13 Testing Result';

SELECT '-------------------- CASE14;�����˻���״̬����Ʒ��������ģ����ѯ��ⵥ��������@iTotalRecord>=1 -------------------------' AS 'Case14';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= '����';
SET @sStaffID = -1;
SET @iStatus = 1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord >= 1, '���Գɹ�', '����ʧ��') AS 'Case14 Testing Result';

SELECT '-------------------- CASE15;�����˻������˺͹�Ӧ��ģ����ѯ��ⵥ��������@iTotalRecord>=1 -------------------------' AS 'Case15';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= '';
SET @sStaffID = 2;
SET @iStatus = -1;
SET @iProviderID = 1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord >= 1, '���Գɹ�', '����ʧ��') AS 'Case15 Testing Result';

SELECT '-------------------- CASE16;�����˻������˺ʹ���ʱ��ģ����ѯ��ⵥ��������@iTotalRecord>=1 -------------------------' AS 'Case16';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= '';
SET @sStaffID = 2;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = '2017/12/5 0:00:00';
SET @dtEnd = '2017/12/7 0:00:00';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord >= 1, '���Գɹ�', '����ʧ��') AS 'Case16 Testing Result';

SELECT '-------------------- CASE17;�����˻������˺���Ʒ��������ģ����ѯ��ⵥ��������@iTotalRecord>=1 -------------------------' AS 'Case17';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= 'ѩ����';
SET @sStaffID = 2;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord >= 1, '���Գɹ�', '����ʧ��') AS 'Case17 Testing Result';

SELECT '-------------------- CASE18;���ݹ�Ӧ�̺ʹ���ʱ��ģ����ѯ��ⵥ��������@iTotalRecord>=1 -------------------------' AS 'Case18';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= '';
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = 1; 
SET @dtStart = '2017/12/5 0:00:00';
SET @dtEnd = '2017/12/7 0:00:00';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord >= 1, '���Գɹ�', '����ʧ��') AS 'Case18 Testing Result';

SELECT '-------------------- CASE19;���ݹ�Ӧ�̺���Ʒ��������ģ����ѯ��ⵥ��������@iTotalRecord>=1 -------------------------' AS 'Case19';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= 'ѩ����';
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = 1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord >= 1, '���Գɹ�', '����ʧ��') AS 'Case19 Testing Result';

SELECT '-------------------- CASE20;���ݴ���ʱ�����Ʒ��������ģ����ѯ��ⵥ��������@iTotalRecord>=1 -------------------------' AS 'Case20';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= '����';
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = '2017/12/5 0:00:00';
SET @dtEnd = '2017/12/7 0:00:00';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord >= 1, '���Գɹ�', '����ʧ��') AS 'Case20 Testing Result';

SELECT '-------------------- CASE21;����ֻ�н���ʱ��û�п�ʼʱ��ģ����ѯ��ⵥ��������@iTotalRecord = �˻��������� -------------------------' AS 'Case21';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= '';
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = '';
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord >= 1, '���Գɹ�', '����ʧ��') AS 'Case21 Testing Result';


SELECT '-------------------- CASE22;����ֻ�п�ʼʱ��û�н���ʱ��ģ����ѯ��ⵥ��������@iTotalRecord = �˻��������� -------------------------' AS 'Case22';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= '';
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord >= 1, '���Գɹ�', '����ʧ��') AS 'Case22 Testing Result';

SELECT '-------------------- CASE23;���ݿյ�����ģ����ѯ��ⵥ��������@iTotalRecord = �˻��������� -------------------------' AS 'Case23';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= '';
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case23 Testing Result';

SELECT '-------------------- CASE24;����ȫ������(string1Ϊ�˻���ID)ģ����ѯ��ⵥ��������@iTotalRecord = �˻��������� -------------------------' AS 'Case24';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= 'TH20190605';
SET @sStaffID = 2;
SET @iStatus = 1;
SET @iProviderID = 1; 
SET @dtStart = '2017/12/6 0:00:00';
SET @dtEnd = '2018/12/6 0:00:00';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord >= 1, '���Գɹ�', '����ʧ��') AS 'Case24 Testing Result';

SELECT '-------------------- CASE25;����ȫ������(string1Ϊ��Ʒ����)ģ����ѯ��ⵥ��������@iTotalRecord = �˻��������� -------------------------' AS 'Case25';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= 'ѩ���鼡����˪';
SET @sStaffID = 2;
SET @iStatus = 1;
SET @iProviderID = 1; 
SET @dtStart = '2017/12/6 0:00:00';
SET @dtEnd = '2018/12/6 0:00:00';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord >= 1, '���Գɹ�', '����ʧ��') AS 'Case25 Testing Result';

SELECT '-------------------- CASE26;����С����С���ȵ��˻�������ģ����ѯ��ⵥ(С��10λ)��������@iTotalRecord=0 -------------------------' AS 'Case26';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= 'TH2019060';
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = 0, '���Գɹ�', '����ʧ��') AS 'Case26 Testing Result';

SELECT '-------------------- CASE27;���ݴ�����С���ȵ��˻�������ģ����ѯ��ⵥ(����20λ)��������@iTotalRecord=0 -------------------------' AS 'Case27';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= 'TH20190605123451234512345';
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = 0, '���Գɹ�', '����ʧ��') AS 'Case27 Testing Result';

SELECT '-------------------- CASE28:������Ʒ���Ʋ�ѯ(δ��˵�)�����Ʒ������� -------------------------' AS 'Case28';
INSERT INTO t_returncommoditysheet (F_StaffID, F_ProviderID, F_ShopID)
VALUES (3, 3,2);

SET @iReturnCommoditySheetID = last_insert_id();

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iReturnCommoditySheetID, 1,'ά������', 10, 100, '��', 8);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= '�ɱȿ�';
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord > 0, '���Գɹ�', '����ʧ��') AS 'Case28 Testing Result';

DELETE FROM t_ReturnCommoditySheetCommodity WHERE F_ReturnCommoditySheetID = @iReturnCommoditySheetID;
DELETE FROM t_ReturnCommoditySheet WHERE F_ID = @iReturnCommoditySheetID;

SELECT '-------------------- CASE29:������Ʒ���Ʋ�ѯ(����˵�)����˻�����Ʒ����Ʒ���� -------------------------' AS 'Case28';
INSERT INTO t_returncommoditysheet (F_StaffID, F_ProviderID, F_Status, F_ShopID)
VALUES (3, 3, 1,2);

SET @iReturnCommoditySheetID = last_insert_id();

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iReturnCommoditySheetID, 1,'ά������', 10, 100, '��', 8);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= 'ά������';
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord > 0, '���Գɹ�', '����ʧ��') AS 'Case29 Testing Result';

DELETE FROM t_ReturnCommoditySheetCommodity WHERE F_ReturnCommoditySheetID = @iReturnCommoditySheetID;
DELETE FROM t_ReturnCommoditySheet WHERE F_ID = @iReturnCommoditySheetID;

SELECT '-------------------- CASE30;���ݵ�����С���ȵ��˻�������ģ����ѯ��ⵥ(����20λ)��������@iTotalRecord=0 -------------------------' AS 'Case30';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= 'TH201906051234512345';
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord = 0, '���Գɹ�', '����ʧ��') AS 'Case30 Testing Result';

SELECT '-------------------- CASE31��������Ʒ���Ʋ�ѯ(����˵ģ�string1����_�������ַ�����ģ������)����˻�����Ʒ����Ʒ���� -------------------------' AS 'Case31';

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_BarcodeID, F_CommodityID,F_CommodityName, F_NO, F_Specification, F_PurchasingPrice)
VALUES (1, 1, 2,'�˻�����Ƭ_12��3', 20, '��', 5.0);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= '_12��3';
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord > 0, '���Գɹ�', '����ʧ��') AS 'CASE31 Testing Result';

DELETE FROM t_returncommoditysheetcommodity WHERE F_CommodityName = '�˻�����Ƭ_12��3';

SELECT '-------------------- CASE32��������Ʒ���Ʋ�ѯ(δ��˵ģ�string1����_�������ַ�����ģ������)�����Ʒ�������-------------------------' AS 'Case32';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'cjs_666','��������','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,30,now(),'20',0,0,'.................', 0);

SET @iID = LAST_INSERT_ID();
INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_BarcodeID, F_CommodityID,F_CommodityName, F_NO, F_Specification, F_PurchasingPrice)
VALUES (3, 1,@iID ,'�˻��ɿڿ���73468', 20, '��', 5.0);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = -1;
SET @string1= '_';
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord > 0, '���Գɹ�', '����ʧ��') AS 'CASE32 Testing Result';

DELETE FROM t_returncommoditysheetcommodity WHERE F_CommodityName = '�˻��ɿڿ���73468';
DELETE FROM t_commodity WHERE F_Name = 'cjs_666';

SELECT '-------------------- CASE33;�����ŵ�ID���в�ѯ��������@iTotalRecord >= 1 -------------------------' AS 'Case33';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sShopID = 2;
SET @string1= 'TH20190605';
SET @sStaffID = -1;
SET @iStatus = -1;
SET @iProviderID = -1; 
SET @dtStart = now() - 300000;
SET @dtEnd = now();
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_returnCommoditySheet_RetrieveN(@iErrorCode, @sErrorMsg, @sShopID, @string1, @sStaffID, @iStatus, @iProviderID, @dtStart, @dtEnd, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord > 0, '���Գɹ�', '����ʧ��') AS 'Case33 Testing Result';