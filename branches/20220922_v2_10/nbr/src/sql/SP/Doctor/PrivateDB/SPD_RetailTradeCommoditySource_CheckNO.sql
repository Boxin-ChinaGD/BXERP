DROP PROCEDURE IF EXISTS `SPD_RetailTradeCommoditySource_CheckNO`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_RetailTradeCommoditySource_CheckNO`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iRetailTradeCommodityID INT;
	DECLARE iReducingCommodityID INT;
	DECLARE iNO INT;
	DECLARE iID INT;
	DECLARE iType INT;
	DECLARE iMultiple INT;
	DECLARE iReturnNO INT;
	DECLARE sGroup_concatID VARCHAR(20);
	DECLARE done INT DEFAULT FALSE;
	
	DECLARE list CURSOR FOR (SELECT F_ID AS iID, F_RetailTradeCommodityID AS iRetailTradeCommodityID, F_ReducingCommodityID AS iReducingCommodityID FROM t_retailtradecommoditysource);
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
			FETCH list INTO iID, iRetailTradeCommodityID, iReducingCommodityID;
			IF done THEN
				LEAVE read_loop;
			END IF;
			   
			-- ������۵���Ʒ��Դ��û�ж�Ӧ�����۵���Ʒ
			-- ���û�У���ô����Ϊ���ݲ�����
			IF NOT EXISTS(SELECT 1 FROM t_retailtradecommodity WHERE F_ID = iRetailTradeCommodityID) THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('IDΪ', iID, '�����۵���Ʒ��Դû�ж�Ӧ�����۵���Ʒ');
			-- ������۵���Ʒ��û�ж�Ӧ�����۵�
			-- ���û�У���ô����Ϊ���ݲ�����
			ELSEIF NOT EXISTS(SELECT 1 FROM t_retailtrade WHERE F_ID = (SELECT F_TradeID FROM t_retailtradecommodity WHERE F_ID = iRetailTradeCommodityID)) THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('IDΪ', iID, '�����۵���Ʒ��Դû�ж�Ӧ�����۵�');
			-- ������۵���Ʒ�Ƿ������Ʒ����
			-- ��������ڣ���ô����Ϊ���ݲ�����
			ELSEIF NOT EXISTS(SELECT 1 FROM t_commodity WHERE F_ID = iReducingCommodityID) THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('IDΪ', iID, '�����۵���Ʒ��Դû�ж�Ӧ����Ʒ');
			ELSE
				-- ��ȡ���۵���Ʒ������(��ͨ��Ʒ�������Ʒ�����װ��Ʒ��������Ʒ)
				-- ���iType��ֵ������0��1��2��3�е�ֵ����ô����Ϊ���ݲ�����
				SELECT F_Type INTO iType FROM t_commodity WHERE F_ID IN (SELECT F_CommodityID FROM t_retailtradecommodity WHERE F_ID = iRetailTradeCommodityID);
				-- ƴ����Դ��id
				SELECT group_concat(F_ID) INTO sGroup_concatID FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = iRetailTradeCommodityID;
				IF iType = 0 THEN
					-- �����������˻�ȥ���Ĵ��ڣ������˻�ʱ������Ҫ�����Դ��
					-- �����ͨ��Ʒ�Ŀ���Ƿ���ȷ
					IF NOT EXISTS(SELECT 1 FROM t_retailtradecommodity WHERE F_ID = iRetailTradeCommodityID AND F_NO = (SELECT sum(F_NO) FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = iRetailTradeCommodityID)
					) THEN				   
						SET done := TRUE;
						SET iErrorCode := 7;
						SET sErrorMsg := CONCAT('IDΪ', sGroup_concatID, '�����۵���Ʒ��Դ�Ŀ�����Ӧ�����۵���Ʒ�Ŀ�治���');
					END IF;
				ELSEIF iType = 1 THEN 
				    -- ��ȡ����
				    SELECT F_SubCommodityNO INTO iMultiple FROM t_subcommodity WHERE F_CommodityID = (SELECT F_CommodityID FROM t_retailtradecommodity WHERE F_ID = iRetailTradeCommodityID) AND F_SubCommodityID = iReducingCommodityID;
					-- ��������Ʒ�Ŀ���Ƿ���ȷ
					IF NOT EXISTS(SELECT 1 FROM t_retailtradecommodity WHERE F_ID = iRetailTradeCommodityID AND F_NO * iMultiple = 
						(SELECT sum(F_NO) FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = iRetailTradeCommodityID AND F_ReducingCommodityID = iReducingCommodityID)
					) THEN 
						SET done := TRUE;
						SET iErrorCode := 7;
						SET sErrorMsg := CONCAT('IDΪ', sGroup_concatID, '�����۵���Ʒ��Դ�Ŀ�����Ӧ�����۵���Ʒ�Ŀ�治���');
					END IF;
				ELSEIF iType = 2 THEN 
					-- ��ȡ����
				    SELECT sum(F_RefCommodityMultiple) INTO iMultiple FROM t_commodity WHERE F_ID = (SELECT F_CommodityID FROM t_retailtradecommodity WHERE F_ID = iRetailTradeCommodityID);
					-- ����Ƿ�����˻�,��������˻����ȼ�ȥ���˻��Ŀ��
					-- ��������Ʒ�Ŀ���Ƿ���ȷ
					IF NOT EXISTS(SELECT 1 FROM t_retailtradecommodity WHERE F_ID = iRetailTradeCommodityID AND F_NO * iMultiple = (SELECT sum(F_NO) FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = iRetailTradeCommodityID)) THEN 
						SET done := TRUE;
						SET iErrorCode := 7;
						SET sErrorMsg := CONCAT('IDΪ', sGroup_concatID, '�����۵���Ʒ��Դ�Ŀ�����Ӧ�����۵���Ʒ�Ŀ�治���');
				    END IF;
				ELSEIF iType = 3 THEN 
					-- ��������Ʒ�Ŀ���Ƿ���ȷ
					IF NOT EXISTS(SELECT 1 FROM t_retailtradecommodity WHERE F_ID = iRetailTradeCommodityID AND F_NO = (SELECT sum(F_NO) FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = iRetailTradeCommodityID)) THEN 
						SET done := TRUE;
						SET iErrorCode := 7;
						SET sErrorMsg := CONCAT('IDΪ', iID, '�����۵���Ʒ��Դ�Ŀ�����Ӧ�����۵���Ʒ�Ŀ�治���');
					END IF;
				ELSE 
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('IDΪ', iID, '�����۵���Ʒ��Դ��Ӧ����Ʒ�����Ͳ���ȷ');
				END IF;
				
			END IF;
		
		-- ������۵���Ʒ��Դ�Ŀ���Ƿ����Ӧ�����۵���Ʒ�Ŀ���Ƿ����(��ͨ��Ʒ�������Ʒ�����װ��Ʒ)
		-- ��������ڣ���ô����Ϊ���ݲ�����
		
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;