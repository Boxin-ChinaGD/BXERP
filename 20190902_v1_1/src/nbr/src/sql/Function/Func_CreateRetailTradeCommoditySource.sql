-- 1.根据商品ID 从商品表获取当值入库ID
-- 2.对比该入库单商品可售数量和售出商品数量
-- 2.1 如果可售数量大于售出商品数量，则直接创建来源表，入库单商品减去相应的可售数量
-- 2.2.1 如果可售数量小于售出商品数量，则先创建一张来源表，来源表的数量等于当前零售单商品可售出数量。这是跨库的情况。
-- 2.2.2 查询该商品的下一张入库单，判断可售数量和剩余售出商品数量
-- 2.2.3 如果可售数量大于售出商品数量，重复2.1，Update商品表的当值入库ID。否则重复2.2.1 - 2.2.3
drop function IF EXISTS Func_CreateRetailTradeCommoditySource;
CREATE DEFINER=`root`@`localhost` FUNCTION `Func_CreateRetailTradeCommoditySource`(
	iCommodityID INT,
	saleNO INT, -- 卖出商品数量 
	currentWarehousingID INT, -- 当值入库ID
	retailTradeCommodityID INT
) RETURNS int(11)
BEGIN
	DECLARE iID INT;
	DECLARE iShopID INT;
	DECLARE iSaleNOCloned INT;
	DECLARE iWarehousingID INT;
	DECLARE lastInsertID INT;
	DECLARE noSalable INT; -- 可售商品数量
	DECLARE warehousingID INT;
	DECLARE done INT DEFAULT FALSE;	-- 创建结束标志变量
	DECLARE list CURSOR FOR (SELECT 
		F_ID AS iID, 
		F_NOSalable AS noSalable, 
		F_WarehousingID AS warehousingID
	FROM t_warehousingcommodity wc WHERE F_CommodityID = iCommodityID 
	AND (SELECT F_Status FROM t_warehousing WHERE F_ID = wc.F_WarehousingID ) = 1 
	AND (CASE ISNULL(currentWarehousingID) 
			WHEN 1 THEN 
				1=1 
			ELSE 
				F_ID >= (SELECT F_ID FROM t_warehousingcommodity WHERE F_WarehousingID = currentWarehousingID AND F_CommodityID = iCommodityID LIMIT 1) 
			END)
	);
	
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  -- 指定游标循环结束时返回值
	
	SET iSaleNOCloned := saleNO;
	SELECT F_ShopID INTO iShopID FROM t_retailtrade WHERE F_ID = (SELECT F_TradeID FROM t_retailtradecommodity WHERE F_ID = retailTradeCommodityID);
	
	IF EXISTS(SELECT 1 FROM t_warehousingcommodity wc WHERE F_CommodityID = iCommodityID AND (SELECT F_Status FROM t_warehousing WHERE F_ID = wc.F_WarehousingID ) = 1 ) THEN
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iID, noSalable, warehousingID; -- noSalable可能是负数，代表卖出的商品多于入库的商品，此时最后一次入库的可售数量是负数
			IF done THEN
		  		LEAVE read_loop;
			END IF;
			-- 如果当值入库ID的可售数量小于0在下面进行相减的时候导致数据计算不正确.
			IF noSalable > 0 THEN
				-- 如果可售数量大于或等于售出商品数量，则直接创建来源表，入库单商品减去相应的可售数量，Update商品的当值入库ID
				IF noSalable >= saleNO THEN
					INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID)
					VALUES (retailTradeCommodityID, saleNO, warehousingID, iCommodityID);
					
					UPDATE t_warehousingcommodity SET F_NOSalable = F_NOSalable - saleNO WHERE F_WarehousingID = warehousingID AND F_CommodityID = iCommodityID;  
					
					UPDATE t_commodityshopinfo SET F_CurrentWarehousingID = warehousingID WHERE F_CommodityID = iCommodityID AND F_ShopID = iShopID;
					
					SET saleNO := 0; 
					
					LEAVE read_loop;
				-- 如果可售数量小于售出商品数量，则先创建一张来源表，来源表的数量等于当前零售单商品可售出数量
				ELSE 
					SET saleNO := saleNO - noSalable; -- 保证来源表中F_NO是正数
					
					INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID)
					VALUES (retailTradeCommodityID, noSalable, warehousingID, iCommodityID);	   
					
					SET lastInsertID := LAST_INSERT_ID();
					UPDATE t_warehousingcommodity SET F_NOSalable = 0 WHERE F_WarehousingID = warehousingID AND F_CommodityID = iCommodityID;
				END IF;
			END IF;
				
		END LOOP read_loop;
		CLOSE list;
		
		-- 如果循环后售出商品数量>0 则Update最后插入的来源单数量
		IF saleNO > 0 THEN
			-- 当iSaleNOCloned = saleNO时（代表没有任何入库，或者把最后一张入库单的可售数量卖成负数 ），代表来源表还未插入这个商品的售卖信息 
			IF iSaleNOCloned = saleNO THEN 
				-- 寻找最后一张入库单ID来插入来源表。 当查询不到时，为null，代表没有入库
				SELECT F_WarehousingID INTO iWarehousingID FROM t_warehousingcommodity wc WHERE F_CommodityID = iCommodityID AND (SELECT F_Status FROM t_warehousing WHERE F_ID = wc.F_WarehousingID ) = 1 
				ORDER BY F_WarehousingID DESC LIMIT 1;
				
				INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) VALUES (retailTradeCommodityID, saleNO, iWarehousingID, iCommodityID);
			ELSE -- iSaleNOCloned > saleNO，代表上面的游标已经插入过来源表。lastInsertID必然>0
				UPDATE t_retailtradecommoditysource SET F_NO = F_NO + saleNO WHERE F_ID = lastInsertID;
		   	END IF;
		   	 
			UPDATE t_warehousingcommodity wc SET F_NOSalable = F_NOSalable - saleNO -- F_NOSalable可能是负数
			WHERE F_CommodityID = iCommodityID 
			AND (SELECT F_Status FROM t_warehousing WHERE F_ID = wc.F_WarehousingID ) = 1
			ORDER BY F_ID DESC LIMIT 1;
			
			UPDATE t_commodityshopinfo SET F_CurrentWarehousingID = 
				(SELECT F_WarehousingID FROM t_warehousingcommodity wc WHERE F_CommodityID = iCommodityID 
				AND (SELECT F_Status FROM t_warehousing WHERE F_ID = wc.F_WarehousingID ) = 1 
				ORDER BY F_ID DESC LIMIT 1)
			WHERE F_CommodityID = iCommodityID AND F_ShopID = iShopID;
		END IF;
		
	ELSE 	
		-- 因为新开店后，用户创建了没有期初值的商品，就直接售卖，创建的零售单是找不到相应的入库记录的，就直接插入数据
		INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID)
		VALUES (retailTradeCommodityID, saleNO, NULL, iCommodityID);	   				
	END IF;
	
	RETURN (0);	
END;