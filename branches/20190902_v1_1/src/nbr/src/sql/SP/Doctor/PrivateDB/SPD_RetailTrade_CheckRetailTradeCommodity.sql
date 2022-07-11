DROP PROCEDURE IF EXISTS `SPD_RetailTrade_CheckRetailTradeCommodity`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_RetailTrade_CheckRetailTradeCommodity`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iRT_ID INT; -- 零售单ID
	DECLARE iSourceID INT; -- 零售退货单相应的零售单ID
	DECLARE iRTC_noSold INT; -- 零售单的售卖数量
	DECLARE iRTC_noReturn INT; -- 零售退货单的退货数量
	DECLARE iReturn INT; -- 零售退货单数量
	DECLARE iRTC_noCanReturn INT; -- 零售单商品和零售退货商品的可退货数量
	DECLARE iAmount DECIMAL(20,6); -- 零售单应收价
	DECLARE iSumPriceReturn DECIMAL(20,6); -- 零售单对应所有零售单商品的总退货价
	DECLARE iSumPriceReturn_ReturnRetailTrade DECIMAL(20,6);
	DECLARE dTolerance DECIMAL(20,6) DEFAULT 0.0001;
	DECLARE done INT DEFAULT FALSE;
	
	DECLARE list CURSOR FOR (
		SELECT F_ID AS iRT_ID, F_SourceID AS iSourceID FROM t_retailtrade); -- 遍历零售单
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
			FETCH list INTO iRT_ID, iSourceID;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- 获取零售单的金额和其相应的零售单商品金额的总和
			SELECT F_Amount INTO iAmount FROM t_retailtrade WHERE F_ID = iRT_ID;
			SELECT sum(F_PriceReturn * F_NO) INTO iSumPriceReturn FROM t_retailtradecommodity WHERE F_TradeID = iRT_ID;


			-- 判断是零售单还是零售退货单
			IF iSourceID = -1 THEN -- 零售单
				-- 获取可退货数量
				SELECT sum(F_NOCanReturn) INTO iRTC_noCanReturn FROM t_retailtradecommodity WHERE F_TradeID = iRT_ID;
				
				-- 获取零售单的退货单数量
				SELECT count(*) INTO iReturn FROM t_retailtrade WHERE F_SourceID = iRT_ID;
				
				-- 获取零售单商品的售卖数量
				SELECT sum(F_NO) INTO iRTC_noSold FROM t_retailtradecommodity WHERE F_TradeID = iRT_ID;
				
				-- 可退货数量必须小于或等于零售数量
				IF IFNULL(iRTC_noCanReturn, 0) > IFNULL(iRTC_noSold, 0) THEN 
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('ID为', iRT_ID, '的零售单的可退货数量大于零售数量');
				-- 零售单有多张零售退货单
				ELSEIF iReturn > 1 THEN 
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('ID为', iRT_ID, '的零售单有多张零售退货单');
				-- 零售单必须要有从表，如果零售单不存在从表则认为数据不健康
				ELSEIF NOT EXISTS(SELECT 1 FROM t_retailtradecommodity WHERE F_TradeID = iRT_ID) THEN
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('ID为', iRT_ID, '的零售单没有相应的零售单商品');
				-- 零售数量不能<=0
				ELSEIF IFNULL(iRTC_noSold,0) <= 0 THEN  
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('ID为', iRT_ID, '的零售单的零售数量必须大于0');
				ELSEIF iSumPriceReturn < 0 THEN -- 零售单从表的总金额必须大于或等于0(存在零元的商品)
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('ID为', iRT_ID, '的零售单从表的总金额必须大于或等于0');
				ELSEIF iAmount < 0 THEN -- 零售单的金额必须大于或等于0
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('ID为', iRT_ID, '的零售单的金额必须大于或等于0');
				ELSEIF abs(abs(IFNULL(iAmount,0)) - abs(IFNULL(iSumPriceReturn,0))) >= dTolerance THEN -- 
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('ID为', iRT_ID, '的零售单的应收价与其所有零售单商品的退货价总和不相等');
				-- 零售单商品有从表，如果零售单商品不存在从表则认为数据不健康
				ELSEIF NOT EXISTS(SELECT 1 FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID IN (
						SELECT F_ID FROM t_retailtradecommodity WHERE F_TradeID = iRT_ID)) THEN 
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('零售单ID为', iRT_ID, '的零售单商品没有相应的零售单商品来源');
				-- 零售单不能有去向表
				ELSEIF EXISTS(SELECT 1 FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID IN (
							SELECT F_ID FROM t_retailtradecommodity WHERE F_TradeID = iRT_ID)) THEN 
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('ID为', iRT_ID, '的零售单不能有零售单商品退货去向表');
				ELSEIF iReturn = 1 THEN -- 零售单有退货
					-- 获取退货数量
					SELECT sum(F_NO) INTO iRTC_noReturn FROM t_retailtradecommodity WHERE F_TradeID = (SELECT F_ID FROM t_retailtrade WHERE F_SourceID = iRT_ID);
					-- 判断退货数量是否大于零售数量
					IF IFNULL(iRTC_noReturn, 0) > IFNULL(iRTC_noSold, 0) THEN 
						SET done := TRUE;
						SET iErrorCode := 7;
						SET sErrorMsg := CONCAT('原零售单ID为', iRT_ID, '的零售退货单的退货数量大于原零售单的零售数量');
					-- 退货数量不能为0
					ELSEIF IFNULL(iRTC_noReturn,0) <= 0 THEN 
						SET done := TRUE;
						SET iErrorCode := 7;
						SET sErrorMsg := CONCAT('原零售单ID为', iRT_ID, '的零售退货单的退货数量必须大于0');
					-- 判断是否完全退货
					ELSEIF IFNULL(iRTC_noSold,0) != IFNULL(iRTC_noReturn,0) THEN -- 部分退货
						-- 检查可退货数量
						IF IFNULL((iRTC_noSold - iRTC_noReturn),0) != IFNULL(iRTC_noCanReturn, 0) THEN 
							SET done := TRUE;
							SET iErrorCode := 7;
							SET sErrorMsg := CONCAT('ID为', iRT_ID, '的零售单退货后，可退货数量不正常');
						END IF;
					ELSE -- 完全退货
						SELECT sum(F_PriceReturn * F_NO) INTO iSumPriceReturn_ReturnRetailTrade FROM t_retailtradecommodity WHERE F_TradeID = (SELECT F_ID FROM t_retailtrade WHERE F_SourceID= iRT_ID);
						-- 完全退货时，可退货数量必须为0
						IF IFNULL(iRTC_noCanReturn, 0) != 0 THEN 
							SET done := TRUE;
							SET iErrorCode := 7;
							SET sErrorMsg := CONCAT('ID为', iRT_ID, '的零售单已经完全退货，可退货数量必须为0');
						ELSEIF iSumPriceReturn_ReturnRetailTrade < 0 THEN 
							SET done := TRUE;
							SET iErrorCode := 7;
							SET sErrorMsg := CONCAT('原零售单ID为', iRT_ID, '的零售退货单从表的总金额必须大于或等于0');
						ELSEIF abs(abs(IFNULL(iSumPriceReturn,0)) - abs(IFNULL(iSumPriceReturn_ReturnRetailTrade,0))) >= dTolerance THEN
							SET done := TRUE;
							SET iErrorCode := 7;
							SET sErrorMsg := CONCAT('ID为', iRT_ID, '的零售单已经完全退货，其金额跟相应的零售退货单的金额不相等');
						END IF;
					END IF;
				END IF;
			ELSE -- 零售退货单
				-- 获取零售退货单的原零售单数量
				SELECT count(*) INTO iReturn FROM t_retailtrade WHERE F_ID = iSourceID;
				
				IF iAmount < 0 THEN -- 零售退货单的金额必须大于或等于0
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('ID为', iRT_ID, '的零售退货单的金额必须大于或等于0');
				ELSEIF iSumPriceReturn < 0 THEN -- 零售退货单从表的总金额必须大于或等于0
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('ID为', iRT_ID, '的零售退货单从表的总金额必须大于或等于0');
				-- 判断该单的Amount和其所有从表的退货价总和是否相等
				ELSEIF abs(abs(IFNULL(iAmount,0)) - abs(IFNULL(iSumPriceReturn,0))) >= dTolerance THEN -- 
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('ID为', iRT_ID, '的零售退货单的应收价与其所有零售单商品的退货价总和不相等');
				-- 一张零售退货单只有一张原零售单,如果一张零售退货单不存在原零售单则认为数据不健康
				ELSEIF iReturn = 0 THEN 
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('ID为', iRT_ID, '的零售退货单没有相应的原零售单');
				-- 零售退货单必须要有从表，如果没有则认为数据不健康
				ELSEIF NOT EXISTS(SELECT 1 FROM t_retailtradecommodity WHERE F_TradeID = iRT_ID) THEN  
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('ID为', iRT_ID, '的零售退货单没有相应的零售退货单商品');
				-- 零售退货单商品没有零售单商品来源表，如果有则认为数据不健康
				ELSEIF EXISTS(SELECT 1 FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID IN (
						SELECT F_ID FROM t_retailtradecommodity WHERE F_TradeID = iRT_ID)) THEN 
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('零售退货单ID为', iRT_ID, '的零售退货单商品不能有零售退货单商品来源');
				-- 零售退货单商品有零售单商品退货去向表，如果没有则认为数据不健康
				ELSEIF NOT EXISTS(SELECT 1 FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID IN (
							SELECT F_ID FROM t_retailtradecommodity WHERE F_TradeID = iRT_ID)) THEN 
						SET done := TRUE;
						SET iErrorCode := 7;
						SET sErrorMsg := CONCAT('ID为', iRT_ID, '的零售退货单没有相应的零售单商品退货去向表');
				END IF;
			END IF;
				
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;