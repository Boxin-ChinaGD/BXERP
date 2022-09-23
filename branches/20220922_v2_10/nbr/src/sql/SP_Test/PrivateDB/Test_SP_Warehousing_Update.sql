SELECT '++++++++++++++++++ Test_SP_Warehousing_Update.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: �޸�δ�����ⵥ -------------------------' AS 'Case1';
-- Case1:�޸�δ�����ⵥ
INSERT INTO T_Warehousing (F_ShopID, F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_CreateDatetime)
VALUES (2, 0, 3, 1, 1, now());
SET @iWarehousingID = last_insert_id();

INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID, 1, 20, 1, '111', 1, 3, 10, now(), 36, now(), now(), now(), 10);
SET @iWarehousingcommodityID = Last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @IWarehouseID = 1;
SET @iProviderID = 2;
CALL SP_Warehousing_Update(@iErrorCode, @sErrorMsg, @iWarehousingID, @IWarehouseID, @iProviderID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_Warehousing WHERE F_ID = @iWarehousingID AND F_WarehouseID = @IWarehouseID AND F_ProviderID = @iProviderID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

DELETE FROM t_warehousingcommodity WHERE F_ID = @iWarehousingcommodityID;
DELETE FROM t_warehousing WHERE F_ID = @iWarehousingID;

SELECT '-------------------- Case2: �޸�����������ⵥ -------------------------' AS 'Case2';
-- Case2:�޸�����������ⵥ
INSERT INTO T_Warehousing (F_ShopID, F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_CreateDatetime)
VALUES (2, 1, 3, 1, 1, now());
SET @iWarehousingID = last_insert_id();

INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID, 1, 20, 1, '111', 1, 3, 10, now(), 36, now(), now(), now(), 10);
SET @iWarehousingcommodityID = Last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @IWarehouseID = 1;
SET @iProviderID = 1;
CALL SP_Warehousing_Update(@iErrorCode, @sErrorMsg, @iWarehousingID, @IWarehouseID, @iProviderID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = 'ֻ���޸�״̬Ϊδ��˵���ⵥ', '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

DELETE FROM t_warehousingcommodity WHERE F_ID = @iWarehousingcommodityID;
DELETE FROM t_warehousing WHERE F_ID = @iWarehousingID;

SELECT '-------------------- Case3: �޸�����˵�������ⵥ -------------------------' AS 'Case3';
-- Case3:�޸�����˵�������ⵥ
INSERT INTO T_Warehousing (F_ShopID, F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_CreateDatetime)
VALUES (2, 2, 3, 1, 1, now());
SET @iWarehousingID = last_insert_id();

INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID, 1, 20, 1, '111', 1, 3, 10, now(), 36, now(), now(), now(), 10);
SET @iWarehousingcommodityID = Last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @IWarehouseID = 1;
SET @iProviderID = 1;
CALL SP_Warehousing_Update(@iErrorCode, @sErrorMsg, @iWarehousingID, @IWarehouseID, @iProviderID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = 'ֻ���޸�״̬Ϊδ��˵���ⵥ', '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';

DELETE FROM t_warehousingcommodity WHERE F_ID = @iWarehousingcommodityID;
DELETE FROM t_warehousing WHERE F_ID = @iWarehousingID;

SELECT '-------------------- Case4: �޸�һ���ֿⲻ���ڵ���ⵥ -------------------------' AS 'Case4';
-- Case4:�޸�һ���ֿⲻ���ڵ���ⵥ
INSERT INTO T_Warehousing (F_ShopID, F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_CreateDatetime)
VALUES (2, 0, 3, 1, 1, now());
SET @sErrorMsg = '';
SET @iWarehousingID = last_insert_id();

INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID, 1, 20, 1, '111', 1, 3, 10, now(), 36, now(), now(), now(), 10);
SET @iWarehousingcommodityID = Last_insert_id();

SET @iErrorCode = 0;
SET @IWarehouseID = 99999;
SET @iProviderID = 1;
CALL SP_Warehousing_Update(@iErrorCode, @sErrorMsg, @iWarehousingID, @IWarehouseID, @iProviderID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '�����޸�һ���ֿⲻ���ڵ���ⵥ', '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';

DELETE FROM t_warehousingcommodity WHERE F_ID = @iWarehousingcommodityID;
DELETE FROM t_warehousing WHERE F_ID = @iWarehousingID;

SELECT '-------------------- Case5: �޸���ⵥ,����Ĺ�Ӧ��ID������ -------------------------' AS 'Case1';
INSERT INTO T_Warehousing (F_ShopID, F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_CreateDatetime)
VALUES (2, 0, 3, 1, 1, now());
SET @iWarehousingID = last_insert_id();

INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID, 1, 20, 1, '111', 1, 3, 10, now(), 36, now(), now(), now(), 10);
SET @iWarehousingcommodityID = Last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @IWarehouseID = 1;
SET @iProviderID = -999;
CALL SP_Warehousing_Update(@iErrorCode, @sErrorMsg, @iWarehousingID, @IWarehouseID, @iProviderID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '�����޸ĳɲ����ڵĹ�Ӧ��', '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';

DELETE FROM t_warehousingcommodity WHERE F_ID = @iWarehousingcommodityID;
DELETE FROM t_warehousing WHERE F_ID = @iWarehousingID;

SELECT '-------------------- Case6: �޸���ⵥ,�������ⵥID������ -------------------------' AS 'Case1';
SET @iID = -1;
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @IWarehouseID = 1;
SET @iProviderID = 1;
CALL SP_Warehousing_Update(@iErrorCode, @sErrorMsg, @iID, @IWarehouseID, @iProviderID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '�����޸ĳɲ����ڵ���ⵥ', '���Գɹ�', '����ʧ��') AS 'Case6 Testing Result';


SELECT '-------------------- Case7: �޸�û�������Ʒ����ⵥ -------------------------' AS 'Case7';
-- Case1:�޸�δ�����ⵥ
INSERT INTO T_Warehousing (F_ShopID, F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_CreateDatetime)
VALUES (2, 0, 3, 1, 1, now());
SET @iWarehousingID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @IWarehouseID = 1;
SET @iProviderID = 2;
CALL SP_Warehousing_Update(@iErrorCode, @sErrorMsg, @iWarehousingID, @IWarehouseID, @iProviderID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_Warehousing WHERE F_ID = @iWarehousingID AND F_WarehouseID = @IWarehouseID AND F_ProviderID = @iProviderID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '����ⵥû�������Ʒ', '���Գɹ�', '����ʧ��') AS 'Case7 Testing Result';

DELETE FROM t_warehousing WHERE F_ID = @iWarehousingID;