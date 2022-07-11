DROP PROCEDURE IF EXISTS `SP_RetailTrade_UploadTrade`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_RetailTrade_UploadTrade` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iVipID INT,
	IN sLocalSN VARCHAR(32),
	IN iPOS_ID INT,
	IN sLogo VARCHAR(128),
	IN dtSaleDatetime DATETIME,
	IN iStaffID INT,
	IN iPaymentType INT,
	IN iPaymentAccount VARCHAR(20),
	IN iStatus INT,
	IN sRemark VARCHAR(20),
	IN iSourceID INT,
	IN fAmount Decimal(20,6),
	IN fAmountCash Decimal(20,6), 
	IN fAmountAlipay Decimal(20,6), 
	IN fAmountWeChat Decimal(20,6), 
	IN fAmount1 Decimal(20,6), 
	IN fAmount2 Decimal(20,6), 
	IN fAmount3 Decimal(20,6), 
	IN fAmount4 Decimal(20,6), 
	IN fAmount5 Decimal(20,6),
	IN iSmallSheetID INT,
	IN sAliPayOrderSN VARCHAR(32),
	IN sWxOrderSN VARCHAR(32),
	IN sWxTradeNO VARCHAR(32),
	IN sWxRefundNO VARCHAR(32),
	IN sWxRefundDesc VARCHAR(80),
	IN sWxRefundSubMchID VARCHAR(32),
	IN sSN VARCHAR(26), -- 原24位，退货单需要在后面添加_1。故26位
	IN dCouponAmount DECIMAL(20,6),
	IN sConsumerOpenID VARCHAR(100),
	IN iShop_ID INT
	)
BEGIN
	DECLARE DBName VARCHAR(20);
	-- 在断网情况下，上传一张退货型零售单A，它的F_SourceID对应的源单可能不存在，这时，需要找出原单的F_ID，作为A的真实的F_SourceID
	DECLARE originalRetailTradeID INT;
	-- 
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
	 	ROLLBACK;
	END;
	
  	START TRANSACTION;
		IF iVipID <= 0 THEN
			SET iVipID := NULL;
		END IF;
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		-- 由于零售单在上传的过程中可能断网，所以如果创建重复的零售单，就直接返回该零售单回去。
		IF EXISTS(SELECT 1 FROM t_retailtrade WHERE F_LocalSN = sLocalSN AND F_POS_ID = iPOS_ID AND F_SaleDatetime = dtSaleDatetime) THEN
				SELECT F_ID, F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark,F_SourceID, F_SyncDatetime, F_Amount
						, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO,
						F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_CouponAmount, F_ConsumerOpenID
		   		FROM t_retailtrade WHERE F_LocalSN = sLocalSN AND F_POS_ID = iPOS_ID AND F_SaleDatetime = dtSaleDatetime;
		   		
				SET iErrorCode := 1;
				SET sErrorMsg := '该零售单已经存在';
		ELSE
			-- 如果是微信支付，那么需要存在公司的子商户号用来收款和退款
			SELECT database() INTO DBName;
			IF (iPaymentType & 4 = 4 AND (SELECT 1 FROM nbr_bx.t_company WHERE F_Submchid IS NULL AND F_DBName = DBName)) THEN
				SET iErrorCode := 7;
				SET sErrorMsg := '支付失败，公司的子商户号未设置！不能进行微信支付';
			ELSEIF iVipID IS NOT NULL AND NOT EXISTS (SELECT 1 FROM t_vip WHERE F_ID = iVipID) THEN
				SET iErrorCode := 2;
				SET sErrorMsg := 'vip不存在';
			ELSEIF NOT EXISTS (SELECT 1 FROM t_pos WHERE F_ID = iPOS_ID) THEN
				SET iErrorCode := 2;
				SET sErrorMsg := 'POS不存在';
			ELSEIF NOT EXISTS (SELECT 1 FROM t_staff WHERE F_ID = iStaffID) THEN
				SET iErrorCode := 2;
				SET sErrorMsg := '员工不存在';
			ELSEIF NOT EXISTS (SELECT 1 FROM t_shop WHERE F_ID = iShop_ID) THEN
				SET iErrorCode := 2;
				SET sErrorMsg := '门店不存在';
			ELSE 
				IF iSourceID = -1 THEN
	 				-- SELECT Func_GenerateSN('LS', sSN) INTO sSN;
					INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount
								, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO
								, F_WxRefundDesc, F_WxRefundSubMchID, F_CouponAmount, F_ConsumerOpenID, F_ShopID)
					VALUES (iVipID, sSN, sLocalSN, iPOS_ID, sLogo, dtSaleDatetime, iStaffID, iPaymentType, iPaymentAccount, iStatus, sRemark, iSourceID, now(), fAmount
								, fAmountCash, fAmountAlipay, fAmountWeChat, fAmount1, fAmount2, fAmount3, fAmount4, fAmount5, iSmallSheetID, sAliPayOrderSN, sWxOrderSN, sWxTradeNO, sWxRefundNO, sWxRefundDesc, sWxRefundSubMchID,
								dCouponAmount, sConsumerOpenID, iShop_ID);
	
					SELECT F_ID, F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark,F_SourceID, F_SyncDatetime, F_Amount
								, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO,
								F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_CouponAmount, F_ConsumerOpenID, F_ShopID
				   	FROM t_retailtrade WHERE F_ID = LAST_INSERT_ID();
	
				   	IF EXISTS(SELECT 1 FROM t_vip WHERE F_ID = iVipID) THEN
						UPDATE t_vip SET 
							F_ConsumeTimes = F_ConsumeTimes + 1,
							F_ConsumeAmount = F_ConsumeAmount + fAmount,
							F_LastConsumeDatetime = now()
						WHERE F_ID = iVipID;
						
						CALL SP_Vip_UpdateBonus(iErrorCode, sErrorMsg, iVipID/*iVipID*/, 0/*iStaffID*/, fAmount * 100/*iAmount*/, 0/*iBonus*/, ''/*sRemark*/, 0/*iManuallyAdded*/, 1/*iIsIncreaseBonus*/);
				   	END IF;
				ELSE -- 处理退货单
					-- 断网情况下，上传的退货单，它的源单的F_SourceID字段可能是SQLite中的临时零售单的F_ID字段，必须将其变为mysql中的真实零售单的F_ID
					IF NOT EXISTS(SELECT 1 FROM t_retailtrade WHERE F_ID = iSourceID AND F_SourceID = -1) THEN
						SELECT F_ID INTO originalRetailTradeID FROM t_retailtrade WHERE sSN = concat(F_SN, '_1') AND F_SourceID = -1; -- 一个个拼接去对比，能不能优化？
						IF originalRetailTradeID IS NOT NULL THEN 
							SET iSourceID = originalRetailTradeID;
							-- ELSE 
							-- ... 这里是偏僻的case，Giggs会再创建JIRA去做。 
						END IF;
					END IF;
					
					IF EXISTS(SELECT 1 FROM t_retailtrade WHERE F_ID = iSourceID AND F_SourceID = -1) THEN					
						-- 查看要退货的零售单是否已有退货单
						IF EXISTS (SELECT 1 FROM t_retailtrade WHERE F_SourceID = iSourceID) THEN 
							SET iErrorCode := 7;
							SET sErrorMsg := '一张零售单，最多只能进行一次退货操作';
						
						-- 交易时间超过一年的订单禁止退款   
						ELSEIF EXISTS (SELECT 1 FROM t_retailtrade WHERE F_ID = iSourceID AND F_SourceID = -1 AND F_SaleDatetime < DATE_SUB(now(), INTERVAL 1 YEAR)) THEN
							SET iErrorCode := 7;
							SET sErrorMsg := '交易时间超过一年的订单禁止退款';
						-- 退款时，对应支付方式的退款金额不能超过零售时的付款的金额（现金除外） -- todo 先不考虑支付宝和现金混合支付
						ELSEIF iPaymentType & 4 = 4 AND iPaymentType & 1 = 1 AND EXISTS(SELECT 1 FROM t_retailtrade WHERE F_ID = iSourceID AND F_AmountWeChat + F_AmountCash < fAmountWeChat + fAmountCash) THEN -- 微信+现金支付
							SET iErrorCode := 7;
							SET sErrorMsg := '退款失败，退款金额不能比零售时微信支付和现金支付的总金额多';							
						ELSEIF iPaymentType & 2 = 2 AND EXISTS(SELECT 1 FROM t_retailtrade WHERE F_ID = iSourceID AND F_AmountAlipay < fAmountAlipay) THEN
							SET iErrorCode := 7;
							SET sErrorMsg := '退款失败，支付宝退款金额不能比零售时支付宝支付的金额多';
						ELSEIF iPaymentType & 4 = 4 AND EXISTS(SELECT 1 FROM t_retailtrade WHERE F_ID = iSourceID AND F_AmountWeChat < fAmountWeChat) THEN
							SET iErrorCode := 7;
							SET sErrorMsg := '退款失败，微信退款金额不能比零售时微信支付的金额多';
						-- 如果零售单没有进行过退货，并且交易时间没有超过一年，就可以进行退货操作
						ELSE 
							-- ... 应该拿源零售单的iVipID来操作
						   	SET @count = 0;
						   	SELECT F_SN, F_VipID INTO sSN, iVipID FROM t_retailtrade WHERE F_ID = iSourceID;
						   	-- SELECT COUNT(1) INTO @count FROM t_retailtrade WHERE F_SourceID = iSourceID;
						  	SELECT CONCAT(sSN, '_', @count + 1) INTO sSN;
						
							INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount
										, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO
								   		, F_WxRefundDesc, F_WxRefundSubMchID, F_CouponAmount, F_ConsumerOpenID, F_ShopID)
							VALUES (iVipID, sSN, sLocalSN, iPOS_ID, sLogo, dtSaleDatetime, iStaffID, iPaymentType, iPaymentAccount, iStatus, sRemark, iSourceID, now(), fAmount
									, fAmountCash, fAmountAlipay, fAmountWeChat, fAmount1, fAmount2, fAmount3, fAmount4, fAmount5, iSmallSheetID, sAliPayOrderSN, sWxOrderSN, sWxTradeNO, sWxRefundNO, sWxRefundDesc, sWxRefundSubMchID,
									dCouponAmount, sConsumerOpenID, iShop_ID);
							
							SELECT F_ID, F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark,F_SourceID, F_SyncDatetime, F_Amount
									, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO
								   	, F_WxRefundDesc, F_WxRefundSubMchID, F_CouponAmount, F_ConsumerOpenID, F_ShopID
					   		FROM t_retailtrade WHERE F_ID = LAST_INSERT_ID();
					   		
							IF EXISTS(SELECT 1 FROM t_vip WHERE F_ID = iVipID) THEN
								UPDATE t_vip SET 
									F_ConsumeTimes = F_ConsumeTimes - 1,
									F_ConsumeAmount = F_ConsumeAmount - fAmount,
									F_LastConsumeDatetime = now()
								WHERE F_ID = iVipID;
								
								SELECT 
									F_ID, 
									F_SN, 
									F_CardID,
									F_Mobile, 
									F_LocalPosSN, 
									F_Sex, 
									F_Logo, 
									F_ICID, 
									F_Name, 
									F_Email,  
									F_ConsumeTimes, 
									F_ConsumeAmount, 
									F_District, 
									F_Category, 
									F_Birthday, 
									F_Bonus, 
									F_LastConsumeDatetime, 
									F_Remark, 
									F_CreateDatetime, 
									F_UpdateDatetime
								FROM t_vip WHERE F_ID = iVipID;
					   		END IF;	
						END IF;
					ELSEIF ((SELECT F_SourceID FROM t_retailtrade WHERE F_ID = iSourceID) <> -1) THEN
						SET iErrorCode := 7;
						SET sErrorMsg := '不能对退货类型的零售单进行退货';
			   		ELSE
						SET iErrorCode := 7;
						SET sErrorMsg := '不能修改不存在的零售单';
			  		END IF;
				END IF;
			END IF;
		END IF;
		
 	COMMIT;
END;