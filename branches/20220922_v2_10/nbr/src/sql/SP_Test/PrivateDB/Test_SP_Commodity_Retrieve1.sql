SELECT '++++++++++++++++++ Test_SP_Commodity_Retrieve1.sql ++++++++++++++++++++';
SELECT '-------------------- Case1:Commodity is not deleted -------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iIncludeDeleted = 0;

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'�ɱȿ���Ƭ�տ�ζ','��Ƭ','��',1,'��',3,2,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................', 0);

SET @iID = last_insert_id();

CALL SP_Commodity_Retrieve1(@iErrorCode, @sErrorMsg, @iID, @iIncludeDeleted);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_commodity WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

SELECT '-------------------- Case2:Commodity is deleted -------------------------' AS 'Case2';
SET @sErrorMsg = '';
UPDATE t_commodity SET F_Status = 2 WHERE F_ID = @iID;

CALL SP_Commodity_Retrieve1(@iErrorCode, @sErrorMsg, @iID, @iIncludeDeleted);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_commodity WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 2, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

SELECT '-------------------- case3:���Բ�ѯ��ɾ������Ʒ -------------------------' AS 'Case3';
SET @iIncludeDeleted = 1;
SET @sErrorMsg = '';
CALL SP_Commodity_Retrieve1(@iErrorCode, @sErrorMsg, @iID, @iIncludeDeleted);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_commodity WHERE F_ID = @iID AND F_Status = 2;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE3 Testing Result';

DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- case4:��ѯ�����ڵ�ID ec=0 -------------------------' AS 'Case4';
SET @iID=-222;
SET @sErrorMsg = '';
CALL SP_Commodity_Retrieve1(@iErrorCode, @sErrorMsg, @iID, @iIncludeDeleted);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_commodity WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE4 Testing Result';

SELECT '-------------------- case5:��ѯ�����Ʒ��Ҳ���ѯ��Ӧ������Ʒ ec=0 -------------------------' AS 'Case5';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'�����Ʒ��Ƭ','��Ƭ','��',1,'��',3,2,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................', 1);
SET @iCommIDA = last_insert_id();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'����Ʒ��ƬA','��Ƭ','��',1,'��',3,2,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................', 0);
SET @iCommIDB = last_insert_id();
INSERT INTO T_SubCommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price)
VALUES (@iCommIDA, @iCommIDB, 1, 8);
SET @iSubCommIDA = last_insert_id();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'����Ʒ��ƬB','��Ƭ','��',1,'��',3,2,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................', 0);
SET @iCommIDC = last_insert_id();
INSERT INTO T_SubCommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price)
VALUES (@iCommIDA, @iCommIDC, 1, 8);
SET @iSubCommIDB = last_insert_id();
-- 
SET @iIncludeDeleted = 0;
SET @iErrorCode = 0;
SET @sErrorMsg = '';
CALL SP_Commodity_Retrieve1(@iErrorCode, @sErrorMsg, @iCommIDA, @iIncludeDeleted);
SELECT IF(found_rows() = 2 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE5 Testing Result';
-- 
DELETE FROM t_subcommodity WHERE F_ID IN (@iSubCommIDA, @iSubCommIDB);
DELETE FROM t_commodity WHERE F_ID IN(@iCommIDA, @iCommIDB, @iCommIDC);