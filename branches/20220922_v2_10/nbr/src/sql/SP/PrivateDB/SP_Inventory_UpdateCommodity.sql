DROP PROCEDURE IF EXISTS `SP_Inventory_UpdateCommodity`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Inventory_UpdateCommodity`(
	OUT iErrorCode INT,	 
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN iNOReal INT
	)
BEGIN
	DECLARE iStatus INT;
	DECLARE TotalCommodityID INT;
   
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SELECT F_Status INTO iStatus FROM t_inventorysheet WHERE F_ID = (SELECT F_InventorySheetID FROM t_inventorycommodity WHERE F_ID = iID);
			 
		IF iStatus = 0 AND iNOReal >= 0 THEN	
			UPDATE t_inventorycommodity SET 
			F_NOReal = iNOReal,
			F_UpdateDatetime = now()
			WHERE F_ID = iID;
				
		    SELECT F_ID, F_InventorySheetID, F_CommodityID, F_NOReal, F_NOSystem,F_CreateDatetime,F_UpdateDatetime FROM t_inventorycommodity 
			WHERE F_ID = iID;
			
	   		SET iErrorCode := 0;
	   		SET sErrorMsg := '';
		ELSE 
			SET iErrorCode := 7;  -- 不符合商业逻辑
			SET sErrorMsg := '修改商品不符合商业逻辑';
	   	END IF;
	
	COMMIT;
END;