DROP PROCEDURE IF EXISTS `SP_RetailTradeDailyReportByStaff_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_RetailTradeDailyReportByStaff_RetrieveN`(
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
			ifnull(sum(F_NO), 0) AS F_NO,
			ifnull(sum(F_TotalAmount), 0) AS F_TotalAmount ,
			ifnull(sum(F_GrossMargin), 0) AS F_GrossMargin,
			(SELECT F_Name FROM t_staff WHERE F_ID = F_StaffID) AS staffName,
			F_StaffID
		FROM T_RetailTradeDailyReportByStaff
		WHERE 1=1
			  AND (CASE ifnull(dtStart, 0) WHEN 0 THEN 1=1 ELSE F_Datetime >= dtStart END)
	  		  AND (CASE ifnull(dtEnd, 0) WHEN 0 THEN 1=1 ELSE F_Datetime <= dtEnd END)
	  		  AND F_ShopID = iShopID
	  		  GROUP BY F_StaffID;
		
			
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END; 