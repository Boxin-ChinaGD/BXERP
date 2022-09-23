DROP PROCEDURE IF EXISTS `SP_RetailTradeDailyReportByCommodity_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_RetailTradeDailyReportByCommodity_RetrieveN`(
   	OUT iErrorCode INT,   	-- ������
   	OUT sErrorMsg VARCHAR(64),
    OUT iTotalRecord INT, 	-- �ܼ�¼��
    IN iShopID INT,
   	IN dtStart DATE,		-- ��ʼʱ��	
	IN dtEnd DATETIME,		-- ����ʱ��
	IN string1 VARCHAR(32),	-- ����ģ����ѯ��״̬
	IN isASC INT,		   	-- ����	
	IN iOrderBy INT,		-- ����״̬��������   0��1������2ë��
	IN iPageIndex INT,		-- ҳ��
	IN iPageSize INT,		-- ҳ����
	IN bIgnoreZeroNO INT,	-- �Ų�Ϊ0    1����ʾ��������Ϊ0����Ʒ�� 0����ʾ
	IN iCategoryID INT		-- ��Ʒ����ID   -1�����ѯ���е���Ʒ����
)
BEGIN
	DECLARE recordIndex INT;
	DECLARE INVALID_ID INT;
	
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;	
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SELECT F_Value INTO INVALID_ID FROM t_nbrconstant WHERE F_Key = 'INVALID_ID';  -- -1
		SET iPageIndex = iPageIndex - 1;
		SET recordIndex = iPageIndex * iPageSize;
		
		-- SELECT * FROM T_RetailTradeDailyReportByCommodity;
	
		-- �����Ĺ�ϵΪ��һ�����۵���Ӧ������۵���Ʒ��  һ�����۵���Ʒ���Ӧһ����Ʒ��һ��������
		SELECT F_ShopID, F_Barcode, F_Name, F_CategoryID, F_Specification, F_PurchasingUnit, F_CommodityID, F_TotalPurchasingAmount, F_NO, F_TotalAmount, F_AveragePrice, F_GrossMargin 
		FROM (
			SELECT F_ShopID, F_Barcode, F_Name, F_CategoryID, F_Specification, F_PurchasingUnit, F_CommodityID, sum(F_TotalPurchasingAmount) AS F_TotalPurchasingAmount, sum(F_NO) AS F_NO, sum(F_TotalAmount) AS F_TotalAmount, sum(F_AveragePrice) AS F_AveragePrice, sum(F_GrossMargin) AS F_GrossMargin 
			FROM (
				SELECT
				  --	group_concat(rtc.F_TradeID) AS F_ID,										-- ���۵���
				  	rtbc.F_ID,
				  	rtbc.F_ShopID AS F_ShopID,
					(SELECT F_Barcode FROM t_barcodes WHERE F_ID in (SELECT F_BarcodeID FROM t_retailtradecommodity WHERE F_ID = rtc.F_ID)) AS F_Barcode , -- ������						
					comm.F_Name AS F_Name,										-- ��Ʒ����
					comm.F_CategoryID AS F_CategoryID,
					comm.F_Specification AS F_Specification,					-- ���
					comm.F_PurchasingUnit AS F_PurchasingUnit,					-- ��װ��λ
					rtbc.F_CommodityID AS F_CommodityID,
					rtbc.F_TotalPurchasingAmount AS F_TotalPurchasingAmount,	-- �����ܶ�
					rtbc.F_NO AS F_NO,											-- ��������
					rtbc.F_TotalAmount AS F_TotalAmount,						-- �����ܶ�
					rtbc.F_TotalAmount / rtbc.F_NO AS F_AveragePrice,			-- ���۾���
					rtbc.F_GrossMargin AS F_GrossMargin -- ,					-- ����ë��
--					rtbc.F_Datetime	AS F_Datetime 								-- ͳ������
				FROM T_RetailTradeDailyReportByCommodity AS rtbc, t_commodity AS comm, t_retailtradecommodity AS rtc
				WHERE rtbc.F_CommodityID = comm.F_ID AND comm.F_ID = rtc.F_CommodityID
					  AND (CASE iCategoryID WHEN INVALID_ID THEN 1=1 ELSE comm.F_CategoryID = iCategoryID END)  -- ������id���в�ѯ
					  AND rtbc.F_Datetime BETWEEN dtStart AND dtEnd
				  	  AND (CASE bIgnoreZeroNO WHEN 1 THEN rtbc.F_NO > 0 ELSE 1 = 1 END) 									-- ����Ϊ0��������Ʒ 
				  	  AND rtbc.F_ShopID = iShopID
			    GROUP BY rtbc.F_ID
			) AS tmp 
			WHERE (CASE string1 WHEN '' THEN 1=1 ELSE (F_Name LIKE CONCAT('%',replace(string1, '_', '\_'), '%') OR F_Barcode = string1) END)
			GROUP BY tmp.F_Name			 	  
		) tmp2 
	   	ORDER BY
			  -- ����Ʒ �����ݽ�������
			  -- iOrderByΪ(0,1,2)ʱ�����۽�����������ë���������isASCΪ1ʱ������������
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
						rtc.F_TradeID AS F_ID,										-- ���۵���
						(SELECT F_Barcode FROM t_barcodes WHERE F_ID in (SELECT F_BarcodeID FROM t_retailtradecommodity WHERE F_ID = rtc.F_ID)) AS F_Barcode , -- ������						
						comm.F_Name AS F_Name,										-- ��Ʒ����
						comm.F_CategoryID AS F_CategoryID,
						comm.F_Specification AS F_Specification,					-- ���
						comm.F_PurchasingUnit AS F_PurchasingUnit,					-- ��װ��λ
						rtbc.F_CommodityID AS F_CommodityID,
						rtbc.F_TotalPurchasingAmount AS F_TotalPurchasingAmount,	-- �����ܶ�
						rtbc.F_NO AS F_NO,											-- ��������
						rtbc.F_TotalAmount AS F_TotalAmount,						-- �����ܶ�
						rtbc.F_TotalAmount / rtbc.F_NO AS F_AveragePrice,			-- ���۾���
						rtbc.F_GrossMargin AS F_GrossMargin,						-- ����ë��
						rtbc.F_Datetime	AS F_Datetime 								-- ͳ������
					FROM T_RetailTradeDailyReportByCommodity AS rtbc, t_commodity AS comm, t_retailtradecommodity AS rtc
					WHERE rtbc.F_CommodityID = comm.F_ID AND comm.F_ID = rtc.F_CommodityID
						  AND (CASE iCategoryID WHEN INVALID_ID THEN 1=1 ELSE comm.F_CategoryID = iCategoryID END)  -- ������id���в�ѯ
						  AND rtbc.F_ShopID = iShopID
				) AS tmp
				WHERE (F_Name LIKE CONCAT('%',replace(string1, '_', '\_'), '%') OR F_Barcode = string1 OR F_ID = string1) 
					  AND (CASE bIgnoreZeroNO WHEN 1 THEN F_NO > 0 ELSE 1 = 1 END) 									-- ����Ϊ0��������Ʒ
				 	  AND F_Datetime BETWEEN dtStart AND dtEnd
			) tmp2 GROUP BY tmp2.F_Name
		)AS tmp;
--		WHERE (F_Name LIKE CONCAT('%',string1, '%') OR F_Barcode = string1 OR F_ID = string1) 
--			  AND (CASE bIgnoreZeroNO WHEN 1 THEN F_NO > 0 ELSE 1 = 1 END) 									-- ����Ϊ0��������Ʒ
--		 	  AND F_Datetime BETWEEN dtStart AND dtEnd;
		 	  
	
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END;