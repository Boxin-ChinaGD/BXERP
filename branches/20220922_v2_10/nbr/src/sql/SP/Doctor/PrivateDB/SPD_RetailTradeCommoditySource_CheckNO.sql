DROP PROCEDURE IF EXISTS `SPD_RetailTradeCommoditySource_CheckNO`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_RetailTradeCommoditySource_CheckNO`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iRetailTradeCommodityID INT;
	DECLARE iReducingCommodityID INT;
	DECLARE iNO INT;
	DECLARE iID INT;
	DECLARE iType INT;
	DECLARE iMultiple INT;
	DECLARE iReturnNO INT;
	DECLARE sGroup_concatID VARCHAR(20);
	DECLARE done INT DEFAULT FALSE;
	
	DECLARE list CURSOR FOR (SELECT F_ID AS iID, F_RetailTradeCommodityID AS iRetailTradeCommodityID, F_ReducingCommodityID AS iReducingCommodityID FROM t_retailtradecommoditysource);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iID, iRetailTradeCommodityID, iReducingCommodityID;
			IF done THEN
				LEAVE read_loop;
			END IF;
			   
			-- 检查零售单商品来源有没有对应的零售单商品
			-- 如果没有，那么就认为数据不健康
			IF NOT EXISTS(SELECT 1 FROM t_retailtradecommodity WHERE F_ID = iRetailTradeCommodityID) THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('ID为', iID, '的零售单商品来源没有对应的零售单商品');
			-- 检查零售单商品有没有对应的零售单
			-- 如果没有，那么就认为数据不健康
			ELSEIF NOT EXISTS(SELECT 1 FROM t_retailtrade WHERE F_ID = (SELECT F_TradeID FROM t_retailtradecommodity WHERE F_ID = iRetailTradeCommodityID)) THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('ID为', iID, '的零售单商品来源没有对应的零售单');
			-- 检查零售单商品是否存在商品表中
			-- 如果不存在，那么就认为数据不健康
			ELSEIF NOT EXISTS(SELECT 1 FROM t_commodity WHERE F_ID = iReducingCommodityID) THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('ID为', iID, '的零售单商品来源没有对应的商品');
			ELSE
				-- 获取零售单商品的类型(普通商品、组合商品、多包装商品、服务商品)
				-- 如果iType的值并不是0，1，2、3中的值，那么就认为数据不健康
				SELECT F_Type INTO iType FROM t_commodity WHERE F_ID IN (SELECT F_CommodityID FROM t_retailtradecommodity WHERE F_ID = iRetailTradeCommodityID);
				-- 拼接来源表id
				SELECT group_concat(F_ID) INTO sGroup_concatID FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = iRetailTradeCommodityID;
				IF iType = 0 THEN
					-- 由于现在有退货去向表的存在，所以退货时不再需要检查来源表
					-- 检查普通商品的库存是否正确
					IF NOT EXISTS(SELECT 1 FROM t_retailtradecommodity WHERE F_ID = iRetailTradeCommodityID AND F_NO = (SELECT sum(F_NO) FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = iRetailTradeCommodityID)
					) THEN				   
						SET done := TRUE;
						SET iErrorCode := 7;
						SET sErrorMsg := CONCAT('ID为', sGroup_concatID, '的零售单商品来源的库存跟相应的零售单商品的库存不相等');
					END IF;
				ELSEIF iType = 1 THEN 
				    -- 获取倍数
				    SELECT F_SubCommodityNO INTO iMultiple FROM t_subcommodity WHERE F_CommodityID = (SELECT F_CommodityID FROM t_retailtradecommodity WHERE F_ID = iRetailTradeCommodityID) AND F_SubCommodityID = iReducingCommodityID;
					-- 检查组合商品的库存是否正确
					IF NOT EXISTS(SELECT 1 FROM t_retailtradecommodity WHERE F_ID = iRetailTradeCommodityID AND F_NO * iMultiple = 
						(SELECT sum(F_NO) FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = iRetailTradeCommodityID AND F_ReducingCommodityID = iReducingCommodityID)
					) THEN 
						SET done := TRUE;
						SET iErrorCode := 7;
						SET sErrorMsg := CONCAT('ID为', sGroup_concatID, '的零售单商品来源的库存跟相应的零售单商品的库存不相等');
					END IF;
				ELSEIF iType = 2 THEN 
					-- 获取倍数
				    SELECT sum(F_RefCommodityMultiple) INTO iMultiple FROM t_commodity WHERE F_ID = (SELECT F_CommodityID FROM t_retailtradecommodity WHERE F_ID = iRetailTradeCommodityID);
					-- 检查是否存在退货,如果存在退货就先减去已退货的库存
					-- 检查组合商品的库存是否正确
					IF NOT EXISTS(SELECT 1 FROM t_retailtradecommodity WHERE F_ID = iRetailTradeCommodityID AND F_NO * iMultiple = (SELECT sum(F_NO) FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = iRetailTradeCommodityID)) THEN 
						SET done := TRUE;
						SET iErrorCode := 7;
						SET sErrorMsg := CONCAT('ID为', sGroup_concatID, '的零售单商品来源的库存跟相应的零售单商品的库存不相等');
				    END IF;
				ELSEIF iType = 3 THEN 
					-- 检查服务商品的库存是否正确
					IF NOT EXISTS(SELECT 1 FROM t_retailtradecommodity WHERE F_ID = iRetailTradeCommodityID AND F_NO = (SELECT sum(F_NO) FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = iRetailTradeCommodityID)) THEN 
						SET done := TRUE;
						SET iErrorCode := 7;
						SET sErrorMsg := CONCAT('ID为', iID, '的零售单商品来源的库存跟相应的零售单商品的库存不相等');
					END IF;
				ELSE 
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('ID为', iID, '的零售单商品来源对应的商品的类型不正确');
				END IF;
				
			END IF;
		
		-- 检查零售单商品来源的库存是否跟对应的零售单商品的库存是否相等(普通商品、组合商品、多包装商品)
		-- 如果不存在，那么就认为数据不健康
		
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;