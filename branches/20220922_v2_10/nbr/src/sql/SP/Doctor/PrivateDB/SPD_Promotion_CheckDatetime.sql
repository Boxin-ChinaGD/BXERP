
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
	-- ��ʱģ�壬����list��Ҫ��������仯
	DECLARE list CURSOR FOR (
	   SELECT F_ID AS iID, F_DatetimeStart AS datetimeStart,F_DatetimeEnd AS datetimeEnd, F_CreateDatetime AS createDatetime FROM t_promotion);
   
	-- ���ڲ��Զ�Ӧ�����۵����۳�ʱ�䣬�����ڿ�ʼʱ��ͽ���ʱ��֮��
	DECLARE list2 CURSOR FOR (
		SELECT F_ID AS iRetailTradeID, F_SaleDatetime AS saleDatetime FROM t_retailtrade WHERE F_ID IN (
			SELECT F_TradeID FROM t_retailtradepromoting WHERE F_ID IN (
				SELECT F_RetailTradePromotingID FROM t_retailtradepromotingflow WHERE F_PromotionID IN (iID))));
           
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
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
	    	
		    -- ��ʼʱ����ڴ���ʱ��;
			IF TIMESTAMPDIFF(SECOND,datetimeStart,createDatetime) = 0 THEN 
				SET done := TRUE;
			    SET iErrorCode := 7; 
			    SET sErrorMsg := CONCAT('����', iID, '��ʼʱ����ڴ���ʱ��');	
			-- ��ʼʱ��>����ʱ�� && ��ʼʱ��<����ʱ��+24H
			ELSEIF unix_timestamp(datetimeStart) > unix_timestamp(createDatetime)
			   AND unix_timestamp(datetimeStart) < unix_timestamp(DATE_ADD(createDatetime,INTERVAL 1 DAY)) THEN
			   	SET done := TRUE;
			    SET iErrorCode := 7; 
			    SET sErrorMsg := CONCAT('����', iID, '��ʼʱ�����(0h~24h)����ʱ��');
			-- ����ʱ��С�ڿ�ʼʱ��;
			ELSEIF unix_timestamp(datetimeStart) > unix_timestamp(datetimeEnd) THEN
			   	SET done := TRUE;
			    SET iErrorCode := 7; 
			    SET sErrorMsg := CONCAT('����', iID, '����ʱ��С�ڿ�ʼʱ��'); 
			-- ����ʱ����ڿ�ʼʱ��
			ELSEIF TIMESTAMPDIFF(SECOND,datetimeStart,datetimeEnd) = 0 THEN
			   	SET done := TRUE;
			    SET iErrorCode := 7; 
			    SET sErrorMsg := CONCAT('����', iID, '����ʱ����ڿ�ʼʱ��'); 
		    -- ����ʱ��С�ڴ���ʱ��
			ELSEIF unix_timestamp(datetimeEnd) < unix_timestamp(createDatetime) THEN
			   	SET done := TRUE;
			    SET iErrorCode := 7; 
			    SET sErrorMsg := CONCAT('����', iID, '����ʱ��С�ڴ���ʱ��'); 
		    -- ����ʱ����ڴ���ʱ��
			ELSEIF TIMESTAMPDIFF(SECOND,datetimeEnd,createDatetime) = 0 THEN
			   	SET done := TRUE;
			    SET iErrorCode := 7; 
			    SET sErrorMsg := CONCAT('����', iID, '����ʱ����ڴ���ʱ��'); 
			-- ��ʼʱ��С�ڴ���ʱ��
			ELSEIF unix_timestamp(datetimeStart) < unix_timestamp(createDatetime) THEN
			   	SET done := TRUE;
			    SET iErrorCode := 7; 
			    SET sErrorMsg := CONCAT('����', iID, '��ʼʱ��С�ڴ���ʱ��'); 	        		  	           
			END IF;
		
			OPEN list2; -- 
				read_loop2: LOOP
			FETCH list2 INTO iRetailTradeID,saleDatetime;
			IF done THEN
		   		LEAVE read_loop2;
	   		END IF;
			   	-- ��Ӧ�����۵����۳�ʱ�䣬�����ڿ�ʼʱ��ͽ���ʱ��֮��  
				IF unix_timestamp(datetimeStart) >= unix_timestamp(saleDatetime) 
				     || unix_timestamp(datetimeEnd) <= unix_timestamp(saleDatetime) THEN  		        	    			
				    SET done := TRUE;
				    SET iErrorCode := 7; 
				    SET sErrorMsg := CONCAT('IDΪ',iRetailTradeID, '�����۵����۳�ʱ��,������IDΪ',iID,'�Ĵ����Ŀ�ʼʱ��ͽ���ʱ��֮��');    		  	           
				END IF;
		
			END LOOP read_loop2;
			CLOSE list2;
		   	SET done := false;
						   	
		END LOOP read_loop;
		CLOSE list;
	COMMIT;
END;