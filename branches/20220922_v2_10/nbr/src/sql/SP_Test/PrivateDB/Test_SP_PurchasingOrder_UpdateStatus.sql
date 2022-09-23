SELECT '++++++++++++++++++ Test_SP_PurchasingOrder_UpdateStatus.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: �޸�״̬Ϊ�������(1 --> 2) -------------------------' AS 'Case1';

INSERT INTO T_PurchasingOrder(F_ShopID, F_Status, F_StaffID, F_ProviderID, F_CreateDatetime, F_ApproveDatetime, F_EndDatetime) 
VALUES(2, 1, 3, 1,now(), now(), now());
SET @iPurchasingOrderID = LAST_INSERT_ID();

INSERT INTO t_purchasingordercommodity (F_PurchasingOrderID, F_CommodityID, F_CommodityNO, F_CommodityName, F_BarcodeID, F_PackageUnitID, F_PriceSuggestion, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iPurchasingOrderID, 1, 10, '�ɱȿ���Ƭ', 1, 1, 11, now(), now());
SET @iPurchasingOrderCommodityID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStatus = 2;

CALL SP_PurchasingOrder_UpdateStatus(@iErrorCode, @sErrorMsg, @iStatus, @iPurchasingOrderID);

SELECT @sErrorMsg;
SELECT 1 FROM t_purchasingorder WHERE F_ID = @iPurchasingOrderID AND F_Status = @iStatus;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE1 Testing Result';

SELECT '-------------------- Case2: �޸�״̬Ϊȫ�����(2 --> 3) -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStatus = 3;

CALL SP_PurchasingOrder_UpdateStatus(@iErrorCode, @sErrorMsg, @iStatus, @iPurchasingOrderID);

SELECT @sErrorMsg;
SELECT 1 FROM t_purchasingorder WHERE F_ID = @iPurchasingOrderID AND F_Status = @iStatus;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE2 Testing Result';


SELECT '-------------------- Case3: �޸�״̬Ϊȫ�����(3 --> 3) -------------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStatus = 3;

CALL SP_PurchasingOrder_UpdateStatus(@iErrorCode, @sErrorMsg, @iStatus, @iPurchasingOrderID);

SELECT @sErrorMsg;
SELECT 1 FROM t_purchasingorder WHERE F_ID = @iPurchasingOrderID AND F_Status = @iStatus;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE3 Testing Result';


SELECT '-------------------- Case4: �޸�ȫ�����״̬�Ķ���Ϊ�������(���ǲ������ ���Է��ش�����7)(3 --> 2) -------------------------' AS 'Case4';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStatus = 0;

CALL SP_PurchasingOrder_UpdateStatus(@iErrorCode, @sErrorMsg, @iStatus, @iPurchasingOrderID);

SELECT @sErrorMsg;
SELECT 1 FROM t_purchasingorder WHERE F_ID = @iPurchasingOrderID AND F_Status = @iStatus;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '�����޸�ȫ�����״̬�Ķ���Ϊ�������', '���Գɹ�', '����ʧ��') AS 'CASE4 Testing Result';

-- DELETE FROM t_purchasingordercommodity WHERE F_ID = @iPurchasingOrderCommodityID;
-- DELETE FROM t_purchasingorder WHERE F_ID = @iPurchasingOrderID;

SELECT '-------------------- Case5: ����һ�������ڵ�iStatus(-1) -------------------------' AS 'Case5';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStatus = -1;
SELECT @iPurchasingOrderID;
CALL SP_PurchasingOrder_UpdateStatus(@iErrorCode, @sErrorMsg, @iStatus, @iPurchasingOrderID);

SELECT @sErrorMsg;
SELECT 1 FROM t_purchasingorder WHERE F_ID = @iPurchasingOrderID AND F_Status = @iStatus;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '�����޸�ȫ�����״̬�Ķ���Ϊ�������', '���Գɹ�', '����ʧ��') AS 'CASE5 Testing Result';

DELETE FROM t_purchasingordercommodity WHERE F_ID = @iPurchasingOrderCommodityID;
DELETE FROM t_purchasingorder WHERE F_ID = @iPurchasingOrderID;

SELECT '-------------------- Case6: ����һ�������ڵ�iID(-1) -------------------------' AS 'Case6';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStatus = 2;
SET @iID=-11;
CALL SP_PurchasingOrder_UpdateStatus(@iErrorCode, @sErrorMsg, @iStatus, @iID);

SELECT @sErrorMsg;
SELECT 1 FROM t_purchasingorder WHERE F_ID = @iID AND F_Status = @iStatus;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '�ɹ�����������', '���Գɹ�', '����ʧ��') AS 'CASE6 Testing Result';

DELETE FROM t_purchasingorder WHERE F_ID = @iID;


--	SELECT '-------------------- Case6: ���ǲɹ�����ΪNULL(��iID=0)�����������һ���µĴ�����2 -------------------------' AS 'Case6';
--	
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @iID = 0; 
--	SET @iStatus = 2;
--	
--	CALL SP_PurchasingOrder_UpdateStatus(@iErrorCode, @sErrorMsg, @iStatus, @iID);
--	
--	SELECT @sErrorMsg;
--	SELECT 1 FROM t_purchasingorder WHERE F_ID = @iID AND F_Status = @iStatus;
--	SELECT IF(found_rows() = 0 AND @iErrorCode = 2, '���Գɹ�', '����ʧ��') AS 'CASE6 Testing Result';

SELECT '-------------------- Case7: �޸�û�вɹ���Ʒ�Ĳɹ����� -------------------------' AS 'Case6';

INSERT INTO T_PurchasingOrder(F_ShopID, F_Status, F_StaffID, F_ProviderID, F_CreateDatetime, F_ApproveDatetime, F_EndDatetime) 
VALUES(2, 0, 3, 1,now(), now(), now());
SET @iID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStatus = 2;
CALL SP_PurchasingOrder_UpdateStatus(@iErrorCode, @sErrorMsg, @iStatus, @iID);

SELECT @sErrorMsg;
SELECT 1 FROM t_purchasingorder WHERE F_ID = @iID AND F_Status = @iStatus;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '�òɹ�����û�вɹ���Ʒ', '���Գɹ�', '����ʧ��') AS 'CASE6 Testing Result';

DELETE FROM t_purchasingorder WHERE F_ID = @iID;