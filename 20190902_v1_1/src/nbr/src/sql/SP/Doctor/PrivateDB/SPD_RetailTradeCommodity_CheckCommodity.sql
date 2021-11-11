DROP PROCEDURE IF EXISTS `SPD_RetailTradeCommodity_CheckCommodity`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_RetailTradeCommodity_CheckCommodity`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	-- ...临时模板，下面的值需要根据需求变化
	DECLARE iID INT;
	DECLARE iNO INT;
	DECLARE iCommodityID INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- 临时模板，下面list需要根据需求变化
	DECLARE list CURSOR FOR (SELECT F_ID AS iID, F_CommodityID AS iCommodityID, F_NO AS iNO FROM t_retailtradecommodity);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iID, iCommodityID, iNO;
			IF done THEN
				LEAVE read_loop;
			END IF;
		
		-- 检查所有零售单商品表的商品数量是否大于0
		-- 如果不是，那么就认为数据不健康
		IF iNO <=0 THEN 
			SET done := TRUE;
			SET iErrorCode := 7;
			SET sErrorMsg := CONCAT('ID为', iID, '的零售单商品的库存必须大于0');
		-- 检查所有零售单商品表的商品是不是商品表中的商品
		-- 如果不是，那么就认为数据不健康
		ELSEIF NOT EXISTS(SELECT 1 FROM t_commodity WHERE F_ID = iCommodityID) THEN 
			SET done := TRUE;
			SET iErrorCode := 7;
			SET sErrorMsg := CONCAT('ID为', iID, '的零售单商品对应的商品不是商品表中的商品');
		ELSE
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
		
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;