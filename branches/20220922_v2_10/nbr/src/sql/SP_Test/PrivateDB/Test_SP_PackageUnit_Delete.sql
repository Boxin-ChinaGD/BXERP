SELECT '++++++++++++++++++ Test_SP_PackageUnit_Delete.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: �ð�װ��λ�ѱ��ɹ�������Ʒʹ�ã�����ɾ�����������Ϊ7 -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '��123';
INSERT INTO t_packageunit (F_Name)
VALUES (@sName);

SET @iPackageUnitID = LAST_INSERT_ID();

-- ����һ����Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'С������','������','��',@iPackageUnitID,@sName,3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',56,3,'1111111',0);
SET @iCommodityID = last_insert_id();
INSERT INTO t_barcodes (F_CommodityID, F_Barcode)
VALUES (@iCommodityID, '123456789');
SET @iBarcodeID = last_insert_ID();
-- ����һ���ɹ�����
INSERT INTO t_purchasingorder (F_Status, F_StaffID, F_ProviderID, F_ProviderName, F_ApproverID, F_Remark, F_CreateDatetime, F_ApproveDatetime, F_EndDatetime, F_UpdateDatetime)
VALUES (0, 1, 1, '1', 1, '1', NOW(), NOW(), NOW(), NOW());
SET @iPurchasingOrderID = last_insert_id();
SET @iCommodityNO = 0;
SET @fPriceSuggestion = 1.0;
CALL SP_PurchasingOrderCommodity_Create(@iErrorCode, @sErrorMsg, @iPurchasingOrderID, @iCommodityID, @iCommodityNO, @iBarcodeID, @fPriceSuggestion);
SELECT 1 FROM t_purchasingordercommodity WHERE F_PurchasingOrderID = @iPurchasingOrderID AND F_CommodityID = @iCommodityID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '�����ɹ�', '����ʧ��') AS '�����ɹ�����';

CALL SP_PackageUnit_Delete(@iErrorCode, @sErrorMsg, @iPackageUnitID);
SELECT @sErrorMsg;
SELECT 1 FROM t_packageunit WHERE F_ID = @iPackageUnitID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';
DELETE FROM t_purchasingordercommodity WHERE F_PurchasingOrderID = @iPurchasingOrderID;
DELETE FROM t_barcodes WHERE F_ID = @iBarcodeID;
DELETE FROM T_PurchasingOrder WHERE F_ID = @iPurchasingOrderID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
DELETE FROM t_packageunit WHERE F_ID = @iPackageUnitID;

SELECT '-------------------- Case2: �ð�װ��λû����Ʒ��ʹ�ã�����ֱ��ɾ�����������Ϊ0 -------------------------' AS 'Case2';

SET @sName = '��12';
SET @iErrorCode = 0;
SET @sErrorMsg = '';

INSERT INTO t_packageunit (F_Name)
VALUES (@sName);

SET @iID = LAST_INSERT_ID();

CALL SP_PackageUnit_Delete(@iErrorCode, @sErrorMsg, @iID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_packageunit WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

SELECT '-------------------- Case3: �ð�װ��λ�ѱ���ⵥ��Ʒʹ�ã�����ɾ�����������Ϊ7 -------------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '��123';
INSERT INTO t_packageunit (F_Name)
VALUES (@sName);

SET @iPackageUnitID = LAST_INSERT_ID();

-- ����һ����Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'С������','������','��',@iPackageUnitID,@sName,3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',56,3,'1111111',0);
SET @iCommodityID = last_insert_id();
-- ����һ����ⵥ
SET @iWarehousingID = 1;
SET @iNO = 1;
SET @iBarcodeID = 1;
SET @iPrice = 10;
SET @iAmount = 3000;
SET @iShelfLife = 22;

CALL SP_WarehousingCommodity_Create(@iErrorCode, @sErrorMsg, @iWarehousingID, @iCommodityID, @iNO, @iBarcodeID, @iPrice, @iAmount, @iShelfLife);
SET @iWarehousingCommodityID = last_insert_id();

CALL SP_PackageUnit_Delete(@iErrorCode, @sErrorMsg, @iPackageUnitID);
SELECT @sErrorMsg;
SELECT 1 FROM t_packageunit WHERE F_ID = @iPackageUnitID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';
DELETE FROM t_warehousingcommodity WHERE F_ID = @iWarehousingCommodityID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
DELETE FROM t_packageunit WHERE F_ID = @iPackageUnitID;

SELECT '-------------------- Case4: �ð�װ��λ�ѱ��̵㵥��Ʒʹ�ã�����ɾ�����������Ϊ7 -------------------------' AS 'Case4';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '��123';
INSERT INTO t_packageunit (F_Name)
VALUES (@sName);

SET @iPackageUnitID = LAST_INSERT_ID();

-- ����һ����Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'С������','������','��',@iPackageUnitID,@sName,3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',56,3,'1111111',0);
SET @iCommodityID = last_insert_id();
-- ����һ���̵㵥
SET @iInventorySheetID = 5;
SET @iNOReal = 0;
SET @iBarcodeID = 1;

CALL SP_InventoryCommodity_Create(@iErrorCode, @sErrorMsg, @iInventorySheetID, @iCommodityID, @iNOReal, @iBarcodeID);
SET @iInventoryCommodityID = last_insert_id();

SELECT @iErrorCode;
SELECT @sErrorMsg;

CALL SP_PackageUnit_Delete(@iErrorCode, @sErrorMsg, @iPackageUnitID);
SELECT @sErrorMsg;
SELECT 1 FROM t_packageunit WHERE F_ID = @iPackageUnitID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';
DELETE FROM t_inventorycommodity WHERE F_ID = @iInventoryCommodityID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
DELETE FROM t_packageunit WHERE F_ID = @iPackageUnitID;

SELECT '-------------------- Case5: �ð�װ��λ�ѱ���Ʒʹ�ã�����ɾ�����������Ϊ7 -------------------------' AS 'Case5';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '��123';
INSERT INTO t_packageunit (F_Name)
VALUES (@sName);

SET @iPackageUnitID = LAST_INSERT_ID();

-- ����һ����Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'С������','������','��',@iPackageUnitID,@sName,3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',56,3,'1111111',0);
SET @iCommodityID = last_insert_id();

CALL SP_PackageUnit_Delete(@iErrorCode, @sErrorMsg, @iPackageUnitID);
SELECT @sErrorMsg;
SELECT 1 FROM t_packageunit WHERE F_ID = @iPackageUnitID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';

DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
DELETE FROM t_packageunit WHERE F_ID = @iPackageUnitID;