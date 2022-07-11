DROP PROCEDURE IF EXISTS `SP_Warehousing_Retrieve1`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Warehousing_Retrieve1`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT
	)
BEGIN
	DECLARE iStatus INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := 'Êý¾Ý¿â´íÎó';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
	SET iErrorCode := 0;
	SET sErrorMsg := '';
		
		SELECT F_Status INTO iStatus FROM t_warehousing WHERE F_ID = iID;
	
		SELECT F_ID, F_ShopID, F_SN, F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID,
		(SELECT F_SN FROM t_purchasingorder WHERE F_ID = ws.F_PurchasingOrderID) AS purchasingOrderSN
		FROM t_warehousing ws WHERE F_ID = iID;
	
		IF iStatus = 1 THEN 		
			SELECT 
				F_ID, 
				F_WarehousingID, 
				F_CommodityID, 
				F_NO, 
				F_PackageUnitID, 
				F_CommodityName, 
				F_BarcodeID, 
				F_Price, 
				F_Amount, 
				F_ProductionDatetime, 
				F_ShelfLife, 
				F_ExpireDatetime, 
				F_CreateDatetime, 
				F_UpdateDatetime, 
				F_NOSalable
			FROM t_warehousingcommodity
			WHERE F_WarehousingID = iID;
		ELSE 
			SELECT 
				F_ID, 
				F_WarehousingID, 
				F_CommodityID, 
				F_NO, 
				F_PackageUnitID, 
				(SELECT F_Name FROM t_commodity WHERE F_ID = F_CommodityID) AS F_CommodityName, 
				F_BarcodeID, 
				F_Price, 
				F_Amount, 
				F_ProductionDatetime, 
				F_ShelfLife, 
				F_ExpireDatetime, 
				F_CreateDatetime, 
				F_UpdateDatetime, 
				F_NOSalable
			FROM t_warehousingcommodity
			WHERE F_WarehousingID = iID;
			
		END IF;
	COMMIT;
END