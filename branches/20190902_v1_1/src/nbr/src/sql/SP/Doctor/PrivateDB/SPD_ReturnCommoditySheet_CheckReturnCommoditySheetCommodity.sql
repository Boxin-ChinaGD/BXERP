DROP PROCEDURE IF EXISTS `SPD_ReturnCommoditySheet_CheckReturnCommoditySheetCommodity`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_ReturnCommoditySheet_CheckReturnCommoditySheetCommodity`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	-- ...临时模板，下面的值需要根据需求变化
	DECLARE iID INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- 临时模板，下面list需要根据需求变化
	DECLARE list CURSOR FOR (SELECT F_ID AS iID FROM t_returncommoditysheet);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iID;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- 检查退货单是否存在退货商品
			-- 如果不存在，则认为数据不健康
			IF EXISTS (SELECT 1 FROM t_returncommoditysheetcommodity WHERE F_ReturnCommoditySheetID = iID) THEN
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			ELSE
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := concat('退货单:', iID, '没有退货商品');
			END IF;
			
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;