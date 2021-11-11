DROP PROCEDURE IF EXISTS `SP_RetailTradeDailyReportByCommodity_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_RetailTradeDailyReportByCommodity_RetrieveN`(
   	OUT iErrorCode INT,   	-- 错误码
   	OUT sErrorMsg VARCHAR(64),
    OUT iTotalRecord INT, 	-- 总记录数
    IN iShopID INT,
   	IN dtStart DATE,		-- 开始时间	
	IN dtEnd DATETIME,		-- 结束时间
	IN string1 VARCHAR(32),	-- 用于模糊查询的状态
	IN isASC INT,		   	-- 升序	
	IN iOrderBy INT,		-- 根据状态进行排序   0金额，1数量，2毛利
	IN iPageIndex INT,		-- 页数
	IN iPageSize INT,		-- 页数量
	IN bIgnoreZeroNO INT,	-- 排查为0    1不显示销售数量为0的商品， 0则显示
	IN iCategoryID INT		-- 商品分类ID   -1代表查询所有的商品分类
)
BEGIN
	DECLARE recordIndex INT;
	DECLARE INVALID_ID INT;
	
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;	
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SELECT F_Value INTO INVALID_ID FROM t_nbrconstant WHERE F_Key = 'INVALID_ID';  -- -1
		SET iPageIndex = iPageIndex - 1;
		SET recordIndex = iPageIndex * iPageSize;
		
		-- SELECT * FROM T_RetailTradeDailyReportByCommodity;
	
		-- 关联的关系为：一张零售单对应多个零售单商品表  一张零售单商品表对应一个商品和一个条形码
		SELECT F_ShopID, F_Barcode, F_Name, F_CategoryID, F_Specification, F_PurchasingUnit, F_CommodityID, F_TotalPurchasingAmount, F_NO, F_TotalAmount, F_AveragePrice, F_GrossMargin 
		FROM (
			SELECT F_ShopID, F_Barcode, F_Name, F_CategoryID, F_Specification, F_PurchasingUnit, F_CommodityID, sum(F_TotalPurchasingAmount) AS F_TotalPurchasingAmount, sum(F_NO) AS F_NO, sum(F_TotalAmount) AS F_TotalAmount, sum(F_AveragePrice) AS F_AveragePrice, sum(F_GrossMargin) AS F_GrossMargin 
			FROM (
				SELECT
				  --	group_concat(rtc.F_TradeID) AS F_ID,										-- 零售单号
				  	rtbc.F_ID,
				  	rtbc.F_ShopID AS F_ShopID,
					(SELECT F_Barcode FROM t_barcodes WHERE F_ID in (SELECT F_BarcodeID FROM t_retailtradecommodity WHERE F_ID = rtc.F_ID)) AS F_Barcode , -- 条形码						
					comm.F_Name AS F_Name,										-- 商品名称
					comm.F_CategoryID AS F_CategoryID,
					comm.F_Specification AS F_Specification,					-- 规格
					comm.F_PurchasingUnit AS F_PurchasingUnit,					-- 包装单位
					rtbc.F_CommodityID AS F_CommodityID,
					rtbc.F_TotalPurchasingAmount AS F_TotalPurchasingAmount,	-- 进货总额
					rtbc.F_NO AS F_NO,											-- 销售数量
					rtbc.F_TotalAmount AS F_TotalAmount,						-- 销售总额
					rtbc.F_TotalAmount / rtbc.F_NO AS F_AveragePrice,			-- 销售均价
					rtbc.F_GrossMargin AS F_GrossMargin -- ,					-- 销售毛利
--					rtbc.F_Datetime	AS F_Datetime 								-- 统计日期
				FROM T_RetailTradeDailyReportByCommodity AS rtbc, t_commodity AS comm, t_retailtradecommodity AS rtc
				WHERE rtbc.F_CommodityID = comm.F_ID AND comm.F_ID = rtc.F_CommodityID
					  AND (CASE iCategoryID WHEN INVALID_ID THEN 1=1 ELSE comm.F_CategoryID = iCategoryID END)  -- 按分类id进行查询
					  AND rtbc.F_Datetime BETWEEN dtStart AND dtEnd
				  	  AND (CASE bIgnoreZeroNO WHEN 1 THEN rtbc.F_NO > 0 ELSE 1 = 1 END) 									-- 过滤为0的销售商品 
				  	  AND rtbc.F_ShopID = iShopID
			    GROUP BY rtbc.F_ID
			) AS tmp 
			WHERE (CASE string1 WHEN '' THEN 1=1 ELSE (F_Name LIKE CONCAT('%',replace(string1, '_', '\_'), '%') OR F_Barcode = string1) END)
			GROUP BY tmp.F_Name			 	  
		) tmp2 
	   	ORDER BY
			  -- 按商品 按单据进行排序
			  -- iOrderBy为(0,1,2)时按销售金额，销售数量，毛利排序，如果isASC为1时，则按升序排序
	    	  CASE isASC WHEN 0 THEN (CASE iOrderBy WHEN 0 THEN tmp2.F_TotalAmount WHEN 1 THEN tmp2.F_NO ELSE tmp2.F_GrossMargin END) END DESC,
	   		  CASE isASC WHEN 1 THEN (CASE iOrderBy WHEN 0 THEN tmp2.F_TotalAmount WHEN 1 THEN tmp2.F_NO ELSE tmp2.F_GrossMargin END) END ASC,
	   		  tmp2.F_CommodityID DESC
		LIMIT recordIndex, iPageSize;
	
	
		SELECT count(1) INTO iTotalRecord   
		FROM(
			SELECT group_concat(F_ID), F_Barcode, F_Name, F_CategoryID, F_Specification, F_PurchasingUnit, F_CommodityID, F_TotalPurchasingAmount, F_NO, F_TotalAmount, F_AveragePrice, F_GrossMargin, F_Datetime 
			FROM (
				SELECT F_ID, F_Barcode, F_Name, F_CategoryID, F_Specification, F_PurchasingUnit, F_CommodityID, F_TotalPurchasingAmount, F_NO, F_TotalAmount, F_AveragePrice, F_GrossMargin, F_Datetime  
				FROM (
					SELECT
						rtc.F_TradeID AS F_ID,										-- 零售单号
						(SELECT F_Barcode FROM t_barcodes WHERE F_ID in (SELECT F_BarcodeID FROM t_retailtradecommodity WHERE F_ID = rtc.F_ID)) AS F_Barcode , -- 条形码						
						comm.F_Name AS F_Name,										-- 商品名称
						comm.F_CategoryID AS F_CategoryID,
						comm.F_Specification AS F_Specification,					-- 规格
						comm.F_PurchasingUnit AS F_PurchasingUnit,					-- 包装单位
						rtbc.F_CommodityID AS F_CommodityID,
						rtbc.F_TotalPurchasingAmount AS F_TotalPurchasingAmount,	-- 进货总额
						rtbc.F_NO AS F_NO,											-- 销售数量
						rtbc.F_TotalAmount AS F_TotalAmount,						-- 销售总额
						rtbc.F_TotalAmount / rtbc.F_NO AS F_AveragePrice,			-- 销售均价
						rtbc.F_GrossMargin AS F_GrossMargin,						-- 销售毛利
						rtbc.F_Datetime	AS F_Datetime 								-- 统计日期
					FROM T_RetailTradeDailyReportByCommodity AS rtbc, t_commodity AS comm, t_retailtradecommodity AS rtc
					WHERE rtbc.F_CommodityID = comm.F_ID AND comm.F_ID = rtc.F_CommodityID
						  AND (CASE iCategoryID WHEN INVALID_ID THEN 1=1 ELSE comm.F_CategoryID = iCategoryID END)  -- 按分类id进行查询
						  AND rtbc.F_ShopID = iShopID
				) AS tmp
				WHERE (F_Name LIKE CONCAT('%',replace(string1, '_', '\_'), '%') OR F_Barcode = string1 OR F_ID = string1) 
					  AND (CASE bIgnoreZeroNO WHEN 1 THEN F_NO > 0 ELSE 1 = 1 END) 									-- 过滤为0的销售商品
				 	  AND F_Datetime BETWEEN dtStart AND dtEnd
			) tmp2 GROUP BY tmp2.F_Name
		)AS tmp;
--		WHERE (F_Name LIKE CONCAT('%',string1, '%') OR F_Barcode = string1 OR F_ID = string1) 
--			  AND (CASE bIgnoreZeroNO WHEN 1 THEN F_NO > 0 ELSE 1 = 1 END) 									-- 过滤为0的销售商品
--		 	  AND F_Datetime BETWEEN dtStart AND dtEnd;
		 	  
	
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END;