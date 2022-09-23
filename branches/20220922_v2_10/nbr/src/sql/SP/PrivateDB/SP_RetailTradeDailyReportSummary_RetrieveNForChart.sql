DROP PROCEDURE IF EXISTS `SP_RetailTradeDailyReportSummary_RetrieveNForChart`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_RetailTradeDailyReportSummary_RetrieveNForChart`(
	OUT iErrorCode INT,		-- ������
	OUT sErrorMsg VARCHAR(64),
	IN iShopID INT, 
	IN dtStart DATE,		-- ��ʼʱ��
	IN dtEnd DATETIME		-- ����ʱ��
)
BEGIN
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode:=3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SELECT 
			F_ID,
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
		WHERE F_Datetime BETWEEN dtStart AND dtEnd
		AND F_ShopID = iShopID;
		
		
		SET iErrorCode :=0;
		SET sErrorMsg := '';

	
	COMMIT;
END;