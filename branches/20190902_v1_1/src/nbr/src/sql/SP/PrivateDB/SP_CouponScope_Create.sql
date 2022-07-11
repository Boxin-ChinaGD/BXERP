DROP PROCEDURE IF EXISTS `SP_CouponScope_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_CouponScope_Create`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iCouponID INT,
	IN iCommodityID INT
)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		IF NOT EXISTS(SELECT 1 FROM t_coupon WHERE F_ID = iCouponID) THEN
			SET iErrorCode := 2;
			SET sErrorMsg := '该优惠券不存在';
		ELSEIF NOT EXISTS(SELECT 1 FROM t_commodity WHERE F_ID = iCommodityID) THEN
	  		SET iErrorCode := 2;
			SET sErrorMsg := '该商品不存在';
		ELSE
		    INSERT INTO t_couponscope (
			    F_CouponID, 
			    F_CommodityID, 
			    F_CommodityName
		    )VALUES (
			    iCouponID, 
			    iCommodityID, 
			    (SELECT F_Name FROM t_commodity WHERE F_ID = iCommodityID)
		    );
			
			SELECT F_ID, F_CouponID, F_CommodityID, F_CommodityName
			FROM t_couponscope WHERE F_ID = LAST_INSERT_ID();
		END IF;

	COMMIT;
END;