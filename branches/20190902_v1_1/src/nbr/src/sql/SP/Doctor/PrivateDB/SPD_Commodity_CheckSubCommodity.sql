DROP PROCEDURE IF EXISTS `SPD_Commodity_CheckSubCommodity`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_Commodity_CheckSubCommodity`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iCommodityID INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	DECLARE list CURSOR FOR (SELECT F_ID AS iCommodityID FROM t_commodity WHERE F_Status <> 2 AND F_Type = 1);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iCommodityID;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			IF NOT EXISTS(SELECT 1 FROM t_subcommodity WHERE F_CommodityID = iCommodityID) THEN 
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('组合商品', iCommodityID ,'没有对应的子商品');
			ELSEIF EXISTS(SELECT 1 FROM t_commodity WHERE F_ID IN (SELECT F_SubCommodityID FROM t_subcommodity WHERE F_CommodityID = iCommodityID) AND F_Type != 0) THEN 
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('组合商品', iCommodityID ,'对应的子商品不是普通商品');
			ELSE
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			END IF;
		
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;