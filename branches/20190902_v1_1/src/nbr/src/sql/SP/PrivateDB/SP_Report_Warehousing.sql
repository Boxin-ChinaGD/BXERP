-- 入库报表是统计已审核的入库单，未审核的不进行统计
-- TODO 2019.11.25 tzq  现在未进行入库单状态统计，以后要重构

DROP PROCEDURE IF EXISTS `SP_Report_Warehousing`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Report_Warehousing` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN dtStart DATE,
	IN dtEnd DATETIME,
	IN iOrderBy INT,
	IN isASC INT,
	IN string1 VARCHAR(32),
	IN bIgnoreZeroNO INT,
	IN iCategoryID INT,
	IN iPageIndex INT,
	IN iPageSize INT,
	OUT iTotalRecord INT
	)
BEGIN
	DECLARE recordIndex INT;
	DECLARE iTotalWarehousingNumber INT;
	DECLARE sCommodityName VARCHAR(32);
	DECLARE fCommodityMaxAmount DECIMAL(20, 6);
	DECLARE sProviderName VARCHAR(32);
	DECLARE fProviderMaxAmount DECIMAL(20, 6);
	DECLARE iTotalWarehousingAmount DECIMAL(20, 6);
	DECLARE INVALID_ID INT;
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SELECT F_Value INTO INVALID_ID FROM t_nbrconstant WHERE F_Key = 'INVALID_ID'; 
		
		SET iPageIndex = iPageIndex - 1;
		
		SET recordIndex = iPageIndex * iPageSize;
		
		-- iOrderBy为(0,1,2)时按ID，进货数量，进货总额排序，如果isASC为1时，则按升序排序
		SELECT F_ID, F_Name, F_Barcode, F_Specification, F_ProviderName, F_PackageUnitName, F_NO, F_Price, F_Amount, F_WarehousingDatetime FROM 	
		(
			SELECT tmp.ID AS F_ID, tmp.Name AS F_Name, tmp.Barcode AS F_Barcode, tmp.Specification AS F_Specification, group_concat(ProviderName) AS F_ProviderName, tmp.PackageUnitName AS F_PackageUnitName, 
				sum(NO) AS F_NO, SUM(Amount) / SUM(NO) AS F_Price, sum(Amount) AS F_Amount ,WarehousingDatetime AS F_WarehousingDatetime
			FROM
			(
				SELECT 
				comm.F_ID AS ID,
				comm.F_Name AS Name,
				comm.F_Specification AS Specification,
				(SELECT F_Name FROM t_packageunit pu WHERE pu.F_ID IN (SELECT comm.F_PackageUnitID FROM t_commodity WHERE pu.F_ID = comm.F_PackageUnitID)) AS PackageUnitName,
				(SELECT group_concat(F_Barcode) FROM t_barcodes bar WHERE bar.F_CommodityID = wc.F_CommodityID) AS Barcode, 
				(SELECT F_Name FROM t_provider pv 
					WHERE pv.F_ID = 
					(
						SELECT F_ProviderID FROM t_purchasingorder po 
							WHERE po.F_ID = 
							(
								SELECT F_PurchasingOrderID FROM t_warehousing WHERE F_ID = wc.F_WarehousingID
							)
					)
				) AS ProviderName,
				wc.F_NO AS NO,
				wc.F_Price AS Price,
				wc.F_Amount AS Amount,
				wc.F_UpdateDatetime AS WarehousingDatetime
				FROM t_warehousingcommodity wc, t_commodity comm
				WHERE  wc.F_CommodityID = comm.F_ID AND comm.F_Status <> 2 AND (CASE iCategoryID WHEN INVALID_ID THEN 1=1 ELSE comm.F_CategoryID = iCategoryID END)AND
					   (
					   		-- 根据商品名称和条形码模糊搜索
					   		EXISTS (SELECT 1 FROM t_barcodes WHERE F_Barcode = string1 AND F_CommodityID = comm.F_ID)                        
					        OR comm.F_Name LIKE CONCAT('%', string1, '%')        
					    )
			) AS tmp
			WHERE tmp.WarehousingDatetime BETWEEN dtStart AND dtEnd
			GROUP BY tmp.ID
		) AS tmp
		WHERE (CASE bIgnoreZeroNO WHEN 1 THEN F_NO > 0 ELSE 1 = 1 END)
		ORDER BY
			  CASE isASC WHEN 0 THEN (CASE iOrderBy WHEN 0 THEN tmp.F_ID WHEN 1 THEN tmp.F_NO ELSE tmp.F_Amount END) END DESC,
			  CASE isASC WHEN 1 THEN (CASE iOrderBy WHEN 0 THEN tmp.F_ID WHEN 1 THEN tmp.F_NO ELSE tmp.F_Amount END) END ASC
		LIMIT recordIndex, iPageSize;
		
		-- 计算进货报表的总条数
		SELECT count(1) into iTotalRecord
		FROM 
		(
			SELECT 1
			FROM
			(
				SELECT 
				comm.F_ID AS ID,
				wc.F_UpdateDatetime AS WarehousingDatetime
				FROM t_warehousingcommodity wc, t_commodity comm 
				WHERE  wc.F_CommodityID = comm.F_ID AND comm.F_Status <> 2
				ORDER BY comm.F_Name ASC
			) AS tmp
			WHERE tmp.WarehousingDatetime BETWEEN dtStart AND dtEnd GROUP BY tmp.ID ASC
		) AS tmp2;
		
		-- 计算进货笔数（入库次数）
		SELECT count(1) INTO iTotalWarehousingNumber FROM (select distinct F_WarehousingID FROM t_warehousingcommodity WHERE F_UpdateDatetime BETWEEN dtStart AND dtEnd) AS tmp;
		
		-- 计算进货额最高的商品
		SELECT Name, fAmount into sCommodityName, fCommodityMaxAmount
		FROM 
		(
			SELECT ID, Name, sum(Amount) AS fAmount
			FROM
			(
				SELECT 
				comm.F_Name AS Name,
				comm.F_ID AS ID,
				wc.F_Amount AS Amount,
				wc.F_UpdateDatetime AS WarehousingDatetime
				FROM t_warehousingcommodity wc, t_commodity comm 
				WHERE  wc.F_CommodityID = comm.F_ID AND comm.F_Status <> 2
				ORDER BY comm.F_Name ASC
			) AS tmp
			WHERE tmp.WarehousingDatetime BETWEEN dtStart AND dtEnd
			GROUP BY tmp.ID, tmp.Name ASC
		) AS tmp
		ORDER BY fAmount desc
		limit 1;
		
		--	计算进货总额最高的供应商
		SELECT ProviderName, SUM(Amount) into sProviderName, fProviderMaxAmount 
		FROM
		(
			SELECT 
			comm.F_ID AS ID,
			(SELECT F_Name FROM t_provider pv 
				WHERE pv.F_ID = 
				(
					SELECT F_ProviderID FROM t_purchasingorder po 
						WHERE po.F_ID = 
						(
							SELECT F_PurchasingOrderID FROM t_warehousing WHERE F_ID = wc.F_WarehousingID
						)
				)
			) AS ProviderName,
			wc.F_Amount AS Amount,
			wc.F_UpdateDatetime AS WarehousingDatetime
			FROM t_warehousingcommodity wc, t_commodity comm 
			WHERE  wc.F_CommodityID = comm.F_ID AND comm.F_Status <> 2
		) AS tmp
		WHERE tmp.WarehousingDatetime BETWEEN dtStart AND dtEnd
		GROUP BY tmp.ProviderName 
		ORDER BY amount desc 
		LIMIT 1;
		
		-- 计算报表进货额总和
		SELECT sum(fAmouunt) INTO iTotalWarehousingAmount 
		FROM (
				SELECT sum(Amount) AS fAmouunt
				FROM
				(
					SELECT 
					comm.F_ID AS ID,
					comm.F_Name AS Name,
					wc.F_Amount AS Amount,
					wc.F_UpdateDatetime AS WarehousingDatetime
					FROM t_warehousingcommodity wc, t_commodity comm 
					WHERE  wc.F_CommodityID = comm.F_ID AND comm.F_Status <> 2
					ORDER BY comm.F_Name ASC
				) AS tmp
				WHERE tmp.WarehousingDatetime BETWEEN dtStart AND dtEnd
			) AS tmp;
		
		SELECT  iTotalWarehousingNumber, sCommodityName, fCommodityMaxAmount, sProviderName, fProviderMaxAmount, iTotalWarehousingAmount;
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END;  