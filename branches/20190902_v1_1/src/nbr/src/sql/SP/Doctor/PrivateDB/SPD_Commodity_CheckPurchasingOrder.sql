DROP PROCEDURE IF EXISTS `SPD_Commodity_CheckPurchasingOrder`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_Commodity_CheckPurchasingOrder`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iCommodityID INT;
	DECLARE iType INT;
	DECLARE sPurchasingUnit VARCHAR(16);
	DECLARE done INT DEFAULT FALSE;
	-- 
	DECLARE list CURSOR FOR (SELECT F_ID AS iCommodityID, F_PurchasingUnit AS sPurchasingUnit, F_Type AS iType FROM t_commodity WHERE F_Status <> 2);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iCommodityID, sPurchasingUnit, iType;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- 服务商品、组合商品和多包装商品不能被采购
			IF sPurchasingUnit IS NOT NULL AND iType <> 0 THEN 
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT(IF(iType = 1, '组合商品', IF(iType = 2, '多包装商品', '服务商品')), iCommodityID, '不能有采购单位');
			END IF;
			
			IF done = FALSE THEN -- 如果上面的终止标识为true，那么将不运行下面的代码
			
				-- 如果商品的采购单位为空，那么该商品不能存在采购订单
				IF sPurchasingUnit IS NULL THEN 
					IF NOT EXISTS(SELECT 1 FROM t_purchasingordercommodity WHERE F_CommodityID = iCommodityID) THEN 
						SET iErrorCode := 0;
						SET sErrorMsg := '';
					ELSE
						SET done := TRUE;
						SET iErrorCode := 7;
						SET sErrorMsg := CONCAT('商品', iCommodityID ,'不能被采购订单引用');
					END IF;
				ELSE
					IF EXISTS(SELECT 1 FROM t_purchasingordercommodity WHERE F_CommodityID = iCommodityID)  THEN 
						SET iErrorCode := 0;
						SET sErrorMsg := '';
					ELSE
						SET done := TRUE;
						SET iErrorCode := 7;
						SET sErrorMsg := CONCAT('商品', iCommodityID ,'需要有对应的采购订单');
					END IF;
				END IF;
			END IF;
			
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;