DROP PROCEDURE IF EXISTS `SP_RefCommodityHub_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_RefCommodityHub_RetrieveN`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN sBarcode VARCHAR(20),
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
	
		SET iPageIndex = iPageIndex - 1;
		
		SET recordIndex = iPageIndex * iPageSize;
	
		SELECT 
			F_ID, 
			F_Barcode, 
			F_Name, 
			F_ShortName, 
			F_Specification, 
			F_PackageUnitName, 
			F_PurchasingUnit, 
			F_BrandName, 
			F_CategoryName, 
			F_MnemonicCode, 
			F_PricingType, 	  
			F_Type, 
			F_PricePurchase, 
			F_LatestPricePurchase, 
			F_PriceRetail, 
			F_PriceVIP, 
			F_PriceWholesale, 	
			F_ShelfLife, 
			F_ReturnDays
		FROM staticDB.t_refcommodityhub WHERE F_Barcode = sBarcode
		ORDER BY F_ID DESC
		LIMIT recordIndex, iPageSize;
	
		SELECT count(1) into iTotalRecord
		FROM staticDB.t_refcommodityhub WHERE F_Barcode = sBarcode;
	
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END;