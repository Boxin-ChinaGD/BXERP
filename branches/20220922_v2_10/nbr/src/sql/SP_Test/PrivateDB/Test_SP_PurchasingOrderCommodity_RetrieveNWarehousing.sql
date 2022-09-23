SELECT '++++++++++++++++++ Test_SP_PurchasingOrderCommodity_RetrieveNWarehousing.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: iWarehousingΪ0ʱ��ѯδ������  ���Ϊ���¿���ʣ��100,�����Ұ�����ʣ��200 -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iWarehousing = 0;
SET @iPurchasingOrderID = 10;
SET @iTotalRecord = 0;

CALL SP_PurchasingOrderCommodity_RetrieveNWarehousing(@iErrorCode, @sErrorMsg, @iPurchasingOrderID, @iWarehousing, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;

SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

SELECT '-------------------- Case2: iWarehousingΪ1ʱ��ѯ������ һ����3����ⵥ����ֻ����˹�����������⣬�����ܹ���������ⵥ -------------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iWarehousing = 1;
SET @iPurchasingOrderID = 10;
SET @iTotalRecord = 0;

CALL SP_PurchasingOrderCommodity_RetrieveNWarehousing(@iErrorCode, @sErrorMsg, @iPurchasingOrderID, @iWarehousing,@iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;

SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';


SELECT '-------------------- Case3: iWarehousingΪ-99ʱ,������=7 -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iWarehousing = -99;
SET @iPurchasingOrderID = 10;
SET @iTotalRecord = 0;

CALL SP_PurchasingOrderCommodity_RetrieveNWarehousing(@iErrorCode, @sErrorMsg, @iPurchasingOrderID, @iWarehousing,@iTotalRecord);

SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '���ܲ�ѯ������״̬����ⵥ', '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';

SELECT '-------------------- Case4: iWarehousingΪ1ʱ,�ɹ�����IDΪ�����ڵ�ID -------------------------' AS 'Case4';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iWarehousing = 1;
SET @iPurchasingOrderID = -99;
SET @iTotalRecord = 0;

CALL SP_PurchasingOrderCommodity_RetrieveNWarehousing(@iErrorCode, @sErrorMsg, @iPurchasingOrderID, @iWarehousing,@iTotalRecord);

SELECT IF(@iTotalRecord = '' AND @iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';