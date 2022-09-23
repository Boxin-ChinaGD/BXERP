DROP PROCEDURE IF EXISTS `SPD_Commodity_CheckReturnCommoditySheet`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_Commodity_CheckReturnCommoditySheet`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iCommodityID INT;
	DECLARE iType INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- 临时模板，下面list需要根据需求变化
	DECLARE list CURSOR FOR (SELECT F_ID AS iCommodityID, F_Type AS iType FROM t_commodity WHERE F_Status <> 2);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iCommodityID, iType;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- 商品类型为组合商品不能被退货
			-- 商品类型为服务商品不能被退货
			IF EXISTS(SELECT 1 FROM t_returncommoditysheetcommodity WHERE F_CommodityID = iCommodityID) AND iType = 1 THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('商品', iCommodityID, '是组合商品，不能退货');
			ELSEIF EXISTS(SELECT 1 FROM t_returncommoditysheetcommodity WHERE F_CommodityID = iCommodityID) AND iType = 3 THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('商品', iCommodityID, '是服务商品，不能退货');
			ELSE 
			-- 商品可能没有退货单；组合商品和服务商品不能退货,其他类型的商品能退货
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			END IF;
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;