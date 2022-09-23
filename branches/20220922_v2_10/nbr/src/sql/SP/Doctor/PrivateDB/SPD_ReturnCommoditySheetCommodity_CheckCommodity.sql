DROP PROCEDURE IF EXISTS `SPD_ReturnCommoditySheetCommodity_CheckCommodity`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_ReturnCommoditySheetCommodity_CheckCommodity`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	-- ...临时模板，下面的值需要根据需求变化
	DECLARE iReturnCommoditySheetCommodityID INT;
	DECLARE iNO INT;
	DECLARE iCommodityID INT;
	DECLARE iType INT;
	DECLARE iStatus INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- 临时模板，下面list需要根据需求变化
	DECLARE list CURSOR FOR (SELECT F_ID AS iReturnCommoditySheetCommodityID, F_NO AS iNO FROM t_returncommoditysheetcommodity );
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iReturnCommoditySheetCommodityID, iNO;
			IF done THEN
				LEAVE read_loop;
			END IF;
			-- 
			SELECT F_CommodityID INTO iCommodityID FROM t_returncommoditysheetcommodity WHERE F_ID = iReturnCommoditySheetCommodityID;
			-- 检查所有退货单商品中的商品存在于商品表中
			-- 如果不存在，则认为数据不健康
			IF EXISTS (SELECT 1 FROM t_commodity WHERE F_ID = iCommodityID) THEN
				-- 检查所有退货单商品中的商品不能为多包装商品，组合商品，已删除商品和服务商品
				-- 如果为已删除的商品，那么则认为数据不健康
				SELECT F_Type, F_Status INTO iType, iStatus FROM t_commodity WHERE F_ID = iCommodityID;
				IF (iType = 0 AND iStatus IN (0, 1)) THEN
					-- 检查所有退货单商品表中的商品数量，必须全部大于0
					-- 如果不大于0，则认为数据不健康
					IF iNO > 0 THEN 
						SET iErrorCode := 0;
						SET sErrorMsg := '';
					ELSE
						SET done := TRUE;
						SET iErrorCode := 7;
						SET sErrorMsg := concat('退货单商品', iReturnCommoditySheetCommodityID, '的商品数量必须大于0');
					END IF;
				ELSE
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := concat('退货单商品', iReturnCommoditySheetCommodityID, '只能是未删除的普通商品');
				END IF;
			ELSE
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := concat('退货单商品', iReturnCommoditySheetCommodityID, '不存在在商品表中');
			END IF;
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;