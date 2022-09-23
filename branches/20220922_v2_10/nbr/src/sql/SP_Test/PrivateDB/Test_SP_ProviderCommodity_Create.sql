SELECT '++++++++++++++++++ Test_SP_ProviderCommodity_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: ��Ӳ��ظ���Ӧ�̵���Ʒ�У�������Ϊ0 -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 1;
SET @iProviderID = 11;

CALL SP_ProviderCommodity_Create(@iErrorCode, @sErrorMsg, @iCommodityID, @iProviderID);

SELECT 1 FROM t_providercommodity WHERE F_CommodityID = @iCommodityID AND F_ProviderID = @iProviderID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

DELETE FROM t_providercommodity WHERE F_ID = last_insert_id();

SELECT '-------------------- Case2: �����ͬ�Ĺ�Ӧ�̵�ͬһ�����Ʒ�У�������Ϊ1 -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 1;
SET @iProviderID = 1;

CALL SP_ProviderCommodity_Create(@iErrorCode, @sErrorMsg, @iCommodityID, @iProviderID);

SELECT 1 FROM t_providercommodity WHERE F_CommodityID = @iCommodityID AND F_ProviderID = @iProviderID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

SELECT '-------------------- Case3: ��������Ʒ����Ӧ����Ʒ���У�������Ϊ7 -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 45;
SET @iProviderID = 6;

CALL SP_ProviderCommodity_Create(@iErrorCode, @sErrorMsg, @iCommodityID, @iProviderID);

SELECT @iErrorCode;
SELECT 1 FROM t_providercommodity WHERE F_CommodityID = @iCommodityID AND F_ProviderID = @iProviderID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';

SELECT '-------------------- Case4: �����ɾ����Ʒ����Ӧ����Ʒ���У�������Ϊ7 -------------------------' AS 'Case4';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 50;
SET @iProviderID = 6;

CALL SP_ProviderCommodity_Create(@iErrorCode, @sErrorMsg, @iCommodityID, @iProviderID);

SELECT @iErrorCode;
SELECT 1 FROM t_providercommodity WHERE F_CommodityID = @iCommodityID AND F_ProviderID = @iProviderID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';

SELECT '-------------------- Case5: ��Ӳ����ڵ���Ʒ����Ӧ����Ʒ���У�������Ϊ7 -------------------------' AS 'Case5';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = -99;
SET @iProviderID = 6;

CALL SP_ProviderCommodity_Create(@iErrorCode, @sErrorMsg, @iCommodityID, @iProviderID);

SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';

SELECT '-------------------- Case6: ��Ӳ����ڵĹ�Ӧ�̵���Ӧ����Ʒ���У�������Ϊ7 -------------------------' AS 'Case6';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 20;
SET @iProviderID = -99;

CALL SP_ProviderCommodity_Create(@iErrorCode, @sErrorMsg, @iCommodityID, @iProviderID);

SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case6 Testing Result';