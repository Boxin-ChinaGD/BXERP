DROP PROCEDURE IF EXISTS `SPD_Commodity_CheckWarehousing`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_Commodity_CheckWarehousing`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iCommodityID INT;
	DECLARE iType INT;
	DECLARE iNOStart INT;
	DECLARE iPurchasingPriceStart FLOAT;
	DECLARE iNOSalable INT;
	DECLARE iPrice FLOAT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- 临时模板，下面list需要根据需求变化
	DECLARE list CURSOR FOR (SELECT F_ID AS iCommodityID, F_Type AS iType, F_NOStart AS iNOStart, F_PurchasingPriceStart AS iPurchasingPriceStart FROM t_commodity WHERE F_Status <> 2);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iCommodityID, iType, iNOStart, iPurchasingPriceStart;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- 期初商品的期初数量和期初采购价与检查第一张入库单的可售数量和进货价相等
			IF iType = 0 THEN
				IF iNOStart > 0 AND iPurchasingPriceStart > 0 THEN
					SELECT F_NOSalable INTO iNOSalable FROM t_warehousingcommodity WHERE  F_CommodityID = iCommodityID ORDER BY F_ID ASC LIMIT 0,1;
					SELECT F_Price INTO iPrice FROM t_warehousingcommodity WHERE  F_CommodityID = iCommodityID ORDER BY F_ID ASC LIMIT 0,1;
					IF iNOSalable = iNOStart AND iPrice = iPurchasingPriceStart THEN
						SET iErrorCode := 0;
						SET sErrorMsg := '';
					ELSEIF iNOSalable != iNOStart THEN
						SET done := TRUE;
						SET iErrorCode := 7;
						SET sErrorMsg := CONCAT('商品', iCommodityID, '的期初数量和对应入库单的可售数量不相等');
					ELSE
						SET done := TRUE;
						SET iErrorCode := 7;
						SET sErrorMsg := CONCAT('商品', iCommodityID, '的期初采购价和对应入库单的进货价不相等');
					END IF;
				ELSEIF iNOStart = -1 AND iPurchasingPriceStart = -1 THEN
					SET iErrorCode := 0;
					SET sErrorMsg := '';
				ELSE 
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('商品', iCommodityID, '的期初数量和期初采购价不正确，期初数量和期初采购价应该同时为-1或者同时为大于0的数');
				END IF;
			-- 检查商品对应的入库单商品表，如果该商品不是单品，不能存在对应的入库单商品表
			ELSE 
				IF EXISTS(SELECT 1 FROM t_warehousingcommodity WHERE  F_CommodityID = iCommodityID) THEN
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('商品', iCommodityID, '不是单品，不能入库');
				ELSE 
					SET iErrorCode := 0;
					SET sErrorMsg := '';
				END IF;
			END IF;
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;