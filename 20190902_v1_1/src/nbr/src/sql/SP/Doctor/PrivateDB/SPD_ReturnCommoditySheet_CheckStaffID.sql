DROP PROCEDURE IF EXISTS `SPD_ReturnCommoditySheet_CheckStaffID`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_ReturnCommoditySheet_CheckStaffID`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	-- ...临时模板，下面的值需要根据需求变化
	DECLARE iReturnCommoditySheetID INT;
	DECLARE iStaffID INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- 临时模板，下面list需要根据需求变化
	DECLARE list CURSOR FOR (SELECT F_ID AS iReturnCommoditySheetID, F_StaffID AS iStaffID FROM t_returncommoditysheet);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iReturnCommoditySheetID, iStaffID;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- 检查所有的退货单的staffID不能为空，检查退货单的staffID是否存在Staff表中
			IF iStaffID IS NOT NULL THEN 
				IF EXISTS (SELECT 1 FROM t_staff WHERE F_ID = iStaffID) THEN 
					SET iErrorCode = 0;
					SET sErrorMsg = '';
				ELSE
					SET done = TRUE;
					SET iErrorCode = 7;
					SET sErrorMsg = concat('退货单', iReturnCommoditySheetID, '没有对应的操作人ID');
				END IF;
			ELSE
				SET done = TRUE;
				SET iErrorCode = 7;
				SET sErrorMsg = concat('退货单', iReturnCommoditySheetID, '操作人ID为空');
			END IF;

		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;