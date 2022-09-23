
-- ����ֿ��е�����ֵ ��
-- ���ֿ�Ŀ���ܶ��������(���ɹ���)����
-- ����ܶ���ߵ���Ʒ�Ŀ���ܶ�
-- ����ܶ���ߵ���Ʒ
DROP PROCEDURE IF EXISTS `SP_Warehouse_RetrieveInventory`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Warehouse_RetrieveInventory` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iShopID INT 
	)
BEGIN  
	DECLARE iNO INT;
	DECLARE iCommID INT;
	DECLARE fPrice Decimal(20, 6); 
	DECLARE fTotalInventory Decimal(20, 6); 
	DECLARE fMaxTotalInventory Decimal(20, 6); 
    DECLARE fTmpAmount Decimal(20, 6);
    DECLARE	sTargetCommodityName VARCHAR(32);
    DECLARE	sCommodityName VARCHAR(32);
    DECLARE iMaxTotalInventoryNO INT;
    DECLARE fMaxTotalInventoryPrice Decimal(20, 6); 
    
    DECLARE done INT DEFAULT  false;  
    
    -- ����������������㣬����������ʷƽ��ֵ��Ҳ��������ⵥ�Ľ����ۼ���
    -- ֻ���㵥Ʒ
--	DECLARE list CURSOR FOR (SELECT F_NO AS iNO, F_LatestPricePurchase AS fPrice, F_Name AS sTargetCommodityName FROM t_commodity WHERE F_Status != 2 AND F_Type = 0);
	DECLARE list CURSOR FOR (SELECT F_ID AS iCommID, F_Name AS sTargetCommodityName FROM t_commodity WHERE F_Status != 2 AND F_Type = 0);
	
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = true;  
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
    
    START TRANSACTION;
    -- ������ܶ��δɾ���ĵ�Ʒʱ���Ե�һ����Ϊ��ʼֵ�������Ϊ0
    IF EXISTS(SELECT 1 FROM t_commodity WHERE F_Status != 2 AND F_Type = 0) THEN 
--		SELECT F_NO,F_LatestPricePurchase,F_Name into iMaxTotalInventoryNO, fMaxTotalInventoryPrice,sCommodityName FROM t_commodity WHERE F_Status != 2 AND F_Type = 0 limit 1;
    	SELECT F_Name into sCommodityName FROM t_commodity WHERE F_Status != 2 AND F_Type = 0 limit 1;
    	SELECT F_NO,F_LatestPricePurchase into iMaxTotalInventoryNO, fMaxTotalInventoryPrice FROM t_commodityshopinfo WHERE F_ShopID = iShopID AND F_CommodityID = (SELECT F_ID FROM t_commodity WHERE F_Status != 2 AND F_Type = 0 limit 1);
    	SET fMaxTotalInventory = iMaxTotalInventoryNO * fMaxTotalInventoryPrice;
    ELSE		  
	    SET fMaxTotalInventory = 0.000000;  
    END IF;
    
	    SET fTotalInventory = 0.000000;
--	    OPEN  list;  
--	    	read_loop: LOOP	  
--	    FETCH list INTO iNO, fPrice, sTargetCommodityName;   
--		    IF done THEN  
--		        LEAVE read_loop;   
--		    END IF;  
--		    IF fPrice - -1.000000 < 0.000001 THEN 
--		    	SET fPrice = 0.000000;-- ������Ʒ������ɹ��ۣ������ۣ���û���κν����������Ĭ��ֵ��-1��Ϊ�˱��������󣬽�fPrice��Ϊ0
--		    END IF;
--
--	    	SET fTmpAmount = iNO * fPrice;
--	   		SET fTotalInventory = fTotalInventory + fTmpAmount;
--	   		IF fMaxTotalInventory < fTmpAmount THEN 
--	   			SET fMaxTotalInventory = fTmpAmount;
--	   			SET sCommodityName = sTargetCommodityName;
--	   		END IF;
--	 	END LOOP read_loop;
--	   	CLOSE list;
	    OPEN  list;  
	    	read_loop: LOOP	  
	    FETCH list INTO iCommID, sTargetCommodityName;   
		    IF done THEN  
		        LEAVE read_loop;   
		    END IF;  
		    SELECT F_NO, F_LatestPricePurchase INTO iNO, fPrice FROM t_commodityshopinfo WHERE F_CommodityID = iCommID AND F_ShopID = iShopID;
		    IF fPrice - -1.000000 < 0.000001 THEN 
		    	SET fPrice = 0.000000;-- ������Ʒ������ɹ��ۣ������ۣ���û���κν����������Ĭ��ֵ��-1��Ϊ�˱��������󣬽�fPrice��Ϊ0
		    END IF;

	    	SET fTmpAmount = iNO * fPrice;
	   		SET fTotalInventory = fTotalInventory + fTmpAmount;
	   		IF fMaxTotalInventory < fTmpAmount THEN 
	   			SET fMaxTotalInventory = fTmpAmount;
	   			SET sCommodityName = sTargetCommodityName;
	   		END IF;
	 	END LOOP read_loop;
	   	CLOSE list;
	   	SELECT fMaxTotalInventory, fTotalInventory, sCommodityName;
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END;  