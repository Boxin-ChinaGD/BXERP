DROP PROCEDURE IF EXISTS `SP_VIP_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_VIP_Create` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN sSN VARCHAR(9), -- ��������²�����ֵ,SP����Զ�����,�ò����Ժ������Ҫ�õ���ʱ����Դ�
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
	IN iBonusImported INT -- -1ʱ����Ա���ֲ��ǵ���ġ�>=0���ǵ���ġ�����ֵ��Java����ֹ�ˡ�
)
BEGIN	
	DECLARE maxID INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		IF iBonusImported = -1 THEN -- С����,nbr������Աʱ��ֵΪ-1.
			SELECT F_InitIncreaseBonus INTO iBonusImported FROM t_bonusrule LIMIT 0, 1;
		END IF;
	
		SELECT (ifnull(max(F_ID), 0) + 1) INTO maxID FROM t_vip LIMIT 1; -- ��ǰ����ID = ���ID + 1�� LIMIT 1����ӣ�����t_vip����һ������ʱ�����쳣
		
		IF EXISTS(SELECT 1 FROM t_vip WHERE F_ICID = sICID AND sICID <> '') THEN -- �ж�Ψһֵ�Ƿ��ظ�
			SET iErrorCode := 7;
			SET sErrorMsg := '�Ѵ�����ͬ���֤�ŵĻ�Ա������ʧ��';
		ELSEIF EXISTS(SELECT 1 FROM t_vip WHERE F_Mobile = sMobile) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '�Ѵ�����ͬ�绰����Ļ�Ա������ʧ��';
		ELSEIF EXISTS(SELECT 1 FROM t_vip WHERE F_Email = sEmail AND sEmail <> '') THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '�Ѵ�����ͬ����Ļ�Ա������ʧ��';
		ELSEIF NOT EXISTS(SELECT 1 FROM t_vip_category WHERE F_ID = iCategory) THEN 
			SET iErrorCode := 7;
			SET sErrorMsg := '����ʹ�ò����ڵ�VipCategory����';
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
		   		(CASE sSN WHEN '' THEN CONCAT('VIP',LPAD(maxID, 6, 0)) ELSE sSN END),  -- ���ֶθ�ʽΪVIP + 000001 - 999999 ����ˮ��,����VIP000001
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