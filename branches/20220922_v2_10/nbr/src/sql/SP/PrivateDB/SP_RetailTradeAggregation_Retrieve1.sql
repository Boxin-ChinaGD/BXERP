
DROP PROCEDURE IF EXISTS `SP_RetailTradeAggregation_Retrieve1`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_RetailTradeAggregation_Retrieve1` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iStaffID INT
   --	IN dtNow DATETIME
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
			F_StaffID, 
			F_PosID,
			F_WorkTimeStart, 
			F_WorkTimeEnd, 
			F_TradeNO, 
			F_Amount, 
			F_ReserveAmount, 
			F_CashAmount, 
			F_WechatAmount, 
			F_AlipayAmount, 
			F_Amount1, 
			F_Amount2, 
			F_Amount3, 
			F_Amount4, 
			F_Amount5, 
			F_UploadDateTime
		FROM t_retailtradeaggregation WHERE F_StaffID = iStaffID AND (CASE IF(TIMESTAMPDIFF(minute,now(),F_UploadDateTime) = 0,0,1) WHEN 0 THEN 1 = 1 ELSE 0 = 1 END)
		ORDER BY F_ID DESC LIMIT 0,1;
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
	COMMIT;
END;