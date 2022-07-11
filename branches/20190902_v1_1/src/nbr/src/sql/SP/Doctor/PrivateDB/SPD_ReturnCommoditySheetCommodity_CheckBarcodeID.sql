DROP PROCEDURE IF EXISTS `SPD_ReturnCommoditySheetCommodity_CheckBarcodeID`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_ReturnCommoditySheetCommodity_CheckBarcodeID`(
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
			
			-- 检查所有的退货单商品表的barcodesID,必须存在barcodes表中
			IF EXISTS (SELECT 1 FROM t_barcodes WHERE F_ID IN (SELECT F_BarcodeID FROM t_returncommoditysheetcommodity WHERE F_ID = iReturnCommoditySheetCommodityID)) THEN 
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			-- 如果不存在，则认为数据不健康
			ELSE 
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := concat('退货单商品', iReturnCommoditySheetCommodityID, '的条形码ID不存在');
			END IF;
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;