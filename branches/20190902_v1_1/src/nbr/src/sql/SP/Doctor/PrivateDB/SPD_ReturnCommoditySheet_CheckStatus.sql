DROP PROCEDURE IF EXISTS `SPD_ReturnCommoditySheet_CheckStatus`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_ReturnCommoditySheet_CheckStatus`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	-- ...临时模板，下面的值需要根据需求变化
	DECLARE iReturnCommoditySheetID INT;
	DECLARE iStatus INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- 临时模板，下面list需要根据需求变化
	DECLARE list CURSOR FOR (SELECT F_ID AS iReturnCommoditySheetID, F_Status AS iStatus FROM t_returncommoditysheet);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iReturnCommoditySheetID, iStatus;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- 检查所有的退货单的状态只能为0，1
			IF iStatus IN (0, 1) THEN 
				SET iErrorCode = 0;
				SET sErrorMsg = '';
			-- 如果不是0，1之间，那么则认为数据不健康
			ELSE
				SET iErrorCode = 7;
				SET sErrorMsg = concat('退货单', iReturnCommoditySheetID, '状态异常');
				SET done = TRUE;
			END IF;
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;