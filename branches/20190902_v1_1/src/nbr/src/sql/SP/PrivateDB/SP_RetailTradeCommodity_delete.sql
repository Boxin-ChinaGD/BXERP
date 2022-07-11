DROP PROCEDURE IF EXISTS `SP_RetailTradeCommodity_delete`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_RetailTradeCommodity_delete` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iTradeID INT
	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := 'Êý¾Ý¿â´íÎó';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		DELETE FROM t_retailtradecommodity WHERE F_TradeID = iTradeID;
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END;