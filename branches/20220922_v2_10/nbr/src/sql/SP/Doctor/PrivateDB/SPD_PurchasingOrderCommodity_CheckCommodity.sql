DROP PROCEDURE IF EXISTS `SPD_PurchasingOrderCommodity_CheckCommodity`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_PurchasingOrderCommodity_CheckCommodity`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iPurchasingOrderCommodityID INT;
	DECLARE iStatus INT;
	DECLARE iType INT;
	DECLARE iNO INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
    DECLARE list CURSOR FOR (SELECT F_ID AS iPurchasingOrderCommodityID FROM t_purchasingordercommodity);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
		
			FETCH list INTO iPurchasingOrderCommodityID;
			IF done THEN
				LEAVE read_loop;
			END IF;
			-- 
			-- 检查所有采购订单中商品的商品存在于商品表中
			-- 如果不存在，则认为数据不健康
			SELECT 1 FROM t_commodity WHERE F_ID IN (SELECT F_CommodityID FROM t_purchasingordercommodity WHERE F_ID = iPurchasingOrderCommodityID);
			-- 
			IF found_rows() > 0 THEN
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			ELSE
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('采购订单商品', iPurchasingOrderCommodityID, '没有对应的商品ID');
			   	LEAVE read_loop;
			END IF;
			-- 	
			-- 检查所有采购订单中的商品不能为多包装商品，组合商品，已删除商品和服务商品
			-- 如果为已删除的商品，那么则认为数据不健康
			SELECT F_Status INTO iStatus FROM t_commodity WHERE F_ID IN (SELECT F_CommodityID FROM t_purchasingordercommodity WHERE F_ID = iPurchasingOrderCommodityID);
			-- 
			IF iStatus = 2 THEN 
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('采购订单商品', iPurchasingOrderCommodityID, '对应的商品不能为已删除商品');
			  	LEAVE read_loop;
			ELSE 
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			END IF;
			-- 
		  	SELECT F_Type INTO iType FROM t_commodity WHERE F_ID IN (SELECT F_CommodityID FROM t_purchasingordercommodity WHERE F_ID = iPurchasingOrderCommodityID);
			IF iType = 2 THEN
				SET done = TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('采购订单商品', iPurchasingOrderCommodityID, '对应的商品不能为多包装商品');
			   	LEAVE read_loop;
		  	ELSEIF iType = 1 THEN
				SET done = TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('采购订单商品', iPurchasingOrderCommodityID, '对应的商品不能为组合型商品');
			 	LEAVE read_loop;
			ELSEIF iType = 3 THEN
				SET done = TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('采购订单商品', iPurchasingOrderCommodityID, '对应的商品不能为服务型商品');
			 	LEAVE read_loop;
			ELSE 
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			END IF;
			-- 检查所有采购商品表中的商品数量，必须全部大于0
			-- 如果不大于0，则认为数据不健康
			SELECT F_CommodityNO INTO iNO FROM t_purchasingordercommodity WHERE F_ID = iPurchasingOrderCommodityID;
			IF iNO > 0 THEN
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			ELSE 
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('采购订单商品', iPurchasingOrderCommodityID, '的商品数量必须大于0');
			    LEAVE read_loop;
			END IF;

		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;