SELECT '++++++++++++++++++ Test_SP_Commodity_CreateMultiPackaging.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:�������� -------------------------' AS 'Case1';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'TestCreateMultiPackaging1','����','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,30,now(),'20',0,0,'.................',0);
SET @iCommodityID = LAST_INSERT_ID(); -- �õ�Ʒ���ڱ����Դ������װ��Ʒ�Ĳ�����Ʒ

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStatus = 0;
SET @sName = '�ɱȿ���Ƭ(����)333';
SET @sShortName = '��Ƭ';
SET @sSpecification = '��';
SET @iPackageUnitID = 1;
SET @sPurchasingUnit = '��';
SET @sBrandID = 3;
SET @sCategoryID = 1;
SET @sMnemonicCode = 'SP';
SET @sPricingType = 1;
SET @iLatestPricePurchase = 8;
SET @sPriceRetail = 12;
SET @sPriceVIP = 11.8;
SET @sPriceWholesale = 11;
SET @sCanChangePrice = 1;
SET @sRuleOfPoint = 1;
SET @sShelfLife = 3;
SET @sReturnDays = 30;
SET @sPurchaseFlag = 20;
SET @sRefCommodityID = @iCommodityID;
SET @sRefCommodityMultiple = 10;
SET @sTag = '...............';
SET @iStaffID = 3; 
SET @sBarcode = '5sdf31Sd8f321Sd832sd';
SET @sPropertyValue1 = 1;
SET @sPropertyValue2 = 1;
SET @sPropertyValue3 = 1;
SET @sPropertyValue4 = 1;
SET @iSyncSequence = 1;
SET @iNOStart = -1;
SET @fPurchasingPriceStart = -1;
SET @warehousingID = 0;

CALL SP_Commodity_CreateMultiPackaging(@iErrorCode, @sErrorMsg, @iStatus, @sName, @sShortName, @sSpecification, @iPackageUnitID, @sPurchasingUnit, @sBrandID, @sCategoryID, 
	@sMnemonicCode, @sPricingType, @sPriceVIP, @sPriceWholesale, @sCanChangePrice, @sRuleOfPoint, @sShelfLife,
	@sReturnDays, @sPurchaseFlag, @sRefCommodityID, @sRefCommodityMultiple, @sTag, @iStaffID, @sBarcode, @sPropertyValue1, @sPropertyValue2, 
	@sPropertyValue3, @sPropertyValue4, @iSyncSequence);
	
SELECT @iErrorCode;
SELECT @sErrorMsg;		
SELECT 1 FROM t_commodity WHERE 
	F_Status = @iStatus AND
	F_Name = @sName AND
	F_BrandID = @sBrandID AND
	F_CategoryID = @sCategoryID AND
	F_MnemonicCode = @sMnemonicCode AND
	F_Tag = @sTag;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE1 Testing Result';
-- 
SET @iCommodityIDforCommShopInfo = 0;
SELECT F_ID INTO @iCommodityIDforCommShopInfo FROM t_commodity ORDER BY F_ID DESC LIMIT 0,1;
SELECT @iCommodityIDforCommShopInfo;
SET @iShopID = 2;
-- 
CALL SP_CommodityShopInfo_Create(@iErrorCode, @sErrorMsg, @iCommodityIDforCommShopInfo, @iShopID, @iLatestPricePurchase, @sPriceRetail, @iNOStart, @fPurchasingPriceStart, @iStaffID, @warehousingID);
-- 
SELECT @sErrorMsg;
SELECT 1 FROM t_commodityshopinfo WHERE F_CommodityID = @iCommodityIDforCommShopInfo AND F_ShopID = @iShopID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';
-- 
-- 
SELECT 1 FROM t_commodityhistory WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 0,1)
AND F_OldValue = ''
AND F_NewValue = @sBarcode;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE1 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 1,1)
AND F_OldValue = ''
AND F_NewValue = (SELECT F_Name FROM t_packageunit WHERE F_ID = @iPackageUnitID);
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE1 Testing Result';
-- 
SELECT 1 FROM t_barcodes WHERE F_Barcode = '5sdf31Sd8f321Sd832sd';
SELECT IF(found_rows() = 1, '���Գɹ�', '����ʧ��') AS 'CASE1 Testing Result';

SELECT '-------------------- Case2:�����ظ� -------------------------' AS 'Case2';
SET @sErrorMsg = '';
SET @sName = '�ɱȿ���Ƭ(����)333';
SET @iSyncSequence = 1;

CALL SP_Commodity_CreateMultiPackaging(@iErrorCode, @sErrorMsg, @iStatus, @sName, @sShortName, @sSpecification, @iPackageUnitID, @sPurchasingUnit, @sBrandID, @sCategoryID, 
	@sMnemonicCode, @sPricingType, @sPriceVIP, @sPriceWholesale, @sCanChangePrice, @sRuleOfPoint, @sShelfLife,
	@sReturnDays, @sPurchaseFlag, @sRefCommodityID, @sRefCommodityMultiple, @sTag, @iStaffID, @sBarcode, @sPropertyValue1, @sPropertyValue2, 
	@sPropertyValue3, @sPropertyValue4, @iSyncSequence);
	
SELECT @iErrorCode;
SELECT @sErrorMsg;	
SELECT 1 FROM t_commodity WHERE 
	F_Status = @iStatus AND
	F_Name = @sName AND
	F_BrandID = @sBrandID AND
	F_CategoryID = @sCategoryID AND
	F_MnemonicCode = @sMnemonicCode AND
	F_Tag = @sTag;
SELECT IF(found_rows() = 1 AND @iErrorCode = 1, '���Գɹ�', '����ʧ��') AS 'CASE2 Testing Result';

SELECT 1 FROM t_barcodes WHERE F_Barcode = '5sdf31Sd8f321Sd832sd';
SELECT IF(found_rows() = 1, '���Գɹ�', '����ʧ��') AS 'CASE1 Testing Result';

DELETE FROM t_commodityhistory WHERE F_CommodityID IN (SELECT F_ID FROM t_commodity WHERE F_Name = '�ɱȿ���Ƭ(����)333');
DELETE FROM t_barcodes WHERE F_Barcode = '5sdf31Sd8f321Sd832sd';
DELETE FROM t_commodityshopinfo WHERE F_CommodityID = @iCommodityIDforCommShopInfo;
DELETE FROM t_commodity WHERE F_Name = '�ɱȿ���Ƭ(����)333';

SELECT '-------------------- case3�����Ʋ��ظ���not null�ֶδ���һ��null ���ش�����3 -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '�ɱȿ���Ƭ(����)3a3as';
SET @sSpecification = NULL;
SET @iSyncSequence = 1;

CALL SP_Commodity_CreateMultiPackaging(@iErrorCode, @sErrorMsg, @iStatus, @sName, @sShortName, @sSpecification, @iPackageUnitID, @sPurchasingUnit, @sBrandID, @sCategoryID, 
	@sMnemonicCode, @sPricingType, @sPriceVIP, @sPriceWholesale, @sCanChangePrice, @sRuleOfPoint, @sShelfLife,
	@sReturnDays, @sPurchaseFlag, @sRefCommodityID, @sRefCommodityMultiple, @sTag, @iStaffID, @sBarcode, @sPropertyValue1, @sPropertyValue2, 
	@sPropertyValue3, @sPropertyValue4, @iSyncSequence);

SELECT @iErrorCode;
SELECT @sErrorMsg;	
SELECT IF(@iErrorCode = 3, '���Գɹ�', '����ʧ��') AS 'CASE3 Testing Result';

SELECT '-------------------- Case5:staffID�����ڣ�����4 -------------------------' AS 'Case5';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStatus = 0;
SET @sName = '�ɱȿ���Ƭ(����)33';
SET @sShortName = '��Ƭ';
SET @sSpecification = '��';
SET @iPackageUnitID = 1;
SET @sPurchasingUnit = '��';
SET @sBrandID = 3;
SET @sCategoryID = 1;
SET @sMnemonicCode = 'SP';
SET @sPricingType = 1;
SET @iLatestPricePurchase = 8;
SET @sPriceRetail = 12;
SET @sPriceVIP = 11.8;
SET @sPriceWholesale = 11;
SET @sCanChangePrice = 1;
SET @sRuleOfPoint = 1;
SET @sShelfLife = 3;
SET @sReturnDays = 30;
SET @sPurchaseFlag = 20;
SET @sRefCommodityID = @iCommodityID;
SET @sRefCommodityMultiple = 10;
SET @sTag = '...............';
SET @sStartValueRemark = 'AAAAAAAAA';
SET @iStaffID = -1; 
SET @sBarcode = '5sdf31Sd8f321Sd832sd';
SET @sPropertyValue1 = 1;
SET @sPropertyValue2 = 1;
SET @sPropertyValue3 = 1;
SET @sPropertyValue4 = 1;
SET @iSyncSequence = 1;

CALL SP_Commodity_CreateMultiPackaging(@iErrorCode, @sErrorMsg, @iStatus, @sName, @sShortName, @sSpecification, @iPackageUnitID, @sPurchasingUnit, @sBrandID, @sCategoryID, 
	@sMnemonicCode, @sPricingType, @sPriceVIP, @sPriceWholesale, @sCanChangePrice, @sRuleOfPoint, @sShelfLife,
	@sReturnDays, @sPurchaseFlag, @sRefCommodityID, @sRefCommodityMultiple, @sTag, @iStaffID, @sBarcode, @sPropertyValue1, @sPropertyValue2, 
	@sPropertyValue3, @sPropertyValue4, @iSyncSequence);
	
SELECT @sErrorMsg;		
SELECT 1 FROM t_commodity WHERE 
	F_Status = @iStatus AND
	F_Name = @sName AND
	F_BrandID = @sBrandID AND
	F_CategoryID = @sCategoryID AND
	F_MnemonicCode = @sMnemonicCode AND
	F_Tag = @sTag;
SELECT IF(found_rows() = 0 AND @iErrorCode = 4, '���Գɹ�', '����ʧ��') AS 'CASE5 Testing Result';

SELECT '-------------------- Case7:�ò����ڵ�Ʒ��ID���д��� -------------------------' AS 'Case7';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStatus = 0;
SET @sName = '�ɱȿ���Ƭ(����)333';
SET @sShortName = '��Ƭ';
SET @sSpecification = '��';
SET @iPackageUnitID = 1;
SET @sPurchasingUnit = '��';
SET @sBrandID = -999;
SET @sCategoryID = 1;
SET @sMnemonicCode = 'SP';
SET @sPricingType = 1;
SET @iLatestPricePurchase = 8;
SET @sPriceRetail = 12;
SET @sPriceVIP = 11.8;
SET @sPriceWholesale = 11;
SET @sCanChangePrice = 1;
SET @sRuleOfPoint = 1;
SET @sShelfLife = 3;
SET @sReturnDays = 30;
SET @sPurchaseFlag = 20;
SET @sRefCommodityID = @iCommodityID;
SET @sRefCommodityMultiple = 10;
SET @sTag = '...............';
SET @iStaffID = 3; 
SET @sBarcode = '5sdf31Sd8f321Sd832sd';
SET @sPropertyValue1 = 1;
SET @sPropertyValue2 = 1;
SET @sPropertyValue3 = 1;
SET @sPropertyValue4 = 1;
SET @iSyncSequence = 1;

CALL SP_Commodity_CreateMultiPackaging(@iErrorCode, @sErrorMsg, @iStatus, @sName, @sShortName, @sSpecification, @iPackageUnitID, @sPurchasingUnit, @sBrandID, @sCategoryID, 
	@sMnemonicCode, @sPricingType, @sPriceVIP, @sPriceWholesale, @sCanChangePrice, @sRuleOfPoint, @sShelfLife,
	@sReturnDays, @sPurchaseFlag, @sRefCommodityID, @sRefCommodityMultiple, @sTag, @iStaffID, @sBarcode, @sPropertyValue1, @sPropertyValue2, 
	@sPropertyValue3, @sPropertyValue4, @iSyncSequence);
	
SELECT @iErrorCode;
SELECT @sErrorMsg;	
SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'CASE7 Testing Result';

SELECT '-------------------- Case8:�ò����ڵ����ID���д��� -------------------------' AS 'Case8';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStatus = 0;
SET @sName = '�ɱȿ���Ƭ(����)3333';
SET @sShortName = '��Ƭ';
SET @sSpecification = '��';
SET @iPackageUnitID = 1;
SET @sPurchasingUnit = '��';
SET @sBrandID = 1;
SET @sCategoryID = -999;
SET @sMnemonicCode = 'SP';
SET @sPricingType = 1;
SET @iLatestPricePurchase = 8;
SET @sPriceRetail = 12;
SET @sPriceVIP = 11.8;
SET @sPriceWholesale = 11;
SET @sCanChangePrice = 1;
SET @sRuleOfPoint = 1;
SET @sShelfLife = 3;
SET @sReturnDays = 30;
SET @sPurchaseFlag = 20;
SET @sRefCommodityID = @iCommodityID;
SET @sRefCommodityMultiple = 10;
SET @sTag = '...............';
SET @iStaffID = 3; 
SET @sBarcode = '5sdf31Sd8f321Sd832sd';
SET @sPropertyValue1 = 1;
SET @sPropertyValue2 = 1;
SET @sPropertyValue3 = 1;
SET @sPropertyValue4 = 1;
SET @iSyncSequence = 1;

CALL SP_Commodity_CreateMultiPackaging(@iErrorCode, @sErrorMsg, @iStatus, @sName, @sShortName, @sSpecification, @iPackageUnitID, @sPurchasingUnit, @sBrandID, @sCategoryID, 
	@sMnemonicCode, @sPricingType, @sPriceVIP, @sPriceWholesale, @sCanChangePrice, @sRuleOfPoint, @sShelfLife,
	@sReturnDays, @sPurchaseFlag, @sRefCommodityID, @sRefCommodityMultiple, @sTag, @iStaffID, @sBarcode, @sPropertyValue1, @sPropertyValue2, 
	@sPropertyValue3, @sPropertyValue4, @iSyncSequence);
	
SELECT @iErrorCode;
SELECT @sErrorMsg;	
SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'CASE8 Testing Result';

SELECT '-------------------- Case9:�ò����ڵİ�װ��λID���д��� -------------------------' AS 'Case9';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStatus = 0;
SET @sName = '�ɱȿ���Ƭ(����)3333';
SET @sShortName = '��Ƭ';
SET @sSpecification = '��';
SET @iPackageUnitID = -99;
SET @sPurchasingUnit = '��';
SET @sBrandID = 1;
SET @sCategoryID = 1;
SET @sMnemonicCode = 'SP';
SET @sPricingType = 1;
SET @iLatestPricePurchase = 8;
SET @sPriceRetail = 12;
SET @sPriceVIP = 11.8;
SET @sPriceWholesale = 11;
SET @sCanChangePrice = 1;
SET @sRuleOfPoint = 1;
SET @sShelfLife = 3;
SET @sReturnDays = 30;
SET @sPurchaseFlag = 20;
SET @sRefCommodityID = @iCommodityID;
SET @sRefCommodityMultiple = 10;
SET @sTag = '...............';
SET @iStaffID = 3; 
SET @sBarcode = '5sdf31Sd8f321Sd832sd';
SET @sPropertyValue1 = 1;
SET @sPropertyValue2 = 1;
SET @sPropertyValue3 = 1;
SET @sPropertyValue4 = 1;
SET @iSyncSequence = 1;

CALL SP_Commodity_CreateMultiPackaging(@iErrorCode, @sErrorMsg, @iStatus, @sName, @sShortName, @sSpecification, @iPackageUnitID, @sPurchasingUnit, @sBrandID, @sCategoryID, 
	@sMnemonicCode, @sPricingType, @sPriceVIP, @sPriceWholesale, @sCanChangePrice, @sRuleOfPoint, @sShelfLife,
	@sReturnDays, @sPurchaseFlag, @sRefCommodityID, @sRefCommodityMultiple, @sTag, @iStaffID, @sBarcode, @sPropertyValue1, @sPropertyValue2, 
	@sPropertyValue3, @sPropertyValue4, @iSyncSequence);
	
SELECT @iErrorCode;
SELECT @sErrorMsg;	
SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'CASE9 Testing Result';

SELECT '-------------------- Case16:�������װ��Ʒ��RefCommodityID �����ڣ�����ʧ��-------------------------' AS 'Case16';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStatus = 0;
SET @sName = '�ɱȿ���Ƭ(����)333';
SET @sShortName = '��Ƭ';
SET @sSpecification = '��';
SET @iPackageUnitID = 1;
SET @sPurchasingUnit = '��';
SET @sBrandID = 3;
SET @sCategoryID = 1;
SET @sMnemonicCode = 'SP';
SET @sPricingType = 1;
SET @iLatestPricePurchase = 8;
SET @sPriceRetail = 12;
SET @sPriceVIP = 11.8;
SET @sPriceWholesale = 11;
SET @sCanChangePrice = 1;
SET @sRuleOfPoint = 1;
SET @sShelfLife = 3;
SET @sReturnDays = 30;
SET @sPurchaseFlag = 20;
SET @sRefCommodityID = 0;
SET @sRefCommodityMultiple = 10;
SET @sTag = '...............';
SET @iStaffID = 3; 
SET @sBarcode = '5sdf31Sd8f321Sd832sd';
SET @sPropertyValue1 = 1;
SET @sPropertyValue2 = 1;
SET @sPropertyValue3 = 1;
SET @sPropertyValue4 = 1;
SET @iSyncSequence = 1;

CALL SP_Commodity_CreateMultiPackaging(@iErrorCode, @sErrorMsg, @iStatus, @sName, @sShortName, @sSpecification, @iPackageUnitID, @sPurchasingUnit, @sBrandID, @sCategoryID, 
	@sMnemonicCode, @sPricingType, @sPriceVIP, @sPriceWholesale, @sCanChangePrice, @sRuleOfPoint, @sShelfLife,
	@sReturnDays, @sPurchaseFlag, @sRefCommodityID, @sRefCommodityMultiple, @sTag, @iStaffID, @sBarcode, @sPropertyValue1, @sPropertyValue2, 
	@sPropertyValue3, @sPropertyValue4, @iSyncSequence);
	
SELECT @iErrorCode;
SELECT @sErrorMsg;		
SELECT 1 FROM t_commodity WHERE 
	F_Status = @iStatus AND
	F_Name = @sName AND
	F_BrandID = @sBrandID AND
	F_CategoryID = @sCategoryID AND
	F_MnemonicCode = @sMnemonicCode AND
	F_Tag = @sTag;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'CASE16 Testing Result';

SELECT '-------------------- Case17:�������װ��Ʒ��RefCommodityID ��Ӧ����Ʒ�������Ʒ������ʧ��-------------------------' AS 'Case17';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������1��','����','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,30,now(),'20',0,0,'.................',1);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStatus = 0;
SET @sName = '�ɱȿ���Ƭ(����)333';
SET @sShortName = '��Ƭ';
SET @sSpecification = '��';
SET @iPackageUnitID = 1;
SET @sPurchasingUnit = '��';
SET @sBrandID = 3;
SET @sCategoryID = 1;
SET @sMnemonicCode = 'SP';
SET @sPricingType = 1;
SET @iLatestPricePurchase = 8;
SET @sPriceRetail = 12;
SET @sPriceVIP = 11.8;
SET @sPriceWholesale = 11;
SET @sCanChangePrice = 1;
SET @sRuleOfPoint = 1;
SET @sShelfLife = 3;
SET @sReturnDays = 30;
SET @sPurchaseFlag = 20;
SET @sRefCommodityID = last_insert_id();
SET @sRefCommodityMultiple = 10;
SET @sTag = '...............';
SET @iStaffID = 3; 
SET @sBarcode = '5sdf31Sd8f321Sd832sd';
SET @sPropertyValue1 = 1;
SET @sPropertyValue2 = 1;
SET @sPropertyValue3 = 1;
SET @sPropertyValue4 = 1;
SET @iSyncSequence = 1;

CALL SP_Commodity_CreateMultiPackaging(@iErrorCode, @sErrorMsg, @iStatus, @sName, @sShortName, @sSpecification, @iPackageUnitID, @sPurchasingUnit, @sBrandID, @sCategoryID, 
	@sMnemonicCode, @sPricingType, @sPriceVIP, @sPriceWholesale, @sCanChangePrice, @sRuleOfPoint, @sShelfLife,
	@sReturnDays, @sPurchaseFlag, @sRefCommodityID, @sRefCommodityMultiple, @sTag, @iStaffID, @sBarcode, @sPropertyValue1, @sPropertyValue2, 
	@sPropertyValue3, @sPropertyValue4, @iSyncSequence);
	
SELECT @iErrorCode;
SELECT @sErrorMsg;		
SELECT 1 FROM t_commodity WHERE 
	F_Status = @iStatus AND
	F_Name = @sName AND
	F_BrandID = @sBrandID AND
	F_CategoryID = @sCategoryID AND
	F_MnemonicCode = @sMnemonicCode AND
	F_Tag = @sTag;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'CASE17 Testing Result';

DELETE FROM t_commodity WHERE F_ID = @sRefCommodityID;


SELECT '-------------------- Case19:��������Ʒ��Ԥ��̭��Ʒ���� -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStatus = 0;
SET @sName = '��ʦ��ţ����';
SET @sShortName = '������';
SET @sSpecification = '��';
SET @iPackageUnitID = 1;
SET @sPurchasingUnit = '��';
SET @sBrandID = 3;
SET @sCategoryID = 1;
SET @sMnemonicCode = 'SP';
SET @sPricingType = 1;
SET @iLatestPricePurchase = 8;
SET @sPriceRetail = 12;
SET @sPriceVIP = 11.8;
SET @sPriceWholesale = 11;
SET @sCanChangePrice = 1;
SET @sRuleOfPoint = 1;
SET @sShelfLife = 3;
SET @sReturnDays = 30;
SET @sPurchaseFlag = 20;
SET @sRefCommodityID = @iCommodityID;
SET @sRefCommodityMultiple = 10;
SET @sTag = '...............';
SET @iStaffID = 3; 
SET @sBarcode = '5sdf61Sd8f331Sd832sd';
SET @sPropertyValue1 = 1;
SET @sPropertyValue2 = 1;
SET @sPropertyValue3 = 1;
SET @sPropertyValue4 = 1;
SET @iSyncSequence = 1;

CALL SP_Commodity_CreateMultiPackaging(@iErrorCode, @sErrorMsg, @iStatus, @sName, @sShortName, @sSpecification, @iPackageUnitID, @sPurchasingUnit, @sBrandID, @sCategoryID, 
	@sMnemonicCode, @sPricingType, @sPriceVIP, @sPriceWholesale, @sCanChangePrice, @sRuleOfPoint, @sShelfLife,
	@sReturnDays, @sPurchaseFlag, @sRefCommodityID, @sRefCommodityMultiple, @sTag, @iStaffID, @sBarcode, @sPropertyValue1, @sPropertyValue2, 
	@sPropertyValue3, @sPropertyValue4, @iSyncSequence);
	
SELECT @iErrorCode;
SELECT @sErrorMsg;		
SELECT 1 FROM t_commodity WHERE F_Name = @sName;
SELECT IF(found_rows() = 1 AND @iErrorCode = 1, '���Գɹ�', '����ʧ��') AS 'CASE19 Testing Result';

SELECT '-------------------- Case20:����һ����Ʒ������ͬ��ɾ������Ʒ��������ͬ,�����ɹ� -------------------------' AS 'Case20';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStatus = 0;
SET @sName = '�ɱȿ���Ƭ(���)233';
SET @sShortName = '��Ƭ';
SET @sSpecification = '��';
SET @iPackageUnitID = 1;
SET @sPurchasingUnit = '��';
SET @sBrandID = 3;
SET @sCategoryID = 1;
SET @sMnemonicCode = 'SP';
SET @sPricingType = 1;
SET @iLatestPricePurchase = 8;
SET @sPriceRetail = 12;
SET @sPriceVIP = 11.8;
SET @sPriceWholesale = 11;
SET @sCanChangePrice = 1;
SET @sRuleOfPoint = 1;
SET @sShelfLife = 3;
SET @sReturnDays = 30;
SET @sPurchaseFlag = 20;
SET @sRefCommodityID = @iCommodityID;
SET @sRefCommodityMultiple = 10;
SET @sTag = '...............';
SET @iStaffID = 3; 
SET @sBarcode = '6sdf42Sd8f321Sd832sd';
SET @sPropertyValue1 = 1;
SET @sPropertyValue2 = 1;
SET @sPropertyValue3 = 1;
SET @sPropertyValue4 = 1;
SET @iSyncSequence = 1;

CALL SP_Commodity_CreateMultiPackaging(@iErrorCode, @sErrorMsg, @iStatus, @sName, @sShortName, @sSpecification, @iPackageUnitID, @sPurchasingUnit, @sBrandID, @sCategoryID, 
	@sMnemonicCode, @sPricingType, @sPriceVIP, @sPriceWholesale, @sCanChangePrice, @sRuleOfPoint, @sShelfLife,
	@sReturnDays, @sPurchaseFlag, @sRefCommodityID, @sRefCommodityMultiple, @sTag, @iStaffID, @sBarcode, @sPropertyValue1, @sPropertyValue2, 
	@sPropertyValue3, @sPropertyValue4, @iSyncSequence);
	
SELECT @iErrorCode;
SELECT @sErrorMsg;		
SELECT 1 FROM t_commodity WHERE 
	F_Status = @iStatus AND
	F_Name = @sName AND
	F_BrandID = @sBrandID AND
	F_CategoryID = @sCategoryID AND
	F_MnemonicCode = @sMnemonicCode AND
	F_Tag = @sTag;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE20 Testing Result';
-- 
SET @iCommodityIDforCommShopInfo = 0;
SELECT F_ID INTO @iCommodityIDforCommShopInfo FROM t_commodity ORDER BY F_ID DESC LIMIT 0,1;
SET @iShopID = 2;
-- 
CALL SP_CommodityShopInfo_Create(@iErrorCode, @sErrorMsg, @iCommodityIDforCommShopInfo, @iShopID, @iLatestPricePurchase, @sPriceRetail, @iNOStart, @fPurchasingPriceStart, @iStaffID, @warehousingID);
-- 
SELECT @sErrorMsg;
SELECT 1 FROM t_commodityshopinfo WHERE F_CommodityID = @iCommodityIDforCommShopInfo AND F_ShopID = @iShopID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';
-- 
-- 
SELECT 1 FROM t_commodityhistory WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 0,1)
AND F_OldValue = ''
AND F_NewValue = @sBarcode;
SELECT IF(found_rows() = 1, '���Գɹ�', '����ʧ��') AS 'CASE20 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 1,1)
AND F_OldValue = ''
AND F_NewValue = (SELECT F_Name FROM t_packageunit WHERE F_ID = @iPackageUnitID);
SELECT IF(found_rows() = 1, '���Գɹ�', '����ʧ��') AS 'CASE20 Testing Result';
-- 
SELECT 1 FROM t_barcodes WHERE F_Barcode = '6sdf42Sd8f321Sd832sd';
SELECT IF(found_rows() = 1, '���Գɹ�', '����ʧ��') AS 'CASE20 Testing Result';

-- ����Ʒ״̬����Ϊɾ��״̬����2.
DELETE FROM t_barcodes WHERE F_Barcode = '6sdf42Sd8f321Sd832sd';
DELETE FROM t_commodityshopinfo WHERE F_CommodityID = @iCommodityIDforCommShopInfo;
UPDATE t_commodity SET F_Status = 2 WHERE F_Name = '�ɱȿ���Ƭ(���)233'; 

-- ��Ʒ��������ɾ������Ʒ������ͬ�Ĵ���
SET @sErrorMsg = '';
SET @sName = '�ɱȿ���Ƭ(���)233';
SET @sBarcode='6sdf42W1ASD81Sd832sd';
SET @iSyncSequence = 1;
-- 
CALL SP_Commodity_CreateMultiPackaging(@iErrorCode, @sErrorMsg, @iStatus, @sName, @sShortName, @sSpecification, @iPackageUnitID, @sPurchasingUnit, @sBrandID, @sCategoryID, 
	@sMnemonicCode, @sPricingType, @sPriceVIP, @sPriceWholesale, @sCanChangePrice, @sRuleOfPoint, @sShelfLife,
	@sReturnDays, @sPurchaseFlag, @sRefCommodityID, @sRefCommodityMultiple, @sTag, @iStaffID, @sBarcode, @sPropertyValue1, @sPropertyValue2, 
	@sPropertyValue3, @sPropertyValue4, @iSyncSequence);
-- 
SELECT 1 FROM t_commodity WHERE 
	F_Status = @iStatus AND
	F_Name = @sName AND
	F_BrandID = @sBrandID AND
	F_CategoryID = @sCategoryID AND
	F_MnemonicCode = @sMnemonicCode AND
	F_Tag = @sTag;
SELECT @iErrorCode;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE20 Testing Result';
-- 
SET @iCommodityIDforCommShopInfo = 0;
SELECT F_ID INTO @iCommodityIDforCommShopInfo FROM t_commodity ORDER BY F_ID DESC LIMIT 0,1;
SELECT @iCommodityIDforCommShopInfo;
SET @iShopID = 2;
-- 
CALL SP_CommodityShopInfo_Create(@iErrorCode, @sErrorMsg, @iCommodityIDforCommShopInfo, @iShopID, @iLatestPricePurchase, @sPriceRetail, @iNOStart, @fPurchasingPriceStart, @iStaffID, @warehousingID);
-- 
SELECT @sErrorMsg;
SELECT 1 FROM t_commodityshopinfo WHERE F_CommodityID = @iCommodityIDforCommShopInfo AND F_ShopID = @iShopID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';
-- 
-- 
SELECT 1 FROM t_commodityhistory WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 0,1)
AND F_OldValue = ''
AND F_NewValue = @sBarcode;
SELECT IF(found_rows() = 1, '���Գɹ�', '����ʧ��') AS 'CASE20 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 1,1)
AND F_OldValue = ''
AND F_NewValue = (SELECT F_Name FROM t_packageunit WHERE F_ID = @iPackageUnitID);
SELECT IF(found_rows() = 1, '���Գɹ�', '����ʧ��') AS 'CASE20 Testing Result';
-- 
SELECT 1 FROM t_barcodes WHERE F_Barcode = '6sdf42W1ASD81Sd832sd';
SELECT IF(found_rows() = 1, '���Գɹ�', '����ʧ��') AS 'CASE20 Testing Result';
-- 
DELETE FROM t_commodityhistory WHERE F_CommodityID IN (SELECT F_ID FROM t_commodity WHERE F_Name = '�ɱȿ���Ƭ(���)233');
DELETE FROM t_barcodes WHERE F_Barcode = '6sdf42W1ASD81Sd832sd';
DELETE FROM t_commodityshopinfo WHERE F_CommodityID = @iCommodityIDforCommShopInfo;
DELETE FROM t_commodity WHERE F_Name = '�ɱȿ���Ƭ(���)233';


SELECT '-------------------- Case21:������ĳ���Ϊ1ʱ�����������볤��ֻ����7��64λ�� -------------------------' AS 'Case21';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStatus = 0;
SET @sName = '�ɱȿ���Ƭ(����)333';
SET @sShortName = '��Ƭ';
SET @sSpecification = '��';
SET @iPackageUnitID = 1;
SET @sPurchasingUnit = '��';
SET @sBrandID = 3;
SET @sCategoryID = 1;
SET @sMnemonicCode = 'SP';
SET @sPricingType = 1;
SET @iLatestPricePurchase = 8;
SET @sPriceRetail = 12;
SET @sPriceVIP = 11.8;
SET @sPriceWholesale = 11;
SET @sCanChangePrice = 1;
SET @sRuleOfPoint = 1;
SET @sShelfLife = 3;
SET @sReturnDays = 30;
SET @sPurchaseFlag = 20;
SET @sRefCommodityID = @iCommodityID;
SET @sRefCommodityMultiple = 10;
SET @sTag = '...............';
SET @iStaffID = 3; 
SET @sBarcode = '1';
SET @sPropertyValue1 = 1;
SET @sPropertyValue2 = 1;
SET @sPropertyValue3 = 1;
SET @sPropertyValue4 = 1;
SET @iSyncSequence = 1;

CALL SP_Commodity_CreateMultiPackaging(@iErrorCode, @sErrorMsg, @iStatus, @sName, @sShortName, @sSpecification, @iPackageUnitID, @sPurchasingUnit, @sBrandID, @sCategoryID, 
	@sMnemonicCode, @sPricingType, @sPriceVIP, @sPriceWholesale, @sCanChangePrice, @sRuleOfPoint, @sShelfLife,
	@sReturnDays, @sPurchaseFlag, @sRefCommodityID, @sRefCommodityMultiple, @sTag, @iStaffID, @sBarcode, @sPropertyValue1, @sPropertyValue2, 
	@sPropertyValue3, @sPropertyValue4, @iSyncSequence);
	
SELECT @iErrorCode;
SELECT @sErrorMsg;		
SELECT IF(@iErrorCode = 8, '���Գɹ�', '����ʧ��') AS 'CASE21 Testing Result';

SELECT '-------------------- Case22:�������װ��Ʒ��RefCommodityID ��Ӧ����Ʒ�Ƕ��װ��Ʒ������ʧ��-------------------------' AS 'Case17';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������1��','����','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,30,now(),'20',0,0,'.................',2);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStatus = 0;
SET @sName = '�ɱȿ���Ƭ(����)333';
SET @sShortName = '��Ƭ';
SET @sSpecification = '��';
SET @iPackageUnitID = 1;
SET @sPurchasingUnit = '��';
SET @sBrandID = 3;
SET @sCategoryID = 1;
SET @sMnemonicCode = 'SP';
SET @sPricingType = 1;
SET @iLatestPricePurchase = 8;
SET @sPriceRetail = 12;
SET @sPriceVIP = 11.8;
SET @sPriceWholesale = 11;
SET @sCanChangePrice = 1;
SET @sRuleOfPoint = 1;
SET @sShelfLife = 3;
SET @sReturnDays = 30;
SET @sPurchaseFlag = 20;
SET @sRefCommodityID = last_insert_id();
SET @sRefCommodityMultiple = 10;
SET @sTag = '...............';
SET @iStaffID = 3; 
SET @sBarcode = '5sdf31Sd8f321Sd832sd';
SET @sPropertyValue1 = 1;
SET @sPropertyValue2 = 1;
SET @sPropertyValue3 = 1;
SET @sPropertyValue4 = 1;
SET @iSyncSequence = 1;

CALL SP_Commodity_CreateMultiPackaging(@iErrorCode, @sErrorMsg, @iStatus, @sName, @sShortName, @sSpecification, @iPackageUnitID, @sPurchasingUnit, @sBrandID, @sCategoryID, 
	@sMnemonicCode, @sPricingType, @sPriceVIP, @sPriceWholesale, @sCanChangePrice, @sRuleOfPoint, @sShelfLife,
	@sReturnDays, @sPurchaseFlag, @sRefCommodityID, @sRefCommodityMultiple, @sTag, @iStaffID, @sBarcode, @sPropertyValue1, @sPropertyValue2, 
	@sPropertyValue3, @sPropertyValue4, @iSyncSequence);
	
SELECT @iErrorCode;
SELECT @sErrorMsg;		
SELECT 1 FROM t_commodity WHERE 
	F_Status = @iStatus AND
	F_Name = @sName AND
	F_BrandID = @sBrandID AND
	F_CategoryID = @sCategoryID AND
	F_MnemonicCode = @sMnemonicCode AND
	F_Tag = @sTag;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'CASE17 Testing Result';

DELETE FROM t_commodity WHERE F_ID = @sRefCommodityID;

SELECT '-------------------- Case23:�������װ��Ʒ��RefCommodityID ��Ӧ����Ʒ�Ƿ�����Ʒ������ʧ��-------------------------' AS 'Case17';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������1��','����','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,30,now(),'20',0,0,'.................',3);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStatus = 0;
SET @sName = '�ɱȿ���Ƭ(����)333';
SET @sShortName = '��Ƭ';
SET @sSpecification = '��';
SET @iPackageUnitID = 1;
SET @sPurchasingUnit = '��';
SET @sBrandID = 3;
SET @sCategoryID = 1;
SET @sMnemonicCode = 'SP';
SET @sPricingType = 1;
SET @iLatestPricePurchase = 8;
SET @sPriceRetail = 12;
SET @sPriceVIP = 11.8;
SET @sPriceWholesale = 11;
SET @sCanChangePrice = 1;
SET @sRuleOfPoint = 1;
SET @sShelfLife = 3;
SET @sReturnDays = 30;
SET @sPurchaseFlag = 20;
SET @sRefCommodityID = last_insert_id();
SET @sRefCommodityMultiple = 10;
SET @sTag = '...............';
SET @iType = 2;
SET @iNOStart = -1;
SET @fPurchasingPriceStart = -1;
SET @sStartValueRemark = '';
SET @iStaffID = 3; 
SET @sBarcode = '5sdf31Sd8f321Sd832sd';
SET @sPropertyValue1 = 1;
SET @sPropertyValue2 = 1;
SET @sPropertyValue3 = 1;
SET @sPropertyValue4 = 1;
SET @iSyncSequence = 1;

CALL SP_Commodity_CreateMultiPackaging(@iErrorCode, @sErrorMsg, @iStatus, @sName, @sShortName, @sSpecification, @iPackageUnitID, @sPurchasingUnit, @sBrandID, @sCategoryID, 
	@sMnemonicCode, @sPricingType, @sPriceVIP, @sPriceWholesale, @sCanChangePrice, @sRuleOfPoint, @sShelfLife,
	@sReturnDays, @sPurchaseFlag, @sRefCommodityID, @sRefCommodityMultiple, @sTag, @iStaffID, @sBarcode, @sPropertyValue1, @sPropertyValue2, 
	@sPropertyValue3, @sPropertyValue4, @iSyncSequence);
	
SELECT @iErrorCode;
SELECT @sErrorMsg;		
SELECT 1 FROM t_commodity WHERE 
	F_Status = @iStatus AND
	F_Name = @sName AND
	F_BrandID = @sBrandID AND
	F_CategoryID = @sCategoryID AND
	F_MnemonicCode = @sMnemonicCode AND
	F_Tag = @sTag AND 
	F_Type = @iType;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'CASE17 Testing Result';

DELETE FROM t_commodity WHERE F_ID = @sRefCommodityID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;