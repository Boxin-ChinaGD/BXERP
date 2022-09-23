DROP FUNCTION IF EXISTS Func_DeleteRetailTradeCommoditySource;
-- ... ҵ���߼��Ѿ��ı䣬������������Ҫ�ı�ΪFunc_GenerateReturnRetailTradeCommodityDestination��ע�ͣ��˻�ʱ��������Դ������ȥ���
CREATE DEFINER=`root`@`localhost` FUNCTION `Func_DeleteRetailTradeCommoditySource`(
	iCommodityID INT,					-- Ҫ���ĸ���Ʒ
	returnNO INT, 						-- �˻���Ʒ���� 
	canReturnNO INT, 					-- ���˻�����
	currentWarehousingID INT, 			-- ��ֵ���ID
	retailTradeCommodityID INT 			-- �˻���Դ��(���۵�)�Ĵӱ�ID����t_retailtradecommoditysource�ĸ���ID
) RETURNS int(11)
BEGIN
	DECLARE iID INT;
	DECLARE iShopID INT;
	DECLARE NO INT;
	DECLARE sourceNO INT;
	DECLARE noSalable INT; 				-- ������Ʒ����
	DECLARE warehousingNO INT;
	DECLARE warehousingID INT;
	DECLARE iIncreasingCommodityID INT; 	-- �˻���ƷID,���ڲ����˻���Ʒȥ���
	DECLARE iReturnwarehousingID INT;		-- ��ⵥID,���ڲ����˻���Ʒȥ���
	DECLARE iRetailTradeCommodityID INT;		-- �˻����ӱ��ID,���ڲ����˻���Ʒȥ���
	
	DECLARE done INT DEFAULT FALSE;		-- ����������־����
	
	-- ���������iCommodityID������
	DECLARE list CURSOR FOR (SELECT 
		F_ID AS iID, 
		F_NO AS warehousingNO,
		F_NOSalable AS noSalable, 
		F_WarehousingID AS warehousingID
	FROM t_warehousingcommodity wc WHERE F_CommodityID = iCommodityID 
	AND (SELECT F_Status FROM t_warehousing WHERE F_ID = wc.F_WarehousingID ) = 1 -- =1Ϊ�����״̬
	AND (CASE ISNULL(currentWarehousingID) 
			WHEN 1 THEN 
				1=1 
			ELSE 
				F_ID <= (SELECT F_ID FROM t_warehousingcommodity WHERE F_WarehousingID = currentWarehousingID AND F_CommodityID = iCommodityID) 
			END)
	ORDER BY F_ID DESC
	);
	
	-- ������Ӧ����Դ��
	DECLARE listSource CURSOR FOR (
		SELECT 
			F_reducingCommodityID AS iIncreasingCommodityID, 
			F_NO AS iSourceNO,
			F_WarehousingID AS iReturnwarehousingID
		FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = retailTradeCommodityID AND F_ReducingCommodityID = iCommodityID
		ORDER BY F_ID DESC
	);
	
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  -- ָ���α�ѭ������ʱ����ֵ

	
	SET NO := returnNO;
	
	SELECT F_ShopID INTO iShopID FROM t_retailtrade WHERE F_ID = (SELECT F_TradeID FROM t_retailtradecommodity WHERE F_ID = retailTradeCommodityID);
	
	IF NO > canReturnNO THEN
		RETURN (7);
	ELSEIF EXISTS(SELECT 1 FROM t_warehousingcommodity WHERE F_CommodityID = iCommodityID) THEN
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iID, warehousingNO, noSalable, warehousingID;
	
			IF done THEN
		  		LEAVE read_loop;
			END IF;
			
			-- ���(�˻�����+��ⵥ��Ʒ��������)С�����������ֱ��Update��ⵥ��Ʒ�������������Ʒ�ĵ�ֵ���ID��
			IF (NO + noSalable) <= warehousingNO THEN
				UPDATE t_warehousingcommodity SET F_NOSalable = NO + noSalable WHERE F_WarehousingID = warehousingID AND F_CommodityID = iCommodityID;  
				
				UPDATE t_commodityshopinfo SET F_CurrentWarehousingID = warehousingID WHERE F_CommodityID = iCommodityID AND F_ShopID = iShopID;
				
				SET NO := 0; 
					
				LEAVE read_loop;
			-- ���(�˻�����+��ⵥ��Ʒ��������)��������������ȼ�������ֵ��Ȼ�������ⵥ��Ʒ��������Ϊ���������
			ELSE 
				SET NO := (NO + noSalable) - warehousingNO;  
				
				UPDATE t_warehousingcommodity SET F_NOSalable = warehousingNO WHERE F_WarehousingID = warehousingID AND F_CommodityID = iCommodityID;
			END IF;
				
		END LOOP read_loop;
		CLOSE list;
		
		-- ���ѭ�����˻���Ʒ����>0 ��Update���һ����ⵥ�Ŀ�������
		IF NO > 0 THEN
			UPDATE t_warehousingcommodity wc SET F_NOSalable = F_NOSalable + NO 
			WHERE F_CommodityID = iCommodityID
			AND (SELECT F_Status FROM t_warehousing WHERE F_ID = wc.F_WarehousingID ) = 1 
			ORDER BY F_ID LIMIT 1;
			
			UPDATE t_commodityshopinfo SET F_CurrentWarehousingID = 
				(SELECT F_WarehousingID FROM t_warehousingcommodity wc
				WHERE F_CommodityID = iCommodityID 
				AND (SELECT F_Status FROM t_warehousing WHERE F_ID = wc.F_WarehousingID ) = 1  
				ORDER BY F_ID LIMIT 1) 
			WHERE F_CommodityID = iCommodityID AND F_ShopID = iShopID;
		END IF;
	END IF;
	
	-- ����Ʒ�˻�ʱ����T_ReturnRetailTradeCommodityDestination�������ݣ���¼��Ʒ�˻���������������������ⵥ
	SET NO := returnNO;
	SET done := FALSE; -- ��ʼ��done
	OPEN listSource;
		read_loop: LOOP
			FETCH listSource INTO iIncreasingCommodityID, sourceNO, iReturnwarehousingID;
			
			IF done THEN
		  		LEAVE read_loop;
			END IF;

			-- ����˻����� - ��Դ������� > 0 �����һ����Դ���������꣬��Ҫ�Եڶ�����Դ������˻�
			-- ��������������ⵥ1����Ʒ5��;��ⵥ2����Ʒ5��������10�����˻�8��; ��һ�Σ�8 - 5 = 3 > 0���ڶ��Σ�3 - 5 = -2 < 0�� 
			-- ������10������һ�Σ�10 - 5 = 5 > 0���ڶ��Σ�5 - 5 = 0 = 0
			-- ע�⣺F_RetailTradeCommodityID���˻����Ĵӱ�ID
			
			-- ʹ�����۵�Դ��(���۵�)�Ĵӱ��ID ���ҵ��˻����ӱ��id
			SELECT F_ID INTO iRetailTradeCommodityID
			FROM t_retailtradecommodity WHERE F_CommodityID = (SELECT F_CommodityID FROM t_retailtradecommodity WHERE F_ID = retailTradeCommodityID) 
			AND F_TradeID = (
				SELECT F_ID FROM t_retailtrade WHERE F_SourceID = ( -- �ҵ��˻�����ID
					SELECT F_TradeID 
					FROM t_retailtradecommodity 
					WHERE F_ID = retailTradeCommodityID
				) 
			);
			
--			IF iRetailTradeCommodityID IS NOT NULL THEN 
				IF (NO - sourceNO) > 0 THEN 
					INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
					VALUES (iRetailTradeCommodityID, iIncreasingCommodityID, sourceNO, iReturnwarehousingID);
					
					SET NO := NO - sourceNO;
				ELSE 
				 	-- ���˻����� - ��Դ������� <= 0 
					INSERT INTO t_returnretailtradecommoditydestination (F_RetailTradeCommodityID, F_IncreasingCommodityID, F_NO, F_WarehousingID)
					VALUES (iRetailTradeCommodityID, iIncreasingCommodityID, NO, iReturnwarehousingID);
					
					LEAVE read_loop;
				END IF; 
--			END IF; 
				
		END LOOP read_loop;
		CLOSE listSource;

	RETURN (0);
END;