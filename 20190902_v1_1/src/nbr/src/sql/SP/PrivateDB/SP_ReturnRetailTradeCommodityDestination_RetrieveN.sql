DROP PROCEDURE IF EXISTS `SP_ReturnRetailTradeCommodityDestination_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_ReturnRetailTradeCommodityDestination_RetrieveN` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iRetailTradeCommodityID INT, 
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
	
		SELECT 
			F_ID, 
			F_RetailTradeCommodityID, 
			F_IncreasingCommodityID, 
			F_NO, 
			F_WarehousingID 
		FROM t_returnretailtradecommoditydestination 
		WHERE 1=1
		AND (CASE iRetailTradeCommodityID WHEN 0 THEN 1=1 ELSE F_RetailTradeCommodityID = iRetailTradeCommodityID END)
		ORDER BY F_ID DESC;
		
	    SELECT count(1) into iTotalRecord FROM t_returnretailtradecommoditydestination 
	   	WHERE 1=1
		AND (CASE iRetailTradeCommodityID WHEN 0 THEN 1=1 ELSE F_RetailTradeCommodityID = iRetailTradeCommodityID END)
		ORDER BY F_ID DESC;
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	COMMIT;
END;