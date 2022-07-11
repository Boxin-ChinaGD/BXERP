DROP PROCEDURE IF EXISTS `SPD_WarehousingCommodity_CheckCommodity`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_WarehousingCommodity_CheckCommodity`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	-- ...临时模板，下面的值需要根据需求变化
	DECLARE iCommodityID INT;
	DECLARE iWarehousingCommodityID INT;
	DECLARE iNO INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- 临时模板，下面list需要根据需求变化
	DECLARE list CURSOR FOR (SELECT F_ID AS iWarehousingCommodityID, F_CommodityID AS iCommodityID, F_NO AS iNO FROM t_warehousingcommodity);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iWarehousingCommodityID, iCommodityID, iNO;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- 检查所有入库单商品表的商品是不是商品表中的商品
			IF EXISTS (SELECT 1 FROM t_commodity WHERE F_ID = iCommodityID) THEN
				-- 
				-- 如果是则继续检查所有入库单商品表的商品ID是否单品，如果不是单品，那么就认为数据不健康
				IF (SELECT F_Type FROM t_commodity WHERE F_ID = iCommodityID) = 0 THEN
			   	    -- 
			   		-- 如果是则继续检查所有入库单商品表的入库数量是否大于0
			   		IF iNO > 0 THEN
						SET iErrorCode = 0;
						SET sErrorMsg = '';
					-- 如果不是，那么就认为数据不健康
					ELSE
						SET iErrorCode = 7;
						SET sErrorMsg = CONCAT('入库单商品', iWarehousingCommodityID, '入库数量小于1');
						SET done = TRUE;
					END IF;
			   		
				ELSE
					SET iErrorCode = 7;
					SET sErrorMsg = CONCAT('入库单商品', iWarehousingCommodityID, '不是单品');
					SET done = TRUE;
				END IF;
			ELSE
				SET done = TRUE;
				SET iErrorCode = 7;
				SET sErrorMsg = CONCAT('入库单商品', iWarehousingCommodityID, '不是商品表中的商品');
			END IF;
			
			
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;