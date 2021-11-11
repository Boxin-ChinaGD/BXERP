DROP PROCEDURE IF EXISTS `SP_RetailTradePromotingFlow_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_RetailTradePromotingFlow_RetrieveN`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iRetailTradePromotingID INT,
	IN iPageIndex INT,
	IN iPageSize INT,
	OUT iTotalRecord INT
	)
BEGIN
	DECLARE recordIndex INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := 'Êý¾Ý¿â´íÎó';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SET iPageIndex = iPageIndex - 1;
		
		SET recordIndex = iPageIndex * iPageSize;
	   	
	    SELECT 
		   F_ID,
		   F_RetailTradePromotingID,
		   F_PromotionID,
		   F_ProcessFlow ,
		   F_CreateDatetime
		FROM T_RetailTradePromotingFlow WHERE F_RetailTradePromotingID = iRetailTradePromotingID ORDER BY F_ID DESC LIMIT recordIndex, iPageSize;
		
	    SELECT count(1) into iTotalRecord
		FROM T_RetailTradePromotingFlow WHERE F_RetailTradePromotingID = iRetailTradePromotingID;
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END;