DROP PROCEDURE IF EXISTS `SP_PurchasingOrderCommodity_Delete`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_PurchasingOrderCommodity_Delete`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
   	IN iPurchasingOrderID INT,
   	IN iCommodityID INT
	)
BEGIN 
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		IF iCommodityID > 0 THEN
			DELETE FROM t_purchasingordercommodity WHERE F_CommodityID = iCommodityID AND F_PurchasingOrderID = iPurchasingOrderID;
		ELSE
			DELETE FROM t_purchasingordercommodity WHERE F_PurchasingOrderID = iPurchasingOrderID;
		END IF;
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END