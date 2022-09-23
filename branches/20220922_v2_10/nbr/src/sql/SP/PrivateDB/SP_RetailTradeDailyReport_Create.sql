
DROP PROCEDURE IF EXISTS `SP_RetailTradeDailyReport_Create`;
-- 毛利率 > 0时，才生成报表，否则不生成报表。比如，今天卖出的商品，全部被退了，这一天是没有报表生成的，因为毛利率为0。如果今天全退，也退了昨天的，那么，毛利是负数，销售额
-- 也是负数，负负得正，毛利率也是正常，仍然会生成报表
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_RetailTradeDailyReport_Create`(
   	OUT iErrorCode INT,   
   	OUT sErrorMsg VARCHAR(64),
   	IN iShopID INT, 				-- 门店ID	
   	IN dSaleDatetime DATETIME,		-- 销售日期
   	IN deleteOldData INT
)
BEGIN

	DECLARE totalNO INT;   						 	-- 销售笔数
	DECLARE topSaleCommodityName VARCHAR(32);    	-- 销售额最高的商品
	DECLARE topSaleCommodityNO INT;			   		-- 销售额最高的商品的数量
	DECLARE topSaleCommodityID INT;              	-- 销售额最高的商品的商品ID
	DECLARE totalAmount DECIMAL(20, 6); 		 	-- 当天销售额
	DECLARE retailAmount DECIMAL(20, 6); 		 	-- 零售总额
	DECLARE returnAmount DECIMAL(20, 6); 		 	-- 退货总额
	DECLARE pricePurchase DECIMAL(20, 6); 			-- 一天销售商品的总进货价
	DECLARE returnPricePurchase DECIMAL(20, 6); 	-- 一天退货商品的总进货价
	DECLARE totalGross DECIMAL(20, 6); 			 	-- 销售毛利
	DECLARE ratioGrossMargin DECIMAL(20, 6); 	 	-- 销售毛利率
	DECLARE topPurchaseCustomerName VARCHAR(30); 	-- 购买额最高的客户
	DECLARE averageAmountOfCustomer DECIMAL(20, 6); -- 客单价
	DECLARE topSaleCommodityAmount DECIMAL(30,6);  	-- 当天最高销售额
	
	DECLARE type INT; 								-- 商品类型
	DECLARE commID INT;								-- 商品ID
	DECLARE commNO INT;   							-- 每个商品的销售数量,这里指实际销售数量，不需要减去退货的商品数量
	DECLARE commodityAmount DECIMAL(20, 6); 	    -- 每个商品的销售额,需要减去退货商品的销售额后,得到真正的销售额
	DECLARE commodityPricePurchase DECIMAL(20, 6); 	-- 每个商品的进货价,需要减去退货商品的进货价后,得到真正的进货价
	DECLARE commGross DECIMAL(20, 6); 				-- 每个商品的销售毛利
	DECLARE done INT DEFAULT  false; 
	
	DECLARE dtSaleDate DATE;				-- 对传入的时间进行格式化
	
	DECLARE list CURSOR FOR (
		-- 查询每个商品的当天的销售数量，销售金额，进货总价，毛利
		-- 计算单品
		SELECT * FROM 
		(
			(
				SELECT F_CommodityID, sum(rtcsNO) AS commNO, commAmount
				FROM
				(
					SELECT rtc.F_ID, rtc.F_TradeID, rt.F_SaleDatetime, rtc.F_CommodityID, rtcs.F_ReducingCommodityID, rtcs.F_NO AS rtcsNO
					FROM t_retailtrade rt,t_retailtradecommodity rtc,t_retailtradecommoditysource rtcs
					WHERE rt.F_ID = rtc.F_TradeID 
					AND rt.F_SourceID = -1
					AND rt.F_ShopID = iShopID
					AND rtc.F_ID = rtcs.F_RetailTradeCommodityID 
					AND datediff(rt.F_SaleDatetime, dSaleDatetime) = 0 
				) AS tmp1,
				(
					SELECT CommodityID, sum(amount) AS commAmount
					FROM
					(
						SELECT 
						comm.F_ID AS CommodityID,
						rt.F_SaleDatetime,
						rtc.F_PriceReturn * rtc.F_NOCanReturn AS amount
						FROM T_RetailTradeCommodity AS rtc, t_commodity AS comm, t_retailtrade AS rt
						WHERE rtc.F_TradeID = rt.F_ID 
						AND rt.F_ShopID = iShopID
						AND rtc.F_CommodityID = comm.F_ID
						AND comm.F_Type = 0 -- 单品
						GROUP BY rtc.F_ID
					) AS tmp
					WHERE datediff(F_SaleDatetime, dSaleDatetime) = 0 GROUP BY CommodityID
				) AS tmp2
				WHERE tmp1.F_CommodityID = tmp2.CommodityID
				GROUP BY F_CommodityID
			)
			UNION
			-- 计算组合
			(
			   SELECT F_CommodityID, no AS commNO, commAmount
				FROM
				(
					SELECT rtc.F_ID, rtc.F_TradeID, rt.F_SaleDatetime, rtc.F_CommodityID, rtc.F_NOCanReturn, rtcs.F_ReducingCommodityID
					FROM t_retailtrade rt,t_retailtradecommodity rtc,t_retailtradecommoditysource rtcs
					WHERE rt.F_ID = rtc.F_TradeID 
					AND rt.F_SourceID = -1
					AND rt.F_ShopID = iShopID
					AND rtc.F_ID = rtcs.F_RetailTradeCommodityID  
					AND datediff(rt.F_SaleDatetime, dSaleDatetime) = 0 
				) AS tmp1,
				(
					SELECT CommodityID, sum(rtcsNO) AS no, sum(amount) AS commAmount
					FROM
					(
						SELECT 
						comm.F_ID AS CommodityID,
						rt.F_SaleDatetime,
						rtc.F_PriceReturn * rtc.F_NOCanReturn AS amount,
						rtc.F_NO AS rtcsNO	-- 这里要查询出商品实际售出的数量，所以要用F_NO，而不是F_NOCanReturn
						FROM T_RetailTradeCommodity AS rtc, t_commodity AS comm, t_retailtrade AS rt
						WHERE rtc.F_TradeID = rt.F_ID 
						AND rtc.F_CommodityID = comm.F_ID
						AND comm.F_Type = 1 -- 组合
						AND rt.F_SourceID = -1 
						AND rt.F_ShopID = iShopID
						GROUP BY rtc.F_ID
					) AS tmp
					WHERE datediff(F_SaleDatetime, dSaleDatetime) = 0 GROUP BY CommodityID
				) AS tmp2
				WHERE tmp1.F_CommodityID = tmp2.CommodityID
				GROUP BY F_CommodityID
			) 
			UNION
			-- 计算多包装
			(
			   SELECT F_CommodityID, no AS commNO, commAmount
				FROM
				(
					SELECT rtc.F_ID, rtc.F_TradeID, rt.F_SaleDatetime, rtc.F_CommodityID, rtcs.F_ReducingCommodityID, rtc.F_NOCanReturn AS rtcsNO
					FROM t_retailtrade rt,t_retailtradecommodity rtc,t_retailtradecommoditysource rtcs
					WHERE rt.F_ID = rtc.F_TradeID 
					AND rt.F_SourceID = -1
					AND rt.F_ShopID = iShopID
					AND rtc.F_ID = rtcs.F_RetailTradeCommodityID  
					AND datediff(rt.F_SaleDatetime, dSaleDatetime) = 0 
				) AS tmp1,
				(
					SELECT CommodityID, sum(amount) AS commAmount, sum(rtcsNO) AS no
					FROM
					(
						SELECT 
						comm.F_ID AS CommodityID,
						rt.F_SaleDatetime,
						rtc.F_PriceReturn * rtc.F_NOCanReturn AS amount,
						rtc.F_NO AS rtcsNO
						FROM T_RetailTradeCommodity AS rtc, t_commodity AS comm, t_retailtrade AS rt
						WHERE rtc.F_TradeID = rt.F_ID 
						AND rt.F_SourceID = -1 
				   		AND rt.F_ShopID = iShopID
						AND rtc.F_CommodityID = comm.F_ID
						AND comm.F_Type = 2 -- 多包装
						GROUP BY rtc.F_ID
					) AS tmp
					WHERE datediff(F_SaleDatetime, dSaleDatetime) = 0 GROUP BY CommodityID
				) AS tmp2
				WHERE tmp1.F_CommodityID = tmp2.CommodityID
				GROUP BY F_CommodityID
			) 
			UNION 
			-- 计算服务商品
			(
				SELECT F_CommodityID, sum(rtcsNO) AS commNO, commAmount
				FROM
				(
					SELECT rtc.F_ID, rtc.F_TradeID, rt.F_SaleDatetime, rtc.F_CommodityID, rtcs.F_ReducingCommodityID, rtcs.F_NO AS rtcsNO
					FROM t_retailtrade rt,t_retailtradecommodity rtc,t_retailtradecommoditysource rtcs
					WHERE rt.F_ID = rtc.F_TradeID 
					AND rt.F_SourceID = -1
					AND rt.F_ShopID = iShopID
					AND rtc.F_ID = rtcs.F_RetailTradeCommodityID 
					AND datediff(rt.F_SaleDatetime, dSaleDatetime) = 0 
				) AS tmp1,
				(
					SELECT CommodityID, sum(amount) AS commAmount
					FROM
					(
						SELECT 
						comm.F_ID AS CommodityID,
						rt.F_SaleDatetime,
						rtc.F_PriceReturn * rtc.F_NOCanReturn AS amount
						FROM T_RetailTradeCommodity AS rtc, t_commodity AS comm, t_retailtrade AS rt
						WHERE rtc.F_TradeID = rt.F_ID 
						AND rtc.F_CommodityID = comm.F_ID
						AND comm.F_Type = 3 -- 服务商品
						AND rt.F_ShopID = iShopID
						GROUP BY rtc.F_ID
					) AS tmp
					WHERE datediff(F_SaleDatetime, dSaleDatetime) = 0 GROUP BY CommodityID
				) AS tmp2
				WHERE tmp1.F_CommodityID = tmp2.CommodityID
				GROUP BY F_CommodityID
			)
		) AS TMP
	
	); 
	
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = true; 
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;	
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		-- 仅用于测试，保证测试通过
		IF deleteOldData = 1 THEN 
		   DELETE FROM t_retailtradedailyreportsummary WHERE F_Datetime = DATE_FORMAT(dSaleDatetime,'%Y-%m-%d') AND F_ShopID = iShopID;
		   DELETE FROM t_retailtradedailyreportbycommodity WHERE F_Datetime = DATE_FORMAT(dSaleDatetime,'%Y-%m-%d') AND F_ShopID = iShopID;
		END IF;	
	
		-- 计算销售笔数（零售单数）
		SELECT NO INTO totalNO 
		FROM 
		(
			SELECT count(F_ID) AS NO
			FROM  t_retailtrade 
			WHERE datediff(F_SaleDatetime,dSaleDatetime) = 0  -- datediff(date1,date2) 返回两个date之间的相隔天数
			AND F_SourceID = -1
			AND F_ShopID = iShopID
		) AS tmp1;

		-- 根据日期查询当天最高销售数量商品的ID、商品名称、销售数量
	   	SELECT ifnull(Amount, 0.000000), F_CommodityID, F_Name, NO INTO topSaleCommodityAmount, topSaleCommodityID, topSaleCommodityName, topSaleCommodityNO  FROM  
		(
			SELECT 
			 rtc.F_CommodityID, rtc.F_PriceReturn * rtc.F_NOCanReturn , c.F_Name, sum(rtc.F_NOCanReturn) AS NO, rt.F_SaleDatetime, sum(rtc.F_PriceReturn * rtc.F_NOCanReturn) AS Amount
			FROM T_RetailTradeCommodity AS rtc, t_retailtrade AS rt, t_commodity c
			WHERE rtc.F_TradeID = rt.F_ID 
			AND rt.F_SourceID = -1
			AND rt.F_ShopID = iShopID
			AND rtc.F_CommodityID = c.F_ID 
			AND datediff(rt.F_SaleDatetime, dSaleDatetime) = 0
			GROUP BY rtc.F_CommodityID
			ORDER BY NO DESC, rtc.F_CommodityID DESC LIMIT 1
		) AS tmp2;
		
		 -- 统计零售总额
		 SELECT IF(Amount IS NULL, 0.000000, Amount) INTO retailAmount
		 FROM 
		 (
		 	SELECT sum(F_Amount) AS Amount
		 	FROM t_retailtrade
		 	WHERE datediff(F_SaleDatetime, dSaleDatetime) = 0
		 	AND F_SourceID = -1
			AND F_ShopID = iShopID
		 ) AS tmp4;	
		 
		 -- 统计退货总额
		 SELECT IF(Amount IS NULL, 0.000000, Amount) INTO returnAmount
		 FROM 
		 (
		 	SELECT sum(F_Amount) AS Amount
		 	FROM t_retailtrade
		 	WHERE datediff(F_SaleDatetime, dSaleDatetime) = 0
		 	AND F_SourceID <> -1
			AND F_ShopID = iShopID
		 	
		 ) AS tmp4;	
	
		-- 计算进货总价,若商品未入库，进货价为0,进货价 = 总进货价 - 退货进货价
		SELECT IF(pricePurchaseSum IS NULL, 0.000000, pricePurchaseSum) INTO pricePurchase
		FROM (
		SELECT sum(totalPricePurchase) AS pricePurchaseSum FROM (	 
		   	SELECT rtc.F_CommodityID, rtcs.F_NO, wc.F_Price, rtcs.F_NO * wc.F_Price AS totalpricePurchase 
			FROM t_retailtrade rt,t_retailtradecommodity rtc,t_retailtradecommoditysource rtcs,t_warehousing w,t_warehousingcommodity wc
			WHERE rt.F_ID = rtc.F_TradeID AND 
					rt.F_ShopID = iShopID AND 
					(SELECT c.F_Type FROM t_commodity c WHERE c.F_ID = rtc.F_CommodityID) <> 3 AND -- 服务商品没有进货价
					rtc.F_ID = rtcs.F_RetailTradeCommodityID AND 
					w.F_ID = rtcs.F_WarehousingID  AND 
					w.F_ID = wc.F_WarehousingID AND
				    w.F_ShopID = iShopID AND
					wc.F_CommodityID = rtcs.F_ReducingCommodityID AND  
					datediff(rt.F_SaleDatetime, dSaleDatetime) = 0
			GROUP BY rtcs.F_ID
			) AS tmp5
		) AS tmp6;	 
		
		-- 计算退货进货总价,若商品未入库，进货价为0
		SELECT IF(pricePurchaseSum IS NULL, 0.000000, pricePurchaseSum) INTO returnPricePurchase
		FROM (
		SELECT sum(totalPricePurchase) AS pricePurchaseSum FROM (	 
		   	SELECT rtc.F_CommodityID, rtcd.F_NO, wc.F_Price, rtcd.F_NO * wc.F_Price AS totalpricePurchase 
			FROM t_retailtrade rt,t_retailtradecommodity rtc,t_returnretailtradecommoditydestination rtcd,t_warehousing w,t_warehousingcommodity wc
			WHERE rt.F_ID = rtc.F_TradeID AND 
					rt.F_ShopID = iShopID AND 
					(SELECT c.F_Type FROM t_commodity c WHERE c.F_ID = rtc.F_CommodityID) <> 3 AND -- 服务商品没有进货价
					rtc.F_ID = rtcd.F_RetailTradeCommodityID AND 
					w.F_ID = rtcd.F_WarehousingID  AND 
					w.F_ID = wc.F_WarehousingID AND
					w.F_ShopID = iShopID AND 
					wc.F_CommodityID = rtcd.F_IncreasingCommodityID AND  
					datediff(rt.F_SaleDatetime, dSaleDatetime) = 0
			GROUP BY rtcd.F_ID
			) AS tmp5
		) AS tmp6;	
		
		-- case1:当天仅有售出。
		-- case2:当天有售出和退货,售出总进货价大于退货总进货价
		-- case3:当天有售出和退货,售出总进货价小于退货总进货价
		-- case4:当天有售出和退货,售出总进货价等于退货总进货价
		-- case5:当天只有退货
		-- 以上case进行计算总进货价方式均为  售出总进货价减去退货总进货价
		-- 计算当天真正的进货总价,当计算的价格为负数时,此时就不能说是总进货价
		SET pricePurchase = pricePurchase - returnPricePurchase;
		-- ...计算购买额最高的客户(未研究出来，暂时空着)
		
		 -- 当天销售额  当天销售额 = 零售总额 - 退货总额
	    SET totalAmount = retailAmount - returnAmount;
	    
	    -- 计算销售毛利  毛利 = 销售额 - 进货总价
	    SET totalGross = totalAmount - pricePurchase;
	  	
	    -- 计算销售毛利率  毛利率 = 毛利 / 销售额
	    SET ratioGrossMargin = IF(totalAmount = 0, 0, totalGross / totalAmount);
	    
	    -- 计算客单价   客单价 = 销售额 / 销售笔数	
		SET averageAmountOfCustomer = IF(totalNO = 0, 0, totalAmount / totalNO);
		
	    -- SELECT totalNO, totalAmount, averageAmountOfCustomer, totalGross, ratioGrossMargin, pricePurchase, topSaleCommodityID, topSaleCommodityName, topSaleCommodityAmount, topSaleCommodityNO;
	
		-- 进行时间格式化，插入到数据库不需要时分秒
		SET dtSaleDate = DATE_FORMAT(dSaleDatetime,'%Y-%m-%d');
		
	 	 IF ratioGrossMargin <> 0 THEN 
			-- 根据日期查询当天最高销售额的商品名称
			SELECT topSaleCommodityName;

			-- 零售单日报汇总表
			INSERT INTO t_retailtradedailyreportsummary (
				F_ShopID,
				F_Datetime, 
				F_TotalNO, 
				F_PricePurchase, 
				F_TotalAmount, 
				F_AverageAmountOfCustomer, 
				F_TotalGross, 
				F_RatioGrossMargin, 
				F_TopSaleCommodityID, 
				F_TopSaleCommodityNO,
				F_TopSaleCommodityAmount)
			VALUES (
				iShopID,
				dtSaleDate, 
				totalNO, 
				pricePurchase, 
				totalAmount, 
				averageAmountOfCustomer, 
				totalGross, 
				ratioGrossMargin, 
				topSaleCommodityID, 
				(CASE WHEN topSaleCommodityID IS NULL THEN 0 ELSE topSaleCommodityNO END),
				(CASE WHEN topSaleCommodityID IS NULL THEN 0 ELSE topSaleCommodityAmount END)
				);

			SELECT F_ID, 
				F_ShopID,
				F_Datetime, 
				F_TotalNO, 
				F_PricePurchase, 
				F_TotalAmount, 
				F_AverageAmountOfCustomer, 
				F_TotalGross, 
				F_RatioGrossMargin, 
				F_TopSaleCommodityID, 
				F_TopSaleCommodityNO, 
				F_TopSaleCommodityAmount, 
				F_TopPurchaseCustomerName, 
				F_CreateDatetime, 
				F_UpdateDatetime
			FROM t_retailtradedailyreportsummary
			WHERE F_ID = last_insert_id() 
			AND F_ShopID = iShopID;
			
			-- 	零售单日报商品表
			OPEN  list;  
		    	read_loop: LOOP	  
		    FETCH list INTO commID, commNO, commodityAmount;   
		    IF done THEN  
		        LEAVE read_loop;   
		    END IF;
		    	SELECT F_Type INTO type FROM t_commodity WHERE F_ID = commID;
		    	-- 以下几种情况不可以合并为一种情况 ，只需要从来源表中拿数计算进货总价
		    	IF type = 2 THEN
		    		-- 商品进货总价 = 总进货价 - 退货商品进货总价(这两个查询语句不能合并，因为有可能没有退货信息，就会导致最终计算结果为0)
		    		SELECT ifnull(sum(totalpricePurchase), 0.000000) INTO commodityPricePurchase
						FROM
						(
							SELECT rtcs.F_NO * wc.F_Price AS totalpricePurchase
							FROM t_retailtrade rt,t_retailtradecommodity rtc,t_retailtradecommoditysource rtcs,t_warehousing w,t_warehousingcommodity wc
							WHERE rt.F_ID = rtc.F_TradeID 
							AND rt.F_SourceID = -1
							AND rt.F_ShopID = iShopID 
							AND rtc.F_ID = rtcs.F_RetailTradeCommodityID 
							AND w.F_ID = rtcs.F_WarehousingID  
							AND w.F_ID = wc.F_WarehousingID 
							AND rtc.F_CommodityID = commID 
							AND wc.F_CommodityID = rtcs.F_ReducingCommodityID
							AND w.F_ShopID = iShopID
							AND datediff(rt.F_SaleDatetime, dSaleDatetime) = 0 
						) AS tmp7;
						
					SELECT ifnull(sum(returnTotalpricePurchase), 0.000000) INTO returnPricePurchase
						FROM	
						(
							SELECT rtcd.F_NO * wc.F_Price AS returnTotalpricePurchase -- 这里使用ifnull没用，因为结果集是空，不是null，也不是其它值
							FROM t_retailtrade rt,t_retailtradecommodity rtc,t_returnretailtradecommoditydestination rtcd,t_warehousing w,t_warehousingcommodity wc
							WHERE rt.F_ID = rtc.F_TradeID 
							AND rt.F_SourceID > 0
							AND rt.F_ShopID = iShopID
							AND rtc.F_ID = rtcd.F_RetailTradeCommodityID 
							AND w.F_ID = rtcd.F_WarehousingID  
							AND w.F_ID = wc.F_WarehousingID 
							AND w.F_ShopID = iShopID
							AND rtc.F_CommodityID = commID
							AND wc.F_CommodityID = rtcd.F_IncreasingCommodityID
							AND datediff(rt.F_SaleDatetime, dSaleDatetime) = 0 	
						)AS tmp8;		   
				ELSEIF type = 1 THEN
					SELECT ifnull(sum(totalpricePurchase),0.000000) INTO commodityPricePurchase
					FROM
					( 
						SELECT rtcs.F_NO * wc.F_Price AS totalpricePurchase
						FROM t_retailtrade rt,t_retailtradecommodity rtc,t_retailtradecommoditysource rtcs,t_warehousing w,t_warehousingcommodity wc
						WHERE rt.F_ID = rtc.F_TradeID 
						AND rt.F_SourceID = -1
						AND rt.F_ShopID = iShopID
						AND rtc.F_ID = rtcs.F_RetailTradeCommodityID 
						AND rtc.F_CommodityID = commID
						AND w.F_ID = rtcs.F_WarehousingID
						AND w.F_ID = wc.F_WarehousingID 
						AND w.F_ShopID = iShopID
						AND wc.F_CommodityID = rtcs.F_ReducingCommodityID
						AND datediff(rt.F_SaleDatetime, dSaleDatetime) = 0 
					) AS tmp7;

					SELECT ifnull(sum(totalpricePurchase),0.000000) INTO returnPricePurchase 
					FROM
					( 
						SELECT rtcd.F_NO * wc.F_Price AS totalpricePurchase	-- 这里使用ifnull没用，因为结果集是空，不是null，也不是其它值
						FROM t_retailtrade rt,t_retailtradecommodity rtc,t_returnretailtradecommoditydestination rtcd,t_warehousing w,t_warehousingcommodity wc
						WHERE rt.F_ID = rtc.F_TradeID 
						AND rt.F_SourceID > 0
						AND rt.F_ShopID = iShopID
						AND rtc.F_ID = rtcd.F_RetailTradeCommodityID 
						AND rtc.F_CommodityID = commID
						AND w.F_ID = rtcd.F_WarehousingID  
						AND w.F_ID = wc.F_WarehousingID 
						AND w.F_ShopID = iShopID
						AND wc.F_CommodityID = rtcd.F_IncreasingCommodityID
						AND datediff(rt.F_SaleDatetime, dSaleDatetime) = 0 	
					)AS tmp8; 				
				ELSE
			    	SELECT ifnull(sum(totalpricePurchase), 0.000000) INTO commodityPricePurchase
					FROM
					(
						SELECT rtcs.F_NO * wc.F_Price AS totalpricePurchase
						FROM t_retailtrade rt,t_retailtradecommodity rtc,t_retailtradecommoditysource rtcs,t_warehousing w,t_warehousingcommodity wc
						WHERE rt.F_ID = rtc.F_TradeID 
						AND (SELECT c.F_Type FROM t_commodity c WHERE c.F_ID = commID) <> 3
						AND rt.F_SourceID = -1
						AND rt.F_ShopID = iShopID
						AND rtc.F_ID = rtcs.F_RetailTradeCommodityID 
						AND w.F_ID = rtcs.F_WarehousingID  
						AND w.F_ID = wc.F_WarehousingID 
						AND w.F_ShopID = iShopID
						AND rtc.F_CommodityID = commID
						AND rtc.F_CommodityID = wc.F_CommodityID
						AND datediff(rt.F_SaleDatetime, dSaleDatetime) = 0 
					) AS tmp7;
					
					SELECT ifnull(sum(returnTotalpricePurchase), 0.000000) INTO returnPricePurchase
					FROM
					(
						SELECT rtcd.F_NO * wc.F_Price AS returnTotalpricePurchase	-- 这里使用ifnull没用，因为结果集是空，不是null，也不是其它值
						FROM t_retailtrade rt,t_retailtradecommodity rtc,t_returnretailtradecommoditydestination rtcd,t_warehousing w,t_warehousingcommodity wc
						WHERE rt.F_ID = rtc.F_TradeID 
						AND (SELECT c.F_Type FROM t_commodity c WHERE c.F_ID = commID) <> 3
						AND rt.F_SourceID > 0
						AND rt.F_ShopID = iShopID
						AND rtc.F_ID = rtcd.F_RetailTradeCommodityID 
						AND w.F_ID = rtcd.F_WarehousingID  
						AND w.F_ID = wc.F_WarehousingID 
						AND w.F_ShopID = iShopID
						AND rtc.F_CommodityID = commID
						AND rtc.F_CommodityID = wc.F_CommodityID
						AND datediff(rt.F_SaleDatetime, dSaleDatetime) = 0 	
					)AS tmp8;
				END IF;
				
				SET commodityPricePurchase = commodityPricePurchase - returnPricePurchase;	
				
				INSERT INTO t_retailtradedailyreportbycommodity (
					F_ShopID,
					F_Datetime, 
					F_CommodityID, 
					F_NO, 
					F_TotalPurchasingAmount, 
					F_TotalAmount, 
					F_GrossMargin)
				VALUES (
					iShopID,
					dtSaleDate, 
					commID, 
					commNO, 
					commodityPricePurchase, 
					commodityAmount, 
					commodityAmount - commodityPricePurchase
					);
			END LOOP read_loop;
			 
	
	   		CLOSE list;
		
			SELECT 
				F_ID, 
				F_ShopID,
				F_Datetime, 
				F_CommodityID, 
				F_NO, 
				F_TotalPurchasingAmount,
				F_TotalAmount, 
				F_GrossMargin, 
				F_CreateDatetime, 
				F_UpdateDatetime
			FROM t_retailtradedailyreportbycommodity
			WHERE F_Datetime = dtSaleDate 
			AND F_ShopID = iShopID;
		 ELSE
		 	SELECT topSaleCommodityName;
		  	-- 零售单日报汇总表
			INSERT INTO t_retailtradedailyreportsummary (
				F_ShopID,
				F_Datetime, 
				F_TotalNO, 
				F_PricePurchase, 
				F_TotalAmount, 
				F_AverageAmountOfCustomer, 
				F_TotalGross, 
				F_RatioGrossMargin, 
				F_TopSaleCommodityID, 
				F_TopSaleCommodityNO,
				F_TopSaleCommodityAmount)
			VALUES (
				iShopID,
				dtSaleDate, 
				0, 
				0, 
				0, 
				0, 
				0, 
				0, 
				NULL, 
				0,
				0
				);

			SELECT F_ID, 
				F_ShopID,
				F_Datetime, 
				F_TotalNO, 
				F_PricePurchase, 
				F_TotalAmount, 
				F_AverageAmountOfCustomer, 
				F_TotalGross, 
				F_RatioGrossMargin, 
				F_TopSaleCommodityID, 
				F_TopSaleCommodityNO, 
				F_TopSaleCommodityAmount, 
				F_TopPurchaseCustomerName, 
				F_CreateDatetime, 
				F_UpdateDatetime
			FROM t_retailtradedailyreportsummary
			WHERE F_ID = last_insert_id() 
			AND F_ShopID = iShopID; 
		END IF; 	
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END;