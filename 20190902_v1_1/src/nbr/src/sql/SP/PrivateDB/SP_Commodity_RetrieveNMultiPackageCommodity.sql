DROP PROCEDURE IF EXISTS `SP_Commodity_RetrieveNMultiPackageCommodity`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Commodity_RetrieveNMultiPackageCommodity`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN iPageIndex INT,
	IN iPageSize INT,
	OUT iTotalRecord INT
	)
BEGIN
	DECLARE recordIndex INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
	  	SET sErrorMsg := 'Êý¾Ý¿â´íÎó';
	  	ROLLBACK;
  	END;
  	
  	START TRANSACTION;
  	
	  	SET iPageIndex = iPageIndex -1;
	  	
	  	SET recordIndex = iPageIndex * iPageSize;
	  	
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
	--			F_IsServiceType, 
	--			F_PricePurchase, 
--				F_LatestPricePurchase, 
--				F_PriceRetail, 
				F_PriceVIP, 
				F_PriceWholesale, 
--				F_RatioGrossMargin, 
				F_CanChangePrice, 
				F_RuleOfPoint, 
				F_Picture, 
				F_ShelfLife, 
				F_ReturnDays, 
				F_CreateDate, 
				F_PurchaseFlag, 
				F_RefCommodityID, 
				F_RefCommodityMultiple, 
--				F_IsGift, 
				F_Tag, 
--				F_NO, 
--				F_NOAccumulated, 
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
			FROM T_Commodity
	 		WHERE  F_Status != 2 AND F_RefCommodityID = iID
			ORDER BY F_ID DESC 
			LIMIT recordIndex, iPageSize;
			
			SELECT count(1) into iTotalRecord FROM T_Commodity
	 		WHERE  F_Status != 2 
			AND F_RefCommodityID = iID
			ORDER BY F_ID DESC 
			LIMIT recordIndex, iPageSize;	-- ... Î£ÏÕ£¡£¡£¡£¡£¡
			
			SET iErrorCode := 0;
			SET sErrorMsg := '';
	
	COMMIT;
END;