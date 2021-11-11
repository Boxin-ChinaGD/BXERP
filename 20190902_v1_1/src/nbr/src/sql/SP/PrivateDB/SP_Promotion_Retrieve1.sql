DROP PROCEDURE IF EXISTS `SP_Promotion_Retrieve1`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Promotion_Retrieve1`(
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
	
		SELECT 
			F_ID, 
			F_SN,
			F_Name, 
			F_Status, 
			F_Type, 
			F_DatetimeStart, 
			F_DatetimeEnd, 
			F_ExcecutionThreshold, 
			F_ExcecutionAmount, 
			F_ExcecutionDiscount, 
			F_Scope, 
			F_ShopScope, 
			F_Staff, 
			F_CreateDatetime, 
			F_UpdateDatetime
	     FROM t_promotion
	     WHERE F_ID = iID;
	     
	     SET iErrorCode := 0;
	     SET sErrorMsg := '';
	
	COMMIT;
END;