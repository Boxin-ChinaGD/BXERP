DROP PROCEDURE IF EXISTS `SP_Commodity_Retrieve1`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Commodity_Retrieve1`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN iIncludeDeleted INT
	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
		IF EXISTS (SELECT 1 FROM T_Commodity WHERE F_ID = iID AND F_Status = 2) AND iIncludeDeleted = 0 THEN
			SET iErrorCode := 2;
			SET sErrorMsg := '此商品是删除商品';
		ELSE
			
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
			FROM T_Commodity WHERE F_ID = iID;
			
			IF (SELECT F_Type FROM t_commodity WHERE F_ID = iID) = 1 THEN
				SELECT 
					F_ID, 
					F_CommodityID, 
					F_SubCommodityID,
					F_SubCommodityNO,
					F_Price
				FROM t_subcommodity
				WHERE F_CommodityID = iID;
			END IF;
		END IF;
	
	COMMIT;
END;