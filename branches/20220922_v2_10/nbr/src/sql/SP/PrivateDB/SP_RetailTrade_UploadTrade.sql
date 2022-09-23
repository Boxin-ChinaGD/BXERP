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
	IN fAmountPaidIn Decimal(20,6),
	IN fAmountChange Decimal(20,6),
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
	IN sSN VARCHAR(26), -- ԭ24λ���˻�����Ҫ�ں������_1����26λ
	IN dCouponAmount DECIMAL(20,6),
	IN sConsumerOpenID VARCHAR(100),
	IN iShop_ID INT
	)
BEGIN
	DECLARE DBName VARCHAR(20);
	-- �ڶ�������£��ϴ�һ���˻������۵�A������F_SourceID��Ӧ��Դ�����ܲ����ڣ���ʱ����Ҫ�ҳ�ԭ����F_ID����ΪA����ʵ��F_SourceID
	DECLARE originalRetailTradeID INT;
	-- 
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
	 	ROLLBACK;
	END;
	
  	START TRANSACTION;
		IF iVipID <= 0 THEN
			SET iVipID := NULL;
		END IF;
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		-- �������۵����ϴ��Ĺ����п��ܶ�����������������ظ������۵�����ֱ�ӷ��ظ����۵���ȥ��
		IF EXISTS(SELECT 1 FROM t_retailtrade WHERE F_LocalSN = sLocalSN AND F_POS_ID = iPOS_ID AND F_SaleDatetime = dtSaleDatetime) THEN
				SELECT F_ID, F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark,F_SourceID, F_SyncDatetime, F_Amount, F_AmountPaidIn, F_AmountChange
						, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO,
						F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID, F_CouponAmount, F_ConsumerOpenID
		   		FROM t_retailtrade WHERE F_LocalSN = sLocalSN AND F_POS_ID = iPOS_ID AND F_SaleDatetime = dtSaleDatetime;
		   		
				SET iErrorCode := 1;
				SET sErrorMsg := '�����۵��Ѿ�����';
		ELSE
			-- �����΢��֧������ô��Ҫ���ڹ�˾�����̻��������տ���˿�
			SELECT database() INTO DBName;
			IF (iPaymentType & 4 = 4 AND (SELECT 1 FROM nbr_bx.t_company WHERE F_Submchid IS NULL AND F_DBName = DBName)) THEN
				SET iErrorCode := 7;
				SET sErrorMsg := '֧��ʧ�ܣ���˾�����̻���δ���ã����ܽ���΢��֧��';
			ELSEIF iVipID IS NOT NULL AND NOT EXISTS (SELECT 1 FROM t_vip WHERE F_ID = iVipID) THEN
				SET iErrorCode := 2;
				SET sErrorMsg := 'vip������';
			ELSEIF NOT EXISTS (SELECT 1 FROM t_pos WHERE F_ID = iPOS_ID) THEN
				SET iErrorCode := 2;
				SET sErrorMsg := 'POS������';
			ELSEIF NOT EXISTS (SELECT 1 FROM t_staff WHERE F_ID = iStaffID) THEN
				SET iErrorCode := 2;
				SET sErrorMsg := 'Ա��������';
			ELSEIF NOT EXISTS (SELECT 1 FROM t_shop WHERE F_ID = iShop_ID) THEN
				SET iErrorCode := 2;
				SET sErrorMsg := '�ŵ겻����';
			ELSE 
				IF iSourceID = -1 THEN
	 				-- SELECT Func_GenerateSN('LS', sSN) INTO sSN;
					INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountPaidIn, F_AmountChange
								, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO
								, F_WxRefundDesc, F_WxRefundSubMchID, F_CouponAmount, F_ConsumerOpenID, F_ShopID)
					VALUES (iVipID, sSN, sLocalSN, iPOS_ID, sLogo, dtSaleDatetime, iStaffID, iPaymentType, iPaymentAccount, iStatus, sRemark, iSourceID, now(), fAmount, fAmountPaidIn, fAmountChange
								, fAmountCash, fAmountAlipay, fAmountWeChat, fAmount1, fAmount2, fAmount3, fAmount4, fAmount5, iSmallSheetID, sAliPayOrderSN, sWxOrderSN, sWxTradeNO, sWxRefundNO, sWxRefundDesc, sWxRefundSubMchID,
								dCouponAmount, sConsumerOpenID, iShop_ID);
	
					SELECT F_ID, F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark,F_SourceID, F_SyncDatetime, F_Amount, F_AmountPaidIn, F_AmountChange
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
				ELSE -- �����˻���
					-- ��������£��ϴ����˻���������Դ����F_SourceID�ֶο�����SQLite�е���ʱ���۵���F_ID�ֶΣ����뽫���Ϊmysql�е���ʵ���۵���F_ID
					IF NOT EXISTS(SELECT 1 FROM t_retailtrade WHERE F_ID = iSourceID AND F_SourceID = -1) THEN
						SELECT F_ID INTO originalRetailTradeID FROM t_retailtrade WHERE sSN = concat(F_SN, '_1') AND F_SourceID = -1; -- һ����ƴ��ȥ�Աȣ��ܲ����Ż���
						IF originalRetailTradeID IS NOT NULL THEN 
							SET iSourceID = originalRetailTradeID;
							-- ELSE 
							-- ... ������ƫƧ��case��Giggs���ٴ���JIRAȥ���� 
						END IF;
					END IF;
					
					IF EXISTS(SELECT 1 FROM t_retailtrade WHERE F_ID = iSourceID AND F_SourceID = -1) THEN					
						-- �鿴Ҫ�˻������۵��Ƿ������˻���
						IF EXISTS (SELECT 1 FROM t_retailtrade WHERE F_SourceID = iSourceID) THEN 
							SET iErrorCode := 7;
							SET sErrorMsg := 'һ�����۵������ֻ�ܽ���һ���˻�����';
						
						-- ����ʱ�䳬��һ��Ķ�����ֹ�˿�   
						ELSEIF EXISTS (SELECT 1 FROM t_retailtrade WHERE F_ID = iSourceID AND F_SourceID = -1 AND F_SaleDatetime < DATE_SUB(now(), INTERVAL 1 YEAR)) THEN
							SET iErrorCode := 7;
							SET sErrorMsg := '����ʱ�䳬��һ��Ķ�����ֹ�˿�';
						-- �˿�ʱ����Ӧ֧����ʽ���˿���ܳ�������ʱ�ĸ���Ľ��ֽ���⣩ -- todo �Ȳ�����֧�������ֽ���֧��
						ELSEIF iPaymentType & 4 = 4 AND iPaymentType & 1 = 1 AND EXISTS(SELECT 1 FROM t_retailtrade WHERE F_ID = iSourceID AND F_AmountWeChat + F_AmountCash < fAmountWeChat + fAmountCash) THEN -- ΢��+�ֽ�֧��
							SET iErrorCode := 7;
							SET sErrorMsg := '�˿�ʧ�ܣ��˿���ܱ�����ʱ΢��֧�����ֽ�֧�����ܽ���';							
						ELSEIF iPaymentType & 2 = 2 AND EXISTS(SELECT 1 FROM t_retailtrade WHERE F_ID = iSourceID AND F_AmountAlipay < fAmountAlipay) THEN
							SET iErrorCode := 7;
							SET sErrorMsg := '�˿�ʧ�ܣ�֧�����˿���ܱ�����ʱ֧����֧���Ľ���';
						ELSEIF iPaymentType & 4 = 4 AND EXISTS(SELECT 1 FROM t_retailtrade WHERE F_ID = iSourceID AND F_AmountWeChat < fAmountWeChat) THEN
							SET iErrorCode := 7;
							SET sErrorMsg := '�˿�ʧ�ܣ�΢���˿���ܱ�����ʱ΢��֧���Ľ���';
						-- ������۵�û�н��й��˻������ҽ���ʱ��û�г���һ�꣬�Ϳ��Խ����˻�����
						ELSE 
							-- ... Ӧ����Դ���۵���iVipID������
						   	SET @count = 0;
						   	SELECT F_SN, F_VipID INTO sSN, iVipID FROM t_retailtrade WHERE F_ID = iSourceID;
						   	-- SELECT COUNT(1) INTO @count FROM t_retailtrade WHERE F_SourceID = iSourceID;
						  	SELECT CONCAT(sSN, '_', @count + 1) INTO sSN;
						
							INSERT INTO t_retailtrade (F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountPaidIn, F_AmountChange
										, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO
								   		, F_WxRefundDesc, F_WxRefundSubMchID, F_CouponAmount, F_ConsumerOpenID, F_ShopID)
							VALUES (iVipID, sSN, sLocalSN, iPOS_ID, sLogo, dtSaleDatetime, iStaffID, iPaymentType, iPaymentAccount, iStatus, sRemark, iSourceID, now(), fAmount, fAmountPaidIn, fAmountChange
									, fAmountCash, fAmountAlipay, fAmountWeChat, fAmount1, fAmount2, fAmount3, fAmount4, fAmount5, iSmallSheetID, sAliPayOrderSN, sWxOrderSN, sWxTradeNO, sWxRefundNO, sWxRefundDesc, sWxRefundSubMchID,
									dCouponAmount, sConsumerOpenID, iShop_ID);
							
							SELECT F_ID, F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark,F_SourceID, F_SyncDatetime, F_Amount, F_AmountPaidIn, F_Amount
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
						SET sErrorMsg := '���ܶ��˻����͵����۵������˻�';
			   		ELSE
						SET iErrorCode := 7;
						SET sErrorMsg := '�����޸Ĳ����ڵ����۵�';
			  		END IF;
				END IF;
			END IF;
		END IF;
		
 	COMMIT;
END;