-- case 1���ϰ����û�Ա�Ļ��֣�iManuallyAdded=1��iBonus�������ø���Ա�Ļ���ֵ
-- case 2������Ա�ϴ����۵���NBR����Ա���ѵû��֣�iManuallyAdded=0��iIsIncreaseBonus=1
-- case 3������Ա�ϴ����۵���NBR/�һ�ȯ����Ա�õ����֣�iManuallyAdded=0��iIsIncreaseBonus=0���˻�������ԭ�����ӵĻ���
DROP PROCEDURE IF EXISTS `SP_Vip_UpdateBonus`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Vip_UpdateBonus` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iVipID INT,
	IN iStaffID INT,
	IN iAmount INT, -- ���ѽ��Է�Ϊ��λ
	IN iBonus INT, -- �ϰ����õĻ��֣������Ǿ���ʹ�õ��Ļ���
	IN sRemark VARCHAR(48),
	IN iManuallyAdded INT, -- 1���ϰ��ֶ��� 0��ϵͳ��
	IN iIsIncreaseBonus INT -- 1�����ӻ��� 0�����ٻ��� 
)
BEGIN	
	DECLARE iAmountUnit INT; -- ���ѽ��Է�Ϊ��λ
	DECLARE iIncreaseBonus INT; -- ��Ӧ���ӵĻ���
	DECLARE iMaxIncreaseBonus INT; -- �û����οɻ�ȡ�Ļ�������
	DECLARE iBonusFromAmount INT; -- ������������
	DECLARE iOldBonus INT; -- �޸�ǰ�Ļ���
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
   	START TRANSACTION;
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
				
		IF NOT EXISTS(SELECT 1 FROM t_vip WHERE F_ID = iVipID) THEN
			SET iErrorCode := 2;
			SET sErrorMsg := '��Ա������';
		ELSEIF iManuallyAdded = 1 THEN
			IF iBonus < 0 THEN 
				SET iErrorCode := 7;
		   		SET sErrorMsg := '�޸ĺ�Ļ��ֲ���С��0';
		   	ELSE 
		   		SELECT F_Bonus INTO iOldBonus FROM t_vip WHERE F_ID = iVipID;
		   		
				UPDATE t_vip SET F_Bonus = iBonus WHERE F_ID = iVipID;
				-- ���������ʷ
				INSERT INTO t_bonusconsumehistory (F_VipID, F_StaffID, F_Bonus, F_AddedBonus, F_Remark, F_CreateDatetime)
				VALUES (iVipID, iStaffID, iBonus, iBonus - iOldBonus, '�޸�', now());
			END IF;
		ELSEIF iIsIncreaseBonus = 1 AND iAmount > 0 THEN  -- �������ӻ���,�����۽�����0ʱ������Ҫ���ӻ��ֲ���
			IF EXISTS(SELECT 1 FROM t_bonusrule WHERE F_VipCardID = (SELECT F_CardID FROM t_vip WHERE F_ID = iVipID)) THEN -- ���ӻ���
				SELECT F_AmountUnit, F_IncreaseBonus, F_MaxIncreaseBonus INTO iAmountUnit, iIncreaseBonus, iMaxIncreaseBonus 
				FROM t_bonusrule WHERE F_VipCardID = (SELECT F_CardID FROM t_vip WHERE F_ID = iVipID) LIMIT 0, 1; -- TODO �ع����RetailTradeCommodityActionTest.createExTest4 (42)���Ƿ񻹻��д���
				-- ����Ҫ���ӵĻ���
			    SET iBonusFromAmount = (CASE iAmountUnit WHEN 0 THEN 0 ELSE iAmount / iAmountUnit * iIncreaseBonus END);
			    -- �ж��Ƿ񳬹����λ�ȡ��������
				IF iBonusFromAmount >= iMaxIncreaseBonus THEN
					SET iBonusFromAmount = iMaxIncreaseBonus;
				END IF;
			
				UPDATE t_vip SET F_Bonus = F_Bonus + iBonusFromAmount WHERE F_ID = iVipID;
				
				-- ���������ʷ
				INSERT INTO t_bonusconsumehistory (F_VipID, F_StaffID, F_Bonus, F_AddedBonus, F_Remark, F_CreateDatetime)
				VALUES (iVipID, NULL, (SELECT F_Bonus FROM t_vip WHERE F_ID = iVipID), iBonusFromAmount, '����', now());
			ELSE
				SET iErrorCode := 0;
				SET sErrorMsg := 'û�л��ֹ������Բ������ֱ䶯';
			END IF;
		ELSEIF iIsIncreaseBonus = 0 THEN -- ��Ա�õ����֣��һ�ȯ�����˻�������ԭ�����ӵĻ���
	  		IF iBonus >= 0 THEN
	  			SET iErrorCode := 7;
				SET sErrorMsg := '�õ��Ļ��ֱ���Ϊ����';
	  		ELSEIF ((SELECT F_Bonus FROM t_vip WHERE F_ID = iVipID) + iBonus) < 0 THEN
	  			SET iErrorCode := 7;
				SET sErrorMsg := 'ʹ�û��ִ��ڻ�Ա���еĻ���';
			ELSE
				UPDATE t_vip SET F_Bonus = F_Bonus + iBonus WHERE F_ID = iVipID;
				-- ���������ʷ
				INSERT INTO t_bonusconsumehistory (F_VipID, F_StaffID, F_Bonus, F_AddedBonus, F_Remark, F_CreateDatetime)
				VALUES (iVipID, NULL, (SELECT F_Bonus FROM t_vip WHERE F_ID = iVipID), iBonus, '�һ�', now());
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