DROP PROCEDURE IF EXISTS `SP_CouponCode_Consume`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_CouponCode_Consume`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
    IN iID INT
)
BEGIN
	DECLARE sSN VARCHAR(15);
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	
	START TRANSACTION;
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
        
        IF NOT EXISTS(SELECT 1 FROM t_couponcode WHERE F_ID = iID) THEN
        	SET iErrorCode := 2;
			SET sErrorMsg := '优惠券不存在';
		ELSE
			UPDATE t_couponcode SET F_Status = 1 WHERE F_ID = iID;

			SELECT F_ID, F_VipID, F_CouponID, F_Status, F_SN, F_CreateDatetime, F_UsedDatetime
			FROM t_couponcode WHERE F_ID = iID;
		END IF;

	COMMIT;
END;