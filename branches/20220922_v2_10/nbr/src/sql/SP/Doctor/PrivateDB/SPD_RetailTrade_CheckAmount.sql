DROP PROCEDURE IF EXISTS `SPD_RetailTrade_CheckAmount`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_RetailTrade_CheckAmount`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	-- ...��ʱģ�壬�����ֵ��Ҫ��������仯
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
	-- ��ʱģ�壬����list��Ҫ��������仯
	DECLARE list CURSOR FOR (
		SELECT F_ID AS iRetailtrade,F_PaymentType AS iPaymentType, F_Amount AS iAmount, F_AmountCash AS iAmountCash, 
			F_AmountAlipay AS iAmountAlipay, F_AmountWeChat AS iAmountWeChat, F_Amount1 AS iAmount1, 
			F_Amount2 AS iAmount2, F_Amount3 AS iAmount3, F_Amount4 AS iAmount4, F_Amount5 AS iAmount5 
		FROM t_retailtrade);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '���ݿ����'; 
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
			
  	 		-- ����������۵��Ĺ˿�֧���Ľ�� = �ֽ�֧����Ŀ+֧����֧����Ŀ+΢��֧����Ŀ+����֧������Ŀ1+����֧������Ŀ2+����֧������Ŀ3+����֧������Ŀ4+����֧������Ŀ5
			IF iAmount != iAmountCash + iAmountAlipay + iAmountWeChat + iAmount1 + iAmount2 + iAmount3 + iAmount4 + iAmount5 THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('IDΪ', iRetailtrade, '�����۵���֧�����͸��ַ�ʽ֧�������ܺͲ����');
			END IF;
			
			-- ���ֻ���ֽ�֧���������Ϊ0.000000d��������֧�������ֽ�֧������ô�ֽ�֧������Ϊ0.000000d��
			-- �����������֧����ʽ����Ӧ��ǮӦ��>0.000000d��
			-- ���û������֧����ʽ����ô����֧�����ҪΪ0.000000d��
			IF iPaymentType & 1 = 0 AND iAmountCash != 0 THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('IDΪ', iRetailtrade, '�����۵��Ĳ������ֽ�֧����ʽ�������ֽ�֧�����ҪΪ0');
			ELSEIF iPaymentType !=1 AND iPaymentType & 1 = 1 AND iAmountCash = 0 THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('IDΪ', iRetailtrade, '�����۵��Ĵ����ֽ�֧����ʽ�������ֽ�֧������Ϊ0');
			END IF;
			
			IF iPaymentType & 2 = 0 AND iAmountAlipay != 0 THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('IDΪ', iRetailtrade, '�����۵��Ĳ�����֧����֧����ʽ������֧����֧�����ҪΪ0');
			ELSEIF iPaymentType & 2 = 2 AND iAmountAlipay = 0 THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('IDΪ', iRetailtrade, '�����۵��Ĵ���֧����֧����ʽ������֧����֧������Ϊ0');
			END IF;
			
			IF iPaymentType & 4 = 0 AND iAmountWeChat != 0 THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('IDΪ', iRetailtrade, '�����۵��Ĳ�����΢��֧����ʽ������΢��֧�����ҪΪ0');
			ELSEIF iPaymentType & 4 = 4 AND iAmountWeChat = 0 THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('IDΪ', iRetailtrade, '�����۵��Ĵ���΢��֧����ʽ������΢��֧������Ϊ0');
			END IF;
				 
			IF iPaymentType & 8 = 0 AND iAmount1 != 0 THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('IDΪ', iRetailtrade, '�����۵��Ĳ�����Amount1֧����ʽ������Amount1֧�����ҪΪ0');
			ELSEIF iPaymentType & 8 = 8 AND iAmount1 = 0 THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('IDΪ', iRetailtrade, '�����۵��Ĵ���Amount1֧����ʽ������Amount1֧������Ϊ0');
			END IF;
			
			IF iPaymentType & 16 = 0 AND iAmount2 != 0 THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('IDΪ', iRetailtrade, '�����۵��Ĳ�����Amount2֧����ʽ������Amount2֧�����ҪΪ0');
			ELSEIF iPaymentType & 16 = 16 AND iAmount2 = 0 THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('IDΪ', iRetailtrade, '�����۵��Ĵ���Amount2֧����ʽ������Amount2֧������Ϊ0');
			END IF;
				 
			IF iPaymentType & 32 = 0 AND iAmount3 != 0 THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('IDΪ', iRetailtrade, '�����۵��Ĳ�����Amount3֧����ʽ������Amount3֧�����ҪΪ0');
			ELSEIF iPaymentType & 32 = 32 AND iAmount3 = 0 THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('IDΪ', iRetailtrade, '�����۵��Ĵ���Amount3֧����ʽ������Amount3֧������Ϊ0');
			END IF;
			
			IF iPaymentType & 64 = 0 AND iAmount4 != 0 THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('IDΪ', iRetailtrade, '�����۵��Ĳ�����Amount4֧����ʽ������Amount4֧�����ҪΪ0');
			ELSEIF iPaymentType & 64 = 64 AND iAmount4 = 0 THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('IDΪ', iRetailtrade, '�����۵��Ĵ���Amount4֧����ʽ������Amount4֧������Ϊ0');
			END IF;
			
			IF iPaymentType & 128 = 0 AND iAmount5 != 0 THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('IDΪ', iRetailtrade, '�����۵��Ĳ�����Amount5֧����ʽ������Amount5֧�����ҪΪ0');
			ELSEIF iPaymentType & 128 = 128 AND iAmount5 = 0 THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('IDΪ', iRetailtrade, '�����۵��Ĵ���Amount5֧����ʽ������Amount5֧������Ϊ0');
			END IF;
			
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;