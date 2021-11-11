DROP PROCEDURE IF EXISTS `SPD_Warehousing_CheckWarehousingCommodity`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_Warehousing_CheckWarehousingCommodity`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	-- ...临时模板，下面的值需要根据需求变化
	DECLARE iID INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- 临时模板，下面list需要根据需求变化
	DECLARE list CURSOR FOR (SELECT F_ID AS iID FROM t_warehousing);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iID;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- 检查所有入库单是否存在入库商品
			-- 如果不存在则认为数据不健康
			IF EXISTS (SELECT 1 FROM t_warehousingcommodity WHERE F_WarehousingID = iID) THEN 
				SET iErrorCode = 0;
				SET sErrorMsg = '';
			ELSE
				SET done = TRUE;
				SET iErrorCode = 7;
				SET sErrorMsg = CONCAT('入库单:', iID, '没有入库商品');
			END IF;
			
			
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;