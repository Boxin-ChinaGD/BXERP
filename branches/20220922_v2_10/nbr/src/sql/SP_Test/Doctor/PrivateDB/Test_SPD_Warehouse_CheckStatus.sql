SELECT '++++++++++++++++++ Test_SPD_Warehouse_CheckStatus.sql ++++++++++++++++++++';
SELECT '------------------ �������� --------------------' AS 'CASE1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Warehouse_CheckStatus(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';
-- 
SELECT '------------------ ״̬�쳣 --------------------' AS 'CASE2';
-- 
INSERT INTO t_warehouse (F_Name, F_Address, F_Status, F_StaffID, F_Phone, F_CreateDatetime, F_UpdateDatetime)
VALUES ('�쳣�Ĳֿ�','ֲ��԰',2,NULL,'',now(),now());
SET @iID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Warehouse_CheckStatus(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iID ,'�Ĳֿ��״̬�쳣') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
DELETE FROM t_warehouse WHERE F_ID = @iID;
-- 