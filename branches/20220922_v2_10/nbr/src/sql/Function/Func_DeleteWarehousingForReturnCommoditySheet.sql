drop function IF EXISTS Func_DeleteWarehousingForReturnCommoditySheet;
CREATE DEFINER=`root`@`localhost` FUNCTION `Func_DeleteWarehousingForReturnCommoditySheet`(
	iCommodityID INT,
	returnNO INT, -- �˻���Ʒ���� 
	currentWarehousingID INT, -- ��ֵ���ID
	iShopID INT
) RETURNS int(11)
BEGIN
	DECLARE iID INT;
	DECLARE NO INT;
	DECLARE noSalable INT; -- ������Ʒ����
	DECLARE warehousingID INT;
	DECLARE done INT DEFAULT FALSE;	-- ����������־����
	DECLARE list CURSOR FOR (SELECT 
		F_ID AS iID, 
		F_NOSalable AS noSalable, 
		F_WarehousingID AS warehousingID
	FROM t_warehousingcommodity wc
	WHERE F_CommodityID = iCommodityID
	AND (SELECT F_Status FROM t_warehousing WHERE F_ID = wc.F_WarehousingID AND F_ShopID = iShopID) = 1 
	AND (CASE ISNULL(currentWarehousingID) 
			WHEN 1 THEN 
				1=1 
			ELSE 
				F_ID >= (SELECT F_ID FROM t_warehousingcommodity WHERE F_WarehousingID = currentWarehousingID AND F_CommodityID = iCommodityID) 
			END)
	);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  -- ָ���α�ѭ������ʱ����ֵ
	
	
	SET NO := returnNO;
	IF EXISTS(SELECT 1 FROM t_warehousingcommodity WHERE F_CommodityID = iCommodityID) THEN
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iID, noSalable, warehousingID;
	
			IF done THEN
		  		LEAVE read_loop;
			END IF;
			-- ���(�˻�����<=��ⵥ��Ʒ��������)��ֱ��Update��ⵥ��Ʒ����������Ϳ�����������Ʒ�ĵ�ֵ���ID��
			IF (NO <= noSalable) THEN
					
				UPDATE t_warehousingcommodity SET F_NOSalable = F_NOSalable - NO WHERE F_WarehousingID = warehousingID AND F_CommodityID = iCommodityID;  
				
--				UPDATE t_commodity SET F_CurrentWarehousingID = warehousingID WHERE F_ID = iCommodityID;
				UPDATE t_commodityshopinfo SET F_CurrentWarehousingID = warehousingID WHERE F_CommodityID = iCommodityID AND F_ShopID = iShopID;
				
				SET NO := 0; 
					
				LEAVE read_loop;
			-- ���(�˻�����>��ⵥ��Ʒ�������������ȼ�������ֵ��Ȼ�������ⵥ��Ʒ��������Ϊ0���������Ϊ���������ȥ����������
			ELSE 
				SET NO := NO - noSalable;  
				
				UPDATE t_warehousingcommodity SET F_NOSalable = 0 WHERE F_WarehousingID = warehousingID AND F_CommodityID = iCommodityID;
			END IF;
			
		END LOOP read_loop;
		CLOSE list;
		
		-- ���ѭ�����˻���Ʒ����>0 ��Update���һ����ⵥ�Ŀ�������
		IF NO > 0 THEN
			UPDATE t_warehousingcommodity wc SET F_NOSalable = F_NOSalable - NO 
			WHERE F_CommodityID = iCommodityID 
			AND (SELECT F_Status FROM t_warehousing WHERE F_ID = wc.F_WarehousingID ) = 1 
			ORDER BY F_ID DESC LIMIT 1;
			
			UPDATE t_commodityshopinfo SET F_CurrentWarehousingID = 
				(SELECT F_WarehousingID FROM t_warehousingcommodity wc
				WHERE F_CommodityID = iCommodityID
				AND (SELECT F_Status FROM t_warehousing WHERE F_ID = wc.F_WarehousingID ) = 1  
				ORDER BY F_ID DESC LIMIT 1) 
			WHERE F_CommodityID = iCommodityID AND F_ShopID = iShopID;
		END IF;
						
		RETURN (0);
	ELSE 
		RETURN (2);
	END IF;
END;