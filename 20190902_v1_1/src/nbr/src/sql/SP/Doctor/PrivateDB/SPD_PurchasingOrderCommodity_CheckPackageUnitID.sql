DROP PROCEDURE IF EXISTS `SPD_PurchasingOrderCommodity_CheckPackageUnitID`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_PurchasingOrderCommodity_CheckPackageUnitID`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iPurchasingOrderCommodityID INT;
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
			-- 检查所有的采购订单的PackageUnitID,必须存在包装单位表中
			-- 如果不存在，则认为数据不健康
			SELECT 1 FROM t_packageunit WHERE F_ID IN (SELECT F_PackageUnitID FROM t_purchasingordercommodity WHERE F_ID = iPurchasingOrderCommodityID);
			-- 
			IF found_rows() > 0 THEN
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			ELSE
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('采购订单商品', iPurchasingOrderCommodityID, '没有对应的包装单位ID');
			END IF;

		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;