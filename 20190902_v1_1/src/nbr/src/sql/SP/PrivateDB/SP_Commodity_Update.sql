DROP PROCEDURE IF EXISTS `SP_Commodity_Update`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Commodity_Update`(	
	OUT iErrorCode INT,	
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,	 
	IN sName VARCHAR (32),		          
	IN sShortName VARCHAR (32),			   		  
	IN sSpecification VARCHAR (8), 		  
	IN iPackageUnitID INT,    		 		   		 		 
	IN iBrandID INT,     		 		  
	IN iCategoryID INT,		   	          
	IN sMnemonicCode VARCHAR (32),  		  
	IN sPricingType INT,   		  
--	IN iIsServiceType INT,  		  		   	  
	IN fPriceVIP Decimal(20,6),			   		 
	IN fPriceWholesale Decimal(20,6),  	     	  
--	IN fRatioGrossMargin Decimal(20,6),	   		  
	IN sCanChangePrice INT, 		 
	IN sRuleOfPoint INT,   		 
	IN sPicture VARCHAR (128),	   		 
	IN sShelfLife INT,     		  
	IN sReturnDays INT,	   		  		   		  
	IN fPurchaseFlag INT,		         
--	IN sRefCommodityID INT,
	IN sRefCommodityMultiple INT, 
--	IN iIsGift INT,	 			 
	IN sTag VARCHAR (32),
--	IN iType INT,
	IN iINT2 INT,
	IN sStartValueRemark VARCHAR(50),
	IN sPropertyValue1 VARCHAR(50),
	IN sPropertyValue2 VARCHAR(50),
	IN sPropertyValue3 VARCHAR(50),
	IN sPropertyValue4 VARCHAR(50),
	IN iShopID INT 
	)
BEGIN	
	DECLARE specification VARCHAR(8);
	DECLARE name VARCHAR(32);
	DECLARE packageUnitID INT;
	DECLARE categoryID INT;
	DECLARE iFuncReturnCode INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;	
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SELECT F_Specification, F_Name, F_PackageUnitID, F_CategoryID INTO specification, name, packageUnitID, categoryID FROM t_commodity WHERE F_ID = iID;
		IF EXISTS (SELECT 1 FROM t_commodity WHERE F_Name = sName AND F_ID <> iID AND (F_Status = 0 OR F_Status = 1)) THEN 
	    	SET iErrorCode := 1;
	    	SET sErrorMsg := '不能修改数据库存在的名称';
	    ELSEIF NOT EXISTS (SELECT 1 FROM t_brand WHERE F_ID = iBrandID) THEN
	    	SET iErrorCode := 7;
			SET sErrorMsg := '不能用不存在的BrandID修改商品';
	    ELSEIF NOT EXISTS (SELECT 1 FROM t_category WHERE F_ID = iCategoryID) THEN
	    	SET iErrorCode := 7;
			SET sErrorMsg := '不能用不存在的CategoryID修改商品';
		ELSEIF NOT EXISTS (SELECT 1 FROM t_packageunit WHERE F_ID = iPackageUnitID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '不能用不存在的PackageUnitID修改商品';
	    ELSEIF NOT EXISTS (SELECT 1 FROM t_staff WHERE F_ID = iINT2) THEN
	    	SET iErrorCode := 4;
			SET sErrorMsg := '该用户不存在，黑客行为';
	    ELSE
	    	SELECT F_Specification, F_Name, F_PackageUnitID, F_CategoryID INTO specification, name, packageUnitID, categoryID FROM t_commodity WHERE F_ID = iID;
	    	            		
    		 -- 如果sPicture传进来是个:,就不做更新操作
    		IF sPicture <> ':' THEN
	    		IF sPicture = '' THEN -- 如果sPicture传进来是个'',就删除
	    			update t_commodity set F_Picture = '' WHERE F_ID = iID;
	    		ELSE  
	    			SET @sContentType := RIGHT(sPicture,4);
					IF @sContentType = 'jpeg' THEN
				  		SET @sContentType := '.jpeg';
			   		END IF;
	    			-- 将商品图片的名称根据ID进行命名
					update t_commodity set F_Picture = CONCAT(substring_index(sPicture, '/', 4),'',CONCAT('/', iID, @sContentType)) WHERE F_ID = iID;
	    		END IF;
			END IF;
			
	    	
			Update T_Commodity SET 
				F_Name = sName,
				F_ShortName = sShortName,
				F_Specification = sSpecification,
				F_PackageUnitID = iPackageUnitID,
				F_BrandID = iBrandID,
				F_CategoryID = iCategoryID,
				F_MnemonicCode = sMnemonicCode,
				F_PricingType = sPricingType,
--				F_IsServiceType = iIsServiceType,
				F_PriceVIP = fPriceVIP,
				F_PriceWholesale = fPriceWholesale,
--				F_RatioGrossMargin = fRatioGrossMargin,
				F_CanChangePrice = sCanChangePrice,
				F_RuleOfPoint = sRuleOfPoint,
				F_ShelfLife = sShelfLife,
				F_ReturnDays = sReturnDays,
				F_PurchaseFlag = fPurchaseFlag,
--				F_RefCommodityID = sRefCommodityID,
				F_RefCommodityMultiple = sRefCommodityMultiple,
--				F_IsGift = iIsGift,
				F_Tag = sTag,
--				F_Type = iType,
				F_UpdateDatetime = now(), 
				F_StartValueRemark = sStartValueRemark,
				F_PropertyValue1 = sPropertyValue1,
				F_PropertyValue2 = sPropertyValue2,
				F_PropertyValue3 = sPropertyValue3,
				F_PropertyValue4 = sPropertyValue4
			WHERE F_ID = iID and (F_Status = 0  OR F_Status = 1);
			IF ROW_COUNT() = 1 THEN 
				-- 插入商品修改历史表
		    	select Func_CreateCommodityHistory(iID, name, '$', specification, packageUnitID, categoryID, -100000000, -100000000, iINT2, '$', iShopID) INTO iFuncReturnCode;	 
		
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
 --					F_IsServiceType, 
--					F_LatestPricePurchase, 
--					F_PriceRetail, 
					F_PriceVIP, 
					F_PriceWholesale, 
--					F_RatioGrossMargin, 
					F_CanChangePrice, 
					F_RuleOfPoint, 
					F_Picture, 
					F_ShelfLife, 
					F_ReturnDays, 
					F_CreateDate, 
					F_PurchaseFlag, 
					F_RefCommodityID, 
					F_RefCommodityMultiple, 
--					F_IsGift, 
					F_Tag, 
--					F_NO, 
--					F_NOAccumulated, 
					F_Type, 
--					F_NOStart, 
--					F_PurchasingPriceStart, 
					F_StartValueRemark, 
					F_CreateDatetime, 
					F_UpdateDatetime, 
					F_PropertyValue1, 
					F_PropertyValue2, 
					F_PropertyValue3, 
					F_PropertyValue4
--			  		F_CurrentWarehousingID
				FROM T_Commodity WHERE F_ID = iID AND (F_Status = 0 OR F_Status = 1);	
			
				SET iErrorCode := 0;	
				SET sErrorMsg := '';
			ELSE			
				SET iErrorCode := 2;	-- 该行是已删除状态，对其更新是无效操作
				SET sErrorMsg := '已删除状态，更新无效';
				
			END IF;
		END IF;	

	COMMIT;
END;