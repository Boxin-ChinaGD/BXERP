SELECT '++++++++++++++++++ Test_SPD_Warehousing_CheckWarehousingCommodity.sql ++++++++++++++++++++';
SELECT '------------------ CASE1:�������� --------------------' AS 'CASE1';
--  
SET @iErrorCode = 0;
SET @sErrorMsg = '';
--  
CALL SPD_Warehousing_CheckWarehousingCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
--  
SELECT IF(@iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';
--  


SELECT '------------------ CASE2:��ⵥ�����������Ʒ --------------------' AS 'CASE2';
-- 
INSERT INTO t_warehousing (F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (0, 1, 1, 1, now(), 1, now());
SET @iID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
--  
CALL SPD_Warehousing_CheckWarehousingCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
--  
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = CONCAT('��ⵥ:', @iID, 'û�������Ʒ'), '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
--  
DELETE FROM t_warehousing WHERE F_ID = @iID;