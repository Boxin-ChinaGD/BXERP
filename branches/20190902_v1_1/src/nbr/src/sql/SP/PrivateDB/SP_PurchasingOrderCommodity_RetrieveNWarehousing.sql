DROP PROCEDURE IF EXISTS `SP_PurchasingOrderCommodity_RetrieveNWarehousing`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_PurchasingOrderCommodity_RetrieveNWarehousing`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iPurchasingOrderID INT,
	IN iWarehousing INT,	-- 1，已入库。0，未入库
--	IN iPageIndex INT,
--	IN iPageSize INT,
	OUT iTotalRecord INT
	)
BEGIN
	DECLARE recordIndex INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		IF iWarehousing = 1 THEN
		
			SELECT F_ID, F_Status, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime 
			FROM t_warehousing 
			WHERE F_PurchasingOrderID = iPurchasingOrderID AND F_Status = 1;
			
			SELECT F_ID, F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime
			FROM t_warehousingcommodity
			WHERE F_WarehousingID IN (SELECT F_ID	FROM t_warehousing WHERE F_PurchasingOrderID = iPurchasingOrderID AND F_Status = 1);	-- 不分页
			
			SELECT FOUND_ROWS() INTO iTotalRecord;
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		ELSEIF iWarehousing = 0 THEN 
			
			SELECT 
				F_PurchasingOrderID, 
				F_CommodityID, 
				F_CommodityName, 
				F_BarcodeID, 
				F_PackageUnitID, 
				F_PriceSuggestion, 
				F_CommodityNO, 
				(tmp.F_CommodityNO - IF (tmp.F_TotalPurchased is NULL, 0, tmp.F_TotalPurchased)) AS F_NORemaining 
			FROM 
			(
				SELECT
					F_PurchasingOrderID, 
					F_CommodityID, 
					F_CommodityName, 
					F_BarcodeID, 
					F_PackageUnitID, 
					F_PriceSuggestion, 
					F_CommodityNO,		
					(
						SELECT sum(wc.F_NO) FROM t_warehousingcommodity wc
						WHERE wc.F_WarehousingID in 
							(
								SELECT F_ID FROM t_warehousing WHERE F_PurchasingOrderID = iPurchasingOrderID AND F_Status = 1
							) 
							AND wc.F_CommodityID = poc.F_CommodityID
					) AS F_TotalPurchased 
				FROM t_purchasingordercommodity poc
				WHERE poc.F_PurchasingOrderID = iPurchasingOrderID
			) AS tmp
			WHERE tmp.F_CommodityNO - IF (tmp.F_TotalPurchased is NULL, 0, tmp.F_TotalPurchased) > 0;
			
			SELECT FOUND_ROWS() INTO iTotalRecord;
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		ELSE
			SET iErrorCode := 7;
			SET sErrorMsg := '不能查询到其他状态的入库单';
		END IF;
	
	COMMIT;
END;