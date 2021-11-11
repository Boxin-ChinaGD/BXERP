DROP PROCEDURE IF EXISTS `SP_VIP_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_VIP_Create` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN sSN VARCHAR(9), -- 正常情况下不传该值,SP里会自动生成,该参数以后测试需要用到的时候可以传
	IN iCardID INT,
	IN sMobile VARCHAR(11),
	IN iLocalPosSN  VARCHAR(32),
	IN iSex INT, -- 
	IN sLogo VARCHAR(128), -- 
	IN sICID  VARCHAR(30),
	IN sName VARCHAR(30),
	IN sEmail VARCHAR(30),
	IN iConsumeTimes INT,
	IN fConsumeAmount Decimal(20,6),
	IN sDistrict VARCHAR(30),
	IN iCategory INT,
	IN dtBirthday DATE,
	IN dtLastConsumeDatetime DATETIME,
	IN dtCreateDatetime DATETIME,
	IN iBonusImported INT -- -1时，会员积分不是导入的。>=0，是导入的。其它值在Java层阻止了。
)
BEGIN	
	DECLARE maxID INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		IF iBonusImported = -1 THEN -- 小程序,nbr创建会员时该值为-1.
			SELECT F_InitIncreaseBonus INTO iBonusImported FROM t_bonusrule LIMIT 0, 1;
		END IF;
	
		SELECT (ifnull(max(F_ID), 0) + 1) INTO maxID FROM t_vip LIMIT 1; -- 当前创建ID = 最大ID + 1。 LIMIT 1必须加，否则t_vip多于一条数据时会有异常
		
		IF EXISTS(SELECT 1 FROM t_vip WHERE F_ICID = sICID AND sICID <> '') THEN -- 判断唯一值是否重复
			SET iErrorCode := 7;
			SET sErrorMsg := '已存在相同身份证号的会员，创建失败';
		ELSEIF EXISTS(SELECT 1 FROM t_vip WHERE F_Mobile = sMobile) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '已存在相同电话号码的会员，创建失败';
		ELSEIF EXISTS(SELECT 1 FROM t_vip WHERE F_Email = sEmail AND sEmail <> '') THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '已存在相同邮箱的会员，创建失败';
		ELSEIF NOT EXISTS(SELECT 1 FROM t_vip_category WHERE F_ID = iCategory) THEN 
			SET iErrorCode := 7;
			SET sErrorMsg := '不能使用不存在的VipCategory创建';
		ELSE 
			
			INSERT INTO t_vip (
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
				F_CreateDatetime)
			VALUES (
		   		(CASE sSN WHEN '' THEN CONCAT('VIP',LPAD(maxID, 6, 0)) ELSE sSN END),  -- 改字段格式为VIP + 000001 - 999999 的流水号,例如VIP000001
				iCardID,
				sMobile, 
				iLocalPosSN, 
				iSex,
				sLogo,
				IF(sICID='',NULL,sICID), 
				sName, 
				IF(sEmail='',NULL,sEmail), 
				iConsumeTimes, 
				fConsumeAmount, 
				sDistrict, 
				iCategory, 
				dtBirthday, 
				iBonusImported,
				dtLastConsumeDatetime,
				dtCreateDatetime);
			  
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
				F_CreateDatetime,
				F_UpdateDatetime
			FROM t_vip WHERE F_ID = LAST_INSERT_ID();
			
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
		
	COMMIT;
END;