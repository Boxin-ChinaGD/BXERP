DROP PROCEDURE IF EXISTS SP_RetailTradeDailyReportSummary_Retrieve1;
CREATE DEFINER=`root`@`localhost` PROCEDURE SP_RetailTradeDailyReportSummary_Retrieve1(
	OUT iErrorCode INT,   	-- 错误码
	OUT sErrorMsg VARCHAR(64),
	IN iShopID INT,
   	IN dtStart DATETIME,	-- 开始时间	
	IN dtEnd DATETIME		-- 结束时间
)
BEGIN
	DECLARE iTotalRecord INT;
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		-- SELECT * FROM t_retailtradedailyreportSummary;
		
		SELECT TotalNO, PricePurchase, TotalAmount, TotalGross, RatioGrossMargin
		FROM 
		(
			SELECT 
				sum(F_TotalNO) AS TotalNO,								    -- 销售笔数
				sum(F_PricePurchase) AS PricePurchase,					    -- 总进货价
				sum(F_TotalAmount) AS TotalAmount,			    		    -- 销售额
				sum(F_TotalAmount) - sum(F_PricePurchase) AS TotalGross, 	-- 销售毛利 
				IF (sum(F_TotalAmount) = 0, 0.000000, (sum(F_TotalAmount) - sum(F_PricePurchase)) / sum(F_TotalAmount)) AS RatioGrossMargin	-- 销售毛利率  
			FROM t_retailtradedailyreportSummary
			WHERE F_Datetime BETWEEN dtStart AND dtEnd -- 查询某一个时间段的销售数据,将时间段内的结果进行汇总
			AND F_ShopID = iShopID
		) AS tmp
		WHERE TotalNO IS NOT NULL;
		
		IF (found_rows() = 0) THEN 
			SET iErrorCode := 2;  
			SET sErrorMsg := '该日期无销售数据';
		ELSE 		
		 	-- 计算一段时间内销售最高的商品名称（显示金额和名称）
		 	SELECT max(TotalAmount) AS topSalesAmount, Name AS commodityName
		 	FROM 
		 	(
			 	SELECT 
			 		sum(F_TotalAmount) AS TotalAmount,
			 		(SELECT F_Name FROM t_commodity WHERE F_ID = rtrs.F_TopSaleCommodityID) AS Name
			 	FROM t_retailtradedailyreportSummary rtrs
			 	WHERE F_Datetime BETWEEN dtStart AND dtEnd
			 	AND rtrs.F_ShopID = iShopID
			 	GROUP BY rtrs.F_TopSaleCommodityID
			) AS tmp;
	
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
	
	COMMIT;
END;	
			