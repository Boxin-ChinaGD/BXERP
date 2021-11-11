DROP PROCEDURE IF EXISTS `SP_RetailTradeReportByCommodity_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_RetailTradeReportByCommodity_RetrieveN`(
   	OUT iErrorCode INT,   	-- 错误码
   	OUT sErrorMsg VARCHAR(64),
    OUT iTotalRecord INT, 	-- 总记录数
   	IN dtStart DATE,		-- 销售日期的开始时间	
	IN dtEnd DATETIME,		-- 销售日期的结束时间
	IN string1 VARCHAR(32),	-- 用于模糊查询的状态
	IN isASC INT,		   	-- 升序	
	IN iOrderBy INT,		-- 根据状态进行排序   0金额，1数量，2毛利
	IN iPageIndex INT,		-- 页数
	IN iPageSize INT,		-- 页数量
	IN bIgnoreZeroNO INT,	-- 排查为0    1不显示销售数量为0的商品， 0则显示
	IN iCategoryID INT,		-- 商品分类ID   -1代表查询所有的商品分类
	IN iShopID INT
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
	
		-- 关联的关系为：一张零售单对应多个零售单商品表  一张零售单商品表对应一个商品和一个条形码
		SELECT F_ID, F_Name, F_Specification, F_PackageUnitName, F_Barcode, F_NO, F_Amouunt, F_TradeID, F_AveragePrice, F_GrossMargin, F_SaleDatetime  
		FROM(
			SELECT
				comm.F_ID AS F_ID,
				comm.F_Name AS F_Name,
				comm.F_Specification AS F_Specification,  					-- 规格
				(SELECT F_Name FROM t_packageunit pu WHERE pu.F_ID IN ( SELECT comm.F_PackageUnitID FROM t_commodity WHERE comm.F_PackageUnitID = pu.F_ID )) AS F_PackageUnitName, -- 包装单位名称
				(SELECT group_concat(F_Barcode) FROM t_barcodes bc WHERE bc.F_ID = rtc.F_BarcodeID) AS F_Barcode, -- 条形码
				rtc.F_NO AS F_NO,
			    commShopInfo.F_PriceRetail * rtc.F_NO AS F_Amouunt,					-- 金额
			    rtc.F_TradeID AS F_TradeID,                 				-- 零售单商品表中的主表id(零售单id)
			    (commShopInfo.F_PriceRetail * rtc.F_NO) / rtc.F_NO AS F_AveragePrice, -- 销售均价
			    rtc.F_NO / (commShopInfo.F_PriceRetail * rtc.F_NO) AS F_GrossMargin,  -- 销售毛利   -- ...  (还未研究出来 暂时空着)
				rt.F_SaleDatetime AS F_SaleDatetime
			FROM T_RetailTradeCommodity AS rtc, t_commodity AS comm, t_retailtrade AS rt, t_commodityshopinfo AS commShopInfo
			WHERE rtc.F_CommodityID = comm.F_ID AND rtc.F_TradeID = rt.F_ID AND commShopInfo.F_CommodityID = comm.F_ID AND commShopInfo.F_ShopID = iShopID
				  AND (CASE iCategoryID WHEN INVALID_ID THEN 1=1 ELSE comm.F_CategoryID = iCategoryID END)  -- 按分类id进行查询
		)AS tmp
		WHERE (F_Name LIKE CONCAT('%',replace(string1, '_', '\_'), '%') OR F_Barcode = string1 OR F_TradeID = string1) 
			  AND (CASE bIgnoreZeroNO WHEN 1 THEN F_NO > 0 ELSE 1 = 1 END) 									-- 过滤为0的销售商品
		 	  AND F_SaleDatetime BETWEEN dtStart AND dtEnd
		ORDER BY
			  -- 按商品 按单据进行排序
			  -- iOrderBy为(0,1,2)时按销售金额，销售数量，毛利率排序，如果isASC为1时，则按升序排序
	    	  CASE isASC WHEN 0 THEN (CASE iOrderBy WHEN 0 THEN tmp.F_Amouunt WHEN 1 THEN tmp.F_NO ELSE tmp.F_GrossMargin END) END DESC,
			  CASE isASC WHEN 1 THEN (CASE iOrderBy WHEN 0 THEN tmp.F_Amouunt WHEN 1 THEN tmp.F_NO ELSE tmp.F_GrossMargin END) END ASC
		LIMIT recordIndex, iPageSize;
	
	 
		-- 计算出销售报表的总条数
		SELECT count(1) INTO iTotalRecord
		FROM(
			SELECT
				comm.F_ID AS F_ID,
				comm.F_Name AS F_Name,
				comm.F_Specification AS F_Specification,  					-- 规格
				(SELECT F_Name FROM t_packageunit pu WHERE pu.F_ID IN ( SELECT comm.F_PackageUnitID FROM t_commodity WHERE comm.F_PackageUnitID = pu.F_ID )) AS F_PackageUnitName, -- 包装单位名称
				(SELECT group_concat(F_Barcode) FROM t_barcodes bc WHERE bc.F_ID = rtc.F_BarcodeID) AS F_Barcode, -- 条形码
				rtc.F_NO AS F_NO,
			    commShopInfo.F_PriceRetail * rtc.F_NO AS F_Amouunt,					-- 金额
			    rtc.F_TradeID AS F_TradeID,                 				-- 零售单商品表中的主表id(零售单id)
			    (commShopInfo.F_PriceRetail * rtc.F_NO) / rtc.F_NO AS F_AveragePrice, -- 销售均价
			    rtc.F_NO / (commShopInfo.F_PriceRetail * rtc.F_NO) AS F_GrossMargin,  -- 销售毛利   -- ...  (还未研究出来 暂时空着)
				rt.F_SaleDatetime AS F_SaleDatetime
			FROM T_RetailTradeCommodity AS rtc, t_commodity AS comm, t_retailtrade AS rt, t_commodityshopinfo AS commShopInfo
			WHERE rtc.F_CommodityID = comm.F_ID AND rtc.F_TradeID = rt.F_ID  AND commShopInfo.F_CommodityID = comm.F_ID AND commShopInfo.F_ShopID = iShopID
				  AND (CASE iCategoryID WHEN INVALID_ID THEN 1=1 ELSE comm.F_CategoryID = iCategoryID END)  -- 按分类id进行查询
		)AS tmp
		WHERE (F_Name LIKE CONCAT('%',replace(string1, '_', '\_'), '%') OR F_Barcode = string1 OR F_TradeID = string1) 
			  AND (CASE bIgnoreZeroNO WHEN 1 THEN F_NO > 0 ELSE 1 = 1 END) 									-- 过滤为0的销售商品
		 	  AND F_SaleDatetime BETWEEN dtStart AND dtEnd
		ORDER BY
			  -- 按商品 按单据进行排序
			  -- iOrderBy为(0,1,2)时按销售金额，销售数量，毛利率排序，如果isASC为1时，则按升序排序
	    	  CASE isASC WHEN 0 THEN (CASE iOrderBy WHEN 0 THEN tmp.F_Amouunt WHEN 1 THEN tmp.F_NO ELSE tmp.F_GrossMargin END) END DESC,
			  CASE isASC WHEN 1 THEN (CASE iOrderBy WHEN 0 THEN tmp.F_Amouunt WHEN 1 THEN tmp.F_NO ELSE tmp.F_GrossMargin END) END ASC;
	   
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END;