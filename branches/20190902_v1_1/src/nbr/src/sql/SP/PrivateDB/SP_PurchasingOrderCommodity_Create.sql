DROP PROCEDURE IF EXISTS `SP_PurchasingOrderCommodity_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_PurchasingOrderCommodity_Create`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
   	IN iPurchasingOrderID INT,
   	IN iCommodityID INT,
   	IN iCommodityNO INT,
   	IN iBarcodeID INT,
   	IN fPriceSuggestion Decimal(20,6)
	)
BEGIN

	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		IF EXISTS (SELECT 1 FROM t_commodity WHERE F_ID = iCommodityID AND F_Status = 2) THEN
			SET iErrorCode := 2;
			SET sErrorMsg := '不能添加已删除的商品进采购订单';
		ELSEIF EXISTS (SELECT 1 FROM t_commodity WHERE F_ID = iCommodityID AND F_Type <> 0) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '不能添加非单品的商品进采购订单';
		ELSEIF EXISTS (SELECT 1 FROM t_purchasingordercommodity WHERE F_CommodityID = iCommodityID AND F_PurchasingOrderID = iPurchasingOrderID) THEN 
		
			SET iErrorCode := 1;
			SET sErrorMsg := '不能添加重复的商品到同一采购订单中';
		
		-- 这里会覆盖掉商品不存在的情况，所以最后的ELSE可能执行不到
		ELSEIF NOT EXISTS (SELECT 1 FROM t_barcodes WHERE F_CommodityID = iCommodityID AND F_ID = iBarcodeID) THEN
		   		SET iErrorCode := 7;
		   		SET sErrorMsg := '条形码ID与商品实际条形码ID不对应';
		   		
		ELSEIF EXISTS (SELECT 1 FROM t_commodity WHERE F_ID = iCommodityID) THEN
		
		   	INSERT INTO t_purchasingordercommodity (
				F_PurchasingOrderID, 
				F_CommodityID, 
				F_CommodityNO, 
				F_CommodityName, 
				F_BarcodeID, 
				F_PackageUnitID, 
				F_PriceSuggestion)
			VALUES (
				iPurchasingOrderID, 
				iCommodityID, 
				iCommodityNO, 
				(SELECT F_Name FROM t_commodity WHERE F_ID = iCommodityID), 
				iBarcodeID, 
				(SELECT F_PackageUnitID FROM t_commodity WHERE F_ID = iCommodityID), 
				fPriceSuggestion);
			
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
				F_UpdateDatetime
			FROM t_purchasingordercommodity
			WHERE F_ID = last_insert_id();
			
			SET iErrorCode := 0;
			SET sErrorMsg := '';
	 	ELSE
	 	 	SET iErrorCode := 7;
	 	 	SET sErrorMsg := '不能添加一个不存在的商品进采购订单';
		END IF;
	
	COMMIT;
END;