DROP PROCEDURE IF EXISTS `SP_PackageUnit_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_PackageUnit_RetrieveN`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iNormalCommodityID INT,
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
		
		IF iNormalCommodityID > 0 THEN
	   		SELECT F_ID, F_Name, F_CreateDatetime, F_UpdateDatetime FROM t_packageunit WHERE F_ID IN 
	   		(
	   			SELECT F_packageUnitID FROM t_commodity WHERE F_Type = 2 AND F_RefCommodityID = iNormalCommodityID
	   		);
	   		
	   		SELECT COUNT(1) INTO iTotalRecord FROM t_packageunit WHERE F_ID IN 
	   		(
	   			SELECT F_packageUnitID FROM t_commodity WHERE F_Type = 2 AND F_RefCommodityID = iNormalCommodityID
	   		);
	   	ELSE
	   	
			SELECT F_ID, F_Name, F_CreateDatetime, F_UpdateDatetime FROM t_packageunit
			ORDER BY F_ID DESC
			LIMIT recordIndex, iPageSize;
			
			SELECT count(1) into iTotalRecord FROM t_packageunit;
	
		END IF;
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END;