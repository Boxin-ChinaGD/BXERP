
DROP PROCEDURE IF EXISTS `SPD_Promotion_CheckDatetime`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_Promotion_CheckDatetime`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iID INT;
	DECLARE datetimeStart DATETIME;
	DECLARE datetimeEnd DATETIME;
	DECLARE createDatetime DATETIME;
	DECLARE iRetailTradeID INT;
	DECLARE saleDatetime DATETIME;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- 临时模板，下面list需要根据需求变化
	DECLARE list CURSOR FOR (
	   SELECT F_ID AS iID, F_DatetimeStart AS datetimeStart,F_DatetimeEnd AS datetimeEnd, F_CreateDatetime AS createDatetime FROM t_promotion);
   
	-- 用于测试对应的零售单的售出时间，必须在开始时间和结束时间之间
	DECLARE list2 CURSOR FOR (
		SELECT F_ID AS iRetailTradeID, F_SaleDatetime AS saleDatetime FROM t_retailtrade WHERE F_ID IN (
			SELECT F_TradeID FROM t_retailtradepromoting WHERE F_ID IN (
				SELECT F_RetailTradePromotingID FROM t_retailtradepromotingflow WHERE F_PromotionID IN (iID))));
           
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
			FETCH list INTO iID,datetimeStart,datetimeEnd,createDatetime;
			IF done THEN
				LEAVE read_loop;
			END IF;
	    	
		    -- 开始时间等于创建时间;
			IF TIMESTAMPDIFF(SECOND,datetimeStart,createDatetime) = 0 THEN 
				SET done := TRUE;
			    SET iErrorCode := 7; 
			    SET sErrorMsg := CONCAT('促销', iID, '开始时间等于创建时间');	
			-- 开始时间>创建时间 && 开始时间<创建时间+24H
			ELSEIF unix_timestamp(datetimeStart) > unix_timestamp(createDatetime)
			   AND unix_timestamp(datetimeStart) < unix_timestamp(DATE_ADD(createDatetime,INTERVAL 1 DAY)) THEN
			   	SET done := TRUE;
			    SET iErrorCode := 7; 
			    SET sErrorMsg := CONCAT('促销', iID, '开始时间大于(0h~24h)创建时间');
			-- 结束时间小于开始时间;
			ELSEIF unix_timestamp(datetimeStart) > unix_timestamp(datetimeEnd) THEN
			   	SET done := TRUE;
			    SET iErrorCode := 7; 
			    SET sErrorMsg := CONCAT('促销', iID, '结束时间小于开始时间'); 
			-- 结束时间等于开始时间
			ELSEIF TIMESTAMPDIFF(SECOND,datetimeStart,datetimeEnd) = 0 THEN
			   	SET done := TRUE;
			    SET iErrorCode := 7; 
			    SET sErrorMsg := CONCAT('促销', iID, '结束时间等于开始时间'); 
		    -- 结束时间小于创建时间
			ELSEIF unix_timestamp(datetimeEnd) < unix_timestamp(createDatetime) THEN
			   	SET done := TRUE;
			    SET iErrorCode := 7; 
			    SET sErrorMsg := CONCAT('促销', iID, '结束时间小于创建时间'); 
		    -- 结束时间等于创建时间
			ELSEIF TIMESTAMPDIFF(SECOND,datetimeEnd,createDatetime) = 0 THEN
			   	SET done := TRUE;
			    SET iErrorCode := 7; 
			    SET sErrorMsg := CONCAT('促销', iID, '结束时间等于创建时间'); 
			-- 开始时间小于创建时间
			ELSEIF unix_timestamp(datetimeStart) < unix_timestamp(createDatetime) THEN
			   	SET done := TRUE;
			    SET iErrorCode := 7; 
			    SET sErrorMsg := CONCAT('促销', iID, '开始时间小于创建时间'); 	        		  	           
			END IF;
		
			OPEN list2; -- 
				read_loop2: LOOP
			FETCH list2 INTO iRetailTradeID,saleDatetime;
			IF done THEN
		   		LEAVE read_loop2;
	   		END IF;
			   	-- 对应的零售单的售出时间，必须在开始时间和结束时间之间  
				IF unix_timestamp(datetimeStart) >= unix_timestamp(saleDatetime) 
				     || unix_timestamp(datetimeEnd) <= unix_timestamp(saleDatetime) THEN  		        	    			
				    SET done := TRUE;
				    SET iErrorCode := 7; 
				    SET sErrorMsg := CONCAT('ID为',iRetailTradeID, '的零售单的售出时间,必须是ID为',iID,'的促销的开始时间和结束时间之间');    		  	           
				END IF;
		
			END LOOP read_loop2;
			CLOSE list2;
		   	SET done := false;
						   	
		END LOOP read_loop;
		CLOSE list;
	COMMIT;
END;