DROP PROCEDURE IF EXISTS `SP_PurchasingOrder_Retrieve1`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_PurchasingOrder_Retrieve1`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT
	)
BEGIN
   --	DECLARE F_warehousingNO INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;

	SET iErrorCode := 0;
    SET sErrorMsg := '';

	START TRANSACTION;
	
	 	SELECT
			F_ID,
			F_ShopID,
			F_SN,
			F_Status,
			F_StaffID,
			F_ProviderID,
			F_ProviderName,
			F_ApproverID,
			F_Remark,
			F_CreateDatetime,
			F_ApproveDatetime,
			F_EndDatetime,
			F_UpdateDatetime
		FROM t_purchasingorder WHERE F_ID = iID AND F_Status <> 4;
		
		-- 返回采购订单商品信息（从表）
		SELECT 
			F_ID, 
			F_PurchasingOrderID, 
			F_CommodityID, 
			F_CommodityNO, 
			F_CommodityName, 
			F_BarcodeID, 
			F_PackageUnitID, 
			F_PriceSuggestion, 
			F_CreateDatetime, 
			F_UpdateDatetime,
			(SELECT F_Barcode FROM t_barcodes WHERE F_ID = poc.F_BarcodeID) AS barcode,
		   	(SELECT F_Name FROM t_packageunit WHERE F_ID = poc.F_PackageUnitID) AS packageUnitName ,
			(
			SELECT sum(wc.F_NO) FROM t_warehousingcommodity wc
			WHERE wc.F_WarehousingID in 
				(
					SELECT F_ID FROM t_warehousing WHERE F_PurchasingOrderID = iID AND F_Status = 1
				) 
				AND wc.F_CommodityID = poc.F_CommodityID
			) AS warehousingNO
		FROM t_purchasingordercommodity poc
		WHERE F_PurchasingOrderID = iID
		ORDER BY F_ID ASC;	
		
	COMMIT;
END;