DROP PROCEDURE IF EXISTS `SPD_RefCommodityHub_CheckBarcode`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_RefCommodityHub_CheckBarcode`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iID INT;
	DECLARE sBarcode VARCHAR(20);
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- 临时模板，下面list需要根据需求变化
	DECLARE list CURSOR FOR (SELECT F_ID AS iID, F_Barcode AS sBarcode FROM staticdb.t_refcommodityhub);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iID,sBarcode;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- 检查条形码的长度是否满足[7~20]
			IF length(sBarcode) < 7 OR length(sBarcode) > 20 THEN 
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('ID为', iID ,'的参考商品的条形码长度必须是[7~20]');
			ELSE 
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			END IF;
			
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;