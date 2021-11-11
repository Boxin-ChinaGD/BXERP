DROP PROCEDURE IF EXISTS `SPD_InventoryCommodtiy_CheckCommodity`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_InventoryCommodtiy_CheckCommodity`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iInventoryCommodityID INT;
	DECLARE iInventorysheetID INT;
	DECLARE iCommodityID INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	DECLARE list CURSOR FOR (SELECT F_ID AS iInventoryCommodityID, F_CommodityID AS iCommodityID,F_InventorysheetID AS iInventorysheetID FROM t_inventorycommodity);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iInventoryCommodityID,iCommodityID,iInventorysheetID;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- 检查商品ID是否是商品中已经存在的商品(存在盘点单的商品不能被删除)
			-- 检查所有盘点商品表的商品只能有普通商品的盘点单
			IF EXISTS(SELECT 1 FROM t_commodity WHERE F_ID = iCommodityID AND F_Type = 0 AND F_Status <> 2) THEN
				-- 检查是否有相同的商品到同一个盘点单中
				IF (SELECT count(1) FROM t_inventorycommodity WHERE F_CommodityID = iCommodityID AND F_InventorysheetID = iInventorysheetID) >= 2 THEN
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('盘点单商品', iInventoryCommodityID ,'不能有相同的商品在同一个盘点单');
				ELSE 
					SET iErrorCode := 0;
					SET sErrorMsg := '';
				END IF;
			ELSEIF EXISTS(SELECT 1 FROM t_commodity WHERE F_ID = iCommodityID AND F_Type <> 0 AND F_Status <> 2) THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('盘点单商品', iInventoryCommodityID ,'对应的商品的商品类型不是单品');
			ELSE 
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('盘点单商品', iInventoryCommodityID ,'对应的商品不存在');
			END IF;
			
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;