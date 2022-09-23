SELECT '++++++++++++++++++ Test_SP_Warehouse_RetrieveInventory.sql ++++++++++++++++++++';
SELECT '-------------------- Case1:������ѯ  -------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 2;
--	SET @fTotalInventory = 0.000000;
--	SET @fMaxTotalInventory = 0.000000;
--	SET @sName = '';

--	CALL SP_Warehouse_RetrieveInventory(@iErrorCode, @sErrorMsg, @fTotalInventory, @fMaxTotalInventory, @sName);
CALL SP_Warehouse_RetrieveInventory(@iErrorCode, @sErrorMsg, @iShopID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

SELECT '-------------------- Case2:��ѯ������Ʒ�����ܶ���ߵ���Ʒ�������ܶ�-------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_Type)
VALUES (0,'�ɱȿ���Ƭ22','��Ƭ','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,30,'2018-04-14 01:00:01','20',0,0,'1111111',0);

SET @commodityID=last_insert_id();
SET @iShopID = 2;
INSERT INTO t_commodityshopinfo (F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@commodityID, 2,10000000/*F_LatestPricePurchase*/,12, 2, -1, -1, NULL);
SET @iCommShopInfoID = last_insert_id();

CALL SP_Warehouse_RetrieveInventory(@iErrorCode, @sErrorMsg, @iShopID);

SELECT @iErrorCode;
SELECT @sErrorMsg;

SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

DELETE FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
DELETE FROM t_commodity WHERE F_ID = @commodityID;

SELECT '-------------------- Case3:��ѯ������Ʒ�������ܶ�Ϊ��-------------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_Type)
VALUES (0,'�ɱȿ���Ƭ22','��Ƭ','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,30,'2018-04-14 01:00:01','20',0,0,'1111111',0);

SET @commodityID=last_insert_id();
SET @iShopID = 2;
INSERT INTO t_commodityshopinfo (F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@commodityID, 2,10000000/*F_LatestPricePurchase*/,12, -2, -1, -1, NULL);
SET @iCommShopInfoID = last_insert_id();

CALL SP_Warehouse_RetrieveInventory(@iErrorCode, @sErrorMsg, @iShopID);

SELECT @iErrorCode;
SELECT @sErrorMsg;

SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';
DELETE FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
DELETE FROM t_commodity WHERE F_ID = @commodityID;

-- �����������������Ҫ�Ȱ�����������Ʒɾ��
-- SELECT '-------------------- Case4:��ѯ������Ʒ����������ܶ�Ϊ��-------------------------' AS 'Case4';
-- ��Ҫ�ֶ��������case�����裺
-- ɾ��������Ʒ(��������������һ�д���)��
-- update T_Commodity set F_Status = 2;
-- Ȼ������case 3��
-- SELECT '-------------------- Case5:��δɾ���ĵ�Ʒʱ��������ܶ�Ϊ0-------------------------' AS 'Case5';
-- ģ��case 4��ֻ��case 3�е�CALL SP_Warehouse_RetrieveInventory(@iErrorCode, @sErrorMsg);�ͽ����֤����