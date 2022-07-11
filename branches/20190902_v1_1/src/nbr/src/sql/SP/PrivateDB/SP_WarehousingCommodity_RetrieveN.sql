DROP PROCEDURE IF EXISTS `SP_WarehousingCommodity_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_WarehousingCommodity_RetrieveN`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iWarehousingID INT,
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
			F_NOSalable
		FROM t_warehousingcommodity WHERE F_WarehousingID = iWarehousingID
		ORDER BY F_ID DESC
		LIMIT recordIndex, iPageSize;
		
		SELECT count(1) into iTotalRecord
		FROM t_warehousingcommodity WHERE F_WarehousingID = iWarehousingID;
	   
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END;