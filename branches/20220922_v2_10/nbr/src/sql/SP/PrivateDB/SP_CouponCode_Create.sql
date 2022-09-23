DROP PROCEDURE IF EXISTS `SP_CouponCode_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_CouponCode_Create`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
    IN iVipID INT,
	IN iCouponID INT,
	IN iStatus INT -- ʵ��������ֶ�û���ã�����������MapperTest
)
BEGIN
	DECLARE sSN VARCHAR(15);
	DECLARE iVipBonus INT; -- ��Ա����
	DECLARE iCouponBonus INT; -- �һ��Ż�ȯ��Ҫ����
	DECLARE iRemainingQuantity INT; -- �Ż�ȯ���
	DECLARE iPersonalLimit INT; -- ÿ����ȯ����
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	SELECT F_Bonus INTO iVipBonus FROM t_vip WHERE F_ID = iVipID;
	SELECT F_Bonus, F_RemainingQuantity, F_PersonalLimit INTO iCouponBonus, iRemainingQuantity, iPersonalLimit FROM t_coupon WHERE F_ID = iCouponID;
	
	START TRANSACTION;
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
        
        IF iVipBonus IS NULL THEN
        	SET iErrorCode := 2;
			SET sErrorMsg := '�û�Ա������';
		ELSEIF NOT EXISTS(SELECT 1 FROM t_coupon WHERE F_ID = iCouponID AND F_Status = 0) THEN
        	SET iErrorCode := 2;
			SET sErrorMsg := '���Ż�ȯ������';
		ELSEIF iRemainingQuantity < 1 THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '�Ż�ȯ��治��,�޷���ȡ';
		ELSEIF (SELECT COUNT(1) FROM t_couponcode WHERE F_VipID = iVipID AND F_CouponID = iCouponID) >= iPersonalLimit THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '��Ա�ѳ�����ȡ���Ż�ȯ�ĸ�����Ŀ���ޣ��޷���ȡ';
		ELSEIF iVipBonus < iCouponBonus THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '��Ա���ֲ���,�޷���ȡ���Ż�ȯ';
		ELSE
			read_loop: LOOP -- ѭ����ȡ��Ψһ��SN��Ϊֹ
		    	SET sSN = Func_GenerateCouponSN(15);
		    	
	            IF NOT EXISTS(SELECT 1 FROM t_couponcode WHERE F_SN = sSN) THEN
	                LEAVE read_loop;
	            end IF;
        	END LOOP read_loop;
        	
			IF iCouponBonus > 0 THEN
				-- ���»�Ա����
				UPDATE t_vip SET F_Bonus = F_Bonus - iCouponBonus WHERE F_ID = iVipID;
				-- �����Ա������ʷ
				INSERT INTO t_bonusconsumehistory (F_VipID, F_StaffID, F_Bonus, F_AddedBonus, F_Remark, F_CreateDatetime)
				VALUES (iVipID, NULL, (SELECT F_Bonus FROM t_vip WHERE F_ID = iVipID), (iCouponBonus * -1), '�һ�', now());
			END IF;
        	
	        INSERT INTO t_couponcode (F_VipID, F_CouponID, F_Status, F_SN, F_CreateDatetime, F_UsedDatetime)
			VALUES (iVipID, iCouponID, iStatus, sSN, now(), null);
			
			SELECT F_ID, F_VipID, F_CouponID, F_Status, F_SN, F_CreateDatetime, F_UsedDatetime
			FROM t_couponcode WHERE F_ID = LAST_INSERT_ID();
			
			-- �����Ż�ȯ��ʣ������
			UPDATE t_coupon SET F_RemainingQuantity = F_RemainingQuantity - 1 WHERE F_ID = iCouponID;
			

		END IF;

	COMMIT;
END;