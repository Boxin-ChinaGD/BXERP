DROP PROCEDURE IF EXISTS `SP_Commodity_UpdatePrice`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Commodity_UpdatePrice`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN iINT2 INT, -- staffID
	IN iPriceRetail Decimal(20,6),
--	IN iPricePurchase Decimal(20,6),
	IN iLatestPricePurchase Decimal(20,6),
	IN iShopID INT
)
BEGIN
	DECLARE oldPriceRetail Decimal(20,2);    -- 旧的零售价（商品价格全部是精确到分，所以商品改价，只会改到分的程度，所以只用Decimal(20,2)）
	DECLARE iFuncReturnCode INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		IF NOT EXISTS (SELECT 1 FROM t_staff WHERE F_ID = iINT2 AND F_Status = 0) THEN
			SET iErrorCode := 4;
			SET sErrorMsg := '该用户不存在';
		ELSE
			SELECT F_PriceRetail INTO oldPriceRetail FROM t_commodityshopinfo WHERE F_CommodityID = iID AND F_ShopID = iShopID;
			
			IF iPriceRetail >= 0 THEN
				
				UPDATE t_commodityshopinfo SET F_PriceRetail = iPriceRetail WHERE F_CommodityID = iID AND F_ShopID = iShopID;
				UPDATE t_commodity SET F_UpdateDatetime = now() WHERE F_ID = iID;
				-- 插入商品修改历史表
				select Func_CreateCommodityHistory(iID, '$', '$', '$', -1, -1, oldPriceRetail, -100000000, iINT2, '$', iShopID) INTO iFuncReturnCode;
			END IF;
			
			
		--	IF iPricePurchase >= 0 THEN
		--		UPDATE t_commodity SET F_PricePurchase = iPricePurchase,F_UpdateDatetime = now() WHERE F_ID = iID;
		--	END IF;
			
			IF iLatestPricePurchase >=0 THEN 
			  	UPDATE t_commodityshopinfo SET F_LatestPricePurchase = iLatestPricePurchase WHERE F_CommodityID = iID AND F_ShopID = iShopID;
			  	UPDATE t_commodity SET F_UpdateDatetime = now() WHERE F_ID = iID;
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
			 --	F_IsServiceType, 
			 --	F_PricePurchase, 
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
			FROM t_commodity WHERE F_ID = iID;
			
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
	
	COMMIT;
END;