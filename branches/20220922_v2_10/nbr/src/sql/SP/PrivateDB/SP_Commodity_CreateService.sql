DROP PROCEDURE IF EXISTS `SP_Commodity_CreateService`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Commodity_CreateService`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iStatus INT ,  
	IN sName VARCHAR (32),          
	IN sShortName VARCHAR (32),     
	IN sSpecification VARCHAR (8),   
	IN iPackageUnitID INT,     
	IN sPurchasingUnit VARCHAR (16),        
	IN sBrandID INT,        
	IN sCategoryID INT,             
	IN sMnemonicCode VARCHAR (32),    
	IN sPricingType INT,     
--	IN iLatestPricePurchase Decimal(20,6),
--	IN sPriceRetail Decimal(20,6),     
	IN sPriceVIP Decimal(20,6),    
	IN sPriceWholesale Decimal(20,6),          
	IN sCanChangePrice INT,  
	IN sRuleOfPoint INT,    
	IN sPicture VARCHAR (128),    	
	IN sShelfLife INT,       
	IN sReturnDays INT,     
	IN sPurchaseFlag INT,         
	IN sTag VARCHAR (32),
	IN iStaffID INT, -- int2
	IN sBarcode VARCHAR(64), -- string1
	IN sPropertyValue1 VARCHAR(50),
	IN sPropertyValue2 VARCHAR(50),
	IN sPropertyValue3 VARCHAR(50),
	IN sPropertyValue4 VARCHAR(50),
	IN iSyncSequence INT 
)
BEGIN
	DECLARE iFuncReturnCode INT; -- ���պ����ķ���ֵ�����䲻Ҫ���ؽ�������ϲ�������ŵ������Ľ��������
	DECLARE commID INT;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION 
    BEGIN
	    SET iErrorCode := 3;
	    SET sErrorMsg := '���ݿ����';
  	    ROLLBACK;
    END;
    
    SET iErrorCode = 0; 
	SET sErrorMsg := '';
	
    START TRANSACTION;
	    IF NOT EXISTS (SELECT 1 FROM t_staff WHERE F_ID = iStaffID AND F_Status = 0) THEN
	    	SET iErrorCode := 4;
	    	SET sErrorMsg := 'staffID������';
	    ELSEIF EXISTS (SELECT 1 FROM t_commodity WHERE F_Name = sName AND (F_Status = 0 OR F_Status = 1)) THEN 
	    	SET iErrorCode := 1;
	    	SET sErrorMsg := '�����ظ�';
		ELSEIF NOT EXISTS (SELECT 1 FROM t_brand WHERE F_ID = sBrandID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := 'Ʒ�Ʋ�����';
		ELSEIF NOT EXISTS (SELECT 1 FROM t_category WHERE F_ID = sCategoryID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '���಻����';
		ELSEIF NOT EXISTS (SELECT 1 FROM t_packageunit WHERE F_ID = iPackageUnitID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '��װ��λ������';
		ELSEIF  length(sBarcode) < 7 or length(sBarcode) > 64 THEN 
			SET iErrorCode := 8;
			SET sErrorMsg := '�����볤��ֻ����7��64λ';
	    ELSE 
	    	-- ������Ʒ
		    INSERT INTO t_commodity 
		    (
			    F_Status, 
			    F_Name, 
			    F_ShortName, 
			    F_Specification, 
			    F_PackageUnitID, 
			    F_PurchasingUnit,
			    F_BrandID, 
			    F_CategoryID, 
			    F_MnemonicCode, 
			    F_PricingType, 
--			    F_LatestPricePurchase, 
--			    F_PriceRetail, 
			    F_PriceVIP, 
			    F_PriceWholesale, 
			    F_CanChangePrice, 
			    F_RuleOfPoint, 
			    F_Picture, 
			    F_ShelfLife, 
			    F_ReturnDays, 
			    F_CreateDate, 
			    F_PurchaseFlag,
			    F_Tag, 
--			    F_NO, 
			    F_Type,
			    F_PropertyValue1, 
				F_PropertyValue2, 
				F_PropertyValue3, 
				F_PropertyValue4
		    )
			VALUES (
				iStatus,
				sName,
				sShortName,
				sSpecification,
				iPackageUnitID,
				sPurchasingUnit,
				sBrandID,
				sCategoryID,
				sMnemonicCode,
				sPricingType,
--				iLatestPricePurchase,
--				sPriceRetail,
				sPriceVIP,
				sPriceWholesale,
				sCanChangePrice, 
				sRuleOfPoint, -- ����������Ʒ����ΪNULL
				sPicture,
				sShelfLife,
				sReturnDays,
				now(),
				sPurchaseFlag,
				sTag,
--				0, -- NO
				3,
				sPropertyValue1,
				sPropertyValue2,
				sPropertyValue3,
				sPropertyValue4
			);
			SET commID := last_insert_id();
			-- 
			IF sPicture <> '' THEN 
				SET @sContentType := RIGHT(sPicture,4);
				IF @sContentType = 'jpeg' THEN
				   SET @sContentType := '.jpeg';
				END IF;
				-- ����ƷͼƬ�����Ƹ���ID��������
				update t_commodity set F_Picture = CONCAT(substring_index(sPicture, '/', 4),'',CONCAT('/', commID, @sContentType)) WHERE F_ID = commID;
			END IF;
			
			SELECT 
				F_ID, 
				F_Status, 
				F_Name, 
				F_ShortName, 
				F_Specification, 
				F_PackageUnitID, 
				F_PurchasingUnit, 
				F_BrandID, 
				F_CategoryID, 
				F_MnemonicCode, 
				F_PricingType, 
--				F_LatestPricePurchase, 
--				F_PriceRetail, 
				F_PriceVIP, 
				F_PriceWholesale, 
				F_CanChangePrice, 
				F_RuleOfPoint, 
				F_Picture, 
				F_ShelfLife, 
				F_ReturnDays, 
				F_CreateDate, 
				F_PurchaseFlag, 
				F_RefCommodityID, 
				F_RefCommodityMultiple,  
				F_Tag, 
--				F_NO, 
				F_Type, 
--				F_NOStart, 
--				F_PurchasingPriceStart, 
				F_StartValueRemark, 
				F_CreateDatetime, 
				F_UpdateDatetime, 
				F_PropertyValue1,                                   
				F_PropertyValue2, 
				F_PropertyValue3, 
				F_PropertyValue4
--				F_CurrentWarehousingID
			FROM t_commodity WHERE F_ID = commID;
			
			-- ����������
			INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime) VALUES (commID, sBarcode, now());
			SET @barcodeID = last_insert_id();		
			SELECT F_ID, F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime FROM t_barcodes WHERE F_ID = @barcodeID;
			
			-- �����������ͬ������Ϣ�����ض�����������ҳ��������ֻ����ͬ�����������
			INSERT INTO t_barcodessynccache (F_SyncData_ID, F_SyncType, F_SyncSequence) VALUES (@barcodeID, 'C', iSyncSequence);
	        SELECT F_ID, F_SyncData_ID, F_SyncSequence, F_SyncType,F_CreateDatetime FROM t_barcodessynccache WHERE F_ID = LAST_INSERT_ID();
			
--			-- ������Ʒ�޸���ʷ��
--			SELECT Func_CreateCommodityHistory(
--			commID, 
--			'$', 
--			sBarcode, 
--			'$', 
--			0, 
--			-1, 
--			-100000000, 
--			-100000000, 
--			iStaffID,
--			'') INTO iFuncReturnCode; -- 0
		  END IF;	
 	COMMIT;
END;