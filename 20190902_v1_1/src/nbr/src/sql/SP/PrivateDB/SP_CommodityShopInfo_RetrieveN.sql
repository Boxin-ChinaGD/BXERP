DROP PROCEDURE IF EXISTS `SP_CommodityShopInfo_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_CommodityShopInfo_RetrieveN`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iCommodityID INT,
	IN iShopID INT,
 	OUT iTotalRecord INT
)
BEGIN
	DECLARE recordIndex INT;
	DECLARE INVALID_ID INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := 'Êý¾Ý¿â´íÎó';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		SELECT F_ID, F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID
		FROM t_commodityshopinfo 
		WHERE 1=1
			AND CASE iCommodityID WHEN -1 THEN 1=1 ELSE F_CommodityID = iCommodityID END 
			AND CASE iShopID WHEN 0 THEN 1=1 ELSE F_ShopID = iShopID END;
		
		SELECT count(1) into iTotalRecord
		FROM t_commodityshopinfo 
		WHERE 1=1
			AND CASE iCommodityID WHEN -1 THEN 1=1 ELSE F_CommodityID = iCommodityID END 
			AND CASE iShopID WHEN 0 THEN 1=1 ELSE F_ShopID = iShopID END;
			
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END;