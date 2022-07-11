DROP PROCEDURE IF EXISTS `SP_RetailTrade_RetrieveNByCommodityNameInTimeRange`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_RetailTrade_RetrieveNByCommodityNameInTimeRange` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN commodityName VARCHAR(32),-- 关键字
	IN iStaffID INT,
	IN iPaymentType INT,
	IN dtStartDate DATETIME,
	IN dtEndDate DATETIME,
	IN iPageIndex INT,
	IN iPageSize INT,
	OUT iTotalRecord INT,
	OUT dRetailAmount DECIMAL(20, 6),	-- 零售总额
	OUT dTotalCommNO INT,				-- 商品的销售数量,这里指实际销售数量，不需要减去退货的商品数量
	OUT dTotalGross DECIMAL(20, 6)		-- 销售毛利
	)
BEGIN
	DECLARE recordIndex INT;
	DECLARE done INT DEFAULT  false; 
	DECLARE pricePurchase DECIMAL(20, 6); 			-- 一天销售商品的总进货价							
	DECLARE commID INT;								-- 商品ID
	DECLARE commNO INT;   							-- 每个商品的销售数量,这里指实际销售数量，不需要减去退货的商品数量
	DECLARE commodityAmount DECIMAL(20, 6); 	    -- 每个商品的销售额,需要减去退货商品的销售额后,得到真正的销售额
	
	-- 查询每个商品在该dtStarDate, dtEndDate时间段的销售数量，销售金额，进货总价，毛利
	DECLARE list CURSOR FOR (
		-- 计算零售商品中，各个单品的销售数量和销售总额
		SELECT * FROM 
		(
	  		(
				SELECT F_CommodityID, sum(rtCommmSourceNO) AS commNO, commAmount
				FROM 
				(
					-- 查询目标时间段内的零售单
					SELECT rtCommSource.F_NO AS rtCommmSourceNO, rtComm.F_CommodityID 
					FROM t_retailtrade retailtrade, t_retailtradecommodity rtComm, t_retailtradecommoditysource rtCommSource
					WHERE 
						1=1
						AND (CASE ifnull(dtStartDate, 0) WHEN 0 THEN 1=1 ELSE F_SaleDatetime >= dtStartDate END) 
		  				AND (CASE ifnull(dtEndDate, 0) WHEN 0 THEN 1=1 ELSE F_SaleDatetime <= dtEndDate END)	  				
						AND retailtrade.F_ID = rtComm.F_TradeID
						AND rtComm.F_ID = rtCommSource.F_RetailTradeCommodityID
						AND retailtrade.F_SourceID = -1
				) AS tmp1,
				(	
					-- 计算零售商品中，各个单品的销售总额
					SELECT commodityID, sum(amount) AS commAmount
					FROM 
					(
						SELECT 
							commodity.F_ID AS commodityID,
							retailtrade.F_SaleDatetime,
							rtComm.F_NO * rtComm.F_PriceReturn AS amount
					    FROM t_commodity commodity, t_retailtradecommodity rtComm, t_retailtrade retailtrade
						WHERE rtComm.F_TradeID = retailtrade.F_ID
							AND rtComm.F_CommodityID = commodity.F_ID
							AND commodity.F_Type = 0 -- 单品
							AND retailtrade.F_SourceID = -1
							AND (CASE IFNULL(commodityName, '') WHEN '' THEN 1=1 ELSE commodity.F_Name LIKE CONCAT('%',replace(commodityName, '_', '\_'), '%')
							 	END) 						
							AND (CASE iStaffID WHEN 0 THEN 1=1 ELSE retailtrade.F_StaffID = iStaffID END) 
							AND (CASE iPaymentType WHEN 0 THEN 1=1 ELSE (retailtrade.F_PaymentType & iPaymentType) = iPaymentType END) 
						GROUP BY rtComm.F_ID
					) AS tmp
					WHERE 
						1=1
						AND (CASE ifnull(dtStartDate, 0) WHEN 0 THEN 1=1 ELSE F_SaleDatetime >= dtStartDate END) 
		  				AND (CASE ifnull(dtEndDate, 0) WHEN 0 THEN 1=1 ELSE F_SaleDatetime <= dtEndDate END)
	  				
					GROUP BY commodityID
				) AS tmp2
				WHERE tmp1.F_CommodityID = tmp2.commodityID -- 连接
				GROUP BY F_CommodityID
			)
			UNION
			-- 计算组合
			(
				SELECT F_CommodityID, no AS commNO, commAmount
				FROM 
				(
					SELECT rtCommSource.F_NO AS rtCommmSourceNO, rtComm.F_CommodityID 
					FROM t_retailtrade retailtrade, t_retailtradecommodity rtComm, t_retailtradecommoditysource rtCommSource
					WHERE 
						1=1
		  				AND (CASE ifnull(dtStartDate, 0) WHEN 0 THEN 1=1 ELSE F_SaleDatetime >= dtStartDate END) 
		  				AND (CASE ifnull(dtEndDate, 0) WHEN 0 THEN 1=1 ELSE F_SaleDatetime <= dtEndDate END)
		  				
						AND retailtrade.F_ID = rtComm.F_TradeID
						AND rtComm.F_ID = rtCommSource.F_RetailTradeCommodityID
						AND retailtrade.F_SourceID = -1
				) AS tmp1,
				(	
					-- 计算零售商品中，各个单品的销售总额
					SELECT commodityID, sum(amount) AS commAmount, sum(rtCommSourceNO) AS no
					FROM 
					(
						SELECT 
							commodity.F_ID AS commodityID,
							retailtrade.F_SaleDatetime,
							rtComm.F_NO * rtComm.F_PriceReturn AS amount,
							rtComm.F_NO AS rtCommSourceNO
					    FROM t_commodity commodity, t_retailtradecommodity rtComm, t_retailtrade retailtrade
							WHERE rtComm.F_TradeID = retailtrade.F_ID
							AND rtComm.F_CommodityID = commodity.F_ID
							AND retailtrade.F_SourceID = -1
							AND commodity.F_Type = 1 -- 组合
							AND (CASE IFNULL(commodityName, '') WHEN '' THEN 1=1 ELSE commodity.F_Name LIKE CONCAT('%',replace(commodityName, '_', '\_'), '%')
							 	END)
							AND (CASE iStaffID WHEN 0 THEN 1=1 ELSE retailtrade.F_StaffID = iStaffID END) 
							AND (CASE iPaymentType WHEN 0 THEN 1=1 ELSE (retailtrade.F_PaymentType & iPaymentType) = iPaymentType END)	   				
						GROUP BY rtComm.F_ID
					) AS tmp
					WHERE 
						1=1
						AND (CASE ifnull(dtStartDate, 0) WHEN 0 THEN 1=1 ELSE F_SaleDatetime >= dtStartDate END) 
		  				AND (CASE ifnull(dtEndDate, 0) WHEN 0 THEN 1=1 ELSE F_SaleDatetime <= dtEndDate END)
					GROUP BY commodityID
				) AS tmp2
				WHERE tmp1.F_CommodityID = tmp2.commodityID -- 连接
				GROUP BY F_CommodityID
			)
			UNION 
			-- 计算多包装
			(
				SELECT F_CommodityID, no AS commNO, commAmount
				FROM 
				(
					SELECT rtCommSource.F_NO AS rtCommmSourceNO, rtComm.F_CommodityID 
					FROM t_retailtrade retailtrade, t_retailtradecommodity rtComm, t_retailtradecommoditysource rtCommSource
					WHERE 
						1=1
						AND (CASE ifnull(dtStartDate, 0) WHEN 0 THEN 1=1 ELSE F_SaleDatetime >= dtStartDate END) 
		  				AND (CASE ifnull(dtEndDate, 0) WHEN 0 THEN 1=1 ELSE F_SaleDatetime <= dtEndDate END)
						AND retailtrade.F_ID = rtComm.F_TradeID
						AND rtComm.F_ID = rtCommSource.F_RetailTradeCommodityID
						AND retailtrade.F_SourceID = -1
				) AS tmp1,
				(	
					-- 计算零售商品中，各个单品的销售总额
					SELECT commodityID, sum(amount) AS commAmount, sum(rtCommSourceNO) AS no
					FROM 
					(
						SELECT 
							commodity.F_ID AS commodityID,
							retailtrade.F_SaleDatetime,
							rtComm.F_NO * rtComm.F_PriceReturn AS amount,
							rtComm.F_NO AS rtCommSourceNO
					    FROM t_commodity commodity, t_retailtradecommodity rtComm, t_retailtrade retailtrade
						WHERE rtComm.F_TradeID = retailtrade.F_ID
							AND rtComm.F_CommodityID = commodity.F_ID
							AND retailtrade.F_SourceID = -1
							AND commodity.F_Type = 2 -- 多包装
							AND (CASE IFNULL(commodityName, '') WHEN '' THEN 1=1 ELSE commodity.F_Name LIKE CONCAT('%',replace(commodityName, '_', '\_'), '%')
							 	END)
							AND (CASE iStaffID WHEN 0 THEN 1=1 ELSE retailtrade.F_StaffID = iStaffID END)  
							AND (CASE iPaymentType WHEN 0 THEN 1=1 ELSE (retailtrade.F_PaymentType & iPaymentType) = iPaymentType END)			
						GROUP BY rtComm.F_ID
					) AS tmp
					WHERE 
						1=1
						AND (CASE ifnull(dtStartDate, 0) WHEN 0 THEN 1=1 ELSE F_SaleDatetime >= dtStartDate END) 
		  				AND (CASE ifnull(dtEndDate, 0) WHEN 0 THEN 1=1 ELSE F_SaleDatetime <= dtEndDate END)
					GROUP BY commodityID
				) AS tmp2
				WHERE tmp1.F_CommodityID = tmp2.commodityID -- 连接
				GROUP BY F_CommodityID
			)
			UNION 
			-- 计算服务商品
			(
				SELECT F_CommodityID, sum(rtCommmSourceNO) AS commNO, commAmount
				FROM 
				(
					SELECT rtCommSource.F_NO AS rtCommmSourceNO, rtComm.F_CommodityID 
					FROM t_retailtrade retailtrade, t_retailtradecommodity rtComm, t_retailtradecommoditysource rtCommSource
					WHERE 
						1=1
						AND (CASE ifnull(dtStartDate, 0) WHEN 0 THEN 1=1 ELSE F_SaleDatetime >= dtStartDate END) 
		  				AND (CASE ifnull(dtEndDate, 0) WHEN 0 THEN 1=1 ELSE F_SaleDatetime <= dtEndDate END)
						AND retailtrade.F_ID = rtComm.F_TradeID
						AND rtComm.F_ID = rtCommSource.F_RetailTradeCommodityID
						AND retailtrade.F_SourceID = -1
				) AS tmp1,
				(	
					-- 计算零售商品中，各个单品的销售总额
					SELECT commodityID, sum(amount) AS commAmount
					FROM 
					(
						SELECT 
							commodity.F_ID AS commodityID,
							retailtrade.F_SaleDatetime,
							rtComm.F_NO * rtComm.F_PriceReturn AS amount,
							rtComm.F_NO AS rtCommSourceNO
					    FROM t_commodity commodity, t_retailtradecommodity rtComm, t_retailtrade retailtrade
						WHERE rtComm.F_TradeID = retailtrade.F_ID
							AND rtComm.F_CommodityID = commodity.F_ID
							AND commodity.F_Type = 3 -- 服务商品
							AND retailtrade.F_SourceID = -1
							AND (CASE IFNULL(commodityName, '') WHEN '' THEN 1=1 ELSE commodity.F_Name LIKE CONCAT('%',replace(commodityName, '_', '\_'), '%')
							 	END)
							AND (CASE iStaffID WHEN 0 THEN 1=1 ELSE retailtrade.F_StaffID = iStaffID END)  
							AND (CASE iPaymentType WHEN 0 THEN 1=1 ELSE (retailtrade.F_PaymentType & iPaymentType) = iPaymentType END)					
						GROUP BY rtComm.F_ID
					) AS tmp
					WHERE 
						1=1
						AND (CASE ifnull(dtStartDate, 0) WHEN 0 THEN 1=1 ELSE F_SaleDatetime >= dtStartDate END) 
		  				AND (CASE ifnull(dtEndDate, 0) WHEN 0 THEN 1=1 ELSE F_SaleDatetime <= dtEndDate END)
					GROUP BY commodityID
				) AS tmp2
				WHERE tmp1.F_CommodityID = tmp2.commodityID -- 连接
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
		
		SET iPageIndex = iPageIndex - 1;
		
		SET recordIndex = iPageIndex * iPageSize;
		
		-- 	商品销售数量
		SET dTotalCommNO = 0;
		-- 符合条件的商品总额
		SET dRetailAmount = 0;
		OPEN list;  
	    	read_loop: LOOP	  
	    	
	    	FETCH list INTO commID, commNO, commodityAmount;   
	    	
		    IF done THEN  
		        LEAVE read_loop;   
		    END IF;
			   	SET dTotalCommNO := dTotalCommNO + IF(commNO IS NULL, 0, commNO);
			   	SET dRetailAmount := dRetailAmount + IF(commodityAmount IS NULL, 0.000000, commodityAmount);
			END LOOP read_loop;

   		CLOSE list;
   		
   		-- 计算进货总价,若商品未入库，进货价为0
		SELECT IF(pricePurchaseSum IS NULL, 0.000000, pricePurchaseSum) INTO pricePurchase
		FROM (
			SELECT sum(totalPricePurchase) AS pricePurchaseSum FROM (	 
			   	SELECT rtcs.F_NO * wc.F_Price AS totalPricePurchase 
				FROM t_retailtrade rt,t_retailtradecommodity rtc,t_retailtradecommoditysource rtcs,t_warehousing w,t_warehousingcommodity wc
				WHERE 
					rt.F_ID = rtc.F_TradeID 
					AND (SELECT c.F_Type FROM t_commodity c WHERE c.F_ID = rtc.F_CommodityID) <> 3 -- 服务商品没有进货价
					AND rtc.F_ID = rtcs.F_RetailTradeCommodityID 
					AND w.F_ID = rtcs.F_WarehousingID  -- 入库单ID为来源表的warehousingID
					AND w.F_ID = wc.F_WarehousingID   --  入库单ID为入库商品表的warehousingID
  					AND wc.F_CommodityID = rtcs.F_ReducingCommodityID 
	--					AND rt.F_SourceID = -1
					AND (CASE ifnull(dtStartDate, 0) WHEN 0 THEN 1=1 ELSE F_SaleDatetime >= dtStartDate END) 
	  				AND (CASE ifnull(dtEndDate, 0) WHEN 0 THEN 1=1 ELSE F_SaleDatetime <= dtEndDate END) 
					AND (CASE IFNULL(commodityName, '') WHEN '' THEN 1=1 ELSE rtc.F_ID IN (
							SELECT F_ID FROM t_retailtradecommodity WHERE F_CommodityName LIKE CONCAT('%',replace(commodityName, '_', '\_'), '%')
						) END) 	 
					AND (CASE iStaffID WHEN 0 THEN 1=1 ELSE rt.F_StaffID = iStaffID END)  
					AND (CASE iPaymentType WHEN 0 THEN 1=1 ELSE (rt.F_PaymentType & iPaymentType) = iPaymentType END)					
				GROUP BY rtcs.F_ID
			) AS tmp5
		) AS tmp6;
		
		SET dTotalGross = dRetailAmount - pricePurchase;
		
		SELECT 
			F_ID, 
			F_VipID, 
			F_SN, 
			F_LocalSN, 
			F_POS_ID, 
			F_Logo, 
			F_SaleDatetime, 
			F_StaffID, 
			F_PaymentType, 
			F_PaymentAccount, 
			F_Status, 
			F_Remark, 
			F_SourceID, 
			F_SyncDatetime, 
			F_Amount, 
			F_AmountCash, 
			F_AmountAlipay, 
			F_AmountWeChat, 
			F_Amount1, 
			F_Amount2, 
			F_Amount3, 
			F_Amount4, 
			F_Amount5, 
			F_SmallSheetID, 
			F_AliPayOrderSN, 
			F_WxOrderSN, 
			F_WxTradeNO,
			F_WxRefundNO, 
			F_WxRefundDesc, 
			F_WxRefundSubMchID
		FROM (
			SELECT 
				F_ID, 
				F_VipID, 
				F_SN, 
				F_LocalSN, 
				F_POS_ID, 
				F_Logo, 
				F_SaleDatetime, 
				F_StaffID, 
				F_PaymentType, 
				F_PaymentAccount, 
				F_Status, 
				F_Remark, 
				F_SourceID, 
				F_SyncDatetime, 
				F_Amount, 
				F_AmountCash, 
				F_AmountAlipay, 
				F_AmountWeChat, 
				F_Amount1, 
				F_Amount2, 
				F_Amount3, 
				F_Amount4, 
				F_Amount5, 
				F_SmallSheetID, 
				F_AliPayOrderSN, 
				F_WxOrderSN, 
				F_WxTradeNO,
				F_WxRefundNO, 
				F_WxRefundDesc, 
				F_WxRefundSubMchID
			FROM t_retailtrade 
			WHERE  (CASE IFNULL(commodityName, '') WHEN '' THEN 1=1 ELSE F_ID IN (
						SELECT F_TradeID FROM t_retailtradecommodity WHERE F_CommodityName LIKE CONCAT('%',replace(commodityName, '_', '\_'), '%')
					) END) 
				AND (CASE iStaffID WHEN 0 THEN 1=1 ELSE F_StaffID = iStaffID END)
				AND (CASE iPaymentType WHEN 0 THEN 1=1 ELSE (F_PaymentType & iPaymentType) = iPaymentType END)
			  	AND (CASE ifnull(dtStartDate, 0) WHEN 0 THEN 1=1 ELSE F_SaleDatetime >= dtStartDate END) 
			  	AND (CASE ifnull(dtEndDate, 0) WHEN 0 THEN 1=1 ELSE F_SaleDatetime <= dtEndDate END) 
		)AS TMP
		ORDER BY F_ID DESC
		LIMIT recordIndex, iPageSize;
		
		SELECT count(1) into iTotalRecord
		FROM (
			SELECT 
				F_ID, 
				F_VipID, 
				F_SN, 
				F_LocalSN, 
				F_POS_ID, 
				F_Logo, 
				F_SaleDatetime, 
				F_StaffID, 
				F_PaymentType, 
				F_PaymentAccount, 
				F_Status, 
				F_Remark, 
				F_SourceID, 
				F_SyncDatetime, 
				F_Amount, 
				F_AmountCash, 
				F_AmountAlipay, 
				F_AmountWeChat, 
				F_Amount1, 
				F_Amount2, 
				F_Amount3, 
				F_Amount4, 
				F_Amount5, 
				F_SmallSheetID, 
				F_AliPayOrderSN, 
				F_WxOrderSN, 
				F_WxTradeNO,
				F_WxRefundNO, 
				F_WxRefundDesc, 
				F_WxRefundSubMchID
			FROM t_retailtrade 
			WHERE (CASE IFNULL(commodityName, '') WHEN '' THEN 1=1 ELSE F_ID IN (
						SELECT F_TradeID FROM t_retailtradecommodity WHERE F_CommodityName LIKE CONCAT('%',replace(commodityName, '_', '\_'), '%')
					) END) 
				AND (CASE iStaffID WHEN 0 THEN 1=1 ELSE F_StaffID = iStaffID END)
				AND (CASE iPaymentType WHEN 0 THEN 1=1 ELSE (F_PaymentType & iPaymentType) = iPaymentType END)
			  	AND (CASE ifnull(dtStartDate, 0) WHEN 0 THEN 1=1 ELSE F_SaleDatetime >= dtStartDate END) 
			  	AND (CASE ifnull(dtEndDate, 0) WHEN 0 THEN 1=1 ELSE F_SaleDatetime <= dtEndDate END)
		) AS TMP;
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
	COMMIT;
END;