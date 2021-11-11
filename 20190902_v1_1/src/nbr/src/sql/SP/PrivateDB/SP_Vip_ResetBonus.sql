DROP PROCEDURE IF EXISTS `SP_Vip_ResetBonus`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Vip_ResetBonus` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN 
	DECLARE iClearBonusDay INT;
	DECLARE dtClearBonusDatetime DATETIME;	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
		SELECT F_ClearBonusDay, F_ClearBonusDatetime INTO iClearBonusDay, dtClearBonusDatetime FROM t_vipcard WHERE F_ID = 1;
		
		IF iClearBonusDay IS NOT NULL THEN -- 根据固定天数来重置积分
		   	UPDATE t_vip SET F_Bonus = 0 
		   	WHERE datediff(NOW(), F_CreateDatetime) <> 0 -- 由于0 % 任何数都是0,所以需要增加该判断
		   	AND (datediff(NOW(), F_CreateDatetime) % iClearBonusDay = 0);
		ELSEIF dtClearBonusDatetime IS NOT NULL THEN -- 根据固定日期来重置积分,只判断月和日
			IF MONTH(now()) = MONTH(dtClearBonusDatetime) AND DAY(now()) = DAY(dtClearBonusDatetime) THEN
				UPDATE t_vip SET F_Bonus = 0;
			END IF;
		ELSE
			SET iErrorCode := 7;
			SET sErrorMsg := '会员卡积分清零规则异常';
		END IF;
		
	COMMIT;
END;