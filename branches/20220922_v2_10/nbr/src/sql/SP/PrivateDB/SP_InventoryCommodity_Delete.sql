DROP PROCEDURE IF EXISTS `SP_InventoryCommodity_Delete`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_InventoryCommodity_Delete`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iInventorySheetID INT,
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
			DELETE FROM t_inventorycommodity WHERE F_CommodityID = iCommodityID;
		ELSE
			DELETE FROM t_inventorycommodity WHERE F_InventorySheetID = iInventorySheetID;
		END IF;
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
	COMMIT;
END;