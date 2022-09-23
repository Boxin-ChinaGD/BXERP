DROP PROCEDURE IF EXISTS `SP_Coupon_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Coupon_RetrieveN`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iPosID INT,
	IN iBonus INT,
	IN iType INT,
	IN iPageIndex INT,
	IN iPageSize INT,
 	OUT iTotalRecord INT
)
BEGIN
	DECLARE recordIndex INT;
	DECLARE INVALID_ID INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
		SELECT F_Value INTO INVALID_ID FROM t_nbrconstant WHERE F_Key = 'INVALID_ID';  
		
		IF iPosID = INVALID_ID THEN -- 网页请求
			SET iPageIndex = iPageIndex - 1;
			SET recordIndex = iPageIndex * iPageSize;
	
		    SELECT 
				F_ID, 
				F_Status, 
				F_Type, 
				F_Bonus, 
				F_LeastAmount, 
				F_ReduceAmount, 
				F_Discount, 
				F_Title, 
				F_Color, 
				F_Description, 
				F_PersonalLimit, 
				F_WeekDayAvailable, 
				F_BeginTime, 
				F_EndTime, 
				F_BeginDateTime, 
				F_EndDateTime, 
				F_Quantity, 
				F_RemainingQuantity, 
				F_Scope
	  		FROM t_coupon 
			ORDER BY F_ID DESC
			LIMIT recordIndex, iPageSize;
			
  			SELECT count(1) into iTotalRecord FROM t_coupon;
  			
  		ELSEIF iPosID = '-2' THEN -- 小程序会员在领券中心的RN
  			SET iPageIndex = iPageIndex - 1;
			SET recordIndex = iPageIndex * iPageSize;
	
		    SELECT 
				F_ID, 
				F_Status, 
				F_Type, 
				F_Bonus, 
				F_LeastAmount, 
				F_ReduceAmount, 
				F_Discount, 
				F_Title, 
				F_Color, 
				F_Description, 
				F_PersonalLimit, 
				F_WeekDayAvailable, 
				F_BeginTime, 
				F_EndTime, 
				F_BeginDateTime, 
				F_EndDateTime, 
				F_Quantity, 
				F_RemainingQuantity, 
				F_Scope
	  		FROM t_coupon 
	  		WHERE F_Status = 0 AND now() < F_EndDateTime -- 包括未开始的，不包括过期、和已删除的
	  		AND (CASE iBonus WHEN INVALID_ID THEN 1=1 WHEN 0 THEN F_Bonus = 0 ELSE F_Bonus > 0 END)
	  		AND (CASE iType WHEN INVALID_ID THEN 1=1 ELSE F_Type = iType END)
			ORDER BY F_ID DESC
			LIMIT recordIndex, iPageSize;
			-- 
			SELECT count(1) into iTotalRecord FROM t_coupon 
	  		WHERE F_Status = 0 AND now() < F_EndDateTime -- 包括未开始的，不包括过期、和已删除的
	  		AND (CASE iBonus WHEN INVALID_ID THEN 1=1 WHEN 0 THEN F_Bonus = 0 ELSE F_Bonus > 0 END)
	  		AND (CASE iType WHEN INVALID_ID THEN 1=1 ELSE F_Type = iType END);
		ELSE  
			
			SELECT 
				F_ID, 
				F_Status, 
				F_Type, 
				F_Bonus, 
				F_LeastAmount, 
				F_ReduceAmount, 
				F_Discount, 
				F_Title, 
				F_Color, 
				F_Description, 
				F_PersonalLimit, 
				F_WeekDayAvailable, 
				F_BeginTime, 
				F_EndTime, 
				F_BeginDateTime, 
				F_EndDateTime, 
				F_Quantity, 
				F_RemainingQuantity, 
				F_Scope
	  		FROM t_coupon WHERE F_Status = 0 AND now() < F_EndDateTime OR F_Status = 1 AND now() < F_EndDateTime AND F_ID IN 
	  			(SELECT F_CouponID FROM t_couponcode WHERE F_Status = 0);	
	  		
	  		SELECT count(1) into iTotalRecord FROM t_coupon 
	  		WHERE F_Status = 0 AND now() < F_EndDateTime OR F_Status = 1 AND now() < F_EndDateTime AND F_ID IN 
	  			(SELECT F_CouponID FROM t_couponcode WHERE F_Status = 0);	
		END IF;
		
	COMMIT;
END;