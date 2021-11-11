
DROP PROCEDURE IF EXISTS `SPD_Promotion_CheckType`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_Promotion_CheckType`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iID INT;
	DECLARE type INT;
	DECLARE excecutionThreshold DECIMAL(20,6); -- 满减阀值
	DECLARE excecutionDiscount DECIMAL(20,6); -- 满减折扣
	DECLARE excecutionAmount DECIMAL(20,6); -- 满减金额
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- 临时模板，下面list需要根据需求变化
	DECLARE list CURSOR FOR (SELECT F_ID AS iID,F_Type AS type,F_ExcecutionThreshold AS excecutionThreshold,F_ExcecutionDiscount AS excecutionDiscount,F_ExcecutionAmount AS excecutionAmount FROM t_promotion);
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
			FETCH list INTO iID,type,excecutionThreshold,excecutionDiscount,excecutionAmount;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			IF type = 1 THEN
			    -- 满折时，满减阀值小于0
			    IF excecutionThreshold < 0 THEN 
		          SET done := TRUE;
		          SET iErrorCode := 7; 
		          SET sErrorMsg := CONCAT('促销', iID, '满折时，满减阀值小于0');
		          
		        -- 满折时，满减阀值等于0  
		        ElSEIF excecutionThreshold = 0 THEN 
			      SET done := TRUE;
			      SET iErrorCode := 7; 
			      SET sErrorMsg := CONCAT('促销', iID, '满折时，满减阀值等于0');
			    -- 满折时，满减阀值大于10000
			    ELSEIF excecutionThreshold > 10000 THEN 
			      SET done := TRUE;
			      SET iErrorCode := 7; 
			      SET sErrorMsg := CONCAT('促销', iID, '满折时，满减阀值大于10000'); 
			     -- 满折时，满减折扣小于0   
			    ELSEIF excecutionDiscount < 0 THEN 
			      SET done := TRUE;
			      SET iErrorCode := 7; 
			      SET sErrorMsg := CONCAT('促销', iID, '满折时，满减折扣小于0');  
			    -- 满折时，满减折扣等于0 
		    	ELSEIF excecutionDiscount = 0 THEN 
			      SET done := TRUE;
			      SET iErrorCode := 7; 
			      SET sErrorMsg := CONCAT('促销', iID, '满折时，满减折扣等于0'); 
			    -- 满折时，满减折扣大于1
		    	ELSEIF excecutionDiscount > 1 THEN 
			      SET done := TRUE;
			      SET iErrorCode := 7; 
			      SET sErrorMsg := CONCAT('促销', iID, '满折时，满减折扣大于1'); 
			    END IF;
			ELSEIF type = 0 THEN 
			   -- 满减时，满减阀值小于0
			   IF excecutionThreshold < 0 THEN 
			     SET done := TRUE;
			     SET iErrorCode := 7; 
			     SET sErrorMsg := CONCAT('促销', iID, '满减时，满减阀值小于0'); 
			   -- 满减时，满减阀值等于0
			   ELSEIF excecutionThreshold = 0 THEN 
			     SET done := TRUE;
			     SET iErrorCode := 7; 
			     SET sErrorMsg := CONCAT('促销', iID, '满减时，满减阀值等于0');
			   -- 满减时，满减阀值大于10000
			   ELSEIF excecutionThreshold > 10000 THEN 
			     SET done := TRUE;
			     SET iErrorCode := 7; 
		         SET sErrorMsg := CONCAT('促销', iID, '满减时，满减阀值大于10000');
		       -- 满减时，满减阀值小于满减金额
		       ELSEIF excecutionThreshold < excecutionAmount THEN 
			     SET done := TRUE;
			     SET iErrorCode := 7; 
			     SET sErrorMsg := CONCAT('促销', iID, '满减时，满减阀值小于满减金额');
			   -- 满减时，满减金额小于0 
		       ELSEIF excecutionAmount < 0 THEN 
			     SET done := TRUE;
			     SET iErrorCode := 7; 
			     SET sErrorMsg := CONCAT('促销', iID, '满减时，满减金额小于0');  
			    -- 满减时，满减金额等于0 
		       ELSEIF excecutionAmount = 0 THEN 
			     SET done := TRUE;
			     SET iErrorCode := 7; 
			     SET sErrorMsg := CONCAT('促销', iID, '满减时，满减金额等于0');  
	           END IF;   
			ELSE 
			    SET done := TRUE;
			    SET iErrorCode := 7; 
			    SET sErrorMsg := CONCAT('促销', iID, '促销类型不是满折类型跟满减类型');
			END IF;
		 					   	
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;