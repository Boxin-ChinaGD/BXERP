SELECT '++++++++++++++++++ Test_SP_Commodity_Update.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:�����޸� F_Nameδ�޸� �޸�����������-------------------------' AS 'Case1'; 
	
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������1��','����','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,30,now(),'20',0,0,'.................', 0);
SET @insertId = last_insert_id();
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();		 
SET @sName = '������2��';		          
SET @sShortName = '����2';     
SET @sSpecification = '����';   
SET @iPackageUnitID = 2;     
SET @iBrandID = 3;        
SET @iCategoryID = 4;             
SET @sMnemonicCode = 'BS';    
SET @fPricingType = 1;     
-- SET @iIsServiceType = 1;
SET @fPriceVIP = 11.8;    
SET @fPriceWholesale = 0.363986;         
-- SET @fRatioGrossMargin = 0.1;     
SET @fsCanChangePrice = 0;  
SET @sRuleOfPoint = 1;    
SET @sPicture = '/p/private_db/nbr/1.jpg';  
SET @sShelfLife = 3;       
SET @sReturnDays = 30;        
SET @fPurchaseFlag = 0;         
-- SET @sRefCommodityID = 1;
SET @sRefCommodityMultiple = 1; 
-- SET @sIsGift = 0;  
SET @sTag = '111';
-- SET @iType = 0;
SET @iINT2 = 3;
SET @sStartValueRemark = '';
SET @sPropertyValue1 = 1;
SET @sPropertyValue2 = 1;
SET @sPropertyValue3 = 1;
SET @sPropertyValue4 = 1;
SET @iShopID = 2;

call SP_Commodity_Update(
	@iErrorCode,
	@sErrorMsg,
	@iID,
	@sName,
	@sShortName,
	@sSpecification,
	@iPackageUnitID,
	@iBrandID,
	@iCategoryID,   
	@sMnemonicCode,
	@fPricingType,
--	@iIsServiceType,
	@fPriceVIP,
	@fPriceWholesale,
--	@fRatioGrossMargin,  
	@fsCanChangePrice,
	@sRuleOfPoint,
	@sPicture,    
	@sShelfLife,       
	@sReturnDays,     
	@fPurchaseFlag,         
--	@sRefCommodityID,
	@sRefCommodityMultiple, 
--	@sIsGift,
	@sTag,
--	@iType,
	@iINT2,
	@sStartValueRemark,
	@sPropertyValue1,
	@sPropertyValue2,
	@sPropertyValue3,
	@sPropertyValue4,
	@iShopID
	);

SELECT 1 FROM t_commodity 
	WHERE F_ID = @iID
	AND F_Name = @sName
	AND F_ShortName = @sShortName
	AND F_Specification = @sSpecification
	AND F_PackageUnitID = @iPackageUnitID
	AND F_BrandID = @iBrandID
	AND F_CategoryID = @iCategoryID   
	AND F_MnemonicCode = @sMnemonicCode
	AND F_PricingType = @fPricingType
--	AND F_IsServiceType = @iIsServiceType
	AND F_PriceVIP = @fPriceVIP
	AND F_PriceWholesale = @fPriceWholesale
--	AND F_RatioGrossMargin = @fRatioGrossMargin
	AND F_CanChangePrice = @fsCanChangePrice
	AND F_RuleOfPoint = @sRuleOfPoint
	AND F_Picture = @sPicture   
	AND F_ShelfLife = @sShelfLife      
	AND F_ReturnDays = @sReturnDays     
	AND F_PurchaseFlag = @fPurchaseFlag         
--	AND F_RefCommodityID = @sRefCommodityID
	AND F_RefCommodityMultiple = @sRefCommodityMultiple
--	AND F_IsGift = @sIsGift
	AND F_Tag = @sTag
--	AND F_Type = @iType
	AND F_StartValueRemark = @sStartValueRemark 
	AND F_PropertyValue1 = @sPropertyValue1
	AND F_PropertyValue2 = @sPropertyValue2
	AND F_PropertyValue3 = @sPropertyValue3
	AND F_PropertyValue4 = @sPropertyValue4;
	
SELECT @iErrorCode;
SELECT @sErrorMsg;	
SELECT IF(found_rows() = 1 AND @iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'CASE1 Testing Result';
DELETE FROM t_commodityhistory WHERE F_CommodityID = @insertId;
DELETE FROM t_commodity WHERE F_ID = @insertId;

SELECT '-------------------- Case2:F_Name�޸�Ϊ���ݿⲻ���ڵ�����  û�޸��������� -------------------------' AS 'Case2'; 
	
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������2��','����','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,30,now(),'20',0,0,'.................', 0);
SET @insertId = last_insert_id();
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();		 
SET @sName = '������3��';		          
SET @sShortName = '����';     
SET @sSpecification = '��';   
SET @iPackageUnitID = 1;     
SET @iBrandID = 3;        
SET @iCategoryID = 1;             
SET @sMnemonicCode = 'SP';    
SET @fPricingType = 1;     
-- SET @iIsServiceType = 1;
SET @fPriceVIP = 11.8;    
SET @fPriceWholesale = 0.363986;         
-- SET @fRatioGrossMargin = 0.1;     
SET @fsCanChangePrice = 0;  
SET @sRuleOfPoint = 1;    
SET @sPicture = '/p/private_db/nbr/1.jpg';   
SET @sShelfLife = 3;       
SET @sReturnDays = 30;        
SET @fPurchaseFlag = 0;         
-- SET @sRefCommodityID = 1;
SET @sRefCommodityMultiple = 1; 
-- SET @sIsGift = 0;  
SET @sTag = '111';
-- SET @iType = 0;
SET @iINT2 = 3;
SET @sStartValueRemark = '';
SET @sPropertyValue1 = 1;
SET @sPropertyValue2 = 1;
SET @sPropertyValue3 = 1;
SET @sPropertyValue4 = 1;
SET @iShopID = 2;

call SP_Commodity_Update(
	@iErrorCode,
	@sErrorMsg,
	@iID,
	@sName,
	@sShortName,
	@sSpecification,
	@iPackageUnitID,
	@iBrandID,
	@iCategoryID,   
	@sMnemonicCode,
	@fPricingType,
--	@iIsServiceType,
	@fPriceVIP,
	@fPriceWholesale,
--	@fRatioGrossMargin,  
	@fsCanChangePrice,
	@sRuleOfPoint,
	@sPicture,    
	@sShelfLife,       
	@sReturnDays,     
	@fPurchaseFlag,         
--	@sRefCommodityID,
	@sRefCommodityMultiple, 
--	@sIsGift,
	@sTag,
--	@iType,
	@iINT2,
	@sStartValueRemark,
	@sPropertyValue1,
	@sPropertyValue2,
	@sPropertyValue3,
	@sPropertyValue4,
	@iShopID
	);

SELECT 1 FROM t_commodity 
	WHERE F_ID = @iID
	AND F_Name = @sName
	AND F_ShortName = @sShortName
	AND F_Specification = @sSpecification
	AND F_PackageUnitID = @iPackageUnitID
	AND F_BrandID = @iBrandID
	AND F_CategoryID = @iCategoryID   
	AND F_MnemonicCode = @sMnemonicCode
	AND F_PricingType = @fPricingType
--	AND F_IsServiceType = @iIsServiceType
	AND F_PriceVIP = @fPriceVIP
	AND F_PriceWholesale = @fPriceWholesale
--	AND F_RatioGrossMargin = @fRatioGrossMargin
	AND F_CanChangePrice = @fsCanChangePrice
	AND F_RuleOfPoint = @sRuleOfPoint
	AND F_Picture = @sPicture   
	AND F_ShelfLife = @sShelfLife      
	AND F_ReturnDays = @sReturnDays     
	AND F_PurchaseFlag = @fPurchaseFlag         
--	AND F_RefCommodityID = @sRefCommodityID
	AND F_RefCommodityMultiple = @sRefCommodityMultiple
--	AND F_IsGift = @sIsGift
	AND F_Tag = @sTag
--	AND F_Type = @iType
	AND F_StartValueRemark = @sStartValueRemark 
	AND F_PropertyValue1 = @sPropertyValue1
	AND F_PropertyValue2 = @sPropertyValue2
	AND F_PropertyValue3 = @sPropertyValue3
	AND F_PropertyValue4 = @sPropertyValue4;
	
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'CASE2 Testing Result';
DELETE FROM t_commodityhistory WHERE F_CommodityID = @insertId;
DELETE FROM t_commodity WHERE F_ID = @insertId;

SELECT '-------------------- Case3:F_Name�޸�Ϊ���ݿⲻ���ڵ�����  �޸����������� -------------------------' AS 'Case3'; 
	
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������4��','����','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,30,now(),'20',0,0,'.................', 0);
SET @insertId = last_insert_id();
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();		 
SET @sName = '������5��';		          
SET @sShortName = '����';     
SET @sSpecification = '��';   
SET @iPackageUnitID = 1;     
SET @iBrandID = 3;        
SET @iCategoryID = 1;             
SET @sMnemonicCode = 'SP';    
SET @fPricingType = 1;     
-- SET @iIsServiceType = 1;
SET @fPriceVIP = 11.8;    
SET @fPriceWholesale = 0.363986;         
-- SET @fRatioGrossMargin = 0.1;     
SET @fsCanChangePrice = 0;  
SET @sRuleOfPoint = 1;    
SET @sPicture = '/p/private_db/nbr/1.jpg';    
SET @sShelfLife = 3;       
SET @sReturnDays = 30;        
SET @fPurchaseFlag = 0;         
-- SET @sRefCommodityID = 1;
SET @sRefCommodityMultiple = 1; 
-- SET @sIsGift = 0;  
SET @sTag = '111';
-- SET @iType = 0;
SET @iINT2 = 3;
SET @sStartValueRemark = '';
SET @sPropertyValue1 = 1;
SET @sPropertyValue2 = 1;
SET @sPropertyValue3 = 1;
SET @sPropertyValue4 = 1;
SET @iShopID = 2;

call SP_Commodity_Update(
	@iErrorCode,
	@sErrorMsg,
	@iID,
	@sName,
	@sShortName,
	@sSpecification,
	@iPackageUnitID,
	@iBrandID,
	@iCategoryID,   
	@sMnemonicCode,
	@fPricingType,
--	@iIsServiceType,
	@fPriceVIP,
	@fPriceWholesale,
--	@fRatioGrossMargin,  
	@fsCanChangePrice,
	@sRuleOfPoint,
	@sPicture,    
	@sShelfLife,       
	@sReturnDays,     
	@fPurchaseFlag,         
--	@sRefCommodityID,
	@sRefCommodityMultiple, 
--	@sIsGift,
	@sTag,
--	@iType,
	@iINT2,
	@sStartValueRemark,
	@sPropertyValue1,
	@sPropertyValue2,
	@sPropertyValue3,
	@sPropertyValue4,
	@iShopID
	);

SELECT 1 FROM t_commodity 
	WHERE F_ID = @iID
	AND F_Name = @sName
	AND F_ShortName = @sShortName
	AND F_Specification = @sSpecification
	AND F_PackageUnitID = @iPackageUnitID
	AND F_BrandID = @iBrandID
	AND F_CategoryID = @iCategoryID   
	AND F_MnemonicCode = @sMnemonicCode
	AND F_PricingType = @fPricingType
 --	AND F_IsServiceType = @iIsServiceType
	AND F_PriceVIP = @fPriceVIP
	AND F_PriceWholesale = @fPriceWholesale
--	AND F_RatioGrossMargin = @fRatioGrossMargin
	AND F_CanChangePrice = @fsCanChangePrice
	AND F_RuleOfPoint = @sRuleOfPoint
	AND F_Picture = @sPicture   
	AND F_ShelfLife = @sShelfLife      
	AND F_ReturnDays = @sReturnDays     
	AND F_PurchaseFlag = @fPurchaseFlag         
--	AND F_RefCommodityID = @sRefCommodityID
	AND F_RefCommodityMultiple = @sRefCommodityMultiple
--	AND F_IsGift = @sIsGift
	AND F_Tag = @sTag
--	AND F_Type = @iType
	AND F_StartValueRemark = @sStartValueRemark 
	AND F_PropertyValue1 = @sPropertyValue1
	AND F_PropertyValue2 = @sPropertyValue2
	AND F_PropertyValue3 = @sPropertyValue3
	AND F_PropertyValue4 = @sPropertyValue4;
	
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'CASE3 Testing Result';
DELETE FROM t_commodityhistory WHERE F_CommodityID = @insertId;
DELETE FROM t_commodity WHERE F_ID = @insertId;

SELECT '-------------------- Case4:F_Name�޸�Ϊ���ݿ���ڵ�����  ������Ϊ���޸�ʧ��-------------------------' AS 'Case4'; 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������6��','����','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,30,now(),'20',0,0,'.................', 0);
SET @insertId1 = last_insert_id();
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������7��','����','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,30,now(),'20',0,0,'.................', 0);
SET @insertId2 = last_insert_id();
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();		 
SET @sName = '������6��';		          
SET @sShortName = '����';     
SET @sSpecification = '��';   
SET @iPackageUnitID = 1;     
SET @iBrandID = 3;        
SET @iCategoryID = 1;             
SET @sMnemonicCode = 'SP';    
SET @fPricingType = 1;     
-- SET @iIsServiceType = 1;
SET @fPriceVIP = 11.8;    
SET @fPriceWholesale = 0.363986;         
-- SET @fRatioGrossMargin = 0.1;     
SET @fsCanChangePrice = 0;  
SET @sRuleOfPoint = 1;    
SET @sPicture = '/p/private_db/nbr/1.jpg';     
SET @sShelfLife = 3;       
SET @sReturnDays = 30;        
SET @fPurchaseFlag = 0;         
-- SET @sRefCommodityID = 1;
SET @sRefCommodityMultiple = 1; 
-- SET @sIsGift = 0;  
SET @sTag = '111';
-- SET @iType = 0;
SET @iINT2 = 3;
SET @sStartValueRemark = '';
SET @sPropertyValue1 = 1;
SET @sPropertyValue2 = 1;
SET @sPropertyValue3 = 1;
SET @sPropertyValue4 = 1;
SET @iShopID = 2;

call SP_Commodity_Update(
	@iErrorCode,
	@sErrorMsg,
	@iID,
	@sName,
	@sShortName,
	@sSpecification,
	@iPackageUnitID,
	@iBrandID,
	@iCategoryID,   
	@sMnemonicCode,
	@fPricingType,
--	@iIsServiceType,
	@fPriceVIP,
	@fPriceWholesale,
--	@fRatioGrossMargin,  
	@fsCanChangePrice,
	@sRuleOfPoint,
	@sPicture,    
	@sShelfLife,       
	@sReturnDays,     
	@fPurchaseFlag,         
--	@sRefCommodityID,
	@sRefCommodityMultiple, 
--	@sIsGift,
	@sTag,
--	@iType,
	@iINT2,
	@sStartValueRemark,
	@sPropertyValue1,
	@sPropertyValue2,
	@sPropertyValue3,
	@sPropertyValue4,
	@iShopID
	);

SELECT 1 FROM t_commodity 
	WHERE F_ID = @iID
	AND F_Name = @sName
	AND F_ShortName = @sShortName
	AND F_Specification = @sSpecification
	AND F_PackageUnitID = @iPackageUnitID
	AND F_BrandID = @iBrandID
	AND F_CategoryID = @iCategoryID   
	AND F_MnemonicCode = @sMnemonicCode
	AND F_PricingType = @fPricingType
 --	AND F_IsServiceType = @iIsServiceType
	AND F_PriceVIP = @fPriceVIP
	AND F_PriceWholesale = @fPriceWholesale
--	AND F_RatioGrossMargin = @fRatioGrossMargin
	AND F_CanChangePrice = @fsCanChangePrice
	AND F_RuleOfPoint = @sRuleOfPoint
	AND F_Picture = @sPicture   
	AND F_ShelfLife = @sShelfLife      
	AND F_ReturnDays = @sReturnDays     
	AND F_PurchaseFlag = @fPurchaseFlag         
--	AND F_RefCommodityID = @sRefCommodityID
	AND F_RefCommodityMultiple = @sRefCommodityMultiple
--	AND F_IsGift = @sIsGift
	AND F_Tag = @sTag
--	AND F_Type = @iType
	AND F_StartValueRemark = @sStartValueRemark 
	AND F_PropertyValue1 = @sPropertyValue1
	AND F_PropertyValue2 = @sPropertyValue2
	AND F_PropertyValue3 = @sPropertyValue3
	AND F_PropertyValue4 = @sPropertyValue4;
	
SELECT @sErrorMsg;
SELECT @iErrorCode;	
SELECT IF(@iErrorCode = 1 AND @sErrorMsg = '����Ʒ�����Ѵ���', '���Գɹ�', '����ʧ��') AS 'CASE4 Testing Result';
DELETE FROM t_commodity WHERE F_ID = @insertId1;
DELETE FROM t_commodity WHERE F_ID = @insertId2;


SELECT '-------------------- Case5:��ɾ������Ʒ�����²��� -------------------------' AS 'Case5'; 

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 49;		 
SET @sName = '�ɱȿ���Ƭ(����)';		          
SET @sShortName = '����1';     
SET @sSpecification = '����1';   
SET @iPackageUnitID = 2;     
SET @iBrandID = 2;        
SET @iCategoryID = 2;             
SET @sMnemonicCode = 'KBKSP';    
SET @fPricingType = 2;     
-- SET @iIsServiceType = 2;
SET @fPriceVIP = 11.9;    
SET @fPriceWholesale = 0.363386;         
-- SET @fRatioGrossMargin = 0.2;     
SET @fsCanChangePrice = 0;  
SET @sRuleOfPoint = 2;    
SET @sPicture = '/p/private_db/nbr/1.jpg';   
SET @sShelfLife = 2;       
SET @sReturnDays = 60;        
SET @fPurchaseFlag = 0;         
-- SET @sRefCommodityID = 6;
SET @sRefCommodityMultiple = 7; 
-- SET @sIsGift = 0;  
SET @sTag = '11112313';
-- SET @iType = 0;
SET @iINT2 = 3;
SET @sStartValueRemark = '';
SET @iShopID = 2;

call SP_Commodity_Update(
	@iErrorCode,
	@sErrorMsg,
	@iID,
	@sName,
	@sShortName,
	@sSpecification,
	@iPackageUnitID,
	@iBrandID,
	@iCategoryID,   
	@sMnemonicCode,
	@fPricingType,
--	@iIsServiceType,
	@fPriceVIP,
	@fPriceWholesale,
--	@fRatioGrossMargin,  
	@fsCanChangePrice,
	@sRuleOfPoint,
	@sPicture,    
	@sShelfLife,       
	@sReturnDays,     
	@fPurchaseFlag,         
--	@sRefCommodityID,
	@sRefCommodityMultiple, 
--	@sIsGift,
	@sTag,
--	@iType,
	@iINT2,
	@sStartValueRemark,
	@sPropertyValue1,
	@sPropertyValue2,
	@sPropertyValue3,
	@sPropertyValue4,
	@iShopID
	);
	SELECT @iErrorCode;
	
SELECT 1 FROM t_commodity 
	WHERE F_ID = @iID
	AND F_Name = @sName
	AND F_ShortName = @sShortName
	AND F_Specification = @sSpecification
	AND F_PackageUnitID = @iPackageUnitID
	AND F_BrandID = @iBrandID
	AND F_CategoryID = @iCategoryID   
	AND F_MnemonicCode = @sMnemonicCode
	AND F_PricingType = @fPricingType
--	AND F_IsServiceType = @iIsServiceType
	AND F_PriceVIP = @fPriceVIP
	AND F_PriceWholesale = @fPriceWholesale
--	AND F_RatioGrossMargin = @fRatioGrossMargin
	AND F_CanChangePrice = @fsCanChangePrice
	AND F_RuleOfPoint = @sRuleOfPoint
	AND F_Picture = @sPicture   
	AND F_ShelfLife = @sShelfLife      
	AND F_ReturnDays = @sReturnDays     
	AND F_PurchaseFlag = @fPurchaseFlag         
--	AND F_RefCommodityID = @sRefCommodityID
	AND F_RefCommodityMultiple = @sRefCommodityMultiple
--	AND F_IsGift = @sIsGift
	AND F_Tag = @sTag
--	AND F_Type = @iType	
	AND F_StartValueRemark = @sStartValueRemark 
	AND F_PropertyValue1 = @sPropertyValue1
	AND F_PropertyValue2 = @sPropertyValue2
	AND F_PropertyValue3 = @sPropertyValue3
	AND F_PropertyValue4 = @sPropertyValue4;
	
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 2 AND @sErrorMsg = '��ɾ��״̬��������Ч', '���Գɹ�', '����ʧ��') AS 'CASE5 Testing Result';

SELECT '-------------------- Case6:�ò����ڵ�BrandID�޸���Ʒ-------------------------' AS 'Case6'; 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 2;		 
SET @sName = '������2��';		          
SET @sShortName = '����2';     
SET @sSpecification = '����';   
SET @iPackageUnitID = 1;     
SET @iBrandID = -999;        
SET @iCategoryID = 1;             
SET @sMnemonicCode = 'BS';    
SET @fPricingType = 1;     
-- SET @iIsServiceType = 1;
SET @fPriceVIP = 11.8;    
SET @fPriceWholesale = 0.363986;         
-- SET @fRatioGrossMargin = 0.1;     
SET @fsCanChangePrice = 0;  
SET @sRuleOfPoint = 1;    
SET @sPicture = '/p/private_db/nbr/1.jpg';   
SET @sShelfLife = 3;       
SET @sReturnDays = 30;        
SET @fPurchaseFlag = 0;         
-- SET @sRefCommodityID = 1;
SET @sRefCommodityMultiple = 1; 
-- SET @sIsGift = 0;  
SET @sTag = '111';
-- SET @iType = 0;
SET @iINT2 = 3;
SET @sStartValueRemark = '';
SET @sPropertyValue1 = 1;
SET @sPropertyValue2 = 1;
SET @sPropertyValue3 = 1;
SET @sPropertyValue4 = 1;
SET @iShopID = 2;

call SP_Commodity_Update(
	@iErrorCode,
	@sErrorMsg,
	@iID,
	@sName,
	@sShortName,
	@sSpecification,
	@iPackageUnitID,
	@iBrandID,
	@iCategoryID,   
	@sMnemonicCode,
	@fPricingType,
--	@iIsServiceType,
	@fPriceVIP,
	@fPriceWholesale,
--	@fRatioGrossMargin,  
	@fsCanChangePrice,
	@sRuleOfPoint,
	@sPicture,    
	@sShelfLife,       
	@sReturnDays,     
	@fPurchaseFlag,         
--	@sRefCommodityID,
	@sRefCommodityMultiple, 
--	@sIsGift,
	@sTag,
--	@iType,
	@iINT2,
	@sStartValueRemark,
	@sPropertyValue1,
	@sPropertyValue2,
	@sPropertyValue3,
	@sPropertyValue4,
	@iShopID
	);
	
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '�����ò����ڵ�BrandID�޸���Ʒ', '���Գɹ�', '����ʧ��') AS 'CASE6 Testing Result';

SELECT '-------------------- Case7:�ò����ڵ�CategoryID�޸���Ʒ-------------------------' AS 'Case7'; 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 2;		 
SET @sName = '������2��';		          
SET @sShortName = '����2';     
SET @sSpecification = '����';   
SET @iPackageUnitID = 1;     
SET @iBrandID = 1;        
SET @iCategoryID = -99;             
SET @sMnemonicCode = 'BS';    
SET @fPricingType = 1;     
-- SET @iIsServiceType = 1;
SET @fPriceVIP = 11.8;    
SET @fPriceWholesale = 0.363986;         
-- SET @fRatioGrossMargin = 0.1;     
SET @fsCanChangePrice = 0;  
SET @sRuleOfPoint = 1;    
SET @sPicture = '/p/private_db/nbr/1.jpg';    
SET @sShelfLife = 3;       
SET @sReturnDays = 30;        
SET @fPurchaseFlag = 0;         
-- SET @sRefCommodityID = 1;
SET @sRefCommodityMultiple = 1; 
-- SET @sIsGift = 0;  
SET @sTag = '111';
-- SET @iType = 0;
SET @iINT2 = 3;
SET @sStartValueRemark = '';
SET @sPropertyValue1 = 1;
SET @sPropertyValue2 = 1;
SET @sPropertyValue3 = 1;
SET @sPropertyValue4 = 1;
SET @iShopID = 2;

call SP_Commodity_Update(
	@iErrorCode,
	@sErrorMsg,
	@iID,
	@sName,
	@sShortName,
	@sSpecification,
	@iPackageUnitID,
	@iBrandID,
	@iCategoryID,   
	@sMnemonicCode,
	@fPricingType,
--	@iIsServiceType,
	@fPriceVIP,
	@fPriceWholesale,
--	@fRatioGrossMargin,  
	@fsCanChangePrice,
	@sRuleOfPoint,
	@sPicture,    
	@sShelfLife,       
	@sReturnDays,     
	@fPurchaseFlag,         
--	@sRefCommodityID,
	@sRefCommodityMultiple, 
--	@sIsGift,
	@sTag,
--	@iType,
	@iINT2,
	@sStartValueRemark,
	@sPropertyValue1,
	@sPropertyValue2,
	@sPropertyValue3,
	@sPropertyValue4,
	@iShopID
	);
	
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '�����ò����ڵ�CategoryID�޸���Ʒ', '���Գɹ�', '����ʧ��') AS 'CASE7 Testing Result';

SELECT '-------------------- Case8:�ò����ڵ�PackageUnitID�޸���Ʒ-------------------------' AS 'Case8'; 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 2;		 
SET @sName = '������2��';		          
SET @sShortName = '����2';     
SET @sSpecification = '����';   
SET @iPackageUnitID = -99;     
SET @iBrandID = 1;        
SET @iCategoryID = 1;             
SET @sMnemonicCode = 'BS';    
SET @fPricingType = 1;     
-- SET @iIsServiceType = 1;
SET @fPriceVIP = 11.8;    
SET @fPriceWholesale = 0.363986;         
-- SET @fRatioGrossMargin = 0.1;     
SET @fsCanChangePrice = 0;  
SET @sRuleOfPoint = 1;    
SET @sPicture = '/p/private_db/nbr/1.jpg';    
SET @sShelfLife = 3;       
SET @sReturnDays = 30;        
SET @fPurchaseFlag = 0;         
-- SET @sRefCommodityID = 1;
SET @sRefCommodityMultiple = 1; 
-- SET @sIsGift = 0;  
SET @sTag = '111';
-- SET @iType = 0;
SET @iINT2 = 3;
SET @sStartValueRemark = '';
SET @sPropertyValue1 = 1;
SET @sPropertyValue2 = 1;
SET @sPropertyValue3 = 1;
SET @sPropertyValue4 = 1;
SET @iShopID = 2;

call SP_Commodity_Update(
	@iErrorCode,
	@sErrorMsg,
	@iID,
	@sName,
	@sShortName,
	@sSpecification,
	@iPackageUnitID,
	@iBrandID,
	@iCategoryID,   
	@sMnemonicCode,
	@fPricingType,
--	@iIsServiceType,
	@fPriceVIP,
	@fPriceWholesale,
--	@fRatioGrossMargin,  
	@fsCanChangePrice,
	@sRuleOfPoint,
	@sPicture,    
	@sShelfLife,       
	@sReturnDays,     
	@fPurchaseFlag,         
--	@sRefCommodityID,
	@sRefCommodityMultiple, 
--	@sIsGift,
	@sTag,
--	@iType,
	@iINT2,
	@sStartValueRemark,
	@sPropertyValue1,
	@sPropertyValue2,
	@sPropertyValue3,
	@sPropertyValue4,
	@iShopID
	);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '�����ò����ڵ�PackageUnitID�޸���Ʒ', '���Գɹ�', '����ʧ��') AS 'CASE8 Testing Result';

SELECT '-------------------- Case9:�����˲�����-------------------------' AS 'Case1'; 
	
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������1��','����','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,30,now(),'20',0,0,'.................', 0);
SET @insertId = last_insert_id();
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();		 
SET @sName = '������2��';		          
SET @sShortName = '����2';     
SET @sSpecification = '����';   
SET @iPackageUnitID = 2;     
SET @iBrandID = 3;        
SET @iCategoryID = 4;             
SET @sMnemonicCode = 'BS';    
SET @fPricingType = 1;     
-- SET @iIsServiceType = 1;
SET @fPriceVIP = 11.8;    
SET @fPriceWholesale = 0.363986;         
-- SET @fRatioGrossMargin = 0.1;     
SET @fsCanChangePrice = 0;  
SET @sRuleOfPoint = 1;    
SET @sPicture = '/p/private_db/nbr/1.jpg';   
SET @sShelfLife = 3;       
SET @sReturnDays = 30;        
SET @fPurchaseFlag = 0;         
-- SET @sRefCommodityID = 1;
SET @sRefCommodityMultiple = 1; 
-- SET @sIsGift = 0;  
SET @sTag = '111';
-- SET @iType = 0;
SET @iINT2 = -5;
SET @sStartValueRemark = '';
SET @sPropertyValue1 = 1;
SET @sPropertyValue2 = 1;
SET @sPropertyValue3 = 1;
SET @sPropertyValue4 = 1;
SET @ISHOPID = 2;

call SP_Commodity_Update(
	@iErrorCode,
	@sErrorMsg,
	@iID,
	@sName,
	@sShortName,
	@sSpecification,
	@iPackageUnitID,
	@iBrandID,
	@iCategoryID,   
	@sMnemonicCode,
	@fPricingType,
--	@iIsServiceType,
	@fPriceVIP,
	@fPriceWholesale,
--	@fRatioGrossMargin,  
	@fsCanChangePrice,
	@sRuleOfPoint,
	@sPicture,    
	@sShelfLife,       
	@sReturnDays,     
	@fPurchaseFlag,         
--	@sRefCommodityID,
	@sRefCommodityMultiple, 
--	@sIsGift,
	@sTag,
--	@iType,
	@iINT2,
	@sStartValueRemark,
	@sPropertyValue1,
	@sPropertyValue2,
	@sPropertyValue3,
	@sPropertyValue4,
	@iShopID
	);
	
SELECT @iErrorCode;
SELECT @sErrorMsg;	
SELECT IF(@iErrorCode = 4 AND @sErrorMsg = '���û������ڣ��ڿ���Ϊ', '���Գɹ�', '����ʧ��') AS 'CASE9 Testing Result';
DELETE FROM t_commodityhistory WHERE F_CommodityID = @insertId;
DELETE FROM t_commodity WHERE F_ID = @insertId;

SELECT '-------------------- Case10:�޸���Ʒ����Ϊ��ɾ������Ʒ���֣��޸ĳɹ�-------------------------' AS 'Case10'; 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (2,'֩��10��','����','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,30,now(),'20',0,0,'.................', 0);
SET @insertId1 = last_insert_id();


INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������11��','����','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,30,now(),'20',0,0,'.................', 0);
SET @insertId2 = last_insert_id();
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();		 
SET @sName = '֩��10��';		          
SET @sShortName = '����';     
SET @sSpecification = '��';   
SET @iPackageUnitID = 1;     
SET @iBrandID = 3;        
SET @iCategoryID = 1;             
SET @sMnemonicCode = 'SP';    
SET @fPricingType = 1;     
-- SET @iIsServiceType = 1;
SET @fPriceVIP = 11.8;    
SET @fPriceWholesale = 0.363986;         
-- SET @fRatioGrossMargin = 0.1;     
SET @fsCanChangePrice = 0;  
SET @sRuleOfPoint = 1;    
SET @sPicture = '/p/private_db/nbr/1.jpg';     
SET @sShelfLife = 3;       
SET @sReturnDays = 30;        
SET @fPurchaseFlag = 0;         
-- SET @sRefCommodityID = 1;
SET @sRefCommodityMultiple = 1; 
-- SET @sIsGift = 0;  
SET @sTag = '111';
-- SET @iType = 0;
SET @iINT2 = 3;
SET @sStartValueRemark = '';
SET @sPropertyValue1 = 1;
SET @sPropertyValue2 = 1;
SET @sPropertyValue3 = 1;
SET @sPropertyValue4 = 1;
SET @iShopID = 2;

call SP_Commodity_Update(
	@iErrorCode,
	@sErrorMsg,
	@iID,
	@sName,
	@sShortName,
	@sSpecification,
	@iPackageUnitID,
	@iBrandID,
	@iCategoryID,   
	@sMnemonicCode,
	@fPricingType,
 --	@iIsServiceType,
	@fPriceVIP,
	@fPriceWholesale,
--	@fRatioGrossMargin,  
	@fsCanChangePrice,
	@sRuleOfPoint,
	@sPicture,    
	@sShelfLife,       
	@sReturnDays,     
	@fPurchaseFlag,         
--	@sRefCommodityID,
	@sRefCommodityMultiple, 
--	@sIsGift,
	@sTag,
--	@iType,
	@iINT2,
	@sStartValueRemark,
	@sPropertyValue1,
	@sPropertyValue2,
	@sPropertyValue3,
	@sPropertyValue4,
	@iShopID
	);

SELECT 1 FROM t_commodity 
	WHERE F_ID = @iID
	AND F_Name = @sName
	AND F_ShortName = @sShortName
	AND F_Specification = @sSpecification
	AND F_PackageUnitID = @iPackageUnitID
	AND F_BrandID = @iBrandID
	AND F_CategoryID = @iCategoryID   
	AND F_MnemonicCode = @sMnemonicCode
	AND F_PricingType = @fPricingType
--	AND F_IsServiceType = @iIsServiceType
	AND F_PriceVIP = @fPriceVIP
	AND F_PriceWholesale = @fPriceWholesale
--	AND F_RatioGrossMargin = @fRatioGrossMargin
	AND F_CanChangePrice = @fsCanChangePrice
	AND F_RuleOfPoint = @sRuleOfPoint
	AND F_Picture = @sPicture   
	AND F_ShelfLife = @sShelfLife      
	AND F_ReturnDays = @sReturnDays     
	AND F_PurchaseFlag = @fPurchaseFlag         
--	AND F_RefCommodityID = @sRefCommodityID
	AND F_RefCommodityMultiple = @sRefCommodityMultiple
--	AND F_IsGift = @sIsGift
	AND F_Tag = @sTag
--	AND F_Type = @iType
	AND F_StartValueRemark = @sStartValueRemark 
	AND F_PropertyValue1 = @sPropertyValue1
	AND F_PropertyValue2 = @sPropertyValue2
	AND F_PropertyValue3 = @sPropertyValue3
	AND F_PropertyValue4 = @sPropertyValue4;
	
SELECT @sErrorMsg;
SELECT @iErrorCode;	
SELECT IF(@iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'CASE10 Testing Result';
DELETE FROM t_commodity WHERE F_ID = @insertId1;
DELETE FROM t_commodityhistory WHERE F_CommodityID = @insertId2;
DELETE FROM t_commodity WHERE F_ID = @insertId2;

SELECT '-------------------- Case11:�����F_PictureΪ :,Update F_PictureΪNULL-------------------------' AS 'Case11'; 
	
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������1��','����','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,30,now(),'20',0,0,'.................', 0);
SET @insertId = last_insert_id();
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();		 
SET @sName = '������2��';		          
SET @sShortName = '����2';     
SET @sSpecification = '����';   
SET @iPackageUnitID = 2;     
SET @iBrandID = 3;        
SET @iCategoryID = 4;             
SET @sMnemonicCode = 'BS';    
SET @fPricingType = 1;     
-- SET @iIsServiceType = 1;
SET @fPriceVIP = 11.8;    
SET @fPriceWholesale = 0.363986;         
-- SET @fRatioGrossMargin = 0.1;     
SET @fsCanChangePrice = 0;  
SET @sRuleOfPoint = 1;    
SET @sPicture = ':';  
SET @sShelfLife = 3;       
SET @sReturnDays = 30;        
SET @fPurchaseFlag = 0;         
-- SET @sRefCommodityID = 1;
SET @sRefCommodityMultiple = 1; 
-- SET @sIsGift = 0;  
SET @sTag = '111';
-- SET @iType = 0;
SET @iINT2 = 3;
SET @sStartValueRemark = '';
SET @sPropertyValue1 = 1;
SET @sPropertyValue2 = 1;
SET @sPropertyValue3 = 1;
SET @sPropertyValue4 = 1;
SET @iShopID = 2;

call SP_Commodity_Update(
	@iErrorCode,
	@sErrorMsg,
	@iID,
	@sName,
	@sShortName,
	@sSpecification,
	@iPackageUnitID,
	@iBrandID,
	@iCategoryID,   
	@sMnemonicCode,
	@fPricingType,
 --	@iIsServiceType,
	@fPriceVIP,
	@fPriceWholesale,
--	@fRatioGrossMargin,  
	@fsCanChangePrice,
	@sRuleOfPoint,
	@sPicture,    
	@sShelfLife,       
	@sReturnDays,     
	@fPurchaseFlag,         
--	@sRefCommodityID,
	@sRefCommodityMultiple, 
--	@sIsGift,
	@sTag,
--	@iType,
	@iINT2,
	@sStartValueRemark,
	@sPropertyValue1,
	@sPropertyValue2,
	@sPropertyValue3,
	@sPropertyValue4,
	@iShopID
	);

SELECT 1 FROM t_commodity 
	WHERE F_ID = @iID
	AND F_Name = @sName
	AND F_ShortName = @sShortName
	AND F_Specification = @sSpecification
	AND F_PackageUnitID = @iPackageUnitID
	AND F_BrandID = @iBrandID
	AND F_CategoryID = @iCategoryID   
	AND F_MnemonicCode = @sMnemonicCode
	AND F_PricingType = @fPricingType
 --	AND F_IsServiceType = @iIsServiceType
	AND F_PriceVIP = @fPriceVIP
	AND F_PriceWholesale = @fPriceWholesale
--	AND F_RatioGrossMargin = @fRatioGrossMargin
	AND F_CanChangePrice = @fsCanChangePrice
	AND F_RuleOfPoint = @sRuleOfPoint
	AND F_Picture = NULL   
	AND F_ShelfLife = @sShelfLife      
	AND F_ReturnDays = @sReturnDays     
	AND F_PurchaseFlag = @fPurchaseFlag         
--	AND F_RefCommodityID = @sRefCommodityID
	AND F_RefCommodityMultiple = @sRefCommodityMultiple
--	AND F_IsGift = @sIsGift
	AND F_Tag = @sTag
--	AND F_Type = @iType
	AND F_StartValueRemark = @sStartValueRemark 
	AND F_PropertyValue1 = @sPropertyValue1
	AND F_PropertyValue2 = @sPropertyValue2
	AND F_PropertyValue3 = @sPropertyValue3
	AND F_PropertyValue4 = @sPropertyValue4;
	
SELECT @iErrorCode;
SELECT @sErrorMsg;	
SELECT IF(found_rows() = 1 AND @iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'CASE11 Testing Result';
DELETE FROM t_commodityhistory WHERE F_CommodityID = @insertId;
DELETE FROM t_commodity WHERE F_ID = @insertId;


SELECT '-------------------- Case12:��ͨ��Ʒ�޸��ڳ�ֵ��ע(���ȳ���)-------------------------' AS 'Case12'; 
	
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������1��','����','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,30,now(),'20',0,0,'.................', 0);
SET @insertId = last_insert_id();
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();		 
SET @sName = '������2��';		          
SET @sShortName = '����2';     
SET @sSpecification = '����';   
SET @iPackageUnitID = 2;     
SET @iBrandID = 3;        
SET @iCategoryID = 4;             
SET @sMnemonicCode = 'BS';    
SET @fPricingType = 1;     
-- SET @iIsServiceType = 1;
SET @fPriceVIP = 11.8;    
SET @fPriceWholesale = 0.363986;         
-- SET @fRatioGrossMargin = 0.1;     
SET @fsCanChangePrice = 0;  
SET @sRuleOfPoint = 1;    
SET @sPicture = '/p/private_db/nbr/1.jpg';  
SET @sShelfLife = 3;       
SET @sReturnDays = 30;        
SET @fPurchaseFlag = 0;         
-- SET @sRefCommodityID = 1;
SET @sRefCommodityMultiple = 1; 
-- SET @sIsGift = 0;  
SET @sTag = '111';
-- SET @iType = 0;
SET @iINT2 = 3;
SET @sStartValueRemark = '1111111122222222333333334444444455555555666666667777777788888888';
SET @sPropertyValue1 = 1;
SET @sPropertyValue2 = 1;
SET @sPropertyValue3 = 1;
SET @sPropertyValue4 = 1;
SET @iShopID = 2;

call SP_Commodity_Update(
	@iErrorCode,
	@sErrorMsg,
	@iID,
	@sName,
	@sShortName,
	@sSpecification,
	@iPackageUnitID,
	@iBrandID,
	@iCategoryID,   
	@sMnemonicCode,
	@fPricingType,
--	@iIsServiceType,
	@fPriceVIP,
	@fPriceWholesale,
--	@fRatioGrossMargin,  
	@fsCanChangePrice,
	@sRuleOfPoint,
	@sPicture,    
	@sShelfLife,       
	@sReturnDays,     
	@fPurchaseFlag,         
--	@sRefCommodityID,
	@sRefCommodityMultiple, 
--	@sIsGift,
	@sTag,
--	@iType,
	@iINT2,
	@sStartValueRemark,
	@sPropertyValue1,
	@sPropertyValue2,
	@sPropertyValue3,
	@sPropertyValue4,
	@iShopID
	);

SELECT 1 FROM t_commodity 
	WHERE F_StartValueRemark = @sStartValueRemark ;
	
SELECT @iErrorCode;
SELECT @sErrorMsg;	
SELECT IF(found_rows() = 1 AND @iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'CASE12 Testing Result';
DELETE FROM t_commodityhistory WHERE F_CommodityID = @insertId;
DELETE FROM t_commodity WHERE F_ID = @insertId;



SELECT '-------------------- Case13:��ͨ��Ʒ�޸��ڳ�ֵ��ע(�����ַ�)-------------------------' AS 'Case13'; 
	
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������1��','����','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,30,now(),'20',0,0,'.................', 0);
SET @insertId = last_insert_id();
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();		 
SET @sName = '������2��';		          
SET @sShortName = '����2';     
SET @sSpecification = '����';   
SET @iPackageUnitID = 2;     
SET @iBrandID = 3;        
SET @iCategoryID = 4;             
SET @sMnemonicCode = 'BS';    
SET @fPricingType = 1;     
-- SET @iIsServiceType = 1;
SET @fPriceVIP = 11.8;    
SET @fPriceWholesale = 0.363986;         
-- SET @fRatioGrossMargin = 0.1;     
SET @fsCanChangePrice = 0;  
SET @sRuleOfPoint = 1;    
SET @sPicture = '/p/private_db/nbr/1.jpg';  
SET @sShelfLife = 3;       
SET @sReturnDays = 30;        
SET @fPurchaseFlag = 0;         
-- SET @sRefCommodityID = 1;
SET @sRefCommodityMultiple = 1; 
-- SET @sIsGift = 0;  
SET @sTag = '111';
-- SET @iType = 0;
SET @iINT2 = 3;
SET @sStartValueRemark = '!@#$$%@@^*&(%&(^)^(';
SET @sPropertyValue1 = 1;
SET @sPropertyValue2 = 1;
SET @sPropertyValue3 = 1;
SET @sPropertyValue4 = 1;
SET @iShopID = 2;

call SP_Commodity_Update(
	@iErrorCode,
	@sErrorMsg,
	@iID,
	@sName,
	@sShortName,
	@sSpecification,
	@iPackageUnitID,
	@iBrandID,
	@iCategoryID,   
	@sMnemonicCode,
	@fPricingType,
--	@iIsServiceType,
	@fPriceVIP,
	@fPriceWholesale,
--	@fRatioGrossMargin,  
	@fsCanChangePrice,
	@sRuleOfPoint,
	@sPicture,    
	@sShelfLife,       
	@sReturnDays,     
	@fPurchaseFlag,         
--	@sRefCommodityID,
	@sRefCommodityMultiple, 
--	@sIsGift,
	@sTag,
--	@iType,
	@iINT2,
	@sStartValueRemark,
	@sPropertyValue1,
	@sPropertyValue2,
	@sPropertyValue3,
	@sPropertyValue4,
	@iShopID
	);

SELECT 1 FROM t_commodity 
	WHERE F_StartValueRemark = @sStartValueRemark ;
	
SELECT @iErrorCode;
SELECT @sErrorMsg;	
SELECT IF(found_rows() = 1 AND @iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'CASE13 Testing Result';
DELETE FROM t_commodityhistory WHERE F_CommodityID = @insertId;
DELETE FROM t_commodity WHERE F_ID = @insertId;