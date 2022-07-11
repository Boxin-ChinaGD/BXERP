
DROP PROCEDURE IF EXISTS `SPD_Promotion_CheckScope`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_Promotion_CheckScope`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iID INT;
	DECLARE sScope INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- 临时模板，下面list需要根据需求变化
	DECLARE list CURSOR FOR (SELECT F_ID AS iID, F_Scope AS sScope FROM t_promotion);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SET iErrorCode := 0; 
		SET sErrorMsg := '';
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iID,sScope;
			IF done THEN
				LEAVE read_loop;
			END IF;
		   
			-- 促销范围只能是全场商品0,或指定商品1
			IF sScope NOT IN (0,1) THEN 
				SET done := TRUE;
			    SET iErrorCode := 7; 
			    SET sErrorMsg := CONCAT('促销范围', iID, '只能是全场商品0,或指定商品1');
			END IF;
						   	
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;