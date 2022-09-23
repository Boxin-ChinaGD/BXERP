SELECT '++++++++++++++++++ Test_SP_Commodity_RetrieveInventory.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:��ѯδɾ��״̬��Ʒ�Ŀ�� -------------------------' AS 'Case1';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_Type)
VALUES (0,'�ɱȿ���Ƭ22','��Ƭ','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,30,'2018-04-14 01:00:01','20',0,0,'1111111',0);
SET @iID = last_insert_id();

SET @iErrorCode=0;
SET @sErrorMsg = '';
SET @iShopID = 2;

CALL SP_Commodity_RetrieveInventory(@iErrorCode, @sErrorMsg, @iID, @iShopID);

SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case2:��ѯ�Ѿ�ɾ��״̬��Ʒ�Ŀ�� -------------------------' AS 'Case2';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (2,'������5','������','��',3,'��',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,30,'2018-04-14','20',56,3,'1111111',2);

SET @iID = last_insert_id();

SET @iErrorCode=0;
SET @sErrorMsg = '';
SET @iShopID = 2;

CALL SP_Commodity_RetrieveInventory(@iErrorCode, @sErrorMsg, @iID, @iShopID);

SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 2, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case3:��ѯ�����ڵ���Ʒ��� -------------------------' AS 'Case3';

SET @iID = -1;
SET @sErrorMsg = '';
SET @iShopID = 2;
CALL SP_Commodity_RetrieveInventory(@iErrorCode, @sErrorMsg, @iID, @iShopID);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';