DROP PROCEDURE IF EXISTS `SP_Coupon_Retrieve1`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Coupon_Retrieve1`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT
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
   		FROM t_coupon WHERE F_ID = iID;

	COMMIT;
END;
	