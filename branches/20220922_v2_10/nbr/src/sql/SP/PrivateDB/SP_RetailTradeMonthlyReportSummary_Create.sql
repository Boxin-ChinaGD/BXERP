DROP PROCEDURE IF EXISTS `SP_RetailTradeMonthlyReportSummary_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_RetailTradeMonthlyReportSummary_Create`(
   	OUT iErrorCode INT,   
   	OUT sErrorMsg VARCHAR(64), 
   	IN iShopID INT,		
   	IN dtEnd DATE,
   	IN iDeleteOldData INT
)
BEGIN

	DECLARE totalAmount DECIMAL(20, 6) DEFAULT 0.000000; 		 	-- ���۶�
	DECLARE totalGross DECIMAL(20, 6) DEFAULT 0.000000; 			 	-- ����ë��
	DECLARE dtStart DATE;						-- ����ʱ�����ڵĵ�һ�죬ֻ�����ڲ���					
	DECLARE datetimeStart DATETIME;-- ����ʱ�����ڵĵ�һ�졣����Date���͵����ݵ�Datetime�У��ڱ��ػ�SP TEST�в��ᷢ�����󣬵�����Tomcat�лᷢ����
	-- 
	DECLARE datetime1970 DATETIME DEFAULT '1970-01-01 00:00:00';	
	DECLARE dt1970 DATE;
	DECLARE gapForDay INT; -- 1970�굽ĳ��ʱ��Ĳ������
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';	
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SET iErrorCode := 0; 
		SET sErrorMsg := '';

		-- ��ȡ����ʱ���һ���ʱ�䣬��������Ч
		SELECT DATE_FORMAT(DATE_ADD(dtEnd, interval - day(dtEnd) + 1 day), '%Y-%m-%d') INTO dtStart;
		
--		SELECT date(concat(DATE_FORMAT(dtStart, '%Y-%m-%d'), ' 00:00:00')) INTO datetimeStart; -- shit�� ʧ�ܡ�����3�д����ػ����۵صõ�datetimeStart
		SELECT DATE_FORMAT(datetime1970, '%Y-%m-%d') INTO dt1970;
		SELECT datediff(dtStart, dt1970) INTO gapForDay;
		SELECT date_add(datetime1970, INTERVAL gapForDay day) INTO datetimeStart;
		
		-- �����±�����������µĽ��ȶ�ȥ���£���������ÿ����һ�Σ�����ԭ�ȵ�����ɾ��
		-- IF iDeleteOldData = 1 THEN 
			DELETE FROM t_retailtrademonthlyreportsummary WHERE F_Datetime = datetimeStart AND F_ShopID = iShopID;
		-- END IF;
		
	    -- ��������۶������ë��
		SELECT ifnull(sum(F_TotalAmount), 0.000000), ifnull(sum(F_TotalGross), 0.000000) INTO totalAmount, totalGross 
		FROM t_retailtradedailyreportsummary  
	   	WHERE (F_Datetime BETWEEN dtStart AND dtEnd) AND F_ShopID = iShopID;
		
		-- �ж�ʱ���Ƿ�Ϸ���ע�͵�����Ϊ��Ҫ�ڲ��Դ���������δ��ʱ��ı���
--		IF currentTime < dtStart THEN 
--			SET iErrorCode := 7; 
--			SET sErrorMsg := '���ܴ���Ƿ�ʱ��';
--	    -- �����ǰʱ���ǵ��µ�һ�죬Ȼ�����ݿⲻ����ʱ��
--		ELSE
			INSERT INTO t_retailtrademonthlyreportsummary(F_Datetime, F_ShopID, F_TotalAmount, F_TotalGross, F_CreateDatetime, F_UpdateDatetime) 
			VALUES (datetimeStart,  -- �����������ʱ���֡���ģ�������Tomcat�����лᱨ���ڱ�������SP TEST���ᣩ����concat(DATE����, ' 00:00:00')���У�ʱ����ȫ����Ϊ0����Ϊ���Դ���Ҫʹ�õ���ͬʱΪ0Ҳ�Ǻ����
				iShopID, totalAmount, totalGross, now(), now());
			
			SELECT 
				F_ID, 
				F_ShopID,
				F_Datetime, 
				F_TotalAmount, 
				F_TotalGross,
				F_CreateDatetime, 
				F_UpdateDatetime
			FROM t_retailtrademonthlyreportsummary
			WHERE F_ID = last_insert_id() AND F_ShopID = iShopID; 
--		END IF; 
		
	COMMIT;
END; 