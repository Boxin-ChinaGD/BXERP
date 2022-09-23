DROP PROCEDURE IF EXISTS SP_RetailTradeDailyReportSummary_Retrieve1;
CREATE DEFINER=`root`@`localhost` PROCEDURE SP_RetailTradeDailyReportSummary_Retrieve1(
	OUT iErrorCode INT,   	-- ������
	OUT sErrorMsg VARCHAR(64),
	IN iShopID INT,
   	IN dtStart DATETIME,	-- ��ʼʱ��	
	IN dtEnd DATETIME		-- ����ʱ��
)
BEGIN
	DECLARE iTotalRecord INT;
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		-- SELECT * FROM t_retailtradedailyreportSummary;
		
		SELECT TotalNO, PricePurchase, TotalAmount, TotalGross, RatioGrossMargin
		FROM 
		(
			SELECT 
				sum(F_TotalNO) AS TotalNO,								    -- ���۱���
				sum(F_PricePurchase) AS PricePurchase,					    -- �ܽ�����
				sum(F_TotalAmount) AS TotalAmount,			    		    -- ���۶�
				sum(F_TotalAmount) - sum(F_PricePurchase) AS TotalGross, 	-- ����ë�� 
				IF (sum(F_TotalAmount) = 0, 0.000000, (sum(F_TotalAmount) - sum(F_PricePurchase)) / sum(F_TotalAmount)) AS RatioGrossMargin	-- ����ë����  
			FROM t_retailtradedailyreportSummary
			WHERE F_Datetime BETWEEN dtStart AND dtEnd -- ��ѯĳһ��ʱ��ε���������,��ʱ����ڵĽ�����л���
			AND F_ShopID = iShopID
		) AS tmp
		WHERE TotalNO IS NOT NULL;
		
		IF (found_rows() = 0) THEN 
			SET iErrorCode := 2;  
			SET sErrorMsg := '����������������';
		ELSE 		
		 	-- ����һ��ʱ����������ߵ���Ʒ���ƣ���ʾ�������ƣ�
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
			