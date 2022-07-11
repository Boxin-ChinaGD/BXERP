DROP PROCEDURE IF EXISTS `SPD_PurchasingOrder_CheckPurchasingOrderCommodity`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_PurchasingOrder_CheckPurchasingOrderCommodity`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iID INT;
	DECLARE iStatus INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
    DECLARE list CURSOR FOR (SELECT F_ID AS iID, F_Status AS iStatus FROM t_purchasingorder);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
		
			FETCH list INTO iID, iStatus;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- 检查所有采购订单的采购订单商品存不存在
			-- 如果不存在，则认为数据不健康 
			IF iStatus IN (0,1,2,3) THEN
				IF NOT EXISTS(SELECT 1 FROM t_purchasingordercommodity WHERE F_PurchasingOrderID = iID) THEN
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('采购订单:', iID, '没有采购商品');
				   	LEAVE read_loop;
				ELSE
					SET iErrorCode := 0;
					SET sErrorMsg := '';
				END IF;
			ELSE -- 为删除状态的采购订单可以没有采购商品
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			END IF;
			-- 	

		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;