SELECT '++++++++++++++++++ Test_SP_RetailTradeMonthlyReportSummary_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:������� -------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @dtEnd = '2000/07/31 23:59:59';
SET @iDeleteOldData = 0;
SET @iShopID = 2;


CALL SP_RetailTradeMonthlyReportSummary_Create(@iErrorCode, @sErrorMsg, @iShopID, @dtEnd, @iDeleteOldData);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_retailtrademonthlyreportsummary WHERE F_Datetime = '2000/07/1 00:00:00';
SELECT IF(@iErrorCode = 0 AND found_rows() = 1, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

-- �����Ѿ�֧����������ʱ����±�
--	SELECT '-------------------- Case2:���һ�������ڵ�ʱ��(�����ձ������±������ձ�û�����ݲ��ᴴ�����µ��±�) -------------------------' AS 'Case2';
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @dtEnd = '2099-01-31 23:59:59';
--	
--	CALL SP_RetailTradeMonthlyReportSummary_Create(@iErrorCode, @sErrorMsg, @dtEnd);
--	SELECT @iErrorCode;
--	SELECT @sErrorMsg;
--	SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';


SELECT '-------------------- Case3:���һ��ʱ�䲻�����������ݣ���������ifnull������ -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dtEnd = '1970-04-30 23:59:59';
SET @iDeleteOldData = 1;
SET @iShopID = 2;

CALL SP_RetailTradeMonthlyReportSummary_Create(@iErrorCode, @sErrorMsg, @iShopID, @dtEnd, @iDeleteOldData);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';
