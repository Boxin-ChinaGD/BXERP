DROP PROCEDURE IF EXISTS `SP_RetailTradePromoting_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_RetailTradePromoting_RetrieveN` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iTradeID INT,
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
	
		SELECT F_ID, F_TradeID, F_CreateDatetime FROM t_retailtradepromoting 
		WHERE 1=1
		AND (CASE iTradeID WHEN 0 THEN 1=1 ELSE F_TradeID = iTradeID END)
		ORDER BY F_ID DESC
		LIMIT recordIndex, iPageSize;
		
		SELECT COUNT(1) INTO iTotalRecord FROM t_retailtradepromoting 
		WHERE 1=1
		AND (CASE iTradeID WHEN 0 THEN 1=1 ELSE F_TradeID = iTradeID END);
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';

	COMMIT;
END;
	