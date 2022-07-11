DROP PROCEDURE IF EXISTS `SPD_ReturnCommoditySheetCommodity_CheckReturnCommoditySheetID`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_ReturnCommoditySheetCommodity_CheckReturnCommoditySheetID`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	-- ...临时模板，下面的值需要根据需求变化
	DECLARE iReturnCommoditySheetCommodityID INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- 临时模板，下面list需要根据需求变化
	DECLARE list CURSOR FOR (SELECT F_ID AS iReturnCommoditySheetCommodityID FROM t_returncommoditysheetcommodity);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iReturnCommoditySheetCommodityID;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- 检查所有退货单商品表的退货单ID是否为退货单表中的退货单
			IF EXISTS (SELECT 1 FROM t_returncommoditysheet WHERE F_ID IN 
			(SELECT F_ReturnCommoditySheetID FROM t_returncommoditysheetcommodity WHERE F_ID = iReturnCommoditySheetCommodityID)) THEN 
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			-- 如果不是，那么则认为数据不健康
			ELSE 
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := concat('退货商品', iReturnCommoditySheetCommodityID, '没有对应的退货单');
			END IF;
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;