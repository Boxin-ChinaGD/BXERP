-- 1.������ƷID ����Ʒ���ȡ��ֵ���ID
-- 2.�Աȸ���ⵥ��Ʒ�����������۳���Ʒ����
-- 2.1 ����������������۳���Ʒ��������ֱ�Ӵ�����Դ����ⵥ��Ʒ��ȥ��Ӧ�Ŀ�������
-- 2.2.1 �����������С���۳���Ʒ���������ȴ���һ����Դ����Դ����������ڵ�ǰ���۵���Ʒ���۳����������ǿ��������
-- 2.2.2 ��ѯ����Ʒ����һ����ⵥ���жϿ���������ʣ���۳���Ʒ����
-- 2.2.3 ����������������۳���Ʒ�������ظ�2.1��Update��Ʒ��ĵ�ֵ���ID�������ظ�2.2.1 - 2.2.3
drop function IF EXISTS Func_CreateRetailTradeCommoditySource;
CREATE DEFINER=`root`@`localhost` FUNCTION `Func_CreateRetailTradeCommoditySource`(
	iCommodityID INT,
	saleNO INT, -- ������Ʒ���� 
	currentWarehousingID INT, -- ��ֵ���ID
	retailTradeCommodityID INT
) RETURNS int(11)
BEGIN
	DECLARE iID INT;
	DECLARE iShopID INT;
	DECLARE iSaleNOCloned INT;
	DECLARE iWarehousingID INT;
	DECLARE lastInsertID INT;
	DECLARE noSalable INT; -- ������Ʒ����
	DECLARE warehousingID INT;
	DECLARE done INT DEFAULT FALSE;	-- ����������־����
	DECLARE list CURSOR FOR (SELECT 
		F_ID AS iID, 
		F_NOSalable AS noSalable, 
		F_WarehousingID AS warehousingID
	FROM t_warehousingcommodity wc WHERE F_CommodityID = iCommodityID 
	AND (SELECT F_Status FROM t_warehousing WHERE F_ID = wc.F_WarehousingID ) = 1 
	AND (CASE ISNULL(currentWarehousingID) 
			WHEN 1 THEN 
				1=1 
			ELSE 
				F_ID >= (SELECT F_ID FROM t_warehousingcommodity WHERE F_WarehousingID = currentWarehousingID AND F_CommodityID = iCommodityID LIMIT 1) 
			END)
	);
	
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  -- ָ���α�ѭ������ʱ����ֵ
	
	SET iSaleNOCloned := saleNO;
	SELECT F_ShopID INTO iShopID FROM t_retailtrade WHERE F_ID = (SELECT F_TradeID FROM t_retailtradecommodity WHERE F_ID = retailTradeCommodityID);
	
	IF EXISTS(SELECT 1 FROM t_warehousingcommodity wc WHERE F_CommodityID = iCommodityID AND (SELECT F_Status FROM t_warehousing WHERE F_ID = wc.F_WarehousingID ) = 1 ) THEN
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iID, noSalable, warehousingID; -- noSalable�����Ǹ�����������������Ʒ����������Ʒ����ʱ���һ�����Ŀ��������Ǹ���
			IF done THEN
		  		LEAVE read_loop;
			END IF;
			-- �����ֵ���ID�Ŀ�������С��0��������������ʱ�������ݼ��㲻��ȷ.
			IF noSalable > 0 THEN
				-- ��������������ڻ�����۳���Ʒ��������ֱ�Ӵ�����Դ����ⵥ��Ʒ��ȥ��Ӧ�Ŀ���������Update��Ʒ�ĵ�ֵ���ID
				IF noSalable >= saleNO THEN
					INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID)
					VALUES (retailTradeCommodityID, saleNO, warehousingID, iCommodityID);
					
					UPDATE t_warehousingcommodity SET F_NOSalable = F_NOSalable - saleNO WHERE F_WarehousingID = warehousingID AND F_CommodityID = iCommodityID;  
					
					UPDATE t_commodityshopinfo SET F_CurrentWarehousingID = warehousingID WHERE F_CommodityID = iCommodityID AND F_ShopID = iShopID;
					
					SET saleNO := 0; 
					
					LEAVE read_loop;
				-- �����������С���۳���Ʒ���������ȴ���һ����Դ����Դ����������ڵ�ǰ���۵���Ʒ���۳�����
				ELSE 
					SET saleNO := saleNO - noSalable; -- ��֤��Դ����F_NO������
					
					INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID)
					VALUES (retailTradeCommodityID, noSalable, warehousingID, iCommodityID);	   
					
					SET lastInsertID := LAST_INSERT_ID();
					UPDATE t_warehousingcommodity SET F_NOSalable = 0 WHERE F_WarehousingID = warehousingID AND F_CommodityID = iCommodityID;
				END IF;
			END IF;
				
		END LOOP read_loop;
		CLOSE list;
		
		-- ���ѭ�����۳���Ʒ����>0 ��Update���������Դ������
		IF saleNO > 0 THEN
			-- ��iSaleNOCloned = saleNOʱ������û���κ���⣬���߰����һ����ⵥ�Ŀ����������ɸ��� ����������Դ��δ���������Ʒ��������Ϣ 
			IF iSaleNOCloned = saleNO THEN 
				-- Ѱ�����һ����ⵥID��������Դ�� ����ѯ����ʱ��Ϊnull������û�����
				SELECT F_WarehousingID INTO iWarehousingID FROM t_warehousingcommodity wc WHERE F_CommodityID = iCommodityID AND (SELECT F_Status FROM t_warehousing WHERE F_ID = wc.F_WarehousingID ) = 1 
				ORDER BY F_WarehousingID DESC LIMIT 1;
				
				INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID) VALUES (retailTradeCommodityID, saleNO, iWarehousingID, iCommodityID);
			ELSE -- iSaleNOCloned > saleNO������������α��Ѿ��������Դ��lastInsertID��Ȼ>0
				UPDATE t_retailtradecommoditysource SET F_NO = F_NO + saleNO WHERE F_ID = lastInsertID;
		   	END IF;
		   	 
			UPDATE t_warehousingcommodity wc SET F_NOSalable = F_NOSalable - saleNO -- F_NOSalable�����Ǹ���
			WHERE F_CommodityID = iCommodityID 
			AND (SELECT F_Status FROM t_warehousing WHERE F_ID = wc.F_WarehousingID ) = 1
			ORDER BY F_ID DESC LIMIT 1;
			
			UPDATE t_commodityshopinfo SET F_CurrentWarehousingID = 
				(SELECT F_WarehousingID FROM t_warehousingcommodity wc WHERE F_CommodityID = iCommodityID 
				AND (SELECT F_Status FROM t_warehousing WHERE F_ID = wc.F_WarehousingID ) = 1 
				ORDER BY F_ID DESC LIMIT 1)
			WHERE F_CommodityID = iCommodityID AND F_ShopID = iShopID;
		END IF;
		
	ELSE 	
		-- ��Ϊ�¿�����û�������û���ڳ�ֵ����Ʒ����ֱ�����������������۵����Ҳ�����Ӧ������¼�ģ���ֱ�Ӳ�������
		INSERT INTO t_retailtradecommoditysource (F_RetailTradeCommodityID, F_NO, F_WarehousingID, F_ReducingCommodityID)
		VALUES (retailTradeCommodityID, saleNO, NULL, iCommodityID);	   				
	END IF;
	
	RETURN (0);	
END;