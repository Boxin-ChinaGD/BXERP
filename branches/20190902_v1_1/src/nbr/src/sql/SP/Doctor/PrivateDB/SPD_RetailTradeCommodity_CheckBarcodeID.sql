DROP PROCEDURE IF EXISTS `SPD_RetailTradeCommodity_CheckBarcodeID`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_RetailTradeCommodity_CheckBarcodeID`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	-- ...临时模板，下面的值需要根据需求变化
	DECLARE iID INT;
	DECLARE iBarcodeID INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- 临时模板，下面list需要根据需求变化
	DECLARE list CURSOR FOR (SELECT F_ID AS iID, F_BarcodeID AS iBarcodeID FROM t_retailtradecommodity);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iID, iBarcodeID;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- 检查所有的零售单商品表的barcodesID,必须存在barcodes表中
			-- 如果不存在，则认为数据不健康
			IF NOT EXISTS(SELECT 1 FROM t_barcodes WHERE F_ID = iBarcodeID) THEN 
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('ID为', iID, '的零售单商品的BarcodeID没有对应的Barcode');
			ELSE
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			END IF;
			
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;