DROP PROCEDURE IF EXISTS `SP_RetailTradeMonthlyReportSummary_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_RetailTradeMonthlyReportSummary_RetrieveN`(
   	OUT iErrorCode INT,   
   	OUT sErrorMsg VARCHAR(64),
   	IN iShopID INT,	
   	IN dtStart DATETIME,	  
   	IN dtEnd DATETIME
)
BEGIN					
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;	
		SET sErrorMsg := 'Êý¾Ý¿â´íÎó'; 
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SELECT 
			F_ID, 
			F_Datetime, 
			F_TotalAmount, 
			F_TotalGross,
			F_CreateDatetime, 
			F_UpdateDatetime
		FROM t_retailtrademonthlyreportsummary
		WHERE F_Datetime BETWEEN dtStart AND dtEnd
		AND F_ShopID = iShopID;
		
		
		SET iErrorCode := 0; 
		SET sErrorMsg := '';
	
	COMMIT;
END; 