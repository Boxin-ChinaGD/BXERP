SELECT '++++++++++++++++++ Test_SP_CommodityShopInfo_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:������ƷID��ѯ��Ʒ�ŵ���Ϣ -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 1;
SET @iShopID = 0;
SET @iTotalRecord = 0;

CALL SP_CommodityShopInfo_RetrieveN(@iErrorCode, @sErrorMsg, @iCommodityID, @iShopID, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';



SELECT '-------------------- Case2:������ƷID���ŵ�ID��ѯ��Ʒ�ŵ���Ϣ -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 1;
SET @iShopID = 2;
SET @iTotalRecord = 0;

CALL SP_CommodityShopInfo_RetrieveN(@iErrorCode, @sErrorMsg, @iCommodityID, @iShopID, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';



SELECT '-------------------- Case3:�����ŵ�ID��ѯ��Ʒ�ŵ���Ϣ -------------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = -1;
SET @iShopID = 2;
SET @iTotalRecord = 0;

CALL SP_CommodityShopInfo_RetrieveN(@iErrorCode, @sErrorMsg, @iCommodityID, @iShopID, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';