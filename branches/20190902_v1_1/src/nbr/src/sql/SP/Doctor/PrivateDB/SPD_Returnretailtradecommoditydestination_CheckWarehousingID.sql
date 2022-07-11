DROP PROCEDURE IF EXISTS `SPD_Returnretailtradecommoditydestination_CheckWarehousingID`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_Returnretailtradecommoditydestination_CheckWarehousingID`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iRetailTradeCommodityID INT;
	DECLARE iIncreasingCommodityID INT;
	DECLARE iID INT;
	DECLARE iWarehousingID INT;
	DECLARE iType INT;
	DECLARE done INT DEFAULT FALSE;
	
	DECLARE list CURSOR FOR (SELECT F_ID AS iID, F_RetailTradeCommodityID AS iRetailTradeCommodityID, F_IncreasingCommodityID AS iIncreasingCommodityID,F_WarehousingID AS iWarehousingID  FROM t_returnretailtradecommoditydestination);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	START TRANSACTION;
	
	  	SET iErrorCode := 0; 
	    SET sErrorMsg := '';
	            
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iID, iRetailTradeCommodityID, iIncreasingCommodityID,iWarehousingID;
			IF done THEN
				LEAVE read_loop;
			END IF;
			SELECT F_Type INTO iType FROM t_commodity WHERE F_ID IN (SELECT F_CommodityID FROM t_retailtradecommodity WHERE F_ID = iRetailTradeCommodityID);
			 
		    IF iType = 3 THEN
		         -- 检查服务型商品入库ID都为NULL。
		        IF NOT EXISTS (SELECT 1 FROM t_returnretailtradecommoditydestination WHERE F_IncreasingCommodityID = 
		        	(SELECT F_ID FROM t_commodity WHERE F_ID = iIncreasingCommodityID AND F_Type = 3 ) 
		        	AND F_WarehousingID IS NULL
		        ) THEN 
		            SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('ID为', iID, '的退货去向表服务型的入库ID只能为null');
		        END IF ;
		    ELSE 
	            -- 检查入库单为未审核状态，退货去向表入库ID为NULL    
	            IF EXISTS (SELECT 1 FROM t_warehousing WHERE F_Status = 0 AND F_ID = iWarehousingID) THEN 
	               SET done := TRUE;
				   SET iErrorCode := 7;
				   SET sErrorMsg := CONCAT('ID为', iID, '的退货去向表入库ID对应的入库单还是未审核状态,去向表入库ID只能为NULL');				   
				-- 检查未入库商品，退货去向表入库ID都为NULL
			   	ELSEIF NOT EXISTS (SELECT 1 FROM t_warehousingcommodity WHERE F_CommodityID = iIncreasingCommodityID) THEN
			   	   IF EXISTS (SELECT 1 FROM t_returnretailtradecommoditydestination WHERE F_IncreasingCommodityID = iIncreasingCommodityID AND F_WarehousingID IS NOT NULL) THEN 
				   	   SET done := TRUE;
				       SET iErrorCode := 7;
					   SET sErrorMsg := CONCAT('ID为', iID, '的退货去向表,未入库商品的入库ID只能为null'); 
				   END IF ;        
	            END IF;  	            
		    END IF ;
		
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;