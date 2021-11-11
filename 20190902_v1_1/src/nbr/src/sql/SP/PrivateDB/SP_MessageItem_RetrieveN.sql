DROP PROCEDURE IF EXISTS `SP_MessageItem_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_MessageItem_RetrieveN`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iMessageCategoryID INT,
--	IN iShopID INT,
	IN iPageIndex INT,
	IN iPageSize INT,
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
	
		
		SET iPageIndex = iPageIndex - 1;
		
		SET recordIndex = iPageIndex * iPageSize;
		
		IF NOT EXISTS (SELECT 1 FROM t_messagecategory WHERE F_ID = iMessageCategoryID) THEN
			SET iErrorCode = 7;
			SET sErrorMsg = '传入的消息分类ID不正确，不存在该消息分类';
		
		ELSEIF iMessageCategoryID = 8 THEN
			SELECT F_ID,
				   F_MessageID, 
		  		   F_MessageCategoryID, 
			       F_CommodityID, 
			       F_CreateDatetime, 
		   		   F_UpdateDatetime,
		   		   (SELECT F_Name  FROM t_commodity WHERE F_ID = m.F_CommodityID) AS commodityName,
		   		   (SELECT F_Barcode FROM t_barcodes WHERE F_CommodityID = m.F_CommodityID LIMIT 1) AS commodityBarcode,
		   		   -- 目前先使用默认门店(F_ShopID = 2)的零售价和最近采购价
		   		   (SELECT F_PriceRetail FROM t_commodityshopinfo WHERE F_CommodityID = m.F_CommodityID AND F_ShopID = 2) AS commodityPriceRetail,
		   		-- (SELECT F_PriceSuggestion FROM t_purchasingordercommodity WHERE F_CommodityID = m.F_CommodityID AND F_PurchasingOrderID = ( SELECT F_PurchasingOrderID FROM t_warehousing WHERE F_ID = (SELECT F_CurrentWarehousingID FROM t_commodity WHERE F_ID = m.F_CommodityID))) AS commodityPriceSuggestion
		   		   (SELECT F_LatestPricePurchase FROM t_commodityshopinfo WHERE F_CommodityID = m.F_CommodityID AND F_ShopID = 2) AS commodityPriceSuggestion
			FROM t_messageitem m WHERE F_MessageCategoryID = iMessageCategoryID
			ORDER BY F_ID DESC
			LIMIT recordIndex, iPageSize;	-- ... 格式化、效率优化
			
			SELECT count(1) into iTotalRecord
			FROM t_messageitem WHERE F_MessageCategoryID = iMessageCategoryID; 
		
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		
		ELSE 
			SELECT F_ID, F_MessageID, F_MessageCategoryID, F_CommodityID, F_CreateDatetime, F_UpdateDatetime
			FROM t_messageitem WHERE F_MessageCategoryID = iMessageCategoryID
			ORDER BY F_ID DESC
			LIMIT recordIndex, iPageSize;	-- ... 格式化、效率优化
			
			SELECT count(1) into iTotalRecord
			FROM t_messageitem WHERE F_MessageCategoryID = iMessageCategoryID; 
		
			SET iErrorCode := 0;
			SET sErrorMsg := '';	
		END IF;		
	COMMIT;
END;