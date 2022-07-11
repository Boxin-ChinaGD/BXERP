DROP PROCEDURE IF EXISTS `SP_Coupon_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Coupon_Create`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iStatus INT,
	IN iType INT,
	IN iBonus INT,
	IN dLeastAmount Decimal(20, 6),
	IN dReduceAmount Decimal(20, 6),
	IN dDiscount Decimal(20, 6),
	IN sTitle VARCHAR(9),
	IN sColor VARCHAR(16),
	IN sDescription VARCHAR(1024),
	IN iPersonalLimit INT,
	IN iWeekDayAvailable INT,
	IN sBeginTime VARCHAR(8),
	IN sEndTime VARCHAR(8),
	IN dtBeginDateTime DATETIME,
	IN dtEndDateTime DATETIME,
	IN iQuantity INT,
	IN iRemainingQuantity INT,
    IN iScope INT
)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := 'Êý¾Ý¿â´íÎó';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
	    INSERT INTO t_coupon (
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
	    )VALUES (
		    iStatus, 
		    iType, 
		    iBonus, 
		    dLeastAmount, 
		    dReduceAmount, 
		    dDiscount, 
		    sTitle, 
		    sColor, 
		    sDescription, 
		    iPersonalLimit, 
		    iWeekDayAvailable, 
		    sBeginTime, 
		    sEndTime, 
		    dtBeginDateTime, 
		    dtEndDateTime, 
		    iQuantity, 
		    iRemainingQuantity, 
		    iScope
		);
		
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
   		FROM t_coupon WHERE F_ID = last_insert_id();
		
	COMMIT;
END;