DROP FUNCTION IF EXISTS Func_DeleteRetailTradeCommoditySource;
-- ... 业务逻辑已经改变，本函数命名需要改变为Func_GenerateReturnRetailTradeCommodityDestination，注释：退货时，根据来源表生成去向表
CREATE DEFINER=`root`@`localhost` FUNCTION `Func_DeleteRetailTradeCommoditySource`(
	iCommodityID INT,					-- 要退哪个商品
	returnNO INT, 						-- 退货商品数量 
	canReturnNO INT, 					-- 可退货数量
	currentWarehousingID INT, 			-- 当值入库ID
	retailTradeCommodityID INT 			-- 退货单源单(零售单)的从表ID，即t_retailtradecommoditysource的父表ID
) RETURNS int(11)
BEGIN
	DECLARE iID INT;
	DECLARE iShopID INT;
	DECLARE NO INT;
	DECLARE sourceNO INT;
	DECLARE noSalable INT; 				-- 可售商品数量
	DECLARE warehousingNO INT;
	DECLARE warehousingID INT;
	DECLARE iIncreasingCommodityID INT; 	-- 退货商品ID,用于插入退货商品去向表
	DECLARE iReturnwarehousingID INT;		-- 入库单ID,用于插入退货商品去向表
	DECLARE iRetailTradeCommodityID INT;		-- 退货单从表的ID,用于插入退货商品去向表
	
	DECLARE done INT DEFAULT FALSE;		-- 创建结束标志变量
	
	-- 遍历入库了iCommodityID的入库表
	DECLARE list CURSOR FOR (SELECT 
		F_ID AS iID, 
		F_NO AS warehousingNO,
		F_NOSalable AS noSalable, 
		F_WarehousingID AS warehousingID
	FROM t_warehousingcommodity wc WHERE F_CommodityID = iCommodityID 
	AND (SELECT F_Status FROM t_warehousing WHERE F_ID = wc.F_WarehousingID ) = 1 -- =1为已审核状态
	AND (CASE ISNULL(currentWarehousingID) 
			WHEN 1 THEN 
				1=1 
			ELSE 
				F_ID <= (SELECT F_ID FROM t_warehousingcommodity WHERE F_WarehousingID = currentWarehousingID AND F_CommodityID = iCommodityID) 
			END)
	ORDER BY F_ID DESC
	);
	
	-- 遍历对应的来源表
	DECLARE listSource CURSOR FOR (
		SELECT 
			F_reducingCommodityID AS iIncreasingCommodityID, 
			F_NO AS iSourceNO,
			F_WarehousingID AS iReturnwarehousingID
		FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = retailTradeCommodityID AND F_ReducingCommodityID = iCommodityID
		ORDER BY F_ID DESC
	);
	
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  -- 指定游标循环结束时返回值

	
	SET NO := returnNO;
	
	SELECT F_ShopID INTO iShopID FROM t_retailtrade WHERE F_ID = (SELECT F_TradeID FROM t_retailtradecommodity WHERE F_ID = retailTradeCommodityID);
	
	IF NO > canReturnNO THEN
		RETURN (7);
	ELSEIF EXISTS(SELECT 1 FROM t_warehousingcommodity WHERE F_CommodityID = iCommodityID) THEN
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iID, warehousingNO, noSalable, warehousingID;
	
			IF done THEN
		  		LEAVE read_loop;
			END IF;
			
			-- 如果(退货数量+入库单商品可售数量)小于入库数量，直接Update入库单商品的入库数量和商品的当值入库ID。
			IF (NO + noSalable) <= warehousingNO THEN
				UPDATE t_warehousingcommodity SET F_NOSalable = NO + noSalable WHERE F_WarehousingID = warehousingID AND F_CommodityID = iCommodityID;  
				
				UPDATE t_commodityshopinfo SET F_CurrentWarehousingID = warehousingID WHERE F_CommodityID = iCommodityID AND F_ShopID = iShopID;
				
				SET NO := 0; 
					
				LEAVE read_loop;
			-- 如果(退货数量+入库单商品可售数量)大于入库数量，先计算出溢出值，然后更新入库单商品可售数量为入库数量。
			ELSE 
				SET NO := (NO + noSalable) - warehousingNO;  
				
				UPDATE t_warehousingcommodity SET F_NOSalable = warehousingNO WHERE F_WarehousingID = warehousingID AND F_CommodityID = iCommodityID;
			END IF;
				
		END LOOP read_loop;
		CLOSE list;
		
		-- 如果循环后退货商品数量>0 则Update最后一张入库单的可售数量
		IF NO > 0 THEN
			UPDATE t_warehousingcommodity wc SET F_NOSalable = F_NOSalable + NO 
			WHERE F_CommodityID = iCommodityID
			AND (SELECT F_Status FROM t_warehousing WHERE F_ID = wc.F_WarehousingID ) = 1 
			ORDER BY F_ID LIMIT 1;
			
			UPDATE t_commodityshopinfo SET F_CurrentWarehousingID = 
				(SELECT F_WarehousingID FROM t_warehousingcommodity wc
				WHERE F_CommodityID = iCommodityID 
				AND (SELECT F_Status FROM t_warehousing WHERE F_ID = wc.F_WarehousingID ) = 1  
				ORDER BY F_ID LIMIT 1) 
			WHERE F_CommodityID = iCommodityID AND F_ShopID = iShopID;
		END IF;
	END IF;
	
	-- 当商品退货时，往T_ReturnRetailTradeCommodityDestination插入数据，记录商品退货的数量，及来自那张入库单
	SET NO := returnNO;
	SET done := FALSE; -- 初始化done
	OPEN listSource;
		read_loop: LOOP
			FETCH listSource INTO iIncreasingCommodityID, sourceNO, iReturnwarehousingID;
			
			IF done THEN
		  		LEAVE read_loop;
			END IF;

			-- 如果退货数量 - 来源表的数量 > 0 代表第一张来源表数量退完，需要对第二种来源表进行退货
			-- 举例：当存在入库单1，商品5件;入库单2，商品5件。售卖10件，退货8件; 第一次：8 - 5 = 3 > 0，第二次：3 - 5 = -2 < 0。 
			-- 或者退10件，第一次：10 - 5 = 5 > 0，第二次：5 - 5 = 0 = 0
			-- 注意：F_RetailTradeCommodityID是退货单的从表ID
			
			-- 使用零售单源单(零售单)的从表的ID 查找到退货单从表的id
			SELECT F_ID INTO iRetailTradeCommodityID
			FROM t_retailtradecommodity WHERE F_CommodityID = (SELECT F_CommodityID FROM t_retailtradecommodity WHERE F_ID = retailTradeCommodityID) 
			AND F_TradeID = (
				SELECT F_ID FROM t_retailtrade WHERE F_SourceID = ( -- 找到退货单的ID
					SELECT F_TradeID 
					FROM t_retailtradecommodity 
					WHERE F_ID = retailTradeCommodityID
				) 
			);
			
--			IF iRetailTradeCommodityID IS NOT NULL THEN 
				IF (NO - sourceNO) > 0 THEN 
					INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
					VALUES (iRetailTradeCommodityID, iIncreasingCommodityID, sourceNO, iReturnwarehousingID);
					
					SET NO := NO - sourceNO;
				ELSE 
				 	-- 当退货数量 - 来源表的数量 <= 0 
					INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
					VALUES (iRetailTradeCommodityID, iIncreasingCommodityID, NO, iReturnwarehousingID);
					
					LEAVE read_loop;
				END IF; 
--			END IF; 
				
		END LOOP read_loop;
		CLOSE listSource;

	RETURN (0);
END;