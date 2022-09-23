DROP PROCEDURE IF EXISTS `SPD_RetailTrade_CheckPaymentType`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_RetailTrade_CheckPaymentType`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	-- ...��ʱģ�壬�����ֵ��Ҫ��������仯
	DECLARE iID INT;
	DECLARE iPaymentType INT;
	DECLARE iSourceID INT;
	DECLARE iSourcePaymentType INT; -- Դ�����˻���ʽ
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- ��ʱģ�壬����list��Ҫ��������仯
	DECLARE list CURSOR FOR (SELECT F_ID AS iID, F_PaymentType AS iPaymentType, F_SourceID FROM t_retailtrade);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iID, iPaymentType, iSourceID;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- ����������۵���֧����ʽֻ��Ϊ�涨��8��֧����ʽ,���Ի��֧��
			-- �����Ϊ�涨��8��֧����ʽ������Ϊ���ݲ�����
			IF iPaymentType BETWEEN 0 AND 255 THEN 
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			
				-- ������˻������۵������˻���ʽ��Դ���۵���֧����ʽ��Լ�����£�
				-- ֧����ʽ�Ͷ�Ӧ��������˻���ʽ
				-- �ֽ�֧�� -> ���ֽ��˿�
				-- ΢��֧�� -> ���ֽ��˿�  ��΢���˿� 
				-- �ֽ�֧�� + ��΢��֧�� ->�� �ֽ��˿� ��΢���˿�  �� �ֽ�+΢���˿�
			
				IF iSourceID != -1 THEN -- �˻������۵�
					-- �ҵ�Դ���۵���֧����ʽ
					SELECT F_PaymentType INTO iSourcePaymentType FROM t_retailtrade WHERE F_ID = iSourceID;
					-- 
					IF iSourcePaymentType = 1 THEN  -- Դ���ֽ�֧��
						IF iPaymentType != 1 THEN
							SET done := TRUE;
							SET iErrorCode := 7;
							SET sErrorMsg := CONCAT('IDΪ', iID, '���˻������۵����˿ʽֻ�����ֽ��˿��ΪԴ�����ֽ�֧��');
							LEAVE read_loop;
						END IF;
					-- 
					ELSEIF iSourcePaymentType = 2 THEN  -- TODO ֧����֧��������ʱ���ÿ���
						IF iPaymentType NOT IN (1, 2) THEN
							SET done := TRUE;
							SET iErrorCode := 7;
							SET sErrorMsg := CONCAT('IDΪ', iID, '���˻������۵����˿ʽֻ����֧�������ֽ��˿��ΪԴ����֧����֧��');
							LEAVE read_loop;
						END IF;	
					-- 
					ELSEIF iSourcePaymentType = 3 THEN  -- TODO ֧����+�ֽ�֧��������ʱ���ÿ���
						IF iPaymentType NOT IN (1, 2, 3) THEN
							SET done := TRUE;
							SET iErrorCode := 7;
							SET sErrorMsg := CONCAT('IDΪ', iID, '���˻������۵����˿ʽֻ����֧�������ֽ��֧�������ֽ��˿��ΪԴ�����ֽ��֧����֧��');
							LEAVE read_loop;
						END IF;	 
					--   
					ELSEIF iSourcePaymentType = 4 THEN  -- Դ��΢��֧��
						IF iPaymentType NOT IN (1, 4) THEN
							SET done := TRUE;
							SET iErrorCode := 7;
							SET sErrorMsg := CONCAT('IDΪ', iID, '���˻������۵����˿ʽֻ����΢�Ż��ֽ��˿��ΪԴ����΢��֧��');
							LEAVE read_loop;
						END IF;	
					--  
					ELSEIF iSourcePaymentType = 5 THEN  -- Դ��΢��+�ֽ�֧��
						IF iPaymentType NOT IN (1, 4, 5) THEN
							SET done := TRUE;
							SET iErrorCode := 7;
							SET sErrorMsg := CONCAT('IDΪ', iID, '���˻������۵����˿ʽֻ����΢�š��ֽ��΢�ż��ֽ��˿��ΪԴ����΢��+�ֽ�֧��');
							LEAVE read_loop;
						END IF;
					END IF;
					-- 
				END IF;
				-- 
			ELSE
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('IDΪ', iID, '�����۵���֧����ʽ�����ǹ涨8�ֵ�֧����ʽ');
				LEAVE read_loop;
			END IF;
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;