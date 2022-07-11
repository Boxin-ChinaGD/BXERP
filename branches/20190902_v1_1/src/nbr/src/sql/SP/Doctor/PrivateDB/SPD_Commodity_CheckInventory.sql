DROP PROCEDURE IF EXISTS `SPD_Commodity_CheckInventory`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_Commodity_CheckInventory`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iCommodityID INT;
	DECLARE iType INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- 临时模板，下面list需要根据需求变化
	DECLARE list CURSOR FOR (SELECT F_ID AS iCommodityID, F_Type AS iType FROM t_commodity WHERE F_Status <> 2);
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
			
			-- 只有单品可以被盘点
			IF EXISTS(SELECT 1 FROM t_inventorycommodity WHERE F_CommodityID = iCommodityID) AND iType <> 0 THEN
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('商品', iCommodityID ,'不是单品，不能被盘点');
			-- 没有盘点单就不用检查,单品也有没有盘点单都行
			ELSE 
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			END IF;
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;