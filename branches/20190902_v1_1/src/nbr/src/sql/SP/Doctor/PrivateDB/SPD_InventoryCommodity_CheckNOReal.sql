DROP PROCEDURE IF EXISTS `SPD_InventoryCommodtiy_CheckNOReal`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_InventoryCommodtiy_CheckNOReal`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iInventoryCommodityID INT;
	DECLARE iNOReal INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	DECLARE list CURSOR FOR (SELECT F_ID AS iInventoryCommodityID, F_NOReal AS iNOReal FROM t_inventorycommodity);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iInventoryCommodityID,iNOReal;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- 检查盘点单商品的实盘数量是否大于等于0
			IF iNOReal >= 0 THEN
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			ELSE 
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('盘点单商品', iInventoryCommodityID ,'实盘数量不正确，它要大于等于0');
			END IF;
			
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;