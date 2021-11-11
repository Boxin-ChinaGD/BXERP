DROP PROCEDURE IF EXISTS `SP_RetailTradeCoupon_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_RetailTradeCoupon_RetrieveN`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iRetailTradeID INT,
	IN iPageIndex INT,
	IN iPageSize INT,
	OUT iTotalRecord INT
	)
BEGIN
	DECLARE recordIndex INT;
	DECLARE INVALID_ID INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := 'Êý¾Ý¿â´íÎó';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
		SELECT F_Value INTO INVALID_ID FROM t_nbrconstant WHERE F_Key = 'INVALID_ID';
		
		SET iPageIndex = iPageIndex - 1;
	  	SET recordIndex = iPageIndex * iPageSize;
		
		SELECT 
			F_ID, 
			F_RetailTradeID, 
			F_CouponCodeID, 
			F_SyncDatetime
		FROM t_retailtradecoupon 
		WHERE 1 = 1
		AND (CASE iRetailTradeID WHEN INVALID_ID THEN 1 = 1 ELSE F_RetailTradeID = iRetailTradeID END)
		ORDER BY F_ID DESC LIMIT recordIndex, iPageSize;

		SELECT count(1) into iTotalRecord FROM t_retailtradecoupon 
		WHERE 1 = 1 AND (CASE iRetailTradeID WHEN INVALID_ID THEN 1 = 1 ELSE F_RetailTradeID = iRetailTradeID END);
	COMMIT;
END;