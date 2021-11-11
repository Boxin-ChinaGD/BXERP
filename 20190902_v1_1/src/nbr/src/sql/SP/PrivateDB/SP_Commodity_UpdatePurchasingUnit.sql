DROP PROCEDURE IF EXISTS `SP_Commodity_UpdatePurchasingUnit`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Commodity_UpdatePurchasingUnit`(	
	OUT iErrorCode INT,	
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,	
	IN sPurchasingUnit VARCHAR (16)
	)
BEGIN	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;	
		SET sErrorMsg := 'Êý¾Ý¿â´íÎó';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		UPDATE t_commodity SET F_PurchasingUnit = sPurchasingUnit WHERE F_ID = iID;
	
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
	--		F_PricePurchase, 
--			F_LatestPricePurchase, 
--			F_PriceRetail, 
			F_PriceVIP, 
			F_PriceWholesale, 
--			F_RatioGrossMargin, 
			F_CanChangePrice, 
			F_RuleOfPoint, 
			F_Picture, 
			F_ShelfLife, 
			F_ReturnDays, 
			F_CreateDate, 
			F_PurchaseFlag, 
			F_RefCommodityID, 
			F_RefCommodityMultiple, 
--			F_IsGift, 
			F_Tag, 
--			F_NO, 
--			F_NOAccumulated, 
			F_Type, 
--			F_NOStart, 
--			F_PurchasingPriceStart, 
			F_StartValueRemark, 
			F_CreateDatetime, 
			F_UpdateDatetime, 
			F_PropertyValue1, 
			F_PropertyValue2, 
			F_PropertyValue3, 
			F_PropertyValue4
--			F_CurrentWarehousingID
		FROM t_commodity
		WHERE F_ID = iID;
	
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
	COMMIT;
END;