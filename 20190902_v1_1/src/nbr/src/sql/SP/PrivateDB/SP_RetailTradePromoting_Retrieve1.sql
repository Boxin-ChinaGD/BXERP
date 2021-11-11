DROP PROCEDURE IF EXISTS `SP_RetailTradePromoting_Retrieve1`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_RetailTradePromoting_Retrieve1` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT
	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		IF NOT EXISTS(SELECT 1 FROM T_RetailTradePromoting WHERE F_ID = iID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '不能查询到不存在的ID';
		ELSE
			SELECT F_ID, F_TradeID FROM T_RetailTradePromoting WHERE F_ID = iID;
		
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
	
	COMMIT;
END;
	