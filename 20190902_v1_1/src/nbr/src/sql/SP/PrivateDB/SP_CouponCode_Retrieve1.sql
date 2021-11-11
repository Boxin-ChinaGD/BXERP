DROP PROCEDURE IF EXISTS `SP_CouponCode_Retrieve1`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_CouponCode_Retrieve1`(
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
	
	   	SELECT F_ID, F_VipID, F_CouponID, F_Status, F_SN, F_CreateDatetime, F_UsedDatetime
		FROM t_couponcode WHERE F_ID = iID;

	COMMIT;
END;
	