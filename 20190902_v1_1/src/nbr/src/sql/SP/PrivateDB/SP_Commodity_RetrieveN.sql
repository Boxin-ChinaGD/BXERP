DROP PROCEDURE IF EXISTS `SP_Commodity_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Commodity_RetrieveN`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iStatus INT,
	IN iNO INT,
	IN iCategoryID INT,
	IN iBrandID INT,
	IN iType INT,
	IN dStartDatetime DATETIME,
	IN dEndDatetime DATETIME,
	IN string1 VARCHAR(64),
	IN iPageIndex INT,
	IN iPageSize INT,
	OUT iTotalRecord INT
	)
BEGIN
	DECLARE recordIndex INT;
	DECLARE INVALID_ID INT;
	DECLARE INVALID_STATUS INT;
	DECLARE INVALID_NO INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := 'Êý¾Ý¿â´íÎó';
		ROLLBACK;
	END;
			
	SET iErrorCode := 0;
	SET sErrorMsg := '';
			
	START TRANSACTION;
  	
	  	SELECT F_Value INTO INVALID_ID FROM t_nbrconstant WHERE F_Key = 'INVALID_ID'; 
		SELECT F_Value INTO INVALID_STATUS FROM t_nbrconstant WHERE F_Key = 'INVALID_STATUS'; 
	  	SELECT F_Value INTO INVALID_NO FROM t_nbrconstant WHERE F_Key = 'INVALID_NO';
	  	 
	  	SET iPageIndex = iPageIndex - 1;
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
				F_PropertyValue4,
--				F_CurrentWarehousingID,
				(SELECT F_Name FROM t_brand WHERE F_ID = t.F_BrandID) AS brandName,
				(SELECT F_Name FROM t_category WHERE F_ID = t.F_CategoryID) AS categoryName
			FROM T_Commodity t
	 		WHERE  F_Status != 2 AND F_Type != 2
   	   		AND (CASE iCategoryID WHEN INVALID_ID THEN 1=1 ELSE F_CategoryID = iCategoryID END)
   			AND (CASE iBrandID WHEN INVALID_ID THEN 1=1 ELSE F_BrandID = iBrandID END)
      		AND (CASE iStatus WHEN INVALID_STATUS THEN 1 = 1 ELSE F_Status = iStatus END)    
--      		AND (CASE iNO WHEN INVALID_NO THEN 1 = 1 ELSE F_NO > 0 END)
      		AND (CASE iNO WHEN INVALID_NO THEN 1 = 1 ELSE F_ID IN (SELECT F_CommodityID FROM t_commodityshopinfo WHERE F_NO > 0) END)
      		AND (CASE iType WHEN -1 THEN 1 = 1 ELSE F_Type = iType END)
      		AND (CASE IFNULL(dStartDatetime, -1) WHEN -1 THEN 1 = 1 ELSE F_CreateDate >= dStartDatetime END)
      		AND (CASE IFNULL(dEndDatetime, -1) WHEN -1 THEN 1 = 1 ELSE F_CreateDate <= dEndDatetime END)
      		AND (
    			F_Name LIKE CONCAT('%', replace(string1, '_', '\_'), '%')
    			OR F_ShortName LIKE CONCAT('%', replace(string1, '_', '\_'), '%')
   			    OR F_MnemonicCode LIKE CONCAT('%', replace(string1, '_', '\_'), '%') 
    		    OR (
    		    	(length(string1) > 6 AND length(string1) <= 64) 
    		    	AND t.F_ID IN (SELECT b.F_CommodityID FROM t_barcodes b WHERE b.F_Barcode LIKE CONCAT('%', replace(string1, '_', '\_') , '%')))
   			)
      		ORDER BY F_ID DESC 
   			LIMIT recordIndex, iPageSize;
	
		   	SELECT count(1) into iTotalRecord
			FROM T_Commodity t
	 		WHERE  F_Status != 2 AND F_Type != 2
			AND (CASE iCategoryID WHEN INVALID_ID THEN 1=1 ELSE F_CategoryID = iCategoryID END)
			AND (CASE iBrandID WHEN INVALID_ID THEN 1=1 ELSE F_BrandID = iBrandID END)
		    AND (CASE iStatus WHEN INVALID_STATUS THEN 1 = 1 ELSE F_Status = iStatus END)	   
--		    AND (CASE iNO WHEN INVALID_NO THEN 1 = 1 ELSE F_NO > 0 END)
		    AND (CASE iNO WHEN INVALID_NO THEN 1 = 1 ELSE F_ID IN (SELECT F_CommodityID FROM t_commodityshopinfo WHERE F_NO > 0) END)
		    AND (CASE iType WHEN -1 THEN 1 = 1 ELSE F_Type = iType END)
		    AND (CASE IFNULL(dStartDatetime, -1) WHEN -1 THEN 1 = 1 ELSE F_CreateDate >= dStartDatetime END)
		    AND (CASE IFNULL(dEndDatetime, -1) WHEN -1 THEN 1 = 1 ELSE F_CreateDate <= dEndDatetime END)
		    AND (
				F_Name LIKE CONCAT('%', replace(string1, '_', '\_'),'%')
				OR F_ShortName LIKE CONCAT('%', replace(string1, '_', '\_'), '%')
				OR F_MnemonicCode LIKE CONCAT('%', replace(string1, '_', '\_'), '%') 
			    OR (
    		    	(length(string1) > 6 AND length(string1) <= 64) 
    		    	AND t.F_ID IN (SELECT b.F_CommodityID FROM t_barcodes b WHERE b.F_Barcode LIKE CONCAT('%', replace(string1, '_', '\_') , '%')))
    		   );	
	COMMIT;
END;