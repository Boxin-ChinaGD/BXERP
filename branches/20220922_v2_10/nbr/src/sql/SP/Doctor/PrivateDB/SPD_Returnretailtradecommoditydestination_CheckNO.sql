DROP PROCEDURE IF EXISTS `SPD_Returnretailtradecommoditydestination_CheckNO`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_Returnretailtradecommoditydestination_CheckNO`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iRetailTradeCommodityID INT;
	DECLARE iIncreasingCommodityID INT;
	DECLARE iNO INT;
	DECLARE iID INT;
	DECLARE iType INT;
	DECLARE iMultiple INT;
	DECLARE sGroup_concatID VARCHAR(20); 
	DECLARE done INT DEFAULT FALSE;
	
	DECLARE list CURSOR FOR (SELECT F_ID AS iID, F_RetailTradeCommodityID AS iRetailTradeCommodityID, F_IncreasingCommodityID AS iIncreasingCommodityID FROM t_returnretailtradecommoditydestination);
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
			FETCH list INTO iID, iRetailTradeCommodityID, iIncreasingCommodityID;
			IF done THEN
				LEAVE read_loop;
			END IF;	  
            
		    -- ����˻�����Ʒ�Ƿ������Ʒ����
		    IF NOT EXISTS(SELECT 1 FROM t_commodity WHERE F_ID = iIncreasingCommodityID) THEN
		        SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('IDΪ', iID, '���˻�����Ʒ��Դû�ж�Ӧ����Ʒ');
			--  ����˻�ȥ�����Ʒ���Ͳ���0��3����Ʒ
		    ELSEIF NOT EXISTS(SELECT 1 FROM t_commodity WHERE F_Type IN (0,3) AND F_ID = iIncreasingCommodityID) THEN
		        SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('IDΪ', iID, '���˻�ȥ������ƷID��Ӧ����Ʒ����ֻ����0��3����Ʒ');
			-- ����˻�ȥ���û�ж�Ӧ���˻�����Ʒ
			ELSEIF NOT EXISTS(SELECT 1 FROM t_retailtradecommodity WHERE F_ID = iRetailTradeCommodityID) THEN
			    SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('IDΪ', iID, '���˻�ȥ���û�ж�Ӧ���˻�����Ʒ');    
			-- ����˻�����Ʒ��û�ж�Ӧ���˻���
			ELSEIF NOT EXISTS(SELECT 1 FROM t_retailtrade WHERE F_ID = (SELECT F_TradeID FROM t_retailtradecommodity WHERE F_ID = iRetailTradeCommodityID)) THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('IDΪ', iID, '���˻�ȥ���û�ж�Ӧ���˻���'); 
			-- ÿһ���˻�ȥ����F_NO����С�ڻ���ڶ�Ӧ������ԴF_NO
            ELSEIF (SELECT sum(F_NO) FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID = iRetailTradeCommodityID AND F_IncreasingCommodityID = iIncreasingCommodityID) 
            		>	   
			       (
			       	SELECT sum(F_NO) FROM t_retailtradecommoditysource WHERE F_ReducingCommodityID = iIncreasingCommodityID AND F_RetailTradeCommodityID IN 
				       (SELECT F_ID FROM t_retailtradecommodity WHERE F_TradeID = 
				       		(
				       		SELECT F_SourceID FROM t_retailtrade WHERE F_ID = 
				       			(SELECT F_TradeID FROM t_retailtradecommodity WHERE F_ID = iRetailTradeCommodityID)
				       		)
				       	)
				   ) THEN 
			  
			    SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('IDΪ', iID, '�˻�ȥ����F_NO����С�ڻ����������ԴF_NO');
            ELSE 
		        -- ��ȡ���۵���Ʒ������(��ͨ��Ʒ�������Ʒ�����װ��Ʒ��������Ʒ)
		    	-- ���iType��ֵ������0��1��2��3�е�ֵ����ô����Ϊ���ݲ�����
		    	SELECT F_Type INTO iType FROM t_commodity WHERE F_ID IN (SELECT F_CommodityID FROM t_retailtradecommodity WHERE F_ID = iRetailTradeCommodityID);
		    	-- ƴ��ȥ���id
		        SELECT group_concat(F_ID) INTO sGroup_concatID FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID = iRetailTradeCommodityID; 
		        
				IF iType = 0 THEN			 
				    IF NOT EXISTS(SELECT 1 FROM t_retailtradecommodity WHERE F_ID = iRetailTradeCommodityID AND F_NO = 
				    	(SELECT sum(F_NO) FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID = iRetailTradeCommodityID)
				    ) THEN 
					   	SET done := TRUE;
						SET iErrorCode := 7;
						SET sErrorMsg := CONCAT('IDΪ', sGroup_concatID, 'ȥ���,��ͨ��ƷF_NO���Ӧ���˻���ƷF_NO�����');
				    END IF;
				ELSEIF iType = 1 THEN 
				    -- ��ȡ����
				    SELECT F_SubCommodityNO INTO iMultiple FROM t_subcommodity WHERE F_CommodityID = (SELECT F_CommodityID FROM t_retailtradecommodity WHERE F_ID = iRetailTradeCommodityID) 
				    	AND F_SubCommodityID = iIncreasingCommodityID;
					-- ���ȥ��������ƷF_NO���Ӧ���˻���ƷF_NO*iMultiple�Ƿ����	
					IF NOT EXISTS(SELECT 1 FROM t_retailtradecommodity WHERE F_ID = iRetailTradeCommodityID AND F_NO * iMultiple = 
						(SELECT sum(F_NO) FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID = iRetailTradeCommodityID AND F_IncreasingCommodityID = iIncreasingCommodityID)
					) THEN 			 			 
						SET done := TRUE;
						SET iErrorCode := 7;
						SET sErrorMsg := CONCAT('IDΪ', sGroup_concatID, '���˻�ȥ����F_NO����Ӧ���˻�����Ʒ��F_NO�����');
					END IF;
				ELSEIF iType = 2 THEN 
					-- ��ȡ����
				    SELECT sum(F_RefCommodityMultiple) INTO iMultiple FROM t_commodity WHERE F_ID = (SELECT F_CommodityID FROM t_retailtradecommodity WHERE F_ID = iRetailTradeCommodityID);
				   	-- ���ȥ�����װ��ƷF_NO���Ӧ���˻���ƷF_NO*iMultiple�Ƿ����
					IF NOT EXISTS(SELECT 1 FROM t_retailtradecommodity WHERE F_ID = iRetailTradeCommodityID AND F_NO * iMultiple = 
						(SELECT sum(F_NO) FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID = iRetailTradeCommodityID)
					) THEN 
						SET done := TRUE;
						SET iErrorCode := 7;
						SET sErrorMsg := CONCAT('IDΪ', sGroup_concatID, '���˻�ȥ���F_NO����Ӧ���˻�����Ʒ��F_NO�����');
				    END IF;
				ELSEIF iType = 3 THEN 
					-- ���ȥ����������ƷF_NO���Ӧ���˻���ƷF_NO�Ƿ����
					IF NOT EXISTS(SELECT 1 FROM t_retailtradecommodity WHERE F_ID = iRetailTradeCommodityID AND F_NO = 
						(SELECT sum(F_NO) FROM t_returnretailtradecommoditydestination WHERE F_RetailTradeCommodityID = iRetailTradeCommodityID)
					) THEN 			   
						SET done := TRUE;
						SET iErrorCode := 7;
						SET sErrorMsg := CONCAT('IDΪ', iID, '���˻�ȥ���F_NO����Ӧ���˻�����Ʒ��F_NO�����');
					END IF;       
                    
	           END IF ;
		   
		     END IF ;
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;