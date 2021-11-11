DROP PROCEDURE IF EXISTS `SP_RetailTradeCommodity_RetrieveNTradeCommodity`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_RetailTradeCommodity_RetrieveNTradeCommodity` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iTradeID INT,
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
			F_TradeID, 
			F_CommodityID, 
			F_CommodityName,
			F_BarcodeID,
			F_NO, 
			F_PriceOriginal, 
			F_NOCanReturn,
			F_PriceReturn,
			F_PriceSpecialOffer,
			F_PriceVIPOriginal
		FROM t_retailtradecommodity WHERE F_TradeID = iTradeID ORDER BY F_ID DESC LIMIT recordIndex, iPageSize;
		
	    SELECT count(1) into iTotalRecord
		FROM t_retailtradecommodity WHERE F_TradeID = iTradeID;
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END;