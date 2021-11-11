DROP PROCEDURE IF EXISTS `SPD_Commodity_CheckProvider`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_Commodity_CheckProvider`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iCommodityID INT;
	DECLARE iType INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	DECLARE list CURSOR FOR (SELECT F_ID AS iCommodityID, F_Type AS iType FROM t_commodity WHERE F_Status <> 2 AND F_Type <> 1 AND F_Type <> 3);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iCommodityID, iType;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			IF iType = 0 THEN
			
				IF EXISTS (SELECT 1 FROM t_providercommodity WHERE F_CommodityID = iCommodityID) THEN 
					SET iErrorCode := 0;
					SET sErrorMsg := '';
				ELSE
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('商品', iCommodityID ,'没有对应的供应商');
				END IF;
				
			ELSE
				
				IF NOT EXISTS (SELECT 1 FROM t_providercommodity WHERE F_CommodityID = (SELECT F_RefCommodityID FROM t_commodity WHERE F_ID = iCommodityID)) THEN
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('多包装商品', iCommodityID, '对应的单品没有供应商');
				ELSE
					SET iErrorCode := 0;
					SET sErrorMsg := '';
				END IF;
			END IF;
			
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;