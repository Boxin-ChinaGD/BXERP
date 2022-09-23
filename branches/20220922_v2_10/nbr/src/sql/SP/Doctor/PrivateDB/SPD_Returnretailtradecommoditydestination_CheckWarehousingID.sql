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
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
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
		         -- ����������Ʒ���ID��ΪNULL��
		        IF NOT EXISTS (SELECT 1 FROM t_returnretailtradecommoditydestination WHERE F_IncreasingCommodityID = 
		        	(SELECT F_ID FROM t_commodity WHERE F_ID = iIncreasingCommodityID AND F_Type = 3 ) 
		        	AND F_WarehousingID IS NULL
		        ) THEN 
		            SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('IDΪ', iID, '���˻�ȥ�������͵����IDֻ��Ϊnull');
		        END IF ;
		    ELSE 
	            -- �����ⵥΪδ���״̬���˻�ȥ������IDΪNULL    
	            IF EXISTS (SELECT 1 FROM t_warehousing WHERE F_Status = 0 AND F_ID = iWarehousingID) THEN 
	               SET done := TRUE;
				   SET iErrorCode := 7;
				   SET sErrorMsg := CONCAT('IDΪ', iID, '���˻�ȥ������ID��Ӧ����ⵥ����δ���״̬,ȥ������IDֻ��ΪNULL');				   
				-- ���δ�����Ʒ���˻�ȥ������ID��ΪNULL
			   	ELSEIF NOT EXISTS (SELECT 1 FROM t_warehousingcommodity WHERE F_CommodityID = iIncreasingCommodityID) THEN
			   	   IF EXISTS (SELECT 1 FROM t_returnretailtradecommoditydestination WHERE F_IncreasingCommodityID = iIncreasingCommodityID AND F_WarehousingID IS NOT NULL) THEN 
				   	   SET done := TRUE;
				       SET iErrorCode := 7;
					   SET sErrorMsg := CONCAT('IDΪ', iID, '���˻�ȥ���,δ�����Ʒ�����IDֻ��Ϊnull'); 
				   END IF ;        
	            END IF;  	            
		    END IF ;
		
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;