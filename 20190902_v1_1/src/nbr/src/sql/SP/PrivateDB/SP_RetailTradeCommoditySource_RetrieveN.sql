DROP PROCEDURE IF EXISTS `SP_RetailTradeCommoditySource_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_RetailTradeCommoditySource_RetrieveN` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iRetailTradeCommodityID INT,
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
			F_RetailTradeCommodityID, 
			F_ReducingCommodityID, 
			F_NO, 
			F_WarehousingID 
		FROM t_retailtradecommoditysource 
		WHERE 1=1
		AND (CASE iRetailTradeCommodityID WHEN 0 THEN 1=1 ELSE F_RetailTradeCommodityID = iRetailTradeCommodityID END)
		ORDER BY F_ID DESC LIMIT recordIndex, iPageSize;
		
	    SELECT count(1) into iTotalRecord FROM t_retailtradecommoditysource 
	   	WHERE 1=1
		AND (CASE iRetailTradeCommodityID WHEN 0 THEN 1=1 ELSE F_RetailTradeCommodityID = iRetailTradeCommodityID END)
		ORDER BY F_ID DESC LIMIT recordIndex, iPageSize;
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	COMMIT;
END;