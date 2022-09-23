DROP PROCEDURE IF EXISTS `SPD_RefCommodityHub_CheckPriceRetail`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_RefCommodityHub_CheckPriceRetail`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iID INT;
	DECLARE iPriceRetail DECIMAL(20,6);
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- 临时模板，下面list需要根据需求变化
	DECLARE list CURSOR FOR (SELECT F_ID AS iID, F_PriceRetail AS iPriceRetail FROM staticdb.t_refcommodityhub);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iID,iPriceRetail;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- 参考商品的零售价不能为空也不能为0
			IF iPriceRetail IS NULL OR iPriceRetail = 0 THEN 
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('ID为', iID ,'的参考商品的零售价不能为空也不能为0');
			ELSE 
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			END IF;
			
			
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;