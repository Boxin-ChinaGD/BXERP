DROP PROCEDURE IF EXISTS `SP_RetailTradeDailyReportByCategoryParent_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_RetailTradeDailyReportByCategoryParent_RetrieveN`(
	OUT iErrorCode INT,   	-- ������
	OUT sErrorMsg VARCHAR(64),
	IN iShopID INT,
   	IN dtStart DATETIME,	-- ��ʼʱ��	
	IN dtEnd DATETIME		-- ����ʱ��
)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;

		SELECT
			F_ShopID,  
			rdrc.F_CategoryParentID,
			(SELECT F_Name 
			FROM t_categoryparent 
			WHERE F_ID = rdrc.F_CategoryParentID) AS categoryParentName,
			sum(rdrc.F_TotalAmount) AS totalAmountSummary
		FROM t_retailtradedailyreportbycategoryparent rdrc
		WHERE  F_Datetime BETWEEN dtStart AND dtEnd 
		AND rdrc.F_ShopID = iShopID
		GROUP BY F_CategoryParentID;
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END;