DROP PROCEDURE IF EXISTS `SPD_ReturnCommoditySheet_CheckProviderID`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_ReturnCommoditySheet_CheckProviderID`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	-- ...临时模板，下面的值需要根据需求变化
	DECLARE iReturnCommoditySheetID INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- 临时模板，下面list需要根据需求变化
	DECLARE list CURSOR FOR (SELECT F_ID AS iReturnCommoditySheetID FROM t_returncommoditysheet);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iReturnCommoditySheetID;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- 检查所有退货单是否有对应的providerID
			IF EXISTS (SELECT 1 FROM t_provider WHERE F_ID IN (SELECT F_ProviderID FROM t_returncommoditysheet WHERE F_ID = iReturnCommoditySheetID)) THEN 
				SET iErrorCode = 0;
				SET sErrorMsg = '';
			-- 如果没有，那么则认为数据不健康
			ELSE
				SET done = TRUE;
				SET iErrorCode = 7;
				SET sErrorMsg = concat('退货单', iReturnCommoditySheetID, '没有对应的供应商ID');
			END IF;
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;