DROP PROCEDURE IF EXISTS `SPD_Commodity_CheckCategoryID`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_Commodity_CheckCategoryID`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iCommodityID INT;
	DECLARE iStatus INT;
	DECLARE iCategoryID INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- 临时模板，下面list需要根据需求变化
	DECLARE list CURSOR FOR (SELECT F_ID AS iCommodityID, F_Status AS iStatus FROM t_commodity);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iCommodityID, iStatus;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- 检查检查所有商品的CategoryID，删除前应当存在相应的CategoryID,删除后应当为null
			IF iStatus <> 2 THEN
				-- 
				IF EXISTS(SELECT 1 FROM t_category WHERE F_ID IN (SELECT F_CategoryID FROM t_commodity WHERE F_ID = iCommodityID)) THEN 
					SET iErrorCode := 0;
					SET sErrorMsg := '';
				ELSE
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('商品', iCommodityID ,'没有对应的类别ID');
				END IF;
			ELSE	
			   SELECT F_CategoryID INTO iCategoryID FROM t_commodity WHERE F_ID = iCommodityID;
			   
			   IF iCategoryID IS NULL THEN
					SET iErrorCode := 0;
					SET sErrorMsg := '';
				ELSE
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('删除商品', iCommodityID ,'没有正确更改对应的类别ID，它应该是null');
				END IF;
			END IF;
			
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;