SELECT '++++++++++++++++++ Test_SP_Subcommodity_Create ++++++++++++++++++++';

SELECT '-------------------- Case1:��Ӳ��ظ���Ʒ��������Ϊ0 -------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 45;
SET @iSubCommodityID = 33;
SET @iSubCommodityNO = 1;
SET @iPrice = 8;

CALL SP_Subcommodity_Create(@iErrorCode, @sErrorMsg, @iCommodityID, @iSubCommodityID, @iSubCommodityNO, @iPrice);

SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT 1 FROM t_Subcommodity WHERE F_CommodityID = @iCommodityID AND F_SubCommodityID = @iSubCommodityID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE1 Testing Result';

DELETE FROM t_subcommodity WHERE F_ID = last_insert_id();

SELECT '-------------------- Case2:�����ͬ����Ʒ��ͬһ�����Ʒ�У�������Ϊ1 -------------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 49;
SET @iSubCommodityID = 10;
SET @iSubCommodityNO = 1;
SET @iPrice = 8;

CALL SP_Subcommodity_Create(@iErrorCode, @sErrorMsg, @iCommodityID, @iSubCommodityID, @iSubCommodityNO, @iPrice);

SELECT @sErrorMsg;
SELECT 1 FROM t_Subcommodity WHERE F_CommodityID = @iCommodityID AND F_SubCommodityID = @iSubCommodityID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 1, '���Գɹ�', '����ʧ��') AS 'CASE2 Testing Result';

DELETE FROM t_subcommodity WHERE F_ID = last_insert_id();

SELECT '-------------------- Case3:����ͨ��Ʒ���������Ʒ��������Ʒ��������Ϊ7 -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 25;
SET @iSubCommodityID = 10;
SET @iSubCommodityNO = 1;
SET @iPrice = 8;

CALL SP_Subcommodity_Create(@iErrorCode, @sErrorMsg, @iCommodityID, @iSubCommodityID, @iSubCommodityNO, @iPrice);

SELECT @sErrorMsg;
SELECT 1 FROM t_Subcommodity WHERE F_CommodityID = @iCommodityID AND F_SubCommodityID = @iSubCommodityID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'CASE3 Testing Result';


SELECT '-------------------- Case4:��Ӳ����ڵ���Ʒ��������Ϊ7 -------------------------' AS 'Case4';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 999999999;
SET @iSubCommodityID = 11;
SET @iSubCommodityNO = 1;
SET @iPrice = 8;

CALL SP_Subcommodity_Create(@iErrorCode, @sErrorMsg, @iCommodityID, @iSubCommodityID, @iSubCommodityNO, @iPrice);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'CASE4 Testing Result';

SELECT '-------------------- Case5:��Ӳ����ڵ�����Ʒ��������Ϊ7 -------------------------' AS 'Case5';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 2;
SET @iSubCommodityID = 999999999;
SET @iSubCommodityNO = 1;
SET @iPrice = 8;

CALL SP_Subcommodity_Create(@iErrorCode, @sErrorMsg, @iCommodityID, @iSubCommodityID, @iSubCommodityNO, @iPrice);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'CASE5 Testing Result';

SELECT '-------------------- Case6:��Ӷ��װ��ƷΪ����Ʒ��������Ϊ7 -------------------------' AS 'Case6';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 45;
SET @iSubCommodityID = 51;
SET @iSubCommodityNO = 1;
SET @iPrice = 8;

CALL SP_Subcommodity_Create(@iErrorCode, @sErrorMsg, @iCommodityID, @iSubCommodityID, @iSubCommodityNO, @iPrice);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'CASE6 Testing Result';

SELECT '-------------------- Case7:��������ƷΪ����Ʒ��������Ϊ7 -------------------------' AS 'Case7';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 45;
SET @iSubCommodityID = 49;
SET @iSubCommodityNO = 1;
SET @iPrice = 8;

CALL SP_Subcommodity_Create(@iErrorCode, @sErrorMsg, @iCommodityID, @iSubCommodityID, @iSubCommodityNO, @iPrice);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'CASE7 Testing Result';

SELECT '-------------------- Case8:����Ʒ�۸�Ϊ������������Ϊ7 -------------------------' AS 'Case7';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 45;
SET @iSubCommodityID = 2;
SET @iSubCommodityNO = 1;
SET @iPrice = -1;

CALL SP_Subcommodity_Create(@iErrorCode, @sErrorMsg, @iCommodityID, @iSubCommodityID, @iSubCommodityNO, @iPrice);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'CASE8 Testing Result';

SELECT '-------------------- Case9:�����װ��Ʒ���������Ʒ��������Ʒ��������Ϊ7 -------------------------' AS 'Case9';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 51;
SET @iSubCommodityID = 10;
SET @iSubCommodityNO = 1;
SET @iPrice = 8;

CALL SP_Subcommodity_Create(@iErrorCode, @sErrorMsg, @iCommodityID, @iSubCommodityID, @iSubCommodityNO, @iPrice);

SELECT @sErrorMsg;
SELECT 1 FROM t_Subcommodity WHERE F_CommodityID = @iCommodityID AND F_SubCommodityID = @iSubCommodityID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = 'ֻ�������Ʒ�ܲ�������Ʒ', '���Գɹ�', '����ʧ��') AS 'CASE9 Testing Result';

SELECT '-------------------- Case10:��������Ʒ���������Ʒ��������Ʒ��������Ϊ7 -------------------------' AS 'Case10';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 166;
SET @iSubCommodityID = 10;
SET @iSubCommodityNO = 1;
SET @iPrice = 8;

CALL SP_Subcommodity_Create(@iErrorCode, @sErrorMsg, @iCommodityID, @iSubCommodityID, @iSubCommodityNO, @iPrice);

SELECT @sErrorMsg;
SELECT 1 FROM t_Subcommodity WHERE F_CommodityID = @iCommodityID AND F_SubCommodityID = @iSubCommodityID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = 'ֻ�������Ʒ�ܲ�������Ʒ', '���Գɹ�', '����ʧ��') AS 'CASE10 Testing Result';

SELECT '-------------------- Case11:�����Ʒ��ӷ�����ƷΪ����Ʒ -------------------------' AS 'Case11';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 45;
SET @iSubCommodityID = 166;
SET @iSubCommodityNO = 1;
SET @iPrice = 8;

CALL SP_Subcommodity_Create(@iErrorCode, @sErrorMsg, @iCommodityID, @iSubCommodityID, @iSubCommodityNO, @iPrice);

SELECT @sErrorMsg;
SELECT 1 FROM t_Subcommodity WHERE F_CommodityID = @iCommodityID AND F_SubCommodityID = @iSubCommodityID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '������ϳ��˵�Ʒ�������������Ʒ', '���Գɹ�', '����ʧ��') AS 'CASE11 Testing Result';