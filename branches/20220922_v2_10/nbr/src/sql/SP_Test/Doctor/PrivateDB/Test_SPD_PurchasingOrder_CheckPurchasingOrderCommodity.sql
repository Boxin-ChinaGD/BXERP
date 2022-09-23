SELECT '++++++++++++++++++ Test_SPD_PurchasingOrder_CheckPurchasingOrderCommodity.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:�������� -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_PurchasingOrder_CheckPurchasingOrderCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';



SELECT '-------------------- Case2:�ɹ�����û�вɹ���Ʒ -------------------------' AS 'Case2';
-- 
INSERT INTO t_purchasingorder (F_Status, F_StaffID, F_ProviderID, F_ProviderName, F_Remark, F_CreateDatetime, F_EndDatetime, F_UpdateDatetime)
VALUES (0, 1, 1, 'Ĭ�Ϲ�Ӧ��', '...', now(), now(), now());
SET @iID = LAST_INSERT_ID();
-- 
SET @iErrorCode := 0;
SET @sErrorMsg := '';
-- 
CALL SPD_PurchasingOrder_CheckPurchasingOrderCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('�ɹ�����:', @iID, 'û�вɹ���Ʒ') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
-- 
DELETE FROM t_purchasingorder WHERE F_ID = @iID;



SELECT '-------------------- Case3:ɾ��״̬�Ĳɹ�����û�вɹ���Ʒ -------------------------' AS 'Case3';
-- 
INSERT INTO t_purchasingorder (F_Status, F_StaffID, F_ProviderID, F_ProviderName, F_Remark, F_CreateDatetime, F_EndDatetime, F_UpdateDatetime)
VALUES (4, 1, 1, 'Ĭ�Ϲ�Ӧ��', '...', now(), now(), now());
SET @iID = LAST_INSERT_ID();
-- 
SET @iErrorCode := 0;
SET @sErrorMsg := '';
-- 
CALL SPD_PurchasingOrder_CheckPurchasingOrderCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';
-- 
DELETE FROM t_purchasingorder WHERE F_ID = @iID;