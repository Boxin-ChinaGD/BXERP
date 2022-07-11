DROP PROCEDURE IF EXISTS `SP_ProviderCommodity_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_ProviderCommodity_RetrieveN`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iCommodityID INT,
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
			F_CommodityID,
			F_ProviderID
		FROM t_providercommodity
		WHERE F_CommodityID = iCommodityID
		ORDER BY F_ID DESC
		LIMIT recordIndex, iPageSize;
		
		SELECT count(1) into iTotalRecord
		FROM t_providercommodity
		WHERE F_CommodityID = iCommodityID;
	
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END;