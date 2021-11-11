DROP PROCEDURE IF EXISTS `SP_ProviderCommodity_Delete`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_ProviderCommodity_Delete`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
   	IN iCommodityID INT,
   	IN iProviderID INT
	)
BEGIN 
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := 'Êý¾Ý¿â´íÎó';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		DELETE FROM t_providercommodity 
		WHERE F_CommodityID = iCommodityID
		AND (CASE iProviderID WHEN 0 THEN 1=1 ELSE F_ProviderID = iProviderID END);
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
	COMMIT;
END;