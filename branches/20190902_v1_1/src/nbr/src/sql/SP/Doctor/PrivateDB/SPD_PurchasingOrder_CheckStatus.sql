DROP PROCEDURE IF EXISTS `SPD_PurchasingOrder_CheckStatus`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_PurchasingOrder_CheckStatus`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iPurchasingorderID INT;
	DECLARE iStatus INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	DECLARE list CURSOR FOR (SELECT F_ID AS iPurchasingorderID FROM t_purchasingorder);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
	  
			FETCH list INTO iPurchasingorderID;
			IF done THEN
				LEAVE read_loop;
			END IF;
			-- 
			-- 检查所有的采购订单的状态只能为0，1,2,3,4
			-- 如果不是0~4之间，那么则认为数据不健康
			SELECT F_Status INTO iStatus FROM t_purchasingorder WHERE F_ID = iPurchasingorderID;
			IF iStatus IN (0, 1, 2, 3, 4) THEN
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			ELSE
				SET done = TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('采购订单', iPurchasingorderID ,'的状态不在0~4之间');
			END IF;

		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;