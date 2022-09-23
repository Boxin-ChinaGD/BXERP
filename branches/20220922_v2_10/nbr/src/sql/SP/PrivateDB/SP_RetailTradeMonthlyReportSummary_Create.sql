DROP PROCEDURE IF EXISTS `SP_RetailTradeMonthlyReportSummary_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_RetailTradeMonthlyReportSummary_Create`(
   	OUT iErrorCode INT,   
   	OUT sErrorMsg VARCHAR(64), 
   	IN iShopID INT,		
   	IN dtEnd DATE,
   	IN iDeleteOldData INT
)
BEGIN

	DECLARE totalAmount DECIMAL(20, 6) DEFAULT 0.000000; 		 	-- 销售额
	DECLARE totalGross DECIMAL(20, 6) DEFAULT 0.000000; 			 	-- 销售毛利
	DECLARE dtStart DATE;						-- 结束时间日期的第一天，只有日期部分					
	DECLARE datetimeStart DATETIME;-- 结束时间日期的第一天。插入Date类型的数据到Datetime中，在本地或SP TEST中不会发生错误，但是在Tomcat中会发生！
	-- 
	DECLARE datetime1970 DATETIME DEFAULT '1970-01-01 00:00:00';	
	DECLARE dt1970 DATE;
	DECLARE gapForDay INT; -- 1970年到某个时间的差距天数
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '数据库错误';	
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SET iErrorCode := 0; 
		SET sErrorMsg := '';

		-- 获取结束时间第一天的时间，限年月有效
		SELECT DATE_FORMAT(DATE_ADD(dtEnd, interval - day(dtEnd) + 1 day), '%Y-%m-%d') INTO dtStart;
		
--		SELECT date(concat(DATE_FORMAT(dtStart, '%Y-%m-%d'), ' 00:00:00')) INTO datetimeStart; -- shit， 失败。下面3行代码迂回曲折地得到datetimeStart
		SELECT DATE_FORMAT(datetime1970, '%Y-%m-%d') INTO dt1970;
		SELECT datediff(dtStart, dt1970) INTO gapForDay;
		SELECT date_add(datetime1970, INTERVAL gapForDay day) INTO datetimeStart;
		
		-- 由于月报表会根据这个月的进度而去更新，所以这里每创建一次，都将原先的数据删除
		-- IF iDeleteOldData = 1 THEN 
			DELETE FROM t_retailtrademonthlyreportsummary WHERE F_Datetime = datetimeStart AND F_ShopID = iShopID;
		-- END IF;
		
	    -- 计算出销售额和销售毛利
		SELECT ifnull(sum(F_TotalAmount), 0.000000), ifnull(sum(F_TotalGross), 0.000000) INTO totalAmount, totalGross 
		FROM t_retailtradedailyreportsummary  
	   	WHERE (F_Datetime BETWEEN dtStart AND dtEnd) AND F_ShopID = iShopID;
		
		-- 判断时间是否合法。注释掉是因为需要在测试代码中生成未来时间的报表
--		IF currentTime < dtStart THEN 
--			SET iErrorCode := 7; 
--			SET sErrorMsg := '不能传入非法时间';
--	    -- 如果当前时间是当月第一天，然后数据库不存在时间
--		ELSE
			INSERT INTO t_retailtrademonthlyreportsummary(F_Datetime, F_ShopID, F_TotalAmount, F_TotalGross, F_CreateDatetime, F_UpdateDatetime) 
			VALUES (datetimeStart,  -- 这里必须是有时、分、秒的，否则在Tomcat环境中会报错（在本地运行SP TEST不会）！用concat(DATE类型, ' 00:00:00')不行！时分秒全部设为0，因为测试代码要使用到；同时为0也是合理的
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