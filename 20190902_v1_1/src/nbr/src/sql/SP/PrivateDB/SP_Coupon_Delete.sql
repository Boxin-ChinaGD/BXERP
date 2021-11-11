DROP PROCEDURE IF EXISTS `SP_Coupon_Delete`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Coupon_Delete`(
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
		
		UPDATE t_coupon SET F_Status = 1 WHERE F_ID = iID;
	
	COMMIT;
END;