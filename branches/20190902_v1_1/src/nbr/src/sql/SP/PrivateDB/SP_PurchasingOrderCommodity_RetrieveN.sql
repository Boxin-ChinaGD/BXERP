DROP PROCEDURE IF EXISTS `SP_PurchasingOrderCommodity_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_PurchasingOrderCommodity_RetrieveN`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iPurchasingOrderID INT,
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
			poc.F_ID, 
			poc.F_PurchasingOrderID, 
			poc.F_CommodityID, 
			poc.F_CommodityNO, 
			poc.F_CommodityName, 
			poc.F_BarcodeID, 
			poc.F_PackageUnitID, 
			poc.F_PriceSuggestion, 
			poc.F_CreateDatetime, 
			poc.F_UpdateDatetime,
			b.F_Barcode AS barcode,
			pa.F_Name AS packageUnitName,
			(
			SELECT sum(wc.F_NO) FROM t_warehousingcommodity wc
			WHERE wc.F_WarehousingID in 
				(
					SELECT F_ID FROM t_warehousing WHERE F_PurchasingOrderID = iPurchasingOrderID AND F_Status = 1
				) 
				AND wc.F_CommodityID = poc.F_CommodityID
			) AS warehousingNO
			
		FROM t_purchasingordercommodity AS poc, t_barcodes AS b, t_packageunit AS pa 
		WHERE poc.F_PurchasingOrderID = iPurchasingOrderID AND b.F_ID = poc.F_BarcodeID AND pa.F_ID = poc.F_PackageUnitID
		ORDER BY poc.F_ID DESC
		LIMIT recordIndex, iPageSize;
		
		SELECT count(1) into iTotalRecord
		FROM t_purchasingordercommodity WHERE F_PurchasingOrderID = iPurchasingOrderID;
	
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END