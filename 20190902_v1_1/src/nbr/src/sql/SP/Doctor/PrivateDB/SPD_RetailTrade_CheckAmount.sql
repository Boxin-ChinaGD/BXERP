DROP PROCEDURE IF EXISTS `SPD_RetailTrade_CheckAmount`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_RetailTrade_CheckAmount`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	-- ...临时模板，下面的值需要根据需求变化
	DECLARE iRetailtrade INT;
	DECLARE iPaymentType INT;
	DECLARE iAmount DECIMAL(20,6);
	DECLARE iAmountCash DECIMAL(20,6);
	DECLARE iAmountAlipay DECIMAL(20,6);
	DECLARE iAmountWeChat DECIMAL(20,6);
	DECLARE iAmount1 DECIMAL(20,6);
	DECLARE iAmount2 DECIMAL(20,6);
	DECLARE iAmount3 DECIMAL(20,6);
	DECLARE iAmount4 DECIMAL(20,6);
	DECLARE iAmount5 DECIMAL(20,6);
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- 临时模板，下面list需要根据需求变化
	DECLARE list CURSOR FOR (
		SELECT F_ID AS iRetailtrade,F_PaymentType AS iPaymentType, F_Amount AS iAmount, F_AmountCash AS iAmountCash, 
			F_AmountAlipay AS iAmountAlipay, F_AmountWeChat AS iAmountWeChat, F_Amount1 AS iAmount1, 
			F_Amount2 AS iAmount2, F_Amount3 AS iAmount3, F_Amount4 AS iAmount4, F_Amount5 AS iAmount5 
		FROM t_retailtrade);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '数据库错误'; 
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iRetailtrade, iPaymentType, iAmount, iAmountCash, iAmountAlipay, iAmountWeChat, iAmount1, iAmount2, iAmount3, iAmount4, iAmount5;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
  	 		-- 检查所有零售单的顾客支付的金额 = 现金支付数目+支付宝支付数目+微信支付数目+其它支付的数目1+其它支付的数目2+其它支付的数目3+其它支付的数目4+其它支付的数目5
			IF iAmount != iAmountCash + iAmountAlipay + iAmountWeChat + iAmount1 + iAmount2 + iAmount3 + iAmount4 + iAmount5 THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('ID为', iRetailtrade, '的零售单的支付金额和各种方式支付金额的总和不相等');
			END IF;
			
			-- 如果只有现金支付，则可以为0.000000d，如果混合支付带有现金支付，那么现金支付不可为0.000000d；
			-- 如果存在这种支付方式，对应的钱应该>0.000000d；
			-- 如果没有这种支付方式，那么这种支付金额要为0.000000d；
			IF iPaymentType & 1 = 0 AND iAmountCash != 0 THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('ID为', iRetailtrade, '的零售单的不存在现金支付方式，所以现金支付金额要为0');
			ELSEIF iPaymentType !=1 AND iPaymentType & 1 = 1 AND iAmountCash = 0 THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('ID为', iRetailtrade, '的零售单的存在现金支付方式，所以现金支付金额不能为0');
			END IF;
			
			IF iPaymentType & 2 = 0 AND iAmountAlipay != 0 THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('ID为', iRetailtrade, '的零售单的不存在支付宝支付方式，所以支付宝支付金额要为0');
			ELSEIF iPaymentType & 2 = 2 AND iAmountAlipay = 0 THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('ID为', iRetailtrade, '的零售单的存在支付宝支付方式，所以支付宝支付金额不能为0');
			END IF;
			
			IF iPaymentType & 4 = 0 AND iAmountWeChat != 0 THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('ID为', iRetailtrade, '的零售单的不存在微信支付方式，所以微信支付金额要为0');
			ELSEIF iPaymentType & 4 = 4 AND iAmountWeChat = 0 THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('ID为', iRetailtrade, '的零售单的存在微信支付方式，所以微信支付金额不能为0');
			END IF;
				 
			IF iPaymentType & 8 = 0 AND iAmount1 != 0 THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('ID为', iRetailtrade, '的零售单的不存在Amount1支付方式，所以Amount1支付金额要为0');
			ELSEIF iPaymentType & 8 = 8 AND iAmount1 = 0 THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('ID为', iRetailtrade, '的零售单的存在Amount1支付方式，所以Amount1支付金额不能为0');
			END IF;
			
			IF iPaymentType & 16 = 0 AND iAmount2 != 0 THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('ID为', iRetailtrade, '的零售单的不存在Amount2支付方式，所以Amount2支付金额要为0');
			ELSEIF iPaymentType & 16 = 16 AND iAmount2 = 0 THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('ID为', iRetailtrade, '的零售单的存在Amount2支付方式，所以Amount2支付金额不能为0');
			END IF;
				 
			IF iPaymentType & 32 = 0 AND iAmount3 != 0 THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('ID为', iRetailtrade, '的零售单的不存在Amount3支付方式，所以Amount3支付金额要为0');
			ELSEIF iPaymentType & 32 = 32 AND iAmount3 = 0 THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('ID为', iRetailtrade, '的零售单的存在Amount3支付方式，所以Amount3支付金额不能为0');
			END IF;
			
			IF iPaymentType & 64 = 0 AND iAmount4 != 0 THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('ID为', iRetailtrade, '的零售单的不存在Amount4支付方式，所以Amount4支付金额要为0');
			ELSEIF iPaymentType & 64 = 64 AND iAmount4 = 0 THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('ID为', iRetailtrade, '的零售单的存在Amount4支付方式，所以Amount4支付金额不能为0');
			END IF;
			
			IF iPaymentType & 128 = 0 AND iAmount5 != 0 THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('ID为', iRetailtrade, '的零售单的不存在Amount5支付方式，所以Amount5支付金额要为0');
			ELSEIF iPaymentType & 128 = 128 AND iAmount5 = 0 THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('ID为', iRetailtrade, '的零售单的存在Amount5支付方式，所以Amount5支付金额不能为0');
			END IF;
			
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;