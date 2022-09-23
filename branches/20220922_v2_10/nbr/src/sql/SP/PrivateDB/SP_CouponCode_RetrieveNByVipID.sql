DROP PROCEDURE IF EXISTS `SP_CouponCode_RetrieveNByVipID`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_CouponCode_RetrieveNByVipID`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iVipID INT,
	IN iSubStatus INT,
	IN iPageIndex INT,
	IN iPageSize INT,
	OUT iTotalRecord INT
	)
BEGIN
	DECLARE recordIndex INT;
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
			SET iPageIndex = iPageIndex - 1;
			SET recordIndex = iPageIndex * iPageSize;
			
			SELECT cc.F_ID, cc.F_VipID, cc.F_CouponID, cc.F_Status, cc.F_SN, cc.F_CreateDatetime, cc.F_UsedDatetime 
			FROM t_couponcode cc
			WHERE cc.F_VipID = iVipID
			AND (CASE iSubStatus WHEN 0 THEN 1 = 1
								 WHEN 1 THEN cc.F_Status = 0
								 WHEN 2 THEN cc.F_Status = 1
								 WHEN 3 THEN (SELECT c.F_EndDateTime FROM t_coupon c WHERE c.F_ID = cc.F_CouponID) <= now() END)
			ORDER BY F_ID DESC 
			LIMIT recordIndex, iPageSize;					 
			
			SELECT count(1) into iTotalRecord 
			FROM t_couponcode cc
			WHERE cc.F_VipID = iVipID
			AND (CASE iSubStatus WHEN 0 THEN 1 = 1
								 WHEN 1 THEN cc.F_Status = 0
								 WHEN 2 THEN cc.F_Status = 1
								 WHEN 3 THEN (SELECT c.F_EndDateTime FROM t_coupon c WHERE c.F_ID = cc.F_CouponID) <= now() END);
		END IF;	
	COMMIT;

END;