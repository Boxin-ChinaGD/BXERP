DROP PROCEDURE IF EXISTS `SPD_RetailTrade_CheckPaymentType`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_RetailTrade_CheckPaymentType`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	-- ...临时模板，下面的值需要根据需求变化
	DECLARE iID INT;
	DECLARE iPaymentType INT;
	DECLARE iSourceID INT;
	DECLARE iSourcePaymentType INT; -- 源单的退货方式
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- 临时模板，下面list需要根据需求变化
	DECLARE list CURSOR FOR (SELECT F_ID AS iID, F_PaymentType AS iPaymentType, F_SourceID FROM t_retailtrade);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iID, iPaymentType, iSourceID;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- 检查所有零售单的支付方式只能为规定的8种支付方式,可以混合支付
			-- 如果不为规定的8种支付方式，则认为数据不健康
			IF iPaymentType BETWEEN 0 AND 255 THEN 
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			
				-- 如果是退货型零售单，则退货方式受源零售单的支付方式的约束如下：
				-- 支付方式和对应的允许的退货方式
				-- 现金支付 -> ①现金退款
				-- 微信支付 -> ①现金退款  ②微信退款 
				-- 现金支付 + 加微信支付 ->① 现金退款 ②微信退款  ③ 现金+微信退款
			
				IF iSourceID != -1 THEN -- 退货型零售单
					-- 找到源零售单的支付方式
					SELECT F_PaymentType INTO iSourcePaymentType FROM t_retailtrade WHERE F_ID = iSourceID;
					-- 
					IF iSourcePaymentType = 1 THEN  -- 源单现金支付
						IF iPaymentType != 1 THEN
							SET done := TRUE;
							SET iErrorCode := 7;
							SET sErrorMsg := CONCAT('ID为', iID, '的退货型零售单的退款方式只能是现金退款，因为源单是现金支付');
							LEAVE read_loop;
						END IF;
					-- 
					ELSEIF iSourcePaymentType = 2 THEN  -- TODO 支付宝支付好像暂时不用考虑
						IF iPaymentType NOT IN (1, 2) THEN
							SET done := TRUE;
							SET iErrorCode := 7;
							SET sErrorMsg := CONCAT('ID为', iID, '的退货型零售单的退款方式只能是支付宝或现金退款，因为源单是支付宝支付');
							LEAVE read_loop;
						END IF;	
					-- 
					ELSEIF iSourcePaymentType = 3 THEN  -- TODO 支付宝+现金支付好像暂时不用考虑
						IF iPaymentType NOT IN (1, 2, 3) THEN
							SET done := TRUE;
							SET iErrorCode := 7;
							SET sErrorMsg := CONCAT('ID为', iID, '的退货型零售单的退款方式只能是支付宝、现金或支付宝加现金退款，因为源单是现金加支付宝支付');
							LEAVE read_loop;
						END IF;	 
					--   
					ELSEIF iSourcePaymentType = 4 THEN  -- 源单微信支付
						IF iPaymentType NOT IN (1, 4) THEN
							SET done := TRUE;
							SET iErrorCode := 7;
							SET sErrorMsg := CONCAT('ID为', iID, '的退货型零售单的退款方式只能是微信或现金退款，因为源单是微信支付');
							LEAVE read_loop;
						END IF;	
					--  
					ELSEIF iSourcePaymentType = 5 THEN  -- 源单微信+现金支付
						IF iPaymentType NOT IN (1, 4, 5) THEN
							SET done := TRUE;
							SET iErrorCode := 7;
							SET sErrorMsg := CONCAT('ID为', iID, '的退货型零售单的退款方式只能是微信、现金或微信加现金退款，因为源单是微信+现金支付');
							LEAVE read_loop;
						END IF;
					END IF;
					-- 
				END IF;
				-- 
			ELSE
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('ID为', iID, '的零售单的支付方式并不是规定8种的支付方式');
				LEAVE read_loop;
			END IF;
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;