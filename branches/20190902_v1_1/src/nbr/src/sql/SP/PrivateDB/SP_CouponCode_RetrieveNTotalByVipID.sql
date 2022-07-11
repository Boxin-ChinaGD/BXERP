DROP PROCEDURE IF EXISTS `SP_CouponCode_RetrieveNTotalByVipID`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_CouponCode_RetrieveNTotalByVipID`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iVipID INT,
	OUT iTotalRecord INT
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
		
		IF NOT EXISTS (SELECT 1 FROM t_vip WHERE F_ID = iVipID) THEN
			SET iErrorCode := 2;
			SET sErrorMsg := '会员不存在';
		ELSE
			SELECT count(1) into iTotalRecord 
			FROM t_couponcode cc
			WHERE cc.F_VipID = iVipID 
				AND cc.F_Status = 0 
				AND (SELECT c.F_EndDateTime FROM t_coupon c WHERE c.F_ID = cc.F_CouponID) > now();
		END IF;
	COMMIT;
END;