SELECT '++++++++++++++++++ Test_SPD_Warehousing_CheckStatus.sql ++++++++++++++++++++';
SELECT '------------------ �������� --------------------' AS 'CASE1';
--
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Warehousing_CheckStatus(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';
--
SELECT '------------------ ״̬�쳣 --------------------' AS 'CASE2';
--
INSERT INTO T_Warehousing (F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_CreateDatetime)
VALUES (999, 3, 1, 1, now());
SET @iWarehousingID = last_insert_id();
--
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Warehousing_CheckStatus(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('��ⵥ', @iWarehousingID, '״̬�쳣') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
DELETE FROM t_warehousing WHERE F_ID = @iWarehousingID;
--