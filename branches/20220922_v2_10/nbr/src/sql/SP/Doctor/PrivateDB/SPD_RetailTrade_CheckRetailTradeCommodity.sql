DROP PROCEDURE IF EXISTS `SPD_RetailTrade_CheckRetailTradeCommodity`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_RetailTrade_CheckRetailTradeCommodity`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iRT_ID INT; -- ���۵�ID
	DECLARE iSourceID INT; -- �����˻�����Ӧ�����۵�ID
	DECLARE iRTC_noSold INT; -- ���۵�����������
	DECLARE iRTC_noReturn INT; -- �����˻������˻�����
	DECLARE iReturn INT; -- �����˻�������
	DECLARE iRTC_noCanReturn INT; -- ���۵���Ʒ�������˻���Ʒ�Ŀ��˻�����
	DECLARE iAmount DECIMAL(20,6); -- ���۵�Ӧ�ռ�
	DECLARE iSumPriceReturn DECIMAL(20,6); -- ���۵���Ӧ�������۵���Ʒ�����˻���
	DECLARE iSumPriceReturn_ReturnRetailTrade DECIMAL(20,6);
	DECLARE dTolerance DECIMAL(20,6) DEFAULT 0.0001;
	DECLARE done INT DEFAULT FALSE;
	
	DECLARE list CURSOR FOR (
		SELECT F_ID AS iRT_ID, F_SourceID AS iSourceID FROM t_retailtrade); -- �������۵�
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
			FETCH list INTO iRT_ID, iSourceID;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- ��ȡ���۵��Ľ�������Ӧ�����۵���Ʒ�����ܺ�
			SELECT F_Amount INTO iAmount FROM t_retailtrade WHERE F_ID = iRT_ID;
			SELECT sum(F_PriceReturn * F_NO) INTO iSumPriceReturn FROM t_retailtradecommodity WHERE F_TradeID = iRT_ID;


			-- �ж������۵����������˻���
			IF iSourceID = -1 THEN -- ���۵�
				-- ��ȡ���˻�����
				SELECT sum(F_NOCanReturn) INTO iRTC_noCanReturn FROM t_retailtradecommodity WHERE F_TradeID = iRT_ID;
				
				-- ��ȡ���۵����˻�������
				SELECT count(*) INTO iReturn FROM t_retailtrade WHERE F_SourceID = iRT_ID;
				
				-- ��ȡ���۵���Ʒ����������
				SELECT sum(F_NO) INTO iRTC_noSold FROM t_retailtradecommodity WHERE F_TradeID = iRT_ID;
				
				-- ���˻���������С�ڻ������������
				IF IFNULL(iRTC_noCanReturn, 0) > IFNULL(iRTC_noSold, 0) THEN 
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('IDΪ', iRT_ID, '�����۵��Ŀ��˻�����������������');
				-- ���۵��ж��������˻���
				ELSEIF iReturn > 1 THEN 
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('IDΪ', iRT_ID, '�����۵��ж��������˻���');
				-- ���۵�����Ҫ�дӱ�������۵������ڴӱ�����Ϊ���ݲ�����
				ELSEIF NOT EXISTS(SELECT 1 FROM t_retailtradecommodity WHERE F_TradeID = iRT_ID) THEN
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('IDΪ', iRT_ID, '�����۵�û����Ӧ�����۵���Ʒ');
				-- ������������<=0
				ELSEIF IFNULL(iRTC_noSold,0) <= 0 THEN  
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('IDΪ', iRT_ID, '�����۵������������������0');
				ELSEIF iSumPriceReturn < 0 THEN -- ���۵��ӱ���ܽ�������ڻ����0(������Ԫ����Ʒ)
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('IDΪ', iRT_ID, '�����۵��ӱ���ܽ�������ڻ����0');
				ELSEIF iAmount < 0 THEN -- ���۵��Ľ�������ڻ����0
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('IDΪ', iRT_ID, '�����۵��Ľ�������ڻ����0');
				ELSEIF abs(abs(IFNULL(iAmount,0)) - abs(IFNULL(iSumPriceReturn,0))) >= dTolerance THEN -- 
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('IDΪ', iRT_ID, '�����۵���Ӧ�ռ������������۵���Ʒ���˻����ܺͲ����');
				-- ���۵���Ʒ�дӱ�������۵���Ʒ�����ڴӱ�����Ϊ���ݲ�����
				ELSEIF NOT EXISTS(SELECT 1 FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID IN (
						SELECT F_ID FROM t_retailtradecommodity WHERE F_TradeID = iRT_ID)) THEN 
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('���۵�IDΪ', iRT_ID, '�����۵���Ʒû����Ӧ�����۵���Ʒ��Դ');
				-- ���۵�������ȥ���
				ELSEIF EXISTS(SELECT 1 FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID IN (
							SELECT F_ID FROM t_retailtradecommodity WHERE F_TradeID = iRT_ID)) THEN 
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('IDΪ', iRT_ID, '�����۵����������۵���Ʒ�˻�ȥ���');
				ELSEIF iReturn = 1 THEN -- ���۵����˻�
					-- ��ȡ�˻�����
					SELECT sum(F_NO) INTO iRTC_noReturn FROM t_retailtradecommodity WHERE F_TradeID = (SELECT F_ID FROM t_retailtrade WHERE F_SourceID = iRT_ID);
					-- �ж��˻������Ƿ������������
					IF IFNULL(iRTC_noReturn, 0) > IFNULL(iRTC_noSold, 0) THEN 
						SET done := TRUE;
						SET iErrorCode := 7;
						SET sErrorMsg := CONCAT('ԭ���۵�IDΪ', iRT_ID, '�������˻������˻���������ԭ���۵�����������');
					-- �˻���������Ϊ0
					ELSEIF IFNULL(iRTC_noReturn,0) <= 0 THEN 
						SET done := TRUE;
						SET iErrorCode := 7;
						SET sErrorMsg := CONCAT('ԭ���۵�IDΪ', iRT_ID, '�������˻������˻������������0');
					-- �ж��Ƿ���ȫ�˻�
					ELSEIF IFNULL(iRTC_noSold,0) != IFNULL(iRTC_noReturn,0) THEN -- �����˻�
						-- �����˻�����
						IF IFNULL((iRTC_noSold - iRTC_noReturn),0) != IFNULL(iRTC_noCanReturn, 0) THEN 
							SET done := TRUE;
							SET iErrorCode := 7;
							SET sErrorMsg := CONCAT('IDΪ', iRT_ID, '�����۵��˻��󣬿��˻�����������');
						END IF;
					ELSE -- ��ȫ�˻�
						SELECT sum(F_PriceReturn * F_NO) INTO iSumPriceReturn_ReturnRetailTrade FROM t_retailtradecommodity WHERE F_TradeID = (SELECT F_ID FROM t_retailtrade WHERE F_SourceID= iRT_ID);
						-- ��ȫ�˻�ʱ�����˻���������Ϊ0
						IF IFNULL(iRTC_noCanReturn, 0) != 0 THEN 
							SET done := TRUE;
							SET iErrorCode := 7;
							SET sErrorMsg := CONCAT('IDΪ', iRT_ID, '�����۵��Ѿ���ȫ�˻������˻���������Ϊ0');
						ELSEIF iSumPriceReturn_ReturnRetailTrade < 0 THEN 
							SET done := TRUE;
							SET iErrorCode := 7;
							SET sErrorMsg := CONCAT('ԭ���۵�IDΪ', iRT_ID, '�������˻����ӱ���ܽ�������ڻ����0');
						ELSEIF abs(abs(IFNULL(iSumPriceReturn,0)) - abs(IFNULL(iSumPriceReturn_ReturnRetailTrade,0))) >= dTolerance THEN
							SET done := TRUE;
							SET iErrorCode := 7;
							SET sErrorMsg := CONCAT('IDΪ', iRT_ID, '�����۵��Ѿ���ȫ�˻����������Ӧ�������˻����Ľ����');
						END IF;
					END IF;
				END IF;
			ELSE -- �����˻���
				-- ��ȡ�����˻�����ԭ���۵�����
				SELECT count(*) INTO iReturn FROM t_retailtrade WHERE F_ID = iSourceID;
				
				IF iAmount < 0 THEN -- �����˻����Ľ�������ڻ����0
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('IDΪ', iRT_ID, '�������˻����Ľ�������ڻ����0');
				ELSEIF iSumPriceReturn < 0 THEN -- �����˻����ӱ���ܽ�������ڻ����0
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('IDΪ', iRT_ID, '�������˻����ӱ���ܽ�������ڻ����0');
				-- �жϸõ���Amount�������дӱ���˻����ܺ��Ƿ����
				ELSEIF abs(abs(IFNULL(iAmount,0)) - abs(IFNULL(iSumPriceReturn,0))) >= dTolerance THEN -- 
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('IDΪ', iRT_ID, '�������˻�����Ӧ�ռ������������۵���Ʒ���˻����ܺͲ����');
				-- һ�������˻���ֻ��һ��ԭ���۵�,���һ�������˻���������ԭ���۵�����Ϊ���ݲ�����
				ELSEIF iReturn = 0 THEN 
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('IDΪ', iRT_ID, '�������˻���û����Ӧ��ԭ���۵�');
				-- �����˻�������Ҫ�дӱ����û������Ϊ���ݲ�����
				ELSEIF NOT EXISTS(SELECT 1 FROM t_retailtradecommodity WHERE F_TradeID = iRT_ID) THEN  
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('IDΪ', iRT_ID, '�������˻���û����Ӧ�������˻�����Ʒ');
				-- �����˻�����Ʒû�����۵���Ʒ��Դ�����������Ϊ���ݲ�����
				ELSEIF EXISTS(SELECT 1 FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID IN (
						SELECT F_ID FROM t_retailtradecommodity WHERE F_TradeID = iRT_ID)) THEN 
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('�����˻���IDΪ', iRT_ID, '�������˻�����Ʒ�����������˻�����Ʒ��Դ');
				-- �����˻�����Ʒ�����۵���Ʒ�˻�ȥ������û������Ϊ���ݲ�����
				ELSEIF NOT EXISTS(SELECT 1 FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID IN (
							SELECT F_ID FROM t_retailtradecommodity WHERE F_TradeID = iRT_ID)) THEN 
						SET done := TRUE;
						SET iErrorCode := 7;
						SET sErrorMsg := CONCAT('IDΪ', iRT_ID, '�������˻���û����Ӧ�����۵���Ʒ�˻�ȥ���');
				END IF;
			END IF;
				
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;