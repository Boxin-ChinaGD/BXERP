SELECT '++++++++++++++++++ Test_SP_PurchasingOrderCommodity_Delete.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: ���ݲɹ���IDɾ���ɹ��ɹ��������������Ʒ -------------------------' AS 'Case1';

INSERT INTO t_purchasingorder (F_ShopID, F_Status, F_StaffID, F_ProviderID, F_ProviderName, F_ApproverID, F_Remark, F_CreateDatetime, F_ApproveDatetime, F_EndDatetime, F_UpdateDatetime)
VALUES (2, 0, 1, 1, '11', 1, 'aa', now(), now(), now(), now());
SET @iPurchasingOrderID = Last_insert_id();

INSERT INTO t_purchasingordercommodity (F_PurchasingOrderID, F_CommodityID, F_CommodityNO, F_CommodityName, F_PriceSuggestion,F_BarcodeID,F_PackageUnitID, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iPurchasingOrderID, 4, 100, 'AA', 5, 1, 1, now(), now());

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = -1;

CALL SP_PurchasingOrderCommodity_Delete(@iErrorCode, @sErrorMsg, @iPurchasingOrderID, @iCommodityID);

SELECT @sErrorMsg;
SELECT 1 FROM t_purchasingordercommodity WHERE F_PurchasingOrderID = @iPurchasingOrderID;
SELECT IF(FOUND_ROWS() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'case1 Testing Result';

SELECT '-------------------- Case2: ������ƷID�Ͳɹ���IDɾ��������Ʒ -------------------------' AS 'Case2';

INSERT INTO t_purchasingordercommodity (F_PurchasingOrderID, F_CommodityID, F_CommodityNO, F_CommodityName, F_PriceSuggestion,F_BarcodeID,F_PackageUnitID, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iPurchasingOrderID, 3, 100, 'AA', 5, 1, 1, now(), now());

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 3;

CALL SP_PurchasingOrderCommodity_Delete(@iErrorCode, @sErrorMsg, @iPurchasingOrderID, @iCommodityID);

SELECT @sErrorMsg;
SELECT 1 FROM t_purchasingordercommodity WHERE F_CommodityID = @iCommodityID AND F_PurchasingOrderID = @iPurchasingOrderID;
SELECT IF(FOUND_ROWS() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'case2 Testing Result';

DELETE FROM t_purchasingorder WHERE F_ID = @iPurchasingOrderID;

SELECT '-------------------- Case3: �ɹ���ID����ƷID�������� -------------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPurchasingOrderID = -1;
SET @iCommodityID = -1;

CALL SP_PurchasingOrderCommodity_Delete(@iErrorCode, @sErrorMsg, @iPurchasingOrderID, @iCommodityID);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'case3 Testing Result';