SELECT '++++++++++++++++++ Test_SPD_Commodity_CheckWarehousing.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:�������� -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckWarehousing(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

SELECT '-------------------- Case2:��ƷType��0,�ڳ���������0���ڳ��ɹ���Ϊ-1 -------------------------' AS 'Case2';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type,F_NOStart,F_PurchasingPriceStart)
VALUES (0,'�ǰͿ�AB123','����123','��',4,'֧',4,4,'SP',1,
5,38,5.8,5.5,1,1,null,
3,30,'2019-01-14','20',0,0,'�ǰͿ�AB',0,0,20,-1);
SET @iCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckWarehousing(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('��Ʒ', @iCommodityID, '���ڳ��������ڳ��ɹ��۲���ȷ���ڳ��������ڳ��ɹ���Ӧ��ͬʱΪ-1����ͬʱΪ����0����') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
-- 
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- Case3:��ƷType��2,������ⵥ -------------------------' AS 'Case3';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type,F_NOStart,F_PurchasingPriceStart)
VALUES (0,'�ǰͿ�AB123','����123','��',4,'֧',4,4,'SP',1,
5,38,5.8,5.5,1,1,null,
3,30,'2019-01-14','20',0,0,'�ǰͿ�AB',0,2,20,20);
SET @iCommodityID = LAST_INSERT_ID();
-- 
INSERT INTO T_Warehousing (F_Status,F_ProviderID,F_WarehouseID,F_StaffID,F_CreateDatetime,F_PurchasingOrderID) VALUES (1, 1, 1, 3, now(), NULL);
SET @iWarehousingID = last_insert_id();
INSERT INTO T_WarehousingCommodity (F_WarehousingID,F_CommodityID,F_NO,F_PackageUnitID,F_CommodityName,F_BarcodeID,F_Price,F_Amount,F_ProductionDatetime,F_ShelfLife,F_ExpireDatetime,F_NOSalable)
VALUES (@iWarehousingID, @iCommodityID, 20, 1, '�ǰͿ�AB123', 1, 20, 20 * 20, now(), 12, now(), 20);
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckWarehousing(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('��Ʒ', @iCommodityID, '���ǵ�Ʒ���������') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';
-- 
DELETE FROM T_WarehousingCommodity WHERE F_CommodityID = @iCommodityID;
DELETE FROM T_Warehousing WHERE F_ID = @iWarehousingID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- Case4:��ƷType��0,���ڳ���Ʒ���ڳ������Ͷ�Ӧ��ⵥ�Ŀ������������ -------------------------' AS 'Case4';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type,F_NOStart,F_PurchasingPriceStart)
VALUES (0,'�ǰͿ�AB123','����123','��',4,'֧',4,4,'SP',1,
5,38,5.8,5.5,1,1,null,
3,30,'2019-01-14','20',0,0,'�ǰͿ�AB',0,0,25,20);
SET @iCommodityID = LAST_INSERT_ID();
-- 
INSERT INTO T_Warehousing (F_Status,F_ProviderID,F_WarehouseID,F_StaffID,F_CreateDatetime,F_PurchasingOrderID) VALUES (1, 1, 1, 3, now(), NULL);
SET @iWarehousingID = last_insert_id();
INSERT INTO T_WarehousingCommodity (F_WarehousingID,F_CommodityID,F_NO,F_PackageUnitID,F_CommodityName,F_BarcodeID,F_Price,F_Amount,F_ProductionDatetime,F_ShelfLife,F_ExpireDatetime,F_NOSalable)
VALUES (@iWarehousingID, @iCommodityID, 20, 1, '�ǰͿ�AB123', 1, 20, 20 * 20, now(), 12, now(), 20);
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckWarehousing(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('��Ʒ', @iCommodityID, '���ڳ������Ͷ�Ӧ��ⵥ�Ŀ������������') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';
-- 
DELETE FROM T_WarehousingCommodity WHERE F_CommodityID = @iCommodityID;
DELETE FROM T_Warehousing WHERE F_ID = @iWarehousingID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- Case5:��ƷType��0,���ڳ���Ʒ���ڳ��ɹ��ۺͶ�Ӧ��ⵥ�Ľ����۲���� -------------------------' AS 'Case5';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type,F_NOStart,F_PurchasingPriceStart)
VALUES (0,'�ǰͿ�AB123','����123','��',4,'֧',4,4,'SP',1,
5,38,5.8,5.5,1,1,null,
3,30,'2019-01-14','20',0,0,'�ǰͿ�AB',0,0,20,25.1);
SET @iCommodityID = LAST_INSERT_ID();
-- 
INSERT INTO T_Warehousing (F_Status,F_ProviderID,F_WarehouseID,F_StaffID,F_CreateDatetime,F_PurchasingOrderID) VALUES (1, 1, 1, 3, now(), NULL);
SET @iWarehousingID = last_insert_id();
INSERT INTO T_WarehousingCommodity (F_WarehousingID,F_CommodityID,F_NO,F_PackageUnitID,F_CommodityName,F_BarcodeID,F_Price,F_Amount,F_ProductionDatetime,F_ShelfLife,F_ExpireDatetime,F_NOSalable)
VALUES (@iWarehousingID, @iCommodityID, 20, 1, '�ǰͿ�AB123', 1, 20, 20 * 20, now(), 12, now(), 20);
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckWarehousing(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('��Ʒ', @iCommodityID, '���ڳ��ɹ��ۺͶ�Ӧ��ⵥ�Ľ����۲����') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';
-- 
DELETE FROM T_WarehousingCommodity WHERE F_CommodityID = @iCommodityID;
DELETE FROM T_Warehousing WHERE F_ID = @iWarehousingID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- Case6:��ƷType��3,������ⵥ -------------------------' AS 'Case6';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'Բͨ','���','��',4,NULL,4,4,'YT',1,
0,38,5.8,5.5,1,1,null,
0,30,'2019-01-14','0',0,0,'Բͨ���',0,3);
SET @iCommodityID = LAST_INSERT_ID();
-- 
INSERT INTO T_Warehousing (F_Status,F_ProviderID,F_WarehouseID,F_StaffID,F_CreateDatetime,F_PurchasingOrderID) VALUES (1, 1, 1, 3, now(), NULL);
SET @iWarehousingID = last_insert_id();
INSERT INTO T_WarehousingCommodity (F_WarehousingID,F_CommodityID,F_NO,F_PackageUnitID,F_CommodityName,F_BarcodeID,F_Price,F_Amount,F_ProductionDatetime,F_ShelfLife,F_ExpireDatetime,F_NOSalable)
VALUES (@iWarehousingID, @iCommodityID, 20, 1, '�ǰͿ�AB123', 1, 20, 20 * 20, now(), 0, now(), 20);
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckWarehousing(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('��Ʒ', @iCommodityID, '���ǵ�Ʒ���������') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case6 Testing Result';
-- 
DELETE FROM T_WarehousingCommodity WHERE F_CommodityID = @iCommodityID;
DELETE FROM T_Warehousing WHERE F_ID = @iWarehousingID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;