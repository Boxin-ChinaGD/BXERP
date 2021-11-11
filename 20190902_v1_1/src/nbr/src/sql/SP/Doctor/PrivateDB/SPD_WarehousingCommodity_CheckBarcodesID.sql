DROP PROCEDURE IF EXISTS `SPD_WarehousingCommodity_CheckBarcodesID`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_WarehousingCommodity_CheckBarcodesID`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	-- ...临时模板，下面的值需要根据需求变化
	DECLARE iWarehousingCommodityID INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- 临时模板，下面list需要根据需求变化
	DECLARE list CURSOR FOR (SELECT F_ID AS iWarehousingCommodityID FROM t_warehousingcommodity);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iWarehousingCommodityID;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- 检查所有入库单商品表是否有对应的BarcodesID
			SELECT 1 FROM t_barcodes WHERE F_ID IN (SELECT F_BarcodeID FROM t_warehousingcommodity WHERE F_ID = iWarehousingCommodityID);
			-- 如果没有，那么则认为数据不健康
			IF FOUND_ROWS() = 1 THEN
				SET iErrorCode = 0;
				SET sErrorMsg = '';
			ELSE
				SET done = TRUE;
				SET iErrorCode = 7;
				SET sErrorMsg = CONCAT('入库单商品', iWarehousingCommodityID, '没有对应的条形码ID');
			END IF;
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;