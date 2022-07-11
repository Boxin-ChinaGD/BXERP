-- case 1：老板设置会员的积分，iManuallyAdded=1，iBonus就是设置给会员的积分值
-- case 2：收银员上传零售单到NBR，会员消费得积分，iManuallyAdded=0，iIsIncreaseBonus=1
-- case 3：收银员上传零售单到NBR/兑换券，会员用掉积分，iManuallyAdded=0，iIsIncreaseBonus=0。退货不减少原先增加的积分
DROP PROCEDURE IF EXISTS `SP_Vip_UpdateBonus`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Vip_UpdateBonus` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iVipID INT,
	IN iStaffID INT,
	IN iAmount INT, -- 消费金额，以分为单位
	IN iBonus INT, -- 老板设置的积分，或者是具体使用掉的积分
	IN sRemark VARCHAR(48),
	IN iManuallyAdded INT, -- 1，老板手动加 0，系统加
	IN iIsIncreaseBonus INT -- 1，增加积分 0，减少积分 
)
BEGIN	
	DECLARE iAmountUnit INT; -- 消费金额。以分为单位
	DECLARE iIncreaseBonus INT; -- 对应增加的积分
	DECLARE iMaxIncreaseBonus INT; -- 用户单次可获取的积分上限
	DECLARE iBonusFromAmount INT; -- 具体增减积分
	DECLARE iOldBonus INT; -- 修改前的积分
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
   	START TRANSACTION;
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
				
		IF NOT EXISTS(SELECT 1 FROM t_vip WHERE F_ID = iVipID) THEN
			SET iErrorCode := 2;
			SET sErrorMsg := '会员不存在';
		ELSEIF iManuallyAdded = 1 THEN
			IF iBonus < 0 THEN 
				SET iErrorCode := 7;
		   		SET sErrorMsg := '修改后的积分不能小于0';
		   	ELSE 
		   		SELECT F_Bonus INTO iOldBonus FROM t_vip WHERE F_ID = iVipID;
		   		
				UPDATE t_vip SET F_Bonus = iBonus WHERE F_ID = iVipID;
				-- 插入积分历史
				INSERT INTO t_bonusconsumehistory (F_VipID, F_StaffID, F_Bonus, F_AddedBonus, F_Remark, F_CreateDatetime)
				VALUES (iVipID, iStaffID, iBonus, iBonus - iOldBonus, '修改', now());
			END IF;
		ELSEIF iIsIncreaseBonus = 1 AND iAmount > 0 THEN  -- 消费增加积分,当零售金额大于0时，才需要增加积分操作
			IF EXISTS(SELECT 1 FROM t_bonusrule WHERE F_VipCardID = (SELECT F_CardID FROM t_vip WHERE F_ID = iVipID)) THEN -- 增加积分
				SELECT F_AmountUnit, F_IncreaseBonus, F_MaxIncreaseBonus INTO iAmountUnit, iIncreaseBonus, iMaxIncreaseBonus 
				FROM t_bonusrule WHERE F_VipCardID = (SELECT F_CardID FROM t_vip WHERE F_ID = iVipID) LIMIT 0, 1; -- TODO 回归测试RetailTradeCommodityActionTest.createExTest4 (42)看是否还会有错误
				-- 计算要增加的积分
			    SET iBonusFromAmount = (CASE iAmountUnit WHEN 0 THEN 0 ELSE iAmount / iAmountUnit * iIncreaseBonus END);
			    -- 判断是否超过单次获取积分上限
				IF iBonusFromAmount >= iMaxIncreaseBonus THEN
					SET iBonusFromAmount = iMaxIncreaseBonus;
				END IF;
			
				UPDATE t_vip SET F_Bonus = F_Bonus + iBonusFromAmount WHERE F_ID = iVipID;
				
				-- 插入积分历史
				INSERT INTO t_bonusconsumehistory (F_VipID, F_StaffID, F_Bonus, F_AddedBonus, F_Remark, F_CreateDatetime)
				VALUES (iVipID, NULL, (SELECT F_Bonus FROM t_vip WHERE F_ID = iVipID), iBonusFromAmount, '消费', now());
			ELSE
				SET iErrorCode := 0;
				SET sErrorMsg := '没有积分规则，所以不做积分变动';
			END IF;
		ELSEIF iIsIncreaseBonus = 0 THEN -- 会员用掉积分（兑换券）。退货不减少原先增加的积分
	  		IF iBonus >= 0 THEN
	  			SET iErrorCode := 7;
				SET sErrorMsg := '用掉的积分必须为负数';
	  		ELSEIF ((SELECT F_Bonus FROM t_vip WHERE F_ID = iVipID) + iBonus) < 0 THEN
	  			SET iErrorCode := 7;
				SET sErrorMsg := '使用积分大于会员现有的积分';
			ELSE
				UPDATE t_vip SET F_Bonus = F_Bonus + iBonus WHERE F_ID = iVipID;
				-- 插入积分历史
				INSERT INTO t_bonusconsumehistory (F_VipID, F_StaffID, F_Bonus, F_AddedBonus, F_Remark, F_CreateDatetime)
				VALUES (iVipID, NULL, (SELECT F_Bonus FROM t_vip WHERE F_ID = iVipID), iBonus, '兑换', now());
			END IF;
		END IF;
		
		SELECT 
			F_ID, 
			F_SN, 
			F_CardID,
			F_Mobile, 
			F_LocalPosSN, 
			F_Sex, 
			F_Logo, 
			F_ICID, 
			F_Name, 
			F_Email,  
			F_ConsumeTimes, 
			F_ConsumeAmount, 
			F_District, 
			F_Category, 
			F_Birthday, 
			F_Bonus, 
			F_LastConsumeDatetime, 
			F_Remark, 
			F_CreateDatetime, 
			F_UpdateDatetime
		FROM t_vip WHERE F_ID = iVipID;
  	COMMIT;
END;