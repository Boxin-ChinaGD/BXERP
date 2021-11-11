DROP PROCEDURE IF EXISTS `SPD_PurchasingOrder_CheckStaffID`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_PurchasingOrder_CheckStaffID`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iPurchasingorderID INT;
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
			-- 检查所有的采购订单的staffID不能为空，检查采购订单的staffID是否存在Staff表中
			SELECT 1 FROM t_staff WHERE F_ID IN (SELECT F_StaffID FROM t_purchasingorder WHERE F_ID = iPurchasingorderID);
			-- 
			IF FOUND_ROWS() > 0 THEN
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			ELSE 
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('采购订单', iPurchasingorderID ,'没有对应的staffID');
			END IF;

		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;