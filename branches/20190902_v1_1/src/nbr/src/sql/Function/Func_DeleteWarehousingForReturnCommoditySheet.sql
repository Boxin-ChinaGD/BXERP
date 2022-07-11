drop function IF EXISTS Func_DeleteWarehousingForReturnCommoditySheet;
CREATE DEFINER=`root`@`localhost` FUNCTION `Func_DeleteWarehousingForReturnCommoditySheet`(
	iCommodityID INT,
	returnNO INT, -- 退货商品数量 
	currentWarehousingID INT, -- 当值入库ID
	iShopID INT
) RETURNS int(11)
BEGIN
	DECLARE iID INT;
	DECLARE NO INT;
	DECLARE noSalable INT; -- 可售商品数量
	DECLARE warehousingID INT;
	DECLARE done INT DEFAULT FALSE;	-- 创建结束标志变量
	DECLARE list CURSOR FOR (SELECT 
		F_ID AS iID, 
		F_NOSalable AS noSalable, 
		F_WarehousingID AS warehousingID
	FROM t_warehousingcommodity wc
	WHERE F_CommodityID = iCommodityID
	AND (SELECT F_Status FROM t_warehousing WHERE F_ID = wc.F_WarehousingID AND F_ShopID = iShopID) = 1 
	AND (CASE ISNULL(currentWarehousingID) 
			WHEN 1 THEN 
				1=1 
			ELSE 
				F_ID >= (SELECT F_ID FROM t_warehousingcommodity WHERE F_WarehousingID = currentWarehousingID AND F_CommodityID = iCommodityID) 
			END)
	);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  -- 指定游标循环结束时返回值
	
	
	SET NO := returnNO;
	IF EXISTS(SELECT 1 FROM t_warehousingcommodity WHERE F_CommodityID = iCommodityID) THEN
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iID, noSalable, warehousingID;
	
			IF done THEN
		  		LEAVE read_loop;
			END IF;
			-- 如果(退货数量<=入库单商品可售数量)，直接Update入库单商品的入库数量和可售数量和商品的当值入库ID。
			IF (NO <= noSalable) THEN
					
				UPDATE t_warehousingcommodity SET F_NOSalable = F_NOSalable - NO WHERE F_WarehousingID = warehousingID AND F_CommodityID = iCommodityID;  
				
--				UPDATE t_commodity SET F_CurrentWarehousingID = warehousingID WHERE F_ID = iCommodityID;
				UPDATE t_commodityshopinfo SET F_CurrentWarehousingID = warehousingID WHERE F_CommodityID = iCommodityID AND F_ShopID = iShopID;
				
				SET NO := 0; 
					
				LEAVE read_loop;
			-- 如果(退货数量>入库单商品可售数量），先计算出溢出值，然后更新入库单商品可售数量为0，入库数量为入库数量减去可售数量。
			ELSE 
				SET NO := NO - noSalable;  
				
				UPDATE t_warehousingcommodity SET F_NOSalable = 0 WHERE F_WarehousingID = warehousingID AND F_CommodityID = iCommodityID;
			END IF;
			
		END LOOP read_loop;
		CLOSE list;
		
		-- 如果循环后退货商品数量>0 则Update最后一张入库单的可售数量
		IF NO > 0 THEN
			UPDATE t_warehousingcommodity wc SET F_NOSalable = F_NOSalable - NO 
			WHERE F_CommodityID = iCommodityID 
			AND (SELECT F_Status FROM t_warehousing WHERE F_ID = wc.F_WarehousingID ) = 1 
			ORDER BY F_ID DESC LIMIT 1;
			
			UPDATE t_commodityshopinfo SET F_CurrentWarehousingID = 
				(SELECT F_WarehousingID FROM t_warehousingcommodity wc
				WHERE F_CommodityID = iCommodityID
				AND (SELECT F_Status FROM t_warehousing WHERE F_ID = wc.F_WarehousingID ) = 1  
				ORDER BY F_ID DESC LIMIT 1) 
			WHERE F_CommodityID = iCommodityID AND F_ShopID = iShopID;
		END IF;
						
		RETURN (0);
	ELSE 
		RETURN (2);
	END IF;
END;