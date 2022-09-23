SELECT '++++++++++++++++++ Test_SP_Warehousing_Create.sql ++++++++++++++++++++';

-- case1: �ɹ�����״̬Ϊ����ˡ�
INSERT INTO T_PurchasingOrder (F_Status,F_StaffID,F_ProviderID,F_Remark,F_CreateDatetime,F_ApproveDatetime,F_EndDatetime)
VALUES (1,1,1,'...............','2016-12-06','2017-12-02','2017-10-06');
SET @iPurchasingOrderID = last_insert_id();

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iProviderID = 1;
SET @iWarehouseID = 1;
SET @iStaffID = 1;
SET @iShopID = 2;

CALL SP_Warehousing_Create(@iErrorCode, @sErrorMsg, @iShopID, @iProviderID, @iWarehouseID, @iStaffID, @iPurchasingOrderID);
SELECT @sErrorMsg;
SELECT 1 FROM t_Warehousing WHERE F_WarehouseID = @iWarehouseID AND F_StaffID = @iStaffID AND F_PurchasingOrderID = @iPurchasingOrderID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';
DELETE FROM t_warehousing WHERE F_ID = last_insert_id();
DELETE FROM t_purchasingorder WHERE F_ID = @iPurchasingOrderID;

-- case2: �ɹ�����״̬Ϊ������⡣
INSERT INTO T_PurchasingOrder (F_Status,F_StaffID,F_ProviderID,F_Remark,F_CreateDatetime,F_ApproveDatetime,F_EndDatetime)
VALUES (2,1,1,'...............','2016-12-06','2017-12-02','2017-10-06');
SET @iPurchasingOrderID = last_insert_id();

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iProviderID = 1;
SET @iWarehouseID = 1;
SET @iStaffID = 1;
SET @iShopID = 2;

CALL SP_Warehousing_Create(@iErrorCode, @sErrorMsg, @iShopID, @iProviderID, @iWarehouseID, @iStaffID, @iPurchasingOrderID);
SELECT @sErrorMsg;
SELECT 1 FROM t_Warehousing WHERE F_WarehouseID = @iWarehouseID AND F_StaffID = @iStaffID AND F_PurchasingOrderID = @iPurchasingOrderID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
DELETE FROM t_warehousing WHERE F_ID = last_insert_id();
DELETE FROM t_purchasingorder WHERE F_ID = @iPurchasingOrderID;

-- case3: �ɹ�����״̬Ϊδ��˵ġ�
INSERT INTO T_PurchasingOrder (F_Status,F_StaffID,F_ProviderID,F_Remark,F_CreateDatetime,F_ApproveDatetime,F_EndDatetime)
VALUES (0,1,1,'...............','2016-12-06','2017-12-02','2017-10-06');
SET @iPurchasingOrderID = last_insert_id();

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iProviderID = 1;
SET @iWarehouseID = 1;
SET @iStaffID = 1;
SET @iShopID = 2;

CALL SP_Warehousing_Create(@iErrorCode, @sErrorMsg, @iShopID, @iProviderID, @iWarehouseID, @iStaffID, @iPurchasingOrderID);
SELECT @sErrorMsg;
SELECT 1 FROM t_Warehousing WHERE F_WarehouseID = @iWarehouseID AND F_StaffID = @iStaffID AND F_PurchasingOrderID = @iPurchasingOrderID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '�ɹ�����״̬Ϊδ��˵�', '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';
DELETE FROM t_purchasingorder WHERE F_ID = @iPurchasingOrderID;

-- case4: �ɹ�����״̬Ϊȫ�����ġ�
INSERT INTO T_PurchasingOrder (F_Status,F_StaffID,F_ProviderID,F_Remark,F_CreateDatetime,F_ApproveDatetime,F_EndDatetime)
VALUES (3,1,1,'...............','2016-12-06','2017-12-02','2017-10-06');
SET @iPurchasingOrderID = last_insert_id();

SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iProviderID = 1;
SET @iWarehouseID = 1;
SET @iStaffID = 1;
SET @iShopID = 2;

CALL SP_Warehousing_Create(@iErrorCode, @sErrorMsg, @iShopID, @iProviderID, @iWarehouseID, @iStaffID, @iPurchasingOrderID);
SELECT @sErrorMsg;
SELECT 1 FROM t_Warehousing WHERE F_WarehouseID = @iWarehouseID AND F_StaffID = @iStaffID AND F_PurchasingOrderID = @iPurchasingOrderID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '�ɹ�������ȫ�����', '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';
DELETE FROM t_purchasingorder WHERE F_ID = @iPurchasingOrderID;

-- case5: �������òɹ�������
SET @iPurchasingOrderID = 0;
SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iProviderID = 1;
SET @iWarehouseID = 1;
SET @iStaffID = 1;
SET @iShopID = 2;

CALL SP_Warehousing_Create(@iErrorCode, @sErrorMsg, @iShopID, @iProviderID, @iWarehouseID, @iStaffID, @iPurchasingOrderID);
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';
DELETE FROM t_warehousing WHERE F_ID = last_insert_id();
DELETE FROM t_purchasingorder WHERE F_ID = @iPurchasingOrderID;

-- case6: �ò����ڵ�staff
SET @iPurchasingOrderID = 0;
SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iProviderID = 1;
SET @iWarehouseID = 1;
SET @iStaffID = 1111111111111111;
SET @iShopID = 2;

CALL SP_Warehousing_Create(@iErrorCode, @sErrorMsg, @iShopID, @iProviderID, @iWarehouseID, @iStaffID, @iPurchasingOrderID);
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case6 Testing Result';

-- case7: �ò����ڵĲֿ�
SET @iPurchasingOrderID = 0;
SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iProviderID = 1;
SET @iWarehouseID = 9999999999;
SET @iStaffID = 1;
SET @iShopID = 2;

CALL SP_Warehousing_Create(@iErrorCode, @sErrorMsg, @iShopID, @iProviderID, @iWarehouseID, @iStaffID, @iPurchasingOrderID);
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case7 Testing Result';

-- case8: �ò����ڵĹ�Ӧ��ID
SET @iPurchasingOrderID = 0;
SET @iErrorCode =0;
SET @sErrorMsg = '';
SET @iProviderID = 9999999999999999;
SET @iWarehouseID = 1;
SET @iStaffID = 1;
SET @iShopID = 2;

CALL SP_Warehousing_Create(@iErrorCode, @sErrorMsg, @iShopID, @iProviderID, @iWarehouseID, @iStaffID, @iPurchasingOrderID);
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case8 Testing Result';
