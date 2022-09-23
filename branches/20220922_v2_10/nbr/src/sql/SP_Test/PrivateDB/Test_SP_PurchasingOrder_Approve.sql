SELECT '++++++++++++++++++ Test_SP_PurchasingOrder_Approve.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: ��״̬Ϊδ���ʱ��F_Status=0�� -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iApproverID = 3;

INSERT INTO T_PurchasingOrder(F_ShopID, F_Status, F_StaffID, F_ProviderID, F_CreateDatetime, F_ApproveDatetime, F_EndDatetime) 
VALUES(2, 0, 3, 1,now(), now(), now());
SET @iPurchasingOrderID = LAST_INSERT_ID();

INSERT INTO t_purchasingordercommodity (F_PurchasingOrderID, F_CommodityID, F_CommodityNO, F_CommodityName, F_BarcodeID, F_PackageUnitID, F_PriceSuggestion, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iPurchasingOrderID, 1, 10, '�ɱȿ���Ƭ', 1, 1, 11, now(), now());
SET @iPurchasingOrderCommodityID = LAST_INSERT_ID();

CALL SP_PurchasingOrder_Approve(@iErrorCode, @sErrorMsg, @iPurchasingOrderID,@iApproverID);

SELECT @sErrorMsg;
SELECT 1 FROM t_PurchasingOrder WHERE F_ID = @iPurchasingOrderID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'CASE1 Testing Result';

DELETE FROM t_purchasingordercommodity WHERE F_ID = @iPurchasingOrderCommodityID; 
DELETE FROM T_PurchasingOrder WHERE F_ID = @iPurchasingOrderID; 


SELECT '-------------------- Case2: ��״̬Ϊ����ʱ��F_Status=1�� -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iApproverID = 3;

INSERT INTO T_PurchasingOrder(F_ShopID, F_Status, F_StaffID, F_ProviderID, F_CreateDatetime, F_ApproveDatetime, F_EndDatetime) 
VALUES(2, 1, 5, 1,now(), now(), now());
SET @iPurchasingOrderID = LAST_INSERT_ID();

INSERT INTO t_purchasingordercommodity (F_PurchasingOrderID, F_CommodityID, F_CommodityNO, F_CommodityName, F_BarcodeID, F_PackageUnitID, F_PriceSuggestion, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iPurchasingOrderID, 1, 10, '�ɱȿ���Ƭ', 1, 1, 11, now(), now());
SET @iPurchasingOrderCommodityID = LAST_INSERT_ID();

CALL SP_PurchasingOrder_Approve(@iErrorCode, @sErrorMsg, @iPurchasingOrderID,@iApproverID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PurchasingOrder WHERE F_ID = @iPurchasingOrderID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7 AND @sErrorMsg = '�òɹ�������ˣ������ظ�����', '���Գɹ�', '����ʧ��') AS 'CASE2 Testing Result';

DELETE FROM t_purchasingordercommodity WHERE F_ID = @iPurchasingOrderCommodityID; 
DELETE FROM T_PurchasingOrder WHERE F_ID = @iPurchasingOrderID; 

SELECT '-------------------- Case3: ������Ĳɹ��������@iPurchasingOrderID�����ڵ�ʱ��  ���ش�����7 -------------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPurchasingOrderID = -1;
SET @iApproverID = 3;
CALL SP_PurchasingOrder_Approve(@iErrorCode, @sErrorMsg, @iPurchasingOrderID,@iApproverID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PurchasingOrder WHERE F_ID = @iPurchasingOrderID;
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '�ɹ�����������', '���Գɹ�', '����ʧ��') AS 'CASE3 Testing Result';


SELECT '-------------------- Case4: ������������@iApproverID�����ڵ�ʱ��  �޷����ã��������ϵ���ƣ� ���ش�����7 -------------------------' AS 'Case4';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iApproverID = -1;

INSERT INTO T_PurchasingOrder(F_ShopID, F_Status, F_StaffID, F_ProviderID, F_CreateDatetime, F_ApproveDatetime, F_EndDatetime) 
VALUES(2, 0, 3, 1,now(), now(), now());
SET @iPurchasingOrderID = LAST_INSERT_ID();

CALL SP_PurchasingOrder_Approve(@iErrorCode, @sErrorMsg, @iPurchasingOrderID,@iApproverID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PurchasingOrder WHERE F_ID = @iPurchasingOrderID;
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '��ǰ�ʻ����������', '���Գɹ�', '����ʧ��') AS 'CASE4 Testing Result';
DELETE FROM t_purchasingorder WHERE F_ID = @iPurchasingOrderID;

SELECT '-------------------- Case5: ���û�вɹ���Ʒ�Ĳɹ����� -------------------------' AS 'Case5';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iApproverID = 3;

INSERT INTO T_PurchasingOrder(F_ShopID, F_Status, F_StaffID, F_ProviderID, F_CreateDatetime, F_ApproveDatetime, F_EndDatetime) 
VALUES(2, 0, 3, 1,now(), now(), now());
SET @iPurchasingOrderID = LAST_INSERT_ID();

CALL SP_PurchasingOrder_Approve(@iErrorCode, @sErrorMsg, @iPurchasingOrderID,@iApproverID);

SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT 1 FROM t_PurchasingOrder WHERE F_ID = @iPurchasingOrderID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7 AND @sErrorMsg = '�òɹ�����û�вɹ���Ʒ', '���Գɹ�', '����ʧ��') AS 'CASE5 Testing Result';

DELETE FROM T_PurchasingOrder WHERE F_ID = @iPurchasingOrderID; 

SELECT '-------------------- Case6: ������������@iApproverIDΪ��ְԱ��ID��ʱ��  �޷����ã��������ϵ���ƣ� ���ش�����7 -------------------------' AS 'Case4';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iApproverID = 5; -- ��ְԱ��ID

INSERT INTO T_PurchasingOrder(F_ShopID, F_Status, F_StaffID, F_ProviderID, F_CreateDatetime, F_ApproveDatetime, F_EndDatetime) 
VALUES(2, 0, 3, 1,now(), now(), now());
SET @iPurchasingOrderID = LAST_INSERT_ID();

CALL SP_PurchasingOrder_Approve(@iErrorCode, @sErrorMsg, @iPurchasingOrderID,@iApproverID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PurchasingOrder WHERE F_ID = @iPurchasingOrderID;
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '��ǰ�ʻ����������', '���Գɹ�', '����ʧ��') AS 'CASE6 Testing Result';
DELETE FROM t_purchasingorder WHERE F_ID = @iPurchasingOrderID;