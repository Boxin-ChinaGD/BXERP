DROP PROCEDURE IF EXISTS `SPD_InventoryCommodity_CheckInventorySheetID`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_InventoryCommodity_CheckInventorySheetID`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iInventoryCommodityID INT;
	DECLARE iInventorysheetID INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	DECLARE list CURSOR FOR (SELECT F_ID AS iInventoryCommodityID, F_InventorysheetID AS iInventorysheetID FROM t_inventorycommodity);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iInventoryCommodityID,iInventorysheetID;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- 检查所有盘点单商品表的盘点单ID是否为盘点单表中的盘点单
			IF EXISTS(SELECT 1 FROM t_inventorysheet WHERE F_ID = iInventorysheetID) THEN
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			ELSE
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('盘点单商品', iInventoryCommodityID ,'对应的盘点单ID不正确');
			END IF;
			
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;