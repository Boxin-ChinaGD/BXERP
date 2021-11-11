DROP PROCEDURE IF EXISTS `SP_CouponCode_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_CouponCode_Create`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
    IN iVipID INT,
	IN iCouponID INT,
	IN iStatus INT -- 实际上这个字段没有用，除非拿来做MapperTest
)
BEGIN
	DECLARE sSN VARCHAR(15);
	DECLARE iVipBonus INT; -- 会员积分
	DECLARE iCouponBonus INT; -- 兑换优惠券需要积分
	DECLARE iRemainingQuantity INT; -- 优惠券库存
	DECLARE iPersonalLimit INT; -- 每人领券上限
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	SELECT F_Bonus INTO iVipBonus FROM t_vip WHERE F_ID = iVipID;
	SELECT F_Bonus, F_RemainingQuantity, F_PersonalLimit INTO iCouponBonus, iRemainingQuantity, iPersonalLimit FROM t_coupon WHERE F_ID = iCouponID;
	
	START TRANSACTION;
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
        
        IF iVipBonus IS NULL THEN
        	SET iErrorCode := 2;
			SET sErrorMsg := '该会员不存在';
		ELSEIF NOT EXISTS(SELECT 1 FROM t_coupon WHERE F_ID = iCouponID AND F_Status = 0) THEN
        	SET iErrorCode := 2;
			SET sErrorMsg := '该优惠券不存在';
		ELSEIF iRemainingQuantity < 1 THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '优惠券库存不足,无法领取';
		ELSEIF (SELECT COUNT(1) FROM t_couponcode WHERE F_VipID = iVipID AND F_CouponID = iCouponID) >= iPersonalLimit THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '会员已超过领取该优惠券的个人数目上限，无法领取';
		ELSEIF iVipBonus < iCouponBonus THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '会员积分不足,无法领取该优惠券';
		ELSE
			read_loop: LOOP -- 循环获取到唯一的SN码为止
		    	SET sSN = Func_GenerateCouponSN(15);
		    	
	            IF NOT EXISTS(SELECT 1 FROM t_couponcode WHERE F_SN = sSN) THEN
	                LEAVE read_loop;
	            end IF;
        	END LOOP read_loop;
        	
			IF iCouponBonus > 0 THEN
				-- 更新会员积分
				UPDATE t_vip SET F_Bonus = F_Bonus - iCouponBonus WHERE F_ID = iVipID;
				-- 插入会员积分历史
				INSERT INTO t_bonusconsumehistory (F_VipID, F_StaffID, F_Bonus, F_AddedBonus, F_Remark, F_CreateDatetime)
				VALUES (iVipID, NULL, (SELECT F_Bonus FROM t_vip WHERE F_ID = iVipID), (iCouponBonus * -1), '兑换', now());
			END IF;
        	
	        INSERT INTO t_couponcode (F_VipID, F_CouponID, F_Status, F_SN, F_CreateDatetime, F_UsedDatetime)
			VALUES (iVipID, iCouponID, iStatus, sSN, now(), null);
			
			SELECT F_ID, F_VipID, F_CouponID, F_Status, F_SN, F_CreateDatetime, F_UsedDatetime
			FROM t_couponcode WHERE F_ID = LAST_INSERT_ID();
			
			-- 更新优惠券的剩余库存量
			UPDATE t_coupon SET F_RemainingQuantity = F_RemainingQuantity - 1 WHERE F_ID = iCouponID;
			

		END IF;

	COMMIT;
END;